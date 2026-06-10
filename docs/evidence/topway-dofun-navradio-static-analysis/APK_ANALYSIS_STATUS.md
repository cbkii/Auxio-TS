# APK analysis status

## Static evidence quality

| APK/variant | Role | Quality | Notes |
|---|---|---|---|
| DoFun Variety `_ac_anti` | Primary DoFun launcher/theme evidence | Good for assets/config/resources; weak for protected runtime code | Use for `apps_config.json`, `apps_match_config.json`, launcher identity. |
| DoFun Variety original | Baseline correlation | Good for confirming config consistency | Do not prefer over `_ac_anti` unless conflict appears. |
| DoFun Variety `_ac` | Minimal protection evidence | Low | Do not overvalue; recovered names may be synthetic. |
| TW Music original | Primary stock contract | Strong | Use manifest, widget provider, service, broadcasts, appwidget XML/layout. |
| TW Music `_ac_anti` | Secondary resource-normalised evidence | Medium | Use to confirm resources/layouts, not as primary if original is clear. |
| NavRadio+ original | Working comparator | Strong for Media3/widget/overlay concepts; not Android 10 stock contract | minSdk 32 / targetSdk 35; do not treat as direct TS18 stock app. |

## Evidence gaps

- DoFun runtime widget internals are not recoverable enough from static evidence.
- DoFun receiving of `com.tw.music.info` / progress broadcasts is not statically proven in the supplied files.
- Runtime priority between `com.tw.music` and `com.tw.media` when both exist requires TS18 validation.
- Exact DoFun Music Widget hosting mechanism requires runtime observation.
