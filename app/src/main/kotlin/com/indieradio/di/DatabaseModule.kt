package com.indieradio.di

import android.content.Context
import androidx.room.Room
import com.indieradio.data.local.IndieRadioDatabase
import com.indieradio.data.local.dao.FavoriteDao
import com.indieradio.data.local.dao.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing Room database and DAOs
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): IndieRadioDatabase {
        return Room.databaseBuilder(
            context,
            IndieRadioDatabase::class.java,
            IndieRadioDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development, recreate DB on schema changes
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(database: IndieRadioDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(database: IndieRadioDatabase): HistoryDao {
        return database.historyDao()
    }
}
