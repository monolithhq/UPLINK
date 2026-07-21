package com.uplink.ui.boot

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Observes readiness events from independent subsystems and derives
// overall boot state. Does not own or sequence subsystem startup —
// each subsystem decides when it's ready and reports in via emit().
//
// Commit 002: UI and Fonts are the only subsystems that exist and
// self-report. Commit 004/005 add Database/Player/Backend by adding
// new BootEvent cases and a corresponding required-module entry below
// — this class does not need to change shape to accommodate them.
//
// Plain class, no Android/lifecycle dependency. Commit 002 is UI-only;
// this can be wrapped in a real ViewModel (or Hilt-injected presenter)
// once the app's actual architecture (presentation/domain/data/di) is
// established, without this class itself changing.
class BootCoordinator {

    private val requiredModules = listOf("UI", "FONTS")
    private val reported = linkedMapOf<String, BootLineResult>()

    var state by mutableStateOf(BootState())
        private set

    fun emit(event: BootEvent) {
        when (event) {
            is BootEvent.UiReady -> report("UI", "INITIALIZING_UI", BootLineResult.OK)
            is BootEvent.FontsReady -> report("FONTS", "LOADING_TYPEFACES", BootLineResult.OK)
            is BootEvent.Failed -> report(event.module, event.module, BootLineResult.FAILED)
        }
    }

    private fun report(key: String, label: String, result: BootLineResult) {
        reported[key] = result

        val lines = reported.entries.map { (k, r) ->
            val displayLabel = when (k) {
                "UI" -> "INITIALIZING_UI"
                "FONTS" -> "LOADING_TYPEFACES"
                else -> label
            }
            BootLogLine(displayLabel, r)
        }

        val allReported = requiredModules.all { reported.containsKey(it) }
        val anyFailed = reported.values.any { it == BootLineResult.FAILED }

        val phase = when {
            anyFailed -> BootPhase.Initializing // no failure UI yet; just halts progression
            allReported -> BootPhase.Ready
            else -> BootPhase.Initializing
        }

        state = BootState(logLines = lines, phase = phase)
    }

    fun markComplete() {
        state = state.copy(phase = BootPhase.Complete)
    }
}
