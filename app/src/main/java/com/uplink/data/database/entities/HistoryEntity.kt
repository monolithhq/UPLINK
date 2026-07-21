package com.uplink.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Playback history — one row per playback session, not per signal.
// A signal can be played multiple times, producing multiple rows.
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val signalId: String,
    val playedAt: Long,
    val positionSeconds: Long
)
