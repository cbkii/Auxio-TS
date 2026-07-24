# TS18 root storage fast path

## Purpose

Deliver the fastest practical, picker-independent music-source path for the exact TS18/Topway target while preserving Auxio's single playback, queue, MediaSession, notification and audio-focus authorities.

The primary runtime path is DirectFS over app-readable storage. Root is a first-class, Topway-variant capability for discovering, preparing and validating removable storage when Android's file picker, DocumentsUI, MediaStore or mount timing are unreliable.

This design replaces blanket rules such as "never use root during boot" and "never inspect raw mounts" with narrower authority rules:

- do not block `BOOT_COMPLETED`, cached-library restoration, MediaSession readiness or first audio on an interactive `su` process;
- permit bounded, pre-authorised Magisk late-start preparation in parallel with normal startup;
- permit `/mnt/media_rw/usbdiskN` as an internal discovery/backing path;
- save and scan an app-readable `/storage/...` or validated prepared alias whenever possible;
- never claim root-backed playback from directory listing alone;
- separate storage authority from package, DoFun or protected-app mutation authority.

## Evidence and authority

- **Observed:** the target TS18 exposes removable media through `/mnt/media_rw/usbdiskN` and app-facing `/storage/usbdiskN` paths.
- **Observed:** DocumentsUI/SAF can be absent or unreliable on this platform, so a manual/direct path is required.
- **Observed:** the existing root gate is Topway-flavour scoped, opt-in and bounded.
- **Inferred:** one bounded volume snapshot is materially cheaper and more reliable than spawning `su -c` for each inaccessible directory.
- **Requires TS18 validation:** Magisk late-start timing, prepared-alias visibility to the Auxio UID, mount-namespace behaviour, raw-volume fallback playback, ACC sleep/wake and measured scan latency.
- **Porting decision:** directly reuse DirectFS, cached-library startup and the bounded root runner; introduce a typed storage-only root API and external Magisk preparation; do not extend root consent to protected-package mutations.

## Runtime model

```text
Magisk late_start service.d
    -> wait boundedly for /mnt/media_rw/usbdiskN
    -> map raw volume to /storage/usbdiskN when usable
    -> otherwise create a read-only prepared alias
    -> validate a representative audio file through the prepared path
    -> atomically publish a compact volume manifest

Auxio cached startup (parallel, never blocked)
    -> restore database, queue, MediaSession and first audio
    -> read cached prepared-volume index
    -> prefer configured app-readable DirectFS source
    -> refresh prepared index asynchronously when root is enabled
    -> perform one bounded volume snapshot for changed/unavailable sources
    -> incrementally scan only after end-to-end source authority succeeds
```

## Source authority classes

1. `APP_READABLE` — the Auxio UID can list the root and open a representative audio file directly.
2. `PREPARED_ALIAS` — a Magisk-created read-only alias is app-readable and passes the same open test.
3. `ROOT_SNAPSHOT_ONLY` — root can enumerate the backing volume but no app-readable media path is available; discovery evidence only, not a playable configured source.
4. `UNAVAILABLE` — no bounded path currently succeeds.

Only `APP_READABLE` and `PREPARED_ALIAS` may be persisted as active DirectFS sources.

## Implementation checklist

### 1. Policy and capability model

- [ ] Replace blanket no-root-at-boot/no-raw-path wording with the scoped policy above.
- [ ] Keep standard builds unable to execute Topway root storage operations.
- [ ] Separate storage-read authority from compatibility diagnostics and package mutation authority.
- [ ] Keep every root command fixed/typed, bounded and cancellation-safe.

### 2. Magisk boot preparation

- [ ] Add a bounded `service.d` preparation script and module packaging support.
- [ ] Avoid interactive `su`, app launch, playback start, cache clearing and full scans.
- [ ] Publish the prepared-volume manifest atomically with timestamps and typed state.
- [ ] Provide disable/remove rollback and stale-alias cleanup.

### 3. DirectFS-first TS18 source selection

- [ ] Default fresh Topway-compatible source setup to DirectFS.
- [ ] Present discovered `/storage/usbdiskN`, UUID volumes and validated prepared aliases before SAF.
- [ ] Retain manual path entry and MediaStore/SAF as explicit alternatives.
- [ ] Do not prompt for Magisk for ordinary app-readable paths.

### 4. One root process per volume

- [ ] Replace per-directory root listing with one bounded recursive volume snapshot.
- [ ] Enforce configured-root containment, depth, entry, byte and elapsed-time limits.
- [ ] Reject symlinks and malformed snapshot entries.
- [ ] Preserve ordinary Java file traversal as the zero-root fast path.

### 5. End-to-end validation

- [ ] Validate directory listing and representative media openability before saving a source.
- [ ] Report the exact authority class and actionable failure.
- [ ] Revalidate after permission, root-consent, USB and prepared-manifest changes.
- [ ] Preserve the previous committed library when authority is incomplete.

### 6. Cached prepared-volume index

- [ ] Add an app-private cached index keyed by volume identity and prepared-manifest generation.
- [ ] Load the cached index without `su` during immediate startup.
- [ ] Refresh asynchronously and atomically when root is enabled.
- [ ] Invalidate only changed/missing volumes, not the entire library.

### 7. Raw backing-path fallback

- [ ] Map raw `/mnt/media_rw/usbdiskN` paths to an app-facing alias first.
- [ ] Accept a prepared alias only after the Auxio UID passes an actual media-open test.
- [ ] Keep `ROOT_SNAPSHOT_ONLY` volumes out of playback and persisted source state.
- [ ] Document the mount-namespace/SELinux limitation and physical validation requirement.

### 8. Parallel startup and incremental work

- [ ] Keep cache, queue, MediaSession and first audio on the immediate lane.
- [ ] Run root preparation/index refresh at lower priority in parallel.
- [ ] Coalesce boot/activity/USB refresh requests and prevent scan storms.
- [ ] Never make root failure erase or block a usable cached generation.

### 9. Storage-only root boundary

- [ ] Remove protected `com.tw.music` disable/restore mutations from the runtime root gate/UI.
- [ ] Keep read-only compatibility probes separate and explicitly user-started.
- [ ] Move mutation experiments to external Tier 3 validation tooling.
- [ ] Add guard tests preventing storage consent from authorising package mutations.

## Performance acceptance

Automated tests must prove bounded command construction, one-snapshot traversal, index parsing, source classification, invalid-entry rejection, fresh Topway defaults and no standard-flavour root execution.

Physical TS18 validation must measure:

- cached library and first-audio latency with root disabled and enabled;
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
