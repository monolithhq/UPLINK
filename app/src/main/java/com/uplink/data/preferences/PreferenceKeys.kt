package com.uplink.data.preferences

// Centralized preference key constants — no scattered string literals
// at call sites. Add new keys here as Settings grows (cache limits,
// playback preferences, etc. per the original Settings scope note).
object PreferenceKeys {
    const val YOUTUBIO_URL = "youtubio_url"
    const val YOUTUBIO_CONFIG = "youtubio_config"
}
