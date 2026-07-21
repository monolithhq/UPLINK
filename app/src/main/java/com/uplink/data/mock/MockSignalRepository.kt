package com.uplink.data.mock

import com.uplink.domain.model.Signal
import com.uplink.domain.model.SignalStatus
import com.uplink.domain.result.SignalResult
import kotlinx.coroutines.delay

// Commit 003 stand-in for SignalRepository. Commit 004 replaces this
// with YouTubioSignalRepository backed by real GET /catalog, /search,
// /stream calls — screens are written against this same interface
// shape so that swap requires no UI rewrites.
class MockSignalRepository {

    private val signals: List<Signal> = listOf(
        Signal(
            id = "sig_0001",
            title = "Deep Space Archive: Voyager Transmissions",
            channel = "NODE: SIGNAL_ARCHIVE",
            description = "Archival footage compiled from deep-space probe transmission logs.",
            thumbnailUrl = null,
            durationSeconds = 2533,
            sourcePlatform = "youtube",
            sourceId = "yt_abc123",
            streamUrl = "https://example.invalid/stream/sig_0001",
            status = SignalStatus.READY,
            createdAt = 1_753_000_000_000,
            viewCount = 481_203
        ),
        Signal(
            id = "sig_0002",
            title = "Live: Orbital Relay Feed",
            channel = "NODE: ORBITAL_RELAY",
            description = "Continuous live broadcast from the orbital relay node.",
            thumbnailUrl = null,
            durationSeconds = null,
            sourcePlatform = "youtube",
            sourceId = "yt_live456",
            streamUrl = "https://example.invalid/stream/sig_0002",
            status = SignalStatus.PLAYING,
            createdAt = 1_753_100_000_000,
            viewCount = 12_044
        ),
        Signal(
            id = "sig_0003",
            title = "Terminal Diagnostics Broadcast #14",
            channel = "NODE: DIAGNOSTIC_CHANNEL",
            description = "Routine diagnostics broadcast. Contains telemetry commentary.",
            thumbnailUrl = null,
            durationSeconds = 904,
            sourcePlatform = "youtube",
            sourceId = "yt_diag014",
            streamUrl = null,
            status = SignalStatus.RESOLVING,
            createdAt = 1_753_050_000_000,
            viewCount = 3_017
        ),
        Signal(
            id = "sig_0004",
            title = "Abandoned Frequency: Recovered Fragment",
            channel = "NODE: SIGNAL_ARCHIVE",
            description = "Partial recovery. Original source no longer transmitting.",
            thumbnailUrl = null,
            durationSeconds = 611,
            sourcePlatform = "youtube",
            sourceId = "yt_frag099",
            streamUrl = null,
            status = SignalStatus.UNAVAILABLE,
            createdAt = 1_752_900_000_000,
            viewCount = 88_540
        ),
        Signal(
            id = "sig_0005",
            title = "Corrupted Uplink Fragment",
            channel = "NODE: UNKNOWN",
            description = "Resolution failed. Source metadata incomplete.",
            thumbnailUrl = null,
            durationSeconds = null,
            sourcePlatform = "youtube",
            sourceId = "yt_err777",
            streamUrl = null,
            status = SignalStatus.ERROR,
            createdAt = 1_752_800_000_000,
            viewCount = null
        )
    )

    suspend fun getCatalog(): SignalResult<List<Signal>> {
        delay(250)
        return SignalResult.Success(signals)
    }

    suspend fun search(query: String): SignalResult<List<Signal>> {
        delay(250)
        val results = signals.filter {
            it.title.contains(query, ignoreCase = true) ||
                it.channel.contains(query, ignoreCase = true)
        }
        return SignalResult.Success(results)
    }

    suspend fun getLibrary(): SignalResult<List<Signal>> {
        delay(150)
        // Mock "saved" subset — Commit 004+ backs this with Room.
        return SignalResult.Success(signals.filter { it.status != SignalStatus.ERROR })
    }

    suspend fun getSignal(id: String): SignalResult<Signal> {
        delay(150)
        val found = signals.find { it.id == id }
            ?: return SignalResult.NotFound(id)
        return SignalResult.Success(found)
    }
}
