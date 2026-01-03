package com.indieradio.domain.repository

import com.indieradio.domain.model.Station
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for favorite stations operations
 */
interface FavoriteRepository {

    /**
     * Get all favorite stations as a Flow
     */
    fun getAllFavorites(): Flow<List<Station>>

    /**
     * Check if a station is favorited
     */
    fun isFavorite(stationUuid: String): Flow<Boolean>

    /**
     * Add a station to favorites
     */
    suspend fun addFavorite(station: Station)

    /**
     * Remove a station from favorites
     */
    suspend fun removeFavorite(stationUuid: String)

    /**
     * Toggle favorite status
     * Returns true if added, false if removed
     */
    suspend fun toggleFavorite(station: Station): Boolean

    /**
     * Get favorites count
     */
    fun getFavoritesCount(): Flow<Int>

    /**
     * Clear all favorites
     */
    suspend fun clearAllFavorites()
}
