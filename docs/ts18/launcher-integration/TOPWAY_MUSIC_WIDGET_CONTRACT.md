# Topway / DoFun Music Widget Contract for Auxio-TS

## Evidence status

This document records the currently supported in-app compatibility contract for TS18/Topway/DoFun launchers. It is not proof of the full protected DoFun internal method path. It is the safe Auxio-side contract to satisfy every plausible observed integration surface.

Use labels in implementation comments and docs:

- **Observed**: present in current Auxio-TS code, stock Topway string evidence, or captured TS18 diagnostics.
- **Inferred**: likely but not yet proven against protected DoFun runtime classes.
- **Requires device validation**: must be confirmed with TS18 runtime capture.
- **Unsupported**: do not implement without stronger evidence and rollback.

## Outgoing metadata broadcast

Observed stock Topway contract strings:

```text
Intent action: com.tw.music.info
Extras:
  musicTitle   -> String title
  musicaArtist -> String artist; typo is part of the contract and must be preserved
  musicAlbum   -> String album
  musicPath    -> String media path/URI
```

Auxio-TS must publish this broadcast on:

- new playback
- track/index change
- raw metadata change
- metadata restored after ACC/boot/process recreation
- launcher update command (`cmd=update`)
- stop/clear, with blank fields when appropriate

Recommended delivery:

- Send implicit broadcast for legacy/global listeners.
- Send explicit package-targeted broadcast to `com.dofun.variety` when safe.
- Optionally send explicit package-targeted broadcast to known Topway launcher packages only if package exists and no permission error is thrown.
- Do not require root, platform signature, or privileged permission.

## Outgoing progress broadcast

Observed stock Topway contract strings:

```text
Intent action: com.tw.launcher.music_progress_duration
Extras:
  msg_music_progress -> Int progress
  msg_music_duration -> Int duration
```

Implementation requirements:

- Use milliseconds as the default outgoing unit.
- Coerce to `0..Int.MAX_VALUE`.
- Publish immediately on track change, play/pause, seek, and metadata restore.
- While playing, rate-limit periodic publishing. Target: approximately once per second, not every internal progression tick.
- Publish `0, 0` or `0, duration` on stop/clear according to the current internal state. Prefer no stale progress.

## Legacy Android music broadcasts

Auxio-TS already has legacy broadcast constants in media-session code:

```text
com.android.music.metachanged
com.android.music.playstatechanged
```

Implementation requirements:

- Preserve existing behaviour.
- If moving responsibilities into a coordinator, do not regress these broadcasts.
- Keep extras simple and stable: `track`, `artist`, `album`, `duration`, `playing`, `package`.

## Incoming command broadcasts

Observed/in-scope incoming Topway actions:

```text
com.tw.music.action.prev
com.tw.music.action.next
com.tw.music.action.pp
com.tw.music.action.cmd
com.android.launcher.widget_music_progress
```

Observed/in-scope command extra:

```text
cmd=prev
cmd=next
cmd=pp
cmd=update
```

Expected mapping:

```text
prev              -> playback previous
next              -> playback next
pp                -> playback play/pause toggle
cmd=prev          -> playback previous
cmd=next          -> playback next
cmd=pp            -> playback play/pause toggle
cmd=update        -> republish metadata, progress, and playstate immediately
widget progress   -> seek using validated/auto-detected unit policy
```

## Widget seek unit policy

The incoming `music_progress` unit is not proven for this exact DoFun build. Implement a selectable policy:

```kotlin
enum class TopwaySeekUnitPolicy {
    Auto,
    Milliseconds,
    Seconds,
    Percent0To100,
    Permille0To1000,
}
```

Auto policy should be conservative and logged:

```text
if duration <= 0: ignore with diagnostic warning
if value < 0: clamp to 0
if value <= durationMs: treat as milliseconds unless another policy is selected
else if value * 1000 <= durationMs: treat as seconds
else if value in 0..100: treat as percent
else if value in 0..1000: treat as permille
else clamp to durationMs and log the ambiguity
```

If this conflicts with tests, prefer tests that encode observed TS18 runtime behaviour.

## Source/package identity

Observed DoFun config strongly recognises stock Topway identities:

```text
com.tw.media/com.tw.music.MusicActivity
com.tw.music/com.tw.music.MusicActivity
```

Implementation requirements:

- Keep the existing `topway-twmedia` package/flavour path intact.
- Do not assume UID 1000, platform signing, privileged permissions, or replacement of stock `com.tw.music`.
- Do not attempt to disable, uninstall, replace, or impersonate protected vendor packages.
- Do not add an LSposed/system hook as part of this in-app implementation.
- Leave LSposed as a later optional diagnostics/compatibility layer only if the safe paths are proven insufficient.

## Android media-session path

Implementation must not weaken current Android standards integration:

- active `MediaSessionCompat`
- `MediaBrowserServiceCompat`
- `MediaStyle` notification with session token
- complete `MediaMetadataCompat`
- complete `PlaybackStateCompat` actions
- queue and active queue item ID
- TS18-safe notification artwork fallback
- no duplicate media sessions
- no stale session metadata after stop/release

## Diagnostics

Every in-app bridge event must log through `DiagnosticJournal` where available:

```text
CAT_TOPWAY_BROADCAST: outgoing metadata/progress/playstate
CAT_TOPWAY_CMD: incoming prev/next/pp/cmd/update/seek
CAT_PLAYBACK: command result, seek result
CAT_NOTIFICATION: notification/session update where relevant
CAT_SYSTEM: detected package/feature mode where relevant
```

Logging must be bounded and must not leak full filesystem paths unless already visible in media URI strings.
