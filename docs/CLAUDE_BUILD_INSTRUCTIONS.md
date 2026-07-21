UPLINK Claude Build Instructions

Purpose

This document instructs Claude on how to implement UPLINK Phase 1.

UPLINK is an Android-native broadcast terminal application built with Kotlin and Jetpack Compose.

The goal is not to recreate YouTube.

The goal is to create a fast terminal interface that connects to a personal YouTube signal source through YouTubio.

---

1. Project Identity

Application Name

UPLINK

---

Core Concept

UPLINK is:

A private broadcast terminal.

A signal receiver.

A fast YouTube playback interface.

---

Do Not Build

Do not add:

- Upload features
- Profiles
- Settings screens
- Social features
- Comments
- Likes
- Recommendations algorithm
- Creator dashboards

Phase 1 only:

BROWSE

SEARCH

PLAY

---

2. Technology Stack

Required

Use:

Kotlin

Jetpack Compose

Material 3

Coroutines

Flow

Room Database

Hilt Dependency Injection

AndroidX Media3 ExoPlayer

---

3. Architecture

Use:

Clean Architecture
+
MVVM
+
Repository Pattern

---

Package Structure

com.uplink

├── app
│
├── core
│   ├── network
│   ├── database
│   ├── logging
│   └── utils
│
├── data
│   ├── models
│   ├── repository
│   └── datasource
│
├── domain
│   ├── models
│   └── usecases
│
├── player
│
├── ui
│   ├── components
│   ├── screens
│   └── theme
│
└── debug

---

4. UI Implementation Rules

Design System

Follow:

UPLINK_DESIGN_SYSTEM.md
UPLINK_COMPONENT_LIBRARY.md
UPLINK_SCREEN_SPECS.md

---

Colors

Implement exactly:

Void

#08090C


Panel

#111319


Panel2

#171A22


Line

#252A35


Signal

#0DF2C4


Alert

#FF3864


Text

#F2F4F2

---

Typography

Use:

Titles

Oswald

---

Metadata

JetBrains Mono

---

Body

Inter

---

5. Required Screens

Implement only:

BootScreen

HomeScreen

SearchScreen

SignalDetailScreen

PlayerScreen

DebugConsole

---

6. Navigation

Use:

Bottom Navigation

Tabs:

HOME

SEARCH

SIGNAL

---

Do not create:

PROFILE

UPLOAD

SETTINGS

---

7. Components To Build First

Priority order:

System

TerminalHeader

ScanlineOverlay

BlueprintBackground

CornerBracketFrame

---

Navigation

SignalNavigationBar

---

Content

SignalCard

SignalList

SignalStatusBadge

---

Player

PlayerSurface

PlayerControls

PlaybackStatus

---

Debug

DebugLogViewer

---

8. YouTubio Integration

Connection

UPLINK connects to:

http://localhost:7000

during development.

---

Create:

YouTubioClient

responsible for:

- Connection testing
- Search requests
- Signal retrieval
- Stream resolution

---

Do not expose YouTubio models directly to UI.

Flow:

YouTubio Response

↓

Mapper

↓

Signal Model

↓

UI

---

9. Player Implementation

Use:

AndroidX Media3 ExoPlayer

---

Required features:

- Play
- Pause
- Seek
- Fullscreen
- Quality selection
- Error handling

---

Player states:

CONNECTING

READY

PLAYING

BUFFERING

FAILED

---

10. Logging System

Create logging from the beginning.

Every major event must produce:

timestamp

category

message

---

Categories:

APP

NETWORK

YOUBTIO

CACHE

PLAYER

ERROR

---

Example:

12:04:21

PLAYER

STREAM_STARTED

---

11. Database

Use Room.

Tables:

signals

cache

history

debug_logs

preferences

---

Do not store:

- Passwords
- Cookies
- Authentication secrets

---

12. Performance Requirements

Target:

Cold start:
< 2 seconds


Navigation:
< 100ms


Stream startup:
< 3 seconds

---

Rules:

Use:

LazyColumn

Flow

Coroutines

Background loading

Caching

---

Avoid:

Blocking main thread

Large recompositions

Unused resources

---

13. Development Order

Build in this order:

---

Phase 1

Project setup:

- Gradle
- Compose
- Theme
- Navigation

---

Phase 2

Design system:

- Colors
- Fonts
- Background layers
- Components

---

Phase 3

Mock data:

- Signal cards
- Home feed
- Search results

---

Phase 4

Backend:

- YouTubio connection
- Repository
- Data mapping

---

Phase 5

Player:

- Media3
- Stream loading
- Controls

---

Phase 6

Debug:

- Logging
- Performance metrics

---

14. Code Quality Rules

Use:

- Clean naming
- Small composables
- Single responsibility
- Kotlin idioms
- Documentation for complex code

---

Avoid:

- Large files
- Duplicate components
- Hardcoded UI data
- Business logic inside Composables

---

15. Final Acceptance Criteria

UPLINK is complete when:

User can:

[x] Open app

[x] See terminal boot sequence

[x] Browse signals

[x] Search signals

[x] Open signal details

[x] Play video

[x] View playback status

[x] Recover from errors

---

Final Instruction

Build UPLINK as a native Android signal terminal.

The application should feel like:

BOOT

SCAN

CONNECT

DECODE

STREAM

Do not make a YouTube clone.

Build the UPLINK broadcast interface.
