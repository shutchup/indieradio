package com.indieradio.di

import android.content.Context
import com.indieradio.data.repository.FavoriteRepositoryImpl
import com.indieradio.data.repository.HistoryRepositoryImpl
import com.indieradio.data.repository.SkinRepository
import com.indieradio.data.repository.StationRepositoryImpl
import com.indieradio.domain.repository.FavoriteRepository
import com.indieradio.domain.repository.HistoryRepository
import com.indieradio.domain.repository.StationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStationRepository(
        stationRepositoryImpl: StationRepositoryImpl
    ): StationRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository

    companion object {
        @Provides
        @Singleton
        fun provideSkinRepository(
            @ApplicationContext context: Context
        ): SkinRepository {
            return SkinRepository(context)
        }
    }
}
