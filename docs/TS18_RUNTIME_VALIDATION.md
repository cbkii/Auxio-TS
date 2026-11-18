# TS18 runtime validation checklist


## v3.5.0 Music remediation matrix

**Observed:** the original Boot 2 source session reports playing, active item `-1` and blank
metadata despite 1,069 queued items; its four Previous entries record indices 30, 31, 31 and
33 without decision-time positions. Those traces cannot qualify rewind semantics. The
captured APK predates the merged saved-queue/canonical-identity repairs in PR #287.

This matrix maps the remediation plan's Music issues to maintained checks. A named test is
coverage, not a passed result for a new source head. Run the matching checks at that head;
record build, API29 integration and physical results separately.

| Issue | Automated coverage | Remaining exact-TS18 qualification |
| --- | --- | --- |
| M1 background readiness | `LauncherPreparationTest` enters only the existing service and rejects expired entry without starting any service/Activity; `PrimitiveTransportOwnerTest.backgroundRestoreRecoversLegacyQueueWithoutPrimitiveDescriptorOrSnapshot` restores a real multi-item saved-state shape through the owner; `StartupPlaybackPolicyTest` preserves silent preparation. | Real launcher UID/browser admission, no MainActivity, recents cleared, service disconnect/reconnection, process recreation and USB/SAF availability. The original bind failure's exact firmware cause remains unknown. |
| M2 first HOME Play | `LauncherMediaSessionContractTest`, `HomePlayDeadlineTest`, `HomePlayDeadlineGateTest` and `RestoreIntentArbiterTest` cover one bounded request, slow restore/expiry, cancellation/Pause and newer intent. | One audible first tap within the original budget, from the real saved queue with a late source. Connection, playable state and actual audio need separate observations. |
| M3 canonical identity | `PrimitiveTransportOwnerTest.capturedSizeQueueKeepsIdentityThroughThirtyNextCommandsAfterCachedHandoff` covers 1,069 members and 30 actual owner Next calls plus return to the retained decoder; the browse-unavailable case preserves identity independently. | Correct audible item, title/artist, active queue ID and position across 30 Next commands, including rapid bursts, delayed taps, process recreation and deferred browsing. Player/persistence boundaries in the owner test are doubles. |
| M4 published display | `LauncherMediaSessionContractTest` exercises actual session publication; the companion launcher API29 actual-pair job covers the real browser/decoder/session across UIDs with deliberate metadata gaps. | Notification, session and launcher converge on each actual item. The three-item gap probe is distinct from the 1,069-member owner test and does not prove the full captured handoff path. |
| M5 Previous | `PrimitiveTransportOwnerTest` checks live positions 0/2,999/3,000/3,001 ms with paused/playing and committed OFF/ALL order, plus Next→Next→Previous. | Primitive/raw rewind is above 3,000 ms when enabled; canonical playback delegates to the existing player's Previous policy. Verify both sides of the decision threshold, first-item/repeat behaviour and shuffled canonical playback against the actual player. Transport diagnostics now include numeric position and configured rewind state. |
| M6 startup regression | Cached-library handoff, no extra prepare/scan, passive joining and incomplete legacy membership refusal have owner regressions. | Measure first playable/current-item publication and first audio against the same saved source/queue. No local test or merged CI result proves TS18 latency improved. |
| P1 shuffle persistence | `PrimitiveTransportOwnerTest`, `PlaybackViewModelShuffleTest`, `PlaybackStateManagerShuffleScopeTest`, `PersistenceMigrationTest` and `PersistenceCancellationTest` cover scope/order, committed durability and rejected writes. | OFF→ALL→OFF before hydration; ALL→GENRE→OFF afterwards, then process recreation/reboot and ACC. Issue #288 remains a qualification item; do not clear data or treat requested in-memory scope as committed state. |

The focused owner tests do not prove framework service admission or output audibility. Missing or
incomplete saved references must remain unavailable rather than silently substituting All Songs.
Run immediate startup first, then app/service restart, reboot, electrical cold boot and ACC
sleep/wake as separate levels. A fresh kernel boot alone does not prove electrical cold state.

## Audit-remediation validation gate (PR #276)

This gate supplements the normal launcher/bridge/storage/ACC checks below. Record the installed
signed APK's source SHA and SHA-256 before each attempt. The runtime target is the physical
Android 10 / API 29 TS18, not only a Pixel API 29 emulator.

| Check | Setup and action | Pass | Fail | Evidence |
| --- | --- | :---: | :---: | --- |
| Disabled overlay external entry | Grant overlay permission, disable floating controls in app settings, then launch the exported floating-controls activity from another package (or use `adb shell am start -n com.tw.media/org.oxycblt.auxio.car.overlay.CarOverlayActivity`). | ☐ | ☐ | Screenshot/video, preference state before/after |
| Overlay confirmation cancellation | On the confirmation prompt select Cancel. | ☐ | ☐ | Floating controls remain disabled; playback was not cold-started by the prompt |
| Explicit overlay enable | Reopen the floating-controls launcher and confirm. | ☐ | ☐ | Overlay starts, existing playback is restored according to autoplay policy, no duplicate MediaSession |
| Existing overlay launcher | With controls already enabled, relaunch the alias. | ☐ | ☐ | Direct launch/recovery without another consent prompt or duplicate overlay |
| Private Binder rejection | Use a separate non-system-UID test client to bind the private command action and submit the Track-C transaction. | ☐ | ☐ | Rejected as untrusted; no private library-start side effect; record service lifecycle separately |
| Trusted Binder command | With stock `com.tw.music` preserved and the LSPosed bridge scoped only to that package, perform play/pause, next/previous and seek. | ☐ | ☐ | Exactly one command is admitted/applied; cold-start and reconnect still work |
| Mixed-access USB root | Prepare a source containing an unreadable candidate followed by at least one readable audio file; add it through DirectFS. | ☐ | ☐ | Readable media is found and playable; path remains within allowed canonical root |
| USB removal/reinsert | Remove media during playback, then reconnect and retry source validation. | ☐ | ☐ | Cached library is retained as designed; no destructive reset; bounded recovery |
| Canonical queue persistence | For a large queue, seek/skip/repeat and restart the service; repeat after shuffle/reorder. | ☐ | ☐ | Current item, order, play intent and position recover; no partial queue/database state |
| Paused long-session ticker | Pause audio with Topway broadcast mode enabled, then observe CPU/wakeups and progress publication. | ☐ | ☐ | No 1-second paused ticker wakeups; resume restores accurate progress |
| ACC and recovery | Sleep/wake and reboot after the above tests. | ☐ | ☐ | Single playback/session/notification, DoFun widgets and steering controls recover |
| Rollback | Disable LSPosed bridge, reboot, start genuine stock app. | ☐ | ☐ | Stock media app works without Auxio intervention |

**CI prerequisites:** the exact review head must pass Android Build, Android Quality (formatting,
unit tests, lint), API 29 runtime smoke, Manual Release Smoke and release/minified package
validation. GitHub Actions success is not a substitute for TS18 bridge, OEM firmware, audio-focus,
ACC and removable-storage qualification.

**Performance evidence:** record before/after values for first audible audio, queue save latency,
row writes during a position-only save, and RSS/PSS + CPU/wakeup counts during prolonged pause.
The position-only optimisation is still predicted until measured on the limiting hardware.


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

The former exact-package Auxio application is retired and has no build artefact. Remove and reboot
away from any retired exact-package Magisk overlay before collecting evidence.

## Pre-install identity gate

```sh
adb shell cmd package list packages |
  grep -E 'com\.tw\.music|com\.tw\.media|org\.oxycblt\.auxio\.ts18bridge|com\.dofun\.variety'
adb shell pm path com.tw.music
adb shell dumpsys package com.tw.music |
  grep -iE 'codePath|versionCode|versionName|userId|sharedUserId|flags|privateFlags|enabled|User 0'
adb shell dumpsys package com.tw.media |
  grep -iE 'codePath|versionCode|versionName|userId|enabled|User 0' || true
stock_apk="$(adb shell pm path com.tw.music | tr -d '\r' | sed -n 's/^package://p' | head -n1)"
adb pull "${stock_apk}" /tmp/ts18-stock-com.tw.music.apk
apksigner verify --verbose --print-certs /tmp/ts18-stock-com.tw.music.apk |
  grep 'Signer #1 certificate SHA-256 digest'
```

Stop if `com.tw.music` is not the verified system priv-app or an Auxio Magisk overlay is still
active. Its certificate SHA-256 must be
`AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3`. Do not
disable, uninstall-for-user, replace, clear, or re-sign stock `com.tw.music`.

## Install and LSPosed configuration

```sh
adb install -r app/build/outputs/apk/release/app-release.apk
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
