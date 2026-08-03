# Release workflow

`.github/workflows/manual-release.yml` is the only maintained APK publication path. It runs manually
from the current `dev` head, uses one repository-wide release lock, builds only the selected maintained
assets, validates them before publication, publishes through a draft-first transaction, and then tries
to fast-forward the released version metadata to `dev`.

The workflow never publishes the retired Standard APK, a raw Auxio `com.tw.music` APK, or the former
exact-package Magisk overlay.

## Simple repository setup

Manual Release uses the repository-provided `GITHUB_TOKEN`. The workflow already declares the narrow
permission it needs:

```yaml
permissions:
  actions: read
  checks: read
  contents: write
```

No extra release PAT, deploy key, `RELEASE_PUSH_TOKEN`, ruleset import, or special bypass actor is
required. Keep using the existing APK-signing secrets:

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD`

The repository's default Actions token setting may remain read-only because this workflow requests its
own `contents: write` permission. An owner or organisation policy can still cap write permissions; if
that applies, GitHub will reject the tag push before a Release is created.

### Ref-protection requirement

The workflow must be able to create an immutable `vX.Y.Z` Git tag. Therefore, do not keep an active
ruleset or legacy tag-protection rule that blocks the workflow token from creating matching `v*` tags.
The simplest supported configuration is **no repository rulesets and no classic protection on `dev`**,
which matches the earlier working release model.

Protection on `dev` is not required by this workflow. When present, it may reject only the final
post-release metadata fast-forward. That rejection does not delete or invalidate a verified GitHub
Release; the workflow summary reports the required follow-up.

Do not invent or configure a `GitHub Actions` bypass actor. The maintained simple configuration does
not depend on any ruleset bypass.

## Audit or reset release settings

The repository includes a bounded operator script. It is read-only unless `--apply` is supplied.
It checks the authenticated owner, signing-secret names, Actions token default, repository rulesets,
and classic protection on `dev`.

From a current repository checkout in Termux:

```bash
bash scripts/reset-manual-release-settings.sh
```

To deliberately restore the simple earlier model by deleting **all repository rulesets** and classic
protection on `dev`:

```bash
bash scripts/reset-manual-release-settings.sh --apply
```

The reset does not modify APK-signing secrets or the default Actions token permission. Every GitHub API
call is limited to 30 seconds, output is verified after mutation, and the script refuses to run as a
GitHub user other than the repository owner.

The script may report that an unused `RELEASE_PUSH_TOKEN` secret still exists. Manual Release does not
use it. Delete that secret under **Settings → Secrets and variables → Actions** only after confirming no
other workflow depends on it.

### Manual reset path

The equivalent GitHub UI steps are:

1. Open **Settings → Rules → Rulesets**.
2. Delete or disable every repository ruleset, especially any branch rule for `dev` or tag rule matching
   `v*`.
3. Open **Settings → Branches**.
4. Remove classic protection applying to `dev` when restoring the old unprotected model.
5. Open **Settings → Actions → General** and leave Workflow permissions at the preferred repository
   default. Manual Release declares `contents: write` itself.
6. Under **Settings → Secrets and variables → Actions**, retain the four existing APK-signing secrets
   listed above. Do not add a separate release-push secret.

**STOP:** do not retry a release while an active `v*` creation rule remains. The APK build can succeed
completely and the run will still fail at the first remote mutation: creation of the immutable tag.

## Invocation modes

The required `release_mode` choice separates creation from repair.

| Mode | Use | Version behaviour |
| --- | --- | --- |
| `create_new_release` | Create a new immutable tag and GitHub Release | `version_tag` may be blank or explicit |
| `repair_existing_release` | Resume or repair assets for an existing immutable tag | `version_tag` is mandatory; no increment or retagging |

Run **Manual Release** from `dev` only.

### New release

A new release performs this transaction:

1. Confirm the checkout is exactly the current remote `dev`.
2. Resolve the target version from source metadata, strict semantic Git tags and every GitHub Release
   returned by the paginated Releases API, including drafts, prereleases and final releases.
3. Create a local source metadata commit only when the checked-in version differs.
4. Build each required variant at most once.
5. Inspect and stage APKs and sidecars, generate the compact release manifest, and upload recovery
   workflow evidence.
6. Push the immutable release tag using `GITHUB_TOKEN`.
7. Create the GitHub Release as a draft regardless of its requested final status.
8. Upload and verify the exact selected asset set.
9. Apply the requested draft/prerelease/final state.
10. Try to fast-forward the same released metadata commit onto `dev`.

The branch update deliberately occurs after remote Release verification. A tag or draft created by an
interrupted run is retained for repair instead of being deleted.

### Existing-release repair

Repair mode requires an explicit strict `vMAJOR.MINOR.PATCH` tag and uses that tag as immutable source
authority.

| Tag | Release | Result |
| --- | --- | --- |
| absent | absent | Not repairable; use new-release mode |
| present | absent | Build selected assets, create a draft Release, verify, then apply requested status |
| present | present | Preserve current status and repair only selected assets |
| absent | present | **STOP**: inconsistent authority |
| present at conflicting source | any | **STOP**: tags are never moved |

When an existing selected APK plus both sidecars are already present and replacement is disabled, that
complete triplet is reused without rebuilding. A partial triplet fails closed because rebuilding only
one member could make the remote evidence inconsistent. Enable replacement to rebuild and replace the
complete selected triplet.

Replacement uses the GitHub CLI clobber operation only after rebuilt files have staged and validated.
Unrelated historical assets are preserved.

Historical tags must contain the release build and validation contracts required for the selected
variant. The current workflow may orchestrate a repair, but it does not silently substitute current
application source or weaken historical package/signing authority.

## Version authority

Only strict `major.minor.patch` values participate. Optional leading `v` is normalised.

Automatic resolution considers:

- checked-in `app/build.gradle` `versionName`;
- all strict semantic Git tags;
- all strict semantic GitHub Release tags from every status, including draft, prerelease and final.

The Releases API is paginated; the workflow does not use the “latest release” endpoint.

Normally the next version is one patch above the highest external tag/Release authority. When source
metadata is already ahead of all external authority and that version has no tag or Release, the source
version itself is reused. This recovers a prior source-only metadata bump without skipping another
version.

The version code is deterministic:

```text
major * 1,000,000 + minor * 10,000 + patch * 100
```

Examples:

- `6.4.7` → `6040700`
- `6.4.8` → `6040800`
- `6.5.0` → `6050000`

The workflow rejects version-code regressions and unexplained collisions. Repair mode reads the exact
version metadata from the immutable tag rather than inventing a new value.

## Maintained install assets

| Selection | Published asset | Package inside | Constraint |
| --- | --- | --- | --- |
| Topway `twmedia` APK | `Auxio-TS-vX.Y.Z-topway-twmedia-release.apk` | `com.tw.media` | Primary DoFun alternate-entry lane; exact TS18 runtime still requires device validation |
| LSPosed API 100 bridge addon | `Auxio-TS-vX.Y.Z-lsposed-api100-bridge.apk` | `org.oxycblt.auxio.ts18bridge` | Keep genuine stock `com.tw.music`; enable only its static/recommended `com.tw.music` scope |

Each selected maintained lane also has a separately labelled debug companion:

- `Auxio-TS-vX.Y.Z-topway-twmedia-debug.apk`
- `Auxio-TS-vX.Y.Z-lsposed-api100-bridge-debug.apk`

The required `debug_variant_destination` choice controls the APK and its `.sha256` and
`.metadata.txt` sidecars:

| Value | Result |
| --- | --- |
| `workflow_artifacts` | Default. Build debug companions once and upload them only as short-lived, 14-day Actions artifacts. |
| `release_assets` | Publish debug companions and sidecars as explicitly selected GitHub Release assets. Complete existing repair triplets may be reused. |

Debug companions use separate debug application IDs and are diagnostic-only. Publishing them does not
make them the normal install or prove TS18 runtime behaviour.

## Efficient validation model

The release critical path performs:

- one Gradle assembly for each required variant;
- one primary SHA-256 calculation per built APK;
- one primary `aapt dump badging` result per built APK;
- one primary `apksigner verify --verbose --print-certs` result per built APK;
- focused app, diagnostics-boundary, startup-profile and LSPosed contract checks;
- one compact JSON release manifest;
- one remote Release asset listing after upload.

The manifest records publication-critical fields only: filename, asset classification, SHA-256,
package ID, version name/code, signer SHA-256, source commit, release tag and destination.

## Recovery and failure handling

Before remote mutation, the workflow uploads a 14-day recovery artifact containing the staged release
assets, asset plan, version plan and compact manifest. Workflow-only debug companions are uploaded in
a separate 14-day artifact.

- Validation failure before tag creation: no remote release state is changed.
- Tag creation rejected: remove the conflicting tag rule, then rerun `create_new_release`; no Release
  exists yet.
- Tag pushed but Release absent: rerun in repair mode with that tag.
- Draft Release with missing assets: repair the same tag; do not generate another version.
- Published Release missing a selected asset: repair with replacement only when necessary.
- Release verified but `dev` metadata push rejected: preserve the Release and reconcile `dev` with the
  immutable release commit before the next release.

Do not delete or move a valid release tag to “retry” publication.

## Local static validation

```bash
python3 -m py_compile scripts/release-orchestrator.py
python3 scripts/release-orchestrator.py self-test
bash -n scripts/check-manual-release-workflow.sh
bash -n scripts/reset-manual-release-settings.sh
bash scripts/check-manual-release-workflow.sh
bash scripts/check-ci-variant-contracts.sh
bash scripts/check-ts18-apk-reference-contracts.sh
```

Release-equivalent APK validation still uses:

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
```

The `topwayTwMusic` flavour remains an internal contract build only. Never publish its APK or recreate
the retired Magisk overlay lane.
