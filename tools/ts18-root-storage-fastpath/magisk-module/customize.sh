#!/system/bin/sh
ui_print "- Installing Auxio-TS TS18 root storage helper"
ui_print "- Uses Magisk module-root service.sh for bounded late-start preparation"
ui_print "- No /system write, package disable, app launch or library scan is performed"
set_perm_recursive "$MODPATH" 0 0 0755 0644
set_perm "$MODPATH/service.sh" 0 0 0755
