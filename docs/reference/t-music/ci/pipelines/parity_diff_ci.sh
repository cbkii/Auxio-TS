#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

BASE_APK="${BASE_APK:-dist/com.tw.music.apk}"
CANDIDATE_APK="${CANDIDATE_APK:-dist/com.tw.music-unsigned.apk}"
FAIL_ON_DIFF="${FAIL_ON_DIFF:-false}"

echo "[parity-diff] Baseline APK: $BASE_APK"
echo "[parity-diff] Candidate APK: $CANDIDATE_APK"
echo "[parity-diff] Fail on diff: $FAIL_ON_DIFF"

set +e
bash tools/parity/diff_manifest_resources.sh "$BASE_APK" "$CANDIDATE_APK"
rc=$?
set -e

case "$rc" in
  0)
    echo "[parity-diff] No manifest/resource differences found."
    ;;
  2)
    echo "[parity-diff] Differences detected. Review docs/reports/parity-*.diff artifacts."
    if [[ "$FAIL_ON_DIFF" == "true" ]]; then
      echo "[parity-diff] FAIL_ON_DIFF=true, failing run."
      exit 2
    fi
    ;;
  *)
    echo "[parity-diff] Diff step failed unexpectedly (exit=$rc)." >&2
    exit "$rc"
    ;;
esac
