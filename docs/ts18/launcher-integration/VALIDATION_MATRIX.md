# TS18 Launcher Media Integration Validation Matrix

Use this matrix after installing a build that includes the comprehensive in-app integration layer.

## Test apps / sources

- Stock `com.tw.music` if available.
- Current Auxio-TS topway/twmedia build.
- Spotify, YouTube Music, VLC, or another known standards media app if installed.
- Bluetooth music only if relevant to the test scenario.

## Required scenarios

For each source:

| Scenario | Expected Auxio behaviour | Expected launcher behaviour | Evidence to capture |
|---|---|---|---|
| Idle | no stale metadata | widget blank/default or previous source not attributed to Auxio | logcat, media_session |
| Start playback | session active, notification posted, metadata broadcast sent | title/art/progress/play state update | logcat, dumpsys notification/media_session, screenshot if possible |
| Pause | playstate broadcast sent; session state paused | play/pause icon changes; progress stable | logcat |
| Resume | playstate broadcast sent; progress resumes | widget state follows | logcat |
| Next | Auxio advances once | title/progress update once | logcat + widget state |
| Previous | Auxio previous once | title/progress update once | logcat + widget state |
| Seek from launcher | Auxio seeks once using selected unit policy | progress snaps correctly | logcat with raw/interpreted seek value |
| `cmd=update` | Auxio republishes metadata/progress/playstate | widget refreshes without playback disruption | logcat |
| Stop/clear | Auxio clears session/widget-compatible metadata | widget does not show stale Auxio track | logcat |
| ACC sleep/wake | restored state is republished after service/session attach | widget does not remain stale/blank when Auxio resumes | boot/ACC logs |

## Minimum shell captures

Use root where normal shell reports `dumpsys: inaccessible or not found`.

```bash
su -c 'settings get secure enabled_notification_listeners' > enabled_notification_listeners.txt
su -c 'cmd notification listeners 2>/dev/null || true' > notification_listeners_cmd.txt
su -c 'dumpsys media_session' > media_session.txt
su -c 'dumpsys notification --noredact' > notification.txt
su -c 'dumpsys package com.dofun.variety' > package_dofun.txt
su -c 'dumpsys package com.tw.media' > package_twmedia.txt
su -c 'dumpsys package com.tw.music' > package_twmusic.txt
su -c 'logcat -d -b all -v threadtime' > logcat_all.txt
```

## Pass criteria

The in-app implementation passes its own responsibility if:

- Auxio publishes Android session metadata/state correctly.
- Auxio posts a valid TS18-safe `MediaStyle` notification.
- Auxio sends Topway metadata/progress/playstate events in all required transitions.
- Auxio receives and handles all observed Topway commands.
- Auxio logs every incoming/outgoing bridge event.
- Auxio can disable the Topway bridge through settings for rollback.
- No TS18/DoFun/SystemUI notification bitmap crash is reintroduced.

The full device integration passes only if the DoFun widget display and controls visibly follow Auxio across the matrix.

## Failure classification

If widget still fails after Auxio proves all outgoing/incoming paths:

- **Source selection failure**: DoFun ignores Auxio package/source despite valid session/broadcasts.
- **Display mapping failure**: DoFun receives event/session but maps metadata/art/progress incorrectly.
- **Control routing failure**: DoFun clicks do not emit public/observed commands or target only stock package.
- **Permission/listener failure**: DoFun notification listener is disabled/unconnected.
- **Layout failure**: active DoFun theme/page does not render `soft_type=medias` media module.

Only after classifying failure should LSposed diagnostics or compatibility hooks be considered.

## Auxio-TS implementation note (2026-07-06)

The in-app implementation is wired through `TopwayLauncherIntegrationCoordinator` in the isolated Topway bridge package. It keeps the Android MediaSession/MediaBrowser/MediaStyle path intact, publishes the stock Topway metadata/progress broadcasts when the selected mode allows outgoing bridge traffic, routes the observed Topway command/update/seek broadcasts when the selected mode allows incoming command traffic, and retains the existing `topwayCompat` / `com.tw.media` wrapper path where that build variant is selected.

Implemented settings keys for rollback/device validation:

- `auxio_ts18_launcher_integration_mode` stores `Ts18LauncherIntegrationMode` by enum name. Topway-compatible builds default to `AutoAllSafePaths`; generic builds default to `AndroidMediaSessionOnly`.
- `auxio_ts18_launcher_seek_unit_policy` stores `TopwaySeekUnitPolicy` by enum name and defaults to `Auto`.

TS18 runtime validation is still required against the matrix above. This implementation does not include LSposed hooks, package replacement, root requirements, UID 1000 assumptions, platform signing, `/system` writes, or vendor-service changes.

### PR #142 hardening note

Launcher publishing is now driven from playback service state, not from Auxio's Android AppWidget path. The service publishes immediate metadata/progress on attach, new playback, queue/index changes, raw restored metadata changes, play/pause progression changes, launcher seek handling, session end, and service release. Periodic progress is emitted from the service while playback is active and remains rate-limited by the coordinator.

Mode selection is user-visible in the Topway-compatible UI settings screen:

- `AutoAllSafePaths` (default for Topway-compatible builds): Android media session path plus Topway metadata/progress broadcasts, incoming Topway commands, incoming widget seek, diagnostics, and existing `topwayCompat` identity wrappers where that build variant provides them.
- `AndroidMediaSessionOnly`: standards path only.
- `TopwayBroadcastOnly`: outgoing Topway broadcasts only; incoming Topway commands are logged and ignored.
- `TopwayCommandOnly`: incoming Topway commands/seek only; periodic/outgoing broadcasts are suppressed except normal Android standards surfaces.
- `TopwayBroadcastAndCommand`: outgoing Topway broadcasts plus incoming commands.
- `DiagnosticsOnly`: logs opportunities without sending Topway broadcasts or executing commands.
- `Disabled`: suppresses Topway bridge TX/RX behaviour for rollback.

Auto seek policy is deterministic but still requires TS18 validation. It treats values `0..100` as percent, `101..1000` as permille, larger values as seconds only when seconds fit within the known duration, and otherwise clamps as milliseconds. Negative values and unknown durations are ignored. If device validation proves the launcher uses another seekbar unit, switch the visible **Topway widget seek unit** setting to `Milliseconds`, `Seconds`, `Percent 0–100`, or `Permille 0–1000`.

The DoFun targeted broadcast is always attempted with `setPackage("com.dofun.variety")` when outgoing Topway broadcasts are enabled. This avoids per-progress PackageManager IPC and avoids Android 11+ package-visibility false negatives; sending a package-targeted broadcast is safe even when the package is absent.


### Head-unit safety guardrail note

`PR #142` supersedes older WidgetComponent-only Topway broadcast assertions. Head-unit safety checks should now verify `TopwayLauncherIntegrationCoordinator`, `PlaybackServiceFragment` service-driven publishing, mode gates, unconditional DoFun-targeted broadcast sending without PackageManager gating, seek policy conversion, and forced update/clear paths. `WidgetComponent` is not the canonical launcher media publisher.

Future LSposed/runtime DoFun discovery remains outside this in-app PR. If the matrix still fails after these safe paths are proven in logs/dumpsys/screenshots, classify the failure first (source selection, display mapping, control routing, notification listener, or layout) before proposing any later diagnostics-only hook work.
