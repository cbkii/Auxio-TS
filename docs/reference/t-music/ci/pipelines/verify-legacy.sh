#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

echo "[verify-legacy] Running vendor token compatibility scan"
python3 tools/vendor-audit/scan_vendor_tokens.py

echo "[verify-legacy] OK"
