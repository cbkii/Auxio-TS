#!/usr/bin/env bash
# Read-only dependency validator. Use bootstrap-dependencies.sh for repair/fetch.
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

PROFILE="${CHECK_SUBMODULES_PROFILE:-full-build}"
if [[ $# -gt 0 ]]; then
  case "$1" in
    --profile)
      PROFILE="${2:-}"
      shift 2
      ;;
    --repair)
      echo "::notice::check-submodules.sh no longer owns repair policy; delegating to canonical bootstrap."
      exec bash ./scripts/bootstrap-dependencies.sh --profile "$PROFILE"
      ;;
    static-review|jvm-tests|full-build|release)
      PROFILE="$1"
      shift
      ;;
    *)
      echo "::error::Unknown check-submodules argument: $1"
      echo "Usage: bash scripts/check-submodules.sh [--profile <profile>|<profile>|--repair]"
      exit 2
      ;;
  esac
fi

if [[ ! -e .git ]]; then
  echo "::error::SNAPSHOT_LIMITATION: no .git directory detected."
  [[ "$PROFILE" == "static-review" ]] && exit 0
  exit 1
fi

manifest="ci/dependencies/submodules.tsv"
if [[ ! -f "$manifest" ]]; then
  echo "::error::REAL_BUILD_FAILURE: missing $manifest"
  exit 1
fi

pin_mismatch=0
missing=0
profile_requires_path() { [[ ",$1," == *",${PROFILE},"* ]]; }
parent_worktree() { [[ "$1" == "." ]] && printf '%s\n' "$ROOT_DIR" || printf '%s/%s\n' "$ROOT_DIR" "$1"; }
expected_sha_for() {
  local parent="$1" path="$2" rel="$2" parent_dir
  parent_dir="$(parent_worktree "$parent")"
  [[ "$parent" != "." ]] && rel="${path#${parent}/}"
  git -C "$parent_dir" rev-parse "HEAD:${rel}" 2>/dev/null || true
}

echo "--- Submodule pin validation (${PROFILE}) ---"
while IFS=$'\t' read -r path type parent primary fallbacks required_profiles sentinel release_blocking; do
  [[ -z "${path:-}" || "${path:0:1}" == "#" ]] && continue
  profile_requires_path "$required_profiles" || continue
  expected="$(expected_sha_for "$parent" "$path")"
  if [[ -z "$expected" ]]; then
    echo "::error::DEPENDENCY_PIN_MISMATCH: cannot read expected gitlink for $path"
    pin_mismatch=1
    continue
  fi
  if [[ ! -e "$path/$sentinel" ]]; then
    echo "::error::SUBMODULE_BLOCKER: missing $path/$sentinel"
    missing=1
    continue
  fi
  actual="$(git -C "$path" rev-parse HEAD 2>/dev/null || true)"
  if [[ -z "$actual" || "$actual" != "$expected" ]]; then
    echo "::error::DEPENDENCY_PIN_MISMATCH: $path is at ${actual:-<unverified>}, expected $expected"
    pin_mismatch=1
    continue
  fi
  echo "OK $path: $actual"
done < "$manifest"

if [[ "$pin_mismatch" -ne 0 ]]; then
  exit 1
fi
if [[ "$missing" -ne 0 ]]; then
  if [[ "$PROFILE" == "static-review" ]]; then
    echo "::warning::DEGRADED_STATIC_ONLY: missing submodules; static review only."
    exit 0
  fi
  echo "::error::SUBMODULE_BLOCKER: run bash ./scripts/bootstrap-dependencies.sh --profile $PROFILE"
  exit 1
fi

echo "READY: submodule pins validated for profile $PROFILE."
git submodule status --recursive 2>/dev/null || true
