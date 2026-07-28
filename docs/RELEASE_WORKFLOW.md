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
| Topway `twmusic` Magisk module | `Auxio-TS-vX.Y.Z-topway-twmusic-magisk.zip` | Signed `com.tw.music` APK overlaid systemlessly | Requires Magisk, verified stock path, uninstall/rollback media and boot-loop recovery |

The former `org.oxycblt.auxio` standard APK is retired. A raw `com.tw.music` APK is deliberately never published.

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

Package `topwayTwMusic` with `scripts/package-topway-twmusic-magisk-module.sh`; do not publish its APK directly.

## Invocation

Run **Manual Release** from `dev`. Normally create a draft first. Leave `version_tag` blank to auto-increment patch, or provide `vMAJOR.MINOR.PATCH`. Existing releases can append or explicitly replace selected rebuilt assets. Replacement occurs only after new assets are staged and validated.
