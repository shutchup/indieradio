package com.indieradio.data.remote.api

import com.indieradio.data.remote.dto.StationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Radio Browser API interface
 * Base URL: https://all.api.radio-browser.info/
 * Documentation: https://docs.radio-browser.info/
 */
interface RadioBrowserApi {

    /**
     * Get stations by country code
     * @param countryCode ISO 3166-1 alpha-2 country code (e.g., "US", "IN", "GB")
     */
    @GET("json/stations/bycountrycodeexact/{countrycode}")
    suspend fun getStationsByCountryCode(
        @Path("countrycode") countryCode: String,
        @Query("limit") limit: Int = 100,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true
    ): Response<List<StationDto>>

    /**
     * Search stations by name
     * @param name Station name to search for
     */
    @GET("json/stations/byname/{name}")
    suspend fun searchStationsByName(
        @Path("name") name: String,
        @Query("limit") limit: Int = 100,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true
    ): Response<List<StationDto>>

    /**
     * Search stations with advanced options
     */
    @GET("json/stations/search")
    suspend fun searchStations(
        @Query("name") name: String? = null,
        @Query("country") country: String? = null,
        @Query("countrycode") countryCode: String? = null,
        @Query("state") state: String? = null,
        @Query("language") language: String? = null,
        @Query("tag") tag: String? = null,
        @Query("tagExact") tagExact: Boolean = false,
        @Query("limit") limit: Int = 100,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true
    ): Response<List<StationDto>>

    /**
     * Get stations by tag/genre
     * @param tag Genre/tag to filter by (e.g., "jazz", "rock", "news")
     */
    @GET("json/stations/bytag/{tag}")
    suspend fun getStationsByTag(
        @Path("tag") tag: String,
        @Query("limit") limit: Int = 100,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true
    ): Response<List<StationDto>>

    /**
     * Get top voted stations
     */
    @GET("json/stations/topvote/{count}")
    suspend fun getTopVotedStations(
        @Path("count") count: Int = 100
    ): Response<List<StationDto>>

    /**
     * Get recently clicked/popular stations
     */
    @GET("json/stations/topclick/{count}")
    suspend fun getTopClickedStations(
        @Path("count") count: Int = 100
    ): Response<List<StationDto>>

    /**
     * Get stations by language
     * @param language Language name (e.g., "english", "hindi", "spanish")
     */
    @GET("json/stations/bylanguage/{language}")
    suspend fun getStationsByLanguage(
        @Path("language") language: String,
        @Query("limit") limit: Int = 100,
        @Query("order") order: String = "votes",
        @Query("reverse") reverse: Boolean = true
    ): Response<List<StationDto>>

    /**
     * Increment click count for a station (analytics)
     * Call this when user starts playing a station
     */
    @GET("json/url/{stationuuid}")
    suspend fun clickStation(
        @Path("stationuuid") stationUuid: String
    ): Response<StationDto>
}
