#!/bin/bash
# collect-ts18-evidence.sh
# Helper script to collect evidence from a rooted TS18 device

if ! command -v adb &> /dev/null; then
    echo "Error: adb command not found. Please install Android SDK Platform Tools." >&2
    exit 1
fi

if ! adb get-state &> /dev/null; then
    echo "Error: No ADB device connected or authorized. Please connect a device and try again." >&2
    exit 1
fi

echo "--- Collecting Package Identity ---"
echo "Path for com.tw.music:"
adb shell pm path com.tw.music || true
echo -e "\nPath for com.tw.media:"
adb shell pm path com.tw.media || true
echo -e "\nActivity Resolution for com.tw.music/.MusicActivity:"
adb shell cmd package resolve-activity --brief com.tw.music/.MusicActivity || true
echo -e "\nPackage Dump for com.tw.music:"
adb shell dumpsys package com.tw.music > evidence_com_tw_music_dump.txt || true
echo "Dump saved to evidence_com_tw_music_dump.txt"

echo -e "\n--- Collecting MediaSession ---"
adb shell dumpsys media_session > evidence_media_session_dump.txt || true
echo "Dump saved to evidence_media_session_dump.txt"

echo -e "\n--- Collecting USB/Source Storage Information ---"
adb shell ls -lah /storage | grep -i usbdisk || true
adb shell find /storage -maxdepth 1 -type d -name 'usbdisk*' -print 2>/dev/null || true
echo "Iterating through discovered usbdisk directories:"
adb shell 'for d in /storage/usbdisk*; do [ -d "$d" ] && ls -lah "$d"; done' || true
