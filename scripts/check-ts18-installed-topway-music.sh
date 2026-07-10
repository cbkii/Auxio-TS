#!/bin/bash

echo "Checking installed packages for DoFun integration (topwayTwMusic)..."

if ! command -v adb >/dev/null 2>&1; then
    echo "Error: adb is not installed or not in PATH."
    exit 1
fi

if ! adb shell "echo 1" >/dev/null 2>&1; then
    echo "Error: No adb device found or device is unauthorized."
    exit 1
fi

DEBUG_INSTALLED=$(adb shell pm list packages | tr -d '\r' | grep "package:com.tw.music.debug")
RELEASE_INSTALLED=$(adb shell pm list packages | tr -d '\r' | grep -w "package:com.tw.music")

if [ -n "$DEBUG_INSTALLED" ]; then
    echo "WARNING: com.tw.music.debug is installed."
    echo "DoFun integration matching requires the package to be exactly com.tw.music."
    echo "The .debug suffix will cause the launcher to not recognize the app properly."
    echo "Please uninstall com.tw.music.debug and install the release build (topwayTwMusicRelease) via Magisk."
    if [ -n "$RELEASE_INSTALLED" ]; then
        echo "Note: Both .debug and release packages are installed. This may cause conflicts."
    fi
    exit 1
elif [ -n "$RELEASE_INSTALLED" ]; then
    echo "SUCCESS: com.tw.music is installed and ready for DoFun integration testing."
    exit 0
else
    echo "WARNING: Neither com.tw.music nor com.tw.music.debug is installed."
    echo "Please install topwayTwMusicRelease via Magisk to test DoFun integration."
    exit 1
fi
