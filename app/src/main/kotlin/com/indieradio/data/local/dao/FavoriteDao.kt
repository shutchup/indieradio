package com.indieradio.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.indieradio.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for favorite stations operations
 */
@Dao
interface FavoriteDao {

    /**
     * Get all favorite stations ordered by most recently added
     */
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    /**
     * Check if a station is favorited
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE stationUuid = :stationUuid)")
    fun isFavorite(stationUuid: String): Flow<Boolean>

    /**
     * Get a specific favorite by station UUID
     */
    @Query("SELECT * FROM favorites WHERE stationUuid = :stationUuid")
    suspend fun getFavorite(stationUuid: String): FavoriteEntity?

    /**
     * Add a station to favorites
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    /**
     * Remove a station from favorites
     */
    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    /**
     * Remove a station from favorites by UUID
     */
    @Query("DELETE FROM favorites WHERE stationUuid = :stationUuid")
    suspend fun removeFavoriteByUuid(stationUuid: String)

    /**
     * Get count of favorites
     */
    @Query("SELECT COUNT(*) FROM favorites")
    fun getFavoritesCount(): Flow<Int>

    /**
     * Clear all favorites
     */
    @Query("DELETE FROM favorites")
    suspend fun clearAllFavorites()
}
