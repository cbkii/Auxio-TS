# Auxio-TS repository engineering authority

Read this file before repository work. Explicit user requirements and safety constraints have highest authority; conflicting lower-level documents must be updated rather than silently followed.

## Product matrix

Auxio-TS maintains three strictly separated application variants:

- `standard`: normal Auxio identity `org.oxycblt.auxio`; `TOPWAY_COMPAT_ENABLED=false`; no Topway/DoFun-only components or runtime policy.
- `topwayTwMedia`: installable TS18 lane, application ID `com.tw.media`; contains bounded Topway/DoFun compatibility adapters.
- `topwayTwMusic`: internal exact-package build, application ID `com.tw.music`; never publish or recommend its raw APK.

`musikr` is an internal library. `startup-benchmark` mirrors the distribution variants for validation. `lsposed-bridge` is an optional, separately installed Track-C add-on statically scoped exactly to genuine stock `com.tw.music`; `libxposed-api100-stubs` is compile-only.

## Exact com.tw.music boundary

Where exact package identity is genuinely required, distribute `topwayTwMusic` only inside the repository's systemless Magisk module. The observed TS18 overlay target is `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk`; the packager must fail closed when that exact stock target is not present.

The module must not delete, rename, disable or overwrite the protected stock APK, and must not write directly to `/system`, `/product` or `/vendor`. Disable/remove module + reboot is the rollback model. Magisk filesystem overlay capability does not confer platform signing, shared UID/UID 1000, signature permissions, or private Topway/vendor authority. Never claim otherwise.

Raw `topwayTwMusic` APKs are internal build inputs only and must be impossible to select or upload as public release assets.

## Runtime ownership

Preserve exactly one Auxio playback service/player, canonical queue/PlaybackStateManager, MediaSession, notification authority and audio-focus owner. Variant-specific code may adapt package/component/launcher integration only; it must never fork playback authority.

Keep Android framework, Auxio core, Topway/DoFun, MCU/CAN, DSP/radio, root/Magisk and LSPosed authorities separate. Preserve Android 10/API 29 behaviour and API-gate newer APIs.

Track A is the bounded Topway/DoFun integration in the two Topway variants. Track C remains optional, fail-open and independent. It must not become a second playback stack or imply stock/private privileges. No Track-B `com.dofun.variety` module exists without a separate explicit architecture decision.

## Source/build isolation

- `app/src/main`: neutral shared core and Standard-compatible manifest/resources.
- `app/src/topwayCompat`: Topway-only source/resources/manifest overlay, attached only to `topwayTwMedia` and `topwayTwMusic`.
- package-specific provider/resource overrides belong to their variant source sets.
- Topway-only dependencies such as `lifecycle-process` belong only to Topway variants.
- CI, lint, API-29 tests, screenshots and benchmark tasks must use explicit flavour-qualified targets.

Standard must not package Topway activity/service/widget/boot/overlay components. The two Topway variants may expose stock-compatible component names, but component compatibility is not platform identity or signing authority.

## Release policy

Manual Release supports an explicit public asset matrix:

- Standard APK;
- `topwayTwMedia` / `com.tw.media` APK;
- optional exact-`com.tw.music` systemless Magisk ZIP only;
- optional LSPosed Track-C add-on;
- debug APKs only when explicitly requested.

Never publish a raw `topwayTwMusic` APK. Preserve immutable tag/release, signer, checksum, metadata, source-SHA and bounded recovery contracts.

## Engineering workflow

Use current source and repository-owned checks, not stale prompts. Keep changes in the smallest owning boundary. At minimum for product/build/release work run:

```bash
bash scripts/ci-scope.sh --self-test
bash scripts/check-product-contracts.sh
bash scripts/check-topway-manifest-components.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-manual-release-workflow.sh
python3 scripts/check-documentation-policy.py
```

Run relevant flavour-qualified Gradle build/test/lint/API-29 gates as defined in current workflows. Do not weaken a guard to make CI pass; fix the underlying contract or classify a genuinely stale check.

Use evidence labels consistently: **Observed**, **Inferred**, **Proposed**, **Requires TS18 validation**. CI/emulator success does not prove exact TS18 launcher, widget, USB, ACC, MCU/CAN, DSP/radio, Magisk install/rollback or audible-continuity behaviour.

Before delivery inspect the complete diff for generated APKs, ZIPs, logs, reports, credentials, temporary workflows/patchers and stale policy links.
