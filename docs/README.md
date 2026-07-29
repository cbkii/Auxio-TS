# 🧭 Auxio-TS documentation index

Auxio-TS targets TS18, Topway and DoFun Variety head units. Choose the section that matches your task.

> [!NOTE]
> Exact TS18 widget, USB, ACC, MCU, CAN, DSP, radio and launcher behaviour requires physical device validation.

## 🙋 User guides

- [Main README and quick start](../README.md)
- [Settings guide](SETTINGS_GUIDE.md)
- [TS18 installation constraints](TS18_INSTALLATION_CONSTRAINTS.md)
- [DoFun Variety compatibility](DOFUN_VARIETY_COMPATIBILITY.md)
- [Physical TS18 validation](TS18_RUNTIME_VALIDATION.md)

## 🧰 Advanced users

- [Advanced use and contributing](ADVANCED_AND_CONTRIBUTING.md)
- [Root storage fast path](ts18/ROOT_STORAGE_FASTPATH.md)
- [Exact-device context](CODEX_TS18_DEVICE_CONTEXT.md)
- [Topway research index](topway/README.md)

## 🧑‍💻 Contributors

- [Repository instructions](../AGENTS.md)
- [Development setup and maintained variants](DEVELOPMENT.md)
- [CI task policy](CI_TASK_POLICY.md)
- [Release workflow](RELEASE_WORKFLOW.md)
- [Fast interaction startup](architecture/FAST_INTERACTION_STARTUP.md)
- [Incremental library pipeline](architecture/INCREMENTAL_LIBRARY_PIPELINE.md)
- [Startup profiles and benchmarks](architecture/STARTUP_PROFILES_BENCHMARKS.md)
- [Exact TS18 startup validation](validation/EXACT_TS18_STARTUP_VALIDATION.md)

## 🔬 Evidence and reference

- [TS18 APK reference](TS18_APK_REFERENCE.md)
- [Exact TS18 device context](CODEX_TS18_DEVICE_CONTEXT.md)
- [Topway source-led notes](topway/README.md)

Use the repository evidence labels consistently: **Observed**, **Inferred**, **Hypothesis**, **Requires TS18 validation** and **Unsupported**.

## 🛡️ Safety boundary

The normal release is the signed `com.tw.media` APK. The exact-package `com.tw.music` build is published only as a Magisk module **[beta]**.

Root does not provide platform signing, shared UID 1000, MCU, CAN, DSP, radio or protected-package authority. Read [TS18 installation constraints](TS18_INSTALLATION_CONSTRAINTS.md) before any exact-package or stock-app change.

---

[← Main README](../README.md) · [Settings guide](SETTINGS_GUIDE.md) · [Advanced and contributing](ADVANCED_AND_CONTRIBUTING.md)
