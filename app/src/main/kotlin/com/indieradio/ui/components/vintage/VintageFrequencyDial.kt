package com.indieradio.ui.components.vintage

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indieradio.domain.model.Station
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Vintage frequency dial with rotation gesture
 *
 * Maps stations to virtual FM frequencies (88-108 MHz)
 * and allows rotation gesture to tune between them
 */
@Composable
fun VintageFrequencyDial(
    currentFrequency: Float, // 88.0 - 108.0 MHz
    stations: List<Station>,
    onFrequencyChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var rotation by remember { mutableStateOf(0f) }
    val textMeasurer = rememberTextMeasurer()

    // Map frequency to rotation angle (-90° to +90°)
    val frequencyToRotation = { freq: Float ->
        ((freq - 88f) / 20f) * 180f - 90f
    }

    // Map rotation to frequency
    val rotationToFrequency = { rot: Float ->
        ((rot + 90f) / 180f) * 20f + 88f
    }

    // Update rotation when frequency changes externally
    LaunchedEffect(currentFrequency) {
        rotation = frequencyToRotation(currentFrequency)
    }

    Box(
        modifier = modifier
            .size(200.dp)
            .pointerInput(Unit) {
                val center = Offset(size.width / 2f, size.height / 2f)

                detectDragGestures { change, dragAmount ->
                    change.consume()

                    // Calculate angle from center to touch point
                    val touchPoint = change.position
                    val deltaX = touchPoint.x - center.x
                    val deltaY = touchPoint.y - center.y
                    val currentAngle = atan2(deltaY, deltaX) * (180f / Math.PI.toFloat())

                    // Constrain rotation to -90° to +90° (180° range)
                    val newRotation = (rotation + dragAmount.x * 0.5f).coerceIn(-90f, 90f)
                    rotation = newRotation

                    // Convert rotation to frequency and notify
                    val newFrequency = rotationToFrequency(newRotation).coerceIn(88f, 108f)
                    onFrequencyChange(newFrequency)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val radius = size.minDimension / 2f * 0.8f

            // Draw outer brass rim
            drawCircle(
                color = Color(0xFFB8860B), // Brass gold
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 8.dp.toPx())
            )

            // Draw inner dial background
            drawCircle(
                color = Color(0xFF3A2E23), // Dark wood
                radius = radius - 10.dp.toPx(),
                center = Offset(centerX, centerY)
            )

            // Draw frequency tick marks and numbers
            val frequencies = listOf(88f, 92f, 96f, 100f, 104f, 108f)
            frequencies.forEach { freq ->
                val angle = frequencyToRotation(freq) - 90f // Offset by 90° for vertical orientation
                val angleRad = Math.toRadians(angle.toDouble())

                // Major tick mark
                val tickStartRadius = radius - 20.dp.toPx()
                val tickEndRadius = radius - 35.dp.toPx()
                val tickStart = Offset(
                    (centerX + tickStartRadius * cos(angleRad)).toFloat(),
                    (centerY + tickStartRadius * sin(angleRad)).toFloat()
                )
                val tickEnd = Offset(
                    (centerX + tickEndRadius * cos(angleRad)).toFloat(),
                    (centerY + tickEndRadius * sin(angleRad)).toFloat()
                )

                drawLine(
                    color = Color(0xFFFFD700), // Gold
                    start = tickStart,
                    end = tickEnd,
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Frequency number
                val textRadius = radius - 50.dp.toPx()
                val textX = (centerX + textRadius * cos(angleRad)).toFloat()
                val textY = (centerY + textRadius * sin(angleRad)).toFloat()

                val text = freq.toInt().toString()
                val textLayoutResult = textMeasurer.measure(
                    text = text,
                    style = TextStyle(
                        color = Color(0xFFF5F5DC), // Cream
                        fontSize = 12.sp
                    )
                )

                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        textX - textLayoutResult.size.width / 2,
                        textY - textLayoutResult.size.height / 2
                    )
                )
            }

            // Draw red pointer (rotates with gesture)
            rotate(rotation, Offset(centerX, centerY)) {
                drawLine(
                    color = Color(0xFFFF4500), // Deep red
                    start = Offset(centerX, centerY),
                    end = Offset(centerX, centerY - radius + 15.dp.toPx()),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Pointer tip circle
                drawCircle(
                    color = Color(0xFFFF4500),
                    radius = 6.dp.toPx(),
                    center = Offset(centerX, centerY - radius + 15.dp.toPx())
                )
            }

            // Center knob
            drawCircle(
                color = Color(0xFFB8860B), // Brass
                radius = 15.dp.toPx(),
                center = Offset(centerX, centerY)
            )
            drawCircle(
                color = Color(0xFF2C2C2C), // Dark center
                radius = 8.dp.toPx(),
                center = Offset(centerX, centerY)
            )
        }
    }
}
