#!/usr/bin/env bash

warning_count=0
error_count=0

info() {
  printf '[INFO] %s\n' "$*" >&2
}

fail() {
  error_count=$((error_count + 1))
  printf '[ERROR] %s\n' "$*" >&2
}

need_file() {
  if [ ! -f "$1" ]; then
    fail "Missing required file: $1"
    return 1
  fi
  return 0
}

need_fixed() {
  local file="$1"
  local text="$2"
  local label="$3"

  need_file "$file" || return
  if ! grep -Fq "$text" "$file"; then
    fail "$label missing from $file: $text"
  fi
}

need_absent() {
  local file="$1"
  local text="$2"
  local label="$3"

  need_file "$file" || return
  if grep -Fq "$text" "$file"; then
    fail "$label unexpectedly present in $file: $text"
  fi
}

info "Checking PR#117 TS18/t-music fast-path and safety contracts"

widget="app/src/topwayCompat/java/com/tw/music/view/MusicWidgetProvider.kt"
legacy="app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt"
overlay_service="app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt"
overlay_manifest="app/src/topwayCompat/AndroidManifest.xml"
persist="app/src/main/java/org/oxycblt/auxio/playback/persist/PersistenceRepository.kt"
explore="musikr/src/main/java/org/oxycblt/musikr/pipeline/ExploreStep.kt"

need_fixed "$widget" "renderColdWidgetControls" "cold Topway widget render path"
need_fixed "$widget" "TopwayMusicContract.CMD_UPDATE" "stock-style widget cmd=update service request"
need_fixed "$widget" "EXTRA_APP_WIDGET_IDS = \"appWidgetIds\"" "stock-style appWidgetIds extra"
need_fixed "$widget" "STOCK_MUSIC_ACTIVITY_CLASS = \"com.tw.music.MusicActivity\"" "stock launcher activity component"
need_fixed "$widget" "STOCK_WIDGET_ARTWORK_MAX_BYTES = 3_680_000" "stock RemoteViews artwork byte cap"
need_fixed "$widget" "bindTopwayControls" "Topway control PendingIntent binding"
need_absent "$widget" "@AndroidEntryPoint" "non-essential Hilt widget receiver injection"
need_absent "$widget" "DiagnosticJournal" "non-essential widget diagnostics dependency"

need_fixed "$legacy" "ACTION_LEGACY_META_CHANGED = \"com.android.music.metachanged\"" "legacy metadata broadcast action"
need_fixed "$legacy" "ACTION_LEGACY_PLAYSTATE_CHANGED = \"com.android.music.playstatechanged\"" "legacy playstate broadcast action"
need_fixed "$legacy" "BuildConfig.TOPWAY_COMPAT_FLAVOR" "topwayCompat-only broadcast guard"

need_fixed "$overlay_service" "registerScreenOnReceiver" "dynamic SCREEN_ON receiver"
need_fixed "$overlay_service" "Intent.ACTION_SCREEN_ON" "dynamic SCREEN_ON action"
need_fixed "$overlay_service" "prefs.suppressedByAuxioForeground" "foreground suppression persistence"
need_fixed "$overlay_service" "QUICKBOOT_POWERON" "Topway quickboot suppression clearing"
need_absent "$overlay_manifest" "android.intent.action.SCREEN_ON" "manifest SCREEN_ON receiver"
need_fixed "$overlay_manifest" "android:name=\"org.oxycblt.auxio.car.overlay.CarOverlayBootReceiver\"" "overlay boot receiver"
need_fixed "$overlay_manifest" "android:exported=\"false\"" "non-exported overlay receiver"

need_fixed "$persist" "data class FastResumeSnapshot" "minimal TS18 fast-resume snapshot model"
need_fixed "$persist" "saveFastResumeSnapshot" "minimal TS18 fast-resume snapshot persistence"
need_fixed "$explore" "isPotentialMusicFileNameMime" "pure JVM file classifier helper"
need_fixed "$explore" "mimeType: String?" "nullable MIME fallback for USB/unknown files"
need_fixed "$explore" "normalisedMimeType.isNotEmpty()" "empty MIME extension fallback"
need_fixed "$explore" "\"flac\"" "FLAC extension classification"
need_fixed "$explore" "\"m4a\"" "M4A extension classification"
need_fixed "$explore" "\"opus\"" "Opus extension classification"

if [ "$error_count" -ne 0 ]; then
  printf 'RESULT: FAILED, errors=%s warnings=%s\n' "$error_count" "$warning_count" >&2
  exit 1
fi

printf 'RESULT: SUCCESS, errors=0 warnings=%s\n' "$warning_count" >&2
