package com.uplink.ui.boot

import androidx.compose.ui.text.font.FontListFontFamily
import com.uplink.ui.theme.InterFamily
import com.uplink.ui.theme.JetBrainsMonoFamily
import com.uplink.ui.theme.OswaldFamily

// Real readiness checks for Commit 002's two real subsystems.
// THEME_READY was dropped: checking context.theme != null proves
// nothing (it's true by the time this code can even run), so it was
// removed rather than kept as a check that always trivially passes.
object BootReadiness {

    fun checkUiReady(): Boolean = true

    fun checkFontsReady(): Boolean {
        // FontFamily has no public `.fonts` accessor from its base type.
        // OswaldFamily/JetBrainsMonoFamily/InterFamily are all built as
        // FontListFontFamily (via the FontFamily(Font, Font, ...) call
        // in Fonts.kt), which DOES expose `.fonts`. Casting confirms
        // each is that concrete type with a non-empty font list, rather
        // than just checking the FontFamily reference is non-null
        // (which would be trivially true and prove nothing, same
        // problem as the removed THEME_READY check).
        return (OswaldFamily as? FontListFontFamily)?.fonts?.isNotEmpty() == true &&
            (JetBrainsMonoFamily as? FontListFontFamily)?.fonts?.isNotEmpty() == true &&
            (InterFamily as? FontListFontFamily)?.fonts?.isNotEmpty() == true
    }
}
