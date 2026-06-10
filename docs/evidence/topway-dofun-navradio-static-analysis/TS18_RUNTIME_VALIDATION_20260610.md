# TS18 runtime validation addendum — 2026-06-10

This addendum incorporates the archive `ts18_dofun_runtime_validation_20260610_125608.tar.gz` into the existing static evidence pack.

It should be read as **runtime observation under TS18/TermOnePlus constraints**, not as a complete privileged trace. TermOnePlus ran as `uid=10177(u0_a177)` with SELinux context `u:r:untrusted_app:s0:c177,c256,c512,c768`; several synthetic `am`/`cmd` probes failed before delivery because the commands defaulted to Android's current-user sentinel (`-2`) and the caller lacked cross-user permissions. Use the updated script under `scripts/evidence/collect-ts18-dofun-runtime-validation-v2.sh` for any further run.

## New high-value observations

### 1. DoFun has fixed Music and Radio widgets only

Manual observation confirmed the launcher home has the fixed DoFun Music and Radio widgets and **no custom widget area**.

Evidence: `excerpts/runtime/ts18_manual_observations_20260610.md`.

Implementation impact: Auxio-TS cannot rely on a user-added standard AppWidget as the primary integration path. It must satisfy the fixed DoFun Music widget/card contract.

### 2. With both apps installed, DoFun Music widget launched stock `com.tw.music`

Manual observation after tapping the DoFun Music widget: `stock`.

Window dump corroborates `com.tw.music/.MusicActivity` as the top app for that phase, while `com.tw.media/com.tw.music.MusicActivity` also existed in the task/window list.

Evidence: `excerpts/runtime/ts18_window_music_widget_opens_stock_20260610.md`.

Implementation impact: DoFun's static config entry for `com.tw.media/com.tw.music.MusicActivity` is real, but current runtime behaviour does **not** prefer `com.tw.media` while stock `com.tw.music` remains installed/enabled. This strongly suggests one of:

- DoFun chooses the system/stock package when both `com.tw.music` and `com.tw.media` are present;
- DoFun caches the stock target until launcher/theme data is reset or refreshed;
- DoFun only uses the `com.tw.media` entry as a fallback when stock `com.tw.music` is absent/disabled;
- the static `apps_match_config.json` entry is not the only runtime selector.

Do not assume the `com.tw.media` variant can replace stock Music widget routing without either runtime validation or an additional fallback.

### 3. Auxio-TS is visible to Android media-session routing on TS18

`dumpsys media_session` showed an active Auxio-TS session:

- package `com.tw.media`
- `mediaButtonReceiver=... com.tw.media broadcastIntent`
- `active=true`
- selected as `Media button session is com.tw.media/com.tw.media`
- valid metadata and queue state

Manual observation still says DoFun Music widget controls operated stock music only.

Evidence: `excerpts/runtime/ts18_auxio_media_session_visible_but_not_widget_target_20260610.md`.

Implementation impact: Generic Android media-session visibility is **not sufficient** for the fixed DoFun Music widget. This further prioritises the stock Topway/TW Music action/broadcast/widget contract over a MediaSession-only or Media3-only fix.

### 4. Current Auxio-TS build contains Topway contract strings

The installed `com.tw.media` APK copied from the TS18 contains relevant strings/classes/actions:

- `Lcom/tw/music/MusicService;`
- `Lcom/tw/music/view/MusicWidgetProvider;`
- `com.tw.music.action.cmd`
- `com.tw.music.action.prev`
- `com.tw.music.action.pp`
- `com.tw.music.action.next`
- `com.tw.music.info`
- `com.tw.launcher.music_progress_duration`
- `com.android.launcher.widget_music_progress`
- `musicTitle`, `musicaArtist`, `musicAlbum`, `musicPath`
- `msg_music_progress`, `msg_music_duration`

Evidence: `excerpts/auxio-ts-installed/ts18_com_tw_media_contract_strings_20260610.md`.

Implementation impact: The build contains relevant paths, but static strings alone do **not** prove that:

- manifest entries are exported and correctly named;
- the wrapper service receives launcher service intents;
- update/progress broadcasts are emitted at the right times;
- DoFun is targeting `com.tw.media` rather than `com.tw.music`.

Agent work should verify implementation semantics in source, not merely add strings.

### 5. Auxio-TS overlay/floating control window is present on TS18

Window dumps showed a `com.tw.media` window with `appop=SYSTEM_ALERT_WINDOW`.

Evidence: `excerpts/runtime/ts18_overlay_window_present_20260610.md`.

Implementation impact: Overlay controls are a viable fallback on the target device. They should remain fallback/secondary; they do not solve the fixed DoFun Music widget routing problem.

### 6. NavRadio+ TS18 runtime evidence is weaker than Pixel/static evidence

Manual observation said radio controls worked, but the window dump after the NavRadio comparator phase showed stock `com.tw.radio/.RadioActivity`, and `dumpsys media_session` still only showed Auxio's `com.tw.media` session. No live `com.navimods.radio` media session was captured in this run.

Evidence: `excerpts/runtime/ts18_navradio_radio_widget_ambiguous_20260610.md`.

Implementation impact: Keep NavRadio+ as a comparator for static architecture — real Media3 service, simple transport controls, overlay/file-picker patterns — but do not claim TS18 DoFun widget compatibility is proven to be caused by Media3.

### 7. Synthetic `am broadcast` / `am startservice` probes were invalid in this run

All synthetic probes failed with permission denial before delivery:

`Permission Denial: ... asks to run as user -2 ... requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS`

Evidence: `excerpts/runtime/ts18_am_probe_user_minus2_failures_20260610.md`.

Implementation impact: These failures are not evidence that Auxio-TS ignored the commands. Future scripts must pass `--user 0` explicitly and use `cmd package query-activities/services/receivers` rather than unavailable `query-intent-*` commands.

## Updated confidence table

| Claim | Previous confidence | Updated confidence | Reason |
|---|---:|---:|---|
| DoFun fixed Music widget exists and no custom widget area is available | Medium | High | Direct manual observation. |
| DoFun Music widget opens stock `com.tw.music` when stock and Auxio `com.tw.media` are both installed | Medium | High | Manual observation plus window dump. |
| Auxio-TS is Android media-session visible on TS18 | Medium | High | `dumpsys media_session` captured active `com.tw.media` session. |
| Android media-session visibility alone fixes DoFun Music widget | Low | Lower | Auxio visible but widget still used stock. |
| Stock TW Music private widget/broadcast contract is primary | High | Higher | Runtime behaviour aligns with static stock contract priority. |
| NavRadio+ proves Media3 is the TS18 widget solution | Medium-low | Low | No live NavRadio media session captured on TS18 in this run. |
| Overlay fallback is viable on TS18 | Medium | High | `SYSTEM_ALERT_WINDOW` appop/window observed for `com.tw.media`. |
| Synthetic broadcast/service probes reached Auxio/DoFun | Unknown | False for this run | They failed at ActivityManager user handling. |

## Updated implementation implications

1. Prioritise the stock TW Music contract over generic MediaSession/Media3.
2. Preserve and harden `com.tw.media/com.tw.music.MusicActivity`, but recognise DoFun may prefer stock `com.tw.music` while it remains enabled.
3. Implement robust command handling and outgoing broadcasts even if the Music widget still targets stock; these are necessary for any successful fallback/disable/cache-refresh scenario.
4. Add explicit source validation that `com.tw.music.MusicService` and `com.tw.music.view.MusicWidgetProvider` are real exported components in the Topway variant manifest.
5. Keep simple external controls: previous / play-pause / next.
6. Keep overlay controls as a validated fallback for the target device.
7. Treat any stock-disabling path as separate runtime validation. Do not assume it is possible from TermOnePlus; try only reversible per-user methods first.
