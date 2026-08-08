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

for command in bash base64 git jq keytool python3 sha256sum; do
  command -v "$command" >/dev/null 2>&1 || fail "required command is unavailable: $command"
done

runner_temp=${RUNNER_TEMP:-}
[[ -n "$runner_temp" && -d "$runner_temp" ]] || fail 'RUNNER_TEMP must identify an existing private runner directory'
work=$(mktemp -d "${runner_temp}/manual-release-packaging-smoke.XXXXXX") || fail 'cannot create temporary work directory'
cleanup() {
  rc=$?
  rm -rf -- "$work"
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

# Resolve a clean create transaction directly from current source metadata with no external state.
: > "${work}/git-tags.txt"
: > "${work}/release-tags.txt"
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
  fail 'clean create transaction planning failed'
fi
release_tag=$(jq -r .release_tag "$release_plan")
version_name=$(jq -r .release_version_name "$release_plan")
version_code=$(jq -r .release_version_code "$release_plan")
[[ "$release_tag" == "v${version_name}" && "$version_code" =~ ^[0-9]+$ ]] || fail 'release plan returned malformed version metadata'
release_sha=${GITHUB_SHA:-}
[[ "$release_sha" =~ ^[0-9a-fA-F]{40}$ ]] || release_sha=$(git rev-parse HEAD 2>/dev/null) || fail 'cannot resolve smoke source commit'
[[ "$release_sha" =~ ^[0-9a-fA-F]{40}$ ]] || fail 'smoke source commit is malformed'

# Build both maintained release paths. The bridge-only selection still builds its paired target
# internally, so this also proves the optional addon path without changing publication state.
printf '%s\n' topway_twmedia lsposed_bridge > "${work}/selected-release-variants.txt"
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
[[ "$(wc -l < "${work}/build-variants.txt" | tr -d ' ')" == 2 ]] || fail 'smoke plan must build exactly the primary and LSPosed release APKs'

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
[[ "$(jq 'length' "$manifest")" == 2 ]] || fail 'smoke manifest does not contain exactly two release APK entries'
if jq -e 'any(.[]; (.variant | endswith("_debug")) or .destination != "release")' "$manifest" >/dev/null; then
  fail 'release-only smoke staged a debug or non-release APK'
fi
while IFS=$'\t' read -r name path; do
  [[ -n "$name" ]] || continue
  [[ -f "$path" ]] || fail "planned staged file is missing: $name"
done < "$upload_tsv"

printf 'SUCCESS: Manual Release exact signed Topway + LSPosed release packaging smoke passed for %s\n' "$release_tag"
