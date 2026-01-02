package com.indieradio.domain.model

/**
 * Domain model representing a radio station
 */
data class Station(
    val stationUuid: String,
    val name: String,
    val url: String,
    val urlResolved: String, // Pre-resolved stream URL (use this for playback)
    val homepage: String?,
    val favicon: String?,
    val country: String,
    val countryCode: String,
    val state: String?,
    val language: String?,
    val languageCodes: String?,
    val tags: String?,
    val codec: String?,
    val bitrate: Int?,
    val votes: Int,
    val clickCount: Int,
    val clickTrend: Int,
    val geoLat: Double?,
    val geoLong: Double?,
    val lastCheckOk: Boolean,
    val lastCheckTime: String?,
    val clickTimestamp: String?,
    val changeUuid: String?
) {
    /**
     * Get a user-friendly bitrate string
     */
    fun getBitrateString(): String {
        return if (bitrate != null && bitrate > 0) {
            "$bitrate kbps"
        } else {
            "Unknown"
        }
    }

    /**
     * Get list of tags/genres
     */
    fun getTagList(): List<String> {
        return tags?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?: emptyList()
    }

    /**
     * Check if station has valid stream URL
     */
    fun hasValidStreamUrl(): Boolean {
        return urlResolved.isNotBlank() && urlResolved.startsWith("http")
    }
}
