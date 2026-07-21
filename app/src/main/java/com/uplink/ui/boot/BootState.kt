package com.uplink.ui.boot

// Overall boot phase, driven by BootCoordinator as readiness events
// arrive. Not a fixed sequence the screen walks through — the screen
// only ever renders whatever this state currently is.
sealed class BootPhase {
    object Initializing : BootPhase()
    object Ready : BootPhase()
    object Complete : BootPhase()
}

data class BootLogLine(
    val label: String,
    val result: BootLineResult
)

enum class BootLineResult {
    PENDING,
    OK,
    FAILED
}

// Snapshot BootScreen reads from BootCoordinator. Log lines accumulate
// as events arrive; phase reflects whether all currently-known modules
// have reported in.
data class BootState(
    val logLines: List<BootLogLine> = emptyList(),
    val phase: BootPhase = BootPhase.Initializing
)
