# Auxio-TS CI task policy

## Maintained variants

`standard` is retired. Automatic CI and releases support:

- `topwayTwMedia` (`com.tw.media`) — primary build, JVM test, lint, API 29, benchmark, screenshot and APK release authority.
- `topwayTwMusic` (`com.tw.music`) — exact-package compatibility build and Magisk packaging authority.

`scripts/check-ci-variant-contracts.sh` fails if active build or workflow configuration restores a Standard task, input or asset.

## Automatic workflow shape

| Lane | Triggered work |
| --- | --- |
| CI scope | Every PR/push/manual run; classifies changed files and fails open to full CI when uncertain |
| Workflow/script syntax | YAML, shell/Python syntax, release, startup and variant contracts |
| Consolidated app quality | Selected Kotlin formatting, app tests, Musikr tests and `topwayTwMedia` lint in one setup/invocation |
| C/C++ formatting | Selected only for native/full scope; reuses the quality setup and has one bounded retry for transient Eclipse P2 provisioning |
| Topway build | Only selected maintained debug APKs, grouped into one Gradle invocation when both are required |
| API 29 smoke | High-risk runtime changes, all `dev` integration pushes, `ci:full`, manual full CI |
| Head-unit safety | TS18/DoFun/Topway source and package contract guardrails |

The small `Formatting`, `Unit tests` and `Android lint` gate jobs preserve existing branch-protection check names while selected expensive Gradle work runs once.

## Changed-file authority

`scripts/ci-scope.sh` publishes explicit scope flags and consumes them at job/task level:

| Change authority | Automatic evidence |
| --- | --- |
| Workflow, documentation or ordinary shell contracts | Static YAML/syntax/contract checks only |
| Ordinary app Kotlin/Java | Formatting, app JVM tests, app lint and the primary `topwayTwMedia` compile |
| Musikr implementation/tests | Musikr JVM tests plus one `topwayTwMedia` integration compile |
| `topwayTwMedia` resources/package surface | Primary variant build and selected compatibility checks |
| `topwayTwMusic` resources/package surface | Exact-package variant build and selected compatibility checks |
| Shared `topwayCompat` code/manifest/resources | Both maintained APKs and compatibility contracts |
| Database, service, provider, receiver, manifest, startup or storage authority | Primary quality/build lanes plus API 29 instrumentation |
| Native C/C++ | Native formatter and relevant primary compile/test evidence |
| Gradle/dependency authority or unknown path | Full maintained validation |
| Push to `dev` | API 29 retained even for a narrow change |

Use the `ci:full` PR label or manual dispatch to request every maintained lane. An unavailable comparison range and any unclassified path fail open to full validation rather than guessing.

## Gradle invocation policy

CI uses `scripts/ci-gradle.sh` with explicit tasks. Full-maintained examples:

```bash
bash ./scripts/ci-gradle.sh --continue \
  spotlessKotlinCheck \
  :app:testTopwayTwMediaDebugUnitTest \
  :musikr:testDebugUnitTest \
  :app:lintTopwayTwMediaDebug

bash ./scripts/ci-gradle.sh \
  :app:assembleTopwayTwMediaDebug \
  :app:assembleTopwayTwMusicDebug
```

Avoid generic `build`, `check`, `test` and `lint` as PR proof.

The wrapper defaults to no-daemon, build cache, sequential execution and **one Gradle worker**. The one-worker limit prevents the two large Kotlin/KSP/Hilt variant compilations from exhausting the 1 GiB Kotlin daemon when both APKs share an invocation. A caller may set `AUXIO_TS_CI_GRADLE_MAX_WORKERS` explicitly for controlled evidence gathering.

The Eclipse CDT formatter uses external P2 repositories. It runs separately only when selected, with at most two attempts and preserved attempt logs. This retry is for transient repository timeouts; a deterministic formatting violation still fails the required Formatting check.

Configuration cache and parallel execution remain opt-in:

```bash
AUXIO_TS_CI_CONFIGURATION_CACHE=1 bash ./scripts/ci-gradle.sh <tasks...>
AUXIO_TS_CI_GRADLE_PARALLEL=1 bash ./scripts/ci-gradle.sh <tasks...>
```

Use the manual **Gradle Optimisation Pilot** workflow for evidence. Do not enable either mode automatically until the exact task set is proven race-free and configuration-cache compatible.

## Checkout policy

Build/test jobs use shallow checkout (`fetch-depth: 1`) and let the repository bootstrap initialise exact current gitlinks. The scope job fetches only the comparison boundary. Release workflows keep full history and tags.

## Evidence boundary

CI and emulators prove repository contracts only. They do not prove fixed DoFun widget behaviour, USB/ACC lifecycle, MCU/CAN, DSP/radio or exact TS18 performance.
