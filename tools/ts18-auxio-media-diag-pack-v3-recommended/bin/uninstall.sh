#!/system/bin/sh
su -c 'rm -f /data/adb/service.d/60-ts18-auxio-media-diag.sh /data/adb/ts18-auxio-media-diag.conf; touch /storage/emulated/0/Download/TS18_AuxioMediaDiag.STOP'
echo 'Removed service script and config. Existing captured output was not deleted.'
