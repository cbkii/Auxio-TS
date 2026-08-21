# DoFun Variety / Topway music compatibility

## Current product authority

The supported player is Auxio-TS:

```text
application ID: com.tw.media
playback owner: Auxio's existing service, queue, MediaSession and notification
launcher: com.dofun.variety
```

Genuine stock `com.tw.music` is retained compatibility/recovery evidence, not the intended playback
architecture.

Use this dependency order:

1. **Track A** — direct `com.tw.media` integration through app-owned Android and observed
   Topway-compatible surfaces.
2. **Track B** — a separate `com.dofun.variety` LSPosed adapter only if exact evidence proves a
   required launcher-private behaviour Track A cannot provide.
3. **Track C** — the existing `com.tw.music`-scoped legacy stock shim only if a supported
   configuration still requires genuine stock as a relay.

Do not scope `com.tw.media` in LSPosed. Auxio owns that process and required behaviour belongs in the
app source.

## Supplied proxy reference evidence

Static inspection on 2026-08-22 used the supplied `MusicProxy-2.5.5.apk` and
`RadioProxyFYT_1.1.apk` as architectural references. MusicProxy exposes stock FYT `com.syu.music`
surfaces, follows boot/ACC and target-player lifecycle, controls an external active MediaSession,
handles stock previous/next/play-pause actions, watches `closeApp com.syu.music`, and retries/relaunches
its selected player. RadioProxy applies the same broad pattern to `com.syu.radio`, including boot/ACC,
audio-focus observation, repeated autoplay/key-event attempts, close-app watching and NavRadio
handling.

The useful precedent is therefore:

```text
vendor-owned launcher/system expectation
        -> compatibility surface
        -> one real playback owner
```

For this TS18 the safe equivalent is DoFun/Topway -> the recognised `com.tw.media` compatibility
surfaces -> Auxio's existing playback owner, with optional genuine-stock Track C only when current
exact-device evidence proves stock remains selected. FYT/SYU actions and package authority are **not**
Topway contracts and are not copied into the product.

Reference binary hashes:

```text
MusicProxy-2.5.5.apk
SHA-256 601b33f60934158e49d0dde6ea35592fafff1004f380d0fb66756e2f8d56187c

RadioProxyFYT_1.1.apk
SHA-256 f9f658bb82c65f59f698f44b9d8dfbd7987a6de720b68a27482e6d7ce2dc5398
```

## Exact observed evidence

The reviewed DoFun APK contains fixed music matches for both:

```text
com.tw.media/com.tw.music.MusicActivity
com.tw.music/com.tw.music.MusicActivity
```

This proves DoFun recognises both component identities. It does not prove which candidate is selected
when both exist or which adapter owns fixed-panel commands/state.

Retained physical evidence from the earlier configuration established:

- tapping the fixed Music surface opened stock `com.tw.music`;
- fixed previous, play/pause and next controlled stock;
- stock metadata/progress updated the fixed surface;
- tested Auxio broadcasts did not make that fixed surface control `com.tw.media`;
- stock remained installed and selected during that capture.

That is historical evidence of a stock-selected configuration, not proof of current direct
`com.tw.media` behaviour.

A second historical exact-device observation found a rich active Auxio MediaSession while
`com.tw.media.channel.PLAYBACK` had importance `0` and no live Auxio playback notification was
present. DoFun's notification listener was connected and VLC's playback notification remained
available. The channel state is volatile/persisted user state and must be re-read before reuse.

## Current repository implementation

Track A lives under:

```text
app/src/main/java/org/oxycblt/auxio/headunit/topway/
```

Important current classes include:

```text
TopwayLauncherIntegrationCoordinator
TopwayMusicBridgeReceiver
TopwayCommandServiceClient
TopwayCommandServiceContract
TopwayMusicCommandMapper
TopwayMusicContract
TopwaySeekUnitPolicy
TopwayProgressStatePolicy
TopwayWidgetProviderPolicy
TopwayBridgeExtrasPolicy
LauncherIntegrationTelemetry
```

`PlaybackServiceFragment` is the principal playback-state publishing/command call-site.
`com.tw.music.MusicActivity`, `com.tw.music.MusicService` and the stock-name widget provider are
compatibility class/component names inside the independently signed `com.tw.media` APK; they do not
grant stock package identity, signer or UID.

The Topway-compatible **fresh-install default is `AutoAllSafePaths`**. This is intentionally additive:
it keeps one Auxio playback authority while exposing every currently observed safe generic and
Topway-compatible surface. Existing persisted mode choices are preserved because older releases did
not record enough preference provenance to distinguish an old default from an explicit user choice.

### `GenericDofunMedia`

Enables:

- Android MediaSession/MediaBrowser/media-button surfaces;
- conventional previous | play/pause | next MediaStyle notification;
- public/VLC-compatible `com.android.music.metachanged` and
  `com.android.music.playstatechanged` broadcasts.

Does not enable:

- `com.tw.music.info` / Topway metadata TX;
- `com.tw.launcher.music_progress_duration` / Topway progress TX;
- Topway command execution;
- CommandService binding.

It remains an important physical comparator and fallback mode.

### `AutoAllSafePaths`

Retains the same three-action generic notification and public legacy Android broadcasts, and
additionally enables:

- Topway metadata/progress TX;
- observed Topway command/update/seek RX;
- verified CommandService callback registration and source query.

It does not force a Topway source or create a second playback authority.

Transient CommandService failures use a short bounded reconnect burst followed by a quiet cooldown
and re-arm. Exhausting the initial boot/wake race therefore no longer disables the callback lane for
the remainder of a live Auxio process. Descriptor mismatch remains a hard fail-open STOP for that
adapter rather than a retry condition.

The existing `lsposed-bridge/` module is Track C and remains statically scoped to:

```text
com.tw.music
```

Track C now uses the same bounded-burst + low-rate re-arm principle for its Auxio MediaBrowser and
command-endpoint connections. A command Binder timeout still trips the existing process-lifetime
circuit breaker and leaves stock behaviour in control.

No Track-B `com.dofun.variety` module exists.

## Public and observed Topway-compatible contract

Incoming actions:

```text
com.tw.music.action.cmd
com.tw.music.action.prev
com.tw.music.action.next
com.tw.music.action.pp
com.android.launcher.widget_music_progress
```

Known extras:

```text
cmd = prev | next | pp | update
music_progress
appWidgetIds
```

Outgoing metadata:

```text
action: com.tw.music.info
extras: musicTitle, musicaArtist, musicAlbum, musicPath
```

Outgoing progress:

```text
action: com.tw.launcher.music_progress_duration
extras: msg_music_progress, msg_music_duration
```

Preserve the observed spelling `musicaArtist`. Treat values as milliseconds unless exact current
evidence proves another unit.

The separate VLC-compatible public legacy Android broadcasts use:

```text
com.android.music.metachanged
com.android.music.playstatechanged
```

They are not Topway-private TX and are independently mode-gated.

Required Android surfaces remain:

```text
MediaSession
MediaBrowserService
media notification
media-button handling
metadata and playback-state updates
```

A valid MediaSession does not by itself prove fixed-panel parity.

## Selection evidence

Package installation is topology, not selection. Runtime diagnostics therefore report separately:

- installed release/debug Auxio, stock and DoFun packages;
- DoFun selected music target only when a launcher-owned exported surface proves it;
- `UNKNOWN` when selection is not found in the inspected scope.

Current read-only selection probe:

```text
content://com.dofun.variety.ExportedProvider/hotseat_app_music
```

Permission denial, empty output, ambiguous values and failed probes remain unknown. Do not infer
selection from package presence.

## Track A acceptance

Track A is selected when the fixed DoFun surface can launch and control `com.tw.media` without
genuine stock acting as an unseen relay.

Track A must preserve:

- one Auxio playback service implementation;
- one queue authority;
- one MediaSession authority;
- one playback notification authority;
- one audio-focus owner;
- one command dispatcher;
- one Topway state publisher;
- bounded exported-entry rate limiting;
- API 29 compatibility.

Exactly-once control is a physical acceptance criterion. Do **not** add a broad cross-path debounce
until one physical press is proven to arrive through two distinct external ingress paths. Current
bounded telemetry exists specifically to make that decision from evidence.

## Current physical validation

Run the read-only collector:

```sh
sh scripts/evidence/collect-ts18-dofun-homepage-media-v1.sh
```

Then follow [`ts18/launcher-integration/VALIDATION_MATRIX.md`](ts18/launcher-integration/VALIDATION_MATRIX.md).

The sequence is deliberately discriminating:

1. prove a healthy Auxio playback channel, live notification, DoFun listener and active session;
2. establish a current VLC positive control;
3. test `GenericDofunMedia` one button at a time;
4. test `AutoAllSafePaths` identically;
5. use a non-fixed identity comparator only if needed;
6. only with explicit approval and saved rollback evidence, use the existing guarded reversible
   stock-selection comparator.

Do not interpret generic-path failure while the playback notification channel is blocked.

## Track B eligibility

Do not create a DoFun-scoped adapter merely because historical Track A validation failed while stock
was selected.

Track B requires current exact evidence that a release-required fixed-panel action/state path is
private to `com.dofun.variety` and cannot be supplied by Track A after the healthy notification,
hybrid, identity and stock-selection discriminators above.

If proven, implement a separate optional module with:

- exact package, signer, APK/version and method fingerprints;
- log-only first operation;
- fail-open hooks;
- independent kill switch/circuit breaker;
- bounded process-safe IPC to Auxio;
- no main-thread disk, hashing, service-binding or unbounded waits.

Do not repurpose the stock-shim module for Track B.

## Track C eligibility

Keep the existing stock shim only when current evidence proves a supported configuration still routes
required behaviour through genuine stock `com.tw.music`.

Track C must:

- remain optional and independently disableable;
- remain statically scoped exactly to `com.tw.music` and route only its captured default/main process;
- probe each exact known Activity, Service, receiver and presenter surface independently;
- treat unavailable or changed hook surfaces as fail-open stock behaviour;
- keep the emergency kill switch fail-safe (`UNKNOWN` means no bridge action);
- suppress stock only after bounded positive Auxio command admission;
- avoid reviving stock playback, duplicate sessions, focus or commands.

Build/reference signer fingerprints are evidence about retained APKs, not an invented runtime
activation gate. A signer match alone does not authorise unknown obfuscated stock methods.

## Release posture

Published primary asset:

```text
Auxio-TS release APK (com.tw.media)
```

Retired application lane:

```text
former exact-package Auxio com.tw.music app - no build, test, install or release capability
```

Optional artifacts:

```text
Track C stock shim - opt-in only while justified
Track B DoFun adapter - only after separate evidence-gated implementation and qualification
```

Manual Release must not select the legacy Track-C bridge by default.

## STOP boundaries

Do not:

- delete/uninstall stock `com.tw.music` for diagnosis;
- clear/write DoFun private data without an explicit recovery plan;
- force Topway source `3`;
- invent Binder transaction codes;
- copy vendor smali;
- claim UID 1000/platform signing from root;
- add a second playback service/player/queue/MediaSession/notification;
- write `/system`, `/vendor`, boot/vbmeta/super or firmware data;
- claim exact DoFun acceptance from CI/emulator/static evidence.

## Current evidence boundary

Existing software evidence is enough to implement and test the corrected direct/hybrid capabilities.
It is not enough to claim:

- current direct fixed-panel parity;
- a current identity trap;
- a current stock-source conflict;
- Track B is required;
- Track C is required;
- cold-boot/ACC parity;
- exactly-once physical control across all routes.

Until current TS18 evidence resolves those points, use the more precise classifications documented in
the validation matrix, including `TRACK_A_SOFTWARE_READY_NEEDS_DEVICE_TEST` and
`PHYSICAL_EVIDENCE_INCOMPLETE`.
