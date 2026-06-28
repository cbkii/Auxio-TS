# TS18 Runtime Hardening Notes

Status: TS18/head-unit feature completion and robustness pass complete.

## Implemented Behaviors

### /storage/emulated/0/Download Fallback
- `/storage/emulated/0/Download` is now considered a fully-fledged safe Direct FS candidate root for the TS18 variant.
- It is accepted generically as a fallback option when standard USB options are unavailable or inaccessible.

### Source Path Selection & Scan Backend Reliability
- Transiently missing USB mounts or unreadable volumes no longer cause Auxio-TS to wipe out existing validated cache indexes (`KEEP_CACHED_LIBRARY`).
- Re-scans correctly maintain index readiness, failing closed without deleting everything when physical drives are temporarily unreadable.

### Autostart Mode: Floating Controls Only
- `BootReceiver` now checks `autostartControlsOnly` in preferences and selectively launches `CarFloatingControlsService` dynamically without opening the main UI.

### Scan Eligibility & Speed
- Non-audio extension files (e.g., images, text, configs, apks) are completely ignored during `ExploreStep` using simple, zero-metadata string filters.
- Extensionless `application/ogg` and `application/x-ogg` files are correctly handled before the allow-list logic.
- Exclusion is purely extension and MIME-type based now to ensure valid large audio files aren't arbitrarily dropped.

### Raw Fast-Resume & MediaReconciliation
- `RawFastResumeValidator` verifies `content://` with a cheap audio-likeness check, and gracefully falls back to direct-paths if necessary.
- During raw play, Auxio properly respects `isPlaying` preservation without suddenly triggering playback if stopped via the correct `playbackManager` flow.

### Shuffle Mode Current-Song Preservation
- We now track the exact playing song and `currentMediaItemIndex` across `PlaybackViewModel` and `ExoPlaybackStateHolder` when shuffling. Playback remains perfectly on the same media item, position, and `isPlaying` state, guarding against invalid media index seeks.

### Metadata Delivery Speed
- Text metadata populates MediaSession, Notifications, and Topway/DoFun Widgets synchronously before waiting for Coils/Bitmaps to fetch, resolving widget pop-in lag.
- `WidgetComponent` validates that async artwork callbacks don't accidentally push stale song state on completion.

## Partially Implemented / Follow-Up Work
- **Floating Controls True Persistence**: The system uses lifecycle and receiver hooks to restore appropriately, but a full central supervisor/watchdog process was deferred as out of scope for this pass.
- **Root-First Source Access**: `RootStateHolder` correctly gates according to user choices (`isRootEnabledByUser`), but `DirectFS` still executes normal `listFiles()` before attempting `runRootCommandSync`. True root-first logic is deferred.
- **Source Repair Semantics**: Repair logic is currently direct-root/default-USB based rather than configured-source based. Further refinement of SAF repair states is needed.

## Requires Device Validation
- Run DoFun launcher widget checks from cold start.
- Confirm USB mount / unmount timing logic matches standard expected TS18 behavior.
- Validate `autostartControlsOnly` works seamlessly without triggering background-service kills from Android 10+.