# DoFun generic media compatibility implementation

## Status

Current Track-A implementation specification for the published `topwayTwMedia` / `com.tw.media`
product.

- **Observed software contracts:** one canonical playback service/player/queue/MediaSession/
  notification authority; generic three-action MediaStyle profile; MediaBrowser; media-button
  handling; cold PLAY restoration; optional Topway broadcasts and verified CommandService callback
  subset.
- **Historical exact-device observation:** Auxio's playback channel was previously importance `0`
  while DoFun's notification listener was connected and VLC's playback notification remained
  available. This is historical until re-read on the current device.
- **Inference:** DoFun may combine a generic notification/MediaSession adapter with a distinct fixed
  local-music source adapter for `com.tw.media` / `com.tw.music`.
- **Exact DoFun acceptance:** still requires current physical TS18 validation.

Passing CI, MediaSession or notification checks does not establish that DoFun selected Auxio as the
homepage source.

## Architecture

DoFun evidence supports one host-rendered media presentation surface backed by multiple possible
source adapters. Exact APK assets recognise both fixed local-music components:

```text
com.tw.media/com.tw.music.MusicActivity
com.tw.music/com.tw.music.MusicActivity
```

Other packages such as VLC and branded media players can use ordinary Android media surfaces. The
published Auxio package therefore sits at an important boundary: it has conventional Android media
behaviour **and** an identity DoFun recognises as fixed local music.

The implementation must keep these authorities separate rather than assuming one protocol selects
the homepage source.

## Current launcher modes

### `GenericDofunMedia`

Fresh Topway-compatible default. Enables:

- canonical Android MediaSession/MediaBrowser/media-button path;
- exactly three MediaStyle actions: previous | play/pause | next;
- compact actions `0,1,2`;
- canonical session token;
- paused notification retained for control/resumption;
- VLC-compatible legacy Android broadcasts:
  - `com.android.music.metachanged`;
  - `com.android.music.playstatechanged`.

It does **not** enable:

- `com.tw.music.info` metadata TX;
- `com.tw.launcher.music_progress_duration` progress TX;
- Topway command execution;
- `com.tw.service.xt.CommandService` binding.

The legacy `com.android.music.*` broadcasts are public/generic compatibility signals and must not be
gated as if they were Topway-private traffic.

### `AutoAllSafePaths`

Explicit additive physical-comparison mode. It keeps the same generic three-action notification and
legacy Android broadcasts, and additionally enables the already observed safe Topway paths:

- Topway metadata/progress broadcasts;
- incoming observed Topway command/update/seek actions;
- verified CommandService callback registration and source **query**.

It does not add source-setting transactions or another playback owner.

### Other modes

The remaining Topway broadcast-only, command-only, combined, diagnostic and disabled modes stay
explicit comparison/rollback surfaces. `AndroidMediaSessionOnly` retains the ordinary rich Auxio
notification without Topway integration.

## Generic MediaStyle contract

For `GenericDofunMedia` and `AutoAllSafePaths`, the canonical notification uses:

```text
previous | play/pause | next
```

Requirements:

- Android media-button PendingIntents targeting the canonical service;
- compact indices `0,1,2`;
- canonical MediaSession token;
- transport category and public visibility;
- ongoing only while playing;
- paused notification retained;
- delete/cancel routed to media STOP;
- bounded artwork;
- no custom OEM-fragile RemoteViews;
- stable playback channel ID.

No second notification/session/service is introduced.

## Legacy Android broadcast contract

`GenericDofunMedia` and `AutoAllSafePaths` publish the VLC-compatible public signals independently
from Topway TX.

`com.android.music.metachanged` extras:

- `track`;
- `artist`;
- `album`;
- `duration`;
- `playing`;
- `package`.

`com.android.music.playstatechanged` extras:

- `playing`;
- `package`.

These remain non-sticky unless new exact comparator evidence proves otherwise.

## Notification-channel diagnosis

`PlaybackNotificationChannel` reports:

- exact channel ID;
- package notification enabled state;
- channel existence;
- channel importance;
- usable/not-created/blocked classification;
- whether publication was requested during this process;
- first process-relative publication-request timestamp.

Auxio opens the exact system channel settings where available. It must not silently raise importance
or rotate the channel ID to bypass persisted user/system notification state.

A blocked playback channel is a first-order failure for the generic-notification hypothesis. Fix that
physical prerequisite before interpreting DoFun selection.

## Cold transport and MediaBrowser behaviour

A first PLAY/PLAY_PAUSE after process death may request bounded saved-state restoration without prior
audio focus. Repeated/non-ACTION_DOWN media keys remain rejected by the exported receiver policy and
unsafe cold previous/next/pause behaviour remains contained.

The MediaBrowser token/root path remains part of the one Auxio service. Do not add a duplicate
browser/playback service or wait for a full filesystem scan on a binder call.

## Selection evidence is separate from package topology

Installed packages establish availability only. They do not prove DoFun selection.

Diagnostics therefore report separately:

- package topology (`com.tw.media`, debug Auxio, stock `com.tw.music`, DoFun);
- DoFun selected music target when a launcher-owned exported surface actually proves one;
- `UNKNOWN` when selection is not found in the inspected scope.

The read-only provider probe is:

```text
content://com.dofun.variety.ExportedProvider/hotseat_app_music
```

Permission denial, empty output or ambiguous values remain `UNKNOWN`; they are not converted into an
absence claim.

## Bounded ingress telemetry

Physical validation must distinguish command origins before any duplicate-suppression policy is
added. Current telemetry records monotonic timing, current launcher mode, origin, command and outcome
for the public/media-button paths, session transport callbacks, Topway broadcast ingress, legacy
Android broadcast publication and MediaSession activation transitions. Existing Topway coordinator
and CommandService diagnostics retain metadata/progress/callback/source-query evidence.

No broad cross-path debounce is implemented from speculation. If one physical press is later proven
to generate equivalent commands through two distinct external ingress origins, add a narrow
short-window monotonic correlator at that external boundary only. Normal in-app controls must not use
it.

## MediaSession activation

Do not keep an empty MediaSession permanently active merely to chase DoFun discovery. Current policy
activates the session only when a current song, raw restored metadata, hydrated queue or primitive
queue window exists. Activation transitions are now observable.

Change this only after exact-device timing proves a one-shot DoFun scan occurs during a recoverable
inactive restore window.

## CommandService boundary

The verified adapter may:

- bind to the observed service/descriptor;
- register the known callback subset;
- query current source state;
- receive music-control callbacks.

It must not:

- set source `3`;
- invent unknown transact codes;
- write TWUtil/MCU/CAN state;
- assume platform signing or UID 1000.

## Current physical validation

Use:

```sh
sh scripts/evidence/collect-ts18-dofun-homepage-media-v1.sh
```

Then follow [`VALIDATION_MATRIX.md`](VALIDATION_MATRIX.md):

1. prove a healthy Auxio notification/session/listener baseline;
2. establish a current VLC positive control;
3. test `GenericDofunMedia` one button at a time;
4. test corrected `AutoAllSafePaths` identically;
5. use a non-fixed identity comparator only if needed;
6. only with explicit approval and rollback evidence, perform the existing guarded stock-selection
   comparator.

The normal collector is read-only and exports to `/storage/emulated/0/Download/AuxioTS/`.

## Track-B promotion gate

A separate DoFun-scoped LSPosed research adapter is not part of Track A. It becomes justified only
if the current device proves all of the following insufficient:

- healthy notification/channel/listener state;
- corrected generic/hybrid public surfaces;
- identity comparator;
- reversible stock source-selection comparator.

Any Track-B module must be separate from the existing stock `com.tw.music` Track-C bridge, scoped
only to the exact DoFun process/version, initially logging-only, bounded, kill-switchable and
fail-open.

## Non-goals / STOP boundaries

- no platform signing/shared UID/UID 1000 claims;
- no stock APK deletion or automatic disable;
- no DoFun private-database writes;
- no source-forcing Binder transaction;
- no TWUtil/MCU/CAN writes;
- no copied vendor smali;
- no fake Spotify/Apple/YouTube/Kuwo/QQ identity;
- no `/system`, `/vendor`, boot, vbmeta or firmware writes;
- no second player, queue, MediaSession, notification or playback service;
- no claim that CI proves fixed DoFun homepage acceptance.

## Migration

Fresh Topway-compatible installs default to `GenericDofunMedia`. Existing valid persisted selections,
including `AutoAllSafePaths`, remain untouched because older versions did not record whether a value
was an explicit user choice or an old default.

Exact TS18 acceptance remains a physical gate.
