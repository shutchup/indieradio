package com.indieradio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity for recently played stations
 * Stores last 50 stations with play timestamp
 */
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stationUuid: String,
    val name: String,
    val url: String,
    val urlResolved: String,
    val country: String,
    val countryCode: String,
    val state: String?,
    val language: String?,
    val tags: String?,
    val codec: String?,
    val bitrate: Int?,
    val homepage: String?,
    val favicon: String?,
    val votes: Int,
    val playedAt: Long = System.currentTimeMillis()
)
