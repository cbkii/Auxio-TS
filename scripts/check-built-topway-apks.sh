#!/usr/bin/env bash
# Validate built Auxio-TS distribution variants through the canonical head-unit boundary.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || exit 1
cd -- "$repo_root"
fail() { printf '::error::%s\n' "$*" >&2; exit 1; }

bash ./scripts/check-headunit-compat-safety.sh
bash ./scripts/check-runtime-hardening-contracts.sh
bash ./scripts/check-topway-manifest-components.sh

sdk_root=${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}
apkanalyzer=${sdk_root:+${sdk_root}/cmdline-tools/latest/bin/apkanalyzer}
if [[ -z "$apkanalyzer" || ! -x "$apkanalyzer" ]]; then apkanalyzer=$(command -v apkanalyzer || true); fi
[[ -n "$apkanalyzer" && -x "$apkanalyzer" ]] || fail 'apkanalyzer was not found.'

validate_apk() {
  local directory=$1 expected_package=$2 label=$3 expect_topway=$4 apks=()
  [[ -d "$directory" ]] || fail "Missing ${label} output directory: ${directory}"
  mapfile -t apks < <(find "$directory" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' -print | sort)
  ((${#apks[@]} == 1)) || fail "Expected one ${label} APK in ${directory}, found ${#apks[@]}."
  local actual_package manifest manifest_dump alias_error
  actual_package=$("$apkanalyzer" manifest application-id "${apks[0]}")
  [[ "$actual_package" == "$expected_package" ]] || fail "${label} application id expected ${expected_package}, got ${actual_package}."
  manifest=$("$apkanalyzer" manifest print "${apks[0]}")
  if [[ "$expect_topway" == true ]]; then
    manifest_dump=$(mktemp)
    printf '%s\n' "$manifest" > "$manifest_dump"
    if ! alias_error=$(python3 ./scripts/check-manifest-alias-target.py "$manifest_dump" \
      com.tw.music.MusicActivity org.oxycblt.auxio.MainActivity 2>&1); then
      rm -f -- "$manifest_dump"
      fail "${label} alias target mismatch: ${alias_error}"
    fi
    rm -f -- "$manifest_dump"
  else
    if grep -Fq 'com.tw.music.MusicActivity' <<<"$manifest" || \
       grep -Fq 'com.tw.music.MusicService' <<<"$manifest" || \
       grep -Fq 'com.tw.music.view.MusicWidgetProvider' <<<"$manifest"; then
      fail "${label} leaked Topway-only components."
    fi
  fi
  printf 'Validated %s: %s (%s)\n' "$label" "${apks[0]}" "$actual_package"
}

if [[ ${BUILD_APP:-true} == true ]]; then
  validate_apk app/build/outputs/apk/standard/debug org.oxycblt.auxio.debug StandardDebug false
  validate_apk app/build/outputs/apk/topwayTwMedia/debug com.tw.media.debug TopwayTwMediaDebug true
  validate_apk app/build/outputs/apk/topwayTwMusic/debug com.tw.music.debug TopwayTwMusicDebug true
else
  fail 'Application variants were not selected for binary validation.'
fi

for spec in \
  'standard/release org.oxycblt.auxio StandardRelease false' \
  'topwayTwMedia/release com.tw.media TopwayTwMediaRelease true' \
  'topwayTwMusic/release com.tw.music TopwayTwMusicRelease true'; do
  read -r rel expected label topway <<<"$spec"
  directory="app/build/outputs/apk/${rel}"
  [[ -d "$directory" ]] || continue
  mapfile -t release_apks < <(find "$directory" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' -print | sort)
  if ((${#release_apks[@]})); then
    validate_apk "$directory" "$expected" "$label" "$topway"
    bash ./scripts/check-native-abi-contracts.sh "${release_apks[0]}"
  fi
done

printf 'Auxio-TS three-variant APK checks: PASS\n'
