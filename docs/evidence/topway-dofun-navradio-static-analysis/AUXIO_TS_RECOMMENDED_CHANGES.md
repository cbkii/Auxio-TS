# Recommended Auxio-TS changes

These recommendations translate static DoFun/TW Music/NavRadio evidence into implementation tasks. Keep them Topway/DoFun variant-scoped unless a change is clearly safe upstream-compatible behaviour.

## Priority 1: preserve exact DoFun launch identity

Ensure the Topway `com.tw.media` variant exposes:

- package: `com.tw.media`
- activity class: `com.tw.music.MusicActivity`
- activity behaviour: exported, MAIN/LAUNCHER-compatible, cold-start safe, `singleTask` or equivalent where applicable

Keep the existing `com.tw.music` package/component strategy only where it is actually installable. On no-root TS18 with stock `com.tw.music` installed, `com.tw.media/com.tw.music.MusicActivity` is the practical replacement path.

## Priority 2: implement stock widget/provider contract

Implement/verify:

- `com.tw.music.view.MusicWidgetProvider`
- `android.appwidget.action.APPWIDGET_UPDATE`
- `android.appwidget.provider` metadata pointing to a Topway widget info XML
- RemoteViews with stable IDs/semantics for album/title/artist/prev/play-next/current/duration/progress
- simple primary controls: Previous / Play-Pause / Next

Do not make repeat/shuffle the first-class launcher-facing controls for the Topway widget surface.

## Priority 3: implement command bridge

Accept all of these, safely and idempotently:

- action `com.tw.music.action.prev`
- action `com.tw.music.action.next`
- action `com.tw.music.action.pp`
- action `com.tw.music.action.cmd` + `cmd=prev`
- action `com.tw.music.action.cmd` + `cmd=next`
- action `com.tw.music.action.cmd` + `cmd=pp`
- action `com.tw.music.action.cmd` + `cmd=update`

Recommended behaviour:

- `update`: refresh widget/broadcast state only; do not start playback.
- `prev`/`next`: perform command if queue/player ready; otherwise safe no-op with diagnostic log and placeholder broadcast/widget update.
- `pp`: if playing, pause; if paused/stopped, try resume; if no queue/current song, use a conservative Topway-only restore fallback or safe no-op with visible/logged state.
- invalid or missing `cmd`: safe no-op.

Prefer a dedicated exported bridge receiver for external implicit broadcasts plus service handling for same-app PendingIntent/service paths. Do not over-export the playback service without reviewing attack surface.

## Priority 4: emit TW Music broadcasts

Emit metadata/state:

```text
com.tw.music.info
  musicTitle
  musicaArtist
  musicAlbum
  musicPath
```

Emit progress:

```text
com.tw.launcher.music_progress_duration
  msg_music_progress
  msg_music_duration
```

Trigger broadcasts on:

- track change
- playback state change
- progress tick while playing
- seek
- `cmd=update`
- widget update
- cold start once state is known or placeholder state is required

Preserve exact spelling, especially `musicaArtist`.

## Priority 5: implement launcher seek input

Accept:

```text
com.android.launcher.widget_music_progress
  music_progress=<int>
```

Clamp negative/out-of-range positions, require player readiness, and log nonfatal rejected seeks.

## Priority 6: keep Android 10 compatibility boundaries

The TS18 is Android 10/SDK29. Avoid Android 12+ assumptions in the Topway runtime path. For foreground services and notification actions, keep API guards explicit and avoid `specialUse` service types for API29.

## Priority 7: consider real Media3 shim after stock contract

NavRadio+ supports the hypothesis that a real Media3 `MediaSessionService` may improve launcher/controller compatibility. If implemented, it must be a real Topway-only Media3 shim that forwards commands/state to existing Auxio playback. A manifest-only Media3 service declaration is misleading and should not be merged.

## Priority 8: file-picker and overlay improvements

File picker: keep SAF/file-picker source selection robust for Android 10 and limited file managers. NavRadio+ provides only comparator evidence here.

Overlay: keep as fallback UX. Use normal `SYSTEM_ALERT_WINDOW` flow only. Do not rely on stock TW Music privileged `SYSTEM_OVERLAY_WINDOW` or system UID.

## Suggested repo areas to inspect

Use actual current repo paths. Likely areas include:

- `app/src/topwayCompat/AndroidManifest.xml`
- `app/src/topwayCompat/**/com/tw/music/**`
- `app/src/topwayCompat/**/MusicWidgetProvider*`
- `app/src/main/java/org/oxycblt/auxio/playback/service/**`
- `app/src/main/java/org/oxycblt/auxio/widgets/**`
- `app/src/main/res/xml/*appwidget*`
- Gradle source-set/flavour configuration for `topwayTwMusic` / `topwayTwMedia`
- tests under `app/src/test`, `app/src/androidTest`, or existing Robolectric setup

## Runtime-prioritised changes after TS18 validation, 2026-06-10

The TS18 runtime run showed Auxio-TS is already Android media-session visible, but the fixed DoFun Music widget still opened/controlled stock `com.tw.music` while both stock and Auxio were installed. Therefore:

1. Treat DoFun/TW Music private compatibility as the primary fix path, not generic media-session discovery.
2. Verify the Topway `com.tw.media` manifest actually exports `com.tw.music.MusicActivity`, `com.tw.music.MusicService`, and `com.tw.music.view.MusicWidgetProvider` with Android 10-compatible declarations.
3. Ensure `com.tw.music.MusicService` accepts both direct service actions and `com.tw.music.action.cmd` extras for `prev`, `pp`, `next`, and `update`, with safe cold-start/no-library/no-permission behaviour.
4. Emit `com.tw.music.info` and `com.tw.launcher.music_progress_duration` from actual playback state changes and from explicit update requests.
5. Investigate DoFun package-selection coexistence. Static config contains `com.tw.media`, but runtime still selected stock `com.tw.music`. Add docs/UX guidance for testing `com.tw.media` only after stock disable/hide/per-user-uninstall attempts, but do not assume those are possible without root.
6. Preserve overlay/floating controls as a validated fallback on TS18.
7. Keep Media3 work honest: implement only a real Topway-only shim if chosen, and do not claim it fixes the fixed DoFun Music widget until validated on TS18.


## Final priorities after TS18 v2 runtime validation

1. **Treat stock TW Music behaviour as the primary contract.** DoFun's fixed Music widget demonstrably controls stock TW Music. Auxio must emulate the relevant `com.tw.music` component/action/broadcast behaviour as closely as a user app can.
2. **Do not assume `com.tw.media` wins while stock exists.** The fixed Music widget still opened stock `com.tw.music` while Auxio `com.tw.media` was installed and Android-media-session-visible. Source changes should therefore include fallback UX and validation hooks rather than promising transparent replacement without TS18 validation.
3. **Make wrapper services real.** `com.tw.music.MusicService` must actually receive/handle prev/next/play-pause/update intents and cold-start safely. Static string presence is insufficient.
4. **Broadcast from current state.** Emit `com.tw.music.info` and `com.tw.launcher.music_progress_duration` from playback transitions, update requests, and safe placeholder states.
5. **Keep the external surface simple.** Topway/DoFun-facing notification/widget/session actions should emphasise Previous / Play-Pause / Next. Keep repeat/shuffle in-app unless explicitly needed externally.
6. **Preserve overlay as fallback.** TS18 runtime shows a `com.tw.media` overlay window. Do not remove this path while the fixed DoFun widget continues routing to stock.
7. **Add diagnostic logs/tests.** Log every Topway command received, every outgoing stock-compatible broadcast, and every safe no-op reason. Tests should cover no current song, no library, missing permissions, null player, malformed extras, and background start constraints on API 29.
