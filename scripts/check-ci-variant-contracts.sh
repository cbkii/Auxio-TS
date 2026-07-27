#!/usr/bin/env bash
# Guard the final two-variant Auxio-TS build and CI contract.

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || {
  printf '::error::Cannot resolve repository root.\n' >&2
  exit 1
}
cd -- "$repo_root" || exit 1

failures=0
pass() { printf 'OK: %s\n' "$*"; }
fail() { printf 'ERROR: %s\n' "$*" >&2; failures=$((failures + 1)); }

require_contains() {
  local path=$1 pattern=$2 description=$3
  if [[ -f "$path" ]] && grep -Fq -- "$pattern" "$path"; then
    pass "$description"
  else
    fail "$description (missing '$pattern' in $path)"
  fi
}

require_absent() {
  local path=$1 pattern=$2 description=$3
  if [[ ! -f "$path" ]]; then
    fail "$description (missing file $path)"
  elif grep -Fq -- "$pattern" "$path"; then
    fail "$description (forbidden '$pattern' in $path)"
  else
    pass "$description"
  fi
}

app_gradle=app/build.gradle
benchmark_gradle=startup-benchmark/build.gradle
android_workflow=.github/workflows/android.yml
quality_workflow=.github/workflows/lint.yml
startup_workflow=.github/workflows/startup-performance.yml
benchmark_workflow=.github/workflows/startup-benchmarks.yml
screenshots_workflow=.github/workflows/ui-screenshots.yml
release_workflow=.github/workflows/manual-release.yml
mode_file=app/src/main/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationMode.kt
mode_test=app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt

require_contains "$app_gradle" 'topwayTwMedia {' 'topwayTwMedia app flavour exists'
require_contains "$app_gradle" 'applicationId "com.tw.media"' 'topwayTwMedia keeps com.tw.media identity'
require_contains "$app_gradle" 'topwayTwMusic {' 'topwayTwMusic app flavour exists'
require_contains "$app_gradle" 'applicationId "com.tw.music"' 'topwayTwMusic keeps com.tw.music identity'
require_absent "$app_gradle" '        standard {' 'standard app flavour is retired'

require_contains "$benchmark_gradle" 'topwayTwMedia {' 'topwayTwMedia benchmark flavour exists'
require_contains "$benchmark_gradle" 'topwayTwMusic {' 'topwayTwMusic benchmark flavour exists'
require_absent "$benchmark_gradle" '        standard {' 'standard benchmark flavour is retired'
require_absent "$benchmark_gradle" 'org.oxycblt.auxio' 'benchmark module has no retired standard target package'

require_contains "$android_workflow" ':app:assembleTopwayTwMediaDebug' 'automatic build compiles primary Topway lane'
require_contains "$android_workflow" ':app:assembleTopwayTwMusicDebug' 'automatic build compiles exact-package Topway lane'
require_contains "$android_workflow" ':app:connectedTopwayTwMediaDebugAndroidTest' 'API 29 gate targets topwayTwMedia'
require_contains "$quality_workflow" ':app:testTopwayTwMediaDebugUnitTest' 'unit-test authority targets topwayTwMedia'
require_contains "$quality_workflow" ':app:lintTopwayTwMediaDebug' 'lint authority targets topwayTwMedia'
require_contains "$quality_workflow" 'scripts/ci-scope.sh' 'quality workflow uses central changed-file scope'
require_contains "$android_workflow" 'scripts/ci-scope.sh' 'build workflow uses central changed-file scope'

for path in \
  "$android_workflow" \
  "$quality_workflow" \
  "$startup_workflow" \
  "$benchmark_workflow" \
  "$screenshots_workflow" \
  "$release_workflow"; do
  require_absent "$path" 'StandardDebug' "$path has no retired StandardDebug task"
  require_absent "$path" 'StandardRelease' "$path has no retired StandardRelease task"
  require_absent "$path" 'standard-release.apk' "$path has no retired standard release asset"
done
require_absent "$release_workflow" 'include_standard_apk' 'manual release has no standard selection'
require_absent "$benchmark_workflow" '          - standard' 'startup benchmark has no standard choice'
require_absent "$screenshots_workflow" '          - standard' 'Roborazzi workflow has no standard choice'

require_contains "$mode_file" 'fun defaultFor(topwayCompatFlavor: Boolean)' 'launcher default policy is testable without a flavour'
require_contains "$mode_test" 'default policy is explicit for both compatibility states' 'pure launcher default policy covers true and false states'
require_contains "$mode_test" 'topwayCompatFlavor = false' 'non-Topway fallback policy remains covered without a distributable flavour'

if (( failures > 0 )); then
  printf 'CI variant contracts: FAIL (%d issue(s))\n' "$failures" >&2
  exit 1
fi
printf 'CI variant contracts: PASS\n'
