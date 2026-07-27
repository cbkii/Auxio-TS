# Development

## Prerequisites

- JDK 21
- Android SDK, platform/build-tools 36 and NDK 28.2.13676358
- CMake, Ninja and Git
- Recursive access to the pinned `media`, FFmpeg, TagLib and utfcpp submodules

## Setup

```bash
bash scripts/bootstrap-dependencies.sh --profile full-build
bash scripts/ci-gradle.sh :app:assembleTopwayTwMediaDebug
```

`prepare-ci-environment.sh` remains a compatibility wrapper around `bootstrap-dependencies.sh`. A ZIP/snapshot checkout is suitable only for degraded static review because Gradle needs exact gitlink-pinned submodules.

For a fresh Linux agent, `scripts/setup-codex-android-env.sh` can verify the Android toolchain and run a smoke test. Report `DEGRADED_STATIC_ONLY` rather than claiming Gradle success when dependencies cannot be fetched.

## Dependency profiles

| Profile | Use | Failure policy |
| --- | --- | --- |
| `static-review` | Source/YAML/script review | May degrade honestly |
| `jvm-tests` | JVM tests | Strict because Gradle configuration needs the native/media graph |
| `full-build` | CI/debug/release-equivalent builds | Strict |
| `release` | Signed release | Fail closed |

Dependency policy lives under `ci/dependencies/`; shared read-only parsing and pin checks live in `scripts/dependency-lib.sh`. Approved mirrors may fetch only the exact pinned commit.

## Maintained product variants

| Variant | Package | Purpose |
| --- | --- | --- |
| `topwayTwMedia` | `com.tw.media` | Primary automatic CI, normal APK release and alternate DoFun fixed entry |
| `topwayTwMusic` | `com.tw.music` | Exact stock-package compatibility and Magisk packaging |

The old `standard` flavour is retired. Generic/non-Topway policy behaviour remains covered through pure inputs/tests rather than another distributable package.

Both variants share `app/src/topwayCompat/`, exposing `com.tw.music.MusicActivity` while keeping distinct package identities.

## Key Gradle tasks

| Task | Purpose |
| --- | --- |
| `:app:assembleTopwayTwMediaDebug` | Primary debug APK |
| `:app:assembleTopwayTwMusicDebug` | Exact-package compatibility debug APK |
| `:app:assembleTopwayTwMediaRelease` | Primary signed release APK |
| `:app:assembleTopwayTwMusicRelease` | Internal APK used for Magisk packaging |
| `:app:testTopwayTwMediaDebugUnitTest` | App JVM test authority |
| `:app:lintTopwayTwMediaDebug` | App Android lint authority |
| `:musikr:testDebugUnitTest` | Musikr JVM tests |
| `:app:connectedTopwayTwMediaDebugAndroidTest` | API 29 app instrumentation authority |
| `:startup-benchmark:assembleTopwayTwMediaBenchmarkBenchmark` | Primary benchmark instrumentation |
| `:startup-benchmark:assembleTopwayTwMusicBenchmarkBenchmark` | Exact-package benchmark instrumentation |

CI-equivalent local quality:

```bash
bash scripts/bootstrap-dependencies.sh --profile full-build
bash scripts/ci-gradle.sh --continue \
  spotlessKotlinCheck \
  :app:testTopwayTwMediaDebugUnitTest \
  :musikr:testDebugUnitTest \
  :app:lintTopwayTwMediaDebug
bash scripts/ci-gradle.sh \
  :app:assembleTopwayTwMediaDebug \
  :app:assembleTopwayTwMusicDebug
```

## CI architecture

`scripts/ci-scope.sh` classifies changed files and fails open to full maintained CI when comparison evidence is unavailable. Automatic workflows start for every PR/push and condition jobs rather than skipping a whole required workflow.

- `Android Build`: both Topway APKs in one Gradle invocation; API 29 for high-risk changes and all `dev` pushes.
- `Android Quality`: one consolidated Gradle setup/invocation for formatting, tests and lint; small gate jobs preserve required check names.
- `Startup Release Validation`: manual exhaustive profile/release validation for only the maintained variants.
- `Startup Benchmarks`: manual API 29 macrobenchmark or API 35 Baseline Profile generation; defaults to `topwayTwMedia`.
- `UI Screenshots`: manual Roborazzi for `topwayTwMedia` or `topwayTwMusic`; defaults to `topwayTwMedia`.
- `Gradle Optimisation Pilot`: manual configuration-cache and parallel-execution evidence.
- `Manual Release`: signed `com.tw.media` APK and optional `com.tw.music` Magisk module only.

Build/test jobs use `fetch-depth: 1`; scope fetches only the boundary commit. Release keeps full history and tags.

Configuration cache and Gradle parallelism remain opt-in through the wrapper and manual pilot until proven safe for the exact current task set.

## Required checks

Keep these branch-protection names:

- `Android Build / build`
- `Android Quality / Workflow/script syntax`
- `Android Quality / Formatting`
- `Android Quality / Unit tests`
- `Android Quality / Android lint`
- `Android Quality / Head-unit safety`

## Startup/library architecture

The immediate startup lane restores primitive queue/playback authority and bounded Room-backed Fast Start, Quick Find, direct-folder and early MediaBrowser surfaces. It does not wait for full `Musikr.loadCached()` graph reconstruction, category sorting or a storage scan. Compatibility hydration may continue asynchronously but cannot replace the queue or block first interaction.

Filesystem scans are limited to first install/no library, explicit refresh/rescan, or bounded recovery. Temporary unmounts preserve the last-known-good generation. Generated playlists remain optional post-load work and must not block source selection or base library publication.

For Topway validation, confirm one `AuxioService`, one MediaSession, early MediaBrowser readiness and Topway command routing while hydration is active. Emulator evidence is not exact TS18 proof.

Checked-in profiles:

- `app/src/main/baseline-prof.txt`
- `app/src/main/generated/baselineProfiles/startup-prof.txt`

Run `scripts/check-startup-performance-contracts.sh` to protect profile composition and packaged artefacts.

## Compatibility checks

```bash
bash scripts/check-ci-variant-contracts.sh
bash scripts/check-ts18-apk-reference-contracts.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-startup-performance-contracts.sh
```

## Roborazzi

Roborazzi runs through Robolectric at 1280×720. Regular unit-test tasks exclude the screenshot smoke test; use explicit `recordRoborazziTopwayTwMediaDebug`, `verifyRoborazziTopwayTwMediaDebug`, and corresponding `TopwayTwMusicDebug` tasks or the manual workflow.

## Release safety

The primary published APK is `com.tw.media`. `topwayTwMusic` is packaged as a Magisk systemless overlay and is never published as a raw APK. Package identity, signing certificate, privileged placement, launcher recognition and root are separate authorities. Read `docs/TS18_INSTALLATION_CONSTRAINTS.md` before installing.
