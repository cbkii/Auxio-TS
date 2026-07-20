# TS18 Topway command-service playback bridge

## Scope and acceptance boundary

This document records the repository design and production-eligibility evidence for the
narrow Binder adapter introduced by PR #185. It does not claim that the physical TS18 has
accepted a normal-app callback, assigned Auxio the local-music source, or delivered DoFun
fixed-widget commands.

The adapter is limited at runtime to the dedicated `topwayTwMusic` and `topwayTwMedia`
compatibility variants. `standard` returns before creating a worker thread or requesting a
vendor-service bind.

## Exact APK evidence

The retained reference analysed for this contract is:

```text
file: com.tw.service.xt uid_1000(1).apk
SHA-256: 341af03ccbaeb6a7debe1929153eaadf9ced421d64a4933016010e0e7aa77267
package: com.tw.service.xt
versionName/versionCode: 2022.02.17 / 2
compile/min/target SDK: 29 / 29 / 22
shared UID: android.uid.system
```

**Observed:** its manifest declares persistent exported service
`com.tw.service.xt.CommandService`, process `:remote`, action
`com.tw.service.xt.CommandService.Bind`, and no service binding permission. This is static
manifest evidence only; firmware policy, SELinux and Binder-side caller checks still require
exact-device validation.

## Verified Binder subset

### Command interface

Descriptor: `com.tw.service.xt.aidl.ITWCommandAidl`

| Transaction | Code | Request parcel after interface token |
|---|---:|---|
| register command callback | 1 | one strong Binder |
| unregister command callback | 2 | one strong Binder |
| register music callback | 5 | one strong Binder |
| unregister music callback | 6 | one strong Binder |
| extended interface | 67 | presence `int`, then `Bundle` when present |

The exact generated proxy uses synchronous transactions, a reply parcel and
`readException()`. Auxio mirrors that order. Unknown descriptors or rejected/malformed
replies fail closed.

### Music callback

Descriptor: `com.tw.service.xt.aidl.IMusicCallBack`

| Code | Callback | Request fields after interface token |
|---:|---|---|
| 1 | `musicNext()` | none |
| 2 | `musicPre()` | none |
| 3 | `musicPlay()` | none |
| 4 | `musicPause()` | none |
| 5 | `musicMode(int)` | one `int` |
| 6 | `extendedInterface(Bundle)` | presence `int`, then `Bundle` when present |

### General command callback

Descriptor: `com.tw.service.xt.aidl.ITWCommandCallbackAidl`

Codes `1..8` are respectively system volume, volume status, Bluetooth phone status,
Bluetooth call status (`int`, `String`, `String`), Bluetooth connection status, reverse
status, sleep status, and extended `Bundle`.

Auxio registers this callback only to observe the source reply. It does not use volume,
Bluetooth, reverse or sleep values as authority.

## Source observation

The verified request is:

```text
project = system
action = source_request
```

The service responds through the general callback with:

```text
action = Source_recieve
SourceValue = <int>
```

The misspelling `Source_recieve` is exact. Static routing values observed in
`CommandService.sendSystemFunction` are radio `1`, local music `3`, Bluetooth `8`, video
`9`, and Android media-key fallback for other values.

**Important:** requesting or observing the source does not make Auxio source `3`. Callback
registration does not prove DoFun command delivery. Auxio does not force source state or
write TWUtil/MCU commands.

## Runtime design

1. `standard` returns from `attach()` before worker creation or binding.
2. A compatibility build creates one bounded worker while the concrete Auxio service lives.
3. Action resolution is attempted first, with the exact component as fallback.
4. The runtime Binder descriptor must match before registration.
5. Music callback registration is required; the general callback/source query is optional.
6. Binder transactions run off the main thread.
7. Playback callbacks post Android media-button intents to the concrete existing Auxio
   service component. `PlaybackServiceFragment` then passes them to the one canonical
   `MediaSessionCompat`; no second player, queue, service, session or notification is added.
8. Known callback parcels require the exact descriptor and minimum verified fields.
   Truncated fields, wrong descriptors, wrong source value types and unparcelable Bundles
   are rejected without escaping the Binder callback.
9. Reconnection is bounded to 500 ms, 1.5 s and 3 s. There is no infinite polling.
10. Release defers rapid reattachment until callback cleanup and old unbind complete, then
    either starts the pending session or shuts down the worker.

## Safety boundaries

The implementation does **not**:

- import or copy vendor Java classes, AIDL source, smali or native libraries;
- require platform signing, UID 1000, shared UID, signature permissions, root or SELinux
  changes;
- call `TWUtil`, force source state, or write MCU/CAN/radio commands;
- implement fake Cardoor services;
- replace the existing playback service, player, queue, MediaSession or notification stack;
- modify the separate systemless Magisk-only exact-`com.tw.music` replacement contract.

`topwayTwMedia` and `topwayTwMusic` remain distinct. The latter is not an ordinary
side-by-side replacement for the platform-signed stock package.

Missing, rejected or descriptor-mismatched vendor services leave Auxio's existing Android
MediaSession, media-button and Topway broadcast paths intact. That is not a claim that the
DoFun fixed widget will successfully fall back to those paths.

## Repository validation

PR #185 repository evidence includes:

- Android Quality #1049: successful;
- Android Build #1053: successful configured PR job;
- maintained debug builds and TS18 APK-reference/DoFun checks: successful;
- release-build step in #1053: skipped, not claimed as passing;
- all current inline review threads: resolved before the final audit patch.

Fresh canonical Android Build and Android Quality checks must pass on the final
non-workflow-token head before the PR is marked ready for code review.

## Exact-device acceptance still required

Install the `topwayTwMedia` build first and validate:

1. normal-app bind and music callback registration;
2. observed source value and whether the ordinary launcher/playback flow reaches source `3`;
3. one previous/play/pause/next callback and exactly one Auxio playback change per press;
4. no simultaneous stock-music action or duplicate callback reaction;
5. fixed-widget launch target, metadata and progress separately from command delivery;
6. Android notification, hardware-key, Bluetooth and MediaSession controls remain intact;
7. Auxio process death/recreation;
8. controlled `com.tw.service.xt` death/restart and bounded reconnect;
9. reboot and real ACC sleep/wake;
10. diagnostics-only, media-session-only and disabled-mode gating;
11. missing, rejected and descriptor-mismatched service behaviour.

If registration succeeds but source `3` or DoFun delivery is not achieved, the remaining
blocker is a Topway source/routing authority boundary. Do not invent source-forcing calls or
claim complete DoFun integration.

## Rollback

Use one of:

- select `AndroidMediaSessionOnly` or `Disabled` in TS18 launcher-integration settings;
- install the previous Auxio-TS APK;
- install `standard`, which never activates the adapter.

No system partition, Topway package, launcher database, MCU/CAN configuration or persistent
root state is modified by this PR.
