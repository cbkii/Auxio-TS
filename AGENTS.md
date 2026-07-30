# AGENTS.md — Auxio-TS coding authority

## Project stance

- Start documentation navigation from `docs/README.md`.
- Use `docs/DEVELOPMENT.md` and `docs/CI_TASK_POLICY.md` for setup, build and CI policy.
- **Auxio-TS is a TS18/TW/TWTHEME product variant.** Android-standard APIs are the preferred first layer, not final authority for TS18-specific behaviour.
- Keep Topway/DoFun integration behind adapters or facades. Evidence and decompiled resources are compatibility evidence only; do not copy vendor implementation code.
- Native/private integration is not for production by default. It requires the formal evidence-gated gap-and-promotion process.

## Maintained product variants

The distributable `standard` flavour is retired. Do not recreate it as a generic test target.

| Variant | Package | Authority |
| --- | --- | --- |
| `topwayTwMedia` | `com.tw.media` | Primary JVM-test, lint, API 29, benchmark, screenshot and normal APK-release lane |
| `topwayTwMusic` | `com.tw.music` | Internal exact-package compatibility/test build; never a release/install asset |

Both variants reuse `app/src/topwayCompat/` and expose `com.tw.music.MusicActivity`. Keep their package/component contracts distinct. Test generic/non-Topway fallback policy through pure inputs, not a third distributable package.

Automatic CI must never invoke a `Standard` Gradle task. `scripts/check-ci-variant-contracts.sh` is the retirement and focused-task guardrail.

## CI scope and required checks

`scripts/ci-scope.sh` is the changed-file classification authority. It must fail open to all maintained lanes when the comparison range or a path is uncertain.

Required mapping:

- workflow, documentation and ordinary shell-only edits: syntax/static/executable contracts;
- ordinary app code: formatting, app JVM tests, app lint and the primary `topwayTwMedia` build;
- Musikr-only work: Musikr JVM tests plus one `topwayTwMedia` integration compile;
- `topwayTwMedia`-specific resources/package work: primary build and relevant package checks;
- `topwayTwMusic`-specific resources/package work: exact-package build and package/component checks;
- shared `topwayCompat` source/manifest/resource work: both maintained APKs and compatibility contracts;
- database, startup, storage, service, provider, receiver and manifest authority: API 29 in addition to focused quality/build work;
- Gradle/dependency authority and unknown paths: full maintained validation;
- every push to `dev`: API 29;
- `ci:full` label or manual dispatch: every maintained lane.

The build workflow selects only relevant variants. When both are required, they remain in one Gradle invocation. The quality workflow selects only relevant formatting, app tests, Musikr tests and lint, while stable gate jobs preserve these required names:

- `Android Build / build`
- `Android Quality / Workflow/script syntax`
- `Android Quality / Formatting`
- `Android Quality / Unit tests`
- `Android Quality / Android lint`
- `Android Quality / Head-unit safety`

Jobs skip through `jobs.<id>.if`; do not skip an entire required workflow at trigger level.

## Gradle execution policy

All CI Gradle calls go through `scripts/ci-gradle.sh` with explicit tasks.

- automatic execution is no-daemon, sequential and limited to one worker;
- build cache is enabled;
- the one-worker default prevents concurrent Kotlin/KSP/Hilt variant compilations from exhausting the Kotlin daemon;
- a controlled caller may set `AUXIO_TS_CI_GRADLE_MAX_WORKERS`;
- configuration cache is opt-in with `AUXIO_TS_CI_CONFIGURATION_CACHE=1`;
- parallel execution is opt-in with `AUXIO_TS_CI_GRADLE_PARALLEL=1`;
- use the manual Gradle Optimisation Pilot for evidence;
- do not enable configuration cache or parallelism automatically until the exact current task set is proven compatible and race-free.

Do not replace scoped tasks with generic `build`, `check`, `test` or `lint` aggregates unless repository policy deliberately changes.

## Validation baseline

Run the narrowest relevant commands, or document the exact blocker:

```bash
bash ./scripts/bootstrap-dependencies.sh --profile full-build
bash ./scripts/ci-scope.sh --self-test
bash ./scripts/check-ci-variant-contracts.sh
bash ./scripts/check-ts18-apk-reference-contracts.sh
bash ./scripts/check-dofun-topway-compat.sh
bash ./scripts/check-headunit-compat-safety.sh
bash ./scripts/check-startup-performance-contracts.sh

# Full-maintained quality evidence
bash ./scripts/ci-gradle.sh --continue \
  spotlessKotlinCheck \
  :app:testTopwayTwMediaDebugUnitTest \
  :musikr:testDebugUnitTest \
  :app:lintTopwayTwMediaDebug

# Full-maintained build evidence
bash ./scripts/ci-gradle.sh \
  :app:assembleTopwayTwMediaDebug \
  :app:assembleTopwayTwMusicDebug
```

Never claim build, test, lint, APK or runtime success unless that exact command passed for the current relevant head. Fetch complete CI logs, identify the earliest root failure, distinguish setup from real build failure, and do not rerun an unchanged deterministic failure.

## Dependency bootstrap authority

The canonical bootstrap is `scripts/bootstrap-dependencies.sh`.

| Profile | Use | Failure policy |
| --- | --- | --- |
| `static-review` | Source/YAML/script review | May degrade honestly to `DEGRADED_STATIC_ONLY` |
| `jvm-tests` | JVM tests | Strict because Gradle configuration needs the media/native graph |
| `full-build` | CI/debug/release-equivalent work | Strict |
| `release` | Signed release | Fail closed |

`scripts/prepare-ci-environment.sh` is a compatibility wrapper. `scripts/check-submodules.sh` is read-only validation. Shared gitlink, mirror, profile and SDK policy lives in `scripts/dependency-lib.sh` and `ci/dependencies/`.

Approved mirrors may fetch only the exact pinned commit. Never substitute dependency HEADs. ZIP/snapshot checkouts without `.git` cannot prove gitlink pins and cannot claim Gradle success.

Build/test jobs use shallow checkout and then repo-owned bootstrap. Release workflows retain full history and tags. Do not reintroduce recursive checkout before repository mirror/pin policy can run.

## DoFun, Topway and stock music authority

Primary references:

- `docs/DOFUN_VARIETY_COMPATIBILITY.md`
- `docs/TS18_APK_REFERENCE.md`
- `docs/reference/ts18-apk/`
- `docs/topway/`
- exact-device diagnostics and evidence packs supplied for the task.

Observed reusable requirements:

- DoFun matching recognises `com.tw.media/com.tw.music.MusicActivity` and `com.tw.music/com.tw.music.MusicActivity`;
- `topwayTwMusic` intentionally uses exact package `com.tw.music`;
- `topwayTwMedia` intentionally uses `com.tw.media` while exposing the stock activity component;
- Topway bridge strings belong only in the isolated bridge, its tests and documentation.

Observed private Cardoor services, `android.uid.system`, shared UID, `TWUtil` and `com.tw.service.xt` AIDL are evidence only. Do not fake services, copy smali, require platform signing or claim a normal rooted APK has UID 1000 authority.

The optional LSPosed addon is the narrow exception for execution context, not an identity
exception for Auxio: it may run inside the already-installed, signer-verified stock
`com.tw.music` UID-1000 process. It must never assign, spoof or grant that identity to Auxio,
replace the stock APK, or broaden its static scope.

## TS18 evidence and architecture

For TS18 work:

1. Search the curated TS18/TW/TWTHEME corpus first.
2. Prefer proven public head-unit implementations over speculative probes.
3. Classify claims with evidence confidence and porting decision.
4. Use fresh diagnostics when supplied or when source-led evidence is insufficient.
5. Keep diagnostic collection external and bounded unless a narrow normal-runtime log marker is justified.
6. Never claim CI/emulator evidence proves exact widget, USB, ACC, MCU, CAN, DSP, radio or launcher behaviour.

Evidence confidence: **Observed**, **Inferred**, **Hypothesis**, **Requires TS18 validation**, **Unsupported**.

Porting decision: **Directly reusable requirement**, **Reusable validation idea**, **Useful as evidence only**, **Obsolete due to Auxio architecture**, **Requires TS18 runtime validation**, **Unsafe to port**, **Should be explicitly avoided**.

Separate Android framework authority, Topway/TW service authority, DoFun launcher authority, HAL/sysfs authority, MCU/CAN authority and Magisk/root authority. Related TS10/TS10M/8581 units are precedent, not exact-device proof.

## Runtime integration authority

- `TopwayLauncherIntegrationCoordinator` owns mode-gated metadata/progress broadcasts and command handling.
- `PlaybackServiceFragment` is the canonical runtime publishing call-site.
- `WidgetComponent` renders Auxio AppWidgets but is not the sole DoFun bridge.
- `TopwayMusicBroadcastBridge` is supporting/legacy code.
- Preserve one playback service, queue authority, MediaSession and notification authority.
- Keep Android Bluetooth, audio focus and MediaSession separate from Topway Bluetooth, DSP/radio, MCU/CAN and launcher control.
- A model, registry, status object or document does not count as implemented until a meaningful runtime or executable workflow consumes it.
- Preserve API 29 compatibility and API-gate newer behaviour.
- Keep I/O bounded and off the main thread; treat startup, notifications, RemoteViews, Material inflation, media scanning and tag parsing as OEM-crash-sensitive.

## Hard constraints

- Do not restore `standard` or add another distributable flavour.
- Do not casually change maintained package/component contracts.
- Do not require privileged/system UID, platform signing or shared UID for an Auxio APK or
  replacement package. The optional LSPosed addon may validate and execute inside the existing
  genuine stock process only under the narrow rule above.
- Do not copy vendor smali or private implementations.
- Do not spread TS18 conditionals through core playback/library code.
- Do not add in-app probe frameworks, hidden diagnostics, package scanners or vendor-service binders.
- Do not add direct external `com.tw.*` or `android.tw.john.*` imports. Approved thin wrappers may expose stock-compatible names and delegate into Auxio-owned code.
- Do not restore the abandoned in-app TS18 Health Diagnostics / Storage Health screen.
- Review final diffs for APKs, reports, logs, credentials, temporary workflows, benchmark outputs, scratch scripts and stale comments.

## UI, startup and physical validation

Roborazzi supports only `topway_twmedia` and `topway_twmusic`; `topway_twmedia` is default. Screenshots are development-only and should cover the relevant 1280×720 head-unit surface.

Startup benchmark/profile work supports only maintained variants. API 29 macrobenchmarks are Android 10 evidence; API 35 is used for Baseline Profile generation. Exact TS18 timing and fixed-widget behaviour require physical validation.

## Release and signing safety

- Manual releases run only from current `dev` with full history/tags.
- Published install assets are the signed `com.tw.media` APK and the separately signed, optional
  LSPosed API 100 bridge addon.
- The former exact-package `topwayTwMusic` Magisk overlay is retired. Never publish its raw APK or
  reconstruct the overlay release lane.
- Keep the LSPosed bridge static-scoped exactly to the genuine stock `com.tw.music` process.
- Never print secrets or commit keystores; use runner temporary storage.
- Stage and validate rebuilt assets before replacing existing release assets.
- Preserve package, version, SDK, ABI, signing-certificate and SHA-256 sidecars.
- Root does not provide platform identity. Exact-package installation requires verified stock path, rollback media and boot-loop recovery.

## Exact-device and root-storage context

Read before exact TS18 install/runtime work:

- `docs/CODEX_TS18_DEVICE_CONTEXT.md`
- `docs/TS18_INSTALLATION_CONSTRAINTS.md`
- `docs/evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md`

DirectFS is the primary fresh-install source path; SAF and MediaStore remain explicit alternatives. Root storage is consent-gated, typed, read-only and bounded. `/mnt/media_rw/usbdiskN` is internal backing/discovery only; persist and play through app-readable `/storage/...` paths or validated aliases. A root listing does not prove parser, artwork or playback access.

Do not block boot, cache restore, MediaSession readiness or first audio on interactive `su`. Root consent never authorises protected-package mutation, system writes, platform identity, MCU/CAN or vendor-service changes.

## Delivery and final response discipline

Large tasks are delivery contracts. Implement real runtime or executable workflow behaviour; docs/tests support but do not replace it. Continue through locally implementable scope despite environment-limited Gradle proof. “Ready to merge” applies only when current-head required checks and actionable review are closed.

Always report what is implemented, partial or scaffold-only; exact validation commands and SHA; environment or physical-device boundaries; and whether the result is a review snapshot or complete.
