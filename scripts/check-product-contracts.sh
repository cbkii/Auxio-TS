#!/usr/bin/env bash
# Repository-owned guardrail for the Auxio-TS single-product architecture.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)
cd -- "$repo_root"
fail() { printf 'product contract: %s\n' "$*" >&2; exit 1; }
contains() { grep -Fq -- "$2" "$1" || fail "$1 lacks required contract: $2"; }
absent() { if grep -Fq -- "$2" "$1"; then fail "$1 contains retired contract: $2"; fi; }

required=(
  app/build.gradle
  app/src/main/AndroidManifest.xml
  startup-benchmark/build.gradle
  settings.gradle
  lsposed-bridge/build.gradle
  lsposed-bridge/src/main/resources/META-INF/xposed/scope.list
  .github/workflows/android.yml
  .github/workflows/lint.yml
  .github/workflows/manual-release.yml
  scripts/ci-scope.sh
)
for path in "${required[@]}"; do [[ -s $path ]] || fail "required file missing: $path"; done

contains app/build.gradle 'namespace "org.oxycblt.auxio"'
contains app/build.gradle 'applicationId "com.tw.media"'
contains app/build.gradle 'TOPWAY_COMPAT_ENABLED", "true"'
contains app/build.gradle 'java.srcDirs += ["src/topwayCompat/java"]'
contains app/build.gradle 'kotlin.srcDirs += ["src/topwayCompat/java", "src/topwayCompat/kotlin"]'
contains startup-benchmark/build.gradle 'TARGET_PACKAGE", '\''"com.tw.media"'\'''
for token in flavorDimensions productFlavors 'applicationId "com.tw.music"' 'topwayTwMusic' 'topwayTwMedia'; do
  absent app/build.gradle "$token"
done
for token in flavorDimensions productFlavors topwayTwMusic topwayTwMedia 'org.oxycblt.auxio"'; do
  absent startup-benchmark/build.gradle "$token"
done

python3 scripts/check-manifest-alias-target.py app/src/main/AndroidManifest.xml \
  com.tw.music.MusicActivity org.oxycblt.auxio.MainActivity >/dev/null
contains app/src/main/AndroidManifest.xml 'android:name="com.tw.music.MusicService"'
contains app/src/main/AndroidManifest.xml 'android:name=".AuxioService"'
contains app/src/main/AndroidManifest.xml 'android:name="com.tw.music.view.MusicWidgetProvider"'
contains app/src/main/res/values/donottranslate.xml 'com.tw.media.image.CoverProvider'
contains app/src/debug/res/values/donottranslate.xml 'com.tw.media.debug.image.CoverProvider'

for retired in \
  app/src/topwayCompat/AndroidManifest.xml \
  app/src/topwayTwMusic app/src/topwayTwMusicDebug \
  app/src/topwayTwMedia app/src/topwayTwMediaDebug; do
  [[ ! -e $retired ]] || fail "retired application source-set remains: $retired"
done

workflow_surface=$(mktemp)
release_surface=$(mktemp)
cleanup() { rm -f -- "$workflow_surface" "$release_surface"; }
trap cleanup EXIT
find .github/workflows -maxdepth 1 -type f -name '*.yml' -print0 | sort -z | xargs -0 cat > "$workflow_surface"
cat .github/workflows/manual-release.yml scripts/release-orchestrator.py scripts/manual-release/*.sh > "$release_surface"

if grep -Eq ':(assemble|bundle|test|lint|connected[A-Za-z]*|recordRoborazzi|verifyRoborazzi|compareRoborazzi)[A-Za-z]*TopwayTw(Music|Media)' "$workflow_surface"; then
  fail 'an active workflow still invokes a retired product-flavour task'
fi
if grep -Eq 'app/build/outputs/apk/topwayTw(Music|Media)' "$workflow_surface" "$release_surface"; then
  fail 'an active build or release path still reads a retired flavour APK directory'
fi
contains .github/workflows/android.yml ':app:assembleDebug'
contains .github/workflows/android.yml ':app:assembleRelease'
contains .github/workflows/lint.yml ':app:testDebugUnitTest'
contains .github/workflows/lint.yml ':app:lintDebug'
contains .github/workflows/manual-release.yml 'include_app_apk:'
contains .github/workflows/manual-release.yml 'include_lsposed_bridge_apk:'
python3 - <<'PY'
import re
from pathlib import Path

workflow = Path('.github/workflows/manual-release.yml').read_text(encoding='utf-8')
match = re.search(
    r'(?ms)^      include_lsposed_bridge_apk:\s*\n(?P<body>(?:^        .*\n?)*)',
    workflow,
)
if match is None or re.search(r'(?m)^        default:\s*false\s*$', match.group('body')) is None:
    raise SystemExit('product contract: include_lsposed_bridge_apk must default to false')
PY
contains "$release_surface" ':app:assembleRelease'
contains "$release_surface" ':lsposed-bridge:assembleRelease'
absent "$release_surface" ':app:assembleTopwayTwMusicRelease'

contains settings.gradle "include ':lsposed-bridge'"
contains settings.gradle "include ':libxposed-api100-stubs'"
contains lsposed-bridge/build.gradle 'applicationId "org.oxycblt.auxio.ts18bridge"'
contains lsposed-bridge/build.gradle 'compileOnly(project(":libxposed-api100-stubs"))'
[[ $(tr -d '\r\n' < lsposed-bridge/src/main/resources/META-INF/xposed/scope.list) == com.tw.music ]] ||
  fail 'LSPosed static scope must be exactly com.tw.music'
if grep -Fq 'implementation project(":lsposed-bridge")' app/build.gradle; then
  fail 'optional LSPosed add-on must not be an app dependency'
fi

bash scripts/ci-scope.sh --self-test >/dev/null
printf 'Auxio-TS single-product contracts: PASS\n'
