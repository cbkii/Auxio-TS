#!/usr/bin/env bash
# Offline, deterministic test of the NDK bootstrap contract.
set -euo pipefail
repo_root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
version="$(sed -n -E "s/^[[:space:]]*ndk_version[[:space:]]*=[[:space:]]*'([^']+)'.*/\1/p" "${repo_root}/build.gradle" | head -n1)"
tmp="$(mktemp -d)"
trap 'rm -rf -- "${tmp}"' EXIT
sdk="${tmp}/sdk"
mkdir -p "${sdk}/cmdline-tools/latest/bin"
cat > "${sdk}/cmdline-tools/latest/bin/sdkmanager" <<'MOCK'
#!/usr/bin/env bash
set -euo pipefail
sdk_root=""
package=""
for arg in "$@"; do
  case "$arg" in
    --sdk_root=*) sdk_root="${arg#--sdk_root=}" ;;
    ndk\;*) package="${arg#ndk;}" ;;
  esac
done
[[ -n "${sdk_root}" && -n "${package}" ]] || exit 3
mkdir -p "${sdk_root}/ndk/${package}"
printf 'Pkg.Revision = %s\n' "${package}" > "${sdk_root}/ndk/${package}/source.properties"
MOCK
chmod +x "${sdk}/cmdline-tools/latest/bin/sdkmanager"
bash "${repo_root}/scripts/ensure-android-ndk.sh" "${sdk}"
test -f "${sdk}/ndk/${version}/source.properties"
# A second invocation must use the installed version without running sdkmanager.
rm "${sdk}/cmdline-tools/latest/bin/sdkmanager"
bash "${repo_root}/scripts/ensure-android-ndk.sh" "${sdk}"
if bash "${repo_root}/scripts/ensure-android-ndk.sh" "${tmp}/missing" >/dev/null 2>&1; then
  echo 'Missing Android SDK unexpectedly accepted' >&2
  exit 1
fi
echo 'NDK bootstrap contract tests: PASS'
