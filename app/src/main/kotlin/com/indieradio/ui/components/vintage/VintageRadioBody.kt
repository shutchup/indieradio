package com.indieradio.ui.components.vintage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indieradio.R
import com.indieradio.domain.model.PlaybackState
import com.indieradio.domain.model.Station
import kotlin.math.atan2

/**
 * Photorealistic vintage radio player
 *
 * The wood panel image contains all cutouts pre-positioned:
 * - Top: Large dial cutout (centered, ~20% from top)
 * - Middle: Two knob cutouts (side-by-side, ~50% from top)
 * - Right: Power switch slot (~35% from top)
 * - Center: Brass decorative bars (~58% from top)
 * - Bottom: Speaker grille cutout (~70-95% from top)
 *
 * This component overlays the interactive elements exactly where the cutouts are.
 */
@Composable
fun VintageRadioBody(
    playbackState: PlaybackState,
    currentVolume: Int,
    onVolumeChange: (Int) -> Unit,
    onPowerToggle: () -> Unit,
    onFrequencyChange: (Float) -> Unit,
    stations: List<Station>,
    modifier: Modifier = Modifier
) {
    var isPoweredOn by remember { mutableStateOf(false) }
    var currentFrequency by remember { mutableStateOf(96.0f) }
    var dialRotation by remember { mutableStateOf(0f) }

    val isPlaying = playbackState is PlaybackState.Playing
    val currentStation = when (playbackState) {
        is PlaybackState.Playing -> playbackState.station
        is PlaybackState.Paused -> playbackState.station
        is PlaybackState.Loading -> playbackState.station
        is PlaybackState.Stopped -> playbackState.station
        else -> null
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Base layer: Wood panel with cutouts (fills entire screen)
        Image(
            painter = painterResource(id = R.drawable.vintage_wood_panel),
            contentDescription = "Radio body",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay layer: All interactive elements positioned to match cutouts
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(9f / 16f),
            contentAlignment = Alignment.TopCenter
        ) {
            // 1. Main frequency dial (top cutout - centered, 20% from top)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .aspectRatio(1f)
                    .align(Alignment.TopCenter)
                    .offset(y = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vintage_dial_complete),
                    contentDescription = "Frequency dial",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 2. Power switch (right side, 35% from top)
            Image(
                painter = painterResource(id = R.drawable.vintage_power_switch),
                contentDescription = "Power switch",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-35).dp, y = 170.dp)
                    .clickable {
                        isPoweredOn = !isPoweredOn
                        onPowerToggle()
                    }
            )

            // 3. Left knob - Volume (left cutout, 50% from top)
            Image(
                painter = painterResource(id = R.drawable.vintage_knob_left),
                contentDescription = "Volume",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.TopStart)
                    .offset(x = 55.dp, y = 310.dp)
            )

            // 4. Right knob - Tuning (right cutout, 50% from top)
            Image(
                painter = painterResource(id = R.drawable.vintage_knob_left),
                contentDescription = "Tuning",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-55).dp, y = 310.dp)
            )

            // 5. Brass bars (58% from top)
            Image(
                painter = painterResource(id = R.drawable.vintage_brass_bars),
                contentDescription = "Brass bars",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(25.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 420.dp)
            )

            // 6. Speaker grille area - Station info display (70-95% from top)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(180.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-30).dp),
                contentAlignment = Alignment.Center
            ) {
                if (currentStation != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = currentStation.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C1810),
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${currentFrequency.toInt()} MHz",
                            fontSize = 16.sp,
                            color = Color(0xFF6B4423),
                            textAlign = TextAlign.Center
                        )

                        if (isPlaying) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "♫ Now Playing",
                                fontSize = 14.sp,
                                color = Color(0xFF8B5A3C),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Turn dial to tune",
                        fontSize = 16.sp,
                        color = Color(0xFF6B4423),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
