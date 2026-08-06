#!/usr/bin/env bash
# shellcheck disable=SC2016 # Backticks in generated release notes are literal Markdown.
set -euo pipefail
created=false
release_json="${RUNNER_TEMP}/active-release.json"
if [[ "${RELEASE_EXISTS}" != true ]]; then
  notes="${RUNNER_TEMP}/release-notes.md"
  {
    echo "## What's changed"
    echo
    if [[ -n "${PREVIOUS_TAG}" ]]; then
      git log --reverse --pretty='- %s' "${PREVIOUS_TAG}..${RELEASE_TAG}"
    else
      echo '- Initial maintained Topway release.'
    fi
    echo
    echo '## Assets'
    echo '- topwayTwMedia is the primary signed APK.'
    echo '- The LSPosed API 100 bridge is the signed TS18 addon for the genuine stock `com.tw.music` process.'
    if [[ "${DEBUG_DESTINATION}" == release_assets ]]; then
      echo '- Debug APKs were explicitly selected as GitHub Release assets.'
      echo '- Debug APKs are diagnostic companions with separate application IDs; they are not recommended normal installs.'
    else
      echo '- Debug APKs remain short-lived workflow artifacts and are not GitHub Release assets.'
    fi
    echo '- The former exact-package Auxio Magisk overlay is retired and is not published.'
    echo '- Exact TS18 runtime behaviour requires device validation.'
  } > "${notes}"
  # A newly pushed tag can take time to become visible to the Releases API.
  # Always provide the validated commit SHA so the request remains valid during that window.
  create_release() {
    timeout 60s gh api --method POST "repos/${GITHUB_REPOSITORY}/releases" \
      -f tag_name="${RELEASE_TAG}" \
      -f target_commitish="${RELEASE_SHA}" \
      -f name="${RELEASE_TAG}" \
      -F body=@"${notes}" \
      -F draft=true \
      -F prerelease=false
  }
  if ! create_release > "${release_json}"; then
    echo "::warning::Release creation did not return success; checking whether GitHub committed it."
    lookup="${RUNNER_TEMP}/release-create-lookup.tsv"
    timeout 60s gh api --paginate "repos/${GITHUB_REPOSITORY}/releases?per_page=100" \
      --jq '.[] | [.id, .tag_name] | @tsv' > "${lookup}"
    mapfile -t created_ids < <(awk -F '\t' -v tag="${RELEASE_TAG}" '$2 == tag {print $1}' "${lookup}")
    if ((${#created_ids[@]} == 1)); then
      timeout 60s gh api \
        "repos/${GITHUB_REPOSITORY}/releases/${created_ids[0]}" > "${release_json}"
    elif ((${#created_ids[@]} == 0)); then
      sleep 5
      create_release > "${release_json}"
    else
      echo "::error::Multiple Releases unexpectedly use ${RELEASE_TAG}."
      exit 1
    fi
  fi
  created=true
else
  [[ -s "${TARGET_RELEASE_FILE}" ]] || {
    echo "::error::Existing release state file is missing for ${RELEASE_TAG}."
    exit 1
  }
  cp "${TARGET_RELEASE_FILE}" "${release_json}"
fi
jq -e --arg tag "${RELEASE_TAG}" '
  (.id | type == "number") and
  (.tag_name == $tag) and
  (.html_url | type == "string" and length > 0) and
  (.draft | type == "boolean") and
  (.prerelease | type == "boolean")
' "${release_json}" >/dev/null || {
  echo "::error::Active release object is invalid or does not match ${RELEASE_TAG}."
  exit 1
}
{
  echo "created=${created}"
  echo "release_id=$(jq -r .id "${release_json}")"
  echo "release_url=$(jq -r .html_url "${release_json}")"
  echo "initial_draft=$(jq -r .draft "${release_json}")"
  echo "initial_prerelease=$(jq -r .prerelease "${release_json}")"
} >> "${GITHUB_OUTPUT}"
