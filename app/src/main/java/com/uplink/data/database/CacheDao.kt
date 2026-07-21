package com.uplink.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uplink.data.database.entities.CacheEntity

@Dao
interface CacheDao {

    @Query("SELECT * FROM cache WHERE `key` = :key")
    suspend fun get(key: String): CacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun put(entry: CacheEntity)

    @Query("DELETE FROM cache WHERE `key` = :key")
    suspend fun delete(key: String)

    @Query("DELETE FROM cache WHERE expiresAt < :nowMillis")
    suspend fun deleteExpired(nowMillis: Long)
}
