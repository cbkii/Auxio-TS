#!/usr/bin/env bash
# dependency-lib.sh — shared helpers for Auxio-TS dependency bootstrap/validation.
#
# This file is meant to be *sourced*, not executed directly:
#
#   source "$(dirname "${BASH_SOURCE[0]}")/dependency-lib.sh"
#
# It centralises the logic that previously drifted between
# scripts/bootstrap-dependencies.sh and scripts/check-submodules.sh:
#   - the canonical supported-profile list and validation,
#   - manifest paths and TSV field parsing,
#   - profile/path matching,
#   - parent-worktree resolution and gitlink/actual SHA lookup,
#   - sentinel presence checks,
#   - GitHub-Actions-friendly logging helpers and classification labels.
#
# Fetch/repair/update actions deliberately stay in bootstrap-dependencies.sh.
# Helpers here are read-only and side-effect free (aside from logging).

# Guard against double-sourcing.
if [[ -n "${_AUXIO_DEPENDENCY_LIB_SOURCED:-}" ]]; then
  return 0 2>/dev/null || exit 0
fi
_AUXIO_DEPENDENCY_LIB_SOURCED=1

# ── Canonical profiles ───────────────────────────────────────────────────────
# The single source of truth for valid bootstrap/validation profiles. Every
# entrypoint validates user/env-supplied profile values against this list so an
# unsupported profile can never silently filter out all manifest entries and
# exit "successfully" (see check-submodules profile-validation regression).
DEP_SUPPORTED_PROFILES=(static-review jvm-tests full-build release)

# Outcome classification labels (documented here so callers stay consistent):
#   READY                  dependencies satisfied for the requested profile
#   SNAPSHOT_LIMITATION    no .git (ZIP/snapshot); git submodule ops impossible
#   SUBMODULE_BLOCKER      required submodule/sentinel missing or unfetchable
#   DEPENDENCY_DIRTY_SUBMODULE submodule contains modified, staged, or untracked files
#   DEPENDENCY_MIRROR_USED an approved fallback mirror supplied the pinned commit
#   DEPENDENCY_PIN_MISMATCH checked-out SHA != expected gitlink SHA
#   SDK_BLOCKER            Android SDK / native tooling missing where required
#   DEGRADED_STATIC_ONLY   static-review continuing without full deps (honest)
#   REAL_BUILD_FAILURE     environment ready; a real build/config error occurred

# ── Logging helpers (GitHub Actions annotations) ─────────────────────────────
dep_err()  { echo "::error::$*"; }
dep_warn() { echo "::warning::$*"; }
dep_note() { echo "::notice::$*"; }
dep_info() { echo "$*"; }
dep_ok()   { echo "OK $*"; }

# ── Profile helpers ──────────────────────────────────────────────────────────
dep_supported_profiles_pipe() {
  local IFS='|'
  printf '%s\n' "${DEP_SUPPORTED_PROFILES[*]}"
}

dep_is_supported_profile() {
  local candidate="${1:-}" p
  for p in "${DEP_SUPPORTED_PROFILES[@]}"; do
    [[ "${candidate}" == "${p}" ]] && return 0
  done
  return 1
}

# dep_validate_profile <profile> [source-label]
# Fails fast (exit 2) when the profile is unsupported. Used for both CLI and
# environment-provided values so bad input never validates zero entries.
dep_validate_profile() {
  local candidate="${1:-}" source_label="${2:-profile}"
  if [[ -z "${candidate}" ]]; then
    dep_err "Missing value for ${source_label}; expected one of: $(dep_supported_profiles_pipe)"
    return 2
  fi
  if ! dep_is_supported_profile "${candidate}"; then
    dep_err "Unsupported ${source_label}: '${candidate}'; expected one of: $(dep_supported_profiles_pipe)"
    return 2
  fi
  return 0
}

# dep_profile_requires_path <required_profiles_csv> <profile>
dep_profile_requires_path() {
  local required_profiles="${1:-}" profile="${2:-}"
  [[ ",${required_profiles}," == *",${profile},"* ]]
}

# ── Manifest paths ───────────────────────────────────────────────────────────
dep_submodule_manifest() { printf '%s/ci/dependencies/submodules.tsv\n' "${1:?repo_root required}"; }
dep_android_sdk_env()    { printf '%s/ci/dependencies/android-sdk.env\n' "${1:?repo_root required}"; }

# ── Worktree / SHA helpers ───────────────────────────────────────────────────
# dep_parent_worktree <repo_root> <parent>
dep_parent_worktree() {
  local repo_root="${1:?repo_root required}" parent="${2:?parent required}"
  if [[ "${parent}" == "." ]]; then
    printf '%s\n' "${repo_root}"
  else
    printf '%s/%s\n' "${repo_root}" "${parent}"
  fi
}

# dep_rel_path <parent> <path> — submodule path relative to its parent worktree.
dep_rel_path() {
  local parent="${1:?parent required}" path="${2:?path required}"
  if [[ "${parent}" == "." ]]; then
    printf '%s\n' "${path}"
  else
    printf '%s\n' "${path#"${parent}"/}"
  fi
}

# dep_expected_sha <repo_root> <parent> <path>
# The pinned gitlink SHA the parent worktree expects at this submodule path.
dep_expected_sha() {
  local repo_root="$1" parent="$2" path="$3" parent_dir rel
  parent_dir="$(dep_parent_worktree "${repo_root}" "${parent}")"
  rel="$(dep_rel_path "${parent}" "${path}")"
  git -C "${parent_dir}" rev-parse "HEAD:${rel}" 2>/dev/null || true
}

# dep_actual_sha <repo_root> <path> — HEAD of the checked-out submodule worktree.
dep_actual_sha() {
  local repo_root="$1" path="$2"
  if [[ -d "${repo_root}/${path}/.git" || -f "${repo_root}/${path}/.git" ]]; then
    git -C "${repo_root}/${path}" rev-parse HEAD 2>/dev/null || true
  fi
}

# dep_sentinel_present <repo_root> <path> <sentinel>
dep_sentinel_present() {
  local repo_root="$1" path="$2" sentinel="$3"
  [[ -e "${repo_root}/${path}/${sentinel}" ]]
}

# Validate that a checked-out submodule worktree has no local modifications.
dep_validate_submodule_clean() {
  local repo_root="${1:?repo_root required}" path="${2:?path required}"
  local dirty=0 untracked

  if ! git -C "${repo_root}/${path}" diff --quiet --ignore-submodules=none; then
    dep_err "DEPENDENCY_DIRTY_SUBMODULE: ${path} has modified tracked content."
    dirty=1
  fi

  if ! git -C "${repo_root}/${path}" diff --cached --quiet --ignore-submodules=none; then
    dep_err "DEPENDENCY_DIRTY_SUBMODULE: ${path} has staged changes."
    dirty=1
  fi

  untracked="$(git -C "${repo_root}/${path}" ls-files -o --exclude-standard | sed -n '1,20p')"
  if [[ -n "${untracked}" ]]; then
    dep_err "DEPENDENCY_DIRTY_SUBMODULE: ${path} has untracked content:"
    printf '%s\n' "${untracked}" >&2
    dirty=1
  fi

  [[ "${dirty}" -eq 0 ]]
}

