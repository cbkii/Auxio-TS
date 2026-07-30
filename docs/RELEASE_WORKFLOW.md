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

The former `org.oxycblt.auxio` standard APK and exact-package Auxio Magisk overlay are retired. A
raw `com.tw.music` Auxio APK is deliberately never published.

Each install asset includes `.sha256` and `.metadata.txt` sidecars recording source commit, tag, package/version/SDK/ABI and `apksigner` certificate evidence.

## Local validation

```bash
bash ./scripts/bootstrap-dependencies.sh --profile release
bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMediaRelease
bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMusicRelease
bash ./scripts/check-startup-performance-contracts.sh path/to/release.apk
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

Run **Manual Release** from `dev`. Normally create a draft first. Leave `version_tag` blank to auto-increment patch, or provide `vMAJOR.MINOR.PATCH`. Existing releases can append or explicitly replace selected rebuilt assets. Replacement occurs only after new assets are staged and validated.
