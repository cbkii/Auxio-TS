#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${repo_root}"

if bash ./scripts/ci-gradle.sh spotlessCheck; then
  exit 0
fi

mkdir -p build/reports/startup-performance
bash ./scripts/ci-gradle.sh spotlessApply
git diff --binary > build/reports/startup-performance/format.patch
printf '\nFormatting differences:\n' >&2
cat build/reports/startup-performance/format.patch >&2
exit 1
