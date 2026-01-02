# Indieradio - Android Radio App with Winamp-Style Skins

A modern Android radio streaming app featuring multiple retro-inspired UI skins that completely transform the interface, similar to classic Winamp skins.

## Documentation Overview

This repository contains comprehensive architecture research and implementation guides for building the Indieradio skinning system:

### 📚 Documentation Files

1. **[SKINNING_ARCHITECTURE.md](./SKINNING_ARCHITECTURE.md)** ⭐ **START HERE**
   - Complete architectural design and rationale
   - Detailed code examples with full implementation
   - Best practices for theming, assets, animations
   - Comparison of approaches (Compose vs XML)
   - Performance optimization strategies
   - Testing and accessibility guidelines
   - ~10,000 words of comprehensive guidance

2. **[QUICK_START_EXAMPLE.md](./QUICK_START_EXAMPLE.md)** 🚀 **FOR RAPID PROTOTYPING**
   - Minimal working example you can copy-paste
   - Complete code for basic skinning system
   - Two skins: Modern and Vintage 80s
   - All necessary setup (Gradle, ViewModels, etc.)
   - Perfect for getting started in minutes

3. **[ARCHITECTURE_DIAGRAM.md](./ARCHITECTURE_DIAGRAM.md)** 📊 **VISUAL REFERENCE**
   - ASCII diagrams of system architecture
   - Data flow visualizations
   - Component hierarchy
   - Resource organization
   - Dependency graphs
   - Performance optimization checklist

4. **[DECISION_GUIDE.md](./DECISION_GUIDE.md)** 🎯 **FOR IMPLEMENTATION DECISIONS**
   - Trade-off analysis for each major decision
   - Comparison tables for different approaches
   - Recommended tech stack summary
   - Red flags to avoid
   - Implementation priority guide
   - Questions to ask during development

---

## Quick Summary: Recommended Architecture

### Core Technology Stack (2026)

```
✅ UI Framework:         Jetpack Compose (BOM 2025.12.01)
✅ Architecture:         MVVM + Repository Pattern
✅ State Management:     ViewModel + StateFlow
✅ Dependency Injection: Hilt
✅ Theming:              Custom CompositionLocal
✅ Animation:            AnimatedContent
✅ Media Playback:       Media3 ExoPlayer
✅ Persistence:          DataStore Preferences
✅ Image Loading:        Coil Compose
```

### Why Jetpack Compose?

**For a skinning system that needs completely different layouts (not just colors), Jetpack Compose is the only viable modern approach.**

- ✅ Can change entire UI structure at runtime
- ✅ Smooth animated transitions between skins
- ✅ Declarative, maintainable code
- ✅ Type-safe theming
- ✅ Future-proof (active development in 2026)

XML Views would require activity recreation or complex fragment swapping, both unacceptable for a music player that needs uninterrupted audio playback.

---

## Planned Skins

1. **Modern Minimal** (2020s)
   - Clean, flat design
   - Waveform visualizer
   - Smooth gradients and rounded corners
   - Material 3 inspired

2. **Vintage 80s Indian Radio** (1980s)
   - Authentic transistor radio aesthetic
   - Wooden texture background
   - Analog frequency dial with rotating needle
   - Vintage knobs and buttons
   - Retro LED-style frequency display
   - No visualizer (period-accurate)

3. **Retro Winamp** (1990s)
   - Classic Winamp 2.x inspired
   - LED spectrum analyzer
   - Pixelated fonts
   - Skeuomorphic buttons
   - Nostalgia-inducing design

4. **Art Deco Radio** (Future)
   - 1920s-30s aesthetic
   - Geometric patterns
   - Gold and black color scheme
   - Elegant typography

---

## Key Features

### Complete UI Transformation
Unlike typical Android theming (which only changes colors), Indieradio skins completely transform:
- ✅ Layouts and component structures
- ✅ Control types (dials vs sliders vs knobs)
- ✅ Background images and textures
- ✅ Typography and fonts
- ✅ Animations and transitions
- ✅ Audio visualizers (modern skins)
- ✅ Sound effects

### Runtime Skin Switching
- Switch skins instantly without restarting
- Smooth animated transitions
- Audio playback continues uninterrupted
- Skin preference persisted across sessions

### Responsive Design
- Adapts to phones, tablets, and foldables
- Vintage designs maintain authentic aspect ratios
- Touch targets optimized for each screen size

### Accessibility
- TalkBack support
- High contrast modes
- Minimum 48dp touch targets
- Haptic feedback for physical controls

---

## Architecture Highlights

### Skin Theme Structure

```kotlin
sealed class SkinTheme {
    abstract val id: String
    abstract val colors: SkinColorScheme
    abstract val typography: SkinTypography
    abstract val shapes: SkinShapes
    abstract val assets: SkinAssets

    data object Vintage80sIndian : SkinTheme() { ... }
    data object ModernMinimal : SkinTheme() { ... }
}
```

### Theme Provision with CompositionLocal

```kotlin
val LocalSkinTheme = compositionLocalOf<SkinTheme> { ... }

@Composable
fun IndieRadioTheme(skin: SkinTheme, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSkinTheme provides skin) {
        AnimatedContent(targetState = skin) { currentSkin ->
            content()
        }
    }
}
```

### Skin-Specific Components

```kotlin
@Composable
fun RadioPlayerScreen() {
    val skin = currentSkin()

    when (skin) {
        is SkinTheme.Vintage80sIndian -> VintageRadioPlayer(...)
        is SkinTheme.ModernMinimal -> ModernRadioPlayer(...)
        is SkinTheme.RetroWinamp -> RetroRadioPlayer(...)
    }
}
```

---

## Project Structure

```
app/src/main/kotlin/com/indieradio/
├── ui/
│   ├── theme/
│   │   └── skin/
│   │       ├── SkinTheme.kt              # Core skin abstraction
│   │       ├── SkinCompositionLocals.kt  # Theme provider
│   │       └── skins/
│   │           ├── Vintage80sIndianSkin.kt
│   │           ├── ModernMinimalSkin.kt
│   │           └── RetroWinampSkin.kt
│   │
│   ├── components/
│   │   ├── common/                       # Abstract interfaces
│   │   └── skinned/                      # Skin-specific implementations
│   │       ├── vintage/
│   │       ├── modern/
│   │       └── retro/
│   │
│   ├── screens/
│   │   └── RadioPlayerScreen.kt
│   │
│   └── viewmodel/
│       ├── SkinViewModel.kt
│       └── RadioViewModel.kt
│
├── data/
│   └── repository/
│       ├── SkinRepository.kt
│       └── RadioRepository.kt
│
└── domain/
    └── usecase/
        └── SwitchSkinUseCase.kt
```

---

## Implementation Roadmap

### ✅ Phase 1: Foundation (Weeks 1-2)
- Set up Jetpack Compose project
- Define SkinTheme sealed class
- Implement CompositionLocal provider
- Basic theme switching

### ✅ Phase 2: First Skin (Weeks 3-4)
- Modern Minimal skin
- Basic radio controls
- Audio playback with Media3
- Waveform visualizer

### ✅ Phase 3: Additional Skins (Weeks 5-8)
- Vintage 80s Indian skin
- Vintage-specific components (dials, knobs)
- Retro Winamp skin
- LED spectrum visualizer

### ✅ Phase 4: Polish (Weeks 9-10)
- Smooth transitions
- Responsive layouts
- Accessibility
- Performance optimization

### 🔮 Phase 5: Advanced Features (Future)
- User-customizable skins
- Downloadable skin packs
- Skin editor
- Community skin sharing

---

## Getting Started

### Prerequisites
- Android Studio Koala or later
- Kotlin 1.9+
- Android SDK 35 (API level 35)
- Minimum SDK 24 (Android 7.0)

### Quick Start

1. **Read the architecture**
   ```bash
   # Start with main architecture doc
   cat SKINNING_ARCHITECTURE.md
   ```

2. **Try the minimal example**
   ```bash
   # Copy code from quick start
   cat QUICK_START_EXAMPLE.md
   ```

3. **Refer to diagrams as needed**
   ```bash
   cat ARCHITECTURE_DIAGRAM.md
   ```

4. **Make informed decisions**
   ```bash
   cat DECISION_GUIDE.md
   ```

---

## Key Dependencies

```kotlin
dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2025.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")

    // ViewModel & Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-compiler:2.51")

    // Media3 for audio
    implementation("androidx.media3:media3-exoplayer:1.4.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Coil for images
    implementation("io.coil-kt:coil-compose:2.6.0")
}
```

---

## Research Sources

This architecture is based on extensive research of Android best practices in 2025-2026:

### Official Android Documentation
- [Anatomy of a theme in Compose](https://developer.android.com/develop/ui/compose/designsystems/anatomy)
- [Custom design systems in Compose](https://developer.android.com/develop/ui/compose/designsystems/custom)
- [Support different display sizes](https://developer.android.com/develop/ui/compose/layouts/adaptive/support-different-display-sizes)
- [Animations in Compose](https://developer.android.com/develop/ui/compose/animation/quick-guide)

### Community Resources
- [Composition Locals Guide](https://proandroiddev.com/composition-locals-in-jetpack-compose-a-beginner-to-advanced-guide-e6a812ca7620)
- [Multi-theme in Jetpack Compose](https://medium.com/@mohammadjoumani/multi-theme-in-jetpack-compose-db2eb7d4d187)
- [Jetpack Compose Animations 2025](https://medium.com/@sixtinbydizora/animations-in-compose-master-smooth-ui-transitions-9739176248c6)

### Open Source Inspiration
- [Xenamp - Winamp Skin Clone](https://github.com/djshaji/WinampSkin)
- [Compose Audio Controls](https://github.com/ygorluizfrazao/compose-audio-controls)
- [Waveform Visualizer Library](https://github.com/karya-inc/Waveform)

**Full source list in SKINNING_ARCHITECTURE.md**

---

## Why This Architecture?

### Problem
Traditional Android theming can change colors and typography, but **cannot change layouts at runtime**. For a Winamp-style skin system where each skin has completely different UI structures (dials vs sliders, different control layouts, etc.), we need a more flexible approach.

### Solution
**Custom CompositionLocal-based theming with Jetpack Compose** allows:
- ✅ Complete layout flexibility per skin
- ✅ Runtime switching without activity recreation
- ✅ Smooth animated transitions
- ✅ Type-safe theme access throughout the app
- ✅ Maintains audio playback during switches

### Alternative Approaches (Rejected)
- ❌ **XML Views**: Requires activity recreation or complex fragment management
- ❌ **Material Theme only**: Limited to colors/typography/shapes
- ❌ **Activity per skin**: Breaks audio playback, poor UX

---

## Design Philosophy

### Think Different
> "Each skin is a completely different app within one codebase"

Don't think of skins as "color schemes." Think of them as:
- Different eras of radio design
- Different interaction paradigms
- Different visual languages
- Different user experiences

### Period Accuracy
Vintage skins should feel authentic:
- No modern visualizers on 80s radios
- Authentic control types (knobs, not sliders)
- Period-appropriate fonts and colors
- Realistic textures and materials

### Modern Performance
Despite vintage aesthetics:
- Smooth 60 FPS animations
- Responsive touch interactions
- Efficient rendering
- Fast skin switching

---

## Contributing

This is currently a research/planning phase. Implementation will begin once the architecture is validated.

---

## License

TBD

---

## Contact

For questions about the architecture or implementation, refer to the documentation files or open an issue.

---

## Acknowledgments

- Inspired by Winamp's legendary skinning system
- Built on modern Android best practices (2026)
- Community research and open-source projects

---

**Last Updated**: January 2, 2026
**Status**: Architecture Planning Phase
**Next Steps**: Begin implementation of Phase 1
