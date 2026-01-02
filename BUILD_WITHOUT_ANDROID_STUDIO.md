# Building Indieradio Without Android Studio

This guide shows you how to build and install the Indieradio app **without Android Studio**.

---

## 🚀 Quick Start (Automated)

### Linux/macOS

```bash
# Make the script executable
chmod +x build-and-install.sh

# Run the build and install script
./build-and-install.sh
```

### Windows

```cmd
# Run the build and install script
build-and-install.bat
```

The script will:
1. ✅ Check Java installation
2. ✅ Build the APK
3. ✅ Install on connected Android device (if ADB available)

---

## 📋 Manual Method (Step by Step)

### Prerequisites

#### 1. Install Java JDK 17

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**macOS:**
```bash
brew install openjdk@17
```

**Windows:**
- Download from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Install and add to PATH

**Verify Installation:**
```bash
java -version
# Should show version 17.x.x
```

#### 2. Install Android SDK Command Line Tools (Optional)

Only needed if you want to use ADB for installation.

**Linux/macOS/Windows:**
1. Download from: https://developer.android.com/studio#command-tools
2. Extract to a folder (e.g., `~/android-sdk` or `C:\android-sdk`)
3. Set environment variable:
   ```bash
   export ANDROID_HOME=~/android-sdk  # Linux/macOS
   # OR
   set ANDROID_HOME=C:\android-sdk    # Windows
   ```

#### 3. Install ADB (Android Debug Bridge)

**Ubuntu/Debian:**
```bash
sudo apt install adb
```

**macOS:**
```bash
brew install android-platform-tools
```

**Windows:**
- Download from: https://developer.android.com/studio/releases/platform-tools
- Extract and add to PATH

---

## 🔨 Building the APK

### Step 1: Navigate to Project Directory

```bash
cd indieradio
```

### Step 2: Build Debug APK

**Linux/macOS:**
```bash
./gradlew assembleDebug
```

**Windows:**
```cmd
gradlew.bat assembleDebug
```

**First build will take 5-10 minutes** as Gradle downloads all dependencies.

### Step 3: Locate the APK

After successful build, the APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

**APK size:** ~15-20 MB

---

## 📱 Installing on Your Phone

### Method 1: Direct Installation via ADB (Recommended)

#### Enable USB Debugging on Phone

1. Go to **Settings** → **About Phone**
2. Tap **Build Number** 7 times (Developer mode enabled)
3. Go to **Settings** → **Developer Options**
4. Enable **USB Debugging**

#### Connect Phone and Install

```bash
# Connect phone via USB cable

# Check if device is detected
adb devices
# Should show your device

# Install the APK
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Success!** The app is now installed on your phone.

---

### Method 2: Manual Installation (No ADB Required)

1. **Copy APK to Phone:**
   - Connect phone via USB (file transfer mode)
   - Copy `app/build/outputs/apk/debug/app-debug.apk` to your phone's Download folder

   **OR**

   - Email the APK to yourself
   - Upload to Google Drive/Dropbox and download on phone

2. **Install APK:**
   - Open **Files** app on phone
   - Navigate to **Downloads** folder
   - Tap on `app-debug.apk`
   - Allow installation from unknown sources if prompted
   - Tap **Install**

3. **Launch:**
   - Find **Indieradio** app icon
   - Tap to launch

---

### Method 3: Wireless Installation (No USB Cable)

#### Setup (One-time)

1. **Connect phone and computer to same WiFi**

2. **Enable Wireless ADB on phone:**
   ```bash
   # First time, connect via USB
   adb tcpip 5555
   ```

3. **Find phone's IP address:**
   - Settings → About Phone → Status → IP Address
   - Example: 192.168.1.100

4. **Connect wirelessly:**
   ```bash
   adb connect 192.168.1.100:5555
   ```

#### Install APK Wirelessly

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Now you can disconnect USB cable!**

---

## 🎯 Build Variants

### Debug Build (Default)

```bash
./gradlew assembleDebug
```

- Package: `com.indieradio.debug`
- Optimizations: OFF
- ProGuard: Disabled
- Size: ~20 MB
- Use for: Testing, development

### Release Build (Smaller, Optimized)

```bash
./gradlew assembleRelease
```

- Package: `com.indieradio`
- Optimizations: ON
- ProGuard: Enabled
- Size: ~10-15 MB
- Use for: Production, distribution

**Note:** Release builds require signing (see Signing section below)

---

## 🔐 Signing APK for Release (Optional)

### Generate Keystore (One-time)

```bash
keytool -genkey -v -keystore indieradio-release.keystore \
  -alias indieradio -keyalg RSA -keysize 2048 -validity 10000
```

Enter a password and fill in the details.

### Configure Signing

Create `app/keystore.properties`:

```properties
storePassword=YOUR_KEYSTORE_PASSWORD
keyPassword=YOUR_KEY_PASSWORD
keyAlias=indieradio
storeFile=../indieradio-release.keystore
```

**Important:** Add `keystore.properties` to `.gitignore` (already done)

### Build Signed Release APK

```bash
./gradlew assembleRelease
```

Signed APK location: `app/build/outputs/apk/release/app-release.apk`

---

## 🛠️ Troubleshooting

### Error: "Java not found"

**Solution:**
```bash
# Install Java 17
sudo apt install openjdk-17-jdk  # Ubuntu/Debian
brew install openjdk@17           # macOS
```

### Error: "ANDROID_HOME not set"

**Solution:**
```bash
# Set ANDROID_HOME (optional for basic builds)
export ANDROID_HOME=~/android-sdk
```

### Error: "adb: device not found"

**Solution:**
1. Check USB debugging is enabled
2. Reconnect USB cable
3. Accept "Allow USB debugging" popup on phone
4. Run `adb devices` again

### Error: "Permission denied: ./gradlew"

**Solution:**
```bash
chmod +x ./gradlew
```

### Error: "Build failed"

**Solution:**
```bash
# Clean and retry
./gradlew clean
./gradlew assembleDebug
```

### Error: "Installation failed: INSTALL_FAILED_INSUFFICIENT_STORAGE"

**Solution:**
- Free up space on your phone (need ~50 MB)

### Error: "Installation failed: INSTALL_PARSE_FAILED"

**Solution:**
- APK is corrupted, rebuild:
  ```bash
  ./gradlew clean assembleDebug
  ```

---

## 📊 Build Commands Reference

### Basic Commands

```bash
# Clean build artifacts
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Build all variants
./gradlew assemble

# Run tests
./gradlew test

# Check dependencies
./gradlew dependencies
```

### Advanced Commands

```bash
# Build with stacktrace for errors
./gradlew assembleDebug --stacktrace

# Build offline (use cached dependencies)
./gradlew assembleDebug --offline

# Build with info logs
./gradlew assembleDebug --info

# Build and show dependency tree
./gradlew assembleDebug --scan
```

---

## 🌐 Alternative: Build on GitHub Actions (Free)

### Setup GitHub Actions

Create `.github/workflows/build.yml`:

```yaml
name: Build APK

on:
  push:
    branches: [ main, claude/* ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build with Gradle
      run: ./gradlew assembleDebug

    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

**Benefits:**
- Builds automatically on every push
- No local setup required
- Download APK from GitHub Actions artifacts
- Free for public repositories

---

## 📦 Distribution Options

### 1. Direct APK Sharing

**Pros:** Simple, no store approval needed
**Cons:** Users must enable "Install from Unknown Sources"

```bash
# Share the APK file directly
app/build/outputs/apk/debug/app-debug.apk
```

### 2. Google Play Store (Internal Testing)

**Pros:** Easy distribution to testers
**Cons:** Requires Google Play Developer account ($25 one-time)

1. Build release APK (signed)
2. Upload to Google Play Console
3. Create internal testing track
4. Share testing link with users

### 3. Third-party Platforms

- **APKPure**: Free APK hosting
- **F-Droid**: Open-source app store (requires FOSS compliance)
- **GitHub Releases**: Host APK in GitHub releases

---

## 🎯 Quick Command Cheatsheet

```bash
# Build debug APK (fastest)
./gradlew assembleDebug

# Build release APK (optimized)
./gradlew assembleRelease

# Install via ADB
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Uninstall from device
adb uninstall com.indieradio.debug

# View device logs
adb logcat | grep Indieradio

# Check connected devices
adb devices

# Restart ADB server
adb kill-server && adb start-server
```

---

## ✅ Success Checklist

After building and installing, verify:

- [ ] APK file exists in `app/build/outputs/apk/debug/`
- [ ] APK size is reasonable (~15-20 MB for debug)
- [ ] App appears in phone's app drawer
- [ ] App launches without crashes
- [ ] Can search for radio stations
- [ ] Can play a radio station
- [ ] Audio continues when minimizing app
- [ ] Notification controls work

---

## 🎉 You're Done!

You've successfully built and installed Indieradio without Android Studio!

**Need help?** Refer to the troubleshooting section or check:
- DEVELOPMENT_GUIDE.md
- GitHub Issues (if available)

**Happy listening! 🎵📻**
