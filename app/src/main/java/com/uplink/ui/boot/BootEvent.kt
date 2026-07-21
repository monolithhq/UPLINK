package com.uplink.ui.boot

// Events reported by individual subsystems as they become ready.
// Commit 002 only has UI and Fonts reporting. Future commits add
// their own event types (Database, Player, Backend) without needing
// BootCoordinator to be rewritten — it just needs to know how to
// react to a new event type when one is added.
sealed class BootEvent {
    object UiReady : BootEvent()
    object FontsReady : BootEvent()
    data class Failed(val module: String, val reason: String) : BootEvent()
}
