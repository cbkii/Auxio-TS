#!/usr/bin/env bash
{
  echo '# Manual release summary'
  echo
  echo "- Result: \`${RESULT}\`"
  echo "- Requested mode: \`${REQUESTED_MODE}\`"
  echo "- Effective action: \`${EFFECTIVE_MODE:-not resolved}\`"
  echo "- Resolution: \`${RESOLUTION_REASON:-not resolved}\`"
  echo "- Tag: ${RELEASE_TAG:-not computed}"
  echo "- Starting dev SHA: ${SOURCE_SHA:-not resolved}"
  echo "- Release/tag SHA: ${RELEASE_SHA:-not resolved}"
  echo "- Maintained assets: ${SELECTED:-not selected}"
  echo "- Debug destination: ${DEBUG_DESTINATION:-not selected}"
  echo "- Release created in this run: ${RELEASE_CREATED:-false}"
  echo "- Assets verified remotely: ${ASSETS_VERIFIED:-false}"
  echo "- Source metadata synchronisation: ${SYNC_STATUS:-not attempted}"
  [[ -n "${RELEASE_URL:-}" ]] && echo "- Release: ${RELEASE_URL}"
  [[ -n "${RELEASE_ARTIFACT_URL:-}" ]] && echo "- Recovery workflow artifact: ${RELEASE_ARTIFACT_URL}"
  [[ -n "${DEBUG_ARTIFACT_URL:-}" ]] && echo "- Debug workflow artifact: ${DEBUG_ARTIFACT_URL}"
  echo
  echo '## Release status'
  if [[ -n "${FINAL_DRAFT:-}" ]]; then
    echo "- Initial transaction state: draft=${INITIAL_DRAFT}, prerelease=${INITIAL_PRERELEASE}"
    echo "- Requested final state: draft=${FINAL_DRAFT}, prerelease=${FINAL_PRERELEASE}"
  else
    echo "- Existing published release status was preserved: draft=${INITIAL_DRAFT:-unknown}, prerelease=${INITIAL_PRERELEASE:-unknown}"
  fi
  if [[ -n "${UNRESOLVED_TAG_ONLY:-}" ]]; then
    echo "- Older unresolved tag-only versions retained: ${UNRESOLVED_TAG_ONLY}"
  fi
  if [[ -n "${UNRESOLVED_RELEASE_WITHOUT_TAG:-}" ]]; then
    echo "- Older Releases without resolvable tags retained: ${UNRESOLVED_RELEASE_WITHOUT_TAG}"
  fi
  if [[ -n "${UNRESOLVED_DRAFT_INDEX:-}" ]]; then
    echo "- Older inconsistent draft-index versions retained: ${UNRESOLVED_DRAFT_INDEX}"
  fi
  echo
  echo '## Selected GitHub Release assets'
  if [[ -f "${VERIFY_NAMES_FILE:-}" && -s "${VERIFY_NAMES_FILE}" ]]; then
    while IFS= read -r asset; do
      [[ -n "${asset}" ]] && echo "- ${asset}"
    done < "${VERIFY_NAMES_FILE}"
  else
    echo '- No release asset list reached planning.'
  fi
  echo
  echo '- Each required variant was built at most once.'
  echo '- Existing complete triplets were reused; interrupted partial triplets were rebuilt and repaired automatically.'
  echo '- Auxio-TS (com.tw.media) is the maintained product APK.'
  echo '- The API 100 LSPosed bridge remains static-scoped only to genuine stock com.tw.music.'
  echo '- The exact-package topwayTwMusic Magisk release lane remains retired.'
  if [[ "${SYNC_STATUS}" == push_rejected || "${SYNC_STATUS}" == dev_moved ]]; then
    echo
    echo '## Follow-up required'
    echo '- The GitHub Release succeeded, but source metadata did not reach dev. Reconcile dev with the immutable release commit before the next release.'
  fi
} >> "${GITHUB_STEP_SUMMARY}"
