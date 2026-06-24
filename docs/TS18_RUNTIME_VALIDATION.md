# TS18 runtime validation checklist

Practical on-device checklist for validating Auxio-TS on a TS18/Topway head unit running DoFun Variety (`com.dofun.variety`). Static scripts, Gradle builds, emulator tests, and Roborazzi screenshots are useful pre-flight evidence, but do **not** prove full DoFun launcher/widget parity without real TS18 hardware validation.

The exact target-device profile is summarised in [`evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md`](evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md).

## Test build under validation

| Field                                 | Value                                                                                                 |
| ------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| Auxio-TS commit SHA                   |                                                                                                       |
| APK file                              | `app/build/outputs/apk/topwayTwMusic/release/app-topwayTwMusic-release.apk` or alternate variant path |
| Expected package                      | `com.tw.music` for exact replacement; `com.tw.media` for alternate DoFun entry once implemented       |
| Expected exact launcher component     | `com.tw.music/.MusicActivity`                                                                         |
| Expected alternate launcher component | `com.tw.media/com.tw.music.MusicActivity`                                                             |
| Head unit model/firmware              | `s9863a1h10_Natv`, Android 10 / SDK 29, if testing the captured target device                         |
| DoFun/Variety package/version         |                                                                                                       |
| Tester/date                           |                                                                                                       |

Use `topwayTwMusicRelease` or another signed exact-package build for final DoFun identity validation. `topwayTwMusicDebug` installs as `com.tw.music.debug`, which is useful for development but is not DoFun's fixed stock package target.

## Validation lanes

### Lane A — normal app shell / TermOne / Termux only

Use this lane when there is no ADB shell, no root, and no Shizuku package-manager authority.

Allowed:

- `getprop` inspection;
- limited `pm list packages` and `pm path` checks;
- storage/mount inspection;
- display/window/audio snapshots where not permission-denied;
- app launch/install through normal package installer.

Not allowed/expected to work:

- disabling/removing stock system packages;
- reliable `dumpsys package` for arbitrary packages;
- `am broadcast` test injection as shell;
- privileged appops/package state changes.

Older TermOne diagnostics were from this lane. The shell identity in that older capture was `u0_a177` under `u:r:untrusted_app`. The latest June root diagnostic facts are a separate Lane D capture and must not be described as normal-app UID evidence.

### Lane B — ADB shell

Use this lane only when `adb shell id` returns `uid=2000(shell)` or better.

Commands in the sections below generally assume this lane.

### Lane C — Shizuku

Use this lane when Shizuku is running and package-manager operations are deliberately routed through its shell/system-mediated authority. Validate exactly which operations were performed and keep recovery steps.

### Lane D — root/system image

Use this lane only for deliberate system package replacement, Magisk/system overlay, or firmware-image changes. Keep stock APK backups and rollback instructions. Latest exact-device root diagnostics are in this lane: `uid=0(root)`, `u:r:magisk:s0`, SELinux permissive at boot-property level, unlocked/orange device state, verity enforcing, dynamic partitions enabled, and read-only `/` plus `/vendor` dm mounts in the captured state. Root does **not** give Auxio UID 1000, platform signing, vendor signing, signature permissions, or private Topway authority.

## Pre-install checks

### Any lane

```sh
pm list packages | grep -E "com\.tw\.music|com\.tw\.media|org\.oxycblt\.auxio|com\.dofun\.variety" || true
pm path com.tw.music 2>/dev/null || true
pm path com.tw.media 2>/dev/null || true
pm path com.dofun.variety 2>/dev/null || true
getprop ro.build.version.release
getprop ro.build.version.sdk
getprop ro.product.model
getprop ro.product.device
getprop service.adb.tcp.port
getprop persist.adb.tcp.port
```

If stock `com.tw.music` is present at `/system/priv-app/...`, a user-signed `topwayTwMusicRelease` may fail to install until stock package state is managed through ADB shell, Shizuku, root, firmware control, or matching signing.

### ADB/Shizuku/root package-state checks

```sh
adb shell cmd package list packages | grep -E "com\.tw\.music|com\.tw\.media|org\.oxycblt\.auxio|com\.dofun\.variety"
adb shell sh -c 'for p in com.tw.music com.tw.media; do pm path "$p" 2>/dev/null || true; done'
adb shell dumpsys package com.tw.music | grep -iE 'codePath|versionCode|versionName|userId|sharedUserId|priv|system|enabled|User 0|installed' || true
adb shell dumpsys package com.tw.media | grep -iE 'codePath|versionCode|versionName|userId|sharedUserId|priv|system|enabled|User 0|installed' || true
adb shell dumpsys package com.dofun.variety | grep -iE 'codePath|versionCode|versionName|userId|enabled|User 0|installed'
```

Prefer reversible disable before uninstall-for-user:

```sh
adb shell pm disable-user --user 0 com.tw.music
```

Only if needed and approved:

```sh
adb shell pm uninstall --user 0 com.tw.music
```

Recovery:

```sh
adb shell cmd package install-existing --user 0 com.tw.music
adb shell pm enable com.tw.music
adb shell pm clear com.dofun.variety || true
adb shell pm clear com.tw.music || true
adb shell pm clear com.tw.media || true
adb reboot
# After cold boot, repeat package resolution, DoFun hotseat, media-session, and broadcast checks.
```

## Install

Exact `com.tw.music` replacement:

```sh
adb install -r app/build/outputs/apk/topwayTwMusic/release/app-topwayTwMusic-release.apk
```

Alternate `com.tw.media` DoFun entry once implemented:

```sh
adb install -r app/build/outputs/apk/topwayTwMedia/release/app-topwayTwMedia-release.apk
```

Normal package-installer testing from the head unit is acceptable only as an installability check. It does not replace ADB/root/Shizuku validation when stock package conflicts exist.

## Package resolution checks

```sh
# Confirm exact com.tw.music component resolves when that variant is installed
adb shell cmd package resolve-activity --brief -n com.tw.music/com.tw.music.MusicActivity

# Confirm alternate com.tw.media component resolves when that variant is installed
adb shell cmd package resolve-activity --brief -n com.tw.media/com.tw.music.MusicActivity

# APP_MUSIC category
adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.APP_MUSIC -p com.tw.music
adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.APP_MUSIC -p com.tw.media

# Manifest/service/provider detail
adb shell dumpsys package com.tw.music | grep -iE 'MusicActivity|MusicService|MusicWidgetProvider|MAIN|MUSIC_PLAYER|APP_MUSIC|LAUNCHER|MediaBrowserService|CoverProvider|com.tw.music.action|widget_music_progress'
adb shell dumpsys package com.tw.media | grep -iE 'MusicActivity|MusicService|MusicWidgetProvider|MAIN|MUSIC_PLAYER|APP_MUSIC|LAUNCHER|MediaBrowserService|CoverProvider|com.tw.music.action|widget_music_progress'

# Exact Topway component checks
adb shell cmd package resolve-activity --brief -n com.tw.music/com.tw.music.MusicActivity
adb shell cmd package resolve-activity --brief -n com.tw.media/com.tw.music.MusicActivity
adb shell cmd package query-intent-services -a android.media.browse.MediaBrowserService | grep -E 'com.tw.music/com.tw.music.MusicService|com.tw.media/com.tw.music.MusicService'
adb shell dumpsys package com.tw.music | grep -F 'com.tw.music.view.MusicWidgetProvider'
adb shell dumpsys package com.tw.media | grep -F 'com.tw.music.view.MusicWidgetProvider'
```

## Media browser check

```sh
adb shell cmd package query-intent-services -a android.media.browse.MediaBrowserService
# Expected: installed Auxio-TS Topway-compatible package is listed.
```

## Media session check

```sh
adb shell dumpsys media_session | grep -i -A80 'com.tw.music\|com.tw.media\|auxio'
# Expected: active session with metadata when playing.

adb shell dumpsys audio | grep -iE 'com.tw.music|com.tw.media|auxio|focus|USAGE_MEDIA|STREAM_MUSIC|com.tw.service'
# Expected: normal media audio focus while playing; note whether com.tw.service mediates focus.
```

Also record any restored media-button receiver:

```sh
adb shell dumpsys media_session | grep -i 'Restored MediaButtonReceiver'
```

The captured target-device diagnostics showed ZLink as the restored media-button receiver in that state. That is a runtime observation, not a permanent failure condition.

## DoFun widget recognition

| Step                                   | Expected                                                         |
| -------------------------------------- | ---------------------------------------------------------------- |
| Open DoFun launcher music panel/widget | Auxio-TS icon appears in music hotseat or selectable music slot  |
| Tap exact music hotseat icon           | Auxio-TS opens through `com.tw.music/com.tw.music.MusicActivity` |
| Tap alternate entry if testing it      | Auxio-TS opens through `com.tw.media/com.tw.music.MusicActivity` |
| Play a track in Auxio-TS               | Widget shows track title/artist                                  |

## Widget playback controls

| Action                       | Expected                  |
| ---------------------------- | ------------------------- |
| Tap play/pause on widget     | Playback toggles          |
| Tap next on widget           | Next track plays          |
| Tap prev on widget           | Previous track plays      |
| Seek via widget progress bar | Playback position changes |

## Broadcast verification

```sh
# Watch Topway broadcasts
adb shell logcat -v time | grep -iE 'Auxio|Topway|tw.music|tw.media|music_progress|dofun|variety|MediaSession|MediaBrowser'

# Manual control tests
adb shell am broadcast -a com.tw.music.action.pp
adb shell am broadcast -a com.tw.music.action.next
adb shell am broadcast -a com.tw.music.action.prev

# Generic command-action form
adb shell am broadcast -a com.tw.music.action.cmd --es cmd update
adb shell am broadcast -a com.tw.music.action.cmd --es cmd pp
adb shell am broadcast -a com.tw.music.action.cmd --es cmd next
adb shell am broadcast -a com.tw.music.action.cmd --es cmd prev

# Optional seek/progress action if the launcher emits it
adb shell am broadcast -a com.android.launcher.widget_music_progress --ei music_progress 30000
```

## Storage/media-library validation

The exact target diagnostics showed USB storage under a `/storage/usbdiskN` style mount. Do not hard-code `usbdisk0`; use the diagnostics screen and shell inspection to record the actual mounted suffix on the device under test.

Validate at least:

```sh
adb shell ls -lah /storage | grep -i usbdisk || true
adb shell find /storage -maxdepth 1 -type d -name 'usbdisk*' -print 2>/dev/null
adb shell for d in /storage/usbdisk*; do [ -d "$d" ] && ls -lah "$d"; done
adb shell ls -lah /sdcard/Music 2>/dev/null || true
```

Manual checks:

- grant Android 10 storage permission when prompted;
- confirm Auxio-TS can scan `/sdcard/Music`;
- confirm Auxio-TS can scan or otherwise access `/storage/usbdiskN` where permitted;
- confirm playback works from the same storage locations stock `twMusic` can access;
- test TS18 Health Diagnostics screen (under Settings > Music > TS18 Health Diagnostics) to verify the separate automated report, guided DoFun integration test, timed activity capture, one-shot startup capture, and event journal;
- in the guided DoFun test, verify all instructions are visible before departure, the optional metadata-marker consent checkbox is honoured, the countdown automatically starts capture, the user leaves Auxio once, taps the DoFun Music card last, returns once, and answers numbered questions with numbered choices plus optional free text;
- in timed capture, verify 2, 5, 10, and 15 minute selections are enforced by the foreground service and the Stop action preserves a partial report;
- arm one-shot startup capture, then validate true boot capture when Android permits it and first normal Auxio-start fallback when boot foreground-service start is blocked; after successful capture, the armed ID/expiry should be consumed;
- verify noisy/problem path display, confirmation before exclusion, exact user-selected source preservation, alias/canonical distinction, and report output for SAF grants, MediaStore volumes, dynamic `/storage/usbdiskN` roots, exclusions, and temporarily unavailable sources;
- verify save/copy/share output: failed saves must not discard the report, successful saves should show the full path, and destination discovery must not create directories until save time;
- retest after ACC/reboot and after USB disk re-mount.

## Overlay validation for PR #53 and later

The exact device has:

- Android 10 / SDK 29;
- 1280x720 display;
- top status bar around 55px;
- right navigation bar around 55px.

Validate:

- overlay permission request opens usable system settings;
- overlay service starts without Android 14-only foreground-service type crashes;
- overlay never saves a permanently off-screen position;
- after reset, overlay appears top-centre with its top edge at physical display `y = 0` and is not shifted down by the old 55px status-bar inset;
- overlay is allowed to overlap/draw into the status-bar or right-nav areas where the firmware permits, while stock Android may still keep `TYPE_APPLICATION_OVERLAY` below critical system windows in z-order;
- overlay can be dragged away and back to the physical top edge and persists position safely;
- overlay recovers after process death/ACC/reboot only when enabled and permitted.

## Process restart check

```sh
adb shell am force-stop com.tw.music
# Reopen from DoFun widget — app should restart and resume.

adb shell am force-stop com.tw.media
# If alternate variant is installed, retest the alternate DoFun entry.
```

## Launcher restart check

```sh
adb shell am force-stop com.dofun.variety
# Wait for launcher to restart — music widget should still show Auxio-TS state.
```

## Reboot check

Reboot the head unit. After boot:

- DoFun music widget should still recognise the installed Auxio-TS Topway-compatible variant;
- tapping the music hotseat should open Auxio-TS;
- previous playback state should be recoverable;
- overlay state should match the user setting and permission state;
- USB media paths should be re-scannable after mount completes.

## Evidence to retain outside git

Keep runtime evidence outside the repository unless a maintainer explicitly asks for a small, redacted fixture. Useful artefacts are:

- `adb shell dumpsys package com.tw.music` / `com.tw.media`;
- `adb shell dumpsys media_session`;
- `adb shell dumpsys audio`;
- filtered logcat covering DoFun widget recognition, controls, playback state, overlay behaviour, and storage scan;
- screenshots/photos of DoFun widget recognition, metadata, progress, and controls;
- exact APK filename, commit SHA, head-unit firmware, and DoFun version.

## Pass/fail summary

| Check                               | Pass | Fail | Notes |
| ----------------------------------- | ---- | ---- | ----- |
| Package resolves as intended        | ☐    | ☐    |       |
| APP_MUSIC intent resolves           | ☐    | ☐    |       |
| MediaBrowserService listed          | ☐    | ☐    |       |
| MediaSession active during playback | ☐    | ☐    |       |
| DoFun widget shows Auxio-TS         | ☐    | ☐    |       |
| Widget controls work                | ☐    | ☐    |       |
| Broadcast commands work             | ☐    | ☐    |       |
| Storage scan/playback works         | ☐    | ☐    |       |
| Overlay safe on Android 10          | ☐    | ☐    |       |
| Process restart recovers            | ☐    | ☐    |       |
| Launcher restart stable             | ☐    | ☐    |       |
| Reboot stable                       | ☐    | ☐    |       |

## Exact-device post-PR#53 validation addendum

Use `docs/CODEX_TS18_DEVICE_CONTEXT.md` and `docs/TS18_INSTALLATION_CONSTRAINTS.md` as the starting point for the `s9863a1h10` Android 10 device.

### Static or ADB-shell component checks

```sh
adb shell cmd package resolve-activity --brief com.tw.music/com.tw.music.MusicActivity
adb shell cmd package resolve-activity --brief com.tw.media/com.tw.music.MusicActivity
adb shell dumpsys package com.tw.music | sed -n '/Package \[com.tw.music\]/,/User 0/p'
adb shell dumpsys package com.tw.media | sed -n '/Package \[com.tw.media\]/,/User 0/p'
```

### Package-management lanes

- TermOne/normal app UID can validate app-visible storage, overlay permission flow, and local diagnostics, but cannot disable or remove stock system packages.
- ADB shell or Shizuku can manage user package state when available.
- Root, firmware/system-image control, or matching OEM signature is required for deeper system priv-app replacement assumptions.
- Prefer reversible disable before uninstall-for-user:

```sh
adb shell pm disable-user --user 0 com.tw.music
adb shell pm enable com.tw.music
adb shell cmd package install-existing --user 0 com.tw.music
```

### Expected package/service resolution

```sh
adb shell cmd package resolve-service --brief -a android.media.browse.MediaBrowserService com.tw.music
adb shell cmd package resolve-service --brief -a android.media.browse.MediaBrowserService com.tw.media
```

Expected for release variants: `com.tw.music/com.tw.music.MusicService` for `topwayTwMusicRelease` and `com.tw.media/com.tw.music.MusicService` for `topwayTwMediaRelease`. If `org.oxycblt.auxio.AuxioService` also resolves as an exported browse service in either Topway-compatible release, treat it as a duplicate-service failure and roll back to the previous APK or re-enable the stock package with the recovery commands in `docs/TS18_INSTALLATION_CONSTRAINTS.md`.

### Android 10 storage and USB/UDisk checks

```sh
adb shell cmd media_session list-sessions 2>/dev/null || true
adb shell content query --uri content://media/external/audio/media --projection _id:_display_name:relative_path:volume_name --where "is_music=1" | head -50
adb shell find /sdcard/Music /storage/emulated/0/Music /storage/usbdiskN -maxdepth 2 -type f 2>/dev/null | head -50
```

Expected: songs copied to local `/sdcard/Music` and to the discovered `/storage/usbdiskN` root appear either in MediaStore query output or in the filesystem probe. Failure interpretation: if discovered USB files exist but do not appear in MediaStore after a media rescan/reboot, use the diagnostics report to compare source accessibility, aliases, SAF grants, exclusions, and temporary-unavailable state before proposing any broader storage permission change.

### Runtime checks that require the real TS18 unit

- Installability of `topwayTwMusicRelease` over or alongside the stock `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk` state.
- Whether `topwayTwMediaRelease` avoids conflicts on a given firmware; do not assume it is a no-root workaround.
- DoFun hotseat/widget launch for both `com.tw.music/com.tw.music.MusicActivity` and `com.tw.media/com.tw.music.MusicActivity`.
- Duplicate media-session/service behaviour: inspect `adb shell dumpsys media_session`, start playback from DoFun/widget, and verify only one active Auxio playback session controls audio. Expected: the Topway-compatible APK exposes `com.tw.music.MusicService` as the external browse component; `org.oxycblt.auxio.AuxioService` should not appear as an additional external browse-service resolution in Topway variants.
- Duplicate service lifecycle/notification behaviour: inspect `adb shell dumpsys activity services | grep -E 'AuxioService|MusicService'` and `adb shell dumpsys notification | grep -E 'Auxio|com.tw.music|com.tw.media'` before and after DoFun/widget actions. Failure interpretation: two simultaneous foreground playback services or duplicate media notifications means the wrapper/base-service routing needs another implementation pass.
- Music-library visibility for `/sdcard/Music`, `/storage/usbdiskN`, and USB/UDisk scanning on Android 10.
- Overlay permission grant/revoke, 1280x720 bounds with about 55px top/right system bars, background-start behaviour, boot completed, and ACC wake/restore.
