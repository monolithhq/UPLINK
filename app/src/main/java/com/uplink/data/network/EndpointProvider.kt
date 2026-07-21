package com.uplink.data.network

import javax.inject.Inject
import javax.inject.Singleton

// Single source of truth for UPLINK's user-configured YouTubio base URL.
// Settings screen calls updateBaseUrl() when the user saves a new
// endpoint; EndpointInterceptor reads currentBaseUrl() per-request.
// Neither Retrofit nor YouTubioRepository know this class exists.
//
// UNCONFIGURED_BASE_URL is a syntactically valid but never-real base URL,
// used both as this provider's initial value and as the placeholder
// Retrofit.baseUrl() in NetworkModule (Retrofit requires a valid URL at
// construction time even though every real request gets its host
// rewritten by EndpointInterceptor before it leaves the device).
@Singleton
class EndpointProvider @Inject constructor() {

    companion object {
        const val UNCONFIGURED_BASE_URL = "http://unconfigured.invalid/"
    }

    @Volatile
    private var baseUrl: String = UNCONFIGURED_BASE_URL

    fun currentBaseUrl(): String = baseUrl

    // Trims and ensures a trailing slash so callers (Settings screen,
    // etc.) don't have to think about formatting. Blank input is ignored
    // rather than silently reverting to UNCONFIGURED_BASE_URL, since a
    // failed/empty save shouldn't clobber a previously-working URL.
    fun updateBaseUrl(url: String) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return
        baseUrl = if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }
}
