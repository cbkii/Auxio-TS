# Copilot/Jules implementation prompt: finalise Auxio-TS DoFun integration and the optional LSPosed adapter

Repository:

```text
cbkii/Auxio-TS
```

Target branch:

```text
dev
```

Current repository head observed when this prompt was prepared:

```text
09dec414e4a6a81af6b44af63f9efe87d4a7f5d9
```

Refresh from current `dev` before changing anything. Work on one focused branch and one pull request targeting `dev`. Do not create a second PR for cleanup or review fixes. Do not merge or publish a release unless the user explicitly instructs you to do so.

## Mission

Mature the TS18 DoFun/Topway music integration to the greatest defensible software readiness possible.

The required normal runtime is:

```text
DoFun launcher:       com.dofun.variety
Playback application: Auxio-TS com.tw.media
Playback authority:   Auxio's existing service, queue, MediaSession and notification
```

The user no longer uses genuine stock `com.tw.music` as the music player or normal control target. Do not preserve the current `com.tw.music`-scoped LSPosed design merely because it exists historically.

This is an implementation task, not a report-only task. Audit current code and evidence, choose the smallest correct architecture, implement it, add tests and CI contracts, update documentation, inspect complete review and CI output, and leave one review-ready PR.

Physical TS18 checks remain a separate evidence boundary. Do not defer implementable software fixes merely because physical validation is pending. Never claim an unrun physical check passed.

## Current source audit that must shape the work

At `09dec414e`, Track A is already substantially implemented under
`app/src/main/java/org/oxycblt/auxio/headunit/topway/`. Do not rebuild it from scratch.

Current shape:

- Track A: coordinator, receiver, command-service client/contract, mapper, seek/progress/widget
  policies and playback-service wiring already exist.
- Track B: no `com.dofun.variety`-scoped adapter exists.
- Track C: the existing `lsposed-bridge` is a `com.tw.music` legacy stock shim.
- The Topway-compatible default is `GenericDofunMedia`; direct Topway broadcast and command lanes
  exist but are separately mode-gated.
- Repository instructions and release defaults that still describe Track C as primary are stale
  and must be aligned.

Treat this as a maturity, integration and architecture-selection task, not a greenfield bridge task.

## Mandatory authority document

Read this committed file in full before making decisions:

```text
docs/LSPOSED_BRIDGE_STABLE_RELEASE_ENGINEERING_BRIEF.md
```

Also read all current repository-local instructions and the complete relevant files, including at minimum:

```text
AGENTS.md
lsposed-bridge/AGENTS.md
settings.gradle
app/build.gradle
lsposed-bridge/build.gradle
lsposed-bridge/src/main/resources/META-INF/xposed/scope.list
lsposed-bridge/src/main/java/org/oxycblt/auxio/ts18bridge/*
app/src/main/java/org/oxycblt/auxio/headunit/topway/*
app/src/topwayTwMedia/*
app/src/topwayTwMusic/*
docs/DOFUN_VARIETY_COMPATIBILITY.md
docs/TS18_RUNTIME_VALIDATION.md
docs/TS18_INSTALLATION_CONSTRAINTS.md
docs/RELEASE_WORKFLOW.md
docs/ts18/launcher-integration/*
scripts/check-lsposed-bridge-contracts.sh
scripts/check-dofun-topway-compat.sh
scripts/check-headunit-compat-safety.sh
.github/workflows/lsposed-bridge.yml
.github/workflows/manual-release.yml
```

Inspect the complete history and final artifact from PR #213 and any newer PRs that changed the bridge or Topway integration. Read full review threads, complete job logs and downloaded artifacts. Do not rely on truncated previews.

Use the existing exact-device evidence and APK-derived reference material already committed to the repository. Do not block the task by asking the user to repeat baseline diagnostics that the project already contains. Request new physical evidence only when a post-change result cannot be established from current repository tests or retained exact-device evidence.

## Required architecture decision

Before changing implementation details, produce a written architecture decision inside the PR and update the engineering brief if the conclusion changes. Resolve the following three tracks in this order.

### Track A: direct Auxio integration, preferred

Prefer this architecture whenever DoFun can launch and control Auxio through existing Android and observed Topway-compatible surfaces:

```text
com.dofun.variety
  -> explicit component, known broadcast, MediaSession or MediaBrowser
  -> com.tw.media
  -> Auxio's one canonical playback authority
```

Under this track:

- the normal release must not depend on genuine stock `com.tw.music` running;
- the normal release must not inject LSPosed code into `com.tw.music`;
- Auxio-owned behaviour belongs in the `com.tw.media` application code, not in an LSPosed hook for `com.tw.media`;
- the existing `com.tw.music.MusicActivity` and `com.tw.music.MusicService` class names may remain inside the `com.tw.media` APK as compatibility component names;
- observed Topway actions, extras, metadata and progress broadcasts must be implemented in one isolated Auxio adapter and routed to the existing playback state manager;
- no additional playback service, queue, MediaSession, audio-focus owner or notification authority may be created;
- if no launcher-private interception is required, retire the LSPosed module from the primary release rather than shipping an empty or irrelevant module.

### Track B: narrowly scoped DoFun adapter, only if proven necessary

Use this only when exact DoFun APK analysis and retained or new runtime evidence prove that a release-required fixed-widget operation occurs inside `com.dofun.variety` and cannot be implemented reliably through Track A.

The LSPosed scope for this track is:

```text
com.dofun.variety
```

Do not add `com.tw.music` merely as a historical carry-over.

A DoFun adapter must:

- verify the exact package, signer and compatible APK/version before functional hooks activate;
- hook only exact recovered classes and methods with tested signatures or fingerprints;
- begin log-only and fail open on every uncertainty;
- use bounded explicit cross-process IPC to `com.tw.media`;
- require positive Auxio command acceptance before suppressing any original launcher action;
- use a short command-ID/deduplication ledger to prevent duplicate handling across broadcasts, MediaSession and hooks;
- have an independent circuit breaker and kill switch;
- rate-limit diagnostics and perform no blocking I/O on the launcher main thread;
- never assume Java statics are shared between DoFun and Auxio;
- preserve launcher behaviour whenever target trust, protocol compatibility or command acceptance is not proven.

### Track C: optional legacy stock shim, only if current evidence proves a supported need

The existing module is historically scoped to genuine `com.tw.music`. Keep that path only if current repository evidence establishes a supported configuration where DoFun still routes through the stock process even though Auxio owns playback.

If retained, it must be clearly separated as an optional legacy compatibility variant or artifact. It must not be the default architecture or primary release requirement for this user's device.

A retained legacy shim must:

- remain independently disableable;
- verify UID 1000, stock signer, compatible APK hash or method fingerprints and exact classes;
- fail open to stock behaviour;
- never start or revive stock playback as a side effect of normal Auxio use;
- prove that it does not create duplicate sessions, focus owners, notifications or command execution;
- be excluded from the primary release if the current exact-device path does not need it.

### Forbidden architecture

Do not use this design:

```text
LSPosed scope: com.tw.media
```

Auxio owns that source and process. Implement required behaviour directly in the application unless an exact platform limitation is documented and no ordinary app implementation can satisfy it.

Also do not scope System Framework, SystemUI, Package Manager, `com.tw.service*`, MCU/CAN, DSP, radio, Bluetooth or unrelated applications.

## Evidence that must guide the decision

The exact DoFun APK evidence contains this fixed music match configuration:

```text
com.tw.media/com.tw.music.MusicActivity
com.tw.music/com.tw.music.MusicActivity
```

This establishes that DoFun recognises a direct `com.tw.media` component. It does not by itself prove which path supplies all fixed-widget controls, metadata, progress or seek behaviour.

The observed Topway-compatible public surface includes:

```text
Incoming commands:
com.tw.music.action.cmd
com.tw.music.action.prev
com.tw.music.action.next
com.tw.music.action.pp
com.android.launcher.widget_music_progress

Known extras:
cmd=prev|next|pp|update
music_progress=<milliseconds>

Outgoing metadata:
com.tw.music.info
musicTitle
musicaArtist
musicAlbum
musicPath

Outgoing progress:
com.tw.launcher.music_progress_duration
msg_music_progress
msg_music_duration

Legacy Android broadcasts:
com.android.music.metachanged
com.android.music.playstatechanged
```

Preserve the observed misspelling `musicaArtist`.

The genuine stock APK evidence remains useful for understanding historical command semantics, but static stock methods are not proof that the current direct Auxio path should depend on the stock process.

## First implementation phase: determine and enforce the product boundary

Perform these steps before broad code changes:

1. Map every current control and state path:
   - DoFun fixed-card launch;
   - previous, next and play/pause;
   - seek;
   - metadata and progress publishing;
   - MediaSession and MediaBrowser;
   - any stock receiver/presenter interception;
   - any direct `com.tw.media` receiver or service command handling.
2. Identify the process and authority for each path.
3. Find duplicate paths that can execute the same command.
4. Identify which paths are current product code and which are historical fixtures, tests or documentation.
5. State which of Track A, B and C is selected and why.
6. Change release packaging so only required artifacts are published.

Do not assume that a MediaSession proves DoFun fixed-widget parity. Do not assume that a package match proves command routing. Do not assume that an installed stock package is used merely because it exists.

## Direct Auxio integration requirements

If Track A is selected, mature the direct application integration as follows.

### Component and manifest contracts

Verify and test that the published `topwayTwMedia` release APK has:

```text
application ID: com.tw.media
activity:       com.tw.media/com.tw.music.MusicActivity
service:        com.tw.media/com.tw.music.MusicService
```

Use the existing Auxio service implementation as the only playback service authority. The stock-name service wrapper may delegate to the existing service implementation but must not create a second playback stack.

Ensure provider authorities, permissions, exported flags and intent filters are variant-correct on API 29. Debug must use its real application ID consistently.

### Command ingress

Centralise Topway command parsing in one adapter. Each accepted command must be:

- from an exact allowlisted action;
- structurally validated;
- bounded, especially seek values;
- mapped once to Auxio's canonical playback command path;
- deduplicated for a short bounded interval using command type, source and relevant value;
- safe when the library, queue or current item is unavailable;
- executed off inappropriate external Binder or broadcast threads where needed;
- logged only through bounded diagnostic events.

Do not duplicate the same command through a receiver and MediaSession unless the deduplication contract proves exactly-once behaviour.

A broadcast receiver cannot generally prove the original sender identity. Do not document sender authentication that Android does not actually provide. Prefer explicit/package-targeted surfaces when compatible with DoFun and use strict input validation otherwise.

### State egress

Publish metadata, play state and progress from one owner. Ensure:

- `musicTitle`, `musicaArtist`, `musicAlbum` and `musicPath` are populated consistently;
- progress and duration use milliseconds and are bounded;
- unknown duration, unavailable media and stopped state are represented safely;
- updates are rate-limited and do not wake or flood the launcher unnecessarily;
- process restart causes a complete state refresh;
- no competing publisher emits stale stock state.

### Exactly-once and single-authority rules

The final design must have:

```text
one Auxio playback service authority
one queue authority
one MediaSession authority
one playback notification authority
one Topway command dispatcher
one Topway state publisher
```

Add tests that fail when a second equivalent receiver, service, session or publisher is introduced.

## LSPosed module requirements, if any module remains

Apply this section only to Track B or C.

### APK composition blocker

Reproduce the historical PR #213 bridge artifact composition issue. The inspected debug APK had approximately:

```text
size: 2,680,605 bytes
DEX files: 4
defined classes: 1,658
1,063 kotlin.* classes
505 android.* classes
25 org.intellij.* classes
7 org.jetbrains.annotations.* classes
5 com.android.tools.* classes
```

Determine the exact Gradle dependency cause. Do not guess. Fix debug and release packaging so the module is small and inspectable.

Required module APK contract:

- one DEX;
- no defined `android.*` classes;
- no AndroidX classes;
- no Kotlin runtime unless source deliberately requires it and the decision is justified;
- no IntelliJ, JetBrains or Android build-tool implementation classes;
- no packaged libxposed API definitions;
- only module-owned/generated classes and required resources;
- correct API-100 metadata;
- exact evidence-approved static scope.

Extend `scripts/check-lsposed-bridge-contracts.sh` to inspect defined DEX classes in debug and release artifacts and reject forbidden prefixes.

### Variant pairing and signer trust

Do not hard-code a debug module to target the release application ID.

Expected target pairing is normally:

```text
debug module   -> actual debug Auxio application ID
release module -> com.tw.media
```

Build the paired Auxio APK first, extract its actual signing certificate SHA-256 and compile the expected value into the module. Runtime trust must require the expected package, signing history, enabled component, protocol version and normal non-system UID.

Release builds must fail if signer input is missing or malformed.

### Positive command acceptance

A void `MediaController.TransportControls` call returning without exception proves submission only. It is not an acknowledgement.

Use the smallest API-29-compatible acknowledged command protocol. Prefer an Auxio-owned command request with:

```text
protocol version
command ID
command type
optional bounded value
source adapter
monotonic timestamp
```

and a bounded result:

```text
ACCEPTED
REJECTED_UNSUPPORTED
REJECTED_NOT_READY
REJECTED_UNTRUSTED
REJECTED_DUPLICATE
REJECTED_INVALID
TIMEOUT
```

Suppress original launcher or stock behaviour only after `ACCEPTED`. Timeout, process death, version mismatch or unavailable target must fail open.

Do not block a protected main thread waiting for a long result. Any wait must be strictly bounded and justified by tests.

### Kill switch and circuit breaker

A kill-switch read error must not silently enable functional hooks. Preserve a known disabled state or fail closed to disabled until a successful read.

Add a crash/exception circuit breaker that disables functional suppression for the current process generation after a bounded threshold while preserving ordinary launcher or stock behaviour.

## Testing requirements

Use the actual current test framework and repository conventions. Add the smallest complete test set for the chosen architecture.

### Required pure tests

Cover at minimum:

- action and extra parsing;
- seek bounds and unit conversion;
- command deduplication and expiry;
- unavailable library/current item handling;
- metadata and progress mapping;
- protocol version negotiation;
- positive acceptance, rejection and timeout;
- process generation reset;
- kill-switch read failure;
- circuit-breaker thresholds;
- debug/release package pairing;
- signer mismatch;
- duplicate service/session/publisher contract detection.

### Required Android/API 29 tests

Where practical, use Robolectric or Android integration tests configured for API 29 to cover:

- actual manifest/component resolution;
- explicit activity launch;
- canonical service connection;
- receiver input validation;
- MediaSession command dispatch into the existing playback manager;
- process/service restart behaviour;
- target package and signer checks;
- optional module IPC and Binder death.

### Required artifact contracts

CI must inspect the final APKs, not only source files.

For the player, verify:

- release application ID `com.tw.media`;
- debug application ID matches the real debug variant;
- exact compatibility activity and service components;
- no duplicate exported MediaBrowser service;
- provider authorities use the application ID;
- one canonical playback service and session path.

For an LSPosed artifact, verify:

- API-100 metadata;
- exact selected scope;
- single DEX and class allowlist;
- target application ID and signer embedded correctly;
- no platform, Kotlin, tooling or libxposed API class pollution;
- release workflow publishes it only when the selected architecture needs it.

## Release workflow and documentation

Update release automation and documentation to match the selected architecture.

If Track A needs no LSPosed module:

- remove the module from primary release assets;
- stop describing LSPosed as mandatory for direct `com.tw.media` use;
- retain historical or optional compatibility documentation only where accurate;
- do not delete useful stock APK fixtures needed by tests without replacing their test value.

If Track B is selected:

- publish a clearly named DoFun adapter paired with the `com.tw.media` APK;
- scope it only to `com.dofun.variety`;
- document the exact supported DoFun APK/signature and kill-switch recovery.

If Track C is retained:

- name it as a legacy stock shim;
- keep it separate from the primary exact-device path;
- do not imply it is required for users who run direct `com.tw.media` integration.

Update all stale files that still claim the only supported design is a `com.tw.music`-scoped bridge.

## Physical validation support

Maintain and update this committed collector:

```text
scripts/evidence/collect-ts18-lsposed-bridge-validation.sh
```

Despite the historical filename, it must validate the selected architecture. It must be:

- safe and read-only by default;
- runnable from Termux with root;
- bounded and interrupt-safe;
- local-only;
- exported under `/storage/emulated/0/Download/Auxio-TS/bridge-validation/`;
- explicit about which steps are automated and which require manual observation;
- able to detect whether `com.tw.music` unexpectedly starts during direct Auxio use;
- able to capture DoFun, Auxio, MediaSession, audio focus, service, notification and process state;
- able to validate previous, next, play/pause, seek, metadata and progress;
- able to validate Auxio restart, DoFun restart, cold boot and ACC sleep/wake as manual boundaries;
- unable to disable/uninstall protected packages or alter LSPosed scope automatically.

The script must produce a final PASS/FAIL/SKIPPED/UNCLEAR checklist and a compressed evidence archive.

## Validation commands before completion

Run all relevant repository checks, including the current equivalents of:

```text
./gradlew :app:testTopwayTwMediaDebugUnitTest
./gradlew :app:assembleTopwayTwMediaDebug
./gradlew :app:assembleTopwayTwMediaRelease
./gradlew :lsposed-bridge:testDebugUnitTest        # only if module remains
./gradlew :lsposed-bridge:lintDebug                # only if module remains
./gradlew :lsposed-bridge:assembleDebug            # only if module remains
./gradlew :lsposed-bridge:assembleRelease          # only if module remains
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-ts18-apk-reference-contracts.sh
bash scripts/check-lsposed-bridge-contracts.sh --variant debug   # only if module remains
bash scripts/check-lsposed-bridge-contracts.sh --variant release # only if module remains
```

Use repository wrappers where required. Do not invent successful results. Retain complete logs for failures.

## PR hardening procedure

Before finishing:

1. Rebase or merge current `dev` as repository policy requires.
2. Inspect the full final diff for scope creep.
3. Inspect every changed complete file, not only snippets.
4. Inspect all generated APK manifests, signers, DEX contents and sizes.
5. Inspect every PR review thread and full CI log.
6. Fix valid findings and explain stale, duplicate or out-of-scope comments.
7. Remove temporary workflows, repair scripts, logs, APKs and generated evidence not intended for source control.
8. Ensure documentation describes the architecture actually implemented.
9. Leave physical-only checks explicitly unverified.

## Stop conditions

Stop and request user input only if a safe implementation is blocked by one of these conditions:

- exact current DoFun APK or required method identity cannot be established from repository evidence and a private hook is necessary;
- the target signing key/certificate cannot be obtained during the paired build;
- a proposed change would require platform signing, shared UID mutation, protected package replacement or firmware changes;
- the only apparent solution requires unbounded main-thread blocking or broad guessed hooks;
- rollback cannot be preserved.

Do not stop merely because physical validation remains. Complete all code, tests, CI, documentation and collector improvements that can be completed honestly.

## Required final response

Provide:

1. selected architecture and evidence;
2. why the historical `com.tw.music` scope was retained, made optional or removed;
3. whether a DoFun LSPosed adapter remains and its exact scope;
4. code and documentation changes;
5. tests and complete commands run;
6. APK IDs, signers, sizes, DEX counts and class-contract results;
7. CI and review status;
8. physical checks still requiring the TS18;
9. exact installation, kill-switch, rollback and evidence-collector commands;
10. residual uncertainty.

Do not describe the result as stable-release-ready unless every repository/software gate passes and the required physical matrix has actual retained evidence. Otherwise state precisely whether the PR is software-complete, release-candidate-ready, or blocked.
