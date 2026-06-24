# Codex prompt — Auxio-TS TS18 overlay, fast resume, diagnostics, VLC/DoFun, permissions, and Magisk-lane hardening

Repository: `cbkii/Auxio-TS`
Base branch: `dev`
Suggested branch: `cx/fix-ts18-overlay-fast-resume-diagnostics-and-vlc-dofun`

## Mandatory first step

Extract this evidence pack at the repository root if it is not already present. Then read these files before changing code:

- `docs/evidence/ts18_auxio_v5_0_6/README.md`
- `docs/evidence/ts18_auxio_v5_0_6/TS18_AuxioMediaDiag_AuxioTS_v5.0.6_report.md`
- `docs/evidence/ts18_auxio_v5_0_6/auxioPerms.md`
- `evidence/ts18_auxio_v5_0_6/raw/TS18_AuxioMediaDiag/ts18-auxio-media-20260624-213139/logs/logcat_filtered.txt`
- `evidence/ts18_auxio_v5_0_6/raw/TS18_AuxioMediaDiag/ts18-auxio-media-20260624-213139/system/mount.txt`
- `evidence/ts18_auxio_v5_0_6/raw/TS18_AuxioMediaDiag/ts18-auxio-media-20260624-213139/system/getprop-all.txt`
- `evidence/ts18_auxio_v5_0_6/raw/TS18_AuxioMediaDiag/ts18-auxio-media-20260624-213139/system/ps-A.txt`
- `evidence/ts18_auxio_v5_0_6/raw/TS18_AuxioMediaDiag/ts18-auxio-media-20260624-213139/run.log`
- `evidence/ts18_auxio_v5_0_6/raw/TS18_AuxioMediaDiag/last-run-refused.log`

The `derived/*_clean.txt` files are NUL-stripped convenience copies only. Preserve raw evidence and never treat cleaned copies as more authoritative than raw.

## Primary goals

Implement a focused TS18 release-hardening pass for Auxio-TS:

1. Stop the observed `LocationObserver` / invalid-provider crash.
2. Make Floating Controls reliably appear and reappear whenever enabled.
3. Make playback resume from Floating Controls / DoFun launcher / widget as immediate as possible and not blocked by full library indexing when a valid cached now-playing item exists.
4. Analyse VLC Android’s working DoFun integration and implement safe public Android compatibility gaps in Auxio-TS.
5. Make “Skip non-music file types” default to enabled and apply consistently across MediaStore, SAF, and DirectFS.
6. Fix diagnostics capture reliability so the next TS18 run proves or disproves the right things.
7. Add justified normal APK permissions only.
8. Investigate, design, and possibly prototype a separate Magisk/privileged-module lane, without making it the default or claiming root equals platform signing.

## TS18 exact-device context from evidence

Treat these as **Observed** from the supplied evidence:

- Android 10 / SDK 29.
- Device/model: `s9863a1h10_Natv`; board/platform `uis8581a2h10`; platform class UIS8581A / SP9863A.
- Build/FOTA: `TS18.2.2_20241210.165912`, `WINDOW-THEME1_1000`, build date `Tue Dec 10 16:59:12 CST 2024`.
- Root: `uid=0(root)` with context `u:r:magisk:s0`; SELinux `Permissive`.
- AVB/orange/unlocked and dynamic partitions are present.
- `/` is about 95% used; `/vendor` about 81% used; `/data` has space.
- Auxio-TS `com.tw.music` ran as normal app UID `u0_a175` / UID `10175`, not UID 1000/system.
- Magisk mounts were observed for `/system/bin`, `/system/etc/permissions`, and `/system/priv-app`; an existing systemless DocumentsUI/privapp-permission pattern was present.
- `com.dofun.variety`, `com.tw.music`, `com.tw.media`, `org.oxycblt.auxio`, `org.oxycblt.auxio.debug`, VLC, Spotify, BTAndroidTS, ts18-intent-bridge, NavRadio+, DocumentsUI packages, and Topway system packages were installed/listed.
- No `/storage/usbdiskN` or `/mnt/media_rw/usbdiskN` mount was visible during the captured mount snapshot.
- `ACTION_OPEN_DOCUMENT_TREE` launched `com.android.documentsui/.picker.PickActivity`; both `com.android.documentsui` and `com.google.android.documentsui` were listed.

## Hard safety rules

- Do not claim physical TS18 validation unless this branch/build is actually tested on the device.
- Do not equate root/Magisk with platform signing, UID 1000, signature permissions, vendor identity, SELinux domain, or Topway private authority.
- Do not add private Topway/Cardoor/vendor APIs.
- Do not use LSPosed/framework hooks in this app pass.
- Do not delete, disable, hide, or replace stock packages as normal app behaviour.
- Do not write to `/system`, `/vendor`, `/product`, `/data`, or package state except via an explicitly separate Magisk module design/prototype with rollback.
- Do not add privileged/system permissions to normal APKs.
- Preserve standard Auxio behaviour and isolate Topway-specific behaviour behind topwayCompat or `BuildConfig.TOPWAY_COMPAT_FLAVOR` gates.
- Keep app diagnostics user-started, visible, bounded, local-only, and auto-stopped.
- Avoid generic phone tweaks, performance folklore, broad background polling, or unrelated refactors.
- Keep changes scoped to demonstrated TS18/Auxio-TS issues.

## Priority order

P0. Fix observed crash: invalid provider / invalid URI in `LocationObserver` or related storage/artwork code.
P1. Fix diagnostics harness completeness and reliability.
P1. Fix Floating Controls reliability.
P1. Implement immediate cached now-playing fast resume.
P1. Make source/picker/USB/direct-path handling robust.
P2. Improve notification/indexer/artwork hardening.
P2. Apply justified permissions.
P2. Compare VLC/Spotify/DoFun integration and implement safe public Android parity gaps.
P2. Design/prototype Magisk module lane only after normal app fixes are done.

---

# Task 1 — P0 crash stopper: invalid URI/provider observer registration

## Observed crash

The attached log includes:

```text
FATAL EXCEPTION: DefaultDispatcher-worker-13
Process: com.tw.music, PID: 17638
java.lang.SecurityException: Failed to find provider  for user 0; expected to find a valid ContentProvider for this authority
  at android.content.ContentResolver.registerContentObserver(...)
  at org.oxycblt.musikr.fs.track.LocationObserver.<init>(...:40)
```

The blank provider strongly suggests a `Uri` with blank/missing authority reached `ContentResolver.registerContentObserver()`.

## Required implementation

Audit and fix:

- `org.oxycblt.musikr.fs.track.LocationObserver`
- SAF/file/direct source URI conversion
- track/artwork fetchers and Coil model/fetcher paths
- source migration/normalisation paths
- any code that registers content observers for source/track/artwork URIs.

Rules:

1. Only register content observers for `content://` URIs whose authority is non-blank and whose provider is resolvable for the current user/context.
2. Do not register observers for `file://`, raw filesystem paths, empty strings, malformed URIs, DirectFS roots, or unsupported schemes.
3. Catch and downgrade `SecurityException`, `IllegalArgumentException`, and relevant `RuntimeException`s around observer registration.
4. Failure to observe must not prevent UI, overlay, MediaSession, cached now-playing, or scanner fallback from continuing.
5. Record a bounded diagnostic entry: source mode, redacted URI/path type, scheme, authority-present boolean, failure class, and recovery action. Do not log full private file paths unless existing diagnostics explicitly permit it.
6. Add persisted-source migration/repair: stale/invalid persisted locations must be rejected or marked unavailable before they instantiate observers.
7. Keep valid artwork and valid provider observation working.

## Required tests

Add regression tests for:

- `content://` URI with blank authority;
- empty string URI;
- cancelled picker/null result path;
- `file://` URI;
- raw `/storage/usbdiskN/...` path;
- missing provider/unknown authority;
- valid media provider or mock provider URI;
- stale persisted SAF tree URI.

## Acceptance

- Bad/stale source URI cannot crash `com.tw.music`.
- App remains operational with a repairable source error.
- Tests fail on the old behaviour and pass on the fix.

---

# Task 2 — Diagnostics harness reliability and evidence capture

## Observed harness defects

The supplied diagnostics archive was partial:

- It duplicated itself under nested `TS18_AuxioMediaDiag/TS18_AuxioMediaDiag/...`.
- A stale lock blocked a later run: `/data/local/tmp/ts18_auxio_media_diag.lock`.
- Expected files/directories were missing or empty: `REPORT.md`, `package_table.tsv`, `media_session_all.txt`, `notification_all.txt`, `audio_all.txt`, `snapshots/*`, `packages/*`, `storage`, `magisk`, `overlay`, `source_paths`, etc.
- `logcat_filtered.txt`, `mount.txt`, and `getprop-all.txt` were NUL-padded/truncated.
- `system/top.txt` contained only a header.
- The logcat header claimed start at `21:31:40`, but log entries were old history ending around `21:26`.

## Required implementation

Fix or create TS18 diagnostics scripts in the repo so a future run reliably captures:

- `REPORT.md` always, even on partial failure;
- `package_table.tsv`;
- `media_session_all.txt`;
- `notification_all.txt`;
- `audio_all.txt`;
- `appops_all.txt`;
- `dumpsys package` for: `com.tw.music`, `com.tw.media`, `org.oxycblt.auxio`, `org.oxycblt.auxio.debug`, `com.dofun.variety`, `org.videolan.vlc`, `com.spotify.music`, `com.android.documentsui`, `com.google.android.documentsui`, `com.android.providers.media`, `com.android.providers.downloads`;
- quick component summaries for launchers, activities, services, receivers, providers, permissions, app IDs/UIDs, install source paths, system/priv-app flags;
- per-snapshot dumps: `dumpsys media_session`, `dumpsys notification`, `dumpsys audio`, `dumpsys appwidget`, `dumpsys window`, `dumpsys activity broadcasts`, `cmd appops get`, package resolve checks;
- storage inventory: `mount`, `df -h`, `/storage`, `/mnt/media_rw`, `/mnt/runtime/default`, `/mnt/runtime/read`, `/mnt/runtime/write`, `/mnt/runtime/full` where available;
- root/Magisk inventory: `id`, `getenforce`, `magisk -V`, `magisk -v`, `/data/adb/modules`, active mounts under `/system/priv-app` and `/system/etc/permissions`.

Script rules:

- Do not modify settings, packages, appops, permissions, or files unless using a separately named `apply` script.
- Every command must record command, exit code, stdout, stderr, timeout status, timestamp, and authority (`shell`, `su`, app, adb, etc.).
- Use safe timeouts for every command.
- Never preallocate fixed-size text files or leave NUL padding.
- Use one top-level archive directory only.
- Lock file must include PID, boot ID, start time, and session path; on startup, detect stale lock safely and record the stale-lock decision.
- Preserve Chinese filenames/paths.
- Empty output directories are bugs unless `REPORT.md` explicitly explains why no data was available.
- Separate pre-session `logcat -d` from live log streaming; do not label old history as live capture.

## Acceptance

- Add shell syntax tests/smoke tests for the diagnostic scripts.
- A local dry-run should produce a non-empty `REPORT.md` and command-status table even without TS18 hardware.
- The next TS18 run should be sufficient to compare Auxio/VLC/Spotify/DoFun surfaces.

---

# Task 3 — Floating Controls reliability

## Observed / current likely weak points

- Overlay foreground notification id `42` on `auxio_car_overlay_channel` appeared once in the log, proving the overlay path started at least once.
- The archive does not prove overlay visibility/touch/z-order or post-boot/post-wake reliability.
- Current `CarOverlayBootReceiver` should be inspected: if it only handles `ACTION_BOOT_COMPLETED`, that is likely insufficient for TS18 boot/wake/relaunch.
- Current `CarFloatingControlsService` restart semantics should be inspected: `START_NOT_STICKY` or idle self-stop paths can leave enabled overlay dead after process death or lifecycle transitions.

## Required behaviour

If Floating Controls are enabled and overlay permission is granted, the overlay must be visible or actively being restored as soon as possible after:

- boot completed;
- locked boot completed/user unlock where safe;
- app process start;
- MainActivity launch/resume;
- AuxioService start/restart;
- PlaybackServiceFragment attach;
- launcher/widget/Topway command path;
- package replaced/update;
- likely TS18 ACC sleep/wake or screen/user-present broadcasts where observable and safe on Android 10;
- service process death/null-intent restart.

## Implementation guidance

- Create a small testable `CarOverlayRestarter` / `CarOverlayVisibilityPolicy` / `CarOverlaySupervisor` abstraction if useful.
- Keep overlay start/restart code in topwayCompat or behind `BuildConfig.TOPWAY_COMPAT_FLAVOR`.
- Consider sticky restart semantics only when enabled+permission-granted; do not loop if disabled or permission missing.
- Start foreground promptly and safely on API 29.
- Do not let `hideWhileAuxioForeground` permanently kill an enabled overlay.
- Add bounded retry only for transient WindowManager/boot readiness; no indefinite polling.
- Add diagnostics: restore requested/skipped/started, source trigger, addView success/failure, service restart/null intent, permission missing, saved position, display bounds, foreground notification state.
- Ensure explicit user stop/triple tap still works.

## Acceptance

- Unit tests for overlay policy decisions.
- Static/manifest checks for receivers/actions in Topway variants only.
- Standard variant does not start overlay service.
- Manual TS18 checklist added for boot, wake, launcher restart, process death, permission loss.

---

# Task 4 — Immediate cached now-playing fast resume

## Problem

Playback resume from Floating Controls / DoFun launcher / Topway widget appears coupled to library indexing. On TS18, controls and resume must be usable before the full library scan finishes.

## Required behaviour

On cold start/process restart, play/pause/next/prev commands from Floating Controls, DoFun widget/window, media button, or Topway bridge should:

1. Attach playback service, MediaSession, notification, widget, and overlay quickly.
2. Publish cached metadata/playback state if a saved now-playing item exists.
3. Prepare and resume the cached track directly if its URI/path is valid and accessible.
4. Continue full library scan in the background.
5. Reconcile transient cached now-playing state with the real library once loaded.
6. If the saved track is inaccessible, publish a paused/error state and fall back to existing deferred restore/shuffle semantics without blocking controls.

## Implementation guidance

- Audit `SavedState`, queue persistence, `DeferredPlayback`, `PlaybackStateManager`, `ExoPlaybackStateHolder`, `StartupLibraryPolicy`, `MusicRepository`, `WidgetComponent`, `TopwayStartIntentHandler`, and MediaSession code.
- Introduce a narrow versioned `FastResumeSnapshot` if existing saved state lacks direct URI/path and metadata.
- Snapshot fields should include: URI/path, source mode, title/artist/album/duration, queue/index/progression, last position, timestamp, library revision if available, and enough error metadata to explain fallback.
- Keep I/O off main thread and bounded.
- Do not fake full library readiness.
- USB absent must not commit false empty library.
- Diagnostics should report snapshot present/absent, access check, fast-resume attempt/result, fallback reason, time to first MediaSession metadata, and time to playable state.

## Required tests

- valid direct file path;
- valid content URI;
- missing USB path;
- revoked SAF permission;
- no saved state;
- source disappears during prepare;
- library loads later and reconciles transient item;
- Topway play/pause with no current song uses fast resume before full scan;
- Floating Controls play/pause uses same path.

---

# Task 5 — Source/picker/USB robustness

## Observed

- `ACTION_OPEN_DOCUMENT_TREE` launched `com.android.documentsui/.picker.PickActivity`.
- `com.google.android.documentsui` was also installed/listed.
- Full roots, URI grants, chosen URI, and source scan result were not captured.
- No `/storage/usbdiskN` or `/mnt/media_rw/usbdiskN` mount was visible at snapshot time.

## Required behaviour

Treat storage access as separate states:

- picker launch;
- provider root visibility;
- returned URI validity;
- persistable grant success;
- source scan success;
- MediaStore visibility;
- DirectFS path readability;
- USB mount lifecycle.

Concrete requirements:

1. Cancelled/null `OPEN_DOCUMENT_TREE` must not mutate existing source state.
2. Returned URI with blank authority or failed grant must be rejected before scanner/observer construction.
3. Direct path mode must support `/storage/emulated/0/...`, `/storage/usbdisk0`, `/storage/usbdisk1`, and dynamic `/storage/usbdiskN` safely.
4. `/mnt/media_rw/...` remains root diagnostic/recovery path, not ordinary app source path.
5. User-visible source states: `Unavailable`, `Permission lost`, `Provider missing`, `Empty folder`, `Unmounted USB`, `Indexing`, `Ready`.
6. USB removal/replug must not crash or wipe queue; mark source unavailable and retry on mount.
7. Diagnostics should explicitly report “no USB visible during capture” rather than implying USB support failure.

---

# Task 6 — Notification, artwork, and indexer hardening

## Observed

- Indexer notification id `41121` on `com.tw.music.channel.INDEXER` was enqueued repeatedly about every 1–2 seconds during indexing.
- Overlay foreground notification id `42` was posted.
- The log did not prove a notification crash, but dumps were missing/truncated.

## Required behaviour

- Throttle/coalesce indexer notification updates. Do not update every 1–2 seconds unless visible progress materially changed.
- Keep API 29 notifications simple and valid.
- Every foreground service must start foreground promptly and use existing channels.
- Large bitmap/artwork must be bounded before notification or MediaSession metadata use.
- Metadata should publish without blocking on remote/large artwork.
- Provide safe small-icon-only fallback notifications.
- Add tests for update throttling and bitmap bounds.

---

# Task 7 — VLC / Spotify / DoFun comparison and public Android parity

## User observation

- VLC still integrates partially with DoFun: title appears and play/next/prev work.
- Spotify no longer integrates.

## Required investigation

Research current VLC Android source/release behaviour and compare with Auxio-TS:

- active `MediaSessionCompat` / platform MediaSession;
- playback state actions: play, pause, play/pause, skip next, skip previous, seek;
- metadata fields: title, artist, album, duration, artwork;
- MediaStyle notification;
- `MediaBrowserService` / `MediaBrowserServiceCompat`;
- media button receiver;
- package/category/launcher metadata;
- any public broadcasts VLC exposes.

Use repo diagnostics to capture and compare on TS18:

- `dumpsys media_session` while VLC plays;
- `dumpsys notification`;
- `dumpsys package org.videolan.vlc`;
- DoFun logs while pressing play/next/prev;
- equivalent Auxio-TS output.

Implementation rules:

- Implement only safe public Android parity gaps.
- Do not spoof VLC.
- Do not add private DoFun/Topway APIs.
- Do not assume Spotify failure means DoFun requires private/proprietary integration.

Deliverable:

- `docs/TS18_VLC_DOFUN_COMPARISON.md` with Observed/Inferred/Requires device validation labels.
- Diagnostics scripts sufficient for next real-device comparison.
- Code fixes for proven public Android compatibility gaps only.

---

# Task 8 — “Skip non-music file types” default for all source modes

## Current suspected state

MediaStore appears to default `excludeNonMusic = true`, but this must be confirmed and applied consistently to SAF and DirectFS.

## Required behaviour

- Default enabled for MediaStore, SAF, and DirectFS.
- Existing users who explicitly disabled it keep that preference.
- UI text must state it applies to all source modes if true.
- Avoid rejecting valid music files just because MIME is missing.

Filtering approach:

- quick extension/MIME allowlist before expensive metadata extraction;
- MediaStore `IS_MUSIC` and not ringtone/notification/alarm/podcast where appropriate;
- for SAF/DirectFS, skip obvious non-audio extensions before metadata parse: images, videos, documents, archives, subtitles, and playlist files unless a playlist importer explicitly handles them;
- preserve support for common music extensions: mp3, flac, m4a, aac, ogg, opus, wav, wma where current decoder stack supports them;
- keep `.nomedia`, hidden-file, Android-folder, symlink, and TS18 noisy-directory safety policies intact.

Tests:

- default true for all source modes;
- explicit false remains respected;
- obvious non-music skipped in SAF/DirectFS;
- common music extensions accepted;
- missing MIME fallback behaves safely.

---

# Task 9 — Permissions

Read `docs/evidence/ts18_auxio_v5_0_6/auxioPerms.md`.

For normal APKs:

1. Add if missing:

```xml
<uses-permission
    android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="29" />
```

Reason: TS18 is Android 10/API 29; app uses `requestLegacyExternalStorage=true`; DirectFS/manual path and legacy media handling may need it.

2. Consider only if a matching user flow is implemented:

```xml
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
```

If added, implement a settings/repair action that opens the request flow. Do not claim it grants exemption automatically.

3. Add Bluetooth permissions only if code actually uses Bluetooth APIs for headset/device connection handling.

Do not add to normal APK:

- `BLUETOOTH_PRIVILEGED`
- `WRITE_SECURE_SETTINGS`
- `READ_LOGS`
- `MANAGE_EXTERNAL_STORAGE`
- `QUERY_ALL_PACKAGES`
- platform/signature permissions

Add manifest/static tests and docs for every permission change.

---

# Task 10 — Optional Magisk/privileged module lane

Investigate and document whether a separate Magisk module release asset is worth creating.

Potential module variants:

1. normal APK only;
2. normal APK plus root helper scripts;
3. privileged Magisk module installing app under `/system/priv-app` systemlessly;
4. exact `com.tw.music` module;
5. alternate `com.tw.media` module.

Possible benefits to evaluate:

- systemless priv-app placement;
- matching `privapp-permissions-auxio-ts.xml` under `/system/etc/permissions`;
- late-start `service.sh` helper for safe setup:
  - grant normal/dangerous permissions where appropriate;
  - request/guide appops only where user approved this lane;
  - restore Floating Controls after boot/user unlock;
  - bounded diagnostics export;
  - optional package compile/optimise;
- uninstall/rollback script.

Critical constraints:

- Root/Magisk does not give platform signing.
- Root/Magisk does not give UID 1000.
- Priv-app does not create stock Topway private authority.
- Android 9+ requires explicit privileged permission allowlisting for privileged permissions; missing allowlists can break boot.
- Privileged XML must match the partition/path used by the mounted app.
- Do not request privileged permissions unless code truly needs them and the module safely grants them.
- Do not `.replace` broad system directories.
- Do not hide/remove stock packages by default.
- Prefer `service.sh` late-start over boot-blocking `post-fs-data.sh`.

Deliverables:

- `docs/TS18_MAGISK_MODULE_DESIGN.md`
- Decision matrix and recommendation: implement now / prototype only / defer.
- If implementing: module layout, `module.prop`, `service.sh`, `uninstall.sh`, README/recovery docs, static layout tests, release workflow asset, disabled-by-default risky helpers.
- If deferring: exact external validation required.

---

# Task 11 — Tests and verification

Run, at minimum:

```sh
./gradlew --no-daemon --stacktrace spotlessCheck
./gradlew --no-daemon --stacktrace :app:testStandardDebugUnitTest :musikr:testDebugUnitTest
./gradlew --no-daemon --stacktrace :app:lintStandardDebug
./gradlew --no-daemon --stacktrace :app:assembleStandardDebug :app:assembleTopwayTwMusicDebug :app:assembleTopwayTwMediaDebug
bash ./scripts/check-ts18-apk-reference-contracts.sh
bash ./scripts/check-dofun-topway-compat.sh
bash ./scripts/check-headunit-compat-safety.sh
```

Add and run focused tests for:

- invalid URI/provider observer crash;
- storage source migration/repair;
- diagnostics harness smoke/dry-run;
- overlay restart policy;
- fast resume snapshot/policy;
- Topway/Floating Controls play/pause fast-resume path;
- indexer notification throttling;
- SAF/DirectFS skip non-music filter;
- VLC/MediaSession public-surface parity helpers;
- manifest permissions;
- Magisk module layout if implemented.

If local Android SDK is unavailable:

- attempt repo-supported SDK bootstrap if present;
- otherwise push a branch and inspect GitHub Actions;
- read full logs/annotations, not summaries;
- fix CI until green or explicitly blocked.

---

# Final response required

Provide:

1. Starting `dev` SHA.
2. Final branch and commit SHA.
3. Files changed.
4. Summary of diagnostics evidence studied.
5. Every observed runtime crash/error and the fix.
6. Evidence matrix using: Observed, Inferred, Requires TS18 device validation, Unsupported, Blocked.
7. Floating Controls changes and remaining TS18 validation.
8. Fast-resume changes and remaining TS18 validation.
9. VLC/Spotify/DoFun analysis summary.
10. Skip-non-music default/filter changes.
11. Permissions added, and permissions deliberately not added.
12. Magisk module decision and rationale.
13. Verification commands and exact results.
14. Manual TS18 validation checklist.
15. Any blocked items and exact external evidence needed.

Proceed autonomously. Do not stop for routine clarification. Make the safest engineering decision that preserves existing features and moves Auxio-TS toward merge/release readiness.
