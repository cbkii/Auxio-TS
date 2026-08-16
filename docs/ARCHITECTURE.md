# Architecture

This document is the canonical high-level ownership and integration-boundary guide. Detailed source and startup decisions remain in the current architecture references linked from [docs/README.md](README.md).

## Runtime ownership

Auxio-TS has one playback authority:

- `AuxioService` and its playback fragments own playback lifecycle;
- one canonical queue/state path owns ordering and restoration;
- one MediaSession exposes Android transport state;
- one notification path publishes playback state;
- one audio-focus path arbitrates audio ownership.

Stock-compatible services, receivers, widgets and activities delegate into these authorities. They must not create another player, queue, MediaSession, notification or audio-focus owner.

## Authority boundaries

| Authority | Owns | Does not own |
| --- | --- | --- |
| Android framework | lifecycle, MediaSession/MediaBrowser, media buttons, audio focus, notifications and storage APIs | Topway/DoFun launcher policy, MCU/CAN, DSP/radio or root privilege |
| Auxio-TS core | library, queue, playback, UI and app state | protected stock identity or vendor-global state |
| Track A compatibility | bounded launcher/component, broadcast, widget and command translation inside `com.tw.media` | a second playback stack or vendor service emulation |
| DoFun/Topway firmware | launcher selection, vendor widgets and observed device services | Auxio queue or MediaSession authority |
| MCU/CAN and DSP/radio | vehicle and audio-hardware state | Android app playback architecture |
| Root/Magisk tooling | explicitly approved storage and diagnostic operations | signer, shared UID, platform privileges or stock-package replacement |
| Track C LSPosed bridge | optional, fail-open translation inside genuine stock `com.tw.music` | Auxio runtime ownership, DoFun scope or system-wide hooks |

## Library and source-scan authority

A successfully committed library is the normal operating state of the maintained TS18 product. App/activity/service lifecycle events are not source-enumeration authority. In particular, a visible Topway launch or resume must restore cached state rather than silently converting slow hydration into a compatibility-recovery scan.

Source traversal is entered only for a positive source reason: initial/pending source configuration, an explicit user refresh/rescan/retry, a changed source configuration, or an independently enabled source-observation/removable-storage path. Wall-clock age alone is not a source reason. An unchanged advisory fingerprint therefore remains reusable until its observed token changes, the source is explicitly invalidated, the configuration changes, or a forced scan is requested.

Generated playlists are derived presentation. Enabling them must not alter source identity, source checkpoints or scan authority. Their whole-library compilation stays on the existing optional background coordinator rather than being moved onto a UI getter; it may run after process recreation when the feature is enabled, but it must never become an indexing/source-scan trigger or block the immediate browse/search lane.

## Integration tracks

### Track A — direct app integration

Track A is primary. Code under `org.oxycblt.auxio.headunit.topway` and thin `com.tw.music` component wrappers is compiled into the one `com.tw.media` product. It uses public Android surfaces first and isolated observed vendor contracts where justified.

### Track B — absent

No `com.dofun.variety` module exists. Do not create one unless exact evidence demonstrates a launcher-private gap and a separate architecture decision defines scope, trust, IPC, release, rollback and validation.

### Track C — optional stock shim

`lsposed-bridge` is a separately installed optional add-on, statically scoped exactly to genuine stock `com.tw.music`. Installation probing begins only after exact package and main-process routing. The current implementation enables bridge actions only when its fail-safe kill-switch state is `ENABLED`; `UNKNOWN`, unavailable and read-error states leave those actions disabled. Signer fingerprints produced by build/reference checks describe the supplied APKs and are not an independent activation rule. Stock suppression additionally requires a positive bounded Auxio command-admission acknowledgement, while timeout, mismatch and unavailable paths preserve stock behaviour. It is not an Auxio app variant.

## Source boundaries

- `app/src/main`: core product and main manifest.
- `app/src/topwayCompat`: thin Kotlin/Java compatibility sources compiled into `main`; production resources and the manifest live in `app/src/main`, with no independent manifest or product flavour.
- `app/src/test`, `app/src/androidTest`, `app/src/topwayCompatTest`: policy, unit and instrumentation coverage.
- `musikr`: internal library implementation.
- `startup-benchmark`: validation-only instrumentation against `com.tw.media`.
- `lsposed-bridge`: stricter Track-C boundary; see its local `AGENTS.md`.

Generic Android fallback behaviour belongs in pure policy inputs and tests, not a generic application flavour. Package/component fixtures may model a contract; a complete protected-package impersonation APK may not.

## Safety and change rules

Preserve API 29 compatibility and API-gate newer behaviour. Keep I/O bounded and off the main thread. Treat notification, RemoteViews, startup, storage scanning and tag parsing as OEM-sensitive.

Architecture-affecting changes must identify product/module classification, runtime authority impact, evidence status, release implications, validation and rollback. The product expansion gate is defined in root [AGENTS.md](../AGENTS.md).
