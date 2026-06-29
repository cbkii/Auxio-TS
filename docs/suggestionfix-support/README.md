# Auxio-TS Jules External Research Pack

Purpose: support Jules while resolving the Auxio-TS suggestion backlog on PR #120 without forcing Jules to leave the repository for basic Android/Media3/security research.

This package is intentionally advisory. It does not replace repo authority. Jules must still inspect current code, `AGENTS.md`, `docs/README.md`, `docs/DEVELOPMENT.md`, PR #120, and the exact branch state before editing.

Primary engineering constraints carried into this research:

- Preserve PR #120's offline-first cached startup and TS18 manual-only scan policy.
- Preserve manual/direct source path selection and SAF fallback handling.
- Keep MediaSession, service startup, Bluetooth, media buttons, and foreground-service work compatible with Android background-execution rules.
- Do not add in-app TS18 diagnostics, private vendor binders, platform-signing assumptions, shared UID assumptions, copied smali, or broad root command surfaces.
- Prefer small, reversible, test-backed changes.

Files in this pack:

- `source-index.md` — primary external references and why each matters.
- `suggestion-map.md` — maps suggestions to external research needs.
- `android-component-security.md` — CoverProvider/exported provider and root command hardening notes.
- `foreground-bluetooth-media-buttons.md` — foreground services, boot, Bluetooth connect, media buttons, and overlay startup notes.
- `media3-playback-contracts.md` — MediaSession results, ShuffleOrder, and AudioProcessor notes.
- `storage-saf-mediastore-overlay.md` — SAF, manual paths, MediaStore filtering, overlay permission notes.
- `ui-accessibility-recyclerview.md` — playback controls accessibility and QueueAdapter update notes.
- `testing-strategy.md` — test strategy for the suggestion set.
- `jules-usage-checklist.md` — practical checklist for applying this research safely.
- `sources.json` — structured source list for agents.

Evidence labels:

- **Observed in external docs:** statement is directly supported by linked documentation.
- **Inferred for Auxio-TS:** likely application to this repo; Jules must verify current code.
- **Requires repo validation:** cannot be decided from external docs alone.
- **Requires TS18 validation:** cannot be proven without the physical head unit.
