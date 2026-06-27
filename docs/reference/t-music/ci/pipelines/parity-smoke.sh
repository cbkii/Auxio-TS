#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

echo "[parity-smoke] Running token-based parity smoke matrix"
bash tools/parity/run_parity_matrix.sh
echo "[parity-smoke] OK"
