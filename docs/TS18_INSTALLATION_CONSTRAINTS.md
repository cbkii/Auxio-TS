# TS18 installation and package-identity constraints

This document records the supported Auxio-TS installation shape for the observed TS18/Topway
device. It complements:

- [`DOFUN_VARIETY_COMPATIBILITY.md`](DOFUN_VARIETY_COMPATIBILITY.md)
- [`TS18_RUNTIME_VALIDATION.md`](TS18_RUNTIME_VALIDATION.md)
- [`ts18/launcher-integration/LSPOSED_API100_BRIDGE.md`](ts18/launcher-integration/LSPOSED_API100_BRIDGE.md)

[Evidence confidence: Observed device diagnostics + APK reference evidence] [Porting decision:
Installation/runbook requirement]

## Supported package layout

The target firmware already contains genuine stock `com.tw.music` as a platform-signed,
UID-1000 system priv-app:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

Do not replace, disable, uninstall-for-user, re-sign, or overlay that package for normal Auxio-TS
operation. The supported release layout is:

| Component | Package | Role |
| --- | --- | --- |
| Stock Topway music | `com.tw.music` | Keeps the OEM/platform identity that DoFun addresses |
| Auxio-TS release | `com.tw.media` | Independently signed playback app and media service |
| Auxio-TS LSPosed API-100 bridge | `org.oxycblt.auxio.ts18bridge` | Runs only inside genuine `com.tw.music` and forwards its public integration surface to Auxio |

The LSPosed module has `staticScope=true`; its packaged, default, and recommended scope is exactly
`com.tw.music`. Do not add `com.tw.media`, `com.dofun.variety`, `android`, `system`, or
`system_server` to its scope.

The former exact-package Auxio application is retired. Package/component coverage uses fixtures,
manifest inspection and tests instead of a replacement APK. The former exact-package Magisk
overlay is retired and must not be installed alongside the bridge.

## Installation authority

| Lane | Supported result | Constraint |
| --- | --- | --- |
| Normal package installer | Install signed `com.tw.media` | Cannot configure LSPosed |
| ADB/Termux shell | Inspect package state and install APKs where permitted | Does not grant platform signing or UID 1000 |
| Root + LSPosed | Install Auxio and the bridge, then enable the static scope | Required for the full stock-identity addon path |
| Firmware/platform signing | Not required | Must not be treated as an Auxio release prerequisite |

Without LSPosed, `com.tw.media` can still be used as a normal media app and may satisfy DoFun's
observed alternate component entry. Fixed-panel parity is not guaranteed until it is validated on
the physical TS18.

## Pre-install identity gate

Before enabling the bridge, prove that `com.tw.music` is the genuine system package:

```sh
adb shell pm path com.tw.music
adb shell dumpsys package com.tw.music |
  grep -iE 'codePath|versionCode|versionName|userId|sharedUserId|flags|privateFlags|enabled'
stock_apk="$(adb shell pm path com.tw.music | tr -d '\r' | sed -n 's/^package://p' | head -n1)"
adb pull "${stock_apk}" /tmp/ts18-stock-com.tw.music.apk
apksigner verify --verbose --print-certs /tmp/ts18-stock-com.tw.music.apk |
  grep 'Signer #1 certificate SHA-256 digest'
```

Expected on the captured target:

- the code path is under `/system/priv-app/`;
- the app UID/shared UID is the platform/system identity;
- the package is enabled for user 0;
- the certificate SHA-256 is
  `AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3`.

If an old Auxio exact-package overlay/module is present, disable or uninstall that Magisk module,
reboot, and repeat the identity gate before installing the bridge. Do not clear stock app state
unless a separate, backed-up recovery procedure explicitly requires it.

## Recommended installation

1. Install the signed Auxio-TS release APK as `com.tw.media`.
2. Install the separately signed LSPosed API-100 bridge APK.
3. In LSPosed, enable the module and leave its scope at the single recommended package:
   `com.tw.music`.
4. Reboot the head unit so the bridge loads with a fresh stock process.
5. Open Auxio once, grant its normal media/storage access, and validate service connectivity.
6. Exercise DoFun launch, metadata, transport controls, seek, process restart, and ACC/reboot
   recovery using `TS18_RUNTIME_VALIDATION.md`.

The bridge verifies the expected stock package/UID/certificate and fails open when the identity is
not trusted or Auxio is unavailable. Its kill switch is disabling the LSPosed module and rebooting;
stock music must remain usable after that rollback.

## Recovery

If the bridge causes a problem:

1. disable the Auxio-TS bridge in LSPosed;
2. reboot;
3. verify genuine stock `com.tw.music` launches normally;
4. optionally uninstall only `org.oxycblt.auxio.ts18bridge` and `com.tw.media`.

Do not use package-database/XML surgery, replace the system APK, or re-enable the retired
exact-package Magisk overlay.

## Documentation rule

Do not describe the former exact-package Auxio app or old Magisk overlay as a supported release/install lane.
Always describe `com.tw.media` plus the single-scope LSPosed addon, and retain the physical TS18
acceptance boundary for DoFun/private-panel behaviour.
