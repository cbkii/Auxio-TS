# DoFun Variety / Topway music compatibility

Auxio-TS targets TS18/Topway head units using DoFun Variety Theme (`com.dofun.variety`). The
supported integration keeps genuine stock `com.tw.music` in place and uses a statically scoped
LSPosed addon to connect its public integration surface to Auxio-TS running as `com.tw.media`.

This document is the concise compatibility guide. Raw APK reference snippets live in `docs/reference/ts18-apk/`. The exact-device runtime profile lives in `docs/evidence/ts18-device-profile/`.

## Primary compatibility contract

DoFun Variety's extracted `assets/apps_match_config.json` contains fixed music hotseat entries for:

```text
com.tw.media / com.tw.music.MusicActivity
com.tw.music / com.tw.music.MusicActivity
```

The fixed identity that DoFun addresses is:

```text
application/package ID: com.tw.music
launcher/activity component: com.tw.music.MusicActivity
label: Music
```

Auxio cannot safely acquire this platform-signed UID-1000 identity. The API-100 bridge runs only
inside the genuine stock process and connects it to the normal Android media surfaces exported by
the separately installed Auxio app.

## Variant expectations

| Build/component | Package/application ID | Scope/component | Purpose |
| --- | --- | --- | --- |
| Auxio-TS primary release | `com.tw.media` | `com.tw.media/com.tw.music.MusicActivity` | Supported independently signed player |
| LSPosed API-100 addon | `org.oxycblt.auxio.ts18bridge` | static scope: `com.tw.music` only | Stock-identity bridge into Auxio |
| Topway exact fixture | `com.tw.music` | internal build/test only | Verifies historical package/component contracts; not published |

Use a real Android product flavour/source set. Do not mutate package names only in CI.

## Exact-device package conflict

The real target TS18 diagnostics show stock `com.tw.music` installed as a system priv-app:

```text
/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
```

[Evidence confidence: Observed from user-provided device diagnostics] [Porting decision: Installation/runbook constraint]

Implications:

- keep genuine stock `com.tw.music` installed and enabled;
- do not publish or install Auxio as an exact-package replacement;
- use the signed bridge with its single static/recommended `com.tw.music` scope;
- retire the former exact-package Magisk overlay instead of running both mechanisms;
- retain real-device validation because static identity evidence cannot prove DoFun panel parity.

## `com.tw.media` alternate-entry posture

DoFun's alternate fixed entry is directly reusable as a variant target:

```text
com.tw.media/com.tw.music.MusicActivity
```

[Evidence confidence: Observed APK/reference evidence] [Porting decision: Directly reusable alternate-entry requirement]

The supported `com.tw.media` application must still be documented honestly:

- It does not remove or neutralise stock `com.tw.music`.
- It may conflict if firmware already provides stock `com.tw.media`.
- It may not be selected if DoFun prioritises `com.tw.music`.
- Without the LSPosed addon, it may not be selected if DoFun prioritises `com.tw.music`.
- The full bridge path requires root/LSPosed even though the Auxio APK itself is normally signed.

## Required Android media surfaces

Keep Android-standard media integration intact:

- `MediaSession`
- `android.media.browse.MediaBrowserService`
- media notification
- media button handling
- metadata and playback state updates

DoFun may combine fixed package matching with notification listening, media session inspection, and private Cardoor services. The safe implementation starts with exact package/component identity plus Android-standard media surfaces plus the observed Topway broadcast bridge.

## Observed Topway public broadcast/control surface

Keep these constants centralised in an isolated bridge package such as:

```text
app/src/main/java/org/oxycblt/auxio/headunit/topway/
```

Outgoing metadata:

```text
action: com.tw.music.info
extras: musicTitle, musicaArtist, musicAlbum, musicPath
```

Outgoing progress/duration:

```text
action: com.tw.launcher.music_progress_duration
extras: msg_music_progress, msg_music_duration
```

Incoming commands:

```text
com.tw.music.action.cmd
com.tw.music.action.prev
com.tw.music.action.next
com.tw.music.action.pp
com.android.launcher.widget_music_progress
```

Known command/progress extras:

```text
cmd
appWidgetIds
music_progress
```

Preserve the misspelt `musicaArtist` extra because that is the observed stock-compatible spelling.

## Observed private surfaces that are not implementation approval

DoFun Variety references these private/Cardoor services:

```text
cn.cardoor.libs.media.RemoteMediaService
cn.cardoor.basic.media.NotifyService
cn.cardoor.libs.media.impl.MediaSourceService
```

Treat these as evidence only. Do not add a fake `RemoteMediaService` or bind to private Cardoor protocols unless a concrete binder/AIDL contract is recovered and approved.

The stock Topway music APK also contains private/system/vendor hooks such as:

```text
android.uid.system
android.tw.john.TWUtil
com.tw.service.xt.aidl.ITWCommandAidl
com.tw.service.xt.aidl.ITWCommandCallbackAidl
```

These must not be copied into Auxio-TS product code. Use Android-standard APIs and the isolated Topway bridge instead unless a later approved Tier 4 design PR proves a safe protocol and rollback path.

## Exact-device private/system evidence

The exact TS18 diagnostic profile indicates the stock music app is part of a broader Topway system environment:

- stock `com.tw.music` is a system priv-app;
- Topway packages such as `com.tw.service`, `com.tw.service.xt`, `com.tw.radio`, `com.tw.bt`, and `com.tw.eq` are system priv-apps;
- `com.tw.service` was observed mediating media audio focus under UID 1000;
- ZLink was observed as the restored media-button receiver in the captured state;
- `/storage/usbdisk0` is the observed USB-media mount path.

[Evidence confidence: Observed from user-provided diagnostics] [Porting decision: Runtime parity risk and validation requirement; not direct approval to port private APIs]

## Runtime fallback posture

Auxio-TS implements the highest-confidence DoFun/Topway path without copying private vendor APIs:

- the published player installs as `com.tw.media`;
- the bridge loads only in genuine `com.tw.music`, verifies its UID/certificate, and fails open;
- the bridge connects to Auxio through Android `MediaBrowser`/`MediaController`;
- the genuine stock process remains the sender/receiver for observed Topway broadcasts;
- cold-start commands connect to Auxio instead of relying on a replaced stock package.

Auxio-TS still deliberately avoids fake `cn.cardoor.libs.media.RemoteMediaService`, `android.tw.john.TWUtil`, and `com.tw.service.xt.aidl.*` implementations unless a later human-approved design PR proves a safe protocol.

## Validation commands

Package/component checks:

```sh
adb shell 'cmd package list packages | grep -E "com\.tw\.music|com\.tw\.media|org\.oxycblt\.auxio|com\.dofun\.variety"'
adb shell cmd package resolve-activity --brief -n com.tw.music/com.tw.music.MusicActivity
adb shell cmd package resolve-activity --brief -n com.tw.media/com.tw.music.MusicActivity
adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.APP_MUSIC
adb shell cmd package query-intent-services -a android.media.browse.MediaBrowserService
```

Media/session and widget traffic:

```sh
adb shell dumpsys media_session | grep -i -A60 'com.tw.music\|com.tw.media\|auxio'
adb shell logcat -v time | grep -iE 'Auxio|Topway|tw.music|tw.media|music_progress|MediaSource|NotifyService|cardoor|dofun|variety|MediaSession|MediaBrowser'
```

Manual widget-command simulation:

```sh
adb shell am broadcast -a com.tw.music.action.pp
adb shell am broadcast -a com.tw.music.action.next
adb shell am broadcast -a com.tw.music.action.prev
```

Manual outgoing-state simulation:

```sh
adb shell am broadcast -a com.tw.music.info   --es musicTitle "Auxio Test"   --es musicaArtist "Test Artist"   --es musicAlbum "Test Album"   --es musicPath "/storage/usbdisk0/Music/test.mp3"

adb shell am broadcast -a com.tw.launcher.music_progress_duration   --el msg_music_progress 30000   --el msg_music_duration 180000
```

## CI and safety checks

CI should protect:

- the retired standard variant remains non-distributable;
- the internal Topway exact fixture still compiles and satisfies the historical contract;
- the Topway alternate release variant is installed with the exact package ID `com.tw.media`;
- the LSPosed addon declares API 100, `staticScope=true`, and only `com.tw.music`;
- `com.tw.music.MusicActivity` alias exists in Topway-compatible variants;
- `MediaBrowserService` remains declared/exported as intended;
- provider authorities follow the variant application ID;
- Topway broadcast/action strings remain isolated;
- private/system/vendor hooks remain forbidden outside approved evidence/docs/tests/wrapper boundaries.

Use/update:

```sh
bash scripts/check-headunit-compat-safety.sh
bash scripts/check-dofun-topway-compat.sh
bash scripts/check-ts18-apk-reference-contracts.sh
```

Add or keep a more specific DoFun/Topway manifest/APK check when the flavour is implemented.

## Exact-device hardening notes

**Evidence confidence:** Observed. **Porting decision:** Directly reusable requirement. The redacted `s9863a1h10` Android 10 profile confirms DoFun fixed entries for both `com.tw.music/com.tw.music.MusicActivity` and `com.tw.media/com.tw.music.MusicActivity`.

Auxio-TS keeps two Topway-compatible build identities but publishes only the non-conflicting player
plus the bridge:

| Component | Package | DoFun/runtime role | Release posture |
| --- | --- | --- | --- |
| `topwayTwMusicRelease` | `com.tw.music` | Internal exact-identity contract fixture | Build/test only; never install or publish |
| `topwayTwMediaRelease` | `com.tw.media` | Independently signed Auxio player | Primary published APK |
| LSPosed API-100 bridge | `org.oxycblt.auxio.ts18bridge` | Runs only in genuine stock `com.tw.music` | Separately signed addon |

Both app variants reuse the same thin wrapper source set (`com.tw.music.MusicActivity`,
`com.tw.music.MusicService`, and `com.tw.music.view.MusicWidgetProvider`) and delegate into
Auxio-owned code. The published `com.tw.media` APK exposes stock-compatible class names without
claiming the stock package, system UID, platform signature, private Cardoor services, TWUtil,
vendor binders, or copied smali.

Topway-compatible variants use `com.tw.music.MusicService` as the canonical exported external MediaBrowserService. The base Auxio `org.oxycblt.auxio.AuxioService` remains available for explicit in-app starts, but its inherited browse/search intent filters are removed in the Topway wrapper manifest so external TS18/DoFun clients do not split across two service component names. **Evidence confidence:** Inferred from manifest design. **Porting decision:** Requires TS18 runtime validation. Runtime validation must still check for duplicate active sessions, duplicate foreground services, and duplicate lifecycle starts before claiming final TS18 parity.
