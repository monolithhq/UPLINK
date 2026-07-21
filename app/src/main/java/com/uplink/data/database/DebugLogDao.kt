package com.uplink.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.uplink.data.database.entities.DebugLogEntity

// Schema-ready for Commit 006. Nothing calls insert() yet.
@Dao
interface DebugLogDao {

    @Query("SELECT * FROM debug_logs ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 200): List<DebugLogEntity>

    @Insert
    suspend fun insert(entry: DebugLogEntity)
}
