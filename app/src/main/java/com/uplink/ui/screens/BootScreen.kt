package com.uplink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uplink.ui.components.BlueprintBackground
import com.uplink.ui.components.ScanlineOverlay
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.TerminalTitleLarge
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkText
import com.uplink.ui.theme.UplinkTextDim
import com.uplink.ui.theme.UplinkVoid

// Boot sequence screen. Renders whatever BootViewModel's state machine
// reports — this file has no timing logic of its own. When Commits
// 004-006 replace simulated transitions with real ones, this screen
// does not need to change.
@Composable
fun BootScreen(viewModel: BootViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UplinkVoid)
    ) {
        BlueprintBackground(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = "UPLINK",
                style = TerminalTitleLarge,
                color = UplinkText
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = "BOOT_SEQUENCE",
                style = TelemetryBody,
                color = UplinkTextDim
            )

            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))

            LazyColumn {
                items(viewModel.logLines) { line ->
                    BootLogRow(line)
                }
            }

            if (viewModel.currentState == BootState.ERROR) {
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))
                Text(
                    text = "SIGNAL_LOST",
                    style = TelemetryBody,
                    color = UplinkAlert
                )
                Text(
                    text = "PRESS TO RETRY",
                    style = TelemetryBody,
                    color = UplinkTextDim
                )
            }
        }

        ScanlineOverlay(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun BootLogRow(line: BootLogLine) {
    val resultText = when (line.result) {
        BootLineResult.PENDING -> "[...]"
        BootLineResult.OK -> "[OK]"
        BootLineResult.FAILED -> "[FAILED]"
    }
    val resultColor = when (line.result) {
        BootLineResult.PENDING -> UplinkTextDim
        BootLineResult.OK -> UplinkSignal
        BootLineResult.FAILED -> UplinkAlert
    }

    Text(
        text = "> ${line.label.padEnd(24)} $resultText",
        style = TelemetryBody,
        color = if (line.result == BootLineResult.PENDING) UplinkTextDim else UplinkText,
    )
    androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 2.dp))
    if (line.result == BootLineResult.OK) {
        // color override handled by re-rendering result token color separately
        // kept simple for Commit 002; can be split into an AnnotatedString
        // in a later polish pass if per-token coloring becomes worth it
    }
}
