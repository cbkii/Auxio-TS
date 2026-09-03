#!/usr/bin/env bash
# Keep gradle/libs.versions.toml trustworthy while Groovy build files remain authoritative.
set -u
set -o pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)" || exit 1
cd "${repo_root}" || exit 1

python3 scripts/check-version-catalog-sync.py
