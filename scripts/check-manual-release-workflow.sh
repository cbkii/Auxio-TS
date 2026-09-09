#!/usr/bin/env bash
# CI-only static/behavioural checks for bounded three-variant Manual Release.
set -u
set -o pipefail
fail() { printf 'ERROR: %s\n' "$*" >&2; exit 1; }
log() { printf '[INFO] %s\n' "$*" >&2; }

workflow=.github/workflows/manual-release.yml
orchestrator=scripts/release-orchestrator.py
asset_tool=scripts/release-asset-matrix.py
release_script_dir=scripts/manual-release
for path in "$workflow" "$orchestrator" "$asset_tool" scripts/package-topway-twmusic-magisk-module.sh; do [[ -s $path ]] || fail "Missing $path"; done

mapfile -t release_scripts < <(find "$release_script_dir" -maxdepth 1 -type f -name '*.sh' -print | sort)
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
[[ "$(printf '%s\n' "${release_scripts[@]}")" == "$(printf '%s\n' "${expected_scripts[@]}")" ]] || fail 'Manual Release script set/order changed unexpectedly.'
for script in "$0" "${release_scripts[@]}" scripts/package-topway-twmusic-magisk-module.sh; do bash -n "$script" || fail "Shell syntax: $script"; done
for script in "$orchestrator" "$asset_tool"; do python3 -m py_compile "$script" || fail "Python syntax: $script"; done
python3 "$orchestrator" self-test || fail 'Version/release orchestrator self-test failed.'
python3 "$asset_tool" self-test || fail 'Three-variant asset matrix self-test failed.'

if command -v actionlint >/dev/null 2>&1; then actionlint "$workflow" || fail 'Manual Release actionlint failed.'; else log 'actionlint unavailable; skipped'; fi
if command -v shellcheck >/dev/null 2>&1; then shellcheck "$0" "${release_scripts[@]}" scripts/package-topway-twmusic-magisk-module.sh || fail 'Manual Release shellcheck failed.'; else log 'shellcheck unavailable; skipped'; fi

python3 - "$workflow" <<'PY'
import re, sys
from pathlib import Path
try:
    import yaml
except ImportError as exc:
    raise SystemExit(f'PyYAML required: {exc}')
path=Path(sys.argv[1]); text=path.read_text(encoding='utf-8'); data=yaml.safe_load(text)
if data.get('permissions') != {'actions':'read','checks':'read','contents':'write'}:
    raise SystemExit('Manual Release permissions changed')
job=data.get('jobs',{}).get('release',{})
if job.get('timeout-minutes') != 90: raise SystemExit('Manual Release timeout must be 90 minutes')
trigger=data.get('on', data.get(True)); inputs=trigger.get('workflow_dispatch',{}).get('inputs',{})
expected={'version_tag','draft','prerelease','include_standard_apk','include_topway_twmedia_apk','include_topway_twmusic_magisk','include_lsposed_bridge_apk','publish_debug_apks','replace_existing_assets'}
if set(inputs)!=expected: raise SystemExit(f'Unexpected Manual Release inputs: {sorted(inputs)}')
for name in ('include_standard_apk','include_topway_twmedia_apk'):
    if inputs[name].get('default') is not True: raise SystemExit(f'{name} must default on')
for name in ('include_topway_twmusic_magisk','include_lsposed_bridge_apk','publish_debug_apks'):
    if inputs[name].get('default') is not False: raise SystemExit(f'{name} must default off')
version_desc=str(inputs['version_tag'].get('description','')).lower()
for phrase in ('blank','resume','next patch','existing tag','repair'):
    if phrase not in version_desc: raise SystemExit(f'version_tag guidance missing {phrase}')
if 'normally leave off' not in str(inputs['publish_debug_apks'].get('description','')).lower():
    raise SystemExit('debug publication safe default is unclear')
for token in (
    'INCLUDE_STANDARD: ${{ inputs.include_standard_apk }}',
    'INCLUDE_TOPWAY_TWMEDIA: ${{ inputs.include_topway_twmedia_apk }}',
    'INCLUDE_TOPWAY_TWMUSIC_MAGISK: ${{ inputs.include_topway_twmusic_magisk }}',
    'ASSET_TOOL: ${{ steps.tooling.outputs.tools_dir }}/release-asset-matrix.py',
    'BUILD_ASSET_NAMES_FILE:', 'persist-credentials: false', 'group: manual-release', 'cancel-in-progress: false',
):
    if token not in text: raise SystemExit(f'Missing release contract: {token}')
if 'include_app_apk:' in text: raise SystemExit('retired single-product input returned')
PY

surface=$(mktemp); trap 'rm -f -- "$surface"' EXIT
cat "$workflow" "$orchestrator" "$asset_tool" "${release_scripts[@]}" scripts/package-topway-twmusic-magisk-module.sh > "$surface"
for token in \
  ':app:assembleStandardRelease' ':app:assembleTopwayTwMediaRelease' ':app:assembleTopwayTwMusicRelease' \
  'topway-twmusic-magisk.zip' 'package-topway-twmusic-magisk-module.sh' \
  'Push immutable release tag' 'Ensure draft release transaction exists' 'Verify remote release asset manifest' \
  'timeout 60s git fetch --tags origin dev' 'timeout 120s git push' 'timeout 45m bash ./scripts/ci-gradle.sh' \
  '--connect-timeout 15' '--max-time 300'; do
  grep -Fq -- "$token" "$surface" || fail "Missing Manual Release behaviour: $token"
done
for forbidden in 'include_app_apk:' 'RELEASE_PUSH_TOKEN' 'gh release upload' 'git push origin ":refs/tags/' '--force refs/tags/'; do
  if grep -Fq -- "$forbidden" "$surface"; then fail "Forbidden release behaviour remains: $forbidden"; fi
done
if grep -Eiq 'Auxio-TS-[^[:space:]]*topway-twmusic[^[:space:]]*\.apk' "$surface"; then fail 'raw topwayTwMusic public APK name detected'; fi

python3 - "$workflow" <<'PY'
from pathlib import Path
text=Path(__import__('sys').argv[1]).read_text(encoding='utf-8')
order=('Build once, inspect once and stage selected assets','Upload release recovery workflow artifact','Push immutable release tag','Ensure draft release transaction exists','Upload or replace planned release assets','Verify remote release asset manifest','Apply requested status after verified create transaction','Synchronise released source metadata to dev')
pos=[text.find(x) for x in order]
if any(x<0 for x in pos) or pos != sorted(pos): raise SystemExit(f'Unsafe transaction order: {list(zip(order,pos))}')
for pin in ('actions/checkout@df4cb1c069e1874edd31b4311f1884172cec0e10','actions/setup-java@03ad4de0992f5dab5e18fcb136590ce7c4a0ac95','gradle/actions/setup-gradle@0723195856401067f7a2779048b490ace7a47d7c','android-actions/setup-android@40fd30fb8d7440372e1316f5d1809ec01dcd3699','actions/upload-artifact@043fb46d1a93c77aae656e7c1c64a875d1fc6a0a'):
    if pin not in text: raise SystemExit(f'Missing immutable action pin: {pin}')
PY

for path in "${release_scripts[@]}"; do
  while IFS= read -r line; do
    stripped=${line#"${line%%[![:space:]]*}"}
    case "$stripped" in git\ fetch\ *|git\ push\ *|gh\ api\ *|sdkmanager\ *|sudo\ apt-get\ *) fail "Unbounded external command in $path: $stripped" ;; esac
  done < "$path"
done

# Selection contract: Standard + topwayTwMedia default lanes, exact-package and Track C opt-in.
tmp=$(mktemp -d); trap 'rm -rf -- "$tmp" "$surface"' EXIT
run_select() {
  : > "$tmp/out"
  RUNNER_TEMP="$tmp" GITHUB_OUTPUT="$tmp/out" INCLUDE_STANDARD="$1" INCLUDE_TOPWAY_TWMEDIA="$2" \
    INCLUDE_TOPWAY_TWMUSIC_MAGISK="$3" INCLUDE_LSPOSED_BRIDGE="$4" PUBLISH_DEBUG_APKS="$5" \
    bash scripts/manual-release/03-select-maintained-release-assets.sh
}
run_select true true false false false || fail 'default asset selection failed'
selected=$(sed -n 's/^selected_file=//p' "$tmp/out")
[[ "$(cat "$selected")" == $'standard\nstandard_debug\ntopway_twmedia\ntopway_twmedia_debug' ]] || fail 'default asset matrix changed unexpectedly'
run_select true true true true false || fail 'full supported asset selection failed'
selected=$(sed -n 's/^selected_file=//p' "$tmp/out")ngrep -Fxq topway_twmusic_magisk "$selected" || fail 'Magisk exact-package logical asset was not selected'
if grep -Fq topway_twmusic.apk "$selected"; then fail 'raw topwayTwMusic APK became selectable'; fi
if run_select true false false true false >/dev/null 2>&1; then fail 'LSPosed selection without topwayTwMedia target was accepted'; fi

printf 'Manual Release three-variant/systemless contracts: PASS\n'
