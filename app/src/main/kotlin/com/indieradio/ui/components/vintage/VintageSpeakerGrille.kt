package com.indieradio.ui.components.vintage

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp

/**
 * Vintage speaker grille with fabric mesh texture
 *
 * Subtle animation when audio is playing
 */
@Composable
fun VintageSpeakerGrille(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    // Subtle vibration effect when playing
    val infiniteTransition = rememberInfiniteTransition(label = "speaker_animation")
    val vibrateOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "vibrate"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Draw fabric mesh pattern (crosshatch)
            val fabricColor = Color(0xFFD4C5B9) // Beige fabric
            val spacing = 4.dp.toPx()

            // Horizontal lines
            var y = 0f
            while (y < height) {
                drawLine(
                    color = fabricColor.copy(alpha = 0.6f),
                    start = Offset(0f, y + vibrateOffset),
                    end = Offset(width, y + vibrateOffset),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 2f))
                )
                y += spacing
            }

            // Vertical lines
            var x = 0f
            while (x < width) {
                drawLine(
                    color = fabricColor.copy(alpha = 0.6f),
                    start = Offset(x, 0f),
                    end = Offset(x, height),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 2f))
                )
                x += spacing
            }

            // Add some dust/aging spots
            val dustColor = Color(0xFF8B7765).copy(alpha = 0.2f)
            for (i in 0..20) {
                val dustX = (i * 47) % width.toInt()
                val dustY = (i * 73) % height.toInt()
                drawCircle(
                    color = dustColor,
                    radius = 2.dp.toPx(),
                    center = Offset(dustX.toFloat(), dustY.toFloat())
                )
            }
        }
    }
}
