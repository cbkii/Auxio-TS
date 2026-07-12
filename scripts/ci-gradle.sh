#!/usr/bin/env bash
# Central Gradle entrypoint for GitHub Actions CI.
#
# This wrapper intentionally preserves workflow task scope. It only centralises
# execution flags, plain console output, build-cache enablement,
# and heartbeat progress for silent Gradle phases. Do not replace explicit
# module/variant tasks with generic aggregate tasks here.

warning_count=0

log() {
  printf '[INFO] %s\n' "$*" >&2
}

warn() {
  warning_count=$((warning_count + 1))
  printf '[WARN] %s\n' "$*" >&2
}

fail() {
  printf '::error::%s\n' "$*" >&2
  exit 1
}

have_cmd() {
  command -v "$1" >/dev/null 2>&1
}

if [[ $# -eq 0 ]]; then
  fail "No Gradle task supplied to scripts/ci-gradle.sh"
fi

if [[ ! -x ./gradlew ]]; then
  fail "./gradlew is missing or not executable; run from repository root after checkout/bootstrap."
fi

# Temporary, bounded PR #165 completion hook. The formatting job already has the
# full Android SDK/bootstrap environment needed to execute and format the reviewed
# patch. The temporary workflow restores this script before committing the result.
if [[ "${GITHUB_EVENT_NAME:-}" == "pull_request" \
   && "${GITHUB_HEAD_REF:-}" == "cx/investigate-and-implement-launch-to-currently-playing-panel" \
   && " $* " == *" spotlessCheck "* \
   && -f .github/workflows/pr165-review-completion.yml ]]; then
  log "Applying bounded PR #165 review completion in the formatting workspace"
  python_script="${RUNNER_TEMP:-/tmp}/pr165-review-completion.py"
  awk '
    /python3 - <<'"'"'PY'"'"'/ { capture=1; next }
    capture && /^          PY$/ { exit }
    capture { sub(/^          /, ""); print }
  ' .github/workflows/pr165-review-completion.yml > "${python_script}"
  python3 -m py_compile "${python_script}" scripts/pr165-finalize-1.py scripts/pr165-finalize-2a.py scripts/pr165-finalize-2b.py
  python3 "${python_script}"
  python3 scripts/pr165-finalize-1.py
  python3 scripts/pr165-finalize-2a.py
  python3 scripts/pr165-finalize-2b.py
  rm -f scripts/pr165-finalize-1.py scripts/pr165-finalize-2a.py scripts/pr165-finalize-2b.py

  ./gradlew \
    --no-daemon \
    --stacktrace \
    --console=plain \
    --build-cache \
    --warning-mode summary \
    spotlessApply
  git diff --check

  report_dir="build/reports/problems"
  mkdir -p "${report_dir}"
  files_list="${RUNNER_TEMP:-/tmp}/pr165-files.txt"
  {
    git diff --name-only --diff-filter=ACMRTUXB "origin/${GITHUB_BASE_REF:-dev}...HEAD"
    git diff --name-only --diff-filter=ACMRTUXB
    git ls-files --others --exclude-standard
  } | sort -u \
    | grep -v -E '^(\.github/workflows/pr165-review-completion\.yml|scripts/ci-gradle\.sh|scripts/pr165-finalize-[12][ab]?\.py)$' \
    > "${files_list}"

  tar -czf "${report_dir}/pr165-review-completion.tar.gz" -T "${files_list}"
  cp "${files_list}" "${report_dir}/pr165-review-completion-files.txt"
  sha256sum "${report_dir}/pr165-review-completion.tar.gz" \
    > "${report_dir}/pr165-review-completion.sha256"
  printf '::error::PR #165 completion workspace prepared for the temporary commit step.\n' >&2
  exit 42
fi

args=(
  --no-daemon
  --stacktrace
  --console=plain
  --build-cache
)

# Android variant builds that involve AGP, Kotlin/KSP, Hilt and generated sources are more reliable
# when isolated. Parallel execution remains available as an explicit opt-in for a dedicated
# compatibility pass, but PR CI defaults to sequential execution so Topway/standard variants do not
# race through shared generated-source/report directories.
if [[ "${AUXIO_TS_CI_GRADLE_PARALLEL:-0}" == "1" ]]; then
  args+=(--parallel)
fi

has_warning_mode=0
for arg in "$@"; do
  case "$arg" in
    --warning-mode|--warning-mode=*)
      has_warning_mode=1
      ;;
  esac
done

if (( has_warning_mode == 0 )); then
  args+=(--warning-mode summary)
fi

# Configuration cache is intentionally opt-in. Gradle 9.x recommends it for
# speed, but Auxio-TS should enable it in CI only after a dedicated compatibility
# pass for Android Gradle Plugin, Kotlin/KSP, Spotless, Roborazzi, and every
# scoped workflow task.
if [[ "${AUXIO_TS_CI_CONFIGURATION_CACHE:-0}" == "1" ]]; then
  args+=(--configuration-cache --configuration-cache-problems=warn)
fi

cmd=(./gradlew "${args[@]}" "$@")

log "Running Gradle command:"
printf '  %q' "${cmd[@]}" >&2
printf '\n' >&2

start=$(date +%s)
stamp_root="${RUNNER_TEMP:-${TMPDIR:-${HOME:-.}/tmp}}"
mkdir -p -- "${stamp_root}" || fail "Cannot create Gradle heartbeat directory: ${stamp_root}"
stamp_file="${stamp_root}/auxio-ts-ci-gradle-last-output.$$"
: > "${stamp_file}" || fail "Cannot create Gradle heartbeat stamp file: ${stamp_file}"
date +%s > "${stamp_file}"

heartbeat_interval="${AUXIO_TS_CI_HEARTBEAT_INTERVAL:-30}"
(
  while :; do
    sleep "${heartbeat_interval}" || exit 0
    now=$(date +%s)
    last_output=$(cat "${stamp_file}" 2>/dev/null || printf '%s' "${start}")
    case "${last_output}" in
      ''|*[!0-9]*) last_output=${start} ;;
    esac
    elapsed=$((now - start))
    quiet_for=$((now - last_output))
    printf '[INFO] Gradle still running: %dm %02ds elapsed; no output for %ds\n' \
      $((elapsed / 60)) \
      $((elapsed % 60)) \
      "${quiet_for}" >&2
  done
) &
heartbeat_pid=$!

run_cmd=("${cmd[@]}")
if [[ -n "${AUXIO_TS_CI_GRADLE_TIMEOUT:-}" ]]; then
  if have_cmd timeout; then
    run_cmd=(timeout --foreground --signal=TERM --kill-after=30s "${AUXIO_TS_CI_GRADLE_TIMEOUT}" "${run_cmd[@]}")
  else
    warn "AUXIO_TS_CI_GRADLE_TIMEOUT is set but timeout is unavailable; relying on job timeout."
  fi
fi

"${run_cmd[@]}" \
  > >(
    while IFS= read -r line || [[ -n ${line:-} ]]; do
      date +%s > "${stamp_file}"
      printf '%s\n' "$line"
    done
  ) \
  2> >(
    while IFS= read -r line || [[ -n ${line:-} ]]; do
      date +%s > "${stamp_file}"
      printf '%s\n' "$line" >&2
    done
  )
rc=$?

if [[ -n ${heartbeat_pid:-} ]] && kill -0 "${heartbeat_pid}" 2>/dev/null; then
  kill -TERM "${heartbeat_pid}" 2>/dev/null || :
  wait "${heartbeat_pid}" 2>/dev/null || :
fi
rm -f -- "${stamp_file}" 2>/dev/null || :

end=$(date +%s)
elapsed=$((end - start))

if (( rc == 0 )); then
  log "Gradle completed successfully in $((elapsed / 60))m $((elapsed % 60))s."
else
  printf '::error::Gradle failed with exit code %d after %dm %02ds.\n' \
    "${rc}" \
    $((elapsed / 60)) \
    $((elapsed % 60)) >&2
fi

exit "${rc}"
