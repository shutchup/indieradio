package com.indieradio.ui.components.vintage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indieradio.domain.model.PlaybackState
import com.indieradio.domain.model.Station

/**
 * Complete vintage 80s radio interface
 *
 * Assembles all vintage components into an authentic radio experience
 */
@Composable
fun VintageRadioBody(
    playbackState: PlaybackState,
    currentVolume: Int, // 0-15
    onVolumeChange: (Int) -> Unit,
    onPowerToggle: () -> Unit,
    onFrequencyChange: (Float) -> Unit,
    stations: List<Station>,
    modifier: Modifier = Modifier
) {
    // Extract current state
    val isPlaying = playbackState is PlaybackState.Playing
    val currentStation = when (playbackState) {
        is PlaybackState.Playing -> playbackState.station
        is PlaybackState.Paused -> playbackState.station
        is PlaybackState.Loading -> playbackState.station
        is PlaybackState.Stopped -> playbackState.station
        else -> null
    }

    var isPoweredOn by remember { mutableStateOf(false) }
    var currentFrequency by remember { mutableStateOf(96.0f) }

    // Map stations to frequencies (simple distribution)
    val stationFrequencyMap = remember(stations) {
        if (stations.isEmpty()) {
            emptyMap()
        } else {
            val frequencies = (0..40).map { 88.0f + it * 0.5f }
            stations.take(frequencies.size).mapIndexed { index, station ->
                frequencies[index] to station
            }.toMap()
        }
    }

    // Find closest station to current frequency
    val closestStation = remember(currentFrequency, stationFrequencyMap) {
        stationFrequencyMap.entries.minByOrNull {
            kotlin.math.abs(it.key - currentFrequency)
        }?.value
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF5C4A3A), // Lighter wood top
                        Color(0xFF4A3728), // Dark walnut middle
                        Color(0xFF3A2E23)  // Deep wood bottom
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 4.dp,
                color = Color(0xFF2C2219), // Darker wood edge
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Brand nameplate (generic vintage style)
            Text(
                text = "VINTAGE RADIO",
                color = Color(0xFFB8860B), // Brass/gold
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Station Display
            VintageStationDisplay(
                frequency = currentFrequency,
                stationName = if (isPoweredOn) {
                    closestStation?.name ?: currentStation?.name ?: "Tuning..."
                } else {
                    ""
                },
                isPlaying = isPlaying && isPoweredOn,
                isPoweredOn = isPoweredOn
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Frequency Dial
            VintageFrequencyDial(
                currentFrequency = currentFrequency,
                stations = stations,
                onFrequencyChange = { newFreq ->
                    if (isPoweredOn) {
                        currentFrequency = newFreq
                        onFrequencyChange(newFreq)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Controls row (Volume + Power)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Volume Knob
                VintageKnob(
                    value = currentVolume,
                    label = "VOLUME",
                    onValueChange = onVolumeChange
                )

                // Power Switch
                VintagePowerSwitch(
                    isOn = isPoweredOn,
                    onToggle = {
                        isPoweredOn = !isPoweredOn
                        onPowerToggle()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Speaker Grille
            VintageSpeakerGrille(
                isPlaying = isPlaying && isPoweredOn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Speaker label
            Text(
                text = "SPEAKER",
                color = Color(0xFF8B7765).copy(alpha = 0.6f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )
        }
    }
}
