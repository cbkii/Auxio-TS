# TS18 Runtime Hardening Notes

Status: TS18/head-unit feature completion and robustness pass complete.

## Implemented Behaviors

### Floating Controls True Persistence
- `CarFloatingControlsService` correctly attaches and restores the overlay upon screen unlocking, sticky service restarts, boot completed broadcasts, and quick boot actions.
- The `CarOverlayVisibilityHooks` suppress the overlay correctly when the user enters the main Auxio-TS app, avoiding duplicate windows and rendering issues.

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
- Files strictly larger than 888MB are also ignored, guaranteeing parsing speed.

### Raw Fast-Resume & MediaReconciliation
- `RawFastResumeValidator` verifies `content://` fallbacks gracefully to direct-paths if necessary.
- During raw play, Auxio properly respects `isPlaying` preservation without suddenly triggering playback if stopped.

### Optional Root-First Source/File Backend
- `RootStateHolder` provides the interface `isRootEnabledByUser`. If true, we opportunistically attempt `su` listing of direct FS sources to bypass MediaStore latency or false negative responses from the system provider.

### Shuffle Mode Current-Song Preservation
- We now track the exact playing song and `currentMediaItemIndex` across `PlaybackViewModel` and `ExoPlaybackStateHolder` when shuffling. Playback remains perfectly on the same media item, position, and `isPlaying` state.

### Metadata Delivery Speed
- Text metadata populates MediaSession, Notifications, and Topway/DoFun Widgets synchronously before waiting for Coils/Bitmaps to fetch, resolving widget pop-in lag.

## Requires Device Validation
- Run DoFun launcher widget checks from cold start.
- Confirm USB mount / unmount timing logic matches standard expected TS18 behavior.
- Validate `autostartControlsOnly` works seamlessly without triggering background-service kills from Android 10+.
