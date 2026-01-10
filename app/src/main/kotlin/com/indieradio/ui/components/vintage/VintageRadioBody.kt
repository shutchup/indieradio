package com.indieradio.ui.components.vintage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indieradio.R
import com.indieradio.domain.model.PlaybackState
import com.indieradio.domain.model.Station
import kotlin.math.atan2

/**
 * Photorealistic vintage 80s radio player using image assets
 *
 * This is a complete redesign using AI-generated photorealistic components
 * layered to create an authentic vintage radio experience
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
    // State
    var isPoweredOn by remember { mutableStateOf(false) }
    var currentFrequency by remember { mutableStateOf(96.0f) }
    var dialRotation by remember { mutableStateOf(0f) } // Pointer rotation angle
    var volumeRotation by remember { mutableStateOf((currentVolume / 15f) * 270f) }

    // Extract current state
    val isPlaying = playbackState is PlaybackState.Playing
    val currentStation = when (playbackState) {
        is PlaybackState.Playing -> playbackState.station
        is PlaybackState.Paused -> playbackState.station
        is PlaybackState.Loading -> playbackState.station
        is PlaybackState.Stopped -> playbackState.station
        else -> null
    }

    // Map stations to frequencies (88-108 MHz)
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

    // Update dial rotation when frequency changes
    LaunchedEffect(currentFrequency) {
        // Map frequency 88-108 MHz to rotation -135° to +135° (270° range)
        val normalizedFreq = (currentFrequency - 88f) / 20f // 0.0 to 1.0
        dialRotation = -135f + (normalizedFreq * 270f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Layer 1: Wood panel background with cutouts
        Image(
            painter = painterResource(id = R.drawable.vintage_wood_panel),
            contentDescription = "Vintage radio body",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f) // Portrait phone aspect ratio
        )

        // Layer 2: Brass dial (positioned at top)
        Image(
            painter = painterResource(id = R.drawable.vintage_dial_complete),
            contentDescription = "Frequency dial",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .align(Alignment.TopCenter)
                .offset(y = 60.dp)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (isPoweredOn) {
                            // Calculate rotation angle based on drag
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val touchX = change.position.x
                            val touchY = change.position.y

                            val angle = atan2(touchY - centerY, touchX - centerX)
                            val degrees = Math.toDegrees(angle.toDouble()).toFloat()

                            // Map angle to frequency (88-108 MHz)
                            val normalizedAngle = ((degrees + 180f) % 360f) / 360f
                            val newFreq = 88f + (normalizedAngle * 20f)

                            if (newFreq in 88f..108f) {
                                currentFrequency = newFreq
                                onFrequencyChange(newFreq)
                            }
                        }
                    }
                }
        )

        // Layer 3: Dial pointer (Canvas-drawn white line that rotates)
        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
                .align(Alignment.TopCenter)
                .offset(y = 60.dp)
        ) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val pointerLength = size.width * 0.35f // Extends from center

            rotate(dialRotation, pivot = Offset(centerX, centerY)) {
                drawLine(
                    color = Color.White,
                    start = Offset(centerX, centerY),
                    end = Offset(centerX, centerY - pointerLength),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }

        // Layer 4: Left knob (Volume control)
        Image(
            painter = painterResource(id = R.drawable.vintage_knob_left),
            contentDescription = "Volume control",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterStart)
                .offset(x = 80.dp, y = 20.dp)
                .pointerInput(Unit) {
                    var lastAngle = 0f
                    detectDragGestures(
                        onDragStart = { offset ->
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            lastAngle = atan2(offset.y - centerY, offset.x - centerX)
                        },
                        onDrag = { change, _ ->
                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val touchX = change.position.x
                            val touchY = change.position.y

                            val currentAngle = atan2(touchY - centerY, touchX - centerX)
                            val angleDelta = currentAngle - lastAngle
                            lastAngle = currentAngle

                            // Update volume rotation (0-270 degrees = 0-15 volume)
                            volumeRotation = (volumeRotation + Math.toDegrees(angleDelta.toDouble()).toFloat())
                                .coerceIn(0f, 270f)

                            val newVolume = ((volumeRotation / 270f) * 15f).toInt()
                            if (newVolume != currentVolume) {
                                onVolumeChange(newVolume)
                            }
                        }
                    )
                }
        )

        // Layer 5: Right knob (Tuning/Balance - same image)
        Image(
            painter = painterResource(id = R.drawable.vintage_knob_left),
            contentDescription = "Tuning control",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-80).dp, y = 20.dp)
        )

        // Layer 6: Power switch
        Image(
            painter = painterResource(id = R.drawable.vintage_power_switch),
            contentDescription = "Power switch",
            modifier = Modifier
                .width(120.dp)
                .height(60.dp)
                .align(Alignment.CenterEnd)
                .offset(x = (-30).dp, y = (-60).dp)
                .pointerInput(Unit) {
                    detectDragGestures { _, _ ->
                        isPoweredOn = !isPoweredOn
                        onPowerToggle()
                    }
                }
        )

        // Layer 7: Brass divider bars
        Image(
            painter = painterResource(id = R.drawable.vintage_brass_bars),
            contentDescription = "Decorative brass bars",
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(40.dp)
                .align(Alignment.Center)
                .offset(y = 140.dp)
        )

        // Layer 8: LED glow effect (if powered on and playing)
        if (isPoweredOn && isPlaying) {
            Canvas(
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-40).dp, y = (-50).dp)
            ) {
                drawCircle(
                    color = Color(0xFFFF4500),
                    radius = size.minDimension / 2f,
                    alpha = 0.8f
                )
                // Glow effect
                drawCircle(
                    color = Color(0xFFFF4500),
                    radius = size.minDimension,
                    alpha = 0.3f,
                    style = Stroke(width = 8.dp.toPx())
                )
            }
        }
    }
}
