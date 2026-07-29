#!/usr/bin/env bash
# Source, merged-manifest and APK checks for the two maintained DoFun/Topway variants.
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${repo_root}"

failures=0
warn() { printf 'WARN: %s\n' "$*" >&2; }
fail() { printf 'ERROR: %s\n' "$*" >&2; failures=$((failures + 1)); }
pass() { printf 'OK: %s\n' "$*"; }

require_file_contains() {
  local file=$1
  local pattern=$2
  local desc=$3
  if [[ ! -f "$file" ]]; then
    fail "missing ${desc}: ${file}"
    return
  fi
  if grep -Fq -- "$pattern" "$file"; then
    pass "${desc} contains ${pattern}"
  else
    fail "${desc} does not contain ${pattern}: ${file}"
  fi
}

require_file_not_contains() {
  local file=$1
  local pattern=$2
  local desc=$3
  if [[ ! -f "$file" ]]; then
    fail "missing ${desc}: ${file}"
    return
  fi
  if grep -Fq -- "$pattern" "$file"; then
    fail "${desc} unexpectedly contains ${pattern}: ${file}"
  else
    pass "${desc} does not contain ${pattern}"
  fi
}

find_merged_manifest() {
  local variant=$1
  find app/build/intermediates/merged_manifest app/build/intermediates/merged_manifests \
    -path "*${variant}*AndroidManifest.xml" -print 2>/dev/null | sort | head -n 1
}

find_apk() {
  local variant_dir=$1
  local build_type=$2
  local apk_dir="app/build/outputs/apk/${variant_dir}/${build_type}"
  local signed_apk
  signed_apk="$(find "${apk_dir}" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' -print 2>/dev/null | sort | head -n 1)"
  if [[ -n "$signed_apk" ]]; then
    printf '%s\n' "$signed_apk"
    return
  fi
  find "${apk_dir}" -maxdepth 1 -type f -name '*.apk' -print 2>/dev/null | sort | head -n 1
}

require_manifest_dump_contains() {
  local manifest_dump=$1
  local pattern=$2
  local pass_message=$3
  local fail_message=$4
  if grep -Fq -- "$pattern" "$manifest_dump"; then
    pass "$pass_message"
  else
    fail "$fail_message"
  fi
}

check_apk_manifest() {
  local apk=$1
  local expected_package=$2
  local label=$3
  local apkanalyzer_bin="${ANDROID_HOME:-}/cmdline-tools/latest/bin/apkanalyzer"
  if [[ ! -x "$apkanalyzer_bin" ]]; then
    apkanalyzer_bin="$(command -v apkanalyzer || true)"
  fi
  if [[ -z "$apkanalyzer_bin" || ! -x "$apkanalyzer_bin" ]]; then
    warn "apkanalyzer not found; skipping binary APK manifest checks for ${label}"
    return
  fi

  local actual_package
  actual_package="$($apkanalyzer_bin manifest application-id "$apk")"
  if [[ "$actual_package" == "$expected_package" ]]; then
    pass "${label} APK application id is ${expected_package}"
  else
    fail "${label} APK application id expected ${expected_package}, got ${actual_package}"
  fi

  local manifest_dump
  manifest_dump="$(mktemp)"
  "$apkanalyzer_bin" manifest print "$apk" > "$manifest_dump"
  require_manifest_dump_contains "$manifest_dump" 'android:name="com.tw.music.MusicActivity"' "${label} APK manifest has com.tw.music.MusicActivity" "${label} APK manifest lacks com.tw.music.MusicActivity"
  require_manifest_dump_contains "$manifest_dump" 'android:targetActivity="org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"' "${label} APK alias targets the Topway entry router" "${label} APK alias target mismatch"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.action.MAIN"' "${label} APK alias has MAIN action" "${label} APK alias lacks MAIN action"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.action.MUSIC_PLAYER"' "${label} APK alias has MUSIC_PLAYER action" "${label} APK alias lacks MUSIC_PLAYER action"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.action.VIEW"' "${label} APK alias has VIEW action" "${label} APK alias lacks VIEW action"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.category.DEFAULT"' "${label} APK alias has DEFAULT category" "${label} APK alias lacks DEFAULT category"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.category.LAUNCHER"' "${label} APK alias has LAUNCHER category" "${label} APK alias lacks LAUNCHER category"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.category.APP_MUSIC"' "${label} APK alias has APP_MUSIC category" "${label} APK alias lacks APP_MUSIC category"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.intent.category.BROWSABLE"' "${label} APK alias has BROWSABLE category" "${label} APK alias lacks BROWSABLE category"
  require_manifest_dump_contains "$manifest_dump" 'android:name="android.media.browse.MediaBrowserService"' "${label} APK manifest has MediaBrowserService" "${label} APK manifest lacks MediaBrowserService"
  require_manifest_dump_contains "$manifest_dump" "android:authorities=\"${expected_package}.image.CoverProvider\"" "${label} APK manifest has applicationId CoverProvider authority" "${label} APK manifest lacks applicationId CoverProvider authority"
  require_manifest_dump_contains "$manifest_dump" 'android:name="com.tw.music.MusicService"' "${label} APK manifest has com.tw.music.MusicService fallback" "${label} APK manifest lacks com.tw.music.MusicService fallback"
  require_manifest_dump_contains "$manifest_dump" 'android:name="com.tw.music.view.MusicWidgetProvider"' "${label} APK manifest has com.tw.music.view.MusicWidgetProvider fallback" "${label} APK manifest lacks com.tw.music.view.MusicWidgetProvider fallback"
  rm -f -- "$manifest_dump"
}

printf 'Checking source-level DoFun/Topway compatibility expectations...\n\n'

require_file_contains app/build.gradle 'topwayTwMusic {' 'Gradle topwayTwMusic product flavour'
require_file_contains app/build.gradle 'topwayTwMedia {' 'Gradle topwayTwMedia product flavour'
require_file_not_contains app/build.gradle '        standard {' 'Gradle product flavours'
require_file_contains app/build.gradle 'applicationId "com.tw.music"' 'Gradle topwayTwMusic applicationId'
require_file_contains app/build.gradle 'applicationId "com.tw.media"' 'Gradle topwayTwMedia applicationId'
require_file_contains app/build.gradle 'src/topwayCompat/java' 'shared Topway Java source set'
require_file_contains app/build.gradle 'src/topwayCompat/AndroidManifest.xml' 'shared Topway manifest'

flavour_manifest=app/src/topwayCompat/AndroidManifest.xml
require_file_contains "$flavour_manifest" 'com.tw.music.MusicActivity' 'Topway activity alias'
require_file_contains "$flavour_manifest" 'org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity' 'Topway alias router target'
require_file_contains "$flavour_manifest" 'org.oxycblt.auxio.MainActivity' 'Topway full-player activity'
require_file_contains "$flavour_manifest" 'com.tw.music.MusicService' 'Topway MusicService component fallback'
require_file_contains "$flavour_manifest" 'org.oxycblt.auxio.AuxioService' 'Topway canonical external service override'
require_file_contains "$flavour_manifest" 'tools:node="remove"' 'Topway base service browser filters removed'
require_file_contains "$flavour_manifest" 'com.tw.music.view.MusicWidgetProvider' 'Topway MusicWidgetProvider component fallback'
require_file_contains "$flavour_manifest" 'android.intent.action.MAIN' 'Topway alias main action'
require_file_contains "$flavour_manifest" 'android.intent.action.MUSIC_PLAYER' 'Topway alias music action'
require_file_contains "$flavour_manifest" 'android.intent.action.VIEW' 'Topway alias view action'
require_file_contains "$flavour_manifest" 'android.intent.category.LAUNCHER' 'Topway alias launcher category'
require_file_contains "$flavour_manifest" 'android.intent.category.DEFAULT' 'Topway alias default category'
require_file_contains "$flavour_manifest" 'android.intent.category.APP_MUSIC' 'Topway alias app music category'
require_file_contains "$flavour_manifest" 'android.intent.category.BROWSABLE' 'Topway alias browsable category'
require_file_not_contains "$flavour_manifest" 'com.tw.music.action.cmd' 'Topway wrapper manifest avoids duplicate command receiver'
require_file_not_contains "$flavour_manifest" 'com.tw.music.action.prev' 'Topway wrapper manifest avoids duplicate previous receiver'
require_file_not_contains "$flavour_manifest" 'com.tw.music.action.next' 'Topway wrapper manifest avoids duplicate next receiver'
require_file_not_contains "$flavour_manifest" 'com.tw.music.action.pp' 'Topway wrapper manifest avoids duplicate play-pause receiver'
require_file_not_contains "$flavour_manifest" 'com.android.launcher.widget_music_progress' 'Topway wrapper manifest avoids duplicate widget-progress receiver'
require_file_contains "$flavour_manifest" 'android:foregroundServiceType="specialUse"' 'overlay manifest specialUse declaration'
require_file_contains "$flavour_manifest" 'FOREGROUND_SERVICE_SPECIAL_USE' 'overlay manifest special-use permission'

require_file_contains app/src/main/AndroidManifest.xml '${applicationId}.image.CoverProvider' 'applicationId-scoped CoverProvider authority'
require_file_contains app/src/main/AndroidManifest.xml 'android.media.browse.MediaBrowserService' 'base MediaBrowserService'
require_file_contains app/src/main/AndroidManifest.xml '.headunit.topway.TopwayMusicBridgeReceiver' 'base Topway command receiver'
require_file_contains app/src/topwayTwMusic/res/values/donottranslate.xml 'com.tw.music.image.CoverProvider' 'Topway music CoverProvider authority'
require_file_contains app/src/topwayTwMusicDebug/res/values/donottranslate.xml 'com.tw.music.debug.image.CoverProvider' 'Topway music debug CoverProvider authority'
require_file_contains app/src/topwayTwMedia/res/values/donottranslate.xml 'com.tw.media.image.CoverProvider' 'Topway media CoverProvider authority'
require_file_contains app/src/topwayTwMediaDebug/res/values/donottranslate.xml 'com.tw.media.debug.image.CoverProvider' 'Topway media debug CoverProvider authority'
require_file_contains app/src/topwayCompat/res/values/donottranslate.xml '>Music<' 'Topway shared label'

contract=app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicContract.kt
for token in \
  com.tw.music.info com.tw.launcher.music_progress_duration com.tw.music.action.cmd \
  com.tw.music.action.prev com.tw.music.action.next com.tw.music.action.pp \
  com.android.launcher.widget_music_progress musicTitle musicaArtist musicAlbum musicPath \
  msg_music_progress msg_music_duration music_progress; do
  require_file_contains "$contract" "$token" "Topway contract string"
done

coordinator=app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinator.kt
playback=app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackServiceFragment.kt
mode=app/src/main/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationMode.kt
seek=app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwaySeekUnitPolicy.kt
require_file_contains "$coordinator" 'class TopwayLauncherIntegrationCoordinator' 'central launcher coordinator'
require_file_contains "$coordinator" 'fun publishMetadata' 'coordinator metadata publisher'
require_file_contains "$coordinator" 'fun publishProgress' 'coordinator progress publisher'
require_file_contains "$coordinator" 'fun handle' 'coordinator incoming command handler'
require_file_contains "$coordinator" 'context.sendBroadcast(Intent(intent).setPackage(DOFUN_PACKAGE))' 'coordinator DoFun-targeted path'
require_file_not_contains "$coordinator" 'getLaunchIntentForPackage(DOFUN_PACKAGE)' 'coordinator package-visibility gate'
require_file_not_contains "$coordinator" 'isDoFunInstalled' 'coordinator installed-package gate'
require_file_contains "$coordinator" 'TopwaySeekPolicyConverter.convert' 'coordinator seek conversion'
require_file_contains "$coordinator" 'mode.sendsTopwayBroadcasts' 'coordinator outgoing mode gate'
require_file_contains "$coordinator" 'mode.handlesTopwayCommands' 'coordinator incoming mode gate'
require_file_contains "$coordinator" 'journal.log' 'coordinator diagnostic journal'

for token in \
  TopwayLauncherIntegrationCoordinator publishTopwayState topwayCoordinator.publishMetadata \
  topwayCoordinator.publishProgress startTopwayProgressTicker topwayCoordinator.handle \
  cmd-update topwayCoordinator.clear 'private var topwayProgressTickerJob: Job? = null'; do
  require_file_contains "$playback" "$token" 'playback service Topway integration'
done

require_file_contains "$mode" 'GenericDofunMedia' 'generic DoFun media mode'
require_file_contains "$mode" 'usesGenericDofunProfile' 'generic DoFun profile gate'
require_file_contains "$mode" 'fun defaultFor(topwayCompatFlavor: Boolean)' 'pure compatibility default policy'
require_file_contains "$mode" 'bindsTopwayCommandService' 'command-service bind gate'
require_file_contains "$mode" 'AutoAllSafePaths' 'legacy all-safe-paths mode'
require_file_contains "$mode" 'DiagnosticsOnly' 'diagnostics-only mode'
require_file_contains "$mode" 'sendsTopwayBroadcasts' 'outgoing mode flag'
require_file_contains "$mode" 'handlesTopwayCommands' 'incoming mode flag'
mode_test=app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt
require_file_contains "$mode_test" 'default policy is explicit for both compatibility states' 'pure launcher default coverage'
require_file_contains "$mode_test" 'topwayCompatFlavor = false' 'non-Topway fallback coverage'
require_file_contains "$mode_test" 'unset topway preference adopts generic media once' 'launcher migration coverage'
require_file_contains "$mode_test" 'persisted all safe paths survives migration' 'explicit fallback preservation'
require_file_contains app/src/topwayCompat/res/xml/preferences_car.xml 'app:defaultValue="GenericDofunMedia"' 'Topway settings generic default'
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/DofunMediaCompatPolicy.kt 'mode == Ts18LauncherIntegrationMode.AndroidMediaSessionOnly' 'Android-only wrapper controls'
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt 'prefs.registerOnSharedPreferenceChangeListener(modePreferenceListener)' 'live mode notification refresh'
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt 'MediaButtonIntentFactory.serviceIntent(context, canonicalServiceClass, keyCode)' 'canonical media-button service intent'
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt 'DofunMediaCompatPolicy.compactActionIndices' 'generic DoFun compact actions'
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionHolder.kt 'buildMediaButtonPendingIntent' 'generic DoFun media-button pending intents'
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackNotificationChannel.kt 'ACTION_CHANNEL_NOTIFICATION_SETTINGS' 'notification-channel recovery path'

require_file_contains "$seek" 'Percent0To100' 'Topway seek percent policy'
require_file_contains "$seek" 'Permille0To1000' 'Topway seek permille policy'
require_file_contains "$seek" 'private fun chooseAuto' 'Topway seek auto policy'
require_file_contains "$seek" 'value <= 100L -> TopwaySeekUnitPolicy.Percent0To100' 'Topway percent precedence'
require_file_contains "$seek" 'value <= 1000L -> TopwaySeekUnitPolicy.Permille0To1000' 'Topway permille precedence'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwaySeekPolicyConverterTest.kt 'TopwaySeekUnitPolicy.Auto' 'seek auto tests'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinatorTest.kt 'countDofunTargeted' 'DoFun-targeted broadcast tests'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinatorTest.kt 'private fun List<Intent>.countImplicit' 'implicit broadcast counter'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinatorTest.kt 'private fun List<Intent>.countDofunTargeted' 'targeted broadcast counter'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinatorTest.kt 'cmd update republishes without toggling playback' 'cmd update test'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayStartRoutingPolicyTest.kt 'assertNull(negative.seekTargetMs)' 'negative seek guard test'

require_file_not_contains app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt 'TopwayMusicBroadcastBridge' 'WidgetComponent canonical bridge ownership'
require_file_not_contains app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt 'topwayBridge.publishMetadata' 'WidgetComponent metadata ownership'
require_file_not_contains app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt 'topwayBridge.publishProgress' 'WidgetComponent progress ownership'
require_file_contains app/build.gradle 'buildConfigField "boolean", "TOPWAY_TWMUSIC_FLAVOR", "true"' 'Topway music build flag'
require_file_contains app/build.gradle 'buildConfigField "boolean", "TOPWAY_TWMEDIA_FLAVOR", "true"' 'Topway media build flag'
require_file_contains app/build.gradle 'buildConfigField "boolean", "TOPWAY_COMPAT_FLAVOR", "true"' 'shared Topway build flag'
require_file_contains app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBroadcastBridge.kt 'BuildConfig.TOPWAY_COMPAT_FLAVOR' 'Topway-compatible bridge gate'
require_file_contains app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayWidgetProviderPolicy.kt 'com.tw.music.view.MusicWidgetProvider' 'Topway widget wrapper policy'

widget=app/src/topwayCompat/java/com/tw/music/view/MusicWidgetProvider.kt
for token in \
  startTopwayWidgetUpdateService renderColdWidgetControls 'EXTRA_APP_WIDGET_IDS = "appWidgetIds"' \
  'STOCK_MUSIC_ACTIVITY_CLASS = "com.tw.music.MusicActivity"' \
  'STOCK_WIDGET_ARTWORK_MAX_BYTES = 3_680_000' PendingIntent.getActivity CMD_UPDATE \
  safelyExtractIncomingExtras AppWidgetManager.ACTION_APPWIDGET_UPDATE; do
  require_file_contains "$widget" "$token" 'Topway widget wrapper'
done
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/SystemPlaybackReceiver.kt 'TopwayWidgetProviderPolicy.shouldHandleTopwayUpdate' 'system receiver widget route'
require_file_contains app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBridgeReceiver.kt 'safelyExtractIncomingExtras' 'bridge receiver extras guard'
require_file_contains app/src/topwayCompat/java/com/tw/music/MusicService.kt 'AndroidEntryPoint' 'Topway MusicService Hilt entry'
require_file_contains app/src/topwayCompat/java/com/tw/music/MusicService.kt 'AuxioService' 'Topway MusicService delegate'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayWidgetProviderPolicyTest.kt 'topwayVariantsServeUpdateEvenWithoutNormalAppWidgetInstance' 'Topway widget policy test'

for token in \
  Build.VERSION_CODES.UPSIDE_DOWN_CAKE ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE \
  FLAG_LAYOUT_IN_SCREEN FLAG_LAYOUT_NO_LIMITS 'DEFAULT_TOP_EDGE_Y = 0' getRealSize; do
  require_file_contains app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt "$token" 'overlay API/runtime guard'
done
require_file_not_contains app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt 'STATUS_BAR_INSET_PX' 'overlay hard-coded status inset'
require_file_not_contains app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt 'NAV_BAR_INSET_PX' 'overlay hard-coded nav inset'
require_file_contains app/src/test/java/org/oxycblt/auxio/car/overlay/CarOverlayForegroundServiceTypePolicyTest.kt 'foregroundServiceTypeForApi(29)' 'overlay API 29 test'
require_file_contains app/src/test/java/org/oxycblt/auxio/car/overlay/CarOverlayBoundsClampingTest.kt 'defaultTopCenterPosition' 'overlay dynamic bounds test'
require_file_contains app/src/test/java/org/oxycblt/auxio/car/overlay/CarOverlayBoundsClampingTest.kt 'assertEquals(0, y)' 'overlay y=0 test'

require_file_contains .github/workflows/manual-release.yml 'assembleTopwayTwMusicRelease' 'manual release builds Topway music'
require_file_contains .github/workflows/manual-release.yml 'assembleTopwayTwMediaRelease' 'manual release builds Topway media'
require_file_contains .github/workflows/manual-release.yml 'topway-twmusic-release.apk' 'manual release forbids raw Topway music APK name'
require_file_contains .github/workflows/manual-release.yml 'topway-twmedia-release.apk' 'manual release names Topway media APK'

printf '\nChecking generated merged manifests when present...\n'
manifest_specs=(
  'topwayTwMusicDebug|com.tw.music.debug|topwayTwMusicDebug'
  'topwayTwMusicRelease|com.tw.music|topwayTwMusicRelease'
  'topwayTwMediaDebug|com.tw.media.debug|topwayTwMediaDebug'
  'topwayTwMediaRelease|com.tw.media|topwayTwMediaRelease'
)
manifest_args=()
for spec in "${manifest_specs[@]}"; do
  IFS='|' read -r variant expected label <<< "$spec"
  path="$(find_merged_manifest "$variant" || true)"
  [[ -n "$path" ]] && manifest_args+=("$path|$expected|$label")
done

if ((${#manifest_args[@]} != 4)); then
  warn 'one or more maintained merged manifests are absent; run processTopwayTwMusicDebugMainManifest, processTopwayTwMusicReleaseMainManifest, processTopwayTwMediaDebugMainManifest and processTopwayTwMediaReleaseMainManifest for output checks'
else
  if ! python3 - "${manifest_args[@]}" <<'PY'
import sys
import xml.etree.ElementTree as ET

ANDROID = "{http://schemas.android.com/apk/res/android}"
failures = 0

def ok(message): print(f"OK: {message}")
def fail(message):
    global failures
    failures += 1
    print(f"ERROR: {message}", file=sys.stderr)
def attr(element, name): return element.attrib.get(ANDROID + name)
def parse(path): return ET.parse(path).getroot()

def components_with_filter(application, tag, action, category):
    matches = []
    for component in application.findall(tag):
        for intent_filter in component.findall("intent-filter"):
            actions = {attr(item, "name") for item in intent_filter.findall("action")}
            categories = {attr(item, "name") for item in intent_filter.findall("category")}
            if action in actions and category in categories:
                matches.append(component)
                break
    return matches

def require_package(root, expected, label):
    actual = root.attrib.get("package")
    if actual == expected: ok(f"{label} package is {expected}")
    else: fail(f"{label} package expected {expected}, got {actual!r}")

def require_provider(application, authority, label):
    providers = [item for item in application.findall("provider") if attr(item, "authorities") == authority]
    if providers: ok(f"{label} has CoverProvider authority {authority}")
    else: fail(f"{label} lacks CoverProvider authority {authority}")

def require_media_browser(application, label):
    services = []
    for service in application.findall("service"):
        for intent_filter in service.findall("intent-filter"):
            actions = {attr(item, "name") for item in intent_filter.findall("action")}
            if "android.media.browse.MediaBrowserService" in actions:
                services.append(service)
    names = [attr(item, "name") for item in services]
    if names != ["com.tw.music.MusicService"]:
        fail(f"{label} MediaBrowserService expected [com.tw.music.MusicService], got {names}")
        return
    if attr(services[0], "exported") != "true": fail(f"{label} MediaBrowserService is not exported=true")
    else: ok(f"{label} has exported canonical MediaBrowserService")

def require_topway_receiver(application, label):
    expected = {
        "com.tw.music.action.cmd",
        "com.tw.music.action.prev",
        "com.tw.music.action.next",
        "com.tw.music.action.pp",
        "com.android.launcher.widget_music_progress",
    }
    receivers = []
    for receiver in application.findall("receiver"):
        actions = set()
        for intent_filter in receiver.findall("intent-filter"):
            actions.update(attr(item, "name") for item in intent_filter.findall("action"))
        if expected & actions: receivers.append((receiver, actions))
    if len(receivers) != 1:
        fail(f"{label} expected one Topway command receiver, got {[attr(item, 'name') for item, _ in receivers]}")
        return
    receiver, actions = receivers[0]
    if attr(receiver, "name") != "org.oxycblt.auxio.headunit.topway.TopwayMusicBridgeReceiver":
        fail(f"{label} Topway command receiver is {attr(receiver, 'name')!r}")
    elif attr(receiver, "exported") != "true":
        fail(f"{label} Topway receiver is not exported=true")
    elif expected - actions:
        fail(f"{label} Topway receiver missing actions: {sorted(expected - actions)}")
    else: ok(f"{label} Topway receiver exposes expected command actions")

def require_launcher_entries(application, label):
    entries = components_with_filter(application, "activity", "android.intent.action.MAIN", "android.intent.category.LAUNCHER")
    entries += components_with_filter(application, "activity-alias", "android.intent.action.MAIN", "android.intent.category.LAUNCHER")
    names = [attr(item, "name") for item in entries]
    expected = {"com.tw.music.MusicActivity"}
    duplicates = sorted({name for name in names if names.count(name) > 1})
    if duplicates: fail(f"{label} duplicate MAIN/LAUNCHER entries: {duplicates}")
    elif set(names) == expected and len(names) == len(expected): ok(f"{label} has one canonical MAIN/LAUNCHER entry")
    else: fail(f"{label} MAIN/LAUNCHER entries expected {sorted(expected)}, got {names}")

def require_topway_alias(application, label):
    aliases = [item for item in application.findall("activity-alias") if attr(item, "name") == "com.tw.music.MusicActivity"]
    if len(aliases) != 1:
        fail(f"{label} expected one com.tw.music.MusicActivity alias, got {len(aliases)}")
        return
    alias = aliases[0]
    if attr(alias, "targetActivity") != "org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity": fail(f"{label} alias target mismatch")
    else: ok(f"{label} alias target is correct")
    if attr(alias, "exported") != "true": fail(f"{label} alias is not exported=true")
    if attr(alias, "label") != "@string/info_topway_music_app_name": fail(f"{label} alias label is {attr(alias, 'label')!r}")
    actions, categories = set(), set()
    for intent_filter in alias.findall("intent-filter"):
        actions.update(attr(item, "name") for item in intent_filter.findall("action"))
        categories.update(attr(item, "name") for item in intent_filter.findall("category"))
    for name in ["android.intent.action.MAIN", "android.intent.action.MUSIC_PLAYER", "android.intent.action.VIEW"]:
        if name not in actions: fail(f"{label} alias lacks action {name}")
    for name in ["android.intent.category.LAUNCHER", "android.intent.category.DEFAULT", "android.intent.category.APP_MUSIC", "android.intent.category.BROWSABLE"]:
        if name not in categories: fail(f"{label} alias lacks category {name}")

def require_main_activity_minimised(application, label):
    activities = [item for item in application.findall("activity") if attr(item, "name") == "org.oxycblt.auxio.MainActivity"]
    if len(activities) != 1:
        fail(f"{label} expected one MainActivity target, got {len(activities)}")
        return
    activity = activities[0]
    if attr(activity, "exported") != "false": fail(f"{label} MainActivity exported is {attr(activity, 'exported')!r}")
    if activity.findall("intent-filter"): fail(f"{label} MainActivity retains external intent filters")
    else: ok(f"{label} MainActivity is minimised")

for raw in sys.argv[1:]:
    path, expected_package, label = raw.split("|", 2)
    root = parse(path)
    application = root.find("application")
    require_package(root, expected_package, label)
    require_launcher_entries(application, label)
    require_topway_alias(application, label)
    require_main_activity_minimised(application, label)
    require_provider(application, f"{expected_package}.image.CoverProvider", label)
    require_media_browser(application, label)
    require_topway_receiver(application, label)

sys.exit(1 if failures else 0)
PY
  then
    failures=$((failures + 1))
  fi
fi

printf '\nChecking manifest-declared Topway component class packaging guardrail...\n'
if ! bash ./scripts/check-topway-manifest-components.sh; then
  failures=$((failures + 1))
fi

printf '\nChecking built APK presence when present...\n'
mode=full
req_topway_music_release=1
req_topway_media_release=1
if [[ -n ${SELECTED_VARIANTS:-} ]]; then
  mode=selective
  req_topway_music_release=0
  req_topway_media_release=0
  while IFS= read -r variant; do
    case "$variant" in
      topway_twmedia) req_topway_media_release=1 ;;
      topway_twmusic_magisk) req_topway_music_release=1 ;;
    esac
  done <<< "$SELECTED_VARIANTS"
fi

topway_debug_apk="$(find_apk topwayTwMusic debug || true)"
topway_release_apk="$(find_apk topwayTwMusic release || true)"
topway_media_debug_apk="$(find_apk topwayTwMedia debug || true)"
topway_media_release_apk="$(find_apk topwayTwMedia release || true)"

debug_outputs_present=0
release_outputs_present=0
[[ -n "$topway_debug_apk" || -n "$topway_media_debug_apk" ]] && debug_outputs_present=1
[[ -n "$topway_release_apk" || -n "$topway_media_release_apk" ]] && release_outputs_present=1

if (( debug_outputs_present )); then
  [[ -n "$topway_debug_apk" ]] && pass "found Topway music debug APK: ${topway_debug_apk}" || fail 'Topway music debug APK missing while debug outputs are present'
  [[ -n "$topway_media_debug_apk" ]] && pass "found Topway media debug APK: ${topway_media_debug_apk}" || fail 'Topway media debug APK missing while debug outputs are present'
elif (( ! release_outputs_present )); then
  warn 'debug APKs not found; run both maintained debug assemble tasks'
fi

if (( release_outputs_present )); then
  if [[ -n "$topway_release_apk" ]]; then
    pass "found internal Topway music release APK: ${topway_release_apk}"
  elif (( req_topway_music_release )); then
    fail "internal Topway music release APK missing in ${mode} mode"
  fi
  if [[ -n "$topway_media_release_apk" ]]; then
    pass "found Topway media release APK: ${topway_media_release_apk}"
  elif (( req_topway_media_release )); then
    fail "Topway media release APK missing in ${mode} mode"
  fi
elif (( ! debug_outputs_present )); then
  warn 'release APKs not found; run both maintained release assemble tasks'
fi

[[ -n "$topway_debug_apk" ]] && check_apk_manifest "$topway_debug_apk" com.tw.music.debug topwayTwMusicDebug
[[ -n "$topway_release_apk" ]] && check_apk_manifest "$topway_release_apk" com.tw.music topwayTwMusicRelease
[[ -n "$topway_media_debug_apk" ]] && check_apk_manifest "$topway_media_debug_apk" com.tw.media.debug topwayTwMediaDebug
[[ -n "$topway_media_release_apk" ]] && check_apk_manifest "$topway_media_release_apk" com.tw.media topwayTwMediaRelease

printf '\nResult: '
if (( failures == 0 )); then
  printf 'PASS\n'
else
  printf 'FAIL (%d issue(s))\n' "$failures"
  exit 1
fi
