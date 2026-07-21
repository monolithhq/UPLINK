package com.uplink.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// Temporary API response cache, keyed by a caller-defined string
// (e.g. a request URL or catalog key). expiresAt is an epoch-millis
// timestamp; callers are responsible for checking it before trusting
// a cached payload — this entity has no built-in eviction.
@Entity(tableName = "cache")
data class CacheEntity(
    @PrimaryKey val key: String,
    val payload: String,
    val expiresAt: Long
)
