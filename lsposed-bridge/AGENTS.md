# LSPosed legacy stock-shim authority

These instructions apply only to `lsposed-bridge/`.

## Product role

- This module is Track C: an optional legacy shim for a supported configuration that still routes
  required DoFun/Topway music operations through genuine stock `com.tw.music`.
- It is not the primary integration path for the current `com.tw.media` player.
- Direct Auxio integration under `app/src/main/java/org/oxycblt/auxio/headunit/topway/` is Track A
  and must be evaluated first.
- No Track B `com.dofun.variety` adapter currently exists. Do not repurpose this module into one.
  A proven launcher-private adapter must be a separate module/artifact with separate instructions.

## Scope and identity

- Keep this module's static scope exactly `com.tw.music` while the legacy shim exists.
- Do not scope or hook DoFun, Auxio, `system_server`, SystemUI, Package Manager,
  `com.tw.service*`, MCU/CAN or unrelated apps from this module.
- Preserve the genuine stock package, platform signer and UID 1000. Never implement signature
  spoofing, shared-UID mutation, package database edits, stock replacement or privilege transfer.
- Target modern libxposed API 100 exactly. Compile against `:libxposed-api100-stubs`; never package
  libxposed, Android platform, Kotlin runtime, IntelliJ or Android tooling classes in the APK.

## Activation and compatibility

- Do not assume the shim is required merely because stock is installed or because this module
  exists historically.
- Functional hooks require the exact package, main process, UID 1000, expected stock signer,
  approved compatibility-registry entry, exact class/method capability and a trusted paired Auxio
  target.
- Private obfuscated presenter hooks require an explicit per-build capability grant. Signer match
  alone is insufficient.
- Verify the installed Auxio signer against an explicit variant-specific expected fingerprint.
  Never trust whichever signer currently owns the target package.
- Debug must pair with `com.tw.media.debug`; release must pair with `com.tw.media`.

## Command and failure semantics

- Preserve one Auxio playback service, queue, MediaSession, notification and audio-focus authority.
- A stock action may be suppressed only after a bounded Auxio-owned protocol returns positive
  `ACCEPTED` after enqueue into the canonical playback command path.
- A void `TransportControls` call or merely sending an asynchronous command is not acknowledgement.
- Do not block either process main thread, deadlock on the same looper, or wait without a strict
  timeout.
- Use command IDs and a short bounded deduplication ledger across receiver, presenter, service,
  reconnect and retry paths.
- Every unmatched, untrusted, unavailable, timed-out or failed path must preserve original stock
  behaviour.

## Kill switch and recovery

- Model the kill switch as `ENABLED`, `DISABLED` or `UNKNOWN`.
- `UNKNOWN` must disable bridge activation while leaving stock behaviour untouched.
- A read error must never silently enable functional hooks.
- Preserve a previously confirmed disabled state across transient read failures where practical.
- Keep diagnostics bounded and rate-limited, and retain a documented emergency rollback.

## Build and release

- Dependency pollution must be removed from the actual runtime classpath; R8 is secondary
  hardening, not the sole fix.
- Inspect every `classes*.dex` in debug and release APKs and reject forbidden defined packages.
- Keep the bridge build/test workflow available while Track C is maintained.
- Manual Release must treat this bridge as opt-in, not a default asset.
- Release builds must be signed, versioned and checked against their paired Auxio artifact.
- Do not claim exact-device, fixed-widget, cold-boot or ACC success without physical execution.
