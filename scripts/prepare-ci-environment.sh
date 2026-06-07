#!/usr/bin/env bash
# Backwards-compatible wrapper around the canonical dependency bootstrap.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

# shellcheck source=scripts/dependency-lib.sh
source "${SCRIPT_DIR}/dependency-lib.sh"

cd "${ROOT_DIR}"

usage() {
  cat <<USAGE
Usage: bash scripts/prepare-ci-environment.sh [--profile <$(dep_supported_profiles_pipe)>|<profile>]

Delegates to scripts/bootstrap-dependencies.sh. The profile defaults to
full-build (override with --profile, a bare profile name, or the
DEPENDENCY_BOOTSTRAP_PROFILE environment variable).
USAGE
}

# Environment-provided default is validated below alongside any CLI value.
PROFILE="${DEPENDENCY_BOOTSTRAP_PROFILE:-full-build}"

if [[ $# -gt 0 ]]; then
  case "$1" in
    --profile)
      if [[ $# -lt 2 ]]; then
        dep_err "Missing value for --profile; expected one of: $(dep_supported_profiles_pipe)"
        usage
        exit 2
      fi
      PROFILE="$2"
      shift 2
      ;;
    --profile=*)
      PROFILE="${1#--profile=}"
      shift
      ;;
    --help|-h)
      usage
      exit 0
      ;;
    -*)
      dep_err "Unknown prepare-ci-environment argument: $1"
      usage
      exit 2
      ;;
    *)
      # Any non-flag positional is treated as a candidate profile and validated
      # centrally below via dep_validate_profile (single source of truth in
      # dependency-lib.sh), so the supported-profile list is not duplicated here.
      PROFILE="$1"
      shift
      ;;
  esac
fi

dep_validate_profile "${PROFILE}" "prepare-ci profile" || { usage; exit 2; }

exec bash "${SCRIPT_DIR}/bootstrap-dependencies.sh" --profile "${PROFILE}"
