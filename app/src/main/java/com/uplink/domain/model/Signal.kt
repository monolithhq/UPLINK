package com.uplink.domain.model

// UPLINK's core abstraction: a discoverable/playable unit of media
// resolved by YouTubio. Not a YouTube-specific object — YouTubio
// translates external sources (YouTube video, live stream, playlist
// item, future sources) into Signals so the app never depends on
// YouTube-specific shapes.
data class Signal(
    val id: String,

    // Display
    val title: String,
    val channel: String,
    val description: String?,

    // Media
    val thumbnailUrl: String?,
    val durationSeconds: Long?,

    // Source
    val sourcePlatform: String,
    val sourceId: String,

    // Playback
    val streamUrl: String?,
    val status: SignalStatus,

    // Metadata
    val createdAt: Long,
    val viewCount: Long?
)

// Lifecycle states for a Signal, per UPLINK_BUILD_CONFIG session
// decisions. DISCOVERED -> RESOLVING -> READY -> PLAYING is the happy
// path; ERROR and UNAVAILABLE are the two distinct failure branches
// (resolution never succeeded, vs. it succeeded but later died).
enum class SignalStatus {
    DISCOVERED,
    RESOLVING,
    READY,
    PLAYING,
    UNAVAILABLE,
    ERROR
}

// UI-facing label mapping. Screens must never show raw enum names —
// Debug Console is the only place raw technical event names appear.
fun SignalStatus.displayLabel(): String =
    when (this) {
        SignalStatus.DISCOVERED -> "DISCOVERED"
        SignalStatus.RESOLVING -> "RESOLVING"
        SignalStatus.READY -> "SIGNAL LOCKED"
        SignalStatus.PLAYING -> "ON AIR"
        SignalStatus.UNAVAILABLE -> "SIGNAL LOST"
        SignalStatus.ERROR -> "FAULT"
    }
