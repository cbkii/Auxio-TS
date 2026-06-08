#!/usr/bin/env bash
# Jules environment setup for Auxio-TS.
#
# Purpose:
# - prepare the best possible Android/Gradle/submodule environment;
# - produce a reusable Jules snapshot whenever source review is possible;
# - never claim Gradle/APK readiness unless strict checks actually pass.
#
# This script is intentionally snapshot-friendly. It should not replace
# scripts/bootstrap-dependencies.sh or CI/release validation.

set -u
set -o pipefail

ROOT="$(git rev-parse --show-toplevel 2>/dev/null || pwd)"
cd "$ROOT"

mkdir -p .jules .jules/logs

STATUS_FILE=".jules/setup-status.env"
SUMMARY_FILE=".jules/setup-summary.md"
LOG_FILE=".jules/logs/setup-jules-env.log"

: > "$LOG_FILE"

log() {
  printf '[jules-setup] %s\n' "$*" | tee -a "$LOG_FILE"
}

run_step() {
  local name="$1"
  shift

  log "--- ${name} ---"
  {
    "$@"
  } >>"$LOG_FILE" 2>&1

  local rc=$?
  if [ "$rc" -eq 0 ]; then
    log "OK: ${name}"
  else
    log "WARN: ${name} failed with exit ${rc}"
  fi
  return "$rc"
}

write_status() {
  local readiness="$1"
  local sdk_ready="$2"
  local submodules_ready="$3"
  local gradle_ready="$4"
  local notes="$5"
  local escaped_notes
  printf -v escaped_notes '%q' "$notes"

  cat > "$STATUS_FILE" <<EOF
AUXIO_JULES_READINESS=${readiness}
AUXIO_JULES_SDK_READY=${sdk_ready}
AUXIO_JULES_SUBMODULES_READY=${submodules_ready}
AUXIO_JULES_GRADLE_READY=${gradle_ready}
AUXIO_JULES_NOTES=${escaped_notes}
EOF

  cat > "$SUMMARY_FILE" <<EOF
# Jules setup summary

- Readiness: \`${readiness}\`
- Android SDK ready: \`${sdk_ready}\`
- Submodules ready: \`${submodules_ready}\`
- Gradle smoke ready: \`${gradle_ready}\`
- Notes: ${notes}

Agents must read \`${STATUS_FILE}\` before claiming build/test/APK validation.

If readiness is \`STATIC_REVIEW_ONLY\`, agents may inspect and edit source/docs/scripts, but must not claim Gradle, APK, or runtime validation passed.

If readiness is \`FULL_BUILD_READY\`, agents may run Gradle tasks and report their actual results.
EOF
}

log "Repo: $ROOT"
log "System"
uname -a | tee -a "$LOG_FILE" || true
id | tee -a "$LOG_FILE" || true

log "Preinstalled tool summary"
if [ -f /opt/environment_summary.sh ]; then
  # Jules recommends this for checking installed tools.
  # shellcheck disable=SC1091
  set +x
  . /opt/environment_summary.sh >>"$LOG_FILE" 2>&1 || true
fi

for cmd in git bash curl unzip zip python3 java javac cmake ninja; do
  if command -v "$cmd" >/dev/null 2>&1; then
    log "FOUND: $cmd -> $(command -v "$cmd")"
  else
    log "MISSING: $cmd"
  fi
done

java -version >>"$LOG_FILE" 2>&1 || true
javac -version >>"$LOG_FILE" 2>&1 || true

# Keep Gradle caches inside the snapshot.
export GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
mkdir -p "$GRADLE_USER_HOME"

# Prefer any existing SDK. Otherwise create a user-local SDK.
if [ -z "${ANDROID_HOME:-}" ]; then
  if [ -d "$HOME/android-sdk" ]; then
    export ANDROID_HOME="$HOME/android-sdk"
  elif [ -d "$HOME/Android/Sdk" ]; then
    export ANDROID_HOME="$HOME/Android/Sdk"
  elif [ -d "/opt/android-sdk" ]; then
    export ANDROID_HOME="/opt/android-sdk"
  else
    export ANDROID_HOME="$HOME/android-sdk"
  fi
fi

export ANDROID_SDK_ROOT="$ANDROID_HOME"
export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/36.0.0:$PATH"

mkdir -p "$ANDROID_HOME"

log "ANDROID_HOME=$ANDROID_HOME"
log "ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT"
log "GRADLE_USER_HOME=$GRADLE_USER_HOME"

# Install Android command-line tools only if sdkmanager is missing.
install_cmdline_tools() {
  if command -v sdkmanager >/dev/null 2>&1; then
    return 0
  fi

  mkdir -p "$ANDROID_HOME/cmdline-tools"
  tmp_zip="$(mktemp -t android-cmdline-tools.XXXXXX.zip)"

  curl -fsSL \
    "https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip" \
    -o "$tmp_zip" || return 1

  rm -rf "$ANDROID_HOME/cmdline-tools/latest" "$ANDROID_HOME/cmdline-tools/cmdline-tools"
  unzip -q "$tmp_zip" -d "$ANDROID_HOME/cmdline-tools" || return 1
  mv "$ANDROID_HOME/cmdline-tools/cmdline-tools" "$ANDROID_HOME/cmdline-tools/latest" || return 1
  rm -f "$tmp_zip"

  export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/36.0.0:$PATH"
  command -v sdkmanager >/dev/null 2>&1
}

sdk_ready=false
submodules_ready=false
gradle_ready=false

if run_step "install Android command-line tools" install_cmdline_tools; then
  if command -v sdkmanager >/dev/null 2>&1; then
    run_step "accept Android SDK licences" bash -c 'yes | sdkmanager --licenses'

    # AGP 9.2 requires Gradle 9.4.1, Build Tools 36.0.0, and defaults to
    # NDK 28.2.13676358. This repo's wrapper already supplies Gradle.
    if run_step "install Android SDK packages" sdkmanager \
      "platform-tools" \
      "platforms;android-36" \
      "build-tools;36.0.0" \
      "cmake;3.22.1" \
      "ndk;28.2.13676358"; then
      sdk_ready=true
      printf 'sdk.dir=%s\n' "$ANDROID_HOME" > local.properties
      log "Wrote local.properties"
    fi
  fi
fi

chmod +x ./gradlew 2>/dev/null || true

# Always try to make static source review viable first.
run_step "bootstrap static-review dependencies" \
  bash scripts/bootstrap-dependencies.sh --profile static-review

# Then try strict full-build dependencies. Failure is recorded, not hidden.
if run_step "bootstrap full-build dependencies" \
  bash scripts/bootstrap-dependencies.sh --profile full-build; then
  if run_step "check full-build submodules" \
    bash scripts/check-submodules.sh --profile full-build; then
    submodules_ready=true
  fi
fi

# Cheap script/static checks should not block snapshotting.
run_step "shell syntax check" bash -c 'find scripts -type f -name "*.sh" -print -exec bash -n {} \;'
run_step "version catalogue sync" bash scripts/check-version-catalog-sync.sh
run_step "TS18 APK reference contracts" bash scripts/check-ts18-apk-reference-contracts.sh
run_step "DoFun/Topway compatibility guardrail" bash scripts/check-dofun-topway-compat.sh
run_step "head-unit safety guardrail" bash scripts/check-headunit-compat-safety.sh

# Warm Gradle only if SDK and submodules are ready. Do not run a long full build
# during snapshot setup.
if [ "$sdk_ready" = true ] && [ "$submodules_ready" = true ]; then
  if run_step "Gradle smoke tasks" ./gradlew --no-daemon --stacktrace tasks; then
    gradle_ready=true
  fi
else
  log "Skipping Gradle smoke: sdk_ready=$sdk_ready submodules_ready=$submodules_ready"
fi

if [ "$sdk_ready" = true ] && [ "$submodules_ready" = true ] && [ "$gradle_ready" = true ]; then
  write_status "FULL_BUILD_READY" "true" "true" "true" "Jules snapshot has SDK, submodules, and Gradle smoke readiness."
else
  write_status "STATIC_REVIEW_ONLY" "$sdk_ready" "$submodules_ready" "$gradle_ready" "Snapshot usable for source/static work; agents must run and report strict validation before claiming build/APK readiness."
fi

log "Setup status:"
cat "$STATUS_FILE" | tee -a "$LOG_FILE"
log "Summary written to $SUMMARY_FILE"
log "Detailed log written to $LOG_FILE"

# Critical: successful exit allows Jules to create a snapshot.
exit 0
