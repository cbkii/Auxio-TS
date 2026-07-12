# Settings architecture

Auxio-TS settings are organized around user tasks rather than implementation areas.

## Root hierarchy

Standard builds show only general and support destinations:

- Appearance & layout
- Library & sources
- Playback & audio
- About Auxio-TS

Topway-compatible builds add the head-unit destinations:

- Car & head unit
- Diagnostics & recovery

The root screen contains navigation rows only. Immediate library actions such as refresh and full rescan live under **Library & sources**.

## Ownership

- **Appearance & layout** owns theme, colour, rounded-interface, library tab, square artwork, and Topway-only Now Playing visualizer presentation.
- **Library & sources** owns source selection, source status, refresh/full rescan, scanning policy, metadata interpretation, artwork loading, and Topway-only root-assisted source access.
- **Playback & audio** owns launch playback, Now Playing launch routing, queue construction, playback-bar action, ReplayGain, and task-close playback behavior.
- **Car & head unit** owns Topway-visible driving UI, floating controls, boot behavior, launcher integration entry points, and advanced launcher protocol controls.
- **Diagnostics & recovery** owns technical status, report generation/export, and guarded stock-package recovery.

## Preference-key policy

Persisted preference keys are preserved when rows move or labels change. User-visible renames such as **Rounded interface**, **Full rescan**, **Preserve play/pause state**, and **Stop playback when Auxio-TS is closed** keep the existing keys and semantics.

The Topway boot mode list is a UI adapter over the existing `auxio_autostart_on_boot` and `auxio_autostart_floating_only` booleans:

- `autostart=false` means **Do nothing**.
- `autostart=true` and `floatingOnly=false` means **Open Auxio-TS**.
- `autostart=true` and `floatingOnly=true` means **Restore floating controls only**.

No storage migration is required for that adapter, and runtime boot readers continue to consume the existing booleans.

## Progressive disclosure

The Music sources dialog remains responsible for source-mode-specific controls such as file picker, system database, direct filesystem paths, folder filters, hidden files, non-music filtering, multithreaded exploration, USB auto-detection, and manual path fallback. The main Library page exposes only the entry point and concise status/action rows.

Advanced Topway protocol choices remain in the Topway source set and are grouped away from routine driving and floating-control settings. Private/native integration remains not for production by default; it requires the formal gap-and-promotion process.
