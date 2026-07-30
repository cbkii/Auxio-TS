#!/usr/bin/env bash
# Validate the staged primary Auxio release APK before it can be published.
set -euo pipefail

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || {
  printf '::error::Cannot resolve repository root.\n' >&2
  exit 1
}
cd -- "$repo_root" || exit 1

fail() {
  printf '::error::%s\n' "$*" >&2
  exit 1
}

apk=''
version_name=''
version_code=''
expected_signer=${EXPECTED_SIGNER_SHA256:-}
sha256_file=''
metadata_file=''
while (($#)); do
  case "$1" in
    --apk)
      (($# >= 2)) || fail '--apk requires a path.'
      apk=$2
      shift 2
      ;;
    --version-name)
      (($# >= 2)) || fail '--version-name requires a value.'
      version_name=$2
      shift 2
      ;;
    --version-code)
      (($# >= 2)) || fail '--version-code requires a value.'
      version_code=$2
      shift 2
      ;;
    --expected-signer)
      (($# >= 2)) || fail '--expected-signer requires a SHA-256 fingerprint.'
      expected_signer=$2
      shift 2
      ;;
    --sha256-file)
      (($# >= 2)) || fail '--sha256-file requires a path.'
      sha256_file=$2
      shift 2
      ;;
    --metadata-file)
      (($# >= 2)) || fail '--metadata-file requires a path.'
      metadata_file=$2
      shift 2
      ;;
    *)
      fail "Unknown argument: $1"
      ;;
  esac
done

[[ -f "$apk" ]] || fail "Release APK not found: ${apk:-<unset>}"
[[ -n "$version_name" ]] || fail 'Expected version name is required.'
[[ "$version_code" =~ ^[0-9]+$ ]] || fail 'Expected version code must be numeric.'
expected_signer=$(tr -d '[:space:]:' <<<"$expected_signer" | tr '[:lower:]' '[:upper:]')
[[ "$expected_signer" =~ ^[0-9A-F]{64}$ ]] ||
  fail 'Expected signer must be a 64-character SHA-256 fingerprint.'
[[ -f "$sha256_file" ]] || fail "Checksum sidecar not found: ${sha256_file:-<unset>}"
[[ -f "$metadata_file" ]] || fail "Metadata sidecar not found: ${metadata_file:-<unset>}"

sdk_root=${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}
apkanalyzer=${sdk_root:+${sdk_root}/cmdline-tools/latest/bin/apkanalyzer}
if [[ -z "$apkanalyzer" || ! -x "$apkanalyzer" ]]; then
  apkanalyzer=$(command -v apkanalyzer || true)
fi
apksigner=${sdk_root:+$(find "$sdk_root/build-tools" -type f -name apksigner -perm -u+x 2>/dev/null | sort -V | tail -n1)}
if [[ -z "$apksigner" || ! -x "$apksigner" ]]; then
  apksigner=$(command -v apksigner || true)
fi
[[ -n "$apkanalyzer" && -x "$apkanalyzer" ]] || fail 'apkanalyzer was not found.'
[[ -n "$apksigner" && -x "$apksigner" ]] || fail 'apksigner was not found.'

actual_package=$("$apkanalyzer" manifest application-id "$apk")
actual_version_name=$("$apkanalyzer" manifest version-name "$apk")
actual_version_code=$("$apkanalyzer" manifest version-code "$apk")
actual_min_sdk=$("$apkanalyzer" manifest min-sdk "$apk")
actual_target_sdk=$("$apkanalyzer" manifest target-sdk "$apk")
[[ "$actual_package" == com.tw.media ]] ||
  fail "Expected release application ID com.tw.media, got ${actual_package:-<empty>}."
[[ "$actual_version_name" == "$version_name" ]] ||
  fail "Expected version name $version_name, got ${actual_version_name:-<empty>}."
[[ "$actual_version_code" == "$version_code" ]] ||
  fail "Expected version code $version_code, got ${actual_version_code:-<empty>}."
[[ "$actual_min_sdk" == 24 ]] ||
  fail "Expected minimum SDK 24, got ${actual_min_sdk:-<empty>}."
[[ "$actual_target_sdk" == 36 ]] ||
  fail "Expected target SDK 36, got ${actual_target_sdk:-<empty>}."

actual_abis=$(
  unzip -Z1 "$apk" |
    sed -n -E 's#^lib/([^/]+)/.*#\1#p' |
    sort -u |
    paste -sd, -
)
expected_abis='arm64-v8a,armeabi-v7a,x86,x86_64'
[[ "$actual_abis" == "$expected_abis" ]] ||
  fail "Expected ABI set $expected_abis, got ${actual_abis:-<empty>}."

manifest=$("$apkanalyzer" manifest print "$apk")
if grep -Eq 'android:debuggable=(true|"true")' <<<"$manifest"; then
  fail 'Primary release APK must not be debuggable.'
fi

signing_report=$("$apksigner" verify --verbose --print-certs "$apk") ||
  fail 'Primary release APK is not validly signed.'
actual_signer=$(
  sed -n -E 's/^Signer #1 certificate SHA-256 digest: (.*)$/\1/p' <<<"$signing_report" |
    head -n1 |
    tr -d '[:space:]:' |
    tr '[:lower:]' '[:upper:]'
)
[[ "$actual_signer" == "$expected_signer" ]] ||
  fail "Release signer mismatch: expected $expected_signer, got ${actual_signer:-<empty>}."

for expected_metadata in \
  "application_id=${actual_package}" \
  "version_code=${actual_version_code}" \
  "version_name=${actual_version_name}" \
  "min_sdk=${actual_min_sdk}" \
  "target_sdk=${actual_target_sdk}" \
  "abis=${actual_abis}"; do
  grep -Fxq -- "$expected_metadata" "$metadata_file" ||
    fail "Release metadata sidecar is missing: $expected_metadata"
done
grep -Fqi -- "$actual_signer" "$metadata_file" ||
  fail 'Release metadata sidecar does not contain the verified signer fingerprint.'

checksum_dir=$(cd -- "$(dirname -- "$sha256_file")" && pwd -P)
checksum_name=$(basename -- "$sha256_file")
apk_dir=$(cd -- "$(dirname -- "$apk")" && pwd -P)
apk_name=$(basename -- "$apk")
checksum_target=$(awk 'NF >= 2 {print $2; exit}' "$sha256_file")
checksum_target=${checksum_target#\*}
[[ "$checksum_dir" == "$apk_dir" && "$checksum_target" == "$apk_name" ]] ||
  fail 'Checksum sidecar does not identify the staged release APK.'
(
  cd -- "$checksum_dir"
  sha256sum --check --strict "$checksum_name"
) || fail 'Release APK checksum sidecar does not verify.'

printf 'Primary Auxio release APK contracts: PASS\n'
