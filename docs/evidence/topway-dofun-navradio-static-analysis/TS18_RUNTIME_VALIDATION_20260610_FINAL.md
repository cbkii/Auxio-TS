# TS18 runtime validation final addendum — 2026-06-10

This addendum incorporates both the earlier TS18 runtime archive and the later `ts18_dofun_runtime_validation_v2_20260610_162755` run.

## What changed with the v2 run

### High-confidence observations

1. The active launcher setup is exactly the target scenario: DoFun launcher, fixed Music widget, fixed Radio widget, and **no custom widget area**. Source: `excerpts/runtime-v2/ts18_manual_observations_v2_20260610.md`.
2. With stock `com.tw.music`, Auxio-TS `com.tw.media`, DoFun Variety, and NavRadio+ all installed, tapping the fixed DoFun **Music** widget opened **stock `com.tw.music`**, not Auxio-TS. Source: manual observation plus selected window excerpts.
3. The fixed DoFun Music widget controlled stock TW Music and updated stock metadata/progress in the manual test. Source: manual observation.
4. The fixed DoFun Music widget did **not** control Auxio-TS when Auxio was already open and playing. Source: manual observation.
5. Auxio-TS is still Android media-session visible on the TS18: `com.tw.media` appears as an active session with metadata and media-button receiver in `dumpsys media_session`. Source: `excerpts/runtime-v2/ts18_media_session_runtime_v2_selected_phases.md`.
6. Auxio-TS overlay/floating controls are visible as a `com.tw.media` window with `SYSTEM_ALERT_WINDOW`. Source: selected window excerpts.
7. The Radio widget manual test reported NavRadio+ control, but runtime window/media evidence remains mixed because stock `com.tw.radio/.RadioActivity` is also present. Treat this as weaker than the TW Music evidence.

### Important limitation discovered

The v2 collector inherited `USER_ID=10177` from TermOnePlus. That value is the app UID, not Android user `0`. As a result, synthetic probes using `--user 10177` are invalid for implementation conclusions:

- `cmd package query-*` probes mostly failed with cross-user permission errors.
- `am startservice` / `am broadcast` probes did not reliably deliver to Auxio or DoFun.
- stock-disable/uninstall probes were not valid user-0 tests.

Source: `excerpts/runtime-v2/ts18_v2_userid_probe_invalid.md` and `excerpts/runtime-v2/ts18_v2_stock_disable_attempts_invalid_user.md`.

## Updated confidence table

| Claim | Final confidence | Basis |
|---|---:|---|
| DoFun has fixed Music and Radio widgets only; no custom widget area | High | Manual target-device observation. |
| Fixed DoFun Music widget prefers/opens stock `com.tw.music` while stock and Auxio `com.tw.media` coexist | High | Manual observation; window state includes stock music activity in the relevant run. |
| Fixed DoFun Music widget controls stock TW Music | High | Manual observation. |
| Fixed DoFun Music widget controls Auxio-TS while stock `com.tw.music` is enabled | Low | Manual observation says no. |
| Auxio-TS is visible to Android media sessions on TS18 | High | `dumpsys media_session` active `com.tw.media` session. |
| Generic MediaSessionCompat visibility is sufficient for fixed DoFun Music widget compatibility | Very low | Auxio visible but widget still goes to stock. |
| Stock TW Music private contract is the primary implementation target | Very high | Static TW Music contract plus runtime stock-widget behaviour. |
| NavRadio+ Media3 proves the fixed DoFun Music widget solution | Low | Comparator useful, but fixed Music widget is stock-TWMusic-oriented. |
| Auxio overlay fallback is viable on TS18 | High | `SYSTEM_ALERT_WINDOW` window present. |
| v2 synthetic shell probes proved Auxio ignored commands | False | Wrong Android user id; probes invalid. |
| stock `com.tw.music` cannot be disabled on user 0 | Unproven | v2 disable attempts used wrong user id. |

## Implementation impact

The final Agent task should not chase a generic media-session-only fix. Auxio already presents an Android media session on TS18. The practical implementation work is:

1. Make the `com.tw.media` Topway build satisfy the stock TW Music wrapper/component/action/broadcast contract exactly.
2. Keep `com.tw.media/com.tw.music.MusicActivity` correct, exported, cold-start safe, and launcher-friendly, but recognise this alone does not override stock while `com.tw.music` remains enabled.
3. Implement `com.tw.music.MusicService` command handling as real behaviour, not just string presence.
4. Emit stock-compatible broadcasts (`com.tw.music.info`, `com.tw.launcher.music_progress_duration`) from actual playback state and safe placeholder state.
5. Keep `com.tw.music.view.MusicWidgetProvider` valid and able to update even if DoFun is not a normal AppWidget host.
6. Preserve overlay/floating controls as the validated fallback when DoFun continues selecting stock.
7. Treat stock-disable/per-user uninstall as a validation path, not a source-code assumption.

## Remaining runtime questions

These remain genuinely unresolved and need validation after implementation or with corrected script v3:

- If stock `com.tw.music` is disabled for Android user `0`, will DoFun fall through to `com.tw.media/com.tw.music.MusicActivity`?
- Will DoFun listen to Auxio's `com.tw.music.info` and `com.tw.launcher.music_progress_duration` broadcasts once its fixed Music widget/card is pointed at Auxio or stock is absent?
- Does DoFun send `com.android.launcher.widget_music_progress` in this theme build?
- Can `am startservice --user 0 -n com.tw.media/com.tw.music.MusicService -a com.tw.music.action.pp` reach the Auxio wrapper service after manifest/source fixes?
- Does a real Media3 shim change anything for the fixed DoFun Music widget, or only for generic launcher/media controls?
