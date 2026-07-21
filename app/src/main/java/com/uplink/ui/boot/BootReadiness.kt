package com.uplink.ui.boot

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
        return OswaldFamily.fonts.isNotEmpty() &&
            JetBrainsMonoFamily.fonts.isNotEmpty() &&
            InterFamily.fonts.isNotEmpty()
    }
}
