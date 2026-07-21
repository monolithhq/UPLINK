package com.uplink.data.api.dto

import kotlinx.serialization.Serializable

// GET /{config}/stream/{type}/{id}.json — playback URL resolution.
// A single catalog id can resolve to multiple stream options (e.g.
// different qualities/sources); UPLINK takes the first for Commit 004,
// since Player itself is still a placeholder until Commit 005.
@Serializable
data class StreamDto(
    val streams: List<StreamItemDto> = emptyList()
)

@Serializable
data class StreamItemDto(
    val url: String? = null,
    val title: String? = null
)
