package com.uplink.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.uplink.ui.theme.UplinkSignal

// Single-shot CRT power-on line, expanding vertically over ~120ms.
// Renders as an overlay ON TOP of whatever's already composed behind
// it (boot log, blueprint grid, etc.) rather than gating their
// existence — this avoids a blank-screen gap on fast devices where
// CRT finishes before content would otherwise be ready to show.
//
// The content behind this is always present in composition; this
// composable's only job is to visually reveal it via an expanding
// line + fade, then report completion once via onComplete.
@Composable
fun CRTStartupEffect(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var started by remember { mutableStateOf(false) }
    var completed by remember { mutableStateOf(false) }

    val progress by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
        label = "crt_expand",
        finishedListener = {
            if (!completed) {
                completed = true
                onComplete()
            }
        }
    )

    LaunchedEffect(Unit) {
        started = true
    }

    // Mask: covers everything except a vertically-centered band whose
    // height grows with progress, revealing content beneath rather
    // than replacing it.
    Canvas(modifier = modifier) {
        val revealHeight = size.height * progress
        val maskHeight = (size.height - revealHeight) / 2f

        if (maskHeight > 0f) {
            drawRect(
                color = androidx.compose.ui.graphics.Color.Black,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, maskHeight)
            )
            drawRect(
                color = androidx.compose.ui.graphics.Color.Black,
                topLeft = Offset(0f, size.height - maskHeight),
                size = Size(size.width, maskHeight)
            )
        }

        // The bright leading edge line, sitting at the current
        // reveal boundary while the mask is still shrinking.
        if (progress < 1f) {
            drawRect(
                color = UplinkSignal,
                topLeft = Offset(0f, maskHeight),
                size = Size(size.width, 2f)
            )
            drawRect(
                color = UplinkSignal,
                topLeft = Offset(0f, size.height - maskHeight - 2f),
                size = Size(size.width, 2f)
            )
        }
    }
}
