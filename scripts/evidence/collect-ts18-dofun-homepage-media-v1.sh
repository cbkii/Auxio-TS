#!/system/bin/sh
# Auxio-TS TS18 / DoFun homepage media evidence collector v1
# Read-only by design. No package disable, launcher writes, source forcing or app-private mutation.

SCRIPT_VERSION="1.0"
ANDROID_USER_ID="${ANDROID_USER_ID:-0}"
TARGET_PKG="${TARGET_PKG:-com.tw.media}"
MAX_CAPTURE_BYTES="${MAX_CAPTURE_BYTES:-4194304}"
CAPTURE_TIMEOUT_SECONDS="${CAPTURE_TIMEOUT_SECONDS:-12}"
TS="$(date +%Y%m%d_%H%M%S 2>/dev/null)"
[ -n "$TS" ] || TS="unknown_time"
BASE="${OUTPUT_DIR:-/storage/emulated/0/Download/AuxioTS/dofun-homepage-media-${TS}}"
WARNINGS=0
ERRORS=0
TRUNCATED=0

log() {
    printf '%s\n' "$*"
}

warn() {
    WARNINGS=$((WARNINGS + 1))
    printf 'WARN: %s\n' "$*" >&2
}

error() {
    ERRORS=$((ERRORS + 1))
    printf 'ERROR: %s\n' "$*" >&2
}

stop_safety() {
    printf 'STOP: %s\n' "$*" >&2
    printf 'STOPPED FOR SAFETY\n' >&2
    exit 4
}

if [ "$ANDROID_USER_ID" != "0" ]; then
    stop_safety "This exact TS18 validation expects Android user 0; set ANDROID_USER_ID=0 unless current device evidence proves otherwise."
fi

case "$MAX_CAPTURE_BYTES" in
    ''|*[!0-9]*) stop_safety "MAX_CAPTURE_BYTES must be an integer" ;;
esac
case "$CAPTURE_TIMEOUT_SECONDS" in
    ''|*[!0-9]*) stop_safety "CAPTURE_TIMEOUT_SECONDS must be an integer" ;;
esac
[ "$MAX_CAPTURE_BYTES" -ge 65536 ] || stop_safety "MAX_CAPTURE_BYTES is too small for useful diagnostics"
[ "$CAPTURE_TIMEOUT_SECONDS" -ge 2 ] || stop_safety "CAPTURE_TIMEOUT_SECONDS is too small"

if command -v timeout >/dev/null 2>&1; then
    TIMEOUT_PREFIX="timeout"
elif command -v toybox >/dev/null 2>&1 && toybox timeout 1 true >/dev/null 2>&1; then
    TIMEOUT_PREFIX="toybox timeout"
else
    stop_safety "No bounded timeout implementation found; refusing potentially unbounded dumpsys/logcat calls"
fi

if ! mkdir -p "$BASE" "$BASE/identity" "$BASE/packages" "$BASE/notification" "$BASE/media" "$BASE/dofun" "$BASE/services" "$BASE/logs" "$BASE/manual" 2>/dev/null; then
    printf 'FAILED\n' >&2
    exit 3
fi

TMP_ROOT="${TMPDIR:-/data/local/tmp}"
if [ ! -d "$TMP_ROOT" ] || [ ! -w "$TMP_ROOT" ]; then
    TMP_ROOT="$BASE/.tmp"
    if ! mkdir -p "$TMP_ROOT" 2>/dev/null; then
        error "No writable temporary directory"
        printf 'FAILED\n' >&2
        exit 3
    fi
    warn "Private temporary storage unavailable; using output-local temporary files"
fi

TMP_DIR="$(mktemp -d "$TMP_ROOT/auxio-dofun-homepage.XXXXXX" 2>/dev/null)"
[ -n "$TMP_DIR" ] && [ -d "$TMP_DIR" ] || stop_safety "Unable to create secure temporary directory"

cleanup() {
    rc=$?
    rm -rf "$TMP_DIR" 2>/dev/null
    trap - EXIT HUP INT TERM
    if [ "$rc" -eq 130 ] || [ "$rc" -eq 143 ]; then
        printf 'INTERRUPTED\n' >&2
    fi
    exit "$rc"
}
trap 'exit 130' HUP INT TERM
trap cleanup EXIT

BOOT_ID="$(cat /proc/sys/kernel/random/boot_id 2>/dev/null | tr -d '\r\n')"
[ -n "$BOOT_ID" ] || BOOT_ID="unavailable"
IDENTITY="$(id 2>&1 | tr '\r\n' ' ' | cut -c1-320)"

run_timeout() {
    seconds=$1
    shift
    if [ "$TIMEOUT_PREFIX" = "timeout" ]; then
        timeout "$seconds" "$@"
    else
        toybox timeout "$seconds" "$@"
    fi
}

write_capture_header() {
    out=$1
    command_text=$2
    timeout_seconds=$3
    {
        printf '###############################################################################\n'
        printf '# Auxio-TS DoFun homepage evidence\n'
        printf '# Script version: %s\n' "$SCRIPT_VERSION"
        printf '# Timestamp: %s\n' "$(date 2>/dev/null)"
        printf '# Boot ID: %s\n' "$BOOT_ID"
        printf '# Android user: %s\n' "$ANDROID_USER_ID"
        printf '# Shell identity: %s\n' "$IDENTITY"
        printf '# Timeout seconds: %s\n' "$timeout_seconds"
        printf '# Command: %s\n' "$command_text"
        printf '###############################################################################\n\n'
    } > "$out"
}

finalize_capture() {
    tmp=$1
    out=$2
    rc=$3
    required=$4
    bytes="$(wc -c < "$tmp" 2>/dev/null | tr -d ' ')"
    [ -n "$bytes" ] || bytes=0
    if [ "$bytes" -gt "$MAX_CAPTURE_BYTES" ]; then
        bounded="$TMP_DIR/bounded.$$"
        head -c "$MAX_CAPTURE_BYTES" "$tmp" > "$bounded" 2>/dev/null
        {
            printf '\n\n[TRUNCATED: original_bytes=%s limit_bytes=%s]\n' "$bytes" "$MAX_CAPTURE_BYTES"
            printf '[Exit status before truncation: %s]\n' "$rc"
        } >> "$bounded"
        mv "$bounded" "$out"
        TRUNCATED=$((TRUNCATED + 1))
        warn "Capture truncated: $out"
    else
        {
            printf '\n[Exit status: %s]\n' "$rc"
            printf '[Truncated: no]\n'
        } >> "$tmp"
        mv "$tmp" "$out"
    fi

    if [ "$rc" -ne 0 ]; then
        if [ "$required" = "required" ]; then
            error "Required capture failed rc=$rc: $out"
        else
            warn "Optional capture failed rc=$rc: $out"
        fi
    fi
}

capture() {
    rel=$1
    required=$2
    timeout_seconds=$3
    shift 3
    out="$BASE/$rel"
    mkdir -p "$(dirname "$out")" 2>/dev/null || {
        error "Unable to create output directory for $rel"
        return 1
    }
    tmp="$TMP_DIR/capture.$$.tmp"
    command_text="$(printf '%s ' "$@" | cut -c1-1000)"
    write_capture_header "$tmp" "$command_text" "$timeout_seconds"
    run_timeout "$timeout_seconds" "$@" >> "$tmp" 2>&1
    rc=$?
    finalize_capture "$tmp" "$out" "$rc" "$required"
    return 0
}

capture_sh() {
    rel=$1
    required=$2
    timeout_seconds=$3
    command_text=$4
    out="$BASE/$rel"
    mkdir -p "$(dirname "$out")" 2>/dev/null || {
        error "Unable to create output directory for $rel"
        return 1
    }
    tmp="$TMP_DIR/capture.$$.tmp"
    write_capture_header "$tmp" "$command_text" "$timeout_seconds"
    run_timeout "$timeout_seconds" sh -c "$command_text" >> "$tmp" 2>&1
    rc=$?
    finalize_capture "$tmp" "$out" "$rc" "$required"
    return 0
}

log "Auxio-TS DoFun homepage media collector v$SCRIPT_VERSION"
log "Output: $BASE"
log "Android user: $ANDROID_USER_ID"
log "Target package: $TARGET_PKG"

capture "identity/id.txt" required 4 id
capture "identity/selinux.txt" optional 4 getenforce
capture_sh "identity/build.txt" required 6 'for p in ro.build.version.release ro.build.version.sdk ro.build.description ro.build.fingerprint ro.product.model ro.product.device ro.hardware; do printf "%s=%s\n" "$p" "$(getprop "$p" 2>/dev/null)"; done'

capture_sh "packages/paths.txt" required 8 "for p in $TARGET_PKG com.tw.music com.dofun.variety com.tw.service.xt org.videolan.vlc; do echo ===== \$p =====; pm path --user $ANDROID_USER_ID \$p 2>&1 || pm path \$p 2>&1; done"
capture_sh "packages/enabled-disabled.txt" required 8 "echo '=== enabled relevant ==='; pm list packages -e -u --user $ANDROID_USER_ID 2>&1 | grep -E 'com\\.tw\\.media|com\\.tw\\.music|com\\.dofun\\.variety|com\\.tw\\.service\\.xt|org\\.videolan\\.vlc' || true; echo; echo '=== disabled relevant ==='; pm list packages -d -u --user $ANDROID_USER_ID 2>&1 | grep -E 'com\\.tw\\.media|com\\.tw\\.music|com\\.dofun\\.variety|com\\.tw\\.service\\.xt|org\\.videolan\\.vlc' || true"
capture "packages/com.tw.media.txt" required "$CAPTURE_TIMEOUT_SECONDS" dumpsys package "$TARGET_PKG"
capture "packages/com.tw.music.txt" optional "$CAPTURE_TIMEOUT_SECONDS" dumpsys package com.tw.music
capture "packages/com.dofun.variety.txt" optional "$CAPTURE_TIMEOUT_SECONDS" dumpsys package com.dofun.variety

capture_sh "packages/component-resolution.txt" required 10 "echo '=== exact DoFun fixed component ==='; cmd package resolve-activity --user $ANDROID_USER_ID --brief -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $TARGET_PKG/com.tw.music.MusicActivity 2>&1 || true; echo; echo '=== MediaBrowser services ==='; cmd package query-services --user $ANDROID_USER_ID --brief --components -a android.media.browse.MediaBrowserService 2>&1 | grep -E '$TARGET_PKG|com\\.tw\\.music|org\\.videolan\\.vlc' || true; echo; echo '=== media button receivers ==='; cmd package query-receivers --user $ANDROID_USER_ID --brief --components -a android.intent.action.MEDIA_BUTTON 2>&1 | grep -E '$TARGET_PKG|com\\.tw\\.music|org\\.videolan\\.vlc' || true"

capture "notification/dumpsys-notification-full.txt" required "$CAPTURE_TIMEOUT_SECONDS" dumpsys notification --noredact
capture_sh "notification/com.tw.media-context.txt" required 8 "dumpsys notification --noredact 2>&1 | grep -n -B 12 -A 80 -E '$TARGET_PKG|$TARGET_PKG\\.channel\\.PLAYBACK' | head -n 1200"
capture_sh "notification/listeners-and-dofun.txt" required 8 "dumpsys notification --noredact 2>&1 | grep -n -B 6 -A 18 -Ei 'NotificationListeners|enabled_notification_listeners|com\\.dofun\\.variety|NotifyService' | head -n 700"

capture "media/dumpsys-media_session-full.txt" required "$CAPTURE_TIMEOUT_SECONDS" dumpsys media_session
capture_sh "media/auxio-session-context.txt" required 8 "dumpsys media_session 2>&1 | grep -n -B 12 -A 80 -E '$TARGET_PKG|media button session|Media button session' | head -n 1400"
capture_sh "media/audio-focus.txt" optional 8 "dumpsys audio 2>&1 | grep -n -B 4 -A 18 -Ei 'focus|$TARGET_PKG|com\\.tw\\.music|org\\.videolan\\.vlc' | head -n 1000"

capture "dofun/hotseat_app_music.txt" optional 8 content query --uri content://com.dofun.variety.ExportedProvider/hotseat_app_music
capture_sh "dofun/services.txt" optional 8 "dumpsys activity services com.dofun.variety 2>&1 | grep -n -B 6 -A 30 -Ei 'NotifyService|media|music|listener' | head -n 900"
capture_sh "services/relevant-processes.txt" required 6 "ps -A 2>/dev/null | grep -E 'com\\.tw\\.media|com\\.tw\\.music|com\\.dofun\\.variety|com\\.tw\\.service\\.xt|org\\.videolan\\.vlc' || ps 2>/dev/null | grep -E 'com\\.tw\\.media|com\\.tw\\.music|com\\.dofun\\.variety|com\\.tw\\.service\\.xt|org\\.videolan\\.vlc' || true"
capture_sh "services/relevant-services.txt" optional 10 "for p in $TARGET_PKG com.tw.music com.dofun.variety com.tw.service.xt; do echo ===== \$p =====; dumpsys activity services \$p 2>&1 | head -n 360; done"

capture_sh "logs/logcat-focused.txt" optional 8 "logcat -d -v threadtime 2>&1 | grep -Ei 'Auxio|com\\.tw\\.media|dofun|variety|Topway|MediaSession|MediaBrowser|MEDIA_BUTTON|metachanged|playstatechanged|channel\\.PLAYBACK|NotifyService|CommandService|MusicActivity|MusicService' | tail -n 2200"

# Optional narrow root read: only the one default-SharedPreferences key needed to identify the
# current launcher mode. This is read-only and is skipped when root is unavailable or not granted.
if command -v su >/dev/null 2>&1; then
    capture_sh "identity/launcher-mode-root-read.txt" optional 6 "su -c 'for f in /data/user/0/$TARGET_PKG/shared_prefs/${TARGET_PKG}_preferences.xml /data/data/$TARGET_PKG/shared_prefs/${TARGET_PKG}_preferences.xml; do if [ -r \"\$f\" ]; then echo FILE=\$f; grep -F \"auxio_ts18_launcher_integration_mode\" \"\$f\" | head -n 4; exit 0; fi; done; echo preference-file-not-readable; exit 0'"
else
    warn "su not available; current launcher mode must be recorded from Auxio diagnostics UI"
fi

cat > "$BASE/manual/PHYSICAL_TEST_SEQUENCE.md" <<'EOF'
# Exact TS18 physical sequence

This collector made no mutations. Use the generated baseline before any protected-package test.

1. **Notification prerequisite** — with `com.tw.media` playing, confirm the playback channel is not
   importance `0`, a live transport notification exists, DoFun `NotifyService` is connected, and
   the Auxio MediaSession is active. If the channel is blocked, re-enable it through Auxio's exact
   playback-channel settings row and repeat the baseline before interpreting DoFun failure.
2. **VLC positive control** — record title/artist/art/progress and one press each of previous,
   play/pause and next, plus source tap/open.
3. **Auxio GenericDofunMedia** — repeat the same one-button-at-a-time observations and export the
   Auxio diagnostics report/journal.
4. **Auxio AutoAllSafePaths** — repeat exactly. Compare whether generic and/or Topway ingress events
   fire and whether any physical press causes more than one playback action.
5. **Identity comparator** — only if needed, use the maintained debug/application-id-suffixed build
   as a non-fixed identity comparator. It is not a release product.
6. **Stock-selection comparator** — only with explicit approval and a saved baseline, use the
   repository's guarded `tools/ts18-root-storage-fastpath/tier3/stock-music-selection-test.sh`.
   Never uninstall/delete stock Music. Restore immediately after the bounded observation.

Do not proceed to DoFun-scoped LSPosed research until the healthy notification, hybrid profile,
identity comparator and stock-selection evidence have discriminated the public/direct paths.
EOF

cat > "$BASE/README_FIRST.txt" <<EOF
Auxio-TS DoFun homepage media evidence collector v$SCRIPT_VERSION
Generated: $(date 2>/dev/null)
Boot ID: $BOOT_ID
Android user: $ANDROID_USER_ID
Target package: $TARGET_PKG
Shell identity: $IDENTITY

Safety: READ-ONLY. This script does not disable/uninstall packages, clear DoFun data, write launcher
state, force Topway source selection, invoke private Binder writes, or modify system/vendor files.

Important interpretation rules:
- package presence is not proof that DoFun selected that package;
- an active MediaSession is not proof that the fixed DoFun media surface adopted Auxio;
- a blocked playback notification channel invalidates generic-notification-path conclusions;
- failed/permission-denied probes mean "not found in the inspected scope", not absence.
EOF

SUMMARY="$BASE/SUMMARY.txt"
{
    printf 'Auxio-TS DoFun homepage collector result\n'
    printf 'Generated: %s\n' "$(date 2>/dev/null)"
    printf 'Output: %s\n' "$BASE"
    printf 'Warnings: %s\n' "$WARNINGS"
    printf 'Errors: %s\n' "$ERRORS"
    printf 'Truncated captures: %s\n' "$TRUNCATED"
    if [ "$ERRORS" -gt 0 ]; then
        printf 'Status: FAILED\n'
    elif [ "$WARNINGS" -gt 0 ] || [ "$TRUNCATED" -gt 0 ]; then
        printf 'Status: COMPLETED WITH WARNINGS\n'
    else
        printf 'Status: SUCCESS\n'
    fi
} > "$SUMMARY"

if [ "$ERRORS" -gt 0 ]; then
    log "FAILED"
    log "Evidence retained at: $BASE"
    exit 2
fi
if [ "$WARNINGS" -gt 0 ] || [ "$TRUNCATED" -gt 0 ]; then
    log "COMPLETED WITH WARNINGS"
    log "Evidence retained at: $BASE"
    exit 0
fi
log "SUCCESS"
log "Evidence retained at: $BASE"
exit 0
