#!/bin/bash
# collect-ts18-evidence.sh
# Helper script to collect evidence from a rooted TS18 device

echo "--- Collecting Package Identity ---"
echo "Path for com.tw.music:"
adb shell pm path com.tw.music || true
echo -e "\nPath for com.tw.media:"
adb shell pm path com.tw.media || true
echo -e "\nActivity Resolution for com.tw.music/.MusicActivity:"
adb shell cmd package resolve-activity --brief com.tw.music/.MusicActivity || true
echo -e "\nPackage Dump for com.tw.music:"
if adb shell dumpsys package com.tw.music > evidence_com_tw_music_dump.txt; then
    echo "Dump saved to evidence_com_tw_music_dump.txt"
else
    echo "Failed to collect package dump."
fi

echo -e "\n--- Collecting MediaSession ---"
if adb shell dumpsys media_session > evidence_media_session_dump.txt; then
    echo "Dump saved to evidence_media_session_dump.txt"
else
    echo "Failed to collect MediaSession dump."
fi

echo -e "\n--- Collecting USB/Source Storage Information ---"
adb shell ls -lah /storage | grep -i usbdisk || true
adb shell find /storage -maxdepth 1 -type d -name 'usbdisk*' -print 2>/dev/null || true
echo "Iterating through discovered usbdisk directories:"
adb shell 'for d in /storage/usbdisk*; do [ -d "$d" ] && ls -lah "$d"; done' || true
