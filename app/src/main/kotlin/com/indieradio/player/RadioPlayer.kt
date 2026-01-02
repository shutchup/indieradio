package com.indieradio.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.indieradio.domain.model.PlaybackState
import com.indieradio.domain.model.Station
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper around ExoPlayer for radio streaming
 */
@Singleton
class RadioPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var exoPlayer: ExoPlayer? = null
    private var currentStation: Station? = null

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            // Set up player listeners
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    updatePlaybackState(playbackState)
                }

                override fun onPlayerError(error: PlaybackException) {
                    _playbackState.value = PlaybackState.Error(
                        station = currentStation,
                        message = error.message ?: "Playback error",
                        throwable = error
                    )
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    currentStation?.let { station ->
                        if (isPlaying) {
                            _playbackState.value = PlaybackState.Playing(
                                station = station,
                                isBuffering = false
                            )
                        }
                    }
                }
            })

            // Configure for radio streaming
            playWhenReady = false
        }
    }

    /**
     * Play a radio station
     */
    fun playStation(station: Station) {
        currentStation = station
        _playbackState.value = PlaybackState.Loading(station)

        exoPlayer?.apply {
            // Create media item from resolved stream URL
            val mediaItem = MediaItem.fromUri(station.urlResolved)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    /**
     * Pause playback
     */
    fun pause() {
        exoPlayer?.playWhenReady = false
        currentStation?.let { station ->
            _playbackState.value = PlaybackState.Paused(station)
        }
    }

    /**
     * Resume playback
     */
    fun resume() {
        exoPlayer?.playWhenReady = true
        currentStation?.let { station ->
            _playbackState.value = PlaybackState.Playing(station, isBuffering = false)
        }
    }

    /**
     * Stop playback
     */
    fun stop() {
        exoPlayer?.stop()
        _playbackState.value = PlaybackState.Stopped(currentStation)
    }

    /**
     * Release the player
     */
    fun release() {
        exoPlayer?.release()
        exoPlayer = null
        _playbackState.value = PlaybackState.Idle
    }

    /**
     * Get the ExoPlayer instance
     */
    fun getPlayer(): Player? = exoPlayer

    /**
     * Update playback state based on ExoPlayer state
     */
    private fun updatePlaybackState(state: Int) {
        when (state) {
            Player.STATE_BUFFERING -> {
                currentStation?.let { station ->
                    _playbackState.value = PlaybackState.Playing(
                        station = station,
                        isBuffering = true
                    )
                }
            }
            Player.STATE_READY -> {
                currentStation?.let { station ->
                    if (exoPlayer?.playWhenReady == true) {
                        _playbackState.value = PlaybackState.Playing(
                            station = station,
                            isBuffering = false
                        )
                    }
                }
            }
            Player.STATE_ENDED -> {
                // Radio streams shouldn't end, but handle it
                _playbackState.value = PlaybackState.Stopped(currentStation)
            }
            Player.STATE_IDLE -> {
                // Do nothing, handled elsewhere
            }
        }
    }

    /**
     * Check if currently playing
     */
    fun isPlaying(): Boolean = exoPlayer?.isPlaying == true

    /**
     * Get current playback position (not relevant for radio, but useful for UI)
     */
    fun getCurrentPosition(): Long = exoPlayer?.currentPosition ?: 0L
}
