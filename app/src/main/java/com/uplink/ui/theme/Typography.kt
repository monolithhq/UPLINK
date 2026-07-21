package com.uplink.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Oswald — screen titles, navigation labels, section headers, important actions
val TerminalTitleLarge = TextStyle(
    fontFamily = OswaldFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 28.sp,
    letterSpacing = 0.5.sp
)

val TerminalTitleMedium = TextStyle(
    fontFamily = OswaldFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = 0.5.sp
)

val TerminalLabel = TextStyle(
    fontFamily = OswaldFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = 1.sp
)

// JetBrains Mono — telemetry, timestamps, status, IDs, debug logs
val TelemetryBody = TextStyle(
    fontFamily = JetBrainsMonoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    letterSpacing = 0.2.sp
)

val TelemetryStatus = TextStyle(
    fontFamily = JetBrainsMonoFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = 0.5.sp
)

val TelemetryCaption = TextStyle(
    fontFamily = JetBrainsMonoFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    letterSpacing = 0.3.sp
)

// Inter — descriptions, long-form human-readable content
val ContentBody = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp
)

val ContentBodySmall = TextStyle(
    fontFamily = InterFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 17.sp
)

// Material3 Typography mapping — used where Compose components expect it
val UplinkTypography = Typography(
    headlineLarge = TerminalTitleLarge,
    headlineMedium = TerminalTitleMedium,
    labelLarge = TerminalLabel,
    bodyLarge = ContentBody,
    bodySmall = ContentBodySmall,
    bodyMedium = TelemetryBody
)
