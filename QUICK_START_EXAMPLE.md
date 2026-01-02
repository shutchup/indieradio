# Quick Start: Minimal Working Example

This is a minimal working example you can copy-paste to get started with the skinning system immediately.

## 1. Build.gradle.kts (app level)

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.indieradio"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.indieradio"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2025.12.01")
    implementation(composeBom)

    // Jetpack Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.activity:activity-compose:1.9.0")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Media3 for audio
    implementation("androidx.media3:media3-exoplayer:1.4.0")
    implementation("androidx.media3:media3-ui:1.4.0")

    // DataStore for preferences
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Coil for images
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}
```

## 2. Minimal Skin Theme (SkinTheme.kt)

```kotlin
package com.indieradio.ui.theme.skin

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

sealed class SkinTheme {
    abstract val id: String
    abstract val name: String
    abstract val colors: SkinColors

    data object Modern : SkinTheme() {
        override val id = "modern"
        override val name = "Modern"
        override val colors = SkinColors(
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            primary = Color(0xFF6200EE),
            onBackground = Color.White,
            onSurface = Color.White,
            dialColor = Color(0xFF03DAC6)
        )
    }

    data object Vintage : SkinTheme() {
        override val id = "vintage"
        override val name = "Vintage 80s"
        override val colors = SkinColors(
            background = Color(0xFF2C1810),
            surface = Color(0xFF4A3728),
            primary = Color(0xFFD4AF37),
            onBackground = Color(0xFFFFF8DC),
            onSurface = Color(0xFFFFF8DC),
            dialColor = Color(0xFFFFE87C)
        )
    }

    companion object {
        fun all() = listOf(Modern, Vintage)
        fun fromId(id: String) = all().find { it.id == id } ?: Modern
    }
}

@Immutable
data class SkinColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val dialColor: Color
)
```

## 3. Theme Provider (IndieRadioTheme.kt)

```kotlin
package com.indieradio.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import com.indieradio.ui.theme.skin.SkinTheme

val LocalSkinTheme = compositionLocalOf<SkinTheme> { SkinTheme.Modern }

@Composable
fun IndieRadioTheme(
    skin: SkinTheme = SkinTheme.Modern,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSkinTheme provides skin) {
        AnimatedContent(
            targetState = skin,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            },
            label = "skin_transition"
        ) { currentSkin ->
            CompositionLocalProvider(LocalSkinTheme provides currentSkin) {
                content()
            }
        }
    }
}

@Composable
fun currentSkin() = LocalSkinTheme.current
```

## 4. Skin Repository (SkinRepository.kt)

```kotlin
package com.indieradio.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.indieradio.ui.theme.skin.SkinTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SkinRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val SKIN_ID_KEY = stringPreferencesKey("skin_id")

    val currentSkinId: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[SKIN_ID_KEY] ?: SkinTheme.Modern.id
        }

    suspend fun saveSkinId(skinId: String) {
        context.dataStore.edit { preferences ->
            preferences[SKIN_ID_KEY] = skinId
        }
    }
}
```

## 5. Skin ViewModel (SkinViewModel.kt)

```kotlin
package com.indieradio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieradio.data.repository.SkinRepository
import com.indieradio.ui.theme.skin.SkinTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkinViewModel @Inject constructor(
    private val skinRepository: SkinRepository
) : ViewModel() {

    private val _currentSkin = MutableStateFlow<SkinTheme>(SkinTheme.Modern)
    val currentSkin: StateFlow<SkinTheme> = _currentSkin.asStateFlow()

    init {
        viewModelScope.launch {
            skinRepository.currentSkinId.collect { skinId ->
                _currentSkin.value = SkinTheme.fromId(skinId)
            }
        }
    }

    fun selectSkin(skin: SkinTheme) {
        viewModelScope.launch {
            _currentSkin.value = skin
            skinRepository.saveSkinId(skin.id)
        }
    }
}
```

## 6. Modern Radio Player Component

```kotlin
package com.indieradio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indieradio.ui.theme.currentSkin

@Composable
fun ModernRadioPlayer(
    frequency: Float,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onFrequencyChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val skin = currentSkin()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(skin.colors.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Frequency Display
        Text(
            text = String.format("%.1f", frequency),
            fontSize = 72.sp,
            color = skin.colors.dialColor
        )

        Text(
            text = "MHz",
            fontSize = 24.sp,
            color = skin.colors.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Frequency Slider
        Slider(
            value = frequency,
            onValueChange = onFrequencyChange,
            valueRange = 88f..108f,
            colors = SliderDefaults.colors(
                thumbColor = skin.colors.primary,
                activeTrackColor = skin.colors.primary,
                inactiveTrackColor = skin.colors.surface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Play/Pause Button
        FloatingActionButton(
            onClick = onPlayPause,
            containerColor = skin.colors.primary,
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
```

## 7. Vintage Radio Player Component

```kotlin
package com.indieradio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.indieradio.ui.theme.currentSkin

@Composable
fun VintageRadioPlayer(
    frequency: Float,
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onFrequencyChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val skin = currentSkin()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(skin.colors.background)
            .padding(24.dp)
    ) {
        // Radio Body
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .background(skin.colors.surface)
                .border(
                    width = 4.dp,
                    color = skin.colors.primary.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Frequency Display (Vintage Style)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(skin.colors.background)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("%.1f MHz", frequency),
                    fontSize = 48.sp,
                    color = skin.colors.dialColor,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Vintage Dial
            Slider(
                value = frequency,
                onValueChange = onFrequencyChange,
                valueRange = 88f..108f,
                colors = SliderDefaults.colors(
                    thumbColor = skin.colors.primary,
                    activeTrackColor = skin.colors.primary,
                    inactiveTrackColor = skin.colors.background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Vintage Button
            Button(
                onClick = onPlayPause,
                colors = ButtonDefaults.buttonColors(
                    containerColor = skin.colors.primary
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.size(width = 120.dp, height = 60.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play"
                )
            }
        }
    }
}
```

## 8. Main Screen

```kotlin
package com.indieradio.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.indieradio.ui.components.ModernRadioPlayer
import com.indieradio.ui.components.VintageRadioPlayer
import com.indieradio.ui.theme.IndieRadioTheme
import com.indieradio.ui.theme.skin.SkinTheme
import com.indieradio.ui.viewmodel.SkinViewModel

@Composable
fun RadioScreen(
    skinViewModel: SkinViewModel = hiltViewModel()
) {
    val currentSkin by skinViewModel.currentSkin.collectAsState()
    var frequency by remember { mutableFloatStateOf(95.0f) }
    var isPlaying by remember { mutableStateOf(false) }

    IndieRadioTheme(skin = currentSkin) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Indieradio - ${currentSkin.name}") },
                    actions = {
                        // Skin switcher dropdown
                        var expanded by remember { mutableStateOf(false) }

                        TextButton(onClick = { expanded = true }) {
                            Text("Change Skin")
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            SkinTheme.all().forEach { skin ->
                                DropdownMenuItem(
                                    text = { Text(skin.name) },
                                    onClick = {
                                        skinViewModel.selectSkin(skin)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                // Render appropriate player based on skin
                when (currentSkin) {
                    is SkinTheme.Modern -> ModernRadioPlayer(
                        frequency = frequency,
                        isPlaying = isPlaying,
                        onPlayPause = { isPlaying = !isPlaying },
                        onFrequencyChange = { frequency = it }
                    )

                    is SkinTheme.Vintage -> VintageRadioPlayer(
                        frequency = frequency,
                        isPlaying = isPlaying,
                        onPlayPause = { isPlaying = !isPlaying },
                        onFrequencyChange = { frequency = it }
                    )
                }
            }
        }
    }
}
```

## 9. MainActivity

```kotlin
package com.indieradio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.indieradio.ui.screens.RadioScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RadioScreen()
        }
    }
}
```

## 10. Application Class

```kotlin
package com.indieradio

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IndieRadioApp : Application()
```

## 11. AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".IndieRadioApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.AppCompat.NoActionBar">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.AppCompat.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

    </application>

</manifest>
```

## Testing It Out

1. Create a new Android Studio project
2. Copy all the files above into the appropriate directories
3. Add the dependencies to build.gradle.kts
4. Sync Gradle
5. Run the app
6. Click "Change Skin" to switch between Modern and Vintage themes

The UI will smoothly animate between the two completely different layouts!

## Next Steps

From this minimal example, you can:

1. Add more skins (Retro Winamp, Art Deco, etc.)
2. Implement actual audio playback with Media3
3. Add custom vintage components (dials, knobs)
4. Create audio visualizers
5. Add skin-specific assets and images
6. Implement haptic feedback
7. Add accessibility features

This gives you a solid foundation to build upon!
