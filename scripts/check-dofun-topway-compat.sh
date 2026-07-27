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
  local file=$1 pattern=$2 desc=$3
  [[ -f "$file" ]] || { fail "missing ${desc}: ${file}"; return; }
  if grep -Fq -- "$pattern" "$file"; then pass "${desc} contains ${pattern}"; else fail "${desc} lacks ${pattern}"; fi
}
require_file_not_contains() {
  local file=$1 pattern=$2 desc=$3
  [[ -f "$file" ]] || { fail "missing ${desc}: ${file}"; return; }
  if grep -Fq -- "$pattern" "$file"; then fail "${desc} unexpectedly contains ${pattern}"; else pass "${desc} excludes ${pattern}"; fi
}
find_merged_manifest() {
  find app/build/intermediates/merged_manifest app/build/intermediates/merged_manifests \
    -path "*$1*AndroidManifest.xml" -print 2>/dev/null | sort | head -n 1
}
find_apk() {
  local variant_dir=$1
  local build_type=$2
  local apk_dir="app/build/outputs/apk/${variant_dir}/${build_type}"
  local signed
  signed="$(find "${apk_dir}" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' -print 2>/dev/null | sort | head -n1)"
  [[ -n "$signed" ]] && { printf '%s\n' "$signed"; return; }
  find "${apk_dir}" -maxdepth 1 -type f -name '*.apk' -print 2>/dev/null | sort | head -n1
}

printf 'Checking source-level DoFun/Topway compatibility expectations...\n\n'
require_file_contains app/build.gradle 'topwayTwMusic {' 'Gradle topwayTwMusic product flavour'
require_file_contains app/build.gradle 'topwayTwMedia {' 'Gradle topwayTwMedia product flavour'
require_file_not_contains app/build.gradle '        standard {' 'Gradle product flavours'
require_file_contains app/build.gradle 'applicationId "com.tw.music"' 'topwayTwMusic applicationId'
require_file_contains app/build.gradle 'applicationId "com.tw.media"' 'topwayTwMedia applicationId'
require_file_contains app/build.gradle 'src/topwayCompat/java' 'shared Topway Java source set'
require_file_contains app/build.gradle 'src/topwayCompat/AndroidManifest.xml' 'shared Topway manifest source set'

flavour_manifest=app/src/topwayCompat/AndroidManifest.xml
for token in \
  com.tw.music.MusicActivity org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity \
  org.oxycblt.auxio.MainActivity com.tw.music.MusicService org.oxycblt.auxio.AuxioService \
  'tools:node="remove"' com.tw.music.view.MusicWidgetProvider android.intent.action.MAIN \
  android.intent.action.MUSIC_PLAYER android.intent.category.LAUNCHER \
  android.intent.category.DEFAULT android.intent.category.APP_MUSIC \
  'android:foregroundServiceType="specialUse"' FOREGROUND_SERVICE_SPECIAL_USE; do
  require_file_contains "$flavour_manifest" "$token" "Topway shared manifest contract"
done
for forbidden in \
  com.tw.music.action.cmd com.tw.music.action.prev com.tw.music.action.next \
  com.tw.music.action.pp com.android.launcher.widget_music_progress; do
  require_file_not_contains "$flavour_manifest" "$forbidden" 'Topway wrapper manifest avoids duplicate command receiver actions'
done

require_file_contains app/src/main/AndroidManifest.xml '${applicationId}.image.CoverProvider' 'applicationId-scoped CoverProvider authority'
require_file_contains app/src/main/AndroidManifest.xml android.media.browse.MediaBrowserService 'base MediaBrowserService'
require_file_contains app/src/main/AndroidManifest.xml .headunit.topway.TopwayMusicBridgeReceiver 'base Topway command receiver'
require_file_contains app/src/topwayTwMusic/res/values/donottranslate.xml com.tw.music.image.CoverProvider 'Topway music CoverProvider authority'
require_file_contains app/src/topwayTwMusicDebug/res/values/donottranslate.xml com.tw.music.debug.image.CoverProvider 'Topway music debug authority'
require_file_contains app/src/topwayTwMedia/res/values/donottranslate.xml com.tw.media.image.CoverProvider 'Topway media authority'
require_file_contains app/src/topwayTwMediaDebug/res/values/donottranslate.xml com.tw.media.debug.image.CoverProvider 'Topway media debug authority'
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
for token in \
  'class TopwayLauncherIntegrationCoordinator' 'fun publishMetadata' 'fun publishProgress' 'fun handle' \
  'context.sendBroadcast(Intent(intent).setPackage(DOFUN_PACKAGE))' TopwaySeekPolicyConverter.convert \
  mode.sendsTopwayBroadcasts mode.handlesTopwayCommands journal.log; do
  require_file_contains "$coordinator" "$token" 'central launcher coordinator'
done
require_file_not_contains "$coordinator" getLaunchIntentForPackage 'coordinator package visibility gate'
require_file_not_contains "$coordinator" isDoFunInstalled 'coordinator DoFun installed gate'
for token in \
  TopwayLauncherIntegrationCoordinator publishTopwayState topwayCoordinator.publishMetadata \
  topwayCoordinator.publishProgress startTopwayProgressTicker topwayCoordinator.handle \
  cmd-update topwayCoordinator.clear 'private var topwayProgressTickerJob: Job? = null'; do
  require_file_contains "$playback" "$token" 'playback service Topway integration'
done
for token in \
  GenericDofunMedia usesGenericDofunProfile defaultFor bindsTopwayCommandService \
  AutoAllSafePaths DiagnosticsOnly sendsTopwayBroadcasts handlesTopwayCommands; do
  require_file_contains "$mode" "$token" 'launcher integration mode policy'
done
for token in Percent0To100 Permille0To1000 'private fun chooseAuto' \
  'value <= 100L -> TopwaySeekUnitPolicy.Percent0To100' \
  'value <= 1000L -> TopwaySeekUnitPolicy.Permille0To1000'; do
  require_file_contains "$seek" "$token" 'Topway seek policy'
done

mode_test=app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt
require_file_contains "$mode_test" 'default policy is explicit for both compatibility states' 'pure launcher default test'
require_file_contains "$mode_test" 'topwayCompatFlavor = false' 'non-Topway fallback policy test'
require_file_contains "$mode_test" 'persisted all safe paths survives migration' 'launcher migration preservation test'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwaySeekPolicyConverterTest.kt TopwaySeekUnitPolicy.Auto 'seek auto unit tests'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinatorTest.kt countDofunTargeted 'coordinator targeted broadcast tests'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayLauncherIntegrationCoordinatorTest.kt 'cmd update republishes without toggling playback' 'command update test'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayStartRoutingPolicyTest.kt 'assertNull(negative.seekTargetMs)' 'negative seek guard test'

require_file_not_contains app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt TopwayMusicBroadcastBridge 'WidgetComponent canonical bridge ownership'
require_file_not_contains app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt topwayBridge.publishMetadata 'WidgetComponent metadata ownership'
require_file_not_contains app/src/main/java/org/oxycblt/auxio/widgets/WidgetComponent.kt topwayBridge.publishProgress 'WidgetComponent progress ownership'
require_file_contains app/build.gradle 'buildConfigField "boolean", "TOPWAY_TWMUSIC_FLAVOR", "true"' 'Topway music build flag'
require_file_contains app/build.gradle 'buildConfigField "boolean", "TOPWAY_TWMEDIA_FLAVOR", "true"' 'Topway media build flag'
require_file_contains app/build.gradle 'buildConfigField "boolean", "TOPWAY_COMPAT_FLAVOR", "true"' 'shared Topway build flag'
require_file_contains app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBroadcastBridge.kt BuildConfig.TOPWAY_COMPAT_FLAVOR 'Topway compatible bridge gate'

widget=app/src/topwayCompat/java/com/tw/music/view/MusicWidgetProvider.kt
for token in \
  startTopwayWidgetUpdateService renderColdWidgetControls 'EXTRA_APP_WIDGET_IDS = "appWidgetIds"' \
  'STOCK_MUSIC_ACTIVITY_CLASS = "com.tw.music.MusicActivity"' \
  'STOCK_WIDGET_ARTWORK_MAX_BYTES = 3_680_000' PendingIntent.getActivity CMD_UPDATE \
  safelyExtractIncomingExtras AppWidgetManager.ACTION_APPWIDGET_UPDATE; do
  require_file_contains "$widget" "$token" 'Topway widget wrapper'
done
require_file_contains app/src/main/java/org/oxycblt/auxio/playback/service/SystemPlaybackReceiver.kt TopwayWidgetProviderPolicy.shouldHandleTopwayUpdate 'system receiver widget route'
require_file_contains app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBridgeReceiver.kt safelyExtractIncomingExtras 'bridge receiver extras guard'
require_file_contains app/src/topwayCompat/java/com/tw/music/MusicService.kt AndroidEntryPoint 'Topway MusicService Hilt entry'
require_file_contains app/src/topwayCompat/java/com/tw/music/MusicService.kt AuxioService 'Topway MusicService delegate'
require_file_contains app/src/test/java/org/oxycblt/auxio/headunit/topway/TopwayWidgetProviderPolicyTest.kt topwayVariantsServeUpdateEvenWithoutNormalAppWidgetInstance 'Topway widget policy test'

for token in \
  Build.VERSION_CODES.UPSIDE_DOWN_CAKE ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE \
  FLAG_LAYOUT_IN_SCREEN FLAG_LAYOUT_NO_LIMITS 'DEFAULT_TOP_EDGE_Y = 0' getRealSize; do
  require_file_contains app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt "$token" 'overlay API/runtime guard'
done
require_file_not_contains app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt STATUS_BAR_INSET_PX 'overlay hard-coded status inset'
require_file_not_contains app/src/topwayCompat/java/org/oxycblt/auxio/car/overlay/CarFloatingControlsService.kt NAV_BAR_INSET_PX 'overlay hard-coded nav inset'
require_file_contains app/src/test/java/org/oxycblt/auxio/car/overlay/CarOverlayForegroundServiceTypePolicyTest.kt 'foregroundServiceTypeForApi(29)' 'overlay API 29 test'
require_file_contains app/src/test/java/org/oxycblt/auxio/car/overlay/CarOverlayBoundsClampingTest.kt defaultTopCenterPosition 'overlay dynamic bounds test'

require_file_contains .github/workflows/manual-release.yml assembleTopwayTwMusicRelease 'manual release builds Topway music'
require_file_contains .github/workflows/manual-release.yml assembleTopwayTwMediaRelease 'manual release builds Topway media'
require_file_contains .github/workflows/manual-release.yml topway-twmusic-release.apk 'manual release forbids raw Topway music APK name'
require_file_contains .github/workflows/manual-release.yml topway-twmedia-release.apk 'manual release names Topway media APK'

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
  if [[ -n "$path" ]]; then
    manifest_args+=("$path|$expected|$label")
  fi
done
if ((${#manifest_args[@]} == 0)); then
  warn 'merged manifests are absent; build/process a maintained Topway variant for output checks'
else
  python3 - "${manifest_args[@]}" <<'PY' || failures=$((failures + 1))
import sys
import xml.etree.ElementTree as ET
A = "{http://schemas.android.com/apk/res/android}"
failures = 0

def fail(msg):
    global failures
    failures += 1
    print(f"ERROR: {msg}", file=sys.stderr)

def ok(msg): print(f"OK: {msg}")
def attr(el, name): return el.attrib.get(A + name)

for raw in sys.argv[1:]:
    path, expected_package, label = raw.split('|', 2)
    root = ET.parse(path).getroot()
    app = root.find('application')
    if root.attrib.get('package') == expected_package: ok(f"{label} package is {expected_package}")
    else: fail(f"{label} package expected {expected_package}, got {root.attrib.get('package')!r}")
    aliases = [x for x in app.findall('activity-alias') if attr(x, 'name') == 'com.tw.music.MusicActivity']
    if len(aliases) != 1: fail(f"{label} expected one com.tw.music.MusicActivity alias, got {len(aliases)}")
    else:
        alias = aliases[0]
        if attr(alias, 'targetActivity') != 'org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity':
            fail(f"{label} alias target mismatch")
        if attr(alias, 'exported') != 'true': fail(f"{label} alias is not exported")
    providers = [p for p in app.findall('provider') if attr(p, 'authorities') == f'{expected_package}.image.CoverProvider']
    if providers: ok(f"{label} CoverProvider authority")
    else: fail(f"{label} missing CoverProvider authority")
    browser = []
    for service in app.findall('service'):
        actions = {attr(a, 'name') for f in service.findall('intent-filter') for a in f.findall('action')}
        if 'android.media.browse.MediaBrowserService' in actions: browser.append(service)
    if len(browser) != 1 or attr(browser[0], 'name') != 'com.tw.music.MusicService' or attr(browser[0], 'exported') != 'true':
        fail(f"{label} canonical MediaBrowserService mismatch")
    else: ok(f"{label} canonical MediaBrowserService")
    expected_actions = {'com.tw.music.action.cmd','com.tw.music.action.prev','com.tw.music.action.next','com.tw.music.action.pp','com.android.launcher.widget_music_progress'}
    receivers = []
    for receiver in app.findall('receiver'):
        actions = {attr(a, 'name') for f in receiver.findall('intent-filter') for a in f.findall('action')}
        if expected_actions & actions: receivers.append((receiver, actions))
    if len(receivers) != 1 or attr(receivers[0][0], 'name') != 'org.oxycblt.auxio.headunit.topway.TopwayMusicBridgeReceiver' or not expected_actions <= receivers[0][1]:
        fail(f"{label} Topway command receiver mismatch")
    else: ok(f"{label} Topway command receiver")

sys.exit(1 if failures else 0)
PY
fi

printf '\nChecking manifest-declared Topway component class packaging guardrail...\n'
bash ./scripts/check-topway-manifest-components.sh || failures=$((failures + 1))

printf '\nChecking built APK presence when outputs exist...\n'
media_debug="$(find_apk topwayTwMedia debug || true)"
music_debug="$(find_apk topwayTwMusic debug || true)"
media_release="$(find_apk topwayTwMedia release || true)"
music_release="$(find_apk topwayTwMusic release || true)"

if [[ -n "$media_debug" || -n "$music_debug" ]]; then
  [[ -n "$media_debug" ]] && pass "found topwayTwMedia debug APK: $media_debug" || fail 'topwayTwMedia debug APK missing while debug outputs exist'
  [[ -n "$music_debug" ]] && pass "found topwayTwMusic debug APK: $music_debug" || fail 'topwayTwMusic debug APK missing while debug outputs exist'
else
  warn 'debug APKs absent; run the two maintained debug assemble tasks for output checks'
fi

req_media=1
req_music=1
if [[ -n ${SELECTED_VARIANTS:-} ]]; then
  req_media=0; req_music=0
  while IFS= read -r variant; do
    case "$variant" in
      topway_twmedia) req_media=1 ;;
      topway_twmusic_magisk) req_music=1 ;;
    esac
  done <<< "$SELECTED_VARIANTS"
fi
if [[ -n "$media_release" || -n "$music_release" ]]; then
  [[ -n "$media_release" ]] && pass "found topwayTwMedia release APK: $media_release" || ((req_media == 0)) || fail 'required topwayTwMedia release APK missing'
  [[ -n "$music_release" ]] && pass "found topwayTwMusic release APK: $music_release" || ((req_music == 0)) || fail 'required topwayTwMusic release APK missing'
else
  warn 'release APKs absent; run the maintained release assemble tasks for output checks'
fi

check_apk() {
  local apk=$1 expected=$2 label=$3 analyzer="${ANDROID_HOME:-}/cmdline-tools/latest/bin/apkanalyzer"
  [[ -x "$analyzer" ]] || analyzer="$(command -v apkanalyzer || true)"
  if [[ -z "$analyzer" || ! -x "$analyzer" ]]; then warn "apkanalyzer unavailable; skipped $label binary manifest"; return; fi
  actual="$($analyzer manifest application-id "$apk")"
  [[ "$actual" == "$expected" ]] && pass "$label APK application id" || fail "$label expected $expected, got $actual"
  dump="$(mktemp)"
  "$analyzer" manifest print "$apk" > "$dump"
  for token in \
    'android:name="com.tw.music.MusicActivity"' \
    'android:targetActivity="org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"' \
    'android:name="android.media.browse.MediaBrowserService"' \
    'android:name="com.tw.music.MusicService"' \
    'android:name="com.tw.music.view.MusicWidgetProvider"' \
    "android:authorities=\"${expected}.image.CoverProvider\""; do
    grep -Fq -- "$token" "$dump" || fail "$label APK manifest lacks $token"
  done
  rm -f "$dump"
}
[[ -n "$media_debug" ]] && check_apk "$media_debug" com.tw.media.debug topwayTwMediaDebug
[[ -n "$music_debug" ]] && check_apk "$music_debug" com.tw.music.debug topwayTwMusicDebug
[[ -n "$media_release" ]] && check_apk "$media_release" com.tw.media topwayTwMediaRelease
[[ -n "$music_release" ]] && check_apk "$music_release" com.tw.music topwayTwMusicRelease

printf '\nResult: '
if ((failures == 0)); then printf 'PASS\n'; else printf 'FAIL (%d issue(s))\n' "$failures"; exit 1; fi
