#!/usr/bin/env bash

# Static and optional APK-level ABI contract for the maintained Auxio-TS lanes.

fail() {
  printf 'FAILED: %s\n' "$1" >&2
  exit 1
}

require_file() {
  [ -f "$1" ] || fail "required file is missing: $1"
}

require_count() {
  local expected=$1
  local pattern=$2
  local file=$3
  local label=$4
  local actual

  actual=$(grep -Ec "$pattern" "$file") || actual=0
  if [ "$actual" -ne "$expected" ]; then
    fail "$label: expected $expected matching line(s), found $actual"
  fi
}

APP_GRADLE='app/build.gradle'
MUSIKR_GRADLE='musikr/build.gradle'
TAGLIB_SCRIPT='musikr/src/main/cpp/build_taglib.sh'

require_file "$APP_GRADLE"
require_file "$MUSIKR_GRADLE"
require_file "$TAGLIB_SCRIPT"

require_count 1 '^[[:space:]]*abiFilters "arm64-v8a"[[:space:]]*$' \
  "$APP_GRADLE" 'primary release ABI contract'
require_count 2 '^[[:space:]]*abiFilters "arm64-v8a", "x86_64"[[:space:]]*$' \
  "$APP_GRADLE" 'debug/benchmark ABI contract'
require_count 1 '^[[:space:]]*abiFilters "arm64-v8a", "x86_64"[[:space:]]*$' \
  "$MUSIKR_GRADLE" 'Musikr native ABI contract'

if ! grep -Fq 'REQUESTED_ABIS=${3:-arm64-v8a,x86_64}' "$TAGLIB_SCRIPT"; then
  fail 'TagLib preparation default must be arm64-v8a,x86_64'
fi
if grep -Eq '^build_for_arch[[:space:]]+(x86|armeabi-v7a)[[:space:]]*$' "$TAGLIB_SCRIPT"; then
  fail 'legacy x86/armeabi-v7a must not be built unconditionally'
fi

APK_PATH=${1:-}
if [ -n "$APK_PATH" ]; then
  require_file "$APK_PATH"
  command -v unzip >/dev/null 2>&1 || fail 'unzip is required for APK ABI inspection'

  mapfile -t packaged_abis < <(
    unzip -Z1 "$APK_PATH" | awk -F/ '/^lib\/[^/]+\/[^/]+$/ {print $2}' | sort -u
  )
  if [ "${#packaged_abis[@]}" -ne 1 ] || [ "${packaged_abis[0]:-}" != 'arm64-v8a' ]; then
    printf 'Observed release APK ABIs:' >&2
    printf ' %s' "${packaged_abis[@]:-<none>}" >&2
    printf '\n' >&2
    fail 'primary topwayTwMedia release APK must package only arm64-v8a'
  fi

  if ! unzip -Z1 "$APK_PATH" | grep -Fxq 'lib/arm64-v8a/libtagJNI.so'; then
    fail 'primary release APK is missing lib/arm64-v8a/libtagJNI.so'
  fi

  printf 'Release APK ABI: arm64-v8a\n' >&2
  printf 'Release libtagJNI.so: present\n' >&2
fi

printf 'SUCCESS: native ABI contracts passed\n' >&2
