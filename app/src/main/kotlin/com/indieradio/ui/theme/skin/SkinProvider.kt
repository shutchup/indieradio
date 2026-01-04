package com.indieradio.ui.theme.skin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Provides skin theme to composable tree
 */
@Composable
fun ProvideSkinTheme(
    skinTheme: SkinTheme,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSkinTheme provides skinTheme,
        LocalSkinColors provides skinTheme.colors,
        content = content
    )
}

/**
 * Access current skin theme
 */
object SkinThemeProvider {
    val current: SkinTheme
        @Composable
        get() = LocalSkinTheme.current

    val colors: SkinColorScheme
        @Composable
        get() = LocalSkinColors.current
}
