# Research notes

## Internet / official documentation used

- Android `dumpsys` documentation: confirms `dumpsys` is the correct command-line tool for system-service state capture and supports service-specific output.
- Android `MediaSessionManager` API reference: active sessions are exposed through media session controllers to callers with the required privilege/notification-listener access; this is the likely generic path DoFun uses for VLC/Spotify.
- Android Media3 background playback documentation: current Android media apps should expose a media session service and a media-style notification for external/system controllers.
- Magisk developer guide: `service.d` is the appropriate late-start boot script location for non-blocking post-boot diagnostics; `post-fs-data` should not be used for long-running capture.

## Russian and Chinese source status

Searches were attempted for Russian/4PDA and Chinese TS10/TS18/Topway/UIS8581 media-widget material. Public search results were sparse/unreliable in this environment, and 4PDA direct fetches were not usable due decoding/access issues. Therefore, this pack does not rely on unverified forum claims.

Instead, the diagnostic scope uses:

- exact-device project captures;
- uploaded TS18/Baidu path listings preserving Chinese filenames and firmware groupings;
- uploaded Topway/DoFun/stock APKs;
- Android official media/session/dumpsys documentation;
- Magisk official documentation;
- current Auxio-TS repository/PR evidence.

## Project-source evidence considered

The scope intentionally covers components found in the TS18 root diagnostics and project conversations:

- DoFun Variety / TWTHEME launcher;
- `com.tw.music`, `com.tw.media`, `com.tw.radio`, `com.tw.bt`, `com.tw.eq`;
- `com.tw.service`, `com.tw.core`, `com.tw.coreservice`, `com.tw.carinfoservice`, `com.tw.reverse`, `com.tw.devicefan`;
- ZLink / Android Auto projection;
- MediaProvider / DocumentsUI / DownloadProvider;
- Magisk modules, service.d and post-fs-data.d;
- ylog/yloglite / vendor Unisoc logging;
- `/storage/usbdiskN` and `/mnt/media_rw` USB paths;
- Auxio-TS, BTAndroidTS, ts18-intent-bridge, NavRadio+.

## Why the script is broad

Auxio-TS failures can appear as:

- MediaSession missing or low priority;
- notification/RemoteViews bitmap crash;
- audio focus denial;
- ExoPlayer state error;
- DoFun/Topway private broadcast mismatch;
- storage source resolving to empty/inaccessible;
- DirectFS/root gate timeout or denial;
- DocumentsUI/SAF picker failure;
- overlay/window permission failure;
- package identity/alias mismatch;
- vendor audio service/radio/BT interaction;
- boot/autostart timing failure.

The collector therefore captures packages, services, audio, notification, media-session, storage, Magisk, logs, power/window state and per-package snapshots over time.
