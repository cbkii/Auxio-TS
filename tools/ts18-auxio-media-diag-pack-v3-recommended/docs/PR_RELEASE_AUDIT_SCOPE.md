# Auxio-TS PR/release audit scope added to diagnostics

This document records the Auxio-TS feature/change areas from PR descriptions, release notes and project conversations that the diagnostics pack now explicitly captures. It is intentionally organised by runtime behaviour rather than only by PR number, because several PRs superseded, merged, or closed parallel work.

## Important version caveat

The requested audit baseline is “since v5.3.0”. The accessible repository metadata checked during this packaging pass shows `app/build.gradle` on `dev` at `versionName 5.0.5` / `versionCode 85`. The diagnostics therefore captures the installed version for every Auxio variant and records runtime evidence for all known post-baseline feature areas, without assuming the local APK/release tag is named exactly the same as the repo version.

## Runtime evidence buckets

| Bucket | PR/release themes included | Diagnostic evidence produced |
|---|---|---|
| APK identity and variants | standard, `com.tw.media`, `com.tw.music`, release/debug suffixes, package conflict risk | `package_table.tsv`, package dumps, APK paths/hashes, UID/sharedUserId flags |
| DoFun/Topway fixed slots | Topway aliases, stock activity/service/widget wrappers, DoFun launcher slot assumptions | `packages/*/quick-components.txt`, resolver dumps, DoFun package dump |
| Generic media integration | Spotify/VLC comparison, Android MediaSession, MediaBrowserService, MediaStyle notification | `media_session_all.txt`, `notification_all.txt`, per-snapshot package filters |
| Metadata/artwork timing | immediate metadata vs delayed bitmap, RemoteViews/SystemUI crash fixes, large-icon fallbacks | filtered logcat, notification dumps, crash/dropbox logs, Auxio crash reports |
| Playback stability | autoplay, restore, play/pause retention, shuffle/current item, queue bounds, seek/next/previous | MediaSession state/actions, ExoPlayer log markers, audio focus/policy dumps |
| Library startup | cached startup, no forced scan, startup policy, preserving inaccessible sources | logcat indexing markers, Auxio diagnostics, storage/appdata inventories |
| Storage and source modes | MediaStore, SAF, DocumentsUI module, direct/manual paths, USB roots, unmount/replug | `storage/`, DocumentsUI dumps, media/document intent resolver dump |
| DirectFS/root gating | RootStateHolder, bounded `su`, timeout/denial, protected root rejection | Magisk module state, safe root probes, DirectFS/RootGate logs |
| Widgets | DoFun widget card, zero-ID fallback, Android AppWidget IDs, progress broadcasts | appwidget dumps, widget/Topway log filters |
| Overlay/floating controls | overlay permission, boot restore, bounds/insets, WindowManager behaviour | appops, window/display/input dumps, overlay log filters |
| TS18 diagnostics | automated/guided/timed/boot captures, DiagnosticService, DiagnosticJournal | DiagnosticService start attempt, journal/logcat markers, copied reports |
| Home UI/pills/chips | clickable head-unit shortcuts and z-order | window/focus/input dumps, UI exception logs |
| BTAndroidTS | Android Bluetooth vs Topway BT, privileged/module experiments, audio focus impacts | package dumps, appops, bluetooth_manager, audio dumps, log filters |
| ts18-intent-bridge | LSPosed/Zygisk/intent redirect module app | package dumps, Magisk module list, Zygisk/LSPosed/intent logs |
| NavRadio+/radio | NavRadio+, stock radio, DoFun radio widget coexistence | package dumps, radio/audio focus logs, media/audio policy dumps |
| Vendor services | `com.tw.service*`, core, carinfoservice, eq, bt, radio, zlink, fota, ylog | package dumps, process/property inventory, filtered logcat |

## PRs/change streams represented

The feature buckets reflect the accessible descriptions for recent Auxio-TS work, including:

- #85 home pills/dashboard chip clickability.
- #87/#88 slow startup/source-selection crash attempts.
- #93 TS18 notification/source-discovery/diagnostics hardening.
- #95/#97/#98/#99/#100 TS18 Health Diagnostics consolidation.
- #96 workflow summary diagnostics, represented by better script output/reporting.
- #102 root-assisted filesystem, DirectFS, album-art modes, autoplay/shuffle stability.
- #103 RootStateHolder/DirectFS hardening.
- #104 root-first filesystem and head-unit stability.
- #105 resource cleanup, listener concurrency, shuffle and filesystem guards.
- #108 DirectFS/root-gating compile/format hardening and Gemini review findings.
- Prior TS18 work around DoFun/Topway wrappers, overlay controls, filtered filesystem, USB source handling, notification stability and audio focus.

## How to use the output

1. Start the capture.
2. Exercise Auxio-TS, VLC and Spotify while DoFun is visible.
3. Share the generated `.tar.gz`.
4. Review `REPORT.md` first, then `00_FEATURE_AUDIT_SCOPE.md` and the listed evidence files for any `UNKNOWN/FAIL` row.
