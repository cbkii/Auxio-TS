# 🧑‍💻 Advanced use and contributing

This page is the entry point for advanced Auxio-TS work. It links to the documents that own each technical subject. Keep detailed instructions in those source documents instead of copying them here.

> [!IMPORTANT]
> Auxio-TS is a TS18, Topway and DoFun product variant. Related TS10, TS10M or other UIS8581 units are useful precedent, but they are not exact-device proof.

## 🧭 Advanced navigation

- 🛡️ [Installation and package identity](#installation-and-package-identity)
- 🧩 [DoFun and Topway integration](#dofun-and-topway-integration)
- 🧱 [Architecture and exact-device context](#architecture-and-exact-device-context)
- 🧑‍🔧 [Development setup](#development-setup)
- ✅ [Testing and CI](#testing-and-ci)
- 📦 [Release and signing](#release-and-signing)
- 🔬 [Evidence and reverse engineering](#evidence-and-reverse-engineering)
- 🤝 [Contributing](#contributing)

<a name="installation-and-package-identity"></a>

## 🛡️ Installation and package identity

Auxio-TS maintains two APK variants plus one optional LSPosed bridge addon:

| Variant | Package | Purpose |
| --- | --- | --- |
| `topwayTwMedia` | `com.tw.media` | **Observed — Directly reusable requirement:** primary APK release and normal installation lane. It exposes `com.tw.music.MusicActivity` for DoFun matching. |
| LSPosed bridge | `org.oxycblt.auxio.ts18bridge` | **Requires TS18 validation — Requires TS18 runtime validation:** optional API 100 addon **[beta]** scoped only to genuine stock `com.tw.music`. |
| `topwayTwMusic` | `com.tw.music` | **Observed — Useful as evidence only:** internal exact-package compatibility/test build; never publish or install. |

The old `org.oxycblt.auxio` standard distributable is retired.

Read:

- [TS18 installation constraints](TS18_INSTALLATION_CONSTRAINTS.md)
- [Exact-device runtime validation](TS18_RUNTIME_VALIDATION.md)
- [Root storage fast path](ts18/ROOT_STORAGE_FASTPATH.md)

> [!WARNING]
> Root does not provide the stock platform certificate, shared UID 1000 or signature permissions.
> Keep genuine stock `com.tw.music` installed. Never publish or install the raw `topwayTwMusic`
> APK, and do not recreate the retired Magisk overlay.

<a name="dofun-and-topway-integration"></a>

## 🧩 DoFun and Topway integration

DoFun package and component matching, Android MediaSession behaviour, Topway broadcasts and fixed-widget control are separate contracts. Fixed-widget integration remains **[beta]** and requires physical TS18 validation.

Read:

- [DoFun Variety compatibility](DOFUN_VARIETY_COMPATIBILITY.md)
- [TS18 APK reference](TS18_APK_REFERENCE.md)
- [Topway research index](topway/README.md)

Keep Android media behaviour as the first layer. Keep Topway-specific code behind the existing adapters and coordinators. Do not copy vendor smali or private implementations.

<a name="architecture-and-exact-device-context"></a>

## 🧱 Architecture and exact-device context

Read:

- [Exact TS18 device context](CODEX_TS18_DEVICE_CONTEXT.md)
- [Fast interaction startup](architecture/FAST_INTERACTION_STARTUP.md)
- [Incremental library pipeline](architecture/INCREMENTAL_LIBRARY_PIPELINE.md)
- [Startup profiles and benchmarks](architecture/STARTUP_PROFILES_BENCHMARKS.md)
- [Exact TS18 startup validation](validation/EXACT_TS18_STARTUP_VALIDATION.md)

Keep these authorities separate:

```text
Android framework
Topway and TW services
DoFun launcher and widgets
HAL and sysfs
MCU and CAN
Magisk and root
Application code
```

<a name="development-setup"></a>

## 🧑‍🔧 Development setup

Use [DEVELOPMENT.md](DEVELOPMENT.md) as the development authority. It covers the toolchain, pinned dependencies, maintained variants and Gradle tasks.

Repository instructions are in [`AGENTS.md`](../AGENTS.md). Read them before changing code or CI.

Do not restore the retired standard flavour. Preserve API 29 compatibility and keep `topwayTwMedia` and `topwayTwMusic` package contracts distinct.

<a name="testing-and-ci"></a>

## ✅ Testing and CI

Use:

- [CI task policy](CI_TASK_POLICY.md)
- [Development validation commands](DEVELOPMENT.md)
- [Physical TS18 runtime validation](TS18_RUNTIME_VALIDATION.md)

Documentation-only changes should use the repository's focused syntax and static checks. App, storage, startup, service, manifest or compatibility changes require the relevant maintained build and API 29 evidence.

CI and emulator success do not prove fixed DoFun widget, USB, ACC, MCU, CAN, DSP, radio or exact-device performance.

<a name="release-and-signing"></a>

## 📦 Release and signing

Use [RELEASE_WORKFLOW.md](RELEASE_WORKFLOW.md) as the release authority.

Published install assets are:

- the signed `com.tw.media` APK;
- the optional signed LSPosed API 100 bridge addon **[beta]**.

Do not publish the raw exact-package APK or a replacement Magisk overlay. Preserve package,
version, SDK, ABI, signing certificate and checksum evidence for release assets.

<a name="evidence-and-reverse-engineering"></a>

## 🔬 Evidence and reverse engineering

Use this confidence language:

- **Observed**
- **Inferred**
- **Hypothesis**
- **Requires TS18 validation**
- **Unsupported**

APK, firmware and decompiled resources are compatibility evidence. They do not grant permission to copy private implementation code or claim a private contract is proven.

Start from:

- [TS18 APK reference](TS18_APK_REFERENCE.md)
- [Exact-device context](CODEX_TS18_DEVICE_CONTEXT.md)
- [Topway research index](topway/README.md)

<a name="contributing"></a>

## 🤝 Contributing

Before opening a pull request:

1. Read [`AGENTS.md`](../AGENTS.md) and the relevant linked authority document.
2. Confirm the issue belongs to Auxio-TS rather than upstream Auxio.
3. Keep changes focused and preserve package, playback-service, queue, MediaSession and notification authority.
4. Add focused tests or documentation for the behaviour changed.
5. Run the narrowest relevant validation and report exactly what ran.
6. Mark physical TS18 behaviour as unverified unless it was tested on the device.
7. Review the final diff for APKs, logs, reports, credentials, generated outputs and unrelated files.

General Auxio issues should be checked against [upstream Auxio](https://github.com/OxygenCobalt/Auxio). TS18, DoFun, `com.tw.media`, `com.tw.music` and Topway bridge issues belong in this repository.

---

[← Main README](../README.md) · [Settings guide](SETTINGS_GUIDE.md) · [Documentation index](README.md)
