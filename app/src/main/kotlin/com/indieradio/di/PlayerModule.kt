package com.indieradio.di

import android.content.Context
import com.indieradio.domain.repository.HistoryRepository
import com.indieradio.player.RadioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideRadioPlayer(
        @ApplicationContext context: Context,
        historyRepository: HistoryRepository
    ): RadioPlayer {
        return RadioPlayer(context, historyRepository)
    }
}
