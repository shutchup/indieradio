#!/bin/bash

# Indieradio - Build and Install Script
# This script builds the APK and installs it on a connected Android device

set -e  # Exit on error

echo "======================================"
echo "   Indieradio Build & Install"
echo "======================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Java is installed
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo -e "${RED}Error: Java is not installed${NC}"
    echo "Please install JDK 17 first"
    echo "Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "macOS: brew install openjdk@17"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
echo -e "${GREEN}✓ Java ${JAVA_VERSION} found${NC}"
echo ""

# Make gradlew executable
chmod +x ./gradlew

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo ""
echo "Building debug APK..."
echo "This may take a few minutes on first run..."
./gradlew assembleDebug

# Check if build was successful
if [ ! -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo -e "${RED}Error: Build failed. APK not found.${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Build successful!${NC}"
echo ""
echo "APK location: app/build/outputs/apk/debug/app-debug.apk"
APK_SIZE=$(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)
echo "APK size: ${APK_SIZE}"
echo ""

# Check if ADB is available
if ! command -v adb &> /dev/null; then
    echo -e "${YELLOW}Warning: ADB not found${NC}"
    echo "To install on your phone, you can:"
    echo "1. Copy the APK to your phone manually and install"
    echo "   APK location: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "2. Install ADB to enable direct installation:"
    echo "   Ubuntu/Debian: sudo apt install adb"
    echo "   macOS: brew install android-platform-tools"
    echo "   Windows: Download from https://developer.android.com/studio/releases/platform-tools"
    exit 0
fi

# Check if device is connected
echo "Checking for connected devices..."
DEVICES=$(adb devices | grep -v "List" | grep "device" | wc -l)

if [ $DEVICES -eq 0 ]; then
    echo -e "${YELLOW}No devices found${NC}"
    echo ""
    echo "To install the app on your phone:"
    echo "1. Enable USB debugging on your phone:"
    echo "   - Settings → About Phone → Tap 'Build Number' 7 times"
    echo "   - Settings → Developer Options → Enable USB Debugging"
    echo ""
    echo "2. Connect your phone via USB cable"
    echo ""
    echo "3. Run this script again, or manually install:"
    echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "Alternatively, copy the APK to your phone and install manually:"
    echo "   APK location: app/build/outputs/apk/debug/app-debug.apk"
    exit 0
fi

echo -e "${GREEN}✓ Found ${DEVICES} device(s)${NC}"
echo ""

# Install APK
echo "Installing APK on device..."
adb install -r app/build/outputs/apk/debug/app-debug.apk

echo ""
echo -e "${GREEN}======================================"
echo "   Installation Complete! 🎉"
echo "======================================${NC}"
echo ""
echo "The Indieradio app should now be installed on your phone."
echo "Look for the 'Indieradio' app icon and launch it!"
echo ""
