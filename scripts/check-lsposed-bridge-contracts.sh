#!/usr/bin/env bash

# Failure policy is explicit. This script validates a completed APK and never modifies the device.

VARIANT=debug
APK_PATH=''
while (($#)); do
  case "$1" in
    --variant)
      (($# >= 2)) || {
        printf '[ERROR] --variant requires debug or release.\n' >&2
        exit 2
      }
      VARIANT=$2
      shift 2
      ;;
    --apk)
      (($# >= 2)) || {
        printf '[ERROR] --apk requires a path.\n' >&2
        exit 2
      }
      APK_PATH=$2
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
    EXPECTED_APP_ID='org.oxycblt.auxio.ts18bridge.debug'
    : "${APK_PATH:=lsposed-bridge/build/outputs/apk/debug/lsposed-bridge-debug.apk}"
    ;;
  release)
    EXPECTED_APP_ID='org.oxycblt.auxio.ts18bridge'
    : "${APK_PATH:=lsposed-bridge/build/outputs/apk/release/lsposed-bridge-release.apk}"
    ;;
  *)
    printf '[ERROR] Unsupported variant: %s\n' "$VARIANT" >&2
    exit 2
    ;;
esac

EXPECTED_ENTRY='org.oxycblt.auxio.ts18bridge.Ts18LsposedBridgeModule'
EXPECTED_SCOPE='com.tw.music'
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

check_defined_dex_classes() {
  python3 - "$APK_PATH" "$EXPECTED_ENTRY" <<'PY'
import re
import struct
import sys
import zipfile
from pathlib import Path

apk_path = Path(sys.argv[1])
expected_descriptor = "L" + sys.argv[2].replace(".", "/") + ";"
forbidden_prefix = "Lio/github/libxposed/"


def u32(data: bytes, offset: int) -> int:
    if offset < 0 or offset + 4 > len(data):
        raise ValueError(f"u32 offset outside DEX: {offset}")
    return struct.unpack_from("<I", data, offset)[0]


def read_uleb128(data: bytes, offset: int) -> tuple[int, int]:
    value = 0
    shift = 0
    for _ in range(5):
        if offset >= len(data):
            raise ValueError("truncated ULEB128")
        byte = data[offset]
        offset += 1
        value |= (byte & 0x7F) << shift
        if byte & 0x80 == 0:
            return value, offset
        shift += 7
    raise ValueError("invalid ULEB128")


def read_string(data: bytes, string_ids_off: int, index: int) -> str:
    string_data_off = u32(data, string_ids_off + index * 4)
    _, cursor = read_uleb128(data, string_data_off)
    end = data.find(b"\x00", cursor)
    if end < 0:
        raise ValueError("unterminated DEX string")
    return data[cursor:end].decode("utf-8", errors="strict")


def defined_descriptors(data: bytes, dex_name: str) -> set[str]:
    if len(data) < 112 or data[:4] != b"dex\n":
        raise ValueError(f"{dex_name}: invalid DEX header")

    string_ids_size = u32(data, 56)
    string_ids_off = u32(data, 60)
    type_ids_size = u32(data, 64)
    type_ids_off = u32(data, 68)
    class_defs_size = u32(data, 96)
    class_defs_off = u32(data, 100)

    if string_ids_off + string_ids_size * 4 > len(data):
        raise ValueError(f"{dex_name}: string_ids outside DEX")
    if type_ids_off + type_ids_size * 4 > len(data):
        raise ValueError(f"{dex_name}: type_ids outside DEX")
    if class_defs_off + class_defs_size * 32 > len(data):
        raise ValueError(f"{dex_name}: class_defs outside DEX")

    descriptors: set[str] = set()
    for class_number in range(class_defs_size):
        class_idx = u32(data, class_defs_off + class_number * 32)
        if class_idx >= type_ids_size:
            raise ValueError(f"{dex_name}: class_idx outside type_ids")
        descriptor_idx = u32(data, type_ids_off + class_idx * 4)
        if descriptor_idx >= string_ids_size:
            raise ValueError(f"{dex_name}: descriptor_idx outside string_ids")
        descriptors.add(read_string(data, string_ids_off, descriptor_idx))
    return descriptors


try:
    with zipfile.ZipFile(apk_path) as apk:
        dex_names = sorted(
            (name for name in apk.namelist() if re.fullmatch(r"classes(?:\d+)?\.dex", name)),
            key=lambda name: (len(name), name),
        )
        if not dex_names:
            raise ValueError("APK contains no classes*.dex files")

        all_defined: set[str] = set()
        for dex_name in dex_names:
            descriptors = defined_descriptors(apk.read(dex_name), dex_name)
            all_defined.update(descriptors)
            print(f"[INFO] {dex_name}: {len(descriptors)} defined classes", file=sys.stderr)

    if expected_descriptor not in all_defined:
        print(
            f"[ERROR] bridge entry class is not defined: {expected_descriptor}",
            file=sys.stderr,
        )
        raise SystemExit(1)

    forbidden = sorted(item for item in all_defined if item.startswith(forbidden_prefix))
    if forbidden:
        print(
            "[ERROR] compile-only libxposed API classes were packaged into the bridge APK:",
            file=sys.stderr,
        )
        for descriptor in forbidden[:20]:
            print(f"  {descriptor}", file=sys.stderr)
        if len(forbidden) > 20:
            print(f"  ... and {len(forbidden) - 20} more", file=sys.stderr)
        raise SystemExit(1)

    print(
        f"[INFO] DEX contract passed: {len(all_defined)} total defined classes; "
        "0 libxposed API definitions",
        file=sys.stderr,
    )
except (OSError, ValueError, zipfile.BadZipFile, UnicodeDecodeError) as error:
    print(f"[ERROR] DEX class-definition inspection failed: {error}", file=sys.stderr)
    raise SystemExit(1)
PY
}

log "Validating LSPosed bridge APK: $APK_PATH"
require_command unzip
require_command python3
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
    error 'apkanalyzer is required to prove manifest contracts'
  else
    app_id=$($apkanalyzer_bin manifest application-id "$APK_PATH" 2>/dev/null) || {
      error 'Unable to read APK application ID'
      app_id=''
    }
    [[ $app_id == "$EXPECTED_APP_ID" ]] ||
      error "Unexpected ${VARIANT} application ID: ${app_id:-<empty>}"

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
    if [[ $VARIANT == release ]] && grep -Eq 'android:debuggable=(true|"true")' <<<"$manifest"; then
      error 'Release bridge APK must not be debuggable'
    fi
  fi

  if ! check_defined_dex_classes; then
    error 'DEX defined-class contract failed'
  fi
fi

if ((ERRORS != 0)); then
  printf '[SUMMARY] LSPosed bridge contract validation failed with %d error(s).\n' "$ERRORS" >&2
  exit 1
fi

printf '[SUMMARY] LSPosed API 100 bridge APK contracts passed.\n' >&2
