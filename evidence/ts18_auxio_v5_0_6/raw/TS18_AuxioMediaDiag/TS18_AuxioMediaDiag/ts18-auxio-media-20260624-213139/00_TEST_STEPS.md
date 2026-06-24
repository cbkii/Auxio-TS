# Manual test steps for this capture

Session: ts18-auxio-media-20260624-213139
Started: 2026-06-24 21:31:39 +1000

During the capture window:

1. Start Auxio-TS playback from the exact APK/variant you want tested.
2. On DoFun home/window widget, observe:
   - title/artist shown or blank;
   - album art shown, placeholder, stale, or crash;
   - progress shown/moving or frozen;
   - play/pause command works;
   - next/previous command works;
   - tapping window opens the expected app/activity.
3. Test Auxio-TS storage/source features and say/write down the exact source mode/path used:
   - MediaStore/system source;
   - SAF source / DocumentsUI picker;
   - DirectFS/manual path if installed;
   - /storage/usbdisk0 or /storage/usbdisk1;
   - /storage/usbdiskN/Music, Download, or custom folder;
   - USB unplug/replug if safe.
4. Test earliest-start readiness:
   - after reboot, note whether Auxio autostarted, restored queue, restored overlay, and published media session before DoFun was ready.
5. Test floating controls / edge conflict:
   - place floating controls near top/status bar and right navigation/edge drawer areas;
   - try status shade, right-edge navigation/gesture drawer, DoFun home gestures;
   - note whether the overlay is hidden, loses touch, is under SystemUI, or is displaced.
6. Test interruption contexts:
   - pause from DoFun, Auxio, notification, headset/BT controller if available, VLC/Spotify takeover, radio/NavRadio, reverse/camera if safe, and ACC sleep/wake if available;
   - note any unexpected pause/resume.
7. Switch to VLC and play audio for 2-3 minutes.
8. Switch to Spotify and play audio for 2-3 minutes.
9. Return to Auxio-TS and repeat play/pause/next/previous.
10. If testing BTAndroidTS or ts18-intent-bridge, trigger their intended user-visible action once during the capture.
11. Stop early with:
   touch /storage/emulated/0/Download/TS18_AuxioMediaDiag.STOP

Share the final .tar.gz if possible, or these files:
- REPORT.md
- package_table.tsv
- media_session_all.txt
- notification_all.txt
- audio_all.txt
- logs/logcat_filtered.txt
- snapshots/*/summary.txt
- packages/*/quick-components.txt
