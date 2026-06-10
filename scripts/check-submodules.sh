#!/usr/bin/env bash
# Read-only dependency validator. Use bootstrap-dependencies.sh for repair/fetch.
#
# Shared, read-only logic lives in scripts/dependency-lib.sh so pin/profile/
# gitlink logic cannot drift from the canonical bootstrap. The only mutating
# action this script performs is delegating `--repair` to the canonical
# bootstrap with a validated profile.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

# shellcheck source=scripts/dependency-lib.sh
source "${SCRIPT_DIR}/dependency-lib.sh"

cd "${ROOT_DIR}"

usage() {
  cat <<USAGE
Usage: bash scripts/check-submodules.sh [--profile <$(dep_supported_profiles_pipe)>|<profile>|--repair]

Read-only validation of pinned submodule SHAs for the selected profile.
The profile defaults to full-build (override with --profile, a bare profile
name, or the CHECK_SUBMODULES_PROFILE environment variable).
--repair delegates to scripts/bootstrap-dependencies.sh for the same profile.
USAGE
}

REPAIR=0
# Environment-provided default is validated below alongside any CLI value so an
# unsupported profile can never silently filter out every manifest entry and
# exit "successfully".
PROFILE="${CHECK_SUBMODULES_PROFILE:-full-build}"

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
    --repair)
      REPAIR=1
      shift
      ;;
    --help|-h)
      usage
      exit 0
      ;;
    static-review|jvm-tests|full-build|release)
      PROFILE="$1"
      shift
      ;;
    *)
      dep_err "Unknown check-submodules argument: $1"
      usage
      exit 2
      ;;
  esac
fi

dep_validate_profile "${PROFILE}" "check-submodules profile" || { usage; exit 2; }

if [[ "${REPAIR}" -eq 1 ]]; then
  dep_note "check-submodules.sh no longer owns repair policy; delegating to canonical bootstrap."
  exec bash "${SCRIPT_DIR}/bootstrap-dependencies.sh" --profile "${PROFILE}"
fi

if [[ ! -e .git ]]; then
  dep_err "SNAPSHOT_LIMITATION: no .git directory detected."
  [[ "${PROFILE}" == "static-review" ]] && exit 0
  exit 1
fi

MANIFEST="$(dep_submodule_manifest "${ROOT_DIR}")"
if [[ ! -f "${MANIFEST}" ]]; then
  dep_err "REAL_BUILD_FAILURE: missing ${MANIFEST}"
  exit 1
fi

pin_mismatch=0
missing=0
dirty_submodule=0

dep_info "--- Submodule pin validation (${PROFILE}) ---"
# Read the manifest on FD 3 so git commands inside the loop cannot consume it
# from stdin.
while IFS=$'\t' read -r path _type parent _primary _fallbacks required_profiles sentinel _release_blocking <&3; do
  [[ -z "${path:-}" || "${path:0:1}" == "#" ]] && continue
  dep_profile_requires_path "${required_profiles}" "${PROFILE}" || continue

  expected="$(dep_expected_sha "${ROOT_DIR}" "${parent}" "${path}")"
  if [[ -z "${expected}" ]]; then
    dep_err "DEPENDENCY_PIN_MISMATCH: cannot read expected gitlink for ${path}"
    pin_mismatch=1
    continue
  fi
  if ! dep_sentinel_present "${ROOT_DIR}" "${path}" "${sentinel}"; then
    dep_err "SUBMODULE_BLOCKER: missing ${path}/${sentinel}"
    missing=1
    continue
  fi
  actual="$(dep_actual_sha "${ROOT_DIR}" "${path}")"
  if [[ -z "${actual}" || "${actual}" != "${expected}" ]]; then
    dep_err "DEPENDENCY_PIN_MISMATCH: ${path} is at ${actual:-<unverified>}, expected ${expected}"
    pin_mismatch=1
    continue
  fi
  dep_ok "${path}: ${actual}"
  dep_validate_submodule_clean "${ROOT_DIR}" "${path}" || dirty_submodule=1
done 3< "${MANIFEST}"

if [[ "${pin_mismatch}" -ne 0 ]]; then
  exit 1
fi
if [[ "${missing}" -ne 0 ]]; then
  if [[ "${PROFILE}" == "static-review" ]]; then
    dep_warn "DEGRADED_STATIC_ONLY: missing submodules; static review only."
    exit 0
  fi
  dep_err "SUBMODULE_BLOCKER: run bash ./scripts/bootstrap-dependencies.sh --profile ${PROFILE}"
  exit 1
fi

if [[ "${dirty_submodule}" -ne 0 ]]; then
  if [[ "${PROFILE}" == "static-review" ]]; then
    dep_warn "DEGRADED_STATIC_ONLY: dirty submodules; static review only."
    exit 0
  fi
  dep_err "DEPENDENCY_DIRTY_SUBMODULE: clean or reset submodules before continuing."
  exit 1
fi

dep_info "READY: submodule pins validated for profile ${PROFILE}."
git submodule status --recursive 2>/dev/null || true
