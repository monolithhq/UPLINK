UPLINK Component Library

Purpose

This document defines the reusable UI components used throughout UPLINK.

All components must follow the UPLINK Design System.

Components should feel like parts of a broadcast terminal, not standard mobile UI elements.

---

Component Rules

All components must:

- Use existing UPLINK colors
- Follow typography rules
- Support terminal states
- Avoid unnecessary decoration
- Reuse brackets, grid, and scanline systems

---

Component Categories

UPLINK COMPONENTS

├── System Components
│
├── Navigation Components
│
├── Signal Components
│
├── Player Components
│
├── Input Components
│
└── Debug Components

---

1. System Components

---

Terminal Header

Purpose

Global screen identification.

Used at the top of screens.

---

Appearance

// SIGNAL_FEED

UPLINK_STABLE

---

Properties

TerminalHeader(
    title,
    status,
    alert
)

---

States

Stable

UPLINK_STABLE

Connecting

ESTABLISHING_LINK

Failed

SIGNAL_LOST

---

Scanline Overlay

Purpose

Atmospheric terminal layer.

---

Rules

- Always active
- Very low opacity
- Does not block interaction
- Does not reduce readability

---

Blueprint Grid

Purpose

Background technical structure.

---

Rules

- Fixed background
- Low contrast
- Applied globally

---

Corner Bracket Frame

Purpose

UPLINK focus indicator.

Replaces shadows and glow.

---

Appearance

┌──────────────┐
│              │
│   SIGNAL     │
│              │
└──────────────┘

---

Usage

Applied to:

- Active signals
- Selected cards
- Focused buttons
- Player states

---

States

Default:

No brackets

Active:

Brackets visible

Pressed:

Brackets + signal color

---

2. Navigation Components

---

Bottom Signal Navigation

Purpose

Primary mobile navigation.

---

Items

HOME

SEARCH

SIGNAL

---

Appearance

┌────────┬────────┬────────┐
│ HOME   │ SEARCH │ SIGNAL │
└────────┴────────┴────────┘

---

Active State

Uses:

- Cyan accent
- Corner brackets
- Oswald typography

---

3. Signal Components

---

Signal Card

Purpose

Main browsing component.

---

Layout

┌───────────────────┐
│                   │
│    THUMBNAIL      │
│                   │
├───────────────────┤
│ // SIGNAL         │
│                   │
│ TITLE             │
│                   │
│ SOURCE_NODE       │
│                   │
│ 24K WATCHING      │
└───────────────────┘

---

Data

SignalCard(
    thumbnail,
    title,
    source,
    activity,
    duration
)

---

Typography

Title:

Oswald

Metadata:

JetBrains Mono

Description:

Inter

---

States

Default

Normal card.

Pressed

- Corner brackets appear
- Cyan indicator

Loading

SCANNING_SIGNAL...

Unavailable

SIGNAL_UNAVAILABLE

---

Featured Signal Card

Purpose

Large highlighted signal.

---

Differences

- Larger thumbnail
- Stronger framing
- More metadata

---

Source Node Badge

Purpose

Identifies origin.

Example:

SOURCE_NODE:
CHANNEL_NAME

---

Style

- Mono font
- Small size
- Text dim color

---

Signal Status Indicator

Purpose

Shows signal state.

---

States

Available:

SIGNAL_READY

Live:

LIVE_SIGNAL

Error:

SIGNAL_LOST

---

4. Player Components

---

Signal Player Surface

Purpose

Video container.

---

Layout

┌─────────────────────┐
│                     │
│       VIDEO         │
│                     │
└─────────────────────┘

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

Player Control Bar

Purpose

Playback controls.

---

Controls

- Play/pause
- Timeline
- Quality
- Fullscreen

---

Rules

Controls should:

- Stay minimal
- Use terminal language
- Avoid standard media player styling

---

Playback Status Panel

Purpose

Technical playback information.

---

Example

STREAM:

ACTIVE

QUALITY:

1080P

BUFFER:

98%

---

5. Input Components

---

Terminal Search Field

Purpose

Network search input.

---

Appearance

> SEARCH_SIGNAL_

---

Properties

SignalSearchField(
    query,
    onSearch
)

---

States

Empty:

> ENTER_QUERY_

Typing:

> cyberpunk_

Searching:

SCANNING...

---

Terminal Button

Purpose

Primary actions.

---

Example

[ CONNECT_SIGNAL ]

---

States

Default:

Bordered

Active:

Cyan + brackets

Disabled:

Signal dim

---

6. Debug Components

---

Debug Log Viewer

Purpose

Developer diagnostics.

---

Appearance

[12:02:14]

PLAYER_READY

CACHE_LOADED

NETWORK_OK

---

Data

DebugLog(
    timestamp,
    level,
    message
)

---

Log Levels

INFO

WARNING

ERROR

---

Connection Monitor

Purpose

Shows system health.

---

Display

YOUBTIO

ONLINE


PLAYER

READY


CACHE

ACTIVE

---

Loading Components

---

Signal Scanner

Purpose

Loading animation.

---

Display

SCANNING_NETWORK...

██████░░░░

---

Error Components

---

Signal Lost Panel

Purpose

Network failure display.

---

Display

SIGNAL_LOST

CONNECTION_FAILED

[ RETRY ]

---

Component Naming Rules

Kotlin naming:

SignalCard()

TerminalHeader()

PlayerSurface()

SignalScanner()

DebugConsole()

---

Component Creation Rules

Before creating a new component:

1. Check if an existing component can be reused.
2. Use existing UPLINK patterns.
3. Do not introduce new visual languages.
4. Keep components small and composable.

---

Component Goal

Every component should feel like a module inside the UPLINK terminal:

SYSTEM

SIGNAL

NETWORK

PLAYER

DEBUG

The interface is not a collection of pages.

It is a connected broadcast machine.
