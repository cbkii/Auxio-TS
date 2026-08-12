#!/usr/bin/env bash
set -euo pipefail
if [[ "${RELEASE_MODE}" == create_new_release ]]; then
  if [[ "${METADATA_CHANGE_REQUIRED}" == true ]]; then
    python3 "${TOOL}" update-gradle \
      --gradle app/build.gradle \
      --version-name "${VERSION_NAME}" \
      --version-code "${VERSION_CODE}"
    git config user.name 'github-actions[bot]'
    git config user.email '41898282+github-actions[bot]@users.noreply.github.com'
    git add app/build.gradle
    git commit -m "chore(release): prepare ${RELEASE_TAG}"
  fi
  release_sha="$(git rev-parse HEAD)"
  resolved_name="${VERSION_NAME}"
  resolved_code="${VERSION_CODE}"
else
  tagged_metadata="${RUNNER_TEMP}/tagged-gradle-metadata.json"
  python3 "${TOOL}" inspect-gradle \
    --gradle app/build.gradle \
    --expected-version "${VERSION_NAME}" \
    --output "${tagged_metadata}"
  release_sha="$(git rev-parse HEAD)"
  resolved_name="$(jq -r .version_name "${tagged_metadata}")"
  resolved_code="$(jq -r .version_code "${tagged_metadata}")"
fi
{
  echo "release_sha=${release_sha}"
  echo "version_name=${resolved_name}"
  echo "version_code=${resolved_code}"
} >> "${GITHUB_OUTPUT}"

