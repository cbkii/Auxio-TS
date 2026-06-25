# TS18 Auxio Media Diagnostics Pack v3

This pack installs a Magisk `service.d` diagnostics collector for the TS18 / Topway / DoFun head unit while testing Auxio-TS and VLC media integration.

It is designed for the current TS18 project baseline:

- `s9863a1h10_Natv` / `uis8581a2h10` / `sp9863a`
- `TS18.2.2_20241210.165912_WINDOW-THEME1`
- Android 10 / SDK 29
- DoFun Variety / TWTHEME
- Magisk 28.1

It is read-only except for its own output directory and its stop/trigger files. It does not remount partitions, flash firmware, disable packages, clear app data, or change media files.

## What this captures

The collector is intended to answer these questions:

1. Does Auxio-TS publish a DoFun-visible media session like VLC?
2. Does Auxio-TS publish a MediaStyle notification with title, artist, progress, transport actions and artwork?
3. Does DoFun send standard media commands, Topway private broadcasts, or both?
4. Does Auxio-TS crash or fail due to RemoteViews/bitmap/artwork, notification, media-session, ExoPlayer, storage, DirectFS, root-gate, overlay, boot, or widget problems?
5. Are VLC still good baselines for the generic Android MediaSession path?
6. Are current TS18 app/module experiments, such as BTAndroidTS, ts18-intent-bridge, NavRadio+, DocumentsUI, Magisk/Zygisk/LSPosed modules, and Topway stock services interacting with media/audio?
7. Which SAF / file path / DirectFS / MediaStore source path is actually configured or reachable, and is the app treating inaccessible paths as empty libraries?
8. How early after boot are storage, package manager, DoFun, Auxio services, overlays, and media sessions ready?
9. Are floating controls obstructed by the 55 px top status bar, right navigation region, gesture/edge drawer, SystemUI, or launcher layers?
10. What caused unexpected pause/resume events: audio focus loss, media button, DoFun command, BT/headset, radio/NavRadio, reverse/camera, telecom, another player, or process death?

## Why these data points matter

DoFun successfully handling VLC implies that the generic Android media path is relevant. The important Android surfaces are:

- active media sessions via `MediaSessionManager`;
- `MediaBrowserService` / `MediaBrowserServiceCompat` / Media3 service compatibility;
- `PlaybackState`, action bitmasks and current position;
- media metadata keys such as title, artist, album, duration and artwork URI;
- `NotificationCompat.MediaStyle` with the media-session token;
- audio focus and audio policy state;
- package manifest component visibility.

The TS18-specific surfaces are:

- DoFun launcher/window/widget behaviour;
- stock `com.tw.music` and `com.tw.media` identities;
- Topway private actions such as `com.tw.music.action.*` and `com.android.launcher.widget_music_progress`;
- Topway audio/radio/Bluetooth services, especially `com.tw.service`, `com.tw.core`, `com.tw.coreservice`, `com.tw.bt`, `com.tw.eq`, `com.tw.radio`, `com.tw.carinfoservice`;
- storage roots such as `/storage/usbdisk0`, `/storage/usbdisk1` and `/mnt/media_rw/...`;
- Magisk modules and service.d scripts;
- vendor logging services such as `ylog`/`yloglite`.

## Files in this pack

```text
service.d/60-ts18-auxio-media-diag.sh   Main Magisk service.d collector
bin/install.sh                          Install to /data/adb/service.d
bin/run-now.sh                          Run the installed collector immediately
bin/stop.sh                             Stop a running collector by creating stop file
bin/uninstall.sh                        Remove installed script/config, preserve outputs
ts18-auxio-media-diag.conf              Config copied to /data/adb on install
docs/OUTPUT_MAP.md                      What each output file means
docs/EVIDENCE_GAPS.md                   Current evidence gaps this capture is designed to close
docs/RESEARCH_NOTES.md                  Research/source notes behind this diagnostic scope
docs/PR_RELEASE_AUDIT_SCOPE.md          PR/release-derived feature audit map
```

## Installation

Extract the ZIP somewhere readable on the TS18, for example:

```sh
cd /storage/emulated/0/Download
unzip ts18-auxio-media-diag-pack.zip -d ts18-auxio-media-diag-pack
su
sh /storage/emulated/0/Download/ts18-auxio-media-diag-pack/bin/install.sh
```

The installer copies:

```text
/data/adb/service.d/60-ts18-auxio-media-diag.sh
/data/adb/ts18-auxio-media-diag.conf
```

## Run immediately

```sh
su -c 'sh /data/adb/service.d/60-ts18-auxio-media-diag.sh now'
```

## Run at boot

By default, `RUN_ON_BOOT=1`, so Magisk runs it after boot with a delay.

To prevent every boot from starting a capture:

```sh
su -c "sed -i 's/^RUN_ON_BOOT=.*/RUN_ON_BOOT=0/' /data/adb/ts18-auxio-media-diag.conf"
```

To require a trigger file before boot capture:

```sh
su -c "sed -i 's/^REQUIRE_TRIGGER_ON_BOOT=.*/REQUIRE_TRIGGER_ON_BOOT=1/' /data/adb/ts18-auxio-media-diag.conf"
touch /storage/emulated/0/Download/TS18_AuxioMediaDiag.RUN
```

## Stop a capture early

```sh
touch /storage/emulated/0/Download/TS18_AuxioMediaDiag.STOP
```

or:

```sh
su -c 'sh /data/adb/service.d/60-ts18-auxio-media-diag.sh stop'
```

## Output location

```text
/storage/emulated/0/Download/TS18_AuxioMediaDiag/ts18-auxio-media-YYYYMMDD-HHMMSS/
/storage/emulated/0/Download/TS18_AuxioMediaDiag/ts18-auxio-media-YYYYMMDD-HHMMSS.tar.gz
```

Share the `.tar.gz` if it is not too large.

If the archive is too large, share these first:

```text
REPORT.md
00_SUMMARY.txt
00_TEST_STEPS.md
package_table.tsv
media_session_all.txt
notification_all.txt
audio_all.txt
window_focus_all.txt
logs/logcat_filtered_tail.txt
logs/logcat_filtered.txt
source_paths/
autostart/
overlay/
interruptions/
storage/
magisk/magisk-modules-state.txt
packages/*/quick-components.txt
snapshots/*/summary.txt
snapshots/*/overlay-window-context.txt
snapshots/*/playback-interruption-context.txt
```

## Manual test procedure during capture

The script writes the same checklist into `00_TEST_STEPS.md` in the output folder.

During the capture window:

1. Start Auxio-TS playback from the exact APK/variant you want tested.
2. On the DoFun home music window, check title, artist, artwork, progress, play/pause, next/previous and tap-to-open.
3. Test Auxio-TS source handling and note the exact mode/path used: MediaStore/System, SAF/DocumentsUI, DirectFS/manual path, `/storage/usbdisk0`, `/storage/usbdisk1`, `/storage/usbdiskN/Music`, `/storage/usbdiskN/Download`, USB unplug/replug if safe.
4. After a reboot capture, note whether Auxio autostarts, restores queue, restores overlay, and publishes a media session before/after DoFun becomes responsive.
5. Move/use floating controls near the top status bar and right navigation/edge drawer areas. Try status shade, DoFun gestures, and right-edge navigation controls. Note whether Auxio is below SystemUI, loses touch, is displaced, or cannot receive edge-area touches.
6. Test interruption contexts: pause from DoFun, Auxio UI, notification, headset/BT controller if available, VLC takeover, radio/NavRadio, reverse/camera if safe, phone/telecom if available, ACC sleep/wake if available.
7. Switch to VLC and play audio for 2-3 minutes.
8. Switch to  play audio for 2-3 minutes.
9. Return to Auxio-TS and repeat play/pause/next/previous.
10. If testing BTAndroidTS or ts18-intent-bridge, trigger their intended user-visible action once.
11. Stop early if needed.

The generated output now includes `00_FEATURE_AUDIT_SCOPE.md`, which maps Auxio-TS PR/release-note feature claims to the runtime evidence files in the capture.

## Config knobs

Edit `/data/adb/ts18-auxio-media-diag.conf`.

Common settings:

```sh
DURATION_SECONDS=2400       # 40 minutes
INTERVAL_SECONDS=15         # snapshot cadence
RUN_ON_BOOT=1               # run from service.d after boot
REQUIRE_TRIGGER_ON_BOOT=0   # set 1 to require trigger file
LOGCAT_ALL=0                # 1 captures full logcat, can be huge/private
COPY_AUXIO_PREFS=0          # 1 copies Auxio shared_preferences XML
CAPTURE_BUGREPORT=0         # 1 attempts bugreportz, huge/slow
CAPTURE_DMESG=1             # root kernel log capture
CAPTURE_CRASH_LOGS=1        # logcat crash/events and tombstone/anr listings
CAPTURE_DROPBOX=1           # dropbox crash/anr entries
CAPTURE_SAFE_ROOT_PROBES=1  # bounded read-only su probes
CAPTURE_DEEP_SOURCE_DIAGS=1 # SAF/DocumentsUI/MediaStore/DirectFS/manual path diagnostics
CAPTURE_AUTOSTART_DIAGS=1   # early service.d snapshot, boot receivers, jobs/alarms/readiness
CAPTURE_OVERLAY_EDGE_DIAGS=1 # floating controls vs status/nav/edge/window/layer diagnostics
CAPTURE_PLAYBACK_INTERRUPT_DIAGS=1 # audio focus, noisy, BT, telecom, radio/media takeover context
CAPTURE_MEDIASTORE_ROWS=1   # bounded MediaStore rows for source/path comparison
COPY_AUXIO_APP_REPORTS=1    # small Auxio crash/diagnostic reports
```

## PR/release audit scope

I audited the accessible Auxio-TS PR descriptions and project release/change notes into runtime evidence buckets rather than adding hundreds of brittle per-PR checks. The script therefore captures evidence for every new, unverified, or lower-confidence area repeatedly mentioned across Auxio-TS PRs: DoFun/Topway integration, generic MediaSession behaviour, notification artwork stability, DirectFS/root-gating, storage source handling, boot/autostart, diagnostics, overlay/floating controls, queue/shuffle/autoplay, BTAndroidTS, ts18-intent-bridge, NavRadio+, DocumentsUI, Magisk/Zygisk/LSPosed, and vendor audio/radio/Bluetooth services.

Important: the accessible `dev` branch currently reports `versionName 5.0.5`, so the pack records the installed package versions in `package_table.tsv` and treats the user-requested “since v5.3.0” scope as a feature-audit baseline to verify on-device.

## v3 deep-focus additions

This version expands the collector for the four high-risk Auxio-TS areas you asked about:

### SAF / file path / music source

Outputs are under `source_paths/` and `storage/`. They include a candidate path matrix for `/storage/emulated/0`, `/storage/usbdisk0`, `/storage/usbdisk1`, `/mnt/media_rw/...`, common `Music` and `Download` folders, SAF/DocumentsUI resolver queries, persisted URI grants, bounded MediaStore audio rows, and a bounded grep of Auxio app data for source/path strings. This is intended to reveal whether Auxio is using MediaStore, SAF, DirectFS, manual paths, raw media_rw, or stale/inaccessible paths.

### Earliest autostart/readiness

Outputs are under `autostart/` plus `_early_boot/` in the base output folder. The script records a pre-main-delay service.d snapshot before `BOOT_WAIT_SECONDS`, boot/readiness properties, BOOT_COMPLETED/LOCKED_BOOT_COMPLETED/USER_UNLOCKED/QUICKBOOT receiver resolvers, broadcast queues, jobs, alarms, and log markers. This cannot observe true init/post-fs-data timing because it runs from Magisk late_start service, but it can show how early Auxio, DoFun, storage and media services are ready from the first service.d opportunity.

### Floating controls vs status bar / edge drawer

Outputs are under `overlay/` and each snapshot’s `overlay-window-context.txt`. The script captures SYSTEM_ALERT_WINDOW appops, window/layer focus, SurfaceFlinger layer names, display size/density, insets/gesture/navigation settings, input/touch devices, status/navigation/gesture windows, and Auxio/DoFun overlay/window markers.

A normal app overlay cannot reliably be forced above privileged SystemUI/status/navigation/gesture layers. The diagnostics therefore aim to prove whether Auxio is below SystemUI, outside touchable regions, blocked by edge gesture handling, or displaced by launcher/SystemUI policy before deciding whether an in-app adjustment, different overlay window flags, coordinate clamping, immersive mode, or a privileged/system-level approach is required.

### Playback interruptions / unexpected pause

Outputs are under `interruptions/`, `audio_all.txt`, `media_session_all.txt`, and each snapshot’s `playback-interruption-context.txt`. The script captures audio focus stack, focus gain/loss/duck markers, `ACTION_AUDIO_BECOMING_NOISY`, media buttons, Bluetooth/telecom/radio/NavRadio hints, DoFun/Topway commands, media-session state changes, power/doze state, and competing players.

## Privacy warning

The output can contain:

- media titles and file paths;
- Bluetooth device names;
- Wi-Fi/network names;
- account/package names;
- notification text;
- logs from other apps;
- USB volume IDs.

Inspect before public sharing. For this ChatGPT TS18 project, the archive is intended to be shared privately for development diagnostics.

## Safety boundaries

This pack does not:

- flash firmware;
- write Android/system/vendor partitions;
- delete or modify media;
- clear app data;
- disable packages;
- restart Topway services;
- change SELinux;
- install Magisk modules;
- alter MCU/CAN/radio/Bluetooth vendor components.

## Interpreting REPORT.md

`REPORT.md` includes:

- package table;
- media-session and notification hit counts;
- log error hints;
- post-v5.3 Auxio-TS feature evidence matrix;
- quick error excerpt;
- list of files most useful to share.

The feature matrix is heuristic. A PASS-ish row means the script saw evidence. UNKNOWN/FAIL means there was no matching evidence in that capture window, not necessarily that the feature is broken.

## What to do after running

Run this quick readout on the TS18:

```sh
OUT="$(ls -td /storage/emulated/0/Download/TS18_AuxioMediaDiag/ts18-auxio-media-* | head -n 1)"
cat "$OUT/REPORT.md"
grep -i -A80 -B25 'org.oxycblt\|com.tw.media\|com.tw.music' "$OUT/media_session_all.txt" | head -n 400
grep -i -A80 -B25 'org.oxycblt\|com.tw.media\|com.tw.music' "$OUT/notification_all.txt" | head -n 400
grep -i -E 'Auxio|DirectFS|FilteredFS|LocationMode|SAF|DocumentsUI|RootState|Topway|DoFun|Overlay|CarFloating|StatusBar|NavigationBar|AudioFocus|AUDIOFOCUS|becoming noisy|RemoteViews|Bitmap|MediaSession|ExoPlayer|FATAL|ANR|Exception|denied|timeout|failed' "$OUT/logs/logcat_filtered_tail.txt" | tail -n 300
ls -la "$OUT/source_paths" "$OUT/autostart" "$OUT/overlay" "$OUT/interruptions" 2>/dev/null
```

Then upload the `.tar.gz` or selected files here.
