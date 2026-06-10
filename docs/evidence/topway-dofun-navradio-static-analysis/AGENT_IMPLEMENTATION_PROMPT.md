# Agent implementation prompt — Auxio-TS DoFun/TW Music widget integration

You are working in the Auxio-TS repository. Implement evidence-based Topway TS18 / DoFun Variety fixed Music widget compatibility for the `com.tw.media` Topway variant.

## Target environment

- Device: Topway TS18 / `s9863a1h10_Natv`
- Android: 10 / SDK 29
- Display: 1280×720 landscape, launcher status/nav insets
- Launcher: `com.dofun.variety`
- Stock music app: `com.tw.music`, system priv-app, cannot be assumed removable
- Auxio-TS variant: `com.tw.media`
- Known DoFun launch targets: `com.tw.music/com.tw.music.MusicActivity` and `com.tw.media/com.tw.music.MusicActivity`

## Evidence to read first

Read:

- `docs/evidence/topway-dofun-navradio-static-analysis/SUMMARY.md`
- `docs/evidence/topway-dofun-navradio-static-analysis/CONTRACT_MATRIX.md`
- `docs/evidence/topway-dofun-navradio-static-analysis/TW_MUSIC_STOCK_CONTRACT.md`
- `docs/evidence/topway-dofun-navradio-static-analysis/TS18_RUNTIME_VALIDATION_20260610_FINAL.md`
- `docs/evidence/topway-dofun-navradio-static-analysis/excerpts/`

Do not copy proprietary APK source. Use the excerpts as behavioural evidence only.

## Key runtime conclusion

On the real TS18, Auxio-TS `com.tw.media` is already visible to Android media sessions, but DoFun's fixed Music widget still opens/controls stock `com.tw.music` while stock is installed. Therefore, a generic MediaSession-only fix is insufficient. Implement the stock Topway/TW Music contract first. Media3 is optional/additive only if implemented as a real service, not a manifest-only fake.

## Required compatibility contract

Implement or verify the `com.tw.media` Topway variant exposes and correctly handles:

### Components

- `com.tw.media/com.tw.music.MusicActivity`
- `com.tw.media/com.tw.music.MusicService`
- `com.tw.media/com.tw.music.view.MusicWidgetProvider`

Ensure manifest entries are correct for Android 10 and future exported-state rules. Wrapper components must cold-start safely and must not crash on missing library, missing SAF permission, null playback state, or no current song.

### Incoming service/receiver actions

Handle both explicit stock actions and `cmd` extras:

- `com.tw.music.action.prev`
- `com.tw.music.action.pp`
- `com.tw.music.action.next`
- `com.tw.music.action.cmd` with `cmd=prev|next|pp|update`

`cmd=update` must update state only and must not start playback. Malformed or missing `cmd` must be a logged safe no-op.

### Outgoing metadata broadcast

Emit:

- action: `com.tw.music.info`
- extras:
  - `musicTitle`
  - `musicaArtist`  ← preserve exact spelling
  - `musicAlbum`
  - `musicPath`

Emit on track/state changes, after update requests, and when publishing safe placeholder state.

### Outgoing progress broadcast

Emit:

- action: `com.tw.launcher.music_progress_duration`
- extras:
  - `msg_music_progress`
  - `msg_music_duration`

Emit during playback ticks, after seek, after track changes, after update requests, and when publishing placeholder state.

### Launcher seek receiver

Handle:

- action: `com.android.launcher.widget_music_progress`
- extra: `music_progress`

Clamp invalid values. If player/current song is unavailable, log and safely ignore.

### Widget/RemoteViews behaviour

Implement/verify `com.tw.music.view.MusicWidgetProvider` uses Android 10-compatible `RemoteViews` and simple external controls:

- Previous
- Play/Pause
- Next

Update even when normal `AppWidgetManager` widget IDs are zero if the Topway wrapper/fixed launcher card needs a broadcast/state path. Do not expose repeat/shuffle as the primary external Topway controls.

## Repo areas to inspect/change

Search and modify as appropriate:

- `app/src/topwayCompat/AndroidManifest.xml`
- Topway source-set wrappers under `app/src/topwayCompat/`
- `com.tw.music.MusicActivity` wrapper
- `com.tw.music.MusicService` wrapper/bridge
- `com.tw.music.view.MusicWidgetProvider`
- media-button receiver/service routing under main Auxio playback service code
- notification/media action construction for Topway variants
- docs/tests around TS18 runtime validation

Do not remove existing fallbacks unless superseded by verified evidence.

## Tests expected

Add or update tests for:

- command parsing: actions and `cmd=prev|next|pp|update`
- safe no-op cases: empty library, no current song, no permission, null player, malformed extras
- outgoing metadata/progress extras and exact spellings
- widget update with zero normal widget IDs
- Android 10 background/foreground service constraints
- Topway-specific external actions remain simple Previous / Play-Pause / Next

If instrumentation is unavailable, add unit-testable command parser/broadcast builder abstractions and clear manual validation docs.

## Explicit non-goals / constraints

- Do not claim DoFun integration is fixed without TS18 runtime validation.
- Do not implement a fake Media3 service by only adding a manifest action.
- Do not copy NavRadio+, DoFun, or TW Music proprietary source.
- Do not assume stock `com.tw.music` can be removed, disabled, or hidden.
- Do not break standard Auxio behaviour outside Topway variants.
- Do not mask failures with broad cleanup. Log cause-specific safe no-ops.
