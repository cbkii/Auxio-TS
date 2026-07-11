# Launch to Currently Playing panel

[Evidence confidence: Inferred from current Auxio-TS source and local policy tests] [Porting decision: Requires TS18 runtime validation]

Auxio-TS routes cold Topway/DoFun-compatible launches to the Currently Playing panel through a single startup route decision in `MainActivity`, then lets `MainFragment` render and acknowledge that durable route only after the playback sheet can actually show it.

## Product contract

| Situation | Expected destination |
|---|---|
| Existing paused session restored | Currently Playing, still paused. Autoplay is not required for the route. |
| Existing playing session restored | Currently Playing, preserving the restore play flag chosen by playback settings. |
| TS18 raw fast-resume active | Keep the route pending until raw playback reconciles to a normal Musikr `Song`; raw metadata is not treated as a normal panel model. |
| No saved session, non-empty library | Topway-compatible landscape cold launch may create the existing paused/playing Shuffle All fallback from playback restore policy, then route to Currently Playing after a song exists. |
| First setup / no source | Setup/home flow; never open playback. |
| Known empty library | Empty-library UI; never open an empty queue overlay. |
| Cache unavailable/recovery | Recovery UI unless a valid current session later becomes available through normal playback state. |
| Explicit NOW_PLAYING intent | Currently Playing with explicit priority. |
| Explicit QUEUE intent | Playback plus queue route with explicit priority. |
| Warm foreground return | Preserve current UI; no new generic startup route. |
| Configuration recreation | Restore current UI; no new generic startup route. |

## Old race

The previous implementation had two policy owners. `MainActivity.maybeRouteToPlaybackOnColdHeadUnitLaunch()` could emit `OpenPanel.PLAYBACK` or `PLAYBACK_QUEUE` immediately after requesting asynchronous playback restore, while `MainFragment.maybeOpenStartupPlayback()` independently inferred another startup route from library readiness and the current song.

The failing order was:

1. Activity launched while `PlaybackViewModel.song == null`.
2. `MainFragment.updateSong(null)` hid the playback sheet.
3. Activity requested `RestoreState`.
4. Activity emitted a transient open-panel command.
5. The fragment received it while the playback sheet target state was `HIDDEN`, so no expansion happened.
6. The transient command was consumed anyway.
7. Restore later supplied a song.
8. The fragment only unhid the sheet to `COLLAPSED`; no route remained to expand it.

## Corrected order

1. Activity requests playback restore and records a durable `PanelRouteRequest` with origin, priority, destination, wait-for-song policy, and reason code.
2. Fragment render attempts that arrive before a song or while the sheet is hidden are acknowledged as pending, not consumed as fulfilled.
3. When a restored/fallback song appears, the sheet is unhidden to collapsed state and the pending route is retried.
4. The route is acknowledged only after the sheet command is rendered, or cancelled/superseded by a higher-priority explicit route.

## Layout semantics

The route model uses semantic destinations:

- `PLAYBACK` means Currently Playing.
- `PLAYBACK_QUEUE` means an explicit queue-capable destination.
- In wide dual-pane layouts, queue content may already be visible beside playback.
- In stacked layouts, generic startup uses `PLAYBACK` so the queue bottom sheet does not cover the Currently Playing panel.

Physical 1280x720 alone is not proof of the selected resource qualifier; TS18 validation must record effective app bounds/density and whether `layout-w720dp` is selected.

## Diagnostics

Reason-coded logs are emitted for startup route selection, pending/fulfilled acknowledgement, route rejection by priority, cancellation, and duplicate deferred playback consumption during state-holder registration.

## TS18 validation status

This implementation is Android-standard runtime behavior plus Topway-flavour routing policy. It does not prove real TS18/TWTHEME launcher parity. Exact-device validation remains required for DoFun launcher restarts, ACC sleep/wake, raw fast-resume timing, and boot activity-start restrictions.
