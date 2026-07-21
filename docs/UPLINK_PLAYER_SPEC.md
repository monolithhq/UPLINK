UPLINK Player Specification

Purpose

Defines the video playback system for UPLINK Phase 1.

The player is responsible for:

- Receiving streams from YouTubio
- Providing low-latency playback
- Managing playback state
- Reporting failures
- Integrating with UPLINK terminal UI

The player should feel like a signal decoder, not a normal media player.

---

Player Architecture

YouTubio

    |
    |
    v

Stream Resolver

    |
    |
    v

UPLINK Player Engine

    |
    |
    +----------------+
    |                |
    v                v

Video Surface    Player State

    |
    |
    v

Compose UI

---

Phase 1 Player Choice

Recommended Engine

Reason:

- Native Android support
- High performance
- Adaptive streaming support
- Active maintenance
- Kotlin-friendly
- Works well with Jetpack Compose

---

Player Stack

UPLINK

Jetpack Compose UI

        |

Media3 Player Controller

        |

ExoPlayer

        |

Android Media Codec

        |

Device Hardware Decoder

---

Core Player Module

Package:

com.uplink.player

Structure:

player/

├── UplinkPlayer.kt

├── PlayerController.kt

├── PlayerState.kt

├── PlayerEvents.kt

├── PlayerSurface.kt

└── PlayerLogger.kt

---

Player Controller

Purpose

Single control layer between UI and playback engine.

---

Kotlin

class PlayerController {

    fun load(signal: Signal)

    fun play()

    fun pause()

    fun seek(position: Long)

    fun stop()

    fun release()

}

---

Player State

Purpose

Single source of truth for playback.

---

data class PlayerState(

    val signalId: String?,

    val playbackState: PlaybackState,

    val position: Long,

    val duration: Long,

    val buffered: Long,

    val quality: String?

)

---

Playback States

enum class PlaybackState {

    IDLE,

    CONNECTING,

    READY,

    PLAYING,

    PAUSED,

    BUFFERING,

    COMPLETED,

    ERROR

}

---

Player Events

Purpose

Internal event stream.

---

sealed class PlayerEvent {

    object Started : PlayerEvent()

    object Paused : PlayerEvent()

    object Buffering : PlayerEvent()

    object Completed : PlayerEvent()

    data class Error(
        val message: String
    ) : PlayerEvent()

}

---

Player Lifecycle

Screen Open

PLAYER_CREATED

↓

ENGINE_INITIALIZED

↓

WAITING_FOR_SIGNAL

---

Signal Selected

LOAD_SIGNAL

↓

RESOLVE_STREAM

↓

PREPARE_PLAYER

↓

CONNECTING_STREAM

---

Playback Start

STREAM_READY

↓

SIGNAL_ACTIVE

↓

PLAYING

---

Exit

STOP_STREAM

↓

RELEASE_PLAYER

↓

CLEAR_RESOURCES

---

Player UI

Player Surface

Main video component.

---

Layout:

┌──────────────────────┐
│                      │
│                      │
│       VIDEO          │
│                      │
│                      │
└──────────────────────┘

---

Active State

When playing:

// SIGNAL_ACTIVE

STREAMING

---

Player Overlay

Minimal overlay.

Contains:

- Signal title
- Playback status
- Controls

---

Example:

// SIGNAL_ACTIVE


VIDEO_TITLE


STREAMING

1080P

---

Controls

Required

Phase 1:

- Play / pause
- Seek
- Fullscreen
- Quality selection

---

Not Included

Phase 1:

- Comments
- Likes
- Recommendations
- Upload controls
- Social features

---

Terminal Control Design

Instead of:

▶

Use:

[ PLAY_SIGNAL ]

Instead of:

loading spinner

Use:

CONNECTING_STREAM...

---

Stream Resolution

Flow

Signal

↓

YouTubio

↓

Stream URL

↓

ExoPlayer

↓

Decoder

↓

Display

---

Stream Failure Handling

Network Failure

Display:

SIGNAL_LOST

CONNECTION_INTERRUPTED

[ RETRY ]

---

Invalid Stream

Display:

STREAM_UNAVAILABLE

SOURCE_FAILURE

---

Timeout

Display:

CONNECTION_TIMEOUT

---

Buffer Strategy

Goal:

Fast startup over maximum quality.

---

Recommended:

Start playback quickly

↓

Buffer dynamically

↓

Adapt quality

---

Settings:

LoadControl {

    minBufferMs = 15000

    maxBufferMs = 50000

}

---

Quality System

Available Quality Model

data class VideoQuality(

    val name: String,

    val width: Int,

    val height: Int

)

---

Examples:

AUTO

1080P

720P

480P

360P

---

Player Logging

Every player event must be logged.

---

Example:

[PLAYER]

12:02:14

STREAM_REQUEST


[PLAYER]

12:02:15

STREAM_READY


[PLAYER]

12:02:16

PLAYBACK_STARTED

---

Debug Integration

Player reports:

- Startup time
- Buffer events
- Errors
- Resolution changes
- Stream failures

---

Performance Requirements

Target

Startup:

< 3 seconds

---

Memory:

Release player when unused

---

CPU:

Use hardware decoding

---

Network:

Adaptive bitrate

---

Security Rules

The player must:

- Never store account credentials
- Never expose cookies
- Never log private stream URLs
- Never send playback data externally

---

Phase 1 Implementation Checklist

Required

[x] Media3 ExoPlayer integration

[x] YouTubio stream loading

[x] Compose player surface

[x] Playback state model

[x] Error handling

[x] Debug logging

[x] Lifecycle management

---

Player Goal

UPLINK playback should feel like:

SIGNAL FOUND

SIGNAL DECODED

STREAM ACTIVE

The player is not a video window.

It is the device that connects the user to the broadcast network.
