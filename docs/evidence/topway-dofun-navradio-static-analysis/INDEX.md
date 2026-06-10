# Index

## Primary docs

- `README.md` — what this pack is, source roles, limitations.
- `SUMMARY.md` — concise findings.
- `CONTRACT_MATRIX.md` — side-by-side DoFun / TW Music / NavRadio / Auxio implications.
- `DOFUN_VARIETY_FINDINGS.md` — DoFun config and launcher-target findings.
- `TW_MUSIC_STOCK_CONTRACT.md` — stock Topway music widget/action/broadcast contract.
- `NAVRADIO_COMPARATOR_FINDINGS.md` — Media3/session/widget/overlay comparator evidence.
- `AUXIO_TS_RECOMMENDED_CHANGES.md` — engineering tasks translated from evidence.
- `VALIDATION_PLAN_TS18.md` — no-root/no-ADB validation approach.
- `AGENT_IMPLEMENTATION_PROMPT.md` — prompt for Codex/Jules/Copilot.

## Excerpts

### DoFun/config

- `excerpts/configs/dofun_apps_match_config_hotseat_music_primary.md`
- `excerpts/configs/dofun_apps_match_config_hotseat_music_secondary.md`
- `excerpts/configs/dofun_apps_config_music_entries.md`
- `excerpts/configs/dofun_link_icon_music_names.md`
- `excerpts/manifests/dofun_manifest_launcher_permissions_excerpt.md`

Use these to justify exact `com.tw.media/com.tw.music.MusicActivity` launch identity.

### TW Music stock contract

- `excerpts/manifests/twmusic_manifest_core_components.md`
- `excerpts/widgets/twmusic_widget_update_flow.md`
- `excerpts/widgets/twmusic_remoteviews_controls.md`
- `excerpts/widgets/twmusic_appwidget_info_default.md`
- `excerpts/widgets/twmusic_appwidget_info_sw768dp.md`
- `excerpts/widgets/twmusic_music_widget_layout.md`
- `excerpts/broadcasts/twmusic_service_command_contract.md`
- `excerpts/broadcasts/twmusic_dynamic_receiver_cmd_update.md`
- `excerpts/broadcasts/twmusic_launcher_seek_receiver.md`
- `excerpts/broadcasts/twmusic_activity_starts_service_registers_seek.md`
- `excerpts/broadcasts/twmusic_metadata_broadcast_extras.md`
- `excerpts/broadcasts/twmusic_progress_broadcast_tick.md`

Use these to implement the Topway/TW Music compatibility layer.

### NavRadio comparator

- `excerpts/manifests/navradio_manifest_media3_widget_overlay.md`
- `excerpts/media-session/navradio_radioservice_class_fields.md`
- `excerpts/media-session/navradio_media3_session_creation.md`
- `excerpts/media-session/navradio_on_get_session.md`
- `excerpts/widgets/navradio_widget_receiver_updates.md`
- `excerpts/widgets/navradio_widget_pendingintents.md`
- `excerpts/navradio/navradio_float_widget_overlay.md`
- `excerpts/navradio/navradio_float_widget_controls.md`
- `excerpts/navradio/navradio_get_content_picker_excerpt.md`
- `excerpts/navradio/navradio_changelog_media_widget_lines.md`

Use these as design comparator evidence only.

## Helper scripts

- `scripts/evidence/grep-topway-contract.sh` — grep for relevant contract strings.
- `scripts/evidence/compare-evidence-contract.sh` — quick static presence check for key exact strings.

Both scripts are read-only and safe to run from the repo root.

## Runtime validation addendum files — 2026-06-10

- `TS18_RUNTIME_VALIDATION_20260610.md` — curated analysis of the TS18 runtime validation archive and its implementation impact.
- `excerpts/runtime/ts18_manual_observations_20260610.md` — manual DoFun Music/Radio widget observations from the target device.
- `excerpts/runtime/ts18_window_music_widget_opens_stock_20260610.md` — window evidence that fixed DoFun Music widget launched stock `com.tw.music` while Auxio `com.tw.media` was installed.
- `excerpts/runtime/ts18_auxio_media_session_visible_but_not_widget_target_20260610.md` — media-session evidence showing Auxio is visible to Android but not selected by fixed DoFun Music widget behaviour.
- `excerpts/runtime/ts18_overlay_window_present_20260610.md` — overlay fallback evidence for `com.tw.media` on TS18.
- `excerpts/runtime/ts18_am_probe_user_minus2_failures_20260610.md` — explains why v1 synthetic probes were invalid and why v2 uses `--user 0`.
- `excerpts/runtime/ts18_installed_package_state_20260610.md` — installed package and stock/auxio coexistence state.
- `excerpts/runtime/ts18_navradio_radio_widget_ambiguous_20260610.md` — cautionary NavRadio/Radio widget runtime evidence.
- `excerpts/auxio-ts-installed/ts18_com_tw_media_contract_strings_20260610.md` — strings from the installed TS18 Auxio APK showing Topway contract terms are present in the build.

Additional helper:

- `scripts/evidence/collect-ts18-dofun-runtime-validation-v2.sh` — improved target-device collector; safe/read-only by default and fixes the v1 `--user -2` problem.


## Final addendum files

- `TS18_RUNTIME_VALIDATION_20260610_FINAL.md` — final runtime interpretation, including the later v2 run and its wrong-user limitation.
- `excerpts/runtime-v2/ts18_manual_observations_v2_20260610.md` — manual fixed-widget validation.
- `excerpts/runtime-v2/ts18_media_session_runtime_v2_selected_phases.md` — passive media-session evidence showing Auxio remains Android-media-session-visible.
- `excerpts/runtime-v2/ts18_window_runtime_v2_selected_phases.md` — selected window/task evidence.
- `excerpts/runtime-v2/ts18_v2_userid_probe_invalid.md` — why v2 synthetic probes must not be treated as negative Auxio evidence.
- `excerpts/runtime-v2/ts18_v2_stock_disable_attempts_invalid_user.md` — why stock-disable conclusions remain unproven.
- `scripts/evidence/collect-ts18-dofun-runtime-validation-v3.sh` — corrected TS18 collector using `ANDROID_USER_ID=0` by default and avoiding `USER_ID` env leakage.
