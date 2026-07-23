# First-launch music source and scan recovery

## Purpose

Restore the previously usable first-launch and missing-library recovery flow for Auxio-TS v6.4.2, then harden it for TS18/Topway Android 10 units.

The current build can present a large indeterminate loader while hiding the actions needed to configure, authorise or rescan a library. System-database permission is only available through a manually tapped card, Direct mode cannot initiate the root consent/probe needed for raw TS18 paths, and the v6.4.2 Home simplification removed the parallel quick-action surface without replacing its recovery role.

This work restores an explicit, state-driven recovery workflow while preserving the playback-first startup architecture, one canonical player/service/session/queue/notification authority, API 29 compatibility, bounded work, source-generation safety and Topway scan-storm protections.

## User-visible goals

- A fresh install explains what is missing and offers the correct action instead of showing only a spinner.
- Storage/library permissions needed by the selected source mode are requestable from inside Auxio-TS.
- Direct mode tries ordinary `/storage/...` access first and requests Magisk/root only when the chosen path or requested operation genuinely needs it.
- Root-enabled Topway variants may opt into bounded boot-time/early-prestart preparation to improve launch speed.
- Missing, inaccessible, empty, failed or unconfigured libraries expose reliable `Choose source`, `Refresh` and `Full rescan` recovery actions.
- Automatic work remains origin-aware: user-confirmed setup may scan immediately, while boot/ACC paths remain bounded and opt-in.

## Current behaviour

- `LocationsDialog` registers a storage permission launcher, but invokes it only from the permission card.
- `HomeFragment` retains a storage permission launcher whose result refreshes the library, but the current Home flow does not launch it.
- DirectFS first attempts ordinary `File.listFiles()` and only falls back to `RootGate`; this ordinary-first behaviour must be preserved.
- Root-assisted access is separately disabled by default and the source picker only receives a root gate when root is already `Available`, creating a first-run dead end for raw `/mnt/media_rw/...` paths.
- `StartupLibraryStatus` already distinguishes `Unknown`, `Usable`, `Empty`, `NeedsMusicSource`, `CacheUnavailable` and `SourceUnavailable`, but the empty-library UI does not consume those distinctions.
- PR #190 removed `home_head_unit_shortcuts`, `home_quick_picks` and `home_metadata_chips`; the existing spinner/empty-state implementation remained without a replacement recovery surface.
- Topway startup currently suppresses automatic scans broadly. This prevents scan storms but also leaves some configured-source/no-cache launches without a direct recovery path.

## Implementation checklist

### Recovery state model

- [ ] Add a dedicated library/source recovery UI policy derived from startup library status, indexing state, source configuration, Android permission, Direct source accessibility, root state, prior library state and last-scan failure.
- [ ] Keep startup capability ordering separate from recoverable source/library state.
- [ ] Distinguish bounded cache wait, permission required, source required, source unavailable, cache unavailable, indexing, confirmed empty, failed scan and usable library.
- [ ] Ensure a stale readiness spinner cannot hide recovery indefinitely.

### Home first-launch and missing-library UI

- [ ] Replace the spinner-only empty presentation with a compact state-driven recovery panel.
- [ ] Restore contextual `Choose source`, `Refresh`, confirmed `Full rescan`, `Grant access` and root-enable actions.
- [ ] Keep useful actions visible while indexing; disable only actions that would be unsafe or duplicate active work.
- [ ] Show meaningful phases/messages rather than a generic indefinite loader.
- [ ] Do not restore unrelated metadata/decade/startup-song chip clutter merely to fix recovery.
- [ ] Preserve TS18 touch target sizing and runtime insets/bounds.

### In-app Android permission flow

- [ ] Request the permission appropriate to the selected source mode from within the app.
- [ ] System Database mode on Android 10 requests `READ_EXTERNAL_STORAGE` when missing.
- [ ] Direct mode requests Android storage access when required for the selected ordinary path.
- [ ] File Picker/SAF mode does not request broad storage permission unnecessarily.
- [ ] Explain why access is needed before launching the system prompt.
- [ ] Revalidate source state after grant and trigger exactly one refresh after valid source confirmation.
- [ ] Detect permanent denial and provide an `Open app settings` recovery action.
- [ ] Remove or correctly use the orphaned Home permission launcher.
- [ ] Keep visualiser `RECORD_AUDIO` permission independent.

### DirectFS and root consent

- [ ] Preserve ordinary app-readable DirectFS as the first choice.
- [ ] Detect when a selected/manual raw path requires root rather than prompting merely because Direct mode was selected.
- [ ] From the source dialog, provide explicit informed consent for root-assisted access.
- [ ] On confirmation enable the authoritative root-access preference, run the bounded `su -c id` probe off-main-thread, show the resulting state, rerun source discovery and revalidate the path.
- [ ] Retain ordinary `/storage/...` fallback paths when root is unavailable.
- [ ] Unify or clearly coordinate `RootAccessPolicy` and `auxio_use_root_fs` so the UI reflects one user decision.
- [ ] Standard variant remains unable to execute root-assisted Topway behaviour.

### Optional root-enabled boot-time / early-prestart

- [ ] Add an explicit Topway-only opt-in for bounded early preparation when root access is enabled and verified.
- [ ] Keep it disabled by default and explain boot/ACC implications.
- [ ] Allow the app to enable/disable the supported prestart mechanism from its own settings/recovery UI.
- [ ] Use an app-owned narrow readiness entry point; shell must not edit app-private databases or caches.
- [ ] Early preparation may verify configured direct sources/mount aliases, warm bounded startup projections/cache access and request only the existing canonical service/start path.
- [ ] Early preparation must not start playback, launch `MainActivity`, run an unconditional full scan, create duplicate playback authority, block `post-fs-data`, or write protected system/vendor/firmware data.
- [ ] Prefer late-start/service-time behaviour with timeout, duplicate-run guard and rollback.
- [ ] Keep any Magisk-module script generation/install/remove idempotent and package/variant aware.
- [ ] Fall back safely to normal startup when root, module state, storage or source identity is unavailable.

### Startup and scan reliability

- [ ] Replace blanket Topway scan suppression with origin-aware policy.
- [ ] Preserve no-scan-storm behaviour for background boot/ACC startup.
- [ ] Permit a user-visible fresh install or confirmed source save to start one bounded initial refresh.
- [ ] Permit optional root-enabled early preparation without implying an automatic full scan.
- [ ] For configured source plus absent/unusable cache, present `Scan now` immediately instead of an indefinite spinner.
- [ ] For prior failure, avoid loops and expose retry, full rescan and error details.
- [ ] Coalesce closely timed permission/source/lifecycle requests into one index request.
- [ ] Preserve committed source generations and prior usable cache when a source is unavailable.

### Diagnostics

- [ ] Record bounded recovery-state, permission, root, source-validation and scan-origin events.
- [ ] Surface early-prestart outcome in app diagnostics/settings.
- [ ] Do not enable continuous Ylog/vendor debug logging.

### Tests and validation

- [ ] Test all recovery states and actions.
- [ ] Test fresh standard and Topway variants with no source/permission.
- [ ] Test already-granted installs such as `pm install -g`.
- [ ] Test SAF, MediaStore and Direct permission decisions.
- [ ] Test readable `/storage/usbdiskN` without root.
- [ ] Test raw `/mnt/media_rw/usbdiskN` with root disabled, granted, denied and timed out.
- [ ] Test configured source with missing/corrupt cache and previously usable library.
- [ ] Test successful empty scans, missing USB, reinsertion and permission revocation.
- [ ] Test exactly one refresh after source save/new grant and active-scan coalescing.
- [ ] Test default boot/ACC path does not trigger a full scan.
- [ ] Test opt-in early-prestart is bounded, idempotent and never starts playback/UI.
- [ ] Run formatting, unit tests, lint, API 29 smoke and head-unit safety checks.
- [ ] Build `standard`, `topwayTwMedia` and `topwayTwMusic` variants.

## Physical TS18 acceptance

Requires device validation:

- [ ] Fresh-install permission prompt and denial recovery.
- [ ] Direct `/storage/usbdiskN` without unnecessary Magisk prompt.
- [ ] Raw `/mnt/media_rw/usbdiskN` with explicit Magisk consent and successful retry.
- [ ] Root denial/timeout ordinary-path fallback.
- [ ] First valid source save begins one scan and populates the library.
- [ ] Missing cache/configured source shows actions rather than permanent spinner.
- [ ] USB removal preserves prior library and reports unavailable source.
- [ ] Reinsert/retry restores source without duplicate scans.
- [ ] Optional early-prestart improves later launch readiness without visible UI or autoplay.
- [ ] Reboot, sleep/wake and ACC cycles avoid scan storms and boot loops.

## Authority and STOP boundaries

- Android runtime permissions are Android framework authority.
- DirectFS/root-assisted listing is app + Magisk authority only.
- Root does not grant platform signing, UID 1000, signature permissions, Topway service authority, DoFun widget authority, MCU/CAN authority or safe firmware writes.
- No stock TW Music deletion or automatic disable.
- No direct `/system` or `/vendor` modification.
- No partition, BOOT/LCD, firmware, MCU or CAN writes.
- No second playback service, player, queue, MediaSession, notification path or audio-focus owner.
- No claim of TS18 acceptance from CI alone.

## References

- Android runtime permissions: <https://developer.android.com/training/permissions/requesting>
- Android 10 shared storage: <https://developer.android.com/about/versions/10/privacy/changes#scoped-storage>
- Storage Access Framework: <https://developer.android.com/guide/topics/providers/document-provider>
- Background execution limits: <https://developer.android.com/about/versions/oreo/background>
- `docs/architecture/FAST_INTERACTION_STARTUP.md`
- `app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt`
- `app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt`
- `app/src/main/java/org/oxycblt/auxio/music/StartupLibraryPolicy.kt`
- `app/src/main/java/org/oxycblt/auxio/home/list/HomeListEmptyState.kt`
