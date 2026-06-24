# Codex TS18 exact-device context

Concise redacted context for agents working on Auxio-TS install/runtime behavior for the observed `s9863a1h10` Android 10 TS18 device.

[Evidence confidence: Observed exact-device diagnostics] [Porting decision: Runtime validation requirement]

## Device identity

- Device/model: `s9863a1h10_Natv` / `s9863a1h10`
- Android: 10 / SDK 29
- Platform: `sp9863a`
- Hardware: `uis8581a2h10`
- FOTA: `WINDOW-THEME1_1000`
- Topway version: `TS18.2.2_20241210.165912_WINDOW-THEME1`
- Firmware family: TOPWAY / UIS8581A

## Authority and package facts

- Latest root diagnostics identity: `uid=0(root)`, `u:r:magisk:s0`.
- Root availability does not give Auxio UID 1000, platform signing, vendor signing, signature permissions, or private Topway authority.
- Stock `com.tw.music` is a privileged system package at `/system/priv-app/com.tw.music_a41e` with observed versionName `TW_THEME.20240715`, userId `1000`, and platform/vendor signature lineage different from user-signed Auxio variants.
- Existing user-installed `com.tw.media` Auxio-TS alternate package was normal app UID and exposed `com.tw.media/com.tw.music.MusicActivity`, the Topway bridge receiver, widget provider, and `com.tw.music.MusicService`.
- `topwayTwMusicRelease` may conflict with stock `com.tw.music` unless package state/signing is deliberately managed.
- `topwayTwMediaRelease` is an alternate fixed DoFun entry, not a guaranteed no-root bypass.

## Display and storage

- Physical/real display: 1280x720 at about 58 Hz.
- App display: 1225x720; stable/content frame: `[0,55]-[1225,720]`.
- Top status region and right nav/sidebar region are about 55 px each.
- Prefer discovered `/storage/usbdiskN` paths for normal app behavior. Raw `/mnt/media_rw/usbdiskN` paths are for root diagnostics/recovery, not normal app behavior.

## Boundaries

Do not copy stock smali, fake Cardoor services, require `android.uid.system`/`sharedUserId`, add TWUtil/TWClient reflection, add vendor-service binders, or claim physical TS18 success without real-device validation. Private/native integration is not for production by default and requires the formal evidence-gated gap-and-promotion process.
