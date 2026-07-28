<p align="center"><img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" width="150"></p>
<h1 align="center"><b>Auxio-TS</b></h1>
<h4 align="center">Auxio adapted for TS18 / Topway / DoFun Variety head units</h4>

<p align="center">
  <a href="https://www.gnu.org/licenses/gpl-3.0"><img alt="License: GPL-3.0-or-later" src="https://img.shields.io/badge/license-GPL--3.0--or--later-2B6DBE.svg?style=flat"></a>
  <img alt="Minimum SDK Version" src="https://img.shields.io/badge/API-24%2B-1450A8?style=flat">
</p>

## About

Auxio-TS is a specialised downstream of [OxygenCobalt/Auxio](https://github.com/OxygenCobalt/Auxio) for TS18/Topway Android head units and DoFun Variety Theme (`com.dofun.variety`). It adds package/component aliases, MediaSession/MediaBrowser integration, Topway-compatible broadcasts and widget-control surfaces needed by this vehicle platform.

The music player, design, playback architecture and library model originate from upstream Auxio. General issues that also affect upstream Auxio belong upstream; TS18, DoFun, `com.tw.media`, `com.tw.music` and Topway bridge issues belong here.

[Support upstream Auxio through GitHub Sponsors](https://github.com/sponsors/OxygenCobalt).

## Maintained variants

Auxio-TS now maintains two product identities:

| Variant | Package | Role |
| --- | --- | --- |
| `topwayTwMedia` | `com.tw.media` | Primary automatic CI and normal APK release; exposes `com.tw.music.MusicActivity` for DoFun matching |
| `topwayTwMusic` | `com.tw.music` | Exact stock-package compatibility build, published only inside a Magisk systemless overlay |

The former `org.oxycblt.auxio` standard distributable is retired. Generic Android fallback policy remains tested as pure logic without another package/flavour.

## Compatibility model

Auxio-TS targets:

- DoFun recognition of `com.tw.media/com.tw.music.MusicActivity` and `com.tw.music/com.tw.music.MusicActivity`;
- Android MediaSession, MediaBrowserService, audio focus and media buttons;
- Topway-style metadata, progress and widget-control broadcasts;
- stock-compatible activity, service and widget wrapper names in isolated compatibility source sets.

Private/system/vendor hooks from stock APKs are evidence only unless a protocol is proven, reviewed, isolated and supplied with safe fallback and rollback. A normal rooted APK does not become platform-signed or UID 1000.

## TS18 constraints

Stock firmware normally contains protected `com.tw.music`. The exact-package variant therefore cannot be treated as a normal universally installable APK. It is packaged as a Magisk module and requires a verified stock path, rollback media and boot-loop recovery.

`com.tw.media` is the primary, less invasive lane, but it can still conflict where that package already exists and does not guarantee DoFun fixed-widget control on every firmware.

See:

- [`docs/TS18_INSTALLATION_CONSTRAINTS.md`](docs/TS18_INSTALLATION_CONSTRAINTS.md)
- [`docs/DOFUN_VARIETY_COMPATIBILITY.md`](docs/DOFUN_VARIETY_COMPATIBILITY.md)
- [`docs/TS18_RUNTIME_VALIDATION.md`](docs/TS18_RUNTIME_VALIDATION.md)

## Music sources

DirectFS is the primary source-selection path on fresh Topway-compatible installs. SAF and MediaStore remain explicit alternatives. Typical readable paths are:

```text
/storage/usbdisk0/*
/storage/emulated/0/*
```

Root-backed discovery is bounded and read-only. Internal `/mnt/media_rw/usbdiskN` paths are not persisted for playback; Auxio-TS uses app-readable `/storage/...` paths or validated aliases.

Generated playlists are optional post-load work and do not control the initial source-selection or base-library loading path.

## Build

Requirements include JDK 21, Android SDK/NDK, CMake, Ninja and access to pinned submodules.

```bash
bash scripts/bootstrap-dependencies.sh --profile full-build

# Primary development APK
bash scripts/ci-gradle.sh :app:assembleTopwayTwMediaDebug

# Full maintained compatibility evidence; both selected tasks share one invocation
bash scripts/ci-gradle.sh \
  :app:assembleTopwayTwMediaDebug \
  :app:assembleTopwayTwMusicDebug
```

Release builds:

```bash
bash scripts/bootstrap-dependencies.sh --profile release
bash scripts/ci-gradle.sh :app:assembleTopwayTwMediaRelease
bash scripts/ci-gradle.sh :app:assembleTopwayTwMusicRelease
```

The raw `topwayTwMusicRelease` APK is an internal packaging input. Use `scripts/package-topway-twmusic-magisk-module.sh`; do not publish it directly.

## Validation

```bash
bash scripts/check-ci-variant-contracts.sh
bash scripts/check-ts18-apk-reference-contracts.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-startup-performance-contracts.sh
```

Before installation, inspect existing package state:

```bash
adb shell 'cmd package list packages | grep -E "com\.tw\.music|com\.tw\.media|com\.dofun\.variety"'
```

After installing a compatible build:

```bash
adb shell cmd package resolve-activity --brief -n com.tw.media/com.tw.music.MusicActivity
adb shell cmd package resolve-activity --brief -n com.tw.music/com.tw.music.MusicActivity
adb shell cmd package query-intent-services -a android.media.browse.MediaBrowserService
adb shell dumpsys media_session | grep -i -A60 'com.tw.music\|com.tw.media\|auxio'
```

CI/emulator success is not proof of exact TS18 widget, USB, ACC, MCU/CAN, DSP/radio or launcher behaviour.

## CI

`topwayTwMedia` is the automatic unit-test, lint and API 29 authority. Changed-file classification selects only the relevant maintained variant(s); shared Topway/full changes build both in one bounded Gradle invocation. Uncertain or Gradle/dependency-sensitive changes fail open to full CI. See [`docs/CI_TASK_POLICY.md`](docs/CI_TASK_POLICY.md).

## Documentation

- [`docs/README.md`](docs/README.md) — documentation index
- [`docs/DEVELOPMENT.md`](docs/DEVELOPMENT.md) — setup, tasks and CI
- [`docs/RELEASE_WORKFLOW.md`](docs/RELEASE_WORKFLOW.md) — signed release flow
- [`docs/TS18_APK_REFERENCE.md`](docs/TS18_APK_REFERENCE.md) — APK-derived evidence
- [`docs/CODEX_TS18_DEVICE_CONTEXT.md`](docs/CODEX_TS18_DEVICE_CONTEXT.md) — exact-device context

## Licence

Auxio-TS remains GPL-3.0-or-later and preserves upstream attribution. See [`LICENSE`](LICENSE) and [`NOTICE`](NOTICE).
