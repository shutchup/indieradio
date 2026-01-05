package com.indieradio

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.indieradio.ui.screens.MainScreen
import com.indieradio.ui.theme.IndieradioTheme
import com.indieradio.util.CrashLogger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Permission launcher for notifications (Android 13+)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, notifications will work
        } else {
            // Permission denied, playback controls won't show in notification
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Check if we should show crash viewer (crash log exists and app previously crashed)
        val prefs = getSharedPreferences("app_state", MODE_PRIVATE)
        val hadPreviousCrash = prefs.getBoolean("had_crash", false)
        val crashLog = CrashLogger.getCrashLog(this)
        val hasCrashLog = crashLog != "No crashes logged"

        if (hadPreviousCrash && hasCrashLog) {
            // Show safe mode crash viewer
            setContent {
                SafeModeCrashViewer(
                    crashLog = crashLog,
                    onClearLogs = {
                        CrashLogger.clearCrashLog(this)
                        prefs.edit().putBoolean("had_crash", false).apply()
                        recreate() // Restart the app
                    }
                )
            }
        } else {
            // Mark that we're attempting to start
            prefs.edit().putBoolean("had_crash", true).apply()

            try {
                // Try to load the normal app
                setContent {
                    IndieradioTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainScreen()
                        }
                    }
                }

                // If we got here successfully, clear the crash flag
                prefs.edit().putBoolean("had_crash", false).apply()
            } catch (e: Exception) {
                // If app crashes during initialization, show crash viewer
                prefs.edit().putBoolean("had_crash", true).apply()
                setContent {
                    SafeModeCrashViewer(
                        crashLog = CrashLogger.getCrashLog(this),
                        onClearLogs = {
                            CrashLogger.clearCrashLog(this)
                            prefs.edit().putBoolean("had_crash", false).apply()
                            recreate() // Restart the app
                        }
                    )
                }
            }
        }
    }
}

/**
 * Safe mode crash viewer - minimal UI that doesn't depend on Hilt or ViewModels
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SafeModeCrashViewer(
    crashLog: String,
    onClearLogs: () -> Unit
) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App Crashed - Safe Mode") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        titleContentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
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
                    text = "The app crashed during startup. Please copy this crash log and share it:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = crashLog,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .horizontalScroll(rememberScrollState())
                            .padding(12.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onClearLogs,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear Logs & Retry")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Instructions:\n1. Long-press on the crash log above\n2. Select 'Copy All'\n3. Share the log with the developer",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
