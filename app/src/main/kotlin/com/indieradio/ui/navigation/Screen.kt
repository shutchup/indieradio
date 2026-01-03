package com.indieradio.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Screen routes for navigation
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Player : Screen("player", "Player", Icons.Default.Home)
    data object Browse : Screen("browse", "Browse", Icons.Default.Search)
    data object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    companion object {
        val bottomNavItems = listOf(Player, Browse, Favorites, Settings)
    }
}
