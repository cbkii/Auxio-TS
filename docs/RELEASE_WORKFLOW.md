# Release workflow

Canonical Auxio-TS publication policy. Release automation does not create runtime authority; it packages already-approved variants.

## Public asset matrix

| Artefact | Default | Requirement |
| --- | --- | --- |
| Standard APK (`org.oxycblt.auxio`) | Included | Signed/validated normal Android lane. |
| `topwayTwMedia` APK (`com.tw.media`) | Included | Signed/validated installable TS18 lane. |
| exact-`com.tw.music` Magisk ZIP | Excluded | Explicit opt-in only where exact package identity is required; systemless overlay containing the internally built `topwayTwMusic` APK. |
| raw `topwayTwMusic` APK | **Forbidden** | Internal build input only; no workflow input, upload name or public asset path may expose it. |
| LSPosed Track-C APK | Excluded | Optional separately signed add-on, statically scoped to genuine stock `com.tw.music`. |
| Debug APKs | Workflow artefacts | Publication requires explicit diagnostic opt-in. No raw `topwayTwMusic` debug asset exists. |

All public binary assets carry SHA-256 and metadata sidecars. Release APKs are checked for package/version/SDK/ABI/signer contracts. The Magisk ZIP metadata records the embedded `com.tw.music` APK signer and the exact observed systemless target.

## Manual Release

`.github/workflows/manual-release.yml` is workflow-dispatch only from current `dev`. It:

- selects Standard and `topwayTwMedia` by default;
- requires explicit opt-in for exact-package Magisk and Track C;
- leaves debug publication off by default;
- stages/signs/inspects complete asset/checksum/metadata triplets;
- supports bounded repair/resume of existing **draft** transactions;
- automatically resumes unfinished current-dev tag/draft state only within the established release-authority rules;
- refuses unsafe historical inference; explicit existing tags are required for historical repair;
- never mutates assets on an already-published release; binary corrections require a new patch release.

Release build tasks are flavour-qualified:

- `:app:assembleStandardRelease`;
- `:app:assembleTopwayTwMediaRelease`;
- internal `:app:assembleTopwayTwMusicRelease` only when the Magisk logical asset is requested;
- `:lsposed-bridge:assembleRelease` only when Track C is selected.

The internally built exact-package APK is inspected as `com.tw.music`, then passed directly to `scripts/package-topway-twmusic-magisk-module.sh`. It is never staged as an upload candidate.

## Exact-package systemless safety

The only approved exact-package public artifact is the Magisk ZIP targeting the observed path `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk`.

The packager/module must:

- fail closed if that exact stock path is absent;
- leave the protected stock APK untouched on disk;
- perform no direct `/system`, `/product` or `/vendor` writes;
- contain no destructive disable/uninstall/delete/rename logic;
- roll back by disabling/removing the module and rebooting;
- state explicitly that package identity does not confer platform signing, UID 1000/shared UID, signature permissions or private-vendor authority.

## Playback/runtime boundary

Standard, both Topway variants, the Magisk packaging lane and Track C do not create alternative playback authorities. Every Auxio application variant retains one player, playback service, canonical queue/PlaybackStateManager, MediaSession, notification and audio-focus owner.

Track C remains optional/fail-open and independently removable. It is not a substitute for exact-package identity and does not expand Auxio privileges.

## Validation

Before release-authority changes run:

```bash
bash scripts/check-product-contracts.sh
bash scripts/check-topway-manifest-components.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-manual-release-workflow.sh
python3 scripts/ci/test_manual_release_matrix.py
python3 scripts/ci/test_release_readiness_hardening.py
bash scripts/check-lsposed-bridge-contracts.sh
```

`Manual Release Smoke` performs the network-free signed packaging path with an ephemeral key and must exercise Standard, `topwayTwMedia`, the internal `com.tw.music` -> Magisk ZIP path, and optional Track C without exposing the raw exact-package APK.

## Evidence and rollback

CI/release smoke proves repository/package contracts only. Exact TS18 Magisk install, boot overlay, package resolution, DoFun behaviour, ACC persistence and disable/remove rollback remain **Requires TS18 validation**.

Standard/`topwayTwMedia` rollback uses a previously verified compatible signed APK. Exact-package rollback disables/removes the Magisk module then reboots to expose the untouched stock package. Track C rolls back independently by disabling/removing the add-on and rebooting.
