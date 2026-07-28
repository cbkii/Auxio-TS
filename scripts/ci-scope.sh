#!/usr/bin/env bash
# Classify the current GitHub change set into the smallest safe Auxio-TS CI lanes.
# Every unknown path or uncertain comparison fails open to full maintained-variant validation.

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
  app_tests=false
  musikr_tests=false
  unit_tests=false
  android_lint=false
  compatibility_contracts=false
  gradle_quality=false
  topway_build=false
  api29=false
  full=false
}

mark_full() {
  # "Full" means every maintained Android/Topway lane. Native formatting remains
  # changed-file driven because Eclipse CDT provisioning is an independent external
  # P2 dependency and provides no evidence for a change set with no C/C++ files.
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
  benchmark=true
  release=true
  formatting=true
  app_tests=true
  musikr_tests=true
  android_lint=true
  compatibility_contracts=true
  api29=true
}

mark_app_primary() {
  app_core=true
  topway_twmedia=true
  app_tests=true
  formatting=true
  static_only=false
}

mark_topway_shared() {
  mark_app_primary
  topway_shared=true
  topway_twmusic=true
  android_lint=true
  compatibility_contracts=true
}

mark_musikr() {
  musikr=true
  musikr_tests=true
  formatting=true
  # One primary application compile is sufficient to prove Musikr integration.
  topway_twmedia=true
  static_only=false
}

classify_path() {
  local path=$1
  local matched=false

  case "$path" in
    build.gradle|settings.gradle|gradle.properties|gradlew|gradlew.bat|gradle/*|.gitmodules|ci/dependencies/*)
      mark_full
      matched=true
      ;;
    app/build.gradle|startup-benchmark/build.gradle|musikr/build.gradle)
      mark_full
      [[ "$path" == startup-benchmark/* ]] && benchmark=true
      matched=true
      ;;
    scripts/ci-gradle.sh|scripts/bootstrap-dependencies.sh|scripts/dependency-lib.sh|scripts/prepare-ci-environment.sh|scripts/check-submodules.sh)
      # These scripts control every Gradle/dependency lane; validate all maintained outputs.
      mark_full
      matched=true
      ;;
    .github/workflows/*|.github/dependabot.yml|scripts/ci-scope.sh|scripts/check-*|scripts/package-*|scripts/summarize-*|scripts/*.py|scripts/*.sh)
      # Workflow/shell-only edits receive syntax and executable contract checks. A caller can
      # request all maintained Android lanes with ci:full or workflow_dispatch.
      static_checks=true
      [[ "$path" == .github/workflows/manual-release.yml || "$path" == scripts/package-* ]] && release=true
      matched=true
      ;;
    README.md|AGENTS.md|NOTICE|eclipse-cdt.xml|docs/*)
      static_checks=true
      matched=true
      ;;
    app/src/topwayCompat/*)
      mark_topway_shared
      matched=true
      ;;
    app/src/topwayTwMedia/*|app/src/topwayTwMediaDebug/*)
      mark_app_primary
      android_lint=true
      compatibility_contracts=true
      matched=true
      ;;
    app/src/topwayTwMusic/*|app/src/topwayTwMusicDebug/*)
      app_core=true
      topway_twmusic=true
      formatting=true
      compatibility_contracts=true
      static_only=false
      matched=true
      ;;
    app/src/androidTest/*|app/src/benchmark/*)
      mark_app_primary
      android_runtime_high_risk=true
      api29=true
      matched=true
      ;;
    app/src/test/*)
      mark_app_primary
      matched=true
      ;;
    app/src/main/*)
      mark_app_primary
      android_lint=true
      matched=true
      ;;
    musikr/src/*)
      mark_musikr
      matched=true
      ;;
    media|media/*)
      mark_full
      matched=true
      ;;
    startup-benchmark/*)
      benchmark=true
      formatting=true
      topway_twmedia=true
      static_only=false
      matched=true
      ;;
  esac

  case "$path" in
    *.kt|*.kts|*.java|*.gradle)
      formatting=true
      ;;
    *.cpp|*.c|*.h|*.hpp)
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

  if [[ "$matched" != true ]]; then
    warn "Unclassified path '$path'; running full maintained validation."
    mark_full
  fi
}

finalise_flags() {
  if [[ "$app_tests" == true || "$musikr_tests" == true ]]; then
    unit_tests=true
  fi
  if [[ "$formatting" == true || "$format_cpp" == true || "$unit_tests" == true || "$android_lint" == true ]]; then
    gradle_quality=true
    static_only=false
  fi
  if [[ "$topway_twmedia" == true || "$topway_twmusic" == true ]]; then
    topway_build=true
    static_only=false
  fi
  if [[ "$api29" == true ]]; then
    # API 29 exercises the primary maintained application and Musikr instrumentation lane.
    topway_twmedia=true
    topway_build=true
    app_tests=true
    musikr_tests=true
    unit_tests=true
    android_lint=true
    gradle_quality=true
    static_only=false
  fi
}

emit_outputs() {
  local output=${GITHUB_OUTPUT:-}
  local changed_count=$1
  local comparison=$2
  finalise_flags
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
    "app_tests=$app_tests"
    "musikr_tests=$musikr_tests"
    "unit_tests=$unit_tests"
    "android_lint=$android_lint"
    "compatibility_contracts=$compatibility_contracts"
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
  finalise_flags
  [[ "$static_only" == true && "$gradle_quality" == false && "$topway_build" == false ]] ||
    fail 'Self-test: docs-only classification is not static-only.'

  reset_flags
  classify_path '.github/workflows/android.yml'
  finalise_flags
  [[ "$static_only" == true && "$gradle_quality" == false && "$topway_build" == false ]] ||
    fail 'Self-test: workflow-only classification must stay static unless full CI is requested.'

  reset_flags
  classify_path 'app/src/main/java/example/PresentationPolicy.kt'
  finalise_flags
  [[ "$app_tests" == true && "$android_lint" == true && "$topway_twmedia" == true && "$topway_twmusic" == false && "$api29" == false ]] ||
    fail 'Self-test: ordinary app code did not select focused primary validation.'

  reset_flags
  classify_path 'app/src/main/java/example/MusicRepository.kt'
  finalise_flags
  [[ "$api29" == true && "$topway_twmedia" == true && "$musikr_tests" == true ]] ||
    fail 'Self-test: high-risk app runtime change missed API 29 maintained validation.'

  reset_flags
  classify_path 'musikr/src/main/java/example/Parser.kt'
  finalise_flags
  [[ "$musikr_tests" == true && "$topway_twmedia" == true && "$topway_twmusic" == false ]] ||
    fail 'Self-test: Musikr change must run Musikr tests plus one primary app compile.'

  reset_flags
  classify_path 'app/src/topwayCompat/java/com/tw/music/MusicService.kt'
  finalise_flags
  [[ "$topway_shared" == true && "$topway_twmedia" == true && "$topway_twmusic" == true && "$compatibility_contracts" == true ]] ||
    fail 'Self-test: shared Topway source must select both maintained variants and contracts.'

  reset_flags
  classify_path 'app/src/topwayTwMedia/res/values/strings.xml'
  finalise_flags
  [[ "$topway_twmedia" == true && "$topway_twmusic" == false && "$topway_build" == true ]] ||
    fail 'Self-test: topwayTwMedia-specific classification is not focused.'

  reset_flags
  classify_path 'app/src/topwayTwMusic/res/values/strings.xml'
  finalise_flags
  [[ "$topway_twmusic" == true && "$topway_twmedia" == false && "$compatibility_contracts" == true ]] ||
    fail 'Self-test: topwayTwMusic-specific classification is not focused.'

  reset_flags
  mark_full
  finalise_flags
  [[ "$full" == true && "$api29" == true && "$topway_twmedia" == true && "$topway_twmusic" == true && "$format_cpp" == false ]] ||
    fail 'Self-test: full maintained Android CI must not invent an unrelated native formatter lane.'

  reset_flags
  classify_path 'musikr/src/main/cpp/example.cpp'
  finalise_flags
  [[ "$native_cpp" == true && "$format_cpp" == true && "$gradle_quality" == true ]] ||
    fail 'Self-test: a native source change must select the C/C++ formatter.'

  reset_flags
  classify_path 'app/build.gradle'
  finalise_flags
  [[ "$full" == true && "$api29" == true && "$topway_twmedia" == true && "$topway_twmusic" == true && "$format_cpp" == false ]] ||
    fail 'Self-test: module build configuration changes must run full maintained Android CI without unrelated C/C++ provisioning.'

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
full_requested=false

if [[ "$event" == workflow_dispatch ]]; then
  log 'Full maintained Android validation explicitly requested by workflow dispatch.'
  mark_full
  emit_outputs 0 manual-full
  exit 0
fi

if [[ "$force_full" == true ]]; then
  # Keep classifying the actual PR diff so a real native change still selects the C/C++ lane.
  full_requested=true
  log 'Full maintained Android validation requested; retaining changed-file native classification.'
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
    warn "Unknown or missing event '$event'; running full maintained Android validation."
    mark_full
    emit_outputs 0 unknown-full
    exit 0
    ;;
esac

if [[ -z "$comparison" || -z "$head_sha" ]] ||
   ! git cat-file -e "${head_sha}^{commit}" 2>/dev/null ||
   ! git diff --name-only "$comparison" >/dev/null 2>&1; then
  warn "No trustworthy comparison range is available (${comparison:-none}); running full maintained Android validation."
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

if [[ "$full_requested" == true ]]; then
  mark_full
fi

# Every integration push to dev retains the API 29 Android 10 gate, even for a narrow change.
if [[ "$event" == push && "$ref_name" == dev ]]; then
  api29=true
  android_runtime_high_risk=true
  static_only=false
fi

finalise_flags
log "comparison=$comparison changed=$changed_count full=$full native_cpp=$native_cpp static_only=$static_only formatting=$formatting format_cpp=$format_cpp app_tests=$app_tests musikr_tests=$musikr_tests lint=$android_lint twmedia=$topway_twmedia twmusic=$topway_twmusic api29=$api29"
emit_outputs "$changed_count" "$comparison"
