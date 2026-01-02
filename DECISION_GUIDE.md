# Architecture Decision Guide

This document helps you make informed decisions during implementation by outlining trade-offs and recommendations for key architectural choices.

---

## Decision 1: Jetpack Compose vs XML Views

### Recommendation: **Jetpack Compose** ✅

### Reasoning:

| Factor | Compose | XML Views | Winner |
|--------|---------|-----------|--------|
| **Dynamic Layouts** | Can change entire component tree at runtime | Requires activity recreation or complex fragment swapping | Compose |
| **Code Maintainability** | Single source of truth in Kotlin | Split between XML and Kotlin | Compose |
| **Runtime Theme Switching** | Native with CompositionLocal | Requires manual view updates or restarts | Compose |
| **Animation Support** | Built-in, declarative | Manual setup, verbose | Compose |
| **Learning Curve** | Steeper for beginners | Familiar to Android devs | XML |
| **Industry Direction** | Future of Android UI (2026) | Legacy/maintenance mode | Compose |
| **Performance** | Smart recomposition, skips unnecessary work | Full view hierarchy traversal | Compose |
| **Type Safety** | Compile-time checking | Runtime errors | Compose |

### When to Consider XML:
- Team has zero Kotlin/Compose experience AND no time to learn
- Need to integrate with heavy legacy codebase
- **Neither applies to Indieradio** → Use Compose

---

## Decision 2: Theme Architecture Pattern

### Options:

#### Option A: Material Theme Extension (Simple)
```kotlin
MaterialTheme(
    colorScheme = when (skin) {
        Vintage -> vintageColors
        Modern -> modernColors
    }
) { content() }
```

**Pros:**
- Quick to implement
- Leverages Material Design
- Good for color/typography changes

**Cons:**
- ❌ Limited to Material's structure
- ❌ Can't change layouts
- ❌ All skins must follow Material patterns

**Verdict:** ❌ Not suitable for Indieradio (needs different layouts)

---

#### Option B: Custom CompositionLocal (Recommended) ✅
```kotlin
val LocalSkinTheme = compositionLocalOf<SkinTheme> { ... }

@Composable
fun IndieRadioTheme(skin: SkinTheme) {
    CompositionLocalProvider(LocalSkinTheme provides skin) {
        content()
    }
}
```

**Pros:**
- ✅ Complete flexibility
- ✅ Can change entire UI structure
- ✅ Type-safe
- ✅ Follows Compose best practices

**Cons:**
- Requires more initial setup
- Custom solution (not library-based)

**Verdict:** ✅ Best for Indieradio's needs

---

#### Option C: Hybrid (Material + Custom)
```kotlin
CompositionLocalProvider(LocalSkinTheme provides skin) {
    MaterialTheme(colorScheme = skin.materialColors) {
        content()
    }
}
```

**Pros:**
- Best of both worlds
- Leverage Material components where applicable
- Custom layouts where needed

**Cons:**
- Slight complexity increase
- Two theming layers

**Verdict:** ⚠️ Good alternative if using Material components heavily

---

### Recommendation: **Option B (Custom CompositionLocal)** for maximum flexibility

---

## Decision 3: Asset Organization Strategy

### Options:

#### Option A: Resource Qualifiers
```
res/
├── drawable-skin-modern/
├── drawable-skin-vintage/
└── drawable-skin-retro/
```

**Pros:**
- ✅ Built-in Android mechanism
- ✅ Automatic resource selection
- ✅ Easy to understand

**Cons:**
- ❌ Not a standard qualifier (need custom build logic)
- ❌ All assets in compiled APK

**Verdict:** ⚠️ Good but increases APK size

---

#### Option B: Asset Bundles (Play Asset Delivery)
```kotlin
// Download skin assets on-demand
assetPackManager.fetch(listOf("skin-vintage"))
```

**Pros:**
- ✅ Smaller initial APK
- ✅ Download skins on-demand
- ✅ Can update skins without app update

**Cons:**
- ❌ Complex setup
- ❌ Requires network for first use
- ❌ Overkill for initial version

**Verdict:** 🔮 Future enhancement, not initial version

---

#### Option C: Embedded Assets with Lazy Loading
```kotlin
// All assets in APK, load lazily
val image = remember(skin) {
    loadSkinAsset(context, skin.id, "radio_body")
}
```

**Pros:**
- ✅ Simple implementation
- ✅ Works offline
- ✅ On-demand loading reduces memory

**Cons:**
- ❌ Large APK size (if many skins)

**Verdict:** ✅ Recommended for initial version (3-5 skins)

---

### Recommendation: **Option C** initially, migrate to **Option B** if needed

---

## Decision 4: Skin Switching Mechanism

### Options:

#### Option A: Activity Recreation
```kotlin
activity.recreate() // Restart activity
```

**Pros:**
- Simple
- Guaranteed fresh state

**Cons:**
- ❌ Jarring user experience
- ❌ Interrupts audio playback
- ❌ Loses transient state

**Verdict:** ❌ Not acceptable for music app

---

#### Option B: State-Driven Recomposition (Recommended) ✅
```kotlin
var currentSkin by remember { mutableStateOf(SkinTheme.Modern) }
```

**Pros:**
- ✅ Smooth transitions
- ✅ Maintains audio playback
- ✅ Preserves app state
- ✅ Compose-native

**Cons:**
- None significant

**Verdict:** ✅ Clear winner

---

#### Option C: Fragment Replacement (XML approach)
```kotlin
fragmentManager.replace(R.id.container, VintageFragment())
```

**Pros:**
- Works with XML
- Familiar pattern

**Cons:**
- ❌ Only relevant for XML views
- ❌ More complex than Compose state

**Verdict:** ❌ Not applicable (using Compose)

---

### Recommendation: **Option B (State-Driven)** - leverages Compose strengths

---

## Decision 5: Audio Visualizer Implementation

### Options:

#### Option A: Canvas Drawing (Custom)
```kotlin
Canvas(modifier) {
    // Draw waveform manually
}
```

**Pros:**
- ✅ Complete control
- ✅ Can match skin aesthetic exactly
- ✅ No dependencies

**Cons:**
- ❌ More code to maintain
- ❌ Need to handle audio analysis

**When to use:** Vintage skins with unique visualizer styles

---

#### Option B: Third-Party Library (e.g., Waveform library)
```kotlin
Waveform(
    audioData = audioData,
    modifier = modifier
)
```

**Pros:**
- ✅ Quick implementation
- ✅ Tested and optimized
- ✅ Less code

**Cons:**
- ❌ Less customization
- ❌ External dependency
- ❌ May not match vintage aesthetics

**When to use:** Modern skins with standard visualizers

---

#### Option C: Hybrid (Library + Custom)
```kotlin
when (skin.visualizerType) {
    WAVEFORM -> WaveformLibrary(...)
    SPECTRUM -> SpectrumLibrary(...)
    VINTAGE_NEEDLE -> CustomVintageVisualizer(...)
}
```

**Pros:**
- ✅ Best of both worlds
- ✅ Use library where appropriate
- ✅ Custom where needed

**Cons:**
- Slightly increased complexity

**Verdict:** ✅ Recommended approach

---

### Recommendation: **Option C (Hybrid)** - pragmatic balance

---

## Decision 6: State Management Pattern

### Options:

#### Option A: ViewModel with StateFlow (Recommended) ✅
```kotlin
@HiltViewModel
class SkinViewModel @Inject constructor(...) : ViewModel() {
    private val _currentSkin = MutableStateFlow<SkinTheme>(...)
    val currentSkin: StateFlow<SkinTheme> = _currentSkin.asStateFlow()
}
```

**Pros:**
- ✅ Survives configuration changes
- ✅ Separation of concerns
- ✅ Testable
- ✅ Android recommended pattern

**Cons:**
- Requires dependency injection setup

**Verdict:** ✅ Industry standard, highly recommended

---

#### Option B: Compose State Only
```kotlin
@Composable
fun App() {
    var skin by remember { mutableStateOf(SkinTheme.Modern) }
}
```

**Pros:**
- ✅ Simple
- ✅ No ViewModel needed

**Cons:**
- ❌ Lost on configuration change (screen rotation)
- ❌ Business logic in UI layer
- ❌ Harder to test

**Verdict:** ❌ Too simplistic for production app

---

#### Option C: MVI Architecture
```kotlin
sealed class SkinIntent {
    data class SelectSkin(val skin: SkinTheme) : SkinIntent()
}

data class SkinState(
    val currentSkin: SkinTheme,
    val availableSkins: List<SkinTheme>
)
```

**Pros:**
- ✅ Very structured
- ✅ Highly testable
- ✅ Predictable state changes

**Cons:**
- ❌ More boilerplate
- ❌ Overkill for simple skin switching

**Verdict:** ⚠️ Good for complex apps, overkill here

---

### Recommendation: **Option A (ViewModel + StateFlow)** - right balance

---

## Decision 7: Dependency Injection

### Options:

#### Option A: Hilt (Recommended) ✅
```kotlin
@HiltViewModel
class SkinViewModel @Inject constructor(
    private val skinRepository: SkinRepository
)
```

**Pros:**
- ✅ Official Android DI solution
- ✅ Compile-time safety
- ✅ Great IDE support
- ✅ Less boilerplate than Dagger

**Cons:**
- Annotation processing overhead

**Verdict:** ✅ Best for Android apps

---

#### Option B: Koin
```kotlin
val appModule = module {
    viewModel { SkinViewModel(get()) }
    single { SkinRepository(get()) }
}
```

**Pros:**
- ✅ Pure Kotlin
- ✅ Easy to learn
- ✅ No code generation

**Cons:**
- ❌ Runtime DI (crashes at runtime, not compile time)
- ❌ Slightly slower than Hilt

**Verdict:** ⚠️ Good alternative if team prefers simplicity

---

#### Option C: Manual Injection
```kotlin
val repository = SkinRepository(context)
val viewModel = SkinViewModel(repository)
```

**Pros:**
- ✅ No library needed
- ✅ Full control

**Cons:**
- ❌ Verbose
- ❌ Hard to test
- ❌ Doesn't scale

**Verdict:** ❌ Not recommended for production

---

### Recommendation: **Hilt** - industry standard for Android

---

## Decision 8: Responsive Design Strategy

### Options:

#### Option A: Window Size Classes (Recommended) ✅
```kotlin
val windowSize = calculateWindowSizeClass(this)

when (windowSize.widthSizeClass) {
    WindowWidthSizeClass.Compact -> CompactLayout()
    WindowWidthSizeClass.Medium -> MediumLayout()
    WindowWidthSizeClass.Expanded -> ExpandedLayout()
}
```

**Pros:**
- ✅ Official Android approach
- ✅ Works across all form factors
- ✅ Material 3 compatible

**Cons:**
- None significant

**Verdict:** ✅ Recommended

---

#### Option B: Custom Breakpoints
```kotlin
val screenWidth = LocalConfiguration.current.screenWidthDp

when {
    screenWidth < 600 -> CompactLayout()
    screenWidth < 840 -> MediumLayout()
    else -> ExpandedLayout()
}
```

**Pros:**
- ✅ Full control over breakpoints
- ✅ Can fine-tune for specific designs

**Cons:**
- ❌ Reinventing the wheel
- ❌ Manual maintenance

**Verdict:** ⚠️ Use only if Window Size Classes don't fit

---

#### Option C: Adaptive Layout Modifiers
```kotlin
Box(
    modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(16f / 9f)
)
```

**Pros:**
- ✅ Simple
- ✅ Works with any screen

**Cons:**
- ❌ Limited to aspect ratio preservation
- ❌ Doesn't adapt content

**Verdict:** ⚠️ Use as complement, not sole strategy

---

### Recommendation: **Option A (Window Size Classes)** + **Option C (Aspect Ratios)**

---

## Decision 9: Persistence Strategy

### Options:

#### Option A: DataStore Preferences (Recommended) ✅
```kotlin
val SKIN_ID = stringPreferencesKey("skin_id")

context.dataStore.edit { prefs ->
    prefs[SKIN_ID] = "vintage"
}
```

**Pros:**
- ✅ Kotlin-first
- ✅ Type-safe
- ✅ Coroutine-based
- ✅ Official Android recommendation (replaces SharedPreferences)

**Cons:**
- None for simple key-value storage

**Verdict:** ✅ Best choice for settings

---

#### Option B: SharedPreferences
```kotlin
sharedPrefs.edit()
    .putString("skin_id", "vintage")
    .apply()
```

**Pros:**
- ✅ Familiar
- ✅ Simple API

**Cons:**
- ❌ Legacy approach
- ❌ Not type-safe
- ❌ Blocking I/O

**Verdict:** ❌ Use DataStore instead

---

#### Option C: Room Database
```kotlin
@Dao
interface SettingsDao {
    @Query("SELECT skin_id FROM settings")
    fun getSkinId(): Flow<String>
}
```

**Pros:**
- ✅ Structured data
- ✅ Complex queries

**Cons:**
- ❌ Overkill for simple settings
- ❌ More setup

**Verdict:** ❌ Not needed for skin preference alone

---

### Recommendation: **DataStore Preferences** - modern and efficient

---

## Decision 10: Animation Strategy

### Options:

#### Option A: AnimatedContent (Recommended) ✅
```kotlin
AnimatedContent(
    targetState = currentSkin,
    transitionSpec = {
        fadeIn() + scaleIn() togetherWith fadeOut() + scaleOut()
    }
) { skin ->
    SkinPlayer(skin)
}
```

**Pros:**
- ✅ Declarative
- ✅ Smooth crossfade
- ✅ Built-in to Compose

**Cons:**
- None for this use case

**Verdict:** ✅ Perfect for skin switching

---

#### Option B: Crossfade
```kotlin
Crossfade(targetState = currentSkin) { skin ->
    SkinPlayer(skin)
}
```

**Pros:**
- ✅ Simple
- ✅ Smooth

**Cons:**
- ❌ Only fade, no scale/slide
- ❌ Less control

**Verdict:** ⚠️ Too basic, use AnimatedContent

---

#### Option C: Shared Element Transitions
```kotlin
SharedTransitionLayout {
    // Animate frequency display between skins
}
```

**Pros:**
- ✅ Sophisticated
- ✅ Maintains visual continuity

**Cons:**
- ❌ Complex setup
- ❌ Requires careful planning
- ❌ Overkill for Indieradio

**Verdict:** 🔮 Future enhancement, not initial version

---

### Recommendation: **AnimatedContent** - right balance of smoothness and simplicity

---

## Summary: Recommended Tech Stack

```
┌─────────────────────────────────────────────────────────┐
│              INDIERADIO TECH STACK 2026                 │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  UI Framework:        Jetpack Compose (BOM 2025.12.01) │
│  Language:            Kotlin 1.9+                       │
│  Min SDK:             24 (Android 7.0)                  │
│  Target SDK:          35 (Android 15)                   │
│                                                         │
│  Architecture:        MVVM + Repository Pattern         │
│  State Management:    ViewModel + StateFlow             │
│  Dependency Injection: Hilt                             │
│  Persistence:         DataStore Preferences             │
│                                                         │
│  Theming:             Custom CompositionLocal           │
│  Animation:           AnimatedContent + Canvas          │
│  Responsive Design:   Window Size Classes               │
│                                                         │
│  Media Playback:      Media3 ExoPlayer                  │
│  Audio Visualization: Hybrid (Library + Custom)         │
│  Image Loading:       Coil Compose                      │
│  Async:               Kotlin Coroutines + Flow          │
│                                                         │
│  Testing:             JUnit 4, Compose Test, Mockk      │
│  Build:               Gradle Kotlin DSL                 │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## Quick Decision Matrix

| Requirement | Solution | Rationale |
|-------------|----------|-----------|
| Multiple completely different UIs | Jetpack Compose + Custom CompositionLocal | Only way to change entire layouts at runtime |
| Smooth skin transitions | AnimatedContent with fade + scale | Built-in, declarative, smooth |
| Asset management | Embedded with lazy loading | Simple, works offline, good for 3-5 skins |
| Responsive vintage UIs | Window Size Classes + AspectRatio | Official approach + authentic proportions |
| Audio visualizers | Hybrid (library for modern, custom for vintage) | Pragmatic balance |
| State persistence | DataStore Preferences | Modern, type-safe, recommended |
| Dependency injection | Hilt | Official, compile-time safe |
| State management | ViewModel + StateFlow | Survives config changes, testable |

---

## Red Flags to Avoid

❌ **Don't** use XML Views - can't change layouts at runtime smoothly
❌ **Don't** recreate Activity for skin switching - breaks audio playback
❌ **Don't** use only Material Theme - too restrictive for custom skins
❌ **Don't** load all skin assets upfront - memory waste
❌ **Don't** hardcode screen sizes - use responsive utilities
❌ **Don't** skip accessibility - support TalkBack from day one
❌ **Don't** use SharedPreferences - deprecated, use DataStore
❌ **Don't** use manual DI - use Hilt for scalability

---

## Implementation Priority

### Phase 1: Foundation (Must Have)
1. ✅ Jetpack Compose setup
2. ✅ Custom SkinTheme sealed class
3. ✅ CompositionLocal provider
4. ✅ Basic Modern skin
5. ✅ SkinViewModel + StateFlow
6. ✅ DataStore persistence

### Phase 2: Core Features (Must Have)
1. ✅ Vintage 80s skin with custom components
2. ✅ AnimatedContent transitions
3. ✅ Window Size Classes
4. ✅ Basic audio playback (Media3)

### Phase 3: Polish (Should Have)
1. ✅ Audio visualizers
2. ✅ Retro Winamp skin
3. ✅ Haptic feedback
4. ✅ Sound effects

### Phase 4: Advanced (Nice to Have)
1. ⚠️ Play Asset Delivery for skins
2. ⚠️ User-customizable skins
3. ⚠️ Shared element transitions
4. ⚠️ Advanced visualizer effects

---

## Questions to Ask During Development

1. **Does this skin need a completely different layout?**
   - Yes → Create new skin-specific composable
   - No → Can reuse with different theme values

2. **Is this asset used by all skins?**
   - Yes → Put in common `drawable/`
   - No → Put in skin-specific folder

3. **Does this state need to survive configuration changes?**
   - Yes → Put in ViewModel
   - No → Can use `remember { }`

4. **Does this calculation happen on every recomposition?**
   - Yes → Wrap in `remember` or `derivedStateOf`
   - No → OK as-is

5. **Is this animation jarring?**
   - Yes → Adjust duration/easing in AnimatedContent
   - No → Good to go

6. **Does this work on tablets?**
   - Test with Window Size Class variations
   - Use aspectRatio for vintage components

---

This guide should help you make confident decisions throughout development. When in doubt, refer back to the recommended tech stack and red flags to avoid!
