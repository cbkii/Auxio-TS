# TS18 v2 synthetic-probe limitation: wrong Android user id

**Source files:**

- `ts18_dofun_runtime_validation_v2_20260610_162755/00_readme/RUN_LOG.txt`
- `ts18_dofun_runtime_validation_v2_20260610_162755/packages/query_components_user0.txt`
- `ts18_dofun_runtime_validation_v2_20260610_162755/probes/*.txt`

**Observation:** The v2 script inherited shell environment variable `USER_ID=10177`, which is the TermOnePlus app UID, not Android user `0`. Therefore synthetic `am`, `pm`, and `cmd package` probes using `--user 10177` mostly failed before delivery or operated on the wrong user context.

**Inference:** Manual observations and `dumpsys media_session/window` remain useful. Synthetic `am broadcast`, `am startservice`, `cmd package query-*`, and stock-disable tests from this run must **not** be treated as evidence that Auxio ignored commands or that stock disabling is impossible on user `0`.

## Run log excerpt

```text
Collecting identity/package baseline...
[state] initial
[state] after_music_widget_tap
[state] after_auxio_active_music_widget_controls
[state] after_stock_active_music_widget_controls
[state] after_navradio_widget_controls
Running delayed visible-state broadcasts with --user 10177...
[state] after_broadcast_music_info_implicit
[state] after_broadcast_music_info_target_dofun
[state] after_broadcast_progress_implicit
Running Auxio service/receiver probes with --user 10177...
[state] after_auxio_service_action_pp
[state] after_auxio_service_cmd_update
[state] after_auxio_service_cmd_prev
[state] after_auxio_service_cmd_next
[state] after_auxio_receiver_action_pp
[state] after_auxio_launcher_seek
Attempting reversible stock com.tw.music disable/hide/suspend tests...
[state] after_stock_disable_attempts
[state] final
Creating archive...
```

## Query/probe failure examples

```text
###############################################################################
# packages/query_components_user0.txt
###############################################################################
Date: Wed Jun 10 16:27:58 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
for a in com.tw.music.action.prev com.tw.music.action.pp com.tw.music.action.next com.tw.music.action.cmd com.tw.music.info com.tw.launcher.music_progress_duration com.android.launcher.widget_music_progress android.media.browse.MediaBrowserService androidx.media3.session.MediaSessionService; do echo ===== ACTION $a =====; cmd package query-services --brief --components --user 10177 -a $a 2>&1 || true; cmd package query-receivers --brief --components --user 10177 -a $a 2>&1 || true; cmd package query-activities --brief --components --user 10177 -a $a 2>&1 || true; done

===== ACTION com.tw.music.action.prev =====
Security exception: Permission Denial: null asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS

java.lang.SecurityException: Permission Denial: null asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
	at com.android.server.am.UserController.handleIncomingUser(UserController.java:1669)
	at com.android.server.am.ActivityManagerService.handleIncomingUser(ActivityManagerService.java:14249)
	at android.app.ActivityManager.handleIncomingUser(ActivityManager.java:3809)
	at com.android.server.pm.PackageManagerShellCommand.parseIntentAndUser(PackageManagerShellCommand.java:897)
	at com.android.server.pm.PackageManagerShellCommand.runQueryIntentServices(PackageManagerShellCommand.java:990)
	at com.android.server.pm.PackageManagerShellCommand.onCommand(PackageManagerShellCommand.java:165)
	at android.os.ShellCommand.exec(ShellCommand.java:104)
	at com.android.server.pm.PackageManagerService.onShellCommand(PackageManagerService.java:22081)
	at android.os.Binder.shellCommand(Binder.java:905)
	at android.os.Binder.onTransact(Binder.java:789)
	at android.content.pm.IPackageManager$Stub.onTransact(IPackageManager.java:4981)
	at com.android.server.pm.PackageManagerService.onTransact(PackageManagerService.java:4097)
	at android.os.Binder.execTransactInternal(Binder.java:1045)
	at android.os.Binder.execTransact(Binder.java:1018)
Security exception: Permission Denial: null asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS

java.lang.SecurityException: Permission Denial: null asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
	at com.android.server.am.UserController.handleIncomingUser(UserController.java:1669)
	at com.android.server.am.ActivityManagerService.handleIncomingUser(ActivityManagerService.java:14249)
	at android.app.ActivityManager.handleIncomingUser(ActivityManager.java:3809)
	at com.android.server.pm.PackageManagerShellCommand.parseIntentAndUser(PackageManagerShellCommand.java:897)
	at com.android.server.pm.PackageManagerShellCommand.runQueryIntentReceivers(PackageManagerShellCommand.java:1024)
	at com.android.server.pm.PackageManagerShellCommand.onCommand(PackageManagerShellCommand.java:167)
	at android.os.ShellCommand.exec(ShellCommand.java:104)
	at com.android.server.pm.PackageManagerService.onShellCommand(PackageManagerService.java:22081)
	at android.os.Binder.shellCommand(Binder.java:905)
	at android.os.Binder.onTransact(Binder.java:789)
	at android.content.pm.IPackageManager$Stub.onTransact(IPackageManager.java:4981)
	at com.android.server.pm.PackageManagerService.onTransact(PackageManagerService.java:4097)
	at android.os.Binder.execTransactInternal(Binder.java:1045)
	at android.os.Binder.execTransact(Binder.java:1018)
Security exception: Permission Denial: null asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS

java.lang.SecurityException: Permission Denial: null asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
	at com.android.server.am.UserController.handleIncomingUser(UserController.java:1669)
	at com.android.server.am.ActivityManagerService.handleIncomingUser(ActivityManagerService.java:14249)
	at android.app.ActivityManager.handleIncomingUser(ActivityManager.java:3809)
	at com.android.server.pm.PackageManagerShellCommand.parseIntent

--- auxio service pp ---
###############################################################################
# probes/auxio_service_action_pp.txt
###############################################################################
Date: Wed Jun 10 16:35:06 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
am startservice --user 10177 -n com.tw.media/com.tw.music.MusicService -a com.tw.music.action.pp 

Starting service: Intent { act=com.tw.music.action.pp cmp=com.tw.media/com.tw.music.MusicService }
Exception: java.lang.SecurityException: Permission Denial: service from com.android.shell asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
java.lang.SecurityException: Permission Denial: service from com.android.shell asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
	at com.android.server.am.UserController.handleIncomingUser(UserController.java:1669)
	at com.android.server.am.ActiveServices.retrieveServiceLocked(ActiveServices.java:2138)
	at com.android.server.am.ActiveServices.startServiceLocked(ActiveServices.java:480)
	at com.android.server.am.ActiveServices.startServiceLocked(ActiveServices.java:454)
	at com.android.server.am.ActivityManagerService.startService(ActivityManagerService.java:14181)
	at com.android.server.am.ActivityManagerShellCommand.runStartService(ActivityManagerShellCommand.java:630)
	at com.android.server.am.ActivityManagerShellCommand.onCommand(ActivityManagerShellCommand.java:175)
	at com.android.server.am.ActivityManagerShellCommandEx.onCommand(ActivityManagerShellCommandEx.java:134)
	at android.os.ShellCommand.exec(ShellCommand.java:104)
	at com.android.server.am.ActivityManagerServiceEx.onShellCommand(ActivityManagerServiceEx.java:152)
	at android.os.Binder.shellCommand(Binder.java:905)
	at android.os.Binder.onTransact(Binder.java:789)
	at android.app.IActivityManager$Stub.onTransact(IActivityManager.java:4592)
	at com.android.server.am.ActivityManagerService.onTransact(ActivityManagerService.java:2774)
	at android.os.Binder.execTransactInternal(Binder.java:1045)
	at android.os.Binder.execTransact(Binder.ja

--- metadata broadcast ---
###############################################################################
# probes/broadcast_music_info_implicit.txt
###############################################################################
Date: Wed Jun 10 16:32:49 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
am broadcast --user 10177 -a com.tw.music.info --es musicTitle TS18_PROBE_TITLE --es musicaArtist TS18_PROBE_ARTIST --es musicAlbum TS18_PROBE_ALBUM --es musicPath /storage/usbdisk0/TS18_PROBE.mp3 

Broadcasting: Intent { act=com.tw.music.info flg=0x400000 (has extras) }
Exception: java.lang.SecurityException: Permission Denial: broadcast asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
java.lang.SecurityException: Permission Denial: broadcast asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL or android.permission.INTERACT_ACROSS_USERS
	at com.android.server.am.UserController.handleIncomingUser(UserController.java:1669)
	at com.android.server.am.ActivityManagerService.broadcastIntentLocked(ActivityManagerService.java:15104)
	at com.android.server.am.ActivityManagerService.broadcastIntentLocked(ActivityManagerService.java:15068)
	at com.android.server.am.ActivityManagerService.broadcastIntent(ActivityManagerService.java:15838)
	at com.android.server.am.ActivityManagerShellCommand.runSendBroadcast(ActivityManagerShellCommand.java:720)
	at com.android.server.am.ActivityManagerShellCommand.onCommand(ActivityManagerShellCommand.java:185)
	at com.android.server.am.ActivityManagerShellCommandEx.onCommand(ActivityManagerShellCommandEx.java:134)
	at android.os.ShellCommand.exec(ShellCo
```
