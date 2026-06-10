# Topway / DoFun / NavRadio static analysis evidence

This pack is a curated, source-control-friendly summary of the compact static APK evidence supplied for Auxio-TS launcher integration work.

Source paths in this document are relative to the temporary merged static evidence directory produced by unzipping the three standalone ZIP parts. The repo-ready pack intentionally does not include raw APKs or full decompiled trees.

## Source APK roles

- **DoFun Variety `_ac_anti`** is treated as the primary DoFun launcher/theme evidence because it preserves the relevant asset/config evidence and normalised resources.
- **DoFun Variety original** is a compact baseline/correlation source.
- **DoFun Variety `_ac`** is treated only as minimal MT Manager Dex anti-confusion/protection evidence.
- **TW Music original** is treated as the primary stock Topway music contract evidence.
- **TW Music `_ac_anti`** is secondary resource-normalised evidence.
- **NavRadio+ original** is the working comparator, mainly for Media3/session/widget/overlay design, not for the stock Topway music-widget protocol.

## Limitations

This is static analysis. It cannot prove that DoFun will dispatch a given runtime action to Auxio-TS on the TS18. It can identify the component names, actions, extras, widget layouts and comparator behaviours that Auxio-TS should satisfy.

Do not claim the launcher integration is fixed without TS18 runtime validation.

## Main source paths

- DoFun config: `com.dofun.variety_V9.7.2.367.260312_ac_anti/apktool/assets/apps_match_config.json`
- DoFun config: `com.dofun.variety_V9.7.2.367.260312_ac_anti/apktool/assets/apps_config.json`
- Stock TW Music manifest: `com.tw.music_TW_THEME.20240715/apktool/AndroidManifest.xml`
- Stock TW Music widget provider: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/view/MusicWidgetProvider.java`
- Stock TW Music service: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/MusicService.java`
- NavRadio+ manifest: `NavRadio+_4.00_apks/apktool/AndroidManifest.xml`
- NavRadio+ service: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/RadioService.java`

See `INDEX.md` for how to use each file.


## Final runtime status

The final TS18 validation update is `TS18_RUNTIME_VALIDATION_20260610_FINAL.md`. The strongest runtime result is that the fixed DoFun Music widget still opens/controls stock `com.tw.music` while Auxio `com.tw.media` is installed and Android-media-session-visible. This pack therefore directs Agents to implement the stock TW Music contract fully, while preserving overlay fallback and avoiding unvalidated claims of DoFun replacement.
