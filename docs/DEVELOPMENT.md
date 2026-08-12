# Development

This document is the canonical setup, build, test and validation guide for the one maintained Auxio-TS app.

## Requirements

- JDK 21;
- Android SDK matching repository Gradle configuration;
- Bash, Python 3, Git and the Android command-line tools;
- Ninja and the native toolchain when building `musikr`/TagLib;
- repository dependency pins prepared by the bootstrap script.

Prepare the repository:

```bash
bash scripts/bootstrap-dependencies.sh --profile full-build
```

Use `--profile static-review` only when no Gradle execution is required. Do not substitute unverified dependency heads for pinned revisions.

## Build the app

```bash
bash scripts/ci-gradle.sh :app:assembleDebug
bash scripts/ci-gradle.sh :app:assembleRelease
```

The unsigned/local release task validates compilation and packaging but is not a publishable signed release. Normal output paths are:

```text
app/build/outputs/apk/debug/
app/build/outputs/apk/release/
```

There is no distribution flavour dimension. Do not use `Standard`, `TopwayTwMedia` or `TopwayTwMusic` task names.

## Test and lint

```bash
bash scripts/ci-gradle.sh --continue \
  spotlessKotlinCheck \
  :app:testDebugUnitTest \
  :musikr:testDebugUnitTest \
  :app:lintDebug
```

API 29 instrumentation:

```bash
bash scripts/ci-gradle.sh \
  :app:connectedDebugAndroidTest \
  :musikr:connectedDebugAndroidTest
```

Roborazzi tasks target the single debug app, for example `:app:recordRoborazziDebug`. Startup validation targets the single app through `:startup-benchmark:assembleBenchmarkBenchmark` and the managed-device tasks resolved by the Startup Benchmarks workflow.

## Repository contracts

Run the narrow checks relevant to the change:

```bash
bash scripts/ci-scope.sh --self-test
bash scripts/check-product-contracts.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-startup-performance-contracts.sh
bash scripts/check-manual-release-workflow.sh
bash scripts/check-lsposed-bridge-contracts.sh
```

`check-product-contracts.sh` is the single-product guardrail. It rejects retired app flavours/tasks and publication paths, verifies `com.tw.media`, the stock-compatible component, unflavoured benchmark/CI tasks and the separate optional LSPosed posture.

## CI policy

`scripts/ci-scope.sh` classifies changed files and fails open to broader validation when the comparison range or ownership is uncertain. Keep the stable required check names where practical:

- `Android Build / build`
- `Android Quality / Workflow/script syntax`
- `Android Quality / Formatting`
- `Android Quality / Unit tests`
- `Android Quality / Android lint`
- `Android Quality / Head-unit safety`

CI Gradle calls use `scripts/ci-gradle.sh` with explicit tasks. Automatic execution is sequential and one-worker by default. Do not replace scoped tasks with broad `build`, `check`, `test` or `lint` aggregates without a policy change.

## Evidence and physical validation

Record the exact command and head for every claimed result. Hosted API 29 and API 35 evidence does not prove physical TS18 behaviour. Use [physical validation](TS18_RUNTIME_VALIDATION.md) for launcher, fixed-widget, USB, ACC, MCU/CAN, DSP or radio claims.
