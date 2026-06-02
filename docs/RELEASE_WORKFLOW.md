# Release workflow

## Build commands

```sh
# Standard Auxio-TS
./gradlew :app:assembleStandardRelease

# Topway/DoFun exact com.tw.music variant
./gradlew :app:assembleTopwayTwMusicRelease

# Topway/DoFun alternate com.tw.media variant, once implemented
./gradlew :app:assembleTopwayTwMediaRelease
```

## Release signing

The manual release workflow (`.github/workflows/manual-release.yml`) uses repository secrets:

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

The keystore is decoded to `$RUNNER_TEMP/release.keystore` and never committed.

## Expected release APK assets

Release assets depend on which variants are implemented and intentionally published.

| Asset name | Package ID | Notes |
|-----------|-----------|-------|
| `Auxio-TS-vX.Y.Z-standard-release.apk` | `org.oxycblt.auxio` | Normal Auxio-TS. Does not satisfy fixed DoFun stock music identity. |
| `Auxio-TS-vX.Y.Z-topway-twmusic-release.apk` | `com.tw.music` | Exact DoFun/stock `twmusic` identity. Conflicts with stock system `com.tw.music` unless package state/signing is managed. |
| `Auxio-TS-vX.Y.Z-topway-twmedia-release.apk` | `com.tw.media` | DoFun alternate-entry candidate once implemented. Stock-conflict-aware/root/Shizuku/ADB-oriented; not a universal no-root bypass. |

## Warnings

- **The Topway exact APK installs as `com.tw.music`.** It will conflict with stock `com.tw.music` if not uninstalled/disabled first or if signatures do not match.
- **The exact target TS18 diagnostics show stock `com.tw.music` as a system priv-app** at `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk`.
- **Stock package conflicts:** Before installing a Topway variant on a TS18 head unit, check for existing package state with `adb shell cmd package list packages | grep -E 'com\.tw\.music|com\.tw\.media'` when ADB shell is available.
- **No-root/no-ADB limitation:** A normal TermOne/Termux app shell cannot disable or remove stock system packages.
- **`com.tw.media` is not a magic bypass:** it matches DoFun's alternate fixed entry, but can still conflict if stock `com.tw.media` exists and may not be preferred by every launcher configuration.

## Running a release

1. Open **Actions → Manual Release → Run workflow** from the `dev` branch. The workflow refuses tag refs and non-`dev` branches so version metadata, tag, and release assets are produced from the protected/default line.
2. Provide `version_tag` (e.g. `1.2.3`) or leave blank for auto-increment.
3. Optionally set `draft: true` for validation.
4. The workflow validates signing secrets before building, builds configured variants, runs DoFun/Topway compatibility contract checks against produced outputs, verifies APK signatures, creates a GitHub release, and uploads expected APKs.

## Release note requirements

Every release containing Topway-compatible APKs must state:

- exact package ID of each APK;
- expected DoFun component identity;
- whether stock package removal/disable may be required;
- whether ADB shell, Shizuku, root, system image control, or matching signing is required for the intended install path;
- that real TS18 validation is still required for launcher/widget parity.
