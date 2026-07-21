package com.uplink.di

import android.content.Context
import androidx.room.Room
import com.uplink.data.database.CacheDao
import com.uplink.data.database.DebugLogDao
import com.uplink.data.database.HistoryDao
import com.uplink.data.database.PreferencesDao
import com.uplink.data.database.SignalDao
import com.uplink.data.database.UplinkDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "uplink.db"

    @Provides
    @Singleton
    fun provideUplinkDatabase(
        @ApplicationContext context: Context
    ): UplinkDatabase = Room.databaseBuilder(
        context,
        UplinkDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideSignalDao(database: UplinkDatabase): SignalDao = database.signalDao()

    @Provides
    @Singleton
    fun provideHistoryDao(database: UplinkDatabase): HistoryDao = database.historyDao()

    @Provides
    @Singleton
    fun provideCacheDao(database: UplinkDatabase): CacheDao = database.cacheDao()

    @Provides
    @Singleton
    fun provideDebugLogDao(database: UplinkDatabase): DebugLogDao = database.debugLogDao()

    @Provides
    @Singleton
    fun providePreferencesDao(database: UplinkDatabase): PreferencesDao = database.preferencesDao()
}
