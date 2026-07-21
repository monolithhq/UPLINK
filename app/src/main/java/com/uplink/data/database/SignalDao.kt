package com.uplink.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uplink.data.database.entities.SignalEntity

@Dao
interface SignalDao {

    @Query("SELECT * FROM signals ORDER BY discoveredAt DESC")
    suspend fun getAll(): List<SignalEntity>

    @Query("SELECT * FROM signals WHERE id = :id")
    suspend fun getById(id: String): SignalEntity?

    @Query(
        "SELECT * FROM signals WHERE title LIKE '%' || :query || '%' " +
            "OR channel LIKE '%' || :query || '%'"
    )
    suspend fun search(query: String): List<SignalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(signal: SignalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(signals: List<SignalEntity>)

    @Query("DELETE FROM signals WHERE id = :id")
    suspend fun delete(id: String)
}
