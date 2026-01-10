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
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Layer 1: Wood panel background (fills most of screen)
        Image(
            painter = painterResource(id = R.drawable.vintage_wood_panel),
            contentDescription = "Vintage radio body",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
        )

        // All other elements positioned relative to the wood panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Layer 2: Main frequency dial (larger, centered)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                // Dial background image
                Image(
                    painter = painterResource(id = R.drawable.vintage_dial_complete),
                    contentDescription = "Frequency dial",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
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

                // Dial pointer overlay
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerX = size.width / 2f
                    val centerY = size.height / 2f
                    val pointerLength = size.width * 0.35f

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
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Layer 3: Power switch and LED (right side)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(80.dp)
            ) {
                // Power switch
                Image(
                    painter = painterResource(id = R.drawable.vintage_power_switch),
                    contentDescription = "Power switch",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(100.dp)
                        .height(50.dp)
                        .align(Alignment.CenterEnd)
                        .pointerInput(Unit) {
                            detectDragGestures { _, _ ->
                                isPoweredOn = !isPoweredOn
                                onPowerToggle()
                            }
                        }
                )

                // LED indicator next to power switch
                if (isPoweredOn && isPlaying) {
                    Canvas(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = (-110).dp)
                    ) {
                        drawCircle(
                            color = Color(0xFFFF4500),
                            radius = size.minDimension / 2f,
                            alpha = 0.9f
                        )
                        // Glow effect
                        drawCircle(
                            color = Color(0xFFFF4500),
                            radius = size.minDimension * 1.2f,
                            alpha = 0.4f,
                            style = Stroke(width = 6.dp.toPx())
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Layer 4: Control knobs (left and right)
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(140.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left knob (Volume)
                Image(
                    painter = painterResource(id = R.drawable.vintage_knob_left),
                    contentDescription = "Volume control",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(120.dp)
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

                // Right knob (Tuning/Balance)
                Image(
                    painter = painterResource(id = R.drawable.vintage_knob_left),
                    contentDescription = "Tuning control",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Layer 5: Brass decorative bars
            Image(
                painter = painterResource(id = R.drawable.vintage_brass_bars),
                contentDescription = "Decorative brass bars",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(30.dp)
            )
        }
    }
}
