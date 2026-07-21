UPLINK YouTubio API

Purpose

Defines the contract between UPLINK and the local YouTubio companion service.

YouTubio runs at http://localhost:7000 in development. Production endpoint is configurable per UPLINK_BUILD_CONFIG.md.

This is the only document YouTubioClient, StreamResolver, and SignalMapper are allowed to depend on. If YouTubio's internals change, only this document and YouTubioClient should need updates — no other layer should know YouTubio exists.

---

Design Rules

- All requests are GET. YouTubio Phase 1 is read-only from UPLINK's perspective.
- All responses are JSON, wrapped in the standard envelope (see below).
- UPLINK never talks to YouTube directly. Every request goes through YouTubio.
- Timeouts: connect 5s, read 15s (streams may take longer to resolve than catalog/search).
- No auth headers in Phase 1 (localhost, single-user, trusted network).

---

Response Envelope

Every endpoint returns this shape, matching YouTubioResponse<T> in UPLINK_DATA_MODELS.md:

{
  "success": true,
  "data": { },
  "error": null
}

On failure:

{
  "success": false,
  "data": null,
  "error": "human-readable message"
}

YouTubioClient unwraps this envelope. Nothing above the remote/ layer should ever see success/data/error directly — only the unwrapped T or a thrown/mapped exception.

---

Endpoint 1 — GET /catalog

Purpose

Returns the default/home feed of signals. Used by HomeScreen on boot and refresh.

Request

GET /catalog?limit=25&cursor=<optional>

Query Parameters

Param| Type| Required| Description
limit| Int| No (default 25)| Max signals to return
cursor| String| No| Pagination cursor from a previous response

Response — data shape

{
  "signals": [
    {
      "id": "yt_dQw4w9WgXcQ",
      "title": "Signal title text",
      "description": "Full description text or null",
      "thumbnailUrl": "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
      "channelId": "UCxxxxxxxxxxxx",
      "channelName": "Source Node Name",
      "channelAvatarUrl": "https://yt3.ggpht.com/xxxx",
      "durationSeconds": 212,
      "uploadedAt": 1710000000,
      "viewCount": 245000
    }
  ],
  "nextCursor": "opaque_cursor_string_or_null"
}

Mapping to Signal (UPLINK_DATA_MODELS.md)

YouTubio field| Signal field
id| id
title| title
description| description
thumbnailUrl| thumbnailUrl
channelName| sourceNode
durationSeconds| duration
uploadedAt| uploadedAt
viewCount| watchingCount
(none — not resolved yet)| playbackUrl = null
(derived)| status = SignalStatus.READY

playbackUrl is always null from /catalog and /search. It is only populated after a /stream call. status is set to READY when the signal has enough metadata to display but has not been stream-resolved yet.

channelId/channelAvatarUrl are used to build a SourceNode only if/when the user drills into a source node view; /catalog and /search do not return a full SourceNode object, only enough fields to render a SignalCard.

Errors

Code (HTTP)| error message| Meaning
200| —| Success, envelope success:true
502| "youtubio_upstream_unreachable"| YouTubio itself cannot reach YouTube
503| "youtubio_not_ready"| yt-dlp backend still initializing
500| "youtubio_internal_error"| Unhandled exception in YouTubio

---

Endpoint 2 — GET /search

Purpose

Network scan. Used by SearchScreen.

Request

GET /search?q=<query>&limit=25&cursor=<optional>

Query Parameters

Param| Type| Required| Description
q| String| Yes| Search query, URL-encoded
limit| Int| No (default 25)| Max results
cursor| String| No| Pagination cursor

Response — data shape

Same shape as /catalog:

{
  "signals": [ ... same signal object shape as /catalog ... ],
  "nextCursor": "opaque_cursor_string_or_null"
}

Mapping

Identical to /catalog mapping above. SearchScreen maps results into SearchState.Results(signals). An empty signals array maps to SearchState.Empty, not an error.

Errors

Same error table as /catalog, plus:

Code (HTTP)| error message| Meaning
400| "query_empty"| q was missing or blank

---

Endpoint 3 — GET /stream

Purpose

Resolves a specific signal ID to a playable stream URL. Called when the user opens PlayerScreen for a signal, not before.

Request

GET /stream?id=<signalId>&quality=<optional>

Query Parameters

Param| Type| Required| Description
id| String| Yes| Signal id, matches Signal.id
quality| String| No| Preferred quality label (e.g. "1080p"); YouTubio falls back to best available if omitted or unavailable

Response — data shape

{
  "id": "yt_dQw4w9WgXcQ",
  "streamUrl": "https://rr-xxxx.googlevideo.com/videoplayback?...",
  "expiresAt": 1710003600,
  "availableQualities": [
    { "label": "1080p", "width": 1920, "height": 1080, "bitrate": 4500000 },
    { "label": "720p", "width": 1280, "height": 720, "bitrate": 2500000 }
  ],
  "selectedQuality": { "label": "1080p", "width": 1920, "height": 1080, "bitrate": 4500000 }
}

Mapping

YouTubio field| Destination
streamUrl| Signal.playbackUrl (patched into the already-loaded Signal) and PlaybackSession source
availableQualities[]| List<StreamQuality>
selectedQuality| StreamQuality used to initialize PlaybackSession.quality
expiresAt| Held by StreamResolver only, used to know when to re-resolve; not exposed to UI models

expiresAt exists because googlevideo stream URLs expire. StreamResolver is responsible for checking expiry before handing playbackUrl to the player, and re-calling /stream if expired. This logic lives entirely in data/remote/StreamResolver.kt — no other layer should know stream URLs expire.

Errors

Code (HTTP)| error message| Meaning
200| —| Success
404| "signal_not_found"| id does not exist or was removed upstream
410| "signal_unavailable"| Video exists but is private/deleted/region-locked — maps to SignalStatus.UNAVAILABLE
502| "youtubio_upstream_unreachable"| Same as above
504| "resolve_timeout"| yt-dlp resolution took too long — maps to SignalStatus.LOST

---

Client-Side Error Mapping (all endpoints)

YouTubioClient converts HTTP/envelope errors into a small sealed type before they reach the repository:

sealed class YouTubioException(message: String) : Exception(message) {
    class Unreachable : YouTubioException("youtubio_upstream_unreachable")
    class NotReady : YouTubioException("youtubio_not_ready")
    class NotFound(val signalId: String) : YouTubioException("signal_not_found")
    class Unavailable(val signalId: String) : YouTubioException("signal_unavailable")
    class Timeout : YouTubioException("resolve_timeout")
    class Unknown(raw: String) : YouTubioException(raw)
}

SignalRepository catches these and translates them into SignalStatus or SearchState.Error — raw YouTubioException never reaches ViewModels or Composables.

---

What YouTubioClient Owns vs What It Must Not Do

Owns:
- Building request URLs against the configured endpoint (YouTubioConfig.endpoint)
- Parsing the response envelope
- Throwing YouTubioException subtypes on failure
- Nothing else — no caching, no retry policy, no UI-facing state

Must not do:
- Must not talk to YouTube directly under any circumstance
- Must not persist anything (that's SignalRepository + Room)
- Must not know about Compose, ViewModels, or navigation

---

Open Item (non-blocking)

YouTubio's own internal yt-dlp invocation, caching, and rate-limiting are out of scope for this document — UPLINK treats YouTubio as a black box behind these three endpoints. If YouTubio's own API surface changes, this file is updated first, then YouTubioClient.kt second. No other file should need to change.
