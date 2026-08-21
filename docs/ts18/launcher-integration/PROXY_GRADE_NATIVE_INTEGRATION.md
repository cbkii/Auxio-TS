# Proxy-grade Topway / DoFun native integration

## Purpose

This note records the implementation lessons taken from the user-supplied FYT proxy applications and how they map to the maintained TS18 product. It does **not** claim that FYT/SYU APIs apply to Topway or that CI proves physical DoFun fixed-panel acceptance.

The supported product remains:

```text
Auxio-TS application: com.tw.media
playback authority: existing Auxio service / queue / MediaSession / notification / audio focus
launcher: com.dofun.variety
optional stock relay: LSPosed Track C, scoped only to genuine com.tw.music
```

## Supplied reference APK evidence

Static inspection on 2026-08-22 used these supplied binaries:

```text
MusicProxy-2.5.5.apk
SHA-256 601b33f60934158e49d0dde6ea35592fafff1004f380d0fb66756e2f8d56187c

RadioProxyFYT_1.1.apk
SHA-256 f9f658bb82c65f59f698f44b9d8dfbd7987a6de720b68a27482e6d7ce2dc5398
```

Observed MusicProxy surfaces include:

- stock FYT package identity `com.syu.music`;
- boot and ACC/wake receivers;
- configurable target package and remembered last player;
- active `MediaController` / media-session control;
- FYT stock actions for previous, next, play/pause and favourite;
- a watcher for `closeApp com.syu.music`;
- retry/concurrent-autoplay behaviour and external-player relaunch.

Observed RadioProxy surfaces include:

- stock FYT package identity `com.syu.radio`;
- boot and ACC/wake receivers;
- target-player launch and wake policy;
- audio-focus observation;
- repeated key-event/autoplay attempts;
- a watcher for `closeApp com.syu.radio`;
- NavRadio package handling.

These are **FYT/SYU-specific implementation details**. They are useful as an architectural precedent, not as a Topway API contract.

## What to copy conceptually

The useful proxy pattern is:

```text
vendor-owned launcher/system expectation
        -> compatibility surface
        -> one real playback owner
```

For the TS18 this becomes:

```text
DoFun / Topway
        -> com.tw.media stock-compatible components + safe Android/Topway transports
        -> Auxio's existing playback service
```

When current exact-device evidence proves DoFun still selects genuine stock `com.tw.music`, optional Track C provides:

```text
DoFun / Topway
        -> genuine stock com.tw.music process
        -> narrowly scoped LSPosed relay
        -> Auxio com.tw.media MediaBrowser + acknowledged command endpoint
```

Track C preserves genuine stock identity and fails open to stock. It is not package replacement, signature spoofing or privilege transfer.

## Reliability parity matrix

| Proxy behaviour | Auxio-TS implementation | Current posture |
| --- | --- | --- |
| vendor-recognised launch identity | `com.tw.media/com.tw.music.MusicActivity` alias | implemented; physical DoFun selection still needs device proof |
| vendor-recognised media service | `com.tw.music.MusicService` wrapper delegating to the one Auxio service | implemented |
| stock-name widget surface | `com.tw.music.view.MusicWidgetProvider` | implemented |
| Android generic controller path | one MediaSession + MediaBrowser + conventional three-action MediaStyle notification | implemented |
| legacy Android metadata/playstate | `com.android.music.metachanged` / `playstatechanged` | implemented |
| Topway metadata/progress | `com.tw.music.info` and `com.tw.launcher.music_progress_duration` | implemented |
| Topway command broadcasts | observed prev/next/pp/cmd/update/seek allowlist | implemented |
| Topway privileged callback lane | verified `com.tw.service.xt.CommandService` subset | implemented, no source-forcing claim |
| stock-selected relay | Track-C LSPosed bridge in genuine `com.tw.music` | implemented, optional |
| player/session death recovery | Binder death + MediaBrowser callbacks | implemented |
| late service/app availability | fast bounded retry bursts plus low-rate cooldown re-arm | implemented by this change |
| state mirror after Track-C reconnect | full metadata/playstate/progress snapshot on MediaBrowser connection | implemented |
| fail-open stock behaviour | stock suppressed only after positive Auxio command admission | implemented |
| duplicate playback authorities | none added | required invariant |
| ACC-specific Topway broadcast | **unknown** for this exact TS18 | do not invent from FYT actions |
| exact DoFun source selection | current provider probe / physical validation | not proven by static code |

## Default policy

A fresh TS18 install now selects `AutoAllSafePaths` because that mode is additive and keeps one playback authority while exposing every currently observed safe compatibility surface:

- Android MediaSession/MediaBrowser/media button support;
- the conventional DoFun/VLC-compatible notification;
- public legacy Android media broadcasts;
- Topway metadata/progress broadcasts;
- observed Topway command broadcasts;
- verified CommandService callback/query support.

Existing persisted mode choices are deliberately preserved. The application cannot safely infer whether a historical `GenericDofunMedia` value was an old default or an explicit user choice.

## Long-lived recovery policy

The short retry sequences are retained for boot, ACC-wake and process-restart races, but exhaustion no longer disables integration for the remainder of a live process.

```text
fast bounded retry burst
        -> quiet 30 s cooldown
        -> re-arm the bounded burst
```

Only one delayed retry is owned per endpoint. Release, mode-disable and Track-C shutdown remove pending callbacks. A Track-C command Binder timeout still trips the existing process-lifetime circuit breaker: a potentially wedged command path must fail open to stock rather than retry aggressively.

## Safety boundary retained

This work does **not**:

- replace, disable or delete genuine stock `com.tw.music`;
- grant `com.tw.media` UID 1000, platform signing or signature permissions;
- write DoFun private state or force Topway source 3;
- hook `com.dofun.variety`, `system_server`, SystemUI or Package Manager;
- add a second player, playback service, queue, MediaSession, notification or audio-focus owner;
- copy FYT actions such as `com.fyt.boot.ACCON` into the TS18 product;
- claim boot, ACC, fixed-panel or exactly-once physical acceptance from software tests.

## Physical acceptance still required

Validate on the exact TS18 in this order:

1. confirm `AutoAllSafePaths` and a usable playback notification channel;
2. confirm current DoFun selected target via the existing read-only probe where available;
3. test source tap, metadata/art/progress and previous/play-pause/next one action at a time;
4. kill/restart Auxio and verify the direct path recovers;
5. restart the Topway command service if a safe reversible method is available and verify callback re-registration;
6. if DoFun is currently stock-selected, enable Track C and repeat the same tests, including stock-process restart and Auxio absent -> available recovery;
7. reboot, cold boot and ACC sleep/wake separately;
8. confirm no stock duplicate playback, duplicate MediaSession, radio/Bluetooth/DSP regression or duplicate control action.

Until those checks pass, classify the implementation as software-ready with physical TS18 acceptance outstanding.
