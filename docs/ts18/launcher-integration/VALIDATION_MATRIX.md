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
