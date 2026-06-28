# TS18 Runtime Hardening Notes

Status: PR#119 host-side hardening pass. Some behaviours remain partial or require exact TS18 validation.

## Implemented in PR#119

- Raw fast-resume content URI/direct-path fallback improvements, including descriptor fallback for inconclusive content URI metadata.
- Raw reconciliation play/pause preservation through playback manager.
- Shuffle valid-index guard and current state preservation.
- Widget text-first update and stale artwork callback guard.
- Scan filtering for non-audio extensions and Ogg MIME handling (arbitrary 888 MiB exclusion removed).
- Root gate user-disable ordering.
- `/storage/emulated/0/Download` is accepted by direct/raw path policy because `/storage/emulated/0/` is an allowed root.

## Partially implemented / follow-up

- Floating Controls restoration via existing lifecycle/broadcast hooks, but no central watchdog/supervisor true persistence yet.
- Source repair remains default/direct-root oriented rather than configured-source aware.
- Root-assisted DirectFS exists, but true root-first listing is not implemented.
- `/storage/emulated/0/Download` is not yet a single shared default fallback across every source-selection feature.

## Not implemented in PR#119

- Autostart Floating Controls only mode.
- Full SAF repair-state semantics.
- Full configured-source-aware repair.
- Exact-device TS18 validation.

## Requires device validation

- DoFun launcher widget cold start.
- Floating overlay persistence after ACC/reboot/process death.
- USB mount/unmount timing.
- `/storage/emulated/0/Download`, `/storage/usbdisk0`, `/storage/usbdisk1` real scans.
- Root granted/denied behaviour on TS18.
- Raw fast resume against TS18 storage providers.