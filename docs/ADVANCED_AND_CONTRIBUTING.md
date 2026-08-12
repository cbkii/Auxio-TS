# Advanced use and contributing

Auxio-TS is one `com.tw.media` in-car-device product. Read [product scope](PRODUCT_SCOPE.md), [architecture](ARCHITECTURE.md) and root [AGENTS.md](../AGENTS.md) before architecture-affecting work.

## Installation and package identity

| Artefact | Purpose |
| --- | --- |
| Auxio-TS `com.tw.media` APK | Maintained product; exposes `com.tw.music.MusicActivity` for compatible launchers. |
| LSPosed bridge | Optional Track-C add-on, exactly scoped to genuine stock `com.tw.music`. |

The generic `standard` app and exact-package Auxio `com.tw.music` app are retired. Keep the genuine stock package installed and unchanged. Root does not provide its platform signer, UID 1000 or signature permissions.

Read [installation constraints](TS18_INSTALLATION_CONSTRAINTS.md), [root storage guidance](ts18/ROOT_STORAGE_FASTPATH.md) and the [physical validation runbook](TS18_RUNTIME_VALIDATION.md).

## Engineering

- [Development setup and current tasks](DEVELOPMENT.md)
- [Runtime and source architecture](ARCHITECTURE.md)
- [DoFun/Topway Track-A compatibility](DOFUN_VARIETY_COMPATIBILITY.md)
- [Optional Track-C bridge](ts18/launcher-integration/LSPOSED_API100_BRIDGE.md)
- [Startup and source architecture references](README.md#current-architecture-references)
- [Release policy](RELEASE_WORKFLOW.md)

Preserve one playback service, queue, MediaSession, notification and audio-focus authority. Keep vendor integration in its owning adapter, preserve API 29, and test Android-standard fallback through pure policy/DI/tests rather than another distributable flavour.

## Evidence

Start from current architecture, then use the [curated evidence index](evidence/README.md) to answer a specific gap. Decompilations, old prompts and historical reports are not instructions. Do not copy vendor smali or infer platform authority from a string hit.

Claims must be labelled **Observed**, **Inferred**, **Proposed** or **Physically unverified**. Exact launcher, widget, USB, ACC, MCU/CAN, DSP and radio behaviour requires physical execution.

## Contributing

Keep changes scoped, update the owning canonical document, run the focused checks in [Development](DEVELOPMENT.md), and use the PR template to record module classification, runtime authority, device evidence, release impact and rollback. Do not commit generated APKs, logs, reports, credentials or raw decompile trees.
