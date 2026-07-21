package com.uplink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uplink.ui.boot.BootCoordinator
import com.uplink.ui.boot.BootEvent
import com.uplink.ui.boot.BootPhase
import com.uplink.ui.boot.BootReadiness
import com.uplink.ui.components.BlueprintBackground
import com.uplink.ui.components.CRTStartupEffect
import com.uplink.ui.components.ScanlineOverlay
import com.uplink.ui.components.SignalLockAnimation
import com.uplink.ui.components.TerminalLogRenderer
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.TerminalTitleLarge
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkText
import com.uplink.ui.theme.UplinkVoid

// Boot flow: content (log, grid, scanlines) composes immediately;
// CRTStartupEffect reveals it over ~120ms as an overlay rather than
// gating its existence, so nothing is blank while CRT plays. Once the
// coordinator reports Ready, a signal-lock beat plays, then onEvent is
// called with Completed.
//
// State/event shape matches what Commit 003+ architecture will expect:
// BootScreen only reads BootCoordinator.state and reports out via a
// single event callback, rather than owning imperative navigation
// logic itself.
sealed class BootScreenEvent {
    object Completed : BootScreenEvent()
}

@Composable
fun BootScreen(
    onEvent: (BootScreenEvent) -> Unit = {}
) {
    val coordinator = remember { BootCoordinator() }
    var showSignalLock by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coordinator.emit(BootEvent.UiReady.takeIf { BootReadiness.checkUiReady() }
            ?: BootEvent.Failed("UI", "ui_check_failed"))
        coordinator.emit(BootEvent.FontsReady.takeIf { BootReadiness.checkFontsReady() }
            ?: BootEvent.Failed("FONTS", "fonts_check_failed"))
    }

    val state = coordinator.state

    LaunchedEffect(state.phase) {
        if (state.phase is BootPhase.Ready && !showSignalLock) {
            showSignalLock = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UplinkVoid)
    ) {
        BlueprintBackground(modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(text = "UPLINK v0.1", style = TerminalTitleLarge, color = UplinkText)

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))

            TerminalLogRenderer(lines = state.logLines)

            if (state.phase is BootPhase.Complete) {
                Text(
                    text = "ENTERING BROADCAST_NET",
                    style = TelemetryBody,
                    color = UplinkSignal
                )
            }

            if (showSignalLock) {
                SignalLockAnimation(
                    onComplete = {
                        coordinator.markComplete()
                        onEvent(BootScreenEvent.Completed)
                    }
                )
            }
        }

        ScanlineOverlay(modifier = Modifier.fillMaxSize())

        CRTStartupEffect(onComplete = {})
    }
}
