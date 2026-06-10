# Summary

## High-confidence observations

1. DoFun Variety configuration explicitly recognises the music hotseat target as both `com.tw.media/com.tw.music.MusicActivity` and `com.tw.music/com.tw.music.MusicActivity`.
2. The stock TW Music APK package is `com.tw.music`, version `TW_THEME.20240715`, `minSdkVersion=29`, `targetSdkVersion=29`; it is a system-style package using `android:sharedUserId="android.uid.system"`.
3. Stock TW Music exposes `com.tw.music.MusicActivity`, `com.tw.music.MusicService`, and `com.tw.music.view.MusicWidgetProvider`.
4. Stock TW Music widget controls use simple `RemoteViews` buttons for previous, play/pause, and next.
5. Stock TW Music widget buttons target `MusicService` via `PendingIntent.getService()` with exact actions `com.tw.music.action.prev`, `com.tw.music.action.pp`, and `com.tw.music.action.next`.
6. Stock TW Music uses `com.tw.music.action.cmd` with extra `cmd=update` and `appWidgetIds` for widget refresh flow.
7. Stock TW Music emits `com.tw.music.info` metadata broadcasts with extras `musicTitle`, `musicaArtist`, `musicAlbum`, and `musicPath`.
8. Stock TW Music emits `com.tw.launcher.music_progress_duration` progress broadcasts with `msg_music_progress` and `msg_music_duration` in a 1000 ms tick loop.
9. Stock TW Music listens for `com.android.launcher.widget_music_progress` with extra `music_progress` and forwards it to seek.
10. NavRadio+ exposes a real `androidx.media3.session.MediaSessionService`, exported AppWidget providers, simple widget broadcast controls, and a floating overlay service; this is useful comparator evidence but not the stock Topway music-widget contract.

## Core inference

Auxio-TS should prioritise faithful Topway/TW Music widget and broadcast compatibility over a generic MediaSession-only implementation. Media3 may still be valuable, especially because NavRadio+ moved to a real Media3 service, but a Media3 service alone will not satisfy the exact DoFun/TW Music contract found in the static evidence.

## Main risk

DoFun's decompiled code is protected/stubbed. Its asset configs prove launch-target recognition, but they do not statically prove every runtime music-widget interaction. The least risky Auxio-TS path is to satisfy both surfaces: the stock Topway component/action/broadcast contract and a modern media-session/simple-transport surface.

## Runtime validation update — 2026-06-10

The TS18 runtime validation archive adds two important observations:

1. With stock `com.tw.music`, Auxio-TS `com.tw.media`, DoFun Variety, and NavRadio+ all installed, the fixed DoFun Music widget opened/controlled **stock `com.tw.music`**, not Auxio-TS. Window state after the widget tap corroborated `com.tw.music/.MusicActivity` as the launched activity.
2. Auxio-TS was nevertheless visible to Android's media-session stack on the TS18 (`Media button session is com.tw.media/com.tw.media`, active session, metadata, queue, and media button receiver present). Therefore the remaining DoFun Music widget gap is not generic media-session visibility alone.

This strengthens the evidence-based implementation priority: satisfy the stock TW Music component/action/broadcast/widget contract first; treat Media3 as additive comparator work only; keep overlay controls as a fallback.

See `TS18_RUNTIME_VALIDATION_20260610.md` and `excerpts/runtime/` for curated runtime evidence.


## Final runtime addendum — v2 run, 2026-06-10

The second TS18 runtime run confirmed the fixed DoFun launcher layout: one Music widget, one Radio widget, and no custom widget host. Manual validation reported that the fixed Music widget opens/controls stock `com.tw.music`, does not control Auxio-TS `com.tw.media`, and that stock TW Music metadata/progress update correctly. Auxio-TS remains visible to Android media sessions, so the missing integration is not generic MediaSession visibility.

Caution: v2 synthetic probes inherited `USER_ID=10177` from TermOnePlus and therefore used the wrong Android user. Treat the manual observations and passive `dumpsys` evidence as useful; treat v2 synthetic `am`, `pm`, and `cmd package` results as invalid unless they were passive state dumps. The final collector script is corrected to use `ANDROID_USER_ID=0` by default.
