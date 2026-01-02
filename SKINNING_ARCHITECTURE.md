# Indieradio Skinning System Architecture (2026)

## Executive Summary

Based on extensive research of Android development best practices in 2025-2026, I recommend a **Jetpack Compose-based architecture with custom CompositionLocal theming** for the Indieradio skinning system. This approach provides maximum flexibility for creating completely different visual designs while maintaining smooth runtime transitions and responsive layouts.

---

## 1. Architecture Recommendation: Jetpack Compose with Custom Design System

### Why Jetpack Compose?

**Modern Standard (2026)**: Jetpack Compose is now the de facto standard for Android UI development, with active updates through BOM 2025.12.01.

**Key Advantages for Skinning**:
- **Runtime theme switching**: Native support for dynamic theming with smooth transitions
- **Declarative UI**: Each skin can be a completely different composable structure, not just color changes
- **Animation-first**: Built-in animation support for smooth skin transitions
- **Responsive by design**: WindowSizeClass and adaptive layouts handle different screen sizes elegantly
- **Type-safe theming**: Kotlin's type system prevents theme-related bugs

### Architecture Pattern: Custom Design System with CompositionLocal

```
Skin System Architecture
├── Skin Definition Layer (Data)
│   ├── SkinTheme (sealed class)
│   ├── SkinAssets (images, fonts, sounds)
│   └── SkinMetadata (name, era, description)
├── Theme Provision Layer (CompositionLocal)
│   ├── LocalSkin (CompositionLocal)
│   ├── LocalSkinColors
│   ├── LocalSkinTypography
│   └── LocalSkinShapes
├── Component Layer (UI)
│   ├── Skin-specific composables
│   ├── Common radio controls (abstracted)
│   └── Visualizers (per-skin)
└── State Management Layer
    ├── SkinRepository (asset loading)
    └── SkinViewModel (selection, switching)
```

---

## 2. Concrete Code Structure

### 2.1 Project Structure

```
app/src/main/kotlin/com/indieradio/
├── ui/
│   ├── theme/
│   │   ├── skin/
│   │   │   ├── SkinTheme.kt              # Core skin abstraction
│   │   │   ├── SkinAssets.kt             # Asset definitions
│   │   │   ├── SkinCompositionLocals.kt  # CompositionLocal providers
│   │   │   └── skins/
│   │   │       ├── Vintage80sIndianSkin.kt
│   │   │       ├── ModernMinimalSkin.kt
│   │   │       ├── RetroWinampSkin.kt
│   │   │       └── Art DecoRadioSkin.kt
│   │   ├── Color.kt
│   │   ├── Typography.kt
│   │   └── Shape.kt
│   ├── components/
│   │   ├── common/
│   │   │   ├── RadioControl.kt           # Abstract radio control interface
│   │   │   ├── FrequencyDisplay.kt
│   │   │   └── VolumeControl.kt
│   │   └── skinned/                      # Skin-specific implementations
│   │       ├── vintage/
│   │       │   ├── VintageFrequencyDial.kt
│   │       │   ├── VintageKnob.kt
│   │       │   └── VintageRadioBody.kt
│   │       ├── modern/
│   │       │   ├── ModernFrequencySlider.kt
│   │       │   ├── ModernVisualizer.kt
│   │       │   └── ModernControls.kt
│   │       └── ...
│   ├── screens/
│   │   └── RadioPlayerScreen.kt
│   └── visualizer/
│       ├── AudioVisualizer.kt            # Abstract visualizer
│       ├── WaveformVisualizer.kt
│       └── SpectrumVisualizer.kt
├── data/
│   ├── repository/
│   │   └── SkinRepository.kt
│   └── model/
│       └── SkinConfig.kt
└── domain/
    └── usecase/
        └── SwitchSkinUseCase.kt

app/src/main/res/
├── drawable/                             # Common drawables
├── drawable-skin-vintage80s/             # Skin-specific resources
│   ├── radio_body_background.png
│   ├── frequency_dial.png
│   ├── knob_volume.png
│   └── texture_fabric.png
├── drawable-skin-modern/
│   └── ...
├── font/
│   ├── vintage_display.ttf
│   ├── modern_sans.ttf
│   └── retro_led.ttf
└── raw/
    ├── skin_vintage80s_switch_sound.mp3
    └── skin_modern_switch_sound.mp3
```

### 2.2 Core Skin Abstraction

```kotlin
// SkinTheme.kt
package com.indieradio.ui.theme.skin

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Sealed class representing available radio skins
 */
sealed class SkinTheme {
    abstract val id: String
    abstract val displayName: String
    abstract val era: String
    abstract val description: String
    abstract val colors: SkinColorScheme
    abstract val typography: SkinTypography
    abstract val shapes: SkinShapes
    abstract val assets: SkinAssets

    data object Vintage80sIndian : SkinTheme() {
        override val id = "vintage_80s_indian"
        override val displayName = "1980s All India Radio"
        override val era = "1980s"
        override val description = "Authentic 80s Indian radio aesthetic"
        override val colors = Vintage80sColorScheme
        override val typography = Vintage80sTypography
        override val shapes = Vintage80sShapes
        override val assets = Vintage80sAssets
    }

    data object ModernMinimal : SkinTheme() {
        override val id = "modern_minimal"
        override val displayName = "Modern Minimal"
        override val era = "2020s"
        override val description = "Clean, modern interface with visualizers"
        override val colors = ModernColorScheme
        override val typography = ModernTypography
        override val shapes = ModernShapes
        override val assets = ModernAssets
    }

    data object RetroWinamp : SkinTheme() {
        override val id = "retro_winamp"
        override val displayName = "Retro Player"
        override val era = "1990s"
        override val description = "Nostalgic Winamp-style interface"
        override val colors = RetroColorScheme
        override val typography = RetroTypography
        override val shapes = RetroShapes
        override val assets = RetroAssets
    }

    companion object {
        fun all() = listOf(Vintage80sIndian, ModernMinimal, RetroWinamp)
        fun fromId(id: String) = all().find { it.id == id } ?: ModernMinimal
    }
}

@Immutable
data class SkinColorScheme(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
    val onPrimary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    // Skin-specific colors
    val dialForeground: Color,
    val dialBackground: Color,
    val knobColor: Color,
    val frequencyText: Color,
    val ledIndicator: Color
)

@Immutable
data class SkinTypography(
    val frequencyDisplay: TextStyle,
    val stationName: TextStyle,
    val controlLabel: TextStyle,
    val metadata: TextStyle
)

@Immutable
data class SkinShapes(
    val radioBodyShape: androidx.compose.ui.graphics.Shape,
    val knobShape: androidx.compose.ui.graphics.Shape,
    val buttonShape: androidx.compose.ui.graphics.Shape,
    val dialShape: androidx.compose.ui.graphics.Shape
)

@Immutable
data class SkinAssets(
    val radioBodyBackground: Int,  // Drawable resource ID
    val frequencyDialImage: Int?,
    val knobImage: Int?,
    val textureOverlay: Int?,
    val switchSound: Int?,  // Raw resource ID
    val hasVisualizer: Boolean,
    val visualizerType: VisualizerType
)

enum class VisualizerType {
    NONE,           // Vintage radios
    WAVEFORM,       // Modern skins
    SPECTRUM,       // Modern skins
    LED_BARS        // Retro skins
}
```

### 2.3 CompositionLocal Theme Provider

```kotlin
// SkinCompositionLocals.kt
package com.indieradio.ui.theme.skin

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * CompositionLocal for current skin theme
 */
val LocalSkinTheme = compositionLocalOf<SkinTheme> {
    SkinTheme.ModernMinimal
}

/**
 * Main theme wrapper for the app with skin support
 */
@Composable
fun IndieRadioTheme(
    skinTheme: SkinTheme = SkinTheme.ModernMinimal,
    content: @Composable () -> Unit
) {
    // Provide skin theme to composition
    CompositionLocalProvider(
        LocalSkinTheme provides skinTheme
    ) {
        // Animated content transition when skin changes
        AnimatedContent(
            targetState = skinTheme,
            transitionSpec = {
                // Customize transition based on skin type
                fadeIn(
                    animationSpec = tween(300, easing = LinearEasing)
                ) + scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                ) togetherWith fadeOut(
                    animationSpec = tween(200, easing = LinearEasing)
                )
            },
            label = "skin_transition"
        ) { currentSkin ->
            // Provide current skin to all children
            CompositionLocalProvider(
                LocalSkinTheme provides currentSkin
            ) {
                content()
            }
        }
    }
}

/**
 * Helper to access current skin in any composable
 */
@Composable
fun currentSkin(): SkinTheme = LocalSkinTheme.current
```

### 2.4 Example Skin Implementation - Vintage 80s Indian

```kotlin
// skins/Vintage80sIndianSkin.kt
package com.indieradio.ui.theme.skin.skins

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.indieradio.R
import com.indieradio.ui.theme.skin.*

// Color Scheme
val Vintage80sColorScheme = SkinColorScheme(
    primary = Color(0xFF8B4513),        // Saddle brown (wood)
    secondary = Color(0xFFD4AF37),      // Gold (metallic accents)
    background = Color(0xFF2C1810),     // Dark brown
    surface = Color(0xFF4A3728),        // Medium brown
    onPrimary = Color(0xFFFFF8DC),      // Cornsilk
    onSecondary = Color(0xFF1A1A1A),    // Near black
    onBackground = Color(0xFFFFF8DC),   // Cornsilk
    onSurface = Color(0xFFFFF8DC),      // Cornsilk

    // Custom colors
    dialForeground = Color(0xFFD4AF37), // Gold dial markings
    dialBackground = Color(0xFF1A1A1A), // Black dial background
    knobColor = Color(0xFFD4AF37),      // Gold knobs
    frequencyText = Color(0xFFFFE87C),  // Warm yellow (like old displays)
    ledIndicator = Color(0xFFFF4500)    // Orange-red LED
)

// Typography
val VintageDisplayFont = FontFamily(
    Font(R.font.vintage_display, FontWeight.Normal)
)

val Vintage80sTypography = SkinTypography(
    frequencyDisplay = TextStyle(
        fontFamily = VintageDisplayFont,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        color = Color(0xFFFFE87C)
    ),
    stationName = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFFFFF8DC)
    ),
    controlLabel = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        color = Color(0xFFD4AF37)
    ),
    metadata = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
        color = Color(0xFFB8B8B8)
    )
)

// Shapes
val Vintage80sShapes = SkinShapes(
    radioBodyShape = RoundedCornerShape(12.dp),
    knobShape = androidx.compose.foundation.shape.CircleShape,
    buttonShape = RoundedCornerShape(4.dp),
    dialShape = RoundedCornerShape(8.dp)
)

// Assets
val Vintage80sAssets = SkinAssets(
    radioBodyBackground = R.drawable.skin_vintage80s_radio_body,
    frequencyDialImage = R.drawable.skin_vintage80s_frequency_dial,
    knobImage = R.drawable.skin_vintage80s_knob,
    textureOverlay = R.drawable.skin_vintage80s_fabric_texture,
    switchSound = R.raw.skin_vintage80s_switch_sound,
    hasVisualizer = false,
    visualizerType = VisualizerType.NONE
)
```

### 2.5 Example Skin-Specific Component

```kotlin
// components/skinned/vintage/VintageFrequencyDial.kt
package com.indieradio.ui.components.skinned.vintage

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.indieradio.ui.theme.skin.currentSkin
import kotlin.math.atan2

@Composable
fun VintageFrequencyDial(
    frequency: Float,
    onFrequencyChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val skin = currentSkin()
    val colors = skin.colors
    val typography = skin.typography
    val assets = skin.assets

    // Frequency range: 88.0 to 108.0 MHz (FM)
    val minFreq = 88.0f
    val maxFreq = 108.0f

    // Rotation angle based on frequency
    val targetAngle = remember(frequency) {
        ((frequency - minFreq) / (maxFreq - minFreq)) * 270f - 135f
    }

    val animatedAngle by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "dial_rotation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f),
        contentAlignment = Alignment.Center
    ) {
        // Background dial plate
        assets.frequencyDialImage?.let { dialRes ->
            Image(
                painter = painterResource(id = dialRes),
                contentDescription = "Frequency dial background",
                modifier = Modifier.fillMaxSize()
            )
        }

        // Frequency marks and numbers
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2.5f

            // Draw frequency marks
            for (i in 88..108 step 2) {
                val angle = ((i - minFreq) / (maxFreq - minFreq)) * 270f - 135f
                rotate(angle, pivot = Offset(centerX, centerY)) {
                    val startY = centerY - radius
                    val endY = startY - 20f
                    drawLine(
                        color = colors.dialForeground,
                        start = Offset(centerX, startY),
                        end = Offset(centerX, endY),
                        strokeWidth = 2f
                    )
                }
            }
        }

        // Dial pointer/needle
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val angle = atan2(
                            change.position.y - center.y,
                            change.position.x - center.x
                        )
                        // Convert angle to frequency
                        val normalizedAngle = (angle * 180f / Math.PI.toFloat() + 90f + 360f) % 360f
                        if (normalizedAngle in 0f..270f) {
                            val newFrequency = minFreq + (normalizedAngle / 270f) * (maxFreq - minFreq)
                            onFrequencyChange(newFrequency)
                        }
                    }
                }
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension / 2.5f

            rotate(animatedAngle, pivot = Offset(centerX, centerY)) {
                // Draw needle
                drawLine(
                    color = colors.ledIndicator,
                    start = Offset(centerX, centerY),
                    end = Offset(centerX, centerY - radius),
                    strokeWidth = 4f
                )

                // Draw needle tip (triangle)
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(centerX, centerY - radius - 15f)
                    lineTo(centerX - 6f, centerY - radius)
                    lineTo(centerX + 6f, centerY - radius)
                    close()
                }
                drawPath(path, color = colors.ledIndicator)
            }
        }

        // Frequency display
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format("%.1f", frequency),
                style = typography.frequencyDisplay
            )
            Text(
                text = "MHz",
                style = typography.controlLabel
            )
        }
    }
}
```

### 2.6 State Management - ViewModel

```kotlin
// SkinViewModel.kt
package com.indieradio.ui.theme.skin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indieradio.data.repository.SkinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkinViewModel @Inject constructor(
    private val skinRepository: SkinRepository
) : ViewModel() {

    private val _currentSkin = MutableStateFlow<SkinTheme>(SkinTheme.ModernMinimal)
    val currentSkin: StateFlow<SkinTheme> = _currentSkin.asStateFlow()

    private val _isTransitioning = MutableStateFlow(false)
    val isTransitioning: StateFlow<Boolean> = _isTransitioning.asStateFlow()

    init {
        loadSavedSkin()
    }

    private fun loadSavedSkin() {
        viewModelScope.launch {
            skinRepository.getSavedSkinId().collect { skinId ->
                _currentSkin.value = SkinTheme.fromId(skinId)
            }
        }
    }

    fun switchSkin(newSkin: SkinTheme) {
        viewModelScope.launch {
            _isTransitioning.value = true

            // Preload skin assets
            skinRepository.preloadSkinAssets(newSkin)

            // Play switch sound if available
            newSkin.assets.switchSound?.let { soundRes ->
                skinRepository.playSwitchSound(soundRes)
            }

            // Switch skin
            _currentSkin.value = newSkin

            // Save preference
            skinRepository.saveSkinPreference(newSkin.id)

            _isTransitioning.value = false
        }
    }

    fun getAllSkins(): List<SkinTheme> = SkinTheme.all()
}
```

### 2.7 Main Screen Implementation

```kotlin
// screens/RadioPlayerScreen.kt
package com.indieradio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.indieradio.ui.components.skinned.modern.ModernRadioPlayer
import com.indieradio.ui.components.skinned.vintage.VintageRadioPlayer
import com.indieradio.ui.theme.skin.*

@Composable
fun RadioPlayerScreen(
    skinViewModel: SkinViewModel = hiltViewModel(),
    radioViewModel: RadioViewModel = hiltViewModel()
) {
    val currentSkin by skinViewModel.currentSkin.collectAsState()
    val radioState by radioViewModel.radioState.collectAsState()

    IndieRadioTheme(skinTheme = currentSkin) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(currentSkin.colors.background)
        ) {
            // Render skin-specific player
            when (currentSkin) {
                is SkinTheme.Vintage80sIndian -> VintageRadioPlayer(
                    radioState = radioState,
                    onFrequencyChange = { radioViewModel.tuneToFrequency(it) },
                    onVolumeChange = { radioViewModel.setVolume(it) },
                    onPlayPause = { radioViewModel.togglePlayback() }
                )

                is SkinTheme.ModernMinimal -> ModernRadioPlayer(
                    radioState = radioState,
                    onFrequencyChange = { radioViewModel.tuneToFrequency(it) },
                    onVolumeChange = { radioViewModel.setVolume(it) },
                    onPlayPause = { radioViewModel.togglePlayback() }
                )

                is SkinTheme.RetroWinamp -> RetroRadioPlayer(
                    radioState = radioState,
                    onFrequencyChange = { radioViewModel.tuneToFrequency(it) },
                    onVolumeChange = { radioViewModel.setVolume(it) },
                    onPlayPause = { radioViewModel.togglePlayback() }
                )
            }
        }
    }
}
```

---

## 3. Asset Organization Strategy

### 3.1 Resource Qualifiers Approach

Use custom resource qualifiers for skin-specific assets:

```
res/
├── drawable/                          # Common assets
├── drawable-skin-vintage80s/          # Vintage skin assets
├── drawable-skin-modern/              # Modern skin assets
├── drawable-skin-retro/               # Retro skin assets
├── font/                              # All custom fonts
└── raw/                               # Audio files, animations
```

### 3.2 Asset Loading Helper

```kotlin
// SkinAssetLoader.kt
package com.indieradio.ui.theme.skin

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object SkinAssetLoader {

    fun getDrawableForSkin(
        context: Context,
        skinId: String,
        assetName: String
    ): Int? {
        val resourceName = "skin_${skinId}_${assetName}"
        return context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        ).takeIf { it != 0 }
    }

    @Composable
    fun getSkinDrawable(assetName: String): Int? {
        val context = LocalContext.current
        val skin = currentSkin()
        return getDrawableForSkin(context, skin.id, assetName)
    }
}
```

---

## 4. Responsive Design for Vintage UIs

### 4.1 Window Size Classes

```kotlin
// ResponsiveSkinLayout.kt
package com.indieradio.ui.components.common

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun <T> rememberSkinResponsiveValue(
    compact: T,
    medium: T = compact,
    expanded: T = medium
): T {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    return when {
        screenWidthDp < 600.dp -> compact
        screenWidthDp < 840.dp -> medium
        else -> expanded
    }
}

// Usage in skin components
@Composable
fun VintageRadioPlayer(...) {
    val radioSize = rememberSkinResponsiveValue(
        compact = 320.dp,
        medium = 450.dp,
        expanded = 600.dp
    )

    val dialSize = rememberSkinResponsiveValue(
        compact = 180.dp,
        medium = 250.dp,
        expanded = 350.dp
    )

    // Use these responsive values in layout
}
```

### 4.2 Aspect Ratio Preservation

For vintage radios, maintain authentic proportions:

```kotlin
@Composable
fun VintageRadioBody(
    modifier: Modifier = Modifier
) {
    // Vintage radios typically had 16:9 or 4:3 aspect ratios
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)  // Preserve authentic proportions
            .padding(16.dp)
    ) {
        // Radio components
    }
}
```

---

## 5. Smooth Transitions Between Skins

### 5.1 AnimatedContent for Layout Changes

```kotlin
// SkinTransition.kt
package com.indieradio.ui.theme.skin

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable

@Composable
fun SkinSwitchTransition(
    currentSkin: SkinTheme,
    content: @Composable (SkinTheme) -> Unit
) {
    AnimatedContent(
        targetState = currentSkin,
        transitionSpec = {
            // Fade + scale for smooth transition
            (fadeIn(
                animationSpec = tween(400, easing = LinearEasing)
            ) + scaleIn(
                initialScale = 0.92f,
                animationSpec = tween(400, easing = FastOutSlowInEasing)
            )).togetherWith(
                fadeOut(
                    animationSpec = tween(300, easing = LinearEasing)
                ) + scaleOut(
                    targetScale = 0.92f,
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            ).using(
                // Disable clipping for smooth transitions
                SizeTransform(clip = false)
            )
        },
        label = "skin_switch"
    ) { skin ->
        content(skin)
    }
}
```

### 5.2 Shared Element Transitions

For elements that exist across skins (like frequency display):

```kotlin
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope

@Composable
fun RadioPlayerWithSharedElements() {
    SharedTransitionLayout {
        // Common elements can animate smoothly between skins
        // This is useful for frequency display, volume, etc.
    }
}
```

---

## 6. Audio Visualizer Implementation

### 6.1 Modern Waveform Visualizer

```kotlin
// visualizer/WaveformVisualizer.kt
package com.indieradio.ui.visualizer

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.indieradio.ui.theme.skin.currentSkin

@Composable
fun WaveformVisualizer(
    audioData: FloatArray,
    modifier: Modifier = Modifier
) {
    val skin = currentSkin()
    val colors = skin.colors

    // Animate waveform
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerY = height / 2

        val path = Path()
        path.moveTo(0f, centerY)

        // Draw waveform based on audio data
        audioData.forEachIndexed { index, amplitude ->
            val x = (index.toFloat() / audioData.size) * width
            val y = centerY + (amplitude * height / 2)
            path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = colors.primary,
            style = Stroke(width = 3f)
        )
    }
}
```

---

## 7. Best Practices Summary

### 7.1 Performance Optimization

1. **Lazy Asset Loading**: Load skin assets on-demand, not all at once
2. **Image Caching**: Use Coil or Glide for efficient image loading
3. **Composition Stability**: Use `@Immutable` and `@Stable` annotations
4. **Avoid Recomposition**: Use `remember`, `derivedStateOf` for computed values

```kotlin
@Composable
fun SkinComponent() {
    val skin = currentSkin()

    // ✅ Good: Computed values are remembered
    val dialColor = remember(skin) {
        skin.colors.dialForeground.copy(alpha = 0.8f)
    }

    // ❌ Bad: Computed on every recomposition
    val badDialColor = skin.colors.dialForeground.copy(alpha = 0.8f)
}
```

### 7.2 Testing Strategy

```kotlin
// Test different skins
@Test
fun testSkinSwitching() {
    composeTestRule.setContent {
        var currentSkin by remember { mutableStateOf(SkinTheme.ModernMinimal) }

        IndieRadioTheme(skinTheme = currentSkin) {
            Button(onClick = { currentSkin = SkinTheme.Vintage80sIndian }) {
                Text("Switch")
            }
        }
    }

    composeTestRule.onNodeWithText("Switch").performClick()
    // Assert skin changed
}
```

### 7.3 Accessibility

Ensure all skins support:
- **TalkBack**: Proper content descriptions
- **High Contrast**: Readable text on all backgrounds
- **Touch Targets**: Minimum 48dp touch targets
- **Haptic Feedback**: For physical control interactions

```kotlin
@Composable
fun VintageKnob(
    value: Float,
    onValueChange: (Float) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .size(80.dp)
            .semantics {
                contentDescription = "Volume knob, current value $value"
                role = Role.Slider
            }
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    // Update value
                }
            }
    ) {
        // Knob UI
    }
}
```

---

## 8. Comparison: Jetpack Compose vs XML Views

| Aspect | Jetpack Compose (Recommended) | XML Views |
|--------|-------------------------------|-----------|
| **Dynamic Layouts** | ✅ Excellent - composables can change structure completely | ⚠️ Limited - requires LayoutInflater and fragment swapping |
| **Runtime Theming** | ✅ Native support with CompositionLocal | ⚠️ Requires activity recreation or complex view updating |
| **Animation** | ✅ Built-in, declarative animations | ❌ Requires manual animation setup |
| **Code Reuse** | ✅ Composables are reusable functions | ⚠️ Requires XML includes and custom views |
| **Type Safety** | ✅ Compile-time type checking | ❌ Runtime errors with wrong resource types |
| **Learning Curve** | ⚠️ Requires Kotlin knowledge | ✅ Familiar to existing Android devs |
| **Performance** | ✅ Smart recomposition, skip unnecessary updates | ⚠️ Full view hierarchy traversal |
| **Modern Support** | ✅ Actively developed (BOM 2025.12.01) | ⚠️ Maintenance mode |

**Verdict**: Jetpack Compose is the clear winner for a skinning system requiring completely different layouts.

---

## 9. Existing Libraries and Patterns

### 9.1 Relevant Libraries (2025-2026)

1. **Material 3 Dynamic Color** - For adaptive colors
   ```kotlin
   dependencies {
       implementation "androidx.compose.material3:material3:1.3.0"
   }
   ```

2. **Accompanist System UI Controller** - For system bar theming
   ```kotlin
   implementation "com.google.accompanist:accompanist-systemuicontroller:0.34.0"
   ```

3. **Coil for Compose** - Efficient image loading
   ```kotlin
   implementation "io.coil-kt:coil-compose:2.6.0"
   ```

4. **ExoPlayer (Media3)** - Audio playback
   ```kotlin
   implementation "androidx.media3:media3-exoplayer:1.4.0"
   implementation "androidx.media3:media3-ui:1.4.0"
   ```

5. **Compose Waveform Library** - Audio visualization
   ```kotlin
   implementation "com.github.karya-inc:Waveform:1.0.0"
   ```

### 9.2 Inspiration from Existing Projects

- **Xenamp** (GitHub: djshaji/WinampSkin) - Winamp-style skinning engine for Android
- **Poweramp** - Commercial app with extensive theming
- **AIMP** - Desktop audio player with excellent skin system

---

## 10. Implementation Roadmap

### Phase 1: Foundation (Weeks 1-2)
- ✅ Set up Jetpack Compose project structure
- ✅ Define `SkinTheme` sealed class and core abstractions
- ✅ Implement CompositionLocal theme provider
- ✅ Create basic theme switching mechanism

### Phase 2: First Skin (Weeks 3-4)
- ✅ Design and implement Modern Minimal skin
- ✅ Create basic radio controls (play, pause, frequency)
- ✅ Implement audio playback with Media3
- ✅ Add waveform visualizer

### Phase 3: Additional Skins (Weeks 5-8)
- ✅ Design Vintage 80s Indian skin with authentic assets
- ✅ Create vintage-specific components (dials, knobs)
- ✅ Design Retro Winamp skin
- ✅ Implement LED spectrum visualizer for retro skin

### Phase 4: Polish (Weeks 9-10)
- ✅ Smooth transitions between skins
- ✅ Responsive layouts for all screen sizes
- ✅ Accessibility improvements
- ✅ Performance optimization

### Phase 5: Additional Features (Weeks 11-12)
- ✅ Skin customization options
- ✅ User-created skins (if applicable)
- ✅ Haptic feedback
- ✅ Sound effects for UI interactions

---

## 11. Key Takeaways

### ✅ Recommended Approach

1. **Use Jetpack Compose** for maximum flexibility and modern development
2. **Custom Design System** with CompositionLocal for theme propagation
3. **Skin-specific Composables** - completely different UI structures per skin
4. **Resource Qualifiers** for asset organization
5. **AnimatedContent** for smooth transitions
6. **Window Size Classes** for responsive vintage designs
7. **Media3 + Compose** for audio playback and visualization

### ⚠️ What to Avoid

1. ❌ Don't use Material Theme alone - it's limited to colors/typography
2. ❌ Don't rely on XML themes - they can't switch layouts dynamically
3. ❌ Don't load all skin assets upfront - use lazy loading
4. ❌ Don't hardcode screen sizes - use responsive utilities
5. ❌ Don't skip accessibility - support TalkBack and high contrast

### 🎯 Core Principle

> **Think of skins as completely different apps within one codebase**
> Each skin should be able to have its own component structure, not just different colors.

---

## 12. Sources

### Official Android Documentation
- [Anatomy of a theme in Compose](https://developer.android.com/develop/ui/compose/designsystems/anatomy)
- [Theming in Compose with Material 3](https://developer.android.com/codelabs/jetpack-compose-theming)
- [Custom design systems in Compose](https://developer.android.com/develop/ui/compose/designsystems/custom)
- [Support different display sizes](https://developer.android.com/develop/ui/compose/layouts/adaptive/support-different-display-sizes)
- [Resources in Compose](https://developer.android.com/develop/ui/compose/resources)
- [Quick guide to Animations in Compose](https://developer.android.com/develop/ui/compose/animation/quick-guide)

### Community Resources
- [Mastering Android Jetpack Compose Theming and Styles](https://30dayscoding.com/blog/implementing-android-jetpack-compose-theming-and-styles)
- [Theme switching in Android Jetpack Compose](https://medium.com/@EazSoftware/theme-switching-in-android-jetpack-compose-68ec577a11bd)
- [Composition Locals in Jetpack Compose: A Beginner-to-Advanced Guide](https://proandroiddev.com/composition-locals-in-jetpack-compose-a-beginner-to-advanced-guide-e6a812ca7620)
- [Multi theme in jetpack compose](https://medium.com/@mohammadjoumani/multi-theme-in-jetpack-compose-db2eb7d4d187)
- [Jetpack Compose Animations: Master Smooth UI Transitions in 2025](https://medium.com/@sixtinbydizora/animations-in-compose-master-smooth-ui-transitions-9739176248c6)

### Open Source Projects
- [Xenamp - Winamp Skin Clone for Android](https://github.com/djshaji/WinampSkin)
- [Compose Audio Controls](https://github.com/ygorluizfrazao/compose-audio-controls)
- [Waveform Visualizer Library](https://github.com/karya-inc/Waveform)
- [Music Player with Jetpack Compose](https://github.com/jslowinski/MusicPlayer)

### Design Resources
- [Exploring 16 Trending UI Design Styles for 2025](https://medium.com/@thisara2000shehankavinda/exploring-16-trending-ui-design-styles-for-2025-322dfa4b57c2)
- [Retro App Designs on Dribbble](https://dribbble.com/tags/retro-app)
- [Winamp Skin Museum](https://skins.webamp.org/)

---

**Document Version**: 1.0
**Last Updated**: January 2, 2026
**Author**: Architecture Research for Indieradio Project
**Technology Stack**: Kotlin, Jetpack Compose, Material 3, Media3, Android SDK 35
