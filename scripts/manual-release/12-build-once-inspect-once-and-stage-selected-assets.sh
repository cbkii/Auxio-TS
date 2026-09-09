#!/usr/bin/env bash
# Build, inspect and stage selected three-variant release assets exactly once per logical target.
set -euo pipefail
release_dir="${RUNNER_TEMP}/selected-release-assets"
debug_dir="${RUNNER_TEMP}/selected-debug-artifacts"
mkdir -p "$release_dir" "$debug_dir"
manifest_ndjson="${RUNNER_TEMP}/release-manifest.ndjson"
manifest_json="${release_dir}/release-manifest.json"
staged_tsv="${RUNNER_TEMP}/staged-assets.tsv"
upload_tsv="${RUNNER_TEMP}/release-upload.tsv"
: > "$manifest_ndjson"; : > "$staged_tsv"

if [[ -s "${BUILD_VARIANTS_FILE}" ]]; then
  sdk_root="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
  tools="$(find "$sdk_root/build-tools" -mindepth 1 -maxdepth 1 -type d | sort -V | tail -n1)"
  apksigner="$tools/apksigner"; aapt="$tools/aapt"; keytool="$JAVA_HOME/bin/keytool"
  [[ -x "$apksigner" && -x "$aapt" ]] || { echo "::error::Required Android build tools are unavailable under ${tools}."; exit 1; }
  source "${TOOLS_DIR}/lib/apksigner-certificate.sh"

  expected_release_signer=''
  if [[ "${NEEDS_SIGNING}" == true ]]; then
    [[ -x "$keytool" ]] || { echo "::error::keytool is unavailable."; exit 1; }
    expected_release_signer="$("$keytool" -exportcert -keystore "$ORG_GRADLE_PROJECT_releaseStoreFile" -storepass "$ORG_GRADLE_PROJECT_releaseStorePassword" -alias "$ORG_GRADLE_PROJECT_releaseKeyAlias" | sha256sum | awk '{print toupper($1)}')"
    [[ "$expected_release_signer" =~ ^[0-9A-F]{64}$ ]] || { echo "::error::Unable to derive configured release signer."; exit 1; }
  fi

  find_apk() {
    local dir=$1 found=()
    mapfile -t found < <(find "$dir" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' | sort)
    ((${#found[@]} == 1)) || { echo "::error::Expected one signed APK in ${dir}, found ${#found[@]}." >&2; return 1; }
    printf '%s\n' "${found[0]}"
  }
  signer_for_apk() {
    local report signer
    report="$("$apksigner" verify --verbose --print-certs "$1" 2>&1)" || return 1
    signer="$(extract_apksigner_certificate_sha256 "$report")" || return 1
    [[ "$signer" =~ ^[0-9A-F]{64}$ ]] || return 1
    printf '%s\n' "$signer"
  }
  inspect_apk() {
    local apk=$1 badging
    badging="$("$aapt" dump badging "$apk")" || return 1
    APK_PACKAGE="$(sed -n -E "s/^package: name='([^']+)'.*/\1/p" <<<"$badging")"
    APK_CODE="$(sed -n -E "s/^package: .*versionCode='([^']+)'.*/\1/p" <<<"$badging")"
    APK_NAME="$(sed -n -E "s/^package: .*versionName='([^']+)'.*/\1/p" <<<"$badging")"
    APK_MIN="$(sed -n -E "s/^sdkVersion:'([^']+)'.*/\1/p" <<<"$badging")"
    APK_TARGET="$(sed -n -E "s/^targetSdkVersion:'([^']+)'.*/\1/p" <<<"$badging")"
    APK_ABIS="$(unzip -Z1 "$apk" | sed -n -E 's#^lib/([^/]+)/.*#\1#p' | sort -u | paste -sd, -)"
    APK_SIGNING_REPORT="$("$apksigner" verify --verbose --print-certs "$apk" 2>&1)" || return 1
    APK_SIGNER="$(extract_apksigner_certificate_sha256 "$APK_SIGNING_REPORT")" || return 1
  }

  standard_release=''; standard_debug=''; twmedia_release=''; twmedia_debug=''; twmusic_release=''
  ensure_variant() {
    local key=$1 task=$2 dir=$3 resolved
    case "$key" in
      standard_release) [[ -n "$standard_release" ]] && return; timeout 45m bash ./scripts/ci-gradle.sh "$task"; resolved="$(find_apk "$dir")"; standard_release="$resolved" ;;
      standard_debug) [[ -n "$standard_debug" ]] && return; timeout 45m bash ./scripts/ci-gradle.sh "$task"; resolved="$(find_apk "$dir")"; standard_debug="$resolved" ;;
      twmedia_release) [[ -n "$twmedia_release" ]] && return; timeout 45m bash ./scripts/ci-gradle.sh "$task"; resolved="$(find_apk "$dir")"; twmedia_release="$resolved" ;;
      twmedia_debug) [[ -n "$twmedia_debug" ]] && return; timeout 45m bash ./scripts/ci-gradle.sh "$task"; resolved="$(find_apk "$dir")"; twmedia_debug="$resolved" ;;
      twmusic_release) [[ -n "$twmusic_release" ]] && return; timeout 45m bash ./scripts/ci-gradle.sh "$task"; resolved="$(find_apk "$dir")"; twmusic_release="$resolved" ;;
      *) echo "::error::Unknown internal variant key ${key}."; exit 1 ;;
    esac
  }

  stage_apk() {
    local variant=$1 apk_path=$2 asset_name=$3 asset_kind=$4 destination=$5 expected_package=$6 asset_dir digest
    if [[ "$destination" == workflow_artifacts ]]; then asset_dir="$debug_dir"; else asset_dir="$release_dir"; fi
    local asset_path="$asset_dir/$asset_name"
    cp "$apk_path" "$asset_path"
    inspect_apk "$apk_path" || { echo "::error::Unable to inspect ${asset_name}."; exit 1; }
    [[ "$APK_PACKAGE" == "$expected_package" ]] || { echo "::error::${variant} package expected ${expected_package}, got ${APK_PACKAGE}."; exit 1; }
    digest="$(sha256sum "$asset_path" | awk '{print $1}')"
    printf '%s  %s\n' "$digest" "$asset_name" > "$asset_dir/$asset_name.sha256"
    {
      echo "asset=$asset_name"; echo "asset_kind=$asset_kind"; echo "asset_sha256=$digest"
      echo "source_commit=$RELEASE_SHA"; echo "release_tag=$RELEASE_TAG"; echo "application_id=$APK_PACKAGE"
      echo "version_code=$APK_CODE"; echo "version_name=$APK_NAME"; echo "min_sdk=$APK_MIN"; echo "target_sdk=$APK_TARGET"; echo "abis=$APK_ABIS"
      echo; echo '[apksigner certificates]'; printf '%s\n' "$APK_SIGNING_REPORT"
    } > "$asset_dir/$asset_name.metadata.txt"
    jq -nc --arg filename "$asset_name" --arg variant "$variant" --arg asset_kind "$asset_kind" --arg sha256 "$digest" \
      --arg application_id "$APK_PACKAGE" --arg version_name "$APK_NAME" --argjson version_code "$APK_CODE" --arg signer_sha256 "$APK_SIGNER" \
      --arg source_commit "$RELEASE_SHA" --arg release_tag "$RELEASE_TAG" --arg destination "$destination" \
      '{filename:$filename,variant:$variant,asset_kind:$asset_kind,sha256:$sha256,application_id:$application_id,version_name:$version_name,version_code:$version_code,signer_sha256:$signer_sha256,source_commit:$source_commit,release_tag:$release_tag,destination:$destination}' >> "$manifest_ndjson"
    printf '%s\t%s\n' "$asset_name" "$asset_path" >> "$staged_tsv"
    printf '%s\t%s\n' "$asset_name.sha256" "$asset_dir/$asset_name.sha256" >> "$staged_tsv"
    printf '%s\t%s\n' "$asset_name.metadata.txt" "$asset_dir/$asset_name.metadata.txt" >> "$staged_tsv"

    if [[ "$destination" == release ]]; then
      APKSIGNER_BIN="$apksigner" bash ./scripts/check-app-release-contracts.sh --apk "$asset_path" --expected-package "$expected_package" \
        --version-name "$VERSION_NAME" --version-code "$VERSION_CODE" --expected-signer "$expected_release_signer" \
        --sha256-file "$asset_dir/$asset_name.sha256" --metadata-file "$asset_dir/$asset_name.metadata.txt"
      bash ./scripts/check-release-diagnostics-boundary.sh "$asset_path"
      if [[ "$variant" == topway_twmedia ]]; then bash ./scripts/check-startup-performance-contracts.sh "$asset_path"; fi
    fi
  }

  stage_magisk() {
    ensure_variant twmusic_release :app:assembleTopwayTwMusicRelease app/build/outputs/apk/topwayTwMusic/release
    inspect_apk "$twmusic_release" || { echo "::error::Unable to inspect internal topwayTwMusic APK."; exit 1; }
    [[ "$APK_PACKAGE" == com.tw.music ]] || { echo "::error::Internal exact-package APK is ${APK_PACKAGE}, expected com.tw.music."; exit 1; }
    [[ "$APK_NAME" == "$VERSION_NAME" && "$APK_CODE" == "$VERSION_CODE" ]] || { echo "::error::Internal topwayTwMusic version mismatch."; exit 1; }
    [[ "$APK_SIGNER" == "$expected_release_signer" ]] || { echo "::error::Internal topwayTwMusic signer mismatch."; exit 1; }
    local asset_name="Auxio-TS-${RELEASE_TAG}-topway-twmusic-magisk.zip" asset_path="$release_dir/$asset_name"
    AAPT_BIN="$aapt" bash scripts/package-topway-twmusic-magisk-module.sh --apk "$twmusic_release" --output "$asset_path" --version "$VERSION_NAME" --version-code "$VERSION_CODE"
    local digest; digest="$(sha256sum "$asset_path" | awk '{print $1}')"
    printf '%s  %s\n' "$digest" "$asset_name" > "$release_dir/$asset_name.sha256"
    {
      echo "asset=$asset_name"; echo 'asset_kind=systemless-magisk-exact-package'; echo "asset_sha256=$digest"
      echo "source_commit=$RELEASE_SHA"; echo "release_tag=$RELEASE_TAG"; echo 'application_id=com.tw.music'
      echo "version_code=$APK_CODE"; echo "version_name=$APK_NAME"; echo "min_sdk=$APK_MIN"; echo "target_sdk=$APK_TARGET"; echo "abis=$APK_ABIS"
      echo 'systemless_target=/system/priv-app/com.tw.music_a41e/com.tw.music_a41e.apk'
      echo 'authority=filesystem-overlay-only; no platform signing, UID 1000, signature permissions or private-vendor authority'
      echo; echo '[embedded apk apksigner certificates]'; printf '%s\n' "$APK_SIGNING_REPORT"
    } > "$release_dir/$asset_name.metadata.txt"
    jq -nc --arg filename "$asset_name" --arg sha256 "$digest" --arg version_name "$APK_NAME" --argjson version_code "$APK_CODE" \
      --arg signer_sha256 "$APK_SIGNER" --arg source_commit "$RELEASE_SHA" --arg release_tag "$RELEASE_TAG" \
      '{filename:$filename,variant:"topway_twmusic_magisk",asset_kind:"systemless-magisk-exact-package",sha256:$sha256,application_id:"com.tw.music",version_name:$version_name,version_code:$version_code,signer_sha256:$signer_sha256,source_commit:$source_commit,release_tag:$release_tag,destination:"release"}' >> "$manifest_ndjson"
    printf '%s\t%s\n' "$asset_name" "$asset_path" >> "$staged_tsv"
    printf '%s\t%s\n' "$asset_name.sha256" "$release_dir/$asset_name.sha256" >> "$staged_tsv"
    printf '%s\t%s\n' "$asset_name.metadata.txt" "$release_dir/$asset_name.metadata.txt" >> "$staged_tsv"
  }

  while IFS= read -r variant; do
    [[ -n "$variant" ]] || continue
    case "$variant" in
      standard)
        ensure_variant standard_release :app:assembleStandardRelease app/build/outputs/apk/standard/release
        stage_apk standard "$standard_release" "Auxio-TS-${RELEASE_TAG}-standard-release.apk" signed-apk release org.oxycblt.auxio ;;
      standard_debug)
        ensure_variant standard_debug :app:assembleStandardDebug app/build/outputs/apk/standard/debug
        stage_apk standard_debug "$standard_debug" "Auxio-TS-${RELEASE_TAG}-standard-debug.apk" debug-diagnostics-apk "$DEBUG_DESTINATION" org.oxycblt.auxio.debug ;;
      topway_twmedia)
        ensure_variant twmedia_release :app:assembleTopwayTwMediaRelease app/build/outputs/apk/topwayTwMedia/release
        stage_apk topway_twmedia "$twmedia_release" "Auxio-TS-${RELEASE_TAG}-topway-twmedia-release.apk" signed-apk release com.tw.media ;;
      topway_twmedia_debug)
        ensure_variant twmedia_debug :app:assembleTopwayTwMediaDebug app/build/outputs/apk/topwayTwMedia/debug
        stage_apk topway_twmedia_debug "$twmedia_debug" "Auxio-TS-${RELEASE_TAG}-topway-twmedia-debug.apk" debug-diagnostics-apk "$DEBUG_DESTINATION" com.tw.media.debug ;;
      topway_twmusic_magisk) stage_magisk ;;
      lsposed_bridge)
        ensure_variant twmedia_release :app:assembleTopwayTwMediaRelease app/build/outputs/apk/topwayTwMedia/release
        paired_signer="$(signer_for_apk "$twmedia_release")" || { echo "::error::Unable to inspect paired TopwayTwMedia signer."; exit 1; }
        timeout 45m bash ./scripts/ci-gradle.sh :lsposed-bridge:assembleRelease -PexpectedTargetSigner="$paired_signer"
        bridge_apk="$(find_apk lsposed-bridge/build/outputs/apk/release)"
        stage_apk lsposed_bridge "$bridge_apk" "Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge.apk" signed-lsposed-api100-addon release org.oxycblt.auxio.ts18bridge
        EXPECTED_SIGNER_SHA256="$expected_release_signer" APKSIGNER_BIN="$apksigner" bash ./scripts/check-lsposed-bridge-contracts.sh --variant release --apk "$bridge_apk" --target-apk "$twmedia_release" ;;
      lsposed_bridge_debug)
        ensure_variant twmedia_debug :app:assembleTopwayTwMediaDebug app/build/outputs/apk/topwayTwMedia/debug
        paired_signer="$(signer_for_apk "$twmedia_debug")" || { echo "::error::Unable to inspect paired debug target signer."; exit 1; }
        timeout 45m bash ./scripts/ci-gradle.sh :lsposed-bridge:assembleDebug -PexpectedTargetSigner="$paired_signer"
        bridge_apk="$(find_apk lsposed-bridge/build/outputs/apk/debug)"
        stage_apk lsposed_bridge_debug "$bridge_apk" "Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge-debug.apk" debug-lsposed-api100-addon "$DEBUG_DESTINATION" org.oxycblt.auxio.ts18bridge.debug
        APKSIGNER_BIN="$apksigner" bash ./scripts/check-lsposed-bridge-contracts.sh --variant debug --apk "$bridge_apk" --target-apk "$twmedia_debug" ;;
      *) echo "::error::Unknown selected variant during build: ${variant}"; exit 1 ;;
    esac
  done < "$BUILD_VARIANTS_FILE"
fi

if [[ -s "$manifest_ndjson" ]]; then jq -s '.' "$manifest_ndjson" > "$manifest_json"; else printf '[]\n' > "$manifest_json"; fi
cp "$ASSET_PLAN_FILE" "$release_dir/asset-plan.json"
cp "$RELEASE_PLAN_FILE" "$release_dir/release-plan.json"
python3 "$ASSET_TOOL" validate-manifest --manifest "$manifest_json" --expected-built-names-file "$BUILD_ASSET_NAMES_FILE" \
  --version-name "$VERSION_NAME" --version-code "$VERSION_CODE" --release-tag "$RELEASE_TAG" --source-commit "$RELEASE_SHA" --debug-destination "$DEBUG_DESTINATION"

python3 - "$staged_tsv" "$UPLOAD_NAMES_FILE" "$upload_tsv" <<'PY'
import sys
from pathlib import Path
staged_path, names_path, output_path = map(Path, sys.argv[1:])
staged = {}
for line in staged_path.read_text(encoding='utf-8').splitlines():
    if line:
        name, path = line.split('\t', 1); staged[name] = path
requested = [line for line in names_path.read_text(encoding='utf-8').splitlines() if line]
missing = [name for name in requested if name not in staged]
if missing: raise SystemExit(f"Planned upload assets were not staged: {missing}")
if any(name.lower().endswith('.apk') and 'topway-twmusic' in name.lower() for name in requested):
    raise SystemExit('Raw topwayTwMusic APK publication is forbidden.')
output_path.write_text(''.join(f"{name}\t{staged[name]}\n" for name in requested), encoding='utf-8')
PY

{
  echo "release_artifact_dir=$release_dir"; echo "debug_artifact_dir=$debug_dir"; echo "manifest_file=$manifest_json"; echo "upload_tsv=$upload_tsv"; echo "staged_tsv=$staged_tsv"
} >> "$GITHUB_OUTPUT"
echo "SUCCESS: selected three-variant Manual Release assets were built, inspected and staged." >&2
