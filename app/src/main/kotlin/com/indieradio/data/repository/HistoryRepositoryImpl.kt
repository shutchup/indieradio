package com.indieradio.data.repository

import com.indieradio.data.local.dao.HistoryDao
import com.indieradio.data.local.mapper.toHistoryEntity
import com.indieradio.data.local.mapper.toStation
import com.indieradio.domain.model.Station
import com.indieradio.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of HistoryRepository using Room database
 */
@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getRecentlyPlayed(): Flow<List<Station>> {
        return historyDao.getRecentlyPlayed().map { history ->
            history.map { it.toStation() }
        }
    }

    override suspend fun addToHistory(station: Station) {
        // Add to history
        historyDao.addToHistory(station.toHistoryEntity())

        // Trim to last 50 entries
        historyDao.trimHistory()
    }

    override suspend fun getLastPlayed(): Station? {
        return historyDao.getLastPlayed()?.toStation()
    }

    override suspend fun clearAllHistory() {
        historyDao.clearAllHistory()
    }
}
