#!/usr/bin/env bash
# Package the internal topwayTwMusic release APK as the only supported exact-com.tw.music asset.
#
# This is a SYSTEMLESS Magisk overlay. It never writes, deletes, renames or disables the stock APK.
# Removing/disabling the module and rebooting exposes the untouched stock path again.
set -euo pipefail

usage() {
  cat <<'EOF'
Usage:
  scripts/package-topway-twmusic-magisk-module.sh \
    --apk PATH --output ZIP --version VERSION --version-code CODE

Required observed TS18 stock target:
  /system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk

The input APK must have applicationId com.tw.music. Packaging does NOT confer platform signing,
UID 1000/shared UID, signature permissions, or private Topway/vendor authority.
EOF
}

apk=''
output=''
version=''
version_code=''
while (($#)); do
  case "$1" in
    --apk) apk=${2:-}; shift 2 ;;
    --output) output=${2:-}; shift 2 ;;
    --version) version=${2:-}; shift 2 ;;
    --version-code) version_code=${2:-}; shift 2 ;;
    -h|--help) usage; exit 0 ;;
    *) printf 'ERROR: unknown argument: %s\n' "$1" >&2; usage >&2; exit 2 ;;
  esac
done

[[ -n "$apk" && -f "$apk" ]] || { printf 'ERROR: --apk must name an existing APK.\n' >&2; exit 2; }
[[ -n "$output" ]] || { printf 'ERROR: --output is required.\n' >&2; exit 2; }
[[ -n "$version" ]] || { printf 'ERROR: --version is required.\n' >&2; exit 2; }
[[ "$version_code" =~ ^[0-9]+$ ]] || { printf 'ERROR: --version-code must be numeric.\n' >&2; exit 2; }
command -v zip >/dev/null 2>&1 || { printf 'ERROR: zip is required.\n' >&2; exit 2; }
command -v unzip >/dev/null 2>&1 || { printf 'ERROR: unzip is required.\n' >&2; exit 2; }

stock_rel='system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk'
module_id='auxio_ts_topway_twmusic'
work=$(mktemp -d)
cleanup() { rm -rf -- "$work"; }
trap cleanup EXIT
stage="$work/module"
mkdir -p "$stage/$(dirname "$stock_rel")"
cp -- "$apk" "$stage/$stock_rel"

cat > "$stage/module.prop" <<EOF
id=${module_id}
name=Auxio-TS exact com.tw.music systemless overlay
version=${version}
versionCode=${version_code}
author=Auxio-TS
description=Systemless exact-package TS18 compatibility overlay. Requires exact-device validation; does not provide platform signing, UID 1000, signature permissions or private-vendor authority.
EOF

cat > "$stage/customize.sh" <<'EOF'
#!/system/bin/sh
# Magisk installer context. Fail closed if the exact observed stock target is absent.
STOCK_APK=/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk
if [ ! -f "$STOCK_APK" ]; then
  abort "STOP: expected TS18 stock com.tw.music target not found at $STOCK_APK; module not installed."
fi
ui_print "Auxio-TS: exact stock target observed; installing systemless overlay only."
ui_print "Auxio-TS: this does not grant platform signing, UID 1000, signature permissions, or vendor authority."
set_perm "$MODPATH/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk" 0 0 0644
EOF
chmod 0755 "$stage/customize.sh"

mkdir -p "$(dirname "$output")"
rm -f -- "$output"
(
  cd "$stage"
  zip -q -r "$OLDPWD/$output" .
)

mapfile -t entries < <(unzip -Z1 "$output" | sort)
required=(
  customize.sh
  module.prop
  "$stock_rel"
)
for entry in "${required[@]}"; do
  printf '%s\n' "${entries[@]}" | grep -Fxq -- "$entry" || {
    printf 'ERROR: packaged Magisk ZIP is missing %s\n' "$entry" >&2
    exit 1
  }
done

# Guard against accidental destructive/install-time mutation mechanisms.
if unzip -p "$output" customize.sh | grep -Eq '(^|[;&|[:space:]])(rm|mv|dd|mount|pm[[:space:]]+(disable|uninstall|clear)|magisk --remove-modules)([;&|[:space:]]|$)'; then
  printf 'ERROR: destructive command detected in customize.sh.\n' >&2
  exit 1
fi

printf 'SUCCESS: packaged systemless topwayTwMusic module: %s\n' "$output"
printf 'Target: /%s\n' "$stock_rel"
printf 'Requires exact TS18 install/boot/rollback validation before any runtime-success claim.\n'
