# Auxio-TS TS18 evidence support pack v3

This pack is designed to be extracted at the root of `cbkii/Auxio-TS` before invoking Codex.

## Corrected architecture

The TS18 diagnostics archive was generated externally by a Magisk/service.d collector. It is not the abandoned Auxio in-app diagnostics harness. The app-side diagnostics code may be removed.

## Key support files

- `docs/prompts/CODEX_START_HERE.md`
- `docs/prompts/codex_ts18_auxio_full_scope_prompt.md`
- `docs/evidence/ts18_auxio_v5_0_6/DIAGNOSTICS_ARCHITECTURE_CLARIFICATION.md`
- `docs/evidence/ts18_auxio_v5_0_6/TS18_AuxioMediaDiag_AuxioTS_v5.0.6_report.md`
- `docs/evidence/ts18_auxio_v5_0_6/auxioPerms.md`
- `docs/research/STOCK_T_MUSIC_AND_VLC_MEDIA_CONTROL_EVIDENCE.md`
- `tools/ts18-auxio-media-diag-pack-v3-original/`
- `tools/ts18-auxio-media-diag-pack-v3-recommended/`

## Priority

1. Remove in-app diagnostics.
2. Fix observed invalid-provider crash.
3. Improve external diagnostics collector.
4. Fix overlay restore.
5. Implement fast cached resume.
6. Use stock `t-music` and VLC as positive references; do not pursue Spotify.


## v4 layout correction

This v4 pack intentionally contains no top-level files and no top-level `evidence/` directory. Everything is nested under `docs/` or `tools/` so it can be extracted safely into the Auxio-TS repo.

Previous v1-v3 pack contents should be removed with the cleanup command before extracting this version.
