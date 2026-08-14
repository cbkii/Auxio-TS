# Topway stock package contract evidence

This file records the compact evidence needed by current Track-A and Track-C engineering. It does not set product policy.

## Provenance and identity

- **Observed:** captured APK `com.tw.music_TW_THEME.20240715.apk`.
- **Observed:** APK SHA-256 `4f5495e270a7c86bab232e2b7ee2ecd2d71f3450f6f20ed5f36feaa4229c1518`.
- **Observed:** package `com.tw.music`, captured version code `118`, platform signer SHA-256 `AA6F9FB3070512AC962425797CD65AA585CF6202937EE3CEEFB14B5802EABDF3` and UID 1000 on the recorded device.
- **Observed:** DoFun APK `com.dofun.variety_V9.7.2.367.260312.apk`, SHA-256 `75e7ea9b46d68754253aa385e6ac750aae957a5b72196fec5449ccf2782c60b1`.
- **Physically unverified:** whether the currently installed device packages still match these captured hashes, signer, version and behaviour.

## Components

- **Observed:** stock `com.tw.music.MusicActivity` and `com.tw.music.MusicService`.
- **Observed:** stock-name widget provider `com.tw.music.view.MusicWidgetProvider` in the compatibility contract.
- **Observed:** DoFun matching includes `com.tw.media/com.tw.music.MusicActivity` and `com.tw.music/com.tw.music.MusicActivity`.
- **Current engineering use:** Auxio-TS exposes the first component contract inside its `com.tw.media` app. Track C operates only in the genuine second package.

## Actions and extras

| Direction | Action | Observed extras/meaning |
| --- | --- | --- |
| Incoming command | `com.tw.music.action.cmd` | `cmd`: captured values include `prev`, `next`, `pp`, `update`. |
| Incoming command | `com.tw.music.action.prev` | Previous. |
| Incoming command | `com.tw.music.action.next` | Next. |
| Incoming command | `com.tw.music.action.pp` | Play/pause. |
| Incoming widget | `com.android.launcher.widget_music_progress` | `music_progress`: seek/progress input in captured paths. |
| Outgoing metadata | `com.tw.music.info` | `musicTitle`, `musicaArtist`, `musicAlbum`, `musicPath`. |
| Outgoing progress | `com.tw.launcher.music_progress_duration` | `msg_music_progress`, `msg_music_duration`. |

These facts justify bounded adapters and fixtures, not copied vendor implementation. Current allowlists, unit conversion and admission behaviour remain owned by source/tests.

## Track-C exact static scope

The optional LSPosed add-on static scope is exactly:

```text
com.tw.music
```

Private obfuscated presenter hooks are pinned to the captured APK SHA-256. An unseen build may retain independently capability-probed public paths but must not guess changed private methods.

## Explicit non-authority

String hits for `android.uid.system`, shared UID, private Cardoor services, `android.tw.john.TWUtil` or `com.tw.service.xt` AIDL do not authorise Auxio to adopt them. Root does not reproduce the stock signer or UID. Exact launcher, widget, ACC, cold-boot and progress behaviour remains physically unverified until executed on the target device.
