UPLINK Screen Specifications

Purpose

This document defines the screens, layouts, states, and behaviors of UPLINK Phase 1.

UPLINK uses a small number of focused screens.

The application prioritizes:

- Fast access
- Minimal navigation
- Clear information hierarchy
- Terminal-style interaction

---

Screen Map

UPLINK

├── Boot Screen
│
├── Home Screen
│
├── Search Screen
│
├── Signal Detail Screen
│
├── Player Screen
│
└── Debug Console (Developer)

---

Global Screen Rules

Every screen must include:

Background System

- Void background
- Blueprint grid overlay
- Scanline overlay

---

Typography

Headers:

Oswald

Metadata:

JetBrains Mono

Body:

Inter

---

System Header

Every main screen has:

// UPLINK_STATUS

UPLINK_STABLE

Example:

// SIGNAL_FEED

UPLINK_STABLE

---

1. Boot Screen

Purpose

Initial startup sequence.

Responsible for:

- Initializing application
- Loading cache
- Connecting to YouTubio
- Preparing player

---

Layout

┌─────────────────────┐
│                     │
│       UPLINK        │
│                     │
│  INITIALIZING...    │
│                     │
└─────────────────────┘

---

Boot Sequence

APP_INIT

DATABASE_START

CACHE_LOADING

YOUBTIO_LINK

PLAYER_READY

UPLINK_STABLE

---

States

Starting

INITIALIZING_SYSTEM...

Connected

UPLINK_STABLE

Failed

SYSTEM_FAILURE
CHECK_CONNECTION

---

2. Home Screen

Purpose

Primary signal browsing interface.

This is the default screen after startup.

---

Header

// SIGNAL_FEED

UPLINK_STABLE

---

Layout

┌──────────────────────┐
│ SIGNAL FEED          │
├──────────────────────┤
│                      │
│  FEATURED SIGNAL     │
│                      │
├──────────────────────┤
│                      │
│  SIGNAL CARD         │
│                      │
├──────────────────────┤
│                      │
│  SIGNAL CARD         │
│                      │
└──────────────────────┘

---

Signal Card

Contains:

- Thumbnail
- Title
- Source node
- Duration
- Watching activity
- Timestamp

Example:

┌─────────────────┐
│                 │
│   THUMBNAIL     │
│                 │
└─────────────────┘

// SIGNAL

TITLE

SOURCE_NODE

24.5K WATCHING

12:30

---

States

Loading

SCANNING_SIGNAL_FEED...

Empty

NO_SIGNALS_FOUND

Offline

LOCAL_CACHE_ONLY

---

3. Search Screen

Purpose

Allows users to scan the network.

---

Header

// NETWORK_SCAN

---

Layout

┌──────────────────────┐
│ > SEARCH_SIGNAL      │
├──────────────────────┤
│                      │
│ RESULTS              │
│                      │
│ SIGNAL CARD          │
│ SIGNAL CARD          │
│                      │
└──────────────────────┘

---

Search Input

Style:

> ENTER_QUERY_

Rules:

- Terminal style
- Mono font
- Cyan cursor indicator

---

States

Searching:

SCANNING_NETWORK...

Results:

SIGNALS_FOUND: 120

Empty:

NO_MATCHING_SIGNAL

---

4. Signal Detail Screen

Purpose

Provides information before playback.

---

Header

// SIGNAL_INFO

---

Layout

┌──────────────────────┐
│                      │
│      IMAGE           │
│                      │
├──────────────────────┤
│ TITLE                │
│                      │
│ SOURCE_NODE          │
│                      │
│ METADATA             │
│                      │
├──────────────────────┤
│ DESCRIPTION          │
│                      │
├──────────────────────┤
│ CONNECT SIGNAL       │
└──────────────────────┘

---

Components

Signal Metadata

Displays:

- Duration
- Watching activity
- Upload date
- Source

---

Connect Button

Style:

[ CONNECT_SIGNAL ]

Active state:

- Cyan
- Corner brackets

---

States

Loading:

RETRIEVING_SIGNAL_DATA...

Error:

SIGNAL_DATA_UNAVAILABLE

---

5. Player Screen

Purpose

Main viewing interface.

---

Header

Minimal overlay:

// SIGNAL_ACTIVE

---

Layout

┌──────────────────────┐
│                      │
│                      │
│       VIDEO          │
│                      │
│                      │
├──────────────────────┤
│ TITLE                │
│ STATUS               │
└──────────────────────┘

---

Player Controls

Controls:

- Play/pause
- Timeline
- Fullscreen
- Quality

---

Player Status

Examples:

STREAMING

BUFFERING

SIGNAL_LOST

---

States

Connecting:

CONNECTING_STREAM...

Playing:

SIGNAL_ACTIVE

Buffering:

BUFFERING_STREAM...

Failed:

SIGNAL_LOST

---

6. Signal Tab

Purpose

Bottom navigation destination.

Shows current playback/system signal state.

---

Layout

// ACTIVE_SIGNAL


CURRENT:

TITLE


STATUS:

STREAMING


SOURCE:

SOURCE_NODE

---

7. Debug Console

Purpose

Developer-only diagnostics.

Not visible in normal usage.

---

Layout

// DEBUG_TERMINAL


APP_INIT        OK

DATABASE        OK

NETWORK         OK

PLAYER          OK

---

Functions

Displays:

- Startup timing
- Network events
- Player events
- Errors
- Cache operations

---

Navigation

Bottom navigation:

┌────────┬────────┬────────┐
│ HOME   │ SEARCH │ SIGNAL │
└────────┴────────┴────────┘

---

Navigation Rules

Home

Default landing screen.

Search

Independent browsing path.

Signal

Current playback and connection state.

---

Screen Count

User Screens

5

- Boot
- Home
- Search
- Signal Detail
- Player

---

Developer Screens

1

- Debug Console

---

Screen Design Goal

Every screen should communicate:

CONNECTED

AVAILABLE

READY

STREAMING

UPLINK should feel like a live terminal connected to a broadcast network.
