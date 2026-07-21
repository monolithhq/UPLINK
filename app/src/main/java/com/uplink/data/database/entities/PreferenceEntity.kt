package com.uplink.data.database.entities

import androidx.room.Entity

// Generic key/value store, per Commit 004 decision — avoids a schema
// migration every time a new setting is introduced. `key` is backtick-
// quoted in DAO queries since it's a SQL reserved word.
@Entity(tableName = "preferences", primaryKeys = ["key"])
data class PreferenceEntity(
    val key: String,
    val value: String
)
