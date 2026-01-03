package com.indieradio.data.repository

import com.indieradio.data.local.dao.FavoriteDao
import com.indieradio.data.local.mapper.toFavoriteEntity
import com.indieradio.data.local.mapper.toStation
import com.indieradio.domain.model.Station
import com.indieradio.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of FavoriteRepository using Room database
 */
@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<Station>> {
        return favoriteDao.getAllFavorites().map { favorites ->
            favorites.map { it.toStation() }
        }
    }

    override fun isFavorite(stationUuid: String): Flow<Boolean> {
        return favoriteDao.isFavorite(stationUuid)
    }

    override suspend fun addFavorite(station: Station) {
        favoriteDao.addFavorite(station.toFavoriteEntity())
    }

    override suspend fun removeFavorite(stationUuid: String) {
        favoriteDao.removeFavoriteByUuid(stationUuid)
    }

    override suspend fun toggleFavorite(station: Station): Boolean {
        val isFavorited = favoriteDao.isFavorite(station.stationUuid).first()

        return if (isFavorited) {
            favoriteDao.removeFavoriteByUuid(station.stationUuid)
            false
        } else {
            favoriteDao.addFavorite(station.toFavoriteEntity())
            true
        }
    }

    override fun getFavoritesCount(): Flow<Int> {
        return favoriteDao.getFavoritesCount()
    }

    override suspend fun clearAllFavorites() {
        favoriteDao.clearAllFavorites()
    }
}
