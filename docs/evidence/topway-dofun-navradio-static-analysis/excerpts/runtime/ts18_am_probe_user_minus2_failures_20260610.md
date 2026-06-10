# TS18 runtime excerpt: `am` probes failed because default user resolved to `-2`

Source: `ts18_dofun_runtime_validation_20260610_125608/probes/*/*.txt` and `runtime/home_start_attempt.txt`

Type: Tooling observation.

Why it matters: The failed synthetic broadcast/startservice probes are not proof that Auxio-TS ignored commands. They failed before delivery because TermOnePlus ran as an untrusted app UID and the shell commands defaulted to user `-2`/current. Future scripts should pass `--user 0` explicitly.

```text
--- probes/broadcasts/music_info_implicit.txt ---
Command:
am broadcast -a com.tw.music.info --es musicTitle TS18_PROBE_TITLE --es musicaArtist TS18_PROBE_ARTIST --es musicAlbum TS18_PROBE_ALBUM --es musicPath /storage/usbdisk0/TS18_PROBE.mp3 
Exception: java.lang.SecurityException: Permission Denial: broadcast asks to run as user -2 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
java.lang.SecurityException: Permission Denial: broadcast asks to run as user -2 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
Exit code: 255
--- probes/service_commands/auxio_service_cmd_update.txt ---
Command:
am startservice -n com.tw.media/com.tw.music.MusicService -a com.tw.music.action.cmd --es cmd update 
Exception: java.lang.SecurityException: Permission Denial: service from com.android.shell asks to run as user -2 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
java.lang.SecurityException: Permission Denial: service from com.android.shell asks to run as user -2 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
Exit code: 255
--- runtime/home_start_attempt.txt ---
Command:
am start -a android.intent.action.MAIN -c android.intent.category.HOME 
Exception: java.lang.SecurityException: Permission Denial: startActivityAsUser asks to run as user -2 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL
java.lang.SecurityException: Permission Denial: startActivityAsUser asks to run as user -2 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL
Exit code: 255
```
