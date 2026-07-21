package com.uplink.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val UplinkColorScheme = darkColorScheme(
    background = UplinkVoid,
    surface = UplinkPanel,
    surfaceVariant = UplinkPanel2,
    outline = UplinkLine,
    primary = UplinkSignal,
    onPrimary = UplinkVoid,
    secondary = UplinkSignalDim,
    error = UplinkAlert,
    tertiary = UplinkAmber,
    onBackground = UplinkText,
    onSurface = UplinkText,
    onSurfaceVariant = UplinkTextDim
)

// Dark-only by design — UPLINK is a low-light terminal environment.
// No light theme branch; system theme setting is intentionally ignored.
@Composable
fun UplinkTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = UplinkColorScheme,
        typography = UplinkTypography,
        content = content
    )
}
