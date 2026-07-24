## Auxio-TS app/runtime priority rules

- Auxio-TS is a TS18/TW/TWTHEME variant app; app/runtime behavior is the priority.
- Source-backed compatibility work should improve real runtime surfaces, not only validation tooling.
- Evidence/validation tooling is primary only when requested or when implementation cannot proceed safely.
- For app-feature tasks, implement visible behavior, route/action wiring, settings/runtime effects, metadata/session/widget improvements, or compatibility call-site wiring.
- Docs/tests/fixtures must not substitute for app code implementation.

## Compatibility-layer wiring rules

- A headunit/compat feature is not implemented until consumed by at least one meaningful runtime call-site.
- Registry entries alone do not count as implementation.
- Status models alone do not count as implementation.
- Metadata policy is not implemented until used by MediaSession, notification, widget, or another runtime publisher.
- Parity maps are not implemented until they drive or verify action/route completeness.
- Settings/status are not implemented until surfaced via existing UI/settings patterns.

## Final response discipline

Always report explicitly:

- which areas were wired into runtime code,
- which areas are scaffold-only,
- which areas remain partial,
- which partials are locally fixable and why not fixed,
- whether output is a review snapshot or complete,
- why any next scope is truly separate from current acceptance criteria.

2026-05-24 implementation note: isolated Topway bridge runtime wiring now exists; keep Topway strings limited to approved bridge/test/docs paths and preserve no-binder/no-impersonation safety boundaries.

## Seeded TS18 exact-device context

Agents must read these concise, redacted context files before exact-device TS18 install/runtime work:

- `docs/CODEX_TS18_DEVICE_CONTEXT.md`
- `docs/TS18_INSTALLATION_CONSTRAINTS.md`
- `docs/evidence/ts18-device-profile/s9863a1h10-android10-termone-2026-05-17.md`

Direct dependencies on external/vendor `com.tw.*` APIs remain forbidden in production code. Thin compatibility wrapper classes under approved Topway/DoFun source sets are allowed only to expose stock-compatible package/class/component names and delegate into Auxio-owned code. Approved wrapper areas include `app/src/topwayCompat/java/com/tw/music/**` (and any future explicitly shared Topway/DoFun wrapper equivalent).

`com.tw.media` is an alternate DoFun fixed-entry variant, not a general no-root bypass. It may conflict on some firmware and still requires real-device validation. Private/native integration remains not for production by default and requires the evidence-gated tier process.

## Auxio-TS Topway/TS18 root storage policy

- Auxio-TS is a Topway/TS18-focused variant app.
- DirectFS is the primary source-selection mode for fresh Topway-compatible installs; SAF and MediaStore remain explicit alternatives.
- Root is a first-class **storage acceleration and recovery** capability on Topway variants, centrally gated by `RootStateHolder` and explicit user consent.
- Do not impose a blanket direct-first or root-first order. Select the lowest expected-cost safe route from cached prepared metadata, already-granted root preparation, ordinary app-UID access and bounded snapshot fallback.
- Cached prepared-volume metadata may lead without starting `su`, especially when its contained representative-media hint reduces validation to one app-UID file open.
- An already-granted root helper may lead for raw `/mnt/media_rw/...` or prepared-alias requests. Do not start root for an ordinary `/storage/...` path when no acceleration evidence exists.
- Do not block `BOOT_COMPLETED`, cache restore, MediaSession readiness or first audio on interactive `su`. Pre-authorised Magisk late-start storage preparation may run independently and publish a bounded manifest.
- `/mnt/media_rw/usbdiskN` is an approved internal backing/discovery path. Persist and play only through an app-readable `/storage/...` path or an app-UID-validated prepared alias.
- A root directory snapshot is discovery evidence only; it does not prove TagLib, artwork or playback file access.
- Root storage operations must be fixed/typed, read-only, one snapshot per changed volume, bounded to 2s probes and at most 20s storage operations, and safely degraded.
- Root storage consent must not authorise protected-package disable/enable, system writes, platform identity, MCU/CAN or vendor-service mutations.
- Product runtime diagnostics remain bounded and user-started; protected-package mutation experiments belong in external Tier 3 tools.
- Playback Stability: all shuffle modes preserve the current track. Autoplay must not be interrupted by background root/index refreshes.
- Album-Art Modes remain `off`, `as-is`, and `optimised`.
