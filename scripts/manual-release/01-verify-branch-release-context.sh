set -euo pipefail
if [[ "${GITHUB_REF_TYPE}" != branch || "${GITHUB_REF_NAME}" != dev ]]; then
  echo "::error::Manual releases must run from the current dev branch."
  exit 1
fi
timeout 60s git fetch --tags origin dev
source_sha="$(git rev-parse HEAD)"
remote_sha="$(git rev-parse origin/dev)"
[[ "${source_sha}" == "${remote_sha}" ]] || {
  echo "::error::Release checkout is stale. Re-run from the latest dev."
  exit 1
}
echo "source_sha=${source_sha}" >> "${GITHUB_OUTPUT}"
