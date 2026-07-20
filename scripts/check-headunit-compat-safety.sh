#!/usr/bin/env bash
set -euo pipefail

allowed_topway_main='app/src/main/java/org/oxycblt/auxio/headunit/topway/'
allowed_topway_test='app/src/test/java/org/oxycblt/auxio/headunit/topway/'
allowed_topway_flavour='app/src/topwayCompat/java/com/tw/music/'
command_bridge_contract="${allowed_topway_main}TopwayCommandServiceContract.kt"
command_bridge_client="${allowed_topway_main}TopwayCommandServiceClient.kt"
command_bridge_contract_test="${allowed_topway_test}TopwayCommandServiceContractTest.kt"
command_bridge_binder_test="${allowed_topway_test}TopwayCallbackBinderTest.kt"
widget_component='app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt'
manifest_path='app/src/main/AndroidManifest.xml'
topway_flavour_manifest='app/src/topwayCompat/AndroidManifest.xml'
topway_media_res='app/src/topwayTwMedia/res/values/donottranslate.xml'
topway_media_debug_res='app/src/topwayTwMediaDebug/res/values/donottranslate.xml'

search_matches() {
  local pattern="$1"
  shift
  if [ "$#" -eq 0 ]; then
    return 0
  fi
  if command -v rg >/dev/null 2>&1; then
    rg -n -H --no-messages "${pattern}" "$@" || true
  else
    grep -RInE "${pattern}" "$@" 2>/dev/null || true
  fi
}

product_sources=()
for path in app/src/main app/src/topwayCompat app/src/topwayTwMusic app/src/topwayTwMedia app/src/topwayTwMediaDebug app/src/test app/src/androidTest musikr/src; do
  [ -e "${path}" ] && product_sources+=("${path}")
done

product_code_sources=()
for path in app/src/main/java app/src/topwayCompat/java app/src/test app/src/androidTest musikr/src; do
  [ -e "${path}" ] && product_code_sources+=("${path}")
done

identity_files=()
for path in app/build.gradle "${manifest_path}" "${topway_flavour_manifest}" app/src/topwayCompat/res/values/donottranslate.xml app/src/topwayTwMusic/res/values/donottranslate.xml app/src/topwayTwMusicDebug/res/values/donottranslate.xml "${topway_media_res}" "${topway_media_debug_res}" musikr/build.gradle settings.gradle; do
  [ -f "${path}" ] && identity_files+=("${path}")
done

if [ "${#product_sources[@]}" -eq 0 ] && [ "${#identity_files[@]}" -eq 0 ]; then
  echo "No product sources found; skipping headunit compat safety checks."
  exit 0
fi

# These remain forbidden everywhere in product code. The approved command bridge below does not use
# TWUtil, platform/shared UID identity, copied vendor classes, or fake Cardoor services.
hard_forbidden_hits="$(search_matches 'android\.tw\.john|android:sharedUserId=|android\.uid\.system|cn\.cardoor\.libs\.media\.RemoteMediaService|cn\.cardoor\.libs\.media\.impl\.MediaSourceService|cn\.cardoor\.basic\.media\.NotifyService' "${product_sources[@]}" "${identity_files[@]}")"
if [ -n "${hard_forbidden_hits}" ]; then
  echo "${hard_forbidden_hits}" >&2
  echo "Forbidden private/vendor hooks found in product code" >&2
  exit 1
fi

# The exact-device command Binder contract is permitted only in its two isolated implementation
# files, their focused tests, and the Topway-only package-visibility declaration. Any spread into
# core playback/UI or another adapter fails closed.
command_bridge_hits="$(search_matches 'com\.tw\.service\.xt|ITWCommandAidl|ITWCommandCallbackAidl|IMusicCallBack' "${product_sources[@]}")"
if [ -n "${command_bridge_hits}" ]; then
  while IFS= read -r line; do
    [ -z "${line}" ] && continue
    path="${line%%:*}"
    case "${path}" in
      "${command_bridge_contract}"|"${command_bridge_client}"|"${command_bridge_contract_test}"|"${command_bridge_binder_test}")
        ;;
      "${topway_flavour_manifest}")
        case "${line}" in
          *'<package android:name="com.tw.service.xt" />'*) ;;
          *)
            echo "${line}" >&2
            echo "Unexpected command-service contract in Topway manifest" >&2
            exit 1
            ;;
        esac
        ;;
      *)
        echo "${line}" >&2
        echo "Topway command-service Binder strings escaped the isolated verified bridge" >&2
        exit 1
        ;;
    esac
  done <<< "${command_bridge_hits}"
fi

# Standard package identity must not be replaced. Dedicated Topway/DoFun flavours are
# allowed to install as com.tw.music or com.tw.media because DoFun Variety uses fixed entries.
identity_hits="$(search_matches 'package="com\.tw\.(music|media)"|applicationId[[:space:]]+"com\.tw\.(music|media)"|namespace[[:space:]]+"com\.tw\.(music|media)"' "${identity_files[@]}")"
if [ -n "${identity_hits}" ]; then
  while IFS= read -r line; do
    [ -z "${line}" ] && continue
    path="${line%%:*}"
    case "${path}" in
      app/build.gradle)
        if grep -Fq 'topwayTwMusic' app/build.gradle && grep -Fq 'applicationId "com.tw.music"' app/build.gradle && grep -Fq 'topwayTwMedia' app/build.gradle && grep -Fq 'applicationId "com.tw.media"' app/build.gradle; then
          continue
        fi
        ;;
      app/src/topwayCompat/*|app/src/topwayTwMusic/*|app/src/topwayTwMusicDebug/*|app/src/topwayTwMedia/*|app/src/topwayTwMediaDebug/*)
        continue
        ;;
    esac
    echo "${line}" >&2
    echo "Unexpected package identity impersonation outside the dedicated Topway/DoFun flavour" >&2
    exit 1
  done <<< "${identity_hits}"
fi

vendor_hits="$(search_matches 'com\.tw\.[A-Za-z0-9_.]+|com\.android\.launcher\.widget_music_progress' "${product_code_sources[@]}")"
if [ -n "${vendor_hits}" ]; then
  while IFS= read -r line; do
    [ -z "${line}" ] && continue
    path="${line%%:*}"
    case "${path}" in
      "${command_bridge_contract}"|"${command_bridge_client}"|"${command_bridge_contract_test}"|"${command_bridge_binder_test}")
        case "${line}" in
          *"com.tw.service.xt"*) ;;
          *)
            echo "${line}" >&2
            echo "Unexpected vendor string in the isolated command-service bridge" >&2
            exit 1
            ;;
        esac
        ;;
      "${widget_component}")
        case "${line}" in
          *'Class.forName("com.tw.music.view.MusicWidgetProvider")'*) ;;
          *)
            echo "${line}" >&2
            echo "Unexpected vendor string in WidgetComponent" >&2
            exit 1
            ;;
        esac
        ;;
      ${allowed_topway_main}*|${allowed_topway_test}*|${allowed_topway_flavour}*)
        case "${line}" in
          *"com.tw.music.action.cmd"*|*"com.tw.music.action.prev"*|*"com.tw.music.action.next"*|*"com.tw.music.action.pp"*|*"com.tw.music.info"*|*"com.tw.launcher.music_progress_duration"*|*"com.android.launcher.widget_music_progress"*|*"com.tw.music.MusicActivity"*|*"com.tw.music.MusicService"*|*"com.tw.music.view.MusicWidgetProvider"*) ;;
          *)
            echo "${line}" >&2
            echo "Unexpected vendor string in isolated Topway bridge/test path" >&2
            exit 1
            ;;
        esac
        ;;
      *)
        echo "${line}" >&2
        echo "Vendor strings must stay in the isolated Topway bridge/test paths" >&2
        exit 1
        ;;
    esac
  done <<< "${vendor_hits}"
fi

if [ -f "${manifest_path}" ]; then
  # Package-visibility declarations are expected and harmless here. Restrict this check to exported
  # Topway intent actions so queries such as com.tw.eq/com.tw.dsp are not mistaken for receivers.
  manifest_tw_action_hits="$(search_matches '<action[[:space:]]+android:name="com\.tw\.[A-Za-z0-9_.]+"' "${manifest_path}")"
  if [ -n "${manifest_tw_action_hits}" ]; then
    while IFS= read -r line; do
      [ -z "${line}" ] && continue
      case "${line}" in
        *"com.tw.music.action.cmd"*|*"com.tw.music.action.prev"*|*"com.tw.music.action.next"*|*"com.tw.music.action.pp"*) ;;
        *)
          echo "${line}" >&2
          echo "Base manifest com.tw.* intent filters must be limited to the approved Topway actions" >&2
          exit 1
          ;;
      esac
    done <<< "${manifest_tw_action_hits}"
  fi
fi

if [ -f "${topway_flavour_manifest}" ]; then
  require_topway_identity() {
    local pattern="$1"
    local label="$2"
    if ! grep -Fq "${pattern}" "${topway_flavour_manifest}"; then
      echo "Missing ${label} in ${topway_flavour_manifest}: ${pattern}" >&2
      exit 1
    fi
  }
  require_topway_identity 'com.tw.music.MusicActivity' 'Topway activity alias'
  require_topway_identity 'org.oxycblt.auxio.MainActivity' 'Auxio alias target'
  require_topway_identity 'com.tw.music.MusicService' 'Topway MusicService fallback'
  require_topway_identity 'org.oxycblt.auxio.AuxioService' 'Topway base service override'
  require_topway_identity 'org.oxycblt.auxio.car.overlay.ACTION_RESTORE_OVERLAY' 'Topway overlay restore action'
  require_topway_identity 'tools:node="remove"' 'Topway base service browse-filter removal'
  require_topway_identity 'com.tw.service.xt' 'Topway command-service package visibility'
  # Manifest MUST declare modern specialUse compatibility for the overlay service (required by
  # Android 14+; safely ignored on Android 10). Runtime code API-gates the constant to API 34+.
  if ! grep -Fq 'android:foregroundServiceType="specialUse"' "${topway_flavour_manifest}"; then
    echo "Topway overlay manifest must declare foregroundServiceType=\"specialUse\" for API 34+ forward compatibility" >&2
    exit 1
  fi
  if ! grep -Fq 'FOREGROUND_SERVICE_SPECIAL_USE' "${topway_flavour_manifest}"; then
    echo "Topway overlay manifest must declare FOREGROUND_SERVICE_SPECIAL_USE permission for API 34+ forward compatibility" >&2
    exit 1
  fi
  require_topway_identity 'com.tw.music.view.MusicWidgetProvider' 'Topway MusicWidgetProvider fallback'
  require_topway_identity 'android.intent.action.MAIN' 'MAIN action'
  require_topway_identity 'android.intent.action.MUSIC_PLAYER' 'MUSIC_PLAYER action'
  require_topway_identity 'android.intent.category.LAUNCHER' 'LAUNCHER category'
  require_topway_identity 'android.intent.category.DEFAULT' 'DEFAULT category'
  require_topway_identity 'android.intent.category.APP_MUSIC' 'APP_MUSIC category'
fi

if [ -f app/src/main/java/org/oxycblt/auxio/headunit/overlay/CarOverlayContract.kt ] && [ -f "${topway_flavour_manifest}" ]; then
  if ! grep -Fq 'const val RESTORE_ACTION = "org.oxycblt.auxio.car.overlay.ACTION_RESTORE_OVERLAY"' app/src/main/java/org/oxycblt/auxio/headunit/overlay/CarOverlayContract.kt; then
    echo "CarOverlayContract RESTORE_ACTION must match the Topway manifest restore action" >&2
    exit 1
  fi
fi

echo "headunit compat safety checks passed"
