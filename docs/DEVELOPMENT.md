# Development

Canonical setup, build, test and validation guide for the three Auxio-TS application variants.

## Requirements

JDK 21, Android SDK matching the repository Gradle configuration, Bash/Python/Git, Android command-line tools, Ninja/native toolchain for `musikr`, and repository dependency pins.

```bash
bash scripts/bootstrap-dependencies.sh --profile full-build
```

## Application variants

| Variant | Application ID | Topway compatibility |
| --- | --- | --- |
| `standard` | `org.oxycblt.auxio` | Off |
| `topwayTwMedia` | `com.tw.media` | On |
| `topwayTwMusic` | `com.tw.music` | On; internal exact-package build only |

Build explicit lanes:

```bash
bash scripts/ci-gradle.sh \
  :app:assembleStandardDebug \
  :app:assembleTopwayTwMediaDebug \
  :app:assembleTopwayTwMusicDebug

bash scripts/ci-gradle.sh \
  :app:assembleStandardRelease \
  :app:assembleTopwayTwMediaRelease \
  :app:assembleTopwayTwMusicRelease
```

Outputs are under `app/build/outputs/apk/<variant>/<buildType>/`. A locally built release APK is not a publishable release unless the Manual Release signing and asset contracts have completed.

The raw `topwayTwMusic` APK is an internal build input. Never distribute it directly. The public exact-`com.tw.music` lane is the systemless Magisk ZIP created by `scripts/package-topway-twmusic-magisk-module.sh`.

## Test and lint

```bash
bash scripts/ci-gradle.sh --continue \
  spotlessKotlinCheck \
  :app:testStandardDebugUnitTest \
  :app:testTopwayTwMediaDebugUnitTest \
  :app:testTopwayTwMusicDebugUnitTest \
  :musikr:testDebugUnitTest \
  :app:lintStandardDebug \
  :app:lintTopwayTwMediaDebug \
  :app:lintTopwayTwMusicDebug
```

Android 10/API 29 instrumentation must cover all lanes:

```bash
bash scripts/ci-gradle.sh \
  :app:connectedStandardDebugAndroidTest \
  :app:connectedTopwayTwMediaDebugAndroidTest \
  :app:connectedTopwayTwMusicDebugAndroidTest \
  :musikr:connectedDebugAndroidTest
```

Roborazzi and `startup-benchmark` are distribution-aware. Use the workflow inputs or flavour-qualified Gradle tasks rather than unflavoured aliases.

## Repository contracts

```bash
bash scripts/ci-scope.sh --self-test
bash scripts/check-product-contracts.sh
bash scripts/check-topway-manifest-components.sh
python3 scripts/check-documentation-policy.py
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-startup-performance-contracts.sh
bash scripts/check-manual-release-workflow.sh
bash scripts/check-lsposed-bridge-contracts.sh
```

`check-product-contracts.sh` enforces identity/source-set/component isolation, API-29 workflow coverage, the systemless Magisk-only exact-package publication boundary and LSPosed separation. Do not weaken it to accommodate an accidental leak.

## Runtime authority

All variants use the same single player, playback service, canonical queue/PlaybackStateManager, MediaSession, notification path and audio-focus owner. Variant work changes package/component integration only unless an explicit architecture change says otherwise.

Topway component compatibility does not confer platform signing, UID 1000/shared UID, signature permissions or private-vendor authority. Magisk provides a systemless filesystem overlay only.

## CI and evidence

CI Gradle calls use `scripts/ci-gradle.sh` with explicit flavour-qualified tasks. Repository workflow success proves only that exact head and environment. Hosted API 29/API 35 results do not prove physical TS18 launcher, fixed-widget, USB, ACC, Magisk boot/rollback, MCU/CAN, DSP/radio or audible behaviour.

Use [physical validation](TS18_RUNTIME_VALIDATION.md) and label unexecuted hardware work **Requires TS18 validation**.
