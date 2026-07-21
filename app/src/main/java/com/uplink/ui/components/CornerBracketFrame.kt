package com.uplink.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.uplink.ui.theme.UplinkSignal

// UPLINK's focus indicator — replaces shadows/glow entirely.
// Per UPLINK_DESIGN_SYSTEM.md: appears on active content and during
// touch interaction; never permanently decorates inactive elements.
// Caller controls visibility (e.g. via `visible` param bound to a
// focus/press state) rather than this component owning that state.
@Composable
fun CornerBracketFrame(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    color: Color = UplinkSignal,
    bracketLengthDp: Float = 16f,
    strokeWidthDp: Float = 2f
) {
    if (!visible) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val len = bracketLengthDp * density
        val stroke = Stroke(width = strokeWidthDp * density)
        val w = size.width
        val h = size.height

        // Top-left
        drawLine(color, Offset(0f, 0f), Offset(len, 0f), stroke.width)
        drawLine(color, Offset(0f, 0f), Offset(0f, len), stroke.width)

        // Top-right
        drawLine(color, Offset(w, 0f), Offset(w - len, 0f), stroke.width)
        drawLine(color, Offset(w, 0f), Offset(w, len), stroke.width)

        // Bottom-left
        drawLine(color, Offset(0f, h), Offset(len, h), stroke.width)
        drawLine(color, Offset(0f, h), Offset(0f, h - len), stroke.width)

        // Bottom-right
        drawLine(color, Offset(w, h), Offset(w - len, h), stroke.width)
        drawLine(color, Offset(w, h), Offset(w, h - len), stroke.width)
    }
}
