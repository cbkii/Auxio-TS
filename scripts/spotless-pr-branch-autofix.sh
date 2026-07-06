#!/usr/bin/env bash

# Runs Spotless for the format-pr-branch workflow.
# It distinguishes real format violations from transient Eclipse P2/provisioning
# failures so the workflow does not run spotlessApply after an infrastructure failure.

WARNING_COUNT=0
LAST_LOG=''

log() { printf '[INFO] %s\n' "$*" >&2; }
warn() { WARNING_COUNT=$((WARNING_COUNT + 1)); printf '[WARN] %s\n' "$*" >&2; }
fail() { printf '::error::%s\n' "$*" >&2; exit 1; }

write_output() {
  local name=$1
  local value=$2

  if [[ -n ${GITHUB_OUTPUT:-} ]]; then
    printf '%s=%s\n' "$name" "$value" >> "$GITHUB_OUTPUT"
  else
    printf '[INFO] output %s=%s\n' "$name" "$value" >&2
  fi
}

RUN_ROOT="${RUNNER_TEMP:-${TMPDIR:-${HOME:-.}/tmp}}/auxio-ts-spotless-autofix.$$"
mkdir -p -- "$RUN_ROOT" || fail "Cannot create Spotless auto-fix log directory: $RUN_ROOT"

cleanup() {
  local rc=$?
  trap - EXIT INT TERM
  rm -rf -- "$RUN_ROOT" 2>/dev/null || :
  exit "$rc"
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

run_gradle_capture() {
  local name=$1
  shift

  LAST_LOG="${RUN_ROOT}/${name}.log"
  log "Running Gradle task for ${name}: $*"

  bash ./scripts/ci-gradle.sh "$@" 2>&1 | tee "$LAST_LOG"
  local rc=${PIPESTATUS[0]}

  if ((rc == 0)); then
    log "${name} completed successfully."
  else
    warn "${name} failed with status ${rc}. Log: ${LAST_LOG}"
  fi

  return "$rc"
}

is_p2_or_network_failure() {
  local file=$1

  grep -Eiq \
    'Failed to provision P2 dependencies|download\.eclipse\.org|archive\.eclipse\.org|p2\.index|500 Internal Server Error|502 Bad Gateway|503 Service Unavailable|504 Gateway Timeout|SocketTimeoutException|Read timed out|Connection reset|UnknownHostException|Could not resolve host|No route to host' \
    "$file"
}

is_formatting_failure() {
  local file=$1

  grep -Eiq \
    'The following files had format violations|Run .*spotlessApply|spotlessApply|format violations|Spotless found' \
    "$file"
}

max_check_attempts=${AUXIO_TS_SPOTLESS_CHECK_ATTEMPTS:-3}
check_attempt=1
needs_apply=0

while ((check_attempt <= max_check_attempts)); do
  if run_gradle_capture "spotlessCheck-${check_attempt}" spotlessCheck; then
    write_output changed false
    log "Formatting already clean; no changes needed."
    exit 0
  fi

  if is_p2_or_network_failure "$LAST_LOG"; then
    if ((check_attempt < max_check_attempts)); then
      warn "Spotless check hit a P2/network provisioning failure; retrying (${check_attempt}/${max_check_attempts})."
      sleep 15
      check_attempt=$((check_attempt + 1))
      continue
    fi

    fail "Spotless check failed because Eclipse P2 dependencies could not be provisioned after ${max_check_attempts} attempts. Refusing to run spotlessApply because this was not a proven formatting violation."
  fi

  if is_formatting_failure "$LAST_LOG"; then
    needs_apply=1
    break
  fi

  fail "spotlessCheck failed, but the failure was not classified as either a formatting violation or a transient P2/network issue. Refusing to auto-apply."
done

if ((needs_apply == 0)); then
  fail "Internal error: Spotless apply was not requested after failed check."
fi

max_apply_attempts=${AUXIO_TS_SPOTLESS_APPLY_ATTEMPTS:-2}
apply_attempt=1

while ((apply_attempt <= max_apply_attempts)); do
  if run_gradle_capture "spotlessApply-${apply_attempt}" spotlessApply; then
    break
  fi

  if is_p2_or_network_failure "$LAST_LOG" && ((apply_attempt < max_apply_attempts)); then
    warn "Spotless apply hit a P2/network provisioning failure; retrying (${apply_attempt}/${max_apply_attempts})."
    sleep 15
    apply_attempt=$((apply_attempt + 1))
    continue
  fi

  fail "spotlessApply failed. See log above for details."
done

run_gradle_capture "spotlessCheck-after-apply" spotlessCheck || {
  if is_p2_or_network_failure "$LAST_LOG"; then
    fail "Spotless verification after apply failed due to P2/network provisioning. Formatting changes may have been written, but verification did not complete."
  fi
  fail "Spotless verification after apply failed."
}

if git diff --quiet; then
  fail "spotlessApply completed after a formatting failure, but produced no git diff."
fi

write_output changed true
log "Spotless formatting changes are ready to commit."
