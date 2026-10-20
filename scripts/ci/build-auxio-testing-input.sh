#!/usr/bin/env bash
# Trusted CI helper: produce an immutable, unprivileged TESTING handoff.
set -euo pipefail
source_dir="$(cd "$1" && pwd -P)"
mkdir -p "$2"
destination="$(cd "$2" && pwd -P)"
cd "$source_dir"
[[ -f app/build.gradle && -x scripts/ci-gradle.sh ]] || { echo "::error::Maintained Auxio source missing."; exit 1; }
grep -Fq 'System.getenv("AUXIO_TS_TESTING_APK")' app/build.gradle || {
  echo "::error::Source predates TESTING version override; safely updateable APK unavailable."
  exit 1
}
: "${TESTING_SOURCE_SHA:?Immutable source SHA required}"
[[ "$(git rev-parse HEAD)" == "$TESTING_SOURCE_SHA" ]] || { echo "::error::Checkout SHA mismatch."; exit 1; }
: "${RUNNER_TEMP:?}"
: "${ANDROID_SDK_ROOT:?}"
umask 077
keystore="$RUNNER_TEMP/auxio-testing-build-only.p12"
storepass=auxio-build-only-discarded
keyalias=auxio-testing-build
timeout 40s keytool -genkeypair -noprompt -keyalg RSA -keysize 2048 -validity 2 \
  -alias "$keyalias" -storetype PKCS12 -keystore "$keystore" \
  -storepass "$storepass" -keypass "$storepass" \
  -dname 'CN=Temporary TESTING handoff,O=Auxio-TS,C=AU' >/dev/null
timeout 30m bash scripts/bootstrap-dependencies.sh --profile release
export AUXIO_TS_TESTING_APK=1
export GITHUB_SHA="$TESTING_SOURCE_SHA"
export ORG_GRADLE_PROJECT_releaseStoreFile="$keystore"
export ORG_GRADLE_PROJECT_releaseStorePassword="$storepass"
export ORG_GRADLE_PROJECT_releaseKeyAlias="$keyalias"
export ORG_GRADLE_PROJECT_releaseKeyPassword="$storepass"
bash scripts/ci-gradle.sh :app:testDebugUnitTest :app:assembleDebug
bash scripts/ci-gradle.sh :app:assembleRelease
release="app/build/outputs/apk/release/app-release.apk"
debug="app/build/outputs/apk/debug/app-debug.apk"
test -s "$release"
test -s "$debug"
tools="$ANDROID_SDK_ROOT/build-tools/36.0.0"
"$tools/apksigner" verify "$release"
"$tools/apksigner" verify "$debug"
actual="$("$tools/aapt" dump badging "$release" | head -n1)"
grep -Fq "name='com.tw.media'" <<< "$actual"
grep -Fq "versionName='0.0.0-TS18-TESTING'" <<< "$actual"
grep -Fq "versionCode='2100000000'" <<< "$actual"
abis="$(unzip -Z1 "$release" | sed -n -E 's#^lib/([^/]+)/.*#\1#p' | sort -u | paste -sd, -)"
[[ "$abis" == arm64-v8a ]] || { echo "::error::Invalid ABI set: $abis"; exit 1; }
cp "$release" "$destination/auxio-release.apk"
cp "$debug" "$destination/auxio-debug.apk"
field() {
  local key="$1" value="${2:-}"
  value="${value//$'\n'/ }"
  value="${value//$'\r'/ }"
  printf '%s=%s\n' "$key" "$value"
}
{
  field repository "${GITHUB_REPOSITORY:-}"
  field source_kind "${TESTING_SOURCE_KIND:-manual}"
  field requested_source "${TESTING_REQUESTED_SOURCE:-}"
  field source_sha "$TESTING_SOURCE_SHA"
  field built_sha "$(git rev-parse HEAD)"
  field source_ref "${TESTING_SOURCE_REF:-}"
  field pr_number "${TESTING_PR_NUMBER:-}"
  field pr_head_sha "${TESTING_PR_HEAD_SHA:-}"
  field pr_base_ref "${TESTING_PR_BASE_REF:-}"
  field pr_base_sha "${TESTING_PR_BASE_SHA:-}"
  field trigger_type "${TESTING_TRIGGER_TYPE:-manual}"
  field trigger_actor "${TESTING_TRIGGER_ACTOR:-${GITHUB_ACTOR:-}}"
  field testing_package com.tw.media
  field testing_version_name 0.0.0-TS18-TESTING
  field testing_version_code 2100000000
  field testing_abi arm64-v8a
  field build_run_id "${GITHUB_RUN_ID:-}"
  field build_run_number "${GITHUB_RUN_NUMBER:-}"
  field build_run_url "${TESTING_BUILD_RUN_URL:-}"
} > "$destination/BUILD_INFO.txt"
