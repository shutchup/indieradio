# Indieradio Skinning System - Visual Architecture

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            INDIERADIO APP                                    │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      │
                    ┌─────────────────┴─────────────────┐
                    │                                   │
         ┌──────────▼──────────┐           ┌───────────▼──────────┐
         │   MainActivity      │           │   IndieRadioApp      │
         │   (Entry Point)     │           │   (Application)      │
         └──────────┬──────────┘           └──────────────────────┘
                    │                               │
                    │ setContent()                  │ @HiltAndroidApp
                    │                               │
         ┌──────────▼──────────────────────────────┴───────────────┐
         │              IndieRadioTheme Composable                 │
         │  ┌───────────────────────────────────────────────────┐  │
         │  │  CompositionLocalProvider(LocalSkinTheme)        │  │
         │  │                                                   │  │
         │  │  ┌─────────────────────────────────────────┐     │  │
         │  │  │  AnimatedContent (Skin Transition)      │     │  │
         │  │  │                                         │     │  │
         │  │  │  Content with Current Skin Applied      │     │  │
         │  │  └─────────────────────────────────────────┘     │  │
         │  └───────────────────────────────────────────────────┘  │
         └─────────────────────────┬────────────────────────────────┘
                                   │
                    ┌──────────────┴──────────────┐
                    │                             │
         ┌──────────▼──────────┐       ┌──────────▼──────────┐
         │   RadioScreen       │       │   SkinViewModel     │
         │   (Main UI)         │◄──────┤   (State Mgmt)      │
         └──────────┬──────────┘       └──────────┬──────────┘
                    │                             │
                    │ observes                    │ manages
                    │ currentSkin                 │ skin state
                    │                             │
         ┌──────────▼──────────────────┬──────────▼──────────┐
         │                             │                     │
    ┌────▼─────────┐          ┌───────▼────────┐   ┌────────▼────────┐
    │ When Modern  │          │  When Vintage  │   │  SkinRepository │
    │              │          │                │   │  (Persistence)  │
    │ ModernRadio  │          │  VintageRadio  │   └─────────────────┘
    │ Player       │          │  Player        │            │
    └────┬─────────┘          └───────┬────────┘            │
         │                            │                     │
         └────────────┬───────────────┘              DataStore
                      │                              Preferences
         ┌────────────▼─────────────┐
         │    Skin-Specific         │
         │    Components            │
         │                          │
         │  ├─ FrequencyDial        │
         │  ├─ VolumeControl        │
         │  ├─ PlayControls         │
         │  └─ Visualizer (if any)  │
         └──────────────────────────┘
```

## Data Flow - Skin Switching

```
┌─────────────┐
│   User      │
│  Taps       │
│ "Change     │
│  Skin"      │
└─────┬───────┘
      │
      │ UI Event
      │
┌─────▼────────────────────┐
│  SkinViewModel           │
│  .selectSkin(newSkin)    │
└─────┬────────────────────┘
      │
      │ 1. Update State
      │
┌─────▼─────────────────────┐
│ _currentSkin.value =      │
│ newSkin                   │
└─────┬─────────────────────┘
      │
      │ 2. Persist Choice
      │
┌─────▼──────────────────────┐
│ SkinRepository             │
│ .saveSkinId(skinId)        │
└─────┬──────────────────────┘
      │
      │ 3. Save to DataStore
      │
┌─────▼──────────────────────┐
│ DataStore Preferences      │
│ [skin_id: "vintage"]       │
└─────┬──────────────────────┘
      │
      │ 4. State Change Triggers Recomposition
      │
┌─────▼──────────────────────┐
│ IndieRadioTheme            │
│ observes currentSkin       │
└─────┬──────────────────────┘
      │
      │ 5. AnimatedContent Transition
      │
┌─────▼──────────────────────┐
│ Fade Out Old Skin          │
│ Fade In New Skin           │
└─────┬──────────────────────┘
      │
      │ 6. Render New Layout
      │
┌─────▼──────────────────────┐
│ VintageRadioPlayer         │
│ (completely different UI)  │
└────────────────────────────┘
```

## Skin Theme Structure

```
SkinTheme (Sealed Class)
│
├─ id: String
├─ name: String
├─ era: String
├─ description: String
│
├─ colors: SkinColorScheme
│   ├─ background
│   ├─ surface
│   ├─ primary
│   ├─ onBackground
│   ├─ onSurface
│   ├─ dialColor
│   ├─ ledIndicator
│   └─ ...custom colors
│
├─ typography: SkinTypography
│   ├─ frequencyDisplay
│   ├─ stationName
│   ├─ controlLabel
│   └─ metadata
│
├─ shapes: SkinShapes
│   ├─ radioBodyShape
│   ├─ knobShape
│   ├─ buttonShape
│   └─ dialShape
│
└─ assets: SkinAssets
    ├─ radioBodyBackground (Drawable)
    ├─ frequencyDialImage (Drawable)
    ├─ knobImage (Drawable)
    ├─ textureOverlay (Drawable)
    ├─ switchSound (Raw Audio)
    ├─ hasVisualizer (Boolean)
    └─ visualizerType (Enum)
```

## Resource Organization

```
app/src/main/res/
│
├── drawable/                      # Shared resources
│   └── ic_launcher.png
│
├── drawable-skin-modern/          # Modern skin resources
│   ├── radio_body_bg.png
│   ├── visualizer_bg.png
│   └── button_play.xml
│
├── drawable-skin-vintage80s/      # Vintage 80s resources
│   ├── radio_body_wood.png
│   ├── frequency_dial.png
│   ├── knob_volume.png
│   ├── knob_tuning.png
│   └── fabric_texture.png
│
├── drawable-skin-retro/           # Retro Winamp resources
│   ├── winamp_body.png
│   ├── led_display_bg.png
│   └── spectrum_bars.png
│
├── font/                          # All fonts
│   ├── modern_sans.ttf
│   ├── vintage_display.ttf
│   └── retro_led.ttf
│
└── raw/                           # Audio and data files
    ├── skin_modern_switch.mp3
    ├── skin_vintage_switch.mp3
    └── skin_retro_switch.mp3
```

## Component Hierarchy - Modern Skin

```
ModernRadioPlayer
│
├── Column (Main Layout)
│   │
│   ├── Text (Frequency Display)
│   │   └── "95.0" in large font
│   │
│   ├── Text (Unit Label)
│   │   └── "MHz"
│   │
│   ├── Slider (Frequency Tuner)
│   │   ├── range: 88-108 MHz
│   │   └── smooth animations
│   │
│   ├── WaveformVisualizer
│   │   ├── Canvas drawing
│   │   ├── Real-time audio data
│   │   └── Animated waveforms
│   │
│   └── FloatingActionButton (Play/Pause)
│       └── Icon (Play or Pause)
│
└── Modifiers
    ├── fillMaxSize()
    ├── background(skin.colors.background)
    └── padding(32.dp)
```

## Component Hierarchy - Vintage Skin

```
VintageRadioPlayer
│
├── Box (Radio Body)
│   │
│   ├── Column (Main Components)
│   │   │
│   │   ├── Box (Frequency Display Window)
│   │   │   ├── Background: vintage_display_bg
│   │   │   └── Text: "95.0 MHz" in retro font
│   │   │
│   │   ├── VintageFrequencyDial
│   │   │   ├── Image (Dial Background)
│   │   │   ├── Canvas (Dial Marks)
│   │   │   ├── Rotatable Needle
│   │   │   └── Drag Gesture Detection
│   │   │
│   │   ├── Row (Control Knobs)
│   │   │   ├── VintageKnob (Volume)
│   │   │   └── VintageKnob (Tone)
│   │   │
│   │   └── Button (Vintage Style Play)
│   │       └── Rectangular, brown color
│   │
│   └── Image (Fabric Texture Overlay)
│
└── Modifiers
    ├── aspectRatio(16/9)
    ├── background(wood texture)
    ├── border(vintage style)
    └── rounded corners
```

## Animation Flow - Skin Transition

```
Initial State                Transition                 Final State
┌──────────────┐            ┌──────────────┐          ┌──────────────┐
│              │            │              │          │              │
│   Modern     │   fadeOut  │              │ fadeIn   │   Vintage    │
│              ├───────────►│   Crossfade  ├─────────►│              │
│   Player     │  scaleOut  │              │ scaleIn  │   Player     │
│              │            │              │          │              │
└──────────────┘            └──────────────┘          └──────────────┘
   alpha: 1.0                 alpha: 0.5                alpha: 1.0
   scale: 1.0                 scale: 0.95               scale: 1.0

   Duration: 0ms              Duration: 200-300ms       Duration: 300ms
```

## Responsive Breakpoints

```
Screen Size            Layout Strategy              Example Component Size
─────────────────────────────────────────────────────────────────────────
< 600dp (Phone)       Compact Layout               Radio: 320dp wide
                      ├─ Vertical stacking         Dial:  180dp diameter
                      ├─ Single column             Knobs:  60dp diameter
                      └─ Touch-optimized 48dp min

600-840dp (Tablet)    Medium Layout                Radio: 450dp wide
                      ├─ More spacing              Dial:  250dp diameter
                      ├─ Larger controls           Knobs:  80dp diameter
                      └─ Enhanced visualizers

> 840dp (Large)       Expanded Layout              Radio: 600dp wide
                      ├─ Side-by-side elements     Dial:  350dp diameter
                      ├─ Maximum detail            Knobs: 100dp diameter
                      └─ Full visualizer canvas
```

## State Management Pattern

```
┌──────────────────────────────────────────────────────────────┐
│                    ViewModel Layer                            │
│  ┌────────────────────┐         ┌─────────────────────┐     │
│  │  SkinViewModel     │         │  RadioViewModel     │     │
│  │                    │         │                     │     │
│  │  - currentSkin     │         │  - isPlaying        │     │
│  │  - isTransitioning │         │  - frequency        │     │
│  │                    │         │  - volume           │     │
│  │  + selectSkin()    │         │  + tuneFrequency()  │     │
│  └─────────┬──────────┘         └──────────┬──────────┘     │
└────────────┼────────────────────────────────┼────────────────┘
             │                                │
             │ StateFlow                      │ StateFlow
             │                                │
┌────────────▼────────────────────────────────▼────────────────┐
│                     UI Layer (Composables)                    │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  RadioScreen (observes both ViewModels)              │   │
│  │  ├─ collectAsState() on currentSkin                  │   │
│  │  ├─ collectAsState() on radioState                   │   │
│  │  └─ Passes state down to skin-specific players       │   │
│  └──────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────┘
             │                                │
             │ User Events                    │ User Events
             │                                │
┌────────────▼────────────────────────────────▼────────────────┐
│               Repository Layer                                │
│  ┌──────────────────────┐    ┌──────────────────────┐       │
│  │  SkinRepository      │    │  RadioRepository     │       │
│  │  (DataStore)         │    │  (Media3 ExoPlayer)  │       │
│  └──────────────────────┘    └──────────────────────┘       │
└───────────────────────────────────────────────────────────────┘
```

## Dependency Injection Graph (Hilt)

```
@HiltAndroidApp
IndieRadioApp
      │
      ├─ @Singleton Modules
      │     │
      │     ├─ RepositoryModule
      │     │     ├─ provides SkinRepository(@ApplicationContext)
      │     │     └─ provides RadioRepository(@ApplicationContext)
      │     │
      │     └─ NetworkModule (if streaming)
      │           └─ provides OkHttpClient, Retrofit
      │
      └─ @AndroidEntryPoint
            MainActivity
                  │
                  └─ @Composable Screen
                         │
                         ├─ @HiltViewModel SkinViewModel
                         │     ← injects SkinRepository
                         │
                         └─ @HiltViewModel RadioViewModel
                               ← injects RadioRepository
```

## Performance Optimization Checklist

```
✓ Use @Immutable / @Stable for theme data classes
   └─ Reduces unnecessary recompositions

✓ Remember computed values
   └─ val dialColor = remember(skin) { skin.colors.primary.copy(alpha = 0.8f) }

✓ Use derivedStateOf for expensive calculations
   └─ val angle by remember { derivedStateOf { calculateAngle(frequency) } }

✓ Lazy load skin assets
   └─ Load assets only when skin is selected

✓ Use Coil for image loading with caching
   └─ AsyncImage with MemoryCache and DiskCache

✓ Optimize Canvas drawings
   └─ Use drawBehind modifier instead of Canvas composable when possible

✓ Limit recomposition scope
   └─ Move frequently changing state to leaf composables

✓ Use LaunchedEffect for side effects
   └─ LaunchedEffect(skin) { preloadAssets(skin) }
```

## Testing Strategy Pyramid

```
                        ┌─────────────┐
                        │     E2E     │
                        │   Tests     │
                        │             │
                        │  - Switch   │
                        │    skins    │
                        │  - Verify   │
                        │    UI       │
                        └─────────────┘
                       ┌───────────────┐
                       │  Integration  │
                       │     Tests     │
                       │               │
                       │ - ViewModel + │
                       │   Repository  │
                       │ - Composables │
                       │   with theme  │
                       └───────────────┘
                 ┌─────────────────────────┐
                 │      Unit Tests          │
                 │                          │
                 │ - SkinTheme logic        │
                 │ - Repository CRUD        │
                 │ - ViewModel state        │
                 │ - Asset loading          │
                 └──────────────────────────┘
```

## Folder Structure Summary

```
indieradio/
├── app/src/main/
│   ├── kotlin/com/indieradio/
│   │   ├── IndieRadioApp.kt
│   │   ├── MainActivity.kt
│   │   │
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   └── RadioState.kt
│   │   │   └── repository/
│   │   │       ├── SkinRepository.kt
│   │   │       └── RadioRepository.kt
│   │   │
│   │   ├── domain/
│   │   │   └── usecase/
│   │   │       ├── SwitchSkinUseCase.kt
│   │   │       └── TuneRadioUseCase.kt
│   │   │
│   │   ├── ui/
│   │   │   ├── theme/
│   │   │   │   ├── Color.kt
│   │   │   │   ├── Typography.kt
│   │   │   │   ├── Shape.kt
│   │   │   │   ├── Theme.kt
│   │   │   │   └── skin/
│   │   │   │       ├── SkinTheme.kt
│   │   │   │       ├── SkinCompositionLocals.kt
│   │   │   │       └── skins/
│   │   │   │           ├── ModernSkin.kt
│   │   │   │           ├── VintageSkin.kt
│   │   │   │           └── RetroSkin.kt
│   │   │   │
│   │   │   ├── components/
│   │   │   │   ├── common/
│   │   │   │   │   └── ResponsiveHelpers.kt
│   │   │   │   └── skinned/
│   │   │   │       ├── modern/
│   │   │   │       │   ├── ModernRadioPlayer.kt
│   │   │   │       │   └── ModernVisualizer.kt
│   │   │   │       └── vintage/
│   │   │   │           ├── VintageRadioPlayer.kt
│   │   │   │           ├── VintageFrequencyDial.kt
│   │   │   │           └── VintageKnob.kt
│   │   │   │
│   │   │   ├── screens/
│   │   │   │   └── RadioPlayerScreen.kt
│   │   │   │
│   │   │   └── viewmodel/
│   │   │       ├── SkinViewModel.kt
│   │   │       └── RadioViewModel.kt
│   │   │
│   │   └── di/
│   │       ├── AppModule.kt
│   │       └── RepositoryModule.kt
│   │
│   └── res/
│       ├── drawable/
│       ├── drawable-skin-modern/
│       ├── drawable-skin-vintage80s/
│       ├── drawable-skin-retro/
│       ├── font/
│       ├── raw/
│       └── values/
│
├── build.gradle.kts
├── SKINNING_ARCHITECTURE.md
├── QUICK_START_EXAMPLE.md
└── ARCHITECTURE_DIAGRAM.md (this file)
```

This visual architecture provides a clear overview of how all components interact in the Indieradio skinning system!
