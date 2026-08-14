# Auxio-TS

Auxio-TS is one maintained local-music application for Android 10 TS18 and related Topway/DoFun in-car devices. It installs as `com.tw.media` and keeps the source namespace `org.oxycblt.auxio`.

The app exposes the stock-compatible `com.tw.music.MusicActivity` entry component needed by supported launchers while preserving the genuine platform-signed stock `com.tw.music` package. Auxio-TS is not a stock-package replacement and does not require platform identity, a shared UID or signature spoofing.

## Supported artefacts

| Artefact | Status | Purpose |
| --- | --- | --- |
| Auxio-TS APK (`com.tw.media`) | Maintained product | Normal installation and release artefact. |
| LSPosed bridge (`org.oxycblt.auxio.ts18bridge`) | Optional Track C add-on | Narrow legacy relay for genuine stock `com.tw.music`; never installed or enabled by default. |
| Debug APKs | Diagnostic workflow artefacts | Development and diagnosis; not normal release assets. |

The former generic `standard` app and exact-package Auxio `com.tw.music` app are retired. They are not build, test or release targets.

## Install

1. Download the signed Auxio-TS app APK from [Releases](https://github.com/cbkii/Auxio-TS/releases).
2. Install and open Auxio-TS, then grant the requested storage and notification permissions.
3. Select a music source. On supported rooted TS18 systems, the optional root-storage fast path can help with otherwise unreadable USB mounts.
4. Configure the optional LSPosed bridge only when the [bridge guide](docs/ts18/launcher-integration/LSPOSED_API100_BRIDGE.md) and device evidence show that the legacy stock relay is required.

> [!WARNING]
> Do not replace, mutate or disable the genuine platform-signed `com.tw.music` package to install Auxio-TS. Root access does not provide its signer or UID 1000 identity.

## What the app provides

- local library scanning and playback;
- a landscape-focused in-car UI and optional floating controls;
- Android MediaSession, MediaBrowser, media-button, notification and audio-focus integration;
- bounded Topway and DoFun Track-A compatibility inside the app;
- Direct, Storage Access Framework and Android system-library source modes;
- cached library, queue and startup restoration;
- Android 10/API 29 compatibility.

Auxio-TS owns one playback service, queue, MediaSession, notification and audio-focus path. Vendor launchers, MCU/CAN, DSP/radio, root and LSPosed remain separate authorities.

## Documentation

- [Product scope and support matrix](docs/PRODUCT_SCOPE.md)
- [Architecture and runtime ownership](docs/ARCHITECTURE.md)
- [Development and validation](docs/DEVELOPMENT.md)
- [Release policy](docs/RELEASE_WORKFLOW.md)
- [Settings guide](docs/SETTINGS_GUIDE.md)
- [TS18 installation constraints](docs/TS18_INSTALLATION_CONSTRAINTS.md)
- [Physical TS18 validation runbook](docs/TS18_RUNTIME_VALIDATION.md)
- [Documentation index](docs/README.md)

Exact widget, USB, ACC, launcher, MCU/CAN, DSP and radio behaviour varies by firmware and requires physical-device validation. Report problems with the Auxio-TS version, package, device build, source mode and reproduction steps; do not attach credentials or private media.

## Licence and origins

Auxio-TS is based on [Auxio](https://github.com/OxygenCobalt/Auxio), created by Alexander Capehart (OxygenCobalt), and is licensed under GPL-3.0-or-later. See [LICENSE](LICENSE) and [NOTICE](NOTICE).
