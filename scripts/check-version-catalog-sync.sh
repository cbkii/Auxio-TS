#!/usr/bin/env bash
# check-version-catalog-sync.sh — guard against drift between the Gradle version
# catalogue (gradle/libs.versions.toml) and the authoritative version
# declarations that are still defined directly in the Groovy build scripts.
#
# Background: gradle/libs.versions.toml currently acts as a curated dependency
# *inventory*. The root build.gradle `buildscript.ext` block and the inline
# `plugins { ... version "..." }` declarations remain the values Gradle actually
# uses. Until the build scripts consume the catalogue via type-safe accessors,
# the same version can be written in two places and drift. This check fails CI
# whenever the catalogue and the authoritative Groovy declarations disagree, so
# the inventory stays trustworthy without performing a risky broad migration.
#
# Only versions that are genuinely duplicated in build.gradle are checked. Other
# catalogue entries (declared once, in app/musikr Gradle files or inventory-only)
# are intentionally out of scope here.
#
# Static check only: no Android SDK, network, or Gradle invocation required.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
cd "${REPO_ROOT}"

CATALOG="gradle/libs.versions.toml"
BUILD_GRADLE="build.gradle"

for f in "${CATALOG}" "${BUILD_GRADLE}"; do
  if [[ ! -f "${f}" ]]; then
    echo "::error::CATALOG_SYNC: required file missing: ${f}"
    exit 1
  fi
done

# value-between-quotes extractor (handles both '...' and "...")
extract_quoted() { sed -E "s/.*['\"]([^'\"]*)['\"][^'\"]*$/\1/"; }

# catalog_version <key> — value declared under [versions] in the catalogue.
# Anchored on `= "` so a [plugins] entry that reuses the key (e.g. `ksp = { id
# = ... }`) is never matched.
catalog_version() {
  local key="$1" line
  line="$(grep -m 1 -E "^${key}[[:space:]]*=[[:space:]]*\"" "${CATALOG}" || true)"
  [[ -z "${line}" ]] && return 1
  printf '%s\n' "${line}" | sed -E 's/.*=[[:space:]]*"([^"]*)".*/\1/'
}

# gradle_version <catalog_key> — authoritative value from build.gradle.
gradle_version() {
  local key="$1" line
  case "${key}" in
    agp)              line="$(grep -m 1 -E "agp_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    kotlin)           line="$(grep -m 1 -E "kotlin_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    kotlinCoroutines) line="$(grep -m 1 -E "kotlin_coroutines_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    navigation)       line="$(grep -m 1 -E "navigation_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    hilt)             line="$(grep -m 1 -E "hilt_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    room)             line="$(grep -m 1 -E "room_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    androidxCore)     line="$(grep -m 1 -E "core_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    desugarJdkLibs)   line="$(grep -m 1 -E "desugaring_version[[:space:]]*=" "${BUILD_GRADLE}")" ;;
    ksp)              line="$(grep -m 1 -E 'com\.google\.devtools\.ksp" version' "${BUILD_GRADLE}")" ;;
    spotless)         line="$(grep -m 1 -E 'com\.diffplug\.spotless" version' "${BUILD_GRADLE}")" ;;
    roborazzi)        line="$(grep -m 1 -E 'io\.github\.takahirom\.roborazzi" version' "${BUILD_GRADLE}")" ;;
    dokka)            line="$(grep -m 1 -E 'dokka-gradle-plugin:' "${BUILD_GRADLE}")" ;;
    *) return 2 ;;
  esac
  [[ -z "${line}" ]] && return 1
  case "${key}" in
    dokka) printf '%s\n' "${line}" | sed -E 's/.*dokka-gradle-plugin:([0-9][^"]*)".*/\1/' ;;
    *)     printf '%s\n' "${line}" | extract_quoted ;;
  esac
}

# Catalogue keys that are duplicated in build.gradle and must stay in sync.
CHECKED_KEYS=(
  agp kotlin kotlinCoroutines navigation hilt room androidxCore desugarJdkLibs
  ksp spotless roborazzi dokka
)

status=0
echo "--- Version catalogue / build.gradle sync (${#CHECKED_KEYS[@]} keys) ---"
for key in "${CHECKED_KEYS[@]}"; do
  cat_v="$(catalog_version "${key}" || true)"
  grd_v="$(gradle_version "${key}" || true)"
  if [[ -z "${cat_v}" ]]; then
    echo "::error::CATALOG_SYNC: '${key}' missing from ${CATALOG} [versions]."
    status=1
    continue
  fi
  if [[ -z "${grd_v}" ]]; then
    echo "::error::CATALOG_SYNC: could not read authoritative '${key}' from ${BUILD_GRADLE}."
    status=1
    continue
  fi
  if [[ "${cat_v}" != "${grd_v}" ]]; then
    echo "::error::CATALOG_SYNC: '${key}' drift — ${CATALOG}=${cat_v} but ${BUILD_GRADLE}=${grd_v}."
    status=1
    continue
  fi
  echo "OK ${key}: ${cat_v}"
done

if [[ "${status}" -ne 0 ]]; then
  echo "::error::CATALOG_SYNC: version catalogue is out of sync with build.gradle; update both to the same value."
  exit 1
fi

echo "READY: version catalogue matches authoritative build.gradle declarations."
