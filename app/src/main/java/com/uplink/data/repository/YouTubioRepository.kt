package com.uplink.data.repository

import com.uplink.data.api.YouTubioClient
import com.uplink.data.api.YouTubioDefaults
import com.uplink.data.database.CacheDao
import com.uplink.data.database.PreferencesDao
import com.uplink.data.database.SignalDao
import com.uplink.data.mapper.enrichedWith
import com.uplink.data.mapper.toEntity
import com.uplink.data.mapper.toSignal
import com.uplink.data.mapper.toSignalStub
import com.uplink.data.mapper.withStream
import com.uplink.data.preferences.PreferenceKeys
import com.uplink.domain.model.Signal
import com.uplink.domain.result.SignalResult
import java.io.IOException
import java.net.SocketTimeoutException
import retrofit2.HttpException

// Real repository backing UPLINK's screens, replacing MockSignalRepository.
// Talks to YouTubio via YouTubioClient, persists/reads through Room via
// SignalDao/CacheDao, and reads the operator-supplied base URL / config
// token via PreferencesDao. NetworkModule owns the actual Retrofit base
// URL; this class only ever supplies the {config} path segment per-call.
//
// {type} is NOT a user setting — YouTubio's addon.js default catalogType
// is "YouTube" (see YouTubioDefaults), not Stremio's typical
// movie/series/channel vocabulary. TODO: once manifest parsing reads the
// real `types` array from a live instance, prefer that over this constant.
//
// IMPORTANT: CatalogItemDto.id is not guaranteed to be a directly-playable
// YouTube video ID — it may represent a video, channel, or playlist entry
// depending on which YouTubio catalog it came from. This repository never
// constructs a YouTube URL directly from sourceId; playback resolution
// always goes through YouTubio's own /stream endpoint.
class YouTubioRepository(
    private val client: YouTubioClient,
    private val signalDao: SignalDao,
    private val cacheDao: CacheDao,
    private val preferencesDao: PreferencesDao
) {

    private suspend fun requireConfig(): String? {
        val url = preferencesDao.getValue(PreferenceKeys.YOUTUBIO_URL)
        val config = preferencesDao.getValue(PreferenceKeys.YOUTUBIO_CONFIG)
        if (url.isNullOrBlank() || config.isNullOrBlank()) return null
        return config
    }

    // Screens check this BEFORE attempting a repository call, to show
    // "UPLINK NOT CONFIGURED" rather than letting an unconfigured call
    // fall through to a generic NetworkError.
    suspend fun isConfigured(): Boolean = requireConfig() != null

    suspend fun getCatalog(catalogId: String = ":ytrec"): SignalResult<List<Signal>> {
        val config = requireConfig() ?: return SignalResult.NotFound(catalogId)
        return runCatching {
            client.catalog(config = config, type = YouTubioDefaults.TYPE, id = catalogId)
        }.fold(
            onSuccess = { dto ->
                val signals = dto.metas.map { it.toSignal(sourcePlatform = YouTubioDefaults.TYPE) }
                signalDao.upsertAll(signals.map { it.toEntity() })
                SignalResult.Success(signals)
            },
            onFailure = { toSignalError(it, catalogId) }
        )
    }

    suspend fun search(query: String): SignalResult<List<Signal>> {
        // YouTubio/Stremio catalogs are id-based, not free-text search
        // endpoints in the general case; Commit 004 searches the local
        // Room cache (populated by prior getCatalog calls) rather than
        // hitting a network search endpoint that may not exist for this
        // addon's default catalogs. Revisit if YouTubio exposes a real
        // search catalog id.
        return runCatching {
            signalDao.search(query).map { it.toSignal() }
        }.fold(
            onSuccess = { SignalResult.Success(it) },
            onFailure = { toSignalError(it, query) }
        )
    }

    suspend fun getLibrary(): SignalResult<List<Signal>> {
        return runCatching {
            signalDao.getAll().map { it.toSignal() }
        }.fold(
            onSuccess = { SignalResult.Success(it) },
            onFailure = { toSignalError(it, "library") }
        )
    }

    suspend fun getSignal(id: String): SignalResult<Signal> {
        val config = requireConfig() ?: return SignalResult.NotFound(id)
        val cached = signalDao.getById(id)?.toSignal()

        return runCatching {
            val metaDto = client.meta(config = config, type = YouTubioDefaults.TYPE, id = id)
            val base = cached ?: metaDto.meta.toSignalStub(sourcePlatform = YouTubioDefaults.TYPE)
            val enriched = base.enrichedWith(metaDto.meta)
            signalDao.upsert(enriched.toEntity())
            enriched
        }.fold(
            onSuccess = { SignalResult.Success(it) },
            onFailure = { error ->
                // Fall back to whatever's cached rather than failing outright
                // if we at least have a local copy of this signal.
                if (cached != null) SignalResult.Success(cached) else toSignalError(error, id)
            }
        )
    }

    // Deep-lookup fallback (Commit 004 decision): a deep link or a
    // search-discovered id may reach here before any getCatalog() call
    // ever cached it locally. Rather than fail with NotFound, fetch
    // /meta first to construct a real Signal, then resolve its stream.
    //
    //   resolveStream(id)
    //       |
    //       +-- Room lookup
    //       |     found?     -> resolve stream directly
    //       |     missing?   -> fetch meta, build Signal, THEN resolve stream
    //
    suspend fun resolveStream(id: String): SignalResult<Signal> {
        val config = requireConfig() ?: return SignalResult.NotFound(id)
        val cached = signalDao.getById(id)?.toSignal()

        return runCatching {
            val existing = cached ?: run {
                val metaDto = client.meta(config = config, type = YouTubioDefaults.TYPE, id = id)
                val stub = metaDto.meta.toSignalStub(sourcePlatform = YouTubioDefaults.TYPE)
                signalDao.upsert(stub.toEntity())
                stub
            }

            val streamDto = client.stream(config = config, type = YouTubioDefaults.TYPE, id = id)
            val streamUrl = streamDto.streams.firstOrNull()?.url
                ?: return SignalResult.BackendError(code = 0, message = "no stream returned")

            val updated = existing.withStream(streamUrl)
            signalDao.upsert(updated.toEntity())
            updated
        }.fold(
            onSuccess = { SignalResult.Success(it) },
            onFailure = { toSignalError(it, id) }
        )
    }

    suspend fun saveYouTubioUrl(url: String) {
        preferencesDao.setValue(
            com.uplink.data.database.entities.PreferenceEntity(
                key = PreferenceKeys.YOUTUBIO_URL,
                value = url
            )
        )
    }

    suspend fun saveYouTubioConfig(config: String) {
        preferencesDao.setValue(
            com.uplink.data.database.entities.PreferenceEntity(
                key = PreferenceKeys.YOUTUBIO_CONFIG,
                value = config
            )
        )
    }

    suspend fun testConnection(): SignalResult<Unit> {
        val config = requireConfig() ?: return SignalResult.NotFound("config")
        return runCatching {
            client.manifest(config = config)
        }.fold(
            onSuccess = { SignalResult.Success(Unit) },
            onFailure = { toSignalError(it, "manifest") }
        )
    }

    private fun <T> toSignalError(error: Throwable, id: String): SignalResult<T> =
        when (error) {
            is SocketTimeoutException -> SignalResult.NetworkError(error)
            is IOException -> SignalResult.NetworkError(error)
            is HttpException -> when (error.code()) {
                404 -> SignalResult.NotFound(id)
                else -> SignalResult.BackendError(code = error.code(), message = error.message())
            }
            else -> SignalResult.BackendError(code = 0, message = error.message)
        }
}
