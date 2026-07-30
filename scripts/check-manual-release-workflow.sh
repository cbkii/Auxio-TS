#!/usr/bin/env bash
# Fast PR-safe checks for the maintained Auxio APK and LSPosed addon release workflow.

set -euo pipefail

fail() { printf '::error::%s\n' "$*" >&2; exit 1; }
log() { printf '[INFO] %s\n' "$*" >&2; }

workflow='.github/workflows/manual-release.yml'
bridge_checker='scripts/check-lsposed-bridge-contracts.sh'

[[ -f "${workflow}" ]] || fail "Missing ${workflow}"
[[ -f "${bridge_checker}" ]] || fail "Missing ${bridge_checker}"

ruby -e 'require "yaml"; Psych.safe_load(File.read(ARGV.fetch(0)), permitted_classes: [], permitted_symbols: [], aliases: false); puts "OK #{ARGV.fetch(0)}"' "${workflow}"
bash -n "${bridge_checker}"

if command -v actionlint >/dev/null 2>&1; then actionlint "${workflow}"; else log 'actionlint unavailable; skipped'; fi
if command -v shellcheck >/dev/null 2>&1; then shellcheck "${bridge_checker}"; else log 'shellcheck unavailable; skipped'; fi

python3 - <<'PY'
from pathlib import Path
text = Path('.github/workflows/manual-release.yml').read_text(encoding='utf-8')
required = {
    'include_topway_twmedia_apk': 'default: true',
    'include_lsposed_bridge_apk': 'default: true',
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
for forbidden in (
    'include_standard_apk',
    'assembleStandardRelease',
    'standard-release.apk',
    'include_topway_twmusic_magisk',
    'topway_twmusic_magisk)',
    'package-topway-twmusic-magisk-module.sh',
):
    if forbidden in text:
        raise SystemExit(f'Retired release token remains: {forbidden}')
if 'At least one maintained release asset must be selected' not in text:
    raise SystemExit('Missing empty-selection guard')
if 'topway-twmusic-release.apk' not in text or 'Raw topwayTwMusic APK asset is forbidden' not in text:
    raise SystemExit('Missing forbidden raw topwayTwMusic APK guard')
for required_bridge in (
    ':lsposed-bridge:assembleRelease',
    'Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge.apk',
    'check-lsposed-bridge-contracts.sh',
    'signed-lsposed-api100-addon',
    'ORG_GRADLE_PROJECT_bridgeVersionName',
    'ORG_GRADLE_PROJECT_bridgeVersionCode',
):
    if required_bridge not in text:
        raise SystemExit(f'Missing LSPosed release contract: {required_bridge}')
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

log 'manual release workflow and LSPosed addon checks passed'
