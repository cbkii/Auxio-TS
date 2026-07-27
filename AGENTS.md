# AGENTS.md — Auxio-TS coding authority

## Project stance

- Start documentation navigation from `docs/README.md`.
- Use `docs/DEVELOPMENT.md` for environment setup, build tasks and CI policy.
- **Auxio-TS is a TS18/TW/TWTHEME product variant.** TS18/TW/TWTHEME parity is the product target.
- Android-standard APIs are the preferred first implementation layer, not final authority for TS18-specific behaviour.
- Keep Topway/DoFun integration behind adapter/facade boundaries.
- Evidence and decompiled resources are compatibility evidence only; do not copy vendor implementation code.
- Native/private integration is not for production by default. It requires the formal evidence-gated gap-and-promotion process.

## Maintained product and CI variants

The distributable `standard` flavour is retired. Do not recreate it to obtain a generic test target.

| Variant | Package | Authority |
| --- | --- | --- |
| `topwayTwMedia` | `com.tw.media` | Primary automatic build, JVM test, lint, API 29, benchmark, screenshot and normal release lane |
| `topwayTwMusic` | `com.tw.music` | Exact-package compatibility build and Magisk packaging lane |

Both variants reuse `app/src/topwayCompat/` and expose `com.tw.music.MusicActivity`. Their package/component contracts must remain distinct. Generic/non-Topway policy behaviour must be tested through pure policy inputs, not a third distributable flavour.

Automatic CI must not invoke any `Standard` Gradle task. `scripts/check-ci-variant-contracts.sh` is the retirement guardrail.

## DoFun Variety / stock twmusic authority

Primary references:

- `docs/DOFUN_VARIETY_COMPATIBILITY.md`
- `docs/TS18_APK_REFERENCE.md`
- `docs/reference/ts18-apk/reference-contracts.json`
- `docs/reference/ts18-apk/dofun-variety/apps_match_config.music-excerpts.json`
- `docs/reference/ts18-apk/twmusic/classes.string-hits.txt`
- `docs/topway/`

Priority for music-widget/package work:

1. DoFun Variety (`com.dofun.variety`) recognition and widget/control behaviour.
2. Stock `twmusic` / `com.tw.music` replacement contract.
3. Android MediaSession/MediaBrowser/notification correctness.
4. Isolated Topway broadcast/action bridge.
5. Private/native investigation only after evidence-gated approval.

Observed reusable requirements:

- DoFun hotseat matching recognises `com.tw.media/com.tw.music.MusicActivity` and `com.tw.music/com.tw.music.MusicActivity`.
- `topwayTwMusic` intentionally uses exact package `com.tw.music`.
- `topwayTwMedia` intentionally uses `com.tw.media` while exposing the stock activity component.
- Topway bridge strings are allowed only inside the isolated bridge package, its tests and documentation.

Observed but not approved for product use include Cardoor media services, `android.uid.system`, `sharedUserId`, `android.tw.john.TWUtil`, and `com.tw.service.xt` private AIDL. Do not fake these services, copy smali, require platform signing or claim a normal APK has UID 1000 authority.

## TS18 native parity tiers

See `docs/TS18_INTEGRATION_ARCHITECTURE.md`.

| Tier | Scope |
| --- | --- |
| 0 | Evidence only: APKs, diagnostics, firmware notes, public projects |
| 1 | Android-standard implementation: MediaSession, MediaBrowser, notifications, audio focus, media buttons, AppWidget |
| 2 | Exact TS18 validation proving which OEM surfaces observe Tier 1 |
| 3 | Isolated external experiments for a specific TW/TWTHEME contract |
| 4 | Production native integration only through an explicit human-approved design PR |

Tier 4 eligibility requires product need, evidence-backed contract, no package impersonation, no copied smali, no platform-signature/system-UID dependency, safe fallback, isolated implementation, validation and rollback.

## Required evidence method

For TS18/TW/TWTHEME work:

1. Search the curated TS18/TW/TWTHEME source corpus first.
2. Prefer proven public head-unit implementations over speculative probes.
3. Classify claims with both evidence confidence and porting decision.
4. Use fresh device diagnostics only when supplied or when source-led evidence is insufficient.
5. Keep diagnostic collection external and bounded unless normal runtime observability requires a narrow log marker.
6. Never claim emulator or CI evidence proves exact TS18 widget, USB, ACC, MCU, CAN, DSP or launcher behaviour.

Evidence confidence labels: **Observed**, **Inferred**, **Hypothesis**, **Requires TS18 validation**, **Unsupported**.

Porting decisions: **Directly reusable requirement**, **Reusable validation idea**, **Useful as evidence only**, **Obsolete due to Auxio architecture**, **Requires TS18 runtime validation**, **Unsafe to port**, **Should be explicitly avoided**.

## Runtime integration authority

- `TopwayLauncherIntegrationCoordinator` owns mode-gated Topway metadata/progress broadcasts and command handling.
- `PlaybackServiceFragment` is the canonical runtime publisher call-site.
- `WidgetComponent` renders Auxio AppWidgets but is not the sole DoFun launcher bridge.
- `TopwayMusicBroadcastBridge` is supporting/legacy bridge code.
- Preserve one playback service, one queue authority, one MediaSession and one notification authority.
- Keep Android Bluetooth, audio focus and MediaSession separate from Topway Bluetooth, DSP/radio, MCU/CAN and launcher control.

## Hard constraints

- Do not add a third distributable flavour or restore `standard` CI tasks.
- Do not change `topwayTwMedia` or `topwayTwMusic` package/component contracts casually.
- Do not require privileged/system UID, platform signing or shared UID.
- Do not copy decompiled smali or private vendor implementations.
- Do not spread TS18 conditionals through core playback/library code.
- Do not add in-app probe frameworks, hidden diagnostics modules, package scanners or vendor-service binders.
- Do not add direct external `com.tw.*` or `android.tw.john.*` imports. Thin wrappers under approved `topwayCompat` source sets may expose stock-compatible names and delegate into Auxio-owned code.
- Do not add Topway action strings outside the isolated bridge/tests/docs scope.
- Do not restore the abandoned in-app TS18 Health Diagnostics / Storage Health screen.
- Never claim tasks/build/test/lint success unless that exact command passed for the current head.
- Inspect complete CI logs before changing code. Do not repeatedly rerun an unchanged deterministic failure.

## Validation baseline

Run the narrowest relevant commands, or document the exact blocker:

```bash
bash ./scripts/bootstrap-dependencies.sh --profile full-build
bash ./scripts/check-ci-variant-contracts.sh
bash ./scripts/check-ts18-apk-reference-contracts.sh
bash ./scripts/check-dofun-topway-compat.sh
bash ./scripts/check-headunit-compat-safety.sh
bash ./scripts/ci-gradle.sh \
  :app:testTopwayTwMediaDebugUnitTest \
  :musikr:testDebugUnitTest \
  :app:lintTopwayTwMediaDebug
bash ./scripts/ci-gradle.sh \
  :app:assembleTopwayTwMediaDebug \
  :app:assembleTopwayTwMusicDebug
```

Avoid generic aggregate `build`, `check`, `test` and `lint` as PR proof unless repository policy is deliberately changed.

## CI scope and required checks

`scripts/ci-scope.sh` is the changed-file classification authority. It must fail open to full maintained validation when the comparison range or classification is uncertain. It publishes focused flags for static checks, Gradle configuration, app core, Musikr, Topway shared/specific code, API 29 risk, native code, benchmarks and releases.

Rules:

- The Android Build and Android Quality workflows start for every PR/push in scope; individual jobs skip through `jobs.<id>.if`, so required checks do not remain pending.
- Workflow/build-system changes run full maintained CI.
- Runtime-sensitive storage, startup, service, provider, receiver, manifest and database changes include API 29.
- Every push to `dev` includes API 29 integration smoke.
- The `ci:full` label or manual dispatch requests all maintained lanes.
- `topwayTwMedia` is the primary automatic test/lint/API 29 target.
- Both Topway debug APKs build in one Gradle invocation.
- Formatting, app/Musikr unit tests and app lint share one setup/bootstrap and one Gradle invocation. Small gate jobs preserve existing required check names.

Current required check names remain:

- `Android Build / build`
- `Android Quality / Workflow/script syntax`
- `Android Quality / Formatting`
- `Android Quality / Unit tests`
- `Android Quality / Android lint`
- `Android Quality / Head-unit safety`

## Checkout and dependency policy

The canonical bootstrap is:

```bash
bash ./scripts/bootstrap-dependencies.sh --profile full-build
```

Profiles:

- `static-review`: may degrade honestly to `DEGRADED_STATIC_ONLY`.
- `jvm-tests`: strict because Gradle configuration still needs pinned submodules.
- `full-build`: strict app/CI build profile.
- `release`: strictest signed-release profile.

Build/test jobs use `fetch-depth: 1`, `submodules: false`, then the repository bootstrap resolves current gitlinks. The scope job fetches only the comparison boundary. Release workflows retain full history and tags because versioning/tag/release logic requires them. Do not reintroduce recursive checkout ahead of repository mirror/pin policy.

Classify bootstrap outcomes accurately: `READY`, `SNAPSHOT_LIMITATION`, `SUBMODULE_BLOCKER`, `DEPENDENCY_DIRTY_SUBMODULE`, `DEPENDENCY_MIRROR_USED`, `DEPENDENCY_PIN_MISMATCH`, `SDK_BLOCKER`, `DEGRADED_STATIC_ONLY`, `REAL_BUILD_FAILURE`.

ZIP/snapshot environments without `.git` cannot prove gitlink pins. They may perform static review only.

## Gradle execution policy

All CI Gradle calls go through `scripts/ci-gradle.sh`.

- Default automatic execution remains sequential and no-daemon.
- Build cache is enabled.
- Configuration cache is opt-in with `AUXIO_TS_CI_CONFIGURATION_CACHE=1`.
- Parallel execution is opt-in with `AUXIO_TS_CI_GRADLE_PARALLEL=1`.
- Use `Gradle Optimisation Pilot (manual)` to collect configuration-cache reuse and parallelism evidence.
- Do not enable either optimisation automatically until a current-head pilot proves the exact maintained task set and generated-source/report outputs are race-free.

## UI and startup validation

Roborazzi supports only `topway_twmedia` and `topway_twmusic`; `topway_twmedia` is the default. Screenshots are 1280×720 and development-only.

Startup benchmark/profile work supports only the two maintained flavours. API 29 macrobenchmarks are Android 10 evidence; API 35 is used for Baseline Profile generation. Exact TS18 timing still requires physical validation.

## Release/signing safety

- Manual release runs only from current `dev` with full history/tags.
- Maintained install assets are the `com.tw.media` APK and optional `com.tw.music` Magisk module.
- Never publish a raw `topwayTwMusic` APK release asset.
- Never print secrets or commit keystores.
- Stage and validate rebuilt assets before replacing existing release assets.
- Preserve package, SDK, ABI, signing certificate and SHA-256 sidecars.
- Root/Magisk does not grant platform identity; exact-package installation requires verified stock path, rollback media and boot-loop recovery.

## Large-scope delivery protocol

- Large tasks are delivery contracts, not suggestion lists.
- Implement real executable/runtime/workflow behaviour; docs/tests are supporting proof.
- Continue through local implementation despite environment-limited Gradle proof.
- A task is not complete while a core requested workstream remains locally fixable.
- Keep final diffs free of temporary workflows, generated APKs, logs, credentials, benchmark outputs and stale stacked-branch references.
- “Ready to merge” applies only after current-head checks and actionable review comments are resolved.

## Exact-device and root storage context

Read before exact TS18 install/runtime work:

- `docs/CODEX_TS18_DEVICE_CONTEXT.md`
- `docs/TS18_INSTALLATION_CONSTRAINTS.md`
- `docs/evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md`

DirectFS is the primary source path for fresh Topway-compatible installs; SAF and MediaStore remain explicit alternatives. Root storage is consent-gated, typed, read-only and bounded. `/mnt/media_rw/usbdiskN` is internal backing/discovery only; persist and play through app-readable `/storage/...` paths or app-UID-validated aliases. Root consent never authorises protected-package mutation, system writes, platform identity, MCU/CAN or vendor-service changes. Playback refreshes must preserve the current track and not interrupt autoplay.
