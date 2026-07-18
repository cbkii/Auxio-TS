#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "${repo_root}"

fail() {
  printf 'startup-performance contract: %s\n' "$*" >&2
  exit 1
}

require_file() {
  local path=$1
  [[ -s "${path}" ]] || fail "required non-empty file is missing: ${path}"
}

require_contains() {
  local path=$1 pattern=$2
  grep -Fq -- "${pattern}" "${path}" || fail "${path} does not contain required contract: ${pattern}"
}

require_absent() {
  local path=$1 pattern=$2
  if grep -Fq -- "${pattern}" "${path}"; then
    fail "${path} contains forbidden startup contract: ${pattern}"
  fi
}

require_file app/src/main/baseline-prof.txt
require_file app/src/main/startup-prof.txt
require_file startup-benchmark/build.gradle
require_file startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BaselineProfileGenerator.kt
require_file startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/StartupMacrobenchmark.kt
require_file startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt
require_file docs/architecture/STARTUP_PROFILES_BENCHMARKS.md

require_contains settings.gradle "include ':startup-benchmark'"
require_contains app/build.gradle 'implementation "androidx.profileinstaller:profileinstaller:1.4.1"'
require_contains app/build.gradle 'benchmark {'
require_contains startup-benchmark/build.gradle 'id "com.android.test"'
require_contains startup-benchmark/build.gradle 'benchmark {'
require_contains startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BaselineProfileGenerator.kt 'BaselineProfileRule'

for count in 500 5_000 20_000; do
  require_contains startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt "${count}"
done
require_contains startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt '"direct:usb0"'
require_contains startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt '"direct:usb1"'
require_contains startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt '"/storage/usbdisk$sourceIndex"'

for required_class in \
  'Lorg/oxycblt/auxio/Auxio;' \
  'Lorg/oxycblt/auxio/AuxioService;' \
  'Lorg/oxycblt/auxio/music/StartupReadinessController;' \
  'Lorg/oxycblt/auxio/music/service/MusicBrowser;' \
  'Lorg/oxycblt/auxio/search/SearchViewModel;' \
  'Lorg/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser;'; do
  require_contains app/src/main/baseline-prof.txt "${required_class}"
  require_contains app/src/main/startup-prof.txt "${required_class}"
done

for forbidden in \
  'DBCache' \
  'Musikr;' \
  'MusicGraph' \
  'LibraryFactory' \
  'EvaluateStep' \
  'ExtractStep' \
  'TagParser' \
  'MetadataExtractor' \
  'Artwork'; do
  require_absent app/src/main/startup-prof.txt "${forbidden}"
done

if find app/src/main musikr/src/main -type f -path '*startupbenchmark*' -print -quit | grep -q .; then
  fail 'benchmark-only classes leaked into production source sets'
fi

for apk in "$@"; do
  [[ -f "${apk}" ]] || fail "APK does not exist: ${apk}"
  if ! unzip -Z1 "${apk}" | grep -Eq '^assets/dexopt/baseline\.(prof|profm)$'; then
    fail "compiled Baseline Profile is missing from ${apk}"
  fi
  printf 'profile present: %s\n' "${apk}"
done

printf 'startup-performance contracts: PASS\n'
