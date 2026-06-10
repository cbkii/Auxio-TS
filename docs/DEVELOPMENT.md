# Development

## Prerequisites

- JDK 21 (Temurin recommended)
- Android SDK with build-tools, platform 34+, NDK
- Gradle (wrapper included)

## Setup

```sh
bash scripts/bootstrap-dependencies.sh --profile full-build   # init submodules, validate pins, create stubs
./gradlew :app:assembleStandardDebug                          # verify build
```

`prepare-ci-environment.sh` remains a backwards-compatible wrapper and defaults to `bootstrap-dependencies.sh --profile full-build`. Run the bootstrap command after clone or submodule changes. A ZIP/snapshot checkout is sufficient only for degraded static source review; Gradle needs the `media` submodule, nested ffmpeg sources, and `musikr` taglib sources.

For Codex, Jules-style agents, or a fresh Linux environment, `bash scripts/setup-codex-android-env.sh` can bootstrap/verify Android command-line tools, SDK platform/build tools, CMake, NDK, submodules, and a Gradle smoke test. If native dependencies cannot be fetched, use `static-review` and report `DEGRADED_STATIC_ONLY` instead of claiming Gradle validation.

## Dependency bootstrap profiles

Dependency policy is centralised under `ci/dependencies/` and enforced by
`scripts/bootstrap-dependencies.sh`. The script configures only approved mirrors,
fetches the same pinned gitlink commits, and verifies root and nested submodule
SHAs. Required submodule build inputs must be tracked in the pinned submodule
commits; bootstrap must not leave generated files inside submodule worktrees.

Shared, read-only parsing/validation logic (supported-profile list, manifest TSV parsing, `profile_requires_path`, parent-worktree resolution, gitlink/actual SHA lookup, sentinel checks, classification labels, logging helpers) lives in `scripts/dependency-lib.sh`, which both `bootstrap-dependencies.sh` and `check-submodules.sh` source so the logic cannot drift. `check-submodules.sh` is read-only validation; its only mutating action is `--repair`, which delegates to `bootstrap-dependencies.sh` with the same (validated) profile. `prepare-ci-environment.sh` is a thin wrapper that also delegates to the bootstrap.

All four entrypoints validate the profile (CLI value, bare profile name, and the `DEPENDENCY_BOOTSTRAP_PROFILE` / `CHECK_SUBMODULES_PROFILE` environment defaults) against the supported set. A missing `--profile` value exits `2` with usage; an unsupported profile exits `2` instead of silently validating zero entries.

| Profile         | Intended use                                                     | Failure policy                                                                                                      |
| --------------- | ---------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- |
| `static-review` | Source/script/XML review in agents or restricted local checkouts | May print `DEGRADED_STATIC_ONLY`; do not run or claim Gradle/build/test success unless dependencies are fully ready |
| `jvm-tests`     | Repo JVM/unit-test Gradle tasks                                  | Strict today because Gradle configuration still requires the native/media submodule graph                           |
| `full-build`    | Debug/full CI builds and local release-equivalent validation     | Strict: missing pins, SDK/tooling, or release-blocking submodules fail                                              |
| `release`       | Manual signed release workflow                                   | Strictest: no degraded mode; media, ffmpeg, and taglib must be present at exact pinned SHAs                         |

Common commands:

```sh
bash scripts/bootstrap-dependencies.sh --profile static-review
bash scripts/bootstrap-dependencies.sh --profile jvm-tests
bash scripts/bootstrap-dependencies.sh --profile full-build
bash scripts/bootstrap-dependencies.sh --profile release
```

Approved mirrors are documented in `ci/dependencies/git-url-overrides.tsv`; they are fallback fetch locations only and must never be used to replace a submodule with arbitrary latest HEAD. Repair an existing clone with `bash scripts/bootstrap-dependencies.sh --profile full-build`; use `--profile release` before signing or tagging.

### Gradle version catalogue status

`gradle/libs.versions.toml` is currently a curated dependency **inventory / partial migration**, not the value Gradle consumes at build time. The authoritative versions still live in the root `build.gradle` `buildscript.ext` block and the inline `plugins { ... version "..." }` strings. `scripts/check-version-catalog-sync.sh` (run in CI by the lint workflow's "Workflow/script syntax" job) fails the build if any version duplicated in `build.gradle` drifts from the catalogue, so the inventory stays trustworthy. When you change a duplicated version, update **both** files.

Fully wiring the catalogue (adding a `[libraries]` section and migrating build scripts to type-safe `libs.*` accessors so the catalogue becomes the sole source of truth) is intentionally **out of scope** for dependency-resilience PRs to avoid broad, risky build changes; it is tracked as future work. Likewise, to introduce or refresh Gradle dependency locks / verification metadata, use a fully bootstrapped SDK environment and document the exact `--write-locks` / `--write-verification-metadata` command in that PR. Avoid broad dependency upgrades in bootstrap/resilience PRs.

### Dependency update automation

`.github/dependabot.yml` opens weekly grouped minor/patch PRs for Gradle dependencies and GitHub Actions, and monthly git-submodule PRs. Every update PR runs the same `android.yml` / `lint.yml` CI (canonical bootstrap → build → native → tests), so no automated update bypasses full-build-equivalent validation. Android Gradle Plugin **major** upgrades are deliberately ignored by Dependabot because they require coordinated Kotlin/KSP/Gradle-wrapper migration and must be done in a human-reviewed PR; minor/patch AGP updates are still allowed.

## Key Gradle tasks

| Task                                | Description                                                 |
| ----------------------------------- | ----------------------------------------------------------- |
| `:app:assembleStandardDebug`        | Standard Auxio-TS debug APK                                 |
| `:app:assembleTopwayTwMusicDebug`   | DoFun-compatible debug APK (`com.tw.music.debug`)           |
| `:app:assembleTopwayTwMusicRelease` | Exact DoFun/Topway replacement release APK (`com.tw.music`) |
| `:app:assembleTopwayTwMediaDebug`   | DoFun alternate-entry debug APK (`com.tw.media.debug`)      |
| `:app:assembleTopwayTwMediaRelease` | DoFun alternate-entry release APK (`com.tw.media`)          |
| `:app:testStandardDebugUnitTest`    | Unit tests                                                  |
| `:app:lintStandardDebug`            | Android lint                                                |
| `spotlessCheck`                     | Code formatting check                                       |

## Repository layout

```
app/                          Android app module
  src/main/                   Shared source (all variants)
  src/topwayCompat/           Shared Topway wrapper manifest/code/resources for com.tw.music and com.tw.media
  src/topwayTwMusic/          Topway music release resources
  src/topwayTwMusicDebug/     Topway music debug resources
  src/topwayTwMedia/          Topway media release resources
  src/topwayTwMediaDebug/     Topway media debug resources
musikr/                       Music indexing library
media/                        Media playback submodule
gradle/                       Gradle wrapper
scripts/                      CI/validation scripts
docs/                         Minimal focused documentation
```

## Startup library loading and scans

Auxio-TS startup must expose the existing library, playback queue, MediaSession, and MediaBrowser surfaces before any slow storage scan. Normal launches rebuild the in-memory library from the persisted Musikr cache/database first and skip automatic rescans when a usable or known-empty library state is already recorded.

Filesystem scans are reserved for first install/no-library startup, explicit user refresh actions (**Refresh music** / **Rescan music** in settings or the home retry action), or recovery cases where persisted library data cannot be used. Manual refreshes run in the background and keep the currently visible library/playback surfaces alive until replacement data is ready.

For Topway/DoFun validation, confirm the `AuxioService` creates its MediaSession and MediaBrowser root promptly and that `TopwayMusicBridgeReceiver` commands still route while any scan notification/progress is active. Static/build validation is not a substitute for real TS18 head-unit widget testing.

## Compatibility check scripts

```sh
bash scripts/check-ts18-apk-reference-contracts.sh # TS18 APK reference evidence/contract baseline
bash scripts/check-dofun-topway-compat.sh       # DoFun/Topway source+manifest validation
bash scripts/check-headunit-compat-safety.sh     # Safety guardrails (forbidden hooks, isolation)
```

All three are run by CI on every PR (`android.yml` and/or `lint.yml` guardrail jobs).

## Product flavours

The app has a `distribution` flavour dimension:

- **`standard`** — normal Auxio identity (`org.oxycblt.auxio`)
- **`topwayTwMusic`** — exact DoFun/Topway stock replacement identity (`com.tw.music`)
- **`topwayTwMedia`** — DoFun alternate fixed-entry identity (`com.tw.media`)

The Topway bridge code lives in `app/src/main/java/org/oxycblt/auxio/headunit/topway/` and is shared by all variants. The `topwayCompat` source set adds the thin stock-name wrappers and overlay resources; `topwayTwMusic` and `topwayTwMedia` both reuse that source set while their flavour application IDs supply `com.tw.music` or `com.tw.media`.

## UI development and Roborazzi screenshots

Roborazzi is wired into the Gradle build (`build.gradle` plugin + `app/build.gradle` dependencies).
Test file: `app/src/test/java/org/oxycblt/auxio/ui/RoborazziSmokeScreenshotTest.kt`.

| Gradle task                                                                                    | Description                          |
| ---------------------------------------------------------------------------------------------- | ------------------------------------ |
| `:app:recordRoborazziStandardDebug / :app:recordRoborazziTopwayTwMusicDebug`                   | Capture new PNG baselines            |
| `:app:verifyRoborazziStandardDebug / :app:verifyRoborazziTopwayTwMusicDebug`                   | Verify against committed baselines   |
| `:app:compareRoborazziStandardDebug / :app:compareRoborazziTopwayTwMusicDebug`                 | Produce diff report without failing  |
| `:app:verifyAndRecordRoborazziStandardDebug / :app:verifyAndRecordRoborazziTopwayTwMusicDebug` | Verify then record changed baselines |

Roborazzi uses Robolectric — no emulator or device required. Regular `test*UnitTest` tasks intentionally exclude `RoborazziSmokeScreenshotTest` so the PR unit-test job is not coupled to Roborazzi runtime artifact downloads; use the Roborazzi tasks/workflow for visual coverage.

Use the **UI Screenshots** (`ui-screenshots.yml`) workflow to trigger these tasks manually on any branch/PR and retrieve PNG + HTML report artifacts. Select `variant=standard` for normal Auxio-TS UI, `variant=topway_twmusic` for the exact `com.tw.music` package-identity variant, or `variant=topway_twmedia` for the alternate `com.tw.media` variant. Trigger with `record` first to establish baselines, then use `verify` to detect regressions.

Baseline PNGs live adjacent to the test source (committed to the repo). They are generated at 1280×720 to match TS18/head-unit landscape resolution.

## CI and workflow coverage

| Workflow             | Trigger                                               | Responsibility                                                                                                                      |
| -------------------- | ----------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- |
| `android.yml`        | push/PR to dev, app/build paths                       | Standard + `topwayTwMusic` + `topwayTwMedia` debug/release builds; DoFun compat checks; APK artifacts                               |
| `lint.yml`           | push/PR to dev, app/build/scripts/docs/workflow paths | Workflow YAML syntax; shell script syntax; formatting (spotless); unit tests; Android lint; head-unit safety + DoFun compat scripts |
| `manual-release.yml` | manual dispatch                                       | Signed standard, `topwayTwMusic`, and `topwayTwMedia` release APKs; package identity verification                                   |
| `ui-screenshots.yml` | manual dispatch                                       | Roborazzi UI regression screenshots; PNG + HTML report artifacts                                                                    |

### Branch protection / required checks

If GitHub branch protection is enabled, required status checks should match the current job names:

- `Android Build / build`
- `Android Quality / Workflow/script syntax`
- `Android Quality / Formatting`
- `Android Quality / Unit tests`
- `Android Quality / Android lint`
- `Android Quality / Head-unit safety`

Remove stale required checks for deleted workflows such as `Manual Roborazzi`, `Manual UI Screenshots`, `ts18-guardrails`, or `ts18-validation-tools`; their active coverage is replaced by `ui-screenshots.yml` and the Android Quality guardrail jobs above.

### Why deleted workflows are not retained

| Removed workflow            | Reason                                                                                                                                                                                                        |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `ts18-guardrails.yml`       | Validated deleted research tooling (evidence scripts, scenario maps, fixture packs). The only still-relevant check (`check-headunit-compat-safety.sh`) is covered by the `headunit-safety` job in `lint.yml`. |
| `ts18-validation-tools.yml` | Validated deleted TS18 Python scripts and scenario map JSON. All referenced files are removed. Relevant headunit-safety check covered in `lint.yml`.                                                          |
| `manual-roborazzi.yml`      | **Replaced** by `ui-screenshots.yml`. Functionality preserved and focused on current app UI needs.                                                                                                            |
| `manual-ui-screenshots.yml` | Depended on deleted `scripts/capture-ui-screenshots.sh` and a brittle Android emulator setup. Superseded by the emulator-free Roborazzi approach in `ui-screenshots.yml`.                                     |
