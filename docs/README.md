# Auxio-TS documentation index

Auxio-TS targets TS18/Topway/DoFun Variety. Start here for current product, CI, release and exact-device guidance.

[Evidence confidence: Requires TS18 validation] [Porting decision: Requires TS18 runtime validation]

## Current guidance

- [`DEVELOPMENT.md`](DEVELOPMENT.md) — setup, maintained variants, Gradle tasks and CI architecture.
- [`CI_TASK_POLICY.md`](CI_TASK_POLICY.md) — changed-file classification, consolidated quality/build lanes and optimisation pilot.
- [`RELEASE_WORKFLOW.md`](RELEASE_WORKFLOW.md) — signed `com.tw.media` APK and `com.tw.music` Magisk release flow.
- [`DOFUN_VARIETY_COMPATIBILITY.md`](DOFUN_VARIETY_COMPATIBILITY.md) — launcher/widget compatibility contract and private-hook boundaries.
- [`TS18_APK_REFERENCE.md`](TS18_APK_REFERENCE.md) — APK-derived DoFun and stock `twmusic` evidence.
- [`TS18_INSTALLATION_CONSTRAINTS.md`](TS18_INSTALLATION_CONSTRAINTS.md) — package/signing/root/install constraints.
- [`TS18_RUNTIME_VALIDATION.md`](TS18_RUNTIME_VALIDATION.md) — physical TS18 validation checklist.
- [`CODEX_TS18_DEVICE_CONTEXT.md`](CODEX_TS18_DEVICE_CONTEXT.md) — exact `s9863a1h10` Android 10 device context.
- [`architecture/FAST_INTERACTION_STARTUP.md`](architecture/FAST_INTERACTION_STARTUP.md) — staged readiness and fast resume.
- [`architecture/INCREMENTAL_LIBRARY_PIPELINE.md`](architecture/INCREMENTAL_LIBRARY_PIPELINE.md) — source generations, scanning and enrichment.
- [`architecture/STARTUP_PROFILES_BENCHMARKS.md`](architecture/STARTUP_PROFILES_BENCHMARKS.md) — profile/benchmark evidence model.
- [`validation/EXACT_TS18_STARTUP_VALIDATION.md`](validation/EXACT_TS18_STARTUP_VALIDATION.md) — exact-device startup test procedure.
- [`ts18/ROOT_STORAGE_FASTPATH.md`](ts18/ROOT_STORAGE_FASTPATH.md) — bounded root storage path and rollback.
- [`topway/README.md`](topway/README.md) — local Topway decompile/source-led notes.

## Maintained package contract

The old `standard` distributable is retired.

| Variant | Package | Role |
| --- | --- | --- |
| `topwayTwMedia` | `com.tw.media` | Primary automatic CI and APK release; exposes `com.tw.music.MusicActivity` |
| `topwayTwMusic` | `com.tw.music` | Exact stock-package compatibility; published only as a Magisk module |

DoFun Variety (`com.dofun.variety`) recognises both package/component combinations. This matching evidence does not prove fixed-widget control on every firmware. Private Cardoor/vendor services remain evidence only unless promoted through the formal gap-and-promotion process.

## Exact-device installation warning

The target unit contains stock `com.tw.music` at:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

A normal APK cannot be assumed to replace that platform/UID 1000 package. Root does not provide platform signing. Read `TS18_INSTALLATION_CONSTRAINTS.md` before any installation claim or stock-package mutation.

## CI entry points

- `android.yml` — changed-file scope, both maintained builds and conditional API 29.
- `lint.yml` — syntax/static checks plus one consolidated formatting/test/lint Gradle lane and stable required-check gates.
- `startup-performance.yml` — manual exhaustive two-variant profile/release validation.
- `startup-benchmarks.yml` — manual API 29 macrobenchmark or API 35 Baseline Profile generation, defaulting to `topwayTwMedia`.
- `ui-screenshots.yml` — manual Roborazzi for the two maintained variants.
- `ci-gradle-optimisation-pilot.yml` — manual configuration-cache and parallelism evidence.
- `manual-release.yml` — selected maintained release assets and evidence sidecars.

Local preflight:

```bash
bash scripts/bootstrap-dependencies.sh --profile full-build
bash scripts/ci-scope.sh --self-test
bash scripts/check-ci-variant-contracts.sh
bash scripts/check-startup-performance-contracts.sh
bash scripts/check-ts18-apk-reference-contracts.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-headunit-compat-safety.sh
```

CI/emulator results remain repository evidence, not proof of exact TS18 widget, USB/ACC, MCU/CAN, DSP/radio or physical performance.
