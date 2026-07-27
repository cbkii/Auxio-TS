# Auxio-TS CI task policy

## Maintained variants

`standard` is retired. Automatic CI and releases support:

- `topwayTwMedia` (`com.tw.media`) — primary build, JVM test, lint, API 29, benchmark, screenshot and APK release authority.
- `topwayTwMusic` (`com.tw.music`) — exact-package compatibility build and Magisk packaging authority.

`scripts/check-ci-variant-contracts.sh` fails if active build or workflow configuration restores a Standard task/input/asset.

## Automatic workflow shape

| Lane | Triggered work |
| --- | --- |
| CI scope | Every PR/push/manual run; classifies changed files and fails open to full CI when uncertain |
| Workflow/script syntax | YAML, shell/Python syntax, release and variant contracts |
| Consolidated Gradle quality | Kotlin formatting, `topwayTwMedia` JVM tests, Musikr tests and `topwayTwMedia` lint in one setup/invocation |
| Topway build | Both maintained debug APKs in one Gradle invocation; release APKs only on push/manual runs |
| API 29 smoke | High-risk runtime changes, all `dev` integration pushes, `ci:full`, manual full CI |
| Head-unit safety | TS18/DoFun/Topway source and package contract guardrails |

The small `Formatting`, `Unit tests` and `Android lint` gate jobs preserve existing branch-protection check names while the expensive Gradle work runs once.

## Changed-file authority

`scripts/ci-scope.sh` publishes scope flags. Workflow/build-system changes run full CI. App/service/storage/startup/manifest/database changes include API 29. Documentation-only changes remain static. An unavailable diff range runs full maintained validation rather than guessing.

Use the `ci:full` PR label for an explicit full run.

## Gradle invocation policy

CI uses `scripts/ci-gradle.sh` with explicit tasks. Routine examples:

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

The wrapper defaults to no-daemon, sequential execution and build cache. Configuration cache and parallel execution remain opt-in:

```bash
AUXIO_TS_CI_CONFIGURATION_CACHE=1 bash ./scripts/ci-gradle.sh <tasks...>
AUXIO_TS_CI_GRADLE_PARALLEL=1 bash ./scripts/ci-gradle.sh <tasks...>
```

Use the manual **Gradle Optimisation Pilot** workflow for evidence. Do not enable either mode automatically until the exact task set is proven race-free and configuration-cache compatible.

## Checkout policy

Build/test jobs use shallow checkout (`fetch-depth: 1`) and let the repository bootstrap initialise exact current gitlinks. The scope job fetches only the comparison boundary. Release workflows keep full history and tags.

## Evidence boundary

CI and emulators prove repository contracts only. They do not prove fixed DoFun widget behaviour, USB/ACC lifecycle, MCU/CAN, DSP/radio or exact TS18 performance.
