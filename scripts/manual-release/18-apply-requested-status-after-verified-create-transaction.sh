#!/usr/bin/env bash
set -euo pipefail
final_json="${RUNNER_TEMP}/final-release.json"
patch_status() {
  timeout 60s gh api --method PATCH "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}" \
    -F draft="${DRAFT}" \
    -F prerelease="${PRERELEASE}"
}
if ! patch_status > "${final_json}"; then
  echo "::warning::Release status update did not return success; checking the remote postcondition."
  timeout 60s gh api "repos/${GITHUB_REPOSITORY}/releases/${RELEASE_ID}" > "${final_json}"
  if ! jq -e --argjson draft "${DRAFT}" --argjson prerelease "${PRERELEASE}" \
    '(.draft == $draft) and (.prerelease == $prerelease)' "${final_json}" >/dev/null; then
    sleep 5
    patch_status > "${final_json}"
  fi
fi
echo "draft=$(jq -r .draft "${final_json}")" >> "${GITHUB_OUTPUT}"
echo "prerelease=$(jq -r .prerelease "${final_json}")" >> "${GITHUB_OUTPUT}"
