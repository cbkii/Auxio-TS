# TS18 PR117 + PR118 release gate

This document is the final merge gate for the stacked TS18 foundation:

1. PR#118 (`cx/ts18-fast-resume-source-repair-stack`) merges into PR#117 (`cx/harden-ts18-integration-for-auxio-ts`).
2. The resulting PR#117 branch runs final CI and exact-device smoke validation.
3. PR#117 merges to `dev` as the clean foundation for future TS18 work.
4. Further feature work starts from a new branch off `dev`.

## Scope status

### PR#117 foundation

Implemented foundation scope:

- removed the abandoned in-app TS18 diagnostics path;
- kept diagnostics external;
- hardened invalid provider/content-observer handling;
- preserved Android 10 direct-file compatibility where normal-app-safe;
- strengthened Topway/DoFun public compatibility surfaces;
- added cold widget control behaviour and bounded RemoteViews handling;
- added minimal fast-resume snapshot persistence.

### PR#118 runtime completion

Implemented runtime scope:

- direct raw URI/path fast resume from `FastResumeSnapshot` before full library availability;
- public ExoPlayer/MediaItem-only playback path;
- safe direct `/storage/usbdiskN` fallback validation;
- stale/invalid position clamping;
- fail-closed handling for missing, inaccessible, unsupported, unsafe, or non-audio-like sources;
- no package identity, UID, private vendor API, Magisk helper, or platform-signing dependency;
- reconciliation from raw item back to normal Auxio `Song` state when the library becomes available;
- raw metadata projection through `PlaybackStateManager`, MediaSession, notification, standard widget, Topway widget, and topwayCompat legacy broadcasts;
- source repair-state model and UI for `/storage/usbdisk0` and `/storage/usbdisk1`;
- bounded first-audio latency logging.

## Required validation before merging PR#117 to `dev`

Run after merging PR#118 into PR#117:

```sh
git diff --check
timeout --foreground "${LONG_TIMEOUT:-45m}" ./gradlew --no-daemon --console=plain --stacktrace spotlessCheck
timeout --foreground "${LONG_TIMEOUT:-45m}" ./gradlew --no-daemon --console=plain --stacktrace :app:testStandardDebugUnitTest :musikr:testDebugUnitTest
timeout --foreground "${LONG_TIMEOUT:-45m}" ./gradlew --no-daemon --console=plain --stacktrace :app:lintStandardDebug
timeout --foreground "${LONG_TIMEOUT:-45m}" ./gradlew --no-daemon --console=plain --stacktrace :app:assembleStandardDebug :app:assembleTopwayTwMusicDebug :app:assembleTopwayTwMediaDebug
timeout --foreground "${LONG_TIMEOUT:-45m}" bash ./scripts/check-ts18-apk-reference-contracts.sh
timeout --foreground "${LONG_TIMEOUT:-45m}" bash ./scripts/check-dofun-topway-compat.sh
timeout --foreground "${LONG_TIMEOUT:-45m}" bash ./scripts/check-headunit-compat-safety.sh
```

If Termux cannot run Gradle reliably, record that explicitly and rely on GitHub Actions for Gradle/lint/assemble results. Do not mark Gradle checks as passed unless they actually ran.

## Required TS18 exact-device smoke tests

Record each result as Observed, Inferred, Hypothesis, Requires device validation, or Unsupported:

- cold boot with valid `/storage/usbdisk0` snapshot;
- cold boot with valid `/storage/usbdisk1` snapshot;
- launcher restart then DoFun widget update request;
- process death then DoFun widget play/pause;
- ACC sleep/wake with USB still present;
- USB removal before restore;
- USB reinsert after raw restore failure;
- empty USB source;
- non-audio-only USB source;
- nested USB layout such as `/storage/usbdisk0/Music/Artist/track.flac`;
- standard variant regression check on a normal Android device.

## Explicit non-goals retained

Do not add these to the PR117/PR118 foundation:

- Magisk priv-app/helper module implementation;
- replacing or impersonating platform-signed `com.tw.music`;
- private Topway/Cardoor/vendor binder protocols;
- firmware, boot, LCD, MCU, CAN, or partition writes;
- generic phone-only media-player assumptions;
- reintroduced in-app TS18 diagnostics capture.

## Release decision

The code scope is complete when PR#118 is merged into PR#117. The release gate is complete only after CI and the exact-device smoke tests above are recorded.
