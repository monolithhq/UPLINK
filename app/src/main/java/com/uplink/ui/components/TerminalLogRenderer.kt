package com.uplink.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.uplink.ui.boot.BootLineResult
import com.uplink.ui.boot.BootLogLine
import com.uplink.ui.theme.TelemetryBody
import com.uplink.ui.theme.UplinkAlert
import com.uplink.ui.theme.UplinkSignal
import com.uplink.ui.theme.UplinkText
import com.uplink.ui.theme.UplinkTextDim
import androidx.compose.foundation.layout.padding

@Composable
fun TerminalLogRenderer(lines: List<BootLogLine>) {
    Column {
        lines.forEach { line -> TerminalLogLine(line) }
    }
}

@Composable
private fun TerminalLogLine(line: BootLogLine) {
    val resultText = when (line.result) {
        BootLineResult.PENDING -> "..."
        BootLineResult.OK -> "[OK]"
        BootLineResult.FAILED -> "[FAILED]"
    }
    val resultColor = when (line.result) {
        BootLineResult.PENDING -> UplinkTextDim
        BootLineResult.OK -> UplinkSignal
        BootLineResult.FAILED -> UplinkAlert
    }

    val annotated = buildAnnotatedString {
        withStyle(SpanStyle(color = UplinkText)) {
            append("> ${line.label.padEnd(24)} ")
        }
        withStyle(SpanStyle(color = resultColor)) {
            append(resultText)
        }
    }

    Text(text = annotated, style = TelemetryBody, modifier = Modifier.padding(vertical = 1.dp))
}
