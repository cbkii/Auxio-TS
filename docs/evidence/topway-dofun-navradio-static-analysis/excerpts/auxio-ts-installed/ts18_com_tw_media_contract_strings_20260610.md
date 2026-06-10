# TS18 installed Auxio-TS APK excerpt: Topway contract strings

Source: `ts18_dofun_runtime_validation_20260610_125608/apks/com.tw.media/base.apk` via `strings` over `classes*.dex`.

Type: Observation with limitation.

Why it matters: The installed Auxio-TS `com.tw.media` APK contains the expected Topway action/extra strings and wrapper class names. This confirms the build contains related code paths, but it does **not** prove manifest registration/exported state or correct runtime handling.

```text
	musicPath
  applicationId: com.tw.media
 com.tw.media.action.OPEN_ARTISTS
 com.tw.media.image.CoverProvider
!com.tw.media.action.WIDGET_UPDATE
!com.tw.media.key.FILE_INTENT_USED
"com.tw.media.action.OPEN_PLAYLISTS
#com.tw.media.action.OPEN_FAVOURITES
#com.tw.media.key.PENDING_SEPARATORS
$com.tw.media.action.OPEN_NOW_PLAYING
%com.tw.media.action.ENTRY_DESTINATION
'Lcom/tw/music/view/MusicWidgetProvider;
'com.tw.launcher.music_progress_duration
'com.tw.media.action.OPEN_RECENTLY_ADDED
'com.tw.media.car.overlay.RESET_POSITION
(androidx.media3.decoder.flac.FlacLibrary
)com.tw.media.car.overlay.AUXIO_FG_CHANGED
*androidx.media3.decoder.flac.FlacExtractor
*androidx.media3.decoder.midi.MidiExtractor
*com.android.launcher.widget_music_progress
+com.tw.media.action.OPEN_HEAD_UNIT_SETTINGS
.androidx.media3.datasource.rtmp.RtmpDataSource
4androidx.media3.exoplayer.hls.HlsMediaSource$Factory
6Lorg/oxycblt/auxio/music/service/MusicServiceFragment;
6androidx.media3.exoplayer.dash.DashMediaSource$Factory
6androidx.media3.exoplayer.rtsp.RtspMediaSource$Factory
8androidx.media3.exoplayer.mediacodec.MediaCodecRenderer_
>Lorg/oxycblt/auxio/music/service/MusicServiceFragment$Factory;
?Lorg/oxycblt/auxio/music/service/MusicServiceFragment$search$1;
?androidx.media3.exoplayer.smoothstreaming.SsMediaSource$Factory
Lcom/tw/music/MusicService;
MusicWidgetProvider
PLorg/oxycblt/auxio/music/service/MusicServiceFragment$$ExternalSyntheticLambda0;
androidx.media3.common.Timeline
com.tw.media.action.EXIT
com.tw.media.action.LOOP
com.tw.media.action.NEXT
com.tw.media.action.OPEN_ALBUMS
com.tw.media.action.OPEN_GENRES
com.tw.media.action.OPEN_QUEUE
com.tw.media.action.PLAY_PAUSE
com.tw.media.action.PREV
com.tw.media.action.SHUFFLE
com.tw.media.action.SHUFFLE_ALL
com.tw.media.car.overlay.HIDE
com.tw.media.car.overlay.SHOW
com.tw.media.car.overlay.START
com.tw.media.car.overlay.STOP
com.tw.media.car.overlay.TOGGLE
com.tw.media.category
com.tw.media.channel.INDEXER
com.tw.media.channel.PLAYBACK
com.tw.media.detail.DUAL_PANE
com.tw.media.detail.SCROLLED
com.tw.media.item
com.tw.media.key.CHILD_OF
com.tw.media.key.PENDING_ACCENT
com.tw.media.key.PENDING_TABS
com.tw.media.metadata.PARENT
com.tw.media.metadata.QUEUE_POS
com.tw.media.service.START
com.tw.media.service.START_ID
com.tw.media.tag.INT_PREF
com.tw.media:IndexingComponent
com.tw.music.action.cmd
com.tw.music.action.next
com.tw.music.action.pp
com.tw.music.action.prev
com.tw.music.info
msg_music_duration
msg_music_progress
musicAlbum
musicTitle
music_progress
musicaArtist
```
