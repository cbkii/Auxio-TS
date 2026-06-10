# Stock TW Music contract

## Identity

Stock TW Music is `com.tw.music`, version `TW_THEME.20240715`, min/target SDK 29. The manifest uses `android:sharedUserId="android.uid.system"`, so it is a privileged/system-style stock app. Auxio-TS should not try to emulate privileged identity; it should emulate the external compatibility contract that DoFun and the widget can observe.

Source: `com.tw.music_TW_THEME.20240715/apktool/AndroidManifest.xml` lines 1-45.

## Components

Observed stock components:

- Activity: `com.tw.music.MusicActivity`
  - `launchMode=singleTask`
  - `MAIN` + `LAUNCHER`
- Service: `com.tw.music.MusicService`
  - no manifest intent-filter
  - used by same-app widget PendingIntents and activity binding/start
- Widget provider: `com.tw.music.view.MusicWidgetProvider`
  - intent-filter: `android.appwidget.action.APPWIDGET_UPDATE`
  - metadata: `android.appwidget.provider @xml/appwidget_info`

For an Auxio targetSdk above Android 12 requirements, components with intent-filters must set `android:exported` explicitly. Do not infer stock unspecified `exported` values blindly; stock targets SDK 29.

## Widget provider XML and layout

Appwidget provider:

- default: minWidth `@dimen/tw_dp_w430`, minHeight `@dimen/tw_dp_h200`, updatePeriodMillis `0`, initialLayout `@layout/music_widget`
- sw768dp: minWidth `424dp`, minHeight `194dp`, updatePeriodMillis `0`, initialLayout `@layout/music_widget`

Widget layout IDs:

- `albumart`
- `title`
- `artist`
- `control_prev`
- `control_play`
- `control_next`
- `tv_current_time`
- `tv_duration`
- `seek_bar_progress`

## Incoming controls

Stock widget buttons create `PendingIntent.getService()` calls to `MusicService` with exact actions:

- `com.tw.music.action.prev`
- `com.tw.music.action.pp`
- `com.tw.music.action.next`

Stock service and dynamic receiver also understand:

- action `com.tw.music.action.cmd`
- extra `cmd=prev`
- extra `cmd=next`
- extra `cmd=pp`
- extra `cmd=update`
- extra `appWidgetIds=int[]` for update

Important nuance: `cmd=update` is handled in the dynamically registered broadcast receiver, not in the shown `onStartCommand()` path. Auxio-TS should handle `cmd=update` in both service and receiver paths for robustness.

## Outgoing metadata

Stock code emits:

- action `com.tw.music.info`
- extras:
  - `musicTitle`
  - `musicaArtist`
  - `musicAlbum`
  - `musicPath`

The spelling `musicaArtist` must be preserved.

## Outgoing progress

Stock code emits:

- action `com.tw.launcher.music_progress_duration`
- extras:
  - `msg_music_progress`
  - `msg_music_duration`

The shown tick code re-schedules after 1000 ms, so launcher progress likely expects periodic updates while active/playing.

## Launcher seek/progress input

`MusicActivity` registers a runtime receiver for:

- action `com.android.launcher.widget_music_progress`
- extra `music_progress`

It forwards the integer extra to `seekTo()`.

## Auxio-TS implementation implication

Auxio-TS should implement this as a Topway compatibility layer, not by copying stock implementation. The safe pattern is:

1. Wrapper activity starts/binds/warms Auxio playback state without blocking UI.
2. Wrapper service/receiver accepts all stock actions/extras and forwards to existing playback commands.
3. Widget provider always builds valid Android 10 RemoteViews and simple Prev/Play-Pause/Next PendingIntents.
4. Metadata/progress broadcasts are emitted from Auxio playback state changes and periodic progress ticks.
5. Missing library, no SAF permission, no queue, no current song, no audio focus, inaccessible USB and cold-start races are nonfatal.

See excerpts under `excerpts/widgets/` and `excerpts/broadcasts/`.
