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
