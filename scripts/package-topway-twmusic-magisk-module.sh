#!/usr/bin/env bash
# Package the signed topwayTwMusic APK as a systemless Magisk module.

set -euo pipefail

fail() {
  printf '::error::%s\n' "$*" >&2
  exit 1
}

usage() {
  cat >&2 <<'EOF'
Usage: scripts/package-topway-twmusic-magisk-module.sh --apk APK --output ZIP [--version VERSION] [--version-code CODE]

Creates a Magisk module ZIP that overlays the signed topwayTwMusic APK at:
  /system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk

The script never writes to /system, never removes/disables stock apps, and only
stages files inside the module's system/ tree.
EOF
}

apk_path=""
output_path=""
version="0.0.0"
version_code="1"

while [[ $# -gt 0 ]]; do
  case "$1" in
    --apk)
      [[ $# -ge 2 ]] || fail "Missing value for --apk"
      apk_path="$2"
      shift 2
      ;;
    --output)
      [[ $# -ge 2 ]] || fail "Missing value for --output"
      output_path="$2"
      shift 2
      ;;
    --version)
      [[ $# -ge 2 ]] || fail "Missing value for --version"
      version="$2"
      shift 2
      ;;
    --version-code)
      [[ $# -ge 2 ]] || fail "Missing value for --version-code"
      version_code="$2"
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      fail "Unknown argument: $1"
      ;;
  esac
done

[[ -n "${apk_path}" ]] || fail "--apk is required"
[[ -n "${output_path}" ]] || fail "--output is required"
[[ -f "${apk_path}" ]] || fail "APK does not exist: ${apk_path}"
[[ "${apk_path}" == *.apk ]] || fail "Input payload must be an APK: ${apk_path}"
[[ "${output_path}" == *.zip ]] || fail "Output must be a .zip file: ${output_path}"
mkdir -p -- "$(dirname -- "${output_path}")"
output_dir="$(cd "$(dirname -- "${output_path}")" && pwd)"
output_path="${output_dir}/$(basename -- "${output_path}")"
command -v zip >/dev/null 2>&1 || fail "zip is required"
command -v unzip >/dev/null 2>&1 || fail "unzip is required"

case "${version_code}" in
  ''|*[!0-9]*) fail "--version-code must be an integer" ;;
esac

module_root="$(mktemp -d "${TMPDIR:-/tmp}/auxio-ts-magisk.XXXXXX")"
cleanup() {
  rm -rf -- "${module_root}"
}
trap cleanup EXIT

payload_dir="${module_root}/system/priv-app/com.tw.music_a41e"
mkdir -p -- "${payload_dir}"
cp -- "${apk_path}" "${payload_dir}/com.tw.music_a41e.apk"

cat > "${module_root}/module.prop" <<EOF
id=auxio_ts_topway_twmusic
name=Auxio-TS Topway Music Replacement
version=${version}
versionCode=${version_code}
author=Auxio-TS
description=Systemless TS18-only replacement overlay for stock Topway com.tw.music at /system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk. Does not grant platform signing, UID1000, or signature permissions.
EOF

cat > "${module_root}/customize.sh" <<'EOF'
#!/system/bin/sh
SKIPMOUNT=false
PROPFILE=true
POSTFSDATA=false
LATESTARTSERVICE=false

ui_print "Installing Auxio-TS Topway Music Replacement"
ui_print "Target overlay: /system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk"
ui_print "Systemless module only: no direct /system writes, package deletion, disabling, or data clearing."
ui_print "Does not grant platform signing, UID1000, or signature permissions. TS18 validation is required."
set_perm_recursive "$MODPATH/system/priv-app/com.tw.music_a41e" 0 0 0755 0644
EOF
chmod 0755 "${module_root}/customize.sh"

find "${module_root}" -type d -exec chmod 0755 {} +
find "${module_root}/system" -type f -exec chmod 0644 {} +

rm -f -- "${output_path}"
(
  cd "${module_root}"
  zip -q -r "${output_path}" module.prop customize.sh system
)

unzip -t "${output_path}" >/dev/null
unzip -l "${output_path}" "module.prop" >/dev/null 2>&1 || fail "Missing module.prop in ZIP"
unzip -l "${output_path}" "customize.sh" >/dev/null 2>&1 || fail "Missing customize.sh in ZIP"
unzip -l "${output_path}" "system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk" >/dev/null 2>&1 || fail "Missing Topway APK payload in ZIP"

printf 'Created Magisk module ZIP: %s\n' "${output_path}"
