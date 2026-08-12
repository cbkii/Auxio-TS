#!/usr/bin/env bash
# Static Track-A compatibility checks for the maintained com.tw.media application.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)
cd -- "$repo_root"
fail() { printf 'DoFun/Topway contract: %s\n' "$*" >&2; exit 1; }
contains() { grep -Fq -- "$2" "$1" || fail "$3 ($1 lacks: $2)"; }
absent() { if grep -Fq -- "$2" "$1"; then fail "$3 ($1 contains: $2)"; fi; }

gradle=app/build.gradle
manifest=app/src/main/AndroidManifest.xml
[[ -s $gradle && -s $manifest ]] || fail 'app Gradle or main manifest is missing'

contains "$gradle" 'applicationId "com.tw.media"' 'maintained application identity changed'
contains "$gradle" 'namespace "org.oxycblt.auxio"' 'source namespace changed'
contains "$gradle" 'TOPWAY_COMPAT_ENABLED", "true"' 'Track-A compatibility is not enabled'
contains "$gradle" 'java.srcDirs += ["src/topwayCompat/java"]' 'Track-A Java sources are not part of main'
contains "$gradle" 'kotlin.srcDirs += ["src/topwayCompat/java", "src/topwayCompat/kotlin"]' 'Track-A Kotlin sources are not part of main'
absent "$gradle" 'productFlavors' 'application flavours must remain retired'
absent "$gradle" 'flavorDimensions' 'distribution flavour dimension must remain retired'
absent "$gradle" 'applicationId "com.tw.music"' 'Auxio must not impersonate the stock package'

python3 scripts/check-manifest-alias-target.py "$manifest" \
  com.tw.music.MusicActivity org.oxycblt.auxio.MainActivity >/dev/null
for token in \
  'com.tw.music.MusicService' \
  'com.tw.music.view.MusicWidgetProvider' \
  'android:name=".AuxioService"' \
  'org.oxycblt.auxio.car.overlay.CarFloatingControlsService' \
  'android.intent.action.MUSIC_PLAYER' \
  'android.intent.category.APP_MUSIC' \
  'android:foregroundServiceType="specialUse"' \
  'android.permission.FOREGROUND_SERVICE_SPECIAL_USE'; do
  contains "$manifest" "$token" 'required Track-A component or permission missing'
done
for token in 'android:sharedUserId'; do
  absent "$manifest" "$token" 'protected identity or duplicate command authority detected'
done

for source in \
  app/src/topwayCompat/java/com/tw/music/MusicService.kt \
  app/src/topwayCompat/java/com/tw/music/view/MusicWidgetProvider.kt \
  app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBroadcastBridge.kt \
  app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayServiceBridge.kt \
  app/src/main/java/org/oxycblt/auxio/headunit/topway/LauncherIntegrationTelemetry.kt \
  app/src/main/java/org/oxycblt/auxio/playback/PlaybackPanelFragment.kt; do
  [[ -s $source ]] || fail "required Track-A source missing: $source"
done

for authority in \
  app/src/main/res/values/donottranslate.xml:com.tw.media.image.CoverProvider \
  app/src/debug/res/values/donottranslate.xml:com.tw.media.debug.image.CoverProvider; do
  path=${authority%%:*}; value=${authority#*:}
  contains "$path" "$value" 'cover-provider identity is incorrect'
done

for retired in app/src/topwayCompat/AndroidManifest.xml app/src/topwayTwMusic app/src/topwayTwMusicDebug app/src/topwayTwMedia app/src/topwayTwMediaDebug; do
  [[ ! -e $retired ]] || fail "retired application source-set remains: $retired"
done

bash scripts/check-topway-manifest-components.sh

if find app/build/outputs/apk -type f -name '*.apk' -print -quit 2>/dev/null | grep -q .; then
  if command -v apkanalyzer >/dev/null 2>&1 || [[ -x ${ANDROID_HOME:-}/cmdline-tools/latest/bin/apkanalyzer ]]; then
    BUILD_APP=true bash scripts/check-built-topway-apks.sh
  else
    printf 'DoFun/Topway contract: APK outputs present; binary validation skipped because apkanalyzer is unavailable.\n'
  fi
fi

printf 'DoFun/Topway Track-A compatibility checks: PASS\n'
