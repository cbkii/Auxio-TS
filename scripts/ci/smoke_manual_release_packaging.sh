#!/usr/bin/env bash
# CI-only end-to-end smoke of the exact Manual Release signing/build/inspection stage.
# Uses a throwaway keystore and never calls GitHub publication APIs or pushes refs.
set -u
set -o pipefail

fail() {
  printf 'FAILED: Manual Release packaging smoke: %s\n' "$*" >&2
  exit 1
}
log() { printf '[manual-release-smoke] %s\n' "$*" >&2; }

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.." 2>/dev/null && pwd -P) ||
  fail 'cannot resolve repository root'
cd -- "$repo_root" || fail "cannot enter repository root: $repo_root"

for command in bash base64 git jq keytool python3 sha256sum timeout; do
  command -v "$command" >/dev/null 2>&1 || fail "required command is unavailable: $command"
done

runner_temp=${RUNNER_TEMP:-}
[[ -n "$runner_temp" && -d "$runner_temp" ]] || fail 'RUNNER_TEMP must identify an existing private runner directory'
work=$(mktemp -d "${runner_temp}/manual-release-packaging-smoke.XXXXXX") || fail 'cannot create temporary work directory'
start_head=$(git rev-parse HEAD) || fail 'cannot capture initial checkout HEAD'
cleanup() {
  rc=$?
  trap - EXIT INT TERM
  rm -rf -- "$work"
  if ! git reset --hard "$start_head" >/dev/null 2>&1; then
    printf 'FAILED: Manual Release packaging smoke: cannot restore initial checkout HEAD %s\n' "$start_head" >&2
    (( rc == 0 )) && rc=1
  fi
  exit "$rc"
}
trap cleanup EXIT INT TERM

# Exercise the same bounded dependency/bootstrap helpers used by Manual Release.
log 'installing exact Android/native release toolchain'
if ! bash scripts/manual-release/09-install-android-and-native-build-tools.sh; then
  fail 'release Android/native tool setup failed'
fi
log 'preparing exact release dependency profile'
if ! bash scripts/manual-release/10-prepare-ci-environment.sh; then
  fail 'release dependency bootstrap failed'
fi

# Generate a disposable CI-only signer. It proves the release signing path without exposing or
# depending on production signing material.
keystore_src="${work}/smoke-source.p12"
store_password='auxio-ts-ci-smoke-only'
key_alias='auxio-ts-ci-smoke'
key_password="$store_password"
log 'generating ephemeral release signer'
if ! timeout 30s keytool -genkeypair \
  -keystore "$keystore_src" \
  -storetype PKCS12 \
  -storepass "$store_password" \
  -keypass "$key_password" \
  -alias "$key_alias" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 2 \
  -dname 'CN=Auxio-TS Manual Release CI Smoke,O=Auxio-TS,C=AU' \
  -noprompt >/dev/null 2>&1; then
  fail 'ephemeral keystore generation failed'
fi
keystore_b64=$(base64 < "$keystore_src" | tr -d '\n') || fail 'ephemeral keystore base64 encoding failed'
[[ -n "$keystore_b64" ]] || fail 'ephemeral keystore encoding is empty'

decode_output="${work}/decode.out"
: > "$decode_output"
log 'exercising Manual Release keystore decode step'
if ! RUNNER_TEMP="$work" GITHUB_OUTPUT="$decode_output" \
  KEYSTORE_BASE64="$keystore_b64" KEYSTORE_PASSWORD="$store_password" \
  KEY_ALIAS="$key_alias" KEY_PASSWORD="$key_password" \
  bash scripts/manual-release/11-decode-release-keystore.sh; then
  fail 'Manual Release keystore decode step rejected a valid ephemeral signer'
fi
decoded_keystore=$(sed -n 's/^path=//p' "$decode_output" | tail -n1)
[[ -n "$decoded_keystore" && -f "$decoded_keystore" ]] || fail 'decoded keystore path was not produced'

# Preserve the exact orchestration helper, as the real workflow does before any immutable checkout.
tooling_output="${work}/tooling.out"
: > "$tooling_output"
if ! RUNNER_TEMP="$work" GITHUB_OUTPUT="$tooling_output" \
  bash scripts/manual-release/04-preserve-release-orchestration-tools.sh; then
  fail 'release orchestration tool preservation/self-test failed'
fi
tools_dir=$(sed -n 's/^tools_dir=//p' "$tooling_output" | tail -n1)
[[ -n "$tools_dir" && -x "$tools_dir/release-orchestrator.py" ]] || fail 'preserved release orchestrator is unavailable'

# Exercise an ordinary new-release metadata transaction. Model the current source version as the
# latest completed external release so automatic mode must create the next patch and commit the
# updated app metadata exactly as the real workflow does.
current_metadata="${work}/current-gradle-metadata.json"
if ! python3 "$tools_dir/release-orchestrator.py" inspect-gradle \
  --gradle app/build.gradle \
  --output "$current_metadata"; then
  fail 'current source metadata inspection failed'
fi
current_version=$(jq -r .version_name "$current_metadata")
printf 'v%s\n' "$current_version" > "${work}/git-tags.txt"
printf 'v%s\n' "$current_version" > "${work}/release-tags.txt"
: > "${work}/draft-tags.txt"
printf '{}\n' > "${work}/target-release.json"
release_plan="${work}/release-plan.json"
if ! python3 "$tools_dir/release-orchestrator.py" resolve \
  --mode auto \
  --source-gradle app/build.gradle \
  --git-tags-file "${work}/git-tags.txt" \
  --release-tags-file "${work}/release-tags.txt" \
  --draft-release-tags-file "${work}/draft-tags.txt" \
  --target-release-json "${work}/target-release.json" \
  --output "$release_plan"; then
  fail 'next-patch create transaction planning failed'
fi
release_tag=$(jq -r .release_tag "$release_plan")
version_name=$(jq -r .release_version_name "$release_plan")
version_code=$(jq -r .release_version_code "$release_plan")
metadata_change_required=$(jq -r .metadata_change_required "$release_plan")
[[ "$(jq -r .effective_mode "$release_plan")" == create_new_release ]] || fail 'smoke planner did not select a new-release transaction'
[[ "$release_tag" == "v${version_name}" && "$version_code" =~ ^[0-9]+$ ]] || fail 'release plan returned malformed version metadata'

metadata_output="${work}/metadata.out"
: > "$metadata_output"
log "preparing local release metadata commit for ${release_tag}"
if ! RUNNER_TEMP="$work" GITHUB_OUTPUT="$metadata_output" \
  TOOL="$tools_dir/release-orchestrator.py" \
  RELEASE_MODE=create_new_release RELEASE_TAG="$release_tag" \
  VERSION_NAME="$version_name" VERSION_CODE="$version_code" \
  METADATA_CHANGE_REQUIRED="$metadata_change_required" \
  bash scripts/manual-release/08-prepare-release-source-metadata.sh; then
  fail 'Manual Release source metadata preparation failed'
fi
release_sha=$(sed -n 's/^release_sha=//p' "$metadata_output" | tail -n1)
[[ "$release_sha" =~ ^[0-9a-fA-F]{40}$ ]] || fail 'prepared release commit SHA is malformed'
[[ "$(git rev-parse HEAD)" == "$release_sha" ]] || fail 'prepared release SHA does not identify the checkout HEAD'
[[ -z "$(git status --porcelain)" ]] || fail 'source metadata preparation left an uncommitted working tree'

prepared_metadata="${work}/prepared-gradle-metadata.json"
if ! python3 "$tools_dir/release-orchestrator.py" inspect-gradle \
  --gradle app/build.gradle \
  --output "$prepared_metadata"; then
  fail 'prepared source metadata inspection failed'
fi
prepared_name=$(jq -r .version_name "$prepared_metadata")
prepared_code=$(jq -r .version_code "$prepared_metadata")
[[ "$prepared_name" == "$version_name" ]] || fail "prepared source versionName mismatch: ${prepared_name} != ${version_name}"
[[ "$prepared_code" == "$version_code" ]] || fail "prepared source versionCode mismatch: ${prepared_code} != ${version_code}"

# Exercise the highest-risk operator selection: primary Topway plus optional LSPosed addon.
# Manual Release deliberately builds debug companions even when they remain workflow artifacts, so
# include all four variants here. This catches both release-only and debug-only packaging regressions.
printf '%s\n' topway_twmedia topway_twmedia_debug lsposed_bridge lsposed_bridge_debug > "${work}/selected-release-variants.txt"
: > "${work}/existing-assets.txt"
asset_plan="${work}/asset-plan.json"
if ! python3 "$tools_dir/release-orchestrator.py" plan-assets \
  --mode create_new_release \
  --release-tag "$release_tag" \
  --selected-variants-file "${work}/selected-release-variants.txt" \
  --debug-destination workflow_artifacts \
  --existing-assets-file "${work}/existing-assets.txt" \
  --replace false \
  --output "$asset_plan"; then
  fail 'release asset planning failed'
fi
jq -r '.build_variants[]' "$asset_plan" > "${work}/build-variants.txt" || fail 'cannot materialise build variant plan'
jq -r '.build_apk_names[]' "$asset_plan" > "${work}/build-apk-names.txt" || fail 'cannot materialise APK name plan'
jq -r '.upload_names[]' "$asset_plan" > "${work}/upload-names.txt" || fail 'cannot materialise upload name plan'
[[ "$(wc -l < "${work}/build-variants.txt" | tr -d ' ')" == 4 ]] || fail 'smoke plan must build primary/bridge release APKs and both debug companions'

build_output="${work}/build.out"
: > "$build_output"
export AUXIO_TS_CI_GRADLE_LOG="${runner_temp}/manual-release-packaging-smoke-gradle.log"
: > "$AUXIO_TS_CI_GRADLE_LOG"
log "running exact build/inspect/stage step for ${release_tag}"
if ! RUNNER_TEMP="$work" GITHUB_OUTPUT="$build_output" \
  ORG_GRADLE_PROJECT_releaseStoreFile="$decoded_keystore" \
  ORG_GRADLE_PROJECT_releaseStorePassword="$store_password" \
  ORG_GRADLE_PROJECT_releaseKeyAlias="$key_alias" \
  ORG_GRADLE_PROJECT_releaseKeyPassword="$key_password" \
  ORG_GRADLE_PROJECT_bridgeVersionName="$version_name" \
  ORG_GRADLE_PROJECT_bridgeVersionCode="$version_code" \
  RELEASE_TAG="$release_tag" VERSION_NAME="$version_name" VERSION_CODE="$version_code" \
  RELEASE_SHA="$release_sha" \
  BUILD_VARIANTS_FILE="${work}/build-variants.txt" \
  BUILD_APK_NAMES_FILE="${work}/build-apk-names.txt" \
  UPLOAD_NAMES_FILE="${work}/upload-names.txt" \
  DEBUG_DESTINATION=workflow_artifacts \
  TOOLS_DIR="$tools_dir" \
  ASSET_PLAN_FILE="$asset_plan" \
  RELEASE_PLAN_FILE="$release_plan" \
  NEEDS_SIGNING=true \
  bash scripts/manual-release/12-build-once-inspect-once-and-stage-selected-assets.sh; then
  if [[ -f "$AUXIO_TS_CI_GRADLE_LOG" ]]; then
    printf '%s\n' '--- last 200 Gradle log lines ---' >&2
    tail -n 200 "$AUXIO_TS_CI_GRADLE_LOG" >&2
  fi
  fail 'exact Manual Release build/inspect/stage step failed'
fi

manifest=$(sed -n 's/^manifest_file=//p' "$build_output" | tail -n1)
upload_tsv=$(sed -n 's/^upload_tsv=//p' "$build_output" | tail -n1)
[[ -f "$manifest" && -f "$upload_tsv" ]] || fail 'build step did not publish manifest/upload outputs'
[[ "$(jq 'length' "$manifest")" == 4 ]] || fail 'smoke manifest does not contain exactly four selected APK entries'
[[ "$(jq '[.[] | select(.destination == "release")] | length' "$manifest")" == 2 ]] || fail 'release destination must contain exactly the primary and bridge release APKs'
[[ "$(jq '[.[] | select(.destination == "workflow_artifacts" and (.variant | endswith("_debug")))] | length' "$manifest")" == 2 ]] || fail 'debug companions must remain workflow artifacts when debug publication is off'
if jq -e 'any(.[]; ((.variant | endswith("_debug")) and .destination != "workflow_artifacts") or ((.variant | endswith("_debug") | not) and .destination != "release"))' "$manifest" >/dev/null; then
  fail 'selected APK destination mapping is inconsistent with the Manual Release default'
fi
while IFS=$'\t' read -r name path; do
  [[ -n "$name" ]] || continue
  [[ -f "$path" ]] || fail "planned staged file is missing: $name"
done < "$upload_tsv"

log 'running final new-release source contracts before any publication boundary'
if ! bash scripts/manual-release/13-validate-new-release-source-contracts.sh; then
  fail 'new-release source contract validation failed after exact packaging'
fi

printf 'SUCCESS: Manual Release exact signed Topway + LSPosed release/debug packaging smoke passed for %s\n' "$release_tag"
