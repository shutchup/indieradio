package com.indieradio.ui.components.vintage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vintage LED/LCD style station display
 *
 * Shows frequency and station name with authentic backlit aesthetic
 */
@Composable
fun VintageStationDisplay(
    frequency: Float, // 88.0 - 108.0
    stationName: String,
    isPlaying: Boolean,
    isPoweredOn: Boolean,
    modifier: Modifier = Modifier
) {
    // Pulsing effect for playing indicator
    val infiniteTransition = rememberInfiniteTransition(label = "playing_pulse")
    val playingAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "playing_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                width = 3.dp,
                color = Color(0xFFB8860B) // Brass frame
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isPoweredOn) {
                        listOf(
                            Color(0xFF2C2C2C), // Dark top
                            Color(0xFF1A1A1A)  // Darker bottom
                        )
                    } else {
                        listOf(Color(0xFF1A1A1A), Color(0xFF0D0D0D))
                    }
                )
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isPoweredOn,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                // Frequency display (larger, LED-style)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FM",
                        color = Color(0xFFFFD700).copy(alpha = 0.8f), // Gold
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = String.format("%.1f", frequency),
                        color = Color(0xFFFF4500), // Deep red LED
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "MHz",
                        color = Color(0xFFFFD700).copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Station name (smaller, scrolling if long)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Playing indicator dot
                    if (isPlaying) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    Color(0xFFFF4500).copy(alpha = playingAlpha),
                                    shape = androidx.compose.foundation.shape.CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Text(
                        text = if (stationName.isNotEmpty()) stationName else "- - -",
                        color = Color(0xFFFFD700), // Gold
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }
            }
        }
    }
}
