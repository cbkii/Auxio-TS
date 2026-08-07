#!/usr/bin/env bash
# shellcheck disable=SC2154,SC2129 # GitHub supplies signing env; staged records append intentionally.
set -euo pipefail
release_dir="${RUNNER_TEMP}/selected-release-assets"
debug_dir="${RUNNER_TEMP}/selected-debug-artifacts"
mkdir -p "${release_dir}" "${debug_dir}"
manifest_ndjson="${RUNNER_TEMP}/release-manifest.ndjson"
manifest_json="${release_dir}/release-manifest.json"
staged_tsv="${RUNNER_TEMP}/staged-assets.tsv"
upload_tsv="${RUNNER_TEMP}/release-upload.tsv"
: > "${manifest_ndjson}"
: > "${staged_tsv}"

if [[ -s "${BUILD_VARIANTS_FILE}" ]]; then
  sdk_root="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
  tools="$(find "${sdk_root}/build-tools" -mindepth 1 -maxdepth 1 -type d | sort -V | tail -n1)"
  apksigner="${tools}/apksigner"
  aapt="${tools}/aapt"
  keytool="${JAVA_HOME}/bin/keytool"
  [[ -x "${apksigner}" && -x "${aapt}" ]] || {
    echo "::error::Required Android build tools are unavailable under ${tools}."
    exit 1
  }
  # shellcheck source=/dev/null
  source "${TOOLS_DIR}/lib/apksigner-certificate.sh"

  expected_release_signer=''
  if [[ "${NEEDS_SIGNING}" == true ]]; then
    [[ -x "${keytool}" ]] || { echo "::error::keytool is unavailable."; exit 1; }
    expected_release_signer="$(
      "${keytool}" -exportcert \
        -keystore "${ORG_GRADLE_PROJECT_releaseStoreFile}" \
        -storepass "${ORG_GRADLE_PROJECT_releaseStorePassword}" \
        -alias "${ORG_GRADLE_PROJECT_releaseKeyAlias}" |
        sha256sum | awk '{print toupper($1)}'
    )"
    [[ "${expected_release_signer}" =~ ^[0-9A-F]{64}$ ]] || {
      echo "::error::Unable to derive the configured release signer fingerprint."
      exit 1
    }
  fi

  find_apk() {
    local dir=$1 found=()
    mapfile -t found < <(find "${dir}" -maxdepth 1 -type f -name '*.apk' ! -name '*unsigned*' | sort)
    ((${#found[@]} == 1)) || {
      echo "::error::Expected one signed APK in ${dir}, found ${#found[@]}."
      exit 1
    }
    printf '%s\n' "${found[0]}"
  }

  signer_for_apk() {
    local apk=$1 report signer
    report="$("${apksigner}" verify --verbose --print-certs "${apk}" 2>&1)" || {
      echo "::error::Signature verification failed for paired target ${apk}." >&2
      return 1
    }
    signer="$(extract_apksigner_certificate_sha256 "${report}")" || {
      echo "::error::Unable to resolve exactly one signer for paired target ${apk}." >&2
      return 1
    }
    [[ "${signer}" =~ ^[0-9A-F]{64}$ ]] || {
      echo "::error::Malformed paired target signer for ${apk}." >&2
      return 1
    }
    printf '%s\n' "${signer}"
  }

  twmedia_release_apk=''
  twmedia_debug_apk=''
  ensure_twmedia_release() {
    if [[ -z "${twmedia_release_apk}" ]]; then
      timeout 45m bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMediaRelease
      twmedia_release_apk="$(find_apk app/build/outputs/apk/topwayTwMedia/release)"
    fi
  }
  ensure_twmedia_debug() {
    if [[ -z "${twmedia_debug_apk}" ]]; then
      timeout 45m bash ./scripts/ci-gradle.sh :app:assembleTopwayTwMediaDebug
      twmedia_debug_apk="$(find_apk app/build/outputs/apk/topwayTwMedia/debug)"
    fi
  }

  while IFS= read -r variant; do
    [[ -n "${variant}" ]] || continue
    paired_target_apk=''
    case "${variant}" in
      topway_twmedia)
        ensure_twmedia_release
        apk_path="${twmedia_release_apk}"
        asset_name="Auxio-TS-${RELEASE_TAG}-topway-twmedia-release.apk"
        asset_kind='signed-apk'
        destination='release'
        ;;
      topway_twmedia_debug)
        ensure_twmedia_debug
        apk_path="${twmedia_debug_apk}"
        asset_name="Auxio-TS-${RELEASE_TAG}-topway-twmedia-debug.apk"
        asset_kind='debug-diagnostics-apk'
        destination="${DEBUG_DESTINATION}"
        ;;
      lsposed_bridge)
        ensure_twmedia_release
        paired_target_apk="${twmedia_release_apk}"
        paired_target_signer="$(signer_for_apk "${paired_target_apk}")"
        timeout 45m bash ./scripts/ci-gradle.sh \
          :lsposed-bridge:assembleRelease \
          -PexpectedTargetSigner="${paired_target_signer}"
        apk_path="$(find_apk lsposed-bridge/build/outputs/apk/release)"
        asset_name="Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge.apk"
        asset_kind='signed-lsposed-api100-addon'
        destination='release'
        ;;
      lsposed_bridge_debug)
        ensure_twmedia_debug
        paired_target_apk="${twmedia_debug_apk}"
        paired_target_signer="$(signer_for_apk "${paired_target_apk}")"
        timeout 45m bash ./scripts/ci-gradle.sh \
          :lsposed-bridge:assembleDebug \
          -PexpectedTargetSigner="${paired_target_signer}"
        apk_path="$(find_apk lsposed-bridge/build/outputs/apk/debug)"
        asset_name="Auxio-TS-${RELEASE_TAG}-lsposed-api100-bridge-debug.apk"
        asset_kind='debug-lsposed-api100-addon'
        destination="${DEBUG_DESTINATION}"
        ;;
      *) echo "::error::Unknown selected variant during build: ${variant}"; exit 1 ;;
    esac

    if [[ "${destination}" == workflow_artifacts ]]; then asset_dir="${debug_dir}"; else asset_dir="${release_dir}"; fi
    asset_path="${asset_dir}/${asset_name}"
    cp "${apk_path}" "${asset_path}"

    digest="$(sha256sum "${asset_path}" | awk '{print $1}')"
    badging="$("${aapt}" dump badging "${apk_path}")"
    signing_report="$("${apksigner}" verify --verbose --print-certs "${apk_path}" 2>&1)" || {
      echo "::error::APK signature verification failed for ${asset_name}."; exit 1;
    }
    signer="$(extract_apksigner_certificate_sha256 "${signing_report}")" || {
      echo "::error::Unable to resolve exactly one signer for ${asset_name}."; exit 1;
    }
    application_id="$(sed -n -E "s/^package: name='([^']+)'.*/\1/p" <<< "${badging}" | head -n1)"
    actual_code="$(sed -n -E "s/^package: .*versionCode='([^']+)'.*/\1/p" <<< "${badging}" | head -n1)"
    actual_name="$(sed -n -E "s/^package: .*versionName='([^']+)'.*/\1/p" <<< "${badging}" | head -n1)"
    min_sdk="$(sed -n -E "s/^sdkVersion:'([^']+)'.*/\1/p" <<< "${badging}" | head -n1)"
    target_sdk="$(sed -n -E "s/^targetSdkVersion:'([^']+)'.*/\1/p" <<< "${badging}" | head -n1)"
    abis="$(unzip -Z1 "${apk_path}" | sed -n -E 's#^lib/([^/]+)/.*#\1#p' | sort -u | paste -sd, -)"

    printf '%s  %s\n' "${digest}" "${asset_name}" > "${asset_dir}/${asset_name}.sha256"
    {
      echo "asset=${asset_name}"
      echo "asset_kind=${asset_kind}"
      echo "asset_sha256=${digest}"
      echo "source_commit=${RELEASE_SHA}"
      echo "release_tag=${RELEASE_TAG}"
      echo "application_id=${application_id}"
      echo "version_code=${actual_code}"
      echo "version_name=${actual_name}"
      echo "min_sdk=${min_sdk}"
      echo "target_sdk=${target_sdk}"
      echo "abis=${abis}"
      echo
      echo '[apksigner certificates]'
      printf '%s\n' "${signing_report}"
    } > "${asset_dir}/${asset_name}.metadata.txt"

    case "${variant}" in
      topway_twmedia)
        for checker in scripts/check-release-diagnostics-boundary.sh scripts/check-startup-performance-contracts.sh scripts/check-app-release-contracts.sh; do
          [[ -x "${checker}" ]] || { echo "::error::Immutable source lacks required release checker ${checker}."; exit 1; }
        done
        bash ./scripts/check-release-diagnostics-boundary.sh "${asset_path}"
        bash ./scripts/check-startup-performance-contracts.sh "${asset_path}"
        APKSIGNER_BIN="${apksigner}" bash ./scripts/check-app-release-contracts.sh \
          --apk "${asset_path}" --version-name "${VERSION_NAME}" --version-code "${VERSION_CODE}" \
          --expected-signer "${expected_release_signer}" \
          --sha256-file "${asset_dir}/${asset_name}.sha256" \
          --metadata-file "${asset_dir}/${asset_name}.metadata.txt"
        ;;
      lsposed_bridge)
        [[ -x scripts/check-lsposed-bridge-contracts.sh ]] || { echo "::error::Missing LSPosed checker."; exit 1; }
        EXPECTED_SIGNER_SHA256="${expected_release_signer}" APKSIGNER_BIN="${apksigner}" \
          bash ./scripts/check-lsposed-bridge-contracts.sh \
            --variant release --apk "${asset_path}" --target-apk "${paired_target_apk}"
        ;;
      lsposed_bridge_debug)
        [[ -x scripts/check-lsposed-bridge-contracts.sh ]] || { echo "::error::Missing LSPosed checker."; exit 1; }
        APKSIGNER_BIN="${apksigner}" bash ./scripts/check-lsposed-bridge-contracts.sh \
          --variant debug --apk "${asset_path}" --target-apk "${paired_target_apk}"
        ;;
    esac

    jq -nc \
      --arg filename "${asset_name}" --arg variant "${variant}" --arg asset_kind "${asset_kind}" \
      --arg sha256 "${digest}" --arg application_id "${application_id}" --arg version_name "${actual_name}" \
      --argjson version_code "${actual_code}" --arg signer_sha256 "${signer}" --arg source_commit "${RELEASE_SHA}" \
      --arg release_tag "${RELEASE_TAG}" --arg destination "${destination}" \
      '{filename:$filename,variant:$variant,asset_kind:$asset_kind,sha256:$sha256,application_id:$application_id,version_name:$version_name,version_code:$version_code,signer_sha256:$signer_sha256,source_commit:$source_commit,release_tag:$release_tag,destination:$destination}' \
      >> "${manifest_ndjson}"

    printf '%s\t%s\n' "${asset_name}" "${asset_path}" >> "${staged_tsv}"
    printf '%s\t%s\n' "${asset_name}.sha256" "${asset_dir}/${asset_name}.sha256" >> "${staged_tsv}"
    printf '%s\t%s\n' "${asset_name}.metadata.txt" "${asset_dir}/${asset_name}.metadata.txt" >> "${staged_tsv}"
  done < "${BUILD_VARIANTS_FILE}"
fi

if [[ -s "${manifest_ndjson}" ]]; then jq -s '.' "${manifest_ndjson}" > "${manifest_json}"; else printf '[]\n' > "${manifest_json}"; fi
cp "${ASSET_PLAN_FILE}" "${release_dir}/asset-plan.json"
cp "${RELEASE_PLAN_FILE}" "${release_dir}/release-plan.json"

python3 "${TOOLS_DIR}/release-orchestrator.py" validate-manifest \
  --manifest "${manifest_json}" --expected-built-names-file "${BUILD_APK_NAMES_FILE}" \
  --version-name "${VERSION_NAME}" --version-code "${VERSION_CODE}" --release-tag "${RELEASE_TAG}" \
  --source-commit "${RELEASE_SHA}" --debug-destination "${DEBUG_DESTINATION}"

python3 - "${staged_tsv}" "${UPLOAD_NAMES_FILE}" "${upload_tsv}" <<'PY'
import sys
from pathlib import Path
staged_path, names_path, output_path = map(Path, sys.argv[1:])
staged = {}
for line in staged_path.read_text(encoding='utf-8').splitlines():
    if line:
        name, path = line.split('\t', 1)
        staged[name] = path
requested = [line for line in names_path.read_text(encoding='utf-8').splitlines() if line]
missing = [name for name in requested if name not in staged]
if missing:
    raise SystemExit(f"Planned upload assets were not staged: {missing}")
output_path.write_text(''.join(f"{name}\t{staged[name]}\n" for name in requested), encoding='utf-8')
PY

{
  echo "release_artifact_dir=${release_dir}"
  echo "debug_artifact_dir=${debug_dir}"
  echo "manifest_file=${manifest_json}"
  echo "upload_tsv=${upload_tsv}"
  echo "staged_tsv=${staged_tsv}"
} >> "${GITHUB_OUTPUT}"
