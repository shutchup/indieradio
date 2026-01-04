package com.indieradio.ui.components.vintage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.atan2

/**
 * Vintage rotary knob component
 *
 * Used for volume control with discrete steps
 */
@Composable
fun VintageKnob(
    value: Int, // 0-15
    label: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var rotation by remember { mutableStateOf(0f) }

    // Map value to rotation (0° to 270°)
    val valueToRotation = { v: Int ->
        (v / 15f) * 270f
    }

    // Map rotation to value
    val rotationToValue = { rot: Float ->
        ((rot / 270f) * 15f).toInt().coerceIn(0, 15)
    }

    // Update rotation when value changes externally
    LaunchedEffect(value) {
        rotation = valueToRotation(value)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .pointerInput(Unit) {
                    val center = Offset(size.width / 2f, size.height / 2f)

                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        // Calculate drag direction and update rotation
                        val touchPoint = change.position
                        val deltaX = touchPoint.x - center.x
                        val deltaY = touchPoint.y - center.y

                        // Determine circular drag
                        val dragAngle = dragAmount.x + dragAmount.y
                        val newRotation = (rotation + dragAngle * 0.5f).coerceIn(0f, 270f)
                        rotation = newRotation

                        // Convert to value and notify
                        val newValue = rotationToValue(newRotation)
                        if (newValue != value) {
                            onValueChange(newValue)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val radius = size.minDimension / 2f * 0.8f

                // Outer metal rim
                drawCircle(
                    color = Color(0xFFC0C0C0), // Silver/chrome
                    radius = radius,
                    center = Offset(centerX, centerY),
                    style = Stroke(width = 4.dp.toPx())
                )

                // Knob body with ribbed texture effect
                drawCircle(
                    color = Color(0xFF2C2C2C), // Dark metal
                    radius = radius - 6.dp.toPx(),
                    center = Offset(centerX, centerY)
                )

                // Draw ribs (radial lines for texture)
                for (i in 0 until 20) {
                    val angle = (i / 20f) * 360f
                    val angleRad = Math.toRadians(angle.toDouble())
                    val ribStart = Offset(
                        (centerX + (radius - 12.dp.toPx()) * kotlin.math.cos(angleRad)).toFloat(),
                        (centerY + (radius - 12.dp.toPx()) * kotlin.math.sin(angleRad)).toFloat()
                    )
                    val ribEnd = Offset(
                        (centerX + (radius - 6.dp.toPx()) * kotlin.math.cos(angleRad)).toFloat(),
                        (centerY + (radius - 6.dp.toPx()) * kotlin.math.sin(angleRad)).toFloat()
                    )

                    drawLine(
                        color = Color(0xFF1A1A1A),
                        start = ribStart,
                        end = ribEnd,
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Rotating indicator dot
                rotate(rotation, Offset(centerX, centerY)) {
                    drawCircle(
                        color = Color(0xFFFFD700), // Gold dot
                        radius = 4.dp.toPx(),
                        center = Offset(centerX, centerY - radius + 12.dp.toPx())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Label
        Text(
            text = label,
            color = Color(0xFFF5F5DC), // Cream
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
