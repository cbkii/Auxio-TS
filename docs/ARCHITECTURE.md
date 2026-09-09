# Architecture

This document is the canonical high-level ownership and integration-boundary guide.

## Runtime ownership

Auxio-TS has one playback authority in every distribution variant:

- `AuxioService` and its playback fragments own playback lifecycle;
- one `PlaybackStateManager`/canonical queue path owns ordering and restoration;
- one MediaSession exposes transport state;
- one notification path publishes playback state;
- one audio-focus path arbitrates audio ownership.

Compatibility activities, aliases, services, receivers and widgets delegate into these authorities. They must not create another player, queue, MediaSession, notification or audio-focus owner.

## Distribution boundaries

| Variant | Identity | Compatibility boundary |
| --- | --- | --- |
| `standard` | `org.oxycblt.auxio` | Neutral Android app. `TOPWAY_COMPAT_ENABLED=false`; no Topway/DoFun-only components or dependencies. |
| `topwayTwMedia` | `com.tw.media` | Primary installable TS18/DoFun Track-A lane. |
| `topwayTwMusic` | `com.tw.music` | Internal exact-package Track-A build; public only when enclosed by the approved systemless Magisk module. |

`app/src/main` is shared neutral core. `app/src/topwayCompat` is attached only to the two Topway variants and owns the stock-compatible activity/service/widget/boot/overlay manifest additions. Package-specific resource/provider authorities live in the corresponding variant source sets. Topway-only dependencies must not leak into Standard.

`startup-benchmark` mirrors all three identities. API 29 is the hosted runtime-compatibility target; profile-generation APIs may use newer managed devices where required by AndroidX.

## Authority boundaries

| Authority | Owns | Does not own |
| --- | --- | --- |
| Android framework | lifecycle, MediaSession/MediaBrowser, media buttons, audio focus, notifications, storage APIs | Topway/DoFun launcher policy, MCU/CAN, DSP/radio, root privilege |
| Auxio core | library, queue, playback, UI/app state | protected stock signer/UID or vendor-global state |
| Track A | bounded package/component, broadcast, widget and command translation in Topway variants | second playback stack or platform/vendor authority |
| DoFun/Topway firmware | launcher selection, vendor widgets/services | Auxio queue or MediaSession authority |
| MCU/CAN and DSP/radio | vehicle/audio-hardware state | Android app playback architecture |
| Root/Magisk | approved systemless filesystem overlay and bounded diagnostics | platform signing, UID 1000/shared UID, signature permissions, private-vendor authority |
| Track C LSPosed bridge | optional fail-open translation in genuine stock `com.tw.music` | Auxio runtime ownership, DoFun scope, system-wide hooks or package replacement |

## Exact com.tw.music systemless replacement

`topwayTwMusic` is built internally with application ID `com.tw.music`, but its raw APK is not a distributable. Where exact application identity is required, Manual Release may package that APK into the systemless Magisk module targeting the observed TS18 path:

`/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk`

The module must fail closed when the exact target is absent, and it must not delete, rename, disable or write the stock file on the protected partition. Disable/remove + reboot restores the untouched stock view. Overlaying an independently signed APK does **not** make it platform-signed or UID 1000 and does not grant stock signature/private-vendor permissions.

This lane therefore provides exact **package/application identity only**, subject to Android package/signing rules and exact-device validation. It is not a privilege-emulation mechanism.

## Integration tracks

### Track A - application integration

Topway compatibility source is compiled only into `topwayTwMedia` and `topwayTwMusic`. Public Android surfaces remain preferred; observed vendor contracts are isolated and fail-open where possible. Both variants still delegate playback to the same Auxio core authority.

### Track B - absent

No `com.dofun.variety` module exists. Creating one requires separate exact evidence, scope, trust, IPC, release, rollback and validation approval.

### Track C - optional stock shim

`lsposed-bridge` is separately installed and statically scoped exactly to genuine stock `com.tw.music`. It remains optional, has its own kill-switch/fail-open behaviour and may translate bounded stock integration into the existing Auxio authority. It is neither an application variant nor a substitute for the systemless exact-package lane.

## Library/source authority

A successfully committed library is normal operating state. Activity/service lifecycle is not source-enumeration authority. Source traversal occurs only for a positive source reason: pending/initial configuration, explicit refresh/retry, changed configuration or enabled removable-source observation. Wall-clock age alone is not a source reason.

Generated playlists are derived presentation and must not alter source identity, checkpoints, queue authority or trigger source scans. Startup/cache/profile instrumentation must not become a second playback or library authority.

## Safety and change rules

Preserve Android 10/API 29 and API-gate newer behaviour. Keep I/O bounded/off-main-thread. Treat startup, RemoteViews, notifications, storage scanning and tag parsing as OEM-sensitive.

Never infer platform signing, UID 1000/shared UID, signature permissions or private-vendor authority from package naming, root, Magisk or LSPosed. Never modify firmware/MCU/CAN or directly mutate protected partitions from this project.

Repository/CI evidence is not exact-device proof. Magisk install/boot/rollback, DoFun fixed-widget selection, ACC behaviour, USB mounts, MCU/CAN/DSP/radio and audible continuity remain **Requires TS18 validation** until captured on the exact unit.
