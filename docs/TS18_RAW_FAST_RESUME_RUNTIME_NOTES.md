# TS18 raw fast-resume and source repair runtime notes

Status: Batch 1 implementation notes for PR#118.

## Evidence classification

- Observed: PR#117 persists a primitive `FastResumeSnapshot` containing URI/path/title/artist/album/duration/position/play state.
- Observed: `PlaybackStateManager` mirrors normal playback as `Song` queues, so raw pre-library playback must not be injected into the normal queue as a fake `Song`.
- Inferred: stock `com.tw.music` path-first resume and service-first command ingress can be adapted safely using public ExoPlayer `MediaItem` APIs.
- Requires device validation: exact first-audio latency on TS18 Android 10 for `/storage/usbdisk0` and `/storage/usbdisk1`.

## Batch 1 implementation shape

Raw fast-resume is owned by `ExoPlaybackStateHolder` only. It creates a temporary one-item ExoPlayer queue from a validated `FastResumeSnapshot` when the normal Musikr library is not ready. The raw item is not exposed as a `Song` and therefore cannot poison the normal queue/cache.

When the library later becomes available, the raw item is reconciled by:

1. exact URI string;
2. resolved direct path equality;
3. conservative title + duration match within 1000 ms.

If reconciliation cannot match, raw playback remains active while still playable. If raw playback fails, the snapshot is cleared and the holder does not call `next()` into an empty library.

## Source repair state model

`Ts18SourceRepairStatePolicy` classifies direct TS18 USB paths without requiring DocumentsUI:

- all sources ready;
- mount missing;
- direct path inaccessible;
- SAF permission missing;
- SAF provider failure;
- source empty;
- source contains no supported audio;
- mixed multiple-volume state;
- unknown failure.

Batch 1 provides the model and bounded direct-path classifier. A later UI batch should surface this state in the existing music/source settings area without broad redesign.

## Stock behaviours adapted versus rejected

Adapted safely:

- service-first playback start before indexing;
- path-first raw fast resume;
- simple play/pause/update command behaviour before library readiness;
- one playback engine / one MediaSession ownership model;
- first-audio latency markers.

Rejected:

- `android.uid.system`;
- platform signing;
- package impersonation;
- private Topway/Cardoor/TW AIDL;
- `TWMediaPlayer`;
- `/data/tw/theme` runtime dependency;
- `persist.*` writes;
- Magisk/system helper behaviour.

## Exact-device validation checklist

Run on TS18 target hardware:

- cold boot with valid `/storage/usbdisk0` snapshot;
- cold boot with valid `/storage/usbdisk1` snapshot;
- process death then DoFun widget play/pause;
- launcher restart then widget update request;
- ACC sleep/wake with USB still present;
- USB removal before restore;
- USB reinsert after raw restore fails;
- empty USB source;
- non-audio-only USB source;
- standard variant regression check on normal Android device.

Label all results as Observed, Inferred, Hypothesis, Requires device validation, or Unsupported.


## Batch 2 implementation shape

Observed: Batch 1 keeps raw fast-resume playback inside `ExoPlaybackStateHolder` so the normal `Song` queue is not poisoned.

Inferred: session, notification, standard widget, and Topway/DoFun widget surfaces still need a way to publish useful metadata while `PlaybackStateManager.currentSong == null`.

Implementation:

- `PlaybackStateHolder.rawPlaybackMetadata` exposes a primitive raw metadata mirror.
- `PlaybackStateManager` now owns and publishes `RawPlaybackMetadata` through the same listener model as queue/progression state.
- `MediaSessionHolder` publishes raw metadata to `MediaSessionCompat`, notification metadata, and Topway-compatible legacy metadata broadcasts while no normal `Song` is available.
- `WidgetComponent`, `WidgetProvider`, and the Topway widget provider render raw metadata using the same public RemoteViews/control path as normal playback.
- The standard widget can show raw title/artist/progress with default artwork; no fake `Song` is created.
- Source repair-state is surfaced in the existing Head unit settings category with bounded `/storage/usbdisk0` and `/storage/usbdisk1` checks.

Requires device validation:

- Whether DoFun launcher polls the standard AppWidget provider, Topway alias provider, legacy broadcasts, or all three during cold process start.
- Exact behaviour when `/storage/usbdiskN` disappears while ExoPlayer still holds an FD.

## Review resolution and finalisation

Observed: Gemini and CodeRabbit review comments on PR#118 identified valid stability and UI responsiveness issues in the Batch 2 implementation.

Resolved:

- `validateContentUri` now treats checked provider/file exceptions as provider failures instead of allowing them to escape raw snapshot validation.
- Raw reconciliation performs the potentially large library search off the main thread, then revalidates the raw item on the main thread before replacing playback state.
- Late raw validation results are ignored once the pending restore has already been consumed by normal library restore.
- Raw fast-resume snapshot saving uses `playbackManager.progression`, not direct ExoPlayer-backed progression, from the IO save path.
- The `first_playing_state` marker is latched so it records the first observed playing transition once per holder lifecycle.
- Source repair detection descends into nested directories while preserving a bounded entry cap.
- Source repair settings refresh runs filesystem probing off the UI thread and renders localized state labels for both summary and per-path details.

Requires device validation:

- Confirm first-audio marker timing on TS18 after ACC sleep/wake and process death.
- Confirm nested USB layouts such as `/storage/usbdisk0/Music/Artist/track.flac` report as ready on-device.
- Confirm DoFun widget cold-start renders raw metadata before the Musikr library becomes available.
