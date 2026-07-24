# TS18 root storage fast path

## Purpose

Deliver the fastest practical, picker-independent music-source path for the exact TS18/Topway target while preserving Auxio's single playback, queue, MediaSession, notification and audio-focus authorities.

The primary runtime path is DirectFS over an app-readable source. Root is a first-class, Topway-variant acceleration and recovery capability for discovering, preparing and validating removable storage when Android's file picker, DocumentsUI, MediaStore or mount timing are unreliable.

This design replaces blanket rules such as "never use root during boot", "never inspect raw mounts" and "always try non-root first" with narrower authority and cost rules:

- do not block `BOOT_COMPLETED`, cached-library restoration, MediaSession readiness or first audio on an interactive `su` process;
- permit bounded, pre-authorised Magisk late-start preparation in parallel with normal startup;
- permit `/mnt/media_rw/usbdiskN` as an internal discovery/backing path;
- allow cached/prepared root metadata to lead when it is the lower-cost path;
- allow an already-granted root helper refresh to lead for raw or prepared-alias requests;
- avoid starting `su` for an ordinary `/storage/...` path when no cached/root acceleration evidence exists;
- save and scan only an app-readable `/storage/...` path or validated prepared alias;
- never claim root-backed playback from directory listing alone;
- separate storage authority from package, DoFun or protected-app mutation authority.

## Evidence and authority

**Evidence confidence:** high for repository implementation and CI-enforced boundaries; medium for the captured TS18 mount layout; physical alias visibility, access and performance remain **Requires TS18 validation**.

- **Observed:** the target TS18 exposes removable media through `/mnt/media_rw/usbdiskN` and app-facing `/storage/usbdiskN` paths.
- **Observed:** DocumentsUI/SAF can be absent or unreliable on this platform, so a manual/direct path is required.
- **Observed:** the existing root gate is Topway-flavour scoped, opt-in and bounded.
- **Inferred:** one bounded volume snapshot is materially cheaper and more reliable than spawning `su -c` for each inaccessible directory.
- **Inferred:** a cached prepared-volume record containing a representative file can validate authority in O(1), and should lead over a bounded directory walk when available.
- **Requires TS18 validation:** Magisk late-start timing, prepared-alias visibility to the Auxio UID, mount-namespace behaviour, raw-volume fallback playback, ACC sleep/wake and measured scan latency.
- **Porting decision:** directly reuse DirectFS, cached-library startup and the bounded root runner; introduce a typed storage-only root API and external Magisk preparation; do not extend root consent to protected-package mutations.

## Runtime model

```text
Magisk module-root service.sh (late_start service)
    -> wait boundedly for /mnt/media_rw/usbdiskN
    -> map raw volume to /storage/usbdiskN when usable
    -> otherwise create and verify a read-only prepared alias
    -> atomically publish a compact volume/alias manifest (no representative hint at boot)

Auxio immediate startup (never blocked by root)
    -> restore database, queue, MediaSession and first audio

Explicit source/recovery flow
    -> instantiate the app-private prepared-volume index and load its cache without su
    -> resolve sources using authority- and cost-aware ordering
    -> optionally run helper --once to publish bounded representative-media hints
    -> perform one bounded volume snapshot only after playable-path resolution fails
    -> persist a source only after representative media opens in the Auxio process
```

The Magisk helper runs independently during late start. Auxio does not start a second boot-time scanner or block its immediate lane waiting for that helper. The cached prepared-volume index participates in the explicit source/recovery flow, not first-audio startup. A later explicit source flow can invoke the same fixed helper with `--once`; helper and app-side refresh requests are locked, serialized and debounced.

Every detected valid `usbdiskN` receives a manifest row. The on-demand acceleration path limits representative-file searches separately to two volumes at one second each, preserving helper-timeout headroom without silently omitting later volumes.

## Authority- and cost-aware resolution order

The app does **not** enforce a blanket zero-root-first or root-first rule. It selects the lowest expected-cost safe route for the current source:

1. **Cached root metadata first** — when root storage is enabled and a cached prepared-volume record matches the requested volume, try its selected path and representative media first. This does not start `su`; it can reduce validation to one app-UID file open.
2. **Fresh root metadata first** — when root is already granted and the request itself is a raw `/mnt/media_rw/...` backing path or `/storage/auxio-root/...` alias, run the fixed helper once, consume the new manifest and validate the selected path before a direct fallback.
3. **Direct first** — for ordinary `/storage/usbdiskN`, UUID or internal-storage paths without a useful cached record, avoid paying root-process overhead and perform the bounded app-UID validation first.
4. **Root after direct miss** — when a removable app-facing path fails and root is enabled, the explicit source flow may perform the bounded consent probe, refresh the prepared index and retry mapped candidates. This restores acceleration after process restart without putting root on the cache/first-audio lane.
5. **Snapshot only last** — one bounded root snapshot may classify the source as `ROOT_SNAPSHOT_ONLY`, but that state is never persisted for playback.

This policy deliberately allows root-derived acceleration to lead when it is faster, while avoiding unnecessary `su` work and preventing root from delaying first audio.

## Source authority classes

1. `APP_READABLE` — the Auxio UID can list the root and open representative audio directly.
2. `PREPARED_ALIAS` — a Magisk-created read-only alias passes the same app-UID media-open test.
3. `ROOT_SNAPSHOT_ONLY` — root can enumerate the backing volume but no app-readable media path is available; discovery evidence only, not a playable configured source.
4. `UNAVAILABLE` — no bounded path currently succeeds.

Only `APP_READABLE` and `PREPARED_ALIAS` may be persisted as active DirectFS sources. An empty or audio-less volume is not granted playable authority merely because its directory can be listed.

## Implementation checklist

### 1. Policy and capability model

- [x] Replace blanket no-root-at-boot/no-raw-path wording with the scoped policy above.
- [x] Replace blanket zero-root-first ordering with authority- and cost-aware selection.
- [x] Keep standard builds unable to execute Topway root storage operations.
- [x] Separate storage-read authority from compatibility diagnostics and package mutation authority.
- [x] Keep every root command fixed/typed, bounded and cancellation-safe.

### 2. Magisk boot preparation

- [x] Add a bounded module-root `service.sh` late-start path and module packaging support.
- [x] Avoid interactive `su` inside the helper, app launch, playback start, cache clearing and full scans.
- [x] Publish the prepared-volume manifest atomically with timestamps and typed state; add representative-media hints only during bounded on-demand refresh.
- [x] Include every detected valid volume while bounding expensive representative searches independently.
- [x] Fail closed when a read-only remount cannot be established.
- [x] Provide disable/remove rollback and stale-alias cleanup.

### 3. DirectFS-first TS18 source mode

- [x] Default fresh Topway-compatible source setup to DirectFS rather than SAF.
- [x] Present discovered `/storage/usbdiskN`, UUID volumes and validated prepared aliases before SAF.
- [x] Retain manual path entry and MediaStore/SAF as explicit alternatives.
- [x] Prefer cached/root-prepared metadata when it reduces work; prefer direct validation when starting root would add avoidable overhead.
- [x] Request Magisk only from explicit enablement/recovery flows.

### 4. One root process per volume

- [x] Replace per-directory root listing with one bounded recursive volume snapshot.
- [x] Enforce configured-root containment, depth, entry, byte and elapsed-time limits.
- [x] Preserve pathname boundaries and reject control-character, symlink and malformed snapshot entries.
- [x] Preserve ordinary Java traversal as a low-overhead path, not a mandatory first step.

### 5. End-to-end validation

- [x] Validate directory listing and representative media openability before saving a source.
- [x] Use a contained cached representative path as an O(1) validation hint when available.
- [x] Report the exact authority class and actionable failure.
- [x] Revalidate after permission, root-consent, USB and prepared-manifest changes.
- [x] Preserve the previous committed library when authority is incomplete.

### 6. Cached prepared-volume index

- [x] Add an app-private cached index keyed by volume identity and prepared-manifest generation.
- [x] Load the cached index without `su` during immediate startup.
- [x] Allow the cached index to lead source resolution when it is cheaper than a directory walk.
- [x] Refresh atomically only when root storage is enabled, with serialization and debounce.
- [x] Keep root/index refresh independent from full-library invalidation.

### 7. Raw backing-path fallback

- [x] Map raw `/mnt/media_rw/usbdiskN` paths to an app-facing alias first.
- [x] Accept a prepared alias only after the Auxio UID passes an actual media-open test.
- [x] Keep `ROOT_SNAPSHOT_ONLY` volumes out of playback and persisted source state.
- [x] Document the mount-namespace/SELinux limitation and physical validation requirement.

### 8. Parallel startup and incremental work

- [x] Keep cache, queue, MediaSession and first audio on the immediate lane.
- [x] Run Magisk storage preparation independently at late start rather than through `BootReceiver`.
- [x] Lock, serialize and debounce boot/source refresh requests to prevent storms.
- [x] Never make root failure erase or block a usable cached generation.

### 9. Storage-only root boundary

- [x] Remove protected `com.tw.music` disable/restore mutations from the runtime root gate/UI.
- [x] Keep read-only compatibility probes separate and explicitly user-started.
- [x] Move mutation experiments to external Tier 3 validation tooling.
- [x] Add guard checks preventing storage consent from authorising package mutations.

## Performance acceptance

Automated tests must prove bounded command construction, one-snapshot traversal, prepared-representative acceleration, cost-aware ordering, index parsing, source classification, invalid-entry rejection, fresh Topway defaults and no standard-flavour root execution.

Physical TS18 validation must measure:

- cached library and first-audio latency with root disabled and enabled;
- cached-root-metadata-first versus direct-first source validation latency;
- first source discovery without DocumentsUI;
- first and repeat scans for `/storage/usbdisk0` and `/storage/usbdisk1`;
- raw backing volume with successful and failed prepared alias;
- process death, reboot, USB removal/reinsert and ACC sleep/wake;
- number and duration of `su` processes per scan;
- CPU, memory, I/O and thermal impact during the bounded preparation window.

No CI result proves mount namespace visibility, DoFun fixed-panel behaviour, platform signing, UID 1000, MCU/CAN authority or physical TS18 acceptance.

## STOP boundaries

- STOP if the prepared alias cannot be opened by the Auxio UID.
- STOP if the configured source escapes its canonical root or contains symlink traversal.
- STOP if the Magisk module target, app identity, rollback path or mount namespace is uncertain.
- No direct `/system` or `/vendor` writes.
- No platform-signing/shared-UID claim.
- No stock package deletion or automatic disable.
- No second playback service, player, queue, MediaSession or notification owner.
- No generic governor, LMK, zRAM, I/O scheduler or property tuning without measured exact-device evidence.
