#!/system/bin/sh
# Auxio-TS TS18 DoFun integration validation collector.
# Historical filename retained for repository continuity.
# Read-only by default. Does not disable/uninstall packages, clear data,
# change LSPosed scope, reboot, or write firmware.

SCRIPT_VERSION="4.0-direct-auxio"
ANDROID_USER_ID="${ANDROID_USER_ID:-0}"
AUXIO_PACKAGE="${AUXIO_PACKAGE:-com.tw.media}"
DOFUN_PACKAGE="${DOFUN_PACKAGE:-com.dofun.variety}"
STOCK_PACKAGE="${STOCK_PACKAGE:-com.tw.music}"
RUN_TAG="${RUN_TAG:-manual}"
RUN_CONTEXT="${RUN_CONTEXT:-normal-runtime}"
COUNTDOWN_SECONDS="${COUNTDOWN_SECONDS:-5}"
ALLOW_FORCE_STOP="${ALLOW_FORCE_STOP:-0}"
INCLUDE_APKS="${INCLUDE_APKS:-0}"
MANAGE_KILL_SWITCH="${MANAGE_KILL_SWITCH:-0}"
KILL_SWITCH="${KILL_SWITCH:-/storage/emulated/0/Auxio-TS/disable-lsposed-bridge}"
OUTPUT_ROOT="${OUTPUT_ROOT:-/storage/emulated/0/Download/Auxio-TS/bridge-validation}"
TS="$(date +%Y%m%d_%H%M%S 2>/dev/null)"
[ -n "$TS" ] || TS="unknown_time"
BASE="$OUTPUT_ROOT/ts18_auxio_dofun_${TS}_${RUN_TAG}"
RUN_LOG="$BASE/00_README/RUN_LOG.txt"
OBS="$BASE/manual/RESULTS.tsv"
SUMMARY="$BASE/00_README/FINAL_SUMMARY.txt"
ORIGINAL_KILL_SWITCH="unknown"

mkdir -p "$BASE/00_README" "$BASE/identity" "$BASE/packages" \
  "$BASE/runtime" "$BASE/logs" "$BASE/manual" "$BASE/apks" "$BASE/optional"
if [ $? -ne 0 ]; then
    echo "ERROR: cannot create output directory: $BASE" >&2
    exit 2
fi

log() {
    printf '%s\n' "$*" | tee -a "$RUN_LOG"
}

command_exists() {
    command -v "$1" >/dev/null 2>&1
}

run_with_timeout() {
    seconds="$1"
    shift
    if command_exists timeout; then
        timeout "$seconds" "$@"
        return $?
    fi
    "$@"
}

capture() {
    out="$1"
    shift
    mkdir -p "$(dirname "$out")"
    {
        echo "File: ${out#$BASE/}"
        echo "Date: $(date 2>/dev/null)"
        echo "Run tag: $RUN_TAG"
        echo "Run context: $RUN_CONTEXT"
        echo "Identity:"
        id 2>&1
        echo
        echo "Command:"
        printf '%s ' "$@"
        echo
        echo
        run_with_timeout 30 "$@" 2>&1
        rc=$?
        echo
        echo "Exit code: $rc"
    } >"$out" 2>&1
}

capture_sh() {
    out="$1"
    shift
    cmd="$*"
    mkdir -p "$(dirname "$out")"
    {
        echo "File: ${out#$BASE/}"
        echo "Date: $(date 2>/dev/null)"
        echo "Run tag: $RUN_TAG"
        echo "Run context: $RUN_CONTEXT"
        echo "Identity:"
        id 2>&1
        echo
        echo "Command:"
        echo "$cmd"
        echo
        if command_exists timeout; then
            timeout 30 sh -c "$cmd" 2>&1
        else
            sh -c "$cmd" 2>&1
        fi
        rc=$?
        echo
        echo "Exit code: $rc"
    } >"$out" 2>&1
}

pause_step() {
    echo
    echo "$1"
    echo "Press Enter after completing the step."
    IFS= read _answer || true
}

record_result() {
    id="$1"
    description="$2"
    echo
    echo "$description"
    echo "Enter PASS, FAIL, SKIPPED or UNCLEAR, then add a short note if useful."
    printf '> '
    IFS= read answer || answer="UNCLEAR"
    [ -n "$answer" ] || answer="UNCLEAR"
    status="$(printf '%s' "$answer" | awk '{print toupper($1)}')"
    case "$status" in
        PASS|FAIL|SKIPPED|UNCLEAR) ;;
        *)
            status="UNCLEAR"
            answer="UNCLEAR - unrecognised input: $answer"
            ;;
    esac
    note="$(printf '%s' "$answer" | sed 's/^[^ ]*[ ]*//')"
    printf '%s\t%s\t%s\t%s\n' "$id" "$status" "$description" "$note" >>"$OBS"
}

countdown() {
    n="$COUNTDOWN_SECONDS"
    while [ "$n" -gt 0 ] 2>/dev/null; do
        printf '%s ' "$n"
        sleep 1
        n=$((n - 1))
    done
    echo
}

package_path() {
    pkg="$1"
    pm path --user "$ANDROID_USER_ID" "$pkg" 2>/dev/null || pm path "$pkg" 2>/dev/null
}

state_capture() {
    phase="$1"
    dir="$BASE/runtime/$phase"
    mkdir -p "$dir"
    log "Capturing state: $phase"

    capture "$dir/media_session.txt" dumpsys media_session
    capture "$dir/audio.txt" dumpsys audio
    capture "$dir/notification.txt" dumpsys notification
    capture "$dir/activity_services.txt" dumpsys activity services
    capture "$dir/activity_activities.txt" dumpsys activity activities

    capture_sh "$dir/processes_relevant.txt" \
      "ps -A -o USER,PID,PPID,NAME,ARGS 2>/dev/null | grep -E '$AUXIO_PACKAGE|$DOFUN_PACKAGE|$STOCK_PACKAGE|org\\.oxycblt\\.auxio' || ps -A 2>/dev/null | grep -E '$AUXIO_PACKAGE|$DOFUN_PACKAGE|$STOCK_PACKAGE|org\\.oxycblt\\.auxio' || true"

    capture_sh "$dir/media_session_relevant.txt" \
      "dumpsys media_session 2>&1 | grep -i -A40 -B8 '$AUXIO_PACKAGE\\|$DOFUN_PACKAGE\\|$STOCK_PACKAGE\\|auxio' | head -n 900"

    capture_sh "$dir/audio_relevant.txt" \
      "dumpsys audio 2>&1 | grep -i -A10 -B5 'focus\\|player\\|playback\\|$AUXIO_PACKAGE\\|$DOFUN_PACKAGE\\|$STOCK_PACKAGE\\|auxio' | head -n 1200"

    capture_sh "$dir/services_relevant.txt" \
      "dumpsys activity services 2>&1 | grep -i -A15 -B5 '$AUXIO_PACKAGE\\|$DOFUN_PACKAGE\\|$STOCK_PACKAGE\\|MusicService\\|AuxioService' | head -n 1000"

    capture_sh "$dir/activities_relevant.txt" \
      "dumpsys activity activities 2>&1 | grep -i -A15 -B5 '$AUXIO_PACKAGE\\|$DOFUN_PACKAGE\\|$STOCK_PACKAGE\\|MusicActivity\\|mResumedActivity' | head -n 1000"

    capture_sh "$dir/notification_relevant.txt" \
      "dumpsys notification 2>&1 | grep -i -A20 -B5 '$AUXIO_PACKAGE\\|$DOFUN_PACKAGE\\|$STOCK_PACKAGE\\|auxio\\|music' | head -n 1000"

    capture_sh "$dir/logcat_relevant.txt" \
      "logcat -d -v threadtime 2>&1 | grep -iE 'Auxio|ts18bridge|Topway|DoFun|variety|tw\\.music|tw\\.media|MediaSession|MediaBrowser|MusicActivity|MusicService|music_progress|msg_music|metachanged|playstatechanged|FATAL EXCEPTION|ANR|SecurityException' | tail -n 2500"
}

copy_package_apks() {
    pkg="$1"
    target="$BASE/apks/$pkg"
    mkdir -p "$target"
    package_path "$pkg" | sed 's/^package://' | while IFS= read apk; do
        [ -n "$apk" ] || continue
        name="$(basename "$apk")"
        cp "$apk" "$target/$name" 2>>"$BASE/apks/COPY_ERRORS.txt"
        rc=$?
        if [ $rc -ne 0 ]; then
            echo "copy failed rc=$rc: $apk" >>"$BASE/apks/COPY_ERRORS.txt"
        fi
    done
}

optional_force_stop() {
    pkg="$1"
    reason="$2"
    if [ "$ALLOW_FORCE_STOP" != "1" ]; then
        log "SKIP force-stop $pkg ($reason): ALLOW_FORCE_STOP is not 1"
        return 1
    fi
    log "Force-stopping $pkg for bounded restart test: $reason"
    am force-stop --user "$ANDROID_USER_ID" "$pkg" >>"$BASE/optional/FORCE_STOP_LOG.txt" 2>&1
    return $?
}

record_kill_switch_state() {
    if [ -e "$KILL_SWITCH" ]; then
        ORIGINAL_KILL_SWITCH="present"
    else
        ORIGINAL_KILL_SWITCH="absent"
    fi
    echo "$ORIGINAL_KILL_SWITCH" >"$BASE/identity/original_kill_switch_state.txt"
}

restore_kill_switch() {
    [ "$MANAGE_KILL_SWITCH" = "1" ] || return 0
    case "$ORIGINAL_KILL_SWITCH" in
        present)
            mkdir -p "$(dirname "$KILL_SWITCH")" 2>/dev/null
            : >"$KILL_SWITCH" 2>/dev/null
            ;;
        absent)
            rm -f "$KILL_SWITCH" 2>/dev/null
            ;;
    esac
}

cleanup() {
    restore_kill_switch
}

trap cleanup EXIT HUP INT TERM

if [ "$(id -u 2>/dev/null)" != "0" ]; then
    log "ERROR: this collector needs root for complete dumpsys/process evidence."
    log "Run it with: su -c '/system/bin/sh /path/to/collect-ts18-lsposed-bridge-validation.sh'"
    exit 3
fi

printf 'test_id\tstatus\tdescription\tnote\n' >"$OBS"
record_kill_switch_state

cat >"$BASE/00_README/README_FIRST.txt" <<EOF2
Auxio-TS TS18 DoFun integration validation collector
Version: $SCRIPT_VERSION
Generated: $(date 2>/dev/null)
Run tag: $RUN_TAG
Run context: $RUN_CONTEXT
Android user: $ANDROID_USER_ID
Auxio package: $AUXIO_PACKAGE
DoFun package: $DOFUN_PACKAGE
Stock package observed only for conflict detection: $STOCK_PACKAGE
Output: $BASE

Read-only by default. ALLOW_FORCE_STOP=1 enables bounded process restart tests.
MANAGE_KILL_SWITCH=1 allows temporary marker changes and restores the original marker state on exit.
INCLUDE_APKS=1 copies readable APKs into the evidence archive.

For cold-boot or ACC evidence, run this script again after the lifecycle boundary with a distinct RUN_CONTEXT, for example:
RUN_CONTEXT=cold-boot RUN_TAG=rc1-coldboot
RUN_CONTEXT=acc-wake RUN_TAG=rc1-accwake
EOF2

log "Starting collector version $SCRIPT_VERSION"
log "Output: $BASE"

capture "$BASE/identity/id.txt" id
capture "$BASE/identity/uname.txt" uname -a
capture_sh "$BASE/identity/build_properties.txt" \
  "for p in ro.build.version.release ro.build.version.sdk ro.build.version.security_patch ro.build.display.id ro.build.fingerprint ro.product.model ro.product.device ro.product.board ro.hardware ro.boot.verifiedbootstate ro.boot.flash.locked; do printf '%-42s %s\\n' \"\$p=\" \"\$(getprop \$p 2>/dev/null)\"; done"
capture "$BASE/identity/boot_id.txt" cat /proc/sys/kernel/random/boot_id
capture_sh "$BASE/identity/magisk_lsposed.txt" \
  "magisk -V 2>&1; magisk -v 2>&1; echo; find /data/adb -maxdepth 5 -type f \( -name 'scope.list' -o -name 'module.prop' -o -name 'modules.list' \) -print 2>/dev/null | head -n 200"
capture_sh "$BASE/identity/kill_switch.txt" \
  "ls -ld '$KILL_SWITCH' 2>&1; echo original_state=$ORIGINAL_KILL_SWITCH"

capture_sh "$BASE/packages/package_paths.txt" \
  "for p in '$AUXIO_PACKAGE' '$DOFUN_PACKAGE' '$STOCK_PACKAGE'; do echo ===== \$p =====; pm path --user $ANDROID_USER_ID \$p 2>&1 || pm path \$p 2>&1; done"
capture_sh "$BASE/packages/package_list.txt" \
  "pm list packages -f -u -U -i --user $ANDROID_USER_ID 2>&1 | grep -E '$AUXIO_PACKAGE|$DOFUN_PACKAGE|$STOCK_PACKAGE|org\\.oxycblt\\.auxio' || true"
capture_sh "$BASE/packages/component_resolution.txt" \
  "cmd package resolve-activity --brief --components --user $ANDROID_USER_ID -n $AUXIO_PACKAGE/com.tw.music.MusicActivity 2>&1; cmd package resolve-activity --brief --components --user $ANDROID_USER_ID -n $STOCK_PACKAGE/com.tw.music.MusicActivity 2>&1; cmd package query-services --brief --components --user $ANDROID_USER_ID -a android.media.browse.MediaBrowserService 2>&1 | grep -E '$AUXIO_PACKAGE|$STOCK_PACKAGE|auxio' || true"
capture_sh "$BASE/packages/dumpsys_auxio.txt" \
  "dumpsys package $AUXIO_PACKAGE 2>&1 | head -n 1800"
capture_sh "$BASE/packages/dumpsys_dofun.txt" \
  "dumpsys package $DOFUN_PACKAGE 2>&1 | head -n 1800"
capture_sh "$BASE/packages/dumpsys_stock.txt" \
  "dumpsys package $STOCK_PACKAGE 2>&1 | head -n 1800"

if [ "$INCLUDE_APKS" = "1" ]; then
    log "Copying readable relevant APKs"
    copy_package_apks "$AUXIO_PACKAGE"
    copy_package_apks "$DOFUN_PACKAGE"
    copy_package_apks "$STOCK_PACKAGE"
    capture_sh "$BASE/apks/SHA256SUMS.txt" \
      "find '$BASE/apks' -type f ! -name 'SHA256SUMS.txt' -print0 2>/dev/null | xargs -0 sha256sum 2>/dev/null"
fi

state_capture "00_initial"

pause_step "Manual test 1: Return to DoFun and tap the fixed Music card once. Then return here."
state_capture "01_after_fixed_music_card"
record_result "fixed-card-launch" \
  "The fixed DoFun Music card opened Auxio-TS com.tw.media, without an unwanted stock player UI or crash."
record_result "stock-not-authority-after-launch" \
  "After the launch, genuine com.tw.music was not the active playback/session/focus authority."

pause_step "Manual test 2: In Auxio, start a known track. Return to DoFun and wait for metadata/progress, then return here."
state_capture "02_auxio_playing_metadata"
record_result "auxio-playback" \
  "Auxio played the selected track and remained the sole audible playback authority."
record_result "metadata-progress" \
  "DoFun displayed the expected title/artist and changing progress/duration without stale stock metadata."

pause_step "Manual test 3: From DoFun, press PREVIOUS once. Wait briefly, then return here."
state_capture "03_previous"
record_result "previous-exactly-once" \
  "PREVIOUS changed Auxio exactly once, with no duplicate skip and no stock reaction."

pause_step "Manual test 4: From DoFun, press NEXT once. Wait briefly, then return here."
state_capture "04_next"
record_result "next-exactly-once" \
  "NEXT changed Auxio exactly once, with no duplicate skip and no stock reaction."

pause_step "Manual test 5: From DoFun, press PLAY/PAUSE once, wait, then press it once again. Return here."
state_capture "05_play_pause"
record_result "play-pause-exactly-once" \
  "Each PLAY/PAUSE press changed Auxio exactly once and restored the expected state."

pause_step "Manual test 6: If DoFun exposes seek/progress interaction, seek to a visibly different point. Otherwise skip. Return here."
state_capture "06_seek"
record_result "seek" \
  "DoFun seek moved Auxio to the expected approximate millisecond position without crash or duplicate jumps."

record_result "single-media-session" \
  "Evidence and observation show one active music MediaSession authority for Auxio."
record_result "single-audio-focus" \
  "Evidence and observation show one music audio-focus/playback authority."
record_result "single-notification" \
  "Only one Auxio playback notification authority was active."
record_result "stock-process-conflict" \
  "Stock com.tw.music did not unexpectedly start, play, publish stale state or consume commands during direct Auxio use."

if optional_force_stop "$AUXIO_PACKAGE" "Auxio reconnect test"; then
    sleep 2
    pause_step "Auxio was force-stopped. Tap the DoFun Music card, start/resume playback, test one NEXT action, then return here."
    state_capture "07_after_auxio_restart"
    record_result "auxio-restart" \
      "After Auxio process restart, the card, control path and complete metadata/progress state recovered without duplicate authority."
else
    record_result "auxio-restart" \
      "Auxio restart test was not automated. Mark SKIPPED unless you performed an equivalent safe restart manually."
fi

if optional_force_stop "$DOFUN_PACKAGE" "DoFun reconnect test"; then
    sleep 2
    am start --user "$ANDROID_USER_ID" -a android.intent.action.MAIN -c android.intent.category.HOME >>"$BASE/optional/FORCE_STOP_LOG.txt" 2>&1
    pause_step "DoFun was restarted. Open the fixed Music card, test PLAY/PAUSE and inspect metadata/progress, then return here."
    state_capture "08_after_dofun_restart"
    record_result "dofun-restart" \
      "After DoFun restart, direct Auxio launch, controls and state publication recovered without stock takeover."
else
    record_result "dofun-restart" \
      "DoFun restart test was not automated. Mark SKIPPED unless you performed an equivalent safe restart manually."
fi

pause_step "Manual test 9: If the current track is on removable USB, safely remove and reinsert the device, then recover playback as appropriate. Otherwise skip. Return here."
state_capture "09_usb_reinsert"
record_result "usb-reinsert" \
  "USB removal/reinsert failed safely and recovered without stale metadata, unavailable queue corruption or stock takeover."

if [ "$MANAGE_KILL_SWITCH" = "1" ]; then
    mkdir -p "$(dirname "$KILL_SWITCH")" 2>/dev/null
    : >"$KILL_SWITCH" 2>/dev/null
    ks_rc=$?
    if [ $ks_rc -eq 0 ]; then
        log "Temporary kill switch enabled: $KILL_SWITCH"
        pause_step "If an LSPosed adapter is part of this build, restart its target process safely, then test a new DoFun command. If no adapter is shipped, mark this test SKIPPED. Return here."
        state_capture "10_kill_switch_enabled"
        record_result "adapter-kill-switch" \
          "The optional adapter kill switch stopped new hook forwarding/suppression while preserving safe ordinary behaviour."
    else
        record_result "adapter-kill-switch" \
          "Kill-switch marker could not be created; inspect optional/FORCE_STOP_LOG.txt and mark UNCLEAR or SKIPPED."
    fi
else
    record_result "adapter-kill-switch" \
      "Kill-switch automation disabled. Mark SKIPPED when the selected architecture has no LSPosed adapter."
fi

record_result "cold-boot" \
  "This evidence run represents a real cold boot/full power restoration and direct Auxio integration recovered. Use RUN_CONTEXT=cold-boot on a dedicated run; otherwise SKIPPED."
record_result "acc-wake" \
  "This evidence run represents a real ACC sleep/wake boundary and direct Auxio integration recovered. Use RUN_CONTEXT=acc-wake on a dedicated run; otherwise SKIPPED."

state_capture "11_final"

{
    echo "Auxio-TS TS18 DoFun validation summary"
    echo "Version: $SCRIPT_VERSION"
    echo "Generated: $(date 2>/dev/null)"
    echo "Run tag: $RUN_TAG"
    echo "Run context: $RUN_CONTEXT"
    echo "Output: $BASE"
    echo
    awk -F '\t' 'NR>1 {count[$2]++} END {print "PASS: " count["PASS"]; print "FAIL: " count["FAIL"]; print "SKIPPED: " count["SKIPPED"]; print "UNCLEAR: " count["UNCLEAR"]}' "$OBS"
    echo
    echo "Detailed results: manual/RESULTS.tsv"
    echo "A missing or unrun physical boundary is not a PASS."
} >"$SUMMARY"

cat "$SUMMARY" | tee -a "$RUN_LOG"

ARCHIVE="${BASE}.tar.gz"
if command_exists tar; then
    parent="$(dirname "$BASE")"
    leaf="$(basename "$BASE")"
    (cd "$parent" && tar -czf "$ARCHIVE" "$leaf") >>"$RUN_LOG" 2>&1
    arc_rc=$?
    if [ $arc_rc -eq 0 ]; then
        log "Archive: $ARCHIVE"
        if command_exists sha256sum; then
            sha256sum "$ARCHIVE" | tee "$ARCHIVE.sha256" | tee -a "$RUN_LOG"
        fi
    else
        log "WARNING: archive creation failed with exit code $arc_rc; directory remains at $BASE"
    fi
else
    log "WARNING: tar not found; evidence directory remains at $BASE"
fi

log "Collector complete."
exit 0
