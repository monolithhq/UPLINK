package com.uplink.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val UplinkVoid = Color(0xFF08090C)
private val UplinkPanel = Color(0xFF111319)
private val UplinkSignal = Color(0xFF0DF2C4)
private val UplinkAlert = Color(0xFFFF3864)
private val UplinkText = Color(0xFFF2F4F2)

private val UplinkColorScheme = darkColorScheme(
    background = UplinkVoid,
    surface = UplinkPanel,
    primary = UplinkSignal,
    error = UplinkAlert,
    onBackground = UplinkText,
    onSurface = UplinkText
)

@Composable
fun UplinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = UplinkColorScheme,
        content = content
    )
}
