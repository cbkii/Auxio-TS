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
