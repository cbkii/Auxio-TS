#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${repo_root}"

report_dir="build/reports/startup-performance"
mkdir -p "${report_dir}"
check_log="${report_dir}/spotless-check.log"
apply_log="${report_dir}/spotless-apply.log"

set +e
bash ./scripts/ci-gradle.sh spotlessCheck 2>&1 | tee "${check_log}"
check_status=${PIPESTATUS[0]}
set -e
if ((check_status == 0)); then
  exit 0
fi

set +e
bash ./scripts/ci-gradle.sh spotlessApply 2>&1 | tee "${apply_log}"
apply_status=${PIPESTATUS[0]}
set -e
if ((apply_status != 0)); then
  printf '\nspotlessApply also failed; inspect %s and %s.\n' "${check_log}" "${apply_log}" >&2
  exit "${apply_status}"
fi

git diff --binary > "${report_dir}/format.patch"
printf '\nFormatting differences:\n' >&2
cat "${report_dir}/format.patch" >&2
exit 1
