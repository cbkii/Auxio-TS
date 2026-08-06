#!/usr/bin/env bash
# CI-only behavioural checks for Manual Release. Runtime publication must not
# execute source-shape assertions against its own workflow.

set -u
set -o pipefail

fail() {
  printf 'ERROR: %s\n' "$*" >&2
  exit 1
}

workflow='.github/workflows/manual-release.yml'
orchestrator='scripts/release-orchestrator.py'
[[ -f "${workflow}" ]] || fail "Missing ${workflow}"
[[ -f "${orchestrator}" ]] || fail "Missing ${orchestrator}"

python3 -m py_compile "${orchestrator}" || fail 'release-orchestrator.py is not valid Python.'
python3 "${orchestrator}" self-test || fail 'Release planner self-tests failed.'

python3 - "${workflow}" <<'PY'
import sys
from pathlib import Path

path = Path(sys.argv[1])
text = path.read_text(encoding='utf-8')

try:
    import yaml
except ImportError:
    yaml = None

if yaml is not None:
    data = yaml.safe_load(text)
    if not isinstance(data, dict) or 'jobs' not in data:
        raise SystemExit('Manual Release YAML did not parse as a workflow mapping.')
    permissions = data.get('permissions', {})
    if permissions.get('contents') != 'write':
        raise SystemExit('Manual Release requires contents: write.')
    release = data['jobs'].get('release', {})
    timeout = release.get('timeout-minutes')
    if not isinstance(timeout, int) or not 60 <= timeout <= 120:
        raise SystemExit('Manual Release job timeout must remain between 60 and 120 minutes.')

required = (
    'workflow_dispatch:',
    'description: "Version (optional): blank resumes an interrupted release or creates the next patch; e.g. v6.5.5"',
    'RELEASE_MODE: auto',
    'group: manual-release',
    'cancel-in-progress: false',
    'scripts/release-orchestrator.py',
    'Push immutable release tag',
    'Ensure draft release transaction exists',
    'Upload or replace planned release assets',
    'Verify remote release asset manifest',
    'Synchronise released source metadata to dev',
    'timeout 60s git fetch --tags origin dev',
    'timeout 2700s bash ./scripts/ci-gradle.sh',
    '--connect-timeout 15',
    '--max-time 300',
    'replace_names_file=',
)
for token in required:
    if token not in text:
        raise SystemExit(f'Missing Manual Release behaviour: {token}')

for forbidden in (
    'RELEASE_PUSH_TOKEN',
    '/releases/tags/',
    'gh release upload',
    'inputs.release_mode',
    'Validate current release workflow contracts',
    'partial triplets require explicit replacement',
):
    if forbidden in text:
        raise SystemExit(f'Forbidden brittle Manual Release behaviour remains: {forbidden}')

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

print('OK Manual Release behavioural contract')
PY

tmp="$(mktemp -d)" || fail 'Unable to create temporary test directory.'
trap 'rm -rf -- "${tmp}"' EXIT
cat > "${tmp}/build.gradle" <<'EOF'
android {
    defaultConfig {
        versionName "6.4.7"
        versionCode 6040700
    }
}
EOF
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
  fail 'Explicit v6.5.5 was incorrectly blocked by an older orphan tag.'

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
  fail 'Partial asset repair did not authorise replacement of existing pieces.'

printf 'Manual Release checks: PASS\n'
