#!/system/bin/sh
# Bounded Magisk late-start preparation for the exact TS18 storage layout.
STATE_DIR=/data/adb/auxio-ts-root
MANIFEST="$STATE_DIR/volumes.tsv"
TEMP="$STATE_DIR/volumes.tsv.tmp.$$"
ALIAS_ROOT=/storage/auxio-root
MAX_WAIT_SECONDS=20
MAX_SAMPLE_DEPTH=6

log_msg() { log -t AuxioRootStorage "$*" 2>/dev/null || true; }
valid_component() {
  case "$1" in
    usbdisk[0-9]|usbdisk[0-9][0-9]) return 0 ;;
    *) return 1 ;;
  esac
}
clean_stale_aliases() {
  [ -d "$ALIAS_ROOT" ] || return 0
  for alias in "$ALIAS_ROOT"/usbdisk*; do
    [ -d "$alias" ] || continue
    name=${alias##*/}
    [ -d "/mnt/media_rw/$name" ] && continue
    umount "$alias" 2>/dev/null || true
    rmdir "$alias" 2>/dev/null || true
  done
}

mkdir -p "$STATE_DIR" || exit 1
chmod 0700 "$STATE_DIR" 2>/dev/null || true
start=$(date +%s 2>/dev/null || echo 0)
waited=0
while [ "$waited" -lt "$MAX_WAIT_SECONDS" ]; do
  found=0
  for raw in /mnt/media_rw/usbdisk*; do [ -d "$raw" ] && found=1 && break; done
  [ "$found" -eq 1 ] && break
  sleep 1
  waited=$((waited + 1))
done

clean_stale_aliases
mkdir -p "$ALIAS_ROOT" 2>/dev/null || true
: > "$TEMP" || exit 1

for raw in /mnt/media_rw/usbdisk*; do
  [ -d "$raw" ] || continue
  name=${raw##*/}
  valid_component "$name" || continue
  app="/storage/$name"
  alias="$ALIAS_ROOT/$name"
  selected=-
  state=raw_only

  if [ -d "$app" ] && [ -r "$app" ]; then
    selected="$app"
    state=app_candidate
  else
    mkdir -p "$alias" 2>/dev/null || true
    umount "$alias" 2>/dev/null || true
    if mount --bind "$raw" "$alias" 2>/dev/null; then
      mount -o remount,bind,ro "$alias" 2>/dev/null || true
      selected="$alias"
      state=alias_candidate
    fi
  fi

  sample=-
  if [ "$selected" != - ] && [ -d "$selected" ]; then
    sample=$(find "$selected" -xdev -maxdepth "$MAX_SAMPLE_DEPTH" -type f \( -iname '*.mp3' -o -iname '*.flac' -o -iname '*.m4a' -o -iname '*.wav' -o -iname '*.ogg' -o -iname '*.opus' -o -iname '*.aac' \) -print 2>/dev/null | head -n 1)
    [ -n "$sample" ] || sample=-
  fi
  generated=$(date +%s 2>/dev/null || echo "$start")
  printf '1\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' \
    "$generated" "$name" "$raw" "$app" "$alias" "$selected" "$state" "$sample" >> "$TEMP"
done

chmod 0600 "$TEMP" 2>/dev/null || true
mv -f "$TEMP" "$MANIFEST" || exit 1
log_msg "prepared manifest=$MANIFEST waited=${waited}s"
exit 0
