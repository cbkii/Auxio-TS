# TS18 Topway command-service playback bridge

## Scope and approval

This document is the design and production-eligibility record for the narrow Binder adapter added by this PR. The change was explicitly requested after exact-device APK analysis showed that Android MediaSession and the existing Topway broadcast bridge do not cover the TS18 local-music source path.

The bridge is limited to the dedicated `topwayTwMusic` and `topwayTwMedia` variants. The standard Auxio variant returns before binding and retains Android-standard behaviour only.

## Evidence and porting decision

### Exact-device service identity

- **Evidence confidence: Observed.** The supplied `com.tw.service.xt` APK declares exported service `com.tw.service.xt.CommandService`, process `:remote`, and action `com.tw.service.xt.CommandService.Bind`, without a manifest binding permission.
- **Porting decision: Directly reusable requirement.** Bind explicitly and verify the runtime Binder descriptor before sending any transaction.

### Command interface subset

- **Evidence confidence: Observed.** Decompilation of the supplied APK established descriptor `com.tw.service.xt.aidl.ITWCommandAidl` and these transaction codes:
  - `1`: register `ITWCommandCallbackAidl`
  - `2`: unregister `ITWCommandCallbackAidl`
  - `5`: register `IMusicCallBack`
  - `6`: unregister `IMusicCallBack`
  - `67`: `extendedInterface(Bundle)`
- **Porting decision: Directly reusable requirement.** Implement only these transactions with local Binder objects. Do not copy decompiled source or smali.

### Music callback routing

- **Evidence confidence: Observed.** `IMusicCallBack` uses transactions `1..6` for next, previous, play, pause, mode, and extended interface. `CommandService.sendSystemFunction` routes local-music source value `3` to this callback instead of Android media-key fallback.
- **Porting decision: Directly reusable requirement.** Convert transactions `1..4` to Android `KEYCODE_MEDIA_NEXT`, `KEYCODE_MEDIA_PREVIOUS`, `KEYCODE_MEDIA_PLAY`, and `KEYCODE_MEDIA_PAUSE`, then send the media-button intent to the concrete, already-running Auxio service component. Mode and unknown bundles are observed/logged only.
- **Evidence confidence: Observed.** The service stores music callbacks in Android's `RemoteCallbackList`, so more than one client can be registered at the same time.
- **Porting decision: Requires device validation.** Auxio does not disable or replace the stock client. Exact-device testing must confirm that each DoFun button produces one Auxio playback change and no parallel stock reaction. If duplicate routing occurs, select `AndroidMediaSessionOnly` or `DiagnosticsOnly`, retain the logs, and revise the routing design rather than modifying stock services.

### Source state

- **Evidence confidence: Observed.** A source request uses `project=system`, `action=source_request`; the response uses the exact misspelling `action=Source_recieve` and integer `SourceValue`. Observed routing values are radio `1`, local music `3`, Bluetooth `8`, and video `9`.
- **Porting decision: Useful as evidence only.** The adapter requests and records the source. It never forces a source and never writes TW/MCU commands.

### DoFun and Android paths

- **Evidence confidence: Observed.** DoFun recognises `com.tw.media/com.tw.music.MusicActivity` and `com.tw.music/com.tw.music.MusicActivity`, while runtime captures show an active Auxio MediaSession was insufficient for the fixed DoFun music controls.
- **Porting decision: Directly reusable requirement.** Keep MediaSession, MediaBrowser, media notification, stock-name wrapper, Topway broadcasts, and the command-service callback as parallel isolated paths. Do not replace one with another.

## Runtime design

`TopwayCommandServiceClient` is created through Hilt and attached to `AuxioService` lifecycle:

1. Standard builds exit without creating a worker or binding.
2. Topway builds create one bounded worker thread and request a service bind.
3. Action resolution is attempted first; the exact component is retained as a fallback.
4. The remote descriptor must equal `ITWCommandAidl`. A mismatch stops this adapter for the service lifetime.
5. The music callback is required. The general callback and source query are optional enhancements.
6. Binder transactions run off the main thread. Playback callbacks are posted to the main thread as media-button service intents.
7. Existing `Ts18LauncherIntegrationMode` gates command execution. `DiagnosticsOnly`, media-session-only, broadcast-only, and disabled modes do not execute callback controls.
8. Reconnection is bounded to delays of 500 ms, 1.5 s, and 3 s. There is no infinite polling.
9. Service destruction unregisters callbacks, unlinks death notification, unbinds, and quits the owned worker.

## Safety boundaries

The implementation deliberately does **not**:

- import vendor Java classes;
- require platform signing, shared UID, root, Magisk, or SELinux changes;
- impersonate stock package identity outside the existing dedicated compatibility flavours;
- call `TWUtil`, force source state, or write MCU/CAN/radio commands;
- implement or fake Cardoor services;
- create a second playback service, MediaSession, notification, or playback state owner;
- keep retrying indefinitely when the service or contract is unavailable.

A descriptor mismatch, registration rejection, missing service, `SecurityException`, or binder death degrades to the existing MediaSession/broadcast paths. Auxio playback remains available.

## Required validation

### CI proof

The PR must pass:

- standard, `topwayTwMusic`, and `topwayTwMedia` debug compilation;
- standard unit tests, including callback Parcel unmarshalling and exact transaction constants;
- formatting, Android lint, and head-unit compatibility safety checks;
- existing TS18 APK-reference and DoFun/Topway contract checks.

### Exact TS18 runtime proof

Install the `topwayTwMedia` build matching the existing `com.tw.media` lane, then validate after a cold launch, process restart, reboot, and real ACC sleep/wake:

1. Start Auxio playback and return to DoFun Home.
2. Verify logs show `Service connected`, descriptor acceptance, and `Music callback registered`.
3. Press DoFun previous, play, pause, and next. Each press must log one matching `Music callback` and cause exactly one playback change, with no simultaneous stock-client reaction.
4. Verify source response is logged as value `3` / `LOCAL_MUSIC` when Topway considers Auxio local music.
5. Verify Android notification controls, hardware media keys, Bluetooth controls, and MediaSession controllers still work.
6. Pause and leave the app idle; confirm no repeated binds, callback loops, duplicate services, duplicate sessions, or duplicate notifications.
7. Kill or restart `com.tw.service.xt` once in a controlled diagnostic window and confirm bounded reconnection without an app crash.
8. Change integration mode to `AndroidMediaSessionOnly` or `DiagnosticsOnly`; callbacks may be observed but must not alter playback.

DoFun fixed-widget identity selection remains separately device-dependent. Successful callback registration proves the playback-control path, not platform signing or full stock-widget replacement.

## Rollback

Application rollback is one of:

- select `AndroidMediaSessionOnly` or `Disabled` in TS18 launcher integration settings;
- install the previous Auxio-TS APK;
- install the standard flavour, which never activates the adapter.

No system partition, Topway package, launcher database, MCU, CAN configuration, or persistent root state is modified.
