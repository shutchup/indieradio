package com.indieradio.ui.components.vintage

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vintage power toggle switch with LED indicator
 */
@Composable
fun VintagePowerSwitch(
    isOn: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val switchOffset by animateFloatAsState(
        targetValue = if (isOn) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "switch_toggle"
    )

    val ledColor by animateColorAsState(
        targetValue = if (isOn) Color(0xFFFF4500) else Color(0xFF3A2E23),
        label = "led_color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(onClick = onToggle)
    ) {
        // LED Indicator
        Box(
            modifier = Modifier.size(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // LED glow effect when on
                if (isOn) {
                    drawCircle(
                        color = Color(0xFFFF4500).copy(alpha = 0.3f),
                        radius = size.width / 2f + 4.dp.toPx()
                    )
                }

                // LED body
                drawCircle(
                    color = ledColor,
                    radius = size.width / 2f
                )

                // LED rim
                drawCircle(
                    color = Color(0xFF2C2C2C),
                    radius = size.width / 2f,
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle Switch
        Canvas(
            modifier = Modifier
                .width(50.dp)
                .height(24.dp)
        ) {
            val switchHeight = size.height
            val switchWidth = size.width

            // Switch track (background)
            drawRoundRect(
                color = Color(0xFF3A2E23), // Dark wood
                topLeft = Offset.Zero,
                size = Size(switchWidth, switchHeight),
                cornerRadius = CornerRadius(switchHeight / 2f),
                style = Stroke(width = 2.dp.toPx())
            )

            drawRoundRect(
                color = Color(0xFF2C2C2C),
                topLeft = Offset(1.dp.toPx(), 1.dp.toPx()),
                size = Size(switchWidth - 2.dp.toPx(), switchHeight - 2.dp.toPx()),
                cornerRadius = CornerRadius(switchHeight / 2f)
            )

            // Switch toggle (moves left/right)
            val toggleX = switchOffset * (switchWidth - switchHeight)
            drawCircle(
                color = Color(0xFFC0C0C0), // Chrome silver
                radius = (switchHeight / 2f) - 2.dp.toPx(),
                center = Offset(
                    toggleX + switchHeight / 2f,
                    switchHeight / 2f
                )
            )

            // Toggle highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.3f),
                radius = (switchHeight / 2f) - 4.dp.toPx(),
                center = Offset(
                    toggleX + switchHeight / 2f - 2.dp.toPx(),
                    switchHeight / 2f - 2.dp.toPx()
                )
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Label
        Text(
            text = if (isOn) "ON" else "OFF",
            color = if (isOn) Color(0xFFFFD700) else Color(0xFFF5F5DC),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
