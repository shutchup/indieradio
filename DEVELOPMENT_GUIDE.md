# Indieradio - Development Guide

## Project Status: MVP Phase 1 Complete ✅

A working Android radio streaming app has been implemented with the following features:

### ✅ Implemented Features

1. **Radio API Integration**
   - Radio Browser API client with Retrofit
   - Search stations by name
   - Browse popular/top voted stations
   - Full station metadata support

2. **Audio Playback**
   - ExoPlayer-based streaming
   - Background playback support
   - MediaPlaybackService for foreground service
   - Playback state management

3. **UI - Modern Minimal Skin**
   - Material 3 design
   - Now Playing section
   - Station search
   - Station list with infinite scroll
   - Playback controls (Play/Pause/Stop)

4. **Architecture**
   - MVVM pattern
   - Clean Architecture (Domain/Data/UI layers)
   - Hilt dependency injection
   - Kotlin Coroutines & StateFlow
   - Repository pattern

### 📋 Project Structure

```
app/src/main/kotlin/com/indieradio/
├── IndieRadioApp.kt                 # Application class
├── MainActivity.kt                  # Main activity
│
├── domain/
│   ├── model/
│   │   ├── Station.kt              # Station domain model
│   │   ├── PlaybackState.kt        # Playback state sealed class
│   │   └── SkinTheme.kt            # Skin theme definitions
│   └── repository/
│       └── StationRepository.kt    # Repository interface
│
├── data/
│   ├── remote/
│   │   ├── api/
│   │   │   └── RadioBrowserApi.kt  # Retrofit API interface
│   │   ├── dto/
│   │   │   └── StationDto.kt       # API response DTOs
│   │   └── mapper/
│   │       └── StationMapper.kt    # DTO to domain mapper
│   └── repository/
│       └── StationRepositoryImpl.kt # Repository implementation
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt                # Color definitions
│   │   ├── Type.kt                 # Typography
│   │   └── Theme.kt                # Material 3 theme
│   ├── screens/
│   │   └── RadioPlayerScreen.kt    # Main player screen
│   └── viewmodel/
│       └── RadioPlayerViewModel.kt # Player ViewModel
│
├── player/
│   ├── RadioPlayer.kt              # ExoPlayer wrapper
│   └── MediaPlaybackService.kt     # Background playback service
│
└── di/
    ├── NetworkModule.kt            # Retrofit/OkHttp DI
    ├── RepositoryModule.kt         # Repository bindings
    └── PlayerModule.kt             # ExoPlayer DI
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later (2023.1.1+)
- JDK 17
- Android SDK 35 (compileSdk)
- Minimum SDK 24 (Android 7.0)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd indieradio
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - File → Open → Select the indieradio directory
   - Wait for Gradle sync to complete

3. **Add Launcher Icons** (Optional for development)
   - Right-click on `res` folder
   - New → Image Asset
   - Configure launcher icons
   - Generate icons

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click Run (Shift+F10)
   - The app should install and launch

### Testing the App

1. **First Launch**:
   - Grant notification permission (Android 13+) for playback controls
   - The app will load popular radio stations

2. **Search Stations**:
   - Use the search bar to find stations by name
   - Example: Search "BBC" or "Jazz"

3. **Play a Station**:
   - Tap any station from the list
   - Playback should start within a few seconds
   - Playback controls appear in the notification

4. **Background Playback**:
   - Press home button
   - Audio should continue playing
   - Use notification controls to pause/stop

---

## 🔧 Development Tasks

### Next Steps (Phase 1 Continuation)

- [ ] **Add launcher icon** (currently placeholder)
- [ ] **Implement location detection** for local stations
- [ ] **Add favorites functionality** (Room database)
- [ ] **Add recently played history**
- [ ] **Implement basic settings screen**
- [ ] **Add error retry logic**
- [ ] **Improve loading states**

### Phase 2: Enhanced UI

- [ ] **Add waveform visualizer** for modern skin
- [ ] **Implement station filtering** (by country, genre, language)
- [ ] **Add bottom navigation** (Player, Browse, Favorites, Settings)
- [ ] **Implement pull-to-refresh**
- [ ] **Add station details screen**
- [ ] **Improve now playing UI**

### Phase 3: Vintage Skin

- [ ] **Design vintage 80s radio skin**
- [ ] **Implement rotary dial control**
- [ ] **Implement volume knob**
- [ ] **Add vintage radio assets**
- [ ] **Implement skin switching**

---

## 🐛 Known Issues

1. **Launcher Icons Missing**: Placeholder icons need to be replaced with actual designs
2. **No Favorites**: Favorites feature not yet implemented (requires Room DB)
3. **No Location Detection**: Local stations feature pending (requires location permission handling)
4. **Limited Error Handling**: Some edge cases may not be handled gracefully

---

## 📝 Testing Checklist

- [ ] App builds successfully
- [ ] App launches without crashes
- [ ] Popular stations load on startup
- [ ] Search functionality works
- [ ] Can play radio stations
- [ ] Playback continues in background
- [ ] Notification controls work
- [ ] Can pause/resume playback
- [ ] Can stop playback
- [ ] Can switch between stations
- [ ] Error states display properly
- [ ] Loading states display properly

---

## 🏗️ Architecture Details

### Data Flow

```
UI (Compose)
  ↓ User Action
ViewModel
  ↓ Business Logic
Repository
  ↓ Data Source
API / Database
  ↓ Response
Repository
  ↓ Domain Model
ViewModel
  ↓ State Update
UI (Compose)
```

### Playback Flow

```
User clicks station
  ↓
ViewModel.playStation()
  ↓
RadioPlayer.playStation()
  ↓
ExoPlayer prepares & plays
  ↓
MediaPlaybackService manages foreground service
  ↓
Notification controls appear
  ↓
PlaybackState updates via StateFlow
  ↓
UI recomposes with new state
```

---

## 🔐 Permissions

### Required Permissions

- `INTERNET`: For streaming radio
- `ACCESS_NETWORK_STATE`: For checking network connectivity
- `FOREGROUND_SERVICE`: For background playback
- `FOREGROUND_SERVICE_MEDIA_PLAYBACK`: Media playback type
- `WAKE_LOCK`: To keep device awake during playback
- `POST_NOTIFICATIONS`: For playback controls (Android 13+)

### Optional Permissions (Not Yet Implemented)

- `ACCESS_COARSE_LOCATION`: For local station discovery
- `ACCESS_FINE_LOCATION`: For precise local station discovery

---

## 📚 Key Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| Jetpack Compose | BOM 2024.02.00 | UI framework |
| Material 3 | (via BOM) | Material Design 3 |
| Hilt | 2.50 | Dependency injection |
| Retrofit | 2.9.0 | HTTP client |
| ExoPlayer (Media3) | 1.3.0 | Audio streaming |
| Kotlin Coroutines | 1.8.0 | Async operations |
| Coil | 2.5.0 | Image loading |
| Room | 2.6.1 | Local database (not yet used) |
| DataStore | 1.0.0 | Settings storage (not yet used) |

---

## 🎨 Design System

### Colors

- **Primary Light**: Purple (#6200EE)
- **Primary Dark**: Purple (#BB86FC)
- **Secondary**: Teal (#03DAC6)
- **Background Light**: White-ish (#FFFBFE)
- **Background Dark**: Near Black (#121212)

### Typography

- Material 3 default typography
- Sans-serif font family
- Responsive text sizes

---

## 🚦 Build Variants

### Debug Build
- Application ID: `com.indieradio.debug`
- Debugging enabled
- ProGuard disabled
- Logging enabled

### Release Build
- Application ID: `com.indieradio`
- Debugging disabled
- ProGuard enabled
- Optimized for production

---

## 📱 Minimum Requirements

- **Android Version**: 7.0 Nougat (API 24)
- **Target Android Version**: 15 (API 35)
- **RAM**: 2GB minimum
- **Storage**: 50MB for app installation
- **Network**: Internet connection required for streaming

---

## 🛠️ Troubleshooting

### Build Errors

**Error**: "Cannot resolve symbol 'R'"
- **Solution**: File → Invalidate Caches / Restart

**Error**: "Duplicate class found"
- **Solution**: Clean and rebuild project

### Runtime Errors

**Error**: "Network error" when loading stations
- **Solution**: Check internet connection, verify Radio Browser API is accessible

**Error**: "Playback error" when playing station
- **Solution**: Some stream URLs may be invalid, try another station

**Error**: Notification controls not appearing
- **Solution**: Grant notification permission (Settings → Apps → Indieradio → Notifications)

---

## 📖 API Documentation

### Radio Browser API

Base URL: `https://all.api.radio-browser.info/`

**Used Endpoints**:
- `GET /json/stations/topvote/{count}` - Get top voted stations
- `GET /json/stations/byname/{name}` - Search stations by name
- `GET /json/stations/bycountrycodeexact/{code}` - Get stations by country
- `GET /json/stations/bytag/{tag}` - Get stations by genre/tag
- `GET /json/url/{uuid}` - Track station click

**Rate Limits**: Fair use policy, no specific published limits

**Documentation**: https://docs.radio-browser.info/

---

## 🤝 Contributing

### Code Style

- Follow Kotlin coding conventions
- Use meaningful variable names
- Add KDoc comments for public APIs
- Keep functions small and focused
- Use Compose best practices

### Commit Message Format

```
<type>: <description>

Examples:
feat: Add favorites functionality
fix: Resolve playback crash on network loss
refactor: Extract station list into separate composable
docs: Update development guide
```

---

## 📄 License

[To be determined]

---

## 👥 Credits

- **Radio Browser API**: https://www.radio-browser.info/
- **ExoPlayer**: Google Media3 team
- **Jetpack Compose**: Android team at Google

---

**Happy Coding! 🎵📻**
