package com.indieradio.ui.theme.skin

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Sealed class representing available radio skins
 */
sealed class SkinTheme(
    val id: String,
    val displayName: String,
    val description: String
) {
    abstract val colors: SkinColorScheme

    data object ModernMinimal : SkinTheme(
        id = "modern_minimal",
        displayName = "Modern Minimal",
        description = "Clean, contemporary design with Material 3"
    ) {
        override val colors = ModernMinimalColors
    }

    data object Vintage80s : SkinTheme(
        id = "vintage_80s",
        displayName = "Vintage 80s Radio",
        description = "Authentic 1980s transistor radio aesthetic"
    ) {
        override val colors = Vintage80sColors
    }

    companion object {
        val all = listOf(ModernMinimal, Vintage80s)

        fun fromId(id: String): SkinTheme {
            return all.find { it.id == id } ?: ModernMinimal
        }
    }
}

/**
 * Color scheme for a skin
 */
@Immutable
data class SkinColorScheme(
    // Background colors
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,

    // Content colors
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,

    // Accent colors
    val primary: Color,
    val primaryVariant: Color,
    val onPrimary: Color,

    // Special colors
    val accent: Color,
    val accentVariant: Color,

    // Status colors
    val error: Color,
    val success: Color,
    val warning: Color
)

/**
 * Modern Minimal color scheme
 */
private val ModernMinimalColors = SkinColorScheme(
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF26252A),
    surfaceVariant = Color(0xFF3B3A3F),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
    onSurfaceVariant = Color(0xFFCAC4D0),
    primary = Color(0xFFD0BCFF),
    primaryVariant = Color(0xFF9965F4),
    onPrimary = Color(0xFF381E72),
    accent = Color(0xFF4CAF50),
    accentVariant = Color(0xFF388E3C),
    error = Color(0xFFFF5252),
    success = Color(0xFF4CAF50),
    warning = Color(0xFFFFC107)
)

/**
 * Vintage 80s color scheme
 * Inspired by wood, brass, and warm analog displays
 */
private val Vintage80sColors = SkinColorScheme(
    // Wood and warm backgrounds
    background = Color(0xFF4A3728), // Dark walnut brown
    surface = Color(0xFF5C4A3A),    // Lighter wood brown
    surfaceVariant = Color(0xFF3A2E23), // Deep wood

    // Text and labels (cream/white embossed on wood)
    onBackground = Color(0xFFF5F5DC), // Cream
    onSurface = Color(0xFFF5F5DC),
    onSurfaceVariant = Color(0xFFE8E0D5),

    // Brass and metallic accents
    primary = Color(0xFFB8860B),      // Dark goldenrod (brass)
    primaryVariant = Color(0xFFDAA520), // Goldenrod (polished brass)
    onPrimary = Color(0xFF2C2C2C),

    // LED and display colors
    accent = Color(0xFFFF4500),       // Deep red (LED indicator)
    accentVariant = Color(0xFFFFD700), // Gold (backlit display)

    // Status colors
    error = Color(0xFF8B0000),        // Dark red
    success = Color(0xFF228B22),      // Forest green
    warning = Color(0xFFFF8C00)       // Dark orange
)
