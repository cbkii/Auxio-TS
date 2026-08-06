# Auxio-TS LSPosed bridge: stable-release engineering brief

**Repository:** `cbkii/Auxio-TS`
**Baseline reviewed:** `dev` at `d8fc91833601f54aacafcb01ec3a8acc57217126` (2026-08-04)
**Module:** `lsposed-bridge` / package `org.oxycblt.auxio.ts18bridge`
**Runtime target:** Topway TS18, Android 10/API 29, genuine stock `com.tw.music`, Auxio-TS `com.tw.media`
**Purpose of this document:** provide a self-contained technical authority for an autonomous coding agent to mature the optional Auxio-TS LSPosed music-control bridge to the greatest defensible release readiness possible without depending on private chat attachments.

This document is an engineering brief, not proof that physical TS18 behaviour has passed. Claims are labelled **Observed**, **Inferred**, **Recommended**, **Requires physical validation**, or **Unsupported**.

---

## 1. Product boundary

The module is a **music-controls and launcher-state bridge**, not the proposed TS18 brightness/volume governor.

It may:

- execute inside the already-installed genuine stock `com.tw.music` process after strict identity verification;
- redirect the stock music activity to Auxio-TS;
- map observed stock/DoFun music commands to Auxio's one canonical MediaSession/playback authority;
- mirror Auxio metadata, play state and progress using the captured Topway broadcast contract;
- fail open to stock behaviour whenever trust, target readiness, hook compatibility or command acceptance is not proven.

It must not:

- broaden LSPosed scope beyond `com.tw.music`;
- hook DoFun, `system_server`, SystemUI, Package Manager, `com.tw.service*`, MCU/CAN, radio, DSP, Bluetooth or unrelated apps;
- assign, spoof or grant UID 1000/platform identity to Auxio;
- replace, disable, delete, re-sign or mutate the stock APK;
- add another playback service, queue, MediaSession, audio-focus or notification authority;
- implement screen-brightness or volume governance;
- copy vendor smali/private implementations;
- depend on platform signing for the normal `com.tw.media` APK;
- claim emulator/CI success proves fixed-widget, ACC, USB, MCU, DSP or vehicle behaviour.

The supported release layout remains:

```text
Genuine Topway stock package
  com.tw.music
  platform signed
  UID 1000
  kept installed and enabled
        │
        │ LSPosed static scope: com.tw.music only
        ▼
Auxio-TS LSPosed bridge addon
  org.oxycblt.auxio.ts18bridge
        │
        │ public Android IPC
        ▼
Auxio-TS player
  com.tw.media
  normal non-system app UID
  one playback service / one MediaSession / one queue authority
```

---

## 2. Exact TS18 baseline

The primary target is not a generic phone/tablet or an interchangeable TS10-family unit.

**Observed target baseline:**

- platform/build family: `s9863a1h10_Natv`, `uis8581a2h10` / `sp9863a`;
- system build: `TS18.2.2_20241210.165912_WINDOW-THEME1`;
- FOTA family: `WINDOW-THEME1_1000`;
- Android 10 / API 29;
- Linux 4.14.133;
- 4 GB RAM;
- 1280×720 head-unit display;
- DoFun Variety/TWTHEME launcher package `com.dofun.variety`;
- Magisk 28.1 and LSPosed available on the physical unit.

Authority separation remains mandatory:

```text
vehicle wiring / MCU / CAN / radio / reverse / amp / DSP / panel / keys
    → kernel and vendor HALs
    → Topway privileged services
    → DoFun/TWTHEME launcher and fixed widgets
    → ordinary Android apps and MediaSession
    → optional Magisk/LSPosed adaptation
```

The bridge operates only at the final two layers. Root and LSPosed do not provide signing keys, package identity, shared UID records, MCU authority or safe firmware writes.

---

## 3. Current repository architecture

At the reviewed baseline, the bridge uses modern libxposed API 100 packaging:

```text
lsposed-bridge/src/main/resources/META-INF/xposed/java_init.list
lsposed-bridge/src/main/resources/META-INF/xposed/module.prop
lsposed-bridge/src/main/resources/META-INF/xposed/scope.list
```

Expected metadata:

```properties
minApiVersion=100
targetApiVersion=100
staticScope=true
```

Static scope:

```text
com.tw.music
```

Entry class:

```text
org.oxycblt.auxio.ts18bridge.Ts18LsposedBridgeModule
```

Current principal classes:

```text
BridgeCommand.java
BridgeContract.java
BridgeEnvironment.java
MediaMirror.java
Ts18LsposedBridgeModule.java
```

Current Auxio target components:

```text
release app package:      com.tw.media
debug app package:        com.tw.media.debug
activity alias class:     com.tw.music.MusicActivity
MediaBrowser service:     com.tw.music.MusicService
underlying service:       org.oxycblt.auxio.AuxioService
MediaSession callback:    org.oxycblt.auxio.playback.service.MediaSessionInterface
```

`com.tw.music.MusicService` is a thin subclass of `AuxioService`; it does not introduce a second Auxio service implementation. The Topway-compatible manifest removes external browse intent filters from the underlying `AuxioService` and exposes the stock-name wrapper as the canonical cross-package MediaBrowser service.

---

## 4. Evidence inventory

### 4.1 Stock TW Music build A

**Observed:**

```text
File identity: com.tw.music_TW_THEME.20240715.apk
SHA-256: 4f5495e270a7c86bab232e2b7ee2ecd2d71f3450f6f20ed5f36feaa4229c1518
Package: com.tw.music
Version code: 118
Version name: TW_THEME.20240715
Shared UID: android.uid.system / runtime UID 1000
Certificate SHA-256:
AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3
```

This is the exact APK hash currently compiled into `KNOWN_TESTED_STOCK_APK_SHA256`.

### 4.2 Stock TW Music build B

**Observed:**

```text
File identity: com.tw.music_ac.apk
SHA-256: 3a14ed3b330723a7f88ae3911804858d370ca673e17d67098cce6c9a543c6b49
Package: com.tw.music
Certificate SHA-256:
AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3
```

**Observed:** the relevant inspected activity, service, receiver and presenter methods have equivalent signatures and control semantics to build A, despite the different whole-APK hash.

**Recommended:** model private-hook compatibility as a registry of explicitly reviewed APK hashes and hook capabilities. Do not treat a matching signer alone as authority for obfuscated presenter methods. Do not require a single whole-file hash forever when more than one exact build has been reviewed.

### 4.3 DoFun launcher build

**Observed:**

```text
File identity: com.dofun.variety_V9.7.2.367.260312.apk
SHA-256: 75e7ea9b46d68754253aa385e6ac750aae957a5b72196fec5449ccf2782c60b1
Package: com.dofun.variety
```

Its `assets/apps_match_config.json` contains the fixed music entry:

```json
{
  "soft_name": "hotseat_app_music",
  "icon_name": "link_icon_music",
  "compare_soft_name": "音乐,音樂,Music",
  "more_packages": [
    {
      "package_name": "com.tw.media",
      "class_name": "com.tw.music.MusicActivity"
    },
    {
      "package_name": "com.tw.music",
      "class_name": "com.tw.music.MusicActivity"
    }
  ],
  "function": "music_set",
  "behavior": ["fixed"]
}
```

This proves package/component recognition, not full runtime widget control authority. Prior physical evidence showed that the fixed widget could still open or control stock `com.tw.music` while Android MediaSession reported `com.tw.media` active. Component matching, widget binding, command broadcasts, MediaSession and Topway runtime routing must remain separate.

### 4.4 Reviewed bridge CI artifact

The final debug artifact recorded by PR #213 was inspected:

```text
Artifact APK SHA-256:
9614571903ab7cd3eb3e4b7ef49e211f46e598761c59abbbc26b542dec1b0dcf

APK size: 2,680,605 bytes
DEX files: 4
Defined classes: 1,658
```

Observed defined-class groups included approximately:

```text
kotlin.*                       1,063
android.*                        505
org.oxycblt.auxio.ts18bridge.*    50
org.intellij.*                    25
org.jetbrains.annotations.*        7
com.android.tools.*                 5
```

Examples of defined platform classes inside the APK included modern APIs such as:

```text
android.adservices.*
android.app.Notification$CallStyle
android.app.appfunctions.*
android.app.appsearch.*
```

The APK also contained Kotlin built-ins and the Kotlin standard library even though the bridge source is Java.

**Observed release blocker:** a narrow Java-only module injected into a protected UID-1000 process must not package hundreds of platform API definitions, a full Kotlin runtime or build-tool implementation classes. The root cause must be determined from the actual Gradle dependency graph; it must not be guessed.

---

## 5. Verified stock control surfaces

The following exact surfaces were observed in the reviewed stock package.

### 5.1 Bootstrap and activity

```text
com.tw.music.MusicApplication.onCreate()
com.tw.music.MusicActivity.onCreate(android.os.Bundle)
com.tw.music.MusicActivity.onNewIntent(android.content.Intent)
```

Current bridge behaviour:

- install a minimal bootstrap hook only after package/main-process/UID checks;
- asynchronously verify stock signer and target readiness;
- install functional hooks only after stock identity is trusted;
- after stock activity creation, launch Auxio's `com.tw.music.MusicActivity` alias and finish the stock activity only if target launch succeeds.

**Observed risk:** stock `MusicActivity.onCreate()` starts/binds stock music services and registers stock receivers before the current after-hook redirects. This may leave a second stock player stack alive. It does not by itself prove duplicate audible playback, but stable release requires evidence that only Auxio owns active playback, audio focus, notification and queue state after redirection.

### 5.2 Stock service

```text
com.tw.music.MusicService.onCreate()
com.tw.music.MusicService.onStartCommand(Intent, int, int)
```

The bridge must allow the stock service lifecycle method to execute. Skipping `onStartCommand()` can violate foreground-service obligations and destabilise the protected stock process.

Current service hooks should therefore remain lifecycle observation/context capture only unless exact evidence justifies a narrower intervention.

### 5.3 Stock receivers

```text
com.tw.music.k.onReceive(Context, Intent)   // command receiver
com.tw.music.j.onReceive(Context, Intent)   // seek receiver
```

Observed actions and extras:

```text
com.tw.music.action.cmd
  cmd=prev
  cmd=next
  cmd=pp
  cmd=update

com.tw.music.action.prev
com.tw.music.action.next
com.tw.music.action.pp

com.android.launcher.widget_music_progress
  music_progress=<integer position>
```

The current bridge maps these to previous, next, play/pause, update and seek.

### 5.4 Obfuscated presenter

```text
Class: com.eckom.xtlibrary.b.f.e.a

rb()          previous
pb()          next
ba()          pause
fa()          play
seekTo(int)   seek
```

These private methods must remain fingerprint/allowlist gated. Missing or changed methods must not be guessed.

### 5.5 Mirrored output contract

Observed output actions:

```text
com.tw.music.info
  musicTitle
  musicaArtist       // spelling is part of the captured contract
  musicAlbum
  musicPath

com.tw.launcher.music_progress_duration
  msg_music_progress
  msg_music_duration

com.android.music.metachanged
com.android.music.playstatechanged
```

The implicit form and `musicPath` reproduce stock behaviour but expose metadata to any matching receiver. Preserve only fields required for stock parity; keep text and numeric values bounded. Physical validation must determine whether `musicPath` can be omitted or narrowed in a later release without breaking the fixed widget.

---

## 6. Current strengths to preserve

The current module already implements important safety properties:

1. API-100 modern module metadata and compile-only intent.
2. Static scope restricted to `com.tw.music`.
3. Main-process-only loading.
4. Runtime UID 1000 verification.
5. Captured Topway signer verification.
6. Whole-APK fingerprint gating for private presenter hooks.
7. Capability probing of exact classes and signatures.
8. Unknown initial readiness state preserves the stock path.
9. Filesystem and PackageManager probes run away from hot host callbacks.
10. MediaBrowser state is confined to the main looper.
11. Reconnect attempts are bounded.
12. Hot-path logging is rate-limited.
13. Stock service lifecycle is not skipped.
14. Receiver/presenter suppression occurs only after the bridge believes target dispatch succeeded.
15. The normal Auxio app remains a separate non-system package.
16. The bridge does not import private Topway implementation APIs.
17. The bridge manifest requests no Android permissions.
18. Release packaging fails closed when signing properties are absent.
19. CI verifies API metadata, static scope, package, SDK, signer and absence of packaged libxposed API definitions.
20. A shared-storage kill switch and rollback procedure exist.

Maturity work must build on these properties rather than replacing them with broad hooks or privileged shortcuts.

---

## 7. Stable-release blockers and major findings

### 7.1 Packaged runtime/platform-class pollution — critical

**Observed:** the inspected bridge artifact contains four DEX files, a full Kotlin runtime, hundreds of `android.*` class definitions and build-tool classes.

Potential consequences inside `com.tw.music` include:

- class-loader collisions or shadowing;
- verifier/runtime failures on Android 10 for newer platform definitions;
- unnecessary memory, I/O and startup cost;
- a much larger injected attack/crash surface;
- misleading CI that checks only for `io.github.libxposed.*` classes.

**Required:**

- inspect `debugRuntimeClasspath`, `releaseRuntimeClasspath`, dependency insight and packaged inputs;
- identify the precise source of every unexpected class group;
- use a true compile-only modern libxposed API input;
- avoid an Android library dependency that leaks platform stubs into the APK;
- ensure the bridge is Java-only unless a justified Kotlin source is deliberately introduced;
- enforce a single-DEX, strict defined-class allowlist;
- reject `android.*`, `androidx.*`, `kotlin.*`, `org.intellij.*`, `org.jetbrains.*`, `com.android.tools.*`, and packaged `io.github.libxposed.*` definitions;
- inspect both debug and signed release outputs.

Do not set an arbitrary size threshold as the sole proof. Validate class definitions and runtime dependencies directly. A reasonable mature result should be a small single-DEX APK containing only bridge-owned/generated classes and ordinary Android resources.

### 7.2 Debug pairing mismatch — critical

**Observed:** both bridge build types currently compile `TARGET_PACKAGE="com.tw.media"`.

**Observed:** Auxio debug adds `.debug`, producing `com.tw.media.debug`.

Therefore the CI bridge debug APK cannot connect to the CI Auxio debug APK unless a separate release-package Auxio is installed.

**Required target matrix:**

```text
bridge debug   → com.tw.media.debug
bridge release → com.tw.media
```

CI must verify the bridge's compiled target package against the actual paired Auxio APK application ID, not just source strings.

### 7.3 Auxio target signer not verified — critical

The stock host identity is strongly verified, but the target currently needs only the expected package/component names, exported state and non-system UID.

A different APK occupying `com.tw.media` could receive stock-origin launches and control traffic.

**Required:**

- compile the expected paired Auxio certificate SHA-256 into each bridge variant;
- validate current and signing-history certificates with `GET_SIGNING_CERTIFICATES`;
- require target package, certificate, non-system UID, enabled state and exact exported components;
- extract the signer from the actual app artifact in CI/release orchestration and inject it into the paired bridge build;
- fail release packaging when the expected target signer is missing or malformed;
- verify the value again from both final APKs in contract tests.

Debug signing may vary by runner. Build the Auxio debug APK first, extract its actual signer, and then build the debug bridge with that signer rather than assuming a universal debug fingerprint.

### 7.4 Transport submission is not command acceptance — critical/high

Current `dispatchCommand()` returns true after:

- a controller exists;
- the current PlaybackState advertises an action;
- a void `TransportControls` method returns without throwing.

This proves a request was submitted, not that Auxio accepted or executed it. Suppressing stock immediately can drop a user action during process/session races.

**Preferred public-API design to evaluate first:**

```text
MediaController.sendCommand(command, args, ResultReceiver)
    ↔ MediaSessionCompat.Callback.onCommand(command, extras, ResultReceiver)
```

Android API 29 can expose current controller information during a MediaSession callback. The Auxio callback should require the trusted stock controller package/UID/certificate before accepting bridge-only commands.

A mature command envelope should include:

```text
protocol version
command ID
command type
optional seek position
sender elapsedRealtime timestamp
stock package/version/fingerprint capability ID
```

A mature result should distinguish:

```text
REJECTED
ACCEPTED
DUPLICATE_ALREADY_ACCEPTED
UNSUPPORTED
MALFORMED
UNTRUSTED_CONTROLLER
NOT_READY
TIMED_OUT
```

Suppression rule:

```text
suppress stock callback only after a trusted, protocol-compatible Auxio authority
has accepted the exact command ID into the canonical playback command path.
```

If `sendCommand`/`ResultReceiver` cannot provide bounded acceptance without unacceptable host-main-thread blocking, implement the smallest explicit synchronous Binder endpoint through the existing exported `com.tw.music.MusicService` component and a distinct bind action. Do not create another Android service or playback authority. The agent must compare both designs using API 29 behaviour and choose the safer implementation.

Requirements for either design:

- bounded waiting; never indefinite;
- fail open on timeout or interruption;
- exactly-once command-ID ledger with bounded memory/expiry;
- no duplicate execution when public receiver and private presenter paths both fire for one user action;
- no lock held across Binder calls;
- no blocking disk/PackageManager work on the callback path;
- source-specific structured diagnostics in debug builds;
- no command broadcast treated as acknowledgement.

### 7.5 Stock service/player coexistence — high

After stock activity `onCreate`, the stock service/player may already be initialised before redirect.

Stable qualification must establish:

- exactly one active playback MediaSession;
- exactly one audio-focus owner for music;
- exactly one playback notification authority;
- exactly one command effect for previous/next/play/pause/seek;
- no stock audio resuming after Auxio pause, process restart or ACC wake;
- no stock queue advancing in parallel.

Do not skip stock service lifecycle or disable the package to force this result. Instrument first. If a conflict is proven, apply the narrowest fingerprint-gated suppression point after Auxio acceptance while preserving service lifecycle.

### 7.6 Kill-switch read failure is permissive — high

The current shared-storage marker check returns “not disabled” after a runtime read failure.

**Required:**

- preserve a previously observed disabled state on read failure;
- if no successful state has ever been read, fail closed to bridge-disabled;
- distinguish marker absent, marker present and marker unreadable;
- expose the transition in bounded logs;
- consider a modern LSPosed remote preference as a second control plane while preserving the documented shared-storage emergency marker;
- avoid depending on Auxio app-private storage from the stock UID process.

The kill switch must remain effective if media storage is late after boot/ACC wake.

### 7.7 Whole-APK private-hook compatibility is too coarse — medium/high

One known-compatible alternate stock APK has a different full hash.

**Recommended stable model:**

```text
mandatory identity:
  package com.tw.music
  main process
  UID 1000
  trusted Topway certificate

public hook capability:
  exact class + method + parameter/return signature

private presenter capability:
  explicit reviewed APK hash registry
  plus exact class + method signatures
```

Known reviewed APK hashes should be represented as data, not scattered conditionals. Each registry entry should declare which private capabilities are enabled. Unknown hashes retain public capability-probed paths and no private hooks.

A method-level code fingerprint may be added only if it can be implemented and tested reliably without brittle runtime DEX parsing. A two-hash reviewed registry is already safer and more maintainable than one hard-coded hash.

### 7.8 Target protocol/version compatibility — medium/high

Package and signer matching do not prove the installed Auxio and bridge understand the same command protocol.

Add a protocol handshake via MediaSession command or session extras:

```text
bridge protocol version
minimum compatible Auxio protocol
Auxio build/version/commit
supported command bitset
metadata-mirror capability version
```

The bridge must preserve stock behaviour when protocol compatibility is absent.

### 7.9 Diagnostics currently prove too little — medium

Current tests mainly cover pure command/action mapping. Stable maturation needs executable tests for:

- identity state transitions;
- target signer matching and signing history;
- malformed package/component states;
- build-type pairing;
- kill-switch present/absent/unreadable;
- reconnect and Binder/session death;
- command acknowledgement, timeout and duplicates;
- concurrent public receiver/private presenter arrivals;
- exactly-once suppression decisions;
- metadata/progress bounding and deduplication;
- handler/looper confinement;
- APK class inventory and signing;
- release workflow pairing.

Physical runtime evidence remains separate and must not be fabricated.

---

## 8. Recommended target architecture

```text
Stock activity / receiver / presenter event
          │
          ▼
Exact capability hook
          │
          ├── identity snapshot not trusted ──► stock continues
          ├── kill switch disabled/read error ─► stock continues
          ├── target signer/protocol not ready ─► stock continues
          │
          ▼
BridgeCommandEnvelope
  protocolVersion
  commandId
  command
  seekMs?
  source
  sentAtElapsedMs
          │
          ▼
Trusted Auxio command acceptance channel
  preferred: MediaController.sendCommand + ResultReceiver
  fallback: narrow synchronous Binder on existing service
          │
          ├── rejected/timeout/error ──────────► stock continues
          │
          ▼
Auxio canonical playback command enqueue
  one PlaybackStateManager / queue / MediaSession authority
          │
          ▼
ACCEPTED(commandId)
          │
          ▼
Bridge suppresses only the matching stock callback

Auxio MediaSession callbacks
          │
          ▼
Metadata/play-state/progress mirror
  bounded, deduplicated, rate-limited
  emitted from trusted stock process identity
```

### 8.1 State snapshots

Keep immutable/atomic snapshots for:

```text
StockIdentityState
TargetIdentityState
TargetProtocolState
KillSwitchState
ConnectionState
CompatibilityCapabilities
```

Hot callbacks must read snapshots only. Package hashing, signer queries and filesystem checks stay off the hot path.

### 8.2 Command ledger

Use a bounded ledger keyed by command ID and a short source/time deduplication key. It should prevent:

- the same intent being seen through receiver and presenter hooks;
- late acknowledgements suppressing a later unrelated command;
- process restart reusing stale IDs;
- unbounded memory growth.

A process-generation nonce plus monotonic sequence is adequate; random UUIDs are acceptable if allocation impact is bounded.

### 8.3 Fail-open and circuit breaker

Normal failures preserve stock behaviour. Repeated internal failures should trip a local circuit breaker that disables functional bridging for the process generation while leaving logs and stock behaviour available.

Suggested triggers:

- repeated hook callback exceptions;
- repeated target protocol failures;
- repeated acknowledgement timeouts;
- unexpected thread/looper violations;
- invalid state transitions.

Do not attempt self-restart loops inside the stock process.

---

## 9. Build and packaging requirements

### 9.1 API-100 dependency

Use the official modern libxposed API or a provably exact pinned compile-only subset. Public reference:

- `https://github.com/LSPosed/LSPosed/wiki/Develop-Xposed-Modules-Using-Modern-Xposed-API`
- `https://github.com/libxposed/api`

Do not package API classes. Do not silently migrate API packages or metadata unless the pinned API source and installed LSPosed runtime prove the change.

### 9.2 Variant pairing

The bridge build must derive target package and signer from the paired Auxio artifact:

```text
debug:
  bridge app ID: org.oxycblt.auxio.ts18bridge.debug
  target app ID: com.tw.media.debug
  target signer: extracted from paired debug app APK

release:
  bridge app ID: org.oxycblt.auxio.ts18bridge
  target app ID: com.tw.media
  target signer: extracted from paired signed release app APK
```

The bridge release signer and Auxio target signer may be the same key today, but the implementation must compare the compiled target value to the actual target APK rather than assume they are identical.

### 9.3 APK contract

CI must prove for both bridge variants:

- correct application ID;
- min SDK 29;
- expected target SDK;
- no requested permissions unless a future permission is explicitly justified;
- non-debuggable release;
- valid expected release signer;
- exact API-100 metadata;
- exact scope `com.tw.music`;
- one DEX file;
- entry class is defined;
- no packaged libxposed classes;
- no `android.*`, AndroidX, Kotlin, IntelliJ, JetBrains or build-tool class definitions;
- compiled target package and signer match the paired Auxio APK;
- release output contains no diagnostics/test fixtures;
- no raw exact-package Auxio APK or retired Magisk overlay is published.

The check should parse DEX class definitions, not rely on ZIP filenames or source greps alone.

### 9.4 Release workflow ordering

A reliable sequence is:

```text
1. build/sign Auxio app artifact
2. inspect and validate app package, SDK, ABI and signer
3. pass validated app ID/signer/protocol version into bridge build
4. build/sign bridge artifact
5. validate bridge package, metadata, class inventory and embedded target contract
6. stage both artifacts and sidecars
7. publish only after all paired checks pass
```

Preserve the repository's transactional Manual Release design.

---

## 10. Auxio command acceptance design

### 10.1 Preferred MediaSession command route

Public Android references:

- `https://developer.android.com/reference/android/media/session/MediaController#sendCommand(java.lang.String,android.os.Bundle,android.os.ResultReceiver)`
- `https://developer.android.com/reference/android/support/v4/media/session/MediaSessionCompat.Callback#onCommand(java.lang.String,android.os.Bundle,android.os.ResultReceiver)`
- `https://developer.android.com/reference/android/support/v4/media/session/MediaSessionCompat#getCurrentControllerInfo()`

Add one isolated Auxio-owned contract, for example:

```text
org.oxycblt.auxio.ts18.bridge.COMMAND
```

Do not reuse arbitrary notification/custom-action strings.

In `MediaSessionInterface.onCommand`:

1. reject unknown command names;
2. read controller info only during the callback;
3. require `com.tw.music` and UID 1000 on API 29;
4. resolve packages for the UID and verify the stock certificate using an isolated verifier;
5. validate protocol, command ID, command enum, seek range and age;
6. deduplicate command ID;
7. enqueue through the same `PlaybackStateManager` methods used by normal MediaSession controls;
8. return a small result code/bundle;
9. never perform disk I/O or long PackageManager work repeatedly on the callback—cache verified controller identity with safe invalidation;
10. fail closed for this private command while normal public MediaSession controls remain unaffected.

Do not expose the command as a general exported broadcast.

### 10.2 Suppression timing

The stock hook must not wait indefinitely. Measure callback latency on API 29 and TS18. Use the shortest defensible bounded timeout; a timeout means stock continues.

Avoid blocking the stock main thread where possible. If the public API route cannot return acceptance within a safe bound, use a direct Binder endpoint on the existing service rather than increasing the timeout.

### 10.3 Commands

Minimum protocol commands:

```text
PREVIOUS
NEXT
PLAY_PAUSE
PLAY
PAUSE
SEEK_MS
PUBLISH_NOW / UPDATE
```

`UPDATE` may be accepted only when it schedules a bounded metadata/state publication; it must not pretend to be a playback action.

### 10.4 Exactly-once behaviour

For each physical/widget input, acceptance criteria are:

```text
one observed source event
→ one bridge command ID
→ one Auxio acceptance
→ one canonical playback state change
→ one stock suppression decision
→ no duplicate public/private path effect
```

---

## 11. Metadata and progress maturity

Retain the current mirror model but harden it:

- all MediaBrowser and MediaController operations stay on their construction looper;
- register/unregister callbacks exactly once per controller generation;
- clear stale callbacks on session destruction;
- bounded reconnect with user-action retry after exhaustion;
- publish metadata only when target identity/protocol remains trusted;
- bound each string and URI;
- coalesce identical metadata/play-state updates;
- one-second progress only while playing;
- publish a final progress update on pause/seek/track change;
- use elapsed realtime and playback speed to estimate position;
- clamp position to `[0, duration]` where duration is known;
- avoid negative speed/overflow surprises;
- stop ticks promptly on disconnect, kill switch or circuit breaker;
- keep implicit broadcast privacy documented;
- keep release logs minimal.

Do not create a second polling service.

---

## 12. Hook compatibility and lifecycle rules

### 12.1 Public surfaces

Each activity/service/receiver hook is an independent capability. Resolve exact methods before installing each hook and report accurate per-capability status. Partial availability is acceptable when the unavailable path remains stock-controlled.

### 12.2 Private presenter surfaces

Install only for a reviewed APK registry entry and exact signatures. Current reviewed hashes:

```text
4f5495e270a7c86bab232e2b7ee2ecd2d71f3450f6f20ed5f36feaa4229c1518
3a14ed3b330723a7f88ae3911804858d370ca673e17d67098cce6c9a543c6b49
```

### 12.3 Activity redirect

Keep redirect fail-open:

- target identity/protocol trusted;
- exact activity enabled/exported;
- start succeeds;
- finish stock activity only after successful target launch.

Do not skip stock `Activity.onCreate()` until a safe lifecycle design is proven. Instrument the stock-service side effect and address only confirmed conflicts.

### 12.4 Process restarts

Validate independently:

- bridge module load;
- stock process death/reload;
- Auxio process death/reconnect;
- DoFun restart;
- cold boot;
- ACC sleep/wake.

Static fields are process-local and reset on stock process death.

---

## 13. Kill switch and recovery

Existing marker:

```text
/storage/emulated/0/Auxio-TS/disable-lsposed-bridge
```

Requirements:

- marker present means disabled;
- marker unreadable means disabled until a successful read proves absence;
- marker state changes are detected within a bounded interval;
- no hot-path filesystem I/O;
- previously disabled state is never lost because of one read failure;
- a second LSPosed-managed preference may be added if supported by the pinned API;
- module disable + reboot remains the primary rollback;
- genuine stock package and data remain untouched.

**STOP installation/activation when:**

- stock package is not UID 1000;
- stock certificate differs;
- static scope cannot remain exactly `com.tw.music`;
- target Auxio package/signer/protocol differs;
- boot-loop recovery is unavailable;
- the bridge APK contains unexpected runtime/platform classes;
- the physical unit lacks a verified rollback path.

---

## 14. Required automated verification

### 14.1 Pure unit tests

Cover:

- action/extra mapping;
- seek bounds;
- protocol envelope validation;
- result-code mapping;
- command-ID deduplication and expiry;
- suppression decision state machine;
- kill-switch tri-state behaviour;
- circuit breaker;
- stock compatibility registry;
- signer digest normalisation/history matching;
- target package matrix;
- metadata/progress bounds;
- reconnect delay bounds;
- log-rate limiter.

### 14.2 Android/Robolectric tests

Cover where practical:

- PackageManager identity combinations;
- exported/enabled component checks;
- target signer mismatch;
- controller identity validation;
- ResultReceiver acceptance/rejection/timeout;
- handler confinement;
- process/session destruction simulation;
- activity redirect success/failure;
- no stock suppression for unknown state.

### 14.3 API 29 integration tests

Create a testable public-API harness that proves:

- MediaBrowser connection to the stock-name Auxio wrapper;
- custom command delivery and ResultReceiver response;
- current controller information on API 29;
- malformed/untrusted command rejection;
- one command produces one canonical playback-manager call;
- controller/session death reconnects cleanly.

Do not weaken release trust checks merely to enable the harness. Use test-only dependency injection or fixtures excluded from release.

### 14.4 Build/CI checks

Run and retain evidence for:

```bash
bash ./scripts/bootstrap-dependencies.sh --profile full-build
bash ./scripts/check-lsposed-bridge-contracts.sh --variant debug
bash ./scripts/check-dofun-topway-compat.sh
bash ./scripts/check-headunit-compat-safety.sh
bash ./scripts/check-manual-release-workflow.sh
bash ./scripts/ci-gradle.sh \
  :lsposed-bridge:testDebugUnitTest \
  :lsposed-bridge:lintDebug \
  :lsposed-bridge:assembleDebug \
  :app:testTopwayTwMediaDebugUnitTest \
  :app:assembleTopwayTwMediaDebug
```

Add the relevant API 29 and paired-artifact tasks developed by the implementation. Release-equivalent proof must use the actual current head and explicit signing inputs; never claim a signed release check from an unsigned debug build.

---

## 15. Physical TS18 validation authority

A comprehensive repo-owned collector is provided at:

```text
scripts/evidence/collect-ts18-lsposed-bridge-validation.sh
```

It is read-only by default, writes under `/storage/emulated/0/Download`, records manual observations and captures timestamped package/process/MediaSession/audio/activity/log evidence. Optional kill-switch management and force-stop tests require explicit environment flags.

### 15.1 Core acceptance matrix

1. **Identity and scope**
   - bridge reports trusted stock UID/certificate;
   - correct reviewed stock hash/capabilities;
   - correct Auxio package/certificate/protocol;
   - LSPosed scope contains only `com.tw.music`.

2. **Kill switch enabled**
   - stock music behaviour remains available;
   - no redirect, forwarding or mirrored state from the bridge;
   - no stock-process crash.

3. **Widget launch**
   - fixed DoFun music widget opens Auxio;
   - stock activity closes;
   - no duplicate foreground UI.

4. **Exactly-once controls**
   - previous, next, play, pause, play/pause and seek each affect Auxio once;
   - no simultaneous stock queue change;
   - no lost command when Auxio is ready;
   - stock path remains when Auxio is unavailable/untrusted.

5. **State mirror**
   - title, artist, album, play state, duration and progress track Auxio;
   - seek units are milliseconds;
   - progress stops/settles on pause;
   - track transitions do not retain stale metadata.

6. **Single authority**
   - one active playback MediaSession;
   - one audio-focus owner;
   - one playback notification;
   - no stock audio resumes unexpectedly.

7. **Recovery**
   - Auxio process restart;
   - stock process restart;
   - DoFun restart;
   - cold boot;
   - ACC sleep/wake;
   - USB removal/reinsert;
   - live kill-switch activation.

8. **Performance/stability**
   - no ANR, fatal exception or boot loop;
   - no unbounded reconnect/log loop;
   - no persistent high CPU or storage writes;
   - bridge APK startup does not materially delay stock process/launcher readiness.

### 15.2 Evidence classification

Physical results must be recorded as:

```text
PASS
FAIL
SKIPPED
UNCLEAR
```

A missing or unclear result is not a pass. CI/emulator results must not fill physical columns.

---

## 16. Release-readiness gates

The agent should advance implementation as far as possible in one focused PR, but final status must be honest.

### Software-complete gate

All must pass:

- no unexpected packaged classes;
- correct debug/release pairing;
- stock and target signer verification;
- protocol handshake;
- accepted-command path with exactly-once tests;
- fail-open suppression state machine;
- kill-switch tri-state hardening;
- reviewed stock compatibility registry;
- comprehensive unit/API29/contract CI;
- signed paired release workflow validation;
- debug/release diagnostics separation;
- docs and physical collector current.

### Stable physical gate

All applicable core acceptance matrix items must pass on the exact TS18 build, including cold boot and ACC sleep/wake.

Until physical evidence is supplied, the correct conclusion is:

```text
software implementation and CI hardened;
physical stable-release qualification still pending.
```

Do not artificially preserve bugs to keep the module “experimental”. Implement mature safeguards and tests now. Do not remove physical-validation gates or claim they passed.

---

## 17. Public references

Use primary sources for API decisions:

- Modern LSPosed module development: `https://github.com/LSPosed/LSPosed/wiki/Develop-Xposed-Modules-Using-Modern-Xposed-API`
- libxposed API source: `https://github.com/libxposed/api`
- `MediaController.sendCommand`: `https://developer.android.com/reference/android/media/session/MediaController#sendCommand(java.lang.String,android.os.Bundle,android.os.ResultReceiver)`
- `MediaSessionCompat.Callback.onCommand`: `https://developer.android.com/reference/android/support/v4/media/session/MediaSessionCompat.Callback#onCommand(java.lang.String,android.os.Bundle,android.os.ResultReceiver)`
- `MediaSessionCompat.getCurrentControllerInfo`: `https://developer.android.com/reference/android/support/v4/media/session/MediaSessionCompat#getCurrentControllerInfo()`
- Platform MediaSession controller identity: `https://developer.android.com/reference/android/media/session/MediaSession#getCurrentControllerInfo()`
- MediaBrowser threading: `https://developer.android.com/reference/android/support/v4/media/MediaBrowserCompat`

The repository's own source, current CI logs/artifacts and exact-device evidence remain the implementation authority. Public API documentation does not prove DoFun or TS18 runtime behaviour.

---

## 18. Final engineering position

**Observed:** the current bridge has a sound narrow trust/scope concept and many fail-open safeguards.

**Observed:** its inspected debug APK packaging is unacceptable for injection into a UID-1000 stock process.

**Observed:** debug bridge/app packages are not paired.

**Observed:** the target Auxio signer and protocol are not verified.

**Observed:** void MediaController transport submission is not positive command acceptance.

**Observed:** two reviewed stock APK hashes share the trusted signer and relevant control semantics.

**Inferred:** stock service initialisation before redirect can create duplicate authority unless proven inert.

**Requires physical validation:** fixed-widget consumption, exactly-once controls, single playback authority, restarts, cold boot and ACC sleep/wake.

**Recommended implementation outcome:** a small, single-DEX, statically scoped, dual-signer-verified, protocol-paired API-100 bridge that suppresses only positively accepted commands, retains stock lifecycle and rollback, and ships with complete CI and a bounded physical validation collector.
