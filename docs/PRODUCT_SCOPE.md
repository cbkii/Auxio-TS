# Product scope

This document is the canonical product, package, module, support and release matrix for Auxio-TS.

## Product definition

Auxio-TS is one maintained in-car local-music application installed as `com.tw.media`. Its Kotlin/Java namespace remains `org.oxycblt.auxio`. It targets Android 10/API 29 TS18 and related Topway/DoFun devices while preserving correct Android media behaviour.

The app exposes the exported stock-compatible `com.tw.music.MusicActivity` component and bounded wrappers required by supported launchers. These are components of `com.tw.media`; they do not make Auxio-TS the genuine stock `com.tw.music` application.

## Product and module matrix

| Area | Classification | Package/identity | Build and release posture |
| --- | --- | --- | --- |
| `app` | Active production product; Track A | App `com.tw.media`; source namespace `org.oxycblt.auxio`; exported `com.tw.music.MusicActivity` | Debug, release, JVM test, lint, API 29, screenshot and benchmark target. Signed release APK is the normal product artefact. |
| `musikr` | Internal library | `org.oxycblt.auxio.musikr` namespace | Built and tested as an app dependency; never a separate product. |
| `lsposed-bridge` | Optional compatibility add-on; Track C | `org.oxycblt.auxio.ts18bridge`; static hook scope exactly `com.tw.music` | Opt-in, separately built, signed and validated. It is not an app flavour and is not selected by default for release. |
| `libxposed-api100-stubs` | Compile-only support | API stubs only | Compile-only dependency of Track C; no runtime or release artefact. |
| `startup-benchmark` | Validation infrastructure | Targets `com.tw.media` | Generates profile/emulator evidence; not a product APK. |
| `scripts`, diagnostics and root-storage/Magisk material | Support tooling | No product identity | Development, validation and bounded device support only; no replacement overlay release. |
| `docs/evidence` and curated reference records | Evidence/reference | Observed third-party or device facts | Non-normative; no runtime or release authority. |
| Former `standard` app | Retired | Former `org.oxycblt.auxio` distributable | Must not return. Standard-Android fallback is tested through pure policy, DI and tests. |
| Former `topwayTwMusic` app | Retired | Former Auxio APK using `com.tw.music` | No Gradle variant, benchmark, screenshot, CI build, upload or release capability. |

No Track-B `com.dofun.variety` module exists. Creating one requires the expansion gate in root [AGENTS.md](../AGENTS.md) and a separate architecture decision.

## Support and release matrix

| Artefact or behaviour | Supported | Notes |
| --- | --- | --- |
| Signed Auxio-TS `com.tw.media` APK | Yes | Primary and default product. |
| Debug Auxio-TS APK | Diagnostics only | Workflow artefact unless explicitly requested for diagnosis. |
| Signed LSPosed Track-C add-on | Optional | May accompany a manual release only by explicit opt-in. |
| Raw Auxio `com.tw.music` APK | No | Retired protected-package impersonation lane. |
| Generic Android app flavour | No | Use policies and tests for fallback behaviour. |
| Magisk stock-app replacement overlay | No | Diagnostics/root-storage tools do not grant package or platform identity. |

## Compatibility status

- **Observed:** DoFun/Topway launchers may address `com.tw.media/com.tw.music.MusicActivity`; the app preserves that contract.
- **Observed:** the genuine stock `com.tw.music` package can be platform signed and run as UID 1000 on documented devices.
- **Inferred:** related firmware may share some action, extra and widget contracts, but related devices are precedent rather than exact TS18 proof.
- **Physically unverified:** every current build still requires device execution for exact launcher selection, fixed-widget, USB, ACC, MCU/CAN, DSP and radio behaviour.

See [Architecture](ARCHITECTURE.md), [release policy](RELEASE_WORKFLOW.md) and the [single-product decision](decisions/0001-single-product-architecture.md).
