#!/bin/bash

echo "Checking installed packages for DoFun integration..."

if ! command -v adb >/dev/null 2>&1; then
    echo "Error: adb is not installed or not in PATH."
    exit 1
fi

if ! adb shell "echo 1" >/dev/null 2>&1; then
    echo "Error: No adb device found or device is unauthorized."
    exit 1
fi

DEBUG_INSTALLED=$(adb shell pm list packages | grep "package:com.tw.media.debug")
RELEASE_INSTALLED=$(adb shell pm list packages | grep "package:com.tw.media$")

if [ -n "$DEBUG_INSTALLED" ]; then
    echo "WARNING: com.tw.media.debug is installed."
    echo "DoFun integration matching requires the package to be exactly com.tw.media."
    echo "The .debug suffix will cause the launcher to not recognize the app."
    echo "Please uninstall com.tw.media.debug and install the release build (topwayTwMediaRelease)."
    if [ -n "$RELEASE_INSTALLED" ]; then
        echo "Note: Both .debug and release packages are installed. This may cause conflicts."
    fi
    exit 1
elif [ -n "$RELEASE_INSTALLED" ]; then
    echo "SUCCESS: com.tw.media is installed and ready for DoFun integration testing."
    exit 0
else
    echo "WARNING: Neither com.tw.media nor com.tw.media.debug is installed."
    echo "Please install topwayTwMediaRelease to test DoFun integration."
    exit 1
fi
