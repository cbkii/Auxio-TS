#!/usr/bin/env bash
# Fast PR-safe checks for the maintained Auxio APK and LSPosed addon release workflow.

set -euo pipefail

fail() { printf '::error::%s\n' "$*" >&2; exit 1; }
log() { printf '[INFO] %s\n' "$*" >&2; }

workflow='.github/workflows/manual-release.yml'
bridge_checker='scripts/check-lsposed-bridge-contracts.sh'
app_checker='scripts/check-app-release-contracts.sh'
signer_parser='scripts/lib/apksigner-certificate.sh'
orchestrator='scripts/release-orchestrator.py'
ruleset_checker='scripts/check-release-ruleset-authority.py'
variant_checker='scripts/check-ci-variant-contracts.sh'

for required in \
  "${workflow}" \
  "${bridge_checker}" \
  "${app_checker}" \
  "${signer_parser}" \
  "${orchestrator}" \
  "${ruleset_checker}" \
  "${variant_checker}"; do
  [[ -f "${required}" ]] || fail "Missing ${required}"
done

ruby -e 'require "yaml"; Psych.safe_load(File.read(ARGV.fetch(0)), permitted_classes: [], permitted_symbols: [], aliases: false); puts "OK #{ARGV.fetch(0)}"' "${workflow}"
for script in "$0" "${bridge_checker}" "${app_checker}" "${signer_parser}" "${variant_checker}"; do
  bash -n "${script}" || fail "Shell syntax check failed for ${script}"
done
python3 -m py_compile "${orchestrator}" "${ruleset_checker}"
python3 "${orchestrator}" self-test
python3 "${ruleset_checker}" --self-test

if command -v actionlint >/dev/null 2>&1; then
  actionlint "${workflow}"
else
  log 'actionlint unavailable; skipped'
fi
if command -v shellcheck >/dev/null 2>&1; then
  shellcheck "$0" "${bridge_checker}" "${app_checker}" "${signer_parser}" "${variant_checker}"
else
  log 'shellcheck unavailable; skipped'
fi

# shellcheck source=scripts/lib/apksigner-certificate.sh
source "${signer_parser}"
expected='0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF'
legacy_report="Signer #1 certificate SHA-256 digest: ${expected}"
range_report='Signer (minSdkVersion=24, maxSdkVersion=32) certificate SHA-256 digest: 01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef'
nested_range_report='Signer (minSdkVersion=35 (dev release=true), maxSdkVersion=2147483647) certificate SHA-256 digest: 01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef'
scheme_report='V2 Signer: certificate SHA-256 digest: 01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef:01:23:45:67:89:ab:cd:ef'
decimal_scheme_report='V3.1 Signer: certificate SHA-256 digest: 01-23-45-67-89-ab-cd-ef-01-23-45-67-89-ab-cd-ef-01-23-45-67-89-ab-cd-ef-01-23-45-67-89-ab-cd-ef'
source_stamp_report="Source Stamp Signer certificate SHA-256 digest: ${expected}"

for report in \
  "${legacy_report}" \
  "${range_report}" \
  "${nested_range_report}" \
  "${scheme_report}" \
  "${decimal_scheme_report}"; do
  [[ "$(extract_apksigner_certificate_sha256 "${report}")" == "${expected}" ]] ||
    fail 'A supported apksigner signer label was not parsed correctly.'
done
[[ "$(extract_apksigner_certificate_sha256 "${legacy_report}"$'\n'"${scheme_report}"$'\n'"${source_stamp_report}")" == "${expected}" ]] ||
  fail 'Duplicate signer records must resolve to one fingerprint while ignoring source stamps.'
if extract_apksigner_certificate_sha256 "${legacy_report}"$'\n''V2 Signer: certificate SHA-256 digest: F123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDE0' >/dev/null 2>&1; then
  fail 'Multiple distinct signer digests must fail closed.'
fi
for malformed in \
  'Signer #1 certificate SHA-256 digest: not-a-digest' \
  'V2 Signer: certificate SHA-256 digest: not-a-digest' \
  'Signer (minSdkVersion=35 (dev release=true), maxSdkVersion=2147483647) certificate SHA-256 digest: not-a-digest' \
  "Signer (minSdkVersion=35 (dev release=true), maxSdkVersion=2147483647)) certificate SHA-256 digest: ${expected}" \
  "V2 Signer certificate SHA-256 digest: ${expected}" \
  "V0 Signer: certificate SHA-256 digest: ${expected}" \
  "${source_stamp_report}"; do
  if extract_apksigner_certificate_sha256 "${malformed}" >/dev/null 2>&1; then
    fail "Malformed or non-signer certificate output was accepted: ${malformed}"
  fi
done
grep -Fq 'summarise_apksigner_certificate_records' "${signer_parser}" ||
  fail 'Signer parser failure diagnostics are missing.'
log 'apksigner output parser self-tests passed'

python3 - <<'PY'
from pathlib import Path
import re

workflow = Path('.github/workflows/manual-release.yml').read_text(encoding='utf-8')
variant = Path('scripts/check-ci-variant-contracts.sh').read_text(encoding='utf-8')
rulesets = Path('scripts/check-release-ruleset-authority.py').read_text(encoding='utf-8')


def input_block(key: str) -> str:
    marker = f'      {key}:\n'
    start = workflow.find(marker)
    if start < 0:
        raise SystemExit(f'Missing workflow_dispatch input: {key}')
    content_start = start + len(marker)
    next_match = re.search(
        r'^      [A-Za-z0-9_]+:\n', workflow[content_start:], flags=re.MULTILINE
    )
    next_input = content_start + next_match.start() if next_match else -1
    permissions = workflow.find('\n\npermissions:', content_start)
    ends = [value for value in (next_input, permissions) if value >= 0]
    return workflow[start:min(ends) if ends else len(workflow)]


def step_block(name: str) -> str:
    marker = f'      - name: {name}\n'
    start = workflow.find(marker)
    if start < 0:
        raise SystemExit(f'Missing workflow step: {name}')
    end = workflow.find('\n      - name:', start + len(marker))
    return workflow[start:end if end >= 0 else len(workflow)]


required_inputs = {
    'release_mode': ('required: true', 'default: create_new_release', 'type: choice'),
    'include_topway_twmedia_apk': ('required: true', 'default: true', 'type: boolean'),
    'include_lsposed_bridge_apk': ('required: true', 'default: true', 'type: boolean'),
    'debug_variant_destination': (
        'required: true',
        'default: workflow_artifacts',
        'type: choice',
    ),
}
for key, tokens in required_inputs.items():
    block = input_block(key)
    for token in tokens:
        if token not in block:
            raise SystemExit(f'{key} does not contain expected {token}')

release_modes = re.findall(
    r'^          - ([a-z_]+)$', input_block('release_mode'), flags=re.MULTILINE
)
if release_modes != ['create_new_release', 'repair_existing_release']:
    raise SystemExit('release_mode options changed unexpectedly')
debug_destinations = re.findall(
    r'^          - ([a-z_]+)$',
    input_block('debug_variant_destination'),
    flags=re.MULTILINE,
)
if debug_destinations != ['workflow_artifacts', 'release_assets']:
    raise SystemExit('debug_variant_destination options changed unexpectedly')

for forbidden in (
    'include_standard_apk',
    'assembleStandardRelease',
    'standard-release.apk',
    'include_topway_twmusic_magisk',
    'package-topway-twmusic-magisk-module.sh',
    'include_debug_apks',
    'Cleanup release tag after failed release creation',
    'git push origin ":refs/tags/',
    '--skip-live-push-rulesets',
    '--classic-protection-json',
):
    if forbidden in workflow:
        raise SystemExit(f'Retired or unsafe release token remains: {forbidden}')

for token in (
    'group: manual-release',
    'if: github.actor == github.repository_owner && github.triggering_actor == github.repository_owner',
    'Verify branch and protected-ref authority',
    'RELEASE_PUSH_TOKEN: ${{ secrets.RELEASE_PUSH_TOKEN }}',
    'gh api user > "${actor_file}"',
    'push_actor_id=',
    "gh api \"repos/${GITHUB_REPOSITORY}\"",
    'repository_owner_id=',
    'RELEASE_PUSH_TOKEN authentication failed or timed out',
    'Unable to verify RELEASE_PUSH_TOKEN access',
    'Verify protected-ref ruleset bypass',
    'scripts/check-release-ruleset-authority.py',
    'rulesets?per_page=100&includes_parents=true&targets=branch%2Ctag',
    'No active branch/tag rulesets were returned',
    'Protected-ref push actor:',
    'Applicable ruleset bypass verified:',
    'gh api --paginate "repos/${GITHUB_REPOSITORY}/releases?per_page=100"',
    "--jq '.[].tag_name'",
    'Push immutable release tag',
    'Ensure draft release transaction exists',
    'Upload or replace planned release assets',
    'Verify remote release asset manifest',
    'Apply requested status to newly created release',
    'Synchronise released source metadata to dev',
    'git push origin "${RELEASE_SHA}:refs/heads/dev"',
    'git merge-base --is-ancestor',
    'Each required variant was built at most once.',
    'Debug APKs are forbidden unless debug_variant_destination=release_assets.',
    'At least one maintained release asset must be selected.',
    'Unsupported debug variant destination',
    ':app:assembleTopwayTwMediaRelease',
    ':app:assembleTopwayTwMediaDebug',
    ':lsposed-bridge:assembleRelease',
    ':lsposed-bridge:assembleDebug',
    'check-release-diagnostics-boundary.sh',
    'check-startup-performance-contracts.sh',
    'check-app-release-contracts.sh',
    'check-lsposed-bridge-contracts.sh',
    'Auxio-TS-${RELEASE_TAG}-topway-twmedia-release.apk',
    'Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge.apk',
    'Auxio-TS-${RELEASE_TAG}-topway-twmedia-debug.apk',
    'Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge-debug.apk',
    'workflow_artifacts|release_assets',
    'persist-credentials: false',
):
    if token not in workflow:
        raise SystemExit(f'Missing release transaction contract: {token}')

identity = step_block('Verify branch and protected-ref authority')
if identity.count('timeout --foreground 30s') != 2:
    raise SystemExit('Identity preflight must bound both GitHub API requests.')
for token in ('actor_error=', 'permission_error=', 'push_actor_id='):
    if token not in identity:
        raise SystemExit(f'Identity preflight lacks guarded handling: {token}')

ruleset_step = step_block('Verify protected-ref ruleset bypass')
if ruleset_step.count('timeout --foreground 30s') != 2:
    raise SystemExit('Ruleset preflight must bound list and detail API calls.')
for token in (
    'ruleset_error=',
    'check-release-ruleset-authority.py',
    '--actor-id "${PUSH_ACTOR_ID}"',
    '--release-tag "${RELEASE_TAG}"',
):
    if token not in ruleset_step:
        raise SystemExit(f'Ruleset preflight lacks required guard: {token}')

for token in (
    '_github_pathname_match',
    'File::FNM_PATHNAME contract',
    'targets=push',
    '_fetch_live_push_rulesets',
    '_verify_push_rulesets',
    'timeout=30',
    '_fetch_live_classic_protection',
    'required_signatures',
):
    if token not in rulesets:
        raise SystemExit(f'Ruleset checker lacks required contract: {token}')

protected_token = 'GH_TOKEN: ${{ secrets.RELEASE_PUSH_TOKEN }}'
for name in ('Push immutable release tag', 'Synchronise released source metadata to dev'):
    if protected_token not in step_block(name):
        raise SystemExit(f'{name} does not use the protected-ref owner token.')
if workflow.count(protected_token) != 2:
    raise SystemExit('Owner token must be scoped to exactly the two Git push steps.')

release_api_token = 'GH_TOKEN: ${{ github.token }}'
for name in (
    'Resolve version and repository release state',
    'Ensure draft release transaction exists',
    'Upload or replace planned release assets',
    'Verify remote release asset manifest',
    'Apply requested status to newly created release',
):
    if release_api_token not in step_block(name):
        raise SystemExit(f'{name} must retain the workflow-scoped GitHub token.')

for guard in (
    "${GITHUB_WORKFLOW:-}",
    'git fetch --quiet origin dev',
    'remote dev moved during release preparation; refusing stale tag publication',
):
    if guard not in variant:
        raise SystemExit(f'Missing pre-tag dev authority guard: {guard}')

order = [
    'Verify branch and protected-ref authority',
    'Verify protected-ref ruleset bypass',
    'Build once, inspect once and stage selected assets',
    'Upload release recovery workflow artifact',
    'Push immutable release tag',
    'Ensure draft release transaction exists',
    'Upload or replace planned release assets',
    'Verify remote release asset manifest',
    'Apply requested status to newly created release',
    'Synchronise released source metadata to dev',
]
positions = [workflow.find(name) for name in order]
if any(position < 0 for position in positions) or positions != sorted(positions):
    raise SystemExit(f'Unsafe release ordering: {list(zip(order, positions))}')

if workflow.find('git push origin "${RELEASE_SHA}:refs/heads/dev"') < workflow.find(
    'Verify remote release asset manifest'
):
    raise SystemExit('dev metadata push occurs before remote release verification')

for pin in (
    'actions/checkout@df4cb1c069e1874edd31b4311f1884172cec0e10',
    'actions/setup-java@03ad4de0992f5dab5e18fcb136590ce7c4a0ac95',
    'gradle/actions/setup-gradle@0723195856401067f7a2779048b490ace7a47d7c',
    'android-actions/setup-android@40fd30fb8d7440372e1316f5d1809ec01dcd3699',
    'actions/upload-artifact@043fb46d1a93c77aae656e7c1c64a875d1fc6a0a',
):
    if pin not in workflow:
        raise SystemExit(f'Missing immutable action pin: {pin}')

print('OK manual-release transactional invariants')
PY

tmp=$(mktemp -d)
trap 'rm -rf -- "${tmp}"' EXIT
cat > "${tmp}/build.gradle" <<'EOF'
android {
    defaultConfig {
        versionName "6.4.7"
        versionCode 6040700
    }
}
EOF
printf '%s\n' v6.4.7 v6.4.8 > "${tmp}/git-tags.txt"
printf '%s\n' v6.4.7 v6.4.8 > "${tmp}/release-tags.txt"
printf '{}\n' > "${tmp}/target.json"
python3 "${orchestrator}" resolve \
  --mode create_new_release \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/new-plan.json"
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["release_tag"])' "${tmp}/new-plan.json")" == v6.4.9 ]] ||
  fail 'Draft releases are not included in automatic version authority.'
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["release_version_code"])' "${tmp}/new-plan.json")" == 6040900 ]] ||
  fail 'Semantic version-code formula is incorrect.'

printf '%s\n' v6.4.7 v6.4.8 > "${tmp}/git-tags.txt"
printf '%s\n' v6.4.7 > "${tmp}/release-tags.txt"
if python3 "${orchestrator}" resolve \
  --mode create_new_release \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/skipped-tag-plan.json" >/dev/null 2>&1; then
  fail 'Create mode must not skip the newest tag-only interrupted release.'
fi
python3 "${orchestrator}" resolve \
  --mode repair_existing_release \
  --input-tag v6.4.8 \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/repair-plan.json"

printf '%s\n' topway_twmedia topway_twmedia_debug > "${tmp}/variants.txt"
: > "${tmp}/assets.txt"
python3 "${orchestrator}" plan-assets \
  --mode create_new_release \
  --release-tag v6.4.9 \
  --selected-variants-file "${tmp}/variants.txt" \
  --debug-destination workflow_artifacts \
  --existing-assets-file "${tmp}/assets.txt" \
  --replace false \
  --output "${tmp}/asset-plan.json"
[[ "$(python3 -c 'import json,sys; print(len(json.load(open(sys.argv[1]))["build_variants"]))' "${tmp}/asset-plan.json")" == 2 ]] ||
  fail 'Create-mode asset planning did not retain release and debug builds.'

base='Auxio-TS-v6.4.9-topway-twmedia-release.apk'
printf '%s\n' "${base}" "${base}.sha256" > "${tmp}/partial-assets.txt"
if python3 "${orchestrator}" plan-assets \
  --mode repair_existing_release \
  --release-tag v6.4.9 \
  --selected-variants-file "${tmp}/variants.txt" \
  --debug-destination workflow_artifacts \
  --existing-assets-file "${tmp}/partial-assets.txt" \
  --replace false \
  --output "${tmp}/partial-plan.json" >/dev/null 2>&1; then
  fail 'Partial existing triplets must fail closed without explicit replacement.'
fi

for checker in "${app_checker}" "${bridge_checker}"; do
  grep -Fq 'extract_apksigner_certificate_sha256' "${checker}" ||
    fail "${checker} does not use the shared signer parser."
  grep -Fq 'verify --verbose --print-certs' "${checker}" ||
    fail "${checker} does not request apksigner certificate output."
done

log 'manual release workflow, ruleset authority, state machine and LSPosed addon checks passed'
