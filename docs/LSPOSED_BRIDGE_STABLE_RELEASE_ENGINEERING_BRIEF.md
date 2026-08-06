# Auxio-TS DoFun integration and optional LSPosed adapter engineering brief

Repository: `cbkii/Auxio-TS`

Repository state observed while preparing this brief: `dev` at `1b7d10665c1d296e227ad34320991749c4cea539` on 2026-08-06.

Primary exact-device target: Topway TS18, Android 10/API 29, DoFun Variety launcher `com.dofun.variety`, Auxio-TS player `com.tw.media`.

Purpose: provide a self-contained technical authority for an autonomous coding agent to finalise the DoFun/Topway music integration without relying on private chat history or the original APK attachments.

Evidence labels used here:

- OBSERVED: directly supported by retained device, APK or repository evidence.
- CURRENT USER REQUIREMENT: stated present operating intent.
- INFERRED: reasoned from identified evidence but not directly executed.
- RECOMMENDED: engineering decision to be implemented unless stronger current evidence contradicts it.
- REQUIRES PHYSICAL VALIDATION: cannot be proven by repository work alone.
- UNSUPPORTED: not established by the inspected material.

## 1. Correct product boundary

CURRENT USER REQUIREMENT:

```text
Auxio-TS com.tw.media is the active music player.
Genuine stock com.tw.music is not the user's intended player or normal control target.
```

The current repository contains a historical LSPosed module whose static scope is `com.tw.music`. That design assumes the genuine stock process remains a compatibility relay between DoFun and Auxio. It must not be treated as the permanent architecture merely because it exists.

The intended normal authority chain is:

```text
DoFun fixed music UI and controls
  -> supported Android or observed Topway-compatible surface
  -> Auxio-TS com.tw.media
  -> Auxio's existing playback service, queue, MediaSession and notification
```

The product is a music-control and launcher-state integration. It is not the separate TS18 volume/brightness governor.

It must not:

- add another playback service, queue, MediaSession, audio-focus or notification authority;
- spoof `com.tw.music`, platform signing or UID 1000 for the Auxio APK;
- hook `com.tw.media` merely to modify Auxio-owned code;
- broaden into SystemUI, system_server, Topway volume, brightness, MCU, CAN, DSP, radio or Bluetooth governance;
- replace, delete or re-sign protected stock packages;
- copy private vendor smali as product code;
- claim CI or emulator success proves physical DoFun or ACC behaviour.

## 2. Exact-device and firmware baseline

OBSERVED retained exact-device profile:

```text
Model/product family: s9863a1h10_Natv / s9863a1h10
Hardware family:      uis8581a2h10 / sp9863a
System build:         TS18.2.2_20241210.165912_WINDOW-THEME1
FOTA family:          WINDOW-THEME1_1000
Android:              10
SDK:                  29
Kernel:               4.14.133
Display:              1280x720 landscape
Launcher:             com.dofun.variety
Player target:        com.tw.media
```

The broader platform model is:

```text
vehicle, MCU, CAN, radio, reverse, amplifier, DSP, panel and keys
  -> kernel, HALs and vendor daemons
  -> privileged Topway services and applications
  -> DoFun launcher and fixed widgets
  -> ordinary Android applications such as Auxio
  -> optional Magisk and LSPosed adaptation
```

This task operates only in the final three layers. Root and LSPosed do not provide platform signing, shared UID authority, MCU control or safe firmware writes.

## 3. Relevant current repository structure

At the reviewed baseline the repository contains:

```text
app/                         Auxio application
lsposed-bridge/              optional API-100 module
libxposed-api100-stubs/      compile-time stubs
app/src/topwayTwMedia/       published com.tw.media variant
app/src/topwayTwMusic/       internal exact-package fixture
```

The published Topway player is intended to use:

```text
application ID: com.tw.media
activity class: com.tw.music.MusicActivity
service class:  com.tw.music.MusicService
```

The stock-compatible class names are inside the independently signed `com.tw.media` APK. A Java/Kotlin class package name does not change the APK application ID or grant the stock package identity.

The existing LSPosed metadata uses modern API 100 files:

```text
META-INF/xposed/java_init.list
META-INF/xposed/module.prop
META-INF/xposed/scope.list
```

The current historical scope is:

```text
com.tw.music
```

That scope is a repository observation, not a required future design.

## 4. Exact DoFun evidence

OBSERVED exact launcher APK identity retained by the project:

```text
Package: com.dofun.variety
Version family: V9.7.2.367.260312
SHA-256: 75e7ea9b46d68754253aa385e6ac750aae957a5b72196fec5449ccf2782c60b1
```

Its extracted `assets/apps_match_config.json` includes a fixed music entry that recognises both:

```text
com.tw.media / com.tw.music.MusicActivity
com.tw.music / com.tw.music.MusicActivity
```

The relevant logical record is:

```json
{
  "soft_name": "hotseat_app_music",
  "icon_name": "link_icon_music",
  "compare_soft_name": "Music",
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

OBSERVED conclusion: DoFun has a direct package/component recognition path for the Auxio `com.tw.media` variant.

Not proven by that file alone:

- which component is selected when both packages are installed;
- whether fixed previous/next/play-pause controls use broadcasts, MediaSession, notification state, private Cardoor services or in-process launcher logic;
- whether seek is external or private;
- whether metadata/progress are accepted from ordinary broadcasts in all launcher states;
- whether any current path silently starts genuine stock `com.tw.music`.

Therefore component matching, command routing, state publishing and private launcher control must be analysed as separate lanes.

## 5. Observed public and Topway-compatible contracts

The following strings and behaviours were recovered from exact stock/launcher evidence and repository adapters.

### 5.1 Incoming command actions

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
music_progress = integer milliseconds
appWidgetIds = optional widget IDs on some paths
```

### 5.2 Outgoing metadata

```text
action: com.tw.music.info
extras:
  musicTitle
  musicaArtist
  musicAlbum
  musicPath
```

The misspelling `musicaArtist` is part of the observed compatibility contract and must be preserved.

### 5.3 Outgoing progress and duration

```text
action: com.tw.launcher.music_progress_duration
extras:
  msg_music_progress
  msg_music_duration
```

Values are treated as milliseconds unless current exact evidence proves otherwise.

### 5.4 Legacy Android media broadcasts

```text
com.android.music.metachanged
com.android.music.playstatechanged
```

These are compatibility surfaces, not substitutes for a correct MediaSession.

### 5.5 Android-standard surfaces

The player must retain:

```text
MediaSession
MediaBrowserService
media notification
media-button handling
playback metadata and state updates
```

A valid MediaSession does not by itself prove DoFun fixed-widget operation.

## 6. Stock APK evidence and its correct role

Two genuine Topway stock music APK builds were inspected.

Build A:

```text
File: com.tw.music_TW_THEME.20240715.apk
SHA-256: 4f5495e270a7c86bab232e2b7ee2ecd2d71f3450f6f20ed5f36feaa4229c1518
Package: com.tw.music
Version code: 118
Version name: TW_THEME.20240715
Shared UID: android.uid.system / runtime UID 1000
Certificate SHA-256:
AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3
```

Build B:

```text
File: com.tw.music_ac.apk
SHA-256: 3a14ed3b330723a7f88ae3911804858d370ca673e17d67098cce6c9a543c6b49
Package: com.tw.music
Certificate SHA-256:
AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3
```

Relevant historical stock methods include:

```text
com.tw.music.MusicApplication.onCreate()
com.tw.music.MusicActivity.onCreate(Bundle)
com.tw.music.MusicActivity.onNewIntent(Intent)
com.tw.music.MusicService.onCreate()
com.tw.music.MusicService.onStartCommand(Intent, int, int)
com.tw.music.k.onReceive(Context, Intent)
com.tw.music.j.onReceive(Context, Intent)

com.eckom.xtlibrary.b.f.e.a:
  rb()        previous
  pb()        next
  ba()        pause
  fa()        play
  seekTo(int) seek
```

These methods explain the historical stock-shim implementation and command semantics. They do not prove the current user's direct `com.tw.media` path should execute inside `com.tw.music`.

The stock fixtures remain useful for:

- contract tests;
- documenting action semantics;
- an optional legacy compatibility variant for other exact configurations;
- detecting unintended stock process activation.

They should not control the primary architecture without current evidence.

## 7. Required architecture decision tree

### 7.1 Track A: direct integration, preferred

Use when DoFun can reach Auxio through package/component recognition, broadcasts, MediaSession or MediaBrowser.

```text
com.dofun.variety
  -> external/public integration
  -> com.tw.media
  -> canonical Auxio playback state
```

Expected LSPosed scope:

```text
none
```

The LSPosed artifact should be retired from the primary release if it has no required launcher-private function.

### 7.2 Track B: DoFun-private LSPosed adapter

Use only when exact APK and runtime evidence prove a required fixed-widget behaviour is private to the launcher process and cannot be provided by Track A.

Expected scope:

```text
com.dofun.variety
```

This adapter must be optional, exact-version guarded, log-only first, fail-open and cross-process safe.

### 7.3 Track C: legacy stock shim

Use only when current evidence proves DoFun still requires genuine stock `com.tw.music` as a relay on a supported configuration.

Expected scope:

```text
com.tw.music
```

This must be a separately named optional legacy artifact or variant, not the primary path for the current user.

### 7.4 Scope that is not justified

```text
com.tw.media
```

Auxio owns this code. Required logic belongs in the app source unless a documented platform limitation makes ordinary app code impossible.

## 8. Direct application integration design

### 8.1 Canonical component design

The published `com.tw.media` APK should expose only the required compatibility components.

Recommended contract:

```text
com.tw.media/com.tw.music.MusicActivity
  thin entry point into Auxio UI

com.tw.media/com.tw.music.MusicService
  canonical externally visible MediaBrowser-compatible wrapper
  delegates to the existing Auxio service implementation
```

Do not permit two exported browse services to compete. The underlying Auxio service may remain available for internal explicit use, but external intent filters should resolve to one canonical component.

### 8.2 One command ingress adapter

All Topway actions must be parsed by one isolated adapter, for example under:

```text
app/src/main/java/org/oxycblt/auxio/headunit/topway/
```

The adapter should:

1. allow only exact known actions;
2. validate required extras and types;
3. clamp or reject seek outside the valid current duration;
4. convert to one internal command model;
5. send the command once to the existing playback state manager;
6. maintain a short bounded deduplication ledger;
7. return or log a precise result;
8. remain safe when no library, queue or current song exists.

Suggested internal result model:

```text
ACCEPTED
REJECTED_UNSUPPORTED
REJECTED_NOT_READY
REJECTED_DUPLICATE
REJECTED_INVALID
```

The direct application path does not need to suppress another process. It must still prevent the same external action from entering through multiple receivers or services.

### 8.3 Broadcast trust limitation

An ordinary broadcast receiver often cannot strongly authenticate the original sender. Do not claim sender verification when Android does not provide it for the chosen route.

Use:

- exact action allowlisting;
- explicit/package-targeted delivery where compatible;
- bounded input validation;
- non-exported components where external access is not required;
- permissions only when DoFun can actually hold or satisfy them;
- rate limits and deduplication.

### 8.4 State publisher

There must be one Topway state publisher.

It should derive state from the canonical Auxio playback state and publish:

- title;
- artist;
- album;
- path or safe empty value;
- playing/paused state;
- position and duration in milliseconds.

Rules:

- publish a complete snapshot after process/service reconnect;
- rate-limit periodic progress;
- immediately publish meaningful track or play-state changes;
- avoid stale stock metadata;
- handle unknown duration and unavailable media safely;
- avoid waking the launcher excessively when nothing changed.

### 8.5 Exactly-once contract

The implementation must make these identities explicit:

```text
command ID or deterministic command key
source surface
command type
bounded value
monotonic receive time
process generation
```

A short ledger should reject duplicate commands generated by near-simultaneous broadcast, hook or MediaSession routes. Expiry must be bounded and tested. Do not use long-lived suppression that drops legitimate repeated button presses.

### 8.6 Single playback authority

The final repository should enforce:

```text
one playback service authority
one queue authority
one MediaSession
one playback notification
one command dispatcher
one Topway state publisher
```

Static manifest and source checks should fail if another equivalent authority is introduced.

## 9. Optional DoFun LSPosed adapter design

Apply only if Track B is proven necessary.

### 9.1 Trust and compatibility gate

Functional hooks may activate only after all required checks succeed:

```text
package == com.dofun.variety
expected signer or signing history
approved APK hash/version or exact method fingerprints
expected process name
exact class and method signatures
compatible adapter/Auxio protocol
kill switch enabled state permits operation
```

A signer match alone is not enough to authorise guessed obfuscated methods.

### 9.2 Process isolation

DoFun and Auxio are separate processes. Java statics, singleton instances and in-memory ledgers are not shared.

Use explicit bounded IPC. The adapter should send an Auxio-owned request containing:

```text
protocol version
command ID
command type
optional bounded value
source adapter
monotonic timestamp
```

Auxio should reply with a bounded result such as:

```text
ACCEPTED
REJECTED_UNSUPPORTED
REJECTED_NOT_READY
REJECTED_UNTRUSTED
REJECTED_DUPLICATE
REJECTED_INVALID
TIMEOUT
```

The launcher action may be suppressed only after `ACCEPTED`.

### 9.3 Main-thread safety

Do not perform package hashing, disk I/O, blocking service binds or long Binder waits on the DoFun main thread.

A hook callback may:

- validate already cached state;
- create a small command request;
- use a strictly bounded asynchronous or synchronous path justified by tests;
- fail open immediately on uncertainty.

### 9.4 Circuit breaker and kill switch

The adapter needs:

- a user-accessible kill switch;
- a fail-closed interpretation when kill-switch state cannot be read;
- a bounded exception counter per process generation;
- automatic functional disable after repeated failures;
- rate-limited diagnostic logging;
- ordinary DoFun behaviour preserved after disable.

## 10. Optional legacy stock shim design

Apply only if Track C is retained.

The stock shim must verify:

```text
package com.tw.music
UID 1000
Topway certificate SHA-256
approved APK or method fingerprint
exact main process
exact target Auxio package and signer
compatible protocol
```

The old design's strongest properties should be retained:

- fail-open hooks;
- exact class capability probes;
- no Package Manager or system_server hooks;
- no shared UID mutation;
- no stock APK replacement;
- bounded logs.

It must additionally prove:

- stock playback does not start during normal Auxio operation;
- activity redirection does not initialise a competing stock player;
- each command executes exactly once;
- target process death and reconnect fail open;
- the legacy artifact is not implied to be required by the direct current-device path.

## 11. Existing LSPosed artifact blockers

If any LSPosed artifact remains, the following historical findings must be resolved.

### 11.1 APK class pollution

The inspected PR #213 debug artifact had:

```text
SHA-256: 9614571903ab7cd3eb3e4b7ef49e211f46e598761c59abbbc26b542dec1b0dcf
size: 2,680,605 bytes
DEX files: 4
defined classes: 1,658
```

Approximate defined-class groups included:

```text
kotlin.*                       1,063
android.*                        505
org.oxycblt.auxio.ts18bridge.*    50
org.intellij.*                    25
org.jetbrains.annotations.*        7
com.android.tools.*                 5
```

This is unacceptable for a narrow module injected into a protected launcher or stock process.

The actual Gradle dependency graph must be inspected for debug and release. Do not guess the cause.

Required module artifact:

```text
one DEX
no defined android.*
no androidx.*
no kotlin.* unless intentionally required
no org.intellij.*
no org.jetbrains.* implementation classes
no com.android.tools.*
no packaged io.github.libxposed.* API definitions
only adapter-owned/generated classes and required resources
```

### 11.2 Debug target mismatch

The historical bridge targeted `com.tw.media` in debug while the app debug ID was `com.tw.media.debug`.

Any retained module must compile against the actual paired target application ID, not a hard-coded release ID.

### 11.3 Target signer verification

The target Auxio APK must be signer-verified by a retained module.

Build order should be:

```text
build paired Auxio APK
extract actual application ID and certificate SHA-256
supply those values to the module build
compile them into the module contract
verify the final pair in CI
```

Release must fail if signer input is missing or malformed.

### 11.4 Submission is not acceptance

A void `MediaController.TransportControls` call returning without exception proves submission only.

A retained adapter must use a positive acceptance protocol before suppressing DoFun or stock behaviour. Timeout, version mismatch, unavailable target or Binder death must fail open.

## 12. Build and variant contracts

### 12.1 Player variants

Expected normal pairing:

```text
release player: com.tw.media
debug player:   actual configured debug application ID
```

The build must inspect actual manifests rather than relying on source assumptions.

### 12.2 Internal exact-package fixture

The `topwayTwMusic` variant may remain as an internal compile/test fixture for historical package contracts. It must not be installable or publishable as a replacement for the genuine platform-signed stock package.

### 12.3 Module release decision

Release automation should follow the selected architecture:

Track A:

```text
publish com.tw.media player
no mandatory LSPosed artifact
```

Track B:

```text
publish com.tw.media player
publish clearly named DoFun adapter scoped only to com.dofun.variety
```

Track C:

```text
publish legacy stock shim only as a separately documented optional asset
```

Do not publish an LSPosed module solely because the workflow historically expected one.

## 13. CI and automated tests

### 13.1 Pure unit tests

Required coverage:

- exact action mapping;
- malformed and unsupported actions;
- command deduplication and expiry;
- seek bounds and milliseconds;
- not-ready library/queue/current-item paths;
- metadata and progress mapping;
- state-publisher rate limits;
- process generation reset;
- protocol negotiation and result mapping;
- signer and package mismatch;
- timeout and Binder death for an optional adapter;
- kill-switch read failure;
- circuit-breaker behaviour.

### 13.2 API 29 Android tests

Where practical, cover:

- final manifest component resolution;
- direct `com.tw.media/com.tw.music.MusicActivity` launch;
- canonical `com.tw.music.MusicService` wrapper behaviour inside `com.tw.media`;
- no duplicate exported browse service;
- command receiver routing to existing playback state;
- process/service restart and full state re-publish;
- optional adapter IPC and positive acceptance;
- fail-open behaviour on target or protocol mismatch.

### 13.3 Static and artifact tests

Player APK checks:

```text
correct application ID
correct compatibility component names
variant-correct provider authorities
one external MediaBrowser service
one playback service/session path
no accidental com.tw.music application ID in published player
```

Module APK checks, when present:

```text
API-100 metadata
exact selected scope
one DEX
forbidden class-prefix rejection
embedded target package and signer match final paired APK
small inspectable artifact
```

### 13.4 Scope contract

CI must not hard-code `com.tw.music` as universally required.

It should validate the architecture declaration, for example:

```text
DIRECT_APP          -> no module required
DOFUN_ADAPTER        -> scope exactly com.dofun.variety
LEGACY_STOCK_SHIM    -> scope exactly com.tw.music
```

If multiple optional artifacts exist, each artifact must have its own exact scope and name. Do not combine unrelated scopes into one broad module without a proven reason.

## 14. Documentation corrections

Update all documentation that currently states or implies:

```text
keep genuine stock com.tw.music installed and enabled
use a com.tw.music-only bridge as the normal supported path
LSPosed is mandatory for com.tw.media integration
```

Those statements may remain only in a clearly labelled legacy compatibility section if current evidence still supports that optional mode.

The primary documentation must say:

- Auxio-TS `com.tw.media` is the intended player;
- direct integration is preferred;
- DoFun recognises the direct component;
- LSPosed is needed only for a proven private launcher or legacy stock relay;
- physical validation still controls claims about fixed-widget parity, cold boot and ACC wake.

## 15. Physical validation matrix

The post-change collector must validate the architecture that was actually selected.

### 15.1 Baseline identity

Capture:

```text
build properties and boot ID
package paths and versions for DoFun, Auxio and stock package if installed
signing certificates where tools allow
component resolution
running processes
MediaSessions
AudioManager focus and player state
services, activities and notifications
```

### 15.2 Direct path tests

Required manual tests:

1. Tap fixed DoFun Music card. Auxio opens.
2. Start Auxio playback.
3. Test previous.
4. Test next.
5. Test play/pause twice.
6. Test seek if the launcher exposes it.
7. Confirm title, artist, album and progress.
8. Confirm exactly one audible command result per action.
9. Confirm one active playback MediaSession.
10. Confirm one audio-focus owner.
11. Confirm one playback notification.
12. Confirm genuine stock `com.tw.music` does not unexpectedly become the playback authority.

### 15.3 Lifecycle tests

Required boundaries:

```text
Auxio process restart
DoFun process restart
Android reboot/cold boot
real ACC sleep/wake
USB removal/reinsert while relevant media is selected
```

Do not substitute a warm process restart for ACC or cold boot.

### 15.4 Optional adapter tests

When a DoFun or legacy stock module exists:

- kill switch before launch;
- target unavailable;
- target process death;
- adapter process death;
- protocol mismatch;
- repeated command deduplication;
- circuit-breaker activation;
- ordinary launcher or stock fallback after disable.

### 15.5 Evidence classification

Each test result must be recorded as:

```text
PASS
FAIL
SKIPPED
UNCLEAR
```

Do not convert missing evidence into PASS.

## 16. Diagnostic script requirements

The committed script path remains:

```text
scripts/evidence/collect-ts18-lsposed-bridge-validation.sh
```

The historical filename may remain to avoid path churn, but its content must validate direct Auxio integration first.

Script properties:

- `/system/bin/sh` compatible;
- root-aware;
- read-only by default;
- no package disable/uninstall or data clearing;
- no automatic LSPosed scope changes;
- no unbounded `logcat` or polling;
- timestamped output under `/storage/emulated/0/Download/Auxio-TS/bridge-validation/`;
- explicit command, exit code and execution identity in captures;
- manual prompts for visual/audio results;
- detects unexpected stock process/session activation;
- compresses the completed evidence directory;
- reports final archive path and checksum when available.

## 17. Rollback model

Track A rollback:

```text
install previous Auxio-TS APK
or disable the changed Topway adapter through app configuration if implemented
```

Track B rollback:

```text
disable DoFun adapter in LSPosed or uninstall its APK
restart DoFun/reboot as required
Auxio remains independently usable
```

Track C rollback:

```text
disable legacy stock shim in LSPosed or uninstall its APK
restart stock process/reboot as required
stock behaviour remains available
```

No rollback should require deleting a protected APK or writing system partitions.

## 18. Stop conditions

Say STOP and request evidence or approval when:

- a required DoFun-private method cannot be identified exactly;
- the current DoFun signer/APK identity cannot be established before functional hooks;
- target Auxio signer pairing cannot be generated during the build;
- a proposed path requires platform signing, shared UID changes or protected package replacement;
- only broad guessed hooks or unbounded main-thread waits appear possible;
- rollback is inadequate.

Do not stop merely because physical validation is pending. Complete all software, CI, documentation and collector work that can be completed honestly.

## 19. Expected mature outcome

Preferred outcome:

```text
Auxio-TS com.tw.media directly satisfies DoFun package/component, command, state and media contracts.
The historical com.tw.music LSPosed bridge is retired from the primary release.
No LSPosed module is shipped unless exact evidence proves a launcher-private or legacy relay need.
```

Acceptable evidence-gated alternative:

```text
A small single-DEX DoFun adapter scoped only to com.dofun.variety forwards exact private controls to Auxio through a versioned acknowledged protocol and fails open.
```

Optional compatibility alternative:

```text
A separately named legacy stock shim remains for proven stock-relay configurations, but is not represented as required by the current com.tw.media path.
```

A stable-release claim requires both complete repository/software gates and retained physical TS18 evidence for fixed-widget controls, single playback authority, process restarts, cold boot and ACC wake.
