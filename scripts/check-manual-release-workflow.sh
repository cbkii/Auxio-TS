#!/usr/bin/env bash
# CI-only behavioural checks for the bounded Manual Release workflow.
# Runtime publication deliberately does not execute source-shape assertions
# against its own workflow; these checks run on pull requests instead.

set -u
set -o pipefail

fail() {
  printf 'ERROR: %s\n' "$*" >&2
  exit 1
}

log() {
  printf '[INFO] %s\n' "$*" >&2
}

workflow='.github/workflows/manual-release.yml'
orchestrator='scripts/release-orchestrator.py'
release_script_dir='scripts/manual-release'

[[ -f "${workflow}" ]] || fail "Missing ${workflow}"
[[ -f "${orchestrator}" ]] || fail "Missing ${orchestrator}"
[[ -d "${release_script_dir}" ]] || fail "Missing ${release_script_dir}"

mapfile -t release_scripts < <(
  find "${release_script_dir}" -maxdepth 1 -type f -name '*.sh' -print | sort
)
expected_scripts=(
  scripts/manual-release/01-verify-branch-release-context.sh
  scripts/manual-release/03-select-maintained-release-assets.sh
  scripts/manual-release/04-preserve-release-orchestration-tools.sh
  scripts/manual-release/05-resolve-version-and-repository-release-state.sh
  scripts/manual-release/06-plan-efficient-asset-work.sh
  scripts/manual-release/07-checkout-immutable-repair-tag.sh
  scripts/manual-release/08-prepare-release-source-metadata.sh
  scripts/manual-release/09-install-android-and-native-build-tools.sh
  scripts/manual-release/10-prepare-ci-environment.sh
  scripts/manual-release/11-decode-release-keystore.sh
  scripts/manual-release/12-build-once-inspect-once-and-stage-selected-assets.sh
  scripts/manual-release/13-validate-new-release-source-contracts.sh
  scripts/manual-release/14-push-immutable-release-tag.sh
  scripts/manual-release/15-ensure-draft-release-transaction-exists.sh
  scripts/manual-release/16-upload-or-replace-planned-release-assets.sh
  scripts/manual-release/17-verify-remote-release-asset-manifest.sh
  scripts/manual-release/18-apply-requested-status-after-verified-create-transaction.sh
  scripts/manual-release/19-synchronise-released-source-metadata-to-dev.sh
  scripts/manual-release/20-write-release-summary.sh
)
[[ "$(printf '%s\n' "${release_scripts[@]}")" == "$(printf '%s\n' "${expected_scripts[@]}")" ]] || {
  printf 'Actual release scripts:\n%s\nExpected release scripts:\n%s\n' \
    "$(printf '%s\n' "${release_scripts[@]}")" \
    "$(printf '%s\n' "${expected_scripts[@]}")" >&2
  fail 'Manual Release script set is incomplete, duplicated or unexpectedly expanded.'
}

for script in "$0" "${release_scripts[@]}"; do
  bash -n "${script}" || fail "Shell syntax check failed for ${script}"
done
python3 -m py_compile "${orchestrator}" || fail 'release-orchestrator.py is not valid Python.'
python3 "${orchestrator}" self-test || fail 'Release planner self-tests failed.'

if command -v actionlint >/dev/null 2>&1; then
  actionlint "${workflow}" || fail 'Manual Release actionlint validation failed.'
else
  log 'actionlint unavailable; skipped'
fi
if command -v shellcheck >/dev/null 2>&1; then
  shellcheck "$0" "${release_scripts[@]}" || fail 'Manual Release shellcheck validation failed.'
else
  log 'shellcheck unavailable; skipped'
fi

python3 - "${workflow}" "${release_script_dir}" <<'PY'
import re
import sys
from pathlib import Path

workflow_path = Path(sys.argv[1])
script_dir = Path(sys.argv[2])
text = workflow_path.read_text(encoding='utf-8')
script_paths = sorted(script_dir.glob('*.sh'))
script_text = {str(path): path.read_text(encoding='utf-8') for path in script_paths}
orchestrator_text = Path('scripts/release-orchestrator.py').read_text(encoding='utf-8')
surface = text + '\n' + orchestrator_text + '\n' + '\n'.join(script_text.values())

try:
    import yaml
except ImportError as exc:
    raise SystemExit(f'PyYAML is required for Manual Release validation: {exc}')

data = yaml.safe_load(text)
if not isinstance(data, dict) or 'jobs' not in data:
    raise SystemExit('Manual Release YAML did not parse as a workflow mapping.')

expected_permissions = {'actions': 'read', 'checks': 'read', 'contents': 'write'}
if data.get('permissions') != expected_permissions:
    raise SystemExit(
        f'Manual Release permissions must equal {expected_permissions}; '
        f'got {data.get("permissions")!r}'
    )
release_job = data.get('jobs', {}).get('release', {})
if release_job.get('timeout-minutes') != 90:
    raise SystemExit('Manual Release job timeout must remain exactly 90 minutes.')

trigger = data.get('on', data.get(True))
if not isinstance(trigger, dict) or 'workflow_dispatch' not in trigger:
    raise SystemExit('Manual Release must remain workflow_dispatch-only.')
inputs = trigger['workflow_dispatch'].get('inputs', {})
expected_input_names = {
    'version_tag',
    'draft',
    'prerelease',
    'include_topway_twmedia_apk',
    'include_lsposed_bridge_apk',
    'publish_debug_apks',
    'replace_existing_assets',
}
if set(inputs) != expected_input_names:
    raise SystemExit(
        f'Manual Release form changed unexpectedly: {sorted(inputs)}; '
        f'expected {sorted(expected_input_names)}'
    )
version_description = str(inputs['version_tag'].get('description', '')).lower()
for phrase in ('blank', 'resume', 'next patch', 'existing tag', 'repair'):
    if phrase not in version_description:
        raise SystemExit(f'version_tag description is missing user guidance: {phrase}')
debug_description = str(inputs['publish_debug_apks'].get('description', '')).lower()
if 'debug' not in debug_description or 'normally leave off' not in debug_description:
    raise SystemExit('publish_debug_apks description must clearly identify the safe default.')
if inputs['publish_debug_apks'].get('default') is not False:
    raise SystemExit('Debug APK publication must default off.')
if inputs['include_topway_twmedia_apk'].get('default') is not True:
    raise SystemExit('The primary topwayTwMedia APK must default on.')

references = re.findall(r'run: bash (scripts/manual-release/[^\s]+\.sh)', text)
expected_references = [str(path) for path in script_paths]
if references != expected_references:
    raise SystemExit(
        'Manual Release script references are incomplete or out of execution order: '
        f'{references}'
    )
for path, body in script_text.items():
    if '${{' in body:
        raise SystemExit(f'Unevaluated GitHub expression was moved into {path}.')

required = (
    'group: manual-release',
    'cancel-in-progress: false',
    'persist-credentials: false',
    'RELEASE_MODE: auto',
    'PUBLISH_DEBUG_APKS: ${{ inputs.publish_debug_apks }}',
    'replace_names_file=',
    'REPLACE_NAMES_FILE: ${{ steps.asset_plan.outputs.replace_names_file }}',
    'Push immutable release tag',
    'Ensure draft release transaction exists',
    'Upload or replace planned release assets',
    'Verify remote release asset manifest',
    'Apply requested status after verified create transaction',
    'Synchronise released source metadata to dev',
    'timeout 60s git fetch --tags origin dev',
    'timeout 120s git push',
    'timeout 45m bash ./scripts/ci-gradle.sh',
    '--connect-timeout 15',
    '--max-time 300',
    'At least one maintained release asset must be selected.',
    'explicit_new_tag',
    'resume_latest_tag_without_release',
    'resume_latest_draft_release',
)
for token in required:
    if token not in surface:
        raise SystemExit(f'Missing Manual Release behaviour: {token}')

for forbidden in (
    'RELEASE_PUSH_TOKEN',
    '/releases/tags/',
    'gh release upload',
    'inputs.release_mode',
    'debug_variant_destination:',
    'Validate current release workflow contracts',
    'git push origin ":refs/tags/',
    '--force refs/tags/',
    'partial triplets require explicit replacement',
):
    if forbidden in surface:
        raise SystemExit(f'Forbidden brittle Manual Release behaviour remains: {forbidden}')

for path, body in script_text.items():
    for line in body.splitlines():
        stripped = line.strip()
        if stripped.startswith(('git fetch ', 'git push ', 'gh api ', 'sdkmanager ', 'sudo apt-get ')):
            raise SystemExit(f'Unbounded external command remains in {path}: {stripped}')

order = (
    'Build once, inspect once and stage selected assets',
    'Upload release recovery workflow artifact',
    'Push immutable release tag',
    'Ensure draft release transaction exists',
    'Upload or replace planned release assets',
    'Verify remote release asset manifest',
    'Apply requested status after verified create transaction',
    'Synchronise released source metadata to dev',
)
positions = [text.find(item) for item in order]
if any(pos < 0 for pos in positions) or positions != sorted(positions):
    raise SystemExit(f'Unsafe release transaction order: {list(zip(order, positions))}')

for pin in (
    'actions/checkout@df4cb1c069e1874edd31b4311f1884172cec0e10',
    'actions/setup-java@03ad4de0992f5dab5e18fcb136590ce7c4a0ac95',
    'gradle/actions/setup-gradle@0723195856401067f7a2779048b490ace7a47d7c',
    'android-actions/setup-android@40fd30fb8d7440372e1316f5d1809ec01dcd3699',
    'actions/upload-artifact@043fb46d1a93c77aae656e7c1c64a875d1fc6a0a',
):
    if pin not in text:
        raise SystemExit(f'Missing immutable action pin: {pin}')

print('OK Manual Release workflow, form, bounded scripts and transaction ordering')
PY
python_validation_status=$?
((python_validation_status == 0)) || fail 'Manual Release static contract validation failed.'

tmp="$(mktemp -d)" || fail 'Unable to create temporary test directory.'
trap 'rm -rf -- "${tmp}"' EXIT
cat > "${tmp}/build.gradle" <<'EOF_GRADLE'
android {
    defaultConfig {
        versionName "6.4.7"
        versionCode 6040700
    }
}
EOF_GRADLE
printf '%s\n' v6.4.7 v6.5.0 > "${tmp}/git-tags.txt"
printf '%s\n' v6.4.7 > "${tmp}/release-tags.txt"
: > "${tmp}/draft-tags.txt"
printf '{}\n' > "${tmp}/target.json"

python3 "${orchestrator}" resolve \
  --mode auto \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --draft-release-tags-file "${tmp}/draft-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/blank.json" || fail 'Blank automatic resume scenario failed.'
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["release_tag"])' "${tmp}/blank.json")" == v6.5.0 ]] ||
  fail 'Blank input did not resume the latest tag-only transaction.'
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["effective_mode"])' "${tmp}/blank.json")" == repair_existing_release ]] ||
  fail 'Blank input did not switch to repair for an interrupted transaction.'

python3 "${orchestrator}" resolve \
  --mode auto \
  --input-tag v6.5.5 \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --draft-release-tags-file "${tmp}/draft-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/explicit.json" || fail 'Explicit newer version scenario failed.'
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["release_tag"])' "${tmp}/explicit.json")" == v6.5.5 ]] ||
  fail 'Explicit v6.5.5 was not preserved.'
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["effective_mode"])' "${tmp}/explicit.json")" == create_new_release ]] ||
  fail 'Explicit v6.5.5 was incorrectly blocked by the older v6.5.0 tag-only transaction.'

python3 "${orchestrator}" resolve \
  --mode auto \
  --input-tag v6.5.0 \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --draft-release-tags-file "${tmp}/draft-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/existing.json" || fail 'Explicit existing tag scenario failed.'
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["effective_mode"])' "${tmp}/existing.json")" == repair_existing_release ]] ||
  fail 'Explicit existing tag did not resolve as repair.'

printf '%s\n' v6.4.7 v6.5.0 > "${tmp}/release-tags.txt"
python3 "${orchestrator}" resolve \
  --mode auto \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --draft-release-tags-file "${tmp}/draft-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/next.json" || fail 'Next completed release scenario failed.'
[[ "$(python3 -c 'import json,sys; print(json.load(open(sys.argv[1]))["release_tag"])' "${tmp}/next.json")" == v6.5.1 ]] ||
  fail 'Blank input did not create the next patch after a complete latest release.'

printf '%s\n' topway_twmedia topway_twmedia_debug > "${tmp}/variants.txt"
base='Auxio-TS-v6.5.0-topway-twmedia-release.apk'
printf '%s\n' "${base}" "${base}.sha256" > "${tmp}/partial-assets.txt"
python3 "${orchestrator}" plan-assets \
  --mode repair_existing_release \
  --release-tag v6.5.0 \
  --selected-variants-file "${tmp}/variants.txt" \
  --debug-destination workflow_artifacts \
  --existing-assets-file "${tmp}/partial-assets.txt" \
  --replace false \
  --output "${tmp}/partial-plan.json" || fail 'Partial interrupted asset triplet did not auto-repair.'
[[ "$(python3 -c 'import json,sys; print(len(json.load(open(sys.argv[1]))["replace_names"]))' "${tmp}/partial-plan.json")" == 2 ]] ||
  fail 'Partial asset repair did not authorise replacement of the existing pieces.'

printf '%s\n' v6.4.7 > "${tmp}/git-tags.txt"
printf '%s\n' v6.4.7 v6.5.0 > "${tmp}/release-tags.txt"
if python3 "${orchestrator}" resolve \
  --mode auto \
  --input-tag v6.5.5 \
  --source-gradle "${tmp}/build.gradle" \
  --git-tags-file "${tmp}/git-tags.txt" \
  --release-tags-file "${tmp}/release-tags.txt" \
  --draft-release-tags-file "${tmp}/draft-tags.txt" \
  --target-release-json "${tmp}/target.json" \
  --output "${tmp}/impossible.json" >/dev/null 2>&1; then
  fail 'A GitHub Release without a resolvable tag must still fail closed.'
fi

printf 'Manual Release checks: PASS\n'
