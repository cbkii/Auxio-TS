#!/system/bin/sh
# TS18 DoFun/TW Music/Auxio runtime validation collector v3.1
# Fixes unbound loop variable expansion in build property capture.
# Safe/read-only by default. Designed for TermOnePlus/TermOne without root or ADB.
# Fixes v1/v2 issues: uses Android user 0 by default and does not inherit TermOnePlus USER_ID app UID.

set -u

ANDROID_USER_ID="${ANDROID_USER_ID:-0}"
USER_ID="$ANDROID_USER_ID"
TAG="${1:-manual-v3}"
TS="$(date +%Y%m%d_%H%M%S 2>/dev/null || echo unknown_time)"
BASE="/sdcard/Download/ts18_dofun_runtime_validation_v3_${TS}"
TRY_DISABLE_STOCK="${TRY_DISABLE_STOCK:-0}"
TRY_UNINSTALL_STOCK="${TRY_UNINSTALL_STOCK:-0}"
RESTORE_STOCK="${RESTORE_STOCK:-0}"
INCLUDE_APKS="${INCLUDE_APKS:-0}"
COUNTDOWN_SECONDS="${COUNTDOWN_SECONDS:-10}"
TARGET_PKG="${TARGET_PKG:-com.tw.media}"

mkdir -p "$BASE" "$BASE/00_readme" "$BASE/identity" "$BASE/packages" "$BASE/runtime/phases" "$BASE/probes" "$BASE/manual" "$BASE/logs" "$BASE/apks"

log(){ echo "$*" | tee -a "$BASE/00_readme/RUN_LOG.txt"; }

cap(){
  out="$1"; shift
  mkdir -p "$(dirname "$out")"
  {
    echo "###############################################################################"
    echo "# ${out#$BASE/}"
    echo "###############################################################################"
    echo "Date: $(date 2>/dev/null)"
    echo "Run tag: $TAG"
    echo "Base: $BASE"
    echo "Shell identity:"
    id 2>&1 || true
    echo
    echo "Command:"
    printf '%s ' "$@"; echo
    echo
    "$@" 2>&1
    rc=$?
    echo
    echo "Exit code: $rc"
  } > "$out" 2>&1
}

cap_sh(){
  out="$1"; shift
  mkdir -p "$(dirname "$out")"
  cmd="$*"
  {
    echo "###############################################################################"
    echo "# ${out#$BASE/}"
    echo "###############################################################################"
    echo "Date: $(date 2>/dev/null)"
    echo "Run tag: $TAG"
    echo "Base: $BASE"
    echo "Shell identity:"
    id 2>&1 || true
    echo
    echo "Command:"
    echo "$cmd"
    echo
    sh -c "$cmd" 2>&1
    rc=$?
    echo
    echo "Exit code: $rc"
  } > "$out" 2>&1
}

ask(){
  prompt="$1"
  echo >> "$BASE/manual/manual_observations.md"
  echo "## $prompt" >> "$BASE/manual/manual_observations.md"
  printf '%s\n> ' "$prompt"
  IFS= read ans || ans=""
  echo "Observation: $ans" >> "$BASE/manual/manual_observations.md"
}

pause_manual(){
  msg="$1"
  echo
  echo "$msg"
  echo "Press Enter here after completing the step."
  IFS= read _dummy || true
  echo >> "$BASE/manual/manual_observations.md"
  echo "## Completed manual step" >> "$BASE/manual/manual_observations.md"
  echo "$msg" >> "$BASE/manual/manual_observations.md"
  echo "Completed at: $(date 2>/dev/null)" >> "$BASE/manual/manual_observations.md"
}

countdown_probe(){
  name="$1"; shift
  out="$BASE/probes/${name}.txt"
  echo
  echo "Probe '$name' will run in $COUNTDOWN_SECONDS seconds. Switch to DoFun launcher now if this probe targets visible widget state."
  i="$COUNTDOWN_SECONDS"
  while [ "$i" -gt 0 ]; do printf '%s ' "$i"; sleep 1; i=$((i-1)); done
  echo
  cap "$out" "$@"
  echo "Probe output: $out"
}

state(){
  phase="$1"
  dir="$BASE/runtime/phases/$phase"
  mkdir -p "$dir"
  log "[state] $phase"
  cap "$dir/media_session.txt" dumpsys media_session
  cap_sh "$dir/window_focus.txt" "dumpsys window windows 2>&1 | grep -Ei 'mCurrentFocus|mFocusedApp|mInputMethodTarget|Window #[0-9]|package=|ActivityRecord|com\\.dofun|com\\.tw\\.music|com\\.tw\\.media|com\\.tw\\.radio|com\\.navimods\\.radio|auxio|MusicActivity|RadioActivity' | head -n 260"
  cap_sh "$dir/audio_focus_filtered.txt" "dumpsys audio 2>&1 | grep -Ei 'focus|playback|player|uid|pid|client|com\\.tw|com\\.dofun|navimods|auxio|music|media|radio' | head -n 360"
  cap_sh "$dir/processes.txt" "ps -A 2>/dev/null | grep -Ei 'com\\.dofun\\.variety|com\\.tw\\.music|com\\.tw\\.media|com\\.tw\\.radio|com\\.navimods\\.radio|org\\.oxycblt|music|radio|launcher' || ps 2>/dev/null | grep -Ei 'com\\.dofun\\.variety|com\\.tw\\.music|com\\.tw\\.media|com\\.tw\\.radio|com\\.navimods\\.radio|org\\.oxycblt|music|radio|launcher' || true"
  cap_sh "$dir/logcat_filtered.txt" "logcat -d -v time 2>&1 | grep -Ei 'dofun|variety|tw\\.music|tw\\.media|tw\\.radio|navimods|auxio|oxycblt|MusicActivity|MusicService|MusicWidget|MediaSession|MediaBrowser|MediaButton|widget_music_progress|music_progress|music_duration|musicTitle|musicaArtist|msg_music|launcher|RemoteViews|AppWidget|SYSTEM_ALERT_WINDOW|overlay|float|exception|crash|fatal|denied|permission' | tail -n 1500"
}

cat > "$BASE/00_readme/README_FIRST.txt" <<EOF2
TS18 DoFun runtime validation v3

Run tag: $TAG
Android user id: $USER_ID
Base: $BASE

This collector is safe/read-only by default. It uses explicit --user $USER_ID for am/pm/cmd probes. Set ANDROID_USER_ID to override; do not use shell USER_ID because TermOnePlus may set USER_ID to its app UID.

Optional modes:
  INCLUDE_APKS=1          copy readable APKs for com.tw.media/com.tw.music/com.dofun.variety/com.navimods.radio/com.tw.radio
  TRY_DISABLE_STOCK=1    attempt reversible pm disable/hide/suspend of stock com.tw.music
  TRY_UNINSTALL_STOCK=1  with TRY_DISABLE_STOCK=1, also try pm uninstall -k --user $USER_ID com.tw.music
  RESTORE_STOCK=1        attempt pm enable/unhide/unsuspend/install-existing for com.tw.music
EOF2

cat > "$BASE/manual/manual_observations.md" <<EOF2
# Manual observations

Run tag: $TAG
Generated: $(date 2>/dev/null)
EOF2


log "Android user id for user-scoped probes: $USER_ID"
if [ "${USER_ID:-0}" != "0" ]; then
  log "WARNING: non-zero Android user id selected. On TS18 primary user is expected to be 0. Continue only if intentional."
fi

log "Collecting identity/package baseline..."
cap "$BASE/identity/id.txt" id
cap "$BASE/identity/uname.txt" uname -a
cap_sh "$BASE/identity/build_props_selected.txt" 'for p in ro.build.version.release ro.build.version.sdk ro.build.version.security_patch ro.build.version.incremental ro.build.description ro.build.fingerprint ro.product.model ro.product.device ro.product.board ro.hardware ro.boot.verifiedbootstate ro.boot.flash.locked init.svc.adbd; do printf '"'"'%-42s %s\n'"'"' "$p=" "$(getprop "$p" 2>/dev/null)"; done'
cap_sh "$BASE/identity/storage.txt" "df -h 2>/dev/null; echo; ls -lah /storage 2>/dev/null; echo; ls -lah /sdcard /sdcard/Download 2>/dev/null"

cap "$BASE/packages/cmd_package_help.txt" cmd package help
cap_sh "$BASE/packages/relevant_paths.txt" "for p in com.dofun.variety com.tw.music com.tw.media com.navimods.radio com.tw.radio com.tw.service com.android.systemui; do echo ===== \$p =====; pm path --user $USER_ID \$p 2>&1 || pm path \$p 2>&1; done"
cap_sh "$BASE/packages/relevant_install_state.txt" "pm list packages -f -u -U -i --user $USER_ID 2>&1 | grep -E 'com\\.dofun\\.variety|com\\.tw\\.music|com\\.tw\\.media|com\\.tw\\.radio|com\\.navimods\\.radio|com\\.tw\\.service|termone' || true"
cap "$BASE/packages/pm_list_packages_disabled.txt" pm list packages -d -u --user "$USER_ID"
cap "$BASE/packages/pm_list_packages_enabled.txt" pm list packages -e --user "$USER_ID"
cap_sh "$BASE/packages/query_components_user0.txt" "for a in com.tw.music.action.prev com.tw.music.action.pp com.tw.music.action.next com.tw.music.action.cmd com.tw.music.info com.tw.launcher.music_progress_duration com.android.launcher.widget_music_progress android.media.browse.MediaBrowserService androidx.media3.session.MediaSessionService; do echo ===== ACTION \$a =====; cmd package query-services --brief --components --user $USER_ID -a \$a 2>&1 || true; cmd package query-receivers --brief --components --user $USER_ID -a \$a 2>&1 || true; cmd package query-activities --brief --components --user $USER_ID -a \$a 2>&1 || true; done"
cap_sh "$BASE/packages/launcher_resolution_user0.txt" "cmd package resolve-activity --brief --components --user $USER_ID -a android.intent.action.MAIN -c android.intent.category.HOME 2>&1 || pm resolve-activity --brief --user $USER_ID -a android.intent.action.MAIN -c android.intent.category.HOME 2>&1 || true"

if [ "$INCLUDE_APKS" = "1" ]; then
  log "Copying readable APKs..."
  for p in com.tw.media com.tw.music com.dofun.variety com.navimods.radio com.tw.radio; do
    mkdir -p "$BASE/apks/$p"
    pm path --user "$USER_ID" "$p" 2>/dev/null | sed 's/^package://' | while IFS= read apk; do
      [ -n "$apk" ] || continue
      b="$(basename "$apk")"
      cp "$apk" "$BASE/apks/$p/$b" 2>> "$BASE/apks/COPY_LOG.txt" || echo "copy failed: $apk" >> "$BASE/apks/COPY_LOG.txt"
    done
  done
  (cd "$BASE/apks" && find . -type f -maxdepth 3 -print0 | xargs -0 sha256sum 2>/dev/null) > "$BASE/apks/hashes.txt" 2>&1 || true
fi

state initial

ask "Confirm visible launcher layout: DoFun launcher? fixed Music widget? fixed Radio widget? any custom widget area?"

pause_manual "Manual 1: Go to DoFun launcher. Tap the fixed Music widget/card once. Return to TermOnePlus."
state after_music_widget_tap
ask "Which app opened after tapping fixed DoFun Music widget: stock com.tw.music, Auxio com.tw.media, neither, or unclear? Did text/progress change?"

pause_manual "Manual 2: Open Auxio-TS com.tw.media and start playback. Go back to DoFun and press Music widget prev/play-pause/next. Return here."
state after_auxio_active_music_widget_controls
ask "Did fixed DoFun Music widget control Auxio-TS? Note previous/play-pause/next/progress/seek behaviour."

pause_manual "Manual 3: Open stock TW Music if possible and start playback. Go back to DoFun and press Music widget prev/play-pause/next. Return here."
state after_stock_active_music_widget_controls
ask "Did fixed DoFun Music widget control stock TW Music? Did metadata/progress update?"

pause_manual "Manual 4: Open NavRadio+ and start radio playback if possible. Go back to DoFun and test Radio widget and Music widget separately. Return here."
state after_navradio_widget_controls
ask "Which widget controlled NavRadio+: Radio, Music, both, neither, unclear? Did window/session evidence show NavRadio or stock com.tw.radio?"

log "Running delayed visible-state broadcasts with --user $USER_ID..."
echo "For each delayed probe, switch to DoFun launcher immediately and observe whether widget state changes."
countdown_probe "broadcast_music_info_implicit" am broadcast --user "$USER_ID" -a com.tw.music.info --es musicTitle TS18_PROBE_TITLE --es musicaArtist TS18_PROBE_ARTIST --es musicAlbum TS18_PROBE_ALBUM --es musicPath /storage/usbdisk0/TS18_PROBE.mp3
state after_broadcast_music_info_implicit
ask "Did DoFun Music widget show TS18_PROBE_TITLE / TS18_PROBE_ARTIST?"
countdown_probe "broadcast_music_info_target_dofun" am broadcast --user "$USER_ID" -a com.tw.music.info -p com.dofun.variety --es musicTitle TS18_PROBE_TITLE_2 --es musicaArtist TS18_PROBE_ARTIST_2 --es musicAlbum TS18_PROBE_ALBUM_2 --es musicPath /storage/usbdisk0/TS18_PROBE_2.mp3
state after_broadcast_music_info_target_dofun
ask "Did targeted DoFun metadata broadcast affect Music widget?"
countdown_probe "broadcast_progress_implicit" am broadcast --user "$USER_ID" -a com.tw.launcher.music_progress_duration --ei msg_music_progress 45000 --ei msg_music_duration 180000
state after_broadcast_progress_implicit
ask "Did progress/duration visibly change?"

log "Running Auxio service/receiver probes with --user $USER_ID..."
cap "$BASE/probes/auxio_service_action_pp.txt" am startservice --user "$USER_ID" -n "$TARGET_PKG/com.tw.music.MusicService" -a com.tw.music.action.pp
state after_auxio_service_action_pp
ask "Did Auxio play/pause/resume, crash, ignore, or show any UI after service action pp?"
cap "$BASE/probes/auxio_service_cmd_update.txt" am startservice --user "$USER_ID" -n "$TARGET_PKG/com.tw.music.MusicService" -a com.tw.music.action.cmd --es cmd update
state after_auxio_service_cmd_update
ask "Did Auxio update safely without starting playback? Any crash/toast?"
cap "$BASE/probes/auxio_service_cmd_prev.txt" am startservice --user "$USER_ID" -n "$TARGET_PKG/com.tw.music.MusicService" -a com.tw.music.action.cmd --es cmd prev
state after_auxio_service_cmd_prev
ask "Did Auxio handle cmd=prev safely?"
cap "$BASE/probes/auxio_service_cmd_next.txt" am startservice --user "$USER_ID" -n "$TARGET_PKG/com.tw.music.MusicService" -a com.tw.music.action.cmd --es cmd next
state after_auxio_service_cmd_next
ask "Did Auxio handle cmd=next safely?"
cap "$BASE/probes/auxio_receiver_action_pp.txt" am broadcast --user "$USER_ID" -a com.tw.music.action.pp -p "$TARGET_PKG"
state after_auxio_receiver_action_pp
ask "Did Auxio respond to targeted broadcast action pp?"
cap "$BASE/probes/auxio_launcher_seek.txt" am broadcast --user "$USER_ID" -a com.android.launcher.widget_music_progress -p "$TARGET_PKG" --ei music_progress 60000
state after_auxio_launcher_seek
ask "If Auxio was playing, did it seek to about 60 seconds? If not, did it fail safely?"

if [ "$TRY_DISABLE_STOCK" = "1" ]; then
  log "Attempting reversible stock com.tw.music disable/hide/suspend tests..."
  cap "$BASE/probes/stock_disable_user.txt" pm disable-user --user "$USER_ID" com.tw.music
  cap "$BASE/probes/stock_hide.txt" pm hide --user "$USER_ID" com.tw.music
  cap "$BASE/probes/stock_suspend.txt" pm suspend --user "$USER_ID" com.tw.music
  if [ "$TRY_UNINSTALL_STOCK" = "1" ]; then
    cap "$BASE/probes/stock_uninstall_user.txt" pm uninstall -k --user "$USER_ID" com.tw.music
  fi
  state after_stock_disable_attempts
  ask "After stock-disable attempts, does DoFun Music widget open Auxio, stock, neither, or unclear?"
fi

if [ "$RESTORE_STOCK" = "1" ]; then
  log "Attempting stock com.tw.music restore..."
  cap "$BASE/probes/stock_enable.txt" pm enable --user "$USER_ID" com.tw.music
  cap "$BASE/probes/stock_unhide.txt" pm unhide --user "$USER_ID" com.tw.music
  cap "$BASE/probes/stock_unsuspend.txt" pm unsuspend --user "$USER_ID" com.tw.music
  cap "$BASE/probes/stock_install_existing.txt" cmd package install-existing --user "$USER_ID" com.tw.music
  state after_stock_restore_attempts
fi

state final
cap "$BASE/logs/logcat_all_tail.txt" logcat -d -v time

log "Creating archive..."
cd /sdcard/Download || cd "$(dirname "$BASE")" || exit 0
name="$(basename "$BASE")"
if command -v zip >/dev/null 2>&1; then
  zip -qr "$name.zip" "$name"
  echo "/sdcard/Download/$name.zip"
else
  tar -czf "$name.tar.gz" "$name"
  echo "/sdcard/Download/$name.tar.gz"
fi
