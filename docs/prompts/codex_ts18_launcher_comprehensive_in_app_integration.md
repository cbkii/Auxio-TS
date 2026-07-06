# Codex prompt: implement comprehensive in-app TS18 launcher media integration for Auxio-TS

You are working in `cbkii/Auxio-TS` on the `dev` branch. Implement the comprehensive in-app TS18/Topway/DoFun launcher media integration path.

## Required reading before editing

Read these newly added support files first:

- `docs/ts18/launcher-integration/TS18_LAUNCHER_COMPREHENSIVE_IN_APP_PLAN.md`
- `docs/ts18/launcher-integration/TOPWAY_MUSIC_WIDGET_CONTRACT.md`
- `docs/ts18/launcher-integration/VALIDATION_MATRIX.md`

Then inspect the current implementation files, at minimum:

- `app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt`
- `app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionInterface.kt`
- `app/src/main/java/org/oxycblt/auxio/AuxioService.kt`
- `app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicContract.kt`
- `app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicIntentFactory.kt`
- `app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBridgeReceiver.kt`
- `app/src/main/java/org/oxycblt/auxio/diagnostics/DiagnosticJournal.kt`
- topwayCompat settings/resources/source-set files, if present
- existing tests around playback service, Topway bridge, metadata, notification, and TS18 compatibility

## Background

DoFun/TS18 launcher integration cannot be assumed to use only Android's normal media session path. The safe in-app strategy is to satisfy every non-destructive known/plausible path at once:

1. Android `MediaSessionCompat` + `MediaBrowserServiceCompat` + `MediaStyle` notification path.
2. Stock Topway metadata/progress broadcast path.
3. Stock Topway launcher command/widget seek receiver path.
4. Existing `topway-twmedia`/`com.tw.media` package identity path, without claiming UID 1000 or platform signing.

Do not implement LSposed hooks in this task. Do not replace stock packages. Do not require root.

## Required implementation

### 1. Add a central coordinator

Create a dedicated in-app coordinator under the existing head-unit/Topway compatibility package, suggested path:

```text
app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinator.kt
```

The coordinator must centralise:

- outgoing Topway metadata broadcasts
- outgoing Topway progress broadcasts
- outgoing legacy Android music metadata/playstate broadcasts if responsibility is moved from existing code
- incoming Topway command handling decisions
- incoming launcher widget seek interpretation
- `cmd=update` handling
- diagnostics for all TX/RX bridge events
- mode gating
- progress rate limiting

Keep the Android standards path in `MediaSessionHolder` intact.

### 2. Add/select a mode

Implement a settings-selectable integration mode. Use existing settings architecture where possible.

Suggested enum:

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

Default behaviour:

- topway/TS18 compatibility builds: `AutoAllSafePaths`
- non-Topway generic builds: do not enable extra Topway broadcasts by default

If adding full UI is too invasive, implement persisted/internal setting support plus a minimal preference entry in the topwayCompat settings XML if such UI already exists. Do not break existing settings migrations.

### 3. Outgoing metadata and progress publishing

Use `TopwayMusicIntentFactory` and preserve `TopwayMusicContract` constants exactly, including `musicaArtist`.

Publish metadata on:

- new playback
- queue/index/track change
- raw metadata change
- service/session attach when current playback state exists
- restored metadata after boot/ACC/process recreation
- `cmd=update`
- stop/clear with blank fields to prevent stale launcher metadata

Publish progress on:

- track change
- play/pause
- seek
- progression while playing, rate-limited to approximately once per second
- `cmd=update`
- stop/clear

Use milliseconds for outgoing Topway progress/duration unless existing tests or observed code prove otherwise. Clamp to valid int range.

Broadcast delivery:

- send implicit broadcast for legacy/global listeners
- also send explicit package-targeted broadcast to `com.dofun.variety` if package exists and send does not throw
- catch `RuntimeException` / `SecurityException` and log diagnostic warnings; do not crash playback

### 4. Incoming command and seek handling

Preserve `TopwayMusicBridgeReceiver` as the exported allowlisted entry point. Strengthen or refactor it to route through the coordinator/service path cleanly.

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

Implement seek policy:

```kotlin
enum class TopwaySeekUnitPolicy {
    Auto,
    Milliseconds,
    Seconds,
    Percent0To100,
    Permille0To1000,
}
```

In Auto, choose conservatively based on value/duration and log the chosen interpretation. Ignore impossible seek if no duration is known.

### 5. Diagnostics

Use `DiagnosticJournal` categories already present:

- `CAT_TOPWAY_BROADCAST`
- `CAT_TOPWAY_CMD`
- `CAT_PLAYBACK`
- `CAT_NOTIFICATION`
- `CAT_SYSTEM`

Log each:

- outgoing metadata broadcast, including action and safe summary of title/artist/duration/path presence
- outgoing progress broadcast, including progress/duration and reason
- incoming command action/cmd
- incoming widget seek raw value and interpreted milliseconds
- mode gating result when an action is suppressed by mode
- broadcast send failure or command handling failure

Do not flood logs with high-frequency progress; respect rate limiting.

### 6. Tests

Add or update tests for pure policies/factories/coordinator behaviour. Required coverage:

- `musicaArtist` typo preserved in outgoing extras
- blank/null metadata produces non-crashing blank extras
- progress extras clamp and never go negative
- command action mapping
- `cmd=update` triggers republish rather than playback toggle
- seek policy conversion for milliseconds/seconds/percent/permille/auto
- disabled/diagnostics-only modes do not mutate playback or send broadcasts
- broadcast-only mode does not execute incoming commands
- command-only mode does not emit periodic broadcasts
- all-safe-paths mode emits/handles all required paths
- rate limiting does not suppress immediate transition events

Use fakes for sender, clock, playback command dispatcher, and diagnostic sink where Android framework testing would be heavy.

### 7. Preserve existing behaviour

Do not regress:

- `MediaSessionCompat` activation and token publication
- `MediaBrowserServiceCompat` service exposure
- `MediaStyle(...).setMediaSession(sessionToken)` notification
- `CATEGORY_TRANSPORT`, public/silent foreground notification behaviour
- TS18-safe notification artwork fallback
- queue, metadata, playback state, custom actions
- boot/fast-resume paths
- `TopwayMusicBridgeReceiver` exported allowlist

### 8. Do not implement unsafe paths

Do not:

- add LSposed hooks
- hook `system_server` or SystemUI
- replace, disable, uninstall, or impersonate protected Topway packages
- require UID 1000 or platform signing
- write to `/system` or `/vendor`
- broaden exported receivers beyond the known allowlist
- add generic phone/car advice unrelated to TS18

If you believe one of these is unavoidable, stop and report why rather than implementing it.

## Validation

Run the most appropriate existing checks. Prefer topway-compatible variants if task names exist:

```text
./gradlew --no-daemon --stacktrace :app:testTopwayCompatDebugUnitTest
./gradlew --no-daemon --stacktrace :app:lintTopwayCompatDebug
./gradlew --no-daemon --stacktrace :app:assembleTopwayCompatDebug
```

If those exact tasks do not exist, run the closest available test/lint/assemble tasks and state the actual commands. Do not claim TS18 device validation unless actually run on the TS18.

## Deliverables

- Code implementing the coordinator/mode/publisher/command handling.
- Tests for factory/policy/coordinator behaviour.
- Updated docs if behaviour or validation steps differ from the support files.
- A concise final summary listing:
  - files changed
  - modes implemented
  - commands/broadcasts supported
  - tests run and results
  - remaining TS18 device-validation requirements
