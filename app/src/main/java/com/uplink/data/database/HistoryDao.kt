package com.uplink.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.uplink.data.database.entities.HistoryEntity

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY playedAt DESC")
    suspend fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM history WHERE signalId = :signalId ORDER BY playedAt DESC LIMIT 1")
    suspend fun getLatestForSignal(signalId: String): HistoryEntity?

    @Insert
    suspend fun insert(entry: HistoryEntity)
}
