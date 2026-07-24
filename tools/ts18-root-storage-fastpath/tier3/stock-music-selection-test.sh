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
if ! pm path "$pkg" >/dev/null 2>&1; then
  echo "STOP: $pkg is not installed" >&2
  exit 3
fi
if [ "$1" = --restore ]; then
  pm enable --user 0 "$pkg" || exit 1
  echo "Restored $pkg for user 0"
  exit 0
fi

out="/data/local/tmp/auxio-stock-selection-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$out" || exit 1
pm path "$pkg" > "$out/pm-path.txt" 2>&1
dumpsys package "$pkg" > "$out/package-before.txt" 2>&1
cmd package resolve-activity --user 0 --brief -a android.intent.action.MAIN -c android.intent.category.LAUNCHER "$pkg" > "$out/resolve-before.txt" 2>&1
pm disable-user --user 0 "$pkg" || exit 1
printf 'Stock music disabled for a bounded manual validation window.\nRun immediately to rollback:\n  %s --restore\nEvidence: %s\n' "$0" "$out"
