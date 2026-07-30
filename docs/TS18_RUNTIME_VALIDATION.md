# TS18 runtime validation checklist

Use this checklist for the supported TS18 layout:

- genuine stock `com.tw.music` remains installed, enabled, platform-signed, and UID 1000;
- signed Auxio-TS runs separately as `com.tw.media`;
- the signed API-100 addon runs only inside `com.tw.music`;
- the addon connects stock integration surfaces to Auxio's Android media service.

Static tests do not prove DoFun launcher/widget parity. Final acceptance requires the physical
TS18/Topway head unit and a rollback test.

## Build and device record

| Field | Value |
| --- | --- |
| Auxio-TS commit SHA | |
| Auxio APK filename/SHA-256 | |
| Bridge APK filename/SHA-256 | |
| Expected Auxio package | `com.tw.media` |
| Bridge package | `org.oxycblt.auxio.ts18bridge` |
| Bridge static/recommended scope | `com.tw.music` only |
| Head-unit model/firmware | `s9863a1h10_Natv`, Android 10 / SDK 29 on the captured target |
| DoFun/Variety package/version | |
| LSPosed version | |
| Tester/date | |

`topwayTwMusic` is an internal build/test fixture. Do not install it for this campaign. Remove and
reboot away from the retired exact-package Magisk overlay before collecting evidence.

## Pre-install identity gate

```sh
adb shell cmd package list packages |
  grep -E 'com\.tw\.music|com\.tw\.media|org\.oxycblt\.auxio\.ts18bridge|com\.dofun\.variety'
adb shell pm path com.tw.music
adb shell dumpsys package com.tw.music |
  grep -iE 'codePath|versionCode|versionName|userId|sharedUserId|flags|privateFlags|enabled|User 0'
adb shell dumpsys package com.tw.media |
  grep -iE 'codePath|versionCode|versionName|userId|enabled|User 0' || true
```

Stop if `com.tw.music` is not the verified system priv-app or an Auxio Magisk overlay is still
active. Do not disable, uninstall-for-user, replace, clear, or re-sign stock `com.tw.music`.

## Install and LSPosed configuration

```sh
adb install -r app/build/outputs/apk/topwayTwMedia/release/app-topwayTwMedia-release.apk
adb install -r lsposed-bridge/build/outputs/apk/release/lsposed-bridge-release.apk
```

In LSPosed:

1. enable **Auxio-TS TS18 Intent Bridge**;
2. confirm the scope contains only `com.tw.music`;
3. do not add Auxio, DoFun, Android framework, System UI, or `system_server`;
4. reboot the head unit.

The module declares `staticScope=true`. A manager that shows a broader selection should be treated
as misconfigured until reduced to the packaged single scope.

## Post-boot package and component checks

```sh
adb shell pm path com.tw.music
adb shell pm path com.tw.media
adb shell pm path org.oxycblt.auxio.ts18bridge
adb shell cmd package resolve-activity --brief \
  -n com.tw.media/com.tw.music.MusicActivity
adb shell cmd package query-intent-services \
  -a android.media.browse.MediaBrowserService |
  grep 'com.tw.media/com.tw.music.MusicService'
```

Expected:

- stock `com.tw.music` still resolves from `/system/priv-app/...`;
- Auxio resolves as `com.tw.media`;
- exactly one Auxio external media-browser service resolves;
- the bridge never becomes a launcher or media-service package.

## Bridge connection and media session

Open Auxio once and grant its normal storage/media permissions. Then:

```sh
adb shell dumpsys media_session |
  grep -i -A80 'com\.tw\.music\|com\.tw\.media\|auxio'
adb shell dumpsys activity services |
  grep -iE 'com\.tw\.media|MusicService|AuxioService'
adb shell dumpsys notification |
  grep -iE 'com\.tw\.media|Auxio'
adb shell dumpsys audio |
  grep -iE 'com\.tw\.music|com\.tw\.media|focus|USAGE_MEDIA|STREAM_MUSIC|com\.tw\.service'
```

Expected: Auxio owns the playback session/service/notification. The stock process supplies the
Topway-facing integration identity through the bridge. Two simultaneous Auxio playback services
or duplicate media notifications are failures.

## DoFun launch and widget checks

| Action | Expected |
| --- | --- |
| Open the DoFun music panel | Music entry remains present |
| Tap the music hotseat | Auxio `com.tw.media` opens through the bridged stock action |
| Start a track in Auxio | Title, artist, album, duration, and progress update |
| Play/pause | Auxio playback toggles once |
| Next/previous | Exactly one track transition occurs |
| Seek from the panel | Auxio position changes to the requested value |
| Background/foreground both apps | Connection and metadata recover |

Record two repetitions of every control, then repeat after a cold reboot.

## Broadcast observation

```sh
adb shell logcat -v time |
  grep -iE 'Auxio|TS18IntentBridge|tw\.music|tw\.media|music_progress|dofun|MediaSession|MediaBrowser'

adb shell am broadcast -a com.tw.music.action.pp
adb shell am broadcast -a com.tw.music.action.next
adb shell am broadcast -a com.tw.music.action.prev
adb shell am broadcast -a com.tw.music.action.cmd --es cmd update
adb shell am broadcast -a com.android.launcher.widget_music_progress \
  --ei music_progress 30000
```

These injections are diagnostics only. Final contracts must be based on traffic observed from the
real DoFun/stock flow in at least two runs. Do not infer private Binder commands from a successful
manual broadcast.

## Storage and overlay regression checks

```sh
adb shell ls -lah /sdcard/Music 2>/dev/null || true
adb shell find /storage -maxdepth 1 -type d -name 'usbdisk*' -print 2>/dev/null
adb shell content query --uri content://media/external/audio/media \
  --projection _id:_display_name:relative_path:volume_name \
  --where 'is_music=1' | head -50
```

Validate:

- local and discovered `/storage/usbdiskN` media scans and plays;
- SAF grants and selected sources survive reboot/remount;
- unavailable USB sources do not erase saved configuration;
- overlay permission grant/revoke works on SDK 29;
- floating controls remain on-screen at 1280x720 and restore only when enabled;
- ACC sleep/wake does not create a duplicate session, notification, or bridge connection.

Use the external TS18 collector for extended evidence. Do not restore retired production
diagnostic screens or broad release logging.

## Restart and reboot checks

```sh
adb shell am force-stop com.tw.media
# Reopen from DoFun and verify reconnect/launch/control recovery.

adb shell am force-stop com.tw.music
# Reopen the genuine stock activity or restart DoFun and verify the bridge reloads.

adb shell am force-stop com.dofun.variety
# Wait for launcher restart and repeat the widget checks.
```

After a cold reboot, verify the same package identities, single scope, media service/session,
metadata, controls, storage, and overlay state.

## Kill-switch acceptance

1. disable the bridge module in LSPosed;
2. reboot;
3. verify genuine stock `com.tw.music` launches and operates without Auxio;
4. verify Auxio still opens independently as `com.tw.media`;
5. re-enable only `com.tw.music`, reboot, and repeat the bridge checks.

Failure to preserve stock behaviour with the module disabled blocks release.

## Evidence and pass/fail summary

Keep evidence outside git unless a maintainer requests a small redacted fixture. Retain APK hashes,
package dumps, LSPosed module/scope screenshots, MediaSession/audio/service/notification dumps,
filtered logs, DoFun photos/video, and firmware/DoFun/LSPosed versions.

| Check | Pass | Fail | Notes |
| --- | --- | --- | --- |
| Genuine stock identity retained | ☐ | ☐ | |
| Bridge scope is only `com.tw.music` | ☐ | ☐ | |
| Auxio resolves as `com.tw.media` | ☐ | ☐ | |
| Single Auxio service/session/notification | ☐ | ☐ | |
| DoFun launch and metadata work | ☐ | ☐ | |
| Transport and seek work twice | ☐ | ☐ | |
| Storage and overlay regressions pass | ☐ | ☐ | |
| Process/ACC/reboot recovery passes | ☐ | ☐ | |
| Disabled-module rollback preserves stock | ☐ | ☐ | |
