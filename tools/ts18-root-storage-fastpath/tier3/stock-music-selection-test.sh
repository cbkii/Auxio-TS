#!/system/bin/sh
# External Tier 3 mutation test. Not called or packaged by the Auxio runtime APK.
case "${1:-}" in
  --disable-after-baseline|--restore) ;;
  *)
    echo "Usage: $0 --disable-after-baseline | --restore" >&2
    exit 2
    ;;
esac

pkg=com.tw.music
state_file=/data/local/tmp/auxio-stock-selection-state
if ! pm path "$pkg" >/dev/null 2>&1; then
  echo "STOP: $pkg is not installed" >&2
  exit 3
fi

if [ "$1" = --restore ]; then
  if [ ! -f "$state_file" ] || [ "$(cat "$state_file" 2>/dev/null)" != enabled ]; then
    echo "STOP: no enabled-before-test rollback marker; refusing to change $pkg" >&2
    exit 4
  fi
  pm enable --user 0 "$pkg" || exit 1
  rm -f "$state_file" || exit 1
  echo "Restored $pkg to its recorded enabled baseline for user 0"
  exit 0
fi

if pm list packages -d --user 0 "$pkg" 2>/dev/null | grep -Fxq "package:$pkg"; then
  echo "STOP: $pkg was already disabled for user 0; no mutation performed" >&2
  exit 4
fi
if [ -e "$state_file" ]; then
  echo "STOP: unresolved prior test marker exists at $state_file; restore or inspect first" >&2
  exit 4
fi

out="/data/local/tmp/auxio-stock-selection-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$out" || exit 1
pm path "$pkg" > "$out/pm-path.txt" 2>&1 || exit 1
dumpsys package "$pkg" > "$out/package-before.txt" 2>&1 || exit 1
cmd package resolve-activity --user 0 --brief -a android.intent.action.MAIN \
  -c android.intent.category.LAUNCHER "$pkg" > "$out/resolve-before.txt" 2>&1 || exit 1
printf '%s\n' enabled > "$state_file" || exit 1
if ! pm disable-user --user 0 "$pkg"; then
  rm -f "$state_file" || true
  exit 1
fi
printf 'Stock music disabled for a bounded manual validation window.\nRun immediately to rollback:\n  %s --restore\nEvidence: %s\nRollback marker: %s\n' \
  "$0" "$out" "$state_file"
