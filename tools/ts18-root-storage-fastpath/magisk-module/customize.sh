#!/system/bin/sh
ui_print "- Installing Auxio-TS TS18 root storage helper"
ui_print "- No /system write, package disable, app launch or library scan is performed"
set_perm_recursive "$MODPATH" 0 0 0755 0644
set_perm "$MODPATH/service.d/55-auxio-root-storage-prepare.sh" 0 0 0755
