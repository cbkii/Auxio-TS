#!/usr/bin/env bash
# CI-only end-to-end smoke of Manual Release signing/build/inspection and Magisk packaging.
# Uses a throwaway keystore; never calls GitHub publication APIs or pushes refs.
set -u
set -o pipefail
fail() { printf 'FAILED: Manual Release packaging smoke: %s\n' "$*" >&2; exit 1; }
log() { printf '[manual-release-smoke] %s\n' "$*" >&2; }

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.." 2>/dev/null && pwd -P) || fail 'cannot resolve repository root'
cd -- "$repo_root" || fail "cannot enter repository root: $repo_root"
for command in bash base64 git jq keytool python3 sha256sum timeout zip unzip; do command -v "$command" >/dev/null 2>&1 || fail "required command unavailable: $command"; done
runner_temp=${RUNNER_TEMP:-}; [[ -n "$runner_temp" && -d "$runner_temp" ]] || fail 'RUNNER_TEMP must identify an existing private runner directory'
work=$(mktemp -d "${runner_temp}/manual-release-packaging-smoke.XXXXXX") || fail 'cannot create temporary work directory'
start_head=$(git rev-parse HEAD) || fail 'cannot capture checkout HEAD'
cleanup() { rc=$?; trap - EXIT INT TERM; rm -rf -- "$work" || ((rc=1)); git reset --hard "$start_head" >/dev/null 2>&1 || ((rc=1)); exit "$rc"; }
trap cleanup EXIT INT TERM

log 'installing release toolchain and dependencies'
bash scripts/manual-release/09-install-android-and-native-build-tools.sh || fail 'release tool setup failed'
bash scripts/manual-release/10-prepare-ci-environment.sh || fail 'release dependency bootstrap failed'

keystore_src="$work/smoke.p12"; store_password='auxio-ts-ci-smoke-only'; key_alias='auxio-ts-ci-smoke'; key_password="$store_password"
timeout 30s keytool -genkeypair -keystore "$keystore_src" -storetype PKCS12 -storepass "$store_password" -keypass "$key_password" -alias "$key_alias" -keyalg RSA -keysize 2048 -validity 2 -dname 'CN=Auxio-TS Manual Release CI Smoke,O=Auxio-TS,C=AU' -noprompt >/dev/null 2>&1 || fail 'ephemeral signer generation failed'
keystore_b64=$(base64 < "$keystore_src" | tr -d '\n') || fail 'keystore encoding failed'
: > "$work/decode.out"
RUNNER_TEMP="$work" GITHUB_OUTPUT="$work/decode.out" KEYSTORE_BASE64="$keystore_b64" KEYSTORE_PASSWORD="$store_password" KEY_ALIAS="$key_alias" KEY_PASSWORD="$key_password" bash scripts/manual-release/11-decode-release-keystore.sh || fail 'keystore decode failed'
decoded_keystore=$(sed -n 's/^path=//p' "$work/decode.out" | tail -n1); [[ -f "$decoded_keystore" ]] || fail 'decoded keystore missing'

: > "$work/tooling.out"
RUNNER_TEMP="$work" GITHUB_OUTPUT="$work/tooling.out" bash scripts/manual-release/04-preserve-release-orchestration-tools.sh || fail 'tool preservation failed'
tools_dir=$(sed -n 's/^tools_dir=//p' "$work/tooling.out" | tail -n1)
[[ -x "$tools_dir/release-orchestrator.py" && -x "$tools_dir/release-asset-matrix.py" ]] || fail 'preserved release tools missing'

python3 "$tools_dir/release-orchestrator.py" inspect-gradle --gradle app/build.gradle --output "$work/current.json" || fail 'metadata inspection failed'
current_version=$(jq -r .version_name "$work/current.json")
printf 'v%s\n' "$current_version" > "$work/git-tags.txt"; cp "$work/git-tags.txt" "$work/release-tags.txt"; : > "$work/draft-tags.txt"; printf '{}\n' > "$work/target-release.json"
python3 "$tools_dir/release-orchestrator.py" resolve --mode auto --source-gradle app/build.gradle --git-tags-file "$work/git-tags.txt" --release-tags-file "$work/release-tags.txt" --draft-release-tags-file "$work/draft-tags.txt" --target-release-json "$work/target-release.json" --output "$work/release-plan.json" || fail 'next-patch planning failed'
release_tag=$(jq -r .release_tag "$work/release-plan.json"); version_name=$(jq -r .release_version_name "$work/release-plan.json"); version_code=$(jq -r .release_version_code "$work/release-plan.json"); metadata_change_required=$(jq -r .metadata_change_required "$work/release-plan.json")
[[ $(jq -r .effective_mode "$work/release-plan.json") == create_new_release ]] || fail 'smoke did not select create transaction'

: > "$work/metadata.out"
RUNNER_TEMP="$work" GITHUB_OUTPUT="$work/metadata.out" TOOL="$tools_dir/release-orchestrator.py" RELEASE_MODE=create_new_release RELEASE_TAG="$release_tag" VERSION_NAME="$version_name" VERSION_CODE="$version_code" METADATA_CHANGE_REQUIRED="$metadata_change_required" bash scripts/manual-release/08-prepare-release-source-metadata.sh || fail 'release metadata preparation failed'
release_sha=$(sed -n 's/^release_sha=//p' "$work/metadata.out" | tail -n1); [[ "$release_sha" =~ ^[0-9a-fA-F]{40}$ ]] || fail 'release SHA malformed'

# Highest-risk supported matrix. There is deliberately no raw topwayTwMusic APK logical variant.
printf '%s\n' standard standard_debug topway_twmedia topway_twmedia_debug topway_twmusic_magisk lsposed_bridge lsposed_bridge_debug > "$work/selected.txt"
: > "$work/existing.txt"
python3 "$tools_dir/release-asset-matrix.py" plan-assets --mode create_new_release --release-tag "$release_tag" --selected-variants-file "$work/selected.txt" --debug-destination workflow_artifacts --existing-assets-file "$work/existing.txt" --replace false --output "$work/asset-plan.json" || fail 'asset planning failed'
jq -r '.build_variants[]' "$work/asset-plan.json" > "$work/build-variants.txt"
jq -r '.build_asset_names[]' "$work/asset-plan.json" > "$work/build-asset-names.txt"
jq -r '.upload_names[]' "$work/asset-plan.json" > "$work/upload-names.txt"
[[ $(wc -l < "$work/build-variants.txt" | tr -d ' ') == 7 ]] || fail 'full supported matrix must build seven logical assets'

: > "$work/build.out"
export AUXIO_TS_CI_GRADLE_LOG="$runner_temp/manual-release-packaging-smoke-gradle.log"; : > "$AUXIO_TS_CI_GRADLE_LOG"
log "running exact three-variant packaging for $release_tag"
if ! RUNNER_TEMP="$work" GITHUB_OUTPUT="$work/build.out" \
  ORG_GRADLE_PROJECT_releaseStoreFile="$decoded_keystore" ORG_GRADLE_PROJECT_releaseStorePassword="$store_password" ORG_GRADLE_PROJECT_releaseKeyAlias="$key_alias" ORG_GRADLE_PROJECT_releaseKeyPassword="$key_password" \
  ORG_GRADLE_PROJECT_bridgeVersionName="$version_name" ORG_GRADLE_PROJECT_bridgeVersionCode="$version_code" \
  RELEASE_TAG="$release_tag" VERSION_NAME="$version_name" VERSION_CODE="$version_code" RELEASE_SHA="$release_sha" \
  BUILD_VARIANTS_FILE="$work/build-variants.txt" BUILD_ASSET_NAMES_FILE="$work/build-asset-names.txt" UPLOAD_NAMES_FILE="$work/upload-names.txt" DEBUG_DESTINATION=workflow_artifacts \
  TOOLS_DIR="$tools_dir" ASSET_TOOL="$tools_dir/release-asset-matrix.py" ASSET_PLAN_FILE="$work/asset-plan.json" RELEASE_PLAN_FILE="$work/release-plan.json" NEEDS_SIGNING=true \
  bash scripts/manual-release/12-build-once-inspect-once-and-stage-selected-assets.sh; then
  tail -n 200 "$AUXIO_TS_CI_GRADLE_LOG" >&2 || true; fail 'exact build/inspect/stage failed'
fi

manifest=$(sed -n 's/^manifest_file=//p' "$work/build.out" | tail -n1); upload_tsv=$(sed -n 's/^upload_tsv=//p' "$work/build.out" | tail -n1)
[[ -f "$manifest" && -f "$upload_tsv" ]] || fail 'build outputs missing'
[[ $(jq 'length' "$manifest") == 7 ]] || fail 'manifest must contain seven selected logical assets'
[[ $(jq '[.[]|select(.destination=="release")]|length' "$manifest") == 4 ]] || fail 'release must contain Standard, TopwayTwMedia, Magisk and LSPosed logical assets'
[[ $(jq '[.[]|select(.destination=="workflow_artifacts")]|length' "$manifest") == 3 ]] || fail 'debug companions must remain workflow artifacts'
jq -e 'any(.[]; .variant=="standard" and .application_id=="org.oxycblt.auxio")' "$manifest" >/dev/null || fail 'Standard identity missing'
jq -e 'any(.[]; .variant=="topway_twmedia" and .application_id=="com.tw.media")' "$manifest" >/dev/null || fail 'TopwayTwMedia identity missing'
jq -e 'any(.[]; .variant=="topway_twmusic_magisk" and .application_id=="com.tw.music" and (.filename|endswith("-magisk.zip")))' "$manifest" >/dev/null || fail 'systemless exact-package asset missing'
if grep -Eiq 'topway[-_]?twmusic.*\.apk' "$upload_tsv"; then fail 'raw topwayTwMusic APK leaked into publication TSV'; fi

magisk=$(jq -r '.[]|select(.variant=="topway_twmusic_magisk")|.filename' "$manifest")
release_dir=$(sed -n 's/^release_artifact_dir=//p' "$work/build.out" | tail -n1)
unzip -Z1 "$release_dir/$magisk" | grep -Fxq 'system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk' || fail 'Magisk ZIP exact stock target missing'
unzip -Z1 "$release_dir/$magisk" | grep -Fxq customize.sh || fail 'Magisk installer missing'

bash scripts/manual-release/13-validate-new-release-source-contracts.sh || fail 'new-release source contracts failed after packaging'
printf 'SUCCESS: Manual Release three-variant + systemless Magisk packaging smoke passed for %s\n' "$release_tag"
