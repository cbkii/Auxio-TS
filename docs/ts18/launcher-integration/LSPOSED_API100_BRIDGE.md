# Auxio-TS LSPosed API 100 stock-music bridge

## Purpose

The bridge keeps the genuine Topway `com.tw.music` package installed, platform-signed and running as `android.uid.system` / UID 1000. It injects narrowly into that genuine process and forwards the observed music activity and control contract to the normal Auxio-TS package `com.tw.media`.

It does **not** copy or spoof the Topway platform key. It does **not** change Package Manager signatures, shared UID records, `packages.xml`, `system_server`, MCU/CAN state, firmware or protected package files.

## Evidence status

- **Observed:** the captured stock package is `com.tw.music`, UID 1000, version code 118 and signed by SHA-256 `AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3`.
- **Observed:** stock `MusicActivity`, `MusicService`, command receiver `com.tw.music.k`, seek receiver `com.tw.music.j` and presenter `com.eckom.xtlibrary.b.f.e.a` implement the current control path.
- **Observed:** Auxio-TS `com.tw.media` exports `com.tw.music.MusicActivity`, the stock-compatible `com.tw.music.MusicService` MediaBrowser wrapper, the narrow Topway command receiver and the media-button receiver.
- **Inferred:** code running inside the genuine stock process can preserve the sender process identity expected by Topway/DoFun while using normal explicit Android IPC to control Auxio-TS.
- **Requires device validation:** fixed DoFun widget launch, control, metadata, progress, ACC sleep/wake, cold boot and launcher restart behaviour on the exact TS18 build.

## Safety model

The module is static-scoped only to:

```text
com.tw.music
```

It additionally refuses to bridge unless the loaded process is the main `com.tw.music` process and the installed stock package has all three captured identity properties: UID 1000, version code 118 and the exact Topway platform certificate fingerprint. Before forwarding, it also verifies that every required cross-package Auxio activity, receiver and MediaBrowser service is enabled and exported.

If Auxio-TS or any required exported component is unavailable, the hook **fails open**: it does not suppress the stock call. Every hook callback catches failures so a bridge error does not intentionally crash the stock process.

### Kill switch

Create this empty file to disable all redirection and forwarding without uninstalling the module:

```text
/storage/emulated/0/Auxio-TS/disable-lsposed-bridge
```

The marker is checked on every launch/control operation and while publishing mirrored state. When present, the genuine stock path remains active.

Remove the marker to enable the bridge again. Restart `com.tw.music` or reboot if a stale activity remains visible.

## What is bridged

| Stock surface | Bridge action |
| --- | --- |
| `com.tw.music.MusicActivity` | Opens `com.tw.media/com.tw.music.MusicActivity`, then finishes the stock activity only after the target launch succeeds. |
| `MusicService.onStartCommand()` | Forwards observed `prev`, `next`, `pp` and `update` commands. The stock handler is skipped only after forwarding succeeds. |
| Dynamic command receiver `com.tw.music.k` | Forwards the observed command actions and skips the stock receiver only after success. |
| Seek receiver `com.tw.music.j` | Forwards `music_progress` to Auxio-TS and skips the stock seek only after success. |
| Presenter `rb`, `pb`, `ba`, `fa`, `seekTo(int)` | Forwards previous, next, pause, play and seek calls that bypass public broadcasts. |
| Auxio Android MediaBrowser/MediaSession | Connects through `com.tw.media/com.tw.music.MusicService` and mirrors title, artist, album, media URI, playback state and one-second progress broadcasts from the genuine stock process. |

The bridge uses Android public IPC between packages. It does not import or copy Topway implementation classes.

## Build provenance

The module targets modern LSPosed/libxposed **API 100**. Compile-only stubs are an unmodified subset of the official `libxposed/api` commit:

```text
45f3e9722a3d4a3e6dae6cc1b51d6583767ec940
```

The API classes are not packaged into the bridge APK. LSPosed provides them at runtime. The bridge has minimum SDK 29 for the Android 10 TS18 runtime and uses the repository's current target SDK for installation compatibility. CI verifies the API-100 metadata, SDK values and that the APK defines no `io.github.libxposed` classes.

## Installation

1. Keep the genuine stock `com.tw.music` enabled and unchanged.
2. Confirm it is version code 118, UID 1000 and signed by the captured Topway certificate.
3. Install the normal Auxio-TS `topwayTwMedia` release so the package is exactly `com.tw.media`.
4. In Auxio-TS, select a music source and verify normal playback first.
5. Create the kill-switch marker shown above.
6. Install the bridge APK.
7. Enable it in LSPosed. Its static scope must show only `com.tw.music`; do not add `system_server`, DoFun, Auxio or other Topway packages.
8. Reboot the head unit.
9. Confirm the stock music app still opens and controls normally while the marker exists.
10. Remove the marker, force-stop/reopen `com.tw.music` or reboot, then run the validation below.

**STOP:** do not proceed if the installed stock package is not version code 118 and UID 1000, the signer fingerprint differs, `com.tw.media` is not installed, LSPosed cannot restrict scope, or boot-loop recovery is not available.

## Device validation

Capture timestamps and test one boundary at a time:

1. Tap the fixed DoFun music widget. Expected: Auxio-TS `MusicActivity` opens; stock activity closes.
2. Test previous, next and play/pause from the fixed widget and steering-wheel/media keys.
3. Start playback, pause and resume. Expected: DoFun title/artist/album and play state follow Auxio.
4. Seek through the widget where supported. Confirm the unit conversion matches the configured Auxio Topway seek policy.
5. Test launcher restart, stock-process restart and Auxio-process restart.
6. Test cold boot, ACC sleep/wake and a second launch after resume.
7. Disconnect/reconnect USB storage and confirm the bridge does not make unavailable queue items playable or alter source authority.
8. Create the kill-switch marker while running. Expected: new commands stop redirecting and stock behaviour remains available.
9. Remove the marker and restart the stock process. Expected: bridging resumes.

Useful log filter:

```text
Auxio-TS LSPosed bridge
```

Do not leave broad Ylog/debug collection enabled after the defined test window.

## Rollback

1. Recreate `/storage/emulated/0/Auxio-TS/disable-lsposed-bridge`.
2. Disable the module in LSPosed and reboot.
3. Uninstall only the bridge APK if it is no longer needed.
4. Leave the genuine stock `com.tw.music` APK and package data untouched.

If the unit cannot reach Android UI, use the already proven LSPosed/Magisk boot-loop recovery path. Do not flash firmware, alter Package Manager databases or delete the stock APK as bridge recovery.
