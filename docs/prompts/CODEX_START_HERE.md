# CODEX START HERE — Auxio-TS TS18 media integration hardening

Read `docs/prompts/codex_ts18_auxio_full_scope_prompt.md` and implement it.

Important corrections from the user:

1. `TS18_AuxioMediaDiag.zip` is from an external Magisk/service.d collector, not Auxio in-app diagnostics. Remove abandoned in-app diagnostics; improve the external collector.
2. Do not investigate Spotify further; it proved not to integrate. Use stock `cbkii/t-music` and public VLC as positive references.
3. Use `GH_TOKEN`/`GITHUB_TOKEN` to inspect `cbkii/t-music` and `videolan/vlc-android`.
4. Fix the observed `LocationObserver` invalid-provider crash first.
5. Then fix overlay reliability, fast resume, source handling, indexer/notification churn, skip-non-music defaults, permissions, and Magisk module decision.

Support files in this pack:

- `docs/evidence/ts18_auxio_v5_0_6/`
- `docs/research/STOCK_T_MUSIC_AND_VLC_MEDIA_CONTROL_EVIDENCE.md`
- `tools/ts18-auxio-media-diag-pack-v3-original/`
- `tools/ts18-auxio-media-diag-pack-v3-recommended/`
- `evidence/ts18_auxio_v5_0_6/`


## v4 layout note

This support pack is intentionally rooted only under `docs/` and `tools/`. There is no top-level `evidence/` directory and no top-level prompt file.
