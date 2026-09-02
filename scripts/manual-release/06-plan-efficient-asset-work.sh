#!/usr/bin/env bash
set -euo pipefail

[[ -f "${TARGET_RELEASE_FILE}" ]] || {
  echo "::error::Target release state file is missing: ${TARGET_RELEASE_FILE}"
  exit 1
}

plan_file="${RUNNER_TEMP}/asset-plan.json"
python3 "${TOOL}" plan-assets \
  --mode "${RELEASE_MODE}" \
  --release-tag "${RELEASE_TAG}" \
  --selected-variants-file "${SELECTED_FILE}" \
  --debug-destination "${DEBUG_DESTINATION}" \
  --existing-assets-file "${EXISTING_ASSETS_FILE}" \
  --replace "${REPLACE}" \
  --output "${plan_file}"

target_exists="$(jq -r 'has("id")' "${TARGET_RELEASE_FILE}")"
target_draft="$(jq -r 'if has("id") then .draft else true end' "${TARGET_RELEASE_FILE}")"
upload_count="$(jq '.upload_names | length' "${plan_file}")"
if [[ "${target_exists}" == true && "${target_draft}" != true && "${upload_count}" -gt 0 ]]; then
  echo "::error::Release ${RELEASE_TAG} is already published. Published APK/checksum/metadata assets are immutable in Manual Release; create a new patch release instead."
  exit 1
fi

build_variants="${RUNNER_TEMP}/build-variants.txt"
build_apk_names="${RUNNER_TEMP}/build-apk-names.txt"
upload_names="${RUNNER_TEMP}/upload-asset-names.txt"
replace_names="${RUNNER_TEMP}/replace-asset-names.txt"
verify_names="${RUNNER_TEMP}/verify-asset-names.txt"
debug_workflow_names="${RUNNER_TEMP}/debug-workflow-asset-names.txt"
jq -r '.build_variants[]' "${plan_file}" > "${build_variants}"
jq -r '.build_apk_names[]' "${plan_file}" > "${build_apk_names}"
jq -r '.upload_names[]' "${plan_file}" > "${upload_names}"
jq -r '.replace_names[]' "${plan_file}" > "${replace_names}"
jq -r '.verify_names[]' "${plan_file}" > "${verify_names}"
jq -r '.debug_workflow_names[]' "${plan_file}" > "${debug_workflow_names}"
{
  echo "plan_file=${plan_file}"
  echo "build_variants_file=${build_variants}"
  echo "build_apk_names_file=${build_apk_names}"
  echo "upload_names_file=${upload_names}"
  echo "replace_names_file=${replace_names}"
  echo "verify_names_file=${verify_names}"
  echo "debug_workflow_names_file=${debug_workflow_names}"
  echo "build_count=$(wc -l < "${build_variants}" | tr -d ' ')"
  echo "needs_signing=$(jq -r .needs_signing "${plan_file}")"
  echo "target_release_draft=${target_draft}"
} >> "${GITHUB_OUTPUT}"
