# TS18 validation plan under no-root/no-ADB constraints

## Known constraints

Prior TS18 diagnostics and the 2026-06-10 runtime validation show TermOnePlus runs as an untrusted app UID:

```text
uid=10177(u0_a177) ... context=u:r:untrusted_app:s0:c177,c256,c512,c768
```

Practical consequences:

- `pm path`, `pm list packages`, selected `dumpsys media_session`, `dumpsys audio`, and `dumpsys window` can provide useful evidence.
- `dumpsys notification`, `dumpsys appwidget`, ActivityManager internals, bugreport/dumpstate, raw input events, and package internals are unreliable or denied.
- `am` commands must pass `--user 0`; otherwise this build may default to current-user sentinel `-2` and fail with `INTERACT_ACROSS_USERS` permission errors.
- `cmd package query-intent-*` is not available; use `cmd package query-activities`, `query-services`, and `query-receivers` with `--user 0`.

## Install/runtime baseline

Collect these first:

```sh
pm path com.dofun.variety
pm path com.tw.music
pm path com.tw.media
pm path com.navimods.radio
pm path com.tw.radio
pm list packages -f -u -U -i | grep -E 'com\.dofun\.variety|com\.tw\.music|com\.tw\.media|com\.navimods\.radio|com\.tw\.radio|com\.tw\.service'
pm list packages -d -u
```

The 2026-06-10 runtime run showed:

- `com.tw.music` installed as `/system/priv-app/...`, UID 1000;
- Auxio-TS `com.tw.media` installed as a normal data app, UID 10196;
- both stock `com.tw.music` and Auxio `com.tw.media` enabled at test time;
- DoFun Music widget opened stock `com.tw.music` while both were present.

## Launch checks

Use explicit user 0:

```sh
am start --user 0 -n com.tw.media/com.tw.music.MusicActivity
am start --user 0 -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n com.tw.media/com.tw.music.MusicActivity
```

Observe:

- launches Auxio-TS rather than stock TW Music;
- no crash/dialog loop;
- no long blocking scan on launch;
- app remains usable if USB storage/library is unavailable.

## Widget/control checks

With DoFun Variety active:

1. Confirm the launcher has only the fixed Music and Radio widgets/cards.
2. Tap the fixed DoFun Music widget.
3. Record whether it opens `com.tw.music` or `com.tw.media`.
4. Start Auxio-TS playback manually, return to DoFun, and test Music widget previous/play-pause/next.
5. Start stock TW Music playback, return to DoFun, and test Music widget previous/play-pause/next.
6. Start NavRadio+, return to DoFun, and test the Radio widget separately from the Music widget.

Also capture:

```sh
dumpsys media_session > /sdcard/Download/media_session_after_each_phase.txt 2>&1
dumpsys window windows > /sdcard/Download/window_after_each_phase.txt 2>&1
dumpsys audio > /sdcard/Download/audio_after_each_phase.txt 2>&1
```

## Manual broadcast/service probes

Use explicit user 0. Failure is still diagnostic, but a user `-2` error means the command did not reach the target.

```sh
am broadcast --user 0 -a com.tw.music.info \
  --es musicTitle TS18_PROBE_TITLE \
  --es musicaArtist TS18_PROBE_ARTIST \
  --es musicAlbum TS18_PROBE_ALBUM \
  --es musicPath /storage/usbdisk0/TS18_PROBE.mp3

am broadcast --user 0 -a com.tw.launcher.music_progress_duration \
  --ei msg_music_progress 45000 \
  --ei msg_music_duration 180000

am startservice --user 0 -n com.tw.media/com.tw.music.MusicService -a com.tw.music.action.pp
am startservice --user 0 -n com.tw.media/com.tw.music.MusicService -a com.tw.music.action.cmd --es cmd update
am startservice --user 0 -n com.tw.media/com.tw.music.MusicService -a com.tw.music.action.cmd --es cmd prev
am startservice --user 0 -n com.tw.media/com.tw.music.MusicService -a com.tw.music.action.cmd --es cmd next

am broadcast --user 0 -a com.android.launcher.widget_music_progress -p com.tw.media --ei music_progress 60000
```

Because TermOnePlus cannot stay visible while the launcher is visible, use a countdown harness: start the command, switch to the launcher, let the command fire, observe, then return.

## Stock TW Music disable/hide experiments

Only run deliberately and record exact output. Try reversible methods first:

```sh
pm disable-user --user 0 com.tw.music
pm hide --user 0 com.tw.music
pm suspend --user 0 com.tw.music
```

Restore attempts:

```sh
pm enable --user 0 com.tw.music
pm unhide --user 0 com.tw.music
pm unsuspend --user 0 com.tw.music
```

Only if deliberately testing per-user removal:

```sh
pm uninstall -k --user 0 com.tw.music
cmd package install-existing --user 0 com.tw.music
```

Do not assume these will work from TermOnePlus. If they fail with permission errors, document that and treat stock package coexistence as a hard runtime constraint.

## What still cannot be proven from static analysis alone

- Whether DoFun hosts `com.tw.music.view.MusicWidgetProvider` as a normal AppWidget or uses a custom fixed card.
- Whether DoFun listens to `com.tw.music.info` and `com.tw.launcher.music_progress_duration` in the current theme/runtime build.
- Whether DoFun sends `com.android.launcher.widget_music_progress` for seek/progress interaction.
- Whether DoFun will choose `com.tw.media` if stock `com.tw.music` is disabled, hidden, suspended, or per-user uninstalled.
- Whether a real Media3 shim changes DoFun fixed Music widget behaviour.


## Corrected validation note after v2

Do not use environment variable `USER_ID` for Android user selection on TermOnePlus. It may be the app UID (`10177`). Use `ANDROID_USER_ID=0` with the corrected v3 collector.

Recommended final pre/post-implementation checks:

```sh
cd /sdcard/Download
ANDROID_USER_ID=0 sh collect-ts18-dofun-runtime-validation-v3.sh baseline
ANDROID_USER_ID=0 TRY_DISABLE_STOCK=1 sh collect-ts18-dofun-runtime-validation-v3.sh user0-disable-stock-test
ANDROID_USER_ID=0 RESTORE_STOCK=1 sh collect-ts18-dofun-runtime-validation-v3.sh restore-stock-user0
```

Only the `baseline` run is safe/read-only. The stock-disable run is optional and should be performed only when prepared to restore stock package state.
