# DoFun generic media compatibility implementation

## Status

Implementation specification for the draft PR that supersedes the current TS18 launcher strategy with a standards-first, evidence-driven generic media compatibility path.

- **Evidence status:** Observed for repository structure, Android media contracts and the captured blocked Auxio playback channel; Inferred for DoFun's use of the generic notification/MediaSession lane.
- **Confidence:** Medium for Android standards compatibility; low-to-medium for exact fixed-widget recognition until physical validation.
- **Porting decision:** Implement and default to the standards-first generic profile on Topway variants, retain explicit legacy adapters as bounded fallbacks, and do not claim exact DoFun acceptance from CI.
- **Device validation:** Exact TS18/DoFun acceptance remains **Requires device validation**.

## Problem statement

Auxio-TS already publishes a capable Android `MediaSessionCompat`, `MediaBrowserServiceCompat`, metadata, queue, playback state, MediaStyle notification, legacy metadata broadcasts, Topway broadcasts, and a verified subset of `com.tw.service.xt.CommandService` callbacks. The DoFun fixed launcher media panel nevertheless does not reliably recognise or control Auxio.

The supplied DoFun APK and comparative app research indicate that DoFun does not use one universal private protocol. Its theme exposes one normalized media surface, while the protected host likely supplies several source adapters:

1. fixed Topway local music (`com.tw.music` / `com.tw.media`);
2. generic Android notification/MediaSession/MediaBrowser control;
3. branded generic sources such as Spotify, YouTube Music and Apple Music;
4. dedicated private adapters for car apps such as Kuwo and QQ Music Car.

The current `com.tw.media` identity may therefore be an identity trap: it is eligible for the fixed local-music slot but may be routed away from the generic Android media lane used by VLC and branded ordinary apps.

## Evidence summary

### Exact DoFun APK

Observed assets include:

- `assets/apps_match_config.json` fixed `music_set` entries for:
  - `com.tw.media/com.tw.music.MusicActivity`;
  - `com.tw.music/com.tw.music.MusicActivity`.
- `assets/apps_config.json` package recognition for:
  - `com.spotify.music`;
  - `com.google.android.apps.youtube.music`;
  - `com.apple.android.music`;
  - `cn.kuwo.kwmusiccar`;
  - `cn.kuwo.tingshucar`;
  - `com.tencent.qqmusiccar`.
- `kp.jar/assets/media/media_config.json` source heads including:
  - `local_music_head`;
  - `kw_music_head`;
  - `spotify_head`;
  - `apple_music_head`;
  - `youtube_music_head`.
- one normalized `medias` presentation surface with title, icon, progress, previous, play/pause and next controls.

### Exact TS18 capture

Observed historical capture:

- Auxio had an active, metadata-rich Android media session;
- Android selected Auxio as the media-button session;
- `com.tw.media.channel.PLAYBACK` had importance `0`;
- no live Auxio MediaStyle notification was present;
- DoFun's notification listener was enabled and connected;
- VLC's playback channel remained enabled.

A blocked notification channel is therefore a first-order compatibility blocker and must be diagnosed explicitly rather than worked around silently.

### Generic Android players

Spotify, YouTube Music, Apple Music and VLC expose the common public substrate:

- one persistent playback service;
- one active media session;
- prompt initial playback state;
- metadata and transport action publication;
- enabled playback notification;
- media-button handling;
- MediaBrowser/controller connectivity where supported.

YouTube Music exposes both an AndroidX media-button receiver and a `MusicBrowserService` handling `android.media.browse.MediaBrowserService` and `android.intent.action.MEDIA_BUTTON`.

Apple's public MusicKit Android sample uses `MediaBrowserServiceCompat`, `MediaSessionCompat`, an explicit callback, media-button handling, a session token and notification lifecycle tied to playback state.

### Dedicated car-app adapters

Kuwo and QQ Music Car prove that vehicle launchers may use dedicated app adapters where a car app exposes one:

- Kuwo: `KWAPI`, Auto SDK service, state listeners and explicit play/pause/previous/next calls;
- QQ Music Car: third-party broadcast protocol and official QPlay/AIDL partner service.

These are architectural precedents only. Auxio must not impersonate Kuwo, QQ Music, platform signing, UID 1000 or partner authority.

## Source/reference links

### Android platform

- Media app architecture: https://developer.android.com/media/legacy/audio/mediabrowserservice
- MediaSession guide: https://developer.android.com/media/legacy/audio/mediasession
- MediaStyle notifications: https://developer.android.com/media/legacy/audio/mediastyle
- Playback resumption: https://developer.android.com/media/implement/surfaces/mobile#resumption
- Notification channels: https://developer.android.com/develop/ui/views/notifications/channels
- MediaButtonReceiver: https://developer.android.com/reference/androidx/media/session/MediaButtonReceiver

### Generic player/controller references

- VLC Android: https://github.com/videolan/vlc-android
- AAIdrive generic `MediaControllerCompat` adapter:
  https://github.com/BimmerGestalt/AAIdrive/blob/72534e5d6bd801ff98476383d3aa7cebbfc1a6f5/app/src/main/java/me/hufman/androidautoidrive/music/controllers/GenericMusicAppController.kt
- AAIdrive media-session discovery:
  https://github.com/BimmerGestalt/AAIdrive/blob/72534e5d6bd801ff98476383d3aa7cebbfc1a6f5/app/src/main/java/me/hufman/androidautoidrive/music/MusicSessions.kt
- Spotify Android SDK/App Remote: https://developer.spotify.com/documentation/android
- Apple MusicKit Android controller:
  https://developer.apple.com/musickit/android/com/apple/android/music/playback/controller/MediaPlayerController.html
- Apple MusicKit Android example:
  https://github.com/fujidaiti/MusicKit-For-Android-Example/blob/f7082bd21713632580627ef6840a74a4a3fb1f33/src/main/java/com/apple/android/music/sdk/testapp/service/MediaPlaybackService.java

### Vehicle-launcher adapters

- Dudu generic system controller:
  https://github.com/ZorroZhou/dudu-launcher/blob/b9ade053676c14b19a2007cebedd2fb516474844/mobile/src/main/java/com/wow/carlauncher/ex/plugin/music/plugin/SystemMusicController.java
- Dudu Kuwo controller:
  https://github.com/ZorroZhou/dudu-launcher/blob/b9ade053676c14b19a2007cebedd2fb516474844/mobile/src/main/java/com/wow/carlauncher/ex/plugin/music/plugin/KuwoMusicController.java
- Dudu QQ Music Car controller:
  https://github.com/ZorroZhou/dudu-launcher/blob/b9ade053676c14b19a2007cebedd2fb516474844/mobile/src/main/java/com/wow/carlauncher/ex/plugin/music/plugin/QQMusicCarController.java
- Tencent Music official QPlay/AIDL demo:
  https://github.com/tencentmusic/QQMusic_Innovation_QPlay_AIDL_OpenID_Demo
- Tencent explicit car-service binding example:
  https://github.com/tencentmusic/QQMusic_Innovation_QPlay_AIDL_OpenID_Demo/blob/6bd8a9e1734e8927b08160440540f44658677f8a/demo/src/main/java/com/tencent/qqmusic/api/demo/util/QPlayBindHelper.kt

## Implementation requirements

### 1. New generic DoFun compatibility profile

Add a standards-first profile that becomes the fresh-install launcher default for Topway variants. Existing persisted valid selections remain explicit user choices and are not overwritten. It must:

- preserve one canonical Auxio playback service, player, queue, MediaSession, notification and audio-focus owner;
- use conventional Android media controls as the primary path;
- keep existing Topway broadcast and CommandService integrations as explicit fallback/diagnostic paths rather than the default authority;
- remain unavailable to the `standard` flavor except for ordinary Android behavior.

### 2. Conventional three-action MediaStyle notification

For the generic DoFun profile, publish exactly:

```text
previous | play/pause | next
```

Requirements:

- AndroidX media-button `PendingIntent`s, not custom implicit app broadcasts;
- compact indices `0,1,2`;
- canonical MediaSession token;
- transport category and public visibility;
- ongoing only while playing;
- paused notification remains available for resumption/control;
- delete/stop intent routed into the canonical service;
- bounded artwork and no OEM-fragile custom RemoteViews;
- stable low-importance channel.

The normal rich Auxio notification remains available outside this compatibility profile.

### 3. MediaSession initialization ordering

Eliminate the active-but-empty discovery window:

1. construct the media-button receiver component/PendingIntent;
2. set flags;
3. set callback;
4. publish an initial `STATE_NONE` state with supported actions;
5. set session activity and queue title;
6. expose the token;
7. activate the session.

No second session may be created.

### 4. Notification-channel diagnostics and recovery

Add a bounded, user-visible playback-notification status row that reports:

- package notifications enabled/disabled;
- playback channel ID and current importance;
- blocked/unavailable/usable state;
- whether Auxio has requested a playback notification during the current process.

Provide a direct settings intent for the exact playback channel. Do not claim or attempt to raise user-controlled importance programmatically.

### 5. Cold controller/media-button restoration

A first `PLAY` or `PLAY_PAUSE` from DoFun after process death/ACC wake must be allowed to trigger bounded saved-state restoration before audio focus already exists.

Requirements:

- continue to reject repeats and non-`ACTION_DOWN` events;
- do not create a second playback stack;
- acquire audio focus only in the playback manager before producing audio;
- preserve current guards for invalid pause/stop/previous/next commands;
- rate-limit exported entry points.

### 6. MediaBrowser readiness

Keep `onGetRoot()` and the root browse path responsive while the library restores:

- token already available;
- no full filesystem scan on the binder thread;
- bounded recent/playable fallback where existing state permits;
- Android 10/API 29 compatibility;
- no duplicate browser or playback service.

### 7. Identity-aware evidence

Expose diagnostic fields sufficient to compare:

- `standard` / `org.oxycblt.auxio`;
- `topwayTwMedia` / `com.tw.media`;
- `topwayTwMusic` / `com.tw.music`.

The code must not merge their package contracts or automatically disable stock TW Music.

### 8. Legacy fallback containment

Existing Topway broadcasts, legacy `metachanged` broadcasts and CommandService callback handling must be:

- explicitly selectable;
- independently logged;
- unable to create duplicate playback reactions;
- disabled by the new generic profile unless a specific fallback is selected;
- preserved for physical comparison and rollback.

## Tests

- notification profile action count/order/compact indices;
- media-button PendingIntent key codes and canonical service target;
- playing/paused ongoing behavior and delete intent;
- channel-state classification;
- exact channel-settings intent;
- session setup order/initial actions through an extracted policy seam;
- cold-play decision matrix;
- flavor/profile defaults and fallback containment;
- no standard-flavor Topway binding/broadcast execution;
- existing Topway contract and Binder tests remain green.

## Physical acceptance matrix

Requires exact TS18 validation for each installed identity:

- notification channel enabled and live MediaStyle notification visible to DoFun;
- DoFun title/artist/art/progress recognition;
- previous/play-pause/next each produce exactly one Auxio action;
- source/icon tap opens the correct Auxio activity;
- no stock TW Music duplicate reaction;
- process death and cold controller play;
- Binder death/reconnect for optional CommandService fallback;
- ACC sleep/wake;
- fallback to ordinary Android media buttons and MediaSession;
- no radio, Bluetooth, DSP, notification or audio-focus regression.

## Non-goals / STOP boundaries

- no platform signing, shared UID or UID 1000 claims;
- no signature-permission assumptions;
- no TWUtil, MCU, CAN, Cardoor or source-forcing writes;
- no fake Spotify/Apple/YouTube/Kuwo/QQ interfaces;
- no copied private vendor smali;
- no stock package deletion or automatic disable;
- no direct `/system` or `/vendor` writes;
- no second player, queue, MediaSession, notification or playback service;
- no claim of physical DoFun acceptance from CI.

## Migration and authority note

The one-time migration writes `GenericDofunMedia` only when no launcher-mode preference exists. It preserves every persisted valid mode, including `AutoAllSafePaths`, because older versions did not record whether that value was a default or an explicit choice. Generic callback registration and a healthy Android media session are compatibility prerequisites, not proof that DoFun selected Auxio as the active fixed-panel source.
