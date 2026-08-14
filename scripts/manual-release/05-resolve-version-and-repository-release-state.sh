#!/usr/bin/env bash
set -euo pipefail
case "${RELEASE_MODE}" in
  auto|create_new_release|repair_existing_release) ;;
  *)
    echo "::error::Unsupported release mode: ${RELEASE_MODE}"
    exit 1
    ;;
esac

git_tags="${RUNNER_TEMP}/git-tags.txt"
release_tags="${RUNNER_TEMP}/github-release-tags.txt"
target_release="${RUNNER_TEMP}/target-release.json"
preliminary="${RUNNER_TEMP}/release-plan-preliminary.json"
final_plan="${RUNNER_TEMP}/release-plan.json"

api_read() {
  local output=$1
  shift
  local attempt rc=1
  for attempt in 1 2 3; do
    if timeout 60s gh api "$@" > "${output}.tmp"; then
      mv "${output}.tmp" "${output}"
      return 0
    fi
    rc=$?
    rm -f -- "${output}.tmp"
    if ((attempt < 3)); then
      echo "::warning::GitHub read failed (attempt ${attempt}/3); retrying."
      sleep $((attempt * 4))
    fi
  done
  echo "::error::GitHub read failed after 3 bounded attempts."
  return "${rc}"
}

git tag --list > "${git_tags}"
release_index="${RUNNER_TEMP}/github-releases.tsv"
draft_release_tags="${RUNNER_TEMP}/github-draft-release-tags.txt"
api_read "${release_index}" --paginate \
  "repos/${GITHUB_REPOSITORY}/releases?per_page=100" \
  --jq '.[] | [.id, .tag_name, .draft] | @tsv'
cut -f2 "${release_index}" > "${release_tags}"
awk -F '\t' '$3 == "true" { print $2 }' "${release_index}" > "${draft_release_tags}"
printf '{}\n' > "${target_release}"

python3 "${TOOL}" resolve \
  --mode "${RELEASE_MODE}" \
  --input-tag "${INPUT_TAG}" \
  --source-gradle app/build.gradle \
  --git-tags-file "${git_tags}" \
  --release-tags-file "${release_tags}" \
  --draft-release-tags-file "${draft_release_tags}" \
  --target-release-json "${target_release}" \
  --output "${preliminary}"

release_tag="$(jq -r .release_tag "${preliminary}")"
release_ids="${RUNNER_TEMP}/target-release-ids.txt"
awk -F '\t' -v tag="${release_tag}" '$2 == tag { print $1 }' \
  "${release_index}" > "${release_ids}"
release_count="$(awk 'NF { count += 1 } END { print count + 0 }' "${release_ids}")"
case "${release_count}" in
  0)
    printf '{}\n' > "${target_release}"
    ;;
  1)
    release_id="$(cat "${release_ids}")"
    [[ "${release_id}" =~ ^[0-9]+$ ]] || {
      echo "::error::Release ${release_tag} resolved to invalid ID ${release_id}."
      exit 1
    }
    api_read "${target_release}" "repos/${GITHUB_REPOSITORY}/releases/${release_id}"
    jq -e --arg tag "${release_tag}" --arg id "${release_id}" \
      '(.id == ($id | tonumber)) and (.tag_name == $tag)' \
      "${target_release}" >/dev/null || {
        echo "::error::Release ID ${release_id} did not resolve back to ${release_tag}."
        exit 1
      }
    echo "Found existing GitHub Release ${release_tag} (ID ${release_id})."
    ;;
  *)
    echo "::error::Multiple GitHub Releases unexpectedly use tag ${release_tag}."
    exit 1
    ;;
esac

python3 "${TOOL}" resolve \
  --mode "${RELEASE_MODE}" \
  --input-tag "${INPUT_TAG}" \
  --source-gradle app/build.gradle \
  --git-tags-file "${git_tags}" \
  --release-tags-file "${release_tags}" \
  --draft-release-tags-file "${draft_release_tags}" \
  --target-release-json "${target_release}" \
  --output "${final_plan}"

existing_assets="${RUNNER_TEMP}/existing-release-assets.txt"
jq -r '.assets[]?.name' "${target_release}" > "${existing_assets}"
{
  echo "plan_file=${final_plan}"
  echo "target_release_file=${target_release}"
  echo "existing_assets_file=${existing_assets}"
  echo "release_tag=$(jq -r .release_tag "${final_plan}")"
  echo "release_version_name=$(jq -r .release_version_name "${final_plan}")"
  echo "release_version_code=$(jq -r .release_version_code "${final_plan}")"
  echo "previous_tag=$(jq -r .previous_tag "${final_plan}")"
  echo "tag_exists=$(jq -r .tag_exists "${final_plan}")"
  echo "release_exists=$(jq -r .release_exists "${final_plan}")"
  echo "effective_mode=$(jq -r .effective_mode "${final_plan}")"
  echo "resolution_reason=$(jq -r .resolution_reason "${final_plan}")"
  echo "apply_requested_status=$(jq -r .apply_requested_status "${final_plan}")"
  echo "unresolved_tag_only_versions=$(jq -r '.unresolved_tag_only_versions | join(",")' "${final_plan}")"
  echo "metadata_change_required=$(jq -r .metadata_change_required "${final_plan}")"
  echo "existing_release_url=$(jq -r .target_release_url "${final_plan}")"
} >> "${GITHUB_OUTPUT}"
