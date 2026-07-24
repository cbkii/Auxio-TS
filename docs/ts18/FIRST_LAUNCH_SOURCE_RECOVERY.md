# First-launch and missing-library source recovery

**Confidence:** High for repository behaviour verified by unit/build checks; physical TS18 behaviour remains unverified.

**Porting decision:** Implement the generic Android 10/API 29 recovery path now. Defer root-only raw-mount access and boot-time early preparation until an evidence-led, separately approved design can provide app-readable descriptors end to end and pass physical TS18 validation.

## Purpose

Restore an explicit recovery path when Auxio-TS has no usable music library. The user must see a concise state and a safe action instead of a permanent indeterminate loader.

## Implemented behaviour

- Distinguish cache wait, permission required, source required, source unavailable, indexing, confirmed empty, cache unavailable, and failed scan states.
- Keep `Choose source` available during indexing.
- Offer `Grant access`, `Refresh`, and confirmed `Full rescan` only when applicable.
- Request Android storage permission for MediaStore and ordinary DirectFS paths that require it; do not request it for SAF unnecessarily.
- Revalidate permission after the system Settings round-trip and continue exactly one pending recovery action.
- Treat the activity-start scan token as process-local and one-shot; exported service callers and MediaBrowser binds remain background origins.
- Require complete current source authority before any automatic scan. Missing/revoked SAF grants, partial source sets, unreadable paths, and non-directory sources suppress automatic scanning and preserve prior library generations.
- Perform source-authority file/provider checks on the existing IO worker, not the service main thread.

## TS18 storage boundary

**Observed:** TS18 USB volumes are exposed to normal apps as `/storage/usbdiskN`; raw mounts are under `/mnt/media_rw/usbdiskN`.

**Porting decision:** Auxio-TS accepts only app-readable source paths. A raw `/mnt/media_rw/...` path is rejected with guidance to select its `/storage/usbdiskN` alias. A bounded root probe cannot make later app-UID scanning and playback descriptors readable, so this PR does not claim root-assisted raw-path support.

## Scan authority

- Standard builds retain their established automatic startup behaviour when current source authority is valid.
- Topway-compatible builds allow the first automatic recovery scan only for a trusted, user-visible activity start.
- Boot, ACC, exported-service, and MediaBrowser bind paths cannot mint or consume that authority.
- User actions may request one refresh/rescan; active requests are coalesced by the existing indexing worker.

## Deferred scope

- Root-only DirectFS traversal and playback.
- App-managed Magisk module lifecycle.
- Boot-time/early-prestart service work.
- Continuous or durable new diagnostic capture. Existing bounded journal events remain sufficient for this PR; deeper captures stay under external `tools/` diagnostics.

## Verification

Repository checks must cover formatting, unit tests, Android lint, standard and Topway-compatible builds, API 29 runtime smoke tests, and head-unit safety scripts.

## Physical TS18 acceptance — requires device validation

- Fresh-install permission grant, denial, permanent-denial, and Settings-return recovery.
- `/storage/usbdiskN` selection without an unnecessary Magisk prompt.
- Raw `/mnt/media_rw/usbdiskN` rejection with a clear app-readable alias path.
- One scan after a valid source save or permission grant; no duplicate scan after recreation.
- Missing cache/source actions, USB removal/reinsert, permission revocation, reboot, sleep/wake, and ACC cycles without scan storms.

No CI result is evidence of DoFun fixed-widget control, platform signing/UID 1000, MCU/CAN authority, or physical TS18 acceptance.
