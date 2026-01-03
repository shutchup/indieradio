package com.indieradio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for favorite stations
 */
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val stationUuid: String,
    val name: String,
    val url: String,
    val urlResolved: String,
    val country: String,
    val countryCode: String,
    val state: String,
    val language: String,
    val tags: String,
    val codec: String?,
    val bitrate: Int?,
    val homepage: String,
    val favicon: String,
    val votes: Int,
    val addedAt: Long = System.currentTimeMillis()
)
