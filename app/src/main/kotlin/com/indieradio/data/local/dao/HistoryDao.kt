package com.indieradio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.indieradio.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for recently played stations
 */
@Dao
interface HistoryDao {

    /**
     * Get recently played stations ordered by most recent
     * Limited to last 50 entries
     */
    @Query("SELECT * FROM history ORDER BY playedAt DESC LIMIT 50")
    fun getRecentlyPlayed(): Flow<List<HistoryEntity>>

    /**
     * Add a station to history
     * If limit exceeded, oldest entries will be deleted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(history: HistoryEntity)

    /**
     * Get history count
     */
    @Query("SELECT COUNT(*) FROM history")
    suspend fun getHistoryCount(): Int

    /**
     * Delete oldest entries to maintain max 50 limit
     */
    @Query("DELETE FROM history WHERE id NOT IN (SELECT id FROM history ORDER BY playedAt DESC LIMIT 50)")
    suspend fun trimHistory()

    /**
     * Clear all history
     */
    @Query("DELETE FROM history")
    suspend fun clearAllHistory()

    /**
     * Get last played station
     */
    @Query("SELECT * FROM history ORDER BY playedAt DESC LIMIT 1")
    suspend fun getLastPlayed(): HistoryEntity?
}
