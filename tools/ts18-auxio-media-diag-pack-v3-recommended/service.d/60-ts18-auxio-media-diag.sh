#!/system/bin/sh
# TS18 Auxio/VLC media diagnostics collector
# Intended path on device: /data/adb/service.d/60-ts18-auxio-media-diag.sh
# Read-only except for its own output folder and optional stop/trigger files.

CONF="/data/adb/ts18-auxio-media-diag.conf"
[ -f "$CONF" ] && . "$CONF"

# Defaults can be overridden in /data/adb/ts18-auxio-media-diag.conf
RUN_ON_BOOT="${RUN_ON_BOOT:-1}"
REQUIRE_TRIGGER_ON_BOOT="${REQUIRE_TRIGGER_ON_BOOT:-0}"
TRIGGER_FILE="${TRIGGER_FILE:-/storage/emulated/0/Download/TS18_AuxioMediaDiag.RUN}"
STOP_FILE="${STOP_FILE:-/storage/emulated/0/Download/TS18_AuxioMediaDiag.STOP}"
BASE_OUT="${BASE_OUT:-/storage/emulated/0/Download/TS18_AuxioMediaDiag}"
DURATION_SECONDS="${DURATION_SECONDS:-2400}"
INTERVAL_SECONDS="${INTERVAL_SECONDS:-15}"
BOOT_WAIT_SECONDS="${BOOT_WAIT_SECONDS:-90}"
SETTLE_WAIT_SECONDS="${SETTLE_WAIT_SECONDS:-10}"
LOGCAT_ALL="${LOGCAT_ALL:-0}"
COPY_AUXIO_PREFS="${COPY_AUXIO_PREFS:-0}"
CAPTURE_BUGREPORT="${CAPTURE_BUGREPORT:-0}"
CAPTURE_DMESG="${CAPTURE_DMESG:-1}"
CAPTURE_SQLITE_LIST="${CAPTURE_SQLITE_LIST:-0}"
COPY_AUXIO_APP_REPORTS="${COPY_AUXIO_APP_REPORTS:-1}"
CAPTURE_DROPBOX="${CAPTURE_DROPBOX:-1}"
CAPTURE_CRASH_LOGS="${CAPTURE_CRASH_LOGS:-1}"
CAPTURE_SAFE_ROOT_PROBES="${CAPTURE_SAFE_ROOT_PROBES:-1}"
CAPTURE_DEEP_SOURCE_DIAGS="${CAPTURE_DEEP_SOURCE_DIAGS:-1}"
CAPTURE_AUTOSTART_DIAGS="${CAPTURE_AUTOSTART_DIAGS:-1}"
CAPTURE_OVERLAY_EDGE_DIAGS="${CAPTURE_OVERLAY_EDGE_DIAGS:-1}"
CAPTURE_PLAYBACK_INTERRUPT_DIAGS="${CAPTURE_PLAYBACK_INTERRUPT_DIAGS:-1}"
CAPTURE_MEDIASTORE_ROWS="${CAPTURE_MEDIASTORE_ROWS:-1}"
MAX_LOG_LINES="${MAX_LOG_LINES:-25000}"
SOURCE_CANDIDATE_PATHS="${SOURCE_CANDIDATE_PATHS:-/storage/emulated/0 /storage/emulated/0/Music /storage/emulated/0/Download /storage/usbdisk0 /storage/usbdisk0/Music /storage/usbdisk0/Download /storage/usbdisk1 /storage/usbdisk1/Music /storage/usbdisk1/Download /mnt/media_rw/usbdisk0 /mnt/media_rw/usbdisk1}"

# Packages: exact and fuzzy discovery are both used because Auxio-TS variant IDs may differ.
BASE_PACKAGES="${BASE_PACKAGES:-
org.oxycblt.auxio
org.oxycblt.auxio.debug
com.tw.media
com.tw.music
org.videolan.vlc
com.dofun.variety
com.cbkii.btandroidts
com.cbkii.btandroidts.debug
com.cbkii.ts18intentbridge
com.cbkii.ts18intentbridge.debug
com.android.documentsui
com.google.android.documentsui
com.navimods.radio
com.navimods.radio.plus
com.tw.radio
com.tw.bt
com.android.bluetooth
com.tw.eq
com.tw.service
com.tw.coreservice
com.tw.core
com.tw.carinfoservice
com.tw.reverse
com.tw.devicefan
com.dofun.carsetting
com.dofun.carassistant.car
com.zjinnova.zlink
com.google.android.projection.gearhead
com.android.providers.media
com.android.providers.downloads
com.sprd.systemupdate
com.abupdate.fota_demo_iot
}"

# Focused logcat. The output still may contain personal data: inspect before public sharing.
LOG_FILTER="${LOG_FILTER:-Auxio|org\\.oxycblt|com\\.tw\\.media|com\\.tw\\.music|Topway|DoFun|dofun|TWTHEME|VLC|videolan|NavRadio|navimods|BTAndroidTS|ts18intent|DocumentsUI|documentsui|MediaSession|MediaBrowser|MediaController|MediaButton|PlaybackState|AudioFocus|AUDIOFOCUS|focus loss|onAudioFocusChange|becoming noisy|ACTION_AUDIO_BECOMING_NOISY|AudioTrack|AudioFlinger|AudioPolicy|ExoPlayer|Notification|RemoteViews|Bitmap|AppWidget|Widget|DirectFS|FilteredFS|LocationMode|LocationsDialog|RootState|RootGate|SAF|DocumentsProvider|OPEN_DOCUMENT|OPEN_DOCUMENT_TREE|PersistableUriPermission|Storage|usbdisk|media_rw|Overlay|WindowManager|Zygisk|LSPosed|Magisk|ANR|FATAL EXCEPTION|AndroidRuntime|SecurityException|IllegalArgumentException|IllegalStateException|NullPointerException|IndexOutOfBoundsException|RemoteServiceException|TransactionTooLargeException|DeadObjectException|Bad notification|denied|timeout|failed}"

PATH="/system/bin:/system/xbin:/vendor/bin:/sbin:/data/adb/magisk:/data/adb/ksu/bin:$PATH"
export PATH

###############################################################################
# Invocation modes
###############################################################################

case "$1" in
  earlyboot)
    _base="${BASE_OUT:-/storage/emulated/0/Download/TS18_AuxioMediaDiag}"
    mkdir -p "$_base/_early_boot" 2>/dev/null || _base="/data/local/tmp/TS18_AuxioMediaDiag"; mkdir -p "$_base/_early_boot" 2>/dev/null
    _out="$_base/_early_boot/earlyboot-$(date '+%Y%m%d-%H%M%S').txt"
    {
      echo "### Early service.d readiness snapshot"
      echo "# This is captured before the main BOOT_WAIT_SECONDS delay. service.d itself still runs at Magisk late_start service, not true init/post-fs-data."
      echo "time=$(date '+%Y-%m-%d %H:%M:%S %z')"
      echo "id=$(id 2>/dev/null)"
      echo "uptime=$(cat /proc/uptime 2>/dev/null)"
      echo
      echo "## boot properties"
      for k in sys.boot_completed dev.bootcomplete service.bootanim.exit init.svc.bootanim init.svc.zygote init.svc.surfaceflinger init.svc.system_server init.svc.audioserver init.svc.media ro.crypto.state ro.crypto.type; do
        echo "$k=$(getprop $k 2>/dev/null)"
      done
      echo
      echo "## mounted storage subset"
      mount 2>/dev/null | grep -Ei 'storage|media_rw|vold|fuse|emulated|usb|fat|exfat|ntfs' || true
      echo
      echo "## package manager/service readiness"
      pm list packages 2>/dev/null | grep -Ei 'auxio|oxycblt|tw|dofun|videolan|documents' || true
      echo
      echo "## quick dumpsys readiness"
      dumpsys -t 5 activity broadcasts 2>&1 | grep -Ei 'BOOT_COMPLETED|LOCKED_BOOT_COMPLETED|USER_UNLOCKED|QUICKBOOT|auxio|tw|dofun' | head -n 200 || true
      echo
      echo "## recent event log boot/media hints"
      logcat -b events -d -v threadtime 2>&1 | grep -Ei 'boot|user|unlock|media|audio|auxio|tw|dofun' | tail -n 300 || true
    } > "$_out" 2>&1
    exit 0
    ;;
  stop)
    mkdir -p "$(dirname "$STOP_FILE")" 2>/dev/null
    touch "$STOP_FILE"
    exit 0
    ;;
  trigger)
    mkdir -p "$(dirname "$TRIGGER_FILE")" 2>/dev/null
    touch "$TRIGGER_FILE"
    exit 0
    ;;
  smoke|dry-run)
    _base="${BASE_OUT:-/tmp/TS18_AuxioMediaDiag}"
    mkdir -p "$_base/ts18-auxio-media-smoke/logs" "$_base/ts18-auxio-media-smoke/commands" 2>/dev/null || exit 2
    {
      echo "# TS18 Auxio/VLC diagnostics report"
      echo
      echo "- Status: smoke"
      echo "- Time: $(date '+%Y-%m-%d %H:%M:%S %z')"
      echo "- Purpose: off-device syntax/output-layout check; no Android device evidence collected."
    } > "$_base/ts18-auxio-media-smoke/REPORT.md"
    printf "time\tlabel\texit_code\ttimeout_s\toutput\n" > "$_base/ts18-auxio-media-smoke/commands/command-index.tsv"
    echo "SMOKE_OK $_base/ts18-auxio-media-smoke"
    exit 0
    ;;
  now)
    ;;
  *)
    if [ "$RUN_ON_BOOT" != "1" ]; then
      exit 0
    fi
    (
      sh "$0" earlyboot >/dev/null 2>&1 || true
      sleep "$BOOT_WAIT_SECONDS"
      if [ "$REQUIRE_TRIGGER_ON_BOOT" = "1" ] && [ ! -f "$TRIGGER_FILE" ]; then
        exit 0
      fi
      sh "$0" now
    ) >/dev/null 2>&1 &
    exit 0
    ;;
esac

###############################################################################
# Helpers
###############################################################################

ts() { date '+%Y%m%d-%H%M%S'; }
human_ts() { date '+%Y-%m-%d %H:%M:%S %z'; }
have_cmd() { command -v "$1" >/dev/null 2>&1; }
safe_name() { echo "$1" | tr '/: ' '___' | tr -cd 'A-Za-z0-9._#@+-'; }

run_timeout() {
  _sec="$1"; shift
  if have_cmd timeout; then
    timeout "$_sec" "$@"
  elif have_cmd toybox; then
    toybox timeout "$_sec" "$@"
  elif have_cmd busybox; then
    busybox timeout "$_sec" "$@"
  else
    "$@"
  fi
}

section() {
  _file="$1"; _title="$2"
  {
    echo
    echo "================================================================================"
    echo "$_title"
    echo "Time: $(human_ts)"
    echo "================================================================================"
  } >> "$_file"
}

cmd_to_file() {
  _label="$1"; _out="$2"; _timeout="$3"; shift 3
  {
    echo "### $_label"
    echo "# Time: $(human_ts)"
    echo "# Command: $*"
    echo
  } > "$_out"
  run_timeout "$_timeout" "$@" >> "$_out" 2>&1
  _rc=$?
  echo "" >> "$_out"
  echo "# Exit: $_rc" >> "$_out"
  if [ -n "$COMMAND_INDEX" ]; then
    printf "%s\t%s\t%s\t%s\t%s\n" "$(human_ts)" "$_label" "$_rc" "$_timeout" "$_out" >> "$COMMAND_INDEX"
  fi
  return "$_rc"
}

dumpsys_file() {
  _service="$1"; _out="$2"; shift 2
  _label="dumpsys $_service $*"
  _timeout="10"
  {
    echo "### dumpsys $_service $*"
    echo "# Time: $(human_ts)"
    echo
  } > "$_out"
  dumpsys -t 10 "$_service" "$@" >> "$_out" 2>&1
  _rc=$?
  echo "" >> "$_out"
  echo "# Exit: $_rc" >> "$_out"
  if [ -n "$COMMAND_INDEX" ]; then
    printf "%s\t%s\t%s\t%s\t%s\n" "$(human_ts)" "$_label" "$_rc" "$_timeout" "$_out" >> "$COMMAND_INDEX"
  fi
  return "$_rc"
}

pkg_installed() { pm path "$1" >/dev/null 2>&1; }

log() {
  _m="$(human_ts) $*"
  echo "$_m"
  echo "$_m" >> "$RUN_LOG"
}

maybe_grep_count() {
  _needle="$1"; _file="$2"
  grep -i -c "$_needle" "$_file" 2>/dev/null || echo 0
}

log_error_count_for() {
  _needle="$1"
  grep -i "$_needle" "$OUT/logs/logcat_filtered.txt" 2>/dev/null \
    | grep -i -E 'FATAL EXCEPTION|ANR|AndroidRuntime|SecurityException|IllegalArgumentException|IllegalStateException|NullPointerException|IndexOutOfBoundsException|RemoteServiceException|TransactionTooLargeException|DeadObjectException|RemoteViews|Bitmap|crash|exception|error|denied|timeout|failed|not allowed|permission' \
    | wc -l | tr -d ' '
}

playing_hint_count_for() {
  _needle="$1"
  grep -i -A 90 -B 25 "$_needle" "$MEDIA_ALL" 2>/dev/null \
    | grep -i -E 'STATE_PLAYING|state=3|state=PlaybackState.*3|PlaybackState.*PLAYING|playing=true|isPlaying=true' \
    | wc -l | tr -d ' '
}

copy_if_exists() {
  _src="$1"; _dst="$2"
  [ -e "$_src" ] && cp -a "$_src" "$_dst" 2>/dev/null
}

###############################################################################
# Output setup
###############################################################################

sleep "$SETTLE_WAIT_SECONDS"
_i=0
while [ "$_i" -lt 60 ]; do
  if mkdir -p "$BASE_OUT" >/dev/null 2>&1 && [ -w "$BASE_OUT" ]; then
    break
  fi
  sleep 2
  _i=$(( _i + 1 ))
done

if [ ! -w "$BASE_OUT" ]; then
  BASE_OUT="/data/local/tmp/TS18_AuxioMediaDiag"
  mkdir -p "$BASE_OUT" 2>/dev/null
fi

LOCK_DIR="/data/local/tmp/ts18_auxio_media_diag.lock"
BOOT_ID="$(cat /proc/sys/kernel/random/boot_id 2>/dev/null || echo unknown)"
LOCK_STALE=0
if ! mkdir "$LOCK_DIR" >/dev/null 2>&1; then
  OLD_PID="$(cat "$LOCK_DIR/pid" 2>/dev/null)"
  OLD_BOOT="$(cat "$LOCK_DIR/boot_id" 2>/dev/null)"
  OLD_TS="$(cat "$LOCK_DIR/epoch" 2>/dev/null)"
  NOW_EPOCH="$(date +%s)"
  AGE=$(( NOW_EPOCH - ${OLD_TS:-0} ))
  if [ -z "$OLD_PID" ] || [ -z "$OLD_BOOT" ] || [ -z "$OLD_TS" ]; then
    mkdir -p "$BASE_OUT" 2>/dev/null
    echo "$(human_ts) Refused: lock initialising $LOCK_DIR" >> "$BASE_OUT/last-run-refused.log"
    exit 0
  fi
  if ! kill -0 "$OLD_PID" >/dev/null 2>&1 || [ "$OLD_BOOT" != "$BOOT_ID" ] || [ "$AGE" -gt $(( DURATION_SECONDS + 900 )) ]; then
    LOCK_STALE=1
    rm -rf "$LOCK_DIR" >/dev/null 2>&1
  fi
  if [ "$LOCK_STALE" != "1" ] || ! mkdir "$LOCK_DIR" >/dev/null 2>&1; then
    mkdir -p "$BASE_OUT" 2>/dev/null
    echo "$(human_ts) Refused: active lock $LOCK_DIR pid=$OLD_PID boot=$OLD_BOOT age=$AGE" >> "$BASE_OUT/last-run-refused.log"
    exit 0
  fi
  echo "$(human_ts) Recovered stale lock $LOCK_DIR pid=$OLD_PID boot=$OLD_BOOT age=$AGE" >> "$BASE_OUT/last-run-refused.log"
fi
echo "$$" > "$LOCK_DIR/pid"
echo "$BOOT_ID" > "$LOCK_DIR/boot_id"
date +%s > "$LOCK_DIR/epoch"

SESSION_ID="ts18-auxio-media-$(ts)"
OUT="$BASE_OUT/$SESSION_ID"
mkdir -p "$OUT" "$OUT/packages" "$OUT/snapshots" "$OUT/system" "$OUT/storage" "$OUT/logs" "$OUT/appdata" "$OUT/auxio" "$OUT/vendor" "$OUT/magisk" "$OUT/summary" "$OUT/commands" "$OUT/source_paths" "$OUT/autostart" "$OUT/overlay" "$OUT/interruptions" 2>/dev/null
RUN_LOG="$OUT/run.log"
COMMAND_INDEX="$OUT/commands/command-index.tsv"
printf "time\tlabel\texit_code\ttimeout_s\toutput\n" > "$COMMAND_INDEX"
REPORT="$OUT/REPORT.md"
SUMMARY="$OUT/00_SUMMARY.txt"
STEPS="$OUT/00_TEST_STEPS.md"
FEATURE_AUDIT="$OUT/00_FEATURE_AUDIT_SCOPE.md"
PKG_TABLE="$OUT/package_table.tsv"
MEDIA_ALL="$OUT/media_session_all.txt"
NOTIF_ALL="$OUT/notification_all.txt"
AUDIO_ALL="$OUT/audio_all.txt"
FOCUS_ALL="$OUT/window_focus_all.txt"
LOGCAT_PID=""
LOGCAT_ALL_PID=""
START_EPOCH="$(date +%s)"
END_EPOCH=$(( START_EPOCH + DURATION_SECONDS ))

rm -f "$STOP_FILE" 2>/dev/null

cleanup() {
  if [ -n "$REPORT" ] && [ -d "$OUT" ] && [ ! -s "$REPORT" ]; then
    { echo "# TS18 Auxio/VLC diagnostics report"; echo; echo "- Status: partial/aborted"; echo "- Session: ${SESSION_ID:-unknown}"; echo "- Ended: $(human_ts)"; echo "- See: run.log and commands/command-index.tsv"; } > "$REPORT" 2>/dev/null || true
  fi
  log "cleanup start"
  [ -n "$LOGCAT_PID" ] && kill "$LOGCAT_PID" >/dev/null 2>&1
  [ -n "$LOGCAT_ALL_PID" ] && kill "$LOGCAT_ALL_PID" >/dev/null 2>&1
  rm -rf "$LOCK_DIR" >/dev/null 2>&1
  log "cleanup end"
}
trap cleanup EXIT INT TERM

###############################################################################
# Human test instructions written into output
###############################################################################

cat > "$STEPS" <<EOF2
# Manual test steps for this capture

Session: $SESSION_ID
Started: $(human_ts)

During the capture window:

1. Start Auxio-TS playback from the exact APK/variant you want tested.
2. On DoFun home/window widget, observe:
   - title/artist shown or blank;
   - album art shown, placeholder, stale, or crash;
   - progress shown/moving or frozen;
   - play/pause command works;
   - next/previous command works;
   - tapping window opens the expected app/activity.
3. Test Auxio-TS storage/source features and say/write down the exact source mode/path used:
   - MediaStore/system source;
   - SAF source / DocumentsUI picker;
   - DirectFS/manual path if installed;
   - /storage/usbdisk0 or /storage/usbdisk1;
   - /storage/usbdiskN/Music, Download, or custom folder;
   - USB unplug/replug if safe.
4. Test earliest-start readiness:
   - after reboot, note whether Auxio autostarted, restored queue, restored overlay, and published media session before DoFun was ready.
5. Test floating controls / edge conflict:
   - place floating controls near top/status bar and right navigation/edge drawer areas;
   - try status shade, right-edge navigation/gesture drawer, DoFun home gestures;
   - note whether the overlay is hidden, loses touch, is under SystemUI, or is displaced.
6. Test interruption contexts:
   - pause from DoFun, Auxio, notification, headset/BT controller if available, VLC takeover, radio/NavRadio, reverse/camera if safe, and ACC sleep/wake if available;
   - note any unexpected pause/resume.
7. Switch to VLC and play audio for 2-3 minutes.
8. Return to Auxio-TS and repeat play/pause/next/previous.
9. If testing BTAndroidTS or ts18-intent-bridge, trigger their intended user-visible action once during the capture.
10. Stop early with:
   touch $STOP_FILE

Share the final .tar.gz if possible, or these files:
- REPORT.md
- package_table.tsv
- media_session_all.txt
- notification_all.txt
- audio_all.txt
- logs/logcat_filtered.txt
- snapshots/*/summary.txt
- packages/*/quick-components.txt
EOF2

cat > "$FEATURE_AUDIT" <<'EOF_FEATURE'
# Auxio-TS PR/release audit scope for this capture

This file maps the runtime capture to Auxio-TS PR/release claims that still need exact TS18 evidence.

## Baseline uncertainty

The requested baseline is "since v5.3.0". The accessible `dev` branch currently reports `versionName 5.0.5` / `versionCode 85` in `app/build.gradle`, so the script treats the post-v5.3.0 request as a *feature/change audit scope* rather than assuming a repo tag exists on-device. The installed APK version is captured in `package_table.tsv` and each package dump.

## PR/release descriptions audited into runtime evidence buckets

| Area | Relevant PRs / release-note themes | Runtime evidence captured |
|---|---|---|
| DoFun/Topway launcher compatibility | DoFun fixed slots, `com.tw.media`, `com.tw.music`, Topway activity/service/widget aliases, Topway bridge | package dumps, quick-components, activity/service/broadcast resolver dumps, DoFun package state, logcat Topway/DoFun filters |
| Generic Android media integration | MediaSession, MediaBrowserServiceCompat, MediaStyle notification, VLC comparison | `media_session_all.txt`, `notification_all.txt`, per-snapshot filtered dumps, audio focus/policy dumps |
| Notification crash hardening | RemoteViews/bitmap/fallback icon/large-icon safety, minimized notifications | filtered logcat, notification dumps, crash/dropbox logs, Auxio crash reports |
| Playback stability | autoplay, restore state, play/pause retention, shuffle/current-track retention, queue bounds, seek/next/prev | MediaSession playback state/actions, logcat ExoPlayer/PlaybackState/queue/shuffle markers, audio dumps |
| Slow startup and cached library | cached startup, first-run scan gating, avoiding always-scan, startup library policy | logcat startup/indexing markers, external collector evidence, storage and appdata inventories |
| Storage/source handling | MediaStore, SAF, DocumentsUI, manual path, DirectFS, FilteredFS, USB roots, inaccessible vs empty | storage dumps, `/storage` + `/mnt/media_rw` listings, DocumentsUI package dump, logcat DirectFS/SAF/MediaStore/root markers |
| Root gate and DirectFS | RootStateHolder, bounded `su`, timeout/denied handling, shell quoting, protected-root rejection | Magisk/root probes, module inventory, logcat RootGate/RootState/DirectFS markers, safe root listing probes |
| Widget and zero-ID fallback | DoFun fixed cards, Android AppWidget IDs, widget broadcasts, progress broadcasts | appwidget dumps, logcat widget/Topway broadcast markers, package receiver summaries |
| Overlay/floating controls | overlay permission activity, foreground service, boot restore, bounds/insets, top/status-bar and right-edge/nav gesture conflicts | appops, window/input/display/policy dumps, SurfaceFlinger layer list, settings gesture/navigation keys, logcat Overlay/WindowManager markers |
| Earliest autostart/readiness | BOOT_COMPLETED receiver, overlay boot receiver, service.d timing, app start fallback, media session readiness before DoFun | earlyboot snapshot, boot props, broadcast queues, receiver resolver tables, logcat boot/user-unlocked/media markers |
| Playback interruption contexts | audio-focus loss/duck, becoming noisy, MediaSession command source, Bluetooth, radio/NavRadio, reverse/camera, phone/telecom, power/doze | per-snapshot audio focus/policy, media_session, notification, telecom/bluetooth dumps, logcat AUDIOFOCUS/MediaButton/Topway command markers |
| External TS18 diagnostics | Magisk service.d collector, logcat/dumpsys/package/storage/session evidence | external collector output; no Auxio in-app DiagnosticService |
| Home UI/pills/chips | z-order/clickability fixes, head-unit shortcut chips | window/focus dumps, logcat UI exceptions, manual test observation notes |
| Bluetooth/peripheral work | BTAndroidTS app/module, Android Bluetooth vs Topway Bluetooth separation | package dumps, bluetooth_manager dumps, appops, audio focus logs, com.tw.bt and Android Bluetooth package state |
| Intent bridge / LSPosed/Zygisk | TS18 intent bridge, module state, redirect risks | Magisk module inventory, LSPosed/Zygisk logcat markers, package dumps for bridge app |
| NavRadio+/radio coexistence | NavRadio+, stock radio, DoFun radio widget coexistence | package dumps, audio focus/policy dumps, logcat radio/NavRadio markers |
| Release/CI guardrails | APK identity, variants, package names, release/debug mismatches | package table, package dumps, APK paths, versionName/versionCode, UID/sharedUserId flags |

## Lower-confidence items this capture is meant to prove or disprove

- Whether DoFun reads Auxio through generic active MediaSession ranking, notification MediaStyle, Topway private broadcasts, or a mixture.
- Whether Auxio publishes metadata early enough for DoFun, rather than waiting for artwork/bitmap completion.
- Whether `com.tw.media` behaves better than `org.oxycblt.auxio` in DoFun's widget/window.
- Whether root-gated DirectFS is required for `/storage/usbdiskN` or only raw `/mnt/media_rw` paths.
- Whether DocumentsUI/SAF is now viable on TS18 after module installation.
- Whether notification artwork fixes fully stop TS18 RemoteViews/SystemUI crashes.
- Whether boot/autostart/ACC wake behaviour differs from normal BOOT_COMPLETED.
- Whether BTAndroidTS, intent bridge, NavRadio+, ZLink/Android Auto, or Topway BT/radio services influence media focus/routing.

EOF_FEATURE

###############################################################################
# Package discovery
###############################################################################

DISCOVERED="$(pm list packages 2>/dev/null | sed 's/^package://' | grep -Ei 'auxio|oxycblt|videolan|vlc|dofun|com\.tw\.|navimods|navradio|btandroidts|intentbridge|documents|lsposed|zygisk|magisk|zlink|gearhead|media|bluetooth|radio|gocsdk|slink|s-link|z-link|fota|sprd|unisoc' | sort -u)"
PKGS="$(printf '%s\n%s\n' "$BASE_PACKAGES" "$DISCOVERED" | tr ' ' '\n' | sed '/^$/d' | sort -u | tr '\n' ' ')"

###############################################################################
# Start logcat
###############################################################################

log "session=$SESSION_ID out=$OUT"
log "packages=$PKGS"
log "starting filtered logcat"
(
  echo "### filtered logcat"
  echo "# Started: $(human_ts)"
  echo "# Filter: $LOG_FILTER"
  echo
  logcat -b main,system,crash,events -v threadtime 2>&1 | grep -i -E "$LOG_FILTER"
) >> "$OUT/logs/logcat_filtered.txt" &
LOGCAT_PID="$!"

if [ "$LOGCAT_ALL" = "1" ]; then
  log "starting full logcat"
  (
    echo "### full logcat; may contain private data"
    echo "# Started: $(human_ts)"
    echo
    logcat -b main,system,crash,events -v threadtime 2>&1
  ) >> "$OUT/logs/logcat_all.txt" &
  LOGCAT_ALL_PID="$!"
fi

###############################################################################
# Baseline system and project-source evidence gaps
###############################################################################

cat > "$SUMMARY" <<EOF2
TS18 Auxio media diagnostics
Session: $SESSION_ID
Started: $(human_ts)
Output: $OUT
Duration: $DURATION_SECONDS sec
Interval: $INTERVAL_SECONDS sec
id: $(id 2>/dev/null)
SELinux: $(getenforce 2>/dev/null)
Android: $(getprop ro.build.version.release 2>/dev/null) / SDK $(getprop ro.build.version.sdk 2>/dev/null)
Build display: $(getprop ro.build.display.id 2>/dev/null)
Device: $(getprop ro.product.device 2>/dev/null)
Model: $(getprop ro.product.model 2>/dev/null)
Fingerprint: $(getprop ro.build.fingerprint 2>/dev/null)
EOF2

# Identity/root/system
cmd_to_file "id" "$OUT/system/id.txt" 5 id
cmd_to_file "getenforce" "$OUT/system/getenforce.txt" 5 getenforce
cmd_to_file "getprop all" "$OUT/system/getprop-all.txt" 15 getprop
cmd_to_file "mount" "$OUT/system/mount.txt" 10 mount
cmd_to_file "df -h" "$OUT/system/df-h.txt" 10 df -h
cmd_to_file "ps -A" "$OUT/system/ps-A.txt" 10 ps -A
cmd_to_file "top -n 1" "$OUT/system/top.txt" 10 top -n 1
cmd_to_file "uptime" "$OUT/system/uptime.txt" 5 uptime
cmd_to_file "date" "$OUT/system/date.txt" 5 date
cmd_to_file "settings secure" "$OUT/system/settings-secure.txt" 10 settings list secure
cmd_to_file "settings system" "$OUT/system/settings-system.txt" 10 settings list system
cmd_to_file "settings global" "$OUT/system/settings-global.txt" 10 settings list global
cmd_to_file "pm list packages -f -U" "$OUT/system/pm-list-packages-f-U.txt" 20 pm list packages -f -U
cmd_to_file "cmd package list packages -f -U" "$OUT/system/cmd-package-list-packages-f-U.txt" 20 cmd package list packages -f -U
cmd_to_file "cmd package list features" "$OUT/system/cmd-package-list-features.txt" 10 cmd package list features
cmd_to_file "cmd package list libraries" "$OUT/system/cmd-package-list-libraries.txt" 10 cmd package list libraries
cmd_to_file "media/document intent resolvers" "$OUT/system/media-document-intent-resolvers.txt" 20 sh -c "for a in android.intent.action.MUSIC_PLAYER android.intent.action.VIEW android.intent.action.OPEN_DOCUMENT android.media.action.MEDIA_PLAY_FROM_SEARCH android.intent.action.MEDIA_BUTTON; do echo ==== $a ====; cmd package query-activities -a $a 2>&1 || true; cmd package query-intent-activities -a $a 2>&1 || true; done"
cmd_to_file "dumpsys services list" "$OUT/system/dumpsys-services-list.txt" 10 dumpsys -l
cmd_to_file "cmd overlay list" "$OUT/system/cmd-overlay-list.txt" 10 cmd overlay list
cmd_to_file "cmd role holders" "$OUT/system/cmd-role.txt" 10 cmd role holders android.app.role.HOME
cmd_to_file "service list" "$OUT/system/service-list.txt" 10 service list

# Init/vendor/Topway state
{
  echo "### Topway/Unisoc/vendor/service property subset"
  echo "# Time: $(human_ts)"
  getprop 2>/dev/null | grep -Ei 'tw\.|topway|dofun|fota|mcu|can|car|radio|audio|bt|bluetooth|zlink|tlink|goc|slink|s-link|z-link|acc|sleep|wake|fan|thermal|ylog|debug|uis|8581|sprd|unisoc|sp9863|storage' | sort
} > "$OUT/vendor/topway-unisoc-props.txt"

{
  echo "### Topway/Unisoc/vendor processes and init services"
  echo "# Time: $(human_ts)"
  echo
  ps -A -o USER,PID,PPID,VSZ,RSS,STAT,NAME,ARGS 2>/dev/null | grep -Ei 'com\.tw|com\.dofun|zlink|tlink|sprd|unisoc|fota|radio|bluetooth|audio|car|mcu|can|goc|slink|navimods|auxio|vlc|videolan' || true
  echo
  echo "===== init services ====="
  getprop 2>/dev/null | grep -Ei '^\[init\.svc\..*(tw|sprd|unisoc|fota|radio|bluetooth|audio|car|mcu|can|ylog|debug|zlink|goc)' | sort || true
} > "$OUT/vendor/topway-unisoc-processes.txt"

# Magisk/modules/root state
{
  echo "### Magisk/root/module state"
  echo "# Time: $(human_ts)"
  echo
  echo "id:"; id 2>&1
  echo; echo "su -v:"; su -v 2>&1 || true
  echo; echo "magisk -v:"; magisk -v 2>&1 || true
  echo; echo "/data/adb:"; ls -laZ /data/adb 2>&1 || ls -la /data/adb 2>&1 || true
  echo; echo "/data/adb/modules:"; find /data/adb/modules -maxdepth 3 -type f -o -type d 2>/dev/null | sort | sed -n '1,500p'
  echo; echo "/data/adb/service.d:"; ls -laZ /data/adb/service.d 2>&1 || ls -la /data/adb/service.d 2>&1 || true
  echo; echo "/data/adb/post-fs-data.d:"; ls -laZ /data/adb/post-fs-data.d 2>&1 || ls -la /data/adb/post-fs-data.d 2>&1 || true
  echo; echo "module.prop files:"; find /data/adb/modules -name module.prop -maxdepth 3 -print -exec cat {} \; 2>/dev/null
} > "$OUT/magisk/magisk-modules-state.txt"


if [ "$CAPTURE_SAFE_ROOT_PROBES" = "1" ]; then
  {
    echo "### Safe root probes"
    echo "# Time: $(human_ts)"
    echo "# Read-only bounded probes only. No partition writes, no remounts."
    echo
    echo "su -c id:"
    su -c id 2>&1 || true
    echo
    echo "su -c getenforce:"
    su -c getenforce 2>&1 || true
    echo
    echo "su -c ls protected/public roots:"
    su -c 'for p in / /system /vendor /data /storage /storage/emulated/0 /mnt/media_rw /mnt/runtime/default /mnt/runtime/write; do echo "--- $p"; ls -ldZ "$p" 2>&1 || ls -ld "$p" 2>&1; done' 2>&1 || true
    echo
    echo "su -c storage root depth check:"
    su -c 'find /storage /mnt/media_rw -maxdepth 2 -type d 2>/dev/null | sort | sed -n "1,200p"' 2>&1 || true
  } > "$OUT/magisk/safe-root-probes.txt"
fi

# Storage/source state
cmd_to_file "sm list-volumes all" "$OUT/storage/sm-list-volumes-all.txt" 10 sm list-volumes all
cmd_to_file "sm list-disks" "$OUT/storage/sm-list-disks.txt" 10 sm list-disks
cmd_to_file "cmd storage volumes" "$OUT/storage/cmd-storage-volumes.txt" 10 cmd storage volumes
cmd_to_file "mount storage subset" "$OUT/storage/mount-storage-subset.txt" 10 sh -c "mount | grep -Ei 'storage|media_rw|vold|usb|sdcard|fuse|fat|exfat|ntfs|emulated|obb' || true"
cmd_to_file "df storage subset" "$OUT/storage/df-storage-subset.txt" 10 sh -c "df -h | grep -Ei 'storage|media_rw|vold|usb|sdcard|fuse|fat|emulated|/data|/system|/vendor' || true"
cmd_to_file "ls /storage" "$OUT/storage/ls-storage.txt" 10 ls -laZ /storage
cmd_to_file "ls /mnt/media_rw" "$OUT/storage/ls-mnt-media-rw.txt" 10 ls -laZ /mnt/media_rw
cmd_to_file "find public storage roots" "$OUT/storage/find-public-storage-roots.txt" 20 sh -c "find /storage /mnt/media_rw -maxdepth 4 -type d 2>/dev/null | sort"
cmd_to_file "MediaProvider package" "$OUT/storage/dumpsys-package-mediaprovider.txt" 10 dumpsys package com.android.providers.media
cmd_to_file "DocumentsUI package" "$OUT/storage/dumpsys-package-documentsui.txt" 10 sh -c "dumpsys package com.android.documentsui 2>&1; echo; dumpsys package com.google.android.documentsui 2>&1"

# Media/display/power system state
for svc in display input power battery alarm jobscheduler deviceidle procstats meminfo cpuinfo appwidget notification media_session audio media.audio_flinger media.audio_policy media.player media.metrics activity window SurfaceFlinger bluetooth_manager connectivity netstats usagestats; do
  safe="$(safe_name "$svc")"
  dumpsys_file "$svc" "$OUT/system/dumpsys-$safe.txt"
done

if [ "$CAPTURE_DMESG" = "1" ]; then
  cmd_to_file "dmesg" "$OUT/system/dmesg.txt" 15 dmesg
fi
if [ "$CAPTURE_BUGREPORT" = "1" ]; then
  cmd_to_file "bugreportz" "$OUT/system/bugreportz.txt" 120 bugreportz
fi

if [ "$CAPTURE_CRASH_LOGS" = "1" ]; then
  cmd_to_file "logcat crash buffer snapshot" "$OUT/logs/logcat_crash_buffer.txt" 15 logcat -b crash -d -v threadtime
  cmd_to_file "logcat events crash/anr snapshot" "$OUT/logs/logcat_events_crash_anr.txt" 15 sh -c "logcat -b events -d -v threadtime | grep -i -E 'am_crash|am_anr|wm_|media|audio|auxio|tw|dofun|vlc|videolan' | tail -n 2000 || true"
  cmd_to_file "tombstones listing" "$OUT/system/tombstones-listing.txt" 10 sh -c "ls -laZ /data/tombstones 2>&1 || ls -la /data/tombstones 2>&1 || true"
  cmd_to_file "anr traces listing" "$OUT/system/anr-listing.txt" 10 sh -c "ls -laZ /data/anr 2>&1 || ls -la /data/anr 2>&1 || true"
fi
if [ "$CAPTURE_DROPBOX" = "1" ]; then
  cmd_to_file "dropbox recent crash/anr entries" "$OUT/logs/dropbox-crash-anr.txt" 30 sh -c "dumpsys dropbox --print data_app_crash 2>&1; dumpsys dropbox --print system_app_crash 2>&1; dumpsys dropbox --print data_app_anr 2>&1; dumpsys dropbox --print system_app_anr 2>&1; dumpsys dropbox --print SYSTEM_TOMBSTONE 2>&1"
fi

###############################################################################
# Package-level dumps
###############################################################################

printf 'package	installed	versionName	versionCode	uid	flags	path
' > "$PKG_TABLE"

for pkg in $PKGS; do
  safe="$(safe_name "$pkg")"
  dir="$OUT/packages/$safe"
  mkdir -p "$dir"
  if ! pkg_installed "$pkg"; then
    echo "$pkg	missing					" >> "$PKG_TABLE"
    echo "Package not installed: $pkg" > "$dir/MISSING.txt"
    continue
  fi

  cmd_to_file "pm path $pkg" "$dir/pm-path.txt" 10 pm path "$pkg"
  dumpsys_file "package $pkg" "$dir/dumpsys-package.txt" package "$pkg"
  cmd_to_file "appops $pkg" "$dir/appops.txt" 10 cmd appops get "$pkg"
  cmd_to_file "dumpsys meminfo $pkg" "$dir/meminfo.txt" 10 dumpsys meminfo "$pkg"
  cmd_to_file "dumpsys batterystats $pkg" "$dir/batterystats.txt" 10 dumpsys batterystats "$pkg"
  cmd_to_file "cmd package resolve activities $pkg" "$dir/resolve-intents.txt" 12 sh -c "cmd package resolve-activity --brief $pkg 2>&1 || true; echo; cmd package query-activities -a android.intent.action.MAIN -c android.intent.category.LAUNCHER 2>/dev/null | grep -i -A20 -B3 '$pkg' || true"

  apk_paths="$(pm path "$pkg" 2>/dev/null | sed 's/^package://')"
  {
    echo "### APK paths and file labels"
    echo "# Time: $(human_ts)"
    echo "$apk_paths" | while read ap; do
      [ -n "$ap" ] || continue
      echo; echo "APK: $ap"
      ls -laZ "$ap" 2>&1 || ls -la "$ap" 2>&1
      if have_cmd sha256sum; then sha256sum "$ap" 2>/dev/null; fi
      if have_cmd md5sum; then md5sum "$ap" 2>/dev/null; fi
    done
  } > "$dir/apk-paths-ls-hash.txt"

  {
    echo "### Quick component/permission summary: $pkg"
    echo "# Time: $(human_ts)"
    echo
    grep -E 'Package \[|userId=|sharedUserId=|versionName=|versionCode=|codePath=|resourcePath=|primaryCpuAbi=|pkgFlags=|privateFlags=|requested permissions:|install permissions:|runtime permissions:|User 0:|enabled=|Activity Resolver Table:|Service Resolver Table:|Receiver Resolver Table:|Provider Resolver Table:|android\.media\.browse|MediaBrowserService|MEDIA_BUTTON|MEDIA_PLAY_FROM_SEARCH|MUSIC_PLAYER|APP_MUSIC|LAUNCHER|VIEW|AppWidget|MediaSession|NotificationListener|BIND_NOTIFICATION_LISTENER_SERVICE|SYSTEM_ALERT_WINDOW|FOREGROUND_SERVICE|READ_EXTERNAL_STORAGE|READ_MEDIA_AUDIO|QUERY_ALL_PACKAGES' "$dir/dumpsys-package.txt" 2>/dev/null
  } > "$dir/quick-components.txt"

  verName="$(grep -m1 'versionName=' "$dir/dumpsys-package.txt" | sed 's/.*versionName=//' | awk '{print $1}' | tr -d '\r')"
  verCode="$(grep -m1 'versionCode=' "$dir/dumpsys-package.txt" | sed 's/.*versionCode=//' | awk '{print $1}' | tr -d '\r')"
  uid="$(grep -m1 'userId=' "$dir/dumpsys-package.txt" | sed 's/.*userId=//' | awk '{print $1}' | tr -d '\r')"
  flags="$(grep -m1 'pkgFlags=' "$dir/dumpsys-package.txt" | sed 's/.*pkgFlags=//' | awk '{print $1}' | tr -d '\r')"
  firstPath="$(echo "$apk_paths" | head -n 1)"
  echo "$pkg	installed	$verName	$verCode	$uid	$flags	$firstPath" >> "$PKG_TABLE"

  # Auxio app data inventory only; prefs copy off by default.
  if grep -q 'org.oxycblt.auxio' "$dir/dumpsys-package.txt" 2>/dev/null; then
    mkdir -p "$OUT/appdata/$safe"
    {
      echo "### Auxio data inventory"
      echo "# Time: $(human_ts)"
      ls -laZ "/data/user/0/$pkg" 2>&1 || ls -la "/data/user/0/$pkg" 2>&1 || true
      echo; echo "shared_prefs:"; ls -laZ "/data/user/0/$pkg/shared_prefs" 2>&1 || ls -la "/data/user/0/$pkg/shared_prefs" 2>&1 || true
      echo; echo "databases:"; ls -laZ "/data/user/0/$pkg/databases" 2>&1 || ls -la "/data/user/0/$pkg/databases" 2>&1 || true
      echo; echo "files/cache no-recursive:"; ls -laZ "/data/user/0/$pkg/files" "/data/user/0/$pkg/cache" 2>&1 || true
    } > "$OUT/appdata/$safe/inventory.txt"
    if [ "$COPY_AUXIO_PREFS" = "1" ]; then
      mkdir -p "$OUT/appdata/$safe/shared_prefs"
      cp -a "/data/user/0/$pkg/shared_prefs/"*.xml "$OUT/appdata/$safe/shared_prefs/" 2>/dev/null || true
    fi
    if [ "$CAPTURE_SQLITE_LIST" = "1" ] && have_cmd sqlite3; then
      for db in /data/user/0/$pkg/databases/*; do
        [ -f "$db" ] || continue
        b="$(safe_name "$(basename "$db")")"
        sqlite3 "$db" '.tables' > "$OUT/appdata/$safe/sqlite-tables-$b.txt" 2>&1 || true
      done
    fi
    if [ "$COPY_AUXIO_APP_REPORTS" = "1" ]; then
      mkdir -p "$OUT/appdata/$safe/reports"
      for src in \
        "/data/user/0/$pkg/files/crash-reports" \
        "/data/user/0/$pkg/files/diagnostics" \
        "/data/user/0/$pkg/files/reports" \
        "/storage/emulated/0/Android/data/$pkg/files/crash-reports" \
        "/storage/emulated/0/Android/data/$pkg/files/diagnostics" \
        "/storage/emulated/0/Android/data/$pkg/files/reports"; do
        if [ -d "$src" ]; then
          base="$(safe_name "$src")"
          mkdir -p "$OUT/appdata/$safe/reports/$base"
          find "$src" -maxdepth 3 -type f -size -2048k 2>/dev/null | while read f; do
            cp -p "$f" "$OUT/appdata/$safe/reports/$base/$(safe_name "$(basename "$f")")" 2>/dev/null || true
          done
        fi
      done
    fi
  fi
done


# Combined quick component summaries for report heuristics.
QUICK_ALL="$OUT/summary/all-quick-components.txt"
cat "$OUT/packages"/*/quick-components.txt > "$QUICK_ALL" 2>/dev/null || true

###############################################################################
# Auxio in-app diagnostics intentionally not started
###############################################################################

# Auxio-TS in-app diagnostics were abandoned.  This external Magisk/service.d
# collector is now the authoritative diagnostics mechanism for TS18 release
# evidence.  Do not start app-side DiagnosticService here; doing so confuses
# capture ownership and reintroduces app code that should be removed from
# Auxio-TS.  Capture package/logcat/dumpsys/storage/media-session evidence
# externally instead.
mkdir -p "$OUT/auxio" 2>/dev/null || true
{
  echo "### Auxio in-app diagnostics"
  echo "status=SKIPPED_ABANDONED"
  echo "reason=External Magisk/service.d collector is authoritative; app DiagnosticService is to be removed."
  echo "time=$(human_ts)"
} > "$OUT/auxio/in-app-diagnostics-skipped.txt"


###############################################################################
# Deep focus-area diagnostics
###############################################################################

if [ "$CAPTURE_DEEP_SOURCE_DIAGS" = "1" ]; then
  log "deep source/path diagnostics"
  {
    echo "### SAF / file path / music source diagnostics"
    echo "# Time: $(human_ts)"
    echo "# SOURCE_CANDIDATE_PATHS=$SOURCE_CANDIDATE_PATHS"
    echo
    echo "## Candidate path matrix"
    for root in $SOURCE_CANDIDATE_PATHS; do
      echo
      echo "--- $root"
      if [ -e "$root" ]; then
        echo "exists=yes"
        ls -ldZ "$root" 2>&1 || ls -ld "$root" 2>&1
        echo "readlink=$(readlink -f "$root" 2>/dev/null)"
        echo "stat:"; stat "$root" 2>&1 || true
        echo "audio sample:"
        find "$root" -maxdepth 3 -type f \( -iname '*.mp3' -o -iname '*.flac' -o -iname '*.m4a' -o -iname '*.aac' -o -iname '*.ogg' -o -iname '*.wav' \) 2>/dev/null | sed -n '1,60p'
        echo "dir sample:"; find "$root" -maxdepth 2 -type d 2>/dev/null | sed -n '1,80p'
      else
        echo "exists=no"
      fi
    done
  } > "$OUT/source_paths/source-candidate-path-matrix.txt"

  cmd_to_file "SAF/DocumentsUI intent resolver matrix" "$OUT/source_paths/saf-documentsui-resolvers.txt" 20 sh -c "for a in android.intent.action.OPEN_DOCUMENT android.intent.action.OPEN_DOCUMENT_TREE android.intent.action.GET_CONTENT android.intent.action.VIEW; do echo ==== action:$a audio ====; cmd package query-activities -a $a -t 'audio/*' 2>&1 || true; echo ==== action:$a any ====; cmd package query-activities -a $a 2>&1 || true; done"
  cmd_to_file "persisted URI grants" "$OUT/source_paths/persisted-uri-grants.txt" 20 sh -c "dumpsys package | grep -i -A8 -B4 'UriPermission\|content://\|tree/' | grep -Ei 'auxio|oxycblt|com.tw.media|com.tw.music|content://|tree/|UriPermission|modeFlags|persisted' || true"
  cmd_to_file "Auxio path/source grep from app data" "$OUT/source_paths/auxio-source-path-grep.txt" 30 sh -c "for p in $PKGS; do case \"$p\" in *auxio*|org.oxycblt*|com.tw.media|com.tw.music) echo ==== $p ====; for d in /data/user/0/$p/shared_prefs /data/user/0/$p/files /storage/emulated/0/Android/data/$p/files; do [ -d \"$d\" ] && find \"$d\" -maxdepth 4 -type f -size -2048k 2>/dev/null | xargs grep -I -n -E '/storage|/mnt/media_rw|content://|tree/|usbdisk|Music|Download|DirectFS|MediaStore|SAF|LocationMode|source|path' 2>/dev/null | sed -n '1,250p'; done;; esac; done"
  cmd_to_file "MediaStore audio rows" "$OUT/source_paths/mediastore-audio-rows.txt" 30 sh -c "if [ '$CAPTURE_MEDIASTORE_ROWS' = '1' ]; then content query --uri content://media/external/audio/media --projection _id,_data,title,artist,album,duration --limit 80 2>&1 || true; echo; content query --uri content://media/external/file --projection _id,_data,mime_type --where \"mime_type LIKE 'audio/%'\" --limit 80 2>&1 || true; else echo disabled; fi"
fi

if [ "$CAPTURE_AUTOSTART_DIAGS" = "1" ]; then
  log "autostart/readiness diagnostics"
  {
    echo "### Autostart / boot readiness diagnostics"
    echo "# Time: $(human_ts)"
    echo
    echo "## boot/readiness properties"
    for k in sys.boot_completed dev.bootcomplete service.bootanim.exit init.svc.bootanim init.svc.zygote init.svc.surfaceflinger init.svc.system_server init.svc.audioserver init.svc.media bootreceiver.enable ro.bootmode persist.sys.boot.reason sys.user.0.ce_available; do echo "$k=$(getprop $k 2>/dev/null)"; done
    echo
    echo "## uptime/proc"
    cat /proc/uptime 2>/dev/null
    echo
    echo "## earlyboot folder"
    ls -la "$BASE_OUT/_early_boot" 2>/dev/null || true
  } > "$OUT/autostart/readiness-baseline.txt"
  cmd_to_file "BOOT_COMPLETED/quickboot receiver resolvers" "$OUT/autostart/boot-receiver-resolvers.txt" 25 sh -c "for a in android.intent.action.LOCKED_BOOT_COMPLETED android.intent.action.BOOT_COMPLETED android.intent.action.USER_UNLOCKED android.intent.action.QUICKBOOT_POWERON android.intent.action.MY_PACKAGE_REPLACED com.htc.intent.action.QUICKBOOT_POWERON; do echo ==== $a ====; cmd package query-receivers -a $a 2>&1 | grep -Ei -A8 -B2 'auxio|oxycblt|com.tw.media|com.tw.music|dofun|tw|overlay|CarOverlay|BootReceiver' || true; done"
  cmd_to_file "activity broadcasts boot queues" "$OUT/autostart/activity-broadcasts-boot.txt" 25 sh -c "dumpsys activity broadcasts | grep -Ei -A15 -B10 'BOOT_COMPLETED|LOCKED_BOOT_COMPLETED|USER_UNLOCKED|QUICKBOOT|auxio|oxycblt|com.tw.media|com.tw.music|dofun|tw|CarOverlay|BootReceiver' || true"
  cmd_to_file "jobscheduler auxio/media boot" "$OUT/autostart/jobscheduler-auxio-media.txt" 20 sh -c "dumpsys jobscheduler | grep -Ei -A30 -B10 'auxio|oxycblt|com.tw.media|com.tw.music|dofun|tw|media|bluetooth|radio' || true"
  cmd_to_file "alarm auxio/media boot" "$OUT/autostart/alarm-auxio-media.txt" 20 sh -c "dumpsys alarm | grep -Ei -A20 -B8 'auxio|oxycblt|com.tw.media|com.tw.music|dofun|tw|media|bluetooth|radio' || true"
fi

if [ "$CAPTURE_OVERLAY_EDGE_DIAGS" = "1" ]; then
  log "overlay/status/nav/edge diagnostics"
  cmd_to_file "overlay appops all target packages" "$OUT/overlay/overlay-appops.txt" 20 sh -c "for p in $PKGS; do pm path $p >/dev/null 2>&1 || continue; echo ==== $p ====; cmd appops get $p SYSTEM_ALERT_WINDOW 2>&1 || true; cmd appops get $p android:system_alert_window 2>&1 || true; done"
  cmd_to_file "wm/display/insets settings" "$OUT/overlay/display-window-insets-settings.txt" 20 sh -c "echo 'wm size:'; wm size 2>&1; echo 'wm density:'; wm density 2>&1; echo 'wm overscan:'; wm overscan 2>&1; echo; echo 'gesture/nav settings:'; settings list secure | grep -Ei 'navigation|gesture|back|edge|assistant|immersive|systemui|status|nav' || true; settings list system | grep -Ei 'navigation|gesture|edge|status|nav|immersive|systemui' || true; settings list global | grep -Ei 'navigation|gesture|edge|status|nav|immersive|policy_control|systemui' || true"
  cmd_to_file "window overlay/status/nav focus" "$OUT/overlay/window-overlay-status-nav.txt" 25 sh -c "dumpsys window windows | grep -Ei -A25 -B10 'Application Overlay|TYPE_APPLICATION_OVERLAY|SYSTEM_ALERT|CarFloating|Floating|Overlay|StatusBar|NavigationBar|Gesture|Edge|Drawer|auxio|oxycblt|com.tw.media|com.tw.music|dofun|mCurrentFocus|mFocusedApp|Touchable|touchableRegion|layout|frame=' || true"
  cmd_to_file "SurfaceFlinger layer list" "$OUT/overlay/surfaceflinger-layers.txt" 15 sh -c "dumpsys SurfaceFlinger --list 2>&1 | grep -Ei 'auxio|oxycblt|com.tw.media|com.tw.music|dofun|StatusBar|NavigationBar|Gesture|Overlay|Floating|SurfaceView|ExoPlayer' || dumpsys SurfaceFlinger --list 2>&1 | sed -n '1,250p'"
  cmd_to_file "input devices/touch/gesture" "$OUT/overlay/input-touch-devices.txt" 20 sh -c "dumpsys input | grep -Ei -A30 -B10 'touch|gesture|navigation|display|pointer|InputDevice|Viewport|SurfaceOrientation|1024|600|1280|720' || true; echo; getevent -lp 2>/dev/null | grep -Ei -A20 -B5 'touch|ABS_MT|KEY_BACK|KEY_HOME|KEY_MENU|event' || true"
fi

if [ "$CAPTURE_PLAYBACK_INTERRUPT_DIAGS" = "1" ]; then
  log "playback interruption diagnostics baseline"
  cmd_to_file "audio focus baseline" "$OUT/interruptions/audio-focus-baseline.txt" 15 sh -c "dumpsys audio | grep -Ei -A50 -B15 'Focus|focus stack|AudioFocus|MediaFocusControl|duck|loss|gain|client|uid|playback|ringer|sco|bluetooth|becoming noisy' || true"
  cmd_to_file "telecom/phone interruption baseline" "$OUT/interruptions/telecom-baseline.txt" 15 sh -c "dumpsys telecom 2>&1 | grep -Ei -A40 -B10 'call|ring|audio|route|bluetooth|speaker|state' || true; echo; dumpsys telephony.registry 2>&1 | grep -Ei -A30 -B10 'call|service|data|signal' || true"
  cmd_to_file "bluetooth/radio/media interruption baseline" "$OUT/interruptions/bluetooth-radio-media-baseline.txt" 25 sh -c "dumpsys bluetooth_manager 2>&1; echo; dumpsys media_session 2>&1; echo; dumpsys audio 2>&1 | grep -Ei -A40 -B10 'focus|player|track|uid|client|policy|device|route' || true"
  cmd_to_file "recent interruption log markers" "$OUT/interruptions/recent-log-interruptions.txt" 15 sh -c "logcat -b main,system,events,crash -d -v threadtime | grep -i -E 'AUDIOFOCUS|AudioFocus|focus loss|LOSS_TRANSIENT|LOSS_TRANSIENT_CAN_DUCK|GAIN|duck|becoming noisy|MEDIA_BUTTON|play/pause|pause|resume|stop|interruption|phone|telecom|bluetooth|sco|a2dp|NavRadio|radio|reverse|camera|auxio|ExoPlayer|PlaybackState|MediaSession' | tail -n 2500 || true"
fi

###############################################################################
# Snapshots
###############################################################################

snapshot_summary() {
  sdir="$1"
  sout="$sdir/summary.txt"
  {
    echo "Snapshot: $(basename "$sdir")"
    echo "Time: $(human_ts)"
    echo
    echo "Focused window/activity:"
    grep -E 'mCurrentFocus|mFocusedApp|topResumedActivity|ResumedActivity|mHoldScreenWindow|ACTIVITY ' "$sdir/window.txt" 2>/dev/null | head -n 80
    echo
    echo "Media session package hits:"
    for p in $PKGS; do
      c="$(maybe_grep_count "$p" "$sdir/media_session.txt")"
      [ "$c" -gt 0 ] 2>/dev/null && echo "  $p: $c"
    done
    echo
    echo "Notification package hits:"
    for p in $PKGS; do
      c="$(maybe_grep_count "$p" "$sdir/notification.txt")"
      [ "$c" -gt 0 ] 2>/dev/null && echo "  $p: $c"
    done
    echo
    echo "Audio/focus hints:"
    grep -i -E 'focus|playback|player|uid|AudioFocus|MediaFocusControl|mPlayback|mFocusStack|AudioTrack|client|session' "$sdir/audio.txt" 2>/dev/null | head -n 160
    echo
    echo "Recent filtered log errors:"
    tail -n 300 "$OUT/logs/logcat_filtered.txt" 2>/dev/null | grep -i -E 'FATAL|ANR|Exception|SecurityException|RemoteServiceException|RemoteViews|Bitmap|denied|timeout|failed|not allowed|Bad notification' | tail -n 80
    echo
    echo "Source/path hints:"
    tail -n 300 "$OUT/logs/logcat_filtered.txt" 2>/dev/null | grep -i -E 'DirectFS|FilteredFS|LocationMode|LocationsDialog|SAF|DocumentsUI|OPEN_DOCUMENT|MediaStore|usbdisk|media_rw|/storage|content://|tree/' | tail -n 80
    echo
    echo "Overlay/edge hints:"
    [ -f "$sdir/overlay-window-context.txt" ] && grep -i -E 'CarFloating|Floating|Overlay|StatusBar|NavigationBar|Gesture|Edge|touchableRegion|mCurrentFocus|frame=' "$sdir/overlay-window-context.txt" | head -n 120
    echo
    echo "Playback interruption hints:"
    [ -f "$sdir/playback-interruption-context.txt" ] && grep -i -E 'Focus|AudioFocus|loss|duck|gain|client|uid|PlaybackState|state=|telecom|bluetooth|route' "$sdir/playback-interruption-context.txt" | head -n 160
  } > "$sout"
}

take_snapshot() {
  idx="$1"
  t="$(ts)"
  sdir="$OUT/snapshots/$(printf '%03d' "$idx")_$t"
  mkdir -p "$sdir"
  log "snapshot $idx"

  dumpsys_file "media_session" "$sdir/media_session.txt" media_session
  section "$MEDIA_ALL" "SNAPSHOT $idx media_session"
  cat "$sdir/media_session.txt" >> "$MEDIA_ALL"

  dumpsys_file "notification" "$sdir/notification.txt" notification
  section "$NOTIF_ALL" "SNAPSHOT $idx notification"
  cat "$sdir/notification.txt" >> "$NOTIF_ALL"

  dumpsys_file "audio" "$sdir/audio.txt" audio
  section "$AUDIO_ALL" "SNAPSHOT $idx audio"
  cat "$sdir/audio.txt" >> "$AUDIO_ALL"

  dumpsys_file "media.audio_flinger" "$sdir/audio-flinger.txt" media.audio_flinger
  dumpsys_file "media.audio_policy" "$sdir/audio-policy.txt" media.audio_policy
  dumpsys_file "window" "$sdir/window.txt" windows
  section "$FOCUS_ALL" "SNAPSHOT $idx window-focus"
  grep -E 'mCurrentFocus|mFocusedApp|topResumedActivity|ResumedActivity|mHoldScreenWindow|ACTIVITY ' "$sdir/window.txt" >> "$FOCUS_ALL" 2>/dev/null
  dumpsys_file "activity services" "$sdir/activity-services.txt" activity services
  dumpsys_file "activity processes" "$sdir/activity-processes.txt" activity processes
  dumpsys_file "appwidget" "$sdir/appwidget.txt" appwidget
  dumpsys_file "power" "$sdir/power.txt" power
  if [ "$CAPTURE_PLAYBACK_INTERRUPT_DIAGS" = "1" ]; then
    cmd_to_file "snapshot playback interruption context" "$sdir/playback-interruption-context.txt" 15 sh -c "echo '### audio focus'; dumpsys audio | grep -Ei -A60 -B20 'Focus|AudioFocus|focus stack|MediaFocusControl|duck|loss|gain|client|uid|player|playback|route|device' || true; echo; echo '### media session focused packages'; dumpsys media_session | grep -Ei -A90 -B25 'auxio|oxycblt|com.tw.media|com.tw.music|videolan|vlc|NavRadio|radio|PlaybackState|state=|metadata|actions' || true; echo; echo '### telecom'; dumpsys telecom 2>&1 | grep -Ei -A30 -B10 'call|audio|route|state' || true"
  fi
  if [ "$CAPTURE_OVERLAY_EDGE_DIAGS" = "1" ]; then
    cmd_to_file "snapshot overlay/window context" "$sdir/overlay-window-context.txt" 15 sh -c "dumpsys window windows | grep -Ei -A25 -B10 'Application Overlay|TYPE_APPLICATION_OVERLAY|CarFloating|Floating|Overlay|StatusBar|NavigationBar|Gesture|Edge|Drawer|auxio|oxycblt|com.tw.media|com.tw.music|mCurrentFocus|Touchable|touchableRegion|frame=' || true"
  fi

  {
    echo "### PIDs/status per package"
    echo "# Time: $(human_ts)"
    for pkg in $PKGS; do
      echo; echo "## $pkg"
      pidof "$pkg" 2>/dev/null || true
      ps -A 2>/dev/null | grep "$pkg" || true
    done
  } > "$sdir/pids.txt"

  for pkg in $PKGS; do
    pkg_installed "$pkg" || continue
    safe="$(safe_name "$pkg")"
    mkdir -p "$sdir/pkg-$safe"
    cmd_to_file "media_session filtered $pkg" "$sdir/pkg-$safe/media-session-filtered.txt" 8 sh -c "dumpsys media_session | grep -i -A 100 -B 35 '$pkg' || true"
    cmd_to_file "notification filtered $pkg" "$sdir/pkg-$safe/notification-filtered.txt" 8 sh -c "dumpsys notification | grep -i -A 100 -B 35 '$pkg' || true"
    cmd_to_file "activity services filtered $pkg" "$sdir/pkg-$safe/activity-services-filtered.txt" 8 sh -c "dumpsys activity services | grep -i -A 80 -B 20 '$pkg' || true"
    cmd_to_file "meminfo $pkg" "$sdir/pkg-$safe/meminfo.txt" 8 dumpsys meminfo "$pkg"
  done

  snapshot_summary "$sdir"
}

idx=1
while :; do
  now="$(date +%s)"
  [ "$now" -ge "$END_EPOCH" ] && { log "duration reached"; break; }
  [ -f "$STOP_FILE" ] && { log "stop file observed"; break; }
  take_snapshot "$idx"
  idx=$((idx + 1))
  slept=0
  while [ "$slept" -lt "$INTERVAL_SECONDS" ]; do
    [ -f "$STOP_FILE" ] && break
    sleep 1
    slept=$((slept + 1))
  done
done

###############################################################################
# Final captures and bounded log tail
###############################################################################

log "final capture"
for svc in media_session notification audio media.audio_flinger media.audio_policy media.player activity window appwidget power meminfo procstats jobscheduler alarm input display bluetooth_manager telecom deviceidle usagestats; do
  safe="$(safe_name "$svc")"
  dumpsys_file "$svc" "$OUT/final-dumpsys-$safe.txt"
done

# Trim filtered log if too large by adding a tail copy for quick sharing.
tail -n "$MAX_LOG_LINES" "$OUT/logs/logcat_filtered.txt" > "$OUT/logs/logcat_filtered_tail.txt" 2>/dev/null || true

###############################################################################
# Report
###############################################################################

feature_status() {
  _name="$1"; _pattern="$2"; _file="$3"
  _count="$(maybe_grep_count "$_pattern" "$_file")"
  if [ "$_count" -gt 0 ] 2>/dev/null; then
    echo "PASS-ish ($_count hits)"
  else
    echo "UNKNOWN/FAIL (0 hits)"
  fi
}

{
  echo "# TS18 Auxio/VLC diagnostics report"
  echo
  echo "- Session: $SESSION_ID"
  echo "- Started: $(date -d "@$START_EPOCH" '+%Y-%m-%d %H:%M:%S %z' 2>/dev/null || echo "$START_EPOCH")"
  echo "- Finished: $(human_ts)"
  echo "- Output path: $OUT"
  echo "- Duration requested: $DURATION_SECONDS seconds"
  echo "- Snapshot interval: $INTERVAL_SECONDS seconds"
  echo
  echo "## Identity"
  echo
  echo '```text'
  echo "id: $(id 2>/dev/null)"
  echo "SELinux: $(getenforce 2>/dev/null)"
  echo "Magisk: $(magisk -v 2>/dev/null)"
  echo "Android: $(getprop ro.build.version.release) / SDK $(getprop ro.build.version.sdk)"
  echo "Build: $(getprop ro.build.display.id)"
  echo "Device: $(getprop ro.product.device)"
  echo "Model: $(getprop ro.product.model)"
  echo "Fingerprint: $(getprop ro.build.fingerprint)"
  echo '```'
  echo
  echo "## PR/release feature audit scope"
  echo
  echo "See \`00_FEATURE_AUDIT_SCOPE.md\` for the PR/release-note-derived runtime evidence map used by this capture."
  echo
  echo "## Package table"
  echo
  echo "| Package | Installed | Version | Code | UID | Flags | APK path |"
  echo "|---|---:|---|---:|---:|---|---|"
  tail -n +2 "$PKG_TABLE" | while IFS="$(printf '\t')" read p installed vn vc uid flags path; do
    [ -n "$p" ] || continue
    echo "| $p | $installed | ${vn:-—} | ${vc:-—} | ${uid:-—} | ${flags:-—} | ${path:-—} |"
  done
  echo
  echo "## MediaSession / notification comparison"
  echo
  echo "| Package | media_session hits | playing hints | notification hits | log error hints |"
  echo "|---|---:|---:|---:|---:|"
  for p in $PKGS; do
    pkg_installed "$p" || continue
    mh="$(maybe_grep_count "$p" "$MEDIA_ALL")"
    ph="$(playing_hint_count_for "$p")"
    nh="$(maybe_grep_count "$p" "$NOTIF_ALL")"
    eh="$(log_error_count_for "$p")"
    echo "| $p | $mh | $ph | $nh | $eh |"
  done
  echo
  echo "## Auxio-TS post-v5.3 runtime evidence matrix"
  echo
  echo "These are heuristic indicators, not proof. Review the linked files when a row is UNKNOWN/FAIL."
  echo
  echo "| Feature/change area | Evidence source | Heuristic status | What to inspect next |"
  echo "|---|---|---|---|"
  echo "| Package/variant identity | package dumps | $(feature_status a 'org.oxycblt.auxio\|com.tw.media\|com.tw.music' "$PKG_TABLE") | packages/*/quick-components.txt |"
  echo "| Generic MediaSession visibility | media_session_all.txt | $(feature_status a 'org.oxycblt\|com.tw.media\|com.tw.music' "$MEDIA_ALL") | media_session_all.txt around Auxio package |"
  echo "| MediaStyle / notification visibility | notification_all.txt | $(feature_status a 'org.oxycblt\|com.tw.media\|com.tw.music' "$NOTIF_ALL") | notification_all.txt and logcat RemoteViews/Bitmap errors |"
  echo "| Topway/DoFun aliases/components | package dumps | $(feature_status a 'com.tw.music.MusicActivity\|com.tw.music.MusicService\|MusicWidgetProvider\|TopwayMusicBridgeReceiver' "$QUICK_ALL") | packages/*/quick-components.txt |"
  echo "| Topway bridge/broadcasts | logcat | $(feature_status a 'Topway|widget_music_progress|com.tw.music.action' "$OUT/logs/logcat_filtered.txt") | logs/logcat_filtered.txt |"
  echo "| Widget/zero-ID fallback | appwidget/logcat | $(feature_status a 'Widget|AppWidget|zero' "$OUT/logs/logcat_filtered.txt") | system/dumpsys-appwidget.txt and snapshots/*/appwidget.txt |"
  echo "| SAF/file path/music source | source_paths/storage/logcat | $(feature_status a 'DirectFS|FilteredFS|LocationMode|LocationsDialog|SAF|DocumentsUI|OPEN_DOCUMENT|/storage/usbdisk|/mnt/media_rw|MediaStore|content://|tree/' "$OUT/logs/logcat_filtered.txt") | source_paths/, storage/, Auxio prefs/path grep |"
  echo "| Earliest autostart/readiness | autostart/earlyboot/logcat | $(feature_status a 'BOOT_COMPLETED|LOCKED_BOOT_COMPLETED|USER_UNLOCKED|BootReceiver|autostart|app_start_fallback|service.d' "$OUT/logs/logcat_filtered.txt") | autostart/, $BASE_OUT/_early_boot, package receiver dumps |"
  echo "| Overlay/floating controls vs status/nav/edge | overlay/window/logcat | $(feature_status a 'Overlay|CarFloating|SYSTEM_ALERT_WINDOW|WindowManager|StatusBar|NavigationBar|Gesture|Edge' "$OUT/logs/logcat_filtered.txt") | overlay/, snapshots/*/overlay-window-context.txt, final-dumpsys-window.txt |"
  echo "| Playback interruption/unexpected pause | interruptions/audio/media/logcat | $(feature_status a 'AUDIOFOCUS|AudioFocus|focus loss|LOSS_TRANSIENT|duck|becoming noisy|MEDIA_BUTTON|pause|interruption|telecom|bluetooth|radio|NavRadio|reverse' "$OUT/logs/logcat_filtered.txt") | interruptions/, audio_all.txt, snapshots/*/playback-interruption-context.txt |"
  echo "| Storage roots / DirectFS | storage/logcat | $(feature_status a 'DirectFS|RootGate|/storage/usbdisk|/mnt/media_rw|MediaStore External Volumes' "$OUT/logs/logcat_filtered.txt") | storage/ and logcat |"
  echo "| Root gate/probes | magisk/logcat | $(feature_status a 'RootState|RootGate|su|Magisk' "$OUT/logs/logcat_filtered.txt") | magisk/magisk-modules-state.txt and logcat |"
  echo "| SAF/DocumentsUI fallback | package/logcat | $(feature_status a 'DocumentsUI|ACTION_OPEN_DOCUMENT|SAF|documentsui' "$OUT/logs/logcat_filtered.txt") | storage/dumpsys-package-documentsui.txt |"
  echo "| Notification artwork safety | logcat | $(feature_status a 'Artwork|Bitmap|RemoteViews|Bad notification' "$OUT/logs/logcat_filtered.txt") | logs/logcat_filtered_tail.txt |"
  echo "| Playback queue/shuffle/seek | media/logcat | $(feature_status a 'shuffle|queue|seek|skip|PlaybackState|ExoPlayer' "$OUT/logs/logcat_filtered.txt") | media_session_all.txt and logcat |"
  echo "| Auxio in-app diagnostics | removed/abandoned | SKIPPED_BY_DESIGN | auxio/in-app-diagnostics-skipped.txt |"
  echo "| BTAndroidTS/peripheral manager | package/logcat | $(feature_status a 'btandroidts|Bluetooth|com.android.bluetooth|com.tw.bt' "$OUT/logs/logcat_filtered.txt") | package dumps and network/bluetooth_manager |"
  echo "| TS18 intent bridge / LSPosed | magisk/logcat | $(feature_status a 'intentbridge|LSPosed|Zygisk|zygote' "$OUT/logs/logcat_filtered.txt") | magisk module list and logcat |"
  echo "| NavRadio+/radio coexistence | package/audio/logcat | $(feature_status a 'NavRadio|navimods|com.tw.radio|radio' "$OUT/logs/logcat_filtered.txt") | audio and package dumps |"
  echo
  echo "## Error signals"
  echo
  echo '```text'
  grep -i -E 'FATAL EXCEPTION|ANR|AndroidRuntime|SecurityException|IllegalArgumentException|IllegalStateException|NullPointerException|IndexOutOfBoundsException|RemoteServiceException|TransactionTooLargeException|DeadObjectException|Bad notification|RemoteViews|Bitmap|denied|timeout|failed|not allowed|permission' "$OUT/logs/logcat_filtered_tail.txt" 2>/dev/null | tail -n 200
  echo '```'
  echo
  echo "## Most useful files"
  echo
  echo "- REPORT.md"
  echo "- 00_FEATURE_AUDIT_SCOPE.md"
  echo "- package_table.tsv"
  echo "- media_session_all.txt"
  echo "- notification_all.txt"
  echo "- audio_all.txt"
  echo "- window_focus_all.txt"
  echo "- logs/logcat_filtered_tail.txt"
  echo "- logs/logcat_filtered.txt"
  echo "- source_paths/"
  echo "- autostart/"
  echo "- overlay/"
  echo "- interruptions/"
  echo "- storage/"
  echo "- magisk/magisk-modules-state.txt"
  echo "- packages/*/quick-components.txt"
  echo "- snapshots/*/summary.txt"
  echo
  echo "## Stop reason"
  if [ -f "$STOP_FILE" ]; then
    echo "Stop file was present."
  else
    echo "Duration completed."
  fi
} > "$REPORT"

###############################################################################
# Archive
###############################################################################

log "creating archive"
ARCHIVE="$BASE_OUT/$SESSION_ID.tar.gz"
(
  cd "$BASE_OUT" || exit 1
  if have_cmd tar; then
    tar -czf "$ARCHIVE" "$SESSION_ID" 2>/dev/null
  elif have_cmd toybox; then
    toybox tar -czf "$ARCHIVE" "$SESSION_ID" 2>/dev/null
  elif have_cmd busybox; then
    busybox tar -czf "$ARCHIVE" "$SESSION_ID" 2>/dev/null
  else
    exit 2
  fi
)

if [ -f "$ARCHIVE" ]; then
  log "archive=$ARCHIVE"
  echo "Archive: $ARCHIVE" >> "$SUMMARY"
else
  log "archive not created; use folder $OUT"
  echo "Archive: not created; use folder $OUT" >> "$SUMMARY"
fi

log "complete"
exit 0
