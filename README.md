<p align="center"><img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" width="150"></p>
<h1 align="center"><b>Auxio-TS</b></h1>
<h4 align="center">TS18/Topway/DoFun Theme variant of Auxio — a replacement/compatibility target for stock <code>twmusic</code> / <code>com.tw.music</code></h4>

<p align="center">
    <a href="https://www.gnu.org/licenses/gpl-3.0"><img alt="License: GPL-3.0-or-later" src="https://img.shields.io/badge/license-GPL--3.0--or--later-2B6DBE.svg?style=flat"></a>
    <img alt="Minimum SDK Version" src="https://img.shields.io/badge/API-24%2B-1450A8?style=flat">
</p>

## About

**Auxio-TS** is a TS18/Topway head-unit variant of [Auxio](https://github.com/OxygenCobalt/Auxio).

It adapts Auxio for in-vehicle TS18/Topway Android head units and integrates with **DoFun Variety Theme** (`com.dofun.variety`) launcher/music widgets by providing compatibility APKs for the stock `twmusic` / `com.tw.music` contract.

Auxio-TS is not a replacement for the upstream Auxio project. It is a specialised downstream variant for a particular head-unit environment.

## Upstream acknowledgement

This project is based on **Auxio**, created and maintained upstream by [OxygenCobalt](https://github.com/OxygenCobalt).

The music player, core design, playback architecture, library model, UI foundation, and most application behaviour come from upstream Auxio. Auxio-TS exists because TS18/Topway/DoFun launchers expect specific stock music-app package names, activity names, broadcast strings, and widget-control behaviour that the normal Auxio app should not need to impersonate.

Please support, credit, and follow the upstream project:

- Upstream project: [OxygenCobalt/Auxio](https://github.com/OxygenCobalt/Auxio)
- Upstream issues/discussions: use upstream only for issues that also affect normal Auxio
- Auxio-TS issues: use this repo for TS18, Topway, DoFun Variety, `com.tw.music`, or head-unit-specific behaviour

### Donate to upstream Auxio

If you find Auxio-TS useful, please consider supporting the original Auxio project and author. You can support Auxio development through OxygenCobalt’s GitHub Sponsors page:

[Donate to upstream Auxio via GitHub Sponsors](https://github.com/sponsors/OxygenCobalt)

## What Auxio-TS changes

Auxio-TS keeps Auxio as the runtime music player base and adds TS18/Topway/DoFun compatibility work around it.

### Key facts

| Area                               | Value                                                       |
| ---------------------------------- | ----------------------------------------------------------- |
| Upstream base                      | [OxygenCobalt/Auxio](https://github.com/OxygenCobalt/Auxio) |
| Primary compatibility target       | DoFun Variety Theme / `com.dofun.variety`                   |
| Stock app replacement contract     | `twmusic` / `com.tw.music`                                  |
| Topway music release package       | `com.tw.music`                                              |
| Topway media alternate package     | `com.tw.media`                                              |
| Topway launcher/activity component | `com.tw.music.MusicActivity`                                |
| Topway release variants            | `topwayTwMusicRelease`, `topwayTwMediaRelease`              |
| Standard development variant       | `org.oxycblt.auxio`                                         |

### Variant model

Auxio-TS has three release identities:

| Variant         | Package identity    | Purpose                                                                                                        |
| --------------- | ------------------- | -------------------------------------------------------------------------------------------------------------- |
| `standard`      | `org.oxycblt.auxio` | Normal Auxio-derived development/upstream baseline                                                             |
| `topwayTwMusic` | `com.tw.music`      | Exact TS18/Topway/DoFun stock `twmusic` replacement identity; stock system priv-app conflicts must be managed  |
| `topwayTwMedia` | `com.tw.media`      | DoFun alternate fixed entry exposing `com.tw.media/com.tw.music.MusicActivity`; not a universal no-root bypass |

Only dedicated Topway/DoFun compatibility variants use stock-style identities. The standard variant remains `org.oxycblt.auxio`; `topwayTwMusicRelease` is `com.tw.music`, and `topwayTwMediaRelease` is `com.tw.media`.

## Compatibility and installation scope

Auxio-TS targets compatibility surfaces needed by DoFun Variety and TS18/Topway launchers, such as:

- `com.tw.music` and `com.tw.media` package identities for dedicated compatibility APKs
- `com.tw.music.MusicActivity` launcher/activity alias
- Android `MediaSession` / `MediaBrowserService`
- Topway-style metadata/progress broadcasts
- Topway-style widget control broadcasts
- DoFun launcher/music-widget recognition

Observed private/system/vendor hooks from stock TS18 apps have been treated as reference evidence only so far. Auxio-TS won't copy private system privileges or vendor-only APIs into production code unless a protocol is fully understood, justified, reviewed, and implemented safely.

## Constraints on TS18 firmware

Stock TS* / Topway / DoFun environment has some limits users should know about.

The target TS18 firmware already includes the stock music app as a protected system app `com.tw.music`.
Because of this, the `topwayTwMusicRelease` APK may not install like a normal app on locked stock firmware; To install this (most-integrated) variant, some units may require an advanced install method, such as ADB, Shizuku, root, disabling the stock app for the current user, or matching the system app signature.

Storage and memory may have some practical limits on TS18 units; for this reason, Auxio-TS can limit TS18 system scans to likely music folders by looking for familiar folder-names **containing** keywords such as `music`, `download`, or `media` (case-insensitive).

For the most reliable library scanning and folder selection, keep music in plainly named folders on the default `storage` or `usb` drive. Recommended examples:

```text
/storage/usbdisk0/*
/storage/emulated/0/*
ensure path contains: *music|media|download*
```

Avoid unusual folder names, including names with emoji, invisible characters, excessive punctuation, very long names, or names that only differ by capitalisation or symbols.

**Note:** Auxio-TS includes a manual **Storage Health** diagnostic screen (under Settings > Music > Storage Health) to help debug USB mount points, alias duplication, and noisy folders on TS18 hardware. It runs only on-demand and can export a plain-text report for troubleshooting.

See [`docs/TS18_INSTALLATION_CONSTRAINTS.md`](docs/TS18_INSTALLATION_CONSTRAINTS.md) before treating a Topway APK as installable on locked stock firmware.

## Building

Auxio-TS inherits upstream Auxio’s Android build requirements.

Auxio relies on a patched Media3 setup and native metadata dependencies, so a working Android build environment needs:

- JDK 21
- Android SDK
- CMake / native build tooling where required
- `ninja-build`
- Git submodules initialised recursively

Initial setup:

```sh
git submodule update --init --recursive
bash scripts/prepare-ci-environment.sh
```

Build the standard development APK:

```sh
./gradlew :app:assembleStandardDebug
```

Build the TS18/Topway/DoFun release APKs:

```sh
# Exact stock package replacement target. This conflicts with stock com.tw.music
# unless the install lane handles package state/signing constraints.
./gradlew :app:assembleTopwayTwMusicRelease

# Alternate DoFun fixed-entry target. This is not a universal no-root bypass and
# may still conflict on firmware where com.tw.media already exists.
./gradlew :app:assembleTopwayTwMediaRelease
```

For CI-equivalent local checks:

```sh
bash scripts/check-ts18-apk-reference-contracts.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-headunit-compat-safety.sh
```

## Validation

Before installing on a TS18/head-unit device, check existing packages:

```sh
adb shell 'cmd package list packages | grep -E "com\.tw\.music|com\.tw\.media|org\.oxycblt\.auxio|com\.dofun\.variety"'
```

After installing a Topway-compatible build, validate package/activity/media visibility:

```sh
adb shell cmd package resolve-activity --brief -n com.tw.music/com.tw.music.MusicActivity
adb shell cmd package resolve-activity --brief -n com.tw.media/com.tw.music.MusicActivity
adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.APP_MUSIC
adb shell cmd package query-intent-services -a android.media.browse.MediaBrowserService
adb shell dumpsys media_session | grep -i -A60 'com.tw.music\|com.tw.media\|auxio'
```

Exercise Topway-style controls:

```sh
adb shell am broadcast -a com.tw.music.action.pp
adb shell am broadcast -a com.tw.music.action.next
adb shell am broadcast -a com.tw.music.action.prev
```

See [`docs/TS18_RUNTIME_VALIDATION.md`](docs/TS18_RUNTIME_VALIDATION.md) for the full on-device checklist.

## Documentation

| Doc                                                                              | Purpose                                                 |
| -------------------------------------------------------------------------------- | ------------------------------------------------------- |
| [`docs/README.md`](docs/README.md)                                               | Documentation index                                     |
| [`docs/DOFUN_VARIETY_COMPATIBILITY.md`](docs/DOFUN_VARIETY_COMPATIBILITY.md)     | DoFun/Topway compatibility contract                     |
| [`docs/TS18_APK_REFERENCE.md`](docs/TS18_APK_REFERENCE.md)                       | Compact APK-derived reference evidence                  |
| [`docs/TS18_INSTALLATION_CONSTRAINTS.md`](docs/TS18_INSTALLATION_CONSTRAINTS.md) | Real TS18 package-conflict and install-lane constraints |
| [`docs/TS18_RUNTIME_VALIDATION.md`](docs/TS18_RUNTIME_VALIDATION.md)             | Head-unit validation checklist                          |
| [`docs/CODEX_TS18_DEVICE_CONTEXT.md`](docs/CODEX_TS18_DEVICE_CONTEXT.md)         | Exact TS18 diagnostic context for agents                |
| [`docs/RELEASE_WORKFLOW.md`](docs/RELEASE_WORKFLOW.md)                           | Release process and expected APK assets                 |
| [`docs/DEVELOPMENT.md`](docs/DEVELOPMENT.md)                                     | Local setup, CI, Roborazzi, and repo layout             |

## Contributing

For general Auxio issues, features, or behaviour that also affects the normal upstream app, prefer the upstream project first:

[OxygenCobalt/Auxio](https://github.com/OxygenCobalt/Auxio)

Use this repo for Auxio-TS-specific work, especially:

- TS18/Topway head-unit behaviour
- DoFun Variety Theme widget/panel integration
- `com.tw.music` compatibility APK behaviour
- `com.tw.media` alternate DoFun fixed-entry behaviour
- Topway broadcast/control bridge behaviour
- release and validation workflows for the TS18 variant

Keep changes aligned with upstream Auxio where practical. Avoid unnecessary divergence from upstream unless required for the TS18/Topway/DoFun compatibility target.

## License

Auxio-TS is a downstream derivative of Auxio and remains aligned with upstream Auxio’s free-software licence terms.

Auxio-TS is distributed under the same GPL-3.0-or-later terms. See [`LICENSE`](LICENSE) and [`NOTICE`](NOTICE) where present for licence and attribution details.

You may use, study, share, and improve this software under those GPL terms. Any redistribution or modification must preserve the applicable GPL licence obligations and upstream attribution.
