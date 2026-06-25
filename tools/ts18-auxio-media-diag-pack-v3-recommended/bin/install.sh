#!/system/bin/sh
set -u
SRC_DIR="$(cd "$(dirname "$0")/.." 2>/dev/null && pwd)"
SERVICE_SRC="$SRC_DIR/service.d/60-ts18-auxio-media-diag.sh"
CONF_SRC="$SRC_DIR/ts18-auxio-media-diag.conf"
SERVICE_DST="/data/adb/service.d/60-ts18-auxio-media-diag.sh"
CONF_DST="/data/adb/ts18-auxio-media-diag.conf"

if [ ! -f "$SERVICE_SRC" ]; then
  echo "Missing $SERVICE_SRC"
  exit 1
fi
mkdir -p /data/adb/service.d || exit 1
cp "$SERVICE_SRC" "$SERVICE_DST" || exit 1
chmod 0755 "$SERVICE_DST" || exit 1
if [ -f "$CONF_SRC" ] && [ ! -f "$CONF_DST" ]; then
  cp "$CONF_SRC" "$CONF_DST" || exit 1
  chmod 0644 "$CONF_DST" || true
fi
mkdir -p /storage/emulated/0/Download/TS18_AuxioMediaDiag 2>/dev/null || true
cat <<EOM
Installed TS18 Auxio media diagnostics.

Service script: $SERVICE_DST
Config file:    $CONF_DST

Run now:
  su -c 'sh $SERVICE_DST now'

Stop running capture:
  su -c 'sh $SERVICE_DST stop'

Disable boot auto-run:
  su -c "sed -i 's/^RUN_ON_BOOT=.*/RUN_ON_BOOT=0/' $CONF_DST"
EOM
