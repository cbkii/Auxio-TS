#!/usr/bin/env bash

# Validate the completed bridge against its exact paired Auxio target APK.
set -uo pipefail

ROOT="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
# shellcheck source=scripts/lib/apksigner-certificate.sh
source "${ROOT}/scripts/lib/apksigner-certificate.sh"

VARIANT=debug
BRIDGE_APK=''
TARGET_APK=''
while (($#)); do
  case "$1" in
    --variant)
      (($# >= 2)) || { printf '[ERROR] --variant requires a value.\n' >&2; exit 2; }
      VARIANT=$2
      shift 2
      ;;
    --apk)
      (($# >= 2)) || { printf '[ERROR] --apk requires a value.\n' >&2; exit 2; }
      BRIDGE_APK=$2
      shift 2
      ;;
    --target-apk)
      (($# >= 2)) || { printf '[ERROR] --target-apk requires a value.\n' >&2; exit 2; }
      TARGET_APK=$2
      shift 2
      ;;
    *)
      printf '[ERROR] Unknown argument: %s\n' "$1" >&2
      exit 2
      ;;
  esac
done

case "$VARIANT" in
  debug)
    EXPECTED_BRIDGE_ID='org.oxycblt.auxio.ts18bridge.debug'
    EXPECTED_TARGET_ID='com.tw.media.debug'
    : "${BRIDGE_APK:=${ROOT}/lsposed-bridge/build/outputs/apk/debug/lsposed-bridge-debug.apk}"
    ;;
  release)
    EXPECTED_BRIDGE_ID='org.oxycblt.auxio.ts18bridge'
    EXPECTED_TARGET_ID='com.tw.media'
    : "${BRIDGE_APK:=${ROOT}/lsposed-bridge/build/outputs/apk/release/lsposed-bridge-release.apk}"
    ;;
  *)
    printf '[ERROR] Unsupported variant: %s\n' "$VARIANT" >&2
    exit 2
    ;;
esac

[[ -n $TARGET_APK ]] || { printf '[ERROR] --target-apk is required.\n' >&2; exit 2; }
ERRORS=0
error() { ERRORS=$((ERRORS + 1)); printf '[ERROR] %s\n' "$*" >&2; }

find_tool() {
  local name=$1 root=${ANDROID_SDK_ROOT:-${ANDROID_HOME:-}}
  if command -v "$name" >/dev/null 2>&1; then command -v "$name"; return; fi
  [[ -n $root ]] || return 1
  if [[ $name == apksigner ]]; then
    find "$root/build-tools" -type f -name "$name" -perm -u+x 2>/dev/null | sort -V | tail -n1
  else
    find "$root/cmdline-tools" -type f -name "$name" -perm -u+x 2>/dev/null | sort | tail -n1
  fi
}

for path in "$BRIDGE_APK" "$TARGET_APK"; do [[ -f $path ]] || error "APK missing: $path"; done
for command in unzip python3; do command -v "$command" >/dev/null 2>&1 || error "Required command missing: $command"; done
APKSIGNER_BIN=${APKSIGNER_BIN:-$(find_tool apksigner || true)}
APKANALYZER_BIN=${APKANALYZER_BIN:-$(find_tool apkanalyzer || true)}
[[ -x ${APKSIGNER_BIN:-} ]] || error 'apksigner is required'
[[ -x ${APKANALYZER_BIN:-} ]] || error 'apkanalyzer is required'

TARGET_SIGNER=''
if ((ERRORS == 0)); then
  bridge_id=$($APKANALYZER_BIN manifest application-id "$BRIDGE_APK" 2>/dev/null) || bridge_id=''
  target_id=$($APKANALYZER_BIN manifest application-id "$TARGET_APK" 2>/dev/null) || target_id=''
  [[ $bridge_id == "$EXPECTED_BRIDGE_ID" ]] || error "Bridge app id mismatch: ${bridge_id:-<empty>}"
  [[ $target_id == "$EXPECTED_TARGET_ID" ]] || error "Target app id mismatch: ${target_id:-<empty>}"

  min_sdk=$($APKANALYZER_BIN manifest min-sdk "$BRIDGE_APK" 2>/dev/null) || min_sdk=''
  target_sdk=$($APKANALYZER_BIN manifest target-sdk "$BRIDGE_APK" 2>/dev/null) || target_sdk=''
  [[ $min_sdk == 29 ]] || error "Bridge minSdk must be 29, got ${min_sdk:-<empty>}"
  [[ $target_sdk == 36 ]] || error "Bridge targetSdk must be 36, got ${target_sdk:-<empty>}"

  manifest=$($APKANALYZER_BIN manifest print "$BRIDGE_APK" 2>/dev/null) || manifest=''
  grep -q '<uses-permission' <<<"$manifest" && error 'Bridge must not request Android permissions'
  if [[ $VARIANT == release ]] && grep -Eq 'android:debuggable=(true|"true")' <<<"$manifest"; then
    error 'Release bridge must not be debuggable'
  fi

  target_report=$($APKSIGNER_BIN verify --verbose --print-certs "$TARGET_APK" 2>&1) || {
    error 'Target APK signature verification failed'; target_report='';
  }
  if [[ -n $target_report ]]; then
    TARGET_SIGNER=$(extract_apksigner_certificate_sha256 "$target_report") || {
      error 'Target APK must resolve to exactly one current signer'; TARGET_SIGNER='';
    }
  fi
  [[ $TARGET_SIGNER =~ ^[0-9A-F]{64}$ ]] || error 'Target signer is malformed'

  bridge_report=$($APKSIGNER_BIN verify --verbose --print-certs "$BRIDGE_APK" 2>&1) || {
    error 'Bridge APK signature verification failed'; bridge_report='';
  }
  if [[ $VARIANT == release && -n $bridge_report ]]; then
    grep -Fq 'CN=Android Debug' <<<"$bridge_report" && error 'Release bridge uses Android debug certificate'
    expected=${EXPECTED_SIGNER_SHA256:-}
    expected=$(printf '%s' "${expected//:/}" | tr '[:lower:]' '[:upper:]')
    if [[ ! $expected =~ ^[0-9A-F]{64}$ ]]; then
      error 'EXPECTED_SIGNER_SHA256 must be a 64-hex release bridge signer'
    else
      actual=$(extract_apksigner_certificate_sha256 "$bridge_report") || actual=''
      [[ $actual == "$expected" ]] || error 'Release bridge signer does not match configured key'
    fi
  fi
fi

check_entry() {
  local path=$1 expected=$2 actual
  actual=$(unzip -p "$BRIDGE_APK" "$path" 2>/dev/null) || { error "Missing $path"; return; }
  actual=${actual%$'\n'}
  [[ $actual == "$expected" ]] || error "$path mismatch: ${actual:-<empty>}"
}

if ((ERRORS == 0)); then
  check_entry META-INF/xposed/java_init.list org.oxycblt.auxio.ts18bridge.Ts18LsposedBridgeModule
  check_entry META-INF/xposed/scope.list com.tw.music
  module_prop=$(unzip -p "$BRIDGE_APK" META-INF/xposed/module.prop 2>/dev/null) || module_prop=''
  for line in minApiVersion=100 targetApiVersion=100 staticScope=true; do
    grep -Fxq "$line" <<<"$module_prop" || error "module.prop missing $line"
  done
fi

if ((ERRORS == 0)); then
  python3 "${ROOT}/scripts/lib/check-lsposed-bridge-dex.py" \
    "$BRIDGE_APK" \
    org.oxycblt.auxio.ts18bridge.Ts18LsposedBridgeModule \
    "$TARGET_SIGNER" \
    "$EXPECTED_TARGET_ID" || ERRORS=$((ERRORS + 1))
fi

if ((ERRORS != 0)); then
  printf '[SUMMARY] LSPosed bridge validation failed with %d error(s).\n' "$ERRORS" >&2
  exit 1
fi
printf '[SUMMARY] LSPosed bridge and paired target contracts passed.\n' >&2
