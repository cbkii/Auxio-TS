# TS18 Post-Merge Validation Checklist

## 1. Installed Package Identity
- [ ] Verify `com.tw.music` path using `adb shell pm path com.tw.music`.
- [ ] Verify `com.tw.music` package dump using `adb shell dumpsys package com.tw.music`.
- [ ] Verify `com.tw.media` path (if installed) using `adb shell pm path com.tw.media`.
- [ ] Verify activity resolution for `com.tw.music/.MusicActivity` using `adb shell cmd package resolve-activity --brief com.tw.music/.MusicActivity`.

## 2. DoFun Launcher / Hotseat Behaviour
- [ ] Verify Auxio-TS launches correctly from the DoFun launcher icon.
- [ ] Verify the DoFun hotseat integration works and brings Auxio-TS to the foreground.

## 3. Widget & Sticky Update Behaviour
- [ ] Verify widgets update correctly on track change/play/pause.
- [ ] Verify sticky update behaviour (e.g., using `adb shell am broadcast -a com.tw.music.action.cmd --es cmd update`).
- [ ] Ensure `android.permission.BROADCAST_STICKY` is held by the app.

## 4. MediaSession Visibility & Alias Routing
- [ ] Verify MediaSession is visible using `adb shell dumpsys media_session`.
- [ ] Verify alias routing for `com.tw.music.MusicActivity` works as expected.

## 5. Startup Readiness
- [ ] Verify cached library restores cleanly on cold boot.
- [ ] Verify behaviour when no source is configured (should not crash, prompt user).
- [ ] Verify behaviour when a source is configured but USB is not mounted yet (must not wipe library, should wait for mount).

## 6. Diagnostics & Recovery
- [ ] Verify integration check runs properly.
- [ ] Export a diagnostics report and verify its contents.
- [ ] Verify stock disable/restore actions are documented and work (if applicable/tested).
- [ ] Verify root unavailable/failure paths fail gracefully and use non-root fallbacks.

## 7. USB / Source Repair
- [ ] Verify source repair state for `/storage/usbdisk0` works properly.
- [ ] Verify source repair state for `/storage/usbdisk1` works properly.
- [ ] Verify any discovered `usbdiskN` paths work (e.g. `/storage/usbdiskN`).
- [ ] Verify `/mnt/media_rw/usbdiskN` paths for root-assisted diagnostics (if applicable).
