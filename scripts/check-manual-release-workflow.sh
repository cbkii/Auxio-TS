#!/usr/bin/env bash
# Fast PR-safe checks for the maintained Auxio APK and LSPosed addon release workflow.

set -euo pipefail

fail() { printf '::error::%s\n' "$*" >&2; exit 1; }
log() { printf '[INFO] %s\n' "$*" >&2; }

workflow='.github/workflows/manual-release.yml'
bridge_checker='scripts/check-lsposed-bridge-contracts.sh'
app_checker='scripts/check-app-release-contracts.sh'
signer_parser='scripts/lib/apksigner-certificate.sh'

[[ -f "${workflow}" ]] || fail "Missing ${workflow}"
[[ -f "${bridge_checker}" ]] || fail "Missing ${bridge_checker}"
[[ -f "${app_checker}" ]] || fail "Missing ${app_checker}"
[[ -f "${signer_parser}" ]] || fail "Missing ${signer_parser}"

ruby -e 'require "yaml"; Psych.safe_load(File.read(ARGV.fetch(0)), permitted_classes: [], permitted_symbols: [], aliases: false); puts "OK #{ARGV.fetch(0)}"' "${workflow}"
bash -n "${bridge_checker}" "${app_checker}" "${signer_parser}"

if command -v actionlint >/dev/null 2>&1; then actionlint "${workflow}"; else log 'actionlint unavailable; skipped'; fi
if command -v shellcheck >/dev/null 2>&1; then
    shellcheck "${bridge_checker}" "${app_checker}" "${signer_parser}"
else
    log 'shellcheck unavailable; skipped'
fi

# shellcheck source=scripts/lib/apksigner-certificate.sh
source "${signer_parser}"
expected='0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF'
legacy_report="Signer #1 certificate SHA-256 digest: ${expected}"
range_report='Signer (minSdkVersion=24, maxSdkVersion=32) certificate SHA-256 digest: 01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef'
[[ "$(extract_apksigner_certificate_sha256 "${legacy_report}")" == "${expected}" ]] ||
  fail 'Legacy apksigner signer output is not parsed correctly.'
[[ "$(extract_apksigner_certificate_sha256 "${range_report}")" == "${expected}" ]] ||
  fail 'SDK-range apksigner signer output is not parsed correctly.'
if extract_apksigner_certificate_sha256 "${legacy_report}"$'\n''Signer #2 certificate SHA-256 digest: F123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDE0' \
  >/dev/null 2>&1; then
  fail 'Multiple distinct signer digests must fail closed.'
fi
if extract_apksigner_certificate_sha256 'Signer #1 certificate SHA-256 digest: not-a-digest' \
  >/dev/null 2>&1; then
  fail 'Malformed signer output must fail closed.'
fi
log 'apksigner output parser self-tests passed'

python3 - <<'PY'
from pathlib import Path
text = Path('.github/workflows/manual-release.yml').read_text(encoding='utf-8')
required = {
    'include_topway_twmedia_apk': 'default: true',
    'include_lsposed_bridge_apk': 'default: true',
    'include_debug_apks': 'default: true',
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
    ':lsposed-bridge:assembleDebug',
    'Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge.apk',
    'Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge-debug.apk',
    'check-lsposed-bridge-contracts.sh',
    'signed-lsposed-api100-addon',
    'debug-lsposed-api100-addon',
    'ORG_GRADLE_PROJECT_bridgeVersionName',
    'ORG_GRADLE_PROJECT_bridgeVersionCode',
    'EXPECTED_SIGNER_SHA256="${expected_release_signer}"',
    'keytool}" -exportcert',
):
    if required_bridge not in text:
        raise SystemExit(f'Missing LSPosed release contract: {required_bridge}')
if 'gh release delete-asset' not in text or 'gh release upload' not in text:
    raise SystemExit('Missing release replacement/upload flow')
if text.find('gh release delete-asset') < text.find('Build, verify and stage selected release assets'):
    raise SystemExit('Release asset deletion appears before rebuilt assets are staged')
for artifact_contract in (
    'path: ${{ steps.assets.outputs.release_artifact_dir }}/*',
    'path: ${{ steps.assets.outputs.debug_artifact_dir }}/*',
    'ASSET_PATHS: ${{ steps.assets.outputs.release_asset_paths }}',
    'ASSET_NAMES: ${{ steps.assets.outputs.release_asset_names }}',
    'Debug APKs and sidecars are forbidden on new GitHub Releases.',
):
    if artifact_contract not in text:
        raise SystemExit(f'Missing release/debug publication boundary: {artifact_contract}')
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
for debug_contract in (
    ':app:assembleTopwayTwMediaDebug',
    'Auxio-TS-${RELEASE_TAG}-topway-twmedia-debug.apk',
    'debug-diagnostics-apk',
    'check-release-diagnostics-boundary.sh "${asset_path}"',
    'Auxio-TS-${{ steps.version.outputs.release_tag }}-debug-companions',
    'Debug APKs are short-lived workflow artifacts and never GitHub Release assets.',
):
    if debug_contract not in text:
        raise SystemExit(f'Missing separated debug/release contract: {debug_contract}')
for suffix in ('.sha256', '.metadata.txt'):
    if suffix not in text:
        raise SystemExit(f'Missing release evidence sidecar: {suffix}')
if 'apksigner certificates' not in text or 'asset_sha256=' not in text:
    raise SystemExit('Release metadata does not record signing and checksum evidence')
for app_contract in (
    'bash ./scripts/check-app-release-contracts.sh',
    '--version-name "${VERSION_NAME}"',
    '--version-code "${VERSION_CODE}"',
    '--expected-signer "${expected_release_signer}"',
    '--sha256-file "${release_artifact_dir}/${asset_name}.sha256"',
    '--metadata-file "${release_artifact_dir}/${asset_name}.metadata.txt"',
):
    if app_contract not in text:
        raise SystemExit(f'Missing primary release APK contract: {app_contract}')
if 'EXISTING_RELEASE: ${{ steps.version.outputs.existing_release }}' not in text:
    raise SystemExit('Existing-release state is not routed through the shell environment')
print('OK manual-release maintained asset invariants')
PY

for checker in "${app_checker}" "${bridge_checker}"; do
  grep -Fq 'extract_apksigner_certificate_sha256' "${checker}" ||
    fail "${checker} does not use the shared signer parser."
  grep -Fq 'verify --verbose --print-certs' "${checker}" ||
    fail "${checker} does not request apksigner certificate output."
done

log 'manual release workflow and LSPosed addon checks passed'
