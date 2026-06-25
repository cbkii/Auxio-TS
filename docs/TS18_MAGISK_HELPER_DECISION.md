# TS18 Magisk/helper module decision

## Decision

**Evidence confidence: Inferred.**  
**Porting decision: Requires TS18 device validation before any production helper lane.**

For this branch, Auxio-TS stays a normal APK plus the external Magisk/service.d diagnostics collector. A production privileged Magisk module, exact `com.tw.music` replacement module, or alternate `com.tw.media` module is **deferred**.

## Rationale

- **Observed:** the June 2026 TS18 capture ran the Auxio-TS `com.tw.music` variant as a normal app UID, not UID 1000/system. Root/Magisk therefore did not provide platform signing, signature permissions, a system UID, SELinux domain equivalence, or private Topway authority.
- **Inferred:** systemless helper scripts are useful for diagnostics and reversible support workflows, but they do not make private `com.tw.*` or Cardoor APIs safe for production.
- **Requires TS18 device validation:** any helper that changes install placement, allowlists permissions, or replaces stock packages must prove rollback, no boot risk, and no regression to standard Auxio behaviour on the exact device.

## Allowed current lane

- Keep using `tools/ts18-auxio-media-diag-pack-v3-recommended/` as an external Magisk/service.d collector.
- Use late-start `service.d` behavior only; avoid boot-blocking `post-fs-data` logic.
- Keep output under app/user-writable diagnostics directories or `/data/local/tmp` fallbacks.
- Keep uninstall/stop scripts reversible.

## Deferred lanes

- Priv-app Magisk module with permission allowlists.
- Exact `com.tw.music` replacement module.
- Alternate `com.tw.media` module.
- Any helper that writes directly to `/system`, `/vendor`, or dynamic partitions.

Private/native integration remains not for production by default and requires the formal evidence-gated gap-and-promotion process.
