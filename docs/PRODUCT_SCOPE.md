# Product scope

This document is the canonical product, package, module, support and release matrix for Auxio-TS.

## Application variants

Auxio-TS targets Android 10/API 29 TS18 while retaining a neutral Android lane. The shared Kotlin/Java namespace remains `org.oxycblt.auxio`; distribution identity is explicit:

| Variant | Application ID | Topway compatibility | Release posture |
| --- | --- | --- | --- |
| `standard` | `org.oxycblt.auxio` | Off | Normal signed APK. No Topway/DoFun-only components. |
| `topwayTwMedia` | `com.tw.media` | On | Maintained installable TS18 signed APK. |
| `topwayTwMusic` | `com.tw.music` | On | Internal exact-package build only. Raw APK is never a public asset. |

The two Topway variants may expose the stock-compatible `com.tw.music.MusicActivity` alias and bounded wrapper/service/widget components. Those names do not confer stock signing, UID 1000, signature permissions or vendor authority.

## Exact-package systemless lane

Where exact `com.tw.music` application identity is genuinely required, the internally built `topwayTwMusic` APK may be packaged only into the repository's **systemless Magisk ZIP**. The observed exact TS18 target is `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk`; packaging/install must fail closed when that target is absent.

The module overlays through Magisk only. It does not mutate the protected stock file. Disabling/removing the module and rebooting is the rollback model. Root/Magisk does not provide platform signing, UID 1000/shared UID, signature permissions or private Topway/vendor authority.

## Other repository areas

| Area | Classification | Identity / authority |
| --- | --- | --- |
| `musikr` | Internal library | No separate product authority. |
| `startup-benchmark` | Validation | Mirrors `standard`, `topwayTwMedia`, `topwayTwMusic`; API 29 is the TS18 runtime-compatibility target. |
| `lsposed-bridge` | Optional Track-C add-on | `org.oxycblt.auxio.ts18bridge`; statically scoped exactly to genuine stock `com.tw.music`; separate from app variants. |
| `libxposed-api100-stubs` | Compile-only | Must not enter runtime APK graphs. |
| scripts/diagnostics/root-storage | Support tooling | No platform/package authority. |
| evidence/reference | Non-normative evidence | Does not set runtime/release policy. |

No Track-B `com.dofun.variety` module exists.

## Runtime ownership

All variants preserve the same single Auxio playback architecture: one player/ExoPlayer, playback service, canonical queue/PlaybackStateManager, MediaSession, notification path and audio-focus owner. Variant code adapts identity and bounded integration only.

## Public release matrix

| Artefact | Supported | Default posture |
| --- | --- | --- |
| Standard APK | Yes | Included by Manual Release unless deselected. |
| `topwayTwMedia` / `com.tw.media` APK | Yes | Included by Manual Release unless deselected. |
| exact-`com.tw.music` Magisk ZIP | Yes, only where required | Explicit opt-in; systemless only. |
| raw `topwayTwMusic` APK | **No** | Internal build input only; publication forbidden. |
| LSPosed Track-C add-on | Optional | Explicit opt-in. |
| Debug APKs | Diagnostics only | Workflow artefacts unless explicitly published. |

## Evidence status

- **Observed:** exact stock TS18 package path `/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk` is the current Magisk packager target.
- **Observed:** genuine stock `com.tw.music` may be platform-signed/UID 1000 on captured devices; that authority does not transfer to Auxio builds.
- **Inferred:** related firmware may share launcher/action/widget contracts; related devices remain precedent rather than exact proof.
- **Requires TS18 validation:** exact launcher selection, fixed-widget behaviour, USB/ACC runtime, systemless module install/boot/rollback, MCU/CAN, DSP/radio and audible continuity.

See [Architecture](ARCHITECTURE.md), [release policy](RELEASE_WORKFLOW.md) and [TS18 installation constraints](TS18_INSTALLATION_CONSTRAINTS.md). The earlier single-product decision is historical and superseded by this current product authority.
