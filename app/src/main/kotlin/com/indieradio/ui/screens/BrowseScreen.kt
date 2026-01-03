package com.indieradio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.indieradio.domain.model.PlaybackState
import com.indieradio.ui.viewmodel.RadioPlayerViewModel
import com.indieradio.ui.viewmodel.StationListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    viewModel: RadioPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    var selectedCategory by remember { mutableStateOf<BrowseCategory>(BrowseCategory.Popular) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Browse Stations") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category tabs
            ScrollableTabRow(
                selectedTabIndex = BrowseCategory.entries.indexOf(selectedCategory),
                modifier = Modifier.fillMaxWidth()
            ) {
                BrowseCategory.entries.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            when (category) {
                                BrowseCategory.Popular -> viewModel.loadPopularStations()
                                BrowseCategory.Country -> viewModel.loadStationsByCountry("US")
                                BrowseCategory.Genre -> viewModel.loadStationsByGenre("rock")
                            }
                        },
                        text = { Text(category.title) },
                        icon = {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = category.title
                            )
                        }
                    )
                }
            }

            HorizontalDivider()

            // Station list
            when (uiState) {
                is StationListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StationListUiState.Success -> {
                    val stations = (uiState as StationListUiState.Success).stations
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(stations) { station ->
                            StationItem(
                                station = station,
                                isPlaying = playbackState is PlaybackState.Playing &&
                                    (playbackState as? PlaybackState.Playing)?.station?.stationUuid == station.stationUuid,
                                onClick = { viewModel.playStation(station) },
                                onFavoriteClick = { viewModel.toggleFavorite(station) },
                                viewModel = viewModel
                            )
                        }
                    }
                }

                is StationListUiState.Empty -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Radio,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("No stations found")
                        }
                    }
                }

                is StationListUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (uiState as StationListUiState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadPopularStations() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class BrowseCategory(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Popular("Popular", Icons.Default.Star),
    Country("Country", Icons.Default.Place),
    Genre("Genre", Icons.Default.MusicNote)
}
