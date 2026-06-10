# TS18 runtime excerpt: Auxio overlay window is present

Source: `ts18_dofun_runtime_validation_20260610_125608/runtime/phases/initial/window_focus.txt` and `final/window_focus.txt`

Type: Observation.

Why it matters: The TS18 runtime showed a `com.tw.media` window with `appop=SYSTEM_ALERT_WINDOW`, so Auxio-TS overlay/floating controls appear to be granted and present. This supports overlay as a validated fallback control surface, not the primary DoFun Music widget fix.

```text
--- initial ---
  Window #3 Window{4a55a7b u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #14 Window{4916762 u0 com.tw.media/com.tw.music.MusicActivity}:
--- final ---
  Window #3 Window{4a55a7b u0 com.tw.media}:
    mOwnerUid=10196 mShowToOwnerOnly=true package=com.tw.media appop=SYSTEM_ALERT_WINDOW
  Window #8 Window{c3a5b0a u0 com.tw.media/com.tw.music.MusicActivity}:
```
