#!/usr/bin/env bash
# Collect TS18 launcher/media integration evidence after installing Auxio-TS.
# This is intentionally read-only. It does not change settings, packages, or services.

SCRIPT_NAME=${0##*/}
STAMP=$(date -u +%Y%m%d-%H%M%SZ 2>/dev/null || date +%Y%m%d-%H%M%S)
OUT_DIR="${1:-/sdcard/Download/ts18-launcher-media-integration-${STAMP}}"
WARNINGS=0

log() { printf '[INFO] %s\n' "$*" >&2; }
warn() { WARNINGS=$((WARNINGS + 1)); printf '[WARN] %s\n' "$*" >&2; }

run_capture() {
  local name=$1
  shift
  log "Capturing ${name}: $*"
  if "$@" > "${OUT_DIR}/${name}" 2> "${OUT_DIR}/${name}.stderr"; then
    :
  else
    warn "Capture failed: ${name}"
  fi
}

run_su_capture() {
  local name=$1
  shift
  if command -v su >/dev/null 2>&1; then
    log "Capturing ${name} via su: $*"
    if su -c "$*" > "${OUT_DIR}/${name}" 2> "${OUT_DIR}/${name}.stderr"; then
      return 0
    fi
    warn "su capture failed, trying normal shell: ${name}"
  else
    warn "su not available, trying normal shell: ${name}"
  fi
  # shellcheck disable=SC2068
  run_capture "$name" $@
}

main() {
  mkdir -p -- "$OUT_DIR" || {
    printf '[ERROR] Cannot create output directory: %s\n' "$OUT_DIR" >&2
    return 1
  }

  log "Output: $OUT_DIR"
  run_capture date.txt date -u
  run_capture id.txt id
  run_capture getprop.txt getprop
  run_capture ps_A.txt ps -A

  run_su_capture enabled_notification_listeners.txt settings get secure enabled_notification_listeners
  run_su_capture notification_listeners_cmd.txt cmd notification listeners
  run_su_capture dumpsys_media_session.txt dumpsys media_session
  run_su_capture dumpsys_notification_noredact.txt dumpsys notification --noredact
  run_su_capture package_dofun.txt dumpsys package com.dofun.variety
  run_su_capture package_twmedia.txt dumpsys package com.tw.media
  run_su_capture package_twmusic.txt dumpsys package com.tw.music
  run_su_capture package_auxio.txt dumpsys package org.oxycblt.auxio
  run_su_capture package_auxio_twmedia.txt dumpsys package com.tw.media

  run_su_capture logcat_all_threadtime.txt logcat -d -b all -v threadtime

  if command -v screencap >/dev/null 2>&1; then
    run_capture screencap.png screencap -p
  else
    warn 'screencap unavailable'
  fi

  cat > "${OUT_DIR}/README_CAPTURE_STEPS.txt" <<'README'
Capture intended sequence:
1. Clear logcat manually if needed: su -c 'logcat -c'
2. Start Auxio playback.
3. Press DoFun launcher media previous/play-next/next/progress controls.
4. Run this script immediately.
5. Repeat for stock com.tw.music and Spotify/VLC for comparison.
README

  if command -v toybox >/dev/null 2>&1 && toybox zip -h >/dev/null 2>&1; then
    (cd "$(dirname "$OUT_DIR")" && toybox zip -r "${OUT_DIR}.zip" "$(basename "$OUT_DIR")") >/dev/null 2>&1 || warn 'zip failed'
  elif command -v zip >/dev/null 2>&1; then
    (cd "$(dirname "$OUT_DIR")" && zip -r "${OUT_DIR}.zip" "$(basename "$OUT_DIR")") >/dev/null 2>&1 || warn 'zip failed'
  else
    warn 'zip not available; directory left uncompressed'
  fi

  printf '\n========================================\n' >&2
  printf 'RESULT: COMPLETED WITH WARNINGS=%d\n' "$WARNINGS" >&2
  printf 'Output: %s\n' "$OUT_DIR" >&2
  printf 'Zip:    %s.zip (if zip command was available)\n' "$OUT_DIR" >&2
  printf '========================================\n' >&2
}

main "$@"
