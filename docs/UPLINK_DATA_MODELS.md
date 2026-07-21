UPLINK Data Models

Purpose

Defines the data structures used by UPLINK Phase 1.

The data layer is designed around a signal broadcast model, where YouTube content is represented as network signals instead of traditional videos.

The models must support:

- Fast browsing
- Local caching
- YouTubio integration
- Playback
- Search
- Debug logging

---

Data Architecture

UPLINK DATA LAYER

Remote Sources

    |
    |
    v

YouTubio Adapter

    |
    |
    v

UPLINK Repository

    |
    |
    +----------------+
    |                |
    v                v

Local Cache       UI Models

    |
    |
    v

Compose Screens

---

Model Rules

All models should:

- Be Kotlin data classes
- Be immutable
- Use nullable fields only when data may genuinely not exist
- Avoid exposing raw YouTubio responses to UI
- Convert external data into UPLINK models

---

1. Signal Model

Purpose

Primary content object.

Represents a YouTube video inside UPLINK.

---

Kotlin

data class Signal(
    val id: String,
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val sourceNode: String?,
    val duration: Long?,
    val uploadedAt: Long?,
    val watchingCount: Long?,
    val playbackUrl: String?,
    val status: SignalStatus
)

---

Fields

Field| Description
id| Unique signal identifier
title| Signal title
description| Signal description
thumbnailUrl| Preview image
sourceNode| Channel/source
duration| Length in seconds
uploadedAt| Upload timestamp
watchingCount| Activity count
playbackUrl| Stream source
status| Current availability

---

Signal Status

enum class SignalStatus {

    READY,

    STREAMING,

    BUFFERING,

    UNAVAILABLE,

    LOST
}

---

2. Channel / Source Node Model

Purpose

Represents the origin of signals.

---

Kotlin

data class SourceNode(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val subscriberCount: Long?,
    val verified: Boolean
)

---

Display Example

SOURCE_NODE

CHANNEL_NAME

245K FOLLOWERS

---

3. Search Model

Purpose

Represents network searches.

---

Kotlin

data class SignalQuery(
    val query: String,
    val timestamp: Long,
    val results: List<Signal>
)

---

Search State

sealed class SearchState {

    object Idle : SearchState()

    object Scanning : SearchState()

    data class Results(
        val signals: List<Signal>
    ) : SearchState()

    object Empty : SearchState()

    data class Error(
        val message: String
    ) : SearchState()
}

---

4. Playback Models

---

Playback Session

Purpose

Tracks an active viewing session.

---

Kotlin

data class PlaybackSession(
    val signalId: String,
    val position: Long,
    val duration: Long,
    val quality: String?,
    val state: PlaybackState
)

---

Playback State

enum class PlaybackState {

    CONNECTING,

    PLAYING,

    PAUSED,

    BUFFERING,

    COMPLETED,

    FAILED
}

---

Player Quality

data class StreamQuality(
    val label: String,
    val width: Int,
    val height: Int,
    val bitrate: Long?
)

Example:

1080P

1920x1080

HIGH

---

5. YouTubio Integration Models

Purpose

Models communication with the local YouTubio service.

---

YouTubio Configuration

data class YouTubioConfig(
    val endpoint: String,
    val connected: Boolean,
    val lastSync: Long?
)

Example:

http://localhost:7000

CONNECTED

---

YouTubio Response Wrapper

data class YouTubioResponse<T>(
    val success: Boolean,
    val data: T?,
    val error: String?
)

---

YouTubio Status

enum class ServiceStatus {

    CONNECTING,

    ONLINE,

    OFFLINE,

    ERROR
}

---

6. Cache Models

Purpose

Local storage.

Phase 1 uses caching for speed.

---

Cached Signal

data class CachedSignal(
    val signal: Signal,
    val cachedAt: Long,
    val expiresAt: Long
)

---

Cache Status

enum class CacheStatus {

    AVAILABLE,

    EXPIRED,

    EMPTY
}

---

7. Application State Model

Purpose

Global app state.

---

data class UplinkState(
    val connection: ServiceStatus,
    val currentSignal: Signal?,
    val playback: PlaybackSession?,
    val cacheStatus: CacheStatus
)

---

8. Debug Models

---

Debug Event

Purpose

Internal logging.

---

data class DebugEvent(
    val timestamp: Long,
    val category: DebugCategory,
    val message: String
)

---

Debug Categories

enum class DebugCategory {

    APP,

    NETWORK,

    CACHE,

    PLAYER,

    YOUBTIO,

    ERROR
}

---

Example Logs

19:04:12

YOUBTIO

CONNECTION_ESTABLISHED


19:04:15

PLAYER

STREAM_STARTED

---

9. Navigation Models

---

Uplink Destination

sealed class Destination {

    object Home : Destination()

    object Search : Destination()

    object Signal : Destination()

}

---

10. User Preferences

Phase 1 does not use accounts.

Only local preferences exist.

---

data class UplinkPreferences(

    val autoplay: Boolean,

    val defaultQuality: String,

    val debugEnabled: Boolean

)

---

Storage Strategy

Room Database

Recommended local storage:

Room

Tables:

signals

cache

history

debug_logs

preferences

---

Data Flow Example

Opening Home

APP START

↓

Load Cache

↓

Request YouTubio Feed

↓

Map Response

↓

Signal Models

↓

Compose UI

---

Playing a Signal

USER SELECTS SIGNAL

↓

Signal ID

↓

YouTubio Request

↓

Playback URL

↓

Player Session

↓

STREAMING

---

Performance Rules

Avoid

- Large API objects in memory
- Repeated network calls
- Blocking UI threads
- Parsing inside Composables

---

Use

- Kotlin Coroutines
- Flow
- Repository pattern
- Background loading
- Local caching

---

Phase 1 Required Models

Minimum implementation:

Signal

SourceNode

PlaybackSession

YouTubioConfig

DebugEvent

UplinkState

---

Data Layer Goal

UPLINK should behave like:

CACHE FIRST

NETWORK SECOND

PLAYER READY

FAST RESPONSE

The data layer exists to keep the terminal responsive even when the broadcast network is slow.
