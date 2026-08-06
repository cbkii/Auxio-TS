set -euo pipefail
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
    ((attempt < 3)) && sleep $((attempt * 4))
  done
  return "${rc}"
}
if [[ ! -s "${UPLOAD_TSV}" ]]; then
  echo 'No remote asset upload is required; selected triplets are already complete.'
  exit 0
fi
[[ "${RELEASE_ID}" =~ ^[0-9]+$ ]] || {
  echo "::error::Release ID is missing or invalid for ${RELEASE_TAG}."
  exit 1
}

duplicate_names="${RUNNER_TEMP}/duplicate-upload-asset-names.txt"
cut -f1 "${UPLOAD_TSV}" | awk 'NF' | sort | uniq -d > "${duplicate_names}"
[[ ! -s "${duplicate_names}" ]] || {
  echo '::error::The upload plan contains duplicate asset names.'
  cat "${duplicate_names}" >&2
  exit 1
}

remote_json="${RUNNER_TEMP}/upload-release.json"
api_read "${remote_json}" "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}"
jq -e --arg tag "${RELEASE_TAG}" --arg id "${RELEASE_ID}" \
  '(.id == ($id | tonumber)) and (.tag_name == $tag)' \
  "${remote_json}" >/dev/null || {
    echo "::error::Upload release ID ${RELEASE_ID} does not match ${RELEASE_TAG}."
    exit 1
  }
upload_template="$(jq -r .upload_url "${remote_json}")"
upload_base="${upload_template%%{*}"
[[ "${upload_base}" == https://* && "${upload_base}" == */releases/${RELEASE_ID}/assets ]] || {
  echo "::error::Release ${RELEASE_ID} returned an invalid upload URL."
  exit 1
}

while IFS=$'\t' read -r name path; do
  [[ -n "${name}" ]] || continue
  [[ -f "${path}" ]] || {
    echo "::error::Staged upload path is missing for ${name}: ${path}"
    exit 1
  }

  mapfile -t existing_ids < <(
    jq -r --arg name "${name}" '.assets[] | select(.name == $name) | .id' "${remote_json}"
  )
  ((${#existing_ids[@]} <= 1)) || {
    echo "::error::Release ${RELEASE_ID} contains duplicate assets named ${name}."
    exit 1
  }
  if ((${#existing_ids[@]} == 1)); then
    existing_id="${existing_ids[0]}"
    [[ "${existing_id}" =~ ^[0-9]+$ ]] || {
      echo "::error::Asset ${name} resolved to invalid ID ${existing_id}."
      exit 1
    }
    [[ "${REPLACE}" == true ]] || {
      echo "::error::Asset ${name} already exists; explicit replacement was not selected."
      exit 1
    }
    timeout 60s gh api --method DELETE \
      "repos/${GITHUB_REPOSITORY}/releases/assets/${existing_id}"
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
  if ! upload_once > "${response}"; then
    echo "::warning::Asset upload did not return success; checking the remote postcondition for ${name}."
    after_upload="${RUNNER_TEMP}/post-upload-${encoded_name}.json"
    api_read "${after_upload}" "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}"
    matches="$(jq --arg name "${name}" '[.assets[] | select(.name == $name and .state == "uploaded")] | length' "${after_upload}")"
    if [[ "${matches}" == 1 ]]; then
      jq --arg name "${name}" '.assets[] | select(.name == $name and .state == "uploaded")' \
        "${after_upload}" > "${response}"
    elif [[ "${matches}" == 0 ]]; then
      sleep 5
      upload_once > "${response}"
    else
      echo "::error::Ambiguous duplicate uploaded assets named ${name}."
      exit 1
    fi
  fi
  jq -e --arg name "${name}" \
    '(.id | type == "number") and (.name == $name) and (.state == "uploaded")' \
    "${response}" >/dev/null || {
      echo "::error::GitHub returned an invalid upload result for ${name}."
      exit 1
    }
done < "${UPLOAD_TSV}"
