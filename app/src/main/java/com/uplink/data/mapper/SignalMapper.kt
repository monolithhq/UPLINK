package com.uplink.data.mapper

import com.uplink.data.api.dto.CatalogItemDto
import com.uplink.data.api.dto.MetaItemDto
import com.uplink.data.database.entities.SignalEntity
import com.uplink.domain.model.Signal
import com.uplink.domain.model.SignalStatus

// Single place where YouTubio DTOs, Room entities, and the domain
// Signal model convert into one another. Keeping this in one file
// means a future schema/DTO change touches one place, not every call
// site that happened to build a Signal.

fun CatalogItemDto.toSignal(sourcePlatform: String): Signal =
    Signal(
        id = id,
        title = name,
        channel = sourcePlatform.uppercase(),
        description = description,
        thumbnailUrl = poster,
        durationSeconds = null,
        sourcePlatform = sourcePlatform,
        sourceId = id,
        streamUrl = null,
        status = SignalStatus.DISCOVERED,
        createdAt = System.currentTimeMillis(),
        viewCount = null
    )

// Builds a stub Signal directly from a /meta response, for the case
// where a signal is looked up (getSignal/resolveStream) on something
// never seen via getCatalog() first — e.g. a deep link, or an id
// discovered via search before catalog caching ran. Distinct from
// toSignal() on CatalogItemDto since MetaItemDto has no viewCount
// guarantee the catalog entry would have had.
fun MetaItemDto.toSignalStub(sourcePlatform: String): Signal =
    Signal(
        id = id,
        title = name,
        channel = sourcePlatform.uppercase(),
        description = description,
        thumbnailUrl = poster,
        durationSeconds = runtime?.toRuntimeSeconds(),
        sourcePlatform = sourcePlatform,
        sourceId = id,
        streamUrl = null,
        status = SignalStatus.DISCOVERED,
        createdAt = System.currentTimeMillis(),
        viewCount = null
    )

// Merges enrichment data onto an existing Signal without discarding
// fields the catalog entry already provided (meta responses are often
// sparser than what the catalog already gave us).
fun Signal.enrichedWith(meta: MetaItemDto): Signal =
    copy(
        title = meta.name,
        description = meta.description ?: description,
        thumbnailUrl = meta.poster ?: thumbnailUrl,
        durationSeconds = meta.runtime?.toRuntimeSeconds() ?: durationSeconds,
        status = SignalStatus.RESOLVING
    )

fun Signal.withStream(streamUrl: String): Signal =
    copy(streamUrl = streamUrl, status = SignalStatus.READY)

fun Signal.toEntity(): SignalEntity =
    SignalEntity(
        id = id,
        sourcePlatform = sourcePlatform,
        sourceId = sourceId,
        title = title,
        description = description,
        thumbnailUrl = thumbnailUrl,
        channel = channel,
        durationSeconds = durationSeconds,
        streamUrl = streamUrl,
        status = status.name,
        discoveredAt = createdAt,
        viewCount = viewCount
    )

fun SignalEntity.toSignal(): Signal =
    Signal(
        id = id,
        title = title,
        channel = channel,
        description = description,
        thumbnailUrl = thumbnailUrl,
        durationSeconds = durationSeconds,
        sourcePlatform = sourcePlatform,
        sourceId = sourceId,
        streamUrl = streamUrl,
        status = runCatching { SignalStatus.valueOf(status) }.getOrDefault(SignalStatus.ERROR),
        createdAt = discoveredAt,
        viewCount = viewCount
    )

// Best-effort "HH:MM:SS" / "MM:SS" runtime string -> seconds. Returns
// null on anything unparseable rather than throwing — a malformed
// runtime string from YouTubio shouldn't crash a screen. Not private:
// used from both enrichedWith() and toSignalStub() in this same file.
fun String.toRuntimeSeconds(): Long? {
    val parts = split(":").mapNotNull { it.trim().toLongOrNull() }
    if (parts.isEmpty() || parts.any { it < 0 }) return null
    return when (parts.size) {
        1 -> parts[0]
        2 -> parts[0] * 60 + parts[1]
        3 -> parts[0] * 3600 + parts[1] * 60 + parts[2]
        else -> null
    }
}
