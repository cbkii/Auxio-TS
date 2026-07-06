# TS18 Launcher Comprehensive In-App Integration Plan

## Goal

Implement a reversible, settings-selectable Auxio-TS integration layer that makes Auxio satisfy all safe known/plausible DoFun/Topway launcher media paths:

1. Android `MediaSession` / `MediaStyle` path.
2. Stock Topway metadata/progress broadcast path.
3. Stock Topway command/widget seek receiver path.
4. Fixed Topway package/source identity path where available through existing `topway-twmedia` build flavour.

This implementation must remain in-app. Do not require LSposed, Magisk, root, platform signing, system partition writes, disabling vendor services, or replacing stock packages.

## Required architecture

Add a dedicated coordinator in the Topway/head-unit compatibility layer. Suggested name:

```text
app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinator.kt
```

The coordinator should be injected/owned by existing playback service components and centralise:

```kotlin
onMetadataChanged(snapshot)
onPlaybackStateChanged(isPlaying)
onProgressChanged(progressMs, durationMs, reason)
onCommandReceived(action, cmd, extras)
onLauncherSeekReceived(rawValue, durationMs)
onUpdateRequested()
onClear(reason)
```

Do not scatter direct Topway broadcasts across unrelated playback code.

## Settings / mode

Implement a settings-selectable mode, with default selected only for TS18/topway compatibility builds.

Suggested mode enum:

```kotlin
enum class Ts18LauncherIntegrationMode {
    Disabled,
    AndroidMediaSessionOnly,
    TopwayBroadcastOnly,
    TopwayCommandOnly,
    TopwayBroadcastAndCommand,
    AutoAllSafePaths,
    DiagnosticsOnly,
}
```

Expected behaviour:

- `Disabled`: no Topway broadcasts or command bridge behaviour beyond standards path; useful for rollback.
- `AndroidMediaSessionOnly`: keep standards path only.
- `TopwayBroadcastOnly`: publish metadata/progress/playstate broadcasts, ignore incoming Topway commands except logging.
- `TopwayCommandOnly`: accept incoming Topway commands/seek, do not publish Topway metadata/progress except on `cmd=update` if needed for response.
- `TopwayBroadcastAndCommand`: enable outgoing and incoming Topway contract paths.
- `AutoAllSafePaths`: enable standards path + broadcasts + command bridge + diagnostics. This is the preferred TS18 default.
- `DiagnosticsOnly`: log detected incoming/outgoing opportunities without sending extra broadcasts or executing incoming commands.

If the project has an existing settings mechanism, integrate with it. If not, implement a small internal preference with a conservative default and expose UI only where the topwayCompat source set already contains TS18 settings UI.

## Outgoing event triggers

Publish metadata and progress on all relevant playback transitions:

- service/session attach when current playback state exists
- new playback
- queue/index moved
- track changed
- raw playback metadata changed
- play/pause progression changed
- seek completed
- repeat/shuffle changes only if they force notification/session refresh; no extra Topway metadata needed unless playback state is republished
- stop/release/task removed, to clear stale display
- `cmd=update` from launcher
- ACC/boot/process restore path if Auxio restores raw playback metadata before library is ready

Progress broadcast must be rate-limited while playing and immediate for discrete transitions.

## Incoming command handling

Map every observed Topway command into the existing playback command path. Preserve cold-start behaviour through foreground service start.

Required mappings:

```text
com.tw.music.action.prev                -> previous
com.tw.music.action.next                -> next
com.tw.music.action.pp                  -> play/pause
com.tw.music.action.cmd cmd=prev        -> previous
com.tw.music.action.cmd cmd=next        -> next
com.tw.music.action.cmd cmd=pp          -> play/pause
com.tw.music.action.cmd cmd=update      -> republish metadata/progress/playstate
com.android.launcher.widget_music_progress music_progress=<value> -> seek
```

Reject unsupported actions/extras with a diagnostic warning, not a crash.

## Notification and media session constraints

Do not regress existing code:

- Do not remove `MediaStyle(...).setMediaSession(sessionToken)`.
- Do not remove `CATEGORY_TRANSPORT`.
- Do not remove `MediaBrowserServiceCompat` exposure.
- Do not remove complete metadata keys.
- Do not remove `PlaybackStateCompat` actions.
- Do not reintroduce tiny/transparent notification bitmaps that crash TS18/DoFun/SystemUI.

## Tests

Add unit tests for:

- `TopwayMusicIntentFactory.metadataExtras` preserves `musicaArtist` and blank handling.
- `TopwayMusicIntentFactory.progressExtras` clamps to `Int.MAX_VALUE` and never emits negative values.
- command mapping for prev/next/pp/cmd/update.
- widget seek unit conversion policies.
- mode gating: disabled/diagnostics-only/broadcast-only/command-only/all-safe-paths.
- rate limiting does not suppress immediate transition updates.

Where the project architecture makes Android broadcast tests hard, test pure factories/policies and use fakes for sender/logger/clock/playback command dispatcher.

## Validation expectations

After implementation, run project-appropriate checks. Do not claim TS18 runtime success without device validation.

At minimum:

```text
./gradlew --no-daemon --stacktrace :app:testTopwayCompatDebugUnitTest
./gradlew --no-daemon --stacktrace :app:lintTopwayCompatDebug
./gradlew --no-daemon --stacktrace :app:assembleTopwayCompatDebug
```

If exact task names differ, use the closest existing topway-compatible unit test/lint/assemble tasks and document the actual commands run.

## Stop conditions

Stop and ask for direction before implementing any of these:

- replacing `com.tw.music`
- requiring UID 1000
- requiring platform signature
- disabling or force-stopping protected Topway services as part of normal operation
- writing `/system`, `/vendor`, dynamic partitions, or privileged app directories
- adding LSposed hooks to system_server/SystemUI
- deleting stock Topway apps
- broad generic Android debloat or battery/process tweaks
