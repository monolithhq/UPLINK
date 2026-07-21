package com.uplink.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Room storage shape for a discovered Signal. Mirrors domain.model.Signal
// but stays a separate type — the database layer should not force the
// domain layer to carry Room annotations. Mapping happens in
// data/mapper (see SignalMapper.kt).
@Entity(tableName = "signals")
data class SignalEntity(
    @PrimaryKey val id: String,
    val sourcePlatform: String,
    val sourceId: String,
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val channel: String,
    val durationSeconds: Long?,
    val streamUrl: String?,
    val status: String,
    val discoveredAt: Long,
    val viewCount: Long?
)
