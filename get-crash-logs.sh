#!/bin/bash

# Script to capture IndieRadio crash logs from connected Android device

echo "IndieRadio Crash Log Collector"
echo "==============================="
echo ""

# Check if adb is available
if ! command -v adb &> /dev/null; then
    echo "ERROR: adb not found. Please install Android SDK Platform Tools."
    exit 1
fi

# Check if device is connected
echo "Checking for connected devices..."
DEVICES=$(adb devices | grep -v "List" | grep "device$" | wc -l)

if [ "$DEVICES" -eq 0 ]; then
    echo "ERROR: No Android device connected."
    echo ""
    echo "Please:"
    echo "1. Enable USB Debugging on your phone (Settings → Developer Options)"
    echo "2. Connect your phone via USB"
    echo "3. Run this script again"
    exit 1
fi

echo "✓ Device connected"
echo ""
echo "Clearing old logs..."
adb logcat -c

echo "✓ Logs cleared"
echo ""
echo "Starting log capture..."
echo "Please launch the IndieRadio app now and let it crash."
echo ""
echo "Press Ctrl+C when done to stop logging."
echo "=================================="
echo ""

# Capture logs with filters for our app
adb logcat -v time "*:E" | grep -E "AndroidRuntime|IndieRadio|com.indieradio|FATAL|dalvikvm"
