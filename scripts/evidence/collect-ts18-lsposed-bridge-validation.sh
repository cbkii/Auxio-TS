#!/system/bin/sh
# Auxio-TS LSPosed bridge physical validation collector
# Safe/read-only by default. Exact target: Topway TS18 Android 10/API 29.
# Output: /storage/emulated/0/Download/Auxio-TS/bridge-validation/<timestamp>/
#
# Default mode never disables/uninstalls packages, clears data, writes firmware,
# changes LSPosed scope, reboots, or force-stops protected packages.
# Optional reversible actions require explicit environment flags:
#   MANAGE_KILL_SWITCH=1   create/remove the documented marker during tests;
#                          original marker state is restored on exit.
#   ALLOW_FORCE_STOP=1     allow explicit process-restart checkpoints only.
#   INCLUDE_APKS=1         copy readable package APKs into the local evidence bundle.
#
# Run as root, for example from Termux:
#   su -c '/system/bin/sh /storage/emulated/0/Download/collect-ts18-lsposed-bridge-validation.sh'
#
# Full controlled mode:
#   su -c 'MANAGE_KILL_SWITCH=1 ALLOW_FORCE_STOP=1 INCLUDE_APKS=1 \
#     /system/bin/sh /storage/emulated/0/Download/collect-ts18-lsposed-bridge-validation.sh'

set -u

SCRIPT_VERSION="1.0"
ANDROID_USER_ID="${ANDROID_USER_ID:-0}"
MANAGE_KILL_SWITCH="${MANAGE_KILL_SWITCH:-0}"
ALLOW_FORCE_STOP="${ALLOW_FORCE_STOP:-0}"
INCLUDE_APKS="${INCLUDE_APKS:-0}"
TARGET_PACKAGE="${TARGET_PACKAGE:-auto}"
BRIDGE_PACKAGE="${BRIDGE_PACKAGE:-auto}"
DOFUN_PACKAGE="${DOFUN_PACKAGE:-com.dofun.variety}"
STOCK_PACKAGE="${STOCK_PACKAGE:-com.tw.music}"
MAX_LOGCAT_LINES="${MAX_LOGCAT_LINES:-3000}"
MAX_LSPOSED_LINES="${MAX_LSPOSED_LINES:-3000}"

STAMP="$(date -u +%Y%m%d-%H%M%SZ 2>/dev/null || date +%Y%m%d-%H%M%S 2>/dev/null || echo unknown-time)"
BASE_ROOT="/storage/emulated/0/Download/Auxio-TS/bridge-validation"
BASE="${BASE_ROOT}/${STAMP}"
KILL_SWITCH="/storage/emulated/0/Auxio-TS/disable-lsposed-bridge"
OBSERVATIONS="${BASE}/manual/operator-observations.tsv"
RUN_LOG="${BASE}/00-run-log.txt"
ORIGINAL_KILL_SWITCH="unknown"
CLEANUP_DONE=0

usage() {
    cat <<USAGE
Auxio-TS LSPosed bridge physical validation collector v${SCRIPT_VERSION}

Environment options:
  ANDROID_USER_ID=0
  TARGET_PACKAGE=auto|com.tw.media|com.tw.media.debug
  BRIDGE_PACKAGE=auto|org.oxycblt.auxio.ts18bridge|org.oxycblt.auxio.ts18bridge.debug
  MANAGE_KILL_SWITCH=0|1
  ALLOW_FORCE_STOP=0|1
  INCLUDE_APKS=0|1
  MAX_LOGCAT_LINES=3000
  MAX_LSPOSED_LINES=3000

The script is read-only by default. It requires root for complete package and
LSPosed evidence. Optional actions are reversible and require explicit flags.
USAGE
}

case "${1:-}" in
    -h|--help)
        usage
        exit 0
        ;;
esac

mkdir -p "$BASE" "$BASE/identity" "$BASE/packages" "$BASE/runtime" \
    "$BASE/manual" "$BASE/logs" "$BASE/lsposed" "$BASE/apks" "$BASE/summary"

log() {
    printf '%s %s\n' "$(date '+%Y-%m-%d %H:%M:%S' 2>/dev/null || echo time-unknown)" "$*" |
        tee -a "$RUN_LOG"
}

command_exists() {
    command -v "$1" >/dev/null 2>&1
}

safe_name() {
    printf '%s' "$1" | tr '/ :\t' '____' | tr -cd 'A-Za-z0-9._-'
}

capture() {
    CAP_OUT="$1"
    shift
    mkdir -p "$(dirname "$CAP_OUT")"
    {
        echo "###############################################################################"
        echo "# ${CAP_OUT#$BASE/}"
        echo "###############################################################################"
        echo "Captured: $(date 2>/dev/null)"
        echo "UTC: $(date -u 2>/dev/null)"
        echo "Command: $*"
        echo
        "$@" 2>&1
        CAP_RC=$?
        echo
        echo "Exit code: $CAP_RC"
    } >"$CAP_OUT" 2>&1
    return 0
}

capture_sh() {
    CAP_OUT="$1"
    shift
    CAP_CMD="$*"
    mkdir -p "$(dirname "$CAP_OUT")"
    {
        echo "###############################################################################"
        echo "# ${CAP_OUT#$BASE/}"
        echo "###############################################################################"
        echo "Captured: $(date 2>/dev/null)"
        echo "UTC: $(date -u 2>/dev/null)"
        echo "Command: $CAP_CMD"
        echo
        /system/bin/sh -c "$CAP_CMD" 2>&1
        CAP_RC=$?
        echo
        echo "Exit code: $CAP_RC"
    } >"$CAP_OUT" 2>&1
    return 0
}

package_installed() {
    pm path --user "$ANDROID_USER_ID" "$1" >/dev/null 2>&1 || pm path "$1" >/dev/null 2>&1
}

package_base_apk() {
    pm path --user "$ANDROID_USER_ID" "$1" 2>/dev/null |
        sed -n 's/^package://p' | sed -n '1p'
}

resolve_target_package() {
    if [ "$TARGET_PACKAGE" != "auto" ]; then
        package_installed "$TARGET_PACKAGE" || {
            log "STOP: requested target package is not installed: $TARGET_PACKAGE"
            return 1
        }
        return 0
    fi

    RTP_RELEASE=0
    RTP_DEBUG=0
    package_installed com.tw.media && RTP_RELEASE=1
    package_installed com.tw.media.debug && RTP_DEBUG=1

    if [ "$RTP_RELEASE" -eq 1 ] && [ "$RTP_DEBUG" -eq 1 ]; then
        log "STOP: both com.tw.media and com.tw.media.debug are installed. Set TARGET_PACKAGE explicitly."
        return 1
    fi
    if [ "$RTP_RELEASE" -eq 1 ]; then
        TARGET_PACKAGE="com.tw.media"
        return 0
    fi
    if [ "$RTP_DEBUG" -eq 1 ]; then
        TARGET_PACKAGE="com.tw.media.debug"
        return 0
    fi
    log "STOP: neither com.tw.media nor com.tw.media.debug is installed."
    return 1
}

resolve_bridge_package() {
    if [ "$BRIDGE_PACKAGE" != "auto" ]; then
        package_installed "$BRIDGE_PACKAGE" || {
            log "STOP: requested bridge package is not installed: $BRIDGE_PACKAGE"
            return 1
        }
        return 0
    fi

    RBP_RELEASE=0
    RBP_DEBUG=0
    package_installed org.oxycblt.auxio.ts18bridge && RBP_RELEASE=1
    package_installed org.oxycblt.auxio.ts18bridge.debug && RBP_DEBUG=1

    if [ "$RBP_RELEASE" -eq 1 ] && [ "$RBP_DEBUG" -eq 1 ]; then
        log "STOP: both bridge release and debug packages are installed. Set BRIDGE_PACKAGE explicitly."
        return 1
    fi
    if [ "$RBP_RELEASE" -eq 1 ]; then
        BRIDGE_PACKAGE="org.oxycblt.auxio.ts18bridge"
        return 0
    fi
    if [ "$RBP_DEBUG" -eq 1 ]; then
        BRIDGE_PACKAGE="org.oxycblt.auxio.ts18bridge.debug"
        return 0
    fi
    log "STOP: no Auxio-TS LSPosed bridge package is installed."
    return 1
}

record_original_kill_switch() {
    if [ -f "$KILL_SWITCH" ]; then
        ORIGINAL_KILL_SWITCH="present"
    elif [ -e "$KILL_SWITCH" ]; then
        ORIGINAL_KILL_SWITCH="non-regular-entry"
    else
        ORIGINAL_KILL_SWITCH="absent"
    fi
    log "Original kill-switch state: $ORIGINAL_KILL_SWITCH"
}

restore_kill_switch() {
    [ "$MANAGE_KILL_SWITCH" = "1" ] || return 0
    case "$ORIGINAL_KILL_SWITCH" in
        present)
            mkdir -p "$(dirname "$KILL_SWITCH")" 2>/dev/null || true
            : >"$KILL_SWITCH" 2>/dev/null || log "WARNING: could not restore present kill switch"
            ;;
        absent)
            rm -f "$KILL_SWITCH" 2>/dev/null || log "WARNING: could not restore absent kill switch"
            ;;
        non-regular-entry|unknown)
            log "WARNING: original kill-switch state was $ORIGINAL_KILL_SWITCH; no automatic restoration performed"
            ;;
    esac
}

cleanup() {
    [ "$CLEANUP_DONE" -eq 0 ] || return 0
    CLEANUP_DONE=1
    restore_kill_switch
    log "Cleanup complete. Original kill-switch state restoration attempted."
}

trap 'cleanup' EXIT HUP INT TERM

set_kill_switch_present() {
    [ "$MANAGE_KILL_SWITCH" = "1" ] || {
        log "Kill-switch automation not enabled; create $KILL_SWITCH manually if required."
        return 1
    }
    mkdir -p "$(dirname "$KILL_SWITCH")" 2>/dev/null || return 1
    : >"$KILL_SWITCH" 2>/dev/null || return 1
    sync 2>/dev/null || true
    log "Kill switch created for controlled test."
    return 0
}

set_kill_switch_absent() {
    [ "$MANAGE_KILL_SWITCH" = "1" ] || {
        log "Kill-switch automation not enabled; remove $KILL_SWITCH manually if required."
        return 1
    }
    rm -f "$KILL_SWITCH" 2>/dev/null || return 1
    sync 2>/dev/null || true
    log "Kill switch removed for controlled test."
    return 0
}

optional_force_stop() {
    OFS_PKG="$1"
    OFS_REASON="$2"
    [ "$ALLOW_FORCE_STOP" = "1" ] || {
        log "Skipping force-stop of $OFS_PKG ($OFS_REASON); ALLOW_FORCE_STOP is not enabled."
        return 1
    }
    log "Reversible force-stop: $OFS_PKG ($OFS_REASON)"
    am force-stop --user "$ANDROID_USER_ID" "$OFS_PKG" 2>&1 | tee -a "$RUN_LOG"
    sleep 2
    return 0
}

capture_package() {
    CP_PKG="$1"
    CP_DIR="$BASE/packages/$(safe_name "$CP_PKG")"
    mkdir -p "$CP_DIR"
    capture "$CP_DIR/pm-path.txt" pm path --user "$ANDROID_USER_ID" "$CP_PKG"
    capture "$CP_DIR/dumpsys-package.txt" dumpsys package "$CP_PKG"
    capture_sh "$CP_DIR/package-filtered.txt" \
        "dumpsys package '$CP_PKG' 2>&1 | grep -Ei 'Package \\[|userId=|sharedUser|versionCode|versionName|codePath|resourcePath|dataDir|flags=|privateFlags=|enabled=|installed=|hidden=|suspended=|signing|certificate|digest|sha-?256|Activity Resolver|Service Resolver|Receiver Resolver|MusicActivity|MusicService|MediaBrowser' | head -n 800"
    CP_APK="$(package_base_apk "$CP_PKG")"
    if [ -n "$CP_APK" ] && [ -r "$CP_APK" ]; then
        if command_exists sha256sum; then
            sha256sum "$CP_APK" >"$CP_DIR/base-apk.sha256" 2>&1
        fi
        stat "$CP_APK" >"$CP_DIR/base-apk.stat.txt" 2>&1 || ls -l "$CP_APK" >"$CP_DIR/base-apk.stat.txt" 2>&1
        if [ "$INCLUDE_APKS" = "1" ]; then
            cp "$CP_APK" "$BASE/apks/$(safe_name "$CP_PKG")-base.apk" 2>>"$RUN_LOG" ||
                log "WARNING: APK copy failed for $CP_PKG"
        fi
    else
        echo "No readable base APK resolved for $CP_PKG" >"$CP_DIR/base-apk.sha256"
    fi
}

snapshot() {
    SNAP_LABEL="$(safe_name "$1")"
    SNAP_DIR="$BASE/runtime/$SNAP_LABEL"
    mkdir -p "$SNAP_DIR"
    log "Capturing runtime snapshot: $SNAP_LABEL"

    capture "$SNAP_DIR/date-uptime.txt" /system/bin/sh -c 'date; date -u; uptime; cat /proc/uptime 2>/dev/null'
    capture_sh "$SNAP_DIR/processes.txt" \
        "ps -A -o USER,PID,PPID,NAME,ARGS 2>/dev/null | grep -Ei 'com\\.tw\\.music|com\\.tw\\.media|org\\.oxycblt\\.auxio\\.ts18bridge|com\\.dofun\\.variety|lspd|lsposed|zygote|mediaserver|audioserver' || ps -A 2>/dev/null | grep -Ei 'com\\.tw\\.music|com\\.tw\\.media|org\\.oxycblt\\.auxio\\.ts18bridge|com\\.dofun\\.variety|lspd|lsposed|music|media' || true"
    capture "$SNAP_DIR/media-session.txt" dumpsys media_session
    capture_sh "$SNAP_DIR/media-session-focused.txt" \
        "dumpsys media_session 2>&1 | grep -Ei -C 3 'com\\.tw\\.music|com\\.tw\\.media|org\\.oxycblt|state=PlaybackState|active=|package=|Media button session|Sessions Stack' | head -n 1200"
    capture "$SNAP_DIR/audio.txt" dumpsys audio
    capture_sh "$SNAP_DIR/audio-focus-focused.txt" \
        "dumpsys audio 2>&1 | grep -Ei -C 3 'focus|AudioFocus|playback|player|clientId|uid|pid|com\\.tw\\.music|com\\.tw\\.media|org\\.oxycblt|duck|loss|gain' | head -n 1400"
    capture "$SNAP_DIR/activity-services-stock.txt" dumpsys activity services "$STOCK_PACKAGE"
    capture "$SNAP_DIR/activity-services-target.txt" dumpsys activity services "$TARGET_PACKAGE"
    capture "$SNAP_DIR/activity-processes.txt" dumpsys activity processes
    capture_sh "$SNAP_DIR/activity-focused.txt" \
        "dumpsys activity activities 2>&1 | grep -Ei -C 3 'mResumedActivity|topResumedActivity|com\\.tw\\.music|com\\.tw\\.media|com\\.dofun\\.variety|MusicActivity|TaskRecord|ActivityRecord' | head -n 1200"
    capture_sh "$SNAP_DIR/notifications-focused.txt" \
        "dumpsys notification 2>&1 | grep -Ei -C 4 'com\\.tw\\.music|com\\.tw\\.media|org\\.oxycblt|media|music|foreground' | head -n 1200"
    capture "$SNAP_DIR/power.txt" dumpsys power
    capture_sh "$SNAP_DIR/logcat-focused.txt" \
        "logcat -d -v threadtime 2>&1 | grep -Ei 'Auxio-TS LSPosed bridge|ts18bridge|libxposed|LSPosed|Xposed|com\\.tw\\.music|com\\.tw\\.media|org\\.oxycblt|MusicActivity|MusicService|MediaBrowser|MediaSession|MediaController|PlaybackState|AudioFocus|FATAL EXCEPTION|ANR in|am_crash|am_anr|SecurityException|DeadObjectException|RemoteException' | tail -n '$MAX_LOGCAT_LINES'"

    if command_exists uiautomator; then
        uiautomator dump "$SNAP_DIR/window.xml" >"$SNAP_DIR/uiautomator.txt" 2>&1 || true
    fi
    if command_exists screencap; then
        screencap -p "$SNAP_DIR/screen.png" >"$SNAP_DIR/screencap.txt" 2>&1 || true
    fi

    {
        echo "Snapshot: $SNAP_LABEL"
        echo "Target package: $TARGET_PACKAGE"
        echo "Bridge package: $BRIDGE_PACKAGE"
        echo "Kill switch: $(if [ -f "$KILL_SWITCH" ]; then echo present; elif [ -e "$KILL_SWITCH" ]; then echo non-regular-entry; else echo absent; fi)"
        echo
        echo "MediaSession package counts (heuristic only):"
        grep -Eio 'com\.tw\.music|com\.tw\.media(\.debug)?' "$SNAP_DIR/media-session.txt" 2>/dev/null |
            sort | uniq -c || true
        echo
        echo "Notification package counts (heuristic only):"
        grep -Eio 'com\.tw\.music|com\.tw\.media(\.debug)?' "$SNAP_DIR/notifications-focused.txt" 2>/dev/null |
            sort | uniq -c || true
    } >"$SNAP_DIR/quick-summary.txt" 2>&1
}

record_observation() {
    RO_STAGE="$1"
    RO_INSTRUCTIONS="$2"
    echo
    echo "==============================================================================="
    echo "STAGE: $RO_STAGE"
    echo "==============================================================================="
    echo "$RO_INSTRUCTIONS"
    echo
    echo "Complete the physical step, return here, then enter one result:"
    echo "  p = PASS   f = FAIL   s = SKIPPED   u = UNCLEAR"
    printf '> '
    IFS= read RO_RESULT || RO_RESULT="u"
    case "$RO_RESULT" in
        p|P|pass|PASS) RO_RESULT="PASS" ;;
        f|F|fail|FAIL) RO_RESULT="FAIL" ;;
        s|S|skip|SKIPPED) RO_RESULT="SKIPPED" ;;
        u|U|unclear|UNCLEAR|*) RO_RESULT="UNCLEAR" ;;
    esac
    echo "Enter a concise observation. Include duplicate actions, wrong app, delay, stale metadata, crash or unexpected audio."
    printf '> '
    IFS= read RO_NOTES || RO_NOTES=""
    RO_NOTES_CLEAN="$(printf '%s' "$RO_NOTES" | tr '\t\r\n' '   ')"
    printf '%s\t%s\t%s\t%s\n' \
        "$(date -u +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || echo unknown)" \
        "$RO_STAGE" "$RO_RESULT" "$RO_NOTES_CLEAN" >>"$OBSERVATIONS"
    snapshot "$RO_STAGE"
}

capture_lsposed_logs() {
    log "Capturing bounded LSPosed logs where readable."
    CLS_INDEX="$BASE/lsposed/index.txt"
    : >"$CLS_INDEX"
    for CLS_DIR in /data/adb/lspd/log /data/adb/lspd/log/verbose /data/adb/lspd/log/modules /data/adb/lsposed/log; do
        [ -d "$CLS_DIR" ] || continue
        echo "Directory: $CLS_DIR" >>"$CLS_INDEX"
        find "$CLS_DIR" -type f 2>/dev/null | sed -n '1,40p' | while IFS= read CLS_FILE; do
            [ -f "$CLS_FILE" ] || continue
            CLS_NAME="$(safe_name "$CLS_FILE")"
            echo "$CLS_FILE -> $CLS_NAME" >>"$CLS_INDEX"
            tail -n "$MAX_LSPOSED_LINES" "$CLS_FILE" >"$BASE/lsposed/$CLS_NAME.tail.txt" 2>&1 || true
        done
    done
}

make_archive() {
    log "Creating evidence manifest and archive."
    if command_exists sha256sum; then
        (
            cd "$BASE" || exit 0
            find . -type f ! -name SHA256SUMS.txt -print 2>/dev/null | sort |
                while IFS= read MF_FILE; do sha256sum "$MF_FILE"; done
        ) >"$BASE/SHA256SUMS.txt" 2>&1
    fi

    ARCHIVE="${BASE}.tar.gz"
    if command_exists tar; then
        tar -czf "$ARCHIVE" -C "$BASE_ROOT" "$(basename "$BASE")" 2>>"$RUN_LOG" || ARCHIVE=""
    else
        ARCHIVE=""
    fi
    if [ -n "$ARCHIVE" ] && [ -f "$ARCHIVE" ] && command_exists sha256sum; then
        sha256sum "$ARCHIVE" >"${ARCHIVE}.sha256" 2>&1
    fi
    echo "$ARCHIVE"
}

# -----------------------------------------------------------------------------
# Preflight
# -----------------------------------------------------------------------------

log "Auxio-TS LSPosed bridge collector v$SCRIPT_VERSION"
log "Evidence directory: $BASE"

if [ "$(id -u 2>/dev/null || echo unknown)" != "0" ]; then
    log "STOP: run this script as root. No package state was modified."
    log "Example: su -c '/system/bin/sh /storage/emulated/0/Download/collect-ts18-lsposed-bridge-validation.sh'"
    exit 3
fi

if [ "$ANDROID_USER_ID" != "0" ]; then
    log "WARNING: Android user $ANDROID_USER_ID selected; primary TS18 user is normally 0."
fi

resolve_target_package || exit 4
resolve_bridge_package || exit 5
package_installed "$STOCK_PACKAGE" || {
    log "STOP: stock package is not installed: $STOCK_PACKAGE"
    exit 6
}
package_installed "$DOFUN_PACKAGE" || log "WARNING: DoFun package not installed/readable: $DOFUN_PACKAGE"

record_original_kill_switch

printf 'timestamp_utc\tstage\tresult\tnotes\n' >"$OBSERVATIONS"

cat >"$BASE/00-README-FIRST.txt" <<README
Auxio-TS LSPosed bridge physical validation evidence

Collector version: $SCRIPT_VERSION
Generated: $STAMP
Android user: $ANDROID_USER_ID
Stock package: $STOCK_PACKAGE
Target package: $TARGET_PACKAGE
Bridge package: $BRIDGE_PACKAGE
DoFun package: $DOFUN_PACKAGE
Original kill switch: $ORIGINAL_KILL_SWITCH
MANAGE_KILL_SWITCH: $MANAGE_KILL_SWITCH
ALLOW_FORCE_STOP: $ALLOW_FORCE_STOP
INCLUDE_APKS: $INCLUDE_APKS

This bundle may contain media titles, package state, screenshots and diagnostic logs.
Keep it local/private unless reviewed and redacted.

PASS/FAIL/SKIPPED/UNCLEAR observations are in:
manual/operator-observations.tsv
README

# -----------------------------------------------------------------------------
# Identity and package baseline
# -----------------------------------------------------------------------------

capture "$BASE/identity/id.txt" id
capture "$BASE/identity/uname.txt" uname -a
capture "$BASE/identity/getprop.txt" getprop
capture_sh "$BASE/identity/build-selected.txt" \
    "for p in ro.build.version.release ro.build.version.sdk ro.build.version.security_patch ro.build.display.id ro.build.description ro.build.fingerprint ro.product.model ro.product.device ro.product.board ro.hardware ro.boot.verifiedbootstate ro.boot.flash.locked; do printf '%-40s %s\\n' \"\$p=\" \"\$(getprop \"\$p\" 2>/dev/null)\"; done"
capture "$BASE/identity/mount.txt" mount
capture "$BASE/identity/storage.txt" df -h
capture_sh "$BASE/identity/lsposed-processes.txt" \
    "ps -A 2>/dev/null | grep -Ei 'lspd|lsposed|zygisk|magisk' || true"
capture_sh "$BASE/identity/kill-switch.txt" \
    "ls -ld '$KILL_SWITCH' '$(dirname "$KILL_SWITCH")' 2>&1; if [ -f '$KILL_SWITCH' ]; then echo PRESENT; elif [ -e '$KILL_SWITCH' ]; then echo NON_REGULAR_ENTRY; else echo ABSENT; fi"

capture_package "$STOCK_PACKAGE"
capture_package "$TARGET_PACKAGE"
capture_package "$BRIDGE_PACKAGE"
package_installed "$DOFUN_PACKAGE" && capture_package "$DOFUN_PACKAGE"

capture_sh "$BASE/packages/component-resolution.txt" \
    "cmd package resolve-activity --brief --components --user '$ANDROID_USER_ID' -n '$STOCK_PACKAGE/com.tw.music.MusicActivity' 2>&1 || true; echo; cmd package resolve-activity --brief --components --user '$ANDROID_USER_ID' -n '$TARGET_PACKAGE/com.tw.music.MusicActivity' 2>&1 || true; echo; cmd package query-services --brief --components --user '$ANDROID_USER_ID' -a android.media.browse.MediaBrowserService '$TARGET_PACKAGE' 2>&1 || true"

snapshot baseline

# -----------------------------------------------------------------------------
# Controlled interactive matrix
# -----------------------------------------------------------------------------

if [ "$MANAGE_KILL_SWITCH" = "1" ]; then
    if set_kill_switch_present; then
        sleep 5
        optional_force_stop "$STOCK_PACKAGE" "reload bridge-disabled state" || true
    fi
fi
record_observation "01-kill-switch-stock-fallback" \
"Ensure the kill switch is PRESENT. Open/tap the fixed DoFun Music widget and try one stock play/pause action. Expected: stock behaviour remains available; Auxio is not redirected/controlled; no crash. If automation is disabled, manage the marker manually before testing."

if [ "$MANAGE_KILL_SWITCH" = "1" ]; then
    if set_kill_switch_absent; then
        sleep 5
        optional_force_stop "$STOCK_PACKAGE" "reload bridge-enabled state" || true
    fi
fi
record_observation "02-fixed-widget-launch" \
"Ensure the kill switch is ABSENT and the module is enabled with scope com.tw.music only. Tap the fixed DoFun Music widget. Expected: Auxio target MusicActivity opens; stock activity closes; no duplicate UI or crash."

record_observation "03-play-pause-exactly-once" \
"Start Auxio playback. Use the fixed widget play/pause once, wait, then once again. Expected: exactly one state change per press; stock audio/queue does not react; metadata/play state follows Auxio."

record_observation "04-previous-exactly-once" \
"With a queue of distinct tracks, press fixed-widget Previous once. Expected: Auxio moves exactly once; stock queue/session does not also move."

record_observation "05-next-exactly-once" \
"Press fixed-widget Next once. Expected: Auxio moves exactly once; no skipped extra track or stock queue change."

record_observation "06-seek" \
"Use the DoFun seek/progress control where available. Expected: Auxio seeks to the intended millisecond position once; no large unit conversion error, duplicate seek or stock response."

record_observation "07-metadata-progress" \
"Play for at least 20 seconds, pause, resume and change track. Expected: title/artist/album/duration/progress track Auxio, progress settles while paused, and no stale previous-track metadata remains."

record_observation "08-hardware-media-controls" \
"Test steering-wheel/media keys that normally target music. Expected: one Auxio action per input when the bridge owns the proven path; no duplicate stock action. Record which keys were actually available."

if [ "$ALLOW_FORCE_STOP" = "1" ]; then
    optional_force_stop "$TARGET_PACKAGE" "target-not-ready fallback test" || true
fi
record_observation "09-target-not-ready-fail-open" \
"Make Auxio unavailable/not ready (force-stop only when explicitly enabled, or close it manually). Trigger one stock/fixed-widget command. Expected: the bridge does not swallow the command merely because transport submission was attempted; stock path remains available and the stock process does not crash."

if [ "$ALLOW_FORCE_STOP" = "1" ]; then
    optional_force_stop "$TARGET_PACKAGE" "Auxio process restart recovery" || true
fi
record_observation "10-auxio-process-restart" \
"Restart/open Auxio after its process was stopped. Start playback and test play/pause/next. Expected: MediaBrowser/session reconnects without reboot, exactly-once controls resume, and stale controller callbacks are gone."

if [ "$ALLOW_FORCE_STOP" = "1" ]; then
    optional_force_stop "$STOCK_PACKAGE" "stock process restart recovery" || true
fi
record_observation "11-stock-process-restart" \
"Restart the stock music process by reopening the fixed widget. Expected: LSPosed module reloads safely, identity checks repeat, Auxio redirect/control/mirroring recover, no boot loop or repeated reconnect storm."

if [ "$ALLOW_FORCE_STOP" = "1" ]; then
    optional_force_stop "$DOFUN_PACKAGE" "DoFun launcher restart recovery" || true
fi
record_observation "12-dofun-restart" \
"Restart/return to DoFun. Expected: fixed widget launch/control/state recover without restarting the whole head unit. Do not force-stop DoFun unless ALLOW_FORCE_STOP=1 and recovery is proven."

record_observation "13-usb-removal-reinsert" \
"While using music from removable storage, remove and reinsert the USB volume using normal safe handling. Expected: the bridge does not make unavailable queue items playable, does not retain misleading progress indefinitely, and does not alter source authority."

if [ "$MANAGE_KILL_SWITCH" = "1" ]; then
    set_kill_switch_present || true
    sleep 5
fi
record_observation "14-live-kill-switch" \
"Activate the kill switch while running, then test a new widget command. Expected: new bridge forwarding/redirection/mirroring stops within the documented bounded refresh; stock behaviour remains; no crash. Original marker state will be restored at script exit when automation is enabled."

record_observation "15-acc-sleep-wake" \
"Perform a real ACC sleep/wake cycle only using the vehicle/head-unit's normal operation. Return after wake. Expected: no boot loop, stock and Auxio processes recover, bridge reconnects once, controls remain exactly-once, and no stale/duplicate audio resumes."

record_observation "16-final-single-authority" \
"With Auxio actively playing, inspect audible behaviour and notifications. Expected: one playback source, one active music session, one audio-focus owner and one playback notification authority; stock audio does not resume after Auxio pause."

# -----------------------------------------------------------------------------
# Final evidence and summary
# -----------------------------------------------------------------------------

snapshot final
capture_lsposed_logs

{
    echo "Auxio-TS LSPosed bridge validation summary"
    echo
    echo "Generated: $STAMP"
    echo "Stock package: $STOCK_PACKAGE"
    echo "Target package: $TARGET_PACKAGE"
    echo "Bridge package: $BRIDGE_PACKAGE"
    echo "Original kill switch: $ORIGINAL_KILL_SWITCH"
    echo
    echo "Operator results:"
    if [ -f "$OBSERVATIONS" ]; then
        awk -F '\t' 'NR>1 {count[$3]++} END {for (k in count) print k ": " count[k]}' "$OBSERVATIONS" | sort
    fi
    echo
    echo "Failures/unclear items:"
    awk -F '\t' 'NR>1 && ($3=="FAIL" || $3=="UNCLEAR") {print $2 "\t" $3 "\t" $4}' "$OBSERVATIONS" 2>/dev/null || true
    echo
    echo "Important: these counts do not replace inspection of MediaSession/audio/log evidence."
} >"$BASE/summary/summary.txt" 2>&1

cleanup
ARCHIVE_PATH="$(make_archive)"

log "Collection complete."
log "Evidence directory: $BASE"
if [ -n "$ARCHIVE_PATH" ] && [ -f "$ARCHIVE_PATH" ]; then
    log "Archive: $ARCHIVE_PATH"
    log "Archive checksum: ${ARCHIVE_PATH}.sha256"
else
    log "Archive creation unavailable; use the evidence directory directly."
fi
log "Review and redact media titles/screenshots before sharing."

exit 0
