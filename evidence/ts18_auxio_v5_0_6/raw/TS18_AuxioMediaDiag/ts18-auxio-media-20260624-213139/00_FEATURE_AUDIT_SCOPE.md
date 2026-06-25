# Auxio-TS PR/release audit scope for this capture

This file maps the runtime capture to Auxio-TS PR/release claims that still need exact TS18 evidence.

## Baseline uncertainty

The requested baseline is "since v5.3.0". The accessible `dev` branch currently reports `versionName 5.0.5` / `versionCode 85` in `app/build.gradle`, so the script treats the post-v5.3.0 request as a *feature/change audit scope* rather than assuming a repo tag exists on-device. The installed APK version is captured in `package_table.tsv` and each package dump.

## PR/release descriptions audited into runtime evidence buckets

| Area | Relevant PRs / release-note themes | Runtime evidence captured |
|---|---|---|
| DoFun/Topway launcher compatibility | DoFun fixed slots, `com.tw.media`, `com.tw.music`, Topway activity/service/widget aliases, Topway bridge | package dumps, quick-components, activity/service/broadcast resolver dumps, DoFun package state, logcat Topway/DoFun filters |
| Generic Android media integration | MediaSession, MediaBrowserServiceCompat, MediaStyle notification, Spotify/VLC comparison | `media_session_all.txt`, `notification_all.txt`, per-snapshot filtered dumps, audio focus/policy dumps |
| Notification crash hardening | RemoteViews/bitmap/fallback icon/large-icon safety, minimized notifications | filtered logcat, notification dumps, crash/dropbox logs, Auxio crash reports |
| Playback stability | autoplay, restore state, play/pause retention, shuffle/current-track retention, queue bounds, seek/next/prev | MediaSession playback state/actions, logcat ExoPlayer/PlaybackState/queue/shuffle markers, audio dumps |
| Slow startup and cached library | cached startup, first-run scan gating, avoiding always-scan, startup library policy | logcat startup/indexing markers, Auxio in-app diagnostics, storage and appdata inventories |
| Storage/source handling | MediaStore, SAF, DocumentsUI, manual path, DirectFS, FilteredFS, USB roots, inaccessible vs empty | storage dumps, `/storage` + `/mnt/media_rw` listings, DocumentsUI package dump, logcat DirectFS/SAF/MediaStore/root markers |
| Root gate and DirectFS | RootStateHolder, bounded `su`, timeout/denied handling, shell quoting, protected-root rejection | Magisk/root probes, module inventory, logcat RootGate/RootState/DirectFS markers, safe root listing probes |
| Widget and zero-ID fallback | DoFun fixed cards, Android AppWidget IDs, widget broadcasts, progress broadcasts | appwidget dumps, logcat widget/Topway broadcast markers, package receiver summaries |
| Overlay/floating controls | overlay permission activity, foreground service, boot restore, bounds/insets, top/status-bar and right-edge/nav gesture conflicts | appops, window/input/display/policy dumps, SurfaceFlinger layer list, settings gesture/navigation keys, logcat Overlay/WindowManager markers |
| Earliest autostart/readiness | BOOT_COMPLETED receiver, overlay boot receiver, service.d timing, app start fallback, media session readiness before DoFun | earlyboot snapshot, boot props, broadcast queues, receiver resolver tables, logcat boot/user-unlocked/media markers |
| Playback interruption contexts | audio-focus loss/duck, becoming noisy, MediaSession command source, Bluetooth, radio/NavRadio, reverse/camera, phone/telecom, power/doze | per-snapshot audio focus/policy, media_session, notification, telecom/bluetooth dumps, logcat AUDIOFOCUS/MediaButton/Topway command markers |
| TS18 Health Diagnostics | automated checks, guided/timed/boot capture, DiagnosticService foregrounding, journal categories | attempted DiagnosticService start, logcat DiagnosticService/Journal markers, Auxio-generated files if present |
| Home UI/pills/chips | z-order/clickability fixes, head-unit shortcut chips | window/focus dumps, logcat UI exceptions, manual test observation notes |
| Bluetooth/peripheral work | BTAndroidTS app/module, Android Bluetooth vs Topway Bluetooth separation | package dumps, bluetooth_manager dumps, appops, audio focus logs, com.tw.bt and Android Bluetooth package state |
| Intent bridge / LSPosed/Zygisk | TS18 intent bridge, module state, redirect risks | Magisk module inventory, LSPosed/Zygisk logcat markers, package dumps for bridge app |
| NavRadio+/radio coexistence | NavRadio+, stock radio, DoFun radio widget coexistence | package dumps, audio focus/policy dumps, logcat radio/NavRadio markers |
| Release/CI guardrails | APK identity, variants, package names, release/debug mismatches | package table, package dumps, APK paths, versionName/versionCode, UID/sharedUserId flags |

## Lower-confidence items this capture is meant to prove or disprove

- Whether DoFun reads Auxio through generic active MediaSession ranking, notification MediaStyle, Topway private broadcasts, or a mixture.
- Whether Auxio publishes metadata early enough for DoFun, rather than waiting for artwork/bitmap completion.
- Whether `com.tw.media` behaves better than `org.oxycblt.auxio` in DoFun's widget/window.
- Whether root-gated DirectFS is required for `/storage/usbdiskN` or only raw `/mnt/media_rw` paths.
- Whether DocumentsUI/SAF is now viable on TS18 after module installation.
- Whether notification artwork fixes fully stop TS18 RemoteViews/SystemUI crashes.
- Whether boot/autostart/ACC wake behaviour differs from normal BOOT_COMPLETED.
- Whether BTAndroidTS, intent bridge, NavRadio+, ZLink/Android Auto, or Topway BT/radio services influence media focus/routing.

