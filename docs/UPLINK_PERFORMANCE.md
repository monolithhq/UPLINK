UPLINK Performance Specification

Purpose

Defines performance targets, optimization strategies, and monitoring requirements for UPLINK Phase 1.

The goal is a fast, low-latency broadcast terminal experience.

UPLINK should prioritize:

- Instant navigation
- Fast signal discovery
- Quick playback startup
- Low memory usage
- Reliable operation on mobile devices

---

Performance Philosophy

CACHE FIRST

LOAD ASYNC

RENDER FAST

STREAM SMART

---

Target Devices

Phase 1 target:

- Android phones
- Mid-range hardware and above
- Limited storage environments
- Variable network conditions

---

Performance Targets

Application Startup

Target:

Cold Start

< 2 seconds

Flow:

APP_START

↓

INITIALIZE_DATABASE

↓

LOAD_LOCAL_STATE

↓

DISPLAY_HOME

↓

SYNC_NETWORK

---

UI Performance

Frame Rate

Target:

60 FPS

Requirements:

- Avoid expensive recompositions
- Use lazy lists
- Avoid blocking the main thread

---

Navigation Speed

Target:

Screen transition

< 100ms

---

Required:

- Keep ViewModels alive
- Cache loaded content
- Avoid unnecessary reloads

---

Home Feed Performance

Goal

The home screen should appear immediately.

---

Flow:

OPEN_APP

↓

SHOW_CACHE

↓

DISPLAY_SIGNALS

↓

UPDATE_BACKGROUND

---

Signal List

Use:

LazyColumn

instead of:

Column

---

Thumbnail Loading

Requirements:

- Lazy loading
- Memory caching
- Placeholder states

Recommended:

---

Search Performance

Target

Search response:

< 1 second

---

Optimization:

- Debounce input
- Cancel previous searches
- Cache recent queries

---

Example:

query
    .debounce(300)
    .distinctUntilChanged()

---

Player Performance

Startup Target

Signal selected:

Tap

↓

Stream resolve

↓

Playback

< 3 seconds

---

Player Optimization

Use:

- Hardware decoding
- Adaptive bitrate
- Preloading where possible
- Proper lifecycle release

---

Memory Management

Target

RAM usage:

< 200MB

---

Rules:

Avoid:

- Holding thumbnails permanently
- Keeping unused players alive
- Large object graphs

---

Required:

Screen closed

↓

Release resources

↓

Clear references

---

Cache Strategy

Cache Layers

LEVEL 1

Memory Cache


LEVEL 2

Room Database


LEVEL 3

Network

---

Signal Cache

Stores:

- Title
- Thumbnail
- Metadata
- Stream availability

Does not store:

- Private credentials
- Cookies
- Authentication tokens

---

Network Performance

Requirements

- Async requests
- Timeout handling
- Retry logic
- Connection monitoring

---

Network States

CONNECTED

LIMITED

OFFLINE

FAILED

---

YouTubio Connection

Monitoring

Track:

CONNECTING

CONNECTED

TIMEOUT

FAILED

---

Coroutine Rules

All network operations:

Dispatchers.IO

Database:

Dispatchers.IO

UI:

Dispatchers.Main

---

Background Tasks

Allowed:

- Cache refresh
- Metadata sync
- Log cleanup

---

Not allowed:

- Heavy processing during startup
- Blocking UI thread
- Continuous background polling

---

Logging Performance

Debug Mode

Full logging:

PLAYER

NETWORK

CACHE

DATABASE

---

Production Mode

Minimal logging:

ERROR

CRITICAL EVENTS

---

Debug Metrics

Track:

Startup

APP_START_TIME
DATABASE_LOAD_TIME
FIRST_SCREEN_RENDER

---

Player

STREAM_REQUEST_TIME
BUFFER_TIME
PLAYBACK_START_TIME

---

Network

REQUEST_DURATION
FAILURE_COUNT

---

Performance Dashboard

Developer console:

// PERFORMANCE


STARTUP

1.4s


CACHE_LOAD

120ms


STREAM_START

2.1s


MEMORY

142MB

---

Battery Optimization

Rules:

- Stop playback when backgrounded
- Avoid constant network polling
- Reduce animations when unnecessary

---

Animation Rules

Animations must:

- Be short
- Avoid heavy effects
- Use GPU-friendly transitions

Recommended:

100-250ms

---

Compose Optimization

Required:

Use:

remember()

derivedStateOf()

LazyColumn()

collectAsStateWithLifecycle()

---

Avoid:

Large recompositions

Mutable global state

Heavy calculations inside UI

---

Database Performance

Room Rules

Use:

- Indexed queries
- Limited result sizes
- Background operations

---

Example:

LIMIT 50

instead of:

SELECT *

---

Error Recovery Performance

Errors should recover quickly.

---

Example:

STREAM_FAILED

↓

RETRY

↓

RECONNECT

↓

RESUME

---

Release Build Requirements

Before release:

Enable:

R8

ProGuard

Resource shrinking

---

Performance Testing

Required tests:

Startup Test

Measure:

Cold launch
Warm launch

---

Feed Test

Measure:

50 signals loaded
100 signals loaded

---

Player Test

Measure:

720p stream

1080p stream

Network interruption

---

Phase 1 Performance Checklist

App

[x] Cold start under 2 seconds

[x] No blocking UI operations

[x] Cached home feed

[x] Lifecycle-safe components

Player

[x] Hardware decoding

[x] Stream startup under 3 seconds

[x] Error recovery

Network

[x] Async requests

[x] Timeout handling

[x] Retry support

Debug

[x] Performance logging

[x] Startup metrics

[x] Player metrics

---

Performance Goal

UPLINK should behave like:

OPEN

↓

READY

↓

SEARCH

↓

CONNECT

↓

STREAM

The user should feel connected to the signal network immediately.
