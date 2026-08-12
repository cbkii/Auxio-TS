#!/usr/bin/env bash
# Validate the maintained Auxio-TS APK through the canonical head-unit boundary.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || exit 1
cd -- "$repo_root"
fail() { printf '::error::%s\n' "$*" >&2; exit 1; }

bash ./scripts/check-headunit-compat-safety.sh
bash ./scripts/check-runtime-hardening-contracts.sh

sdk_root=${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}
apkanalyzer=${sdk_root:+${sdk_root}/cmdline-tools/latest/bin/apkanalyzer}
if [[ -z "$apkanalyzer" || ! -x "$apkanalyzer" ]]; then apkanalyzer=$(command -v apkanalyzer || true); fi
[[ -n "$apkanalyzer" && -x "$apkanalyzer" ]] || fail 'apkanalyzer was not found.'

validate_apk() {
  local directory=$1 expected_package=$2 label=$3 apks=()
  mapfile -t apks < <(find "$directory" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' -print | sort)
  ((${#apks[@]} == 1)) || fail "Expected one ${label} APK in ${directory}, found ${#apks[@]}."
  local actual_package manifest manifest_dump alias_error
  actual_package=$("$apkanalyzer" manifest application-id "${apks[0]}")
  [[ "$actual_package" == "$expected_package" ]] || fail "${label} application id expected ${expected_package}, got ${actual_package}."
  manifest=$("$apkanalyzer" manifest print "${apks[0]}")
  manifest_dump=$(mktemp)
  printf '%s\n' "$manifest" > "$manifest_dump"
  if ! alias_error=$(python3 ./scripts/check-manifest-alias-target.py "$manifest_dump" \
    com.tw.music.MusicActivity org.oxycblt.auxio.MainActivity 2>&1); then
    rm -f -- "$manifest_dump"
    fail "${label} alias target mismatch: ${alias_error}"
  fi
  rm -f -- "$manifest_dump"
  printf 'Validated %s: %s (%s)\n' "$label" "${apks[0]}" "$actual_package"
}

if [[ ${BUILD_APP:-true} == true ]]; then
  validate_apk app/build/outputs/apk/debug com.tw.media.debug Debug
else
  fail 'The maintained app was not selected for binary validation.'
fi

release_apks=()
if [[ -d app/build/outputs/apk/release ]]; then
  mapfile -t release_apks < <(find app/build/outputs/apk/release -maxdepth 1 -type f -name '*.apk' -print | sort)
fi
if ((${#release_apks[@]})); then
  ((${#release_apks[@]} == 1)) || fail "Expected one release APK, found ${#release_apks[@]}."
  bash ./scripts/check-native-abi-contracts.sh "${release_apks[0]}"
fi

printf 'Maintained Auxio-TS APK checks: PASS\n'
