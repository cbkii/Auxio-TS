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
