# DoFun Variety / Topway music compatibility

## Current product authority

The supported player is Auxio-TS:

```text
application ID: com.tw.media
playback owner: Auxio's existing service, queue, MediaSession and notification
launcher: com.dofun.variety
```

Genuine stock `com.tw.music` is not the current user's intended player. Its retained APK and
runtime evidence remain useful for contract recovery and conflict detection, but they do not make
the stock process the primary architecture.

Use this dependency order:

1. Track A - direct `com.tw.media` integration through app-owned Android and observed
   Topway-compatible surfaces.
2. Track B - a separate `com.dofun.variety` LSPosed adapter only if exact evidence proves a
   required launcher-private behaviour that Track A cannot provide.
3. Track C - the existing `com.tw.music`-scoped legacy stock shim only for a supported
   configuration that still requires stock as a relay.

Do not scope `com.tw.media` in LSPosed. Auxio owns that process and required behaviour belongs in
the app source.

## Exact observed evidence

The reviewed DoFun APK contains fixed music matches for both:

```text
com.tw.media/com.tw.music.MusicActivity
com.tw.music/com.tw.music.MusicActivity
```

This proves that DoFun can recognise the `com.tw.media` component identity. It does not prove which
candidate it selects when both exist, nor which path owns fixed-widget commands and state.

Retained physical evidence from the earlier configuration established:

- tapping the fixed Music surface opened stock `com.tw.music`;
- fixed previous, play/pause and next controlled stock;
- stock metadata/progress updated the fixed surface;
- the tested Auxio broadcasts did not make that fixed surface control `com.tw.media`;
- stock remained installed and selected during that capture.

Therefore the prior evidence proves a stock-selected configuration. It does not establish direct
`com.tw.media` behaviour with stock absent, disabled or otherwise not selected.

## Current repository implementation

Track A is already substantially implemented under:

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
```

`PlaybackServiceFragment` is the principal playback-state publishing and command call-site.
`com.tw.music.MusicActivity`, `com.tw.music.MusicService` and the stock-name widget provider are
compatibility class/component names inside the independently signed `com.tw.media` APK; they do not
grant the stock application ID, signer or UID.

The current Topway-compatible default is `GenericDofunMedia`. That mode uses the Android-standard
MediaSession/notification/media-button profile. The observed Topway metadata/progress publisher and
incoming command handler exist, but they are separately mode-gated. Do not describe all direct
Topway lanes as active by default merely because their classes exist.

The existing `lsposed-bridge/` module is Track C. It hooks stock classes and therefore correctly
retains this scope while it exists:

```text
com.tw.music
```

No Track B `com.dofun.variety` module currently exists.

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

Required Android surfaces remain:

```text
MediaSession
MediaBrowserService
media notification
media-button handling
metadata and playback-state updates
```

A valid MediaSession does not by itself prove fixed-widget parity.

## Track A acceptance

Track A is selected when the fixed DoFun surface can launch and control `com.tw.media` without
genuine stock acting as an unseen relay.

Track A must preserve:

- one Auxio playback service implementation;
- one queue authority;
- one active MediaSession;
- one playback notification;
- one audio-focus owner;
- one command dispatcher;
- one Topway state publisher;
- bounded rate limiting and deduplication;
- API 29 compatibility.

Audit the current modes and call-sites before adding new classes. Prefer completing or correcting
the existing coordinator/receiver/service-client implementation over building a second direct
bridge.

## Track B eligibility

Do not create a DoFun-scoped adapter merely because historical Track A validation failed while
stock was selected.

Track B requires exact evidence that a release-required fixed-widget action or state path is private
to `com.dofun.variety` and cannot be supplied by Track A. If proven, implement a separate optional
module with:

- exact package, signer, APK/version and method fingerprints;
- log-only first operation;
- fail-open hooks;
- independent kill switch and circuit breaker;
- bounded process-safe IPC to Auxio;
- command IDs and deduplication;
- positive Auxio acceptance before suppressing launcher behaviour;
- no main-thread disk, hashing, service-binding or unbounded waits.

Do not repurpose the stock-shim module for Track B.

## Track C eligibility

Keep the existing stock shim only when current evidence proves a supported configuration still
routes required behaviour through genuine stock `com.tw.music`.

Track C must:

- remain optional and independently disableable;
- verify stock UID 1000, signer and per-build hook capability;
- verify the paired Auxio package and expected signer;
- fail open to stock behaviour;
- suppress stock only after bounded positive Auxio acceptance;
- avoid reviving stock playback, duplicate sessions, duplicate focus or duplicate commands.

A signer match alone must not authorise unknown obfuscated stock methods.

## Release posture

Published primary asset:

```text
Auxio-TS topwayTwMedia release APK (com.tw.media)
```

Internal fixture:

```text
topwayTwMusic (com.tw.music) - build/test only; never publish or install as the normal product
```

Optional artifacts:

```text
Track C stock shim - opt-in only while justified
Track B DoFun adapter - only after separate evidence-gated implementation and qualification
```

Manual Release must not select the legacy Track C bridge by default.

## Current evidence boundary

Existing project diagnostics are sufficient to establish the earlier stock-selected failure mode.
They are not sufficient to claim any of these current outcomes:

- direct fixed-widget parity with stock absent/not selected;
- Track B is required;
- Track C is no longer required;
- cold-boot or ACC recovery;
- exactly-once control across all routes.

Until current physical evidence resolves the package-selection and command/state lanes, report the
architecture decision as one of:

```text
NO_LSPOSED_REQUIRED
STOCK_SHIM_REQUIRED
DOFUN_ADAPTER_REQUIRED
BOTH_ADAPTERS_REQUIRED
PHYSICAL_EVIDENCE_INCOMPLETE
```

Software maturity work may continue while the physical classification is incomplete, but release
claims must remain explicit.
