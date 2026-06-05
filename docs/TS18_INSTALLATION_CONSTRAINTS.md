# TS18 installation and package-conflict constraints

This document records the practical installation constraints for Auxio-TS on the observed TS18/Topway target device.

It complements:

- [`DOFUN_VARIETY_COMPATIBILITY.md`](DOFUN_VARIETY_COMPATIBILITY.md)
- [`TS18_RUNTIME_VALIDATION.md`](TS18_RUNTIME_VALIDATION.md)
- [`evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md`](evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md)

[Evidence confidence: Observed device diagnostics + APK reference evidence] [Porting decision: Installation/runbook requirement]

## Core issue

The real target TS18 already contains stock `com.tw.music` as a system priv-app:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

Auxio-TS `topwayTwMusicRelease` intentionally builds as:

```text
package/application ID: com.tw.music
launcher alias:       com.tw.music.MusicActivity
```

That identity is correct for DoFun's fixed stock-music contract, but it means the APK can conflict with the stock system app on real firmware.

A user-signed APK cannot normally update an existing system APK with the same package name unless signatures match or the stock package has been removed/disabled for the active user through a privileged path.

## Install lanes

| Lane                              | Authority                            | Can install exact `com.tw.music` replacement? | Notes                                                                            |
| --------------------------------- | ------------------------------------ | --------------------------------------------: | -------------------------------------------------------------------------------- |
| Normal package installer only     | User/app context                     |     Usually no if stock `com.tw.music` exists | Expected package/signature conflict.                                             |
| TermOne/Termux app shell only     | App UID such as `u0_a177`            |                                            No | Can inspect limited state only; cannot disable/remove system packages.           |
| ADB shell                         | `uid=2000(shell)`                    |  Usually yes after disable/uninstall-for-user | Reversible user-level package management is possible.                            |
| Shizuku                           | Shell/system-mediated app operations |                                      Possibly | Depends on Shizuku being started and the package manager action supported.       |
| Root/Magisk/system image          | root/system                          |                                           Yes | Can remove, overlay, or replace system APKs; highest risk and highest authority. |
| Matching OEM/platform signing key | package update authority             |                                           Yes | Not expected for normal Auxio-TS releases.                                       |

## Variants and intended use

| Variant                  | Package ID          | DoFun component target                    | Intended use                                                      | Caveat                                                                       |
| ------------------------ | ------------------- | ----------------------------------------- | ----------------------------------------------------------------- | ---------------------------------------------------------------------------- |
| `standard`               | `org.oxycblt.auxio` | Normal Auxio activity                     | General development/testing                                       | Does not satisfy fixed DoFun stock music identity.                           |
| `topwayTwMusic`          | `com.tw.music`      | `com.tw.music/com.tw.music.MusicActivity` | Exact stock `twmusic` replacement target                          | Conflicts with stock `com.tw.music` unless package state is managed.         |
| proposed `topwayTwMedia` | `com.tw.media`      | `com.tw.media/com.tw.music.MusicActivity` | DoFun alternate-entry candidate for stock-conflict-aware installs | For root/Shizuku/ADB/system-managed setups; not a guaranteed no-root bypass. |

## Recommended operator flow

### With no ADB, no root, no Shizuku

1. Do not assume `topwayTwMusicRelease` will install.
2. Install a standalone/non-conflicting Auxio-TS build for playback/UI testing if available.
3. Use on-device settings/factory menus only where available.
4. Treat DoFun exact-identity replacement as blocked until a privileged package-management path exists.

### With ADB shell or Shizuku

Prefer reversible disable before uninstall-for-user:

```sh
pm disable-user --user 0 com.tw.music
```

Only if needed and understood:

```sh
pm uninstall --user 0 com.tw.music
```

Recovery commands:

```sh
cmd package install-existing --user 0 com.tw.music
pm enable com.tw.music
```

Then attempt the exact replacement APK:

```sh
adb install -r app/build/outputs/apk/topwayTwMusic/release/app-topwayTwMusic-release.apk
```

### With root/system image control

Root/system replacement must remain deliberate and reversible. Keep a copy of the stock APK and package state. Do not conflate successful system replacement with normal user-install compatibility.

## `com.tw.media` clarification

DoFun APK evidence includes an alternate fixed entry:

```text
com.tw.media / com.tw.music.MusicActivity
```

Adding a `topwayTwMedia` variant is useful because it lets Auxio-TS satisfy that alternate identity where firmware/launcher state permits it. However:

- It does not remove or neutralise stock `com.tw.music`.
- It may conflict if the firmware already has stock `com.tw.media`.
- It may not be selected if DoFun prioritises `com.tw.music` over `com.tw.media`.
- It is primarily for users who can manage stock package state through ADB shell, Shizuku, root, or firmware control.

## Documentation rule

Do not state that any Topway variant is universally installable on locked stock TS18 firmware. Always state the required package-state authority and exact package identity being tested.

# TS18 Installation Constraints

This page describes install lanes for the redacted TS18 `s9863a1h10` Android 10 device profile. It does not include raw diagnostics.

## APK lanes

| APK / variant          | Package             | Purpose                                                                   | Stock-conflict note                                                                 |
| ---------------------- | ------------------- | ------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- |
| `standardRelease`      | `org.oxycblt.auxio` | Normal Auxio-TS identity                                                  | Does not replace stock `com.tw.music`                                               |
| `topwayTwMusicRelease` | `com.tw.music`      | Exact stock `twmusic` replacement identity                                | Conflicts with stock system priv-app unless package state/signing is managed        |
| `topwayTwMediaRelease` | `com.tw.media`      | DoFun alternate fixed entry for `com.tw.media/com.tw.music.MusicActivity` | Not a universal no-root bypass; `com.tw.media` may itself conflict on some firmware |

## Install-lane distinction

| Lane                               | What it can do                                                                                                                                          | Constraints                                                                                     |
| ---------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------- |
| Normal app context / TermOne only  | Run app-local diagnostics, inspect app-visible storage, verify overlay permission UI, validate `/sdcard/Music` and `/storage/usbdisk0` media visibility | Cannot disable/remove system priv-app packages; cannot assume install over stock `com.tw.music` |
| ADB shell                          | Install APKs, inspect package state, disable/enable packages for a user, collect `dumpsys` evidence                                                     | Requires physical USB/OTG ADB or another shell path; unavailable in the reported user setup     |
| Shizuku                            | User-mediated package-management and shell-like operations from an app context                                                                          | Requires Shizuku to be installed, authorized, and working on the head unit                      |
| Root/system image/firmware control | Manage system priv-app package state, firmware contents, or matching signing path                                                                       | Device/firmware-specific and outside normal APK expectations                                    |

A user-signed `topwayTwMusicRelease` cannot be assumed installable over `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk`. Package state/signing must be managed by ADB shell, Shizuku, root, firmware/system-image control, matching OEM signature, or prior stock-package removal/disable.

## Reversible recovery notes

Prefer reversible disable before uninstall-for-user when testing package conflicts:

```sh
adb shell pm disable-user --user 0 com.tw.music
adb shell pm enable com.tw.music
adb shell cmd package install-existing --user 0 com.tw.music
```

Apply the same pattern cautiously to `com.tw.media` only after confirming that package exists and understanding the firmware role it plays.
