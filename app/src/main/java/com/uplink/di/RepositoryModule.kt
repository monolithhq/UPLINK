package com.uplink.di

import com.uplink.data.api.YouTubioClient
import com.uplink.data.database.CacheDao
import com.uplink.data.database.PreferencesDao
import com.uplink.data.database.SignalDao
import com.uplink.data.repository.YouTubioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideYouTubioRepository(
        client: YouTubioClient,
        signalDao: SignalDao,
        cacheDao: CacheDao,
        preferencesDao: PreferencesDao
    ): YouTubioRepository = YouTubioRepository(
        client = client,
        signalDao = signalDao,
        cacheDao = cacheDao,
        preferencesDao = preferencesDao
    )
}
