package com.indieradio.domain.model

/**
 * Represents the current state of radio playback
 */
sealed class PlaybackState {
    /**
     * No station loaded
     */
    data object Idle : PlaybackState()

    /**
     * Buffering/loading a station
     */
    data class Loading(val station: Station) : PlaybackState()

    /**
     * Playing a station
     */
    data class Playing(
        val station: Station,
        val isBuffering: Boolean = false
    ) : PlaybackState()

    /**
     * Playback paused
     */
    data class Paused(val station: Station) : PlaybackState()

    /**
     * Playback stopped
     */
    data class Stopped(val station: Station?) : PlaybackState()

    /**
     * Error occurred during playback
     */
    data class Error(
        val station: Station?,
        val message: String,
        val throwable: Throwable? = null
    ) : PlaybackState()
}

/**
 * Extension to check if currently playing
 */
fun PlaybackState.isPlaying(): Boolean = this is PlaybackState.Playing && !this.isBuffering

/**
 * Extension to check if loading/buffering
 */
fun PlaybackState.isLoading(): Boolean = this is PlaybackState.Loading ||
    (this is PlaybackState.Playing && this.isBuffering)

/**
 * Extension to get current station if any
 */
fun PlaybackState.getCurrentStation(): Station? = when (this) {
    is PlaybackState.Loading -> station
    is PlaybackState.Playing -> station
    is PlaybackState.Paused -> station
    is PlaybackState.Stopped -> station
    is PlaybackState.Error -> station
    is PlaybackState.Idle -> null
}
