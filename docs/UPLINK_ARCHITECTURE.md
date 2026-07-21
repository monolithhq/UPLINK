UPLINK Architecture

Purpose

This document defines the technical architecture of UPLINK.

The architecture prioritizes:

- Fast response time
- Local-first data access
- Clean separation of systems
- Easy expansion
- Native Android performance

UPLINK is built as a native Kotlin Android application.

---

System Overview

                         UPLINK APP
                    Kotlin + Jetpack Compose

                              |
                              |
                              v

                    Presentation Layer

                    Screens
                    ViewModels
                    UI State

                              |
                              |
                              v

                    Domain Layer

                    Use Cases
                    Models
                    Business Logic

                              |
                              |
                              v

                    Data Layer

              Repository + Data Sources

                    /              \

                   /                \

                  v                  v

          Local Storage          Network

             Room DB             YouTubio API
             Cache               HTTP Client


                              |
                              v

                        YouTubio Core

                        Node.js
                        yt-dlp
                        Stream Resolver

                              |
                              v

                           YouTube

---

Application Stack

Mobile Application

Language:

Kotlin

UI:

Jetpack Compose

Architecture:

MVVM + Clean Architecture

Database:

Room

Networking:

Ktor Client / Retrofit

Video Player:

AndroidX Media3 ExoPlayer

Async:

Kotlin Coroutines
Flow

---

Core Architecture Principle

Local First

UPLINK should never depend on the network for initial rendering.

The application flow:

OPEN APP

    ↓

LOAD LOCAL CACHE

    ↓

DISPLAY SIGNALS

    ↓

SYNC NETWORK

    ↓

UPDATE CACHE

The user sees content immediately.

---

Module Structure

UPLINK/

app/

├── ui/
│
│   ├── home/
│   ├── search/
│   ├── signal/
│   └── player/
│
├── domain/
│
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── data/
│
│   ├── local/
│   │
│   │   ├── database/
│   │   ├── dao/
│   │   └── entities/
│   │
│   ├── remote/
│   │
│   │   ├── YouTubioClient/
│   │   └── NetworkService/
│   │
│   └── repository/
│
├── player/
│
│   ├── PlayerManager.kt
│   └── MediaController.kt
│
├── debug/
│
│   ├── Logger.kt
│   └── DebugConsole.kt
│
└── core/

    ├── Constants.kt
    ├── Extensions.kt
    └── Security.kt

---

Data Flow

Browse Signals

HOME SCREEN

      |
      v

HomeViewModel

      |
      v

SignalRepository

      |
      +----------------+
      |                |
      v                v

 Room Cache       YouTubio

      |                |

      +-------+--------+

              |

              v

          UI Update

---

Search Flow

USER INPUT

      |

SearchViewModel

      |

SearchRepository

      |

YouTubio Search

      |

Results

      |

Cache

      |

Display

---

Playback Flow

USER SELECTS SIGNAL

        |

Signal Detail

        |

Request Stream URL

        |

YouTubio Resolver

        |

Stream URL

        |

Media3 ExoPlayer

        |

PLAYBACK

---

Player Architecture

UPLINK uses Media3 ExoPlayer as the primary playback engine.

Player Screen

      |

Player Controller

      |

Media3 ExoPlayer

      |

Video Surface

Responsibilities:

Player Manager

Handles:

- Start playback
- Pause
- Seek
- Resume
- Quality changes
- Playback state

---

State Management

UPLINK uses immutable UI states.

Example:

sealed class SignalState {

    object Loading

    data class Ready(
        val signals: List<Signal>
    )

    object Empty

    data class Error(
        val message: String
    )
}

---

Backend Connection

Phase 1:

Android App
      |
      |
 Local Network
      |
      |
YouTubio localhost:7000

Development:

localhost:7000

Production options:

Local server
Private VPS
Docker container

---

Debug Architecture

UPLINK includes a development logging system.

Startup sequence:

APP_INIT

DATABASE_READY

CACHE_LOADED

YOUBTIO_CONNECTED

PLAYER_READY

UPLINK_STABLE

Logs contain:

- Startup timing
- Network requests
- Player events
- Errors
- Cache operations

---

Performance Rules

Required

- Cache before network
- Lazy load images
- Avoid unnecessary recomposition
- Use coroutine dispatchers
- Keep playback separate from UI
- Release resources correctly

---

Security Rules

Phase 1:

- Store credentials locally
- Encrypt sensitive data
- Avoid exposing cookies
- Keep YouTubio connection private

---

Future Expansion

Architecture should allow:

- Multiple media sources
- Remote servers
- Account systems
- AI organization
- Advanced recommendations

Without changing:

- UI architecture
- Player architecture
- Data models

---

Architecture Goal

UPLINK should behave like a fast terminal:

USER ACTION

      ↓

LOCAL RESPONSE

      ↓

NETWORK UPDATE

      ↓

BACKGROUND SYNC

The system should always feel connected, immediate, and controlled.
