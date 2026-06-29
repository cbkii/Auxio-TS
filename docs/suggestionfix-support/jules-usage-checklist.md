# Jules usage checklist

Use this research pack as background only. The repository remains authoritative.

Before editing:

- [ ] Confirm current branch/commit.
- [ ] Read `AGENTS.md`, `docs/README.md`, and `docs/DEVELOPMENT.md`.
- [ ] Inspect PR #120 diff and preserve its performance mission.
- [ ] Mark each suggestion implemented, duplicate, skipped, or deferred.

While editing:

- [ ] Keep TS18 startup offline-first and cached.
- [ ] Do not add automatic startup scans for Topway builds.
- [ ] Do not force SAF picker for source selection.
- [ ] Do not broaden MediaStore/FS scans.
- [ ] Do not blindly force foreground services.
- [ ] Do not start playback from Bluetooth connect unless existing settings and Android rules support it.
- [ ] Do not expose root arbitrary command execution.
- [ ] Do not reintroduce removed album art modes.
- [ ] Do not change package identities or assume platform signing/shared UID.
- [ ] Do not add in-app TS18 diagnostic frameworks.

Before final response:

- [ ] List implemented suggestions by number.
- [ ] List skipped/deferred suggestions with reasons.
- [ ] List duplicate suggestions consolidated.
- [ ] Report tests run and exact results.
- [ ] Report tests not run and blocker labels.
- [ ] State that TS18 runtime validation was not performed unless it actually was.
