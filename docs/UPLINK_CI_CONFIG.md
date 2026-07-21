UPLINK CI Configuration

Purpose

Defines GitHub Actions behavior for UPLINK. Governs .github/workflows/android-build.yml, which is created in Commit 001.

This is a Phase 1, debug-only pipeline. Release signing and a release workflow are explicitly out of scope until UPLINK_BUILD_CONFIG.md's release build type moves out of "Future phase."

---

Trigger Strategy

Workflow runs on:

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]
  workflow_dispatch:

- push to main: builds every commit that lands on main.
- pull_request into main: builds proposed changes before merge.
- workflow_dispatch: allows manually re-running a build from the Actions tab without pushing a new commit.

No scheduled builds. No tag-triggered builds — that's reserved for the future release workflow.

---

Branch Strategy

Phase 1 is single-developer, so this is intentionally minimal:

- main is the only long-lived branch and is always expected to build.
- Feature/commit work may happen directly on main (per the Commit 001–006 plan in CLAUDE_BUILD_INSTRUCTIONS.md) or on short-lived branches merged via PR — either is acceptable; CI behavior is identical either way.
- No branch protection rules are mandated by this doc. If the user wants to require CI to pass before merge, that's a GitHub repo setting made later, not something this file needs to encode.

---

Build Matrix

Single job, single configuration. No matrix needed in Phase 1:

runs-on: ubuntu-latest

- One OS (ubuntu-latest), one JDK version, one build variant.
- No multi-API-level testing, no multiple device configs — that's disproportionate for a single-user app at this stage.

---

Job Steps

1. Checkout — actions/checkout@v4
2. Set up JDK — actions/setup-java@v4, distribution: temurin, java-version: '17' (matches UPLINK_BUILD_CONFIG.md JDK pin exactly)
3. Set up Gradle caching — actions/setup-gradle (or actions/cache keyed on gradle wrapper + dependency lock files) to keep build times reasonable
4. Grant execute permission — chmod +x gradlew
5. Build — ./gradlew assembleDebug
6. Run unit tests — ./gradlew test (per UPLINK_BUILD_CONFIG.md: "Tests: Unit tests enabled")
7. Upload artifact — actions/upload-artifact@v4, see naming below

Steps run in this order; a failure at step 5 or 6 stops the workflow before step 7 — a failed build never produces an artifact.

---

Build Command

./gradlew assembleDebug

- Debug APK only. assembleRelease is not invoked anywhere in this workflow.
- No signing step. Debug builds use the default debug keystore Android's build tools generate automatically — nothing custom is configured, and no keystore secrets are stored in GitHub Secrets in Phase 1.

---

Artifact

Name: uplink-debug.apk

Produced from: app/build/outputs/apk/debug/app-debug.apk (Gradle's default debug output path/filename)

Upload step renames/targets it to the fixed artifact name:

- name: Upload debug APK
  uses: actions/upload-artifact@v4
  with:
    name: uplink-debug.apk
    path: app/build/outputs/apk/debug/app-debug.apk

The artifact name stays fixed (uplink-debug.apk) across all commits/builds — it is not versioned or timestamped in Phase 1. Each workflow run's artifact is scoped to that run automatically by GitHub Actions, so there's no collision risk between runs.

---

Signing

None. Explicitly deferred.

- No release keystore.
- No GitHub Secrets required for signing in Phase 1.
- When release builds are scoped (future phase, per UPLINK_BUILD_CONFIG.md), this document gets a new "Release Signing" section covering keystore storage, signing config in app/build.gradle.kts, and a separate release workflow or job. Until then, this workflow only ever touches the debug build type.

---

Release Workflow

Not created in Phase 1. There is no android-release.yml, no tag-triggered build, no Play Store or GitHub Releases upload step. This section exists only to record that the omission is deliberate, not an oversight — so a future session doesn't mistake "no release workflow" for a gap that needs filling right now.

---

Failure Behavior

- Any step failure fails the whole job (default GitHub Actions behavior — no continue-on-error anywhere in this workflow).
- No retry logic, no flaky-test quarantine — not warranted at this scale.
- Build status is visible via the standard GitHub Actions UI/badge; no external notification integration (Slack, email, etc.) is configured, since this is single-user.

---

What This Config Does Not Cover (non-blocking, noted for later)

- Lint (./gradlew lint) is not part of the pipeline yet — could be added as a step later without restructuring anything above.
- No code coverage reporting.
- No dependency vulnerability scanning (e.g. Dependabot config is a separate concern from this workflow file).
- No caching benchmarks — the Gradle caching step is included for reasonable build times but isn't tuned/measured yet.
