#!/usr/bin/env bash
# Prove that diagnostic capture/export implementation remains debug-only.
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

debug_dir=app/src/debug/java/org/oxycblt/auxio/diagnostics
release_dir=app/src/release/java/org/oxycblt/auxio/diagnostics
main_dir=app/src/main/java/org/oxycblt/auxio/diagnostics

[[ -f "$debug_dir/DiagnosticJournal.kt" ]] || fail 'Debug journal implementation is missing.'
[[ -f "$debug_dir/DiagnosticBundleExporter.kt" ]] || fail 'Debug exporter is missing.'
[[ -f "$debug_dir/TemporaryDeviceValidationLab.kt" ]] || fail 'Debug validation lab is missing.'
[[ -f "$release_dir/DiagnosticJournal.kt" ]] || fail 'Release journal no-op is missing.'
[[ -f "$release_dir/TemporaryDeviceValidationLab.kt" ]] || fail 'Release lab no-op is missing.'
[[ ! -e "$main_dir/DiagnosticJournal.kt" ]] || fail 'Journal implementation leaked into main.'
[[ ! -e "$main_dir/DiagnosticBundleExporter.kt" ]] || fail 'Bundle exporter leaked into main.'
[[ ! -e app/src/main/res/values/strings_diagnostic_bundle.xml ]] ||
  fail 'Diagnostic bundle UI strings leaked into main resources.'

for forbidden in Executors appendText ZipOutputStream Timber AUXIO_TS_CAPTURE_CANARY; do
  if grep -Fq -- "$forbidden" "$release_dir/DiagnosticJournal.kt" "$release_dir/TemporaryDeviceValidationLab.kt"; then
    fail "Release diagnostic no-op contains forbidden implementation token: $forbidden"
  fi
done

fragment=app/src/main/java/org/oxycblt/auxio/settings/categories/DiagnosticsRecoveryPreferenceFragment.kt
grep -Fq 'TemporaryDeviceValidationLab.install' "$fragment" ||
  fail 'Debug lab does not use its compile-time-checked API.'
if grep -Eq 'Class\.forName\("org\.oxycblt\.auxio\.diagnostics\.TemporaryDeviceValidationLab"|ReflectiveOperationException' "$fragment"; then
  fail 'Debug lab installation still uses reflection.'
fi

apk=${1:-}
if [[ -n "$apk" ]]; then
  [[ -f "$apk" ]] || fail "Release APK not found: $apk"
  dex_strings=$(mktemp)
  trap 'rm -f -- "$dex_strings"' EXIT
  unzip -p "$apk" 'classes*.dex' | strings >"$dex_strings"
  for forbidden in \
    DiagnosticBundleExporter \
    AuxioDiagnosticJournal \
    AUXIO_TS_CAPTURE_CANARY \
    'TEMPORARY — Device Validation Lab'; do
    grep -Fq -- "$forbidden" "$dex_strings" &&
      fail "Release APK contains debug diagnostic token: $forbidden"
  done
fi

printf 'Release diagnostics boundary: PASS\n'
