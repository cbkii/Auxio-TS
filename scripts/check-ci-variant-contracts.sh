#!/usr/bin/env bash
# Guard the final two-variant Auxio-TS build and focused CI contract.

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

require_absent_regex() {
  local path=$1 pattern=$2 description=$3
  if [[ ! -f "$path" ]]; then
    fail "$description (missing file $path)"
  elif grep -Eq -- "$pattern" "$path"; then
    fail "$description (forbidden regex '$pattern' in $path)"
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
scope_script=scripts/ci-scope.sh
gradle_wrapper=scripts/ci-gradle.sh
built_apk_check=scripts/check-built-topway-apks.sh
release_diagnostics_check=scripts/check-release-diagnostics-boundary.sh
compatibility_check=scripts/check-dofun-topway-compat.sh
mode_file=app/src/main/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationMode.kt
mode_test=app/src/test/java/org/oxycblt/auxio/headunit/topway/Ts18LauncherIntegrationModeTest.kt

require_contains "$app_gradle" 'topwayTwMedia {' 'topwayTwMedia app flavour exists'
require_contains "$app_gradle" 'applicationId "com.tw.media"' 'topwayTwMedia keeps com.tw.media identity'
require_contains "$app_gradle" 'topwayTwMusic {' 'topwayTwMusic app flavour exists'
require_contains "$app_gradle" 'applicationId "com.tw.music"' 'topwayTwMusic keeps com.tw.music identity'
require_contains "$app_gradle" 'versionCode 6040700' 'version code uses the monotonic TS18 scheme above recorded stock builds'
require_absent_regex "$app_gradle" '^[[:space:]]*standard[[:space:]]*\{' 'standard app flavour is retired regardless of indentation'

require_contains "$benchmark_gradle" 'topwayTwMedia {' 'topwayTwMedia benchmark flavour exists'
require_contains "$benchmark_gradle" 'topwayTwMusic {' 'topwayTwMusic benchmark flavour exists'
require_absent_regex "$benchmark_gradle" '^[[:space:]]*standard[[:space:]]*\{' 'standard benchmark flavour is retired regardless of indentation'
require_absent "$benchmark_gradle" '"org.oxycblt.auxio"' 'benchmark module has no retired standard target package'

require_contains "$android_workflow" 'topway_twmedia: ${{ steps.scope.outputs.topway_twmedia }}' 'build workflow exports focused topwayTwMedia scope'
require_contains "$android_workflow" 'topway_twmusic: ${{ steps.scope.outputs.topway_twmusic }}' 'build workflow exports focused topwayTwMusic scope'
require_contains "$android_workflow" 'BUILD_TWMEDIA: ${{ needs.scope.outputs.topway_twmedia }}' 'build workflow consumes topwayTwMedia scope'
require_contains "$android_workflow" 'BUILD_TWMUSIC: ${{ needs.scope.outputs.topway_twmusic }}' 'build workflow consumes topwayTwMusic scope'
require_contains "$android_workflow" 'tasks+=(:app:assembleTopwayTwMediaDebug)' 'automatic build can compile primary Topway lane'
require_contains "$android_workflow" 'tasks+=(:app:assembleTopwayTwMusicDebug)' 'automatic build can compile exact-package Topway lane'
require_contains "$android_workflow" ':app:connectedTopwayTwMediaDebugAndroidTest' 'API 29 gate targets topwayTwMedia'
require_contains "$android_workflow" 'Validate selected maintained APK outputs' 'selected APKs receive binary output checks'
require_contains "$android_workflow" 'bash ./scripts/check-built-topway-apks.sh' 'workflow delegates binary checks to repository script'
require_contains "$android_workflow" 'auxio-ts-topwayTwMedia-debug' 'workflow exposes the primary debug APK as an individual artifact'
require_contains "$android_workflow" 'auxio-ts-topwayTwMusic-debug' 'workflow exposes the exact-package debug APK as an individual artifact'
require_contains "$android_workflow" 'artifact-url' 'workflow publishes direct artifact URLs in its summary'
require_contains "$android_workflow" "contains(github.event.pull_request.labels.*.name, 'ci:debug-artifacts')" 'PR debug downloads require explicit opt-in'
require_contains "$android_workflow" 'github.event.pull_request.head.repo.full_name == github.repository' 'fork PRs cannot publish downloadable debug APKs'
require_absent "$android_workflow" "github.event_name == 'push' ||" 'ordinary pushes do not create debug APK artifact bloat'
require_contains "$android_workflow" 'topwayTwMusic internal contract APK — do not install' 'exact-package debug artifact is visibly non-installable'
require_contains "$android_workflow" ':app:assembleTopwayTwMediaRelease' 'release-only source changes package the primary release on pull requests'
require_contains "$android_workflow" 'check-release-diagnostics-boundary.sh "${release_apk}"' 'pull requests inspect the optimized release DEX boundary'
require_absent "$android_workflow" 'apkanalyzer' 'workflow YAML does not duplicate APK parsing logic'
require_contains "$built_apk_check" 'bash ./scripts/check-headunit-compat-safety.sh' 'binary output check reuses canonical head-unit safety guardrail'
require_contains "$built_apk_check" 'com.tw.media.debug' 'primary APK application id is checked in repository script'
require_contains "$built_apk_check" 'com.tw.music.debug' 'exact-package APK application id is checked in repository script'
require_contains "$compatibility_check" 'req_topway_music_release=0' 'compatibility checks do not require the retired exact-package release'
require_contains "$compatibility_check" 'topway_twmusic_magisk) req_topway_music_release=1' 'explicit internal exact-package release validation remains available'

require_contains "$quality_workflow" 'app_tests: ${{ steps.scope.outputs.app_tests }}' 'quality workflow exports focused app-test scope'
require_contains "$quality_workflow" 'musikr_tests: ${{ steps.scope.outputs.musikr_tests }}' 'quality workflow exports focused Musikr-test scope'
require_contains "$quality_workflow" 'android_lint: ${{ steps.scope.outputs.android_lint }}' 'quality workflow exports focused lint scope'
require_contains "$quality_workflow" '[[ "${APP_TESTS}" == "true" ]] && tasks+=(:app:testTopwayTwMediaDebugUnitTest)' 'unit-test authority targets topwayTwMedia conditionally'
require_contains "$quality_workflow" '[[ "${MUSIKR_TESTS}" == "true" ]] && tasks+=(:musikr:testDebugUnitTest)' 'Musikr tests are independently selectable'
require_contains "$quality_workflow" '[[ "${ANDROID_LINT}" == "true" ]] && tasks+=(:app:lintTopwayTwMediaDebug)' 'lint authority targets topwayTwMedia conditionally'
require_contains "$quality_workflow" 'C/C++ formatting attempt ${attempt}/2' 'C/C++ formatter retry is bounded and visible'
require_contains "$quality_workflow" 'android-quality-cpp-gradle-*.log' 'C/C++ retry logs are preserved on failure'
require_contains "$quality_workflow" 'android-quality-gradle.log' 'quality failures preserve complete Gradle output'

for token in app_tests musikr_tests unit_tests android_lint compatibility_contracts topway_twmedia topway_twmusic; do
  require_contains "$scope_script" "${token}=" "CI scope publishes ${token}"
done
require_contains "$scope_script" "classify_path '.github/workflows/android.yml'" 'scope self-test covers workflow-only changes'
require_contains "$scope_script" "classify_path 'musikr/src/main/java/example/Parser.kt'" 'scope self-test covers Musikr-only changes'
require_contains "$scope_script" "classify_path 'app/src/topwayCompat/java/com/tw/music/MusicService.kt'" 'scope self-test covers shared Topway changes'
require_contains "$scope_script" "classify_path 'app/src/topwayTwMedia/res/values/strings.xml'" 'scope self-test covers primary variant resources'
require_contains "$scope_script" "classify_path 'app/src/topwayTwMusic/res/values/strings.xml'" 'scope self-test covers exact-package resources'
require_contains "$scope_script" "classify_path 'app/src/debug/java/example/DebugProbe.kt'" 'scope self-test covers debug-only sources'
require_contains "$scope_script" "classify_path 'app/src/release/java/example/ReleaseNoOp.kt'" 'scope self-test covers release-only sources'
if ! bash "$release_diagnostics_check"; then
  fail 'release diagnostics source-set boundary failed'
else
  pass 'release diagnostics source-set boundary passes'
fi
require_contains "$scope_script" 'full maintained Android CI must not invent an unrelated native formatter lane' 'full Android CI keeps native formatting changed-file driven'
require_contains "$scope_script" "classify_path 'musikr/src/main/cpp/example.cpp'" 'scope self-test covers native formatting selection'

require_contains "$gradle_wrapper" 'AUXIO_TS_CI_GRADLE_MAX_WORKERS:-1' 'automatic Gradle uses one worker by default'
require_contains "$gradle_wrapper" 'args+=("--max-workers=${max_workers}")' 'Gradle wrapper enforces bounded worker policy'
require_contains "$gradle_wrapper" 'AUXIO_TS_CI_GRADLE_PARALLEL' 'parallel execution remains explicit pilot input'

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
require_contains "$release_workflow" 'include_debug_apks:' 'manual release exposes separate debug companions'
require_contains "$release_workflow" 'topway-twmedia-debug.apk' 'manual release labels the debug app separately'
require_contains "$release_workflow" 'lsposed-api100-bridge-debug.apk' 'manual release labels the debug addon separately'
require_contains "$release_workflow" 'debug_artifact_dir' 'manual release isolates debug workflow artifacts'
require_contains "$release_workflow" 'release_asset_paths' 'manual release isolates GitHub Release assets'
require_contains "$release_workflow" 'Debug APKs and sidecars are forbidden on new GitHub Releases.' 'manual release forbids debug GitHub Release assets'
require_contains "$release_workflow" 'check-app-release-contracts.sh' 'manual release validates the staged primary APK identity'
require_contains "$release_workflow" 'check-release-diagnostics-boundary.sh "${asset_path}"' 'manual release verifies diagnostics are absent from the release APK'
require_absent_regex "$benchmark_workflow" '^[[:space:]]*-[[:space:]]*standard[[:space:]]*$' 'startup benchmark has no standard choice regardless of indentation'
require_absent_regex "$screenshots_workflow" '^[[:space:]]*-[[:space:]]*standard[[:space:]]*$' 'Roborazzi workflow has no standard choice regardless of indentation'

require_contains "$mode_file" 'fun defaultFor(topwayCompatFlavor: Boolean)' 'launcher default policy is testable without a flavour'
require_contains "$mode_test" 'default policy is explicit for both compatibility states' 'pure launcher default policy covers true and false states'
require_contains "$mode_test" 'topwayCompatFlavor = false' 'non-Topway fallback policy remains covered without a distributable flavour'

if ! bash ./scripts/check-startup-performance-contracts.sh; then
  fail 'startup/profile/PR208 integration contracts failed'
else
  pass 'startup/profile/PR208 integration contracts pass'
fi

if (( failures > 0 )); then
  printf 'CI variant contracts: FAIL (%d issue(s))\n' "$failures" >&2
  exit 1
fi
printf 'CI variant contracts: PASS\n'
