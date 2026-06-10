# TS18 v2 stock-disable attempts were not valid user-0 tests

**Source:** `ts18_dofun_runtime_validation_v2_20260610_162755/probes/stock_*`

**Observation:** `pm disable-user --user 10177 com.tw.music` returned `Package com.tw.music new state: disabled`, but the script was using app UID `10177`, not Android user `0`. Manual observation still reported stock Music widget behaviour unchanged.

**Inference:** This run does not prove whether `com.tw.music` can or cannot be disabled for the real primary user. Future validation must use `ANDROID_USER_ID=0` and record `pm list packages -d -u --user 0` before/after.

```text
## stock_disable_user.txt

###############################################################################
# probes/stock_disable_user.txt
###############################################################################
Date: Wed Jun 10 16:36:51 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
pm disable-user --user 10177 com.tw.music 

Package com.tw.music new state: disabled

Exit code: 0


## stock_hide.txt

###############################################################################
# probes/stock_hide.txt
###############################################################################
Date: Wed Jun 10 16:36:51 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
pm hide --user 10177 com.tw.music 

Security exception: Neither user 10177 nor current process has android.permission.MANAGE_USERS.

java.lang.SecurityException: Neither user 10177 nor current process has android.permission.MANAGE_USERS.
	at android.app.ContextImpl.enforce(ContextImpl.java:1896)
	at android.app.ContextImpl.enforceCallingOrSelfPermission(ContextImpl.java:1924)
	at com.android.server.pm.PackageManagerService.setApplicationHiddenSettingAsUser(PackageManagerService.java:13409)
	at com.android.server.pm.PackageManagerShellCommand.runSetHiddenSetting(PackageManagerShellCommand.java:1853)
	at com.android.server.pm.PackageManagerShellCommand.onCommand(PackageManagerShellCommand.java:222)
	at android.os.ShellCommand.exec(ShellCommand.java:104)
	at com.android.server.pm.PackageManagerService.onShellCommand(PackageManagerService.java:22081)
	at android.os.Binder.shellCommand(Binder.java:905)
	at android.os.Binder.onTransact(Binder.java:789)
	at android.content.pm.IPackageManager$Stub.onTransact(IPackageManager.java:4981)
	at com.android.server.pm.PackageManagerService.onTransact(PackageManagerService.java:4097)
	at android.os.Binder.execTransactInternal(Binder.java:1045)
	at android.os.Binder.execTransact(Binder.java:1018)

Exit code: 255


## stock_suspend.txt

###############################################################################
# probes/stock_suspend.txt
###############################################################################
Date: Wed Jun 10 16:36:51 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
pm suspend --user 10177 com.tw.music 

Security exception: setPackagesSuspendedAsUser: Neither user 10177 nor current process has android.permission.SUSPEND_APPS.

java.lang.SecurityException: setPackagesSuspendedAsUser: Neither user 10177 nor current process has android.permission.SUSPEND_APPS.
	at android.app.ContextImpl.enforce(ContextImpl.java:1896)
	at android.app.ContextImpl.enforceCallingOrSelfPermission(ContextImpl.java:1924)
	at com.android.server.pm.PackageManagerService.enforceCanSetPackagesSuspendedAsUser(PackageManagerService.java:13842)
	at com.android.server.pm.PackageManagerService.setPackagesSuspendedAsUser(PackageManagerService.java:13861)
	at com.android.server.pm.PackageManagerShellCommand.runSuspend(PackageManagerShellCommand.java:1922)
	at com.android.server.pm.PackageManagerShellCommand.onCommand(PackageManagerShellCommand.java:226)
	at android.os.ShellCommand.exec(ShellCommand.java:104)
	at com.android.server.pm.PackageManagerService.onShellCommand(PackageManagerService.java:22081)
	at android.os.Binder.shellCommand(Binder.java:905)
	at android.os.Binder.onTransact(Binder.java:789)
	at android.content.pm.IPackageManager$Stub.onTransact(IPackageManager.java:4981)
	at com.android.server.pm.PackageManagerService.onTransact(PackageManagerService.java:4097)
	at android.os.Binder.execTransactInternal(Binder.java:1045)
	at android.os.Binder.execTransact(Binder.java:1018)

Exit code: 255


## stock_uninstall_user.txt

###############################################################################
# probes/stock_uninstall_user.txt
###############################################################################
Date: Wed Jun 10 16:36:52 AEST 2026
Run tag: v2
Base: /sdcard/Download/ts18_dofun_runtime_validation_v2_20260610_162755
Shell identity:
uid=10177(u0_a177) gid=10177(u0_a177) groups=10177(u0_a177),3003(inet),9997(everybody),20177(u0_a177_cache),50177(all_a177) context=u:r:untrusted_app:s0:c177,c256,c512,c768

Command:
pm uninstall -k --user 10177 com.tw.music 

Security exception: Permission Denial: runUninstall from pm command asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL

java.lang.SecurityException: Permission Denial: runUninstall from pm command asks to run as user 10177 but is calling from uid u0a177; this requires android.permission.INTERACT_ACROSS_USERS_FULL
	at com.android.server.am.UserController.handleIncomingUser(UserController.java:1669)
	at com.android.server.am.ActivityManagerService.handleIncomingUser(ActivityManagerService.java:14249)
	at android.app.ActivityManager.handleIncomingUser(ActivityManager.java:3809)
	at com.android.server.pm.PackageManagerShellCommand.translateUserId(PackageManagerShellCommand.java:2687)
	at com.android.server.pm.PackageManagerShellCommand.runUninstall(PackageManagerShellCommand.java:1674)
	at com.android.server.pm.PackageManagerShellCommand.onCommand(PackageManagerShellCommand.java:206)
	at android.os.ShellCommand.exec(ShellCommand.java:104)
	at com.android.server.pm.PackageManagerService.onShellCommand(PackageManagerService.java:22081)
	at android.os.Binder.shellCommand(Binder.java:905)
	at android.os.Binder.onTransact(Binder.java:789)
	at android.content.pm.IPackageManager$Stub.onTransact(IPackageManager.java:4981)
	at com.android.server.pm.PackageManagerService.onTransact(PackageManagerService.java:4097)
	at android.os.Binder.execTransactInternal(Binder.java:1045)
	at android.os.Binder.execTransact(Binder.java:1018)

Exit code: 255
```
