package com.indieradio.data.repository

import com.indieradio.data.remote.api.RadioBrowserApi
import com.indieradio.data.remote.mapper.StationMapper
import com.indieradio.domain.model.Station
import com.indieradio.domain.repository.StationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of StationRepository
 */
class StationRepositoryImpl @Inject constructor(
    private val api: RadioBrowserApi
) : StationRepository {

    override suspend fun getStationsByCountryCode(
        countryCode: String,
        limit: Int
    ): Result<List<Station>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getStationsByCountryCode(countryCode, limit)
            if (response.isSuccessful && response.body() != null) {
                val stations = StationMapper.toDomainList(response.body()!!)
                    .filter { it.hasValidStreamUrl() } // Filter out invalid stations
                Result.success(stations)
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchStationsByName(
        name: String,
        limit: Int
    ): Result<List<Station>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchStationsByName(name, limit)
            if (response.isSuccessful && response.body() != null) {
                val stations = StationMapper.toDomainList(response.body()!!)
                    .filter { it.hasValidStreamUrl() }
                Result.success(stations)
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchStations(
        name: String?,
        country: String?,
        countryCode: String?,
        tag: String?,
        language: String?,
        limit: Int
    ): Result<List<Station>> = withContext(Dispatchers.IO) {
        try {
            val response = api.searchStations(
                name = name,
                country = country,
                countryCode = countryCode,
                tag = tag,
                language = language,
                limit = limit
            )
            if (response.isSuccessful && response.body() != null) {
                val stations = StationMapper.toDomainList(response.body()!!)
                    .filter { it.hasValidStreamUrl() }
                Result.success(stations)
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStationsByTag(
        tag: String,
        limit: Int
    ): Result<List<Station>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getStationsByTag(tag, limit)
            if (response.isSuccessful && response.body() != null) {
                val stations = StationMapper.toDomainList(response.body()!!)
                    .filter { it.hasValidStreamUrl() }
                Result.success(stations)
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopVotedStations(count: Int): Result<List<Station>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getTopVotedStations(count)
                if (response.isSuccessful && response.body() != null) {
                    val stations = StationMapper.toDomainList(response.body()!!)
                        .filter { it.hasValidStreamUrl() }
                    Result.success(stations)
                } else {
                    Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getTopClickedStations(count: Int): Result<List<Station>> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getTopClickedStations(count)
                if (response.isSuccessful && response.body() != null) {
                    val stations = StationMapper.toDomainList(response.body()!!)
                        .filter { it.hasValidStreamUrl() }
                    Result.success(stations)
                } else {
                    Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getStationsByLanguage(
        language: String,
        limit: Int
    ): Result<List<Station>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getStationsByLanguage(language, limit)
            if (response.isSuccessful && response.body() != null) {
                val stations = StationMapper.toDomainList(response.body()!!)
                    .filter { it.hasValidStreamUrl() }
                Result.success(stations)
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clickStation(stationUuid: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.clickStation(stationUuid)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                // Don't fail if click tracking fails - it's not critical
                Result.success(Unit)
            }
        }
}
