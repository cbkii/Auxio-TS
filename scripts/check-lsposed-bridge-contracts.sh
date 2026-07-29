#!/usr/bin/env bash

# Failure policy is explicit. This script validates a completed APK and never modifies the device.

APK_PATH=${1:-lsposed-bridge/build/outputs/apk/debug/lsposed-bridge-debug.apk}
EXPECTED_ENTRY='org.oxycblt.auxio.ts18bridge.Ts18LsposedBridgeModule'
EXPECTED_SCOPE='com.tw.music'
EXPECTED_APP_ID='org.oxycblt.auxio.ts18bridge.debug'
EXPECTED_MIN_SDK='29'
EXPECTED_TARGET_SDK='36'
ERRORS=0

log() { printf '[INFO] %s\n' "$*" >&2; }
error() { ERRORS=$((ERRORS + 1)); printf '[ERROR] %s\n' "$*" >&2; }

require_command() {
  command -v "$1" >/dev/null 2>&1 || {
    error "Required command is unavailable: $1"
    return 1
  }
}

find_apkanalyzer() {
  if command -v apkanalyzer >/dev/null 2>&1; then
    command -v apkanalyzer
    return 0
  fi
  local sdk_root=${ANDROID_SDK_ROOT:-${ANDROID_HOME:-}}
  [[ -n $sdk_root ]] || return 1
  find "$sdk_root/cmdline-tools" -type f -name apkanalyzer -perm -u+x 2>/dev/null |
    sort | tail -n 1
}

check_exact_entry() {
  local path=$1 expected=$2 actual
  actual=$(unzip -p "$APK_PATH" "$path" 2>/dev/null) || {
    error "Missing APK entry: $path"
    return
  }
  actual=${actual%$'\n'}
  if [[ $actual != "$expected" ]]; then
    error "$path mismatch: expected '$expected', got '$actual'"
  fi
}

log "Validating LSPosed bridge APK: $APK_PATH"
require_command unzip
[[ -f $APK_PATH ]] || error "APK does not exist: $APK_PATH"

if ((ERRORS == 0)); then
  check_exact_entry 'META-INF/xposed/java_init.list' "$EXPECTED_ENTRY"
  check_exact_entry 'META-INF/xposed/scope.list' "$EXPECTED_SCOPE"

  module_prop=$(unzip -p "$APK_PATH" 'META-INF/xposed/module.prop' 2>/dev/null) || {
    error 'Missing META-INF/xposed/module.prop'
    module_prop=''
  }
  for required in 'minApiVersion=100' 'targetApiVersion=100' 'staticScope=true'; do
    grep -Fxq "$required" <<<"$module_prop" || error "module.prop is missing: $required"
  done

  apkanalyzer_bin=$(find_apkanalyzer || true)
  if [[ -z $apkanalyzer_bin || ! -x $apkanalyzer_bin ]]; then
    error 'apkanalyzer is required to prove manifest and defined-class contracts'
  else
    app_id=$($apkanalyzer_bin manifest application-id "$APK_PATH" 2>/dev/null) || {
      error 'Unable to read APK application ID'
      app_id=''
    }
    [[ $app_id == "$EXPECTED_APP_ID" ]] ||
      error "Unexpected debug application ID: ${app_id:-<empty>}"

    min_sdk=$($apkanalyzer_bin manifest min-sdk "$APK_PATH" 2>/dev/null) || {
      error 'Unable to read minimum SDK'
      min_sdk=''
    }
    [[ $min_sdk == "$EXPECTED_MIN_SDK" ]] ||
      error "Expected minimum SDK $EXPECTED_MIN_SDK, got ${min_sdk:-<empty>}"

    target_sdk=$($apkanalyzer_bin manifest target-sdk "$APK_PATH" 2>/dev/null) || {
      error 'Unable to read target SDK'
      target_sdk=''
    }
    [[ $target_sdk == "$EXPECTED_TARGET_SDK" ]] ||
      error "Expected target SDK $EXPECTED_TARGET_SDK, got ${target_sdk:-<empty>}"

    manifest=$($apkanalyzer_bin manifest print "$APK_PATH" 2>/dev/null) || {
      error 'Unable to decode AndroidManifest.xml'
      manifest=''
    }
    if grep -q '<uses-permission' <<<"$manifest"; then
      error 'Bridge APK must not request Android permissions'
    fi

    defined=$($apkanalyzer_bin dex packages --defined-only "$APK_PATH" 2>/dev/null) || {
      error 'Unable to inspect defined DEX classes'
      defined=''
    }
    grep -Fq "$EXPECTED_ENTRY" <<<"$defined" || error 'Bridge entry class is not defined in DEX'
    if grep -Fq 'io.github.libxposed' <<<"$defined"; then
      error 'compile-only libxposed API classes were packaged into the bridge APK'
    fi
  fi
fi

if ((ERRORS != 0)); then
  printf '[SUMMARY] LSPosed bridge contract validation failed with %d error(s).\n' "$ERRORS" >&2
  exit 1
fi

printf '[SUMMARY] LSPosed API 100 bridge APK contracts passed.\n' >&2
