# TS18 Auxio-TS v5.0.6 diagnostics report

Source archive: `TS18_AuxioMediaDiag.zip`  
Analysed session path: `TS18_AuxioMediaDiag/ts18-auxio-media-20260624-213139`

## Executive summary

**Observed:** The archive contains a partial diagnostics run from a Magisk-rooted TS18 / Topway / DoFun unit running Android 10 / SDK 29. The device identity matches `s9863a1h10_Natv`, board/platform `uis8581a2h10`, build `TS18.2.2_20241210.165912`, FOTA `WINDOW-THEME1_1000`, with root context `u:r:magisk:s0` and SELinux permissive.

**Observed:** The strongest app issue in the captured logs is a real crash of `com.tw.music`:

```text
06-24 21:12:08.441 AndroidRuntime: FATAL EXCEPTION: DefaultDispatcher-worker-13
Process: com.tw.music, PID: 17638
java.lang.SecurityException: Failed to find provider  for user 0; expected to find a valid ContentProvider for this authority
  at android.content.ContentResolver.registerContentObserver(...)
  at org.oxycblt.musikr.fs.track.LocationObserver.<init>(...:40)
```

**Important timing note:** the diagnostic session summary says the session started at `21:31:40`, while `logcat_filtered.txt` includes older same-boot log history ending around `21:26`. The fatal crash was therefore pre-session history, not proven to have happened during the later 2400-second window. It is still highly relevant because it is the same boot and the same `com.tw.music` Auxio-TS variant.

**Observed:** The diagnostics package is incomplete. The archive expected to include `REPORT.md`, `package_table.tsv`, `media_session_all.txt`, `notification_all.txt`, `audio_all.txt`, `snapshots/*`, `packages/*`, storage inventories, Magisk module inventory, overlay dumps, and source path dumps, but those directories are empty. `logcat_filtered.txt`, `mount.txt`, and `getprop-all.txt` are NUL-padded/truncated at 32 KiB of real content. A later run was refused because `/data/local/tmp/ts18_auxio_media_diag.lock` still existed. This limits what can be proven about DoFun widgets, MediaSession state, audio focus, overlay z-order, boot readiness, and storage source state.

## Device and runtime baseline

### Observed

From `00_SUMMARY.txt`, `system/getprop-all.txt`, `system/id.txt`, `system/getenforce.txt`, `system/df-h.txt`, `system/mount.txt`, and `system/ps-A.txt`:

- Device: `s9863a1h10_Natv`; product/device `s9863a1h10`; board/hardware `uis8581a2h10`.
- Android: 10 / SDK 29.
- Build: `QP1A.190711.020 release-keys`; build date `Tue Dec 10 16:59:12 CST 2024`; FOTA `TS18.2.2_20241210.165912`, `WINDOW-THEME1_1000`, platform `UIS8581A`.
- Root: `uid=0(root)`, context `u:r:magisk:s0`; SELinux `Permissive`.
- Boot state: `ro.boot.vbmeta.device_state=unlocked`, `ro.boot.verifiedbootstate=orange`, `ro.boot.veritymode=enforcing`, dynamic partitions enabled.
- Storage pressure: `/` is 95% used with only 137 MiB available; `/vendor` is 81% used; `/data` has ~22 GiB available.
- Runtime app identity: `com.tw.music` is running as app UID `u0_a175` / UID `10175`, not UID 1000/system. Notification logs also show UID `10175` for `com.tw.music`.
- `com.dofun.variety`, `com.tw.media`, stock Topway packages, DocumentsUI, Magisk, BTAndroidTS, ts18-intent-bridge, NavRadio+, VLC and Spotify are present in the package list, but package dumps were not captured.

### Inferred impact

- `com.tw.music` package naming is not enough to obtain stock Topway/platform authority. This run does **not** show platform signing, UID 1000, signature permissions, or private Topway Binder authority for the Auxio-TS variant.
- Root/Magisk may help with diagnostics, grants, install placement or helper scripts, but it does not make the app platform-signed and does not make private Topway contracts safe to assume.
- Direct writes to `/system`, `/vendor`, and system app locations are risky because `/` is nearly full and system/vendor are read-only/dynamic-partition backed. Prefer app fixes and systemless Magisk overlays only where required.

## P0 issue: fatal crash in storage/location observer

### Evidence

`logs/logcat_filtered.txt` lines 46-66 contain a fatal exception from `com.tw.music`, PID `17638`:

```text
java.lang.SecurityException: Failed to find provider  for user 0; expected to find a valid ContentProvider for this authority
  at android.content.ContentResolver.registerContentObserver(ContentResolver.java:2263)
  at org.oxycblt.musikr.fs.track.LocationObserver.<init>(...:40)
```

The blank area after `provider` strongly suggests that the app attempted to register a `ContentObserver` against a `Uri` with a missing/blank authority.

### Inferred root cause

A storage/source/location path is being converted into a URI that is not guaranteed to have a valid Android `ContentProvider` authority. Likely cases include:

- a cancelled or malformed SAF `OPEN_DOCUMENT_TREE` result;
- a stale persisted tree URI from an earlier broken DocumentsUI state;
- a DirectFS/manual path represented as an Android URI without a content authority;
- an empty/null source URI that reaches `LocationObserver`;
- a migration path from older Auxio source settings that leaves an invalid location record.

### Required fix

In `LocationObserver` and any storage-source setup code:

1. Validate URI scheme and authority before calling `ContentResolver.registerContentObserver`.
2. Only register content observers for `content://` URIs whose `authority` is non-blank and resolvable.
3. Do not register a ContentObserver for `file://`, raw filesystem paths, DirectFS roots, empty URIs, or unresolved pseudo-locations.
4. Catch and downgrade `SecurityException`, `IllegalArgumentException`, and provider-not-found failures to a recoverable source error, not a process crash.
5. Log a bounded diagnostic record containing source mode, redacted path/URI type, scheme, authority presence, and recovery action.
6. Add a migration/repair step: on startup, detect invalid persisted locations and mark them unavailable with user-facing repair options instead of constructing observers.
7. Add regression tests for:
   - blank authority URI;
   - empty string URI;
   - cancelled picker result;
   - `file://` URI;
   - `/storage/usbdiskN` direct path;
   - missing DocumentsProvider;
   - stale persisted SAF tree URI.

### Acceptance criteria

- A bad or stale source must not crash `com.tw.music`.
- The app should continue to load UI, media session, overlay, and cached now-playing state even when one source is invalid.
- The user should see a repairable source error, not a fatal crash or silent library reset.

## P1 issue: source/picker handling is not robust enough for TS18

### Evidence

The log shows repeated `ACTION_OPEN_DOCUMENT_TREE` flows from `com.tw.music`:

- `21:22:09` resolver activity for `OPEN_DOCUMENT_TREE`.
- `21:22:11` `com.android.documentsui/.picker.PickActivity` starts successfully.
- `21:22:32` activity result returns to `com.tw.music`.
- `21:24:31` and `21:24:41` additional `OPEN_DOCUMENT_TREE` attempts return quickly to `com.tw.music` without a captured DocumentsUI selection flow.

`system/mount.txt` also shows a DocumentsUI Magisk/systemless-style placement:

```text
/dev/block/mmcblk0p44 on /system/priv-app/DocumentsUI/DocumentsUI.apk
/dev/block/mmcblk0p44 on /system/etc/permissions/privapp-permissions-ts18-documentsui.xml
```

### Observed limitation

The picker now resolves and launches, but the diagnostics do not include DocumentsUI roots, persisted URI grants, source mode, chosen URI, or final library source state. It cannot prove that full internal storage roots are exposed, only that `OPEN_DOCUMENT_TREE` no longer fails immediately.

### Required app behaviour

Auxio-TS should treat storage access as multiple separate layers:

- picker launch;
- provider root visibility;
- returned URI validity;
- persistable grant success;
- source scan success;
- MediaStore visibility;
- direct path fallback;
- USB mount lifecycle.

Concrete fixes:

1. When `OPEN_DOCUMENT_TREE` returns cancelled/null, do not mutate existing source state.
2. When a returned URI lacks authority or persistable permission fails, reject it before it reaches the scanner.
3. Add a TS18-compatible manual/direct path source mode for `/storage/emulated/0/...`, `/storage/usbdisk0`, and `/storage/usbdisk1` without requiring SAF to be fully functional.
4. Keep direct `/mnt/media_rw/...` paths as root diagnostic/recovery paths only, not ordinary app source paths.
5. Add user-visible source state: `Unavailable`, `Permission lost`, `Provider missing`, `Empty folder`, `Unmounted USB`, `Indexing`, `Ready`.
6. On USB removal/replug, do not crash or wipe queue; mark source temporarily unavailable and retry on mount change.

## P1 issue: playback restore and controls appear coupled to full indexing

### Evidence

After picker/source changes, `com.tw.music` posts repeated indexer progress notifications on channel `com.tw.music.channel.INDEXER` between `21:22:40` and `21:26:10`. Around `21:25:14` to `21:25:30` it enqueues the indexer notification roughly every 1.5 seconds. The app process PSS rises from about 56 MiB at `21:18:28` to about 119 MiB at `21:25:27` during indexing.

`com.tw.music.MusicActivity` also relaunches twice within seconds at `21:25:46` and `21:25:53`:

```text
am_on_stop_called: com.tw.music.MusicActivity, handleRelaunchActivity
am_on_destroy_called: com.tw.music.MusicActivity, performDestroy
am_on_create_called: com.tw.music.MusicActivity, performCreate
```

### Inferred impact

On TS18, startup, widget metadata, floating controls, and playback resume should not wait for a full library scan. Repeated foreground/indexer notification churn and activity relaunches increase the chance of slow startup, DoFun widget lag, overlay delay, or SystemUI weirdness.

### Required fix

1. Split cold-start into two paths:
   - **fast path:** restore cached now-playing item, queue pointer, playback state, MediaSession, notification, and overlay controls;
   - **slow path:** reconcile/index the library in the background.
2. Persist a small, versioned now-playing snapshot with enough metadata to resume and publish immediately.
3. If the track file exists and can be opened, allow playback resume before the full source scan completes.
4. If the source is unavailable, publish a paused/error state without blocking overlay or media session setup.
5. Throttle indexer notification updates. Do not enqueue progress notifications every 1-2 seconds unless the visible progress materially changed.
6. Avoid UI activity relaunch during indexing unless a genuine configuration change requires it.
7. Bound artwork loading and bitmap decoding; metadata should publish without waiting for remote/large art.

## P1 issue: floating overlay service needs stronger boot/wake/relaunch guarantees

### Evidence

At `21:26:00`, `com.tw.music` posts `auxio_car_overlay_channel` notification ID `42`. Android then posts the system alert-window notification for `com.tw.music`:

```text
notification_enqueue: com.tw.music,42, Notification(channel=auxio_car_overlay_channel ... flags=0x42 ...)
notification_enqueue: android, AlertWindowNotification - com.tw.music ...
```

This proves the overlay path was active at least once in the same boot. It does **not** prove overlay visibility, z-order, touchability, launch-after-boot, launch-after-ACC-wake, or top/right-edge positioning, because overlay/window dumps were not captured.

### Required fix

1. Keep the overlay as a small, independent foreground service that can start without the full library being ready.
2. Restore overlay service on:
   - app process start;
   - boot completed / locked boot completed where available;
   - user unlock;
   - package replaced;
   - launcher/activity restart;
   - TS18 ACC sleep/wake equivalent where detectable.
3. Add a short bounded retry loop when overlay permission or WindowManager is not ready yet.
4. Use runtime display bounds and insets; clamp saved coordinates away from the 55 px top status region and right navigation/gesture region unless the user explicitly places controls there.
5. Detect permission loss and show a clear repair notification/activity instead of silently failing.
6. Add a local overlay health snapshot in app diagnostics: permission, service running, foreground notification visible, last addView/updateView result, last WindowManager exception, display bounds, saved position.

## P2 issue: notification behaviour needs TS18 hardening

### Evidence

The captured non-zero log does not show a `Bad notification`, `RemoteServiceException`, or SystemUI RemoteViews crash. However, the log is truncated and the expected notification dumps were not captured, so this cannot be treated as proven safe.

The app posts at least two kinds of notifications:

- indexer progress notification on `com.tw.music.channel.INDEXER`, ID `41121`;
- overlay foreground notification on `auxio_car_overlay_channel`, ID `42`.

### Required fix

1. Keep TS18 notification layouts minimal on API 29.
2. Do not require large artwork/bitmap availability for notification validity.
3. Bound bitmap size before placing in notification or MediaSession metadata.
4. Ensure every foreground service calls `startForeground()` promptly and uses an existing channel.
5. Rate-limit progress notification updates.
6. Add crash-safe fallback notification with small app icon only.

## P2 issue: Android 10 parser warnings from newer manifest elements

### Evidence

AppManager/package parsing logs show Android 10 warning about unknown manifest/service elements in an APK identified as `com.tw.music` with `targetSdkVersion:36`:

```text
Unknown element under <manifest>: queries ... line #29
Unknown element under <service>: property ... lines #170, #345, #357
setMaxAspectRatio:packageName: com.tw.music targetSdkVersion:36
```

### Interpretation

This is probably Android 10 ignoring newer manifest constructs while AppManager scans the APK from its cache. It is not by itself a crash. It does mean developers must not rely on these newer declarations having any effect on API 29.

### Required fix

- Keep Android 10 / API 29 as the runtime contract.
- API-gate all newer APIs and behaviours.
- Ensure service discovery, media browser/session, foreground service, and exported component behaviour works without API 30+ manifest features.
- Add install/runtime tests on API 29, not only compile/CI tests on newer SDKs.

## P2 issue: app identity and DoFun integration remain unproven

### Evidence

- `com.tw.music` runs as normal app UID `u0_a175` / `10175`, not UID 1000.
- `com.tw.media` is installed, but package/component dumps were not captured.
- No `media_session_all.txt`, `notification_all.txt`, appwidget dump, DoFun widget state, broadcast resolver dump, or launcher slot dump exists in this archive.

### Required fix / validation

Developers should keep these integration layers separate:

1. Package name: `com.tw.music`.
2. Activity/component name: `com.tw.music/.MusicActivity`.
3. Android MediaSession publication.
4. MediaStyle notification metadata/actions.
5. DoFun fixed slot behaviour.
6. Topway private broadcasts or APIs, if any.
7. Widget/appwidget compatibility.
8. Audio focus and hardware key routing.

Do not assume success in one layer proves success in another.

## Diagnostics harness defects that must be fixed before the next run

### Observed defects

1. Archive duplicates itself under `TS18_AuxioMediaDiag/TS18_AuxioMediaDiag/...`.
2. A stale lock blocked a later run: `Refused: existing lock /data/local/tmp/ts18_auxio_media_diag.lock`.
3. Expected directories are empty: `packages`, `snapshots`, `summary`, `storage`, `appdata`, `autostart`, `magisk`, `overlay`, `commands`, `source_paths`, `vendor`, `interruptions`.
4. Expected files named in `00_TEST_STEPS.md` are missing: `REPORT.md`, `package_table.tsv`, `media_session_all.txt`, `notification_all.txt`, `audio_all.txt`, `snapshots/*/summary.txt`, `packages/*/quick-components.txt`.
5. `logcat_filtered.txt` is 49,152 bytes but contains 16,384 NUL bytes and only 32,768 bytes of real log content.
6. `getprop-all.txt` and `mount.txt` are also NUL-padded/truncated.
7. `system/top.txt` contains only the command header, no useful `top` output.
8. The logcat header says the filtered logcat started at `21:31:40`, but captured log entries end around `21:26`, before the session start. This suggests the capture used stale log history or failed to stream live logs.

### Required harness fixes

- Remove stale locks on normal exit and use lock metadata to detect stale PID/boot ID safely.
- Always write a final `REPORT.md` even after partial failure.
- Do not preallocate fixed-size files or leave NUL padding; write text streams normally and rotate by size if needed.
- Capture live logcat after clearing or clearly mark `pre-session logcat -d` versus live logcat.
- Collect required dumps at each snapshot: media sessions, notifications, audio policy/focus, appops, overlays/windows, package dumps, storage roots, DocumentsUI roots/grants, Magisk module list.
- Record command exit codes and stderr for every command.
- Bound every command with timeouts, but keep partial output and record timeout status.
- Package only one top-level directory.
- Include `boot_id`, session start/end timestamps, and monotonic runtime.

## Prioritised developer task list

### P0 — crash stopper

Fix invalid URI/provider handling in `LocationObserver` and storage-source setup. A bad SAF/direct/manual source must never crash the process.

### P1 — startup and playback restore

Implement cached now-playing fast path and decouple playback/media session/overlay from full library indexing.

### P1 — TS18 source fallback

Make source selection robust across MediaStore, SAF, and direct `/storage/...` paths. Treat picker launch, URI grant, source availability, and actual scan as separate states.

### P1 — overlay reliability

Make floating controls start quickly and repeatedly after boot/wake/relaunch, independent of scanner completion. Add overlay diagnostics and coordinate clamping for TS18 insets.

### P2 — notification and artwork hardening

Rate-limit indexer notifications and keep API 29 notification/artwork code minimal and bounded.

### P2 — DoFun integration validation

Add diagnostics and tests proving which layer DoFun uses: generic MediaSession, MediaStyle notification, fixed package/activity, widget, broadcast, or private Topway path.

### P2 — diagnostics reliability

Fix the diagnostics collector before relying on it for release decisions.

## What this archive cannot prove

Because required dumps are missing, this run cannot prove:

- whether DoFun home/window widget reads Auxio metadata correctly;
- whether play/pause/next/previous from DoFun work;
- whether MediaSession actions and queue are correct;
- whether notification actions work;
- whether overlay is visible/touchable after boot or ACC wake;
- whether SAF exposes full internal storage beyond Downloads;
- whether `/storage/usbdisk0` or `/storage/usbdisk1` were mounted or scanned;
- whether VLC/Spotify/Radio/NavRadio audio focus interactions behaved correctly;
- whether BTAndroidTS or ts18-intent-bridge interfered with media focus/routing.

## Recommended next validation run

After fixing the crash and diagnostics harness, rerun with these minimum assertions:

1. Clear logcat or tag pre-existing logs separately.
2. Start `com.tw.music` from cold process.
3. Confirm cached now-playing metadata appears before full scan completes.
4. Confirm MediaSession exists and exposes play/pause/next/previous.
5. Confirm overlay permission, foreground service, and visible window state.
6. Test SAF cancel, SAF valid selection, DirectFS/manual path, and USB mount/unmount.
7. Capture DoFun home/widget state before, during, and after playback.
8. Capture VLC/Spotify takeover and return to Auxio.
9. Export package dumps, audio dumps, notification dumps, media session dumps, window/layer dumps, appops, storage roots, and persisted URI grants.

