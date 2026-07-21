UPLINK Build Configuration

Purpose

This document locks the technical foundation of UPLINK.

These decisions are binding for Commit 001 and all future commits.

Claude, GitHub Actions, and any contributor must treat these values as fixed unless this document is explicitly revised.

---

Language

Kotlin

Version:

2.x

---

Android Gradle Plugin

AGP:

8.x

---

Gradle

Gradle:

8.x

---

Java

JDK:

17

---

Android SDK

compileSdk:

35

targetSdk:

35

minSdk:

26

---

UI Framework

Jetpack Compose:

Enabled

Compose Compiler:

Kotlin compatible

---

Build Types

debug:

Enabled

release:

Future phase

---

CI

GitHub Actions:

Build:

assembleDebug

Tests:

Unit tests enabled

---

Development Environment

Primary:

Termux + GitHub

Secondary:

Android Studio

---

Network

Development endpoint:

http://localhost:7000

Production endpoint:

Configurable

---

Manifest Requirements

Permissions:

INTERNET

Development:

Allow localhost HTTP traffic.

Production:

HTTPS only.

---

Commit 001 Pinned Stack

Component| Version
JDK| 17
Gradle| 8.10+
AGP| 8.7+
Kotlin| 2.0+
Compose BOM| 2025.x
compileSdk| 35
minSdk| 26

---

Reasoning

JDK 17 is the stable Android toolchain baseline.

API 26 keeps compatibility with older Android devices without adding large complexity.

API 35 keeps current Android support.

Kotlin 2.x avoids starting a new project on an older compiler.

---

Commit 001 Files

The first commit creates only:

UPLINK/

├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       └── java/com/uplink/
│           ├── UplinkApplication.kt
│           └── ui/
│               └── MainActivity.kt
│
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties
│
└── .github/
    └── workflows/
        └── android-build.yml

---

Commit 001 Success Criteria

After:

git clone UPLINK
cd UPLINK
./gradlew assembleDebug

Expected:

BUILD SUCCESSFUL

Installing the APK should show:

UPLINK
BOOTING...

Not yet present:

- YouTubio
- Room
- Media3
- Hilt
- Navigation
- Screens

---

Specs Required Before Commit 004

The following documents must exist before the data layer commit begins:

1. UPLINK_YOUTUBIO_API.md

Defines:

GET /catalog
GET /search
GET /stream

Request and response JSON for each.

---

2. UPLINK_DATABASE_SCHEMA.md

Defines SignalEntity:

id
title
channel
thumbnail
duration
streamUrl
createdAt

Plus indexes.

---

3. UPLINK_CI_CONFIG.md

Defines:

- Debug APK only
- No signing
- Artifact name: uplink-debug.apk

---

Status

Once UPLINK_YOUTUBIO_API.md, UPLINK_DATABASE_SCHEMA.md, and UPLINK_CI_CONFIG.md are added, the repo is fully generation-ready through Commit 004.

Commit 001 can proceed immediately using this document as the locked foundation.
