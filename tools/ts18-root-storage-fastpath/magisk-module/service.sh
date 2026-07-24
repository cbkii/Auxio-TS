#!/system/bin/sh
# Bounded Magisk late-start preparation for the exact TS18 storage layout.
# Module-root service.sh is Magisk's supported late_start service entrypoint.

STATE_DIR=/data/adb/auxio-ts-root
MANIFEST="$STATE_DIR/volumes.tsv"
TEMP="$STATE_DIR/volumes.tsv.tmp.$$"
LOCK_DIR="$STATE_DIR/prepare.lock"
ALIAS_ROOT=/storage/auxio-root
BOOT_WAIT_SECONDS=20
ON_DEMAND_WAIT_SECONDS=3

log_msg() { log -t AuxioRootStorage "$*" 2>/dev/null || true; }

valid_component() {
  case "$1" in
    usbdisk*) suffix=${1#usbdisk} ;;
    *) return 1 ;;
  esac
  case "$suffix" in
    ''|*[!0-9]*) return 1 ;;
    *) return 0 ;;
  esac
}

release_lock() {
  rmdir "$LOCK_DIR" 2>/dev/null || true
  rm -f "$TEMP" 2>/dev/null || true
}

acquire_lock() {
  mkdir -p "$STATE_DIR" || return 1
  chmod 0700 "$STATE_DIR" 2>/dev/null || true
  if mkdir "$LOCK_DIR" 2>/dev/null; then
    trap release_lock EXIT HUP INT TERM
    return 0
  fi
  lock_mtime=$(stat -c %Y "$LOCK_DIR" 2>/dev/null || echo 0)
  now=$(date +%s 2>/dev/null || echo 0)
  if [ "$lock_mtime" -gt 0 ] && [ $((now - lock_mtime)) -gt 60 ]; then
    rmdir "$LOCK_DIR" 2>/dev/null || true
    if mkdir "$LOCK_DIR" 2>/dev/null; then
      trap release_lock EXIT HUP INT TERM
      return 0
    fi
  fi
  log_msg "preparation already running"
  return 1
}

clean_stale_aliases() {
  [ -d "$ALIAS_ROOT" ] || return 0
  for alias_path in "$ALIAS_ROOT"/usbdisk*; do
    [ -d "$alias_path" ] || continue
    name=${alias_path##*/}
    valid_component "$name" || continue
    [ -d "/mnt/media_rw/$name" ] && continue
    umount "$alias_path" 2>/dev/null || true
    rmdir "$alias_path" 2>/dev/null || true
  done
}

prepare_manifest() {
  max_wait=$1
  waited=0
  while [ "$waited" -lt "$max_wait" ]; do
    found=0
    for raw_path in /mnt/media_rw/usbdisk*; do
      [ -d "$raw_path" ] && found=1 && break
    done
    [ "$found" -eq 1 ] && break
    sleep 1
    waited=$((waited + 1))
  done

  clean_stale_aliases
  mkdir -p "$ALIAS_ROOT" 2>/dev/null || true
  chmod 0755 "$ALIAS_ROOT" 2>/dev/null || true
  : > "$TEMP" || return 1

  for raw_path in /mnt/media_rw/usbdisk*; do
    [ -d "$raw_path" ] || continue
    name=${raw_path##*/}
    valid_component "$name" || continue
    app_path="/storage/$name"
    alias_path="$ALIAS_ROOT/$name"
    selected=-
    state=raw_only

    if [ -d "$app_path" ] && [ -r "$app_path" ]; then
      selected="$app_path"
      state=app_candidate
    else
      mkdir -p "$alias_path" 2>/dev/null || true
      chmod 0755 "$alias_path" 2>/dev/null || true
      umount "$alias_path" 2>/dev/null || true
      if mount --bind "$raw_path" "$alias_path" 2>/dev/null; then
        if mount -o remount,bind,ro "$raw_path" "$alias_path" 2>/dev/null ||
          mount -o remount,bind,ro "$alias_path" 2>/dev/null; then
          selected="$alias_path"
          state=alias_candidate
        else
          log_msg "read-only remount failed for $alias_path"
          umount "$alias_path" 2>/dev/null || true
          rmdir "$alias_path" 2>/dev/null || true
        fi
      fi
    fi

    # The helper remains volume-only. A root-side file open cannot prove app playback authority;
    # Auxio performs bounded representative-media validation later as the app UID.
    sample=-
    generated=$(date +%s 2>/dev/null || echo 0)
    printf '1\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' \
      "$generated" "$name" "$raw_path" "$app_path" "$alias_path" \
      "$selected" "$state" "$sample" >> "$TEMP"
  done

  chmod 0600 "$TEMP" 2>/dev/null || true
  mv -f "$TEMP" "$MANIFEST" || return 1
  log_msg "prepared manifest=$MANIFEST waited=${waited}s"
  return 0
}

case "${1:-}" in
  --once) wait_seconds=$ON_DEMAND_WAIT_SECONDS ;;
  ''|--boot) wait_seconds=$BOOT_WAIT_SECONDS ;;
  *)
    echo "Usage: $0 [--boot|--once]" >&2
    exit 2
    ;;
esac

acquire_lock || exit 0
prepare_manifest "$wait_seconds" || exit 1
exit 0
