package com.indieradio.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.indieradio.ui.theme.skin.SkinTheme
import com.indieradio.ui.viewmodel.SkinViewModel
import com.indieradio.util.CrashLogger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    skinViewModel: SkinViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentSkin by skinViewModel.currentSkin.collectAsState()
    var showSkinDialog by remember { mutableStateOf(false) }
    var showCrashLogDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "App Settings",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Skin/Theme selector
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Radio Skin",
                subtitle = currentSkin.displayName,
                onClick = { showSkinDialog = true }
            )

            SettingsItem(
                icon = Icons.Default.MusicNote,
                title = "Audio Quality",
                subtitle = "Automatic",
                onClick = { /* TODO: Audio quality settings */ }
            )

            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Now playing notifications",
                onClick = { /* TODO: Notification settings */ }
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Debug",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingsItem(
                icon = Icons.Default.BugReport,
                title = "Crash Logs",
                subtitle = "View app crash reports",
                onClick = { showCrashLogDialog = true }
            )

            Spacer(modifier = Modifier.weight(1f))

            // App info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Indieradio",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Version 1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    // Skin selection dialog
    if (showSkinDialog) {
        SkinSelectionDialog(
            currentSkin = currentSkin,
            availableSkins = skinViewModel.availableSkins,
            onSkinSelected = { skin ->
                skinViewModel.selectSkin(skin)
                showSkinDialog = false
            },
            onDismiss = { showSkinDialog = false }
        )
    }

    // Crash log viewer dialog
    if (showCrashLogDialog) {
        CrashLogDialog(
            onDismiss = { showCrashLogDialog = false },
            onClear = {
                CrashLogger.clearCrashLog(context)
                showCrashLogDialog = false
            }
        )
    }
}

/**
 * Dialog for selecting radio skin
 */
@Composable
private fun SkinSelectionDialog(
    currentSkin: SkinTheme,
    availableSkins: List<SkinTheme>,
    onSkinSelected: (SkinTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Radio Skin") },
        text = {
            Column {
                Text(
                    text = "Select your preferred vintage radio design",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                availableSkins.forEach { skin ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = skin == currentSkin,
                                onClick = { onSkinSelected(skin) }
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = skin == currentSkin,
                            onClick = { onSkinSelected(skin) }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = skin.displayName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = skin.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (skin == currentSkin) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

/**
 * Dialog for viewing crash logs
 */
@Composable
private fun CrashLogDialog(
    onDismiss: () -> Unit,
    onClear: () -> Unit
) {
    val context = LocalContext.current
    val crashLog = remember { CrashLogger.getCrashLog(context) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crash Logs") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                Text(
                    text = "Copy this log and share with the developer:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = crashLog,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        dismissButton = {
            TextButton(onClick = onClear) {
                Text("Clear Logs")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Open",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
