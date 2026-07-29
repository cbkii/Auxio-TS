# ⚙️ Auxio-TS settings guide

This guide explains the settings most useful on a TS18 or Topway head unit. Names below match the maintained Topway builds where possible.

> [!NOTE]
> A firmware or app version may place a setting in a slightly different group. Use the setting name to find it.

## 🧭 Quick navigation

- 🚀 [Recommended first-use setup](#recommended-first-use)
- 📁 [Library and music sources](#library-and-music-sources)
- 🔐 [Root-assisted access](#root-assisted-access)
- ⚡ [Scanning and performance](#scanning-and-performance)
- 📋 [Generated playlists](#generated-playlists)
- ▶️ [Playback and startup](#playback-and-startup)
- 🚘 [Head-unit and DoFun integration](#head-unit-and-dofun-integration)
- 🎨 [Appearance and visualiser](#appearance-and-visualiser)
- 🧰 [Diagnostics](#diagnostics)
- ♻️ [Reset and recovery](#reset-and-recovery)

<a name="recommended-first-use"></a>

## 🚀 Recommended first-use setup

Start with these values. Change one setting at a time after the library loads correctly.

| Setting | Recommended TS18 value | Why |
| --- | --- | --- |
| Music source | **Direct** | Uses known app-readable storage paths. |
| Root storage fast path | **On**, on a rooted TS18 **[beta]** | Requests bounded Magisk access for storage discovery and prepared aliases. |
| Automatic reloading | **Off** | Avoids repeated scanning while first setup is being tested. |
| Startup scanning | **Manual/cache-first** | Keeps startup responsive and uses the existing cache where possible. |
| Generated playlists | **Off** **[beta]** | Keeps optional post-load work separate from the first library load. |
| TS18 launcher integration | **Generic DoFun media** **[beta]** | Uses Android media behaviour first and keeps legacy fallbacks explicit. |
| Detailed performance capture | **Off** | Enable only for a short diagnostic test. |

> [!IMPORTANT]
> Turn on **Root storage fast path** and approve the Magisk prompt before selecting a root-dependent Direct source.

<a name="library-and-music-sources"></a>

## 📁 Library and music sources

Open **Settings → Library & sources → Music sources**.

### 📂 Direct

Use **Direct** for a known folder path. Common examples are:

```text
/storage/usbdisk0/Music
/storage/usbdisk1/Music
/storage/emulated/0/Music
```

Use **Auto-detect USB** when the USB number is not known. Use manual entry when you already know the correct folder.

| Option | Use it when | Notes |
| --- | --- | --- |
| Auto-detect USB | Music is on a TS18 USB drive | Checks common `/storage/usbdiskN` paths. |
| Enter path manually | You know the exact folder | Remove leading or trailing spaces. |
| Folders to load | Only selected folders should be scanned | Music outside these folders is ignored. |
| Excluded folders | A selected source contains unwanted audio | Exclusions are applied inside the chosen source. |

> [!CAUTION]
> Do not use `/mnt/media_rw/usbdiskN` as a saved playback path. Use the matching `/storage/usbdiskN` path.

### 🗂️ File picker

Use the file picker when Android DocumentsUI opens and shows the folder you need. Some TS18 firmware does not include a complete picker. Use **Direct** when the picker is missing, empty or cannot open the selected volume.

### 🗃️ System

Use **System** to load audio indexed by Android. It can be quicker to configure, but it may miss files or include notification, navigation or system sounds.

**Limit system library to music folders** filters likely music, download and media paths. Turn it off only when expected songs are missing.

<a name="root-assisted-access"></a>

## 🔐 Root-assisted access

The maintained Topway builds show these settings under **Advanced source access**:

| Setting | Recommended value | What it does |
| --- | --- | --- |
| Root storage fast path | On only when needed **[beta]** | Gives explicit consent for bounded root storage discovery. Turning it on requests the Magisk grant. |
| Root file access status | Available | Runs or displays the bounded root check. |

Use this order:

1. Turn on **Root storage fast path**.
2. Approve the Magisk superuser request.
3. Open **Root file access status**.
4. Confirm **Available**.
5. Select the Direct source.

If the status is **Denied**, open Magisk and allow Auxio-TS, then retry. If it is **Timed out** or **Unavailable**, use an ordinary readable `/storage/...` path or turn the feature off.

> [!WARNING]
> Root-assisted storage does not make Auxio-TS platform-signed. It does not grant UID 1000, MCU, CAN, DSP, radio or protected-package authority.

<a name="scanning-and-performance"></a>

## ⚡ Scanning and performance

| Setting | Recommended TS18 value | Change it when |
| --- | --- | --- |
| Refresh music | Use first | The source changed or new files were added. Cached tags may be reused. |
| Full rescan | Use only when needed | A normal refresh leaves missing or incorrect metadata. It is slower. |
| Automatic reloading | Off | Enable only after source access is reliable. |
| Startup scanning | Manual/cache-first | Use the cache at startup and refresh after changing USB contents. |
| Use file-system cache | Leave at the current default | It can improve load time but may delay detection of new music. |
| Detailed performance capture | Off | Turn on only for a bounded diagnostic capture. |

**Fast resume status** and cached queue restoration are **[beta]**. They require a usable previous snapshot and a currently available source path.

<a name="generated-playlists"></a>

## 📋 Generated playlists

Generated playlists are optional post-load work **[beta]**. They are off by default and should not control initial source selection or base library loading.

| Setting | Use |
| --- | --- |
| Generated playlists | Enables the feature after the base library is available. |
| Refresh generated playlists | Rebuilds them without rescanning music sources. |
| Generated playlist status | Shows off, waiting, generating, up to date or failed. |

Leave this off during first setup. Enable it after normal songs, albums and artists load correctly.

<a name="playback-and-startup"></a>

## ▶️ Playback and startup

| Setting | Recommended starting value | What it changes |
| --- | --- | --- |
| When the head unit starts | Do nothing | Choose **Open Auxio-TS** or **Restore floating controls only** **[beta]** after normal startup is stable. |
| Display over other apps permission | Allow only for floating controls **[beta]** | Opens Android's permission screen for the floating overlay. |
| Car Floating Controls | Off initially **[beta]** | Enables the movable playback overlay. |
| Launch to Now Playing | Optional | Opens the playback screen without starting audio. |
| Autoplay when Auxio-TS launches | Off initially | Starts playback when Auxio-TS opens. |
| Always play immediately | Off initially | Starts audio even when the previous session was paused. |
| Preserve play/pause state | On | Keeps the current playing or paused state when navigating the queue. |
| Stop playback when Auxio-TS is closed | Off for normal car use | Turning it on stops playback when the app is removed from recents. |
| Volume normalisation | Optional | Applies ReplayGain when suitable tags exist. |

Test startup changes after a normal app restart before testing a full device reboot or ACC sleep and wake.

<a name="head-unit-and-dofun-integration"></a>

## 🚘 Head-unit and DoFun integration

Topway and DoFun integration is **[beta]** because launcher behaviour varies by firmware.

| Setting | Recommended value | Notes |
| --- | --- | --- |
| Left-hand drive layout | Off in right-hand-drive vehicles | Turn on only for a left-hand-drive layout. |
| Show album art in head-unit playback view | On | Keeps artwork visible in the driving playback view. |
| Show dashboard quick access | On | Shows large dashboard links for playback, queue, browse and settings. |
| TS18 launcher integration | Generic DoFun media | Uses Android MediaSession and safe generic behaviour first. |
| Topway widget seek unit | Auto | Change only when physical validation proves the launcher sends another unit. |
| Compatibility status | Read-only check | Shows the current compatibility surfaces. |

> [!NOTE]
> Android media buttons, metadata and playback can work while a fixed DoFun music widget still opens or controls stock `com.tw.music`.

Do not disable, delete or replace stock `com.tw.music` as part of normal setup. Read [TS18 installation constraints](TS18_INSTALLATION_CONSTRAINTS.md) before any exact-package test.

<a name="appearance-and-visualiser"></a>

## 🎨 Appearance and visualiser

| Setting | Suggested start | Notes |
| --- | --- | --- |
| Theme | Automatic | Uses the app's normal light or dark choice. |
| Color scheme | Default | Changes the app accent colours. |
| Black theme | Optional | Uses a pure black dark background. |
| Rounded interface | On | Adds rounded UI elements. |
| Library tabs | Keep defaults first | Reorder or hide tabs after learning the layout. |
| Force square album covers | Off | Crops artwork to a square when enabled. |
| Grid columns | Keep defaults first | Change only when the library layout is too dense or too sparse. |
| Now Playing Visualizer | Off **[beta]** | Android may label its permission as audio recording. Auxio-TS uses the playback audio session and does not record microphone audio. |

Visualizer modes are **Off**, **Fallback (when no artwork)** and **Always**. Test the fallback mode before using **Always** on TS18.

<a name="diagnostics"></a>

## 🧰 Diagnostics

Use diagnostics only for a specific problem and stop when the capture is complete.

| Tool | Use |
| --- | --- |
| TS18 USB source status | Checks configured source state and unavailable paths. |
| Root file access status | Checks bounded root-storage availability. |
| Compatibility status | Summarises media, launcher and fallback readiness. |
| Detailed performance capture, when shown | Records a bounded local timing trace **[beta]**. |
| Export startup report | Shares a text report created by the app. |
| Run head-unit diagnostics | Checks package, root, source, overlay and launcher state. |

> [!CAUTION]
> Controls that disable or restore stock packages are advanced validation tools. They are not normal troubleshooting steps.

<a name="reset-and-recovery"></a>

## ♻️ Reset and recovery

Use the smallest reset that matches the problem:

1. **Refresh music** after files change.
2. **Retry source setup** when a saved source is pending or temporarily unavailable.
3. **Full rescan** when metadata or the tag cache is wrong.
4. Turn **Root storage fast path** off and on again when the Magisk grant was denied or never requested.
5. Reconnect the same USB drive before deleting its saved source.
6. Reset only the affected setting before clearing all app data.

> [!WARNING]
> Clearing app data removes Auxio-TS settings, source configuration, playlists stored only in the app and cached state. Export anything important first.

---

[← Main README](../README.md) · [Advanced and contributing](ADVANCED_AND_CONTRIBUTING.md) · [Documentation index](README.md)
