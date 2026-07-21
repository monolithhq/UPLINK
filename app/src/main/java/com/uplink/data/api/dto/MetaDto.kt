package com.uplink.data.api.dto

import kotlinx.serialization.Serializable

// GET /{config}/meta/{type}/{id}.json — enrichment for a single item.
// Maps onto an existing Signal, filling in fields the catalog entry
// didn't already have (see SignalMapper.enrichedWith).
@Serializable
data class MetaDto(
    val meta: MetaItemDto
)

@Serializable
data class MetaItemDto(
    val id: String,
    val type: String? = null,
    val name: String,
    val poster: String? = null,
    val description: String? = null,
    val runtime: String? = null,
    val releaseInfo: String? = null
)
