# Indieradio - Master Project Plan
**A Minimalist Android Radio Streaming App with Vintage UI**

Version: 1.0
Date: January 2026
Status: Planning Phase

---

## 📋 Table of Contents

1. [Executive Summary](#executive-summary)
2. [Project Vision](#project-vision)
3. [Core Features](#core-features)
4. [Technical Architecture](#technical-architecture)
5. [Technology Stack](#technology-stack)
6. [Development Phases](#development-phases)
7. [Detailed Feature Breakdown](#detailed-feature-breakdown)
8. [Design System](#design-system)
9. [Project Structure](#project-structure)
10. [Risk Assessment](#risk-assessment)
11. [Success Metrics](#success-metrics)
12. [Next Steps](#next-steps)

---

## 1. Executive Summary

**Indieradio** is a minimalist Android app that streams radio stations from around the world with a unique vintage aesthetic inspired by 1980s Indian radio designs. The app features multiple switchable "skins" (like Winamp) that completely transform the UI, from authentic vintage radio interfaces to modern visualizer-driven designs.

### Key Differentiators
- ✨ **Authentic vintage UI**: Mimics physical radio interfaces from the 80s era
- 🎨 **Multiple skins**: Complete UI transformations, not just color themes
- 🌍 **Global reach**: 30,000+ stations via Radio Browser API
- 📍 **Location-aware**: Automatically finds local stations
- 🎵 **Zero friction**: No API keys, no registration, completely free

---

## 2. Project Vision

### The Problem
Modern radio apps are cluttered with ads, complex UIs, and generic designs that lack personality. They've lost the charm and simplicity of physical radios.

### The Solution
Indieradio brings back the joy of tuning a physical radio with:
- **Nostalgic experience**: Turn dials, twist knobs, see frequencies on analog scales
- **Modern convenience**: Global stations, instant access, favorites, search
- **Personalization**: Choose your preferred era/style (vintage, retro, modern)
- **Minimalism**: Focus on the music, not the interface

### Target Audience
- **Nostalgia seekers** (30-50 years): Remember physical radios, want that experience
- **Design enthusiasts**: Appreciate unique, thoughtful UI design
- **International users**: Want access to stations from their home countries
- **Minimalists**: Prefer simple, focused apps over feature-bloated alternatives

---

## 3. Core Features

### 3.1 Radio Playback (MVP)
| Feature | Description | Priority |
|---------|-------------|----------|
| **Stream radio stations** | Play internet radio streams from global sources | P0 (Must-have) |
| **Location-based discovery** | Auto-detect country and show local stations | P0 |
| **Search stations** | Search by name, genre, country, language | P0 |
| **Favorites** | Save and quickly access favorite stations | P0 |
| **Background playback** | Continue playing when app is backgrounded | P0 |
| **Notification controls** | Play/pause/next/previous from notification | P0 |

### 3.2 Skinning System (MVP)
| Feature | Description | Priority |
|---------|-------------|----------|
| **Multiple skins** | 3-4 completely different UI designs | P0 |
| **Runtime switching** | Switch skins without restarting app | P0 |
| **Responsive design** | All skins work on phones and tablets | P0 |
| **Skin persistence** | Remember user's skin choice | P1 |

### 3.3 Vintage Radio Controls (MVP)
| Feature | Description | Priority |
|---------|-------------|----------|
| **Frequency dial** | Analog-style tuning dial (vintage skins) | P0 |
| **On/Off button** | Power toggle with authentic animations | P0 |
| **Volume knob** | Rotatable volume control (vintage) | P0 |
| **Speaker grille** | Decorative audio output visualization | P1 |
| **Station display** | Shows current station info | P0 |

### 3.4 Modern Features (Post-MVP)
| Feature | Description | Priority |
|---------|-------------|----------|
| **Audio visualizers** | Waveform/spectrum for modern skins | P1 |
| **Sleep timer** | Auto-stop after set duration | P2 |
| **Recording** | Record current stream (legal check needed) | P3 |
| **Chromecast support** | Cast to speakers/TVs | P2 |
| **Podcast support** | Expand beyond radio | P3 |

---

## 4. Technical Architecture

### 4.1 Architectural Pattern
**MVVM (Model-View-ViewModel)** with Clean Architecture principles

```
┌─────────────────────────────────────────────────┐
│              UI Layer (Compose)                 │
│  ┌──────────────────────────────────────────┐  │
│  │  Skins: Vintage | Modern | Retro | Art  │  │
│  │  Components: Dials | Knobs | Visualizers│  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
                       ↕
┌─────────────────────────────────────────────────┐
│           ViewModel Layer (State)               │
│  RadioPlayerViewModel | SkinViewModel           │
│  StationListViewModel | FavoritesViewModel      │
└─────────────────────────────────────────────────┘
                       ↕
┌─────────────────────────────────────────────────┐
│         Domain Layer (Use Cases)                │
│  PlayStationUseCase | SearchStationsUseCase     │
│  SwitchSkinUseCase  | GetLocalStationsUseCase   │
└─────────────────────────────────────────────────┘
                       ↕
┌─────────────────────────────────────────────────┐
│          Data Layer (Repository)                │
│  ┌──────────────┐  ┌──────────────────────────┐ │
│  │  Local DB    │  │   Remote API             │ │
│  │  (Room)      │  │   (Radio Browser)        │ │
│  │  - Favorites │  │   - Station search       │ │
│  │  - History   │  │   - Metadata             │ │
│  │  - Settings  │  │   - Stream URLs          │ │
│  └──────────────┘  └──────────────────────────┘ │
└─────────────────────────────────────────────────┘
                       ↕
┌─────────────────────────────────────────────────┐
│         Media Layer (ExoPlayer)                 │
│  Media3 ExoPlayer + MediaSession                │
│  Background playback, notifications, controls   │
└─────────────────────────────────────────────────┘
```

### 4.2 Key Architectural Decisions

**✅ Decision 1: Radio API**
- **Choice**: Radio Browser API (radio-browser.info)
- **Rationale**:
  - Free, no API key required
  - 30,000+ stations globally
  - Location-based search
  - Pre-resolved stream URLs
  - Active maintenance
- **Reference**: See research summary for detailed comparison

**✅ Decision 2: UI Framework**
- **Choice**: Jetpack Compose (100% Compose, no XML Views)
- **Rationale**:
  - Only way to switch complete layouts at runtime without activity recreation
  - Activity recreation would interrupt audio playback
  - Modern standard for Android (2026)
  - Better animation support
  - Type-safe theming
- **Reference**: See SKINNING_ARCHITECTURE.md for detailed analysis

**✅ Decision 3: Audio Player**
- **Choice**: ExoPlayer via Media3 framework
- **Rationale**:
  - Industry standard (YouTube, Spotify use it)
  - Better streaming support (HLS, DASH)
  - Independent of Android system updates
  - Better error handling
  - Required for audio visualizers
- **Alternative**: MediaPlayer (too limited for our needs)

**✅ Decision 4: State Management**
- **Choice**: ViewModel + StateFlow + Kotlin Coroutines
- **Rationale**:
  - Official Android recommendation
  - Lifecycle-aware
  - Survives configuration changes
  - Clean separation of concerns

**✅ Decision 5: Dependency Injection**
- **Choice**: Hilt (Dagger-based)
- **Rationale**:
  - Official Android DI solution
  - Better integration with ViewModels
  - Compile-time safety
  - Good documentation

**✅ Decision 6: Local Storage**
- **Choice**:
  - Room Database (for stations, favorites, history)
  - DataStore Preferences (for settings, skin choice)
- **Rationale**:
  - Room: Type-safe, SQLite-backed, migration support
  - DataStore: Modern replacement for SharedPreferences, async

---

## 5. Technology Stack

### 5.1 Core Dependencies

```kotlin
// build.gradle.kts (Module)
dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Jetpack Compose (BOM 2025.12.01)
    val composeBom = platform("androidx.compose:compose-bom:2025.12.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.activity:activity-compose:1.9.0")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Media3 ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.3.0")
    implementation("androidx.media3:media3-session:1.3.0")
    implementation("androidx.media3:media3-ui:1.3.0")

    // Networking (Retrofit + OkHttp)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Dependency Injection (Hilt)
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Local Storage
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Image Loading
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Location
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
```

### 5.2 Minimum SDK Requirements
- **minSdk**: 24 (Android 7.0 - Nougat)
  - Covers 96%+ of active devices (2026)
  - Required for Media3 modern features
- **targetSdk**: 35 (Android 15)
- **compileSdk**: 35

---

## 6. Development Phases

### Phase 1: Foundation & MVP Audio (Weeks 1-3)

**Goal**: Working app that can play radio stations with basic UI

#### Week 1: Project Setup
- [ ] Create new Android project with Compose
- [ ] Set up Hilt dependency injection
- [ ] Configure Gradle with all dependencies
- [ ] Set up project structure (packages, modules)
- [ ] Create basic data models (Station, SkinTheme)
- [ ] Set up Git repository and branch structure

#### Week 2: Radio API Integration
- [ ] Implement Radio Browser API client (Retrofit)
- [ ] Create StationRepository
- [ ] Implement location detection (GPS + IP fallback)
- [ ] Create station search functionality
- [ ] Implement caching strategy
- [ ] Unit tests for API client

#### Week 3: Audio Playback
- [ ] Set up Media3 ExoPlayer
- [ ] Create MediaPlaybackService (foreground service)
- [ ] Implement MediaSession for notification controls
- [ ] Create RadioPlayerViewModel
- [ ] Basic play/pause/stop functionality
- [ ] Handle network interruptions
- [ ] Background playback support

**Deliverable**: App that can search and play radio stations with system controls

---

### Phase 2: First Skin - Modern Minimal (Weeks 4-5)

**Goal**: Complete working radio app with one polished skin

#### Week 4: Modern UI Components
- [ ] Design Modern Minimal skin in Figma/sketches
- [ ] Implement base SkinTheme architecture
- [ ] Create ModernMinimalSkin composable
- [ ] Build modern frequency slider
- [ ] Build modern volume controls
- [ ] Build play/pause/stop buttons
- [ ] Station info display
- [ ] Search screen

#### Week 5: Modern Features & Polish
- [ ] Waveform audio visualizer
- [ ] Station list with infinite scroll
- [ ] Favorites functionality
- [ ] Recently played history
- [ ] Settings screen (basic)
- [ ] Error states and loading indicators
- [ ] Polish animations and transitions

**Deliverable**: Fully functional radio app with modern UI

---

### Phase 3: Vintage 80s Indian Radio Skin (Weeks 6-8)

**Goal**: Authentic vintage radio experience

#### Week 6: Vintage UI Design
- [ ] Research 80s Indian radio designs (Murphy, Philips)
- [ ] Create high-fidelity designs in Figma
- [ ] Source/create vintage assets (backgrounds, textures)
- [ ] Find period-appropriate fonts
- [ ] Design frequency dial mechanism
- [ ] Design rotary knobs (volume, tuning)

#### Week 7: Vintage Component Development
- [ ] VintageFrequencyDial composable (rotatable)
- [ ] VintageKnob composable (rotatable with haptic feedback)
- [ ] Vintage radio body with wood/metal textures
- [ ] Authentic on/off toggle switch
- [ ] LED/analog station display
- [ ] Speaker grille with subtle animation
- [ ] Handle touch gestures for rotation

#### Week 8: Vintage Polish & Integration
- [ ] Sound effects (dial click, switch toggle)
- [ ] Aging effects (scratches, wear)
- [ ] Responsive layout for different screen sizes
- [ ] Skin switching mechanism
- [ ] Smooth transitions between modern and vintage
- [ ] Save skin preference to DataStore

**Deliverable**: Two complete skins with smooth switching

---

### Phase 4: Additional Skins & Features (Weeks 9-11)

**Goal**: More skin variety and enhanced features

#### Week 9: Retro Winamp-style Skin
- [ ] Design Retro skin (inspired by Winamp, 90s aesthetics)
- [ ] LED spectrum analyzer visualizer
- [ ] Digital frequency display
- [ ] Equalizer UI (visual only, or functional)
- [ ] Playlist management UI
- [ ] Skin implementation and testing

#### Week 10: Art Deco / Fourth Skin (Optional)
- [ ] Design fourth skin (Art Deco radio, 1920s-30s)
- [ ] Ornamental UI elements
- [ ] Brass/gold color scheme
- [ ] Geometric patterns
- [ ] Implementation

#### Week 11: Enhanced Features
- [ ] Sleep timer
- [ ] Alarm/wake up to radio
- [ ] Station recommendations (based on listening)
- [ ] Share station functionality
- [ ] Improved search (filters, sorting)
- [ ] Genre browsing
- [ ] Country/language browsing

**Deliverable**: 3-4 skins + enhanced functionality

---

### Phase 5: Polish, Testing & Optimization (Weeks 12-14)

**Goal**: Production-ready app

#### Week 12: Performance Optimization
- [ ] Profile app performance
- [ ] Optimize image loading (WebP, caching)
- [ ] Reduce APK size
- [ ] Memory leak detection and fixes
- [ ] Battery usage optimization
- [ ] Network usage optimization
- [ ] Smooth 60fps on all skins

#### Week 13: Testing & Bug Fixes
- [ ] Unit tests (ViewModels, Repositories, Use Cases)
- [ ] UI tests (Compose testing)
- [ ] Integration tests
- [ ] Manual QA on multiple devices
- [ ] Edge case testing (no internet, location denied, etc.)
- [ ] Accessibility testing (TalkBack)
- [ ] Bug fixes

#### Week 14: Release Preparation
- [ ] Finalize app icon and branding
- [ ] Create Play Store screenshots (all skins)
- [ ] Write Play Store description
- [ ] Create promotional video/GIF
- [ ] Set up crash reporting (Firebase Crashlytics)
- [ ] Set up analytics (minimal, privacy-focused)
- [ ] Privacy policy
- [ ] Open source license compliance
- [ ] Beta testing (internal/closed)

**Deliverable**: Production-ready APK for Play Store

---

### Phase 6: Launch & Post-Launch (Week 15+)

#### Week 15: Launch
- [ ] Submit to Google Play Store
- [ ] Create website/landing page
- [ ] Social media announcement
- [ ] Post on Reddit (r/Android, r/androidapps)
- [ ] Product Hunt launch
- [ ] Monitor for crash reports
- [ ] Respond to user feedback

#### Post-Launch Roadmap (Future)
- [ ] Additional skins (community-created?)
- [ ] Custom skin creator/editor
- [ ] Tablet-optimized layouts
- [ ] Android Auto support
- [ ] Wear OS companion app
- [ ] Podcast integration
- [ ] Local audio file playback
- [ ] Chromecast/Google Cast support
- [ ] User-submitted stations
- [ ] Social features (share listening, friends)

---

## 7. Detailed Feature Breakdown

### 7.1 Station Discovery & Search

**User Flow**:
1. App opens → Detects location → Shows local stations
2. User can search by:
   - Station name (e.g., "BBC Radio 1")
   - Genre (e.g., "Jazz", "Classical")
   - Country (e.g., "India", "USA")
   - Language (e.g., "Hindi", "English")
3. Results displayed with:
   - Station name
   - Country flag
   - Favicon/logo
   - Genre tags
   - Bitrate/codec
   - Popularity (votes)

**Technical Implementation**:
```kotlin
// Radio Browser API endpoints
GET /json/stations/bycountrycodeexact/{countrycode}
GET /json/stations/search?name={query}
GET /json/stations/bytagexact/{tag}
GET /json/stations/bylanguageexact/{language}

// Repository method
suspend fun searchStations(query: String): Result<List<Station>>
suspend fun getStationsByCountry(countryCode: String): Result<List<Station>>
suspend fun getStationsByGenre(genre: String): Result<List<Station>>
```

### 7.2 Audio Playback

**States**:
- **Idle**: No station loaded
- **Loading**: Buffering stream
- **Playing**: Audio playing
- **Paused**: Playback paused
- **Error**: Stream failed (show error, suggest retry)

**Features**:
- Seamless stream switching (crossfade optional)
- Automatic retry on network errors
- Adaptive buffering based on connection quality
- Gapless playback when changing stations
- Remember last played station (resume on app open)

**Technical Implementation**:
```kotlin
class RadioPlayer @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val mediaSession: MediaSession
) {
    fun playStation(station: Station) {
        val mediaItem = MediaItem.fromUri(station.urlResolved)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pause() = exoPlayer.pause()
    fun resume() = exoPlayer.play()
    fun stop() = exoPlayer.stop()
}
```

### 7.3 Skinning System

**Available Skins** (MVP):

#### 1. **Modern Minimal** (Default)
- Clean, flat design
- Material 3 aesthetics
- Horizontal frequency slider
- Circular play/pause button
- Waveform visualizer
- Station artwork display
- Search bar at top
- Bottom sheet for station list
- Light/dark mode variants

#### 2. **Vintage 80s Indian Radio**
- Wood panel background
- Rotary frequency dial (270° rotation)
- Physical-looking knobs (volume, bass, treble)
- Analog frequency display (87.5 - 108.0 FM)
- Fabric speaker grille
- Toggle power switch (skeuomorphic)
- Warm color scheme (browns, golds, cream)
- Subtle texture and aging effects
- No visualizer (period-accurate)

#### 3. **Retro Winamp-style**
- Dark theme with neon accents
- LED-style digital display
- Spectrum analyzer (bars)
- Playlist drawer
- Mini/standard/equalizer views
- Metallic button appearance
- Customizable accent colors
- 90s nostalgia aesthetic

#### 4. **Art Deco Radio** (Optional)
- 1920s-30s luxury radio aesthetic
- Ornamental gold/brass details
- Geometric patterns
- Arched speaker grille
- Sunburst dial design
- Elegant typography
- Rich color palette

**Skin Switching**:
- Settings menu: "Radio Style" option
- Visual preview of each skin
- Instant switching (no restart)
- Smooth animated transition (fade + scale)
- Preference persisted to DataStore

### 7.4 Vintage Radio Controls

**Frequency Dial** (Vintage skins):
- Visual: Circular dial with frequency markings
- Interaction: Drag to rotate, snap to stations
- Haptic feedback on station snap
- Visual indicator (needle/pointer)
- Range: 87.5 - 108.0 FM (standard FM range)
- Maps to station list index (not real frequencies)

**Volume Knob** (Vintage skins):
- Visual: Circular knob with rotation indicator
- Interaction: Rotate gesture
- Range: 0-100%
- Haptic feedback
- Visual rotation animation

**On/Off Switch** (Vintage skins):
- Visual: Physical toggle switch or rotary button
- Interaction: Tap to toggle
- Animation: Switch flips, lights turn on/off
- Sound effect: Click/snap
- State persisted (remember if "on" or "off")

**Speaker Grille** (Vintage skins):
- Visual: Fabric/mesh texture
- Subtle animation: Pulsing with audio playback
- Decorative element

### 7.5 Audio Visualizers

**Waveform Visualizer** (Modern skin):
- Real-time audio amplitude display
- Smooth animations (60fps)
- Color synchronized with theme
- Low CPU usage

**Spectrum Analyzer** (Retro skin):
- FFT-based frequency bars
- LED-style bar display
- Configurable bar count (16-32)
- Logarithmic frequency scaling

**Technical Implementation**:
```kotlin
// Using ExoPlayer's audio session ID with AudioFX or custom analyzer
@Composable
fun WaveformVisualizer(audioSessionId: Int) {
    val visualizer = remember { Visualizer(audioSessionId) }
    val waveform = remember { mutableStateOf(ByteArray(0)) }

    DisposableEffect(visualizer) {
        visualizer.setDataCaptureListener(
            object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer,
                    waveform: ByteArray,
                    samplingRate: Int
                ) {
                    waveform.value = waveform
                }
                // ... onFftDataCapture for spectrum
            },
            Visualizer.getMaxCaptureRate() / 2,
            true,
            true
        )
        visualizer.enabled = true

        onDispose {
            visualizer.release()
        }
    }

    Canvas(modifier = Modifier.fillMaxWidth().height(100.dp)) {
        // Draw waveform bars
    }
}
```

### 7.6 Favorites & History

**Favorites**:
- Heart icon on station (tap to favorite)
- Dedicated "Favorites" tab
- Swipe to remove
- Sync across devices (future: Firebase)
- Export/import favorites

**History**:
- "Recently Played" section
- Last 50 stations
- Timestamp of last play
- Quick access to return to station
- Clear history option

**Technical Implementation**:
```kotlin
// Room Database
@Entity(tableName = "favorites")
data class FavoriteStation(
    @PrimaryKey val stationUuid: String,
    val name: String,
    val country: String,
    val favicon: String?,
    val urlResolved: String,
    val addedAt: Long
)

@Entity(tableName = "history")
data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val stationUuid: String,
    val playedAt: Long
)

@Dao
interface StationDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getFavorites(): Flow<List<FavoriteStation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(station: FavoriteStation)

    @Delete
    suspend fun removeFavorite(station: FavoriteStation)

    @Query("SELECT * FROM history ORDER BY playedAt DESC LIMIT 50")
    fun getHistory(): Flow<List<HistoryEntry>>

    @Insert
    suspend fun addHistoryEntry(entry: HistoryEntry)
}
```

---

## 8. Design System

### 8.1 Color Palette

**Modern Minimal Skin**:
```kotlin
val ModernLightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.Black
)

val ModernDarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.Black
)
```

**Vintage 80s Skin**:
```kotlin
val VintageColors = ColorScheme(
    primary = Color(0xFF8B4513), // Saddle brown
    secondary = Color(0xFFD4AF37), // Gold
    background = Color(0xFF3E2723), // Dark wood
    surface = Color(0xFF5D4037), // Medium wood
    onPrimary = Color(0xFFFFF8DC), // Cream
    // ... full scheme
)
```

### 8.2 Typography

**Modern Skin**:
```kotlin
val ModernTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    // ... full type scale
)
```

**Vintage Skin**:
```kotlin
// Use period-appropriate fonts
val VintageTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Serif, // Or custom vintage font
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        letterSpacing = 0.15.em
    )
    // ...
)
```

### 8.3 Responsive Layout

**Window Size Classes**:
```kotlin
@Composable
fun RadioPlayerScreen(windowSizeClass: WindowSizeClass) {
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            // Phone portrait: Single column, full-screen radio
            CompactRadioLayout()
        }
        WindowWidthSizeClass.Medium -> {
            // Phone landscape or small tablet: Two columns
            MediumRadioLayout()
        }
        WindowWidthSizeClass.Expanded -> {
            // Large tablet: Three columns (list, radio, info)
            ExpandedRadioLayout()
        }
    }
}
```

**Vintage UI Responsiveness**:
- Maintain aspect ratio of radio body
- Scale entire "radio" to fit screen
- Keep touch targets >= 48dp (accessibility)
- Adapt control sizes for larger screens

---

## 9. Project Structure

### 9.1 Module Organization

```
indieradio/
├── app/                                    # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/indieradio/
│   │   │   │   ├── IndieRadioApp.kt       # Application class
│   │   │   │   ├── MainActivity.kt        # Single Activity (Compose)
│   │   │   │   │
│   │   │   │   ├── ui/
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── skin/
│   │   │   │   │   │   │   ├── SkinTheme.kt
│   │   │   │   │   │   │   ├── SkinAssets.kt
│   │   │   │   │   │   │   ├── SkinCompositionLocals.kt
│   │   │   │   │   │   │   └── skins/
│   │   │   │   │   │   │       ├── ModernMinimalSkin.kt
│   │   │   │   │   │   │       ├── Vintage80sSkin.kt
│   │   │   │   │   │   │       ├── RetroWinampSkin.kt
│   │   │   │   │   │   │       └── ArtDecoSkin.kt
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Typography.kt
│   │   │   │   │   │   └── Shape.kt
│   │   │   │   │   │
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── common/
│   │   │   │   │   │   │   ├── RadioControl.kt
│   │   │   │   │   │   │   ├── FrequencyDisplay.kt
│   │   │   │   │   │   │   └── VolumeControl.kt
│   │   │   │   │   │   └── skinned/
│   │   │   │   │   │       ├── vintage/
│   │   │   │   │   │       │   ├── VintageFrequencyDial.kt
│   │   │   │   │   │       │   ├── VintageKnob.kt
│   │   │   │   │   │       │   ├── VintageRadioBody.kt
│   │   │   │   │   │       │   └── VintagePowerSwitch.kt
│   │   │   │   │   │       ├── modern/
│   │   │   │   │   │       │   ├── ModernFrequencySlider.kt
│   │   │   │   │   │       │   ├── ModernVisualizer.kt
│   │   │   │   │   │       │   └── ModernControls.kt
│   │   │   │   │   │       └── retro/
│   │   │   │   │   │           ├── RetroSpectrumAnalyzer.kt
│   │   │   │   │   │           └── RetroDisplay.kt
│   │   │   │   │   │
│   │   │   │   │   ├── screens/
│   │   │   │   │   │   ├── RadioPlayerScreen.kt
│   │   │   │   │   │   ├── StationListScreen.kt
│   │   │   │   │   │   ├── SearchScreen.kt
│   │   │   │   │   │   ├── FavoritesScreen.kt
│   │   │   │   │   │   └── SettingsScreen.kt
│   │   │   │   │   │
│   │   │   │   │   ├── viewmodel/
│   │   │   │   │   │   ├── RadioPlayerViewModel.kt
│   │   │   │   │   │   ├── StationListViewModel.kt
│   │   │   │   │   │   ├── SearchViewModel.kt
│   │   │   │   │   │   └── SkinViewModel.kt
│   │   │   │   │   │
│   │   │   │   │   └── navigation/
│   │   │   │   │       └── NavGraph.kt
│   │   │   │   │
│   │   │   │   ├── domain/
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── Station.kt
│   │   │   │   │   │   ├── PlaybackState.kt
│   │   │   │   │   │   └── SkinTheme.kt
│   │   │   │   │   ├── usecase/
│   │   │   │   │   │   ├── PlayStationUseCase.kt
│   │   │   │   │   │   ├── SearchStationsUseCase.kt
│   │   │   │   │   │   ├── GetLocalStationsUseCase.kt
│   │   │   │   │   │   ├── ToggleFavoriteUseCase.kt
│   │   │   │   │   │   └── SwitchSkinUseCase.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── StationRepository.kt
│   │   │   │   │       ├── FavoritesRepository.kt
│   │   │   │   │       ├── PlaybackRepository.kt
│   │   │   │   │       └── SkinRepository.kt
│   │   │   │   │
│   │   │   │   ├── data/
│   │   │   │   │   ├── remote/
│   │   │   │   │   │   ├── RadioBrowserApi.kt
│   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   │   ├── StationDto.kt
│   │   │   │   │   │   │   └── CountryDto.kt
│   │   │   │   │   │   └── mapper/
│   │   │   │   │   │       └── StationMapper.kt
│   │   │   │   │   │
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── database/
│   │   │   │   │   │   │   ├── IndieRadioDatabase.kt
│   │   │   │   │   │   │   ├── dao/
│   │   │   │   │   │   │   │   ├── StationDao.kt
│   │   │   │   │   │   │   │   ├── FavoriteDao.kt
│   │   │   │   │   │   │   │   └── HistoryDao.kt
│   │   │   │   │   │   │   └── entity/
│   │   │   │   │   │   │       ├── FavoriteEntity.kt
│   │   │   │   │   │   │       └── HistoryEntity.kt
│   │   │   │   │   │   └── datastore/
│   │   │   │   │   │       └── PreferencesManager.kt
│   │   │   │   │   │
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── StationRepositoryImpl.kt
│   │   │   │   │       ├── FavoritesRepositoryImpl.kt
│   │   │   │   │       └── SkinRepositoryImpl.kt
│   │   │   │   │
│   │   │   │   ├── player/
│   │   │   │   │   ├── RadioPlayer.kt              # ExoPlayer wrapper
│   │   │   │   │   ├── MediaPlaybackService.kt     # Foreground service
│   │   │   │   │   ├── MediaSessionManager.kt      # Notification controls
│   │   │   │   │   └── visualizer/
│   │   │   │   │       ├── AudioVisualizer.kt
│   │   │   │   │       ├── WaveformAnalyzer.kt
│   │   │   │   │       └── SpectrumAnalyzer.kt
│   │   │   │   │
│   │   │   │   ├── util/
│   │   │   │   │   ├── LocationProvider.kt
│   │   │   │   │   ├── NetworkMonitor.kt
│   │   │   │   │   └── Constants.kt
│   │   │   │   │
│   │   │   │   └── di/
│   │   │   │       ├── AppModule.kt
│   │   │   │       ├── NetworkModule.kt
│   │   │   │       ├── DatabaseModule.kt
│   │   │   │       └── PlayerModule.kt
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/                       # Common assets
│   │   │   │   ├── drawable-skin-modern/           # Modern skin assets
│   │   │   │   ├── drawable-skin-vintage80s/       # Vintage skin assets
│   │   │   │   ├── drawable-skin-retro/            # Retro skin assets
│   │   │   │   ├── font/                           # Custom fonts
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml                  # Minimal (Compose-first)
│   │   │   │   └── xml/
│   │   │   │       └── network_security_config.xml # HTTP cleartext for streams
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   ├── test/                                   # Unit tests
│   │   │   └── kotlin/com/indieradio/
│   │   │       ├── viewmodel/
│   │   │       ├── repository/
│   │   │       └── usecase/
│   │   │
│   │   └── androidTest/                            # Instrumentation tests
│   │       └── kotlin/com/indieradio/
│   │           └── ui/
│   │
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── buildSrc/                                       # Dependency management (optional)
│   └── src/main/kotlin/
│       └── Dependencies.kt
│
├── gradle/
│   └── libs.versions.toml                          # Version catalog (optional)
│
├── build.gradle.kts                                # Root build file
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
│
├── .gitignore
├── LICENSE
├── README.md
├── SKINNING_ARCHITECTURE.md                        # Technical docs (this file)
├── QUICK_START_EXAMPLE.md
├── ARCHITECTURE_DIAGRAM.md
└── DECISION_GUIDE.md
```

### 9.2 Package Responsibilities

| Package | Responsibility |
|---------|----------------|
| `ui/theme/skin/` | Skin definitions, theme system, CompositionLocals |
| `ui/components/` | Reusable UI components (both common and skin-specific) |
| `ui/screens/` | Full-screen composables for each app screen |
| `ui/viewmodel/` | ViewModels for screen state management |
| `ui/navigation/` | Navigation graph and routing |
| `domain/model/` | Core business models (Station, PlaybackState) |
| `domain/usecase/` | Business logic operations |
| `domain/repository/` | Repository interfaces |
| `data/remote/` | API clients, DTOs, network logic |
| `data/local/` | Database, DataStore, entities |
| `data/repository/` | Repository implementations |
| `player/` | Audio playback, ExoPlayer, MediaSession, visualizers |
| `util/` | Utilities, extensions, helpers |
| `di/` | Hilt modules for dependency injection |

---

## 10. Risk Assessment

### 10.1 Technical Risks

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| **Radio Browser API downtime** | High | Low | Implement caching, fallback to cached stations, show offline mode |
| **Stream URL failures** | High | Medium | Retry logic, use `url_resolved`, show error state, suggest alternatives |
| **ExoPlayer codec compatibility** | Medium | Medium | Test on multiple devices, fallback to MediaPlayer for unsupported codecs |
| **Battery drain** | Medium | Medium | Optimize visualizers, reduce polling, use efficient rendering |
| **Large APK size** | Low | Medium | Use WebP images, ProGuard/R8, remove unused resources |
| **Skin switching jank** | Medium | Low | Preload skin assets, optimize animations, use remember {} wisely |
| **Location permission denial** | Medium | High | IP-based country detection fallback, manual country selection |
| **Network interruptions** | High | High | Automatic retry, buffer size tuning, clear error messages |

### 10.2 Product Risks

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| **Poor user engagement** | High | Medium | Focus on polish, unique value (skins), marketing, community building |
| **Skin complexity confuses users** | Medium | Low | Default to modern skin, clear skin picker UI, onboarding tutorial |
| **Vintage UI doesn't resonate** | High | Low | User testing, adjustable skeuomorphism level, multiple skin options |
| **Difficult to discover stations** | Medium | Medium | Smart defaults (location), curated lists, good search, recommendations |
| **Competition from established apps** | High | High | Differentiate with unique UI, simplicity, no ads, open source |

### 10.3 Legal Risks

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| **Stream URL copyright** | High | Low | Only link to public streams from Radio Browser, don't host content |
| **Recording feature legality** | High | N/A | Skip recording feature initially, or make it region-specific with disclaimers |
| **Trademark issues (Winamp)** | Medium | Low | Don't use "Winamp" name, only "inspired by" in descriptions, generic retro style |
| **License compliance** | Medium | Low | Audit dependencies, include licenses, comply with open source requirements |

---

## 11. Success Metrics

### 11.1 Launch Goals (First 3 Months)

| Metric | Target | Measurement |
|--------|--------|-------------|
| **Downloads** | 10,000+ | Google Play Console |
| **Active users (DAU)** | 1,000+ | Analytics |
| **Retention (Day 7)** | 30%+ | Analytics |
| **Retention (Day 30)** | 15%+ | Analytics |
| **Average rating** | 4.0+ | Play Store reviews |
| **Crash-free rate** | 98%+ | Crashlytics |
| **Average session duration** | 15+ minutes | Analytics |

### 11.2 User Satisfaction

- **Skin usage**: At least 40% of users try non-default skin
- **Favorites**: Average 5+ favorited stations per active user
- **Positive reviews**: 70%+ of reviews are 4-5 stars
- **Feature requests**: Track most requested features for roadmap

### 11.3 Technical Performance

- **App startup time**: < 2 seconds (cold start)
- **Station play latency**: < 3 seconds (buffering to playback)
- **Memory usage**: < 100MB average
- **APK size**: < 20MB
- **Frame rate**: Consistent 60fps on mid-range devices (2022+)

---

## 12. Next Steps

### Immediate Actions (Pre-Development)

1. **Finalize Design**
   - [ ] Create detailed Figma designs for all 3-4 skins
   - [ ] Design app icon and branding
   - [ ] Create color schemes for each skin
   - [ ] Source/create vintage assets (textures, fonts)

2. **Technical Validation**
   - [ ] Test Radio Browser API (confirm stations work)
   - [ ] Prototype ExoPlayer integration (test stream playback)
   - [ ] Validate location detection approach
   - [ ] Test visualizer libraries

3. **Project Setup**
   - [ ] Create Android Studio project
   - [ ] Set up Git repository (GitHub)
   - [ ] Configure Gradle with all dependencies
   - [ ] Set up CI/CD pipeline (GitHub Actions)
   - [ ] Create initial project structure (packages)

4. **Documentation**
   - [ ] Review and approve this plan
   - [ ] Set up issue tracker (GitHub Issues or Jira)
   - [ ] Create development board (GitHub Projects or Trello)
   - [ ] Define coding standards and contribution guidelines

### Development Kickoff (Week 1)

- [ ] Set up development environment
- [ ] Create initial commit with project structure
- [ ] Implement Radio Browser API client (first endpoint)
- [ ] Create Station data model
- [ ] Set up Hilt for dependency injection
- [ ] Build basic UI scaffold (MainActivity, navigation)

---

## Appendix

### A. Glossary

- **Skin**: A complete visual theme that changes the entire UI layout and design
- **Radio Browser API**: Free, open API providing access to 30,000+ internet radio stations
- **ExoPlayer**: Google's open-source media player for Android
- **Media3**: Modern Android media framework (includes ExoPlayer)
- **Jetpack Compose**: Android's modern declarative UI toolkit
- **MVVM**: Model-View-ViewModel architectural pattern
- **CompositionLocal**: Compose mechanism for passing data down the composition tree
- **StateFlow**: Kotlin coroutine-based observable state holder
- **Hilt**: Android dependency injection library built on Dagger

### B. References

- [Radio Browser API Documentation](https://docs.radio-browser.info/)
- [ExoPlayer Documentation](https://developer.android.com/media/media3/exoplayer)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Android Architecture Guide](https://developer.android.com/topic/architecture)
- **SKINNING_ARCHITECTURE.md** (in this repository)
- **QUICK_START_EXAMPLE.md** (in this repository)

### C. Contact & Support

- **Developer**: [Your Name/Team]
- **Email**: [Your Email]
- **GitHub**: [Repository URL]
- **Discord/Slack**: [Community Link]

---

## Document Version History

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | Jan 2026 | Initial project plan created | Claude (AI Assistant) |

---

**End of Master Project Plan**

This plan serves as the blueprint for building Indieradio. It should be treated as a living document and updated as the project evolves. Review and approval by the development team is required before proceeding to implementation.
