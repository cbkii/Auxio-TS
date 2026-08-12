# Auxio-TS LSPosed API 100 stock-music bridge

## Purpose

The bridge keeps the genuine Topway `com.tw.music` package installed, platform-signed and running as `android.uid.system` / UID 1000. It injects narrowly into that genuine process and bridges the observed music activity and control contract to the normal Auxio-TS package `com.tw.media`.

It does **not** copy or spoof the Topway platform key. It does **not** change Package Manager signatures, shared UID records, `packages.xml`, `system_server`, MCU/CAN state, firmware or protected package files.

This is an explicit narrow execution-context exception to the normal prohibition on privileged-UID
strategies. The exception permits the optional addon to execute only inside the already-installed,
signer-verified stock process; it does not grant Auxio UID 1000, create another UID-1000 package or
weaken the stock identity checks.

This signed addon replaces the retired Auxio-TS exact-package Magisk overlay release lane. The
normal player remains `com.tw.media`; the genuine platform-signed stock `com.tw.music` remains
installed and owns its existing UID 1000 authority.

## Evidence status

- **Observed:** the captured stock package is `com.tw.music`, UID 1000, version code 118 and signed by SHA-256 `AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3`.
- **Observed:** stock `MusicActivity`, `MusicService`, command receiver `com.tw.music.k`, seek receiver `com.tw.music.j` and presenter `com.eckom.xtlibrary.b.f.e.a` implement the captured control path.
- **Observed:** Auxio-TS `com.tw.media` exports `com.tw.music.MusicActivity` and the stock-compatible `com.tw.music.MusicService` MediaBrowser wrapper.
- **Inferred:** code running inside the genuine stock process can preserve the sender process identity expected by Topway/DoFun while controlling Auxio through its public Android MediaSession.
- **Requires device validation:** fixed DoFun widget launch, control, metadata, progress, ACC sleep/wake, cold boot and launcher restart behaviour on the exact TS18 build and on each previously unseen stock APK version.
- **Porting decision — Requires TS18 runtime validation:** keep the addon optional and use it only after the in-app path is insufficient on the exact device.

## Safety and compatibility model

The module is static-scoped only to:

```text
com.tw.music
```

This scope is packaged as both the default and enforced recommendation (`staticScope=true`). Do
not broaden it in LSPosed. In particular, do not select DoFun, Auxio-TS, Android/System Framework,
Package Manager or `com.tw.service*`.

It refuses to bridge unless the loaded process is the main `com.tw.music` process, the installed package is UID 1000 and its signer matches the captured Topway platform certificate. These are the identity authorities.

Stock version code 118 remains recorded as the **known device-tested reference**, but version code is no longer a hard enablement gate. Topway may publish another legitimate build under the same platform identity. For every loaded build, the module capability-probes the public activity, service and receiver surfaces independently. Private obfuscated presenter hooks are additionally pinned to captured stock APK SHA-256 `4f5495e270a7c86bab232e2b7ee2ecd2d71f3450f6f20ed5f36feaa4229c1518`; an unseen APK retains the public paths but keeps those suppression hooks disabled. A missing or changed surface is not guessed and remains stock-controlled.

Before forwarding, the bridge verifies that the exact Auxio activity and MediaBrowser service are enabled, exported and belong to the normal non-system `com.tw.media` package.

A stock control callback is suppressed only when all of the following are true:

1. the stock UID and signer checks pass;
2. the kill switch is absent;
3. the Auxio MediaBrowser connection has produced a live MediaController;
4. Auxio's PlaybackState advertises the required transport action; and
5. the MediaController transport call returns without throwing.

If any condition is unavailable, the hook **fails open** and the stock call continues. The bridge does not treat successful broadcast delivery as proof that Auxio executed a command.

### Kill switch

Create this empty file to disable all redirection and forwarding without uninstalling the module:

```text
/storage/emulated/0/Auxio-TS/disable-lsposed-bridge
```

The marker, signer and target readiness are refreshed on a dedicated worker and published as one
atomic snapshot. Host callbacks only read that snapshot; an unknown initial state preserves the
stock path. Refreshes are bounded to a three-second cache window, so marker activation may take up
to three seconds to become effective without blocking the stock process.

Remove the marker to enable the bridge again. Restart `com.tw.music` or reboot if a stale activity remains visible.

## Bidirectional bridge

### Launcher/stock panel to Auxio-TS

| Stock surface | Bridge action |
| --- | --- |
| `com.tw.music.MusicActivity` | Opens `com.tw.media/com.tw.music.MusicActivity`, then finishes the stock activity only after the target launch succeeds. |
| `MusicService.onStartCommand()` | Maps observed `prev`, `next`, `pp` and `update` commands to the connected Auxio MediaSession. The stock handler always continues so its lifecycle and any foreground-service obligation remain intact. |
| Dynamic command receiver `com.tw.music.k` | Maps the captured command actions to Auxio transport controls and skips the stock receiver only after acknowledged dispatch. |
| Seek receiver `com.tw.music.j` | Sends `music_progress` through `MediaController.TransportControls.seekTo()` only when Auxio advertises seek support. |
| Presenter `rb`, `pb`, `ba`, `fa`, `seekTo(int)` | Maps previous, next, pause, play and seek calls that bypass the public broadcasts. Each exact method is hooked only when present. |

The control direction uses Auxio's exported `com.tw.media/com.tw.music.MusicService` MediaBrowser session. Previous, next, play, pause, play/pause and seek are sent through `MediaController.TransportControls`, not an unacknowledged implicit or explicit command broadcast.

### Auxio-TS to launcher/stock panel

The same MediaController observes Auxio metadata and PlaybackState changes. The bridge republishes the captured Topway/legacy metadata, play-state and one-second progress broadcasts from inside the genuine platform-signed stock process:

- `com.tw.music.info`;
- `com.android.music.metachanged`;
- `com.android.music.playstatechanged`;
- `com.tw.launcher.music_progress_duration`.

This is the return path from Auxio to the DoFun/Topway panel. It mirrors title, artist, album, media URI, playing state, duration and current position. Android/CI validation proves the implemented route; whether the exact fixed DoFun panel consumes every field remains **Requires device validation**.

The four actions remain implicit, and `com.tw.music.info` retains the bounded `musicPath` extra,
because those are captured parts of the stock player contract used by fixed launcher/panel
receivers. Package-targeting the broadcasts or dropping the path would no longer reproduce stock
behaviour. This intentionally has the same privacy boundary as stock: other installed broadcast
receivers can observe the mirrored media metadata. No fields outside the captured contract are
added, and physical validation must confirm whether a future version can narrow the path field.

The bridge uses Android public IPC between packages. It does not import or copy Topway implementation classes.

## Build provenance

The module targets modern LSPosed/libxposed **API 100**. Compile-only stubs are an unmodified subset of the official `libxposed/api` commit:

```text
45f3e9722a3d4a3e6dae6cc1b51d6583767ec940
```

The API classes are not packaged into the bridge APK. LSPosed provides them at runtime. The bridge has minimum SDK 29 for the Android 10 TS18 runtime and uses the repository's current target SDK for installation compatibility. CI verifies the API-100 metadata, SDK values and that the APK defines no `io.github.libxposed` classes.

The Manual Release workflow signs the bridge with the repository release key, assigns the paired
Auxio release version and publishes:

```text
Auxio-TS-vX.Y.Z-lsposed-api100-bridge.apk
```

The release asset has its own checksum and package/signing metadata sidecars. The workflow does not
publish the former `topwayTwMusic` Magisk ZIP or a raw exact-package Auxio APK.

## Dedicated CI gate

The bridge workflow remains **pull-request only**. Its build job runs when any one of these explicit gates matches:

- the PR title contains `xposed`, `lsposed`, `hook`, `bridge` or `platform sign`;
- the PR has the exact label `ci:lsposed-bridge` or `lsposed-bridge`; or
- the head branch starts with `feat/lsposed-` / `fix/lsposed-`, or contains `platform-bridge`.

The workflow listens for `labeled` and `unlabeled` events, so applying the CI label reliably starts the opt-in without rewriting the title. It deliberately does not subscribe to generic PR `edited` events, because title/body automation can otherwise cancel and restart an expensive in-progress build through workflow concurrency. Title matching remains the normal gate when a PR opens or receives a new commit; the label is the reliable trigger when a later title change should enable CI. The branch gate prevents accidental CI loss if a dedicated bridge PR title is simplified. Unrelated PRs still skip the bridge job and do not start its Gradle runner.

Same-repository bridge PRs publish the debug addon and checksum as a short-lived workflow artifact.
Fork PRs run the gate but do not publish downloadable APK binaries.

## Installation

1. Keep the genuine stock `com.tw.music` enabled and unchanged.
2. Confirm it is UID 1000 and signed by the captured Topway certificate. Record its version code; version 118 is the currently device-tested reference.
3. Install the normal Auxio-TS `topwayTwMedia` release so the package is exactly `com.tw.media`.
4. In Auxio-TS, select a music source and verify normal playback first.
5. Create the kill-switch marker shown above.
6. Install the signed bridge APK from the same Auxio-TS release.
7. Enable it in LSPosed. Its static/recommended scope must show only `com.tw.music`; do not add
   System Framework, DoFun, Auxio or other Topway packages.
8. Reboot the head unit.
9. Confirm the stock music app still opens and controls normally while the marker exists.
10. Remove the marker, force-stop/reopen `com.tw.music` or reboot, then run the validation below.

**STOP:** do not proceed if the installed stock package is not UID 1000, the signer fingerprint differs, `com.tw.media` is not installed, LSPosed cannot restrict scope, or boot-loop recovery is not available. For a stock version other than 118, proceed only as a controlled compatibility test: preserve the kill switch and rollback path, inspect bridge logs for unmatched hook groups, and assume no control path works until physically demonstrated.

## Device validation

Capture timestamps and test one boundary at a time:

1. Record stock `com.tw.music` version code/version name and confirm the bridge log reports the exact signer, UID and version.
2. Check the hook capability-probe log. Any missing activity, service, receiver or presenter hook must remain stock-controlled rather than being guessed.
3. Tap the fixed DoFun music widget. Expected: Auxio-TS `MusicActivity` opens; stock activity closes.
4. Test previous, next and play/pause from the fixed widget and steering-wheel/media keys.
5. Start playback, pause and resume. Expected: DoFun title/artist/album and play state follow Auxio.
6. Seek through the widget where supported. Confirm the unit conversion matches the configured Auxio Topway seek policy.
7. Test a command before the Auxio MediaBrowser is ready. Expected: the bridge logs that the session is not ready and retains the stock path; it must not suppress the stock action merely because a broadcast was sent.
8. Test launcher restart, stock-process restart and Auxio-process restart.
9. Test cold boot, ACC sleep/wake and a second launch after resume.
10. Disconnect/reconnect USB storage and confirm the bridge does not make unavailable queue items playable or alter source authority.
11. Create the kill-switch marker while running. Expected: new commands stop redirecting and stock behaviour remains available.
12. Remove the marker and restart the stock process. Expected: bridging resumes.

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

