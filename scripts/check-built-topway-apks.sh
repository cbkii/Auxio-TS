#!/usr/bin/env bash
# Validate the selected built Topway APKs through the canonical head-unit safety boundary.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || {
  printf '::error::Cannot resolve repository root for built APK validation.\n' >&2
  exit 1
}
cd -- "$repo_root" || exit 1

fail() {
  printf '::error::%s\n' "$*" >&2
  exit 1
}

# Source/package safety remains owned by the canonical product-code guardrail. This script only adds
# binary-output assertions that cannot run until the selected APKs have been assembled.
bash ./scripts/check-headunit-compat-safety.sh
bash ./scripts/check-runtime-hardening-contracts.sh

sdk_root=${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}
apkanalyzer=${sdk_root:+${sdk_root}/cmdline-tools/latest/bin/apkanalyzer}
if [[ -z "$apkanalyzer" || ! -x "$apkanalyzer" ]]; then
  apkanalyzer=$(command -v apkanalyzer || true)
fi
[[ -n "$apkanalyzer" && -x "$apkanalyzer" ]] || fail 'apkanalyzer was not found.'

validate_apk() {
  local directory=$1 expected_package=$2 label=$3
  local apks=()
  mapfile -t apks < <(find "$directory" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' -print | sort)
  ((${#apks[@]} == 1)) || fail "Expected one ${label} APK in ${directory}, found ${#apks[@]}."

  local actual_package manifest manifest_dump alias_error
  actual_package=$("$apkanalyzer" manifest application-id "${apks[0]}")
  [[ "$actual_package" == "$expected_package" ]] ||
    fail "${label} application id expected ${expected_package}, got ${actual_package}."

  manifest=$("$apkanalyzer" manifest print "${apks[0]}")
  manifest_dump=$(mktemp)
  printf '%s\n' "$manifest" > "$manifest_dump"
  if ! alias_error=$(
    python3 ./scripts/check-manifest-alias-target.py \
      "$manifest_dump" \
      com.tw.music.MusicActivity \
      org.oxycblt.auxio.MainActivity 2>&1
  ); then
    rm -f -- "$manifest_dump"
    fail "${label} alias target mismatch: ${alias_error}"
  fi
  rm -f -- "$manifest_dump"
  grep -Fq 'android:name="org.oxycblt.auxio.car.overlay.TopwayMusicEntryActivity"' <<<"$manifest" &&
    fail "${label} still packages obsolete TopwayMusicEntryActivity."

  printf 'Validated %s: %s (%s)\n' "$label" "${apks[0]}" "$actual_package"
}

selected=0
if [[ ${BUILD_TWMEDIA:-false} == true ]]; then
  validate_apk app/build/outputs/apk/topwayTwMedia/debug com.tw.media.debug topwayTwMediaDebug
  selected=$((selected + 1))
fi
if [[ ${BUILD_TWMUSIC:-false} == true ]]; then
  validate_apk app/build/outputs/apk/topwayTwMusic/debug com.tw.music.debug topwayTwMusicDebug
  selected=$((selected + 1))
fi
((selected > 0)) || fail 'No maintained Topway APK was selected for binary validation.'

# Release validation is conditional because ordinary PRs intentionally build only maintained debug
# APKs. Whenever the primary release output exists, however, enforce the physical TS18 arm64-only
# packaging contract and the presence of libtagJNI.so.
release_apks=()
if [[ -d app/build/outputs/apk/topwayTwMedia/release ]]; then
  mapfile -t release_apks < <(
    find app/build/outputs/apk/topwayTwMedia/release -maxdepth 1 -type f -name '*.apk' -print | sort
  )
fi
if ((${#release_apks[@]} > 0)); then
  ((${#release_apks[@]} == 1)) ||
    fail "Expected one topwayTwMedia release APK for ABI validation, found ${#release_apks[@]}."
  bash ./scripts/check-native-abi-contracts.sh "${release_apks[0]}"
fi

printf 'Selected built Topway APK checks: PASS\n'
