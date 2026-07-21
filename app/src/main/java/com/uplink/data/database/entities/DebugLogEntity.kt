package com.uplink.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Persisted debug/diagnostic events. Table exists in Commit 004 so
// the schema is stable ahead of time, but nothing writes to it yet —
// DebugConsoleScreen still reads a mock in-memory log until Commit 006
// wires a real BootEventFlow/DebugEventRepository into this table.
@Entity(tableName = "debug_logs")
data class DebugLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val event: String,
    val status: String,
    val message: String?
)
