package com.indieradio.data.local.mapper

import com.indieradio.data.local.entity.FavoriteEntity
import com.indieradio.data.local.entity.HistoryEntity
import com.indieradio.domain.model.Station

/**
 * Mapper functions for converting between domain models and database entities
 */

/**
 * Convert Station domain model to FavoriteEntity
 */
fun Station.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        stationUuid = stationUuid,
        name = name,
        url = url,
        urlResolved = urlResolved,
        country = country,
        countryCode = countryCode,
        state = state,
        language = language,
        tags = tags,
        codec = codec,
        bitrate = bitrate,
        homepage = homepage,
        favicon = favicon,
        votes = votes
    )
}

/**
 * Convert FavoriteEntity to Station domain model
 */
fun FavoriteEntity.toStation(): Station {
    return Station(
        stationUuid = stationUuid,
        name = name,
        url = url,
        urlResolved = urlResolved,
        country = country,
        countryCode = countryCode,
        state = state,
        language = language,
        tags = tags,
        codec = codec,
        bitrate = bitrate,
        homepage = homepage,
        favicon = favicon,
        votes = votes
    )
}

/**
 * Convert Station domain model to HistoryEntity
 */
fun Station.toHistoryEntity(): HistoryEntity {
    return HistoryEntity(
        stationUuid = stationUuid,
        name = name,
        url = url,
        urlResolved = urlResolved,
        country = country,
        countryCode = countryCode,
        state = state,
        language = language,
        tags = tags,
        codec = codec,
        bitrate = bitrate,
        homepage = homepage,
        favicon = favicon,
        votes = votes
    )
}

/**
 * Convert HistoryEntity to Station domain model
 */
fun HistoryEntity.toStation(): Station {
    return Station(
        stationUuid = stationUuid,
        name = name,
        url = url,
        urlResolved = urlResolved,
        country = country,
        countryCode = countryCode,
        state = state,
        language = language,
        tags = tags,
        codec = codec,
        bitrate = bitrate,
        homepage = homepage,
        favicon = favicon,
        votes = votes
    )
}
