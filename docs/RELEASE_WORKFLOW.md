# Release workflow

The maintained manual release workflow is `.github/workflows/manual-release.yml`. It runs only from the current `dev` branch, builds only the selected variants, signs them with repository secrets, validates the resulting packages, records checksums and signing/package metadata, and creates or updates a GitHub release.

## Required repository secrets

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

The workflow fails closed when any signing secret is missing. Checkout credentials are not persisted while repository-controlled build scripts run; the GitHub token is exposed to Git only in the final publication or rollback step.

## Selectable install assets

| Selection | Published install asset | Package inside | Intended lane | Installation constraint |
| --- | --- | --- | --- | --- |
| Standard APK | `Auxio-TS-vX.Y.Z-standard-release.apk` | `org.oxycblt.auxio` | Standalone Auxio-TS and comparison testing | Does not replace a stock TS18 music package |
| Topway `twmedia` APK | `Auxio-TS-vX.Y.Z-topway-twmedia-release.apk` | `com.tw.media` | DoFun alternate fixed entry exposing `com.tw.music.MusicActivity` | Install/update succeeds only when package state and signing authority permit it |
| Topway `twmusic` Magisk module | `Auxio-TS-vX.Y.Z-topway-twmusic-magisk.zip` | signed `com.tw.music` APK overlaid systemlessly | Exact stock-package replacement test lane | Requires Magisk, verified stock path, uninstall/rollback media and boot-loop recovery; a raw `com.tw.music` APK is deliberately not published |

Every install asset is accompanied by:

- `<asset>.sha256` — SHA-256 digest using the published filename;
- `<asset>.metadata.txt` — source commit, release tag, application ID, version, min/target SDK, ABI set and `apksigner` certificate output.

Release APKs are also checked for compiled Baseline Profile data before publication. The workflow runs the TS18 APK-reference and DoFun/Topway compatibility checks after building.

## Manual local validation

Prepare the exact pinned dependencies before any Gradle task:

```sh
bash ./scripts/bootstrap-dependencies.sh --profile release
```

Build the desired variant with release-signing Gradle properties supplied through a secure environment:

```sh
bash ./scripts/ci-gradle.sh :app:assembleStandardRelease
bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMediaRelease
bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMusicRelease
```

Then run the repository gates against each APK:

```sh
bash scripts/check-startup-performance-contracts.sh path/to/release.apk
bash scripts/check-ts18-apk-reference-contracts.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-headunit-compat-safety.sh
```

For `topwayTwMusic`, package the signed APK with the maintained helper rather than publishing the APK directly:

```sh
bash scripts/package-topway-twmusic-magisk-module.sh \
  --apk path/to/topwayTwMusic-release.apk \
  --output Auxio-TS-vX.Y.Z-topway-twmusic-magisk.zip \
  --version X.Y.Z \
  --version-code N
```

## Release invocation

In GitHub Actions, run **Manual Release** from `dev` and normally leave **Create release as draft** enabled for the first validation build. Select only the assets required for the current test lane. The workflow auto-increments the patch tag when `version_tag` is blank, or accepts an explicit `vMAJOR.MINOR.PATCH` value.

Do not publish either Topway-compatible lane as universally installable. Root, package name, signing certificate, privileged placement and launcher recognition are separate authorities. See `docs/TS18_INSTALLATION_CONSTRAINTS.md` and `docs/validation/EXACT_TS18_STARTUP_VALIDATION.md` before installing.
