UPLINK Feature Specification

Purpose

This document defines the features included in UPLINK Phase 1.

UPLINK is a focused broadcast terminal for discovering, searching, and playing video signals.

The application avoids unnecessary social and account features to maintain speed, privacy, and simplicity.

---

Phase 1 Feature Scope

Included Features

✅ Signal browsing
✅ Signal searching
✅ Signal information display
✅ Signal playback
✅ Playback history
✅ Local caching
✅ YouTubio integration
✅ Debug logging
✅ Connection monitoring

---

Excluded Features

❌ User profiles
❌ Uploading content
❌ Comments
❌ Likes
❌ Social features
❌ Public accounts
❌ Push notifications
❌ Creator dashboards
❌ Advanced settings menu

---

Core User Journey

OPEN UPLINK

      ↓

CONNECT TO SIGNAL NETWORK

      ↓

BROWSE SIGNALS

      ↓

SELECT SIGNAL

      ↓

VIEW SIGNAL DATA

      ↓

PLAY SIGNAL

---

Feature 1: Signal Browsing

Description

The main discovery interface where users browse available broadcasts.

Equivalent to a traditional video home feed, but represented as a signal network.

---

Requirements

The home screen must display:

- Signal thumbnail
- Signal title
- Source node
- Duration
- Watching activity
- Upload information
- Status indicators

---

Signal Card Example

┌─────────────────────┐
│                     │
│     THUMBNAIL       │
│                     │
└─────────────────────┘

// SIGNAL

TITLE:
Network Broadcast Example

SOURCE_NODE:
Channel Name

12.4K WATCHING

12:45

---

States

Loading

SCANNING_SIGNAL_FEED...

Empty

NO_SIGNALS_DETECTED

Error

SIGNAL_FEED_UNAVAILABLE

---

Feature 2: Network Search

Description

Allows users to search available video signals.

---

Requirements

Search must support:

- Video search
- Source/channel search
- Search history

---

Search Interface

// NETWORK_SCAN

> ENTER_SIGNAL_QUERY

---

Search States

Searching

SCANNING_NETWORK...

Results

SIGNALS_FOUND: 247

No Results

NO_SIGNAL_MATCH

---

Feature 3: Signal Information

Description

Displays information about a selected signal before playback.

---

Requirements

Display:

- Thumbnail
- Title
- Source
- Description
- Duration
- Watching activity
- Related signals

---

Layout

// SIGNAL_INFO


TITLE

SOURCE_NODE

METADATA

DESCRIPTION


[ CONNECT SIGNAL ]

---

Feature 4: Signal Playback

Description

The main media experience.

Powered by AndroidX Media3 ExoPlayer.

---

Requirements

Support:

- Play
- Pause
- Seek
- Resume playback
- Fullscreen
- Quality selection
- Buffer status

---

Playback States

Connecting

CONNECTING_SIGNAL...

Active

SIGNAL_ACTIVE

Buffering

BUFFERING_STREAM...

Failed

SIGNAL_LOST

---

Feature 5: Playback History

Description

Stores recently viewed signals locally.

---

Purpose

Allows users to quickly return to previous signals without requiring network access.

---

Storage

Local Room database.

---

Data Stored

HistoryItem {

    signalId

    title

    thumbnail

    timestamp

    playbackPosition

}

---

Feature 6: Local Cache

Description

Provides instant loading.

---

Cached Data

- Signal metadata
- Thumbnails
- Search history
- Playback position

---

Cache Strategy

APP OPEN

     ↓

LOAD CACHE

     ↓

SHOW UI

     ↓

SYNC NETWORK

     ↓

UPDATE CACHE

---

Feature 7: YouTubio Integration

Description

UPLINK uses YouTubio as the signal extraction and YouTube communication layer.

---

Responsibilities

YouTubio handles:

- YouTube search
- Subscription data
- Video metadata
- Stream resolution
- Playback URLs

---

Connection

Development:

UPLINK

localhost:7000

YouTubio

Production:

UPLINK

Private Server

YouTubio

---

Feature 8: Debug Logging

Description

Developer-only system monitoring.

---

Logged Events

Application startup:

APP_INIT

DATABASE_READY

CACHE_READY

YOUBTIO_CONNECTED

PLAYER_READY

UPLINK_STABLE

---

Network:

REQUEST_SENT

RESPONSE_RECEIVED

ERROR

---

Player:

STREAM_START

BUFFERING

PLAYBACK_END

---

Feature 9: Connection Monitoring

Description

Shows system connection state.

---

States

Connected:

UPLINK_STABLE

Connecting:

ESTABLISHING_LINK

Disconnected:

SIGNAL_LOST

---

Navigation Structure

Phase 1 navigation:

HOME

SEARCH

SIGNAL

---

Screen Responsibilities

Screen| Responsibility
Home| Browse signals
Search| Find signals
Signal Detail| Inspect signal
Player| Watch signal
Signal Tab| Current playback/status

---

Performance Requirements

UPLINK should:

- Open quickly
- Show cached content immediately
- Start playback quickly
- Avoid blocking UI operations
- Keep animations lightweight

---

Phase 1 Success Criteria

UPLINK Phase 1 is complete when:

- User can open the app
- Signals load quickly
- User can search content
- User can open signal details
- User can play videos
- Playback state is stable
- Debug logs identify failures

---

Product Direction

UPLINK is not a social platform.

It is a fast, private, technical broadcast terminal focused on one thing:

DISCOVER SIGNALS

CONNECT SIGNALS

WATCH SIGNALS
