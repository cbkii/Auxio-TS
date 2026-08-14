# LSPosed Track-C module rules

Root [AGENTS.md](../AGENTS.md) remains authoritative. These stricter rules apply only inside `lsposed-bridge`.

- This module is an optional legacy stock shim, not an Auxio app flavour or the primary integration path.
- Keep static scope exactly `com.tw.music`; never add Auxio, DoFun, `system_server`, SystemUI, Package Manager, vendor services or unrelated apps.
- Preserve the genuine stock package and privileges. No signature spoofing, shared-UID mutation, package replacement or privilege transfer.
- Compile against `:libxposed-api100-stubs` only; no libxposed, Android platform, Kotlin, IntelliJ or tooling runtime classes may be packaged.
- Install probes only after exact `com.tw.music` package and main-process routing.
- Current functional activation is bounded by the exact package/process scope plus the fail-safe kill switch. Signer fingerprints collected by build/reference checks are evidence about supplied APKs, not an independent runtime trust anchor; do not describe them as an activation gate unless an independently configured allowlist is implemented and validated.
- Suppress stock behaviour only after the bounded Auxio protocol positively acknowledges command admission. Timeout, mismatch, unavailable and error paths fail open to stock.
- Do not block either process main thread. Preserve bounded deduplication, rate-limited diagnostics and the `ENABLED`/`DISABLED`/`UNKNOWN` kill switch; `UNKNOWN` disables bridge actions.
- Pair debug with `com.tw.media.debug` and release with signed `com.tw.media`. Manual Release keeps this add-on opt-in.
- Inspect every DEX and run module tests/lint/contract checks. Do not claim exact TS18, fixed-widget, boot or ACC success without physical execution.

## Emergency rollback

Follow the full procedure in [the Track-C runbook](../docs/ts18/launcher-integration/LSPOSED_API100_BRIDGE.md#rollback). The minimum incident path is:

1. Create `/storage/emulated/0/Auxio-TS/disable-lsposed-bridge` so bridge actions fail closed while stock behaviour remains available.
2. Disable the bridge module in LSPosed and reboot. If the add-on is no longer required, uninstall only the bridge APK; never delete, replace or modify the genuine stock `com.tw.music` package or its data as rollback.
3. After reboot, verify the bridge is inactive and the genuine stock music activity/control path works normally before removing any recovery safeguard. If Android UI is unavailable, use only the already-proven LSPosed/Magisk boot-loop recovery path; do not use firmware flashing or Package Manager database edits as bridge recovery.
4. Keep the disable marker in place until the incident is understood. Remove it only when deliberately re-enabling the bridge after the cause is addressed, then repeat the normal bridge and stock fail-open validation.
