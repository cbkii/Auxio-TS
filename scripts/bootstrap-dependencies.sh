#!/usr/bin/env bash
# Canonical dependency bootstrap and validation entry point for Auxio-TS.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
SUBMODULE_MANIFEST="${REPO_ROOT}/ci/dependencies/submodules.tsv"
ANDROID_SDK_ENV="${REPO_ROOT}/ci/dependencies/android-sdk.env"
PROFILE="full-build"
JOBS="${BOOTSTRAP_JOBS:-4}"
MIRROR_USED=0
DEGRADED=0
PIN_MISMATCH=0
SUBMODULE_BLOCKER=0
SDK_BLOCKER=0

usage() {
  cat <<USAGE
Usage: bash scripts/bootstrap-dependencies.sh --profile <static-review|jvm-tests|full-build|release>

Profiles:
  static-review  Best-effort dependencies for source/script review; may print DEGRADED_STATIC_ONLY.
  jvm-tests      Strict dependency bootstrap for Gradle JVM test tasks in this repo.
  full-build     Strict dependency bootstrap for debug/full CI builds.
  release        Strict release bootstrap; all pins and release-blocking deps must be present.
USAGE
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --profile)
      PROFILE="${2:-}"
      shift 2
      ;;
    --help|-h)
      usage
      exit 0
      ;;
    *)
      echo "::error::Unknown argument: $1"
      usage
      exit 2
      ;;
  esac
done

case "${PROFILE}" in
  static-review|jvm-tests|full-build|release) ;;
  *)
    echo "::error::Unknown bootstrap profile: ${PROFILE}"
    usage
    exit 2
    ;;
esac

cd "${REPO_ROOT}"

echo "--- Auxio-TS dependency bootstrap ---"
echo "profile=${PROFILE}"

is_static_review() { [[ "${PROFILE}" == "static-review" ]]; }
is_strict_profile() { [[ "${PROFILE}" != "static-review" ]]; }

if [[ ! -e .git ]]; then
  echo "::error::SNAPSHOT_LIMITATION: no .git directory detected (ZIP/snapshot checkout)."
  if is_static_review; then
    echo "::warning::DEGRADED_STATIC_ONLY: source/script review may continue, but Gradle/build/test validation must not be claimed."
    exit 0
  fi
  echo "::error::SUBMODULE_BLOCKER: ${PROFILE} requires a real git checkout so pinned submodule SHAs can be fetched and verified."
  exit 1
fi

if [[ ! -f "${SUBMODULE_MANIFEST}" ]]; then
  echo "::error::REAL_BUILD_FAILURE: missing dependency manifest: ${SUBMODULE_MANIFEST}"
  exit 1
fi

if [[ -f gradlew ]]; then
  chmod +x gradlew || {
    echo "::error::REAL_BUILD_FAILURE: unable to make gradlew executable."
    exit 1
  }
else
  echo "::error::REAL_BUILD_FAILURE: gradlew is missing from repository root."
  exit 1
fi

load_android_env() {
  if [[ -f "${ANDROID_SDK_ENV}" ]]; then
    # shellcheck disable=SC1090
    set -a; source "${ANDROID_SDK_ENV}"; set +a
  fi
}

profile_requires_path() {
  local required_profiles="$1"
  [[ ",${required_profiles}," == *",${PROFILE},"* ]]
}

parent_worktree() {
  local parent="$1"
  if [[ "${parent}" == "." ]]; then
    printf '%s\n' "${REPO_ROOT}"
  else
    printf '%s/%s\n' "${REPO_ROOT}" "${parent}"
  fi
}

expected_sha_for() {
  local parent="$1" path="$2" rel="$2" parent_dir
  parent_dir="$(parent_worktree "${parent}")"
  if [[ "${parent}" != "." ]]; then
    rel="${path#${parent}/}"
  fi
  git -C "${parent_dir}" rev-parse "HEAD:${rel}" 2>/dev/null || true
}

actual_sha_for() {
  local path="$1"
  if [[ -d "${REPO_ROOT}/${path}/.git" || -f "${REPO_ROOT}/${path}/.git" ]]; then
    git -C "${REPO_ROOT}/${path}" rev-parse HEAD 2>/dev/null || true
  fi
}

sentinel_present() {
  local path="$1" sentinel="$2"
  [[ -e "${REPO_ROOT}/${path}/${sentinel}" ]]
}

set_submodule_url() {
  local parent="$1" path="$2" url="$3" rel="$2" parent_dir
  parent_dir="$(parent_worktree "${parent}")"
  if [[ "${parent}" != "." ]]; then
    rel="${path#${parent}/}"
  fi
  git -C "${parent_dir}" config "submodule.${rel}.url" "${url}"
}

update_one_submodule() {
  local path="$1" parent="$2" primary="$3" fallbacks="$4" rel="$1" parent_dir url
  parent_dir="$(parent_worktree "${parent}")"
  if [[ ! -d "${parent_dir}" ]]; then
    echo "::warning::SUBMODULE_BLOCKER: parent worktree missing for ${path}: ${parent}"
    return 1
  fi
  if [[ "${parent}" != "." ]]; then
    rel="${path#${parent}/}"
  fi

  echo "--- Updating submodule ${path} ---"
  set_submodule_url "${parent}" "${path}" "${primary}"
  if git -C "${parent_dir}" submodule update --init --recursive --jobs "${JOBS}" -- "${rel}"; then
    return 0
  fi

  [[ "${fallbacks}" == "-" ]] && fallbacks=""
  IFS=';' read -r -a fallback_array <<< "${fallbacks}"
  for url in "${fallback_array[@]}"; do
    [[ -z "${url}" ]] && continue
    echo "::warning::DEPENDENCY_MIRROR_USED: retrying ${path} from approved mirror ${url}; the pinned gitlink SHA will still be verified."
    MIRROR_USED=1
    set_submodule_url "${parent}" "${path}" "${url}"
    if git -C "${parent_dir}" submodule update --init --recursive --jobs "${JOBS}" -- "${rel}"; then
      return 0
    fi
  done
  return 1
}

validate_one_submodule() {
  local path="$1" type="$2" parent="$3" primary="$4" fallbacks="$5" required_profiles="$6" sentinel="$7" release_blocking="$8"
  local expected actual
  if ! profile_requires_path "${required_profiles}"; then
    echo "SKIP ${path}: not required for profile ${PROFILE}"
    return 0
  fi

  expected="$(expected_sha_for "${parent}" "${path}")"
  if [[ -z "${expected}" ]]; then
    echo "::error::DEPENDENCY_PIN_MISMATCH: cannot read expected gitlink SHA for ${path} from parent ${parent}."
    PIN_MISMATCH=1
    return 1
  fi

  if ! sentinel_present "${path}" "${sentinel}"; then
    echo "::warning::SUBMODULE_BLOCKER: missing ${path}/${sentinel}"
    return 1
  fi

  actual="$(actual_sha_for "${path}")"
  if [[ -z "${actual}" ]]; then
    echo "::error::DEPENDENCY_PIN_MISMATCH: ${path} exists but is not a git worktree; cannot verify pinned SHA ${expected}."
    PIN_MISMATCH=1
    return 1
  fi

  if [[ "${actual}" != "${expected}" ]]; then
    echo "::error::DEPENDENCY_PIN_MISMATCH: ${path} is at ${actual}, expected ${expected}."
    PIN_MISMATCH=1
    return 1
  fi

  echo "OK ${path}: ${actual}"
}

create_common_ktx_stub_if_required() {
  local stub="${REPO_ROOT}/media/libraries/common_ktx/proguard-rules.txt"
  if [[ -d "${REPO_ROOT}/media/libraries/common_ktx" && ! -f "${stub}" ]]; then
    : > "${stub}"
    echo "::warning::UPSTREAM_MEDIA_QUIRK: created media/libraries/common_ktx/proguard-rules.txt stub."
  fi
}

check_sdk() {
  load_android_env
  local sdk_dir="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
  if [[ -z "${sdk_dir}" || ! -d "${sdk_dir}" ]]; then
    echo "::error::SDK_BLOCKER: ANDROID_HOME/ANDROID_SDK_ROOT is not set to an existing Android SDK directory."
    SDK_BLOCKER=1
    return 1
  fi
  if [[ "${REQUIRES_NINJA:-true}" == "true" ]] && ! command -v ninja >/dev/null 2>&1; then
    echo "::error::SDK_BLOCKER: ninja is required for native builds but is not on PATH."
    SDK_BLOCKER=1
    return 1
  fi
  echo "OK Android SDK: ${sdk_dir}"
}

echo "--- Configuring approved git URL fallback policy ---"
git submodule sync --recursive || true

# Initialise/update in manifest order.  Nested entries work after their parent is fetched.
while IFS=$'\t' read -r path type parent primary fallbacks required_profiles sentinel release_blocking; do
  [[ -z "${path:-}" || "${path:0:1}" == "#" ]] && continue
  profile_requires_path "${required_profiles}" || continue
  if ! update_one_submodule "${path}" "${parent}" "${primary}" "${fallbacks}"; then
    echo "::warning::SUBMODULE_BLOCKER: failed to fetch required submodule ${path} for profile ${PROFILE}."
    SUBMODULE_BLOCKER=1
    if is_strict_profile; then
      break
    fi
  fi
done < "${SUBMODULE_MANIFEST}"

create_common_ktx_stub_if_required

echo "--- Verifying pinned submodule SHAs ---"
while IFS=$'\t' read -r path type parent primary fallbacks required_profiles sentinel release_blocking; do
  [[ -z "${path:-}" || "${path:0:1}" == "#" ]] && continue
  profile_requires_path "${required_profiles}" || continue
  if ! validate_one_submodule "${path}" "${type}" "${parent}" "${primary}" "${fallbacks}" "${required_profiles}" "${sentinel}" "${release_blocking}"; then
    if is_static_review && [[ "${PIN_MISMATCH}" -eq 0 ]]; then
      DEGRADED=1
    fi
  fi
done < "${SUBMODULE_MANIFEST}"

if [[ "${PIN_MISMATCH}" -ne 0 ]]; then
  echo "::error::DEPENDENCY_PIN_MISMATCH: refusing to continue with unverified dependency pins."
  exit 1
fi

if is_strict_profile; then
  check_sdk || true
fi

if [[ "${SUBMODULE_BLOCKER}" -ne 0 || "${SDK_BLOCKER}" -ne 0 ]]; then
  if is_static_review; then
    echo "::warning::DEGRADED_STATIC_ONLY: dependency bootstrap incomplete; do not claim Gradle/build/test validation."
    exit 0
  fi
  if [[ "${SDK_BLOCKER}" -ne 0 ]]; then
    echo "::error::SDK_BLOCKER: ${PROFILE} cannot continue until Android SDK/native tooling is available."
  else
    echo "::error::SUBMODULE_BLOCKER: ${PROFILE} cannot continue until all required pinned submodules are available."
  fi
  exit 1
fi

if [[ "${DEGRADED}" -ne 0 ]]; then
  echo "::warning::DEGRADED_STATIC_ONLY: static-review may continue without full native dependencies; no Gradle validation may be claimed."
  exit 0
fi

if [[ "${MIRROR_USED}" -ne 0 ]]; then
  echo "DEPENDENCY_MIRROR_USED: at least one approved mirror supplied the exact pinned commit."
fi

echo "--- git submodule status --recursive ---"
git submodule status --recursive || true

echo "READY: dependency bootstrap completed for profile ${PROFILE}."
