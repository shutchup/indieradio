package com.indieradio.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Radio Browser API station response
 */
data class StationDto(
    @SerializedName("stationuuid")
    val stationUuid: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("url_resolved")
    val urlResolved: String,

    @SerializedName("homepage")
    val homepage: String?,

    @SerializedName("favicon")
    val favicon: String?,

    @SerializedName("country")
    val country: String,

    @SerializedName("countrycode")
    val countryCode: String,

    @SerializedName("state")
    val state: String?,

    @SerializedName("language")
    val language: String?,

    @SerializedName("languagecodes")
    val languageCodes: String?,

    @SerializedName("tags")
    val tags: String?,

    @SerializedName("codec")
    val codec: String?,

    @SerializedName("bitrate")
    val bitrate: Int?,

    @SerializedName("votes")
    val votes: Int,

    @SerializedName("clickcount")
    val clickCount: Int,

    @SerializedName("clicktrend")
    val clickTrend: Int,

    @SerializedName("geo_lat")
    val geoLat: Double?,

    @SerializedName("geo_long")
    val geoLong: Double?,

    @SerializedName("lastcheckok")
    val lastCheckOk: Int, // 1 = true, 0 = false

    @SerializedName("lastchecktime")
    val lastCheckTime: String?,

    @SerializedName("clicktimestamp")
    val clickTimestamp: String?,

    @SerializedName("changeuuid")
    val changeUuid: String?
)
