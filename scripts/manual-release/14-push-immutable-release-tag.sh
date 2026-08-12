#!/usr/bin/env bash
# Publish one operator-approved immutable release tag, then verify its exact remote SHA.
# This never deletes, rewrites or force-updates an existing tag.
set -euo pipefail
timeout 30s gh auth setup-git
git tag "${RELEASE_TAG}" "${RELEASE_SHA}"
if ! timeout 120s git push origin "refs/tags/${RELEASE_TAG}"; then
  echo "::warning::Tag push did not return success; checking the immutable remote postcondition."
  remote_after_failure="$(timeout 60s git ls-remote --tags origin "refs/tags/${RELEASE_TAG}" | awk '{print $1}')"
  if [[ "${remote_after_failure}" != "${RELEASE_SHA}" ]]; then
    sleep 5
    timeout 120s git push origin "refs/tags/${RELEASE_TAG}"
  fi
fi
remote_sha="$(timeout 60s git ls-remote --tags origin "refs/tags/${RELEASE_TAG}" | awk '{print $1}')"
[[ "${remote_sha}" == "${RELEASE_SHA}" ]] || {
  echo "::error::Remote tag ${RELEASE_TAG} does not match the validated release commit."
  exit 1
}
echo "tag_pushed=true" >> "${GITHUB_OUTPUT}"
