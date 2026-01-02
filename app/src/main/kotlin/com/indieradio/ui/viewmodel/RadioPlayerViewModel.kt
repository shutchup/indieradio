package com.indieradio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieradio.domain.model.PlaybackState
import com.indieradio.domain.model.Station
import com.indieradio.domain.repository.StationRepository
import com.indieradio.player.RadioPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for radio player screen
 */
@HiltViewModel
class RadioPlayerViewModel @Inject constructor(
    private val radioPlayer: RadioPlayer,
    private val stationRepository: StationRepository
) : ViewModel() {

    // Playback state from RadioPlayer
    val playbackState: StateFlow<PlaybackState> = radioPlayer.playbackState

    // UI state for stations list
    private val _uiState = MutableStateFlow<StationListUiState>(StationListUiState.Loading)
    val uiState: StateFlow<StationListUiState> = _uiState.asStateFlow()

    init {
        // Load initial popular stations
        loadPopularStations()
    }

    /**
     * Play a radio station
     */
    fun playStation(station: Station) {
        viewModelScope.launch {
            // Notify API of click (for analytics)
            stationRepository.clickStation(station.stationUuid)
            // Start playback
            radioPlayer.playStation(station)
        }
    }

    /**
     * Pause playback
     */
    fun pause() {
        radioPlayer.pause()
    }

    /**
     * Resume playback
     */
    fun resume() {
        radioPlayer.resume()
    }

    /**
     * Stop playback
     */
    fun stop() {
        radioPlayer.stop()
    }

    /**
     * Search stations by name
     */
    fun searchStations(query: String) {
        if (query.isBlank()) {
            loadPopularStations()
            return
        }

        viewModelScope.launch {
            _uiState.value = StationListUiState.Loading
            stationRepository.searchStationsByName(query, limit = 50).fold(
                onSuccess = { stations ->
                    _uiState.value = if (stations.isEmpty()) {
                        StationListUiState.Empty
                    } else {
                        StationListUiState.Success(stations)
                    }
                },
                onFailure = { error ->
                    _uiState.value = StationListUiState.Error(
                        error.message ?: "Failed to search stations"
                    )
                }
            )
        }
    }

    /**
     * Load popular/top stations
     */
    fun loadPopularStations() {
        viewModelScope.launch {
            _uiState.value = StationListUiState.Loading
            stationRepository.getTopVotedStations(count = 50).fold(
                onSuccess = { stations ->
                    _uiState.value = StationListUiState.Success(stations)
                },
                onFailure = { error ->
                    _uiState.value = StationListUiState.Error(
                        error.message ?: "Failed to load stations"
                    )
                }
            )
        }
    }

    /**
     * Load stations by country code
     */
    fun loadStationsByCountry(countryCode: String) {
        viewModelScope.launch {
            _uiState.value = StationListUiState.Loading
            stationRepository.getStationsByCountryCode(countryCode, limit = 100).fold(
                onSuccess = { stations ->
                    _uiState.value = if (stations.isEmpty()) {
                        StationListUiState.Empty
                    } else {
                        StationListUiState.Success(stations)
                    }
                },
                onFailure = { error ->
                    _uiState.value = StationListUiState.Error(
                        error.message ?: "Failed to load local stations"
                    )
                }
            )
        }
    }

    /**
     * Load stations by genre/tag
     */
    fun loadStationsByGenre(genre: String) {
        viewModelScope.launch {
            _uiState.value = StationListUiState.Loading
            stationRepository.getStationsByTag(genre, limit = 50).fold(
                onSuccess = { stations ->
                    _uiState.value = if (stations.isEmpty()) {
                        StationListUiState.Empty
                    } else {
                        StationListUiState.Success(stations)
                    }
                },
                onFailure = { error ->
                    _uiState.value = StationListUiState.Error(
                        error.message ?: "Failed to load stations by genre"
                    )
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Don't release player here - it's a singleton that should persist
    }
}

/**
 * UI state for station list
 */
sealed class StationListUiState {
    data object Loading : StationListUiState()
    data class Success(val stations: List<Station>) : StationListUiState()
    data object Empty : StationListUiState()
    data class Error(val message: String) : StationListUiState()
}
