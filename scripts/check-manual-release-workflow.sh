#!/usr/bin/env bash
# Fast PR-safe checks for the manual release workflow and Topway Magisk packager.

set -euo pipefail

fail() {
  printf '::error::%s\n' "$*" >&2
  exit 1
}

log() {
  printf '[INFO] %s\n' "$*" >&2
}

workflow='.github/workflows/manual-release.yml'
packager='scripts/package-topway-twmusic-magisk-module.sh'

[[ -f "${workflow}" ]] || fail "Missing ${workflow}"
[[ -f "${packager}" ]] || fail "Missing ${packager}"

ruby -e 'require "yaml"; Psych.safe_load(File.read(ARGV.fetch(0)), permitted_classes: [], permitted_symbols: [], aliases: false); puts "OK #{ARGV.fetch(0)}"' "${workflow}"
bash -n "${packager}"

if command -v actionlint >/dev/null 2>&1; then
  actionlint "${workflow}"
else
  log "actionlint is not installed; skipping actionlint ${workflow}"
fi

if command -v shellcheck >/dev/null 2>&1; then
  shellcheck "${packager}"
else
  log "shellcheck is not installed; skipping shellcheck ${packager}"
fi

python3 - <<'PY'
from pathlib import Path
text = Path('.github/workflows/manual-release.yml').read_text()
required = {
    'include_standard_apk': 'default: false',
    'include_topway_twmedia_apk': 'default: true',
    'include_topway_twmusic_magisk': 'default: false',
}
for key, default in required.items():
    marker = f'      {key}:\n'
    pos = text.find(marker)
    if pos < 0:
        raise SystemExit(f'Missing workflow_dispatch input: {key}')
    next_pos = min([candidate for candidate in (text.find('\n      include_', pos + len(marker)), text.find('\n      replace_existing_assets:', pos + len(marker)), text.find('\n\npermissions:', pos + len(marker))) if candidate != -1] or [len(text)])
    block = text[pos:next_pos]
    if default not in block:
        raise SystemExit(f'{key} does not contain expected {default}')
if 'At least one release asset must be selected' not in text:
    raise SystemExit('Missing empty-selection guard')
if 'topway-twmusic-release.apk' not in text or 'Raw topwayTwMusic APK asset is forbidden' not in text:
    raise SystemExit('Missing forbidden raw topwayTwMusic APK asset guard')
if 'gh release delete-asset' not in text or 'gh release upload' not in text:
    raise SystemExit('Missing release replacement/upload flow')
if text.find('gh release delete-asset') < text.find('Build, verify, and stage selected release assets'):
    raise SystemExit('Release asset deletion appears before rebuilt assets are staged')
if 'path: ${{ steps.assets.outputs.artifact_dir }}/*' not in text:
    raise SystemExit('Upload artifact step must use the selected-asset artifact directory, not all possible asset names')
print('OK manual-release selectable asset invariants')
PY

tmpdir="$(mktemp -d)"
cleanup() {
  rm -rf -- "${tmpdir}"
}
trap cleanup EXIT

printf 'fake apk payload\n' > "${tmpdir}/fake.apk"
(
  cd "${tmpdir}"
  "${OLDPWD}/${packager}" --apk fake.apk --output ./artifact.zip --version 1.2.3 --version-code 123 >/dev/null
)
zip_path="${tmpdir}/artifact.zip"
[[ -f "${zip_path}" ]] || fail "Packager did not create relative output path ${zip_path}"
unzip -t "${zip_path}" >/dev/null
unzip -l "${zip_path}" "module.prop" >/dev/null 2>&1 || fail "ZIP missing module.prop"
unzip -l "${zip_path}" "customize.sh" >/dev/null 2>&1 || fail "ZIP missing customize.sh"
unzip -l "${zip_path}" "system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk" >/dev/null 2>&1 || fail "ZIP missing Topway APK payload"

has_updater_script=false
has_unnecessary_script=false
while IFS= read -r entry; do
  [[ -n "${entry}" ]] || continue
  [[ "${entry}" != ./* ]] || fail "ZIP entry has ./ prefix: ${entry}"
  [[ "${entry}" != /* ]] || fail "ZIP entry is absolute: ${entry}"
  [[ "${entry}" != *'../'* && "${entry}" != ../* ]] || fail "ZIP entry escapes module root: ${entry}"
  [[ "${entry}" != *auxio-ts-magisk* ]] || fail "ZIP entry includes temporary parent prefix: ${entry}"
  [[ "${entry}" != 'META-INF/com/google/android/updater-script' ]] || has_updater_script=true
  if [[ "${entry}" =~ (^|/)install\.sh$|(^|/)post-fs-data\.sh$|(^|/)service\.sh$ ]]; then
    has_unnecessary_script=true
  fi
done < <(unzip -Z -1 "${zip_path}")

if [[ "${has_updater_script}" == "true" ]]; then
  updater_contents="$(unzip -p "${zip_path}" 'META-INF/com/google/android/updater-script')"
  [[ "${updater_contents}" == '#MAGISK' ]] || fail "updater-script must contain exactly #MAGISK"
fi

if [[ "${has_unnecessary_script}" == "true" ]]; then
  fail "Static overlay module ZIP contains unnecessary installer/boot scripts"
fi

customize_contents="$(unzip -p "${zip_path}" customize.sh)"
for forbidden in '/tmp' 'mktemp' 'mapfile' 'readarray' 'exit'; do
  if grep -F -- "${forbidden}" <<< "${customize_contents}" >/dev/null; then
    fail "Generated customize.sh contains forbidden device-side token: ${forbidden}"
  fi
done
if grep -E '(^|[^[:alnum:]_])([[:alnum:]_]+)=\(' <<< "${customize_contents}" >/dev/null; then
  fail "Generated customize.sh appears to contain a Bash array assignment"
fi

log "manual release workflow and Magisk packager checks passed"
