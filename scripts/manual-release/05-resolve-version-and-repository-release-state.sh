#!/usr/bin/env bash
set -euo pipefail
case "${RELEASE_MODE}" in
  auto|create_new_release|repair_existing_release) ;;
  *)
    echo "::error::Unsupported release mode: ${RELEASE_MODE}" >&2
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
      echo "::warning::GitHub read failed (attempt ${attempt}/3); retrying." >&2
      sleep $((attempt * 4))
    fi
  done
  echo "::error::GitHub read failed after 3 bounded attempts." >&2
  return "${rc}"
}

if ! source_sha="$(git rev-parse HEAD 2>/dev/null)"; then
  echo "::error::Current release source did not resolve to a commit." >&2
  exit 1
fi
[[ "${source_sha}" =~ ^[0-9a-f]{40}$ ]] || {
  echo "::error::Current release source did not resolve to a full commit SHA." >&2
  exit 1
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
preliminary_mode="$(jq -r .effective_mode "${preliminary}")"
preliminary_reason="$(jq -r .resolution_reason "${preliminary}")"
selected_tag_sha=""
selected_tag_relation="not_existing"

if [[ "${preliminary_mode}" == repair_existing_release ]]; then
  if ! selected_tag_sha="$(git rev-parse "${release_tag}^{commit}" 2>/dev/null)"; then
    echo "::error::Repair tag ${release_tag} did not resolve to a commit." >&2
    exit 1
  fi
  [[ "${selected_tag_sha}" =~ ^[0-9a-f]{40}$ ]] || {
    echo "::error::Repair tag ${release_tag} did not resolve to a full commit SHA." >&2
    exit 1
  }
  if [[ "${selected_tag_sha}" == "${source_sha}" ]]; then
    selected_tag_relation="source_head"
  else
    selected_parent="$(git rev-parse "${selected_tag_sha}^" 2>/dev/null || true)"
    if [[ "${selected_parent}" == "${source_sha}" ]]; then
      # A create transaction may have produced a release-metadata commit and pushed its tag
      # before the final dev fast-forward. This is the only behind-dev automatic-resume shape
      # that is safe to recognise without explicit user intent.
      selected_tag_relation="source_parent"
    else
      selected_tag_relation="stale"
    fi
  fi

  case "${preliminary_reason}" in
    resume_latest_tag_without_release|resume_latest_draft_release)
      if [[ "${selected_tag_relation}" == stale ]]; then
        echo "::error::Automatic release resume selected ${release_tag} at ${selected_tag_sha}, but current dev is ${source_sha}. Refusing to publish stale source. Explicitly choose ${release_tag} to repair that historical transaction, or leave it untouched and create a newer release from current dev." >&2
        exit 1
      fi
      ;;
  esac
fi

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
      echo "::error::Release ${release_tag} resolved to invalid ID ${release_id}." >&2
      exit 1
    }
    api_read "${target_release}" "repos/${GITHUB_REPOSITORY}/releases/${release_id}"
    jq -e --arg tag "${release_tag}" --arg id "${release_id}" \
      '(.id == ($id | tonumber)) and (.tag_name == $tag)' \
      "${target_release}" >/dev/null || {
        echo "::error::Release ID ${release_id} did not resolve back to ${release_tag}." >&2
        exit 1
      }

    # Refresh the selected target's draft classification from its direct release read before the
    # final plan. If publication changed since the paginated index snapshot, the final resolver
    # must see that transition and either choose a different action or fail the consistency gate.
    target_draft="$(jq -r '.draft' "${target_release}")"
    awk -F '\t' -v tag="${release_tag}" -v draft="${target_draft}" '
      $2 == tag { if (draft == "true") print $2; next }
      $3 == "true" { print $2 }
    ' "${release_index}" > "${draft_release_tags}"
    echo "Found existing GitHub Release ${release_tag} (ID ${release_id}, draft=${target_draft})."
    ;;
  *)
    echo "::error::Multiple GitHub Releases unexpectedly use tag ${release_tag}." >&2
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

[[ "$(jq -r .release_tag "${final_plan}")" == "${release_tag}" ]] || {
  echo "::error::Release plan changed target between repository-state reads." >&2
  exit 1
}
[[ "$(jq -r .effective_mode "${final_plan}")" == "${preliminary_mode}" ]] || {
  echo "::error::Release plan changed mode between repository-state reads." >&2
  exit 1
}

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
  echo "source_sha=${source_sha}"
  echo "selected_tag_sha=${selected_tag_sha}"
  echo "selected_tag_relation=${selected_tag_relation}"
  echo "target_release_draft=$(jq -r 'if has("id") then .draft else true end' "${target_release}")"
} >> "${GITHUB_OUTPUT}"
