#!/usr/bin/env bash
# Classify the current GitHub change set into the smallest safe Auxio-TS CI lanes.
# Every uncertain comparison fails open to full maintained-variant validation.

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || {
  printf '::error::Cannot resolve repository root for CI scope classification.\n' >&2
  exit 1
}
cd -- "$repo_root" || {
  printf '::error::Cannot enter repository root: %s\n' "$repo_root" >&2
  exit 1
}

log() { printf '[CI scope] %s\n' "$*" >&2; }
warn() { printf '::warning::%s\n' "$*" >&2; }
fail() { printf '::error::%s\n' "$*" >&2; exit 1; }

reset_flags() {
  static_checks=true
  static_only=true
  gradle_configuration=false
  app_core=false
  musikr=false
  topway_shared=false
  topway_twmedia=false
  topway_twmusic=false
  android_runtime_high_risk=false
  native_cpp=false
  benchmark=false
  release=false
  legacy_standard=false
  formatting=false
  format_cpp=false
  gradle_quality=false
  topway_build=false
  api29=false
  full=false
}

mark_full() {
  full=true
  static_only=false
  static_checks=true
  gradle_configuration=true
  app_core=true
  musikr=true
  topway_shared=true
  topway_twmedia=true
  topway_twmusic=true
  android_runtime_high_risk=true
  native_cpp=true
  benchmark=true
  release=true
  formatting=true
  format_cpp=true
  gradle_quality=true
  topway_build=true
  api29=true
}

mark_topway_app_work() {
  app_core=true
  topway_shared=true
  gradle_quality=true
  topway_build=true
  formatting=true
  static_only=false
}

classify_path() {
  local path=$1

  case "$path" in
    .github/workflows/*|build.gradle|settings.gradle|gradle.properties|gradlew|gradlew.bat|gradle/*|.gitmodules|ci/dependencies/*)
      mark_full
      return
      ;;
    app/build.gradle|startup-benchmark/build.gradle|musikr/build.gradle)
      gradle_configuration=true
      gradle_quality=true
      topway_build=true
      formatting=true
      static_only=false
      [[ "$path" == startup-benchmark/* ]] && benchmark=true
      return
      ;;
    scripts/ci-gradle.sh|scripts/bootstrap-dependencies.sh|scripts/dependency-lib.sh|scripts/prepare-ci-environment.sh|scripts/check-submodules.sh|scripts/ci-scope.sh)
      mark_full
      return
      ;;
    .github/dependabot.yml)
      static_checks=true
      release=true
      return
      ;;
  esac

  case "$path" in
    app/src/topwayCompat/*)
      mark_topway_app_work
      topway_twmedia=true
      topway_twmusic=true
      ;;
    app/src/topwayTwMedia/*|app/src/topwayTwMediaDebug/*)
      mark_topway_app_work
      topway_twmedia=true
      ;;
    app/src/topwayTwMusic/*|app/src/topwayTwMusicDebug/*)
      mark_topway_app_work
      topway_twmusic=true
      ;;
    app/src/*)
      mark_topway_app_work
      topway_twmedia=true
      topway_twmusic=true
      ;;
    musikr/src/*)
      musikr=true
      gradle_quality=true
      topway_build=true
      formatting=true
      static_only=false
      ;;
    media|media/*)
      gradle_configuration=true
      app_core=true
      musikr=true
      topway_shared=true
      topway_twmedia=true
      topway_twmusic=true
      gradle_quality=true
      topway_build=true
      formatting=true
      android_runtime_high_risk=true
      api29=true
      static_only=false
      ;;
    startup-benchmark/*)
      benchmark=true
      topway_twmedia=true
      topway_twmusic=true
      gradle_quality=true
      topway_build=true
      formatting=true
      static_only=false
      ;;
    scripts/check-*|scripts/package-*|scripts/summarize-*|scripts/*.py)
      static_checks=true
      ;;
    scripts/*)
      static_checks=true
      ;;
    README.md|AGENTS.md|NOTICE|eclipse-cdt.xml|docs/*)
      static_checks=true
      ;;
  esac

  case "$path" in
    *.kt|*.kts|*.java|*.xml|*.gradle|*.cpp|*.c|*.h|*.hpp)
      formatting=true
      ;;
  esac
  case "$path" in
    *.cpp|*.c|*.h|*.hpp|eclipse-cdt.xml)
      native_cpp=true
      format_cpp=true
      ;;
  esac

  case "$path" in
    app/src/main/AndroidManifest.xml|app/src/topwayCompat/AndroidManifest.xml|app/src/*/AndroidManifest.xml|\
    *Database*|*Migration*|*Service*|*Receiver*|*Provider*|*Application*|*Auxio.kt|\
    *MusicRepository*|*MusicSettings*|*Indexing*|*Storage*|*locations*|*startup*|*Startup*|\
    startup-benchmark/*)
      android_runtime_high_risk=true
      api29=true
      ;;
  esac
}

emit_outputs() {
  local output=${GITHUB_OUTPUT:-}
  local changed_count=$1
  local comparison=$2
  local pairs=(
    "static_only=$static_only"
    "static_checks=$static_checks"
    "gradle_configuration=$gradle_configuration"
    "app_core=$app_core"
    "musikr=$musikr"
    "topway_shared=$topway_shared"
    "topway_twmedia=$topway_twmedia"
    "topway_twmusic=$topway_twmusic"
    "android_runtime_high_risk=$android_runtime_high_risk"
    "native_cpp=$native_cpp"
    "benchmark=$benchmark"
    "release=$release"
    "legacy_standard=$legacy_standard"
    "formatting=$formatting"
    "format_cpp=$format_cpp"
    "gradle_quality=$gradle_quality"
    "topway_build=$topway_build"
    "api29=$api29"
    "full=$full"
    "changed_count=$changed_count"
    "comparison=$comparison"
  )

  printf '%s\n' "${pairs[@]}"
  if [[ -n "$output" ]]; then
    printf '%s\n' "${pairs[@]}" >> "$output" || fail "Cannot write GitHub outputs to $output"
  fi
}

self_test() {
  reset_flags
  classify_path 'docs/CI_TASK_POLICY.md'
  [[ "$static_only" == true && "$gradle_quality" == false && "$topway_build" == false ]] ||
    fail 'Self-test: docs-only classification is not static-only.'

  reset_flags
  classify_path 'app/src/main/java/example/MusicRepository.kt'
  [[ "$gradle_quality" == true && "$topway_build" == true && "$api29" == true ]] ||
    fail 'Self-test: app runtime classification missed maintained validation.'

  reset_flags
  classify_path 'app/src/topwayCompat/java/com/tw/music/MusicService.kt'
  [[ "$topway_shared" == true && "$topway_twmedia" == true && "$topway_twmusic" == true ]] ||
    fail 'Self-test: shared Topway source must select both maintained variants.'

  reset_flags
  classify_path 'app/src/topwayTwMedia/res/values/strings.xml'
  [[ "$topway_twmedia" == true && "$topway_twmusic" == false && "$topway_build" == true ]] ||
    fail 'Self-test: topwayTwMedia-specific classification is not focused.'

  reset_flags
  classify_path '.github/workflows/android.yml'
  [[ "$full" == true && "$api29" == true && "$format_cpp" == true ]] ||
    fail 'Self-test: workflow changes must fail open to full CI.'

  log 'self-test PASS'
}

if [[ ${1:-} == '--self-test' ]]; then
  self_test
  exit 0
fi

reset_flags

event=${CI_EVENT_NAME:-${GITHUB_EVENT_NAME:-}}
head_sha=${CI_HEAD_SHA:-${GITHUB_SHA:-}}
base_sha=${CI_BASE_SHA:-}
before_sha=${CI_BEFORE_SHA:-}
force_full=${CI_FORCE_FULL:-false}
ref_name=${CI_REF_NAME:-${GITHUB_REF_NAME:-}}
comparison=''
changed_files=''

if [[ "$force_full" == true || "$event" == workflow_dispatch ]]; then
  log 'Full maintained validation explicitly requested.'
  mark_full
  emit_outputs 0 manual-full
  exit 0
fi

case "$event" in
  pull_request|pull_request_target)
    comparison="${base_sha}...${head_sha}"
    ;;
  push)
    if [[ -n "$before_sha" && ! "$before_sha" =~ ^0+$ ]]; then
      comparison="${before_sha}..${head_sha}"
    fi
    ;;
  *)
    warn "Unknown or missing event '$event'; running full maintained validation."
    mark_full
    emit_outputs 0 unknown-full
    exit 0
    ;;
esac

if [[ -z "$comparison" || -z "$head_sha" ]] ||
   ! git cat-file -e "${head_sha}^{commit}" 2>/dev/null ||
   ! git diff --name-only "$comparison" >/dev/null 2>&1; then
  warn "No trustworthy comparison range is available (${comparison:-none}); running full maintained validation."
  mark_full
  emit_outputs 0 comparison-unavailable
  exit 0
fi

changed_files=$(git diff --name-only "$comparison")
changed_count=0
while IFS= read -r path; do
  [[ -n "$path" ]] || continue
  changed_count=$((changed_count + 1))
  classify_path "$path"
done <<< "$changed_files"

if (( changed_count == 0 )); then
  warn "Comparison $comparison contains no changed files; running static checks only."
fi

# Every integration push to dev retains the API 29 Android 10 gate, even for a narrow change.
if [[ "$event" == push && "$ref_name" == dev ]]; then
  api29=true
  android_runtime_high_risk=true
  static_only=false
fi

# Runtime/API 29 changes necessarily compile and test the maintained primary app lane.
if [[ "$api29" == true ]]; then
  gradle_quality=true
  topway_build=true
  topway_twmedia=true
  static_only=false
fi

log "comparison=$comparison changed=$changed_count static_only=$static_only gradle_quality=$gradle_quality topway_build=$topway_build api29=$api29"
emit_outputs "$changed_count" "$comparison"
