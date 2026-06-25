# Stock `com.tw.music`, VLC, and Auxio-TS media-control evidence

This file is a handoff summary for Codex. It intentionally points Codex to exact upstream/private repo paths instead of copying large source trees. Codex may use `GH_TOKEN` or `GITHUB_TOKEN` to clone/fetch `cbkii/t-music` and the public VLC repo.

## Decision on Spotify

The TS18 test result says Spotify no longer integrates with DoFun. Treat Spotify as a negative control only. Do not spend implementation time reverse-engineering Spotify. Focus on:

1. stock `cbkii/t-music` / stock `com.tw.music`;
2. public VLC Android, because it still partially integrates with DoFun by exposing title and play/next/prev controls;
3. Auxio-TS current public Android surfaces.

## Prior TS18 project precedent to preserve

- Keep one canonical playback/session owner. Topway-compatible aliases should delegate into the same playback/session state rather than fork playback.
- DoFun fixed entries use `com.tw.music/com.tw.music.MusicActivity` and `com.tw.media/com.tw.music.MusicActivity`; test `MAIN`, `LAUNCHER`, `APP_MUSIC`, and `MUSIC_PLAYER` categories where applicable.
- Topway outgoing metadata/progress broadcasts and command mapping belong in a topwayCompat-only bridge; do not affect the standard variant.
- Do not fake TWUtil/Cardoor/private vendor binders in production.

## Stock `com.tw.music` evidence to inspect in `cbkii/t-music`

Repository: `cbkii/t-music`
Observed default branch: `main`
Useful commit in connector results: `2723537ea2be008ac492459592f6dc1f7ba7dc84`

Key files:

- `app/apktool/AndroidManifest.xml`
- `reference/jadx-raw/sources/com/p060tw/music/MusicService.java`
- `reference/jadx-raw/sources/com/p060tw/music/C0781k.java`
- `reference/jadx-raw/sources/com/p060tw/music/view/MusicWidgetProvider.java`
- `docs/playback-symbol-map.md`
- `docs/media-controls-design.md`
- `src-shim/java/com/tw/music/media/MediaControlBridge.java`
- `src-shim/java/com/tw/music/media/PlaybackStateMapper.java`
- `src-shim/java/com/tw/music/media/MediaMetadataMapper.java`
- `src-shim/java/com/tw/music/media/MediaNotificationController.java`

Observed from repo inspection:

- The stock manifest is `package="com.tw.music"`, `compileSdkVersion=29`, and declares `android:sharedUserId="android.uid.system"`. Auxio-TS normal APK cannot reproduce UID 1000/platform signing by package name alone.
- Stock manifest declares `WRITE_EXTERNAL_STORAGE`, `SYSTEM_ALERT_WINDOW`, `SYSTEM_OVERLAY_WINDOW`, `BLUETOOTH`, and `BLUETOOTH_ADMIN`; do not blindly copy privileged/system-only permissions into normal Auxio-TS.
- Stock `MusicActivity` has `launchMode="singleTask"` and `MAIN`/`LAUNCHER`.
- Stock `MusicService` registers a runtime receiver for `com.tw.music.action.cmd`, `.prev`, `.next`, `.pp`.
- `MusicService.onStartCommand()` handles the same command surface and returns `1` (`START_STICKY`). Null intent also returns `1`.
- `C0781k` command receiver mirrors prev/next/play-pause and handles `cmd=update` with `appWidgetIds`.
- `MusicWidgetProvider.onUpdate()` starts `MusicService`, sends sticky broadcast `com.tw.music.action.cmd` with `cmd=update` and `appWidgetIds`, and its widget button `PendingIntent`s target `MusicService` by explicit component using `getService()`.
- Stock widget updates title, artist, progress/duration, and album art, but protects album art by byte-count cap (`<= 3680000`) and falls back to drawable album art.

Implementation implications for Auxio-TS:

- Mirror the stock command surfaces exactly in topwayCompat: action-only and `cmd` extra forms for `.cmd`, `.prev`, `.next`, `.pp`, plus `cmd=update` and `appWidgetIds`.
- Provide explicit component service routing for `com.tw.music.MusicService` alias where possible, but delegate into canonical Auxio playback state.
- Consider `START_STICKY` only for topwayCompat service aliases and overlay service when enabled; do not introduce endless loops when disabled/permission missing.
- Publish widget/launcher metadata immediately from cached now-playing state, then reconcile after library load.
- Preserve typo-compatible fields from earlier Topway bridge work, including `musicaArtist`, only if current Auxio-TS contract/tests already require it.
- Do not copy shared UID or private permissions into normal APK.

## VLC evidence to inspect

Repository: `videolan/vlc-android` public GitHub mirror
Useful commit in connector results: `9dcdea25dc22400fe8d2c8f8f06b46871aed4b37`

Key files:

- `application/vlc-android/src/org/videolan/vlc/PlaybackService.kt`
- `application/vlc-android/src/org/videolan/vlc/MediaSessionCallback.kt`
- `application/vlc-android/src/org/videolan/vlc/gui/helpers/NotificationHelper.kt`

Observed from repo inspection:

- VLC `PlaybackService` is a `MediaBrowserServiceCompat`.
- VLC keeps `MediaSessionCompat` in the playback service and initializes it with a media-button receiver component and `MediaButtonReceiver.buildMediaButtonPendingIntent`.
- VLC sets `FLAG_HANDLES_MEDIA_BUTTONS | FLAG_HANDLES_TRANSPORT_CONTROLS`, sets callback, sets initial playback state, and marks the session active.
- VLC `onPlay()` loads last audio when there is no current media. Media-button play/play-pause also triggers `PlaybackService.loadLastAudio()` when no media is loaded.
- VLC metadata includes title, media ID, genre, track number, artist, album artist, album, duration, display title/subtitle/description, album-art URI, and bounded bitmap art where allowed.
- VLC playback state exposes play/pause/stop, next/previous where available, seek/custom actions, active queue item, repeat/shuffle modes, extras, queue title, and active/inactive transitions.
- VLC sends legacy broadcast `com.android.music.metachanged` with `track`, `artist`, `album`, `duration`, `playing`, and `package=org.videolan.vlc`. This is a strong candidate for why DoFun sees VLC's title even if DoFun is not using MediaSession metadata.
- VLC creates/updates notification from the MediaSession token and handles notification exceptions defensively on bad firmwares.

Implementation implications for Auxio-TS:

- Do not analyse Spotify further. Use VLC and stock app as positive evidence.
- Ensure Auxio-TS topwayCompat publishes a robust Android-standard `MediaSessionCompat` early, active, with media-button flags and transport controls.
- Add/verify a `MediaBrowserServiceCompat` surface or service alias if Auxio-TS's current browser service is not visible through the `com.tw.music`/topwayCompat identity.
- Add legacy broadcast compatibility, topwayCompat-only by default, especially `com.android.music.metachanged`, with VLC-compatible extras.
- Keep Topway private/stock command broadcasts separate from Android-standard session/legacy metadata broadcasts.
- Make `onPlay`/media-button play invoke fast resume when no current song is loaded, similar to VLC `loadLastAudio()`.
- Publish cached metadata before full library scan.
- Use bounded bitmap handling and notification exception guards.
