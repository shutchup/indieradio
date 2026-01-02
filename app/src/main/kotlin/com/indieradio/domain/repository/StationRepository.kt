package com.indieradio.domain.repository

import com.indieradio.domain.model.Station
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for station data operations
 */
interface StationRepository {

    /**
     * Get stations by country code
     */
    suspend fun getStationsByCountryCode(
        countryCode: String,
        limit: Int = 100
    ): Result<List<Station>>

    /**
     * Search stations by name
     */
    suspend fun searchStationsByName(
        name: String,
        limit: Int = 100
    ): Result<List<Station>>

    /**
     * Advanced search with multiple filters
     */
    suspend fun searchStations(
        name: String? = null,
        country: String? = null,
        countryCode: String? = null,
        tag: String? = null,
        language: String? = null,
        limit: Int = 100
    ): Result<List<Station>>

    /**
     * Get stations by tag/genre
     */
    suspend fun getStationsByTag(
        tag: String,
        limit: Int = 100
    ): Result<List<Station>>

    /**
     * Get top voted stations
     */
    suspend fun getTopVotedStations(count: Int = 100): Result<List<Station>>

    /**
     * Get top clicked/popular stations
     */
    suspend fun getTopClickedStations(count: Int = 100): Result<List<Station>>

    /**
     * Get stations by language
     */
    suspend fun getStationsByLanguage(
        language: String,
        limit: Int = 100
    ): Result<List<Station>>

    /**
     * Notify API that a station was clicked/played
     */
    suspend fun clickStation(stationUuid: String): Result<Unit>
}
