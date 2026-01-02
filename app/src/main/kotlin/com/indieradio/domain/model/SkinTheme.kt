package com.indieradio.domain.model

/**
 * Sealed class representing different radio skins/themes
 */
sealed class SkinTheme(
    val id: String,
    val displayName: String,
    val description: String
) {
    /**
     * Modern Minimal skin - clean Material 3 design
     */
    data object Modern : SkinTheme(
        id = "modern",
        displayName = "Modern Minimal",
        description = "Clean, modern design with waveform visualizer"
    )

    /**
     * Vintage 80s Indian Radio skin - authentic retro design
     */
    data object Vintage80s : SkinTheme(
        id = "vintage_80s",
        displayName = "Vintage 80s",
        description = "Classic radio with rotary dials and knobs"
    )

    /**
     * Retro Winamp-style skin - 90s nostalgia
     */
    data object RetroWinamp : SkinTheme(
        id = "retro_winamp",
        displayName = "Retro Winamp",
        description = "90s style with spectrum analyzer"
    )

    /**
     * Art Deco Radio skin - 1920s-30s luxury aesthetic
     */
    data object ArtDeco : SkinTheme(
        id = "art_deco",
        displayName = "Art Deco",
        description = "Elegant 1920s-30s radio design"
    )

    companion object {
        /**
         * Get all available skins
         */
        fun getAllSkins(): List<SkinTheme> = listOf(
            Modern,
            Vintage80s,
            RetroWinamp,
            ArtDeco
        )

        /**
         * Get skin by ID
         */
        fun fromId(id: String): SkinTheme = when (id) {
            "modern" -> Modern
            "vintage_80s" -> Vintage80s
            "retro_winamp" -> RetroWinamp
            "art_deco" -> ArtDeco
            else -> Modern // Default to modern
        }

        /**
         * Default skin
         */
        val DEFAULT = Modern
    }
}
