package com.uplink.ui.screens

// Boot sequence state machine.
//
// Commit 002: all transitions are simulated (timed, but represent real
// future work items rather than an arbitrary single splash delay).
//
// Future commits replace individual simulated transitions with real
// events, without changing this enum or the screen's overall shape:
//   Commit 004 — CHECK_BACKEND waits for actual YouTubio connection result
//   Commit 005 — INITIALIZE_PLAYER waits for real Media3 initialization
//   Commit 006 — every transition emits a DebugEvent via DebugLogger
enum class BootState {
    POWER_ON,
    LOAD_UI,
    LOAD_FONTS,
    INITIALIZE_PLAYER,
    INITIALIZE_DATABASE,
    CHECK_BACKEND,
    READY,
    ERROR
}

// One line of terminal boot output, tied to a specific BootState.
data class BootLogLine(
    val state: BootState,
    val label: String,
    val result: BootLineResult
)

enum class BootLineResult {
    PENDING,   // [...]
    OK,        // [OK]
    FAILED     // [FAILED]
}

// Maps each state to its terminal display label, per
// UPLINK_DESIGN_SYSTEM.md terminal punctuation conventions.
fun BootState.displayLabel(): String = when (this) {
    BootState.POWER_ON -> "POWER_ON"
    BootState.LOAD_UI -> "INITIALIZING_UI"
    BootState.LOAD_FONTS -> "LOADING_TYPEFACES"
    BootState.INITIALIZE_PLAYER -> "INITIALIZING_PLAYER"
    BootState.INITIALIZE_DATABASE -> "INITIALIZING_DATABASE"
    BootState.CHECK_BACKEND -> "CHECKING_SIGNAL_SOURCE"
    BootState.READY -> "UPLINK_STABLE"
    BootState.ERROR -> "SIGNAL_LOST"
}

// Ordered sequence the boot flow walks through. READY and ERROR are
// terminal states, not steps in this list.
val BOOT_SEQUENCE: List<BootState> = listOf(
    BootState.POWER_ON,
    BootState.LOAD_UI,
    BootState.LOAD_FONTS,
    BootState.INITIALIZE_PLAYER,
    BootState.INITIALIZE_DATABASE,
    BootState.CHECK_BACKEND
)
