package com.uplink.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

// Atmospheric terminal layer. Per UPLINK_DESIGN_SYSTEM.md: "Always
// active. Very low opacity. Does not block interaction. Does not
// reduce readability." Rendered above content but ignores all touch
// input (Canvas draws only, no pointer handling attached).
@Composable
fun ScanlineOverlay(
    modifier: Modifier = Modifier,
    lineSpacingDp: Float = 3f,
    baseAlpha: Float = 0.035f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanline_drift")
    val drift: Float by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = lineSpacingDp * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanline_drift_value"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val spacingPx: Float = lineSpacingDp * density
        val driftPx: Float = drift * density
        val lineColor = Color.Black.copy(alpha = baseAlpha)

        var y: Float = -spacingPx + (driftPx.mod(spacingPx))
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
