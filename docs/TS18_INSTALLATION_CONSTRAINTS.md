# TS18 installation and package-identity constraints

This document records supported Auxio-TS installation shapes for the observed TS18/Topway target. It complements [DoFun compatibility](DOFUN_VARIETY_COMPATIBILITY.md), [runtime validation](TS18_RUNTIME_VALIDATION.md), and the [LSPosed Track-C guide](ts18/launcher-integration/LSPOSED_API100_BRIDGE.md).

## Observed stock identity

The captured target contains genuine stock `com.tw.music` at:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

Captured evidence shows privileged/platform characteristics including UID 1000. Those properties belong to the genuine stock package and signer. Auxio builds do **not** inherit them by using the same package name or systemless path.

## Choose one Auxio application lane

Do not run multiple Auxio application variants concurrently on the head unit. That would create competing playback services/MediaSessions/notifications and violates the single-playback-authority contract.

| Lane | Package | Intended use |
| --- | --- | --- |
| `standard` | `org.oxycblt.auxio` | Neutral Android installation; no Topway compatibility. |
| `topwayTwMedia` | `com.tw.media` | Preferred ordinary TS18/DoFun installation. |
| `topwayTwMusic` systemless | `com.tw.music` | Optional exact-package experiment only where exact application identity is required and the exact stock/path/signing constraints are understood. Public artifact is the Magisk ZIP only. |

The raw `topwayTwMusic` APK is never a supported install/download artifact.

## Preferred TS18 lane: topwayTwMedia

For normal TS18 use, install the signed `topwayTwMedia` / `com.tw.media` APK. It contains the bounded stock-compatible activity/service/widget/component wrappers without claiming genuine stock identity.

If the direct Track-A path is insufficient and genuine stock `com.tw.music` must remain authoritative, the optional LSPosed Track-C add-on may be tested. Its static scope remains exactly genuine stock `com.tw.music`; do not add `com.tw.media`, `com.dofun.variety`, `android`, `system` or `system_server`.

## Optional exact-package Magisk lane

Where exact application ID `com.tw.music` is genuinely required, Manual Release can produce a **systemless Magisk ZIP** containing the internally built `topwayTwMusic` APK at the observed overlay path:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

The installer must fail closed if that exact stock file is absent. It leaves the protected stock APK untouched on disk. Disabling/removing the module and rebooting is the rollback model.

**Critical authority limit:** the Auxio APK is independently signed. A Magisk overlay does not make it platform-signed, does not confer UID 1000/shared UID, does not grant signature permissions and does not create private-vendor authority. If PackageManager/shared-UID/signature state rejects the overlaid APK, **STOP**; do not attempt signature spoofing, package-database edits or protected-partition mutation. Exact package identity alone is not proof that this lane will load on the physical TS18.

Do not combine the exact-package Magisk lane with Track C: Track C is designed to run inside the **genuine stock** `com.tw.music` process. Do not keep an active `topwayTwMedia` Auxio instance alongside the exact-package Auxio instance during validation; test one playback authority at a time.

## Preflight for exact-package testing

Before enabling the Magisk module, capture the current stock state and confirm the exact target:

```sh
adb shell pm path com.tw.music
adb shell dumpsys package com.tw.music | grep -iE 'codePath|versionCode|versionName|userId|sharedUserId|flags|privateFlags|enabled'
```

Expected from the captured baseline includes the exact `/system/priv-app/com.tw.music_a41e/...` path. Record the stock signer from a pulled copy with `apksigner` if available. Do not assume a related TS18/TS10/8581 unit has the same package path, shared UID or signer.

**STOP** if the path, device/build identity, current package state, Magisk recovery path or rollback media are not confirmed.

## Exact-package validation sequence

1. Keep a known-good copy of the stock package evidence and the Magisk ZIP outside the head unit.
2. Ensure other Auxio application variants and Track C are not actively providing playback on the same test boot.
3. Install the Magisk module only after its exact-path preflight passes.
4. Reboot and capture `pm path`, `dumpsys package`, process UID, package load errors and logcat before interpreting behaviour.
5. If PackageManager rejects the independently signed/shared-UID state, stop and remove/disable the module; do not try to force platform authority.
6. If the package loads, validate launch, one playback service/MediaSession/notification, DoFun controls/metadata, USB, process restart and ACC sleep/wake.
7. Validate rollback by disabling/removing the module, rebooting and confirming genuine stock `com.tw.music` is visible and functional again.

Every step above remains **Requires TS18 validation** until captured on the exact unit.

## Track-C validation/recovery

For the preferred `com.tw.media` + genuine-stock Track-C model, first prove `com.tw.music` is the genuine stock package, then enable only the bridge's exact static scope. The bridge must fail open when Auxio is unavailable. Recovery is: disable the LSPosed module, reboot, verify stock music, and optionally uninstall the bridge and `com.tw.media`.

## Hard prohibitions

Do not:

- delete/rename/overwrite the stock APK on its protected partition;
- `pm disable`/uninstall-for-user genuine stock as part of normal setup;
- edit package-manager XML/database state to force the exact-package lane;
- claim the Auxio build is platform-signed or UID 1000;
- claim signature/private-vendor permissions from root/Magisk/LSPosed;
- run multiple Auxio variants as simultaneous playback authorities;
- treat emulator/CI packaging success as physical TS18 acceptance.
