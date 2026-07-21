package com.uplink.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.uplink.ui.theme.UplinkLine

// Fixed technical grid, low contrast, sits behind all screen content.
// Per UPLINK_DESIGN_SYSTEM.md: "Low contrast. Behind content. Never
// compete with information." Applied globally, not per-screen.
@Composable
fun BlueprintBackground(
    modifier: Modifier = Modifier,
    spacingDp: Float = 32f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val spacingPx = spacingDp * density
        val lineColor = UplinkLine.copy(alpha = 0.12f)

        var x = 0f
        while (x < size.width) {
            drawLine(
                color = lineColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = 1f
            )
            x += spacingPx
        }

        var y = 0f
        while (y < size.height) {
            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = 1f
            )
            y += spacingPx
        }
    }
}
