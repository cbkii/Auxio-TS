#!/usr/bin/env bash
# Canonical dependency bootstrap and validation entry point for Auxio-TS.
#
# One command for every environment (GitHub Actions, manual release, local
# clones, Codex/Jules/Devin/agents, ZIP/snapshot contexts). It is idempotent,
# non-interactive, and explicit about when it may continue degraded vs when it
# must fail closed. Shared, read-only logic lives in scripts/dependency-lib.sh;
# fetch/repair/stub actions stay here.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

# shellcheck source=scripts/dependency-lib.sh
source "${SCRIPT_DIR}/dependency-lib.sh"

SUBMODULE_MANIFEST="$(dep_submodule_manifest "${REPO_ROOT}")"
ANDROID_SDK_ENV="$(dep_android_sdk_env "${REPO_ROOT}")"

# Default profile may be supplied via the environment; it is validated below so
# a bad DEPENDENCY_BOOTSTRAP_PROFILE cannot silently change behaviour.
PROFILE="${DEPENDENCY_BOOTSTRAP_PROFILE:-full-build}"
JOBS="${BOOTSTRAP_JOBS:-4}"
MIRROR_USED=0
DEGRADED=0
PIN_MISMATCH=0
SUBMODULE_BLOCKER=0
SDK_BLOCKER=0

usage() {
  cat <<USAGE
Usage: bash scripts/bootstrap-dependencies.sh [--profile <$(dep_supported_profiles_pipe)>]

The profile defaults to full-build (override with --profile or the
DEPENDENCY_BOOTSTRAP_PROFILE environment variable).

Profiles:
  static-review  Best-effort dependencies for source/script review; may print DEGRADED_STATIC_ONLY.
  jvm-tests      Strict dependency bootstrap for Gradle JVM/unit-test tasks in this repo.
  full-build     Strict dependency bootstrap for debug/full CI builds (default).
  release        Strict release bootstrap; all pins and release-blocking deps must be present.
USAGE
}

while [[ $# -gt 0 ]]; do
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
    *)
      dep_err "Unknown argument: $1"
      usage
      exit 2
      ;;
  esac
done

dep_validate_profile "${PROFILE}" "bootstrap profile" || { usage; exit 2; }

cd "${REPO_ROOT}"

dep_info "--- Auxio-TS dependency bootstrap ---"
dep_info "profile=${PROFILE}"

is_static_review() { [[ "${PROFILE}" == "static-review" ]]; }
is_strict_profile() { [[ "${PROFILE}" != "static-review" ]]; }

if [[ ! -e .git ]]; then
  dep_err "SNAPSHOT_LIMITATION: no .git directory detected (ZIP/snapshot checkout)."
  if is_static_review; then
    dep_warn "DEGRADED_STATIC_ONLY: source/script review may continue, but Gradle/build/test validation must not be claimed."
    exit 0
  fi
  dep_err "SUBMODULE_BLOCKER: ${PROFILE} requires a real git checkout so pinned submodule SHAs can be fetched and verified."
  exit 1
fi

if [[ ! -f "${SUBMODULE_MANIFEST}" ]]; then
  dep_err "REAL_BUILD_FAILURE: missing dependency manifest: ${SUBMODULE_MANIFEST}"
  exit 1
fi

if [[ -f gradlew ]]; then
  chmod +x gradlew || {
    dep_err "REAL_BUILD_FAILURE: unable to make gradlew executable."
    exit 1
  }
else
  dep_err "REAL_BUILD_FAILURE: gradlew is missing from repository root."
  exit 1
fi

load_android_env() {
  if [[ -f "${ANDROID_SDK_ENV}" ]]; then
    # shellcheck disable=SC1090
    set -a; source "${ANDROID_SDK_ENV}"; set +a
  fi
}

set_submodule_url() {
  local parent="$1" path="$2" url="$3" parent_dir rel
  parent_dir="$(dep_parent_worktree "${REPO_ROOT}" "${parent}")"
  rel="$(dep_rel_path "${parent}" "${path}")"
  git -C "${parent_dir}" config "submodule.${rel}.url" "${url}"
}

update_one_submodule() {
  local path="$1" parent="$2" primary="$3" fallbacks="$4" parent_dir rel url
  parent_dir="$(dep_parent_worktree "${REPO_ROOT}" "${parent}")"
  if [[ ! -d "${parent_dir}" ]]; then
    dep_warn "SUBMODULE_BLOCKER: parent worktree missing for ${path}: ${parent}"
    return 1
  fi
  rel="$(dep_rel_path "${parent}" "${path}")"

  dep_info "--- Updating submodule ${path} ---"
  set_submodule_url "${parent}" "${path}" "${primary}"
  if git -C "${parent_dir}" submodule update --init --recursive --jobs "${JOBS}" -- "${rel}"; then
    return 0
  fi

  [[ "${fallbacks}" == "-" ]] && fallbacks=""
  IFS=';' read -r -a fallback_array <<< "${fallbacks}"
  for url in "${fallback_array[@]}"; do
    [[ -z "${url}" ]] && continue
    dep_warn "DEPENDENCY_MIRROR_USED: retrying ${path} from approved mirror ${url}; the pinned gitlink SHA will still be verified."
    MIRROR_USED=1
    set_submodule_url "${parent}" "${path}" "${url}"
    if git -C "${parent_dir}" submodule update --init --recursive --jobs "${JOBS}" -- "${rel}"; then
      return 0
    fi
  done
  # Restore the primary remote so a failed mirror attempt does not leave the
  # worktree configured against a fallback URL.
  set_submodule_url "${parent}" "${path}" "${primary}"
  return 1
}

# validate_one_submodule sets PIN_MISMATCH for pin failures. It returns non-zero
# on ANY failure (pin or structural). The caller is responsible for translating
# a structural failure into SUBMODULE_BLOCKER (strict) or DEGRADED (static),
# which guarantees strict profiles cannot finish READY with a missing sentinel.
validate_one_submodule() {
  local path="$1" parent="$3" sentinel="$7"
  local expected actual

  expected="$(dep_expected_sha "${REPO_ROOT}" "${parent}" "${path}")"
  if [[ -z "${expected}" ]]; then
    dep_err "DEPENDENCY_PIN_MISMATCH: cannot read expected gitlink SHA for ${path} from parent ${parent}."
    PIN_MISMATCH=1
    return 1
  fi

  if ! dep_sentinel_present "${REPO_ROOT}" "${path}" "${sentinel}"; then
    dep_warn "SUBMODULE_BLOCKER: missing ${path}/${sentinel}"
    return 1
  fi

  actual="$(dep_actual_sha "${REPO_ROOT}" "${path}")"
  if [[ -z "${actual}" ]]; then
    dep_err "DEPENDENCY_PIN_MISMATCH: ${path} exists but is not a git worktree; cannot verify pinned SHA ${expected}."
    PIN_MISMATCH=1
    return 1
  fi

  if [[ "${actual}" != "${expected}" ]]; then
    dep_err "DEPENDENCY_PIN_MISMATCH: ${path} is at ${actual}, expected ${expected}."
    PIN_MISMATCH=1
    return 1
  fi

  dep_ok "${path}: ${actual}"
}

create_common_ktx_stub_if_required() {
  local stub="${REPO_ROOT}/media/libraries/common_ktx/proguard-rules.txt"
  if [[ -d "${REPO_ROOT}/media/libraries/common_ktx" && ! -f "${stub}" ]]; then
    : > "${stub}"
    dep_info "Created media/libraries/common_ktx/proguard-rules.txt stub (expected for pinned media commit)."
  fi
}

check_sdk() {
  load_android_env
  local sdk_dir="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
  if [[ -z "${sdk_dir}" || ! -d "${sdk_dir}" ]]; then
    dep_err "SDK_BLOCKER: ANDROID_HOME/ANDROID_SDK_ROOT is not set to an existing Android SDK directory."
    SDK_BLOCKER=1
    return 1
  fi
  if [[ "${REQUIRES_NINJA:-true}" == "true" ]] && ! command -v ninja >/dev/null 2>&1; then
    dep_err "SDK_BLOCKER: ninja is required for native builds but is not on PATH."
    SDK_BLOCKER=1
    return 1
  fi
  dep_ok "Android SDK: ${sdk_dir}"
}

dep_info "--- Configuring approved git URL fallback policy ---"
git submodule sync --recursive || true

# Initialise/update in manifest order. Nested entries work after their parent is
# fetched. The manifest is read on FD 3 so git commands inside the loop cannot
# consume it from stdin.
while IFS=$'\t' read -r path type parent primary fallbacks required_profiles sentinel release_blocking <&3; do
  [[ -z "${path:-}" || "${path:0:1}" == "#" ]] && continue
  dep_profile_requires_path "${required_profiles}" "${PROFILE}" || continue
  if ! update_one_submodule "${path}" "${parent}" "${primary}" "${fallbacks}"; then
    dep_warn "SUBMODULE_BLOCKER: failed to fetch required submodule ${path} for profile ${PROFILE}."
    SUBMODULE_BLOCKER=1
    if is_strict_profile; then
      break
    fi
  fi
done 3< "${SUBMODULE_MANIFEST}"

create_common_ktx_stub_if_required

dep_info "--- Verifying pinned submodule SHAs ---"
while IFS=$'\t' read -r path type parent primary fallbacks required_profiles sentinel release_blocking <&3; do
  [[ -z "${path:-}" || "${path:0:1}" == "#" ]] && continue
  dep_profile_requires_path "${required_profiles}" "${PROFILE}" || continue
  if ! validate_one_submodule "${path}" "${type}" "${parent}" "${primary}" "${fallbacks}" "${required_profiles}" "${sentinel}" "${release_blocking}"; then
    # Pin mismatches always fail closed (handled below). Structural failures
    # (missing sentinel / non-git worktree) block strict profiles but only
    # degrade static-review.
    if is_static_review && [[ "${PIN_MISMATCH}" -eq 0 ]]; then
      DEGRADED=1
    else
      SUBMODULE_BLOCKER=1
    fi
  fi
done 3< "${SUBMODULE_MANIFEST}"

if [[ "${PIN_MISMATCH}" -ne 0 ]]; then
  dep_err "DEPENDENCY_PIN_MISMATCH: refusing to continue with unverified dependency pins."
  exit 1
fi

if is_strict_profile; then
  check_sdk || true
fi

if [[ "${SUBMODULE_BLOCKER}" -ne 0 || "${SDK_BLOCKER}" -ne 0 ]]; then
  if is_static_review; then
    dep_warn "DEGRADED_STATIC_ONLY: dependency bootstrap incomplete; do not claim Gradle/build/test validation."
    exit 0
  fi
  if [[ "${SDK_BLOCKER}" -ne 0 ]]; then
    dep_err "SDK_BLOCKER: ${PROFILE} cannot continue until Android SDK/native tooling is available."
  fi
  if [[ "${SUBMODULE_BLOCKER}" -ne 0 ]]; then
    dep_err "SUBMODULE_BLOCKER: ${PROFILE} cannot continue until all required pinned submodules are available."
  fi
  exit 1
fi

if [[ "${DEGRADED}" -ne 0 ]]; then
  dep_warn "DEGRADED_STATIC_ONLY: static-review may continue without full native dependencies; no Gradle validation may be claimed."
  exit 0
fi

if [[ "${MIRROR_USED}" -ne 0 ]]; then
  dep_info "DEPENDENCY_MIRROR_USED: at least one approved mirror supplied the exact pinned commit."
fi

dep_info "--- git submodule status --recursive ---"
git submodule status --recursive || true

dep_info "READY: dependency bootstrap completed for profile ${PROFILE}."
