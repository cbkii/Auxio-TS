<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" width="150" alt="Auxio-TS app icon">
</p>

<h1 align="center">🎵 Auxio-TS</h1>

<p align="center"><strong>A local music player adapted for TS18, Topway and DoFun Variety Android head units.</strong></p>

<p align="center">
  <a href="https://github.com/cbkii/Auxio-TS/releases/latest"><img alt="Latest release" src="https://img.shields.io/github/v/release/cbkii/Auxio-TS?include_prereleases&sort=semver"></a>
  <a href="https://github.com/cbkii/Auxio-TS/releases"><img alt="Total downloads" src="https://img.shields.io/github/downloads/cbkii/Auxio-TS/total"></a>
  <a href="https://github.com/cbkii/Auxio-TS/actions/workflows/android.yml"><img alt="Android Build" src="https://github.com/cbkii/Auxio-TS/actions/workflows/android.yml/badge.svg?branch=dev"></a>
  <a href="https://github.com/cbkii/Auxio-TS/actions/workflows/lint.yml"><img alt="Android Quality" src="https://github.com/cbkii/Auxio-TS/actions/workflows/lint.yml/badge.svg?branch=dev"></a>
  <a href="LICENSE"><img alt="GPL-3.0-or-later licence" src="https://img.shields.io/github/license/cbkii/Auxio-TS"></a>
</p>

<p align="center">
  <img alt="TS18 and Topway" src="https://img.shields.io/badge/TS18-Topway-1450A8">
  <img alt="Target device Android 10" src="https://img.shields.io/badge/target%20device-Android%2010-3DDC84">
  <img alt="Minimum Android API 24" src="https://img.shields.io/badge/minimum-API%2024%2B-3DDC84">
  <img alt="Primary package com.tw.media" src="https://img.shields.io/badge/package-com.tw.media-6f42c1">
  <img alt="DoFun Variety compatibility" src="https://img.shields.io/badge/launcher-DoFun%20Variety-f66a0a">
  <img alt="Optional Magisk package" src="https://img.shields.io/badge/Magisk-optional-00AF9C">
</p>

<p align="center">
  <a href="#-what-is-auxio-ts">About</a> ·
  <a href="#-main-features">Features</a> ·
  <a href="#-quick-start">Quick start</a> ·
  <a href="#-settings-guide">Settings</a> ·
  <a href="#-downloads">Downloads</a> ·
  <a href="#-help-and-guides">Help</a>
</p>

## 🚗 What is Auxio-TS?

Auxio-TS is a local music player for TS18 and related Topway Android head units. It is based on [Auxio](https://github.com/OxygenCobalt/Auxio) and adds a head-unit layout, TS18 storage options and safe Topway or DoFun compatibility paths.

It is designed for Android 10 TS18 systems. It is not a general replacement for every Android music player or every Topway firmware.

> [!NOTE]
> Exact behaviour can vary between firmware builds. Android media controls may work even when a fixed DoFun music widget does not.

## ✨ Main features

- 🎵 Browse and play local music by song, album, artist, genre, folder, search and playlist.
- 🚘 Use a landscape interface, large touch controls and a head-unit dashboard.
- 💾 Choose Direct file access, USB auto-detection, Android's file picker or the Android system library. Root-assisted storage access is available **[beta]**.
- ⚡ Restore a cached library, previous queue and playback state after relaunch or restart **[beta]**.
- 🔊 Use Android MediaSession, MediaBrowser, audio focus and media buttons, with safe Topway and DoFun compatibility paths **[beta]**.
- 🪟 Use floating playback controls and optional startup actions **[beta]**.
- 🎨 Change themes, layout, album artwork and the Now Playing visualiser **[beta]**.
- 📋 Create, import and export playlists. Optional generated playlists are available **[beta]**.

## 📦 Downloads

| Use | Choose |
| --- | --- |
| Most TS18 users | The signed `topwayTwMedia` APK. It installs as `com.tw.media`. |
| Advanced rooted users who need the exact stock package name | The published `topwayTwMusic` Magisk module **[beta]**. Read the installation constraints first. |
| Raw `topwayTwMusic` APK | Do not install or distribute it. It is only an internal Magisk packaging input. |

Download published files from the [Releases page](https://github.com/cbkii/Auxio-TS/releases).

> [!WARNING]
> Stock `com.tw.music` is normally a protected, platform-signed system app. Root does not copy its platform signature or UID 1000 identity. Do not replace it with a normal APK.

Read [TS18 installation constraints](docs/TS18_INSTALLATION_CONSTRAINTS.md) before using the exact-package Magisk module.

## 🚀 Quick start

1. Download the signed `topwayTwMedia` APK from the [Releases page](https://github.com/cbkii/Auxio-TS/releases).
2. Install and open Auxio-TS.
3. Grant the Android storage or notification permissions requested by the app.
4. On a rooted TS18, open **Settings → Library & sources → Advanced source access**.
5. Turn on **Root storage fast path**. This is the consent action that triggers the Magisk superuser request. Approve the request.
6. Open **Root file access status** and confirm that it reports **Available**.
7. Open **Music sources**, choose **Direct**, then use **Auto-detect USB** or enter a path such as `/storage/usbdisk0/Music`.
8. Wait for the first library load. Confirm that your songs appear before changing optional settings.

> [!IMPORTANT]
> On a rooted TS18, grant superuser access before selecting a Direct source. This avoids starting the first Direct scan before root-assisted storage access is ready.

> [!TIP]
> Ordinary readable `/storage/...` paths are tried without root where possible. Root-assisted access is bounded and intended for storage discovery or prepared aliases. It does not authorise system writes, protected-package changes or platform privileges.

## 🖼️ Quick-start screenshots

| 1. Enable root access | 2. Select a Direct source | 3. Confirm the library |
| --- | --- | --- |
| ![Placeholder for the root access settings screenshot](docs/images/coming-soon-1.svg) | ![Placeholder for the Direct source selection screenshot](docs/images/coming-soon-2.svg) | ![Placeholder for the loaded library screenshot](docs/images/coming-soon-3.svg) |

## 📁 Choosing a music source

| Source | Best use | Notes |
| --- | --- | --- |
| **Direct** | TS18 USB drives and known folders | Recommended for paths such as `/storage/usbdisk0/Music`. Root-assisted discovery is **[beta]**. |
| **File picker** | Devices with a working Android DocumentsUI | Some TS18 firmware does not include a complete system file picker. |
| **System** | Music already indexed by Android | Faster to configure, but may miss files or include unwanted system audio. |

Common app-readable paths include:

```text
/storage/usbdisk0/
/storage/usbdisk1/
/storage/emulated/0/
```

Do not save `/mnt/media_rw/usbdiskN` as a normal playback path. It is an internal backing path.

## ⚙️ Settings guide

Open the part of the [settings guide](docs/SETTINGS_GUIDE.md) that matches what you need:

- 🚀 [Recommended first-use setup](docs/SETTINGS_GUIDE.md#recommended-first-use)
- 📁 [Library and music sources](docs/SETTINGS_GUIDE.md#library-and-music-sources)
- 🔐 [Root-assisted access](docs/SETTINGS_GUIDE.md#root-assisted-access)
- ⚡ [Scanning and performance](docs/SETTINGS_GUIDE.md#scanning-and-performance)
- 📋 [Generated playlists](docs/SETTINGS_GUIDE.md#generated-playlists)
- ▶️ [Playback and startup](docs/SETTINGS_GUIDE.md#playback-and-startup)
- 🚘 [Head-unit and DoFun integration](docs/SETTINGS_GUIDE.md#head-unit-and-dofun-integration)
- 🎨 [Appearance and visualiser](docs/SETTINGS_GUIDE.md#appearance-and-visualiser)
- 🧰 [Diagnostics](docs/SETTINGS_GUIDE.md#diagnostics)
- ♻️ [Reset and recovery](docs/SETTINGS_GUIDE.md#reset-and-recovery)

## 🧰 Common problems

### 🚫 Magisk did not ask for superuser access

Turn **Root storage fast path** off and on again. Approve the prompt, then open **Root file access status**. Do not continue with a root-dependent Direct path unless the status is **Available**.

### 🎵 No songs appear

Check the selected path, then use **Refresh music**. Use **Full rescan** only when a normal refresh does not repair the library.

### 💾 A USB source is unavailable

Keep the cached library, reconnect the same USB drive and check **TS18 USB source status**. Avoid deleting and recreating the source unless the stored path is wrong.

### 🧩 The DoFun widget does not control Auxio-TS

Android media buttons and metadata can work without fixed-widget control. Keep **Generic DoFun media** as the recommended integration mode. See the [DoFun compatibility guide](docs/DOFUN_VARIETY_COMPATIBILITY.md) for known limits.

## 📚 Help and guides

- 🙋 [Settings guide](docs/SETTINGS_GUIDE.md)
- 🧑‍💻 [Advanced use and contributing](docs/ADVANCED_AND_CONTRIBUTING.md)
- 🧭 [Documentation index](docs/README.md)
- 🛡️ [TS18 installation constraints](docs/TS18_INSTALLATION_CONSTRAINTS.md)
- 🧩 [DoFun Variety compatibility](docs/DOFUN_VARIETY_COMPATIBILITY.md)
- 🧪 [Physical TS18 validation](docs/TS18_RUNTIME_VALIDATION.md)
- 🐞 [Report a problem](https://github.com/cbkii/Auxio-TS/issues)

When reporting a problem, include the Auxio-TS version, package name, TS18 build, selected source mode and the exact path used. Do not upload private files, credentials or personal data.

## 💛 Acknowledgements, licence and donations

Auxio-TS is based on [Auxio](https://github.com/OxygenCobalt/Auxio), created by Alexander Capehart (OxygenCobalt). The original music player, design and playback architecture remain the foundation of this project.

Support the original author through [GitHub Sponsors](https://github.com/sponsors/OxygenCobalt).

Auxio-TS is licensed under GPL-3.0-or-later. See [LICENSE](LICENSE) and [NOTICE](NOTICE).
