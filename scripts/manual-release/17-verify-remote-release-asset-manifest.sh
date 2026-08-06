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
remote_json="${RUNNER_TEMP}/verified-release.json"
actual_names="${RUNNER_TEMP}/actual-release-assets.txt"
[[ "${RELEASE_ID}" =~ ^[0-9]+$ ]] || {
  echo "::error::Release ID is missing or invalid for ${RELEASE_TAG}."
  exit 1
}
api_read "${remote_json}" "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}"
jq -e --arg tag "${RELEASE_TAG}" --arg id "${RELEASE_ID}" \
  '(.id == ($id | tonumber)) and (.tag_name == $tag)' \
  "${remote_json}" >/dev/null || {
    echo "::error::Remote release ID ${RELEASE_ID} no longer matches ${RELEASE_TAG}."
    exit 1
  }
jq -r '.assets[].name' "${remote_json}" | sort -u > "${actual_names}"

while IFS= read -r name; do
  [[ -n "${name}" ]] || continue
  grep -Fxq -- "${name}" "${actual_names}" || {
    echo "::error::Release is missing selected asset ${name}."
    exit 1
  }
done < "${VERIFY_NAMES_FILE}"

if [[ "${RELEASE_MODE}" == create_new_release ]]; then
  expected_sorted="${RUNNER_TEMP}/expected-release-assets.txt"
  sort -u "${VERIFY_NAMES_FILE}" > "${expected_sorted}"
  if ! diff -u "${expected_sorted}" "${actual_names}"; then
    echo "::error::New release contains an unexpected or missing asset."
    exit 1
  fi
  forbidden="Auxio-TS-${RELEASE_TAG}-topway-twmusic-release.apk"
  retired="Auxio-TS-${RELEASE_TAG}-topway-twmusic-magisk.zip"
  grep -Fxq -- "${forbidden}" "${actual_names}" && {
    echo "::error::Raw topwayTwMusic APK asset is forbidden: ${forbidden}"
    exit 1
  }
  grep -Fxq -- "${retired}" "${actual_names}" && {
    echo "::error::Retired topwayTwMusic Magisk asset is forbidden: ${retired}"
    exit 1
  }
  if [[ "${DEBUG_DESTINATION}" != release_assets ]] && grep -Eq -- '-debug\.apk($|\.)' "${actual_names}"; then
    echo "::error::Debug APKs are forbidden unless the resolved debug destination is release_assets."
    exit 1
  fi
fi
echo "verified=true" >> "${GITHUB_OUTPUT}"
