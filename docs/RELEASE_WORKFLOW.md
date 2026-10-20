# Release workflow

This document is the canonical Auxio-TS release policy. A release action does not change product architecture.

## Publishable artefacts

| Artefact | Default | Requirement |
| --- | --- | --- |
| Signed Auxio-TS APK (`com.tw.media`) | Included | Primary product, signed and validated against version, SDK, ABI, signer and SHA-256 contracts. |
| Signed LSPosed Track-C APK | Excluded | Optional explicit opt-in, independently validated and paired to the signed Auxio target. |
| Debug APKs | Workflow artefacts | Release publication is diagnostic-only and requires explicit selection. |

Never publish a raw Auxio APK with application ID `com.tw.music`, a `topwayTwMusic` artefact or a Magisk protected-package replacement overlay.

## Manual Release

`.github/workflows/manual-release.yml` is workflow-dispatch only and operates from current `dev`. Its form:

- includes the maintained app by default;
- leaves the optional LSPosed bridge off by default;
- leaves debug APK publication off by default;
- stages, signs, inspects and verifies complete APK/checksum/metadata triplets before publication;
- supports bounded repair/resume of an existing **draft** release transaction;
- automatically resumes an unfinished tag/draft only when its immutable tag is current `dev` or the single release-metadata child of current `dev`;
- refuses to choose a materially older orphan tag automatically; historical repair requires explicitly selecting that existing tag;
- never adds, deletes or replaces assets on an already-published release. A published binary correction requires a new patch release.

The app is built with `:app:assembleRelease`; Track C, when explicitly selected, is built separately with `:lsposed-bridge:assembleRelease`. Release scripts use `app`/`app_debug` identifiers, not former flavour names.

GitHub's repository-level immutable-release setting is an additional administrative hardening option. Enabling or changing it is separate from this source-controlled workflow and requires an explicit repository-settings decision.

## Rolling `000 Testing Only Version` draft (independent)

The separate **Refresh Testing APK Draft** workflow builds only the maintained
`com.tw.media` Auxio application. It does **not** replace or modify the
production **Manual Release** workflow, versioned releases, immutable tags,
app signing policy or optional LSPosed Track-C publication.

From **Actions → Refresh Testing APK Draft**, run the workflow from `dev`
and enter an exact **PR number**, repository branch, tag or source SHA.
Alternatively add the `testing-apk` label, include `/testing-apk` in a
PR commit subject or comment `/testing-apk` on a same-repository PR.
Other CI runs do not automatically publish snapshots. An explicit request
builds the immutable selected source and hands off three files:
`auxio-release.apk`, `auxio-debug.apk` and `BUILD_INFO.txt`.

The rolling publisher signs the installable Auxio TESTING APK with the
**same configured repository release signer** as normal `com.tw.media`.
A throwaway signer is used only to package the unprivileged intermediate
handoff; it is never an install identity. TESTING uses the deliberately
fixed versionName `0.0.0-TS18-TESTING` and versionCode `2100000000`.
This maximum-version lane can upgrade an existing lower-version Auxio
install **only when its signing certificate matches**, and TESTING builds
can update one another. Returning to an ordinary, lower-version production
APK requires an approved downgrade/rollback procedure; normal in-place
installation will reject the lower versionCode. Preserve validated backups
and on-device app data before moving between production and TESTING lanes.

The managed draft has title `000 Testing Only Version`. It retains the
newest **two complete** groups. Each group contains a release-signed
TESTING APK, a separate DEBUG APK, `BUILD_INFO-*.txt`,
`SIGNER-*.txt` and `SHA256SUMS-*.txt`. The metadata TXT is uploaded
last as the group-complete marker. Each new publication reconciles the
marked release-note section from the retained, uploaded TXT/checksum
assets, preserves outside prose after anchor migration, checks the
read-back body and linked assets, and fails on inconsistencies.

**Reconcile TESTING Release Notes** is a separate, manual, default-branch
metadata-only repair workflow. It does not rebuild, resign, delete, promote,
replace or upload APKs. Both operations serialise publication through the
same release concurrency group.

The TESTING lane is engineering-only and cannot be used as an alternative
production-release policy. Repository CI and APK inspection do not prove
physical TS18 playback, USB, DoFun, HOME or ACC behaviour.

## Safety boundary

Manual Release must not:

- publish or reconstruct the retired exact-package Auxio application;
- publish a Magisk stock-replacement lane;
- mutate the genuine stock package;
- infer platform signing or UID 1000 from root;
- silently include the optional bridge;
- publish incomplete sidecar sets or unverified replacement assets;
- silently publish stale source because an older orphan tag exists;
- mutate assets belonging to a release that is no longer a draft.

The Track-C static scope remains exactly `com.tw.music`. The add-on must fail open to genuine stock behaviour and remain independently removable.

## Validation

Before release-authority changes:

```bash
bash scripts/check-product-contracts.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-manual-release-workflow.sh
python3 scripts/ci/test_manual_release_matrix.py
python3 scripts/ci/test_release_readiness_hardening.py
bash scripts/check-lsposed-bridge-contracts.sh
```

Signed builds additionally require the configured keystore, package/version/SDK/ABI inspection, signer verification and generated SHA-256/metadata sidecars. Do not publish, tag, promote or modify an existing release from an unreviewed development task.

## Rollback

The app rolls back by reinstalling a previously verified signed Auxio-TS release compatible with its data schema. Track C rolls back independently by disabling/removing the LSPosed module and rebooting, leaving genuine stock behaviour intact. Exact-device recovery must follow the device runbook and preserve known-good installation media.
