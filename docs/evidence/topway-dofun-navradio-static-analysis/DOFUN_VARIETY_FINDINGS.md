# DoFun Variety findings

## Observations

DoFun Variety `_ac_anti` is the primary DoFun evidence source. Its code is protected/stubbed enough that the asset/config files are more useful than the recovered Java names.

The strongest DoFun evidence is in `apps_match_config.json`:

- Source: `com.dofun.variety_V9.7.2.367.260312_ac_anti/apktool/assets/apps_match_config.json` lines 15-31.
- It defines `soft_name=hotseat_app_music`, `icon_name=link_icon_music`, `compare_soft_name=音乐,音樂,Music`, `function=music_set`, `behavior=fixed`.
- It lists `more_packages`:
  - `package_name=com.tw.media`, `class_name=com.tw.music.MusicActivity`
  - `package_name=com.tw.music`, `class_name=com.tw.music.MusicActivity`

The same hotseat music mapping appears again at lines 141-153 without the `function`/`behavior` fields. That repetition supports treating the mapping as deliberate.

`apps_config.json` also maps the same Topway identities to music icons:

- `compare_name=com.tw.music`, `image_name=app_music`, `link_image_name=link_icon_music`, `name=音乐`.
- `package_name=com.tw.media`, `compare_name=com.tw.music.MusicActivity`, `image_name=app_music`, `link_image_name=link_icon_music`, `name=音乐`.

## Inference

DoFun's fixed music hotseat/card logic is package/class sensitive. Auxio-TS should continue exposing `com.tw.media/com.tw.music.MusicActivity` as the main no-root replacement target.

## Negative evidence

Searches of the supplied DoFun `_ac_anti` static evidence did not reveal these stock TW Music runtime strings inside DoFun itself:

- `com.tw.music.info`
- `com.tw.launcher.music_progress_duration`
- `msg_music_progress`
- `msg_music_duration`
- `com.android.launcher.widget_music_progress`
- `MusicWidgetProvider`
- `AppWidgetHost`

This does not prove DoFun never uses them. It means they are not recoverable from this protected/static DoFun evidence. The stock TW Music APK remains the stronger source for the music-widget runtime contract.

## Recommended interpretation

Use DoFun evidence to lock down launch identity and hotseat/icon matching. Use stock TW Music evidence to define the widget/command/broadcast protocol. Use NavRadio+ only as a comparator for modern media sessions, simple transport controls, overlay fallback, and robust file-picker behaviour.

See excerpts:

- `excerpts/configs/dofun_apps_match_config_hotseat_music_primary.md`
- `excerpts/configs/dofun_apps_match_config_hotseat_music_secondary.md`
- `excerpts/configs/dofun_apps_config_music_entries.md`
- `excerpts/configs/dofun_link_icon_music_names.md`
