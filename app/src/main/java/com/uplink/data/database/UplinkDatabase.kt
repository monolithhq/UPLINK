package com.uplink.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uplink.data.database.entities.CacheEntity
import com.uplink.data.database.entities.DebugLogEntity
import com.uplink.data.database.entities.HistoryEntity
import com.uplink.data.database.entities.PreferenceEntity
import com.uplink.data.database.entities.SignalEntity

@Database(
    entities = [
        SignalEntity::class,
        HistoryEntity::class,
        CacheEntity::class,
        DebugLogEntity::class,
        PreferenceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class UplinkDatabase : RoomDatabase() {
    abstract fun signalDao(): SignalDao
    abstract fun historyDao(): HistoryDao
    abstract fun cacheDao(): CacheDao
    abstract fun debugLogDao(): DebugLogDao
    abstract fun preferencesDao(): PreferencesDao
}
