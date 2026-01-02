package com.indieradio.data.remote.mapper

import com.indieradio.data.remote.dto.StationDto
import com.indieradio.domain.model.Station

/**
 * Mapper to convert StationDto to domain Station model
 */
object StationMapper {

    fun toDomain(dto: StationDto): Station {
        return Station(
            stationUuid = dto.stationUuid,
            name = dto.name,
            url = dto.url,
            urlResolved = dto.urlResolved,
            homepage = dto.homepage,
            favicon = dto.favicon,
            country = dto.country,
            countryCode = dto.countryCode,
            state = dto.state,
            language = dto.language,
            languageCodes = dto.languageCodes,
            tags = dto.tags,
            codec = dto.codec,
            bitrate = dto.bitrate,
            votes = dto.votes,
            clickCount = dto.clickCount,
            clickTrend = dto.clickTrend,
            geoLat = dto.geoLat,
            geoLong = dto.geoLong,
            lastCheckOk = dto.lastCheckOk == 1,
            lastCheckTime = dto.lastCheckTime,
            clickTimestamp = dto.clickTimestamp,
            changeUuid = dto.changeUuid
        )
    }

    fun toDomainList(dtos: List<StationDto>): List<Station> {
        return dtos.map { toDomain(it) }
    }
}
