# First-launch music source and scan recovery

**Evidence status:** **Observed** for repository implementation and automated policy behaviour; exact TS18 runtime outcomes remain **Requires device validation**.

**Confidence:** High for the Android 10/API 29 implementation and bounded policy design; medium for TS18 launch-readiness improvement until physical ACC, reboot, storage and Magisk paths are exercised.

**Porting decision:** Implement only behind Auxio-TS source, root and Topway compatibility adapters. Do not port or infer private TW/DoFun, platform-signing, UID 1000, MCU/CAN, firmware or partition authority.

## Purpose

Restore the previously usable first-launch and missing-library recovery flow for Auxio-TS v6.4.2, then harden it for TS18/Topway Android 10 units.

The current build can present a large indeterminate loader while hiding the actions needed to configure, authorise or rescan a library. System-database permission is only available through a manually tapped card, Direct mode cannot initiate the root consent/probe needed for raw TS18 paths, and the v6.4.2 Home simplification removed the parallel quick-action surface without replacing its recovery role.

This work restores an explicit, state-driven recovery workflow while preserving the playback-first startup architecture, one canonical player/service/session/queue/notification authority, API 29 compatibility, bounded work, source-generation safety and Topway scan-storm protections.

## User-visible goals

**Confidence:** High for implemented UI/policy behaviour; physical TS18 presentation and timing remain **Requires device validation**.

- A fresh install explains what is missing and offers the correct action instead of showing only a spinner.
- Storage/library permissions needed by the selected source mode are requestable from inside Auxio-TS.
- Direct mode tries ordinary `/storage/...` access first and requests Magisk/root only when the chosen path or requested operation genuinely needs it.
- Root-enabled Topway variants may opt into bounded boot-time/early-prestart preparation to improve launch speed.
- Missing, inaccessible, empty, failed or unconfigured libraries expose reliable `Choose source`, `Refresh` and `Full rescan` recovery actions.
- Automatic work remains origin-aware: user-confirmed setup may scan immediately, while boot/ACC paths remain bounded and opt-in.

## Current behaviour

**Evidence status:** **Observed** from the pre-change repository paths listed below. Claims about exact TS18 mount availability, Magisk prompts and ACC timing remain **Requires device validation**.

- `LocationsDialog` registers a storage permission launcher, but invokes it only from the permission card.
- `HomeFragment` retains a storage permission launcher whose result refreshes the library, but the current Home flow does not launch it.
- DirectFS first attempts ordinary `File.listFiles()` and only falls back to `RootGate`; this ordinary-first behaviour must be preserved.
- Root-assisted access is separately disabled by default and the source picker only receives a root gate when root is already `Available`, creating a first-run dead end for raw `/mnt/media_rw/...` paths.
- `StartupLibraryStatus` already distinguishes `Unknown`, `Usable`, `Empty`, `NeedsMusicSource`, `CacheUnavailable` and `SourceUnavailable`, but the empty-library UI does not consume those distinctions.
- PR #190 removed `home_head_unit_shortcuts`, `home_quick_picks` and `home_metadata_chips`; the existing spinner/empty-state implementation remained without a replacement recovery surface.
- Topway startup currently suppresses automatic scans broadly. This prevents scan storms but also leaves some configured-source/no-cache launches without a direct recovery path.

## Implementation checklist

**Implementation status:** **Observed complete** in this branch. Automated verification is recorded in the PR; exact-device outcomes are separated below.

### Recovery state model

- [x] Add a dedicated library/source recovery UI policy derived from startup library status, indexing state, source configuration, Android permission, Direct source accessibility, root state, prior library state and last-scan failure.
- [x] Keep startup capability ordering separate from recoverable source/library state.
- [x] Distinguish bounded cache wait, permission required, source required, source unavailable, cache unavailable, indexing, confirmed empty, failed scan and usable library.
- [x] Ensure a stale readiness spinner cannot hide recovery indefinitely.

### Home first-launch and missing-library UI

- [x] Replace the spinner-only empty presentation with a compact state-driven recovery panel.
- [x] Restore contextual `Choose source`, `Refresh`, confirmed `Full rescan`, `Grant access` and root-enable actions.
- [x] Keep useful actions visible while indexing; disable only actions that would be unsafe or duplicate active work.
- [x] Show meaningful phases/messages rather than a generic indefinite loader.
- [x] Do not restore unrelated metadata/decade/startup-song chip clutter merely to fix recovery.
- [x] Preserve TS18 touch target sizing and runtime insets/bounds.

### In-app Android permission flow

- [x] Request the permission appropriate to the selected source mode from within the app.
- [x] System Database mode on Android 10 requests `READ_EXTERNAL_STORAGE` when missing.
- [x] Direct mode requests Android storage access when required for the selected ordinary path.
- [x] File Picker/SAF mode does not request broad storage permission unnecessarily.
- [x] Explain why access is needed before launching the system prompt.
- [x] Revalidate source state after grant and trigger exactly one refresh after valid source confirmation.
- [x] Detect permanent denial and provide an `Open app settings` recovery action.
- [x] Remove or correctly use the orphaned Home permission launcher.
- [x] Keep visualiser `RECORD_AUDIO` permission independent.

### DirectFS and root consent

- [x] Preserve ordinary app-readable DirectFS as the first choice.
- [x] Detect when a selected/manual raw path requires root rather than prompting merely because Direct mode was selected.
- [x] From the source dialog, provide explicit informed consent for root-assisted access.
- [x] On confirmation enable the authoritative root-access preference, run the bounded `su -c id` probe off-main-thread, show the resulting state, rerun source discovery and revalidate the path.
- [x] Retain ordinary `/storage/...` fallback paths when root is unavailable.
- [x] Coordinate `RootAccessPolicy` and `auxio_use_root_fs` so the UI reflects one user decision.
- [x] Keep the standard variant unable to execute root-assisted Topway behaviour.

### Optional root-enabled boot-time / early-prestart

- [x] Add an explicit Topway-only opt-in for bounded early preparation when root access is enabled and verified.
- [x] Keep it disabled by default and explain boot/ACC implications.
- [x] Allow the app to enable/disable the supported prestart mechanism from its own settings/recovery UI.
- [x] Use an app-owned narrow readiness entry point; shell must not edit app-private databases or caches.
- [x] Early preparation may verify configured direct sources/mount aliases, warm bounded startup projections/cache access and request only the existing canonical service/start path.
- [x] Early preparation does not start playback, launch `MainActivity`, run an unconditional full scan, create duplicate playback authority, block `post-fs-data`, or write protected system/vendor/firmware data.
- [x] Use late-start/service-time behaviour with timeout, duplicate-run guard and rollback.
- [x] Keep any Magisk-module integration optional; this PR does not install or mutate a module.
- [x] Fall back safely to normal startup when root, module state, storage or source identity is unavailable.

### Startup and scan reliability

- [x] Replace blanket Topway scan suppression with origin-aware policy.
- [x] Preserve no-scan-storm behaviour for background boot/ACC startup.
- [x] Permit a user-visible fresh install or confirmed source save to start one bounded initial refresh.
- [x] Permit optional root-enabled early preparation without implying an automatic full scan.
- [x] For configured source plus absent/unusable cache, present `Scan now` immediately instead of an indefinite spinner.
- [x] For prior failure, avoid loops and expose retry, full rescan and error details.
- [x] Coalesce closely timed permission/source/lifecycle requests into one index request.
- [x] Preserve committed source generations and prior usable cache when a source is unavailable.

### Diagnostics

- [x] Record bounded recovery-state, permission, root, source-validation and scan-origin events.
- [x] Surface early-prestart outcome in app diagnostics/settings.
- [x] Do not enable continuous Ylog/vendor debug logging.

### Tests and validation

- [x] Cover all recovery policy states and actions, including source unavailable, confirmed empty and inconsistent usable/empty states.
- [x] Cover standard and Topway source/permission policy decisions without requiring physical hardware.
- [x] Cover already-granted and missing-permission decision paths.
- [x] Cover SAF, MediaStore and Direct permission decisions.
- [x] Cover ordinary `/storage/usbdiskN` and raw `/mnt/media_rw/usbdiskN` classification without claiming mount availability.
- [x] Cover root disabled, granted, denied and timed-out state handling through bounded root policy/process tests.
- [x] Cover missing/corrupt cache, previously usable library and successful empty-state recovery policy.
- [x] Cover source-save/permission refresh authority and active-scan coalescing.
- [x] Cover default background/boot scan suppression and user-visible scan authority.
- [x] Cover bounded early-prestart policy and no-playback routing in code review and service tests.
- [ ] Run final formatting, unit tests, lint, API 29 smoke and head-unit safety checks on the final stacked head.
- [ ] Build the `standard`, `topwayTwMedia` and `topwayTwMusic` variants on the final stacked head.

## Physical TS18 acceptance

**Confidence:** Unverified.

**Porting decision:** **Requires device validation** on the exact TS18 build, panel, storage paths, Magisk state and ACC lifecycle. These are acceptance observations, not CI-completable implementation tasks.

- Fresh-install permission prompt and denial recovery.
- Direct `/storage/usbdiskN` without unnecessary Magisk prompt.
- Raw `/mnt/media_rw/usbdiskN` with explicit Magisk consent and successful retry.
- Root denial/timeout ordinary-path fallback.
- First valid source save begins one scan and populates the library.
- Missing cache/configured source shows actions rather than permanent spinner.
- USB removal preserves prior library and reports unavailable source.
- Reinsert/retry restores source without duplicate scans.
- Optional early-prestart improves later launch readiness without visible UI or autoplay.
- Reboot, sleep/wake and ACC cycles avoid scan storms and boot loops.

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