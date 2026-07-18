#!/usr/bin/env bash

# Structural and artefact gates for the startup/profile programme.
# Failure policy is explicit: every failed contract exits through fail().

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || {
  printf 'startup-performance contract: cannot resolve repository root\n' >&2
  exit 1
}
cd -- "$repo_root" || {
  printf 'startup-performance contract: cannot enter repository root\n' >&2
  exit 1
}

fail() {
  printf 'startup-performance contract: %s\n' "$*" >&2
  exit 1
}

require_file() {
  local path=$1
  [[ -s $path ]] || fail "required non-empty file is missing: $path"
}

require_contains() {
  local path=$1 pattern=$2
  grep -Fq -- "$pattern" "$path" || fail "$path does not contain required contract: $pattern"
}

require_absent() {
  local path=$1 pattern=$2
  if grep -Fq -- "$pattern" "$path"; then
    fail "$path contains forbidden startup contract: $pattern"
  fi
}

baseline_generator=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BaselineProfileGenerator.kt
macrobenchmark=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/StartupMacrobenchmark.kt
journeys=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/CriticalJourneys.kt
fixture=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt
fixture_receiver=app/src/benchmark/java/org/oxycblt/auxio/benchmark/BenchmarkFixtureReceiver.kt
browser=app/src/main/java/org/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser.kt
perf_timer=app/src/main/java/org/oxycblt/auxio/util/PerfTimer.kt
startup_validation=.github/workflows/startup-performance.yml

for path in \
  app/src/main/baseline-prof.txt \
  app/src/main/startup-prof.txt \
  startup-benchmark/build.gradle \
  "$baseline_generator" \
  "$macrobenchmark" \
  "$journeys" \
  "$fixture" \
  "$fixture_receiver" \
  "$browser" \
  "$perf_timer" \
  "$startup_validation" \
  scripts/summarize-startup-benchmarks.py \
  docs/architecture/STARTUP_PROFILES_BENCHMARKS.md; do
  require_file "$path"
done

require_contains settings.gradle "include ':startup-benchmark'"
require_contains build.gradle 'id "androidx.baselineprofile" version "1.5.0-alpha07" apply false'
require_contains app/build.gradle 'id "androidx.baselineprofile"'
require_contains app/build.gradle 'minifyEnabled false'
require_contains app/build.gradle 'baselineProfile project(":startup-benchmark")'
require_contains app/build.gradle 'implementation "androidx.profileinstaller:profileinstaller:1.4.1"'
require_contains startup-benchmark/build.gradle 'id "com.android.test"'
require_contains startup-benchmark/build.gradle 'id "androidx.baselineprofile"'
require_contains startup-benchmark/build.gradle 'pixel2Api29'
require_contains startup-benchmark/build.gradle 'pixel6Api35'
require_contains startup-benchmark/build.gradle 'managedDevices = ["pixel6Api35"]'

for task in \
  ':startup-benchmark:assembleStandardBenchmarkBenchmark' \
  ':startup-benchmark:assembleTopwayTwMusicBenchmarkBenchmark' \
  ':startup-benchmark:assembleTopwayTwMediaBenchmarkBenchmark'; do
  require_contains "$startup_validation" "$task"
done
require_absent "$startup_validation" ':startup-benchmark:assembleStandardBenchmark 2>&1'

require_contains "$baseline_generator" '@RunWith(AndroidJUnit4::class)'
require_contains "$baseline_generator" 'includeInStartupProfile = true'
require_contains "$baseline_generator" 'includeInStartupProfile = false'
require_contains "$baseline_generator" 'filterPredicate = ::isProductionRule'
require_contains "$baseline_generator" 'rule.contains("/benchmark/")'
require_contains "$baseline_generator" 'rule.contains("/startupbenchmark/")'
require_contains "$baseline_generator" 'exerciseSavedSessionResume()'
require_contains "$baseline_generator" 'exercisePlaybackControls()'
require_contains "$baseline_generator" 'exerciseUsbFolder(sourceIndex = 0)'
require_contains "$baseline_generator" 'exerciseUsbFolder(sourceIndex = 1)'
require_contains "$baseline_generator" 'exercisePagedLibrary()'
require_contains "$baseline_generator" 'exerciseEarlyMediaBrowser()'

for count in 500 5_000 20_000; do
  require_contains "$fixture" "$count"
done
require_contains "$fixture" 'const val SCHEMA_VERSION = 2'
require_contains "$fixture" '"direct:usb0"'
require_contains "$fixture" '"direct:usb1"'
require_contains "$fixture_receiver" 'SOURCE_MODE_USB1_ABSENT'
require_contains "$fixture_receiver" 'SOURCE_MODE_PENDING'
require_contains "$fixture_receiver" 'LibraryGenreData'
require_contains "$fixture_receiver" 'LibraryPlaylistData'
require_contains "$fixture_receiver" 'writeSilenceWave'
require_contains "$fixture_receiver" 'ACTION_REPORT'
require_contains "$fixture_receiver" 'preparePlayableFixtures(context, sourceMode)'
require_contains "$fixture_receiver" 'put("available", available)'
require_contains "$fixture_receiver" 'stateStatement.bindLong(3, if (available) 1 else 0)'
require_contains "$fixture_receiver" 'root.deleteRecursively()'
require_contains "$fixture_receiver" 'seedPlaybackQueue(context, songCount, playableFiles)'
require_contains "$fixture_receiver" 'QueueSessionEntity('
require_contains "$fixture_receiver" 'QueueItemRefEntity('
require_contains "$fixture_receiver" 'queueDao.insertQueueItemRefs(items)'
require_contains "$fixture_receiver" 'private const val QUEUE_INSERT_BATCH_SIZE = 500'
require_contains "$browser" 'benchmarkRoot(context, 0)'
require_contains "$browser" 'playbackPath'
require_contains "$perf_timer" 'BuildConfig.BUILD_TYPE == "benchmark"'

for journey in \
  coldStartupWithoutProfiles \
  coldStartupWithBaselineProfile \
  warmStartupWithBaselineProfile \
  hotStartupWithBaselineProfile \
  savedSessionColdStartupWithBaselineProfile \
  primitiveQueueControlsJourney \
  findAndPlayJourney \
  usbFolderPlaybackJourney \
  secondUsbFolderPlaybackJourney \
  pagedLibraryJourney \
  earlyMediaBrowserJourney \
  coldStartupWithUnavailableSecondUsb \
  coldStartupWithInterruptedPendingGeneration \
  completeLibraryMilestonesRemainNonBlocking; do
  require_contains "$macrobenchmark" "$journey"
done
require_contains "$journeys" 'exerciseSavedSessionResume'
require_contains "$journeys" 'exerciseEarlyMediaBrowser'
require_contains "$journeys" 'exerciseUsbFolder(sourceIndex: Int = 0)'
require_contains "$journeys" 'waitForAudioPlayback'
require_contains "$journeys" 'Required UI object not found'
require_contains "$journeys" 'TRACE_NEXT_COMMAND_TO_NEXT_AUDIO'
require_contains "$journeys" 'TRACE_MEDIA_BROWSER_FIRST_PAGE'
# Public journeys must fail when a required control or row is unavailable. Do not
# use optional-click helpers that silently turn a missing interaction into a pass.
# Helper polling loops may legitimately return once their required condition is met.
require_absent "$journeys" 'clickIfPresent'

for required_class in \
  'Lorg/oxycblt/auxio/Auxio;' \
  'Lorg/oxycblt/auxio/AuxioService;' \
  'Lorg/oxycblt/auxio/music/StartupReadinessController;' \
  'Lorg/oxycblt/auxio/music/service/MusicBrowser;' \
  'Lorg/oxycblt/auxio/search/SearchViewModel;' \
  'Lorg/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser;'; do
  require_contains app/src/main/baseline-prof.txt "$required_class"
  require_contains app/src/main/startup-prof.txt "$required_class"
done

for forbidden in \
  DBCache \
  'Musikr;' \
  MusicGraph \
  LibraryFactory \
  EvaluateStep \
  ExtractStep \
  TagParser \
  MetadataExtractor \
  Artwork \
  '/benchmark/' \
  '/startupbenchmark/'; do
  require_absent app/src/main/startup-prof.txt "$forbidden"
done

if find app/src/main musikr/src/main -type f -path '*startupbenchmark*' -print -quit | grep -q .; then
  fail 'benchmark-only classes leaked into production source sets'
fi

if ! python3 scripts/summarize-startup-benchmarks.py --self-test; then
  fail 'benchmark result summarizer self-test failed'
fi

for artifact in "$@"; do
  [[ -f $artifact ]] || fail "artefact does not exist: $artifact"
  case $artifact in
    *.apk)
      if ! unzip -Z1 "$artifact" | grep -Eq '^assets/dexopt/baseline\.(prof|profm)$'; then
        fail "compiled Baseline Profile is missing from $artifact"
      fi
      printf 'compiled profile present: %s\n' "$artifact"
      ;;
    *.aab)
      if ! unzip -Z1 "$artifact" | grep -Eq '(^|/)BUNDLE-METADATA/com\.android\.tools/r8\.json$'; then
        fail "R8 startup metadata is missing from $artifact"
      fi
      if ! unzip -p "$artifact" '*/BUNDLE-METADATA/com.android.tools/r8.json' 2>/dev/null | grep -Fq '"startup":true' &&
         ! unzip -p "$artifact" 'BUNDLE-METADATA/com.android.tools/r8.json' 2>/dev/null | grep -Eq '"startup"[[:space:]]*:[[:space:]]*true'; then
        fail "R8 metadata does not confirm startup DEX layout in $artifact"
      fi
      printf 'startup DEX metadata present: %s\n' "$artifact"
      ;;
    *baseline-prof.txt|*startup-prof.txt)
      require_file "$artifact"
      require_absent "$artifact" '/benchmark/'
      require_absent "$artifact" '/startupbenchmark/'
      printf 'generated profile validated: %s\n' "$artifact"
      ;;
    *)
      fail "unsupported startup-performance artefact: $artifact"
      ;;
  esac
done

printf 'startup-performance contracts: PASS\n'
