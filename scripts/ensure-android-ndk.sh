#!/usr/bin/env bash
# Install/verify the exact NDK selected by the root Gradle build before configuring
# native Android modules. Invoked by strict dependency bootstrap on CI and locally.
set -euo pipefail

repo_root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
sdk_root="${1:-${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}}"
if [[ -z "${sdk_root}" || ! -d "${sdk_root}" ]]; then
  printf 'SDK_BLOCKER: Android SDK directory does not exist.\n' >&2
  exit 1
fi
ndk_version="$(sed -n -E 's/^[[:space:]]*ndk_version[[:space:]]*=[[:space:]]*[^0-9]*([0-9]+\.[0-9]+\.[0-9]+).*/\1/p' "${repo_root}/build.gradle" | head -n 1)"
if [[ ! "${ndk_version}" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
  printf 'SDK_BLOCKER: Unable to resolve a pinned NDK version in build.gradle.\n' >&2
  exit 1
fi

ndk_dir="${sdk_root}/ndk/${ndk_version}"
source_properties="${ndk_dir}/source.properties"
is_installed() {
  [[ -f "${source_properties}" ]] &&
    grep -Fxq "Pkg.Revision = ${ndk_version}" "${source_properties}"
}
if is_installed; then
  printf '[INFO] Verified Android NDK %s\n' "${ndk_version}"
  exit 0
fi

sdkmanager_bin="${sdk_root}/cmdline-tools/latest/bin/sdkmanager"
if [[ ! -x "${sdkmanager_bin}" ]]; then
  sdkmanager_bin="$(command -v sdkmanager || true)"
fi
if [[ -z "${sdkmanager_bin}" || ! -x "${sdkmanager_bin}" ]]; then
  printf 'SDK_BLOCKER: sdkmanager unavailable to install pinned NDK %s.\n' "${ndk_version}" >&2
  exit 1
fi
printf '[INFO] Installing project-pinned Android NDK %s\n' "${ndk_version}"
timeout 15m "${sdkmanager_bin}" --sdk_root="${sdk_root}" --install "ndk;${ndk_version}"
if ! is_installed; then
  printf 'SDK_BLOCKER: NDK %s missing or has unexpected revision after install.\n' "${ndk_version}" >&2
  exit 1
fi
printf '[INFO] Verified Android NDK %s\n' "${ndk_version}"
