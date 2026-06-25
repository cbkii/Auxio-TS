# Output map

## Top-level files

- `REPORT.md`: main human-readable report and evidence matrix.
- `00_SUMMARY.txt`: device/run summary and archive path.
- `00_TEST_STEPS.md`: manual test instructions.
- `00_FEATURE_AUDIT_SCOPE.md`: PR/release-note-derived evidence map for all new/unverified Auxio-TS areas.
- `package_table.tsv`: installed package/version/UID/path table.
- `media_session_all.txt`: time-ordered `dumpsys media_session` snapshots.
- `notification_all.txt`: time-ordered `dumpsys notification` snapshots.
- `audio_all.txt`: time-ordered `dumpsys audio` snapshots.
- `window_focus_all.txt`: time-ordered focused window/activity hints.
- `run.log`: script progress.

## Directories

- `packages/<pkg>/dumpsys-package.txt`: full package dump.
- `packages/<pkg>/quick-components.txt`: filtered manifest/permission/component summary.
- `snapshots/<n>_<time>/summary.txt`: quick readout for one interval.
- `snapshots/<n>_<time>/pkg-<pkg>/`: per-package filtered media/notification/service snapshots.
- `logs/logcat_filtered.txt`: filtered real-time logcat for Auxio/Topway/media/audio/storage/exception terms.
- `logs/logcat_filtered_tail.txt`: bounded tail for quick sharing.
- `logs/logcat_crash_buffer.txt`: crash-buffer snapshot.
- `logs/dropbox-crash-anr.txt`: DropBox crash/ANR/tombstone entries when enabled.
- `system/`: baseline Android/system dumps.
- `storage/`: volume and mount state.
- `vendor/`: Topway/Unisoc processes and properties.
- `magisk/`: root/Magisk/module/service.d inventory and safe root probes.
- `auxio/`: records that Auxio in-app DiagnosticService is skipped/removed.
- `appdata/`: Auxio data inventory; preferences only copied when enabled.

## Most important evidence for DoFun widget integration

1. `media_session_all.txt`
2. `notification_all.txt`
3. `snapshots/*/summary.txt`
4. `logs/logcat_filtered_tail.txt`
5. `packages/org.oxycblt.../quick-components.txt` or `packages/com.tw.media/quick-components.txt`
6. `packages/com.dofun.variety/quick-components.txt`
7. `packages/org.videolan.vlc/quick-components.txt`

## v3 focus-area outputs

### `source_paths/`

Deep SAF/file-path/source evidence:

- `source-candidate-path-matrix.txt`: existence, labels, canonical path, stat, sample directories and sample audio files for `/storage/emulated/0`, `/storage/usbdisk0`, `/storage/usbdisk1`, `/mnt/media_rw/...`, and common `Music`/`Download` folders.
- `saf-documentsui-resolvers.txt`: resolver availability for `OPEN_DOCUMENT`, `OPEN_DOCUMENT_TREE`, `GET_CONTENT`, and `VIEW` with audio and generic filters.
- `persisted-uri-grants.txt`: persisted/active URI grant evidence related to `content://` and `tree/` URIs.
- `auxio-source-path-grep.txt`: bounded grep of Auxio package data for source/path strings. This may contain private file paths.
- `mediastore-audio-rows.txt`: bounded `content query` rows from MediaStore audio/file tables where supported.

### `autostart/` and base `_early_boot/`

Earliest readiness evidence:

- `_early_boot/earlyboot-*.txt`: snapshot captured before the main collector delay, from Magisk `service.d` late_start timing.
- `readiness-baseline.txt`: boot props and service readiness props.
- `boot-receiver-resolvers.txt`: BOOT_COMPLETED / LOCKED_BOOT_COMPLETED / USER_UNLOCKED / QUICKBOOT receivers related to Auxio, DoFun and Topway.
- `activity-broadcasts-boot.txt`: broadcast queue evidence.
- `jobscheduler-auxio-media.txt` and `alarm-auxio-media.txt`: delayed work that may affect autostart.

### `overlay/` and snapshot `overlay-window-context.txt`

Floating controls/status/nav/edge evidence:

- `overlay-appops.txt`: SYSTEM_ALERT_WINDOW appops for target packages.
- `display-window-insets-settings.txt`: `wm` and navigation/gesture/status settings.
- `window-overlay-status-nav.txt`: overlay/status/nav/gesture/focus/touchable window excerpts.
- `surfaceflinger-layers.txt`: layer names for Auxio/DoFun/SystemUI/overlay context.
- `input-touch-devices.txt`: touch/input/gesture-capable device state.
- `snapshots/*/overlay-window-context.txt`: repeated time-series context while testing overlay placement.

### `interruptions/` and snapshot `playback-interruption-context.txt`

Playback interruption evidence:

- `audio-focus-baseline.txt`: current audio focus stack and related audio focus state.
- `telecom-baseline.txt`: phone/call routing context where available.
- `bluetooth-radio-media-baseline.txt`: Bluetooth/radio/media route context.
- `recent-log-interruptions.txt`: recent log markers for focus loss, noisy intents, media buttons, pause/resume, telecom/Bluetooth/radio/reverse/camera and player takeovers.
- `snapshots/*/playback-interruption-context.txt`: time-series focus/session/telecom snippets during manual tests.
