package com.uplink.data.api.dto

import kotlinx.serialization.Serializable

// GET /{config}/manifest.json — connection/identity check only.
// Fields kept minimal; only what UPLINK actually needs to confirm
// a live, correctly-configured YouTubio instance.
@Serializable
data class ManifestDto(
    val id: String? = null,
    val name: String? = null,
    val version: String? = null
)
