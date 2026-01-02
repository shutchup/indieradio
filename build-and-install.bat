@echo off
REM Indieradio - Build and Install Script for Windows
REM This script builds the APK and installs it on a connected Android device

echo ======================================
echo    Indieradio Build ^& Install
echo ======================================
echo.

REM Check if Java is installed
echo Checking Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Error: Java is not installed
    echo Please install JDK 17 first from:
    echo https://adoptium.net/ or https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
    pause
    exit /b 1
)

echo Java found
echo.

REM Clean previous builds
echo Cleaning previous builds...
call gradlew.bat clean

REM Build debug APK
echo.
echo Building debug APK...
echo This may take a few minutes on first run...
call gradlew.bat assembleDebug

REM Check if build was successful
if not exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo Error: Build failed. APK not found.
    pause
    exit /b 1
)

echo Build successful!
echo.
echo APK location: app\build\outputs\apk\debug\app-debug.apk
for %%I in ("app\build\outputs\apk\debug\app-debug.apk") do echo APK size: %%~zI bytes
echo.

REM Check if ADB is available
where adb >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Warning: ADB not found
    echo.
    echo To install on your phone, you can:
    echo 1. Copy the APK to your phone manually and install
    echo    APK location: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo 2. Install ADB to enable direct installation:
    echo    Download from: https://developer.android.com/studio/releases/platform-tools
    pause
    exit /b 0
)

REM Check if device is connected
echo Checking for connected devices...
adb devices | find /c "device" > temp.txt
set /p DEVICES=<temp.txt
del temp.txt

if %DEVICES% LEQ 1 (
    echo No devices found
    echo.
    echo To install the app on your phone:
    echo 1. Enable USB debugging on your phone
    echo 2. Connect your phone via USB cable
    echo 3. Run this script again
    echo.
    echo Alternatively, copy the APK to your phone and install manually:
    echo    APK location: app\build\outputs\apk\debug\app-debug.apk
    pause
    exit /b 0
)

echo Device found
echo.

REM Install APK
echo Installing APK on device...
adb install -r app\build\outputs\apk\debug\app-debug.apk

echo.
echo ======================================
echo    Installation Complete!
echo ======================================
echo.
echo The Indieradio app should now be installed on your phone.
echo Look for the 'Indieradio' app icon and launch it!
echo.
pause
