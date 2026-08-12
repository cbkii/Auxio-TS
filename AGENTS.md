# Auxio-TS repository engineering authority

This file is the repository-wide authority for engineering work and must be read at the start of every task.

## Instruction precedence

1. Explicit user requirements and safety constraints.
2. This root `AGENTS.md`.
3. A nearer module-local `AGENTS.md`, but only for stricter rules within that directory.
4. The canonical documents: [product scope](docs/PRODUCT_SCOPE.md), [architecture](docs/ARCHITECTURE.md), [development](docs/DEVELOPMENT.md) and [release policy](docs/RELEASE_WORKFLOW.md).
5. Current topic guides and runbooks linked from [docs/README.md](docs/README.md).

Resolve conflicts in favour of the higher authority and update the lower document. Changelogs, evidence, decompilations, generated output, copied vendor files, old prompts, status reports and historical records are non-normative. Consult current architecture before raw evidence.

## Product and repository areas

- **Active product:** `app`, one application installed as `com.tw.media`; namespace `org.oxycblt.auxio`.
- **Internal library:** `musikr`.
- **Optional add-on:** `lsposed-bridge`, Track C, static-scoped only to genuine stock `com.tw.music`.
- **Compile-only support:** `libxposed-api100-stubs`; it must not enter an APK runtime graph.
- **Validation:** `startup-benchmark`, Android tests, JVM tests, lint and screenshots.
- **Tooling:** `scripts`, CI/release automation, diagnostics and root-storage support material.
- **Evidence:** curated observed contracts and device records under `docs`; evidence does not set product policy.
- **Retired:** the generic `standard` app and the exact-package `topwayTwMusic`/`com.tw.music` Auxio app, including their flavours, benchmarks, screenshots and releases.

No Track-B `com.dofun.variety` module exists.

## Architecture boundaries

Track A is direct integration inside the `com.tw.media` app and is the primary architecture. Track C is the optional LSPosed stock shim. Do not repurpose Track C into a DoFun adapter.

Preserve exactly one Auxio playback service, queue authority, MediaSession, notification authority and audio-focus owner. Keep Android framework behaviour separate from Topway, DoFun, MCU/CAN, DSP/radio, root and LSPosed authorities. Preserve Android 10/API 29 behaviour.

Do not add another player, playback service, MediaSession, notification owner, command queue, global audio mode, protected-package mutation, signature spoof, shared UID, platform privilege, vendor-service dependency or Magisk replacement overlay. Never modify or impersonate genuine stock `com.tw.music`.

Keep `com.tw.music.MusicActivity`, `com.tw.music.MusicService` and proven wrapper names as components of `com.tw.media`; component compatibility is not application identity.

## Product expansion gate

Do not add an application flavour or distributable module without an explicit architecture decision that records all of:

- a demonstrated user need and why policy/DI/tests cannot cover it;
- product/module classification and runtime ownership;
- package/component and release policy;
- safety boundaries and interaction with existing authorities;
- validation criteria, physical-device evidence status and rollback plan.

## Engineering workflow

Use the current source and repository-owned checks, not stale prompts. Keep changes within the smallest owning boundary and preserve normal playback, scanning, source authority, startup and UI decisions unless the task explicitly changes them.

Run the narrow relevant checks from [development guidance](docs/DEVELOPMENT.md). CI Gradle calls use `scripts/ci-gradle.sh` with explicit single-product tasks. Never claim a check or physical TS18 scenario passed unless it ran against the reported head.

At minimum for product/build-policy work:

```bash
bash scripts/ci-scope.sh --self-test
bash scripts/check-product-contracts.sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-manual-release-workflow.sh
```

Use evidence labels consistently: **Observed**, **Inferred**, **Proposed** or **Physically unverified**. Emulator and CI results do not prove exact TS18 launcher, widget, USB, ACC, MCU/CAN, DSP or radio behaviour.

Before delivery, review the complete diff for generated APKs, logs, reports, credentials, temporary workflows, copied instructions and stale links.
