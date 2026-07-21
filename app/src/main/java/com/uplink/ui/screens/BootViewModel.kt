package com.uplink.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Drives the boot sequence. Commit 002: every step is simulated with a
// short delay standing in for real work. Each simulated step is a
// direct placeholder for a specific future integration — see BootState.kt
// header for the Commit 004/005/006 mapping. No step here is an arbitrary
// splash timer; each one represents a real initialization task that just
// isn't wired to its real signal yet.
class BootViewModel : ViewModel() {

    var logLines by mutableStateOf<List<BootLogLine>>(emptyList())
        private set

    var currentState by mutableStateOf(BootState.POWER_ON)
        private set

    init {
        runBootSequence()
    }

    private fun runBootSequence() {
        viewModelScope.launch {
            for (state in BOOT_SEQUENCE) {
                currentState = state
                appendLine(state, BootLineResult.PENDING)

                // Simulated work. Replace per the Commit 004/005/006 plan:
                // this delay stands in for a real suspend call once each
                // subsystem exists (YouTubio connect, Media3 init, etc).
                delay(simulatedDurationFor(state))

                updateLastLine(state, BootLineResult.OK)
            }

            currentState = BootState.READY
        }
    }

    private fun simulatedDurationFor(state: BootState): Long = when (state) {
        BootState.POWER_ON -> 150L
        BootState.LOAD_UI -> 200L
        BootState.LOAD_FONTS -> 200L
        BootState.INITIALIZE_PLAYER -> 250L
        BootState.INITIALIZE_DATABASE -> 250L
        BootState.CHECK_BACKEND -> 400L
        else -> 0L
    }

    private fun appendLine(state: BootState, result: BootLineResult) {
        logLines = logLines + BootLogLine(state, state.displayLabel(), result)
    }

    private fun updateLastLine(state: BootState, result: BootLineResult) {
        logLines = logLines.map { line ->
            if (line.state == state) line.copy(result = result) else line
        }
    }
}
