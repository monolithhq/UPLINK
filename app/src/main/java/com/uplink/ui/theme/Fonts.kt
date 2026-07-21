package com.uplink.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.uplink.R

// All three families ship as single variable-font files (one .ttf covers
// the full weight range). Individual FontWeight values below select the
// rendered weight from that one file — no separate Regular/Medium/Bold
// font resources are needed.

val OswaldFamily = FontFamily(
    Font(R.font.oswald_variable, FontWeight.Normal),
    Font(R.font.oswald_variable, FontWeight.Medium),
    Font(R.font.oswald_variable, FontWeight.SemiBold),
    Font(R.font.oswald_variable, FontWeight.Bold)
)

val JetBrainsMonoFamily = FontFamily(
    Font(R.font.jetbrains_mono_variable, FontWeight.Normal),
    Font(R.font.jetbrains_mono_variable, FontWeight.Medium),
    Font(R.font.jetbrains_mono_variable, FontWeight.Bold)
)

val InterFamily = FontFamily(
    Font(R.font.inter_variable, FontWeight.Normal),
    Font(R.font.inter_variable, FontWeight.Medium)
)
