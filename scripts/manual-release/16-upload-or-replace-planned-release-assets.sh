#!/usr/bin/env bash
set -euo pipefail
api_read() {
  local output=$1
  shift
  local attempt rc=1
  for attempt in 1 2 3; do
    if timeout 60s gh api "$@" > "${output}.tmp"; then
      mv "${output}.tmp" "${output}"
      return 0
    else
      rc=$?
    fi
    rm -f -- "${output}.tmp"
    ((attempt < 3)) && sleep $((attempt * 4))
  done
  return "${rc}"
}
if [[ ! -s "${UPLOAD_TSV}" ]]; then
  echo 'No remote asset upload is required; selected triplets are already complete.'
  exit 0
fi
[[ "${RELEASE_ID}" =~ ^[0-9]+$ ]] || {
  echo "::error::Release ID is missing or invalid for ${RELEASE_TAG}." >&2
  exit 1
}
[[ -f "${REPLACE_NAMES_FILE}" ]] || {
  echo "::error::Asset replacement plan is missing: ${REPLACE_NAMES_FILE}" >&2
  exit 1
}

duplicate_names="${RUNNER_TEMP}/duplicate-upload-asset-names.txt"
cut -f1 "${UPLOAD_TSV}" | awk 'NF' | sort | uniq -d > "${duplicate_names}"
[[ ! -s "${duplicate_names}" ]] || {
  echo '::error::The upload plan contains duplicate asset names.' >&2
  cat "${duplicate_names}" >&2
  exit 1
}

remote_json="${RUNNER_TEMP}/upload-release.json"
refresh_draft_release() {
  local phase=$1
  RELEASE_REFRESH_PHASE="${phase}" api_read \
    "${remote_json}" "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}" || {
      echo "::error::GitHub release-state read failed before ${phase}." >&2
      return 1
    }
  jq -e --arg tag "${RELEASE_TAG}" --arg id "${RELEASE_ID}" \
    '(.id == ($id | tonumber)) and (.tag_name == $tag)' \
    "${remote_json}" >/dev/null || {
      echo "::error::Release ID ${RELEASE_ID} no longer resolves to ${RELEASE_TAG} before ${phase}." >&2
      return 1
    }
  jq -e '.draft == true' "${remote_json}" >/dev/null || {
    echo "::error::Release ${RELEASE_TAG} is already published before ${phase}. Refusing to add, delete or replace release assets; create a new patch release instead." >&2
    return 1
  }
}

# Manual Release runs are serialized by the workflow concurrency group. Re-read the target release
# at each destructive/upload boundary as the remote precondition; abort immediately if another
# actor has published it since planning.
refresh_draft_release "asset mutation"
upload_template="$(jq -r .upload_url "${remote_json}")"
upload_base="${upload_template%%{*}"
[[ "${upload_base}" == https://* && "${upload_base}" == */releases/${RELEASE_ID}/assets ]] || {
  echo "::error::Release ${RELEASE_ID} returned an invalid upload URL." >&2
  exit 1
}

ensure_asset_absent() {
  local name=$1 asset_id=$2 encoded=$3
  local attempt after_delete matches live_id
  after_delete="${RUNNER_TEMP}/post-delete-${encoded}.json"
  for attempt in 1 2; do
    refresh_draft_release "deleting ${name}"
    mapfile -t live_ids < <(
      jq -r --arg name "${name}" '.assets[] | select(.name == $name) | .id' "${remote_json}"
    )
    if ((${#live_ids[@]} == 0)); then
      return 0
    fi
    ((${#live_ids[@]} == 1)) || {
      echo "::error::Release ${RELEASE_ID} contains duplicate assets named ${name} before deletion." >&2
      return 1
    }
    live_id="${live_ids[0]}"
    [[ "${live_id}" == "${asset_id}" ]] || {
      echo "::error::Asset ${name} changed identity from ${asset_id} to ${live_id}; refusing stale deletion." >&2
      return 1
    }

    if ! timeout 60s gh api --method DELETE \
      "repos/${GITHUB_REPOSITORY}/releases/assets/${asset_id}"; then
      echo "::warning::Asset delete did not return success; checking the remote postcondition for ${name}." >&2
    fi
    RELEASE_REFRESH_PHASE="post-delete ${name}" api_read \
      "${after_delete}" "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}"
    jq -e '.draft == true' "${after_delete}" >/dev/null || {
      echo "::error::Release ${RELEASE_TAG} became published while deleting ${name}; aborting all further asset mutation." >&2
      return 1
    }
    matches="$(jq --arg name "${name}" --arg id "${asset_id}" \
      '[.assets[] | select(.name == $name and (.id | tostring) == $id)] | length' \
      "${after_delete}")"
    if [[ "${matches}" == 0 ]]; then
      cp "${after_delete}" "${remote_json}"
      return 0
    fi
    [[ "${matches}" == 1 ]] || {
      echo "::error::Asset deletion postcondition is ambiguous for ${name}." >&2
      return 1
    }
    ((attempt < 2)) && sleep 5
  done
  echo "::error::Asset ${name} still exists after bounded deletion attempts." >&2
  return 1
}

while IFS=$'\t' read -r name path; do
  [[ -n "${name}" ]] || continue
  [[ -f "${path}" ]] || {
    echo "::error::Staged upload path is missing for ${name}: ${path}" >&2
    exit 1
  }

  refresh_draft_release "planning mutation for ${name}"
  mapfile -t existing_ids < <(
    jq -r --arg name "${name}" '.assets[] | select(.name == $name) | .id' "${remote_json}"
  )
  ((${#existing_ids[@]} <= 1)) || {
    echo "::error::Release ${RELEASE_ID} contains duplicate assets named ${name}." >&2
    exit 1
  }
  if ((${#existing_ids[@]} == 1)); then
    existing_id="${existing_ids[0]}"
    [[ "${existing_id}" =~ ^[0-9]+$ ]] || {
      echo "::error::Asset ${name} resolved to invalid ID ${existing_id}." >&2
      exit 1
    }
    if [[ "${REPLACE}" != true ]] && ! grep -Fxq -- "${name}" "${REPLACE_NAMES_FILE}"; then
      echo "::error::Asset ${name} exists but the validated repair plan did not authorise replacement." >&2
      exit 1
    fi
    encoded_delete_name="$(jq -rn --arg value "${name}" '$value | @uri')"
    ensure_asset_absent "${name}" "${existing_id}" "${encoded_delete_name}"
  fi

  encoded_name="$(jq -rn --arg value "${name}" '$value | @uri')"
  response="${RUNNER_TEMP}/uploaded-${encoded_name}.json"
  upload_once() {
    curl --fail-with-body --silent --show-error \
      --connect-timeout 15 \
      --max-time 300 \
      --request POST \
      --header 'Accept: application/vnd.github+json' \
      --header "Authorization: Bearer ${GH_TOKEN}" \
      --header 'X-GitHub-Api-Version: 2022-11-28' \
      --header 'Content-Type: application/octet-stream' \
      --data-binary "@${path}" \
      "${upload_base}?name=${encoded_name}&label=${encoded_name}"
  }
  refresh_draft_release "uploading ${name}"
  if ! upload_once > "${response}"; then
    echo "::warning::Asset upload did not return success; checking the remote postcondition for ${name}." >&2
    after_upload="${RUNNER_TEMP}/post-upload-${encoded_name}.json"
    RELEASE_REFRESH_PHASE="post-upload ${name}" api_read \
      "${after_upload}" "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}"
    jq -e '.draft == true' "${after_upload}" >/dev/null || {
      echo "::error::Release ${RELEASE_TAG} became published while uploading ${name}; aborting all further asset mutation." >&2
      exit 1
    }
    matches="$(jq --arg name "${name}" '[.assets[] | select(.name == $name and .state == "uploaded")] | length' "${after_upload}")"
    if [[ "${matches}" == 1 ]]; then
      jq --arg name "${name}" '.assets[] | select(.name == $name and .state == "uploaded")' \
        "${after_upload}" > "${response}"
    elif [[ "${matches}" == 0 ]]; then
      sleep 5
      refresh_draft_release "retrying upload of ${name}"
      upload_once > "${response}"
    else
      echo "::error::Ambiguous duplicate uploaded assets named ${name}." >&2
      exit 1
    fi
  fi
  jq -e --arg name "${name}" \
    '(.id | type == "number") and (.name == $name) and (.state == "uploaded")' \
    "${response}" >/dev/null || {
      echo "::error::GitHub returned an invalid upload result for ${name}." >&2
      exit 1
    }
done < "${UPLOAD_TSV}"
