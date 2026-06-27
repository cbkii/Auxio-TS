# Auxio-TS CI task policy

Automatic CI is intentionally scoped. Do not replace scoped Gradle invocations with generic aggregate tasks unless a later PR proves the aggregate task has the same coverage and failure policy.

## Current automatic CI shape

- Android Build: explicit debug APK variants; release variants only outside pull requests.
- Android Quality / Formatting: `spotlessCheck`.
- Android Quality / Unit tests: explicit `:app` and `:musikr` unit-test tasks.
- Android Quality / Android lint: explicit app lint variant.
- Head-unit safety: repo-owned shell contract checks.

## Docs exclusion

`docs/**` is excluded from automatic CI triggers. A documentation-only PR should not spend Android CI minutes. A PR that changes both docs and code still triggers CI through the code/workflow/script paths.

Keep `README.md` and `AGENTS.md` in automatic path filters because they affect contributor/agent behaviour and repo-level validation expectations.

## Gradle invocation policy

CI calls Gradle through `scripts/ci-gradle.sh`. The wrapper may centralise stable execution flags and heartbeat output, but workflow files remain the source of truth for task scope.

The wrapper currently adds:

- `--no-daemon`
- `--stacktrace`
- `--console=plain`
- `--build-cache`
- `--parallel`
- `--warning-mode summary` when a workflow did not intentionally pass another warning mode

Configuration cache remains opt-in via `AUXIO_TS_CI_CONFIGURATION_CACHE=1` until a dedicated compatibility pass proves every scoped workflow task is safe.

## Local validation examples

Use scoped tasks:

```bash
bash ./scripts/bootstrap-dependencies.sh --profile full-build
bash ./scripts/ci-gradle.sh :app:assembleStandardDebug :app:assembleTopwayTwMusicDebug :app:assembleTopwayTwMediaDebug
bash ./scripts/ci-gradle.sh :app:testStandardDebugUnitTest :musikr:testDebugUnitTest
bash ./scripts/ci-gradle.sh :app:lintStandardDebug
bash ./scripts/ci-gradle.sh spotlessCheck
```

Avoid generic aggregate tasks for PR proof:

```bash
./gradlew check
./gradlew build
./gradlew test
./gradlew lint
```

Those may be useful during investigation, but they are not the Auxio-TS PR CI contract unless the workflows are intentionally changed to use them.
