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

| Lane | Authority | Can install exact `com.tw.music` replacement? | Notes |
|---|---|---:|---|
| Normal package installer only | User/app context | Usually no if stock `com.tw.music` exists | Expected package/signature conflict. |
| TermOne/Termux app shell only | App UID such as `u0_a177` | No | Can inspect limited state only; cannot disable/remove system packages. |
| ADB shell | `uid=2000(shell)` | Usually yes after disable/uninstall-for-user | Reversible user-level package management is possible. |
| Shizuku | Shell/system-mediated app operations | Possibly | Depends on Shizuku being started and the package manager action supported. |
| Root/Magisk/system image | root/system | Yes | Can remove, overlay, or replace system APKs; highest risk and highest authority. |
| Matching OEM/platform signing key | package update authority | Yes | Not expected for normal Auxio-TS releases. |

## Variants and intended use

| Variant | Package ID | DoFun component target | Intended use | Caveat |
|---|---|---|---|---|
| `standard` | `org.oxycblt.auxio` | Normal Auxio activity | General development/testing | Does not satisfy fixed DoFun stock music identity. |
| `topwayTwMusic` | `com.tw.music` | `com.tw.music/com.tw.music.MusicActivity` | Exact stock `twmusic` replacement target | Conflicts with stock `com.tw.music` unless package state is managed. |
| proposed `topwayTwMedia` | `com.tw.media` | `com.tw.media/com.tw.music.MusicActivity` | DoFun alternate-entry candidate for stock-conflict-aware installs | For root/Shizuku/ADB/system-managed setups; not a guaranteed no-root bypass. |

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
