#!/usr/bin/env bash
# Repository-owned guardrail for the Auxio-TS three-variant architecture.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)
cd -- "$repo_root"
fail() { printf 'product contract: %s\n' "$*" >&2; exit 1; }
contains() { grep -Fq -- "$2" "$1" || fail "$1 lacks required contract: $2"; }
absent() { if grep -Fq -- "$2" "$1"; then fail "$1 contains forbidden contract: $2"; fi; }

required=(
  app/build.gradle
  app/src/main/AndroidManifest.xml
  app/src/topwayCompat/AndroidManifest.xml
  startup-benchmark/build.gradle
  scripts/package-topway-twmusic-magisk-module.sh
  .github/workflows/android.yml
  .github/workflows/lint.yml
  .github/workflows/manual-release.yml
  scripts/release-orchestrator.py
  scripts/ci-scope.sh
  scripts/check-ts18-installed-topway-media.sh
)
for path in "${required[@]}"; do [[ -s $path ]] || fail "required file missing: $path"; done

# Build identity and isolation.
contains app/build.gradle 'flavorDimensions += "distribution"'
contains app/build.gradle 'standard {'
contains app/build.gradle 'topwayTwMedia {'
contains app/build.gradle 'topwayTwMusic {'
contains app/build.gradle 'applicationId "com.tw.media"'
contains app/build.gradle 'applicationId "com.tw.music"'
contains app/build.gradle 'TOPWAY_COMPAT_ENABLED", "false"'
contains app/build.gradle 'TOPWAY_COMPAT_ENABLED", "true"'
contains app/build.gradle 'topwayTwMediaImplementation "androidx.lifecycle:lifecycle-process:'
contains app/build.gradle 'topwayTwMusicImplementation "androidx.lifecycle:lifecycle-process:'
absent app/build.gradle 'implementation "androidx.lifecycle:lifecycle-process:'

# Topway implementation must be attached only to Topway source sets, never main.
contains app/build.gradle 'src/topwayCompat/AndroidManifest.xml'
contains app/build.gradle 'src/topwayCompat/java'
if grep -Eq '(^|[[:space:]])main[[:space:]]*\{[^}]*topwayCompat' app/build.gradle; then
  fail 'Topway compatibility sources are injected into main'
fi

# Standard manifest/resource boundary: no stock-compatible wrappers or DoFun/boot overlay components.
for token in \
  'com.tw.music.MusicActivity' \
  'com.tw.music.MusicService' \
  'com.tw.music.view.MusicWidgetProvider' \
  'TopwayMusicCommandReceiver' \
  'CarOverlayBootReceiver' \
  'FloatingControlsLauncher'; do
  absent app/src/main/AndroidManifest.xml "$token"
  contains app/src/topwayCompat/AndroidManifest.xml "$token"
done
python3 scripts/check-manifest-alias-target.py app/src/topwayCompat/AndroidManifest.xml \
  com.tw.music.MusicActivity org.oxycblt.auxio.MainActivity >/dev/null

# Provider authorities are package-specific; Standard remains neutral.
contains app/src/main/res/values/donottranslate.xml 'org.oxycblt.auxio.image.CoverProvider'
contains app/src/topwayTwMedia/res/values/donottranslate.xml 'com.tw.media.image.CoverProvider'
contains app/src/topwayTwMusic/res/values/donottranslate.xml 'com.tw.music.image.CoverProvider'

# Benchmark identity mirrors the three variants and keeps API 29 explicit.
for token in \
  'standard {' \
  'topwayTwMedia {' \
  'topwayTwMusic {' \
  '"org.oxycblt.auxio"' \
  '"com.tw.media"' \
  '"com.tw.music"' \
  'apiLevel = 29'; do
  contains startup-benchmark/build.gradle "$token"
done

# Canonical CI must use flavour-qualified tasks. Raw exact-package output may be built internally,
# but publication is exclusively the Magisk ZIP.
contains .github/workflows/android.yml ':app:assembleStandardDebug'
contains .github/workflows/android.yml ':app:assembleTopwayTwMediaDebug'
contains .github/workflows/android.yml ':app:assembleTopwayTwMusicDebug'
contains .github/workflows/android.yml ':app:connectedStandardDebugAndroidTest'
contains .github/workflows/android.yml ':app:connectedTopwayTwMediaDebugAndroidTest'
contains .github/workflows/android.yml ':app:connectedTopwayTwMusicDebugAndroidTest'
contains .github/workflows/lint.yml ':app:testStandardDebugUnitTest'
contains .github/workflows/lint.yml ':app:testTopwayTwMediaDebugUnitTest'
contains .github/workflows/lint.yml ':app:testTopwayTwMusicDebugUnitTest'
contains .github/workflows/lint.yml ':app:lintStandardDebug'
contains .github/workflows/lint.yml ':app:lintTopwayTwMediaDebug'
contains .github/workflows/lint.yml ':app:lintTopwayTwMusicDebug'

release_surface=$(mktemp)
cleanup() { rm -f -- "$release_surface"; }
trap cleanup EXIT
cat .github/workflows/manual-release.yml scripts/release-orchestrator.py scripts/manual-release/*.sh > "$release_surface"

for token in \
  'include_standard_apk:' \
  'include_topway_twmedia_apk:' \
  'include_topway_twmusic_magisk:' \
  'include_lsposed_bridge_apk:'; do
  contains .github/workflows/manual-release.yml "$token"
done
absent .github/workflows/manual-release.yml 'include_app_apk:'
contains "$release_surface" ':app:assembleStandardRelease'
contains "$release_surface" ':app:assembleTopwayTwMediaRelease'
contains "$release_surface" ':app:assembleTopwayTwMusicRelease'
contains "$release_surface" 'package-topway-twmusic-magisk-module.sh'
contains "$release_surface" 'topway-twmusic-magisk.zip'

# No release path may upload or name a raw topwayTwMusic APK as a public asset.
if grep -Eiq 'upload[^\n]*(topway[-_]?twmusic[^\n]*\.apk)|Auxio-TS-[^[:space:]]*topway-twmusic[^[:space:]]*\.apk' "$release_surface"; then
  fail 'raw topwayTwMusic APK publication path detected'
fi

# Magisk packager is systemless, exact-target and fail-closed.
packager=scripts/package-topway-twmusic-magisk-module.sh
contains "$packager" 'system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk'
contains "$packager" 'STOP: expected TS18 stock com.tw.music target not found'
contains "$packager" 'does not grant platform signing'
for destructive in 'rm "$STOCK_APK"' 'mv "$STOCK_APK"' 'pm disable' 'pm uninstall' 'mount -o remount' 'dd if='; do
  absent "$packager" "$destructive"
done

# LSPosed remains optional and separate, statically scoped to genuine stock com.tw.music.
contains settings.gradle "include ':lsposed-bridge'"
contains settings.gradle "include ':libxposed-api100-stubs'"
contains lsposed-bridge/build.gradle 'applicationId "org.oxycblt.auxio.ts18bridge"'
[[ $(tr -d '\r\n' < lsposed-bridge/src/main/resources/META-INF/xposed/scope.list) == com.tw.music ]] ||
  fail 'LSPosed static scope must be exactly com.tw.music'
if grep -Fq 'implementation project(":lsposed-bridge")' app/build.gradle; then
  fail 'optional LSPosed add-on must not be an app dependency'
fi

bash scripts/check-ts18-installed-topway-media.sh --self-test >/dev/null ||
  fail 'TS18 installed-package preflight self-test failed'
bash scripts/ci-scope.sh --self-test >/dev/null
printf 'Auxio-TS three-variant/systemless package contracts: PASS\n'
