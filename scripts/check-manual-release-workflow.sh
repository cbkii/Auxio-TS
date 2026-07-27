#!/usr/bin/env bash
# Fast PR-safe checks for the maintained Topway release workflow and Magisk packager.

set -euo pipefail

fail() { printf '::error::%s\n' "$*" >&2; exit 1; }
log() { printf '[INFO] %s\n' "$*" >&2; }

workflow='.github/workflows/manual-release.yml'
packager='scripts/package-topway-twmusic-magisk-module.sh'

[[ -f "${workflow}" ]] || fail "Missing ${workflow}"
[[ -f "${packager}" ]] || fail "Missing ${packager}"

ruby -e 'require "yaml"; Psych.safe_load(File.read(ARGV.fetch(0)), permitted_classes: [], permitted_symbols: [], aliases: false); puts "OK #{ARGV.fetch(0)}"' "${workflow}"
bash -n "${packager}"

if command -v actionlint >/dev/null 2>&1; then actionlint "${workflow}"; else log 'actionlint unavailable; skipped'; fi
if command -v shellcheck >/dev/null 2>&1; then shellcheck "${packager}"; else log 'shellcheck unavailable; skipped'; fi

python3 - <<'PY'
from pathlib import Path
text = Path('.github/workflows/manual-release.yml').read_text(encoding='utf-8')
required = {
    'include_topway_twmedia_apk': 'default: true',
    'include_topway_twmusic_magisk': 'default: false',
}
for key, default in required.items():
    marker = f'      {key}:\n'
    pos = text.find(marker)
    if pos < 0:
        raise SystemExit(f'Missing workflow_dispatch input: {key}')
    next_candidates = [
        text.find('\n      include_', pos + len(marker)),
        text.find('\n      replace_existing_assets:', pos + len(marker)),
        text.find('\n\npermissions:', pos + len(marker)),
    ]
    next_pos = min([candidate for candidate in next_candidates if candidate != -1] or [len(text)])
    if default not in text[pos:next_pos]:
        raise SystemExit(f'{key} does not contain expected {default}')
for forbidden in ('include_standard_apk', 'assembleStandardRelease', 'standard-release.apk'):
    if forbidden in text:
        raise SystemExit(f'Retired standard release token remains: {forbidden}')
if 'At least one maintained release asset must be selected' not in text:
    raise SystemExit('Missing empty-selection guard')
if 'topway-twmusic-release.apk' not in text or 'Raw topwayTwMusic APK asset is forbidden' not in text:
    raise SystemExit('Missing forbidden raw topwayTwMusic APK guard')
if 'gh release delete-asset' not in text or 'gh release upload' not in text:
    raise SystemExit('Missing release replacement/upload flow')
if text.find('gh release delete-asset') < text.find('Build, verify and stage selected release assets'):
    raise SystemExit('Release asset deletion appears before rebuilt assets are staged')
if 'path: ${{ steps.assets.outputs.artifact_dir }}/*' not in text:
    raise SystemExit('Artifact upload must use only selected staged assets')
if 'persist-credentials: false' not in text:
    raise SystemExit('Checkout must not persist contents:write credentials')
for pinned in (
    'actions/setup-java@03ad4de0992f5dab5e18fcb136590ce7c4a0ac95',
    'gradle/actions/setup-gradle@0723195856401067f7a2779048b490ace7a47d7c',
):
    if pinned not in text:
        raise SystemExit(f'Missing immutable action pin: {pinned}')
if 'bash ./scripts/check-startup-performance-contracts.sh "${apk_path}"' not in text:
    raise SystemExit('Release APKs are not checked for compiled Baseline Profile data')
for suffix in ('.sha256', '.metadata.txt'):
    if suffix not in text:
        raise SystemExit(f'Missing release evidence sidecar: {suffix}')
if 'apksigner certificates' not in text or 'asset_sha256=' not in text:
    raise SystemExit('Release metadata does not record signing and checksum evidence')
print('OK manual-release maintained asset invariants')
PY

tmpdir="$(mktemp -d)"
cleanup() { rm -rf -- "${tmpdir}"; }
trap cleanup EXIT

printf 'fake apk payload\n' > "${tmpdir}/fake.apk"
(
  cd "${tmpdir}"
  "${OLDPWD}/${packager}" --apk fake.apk --output ./artifact.zip --version 1.2.3 --version-code 123 >/dev/null
)
zip_path="${tmpdir}/artifact.zip"
[[ -f "${zip_path}" ]] || fail "Packager did not create ${zip_path}"
unzip -t "${zip_path}" >/dev/null
for entry in module.prop customize.sh system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk; do
  unzip -l "${zip_path}" "${entry}" >/dev/null 2>&1 || fail "ZIP missing ${entry}"
done

has_updater_script=false
has_unnecessary_script=false
while IFS= read -r entry; do
  [[ -n "${entry}" ]] || continue
  [[ "${entry}" != ./* && "${entry}" != /* ]] || fail "Unsafe ZIP entry: ${entry}"
  [[ "${entry}" != *'../'* && "${entry}" != ../* ]] || fail "ZIP entry escapes module root: ${entry}"
  [[ "${entry}" != *auxio-ts-magisk* ]] || fail "ZIP entry includes temporary prefix: ${entry}"
  [[ "${entry}" != 'META-INF/com/google/android/updater-script' ]] || has_updater_script=true
  if [[ "${entry}" =~ (^|/)install\.sh$|(^|/)post-fs-data\.sh$|(^|/)service\.sh$ ]]; then has_unnecessary_script=true; fi
done < <(unzip -Z -1 "${zip_path}")

if [[ "${has_updater_script}" == true ]]; then
  [[ "$(unzip -p "${zip_path}" 'META-INF/com/google/android/updater-script')" == '#MAGISK' ]] ||
    fail 'updater-script must contain exactly #MAGISK'
fi
[[ "${has_unnecessary_script}" == false ]] || fail 'Static overlay ZIP contains unnecessary installer/boot scripts'

customize_contents="$(unzip -p "${zip_path}" customize.sh)"
for forbidden in /tmp mktemp mapfile readarray exit; do
  grep -F -- "${forbidden}" <<< "${customize_contents}" >/dev/null && fail "Generated customize.sh contains forbidden token: ${forbidden}"
done
if grep -E '(^|[^[:alnum:]_])([[:alnum:]_]+)=\(' <<< "${customize_contents}" >/dev/null; then
  fail 'Generated customize.sh appears to contain a Bash array assignment'
fi

log 'manual release workflow and Magisk packager checks passed'
