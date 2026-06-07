#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

PROFILE="${DEPENDENCY_BOOTSTRAP_PROFILE:-full-build}"
if [[ $# -gt 0 ]]; then
  case "$1" in
    --profile)
      PROFILE="${2:-}"
      shift 2
      ;;
    static-review|jvm-tests|full-build|release)
      PROFILE="$1"
      shift
      ;;
    *)
      echo "::error::Unknown prepare-ci-environment argument: $1"
      echo "Usage: bash scripts/prepare-ci-environment.sh [--profile <profile>|<profile>]"
      exit 2
      ;;
  esac
fi

exec bash ./scripts/bootstrap-dependencies.sh --profile "$PROFILE"
