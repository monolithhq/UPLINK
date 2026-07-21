package com.uplink.data.api.dto

import kotlinx.serialization.Serializable

// GET /{config}/catalog/{type}/{id}.json — list of discoverable items.
// Maps to a list of DISCOVERED Signals (see SignalMapper.kt).
@Serializable
data class CatalogDto(
    val metas: List<CatalogItemDto> = emptyList()
)

@Serializable
data class CatalogItemDto(
    val id: String,
    val type: String? = null,
    val name: String,
    val poster: String? = null,
    val description: String? = null,
    val releaseInfo: String? = null
)
