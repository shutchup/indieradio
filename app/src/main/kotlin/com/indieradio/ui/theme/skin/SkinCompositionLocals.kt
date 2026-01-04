package com.indieradio.ui.theme.skin

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for current skin theme
 */
val LocalSkinTheme = staticCompositionLocalOf<SkinTheme> {
    SkinTheme.ModernMinimal
}

/**
 * CompositionLocal for current skin colors
 */
val LocalSkinColors = staticCompositionLocalOf<SkinColorScheme> {
    SkinTheme.ModernMinimal.colors
}
