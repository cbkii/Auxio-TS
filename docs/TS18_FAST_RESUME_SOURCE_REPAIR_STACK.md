# TS18 fast resume and source repair runtime stack

This is the implementation plan and PR bootstrap document for the stacked branch `cx/ts18-fast-resume-source-repair-stack`.

Base branch: `cx/harden-ts18-integration-for-auxio-ts`

### Motivation

This stacked PR builds on PR#117 after the external-diagnostics/content-observer/Topway hardening scope.

PR#117 intentionally stopped at a safe foundation:

- external TS18 diagnostics only;
- hardened LocationObserver/content-provider handling;
- Android 10 USB/file-path compatibility;
- Topway/DoFun public compatibility surfaces;
- cold widget controls and bounded RemoteViews handling;
- minimal `FastResumeSnapshot` persistence only.

This PR completes the selected follow-up runtime work without adding privileged/vendor impersonation, Magisk helper behaviour, platform signing assumptions, or private vendor API dependencies.

### Stock `com.tw.music` evidence to inspect before implementation

Use the cloned read-only reference under:

- `docs/reference/t-music/docs/playback-architecture.md`
- `docs/reference/t-music/app/apktool/AndroidManifest.xml`
- `docs/reference/t-music/app/apktool/smali_classes*/com/tw/music/**`
- `docs/reference/t-music/app/apktool/smali_classes*/com/eckom/xtlibrary/**`

Emulate the normal-app-safe architecture only:

- service-first playback command ingress;
- `.cmd`, `.prev`, `.next`, `.pp` command shape where already exposed by Auxio-TS topwayCompat;
- widget cold-start update behaviour;
- launcher progress/state projection;
- path-first fast resume;
- direct `/storage/usbdiskN` source handling;
- simple model/state publication before heavyweight indexing.

Do not copy or depend on:

- `android.uid.system`;
- platform signing;
- `com.tw.music` package identity;
- private Topway/Cardoor/TW AIDL;
- vendor radio/EQ handoff;
- `TWMediaPlayer`;
- `/data/tw/theme`;
- `persist.*` write behaviour;
- Magisk/system-app/helper-module behaviour.

### Scope

#### 1. Direct TS18 fast resume before library availability

Implement direct raw URI/path playback from `FastResumeSnapshot` before the full Musikr library/cache is available.

Requirements:

- use public ExoPlayer/MediaItem APIs only;
- prefer persisted `uri`, with direct `/storage/usbdiskN` path fallback only where safe and accessible;
- clamp invalid/stale positions;
- fail closed if URI/path is missing, inaccessible, removed, or no longer audio-like;
- do not bypass Android audio focus or MediaSession ownership;
- do not impersonate platform-signed `com.tw.music` or UID 1000;
- reconcile with the normal library/queue once the library becomes available.

#### 2. Library reconciliation after fast resume

When the library becomes available:

- match the raw fast-resume item back to a known `Song` by URI/path where possible;
- replace temporary raw playback state with normal Auxio queue state;
- preserve position/play state where safe;
- avoid duplicate sessions, duplicate notifications, duplicate media buttons, and duplicate widget publishers;
- prevent raw playback failures from falling into an unsafe empty-library `next()` path.

#### 3. Stock-launcher-compatible public integration path

Complete the public compatibility path required for DoFun/Topway launcher/widget operation:

- ensure play/pause/next/previous/update commands work when no full Auxio library is ready yet;
- make widget cold-update request a reason to restore snapshot metadata and progress, not to trigger a full scan first;
- keep public legacy Android music metadata/playstate broadcasts topwayCompat-only;
- keep MediaSession ownership normal and single;
- do not introduce private vendor or privileged integration.

#### 4. Source repair-state model/UI

Add the missing source repair-state flow for TS18 USB/removable storage:

- detect missing or inaccessible `/storage/usbdiskN` sources;
- distinguish mount missing, permission missing, provider failure, empty source, non-audio-only source, and unknown failure;
- provide a minimal user-facing repair state;
- keep SAF and direct-path fallback separate;
- do not require DocumentsUI to exist;
- support multiple USB volumes;
- preserve the previous valid cached library when removable storage is absent or unstable.

#### 5. TS18 first-audio latency instrumentation

Add bounded, local-only instrumentation:

- service create/start timing;
- fast snapshot read timing;
- first playback request timing;
- ExoPlayer media item set timing;
- prepare timing;
- first `isPlaying` timing;
- source/library availability timing;
- reconciliation timing.

Do not add a long-running in-app diagnostics service. Do not enable background capture without explicit user action. Use existing lightweight logging/perf hooks where possible.

#### 6. Exact-device validation checklist

Document and prepare validation for:

- cold boot;
- launcher restart;
- process death;
- USB removal/reinsert;
- ACC sleep/wake;
- DoFun widget cold-start;
- foreground overlay suppression;
- playback resume from `/storage/usbdisk0`;
- playback resume from `/storage/usbdisk1`;
- empty USB source;
- non-audio-only USB source.

### Out of scope

- Magisk priv-app/helper module implementation.
- Replacing stock platform-signed `com.tw.music`.
- Private Topway/Cardoor/vendor binder protocols unless separately proven and approved.
- Firmware, boot, LCD, MCU, CAN, or partition writes.
- Generic phone-only media-player assumptions.
- Reintroducing PR#117’s removed in-app TS18 diagnostics path.

### Validation

Before ready for review:

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

If a local Termux environment cannot reliably run Gradle, record that clearly and rely on GitHub Actions for those tasks. Do not claim unrun checks passed.

### Notes for reviewers

This PR is intentionally stacked on PR#117. Review the diff relative to `cx/harden-ts18-integration-for-auxio-ts`, not directly against `dev`, to keep the runtime-completion scope isolated from PR#117 close-out work.


## Agent execution notes

- Treat `docs/reference/t-music/**` as read-only evidence.
- Implement normal-app-safe behaviour in Auxio-TS only.
- Do not copy privileged/system/vendor identity assumptions.
- Preserve PR#117 diagnostics removal and source-cache safety guarantees.
