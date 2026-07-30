# Release workflow

`.github/workflows/manual-release.yml` runs only from current `dev`, builds only selected maintained variants, signs them, validates packages and profile data, writes evidence sidecars, then creates or updates a GitHub release.

## Required secrets

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

The workflow fails closed when signing material is missing. Checkout credentials are not persisted while repository scripts run.

## Maintained install assets

| Selection | Published asset | Package inside | Constraint |
| --- | --- | --- | --- |
| Topway `twmedia` APK | `Auxio-TS-vX.Y.Z-topway-twmedia-release.apk` | `com.tw.media` | Primary DoFun alternate-entry lane; package state/signing and TS18 runtime still require validation |
| LSPosed API 100 bridge addon | `Auxio-TS-vX.Y.Z-lsposed-api100-bridge.apk` | `org.oxycblt.auxio.ts18bridge` | Keep genuine stock `com.tw.music`; enable only its static/recommended `com.tw.music` scope |

Each selected maintained lane also builds its separately labelled debug companion:

- `Auxio-TS-vX.Y.Z-topway-twmedia-debug.apk`
- `Auxio-TS-vX.Y.Z-lsposed-api100-bridge-debug.apk`

The required `debug_variant_destination` choice controls where these debug APKs and their `.sha256`
and `.metadata.txt` sidecars are placed:

| Value | Result |
| --- | --- |
| `workflow_artifacts` | Default. Upload debug companions only as short-lived, 14-day GitHub Actions artifacts. They are not added to the GitHub Release. |
| `release_assets` | Publish the debug companions and sidecars as explicitly selected GitHub Release assets. They are not also uploaded as a separate debug workflow artifact. |

Debug companions are signed with the Android debug key, use separate debug application IDs, and are
for diagnostics and validation only. Publishing them does not make them the recommended install,
replace the signed release pair, or prove release-runtime behaviour on the TS18.

The former `org.oxycblt.auxio` standard APK and exact-package Auxio Magisk overlay are retired. A
raw `com.tw.music` Auxio APK is deliberately never published.

Each install asset includes `.sha256` and `.metadata.txt` sidecars recording source commit, tag,
package/version/SDK/ABI and `apksigner` certificate evidence. Before publication, the primary APK
is checked for the exact `com.tw.media` identity, release version, SDK 24/36 contract, packaged ABI
set, configured release signer and verifying SHA-256 sidecar.
The release APK additionally passes a DEX-string boundary check proving that the diagnostic
journal, bundle exporter, canary and temporary validation lab are absent.

## Local validation

```bash
bash ./scripts/bootstrap-dependencies.sh --profile release
bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMediaRelease
bash ./scripts/check-startup-performance-contracts.sh path/to/release.apk
bash ./scripts/check-release-diagnostics-boundary.sh path/to/release.apk
bash ./scripts/check-app-release-contracts.sh \
  --apk path/to/release.apk \
  --version-name X.Y.Z \
  --version-code N \
  --expected-signer SHA256 \
  --sha256-file path/to/release.apk.sha256 \
  --metadata-file path/to/release.apk.metadata.txt
bash ./scripts/check-ci-variant-contracts.sh
bash ./scripts/check-ts18-apk-reference-contracts.sh
bash ./scripts/check-dofun-topway-compat.sh
bash ./scripts/check-headunit-compat-safety.sh
```

Build and validate the LSPosed addon with:

```bash
bash ./scripts/ci-gradle.sh :lsposed-bridge:assembleRelease
bash ./scripts/check-lsposed-bridge-contracts.sh \
  --variant release \
  --apk lsposed-bridge/build/outputs/apk/release/lsposed-bridge-release.apk
```

Release signing properties are mandatory for a publishable bridge APK. `topwayTwMusic` remains an
internal contract build only; do not publish its APK or package it as a Magisk overlay.

## Invocation

Run **Manual Release** from `dev`. Normally create a draft first. Leave `version_tag` blank to
auto-increment patch, or provide `vMAJOR.MINOR.PATCH`. Select `workflow_artifacts` to keep debug
companions in the workflow only, or explicitly select `release_assets` to add them to the GitHub
Release. Existing releases can append or explicitly replace selected rebuilt assets. Replacement
occurs only after new assets are staged and validated.
