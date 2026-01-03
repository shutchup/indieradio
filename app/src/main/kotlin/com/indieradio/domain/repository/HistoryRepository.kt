package com.indieradio.domain.repository

import com.indieradio.domain.model.Station
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for recently played stations
 */
interface HistoryRepository {

    /**
     * Get recently played stations (max 50)
     */
    fun getRecentlyPlayed(): Flow<List<Station>>

    /**
     * Add a station to history
     * Automatically trims to last 50 entries
     */
    suspend fun addToHistory(station: Station)

    /**
     * Get last played station
     */
    suspend fun getLastPlayed(): Station?

    /**
     * Clear all history
     */
    suspend fun clearAllHistory()
}
