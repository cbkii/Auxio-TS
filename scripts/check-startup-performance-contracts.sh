#!/usr/bin/env bash
# Structural and artefact gates for the startup/profile programme.

repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." 2>/dev/null && pwd -P) || {
  printf 'startup-performance contract: cannot resolve repository root\n' >&2
  exit 1
}
cd -- "$repo_root" || exit 1

fail() { printf 'startup-performance contract: %s\n' "$*" >&2; exit 1; }
require_file() { [[ -s $1 ]] || fail "required non-empty file is missing: $1"; }
require_contains() { grep -Fq -- "$2" "$1" || fail "$1 does not contain required contract: $2"; }
require_absent() { grep -Fq -- "$2" "$1" && fail "$1 contains forbidden startup contract: $2" || true; }
require_absent_regex() { grep -Eq -- "$2" "$1" && fail "$1 contains forbidden startup regex: $2" || true; }

baseline_profile=app/src/main/baseline-prof.txt
startup_profile=app/src/main/generated/baselineProfiles/startup-prof.txt
baseline_generator=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BaselineProfileGenerator.kt
macrobenchmark=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/StartupMacrobenchmark.kt
journeys=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/CriticalJourneys.kt
fixture=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt
fixture_receiver=app/src/benchmark/java/org/oxycblt/auxio/benchmark/BenchmarkFixtureReceiver.kt
browser=app/src/main/java/org/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser.kt
perf_timer=app/src/main/java/org/oxycblt/auxio/util/PerfTimer.kt
music_repository=app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt
music_settings=app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt
deferred_startup_test=app/src/test/java/org/oxycblt/auxio/music/DeferredStartupHydrationTest.kt
capture_restore_test=app/src/test/java/org/oxycblt/auxio/music/PerformanceCapturePreferenceRestoreTest.kt
startup_validation=.github/workflows/startup-performance.yml
android_workflow=.github/workflows/android.yml
quality_workflow=.github/workflows/lint.yml
release_workflow=.github/workflows/manual-release.yml
benchmark_workflow=.github/workflows/startup-benchmarks.yml

for path in \
  "$baseline_profile" "$startup_profile" startup-benchmark/build.gradle \
  "$baseline_generator" "$macrobenchmark" "$journeys" "$fixture" "$fixture_receiver" \
  "$browser" "$perf_timer" "$music_repository" "$music_settings" \
  "$deferred_startup_test" "$capture_restore_test" \
  "$startup_validation" "$android_workflow" "$quality_workflow" "$release_workflow" \
  "$benchmark_workflow" scripts/ci-scope.sh scripts/check-ci-variant-contracts.sh \
  scripts/summarize-startup-benchmarks.py docs/architecture/STARTUP_PROFILES_BENCHMARKS.md; do
  require_file "$path"
done

[[ ! -e app/src/main/startup-prof.txt ]] || fail 'obsolete Startup Profile path remains'
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
require_absent_regex startup-benchmark/build.gradle '^[[:space:]]*standard[[:space:]]*\{'
require_absent startup-benchmark/build.gradle 'org.oxycblt.auxio"'

for task in \
  ':startup-benchmark:assembleTopwayTwMusicBenchmarkBenchmark' \
  ':startup-benchmark:assembleTopwayTwMediaBenchmarkBenchmark'; do
  require_contains "$startup_validation" "$task"
done
require_absent "$startup_validation" 'assembleStandard'
require_absent "$startup_validation" 'bundleStandard'

require_contains "$baseline_generator" '@RunWith(AndroidJUnit4::class)'
require_contains "$baseline_generator" 'includeInStartupProfile = true'
require_contains "$baseline_generator" 'includeInStartupProfile = false'
require_contains "$baseline_generator" 'filterPredicate = ::isProductionRule'
require_contains "$baseline_generator" 'rule.contains("/benchmark/")'
require_contains "$baseline_generator" 'rule.contains("/startupbenchmark/")'
for call in \
  'exerciseSavedSessionResume()' 'exercisePlaybackControls()' \
  'exerciseUsbFolder(sourceIndex = 0)' 'exerciseUsbFolder(sourceIndex = 1)' \
  'exercisePagedLibrary()' 'exerciseEarlyMediaBrowser()'; do
  require_contains "$baseline_generator" "$call"
done

for count in 500 5_000 20_000; do require_contains "$fixture" "$count"; done
require_contains "$fixture" 'const val SCHEMA_VERSION = 3'
require_contains "$fixture" '"direct:usb0"'
require_contains "$fixture" '"direct:usb1"'
for token in \
  SOURCE_MODE_USB1_ABSENT SOURCE_MODE_PENDING LibraryGenreData LibraryPlaylistData \
  writeSilenceWave ACTION_REPORT 'preparePlayableFixtures(context, sourceMode)' \
  'put("available", available)' 'stateStatement.bindLong(3, if (available) 1 else 0)' \
  'root.deleteRecursively()' 'seedPlaybackQueue(context, songCount, playableFiles)' \
  'QueueSessionEntity(' 'QueueItemRefEntity(' 'queueDao.insertQueueItemRefs(items)' \
  'private const val QUEUE_INSERT_BATCH_SIZE = 500'; do
  require_contains "$fixture_receiver" "$token"
done
require_contains "$browser" 'benchmarkRoot(context, 0)'
require_contains "$browser" 'playbackPath'
require_contains "$perf_timer" 'BuildConfig.BUILD_TYPE == "benchmark"'

require_contains "$music_repository" 'deferCachedLoad = true'
require_contains "$music_repository" 'startCompatibilityHydration(worker)'
require_contains "$music_repository" 'requestCompatibilityRecoveryIfNeeded('
require_absent "$music_repository" 'readTwStorageSwitch'
require_absent "$music_repository" 'persist.tw.storage.switch'
require_contains "$music_settings" 'PerfTimer.configure(performanceCaptureEnabled)'
require_contains "$deferred_startup_test" 'cached-library-hydration-deferred'
require_contains "$capture_restore_test" 'settings construction restores persisted performance capture preference'
require_contains "$fixture_receiver" 'seedBenchmarkStartupPreferences(context, generatedPlaylistsEnabled)'
require_contains "$fixture_receiver" 'FIXTURE_LIBRARY_REVISION'
require_contains "$fixture_receiver" 'LibraryState.USABLE.name'
require_contains "$fixture_receiver" 'IntegerTable.LOCATION_MODE_DIRECT_FS'
require_contains "$fixture_receiver" 'file:///storage/usbdisk0;file:///storage/usbdisk1'
require_contains "$fixture_receiver" 'EXTRA_GENERATED_PLAYLISTS'

for workflow in "$android_workflow" "$quality_workflow"; do
  require_contains "$workflow" '      - "cx/**"'
  require_contains "$workflow" 'scripts/ci-scope.sh'
  require_contains "$workflow" 'actions/setup-java@03ad4de0992f5dab5e18fcb136590ce7c4a0ac95'
  require_contains "$workflow" 'gradle/actions/setup-gradle@0723195856401067f7a2779048b490ace7a47d7c'
done
require_contains "$android_workflow" ':app:connectedTopwayTwMediaDebugAndroidTest'
require_contains "$quality_workflow" ':app:testTopwayTwMediaDebugUnitTest'
require_contains "$quality_workflow" ':app:lintTopwayTwMediaDebug'
require_contains "$release_workflow" 'persist-credentials: false'
require_contains "$release_workflow" 'bash ./scripts/check-startup-performance-contracts.sh "${asset_path}"'
require_contains "$release_workflow" '.sha256'
require_contains "$release_workflow" '.metadata.txt'
require_absent "$release_workflow" 'include_standard_apk'
require_contains "$benchmark_workflow" 'default: topwayTwMedia'
require_contains "$benchmark_workflow" 'Measurement iterations (15-30)'
require_contains "$benchmark_workflow" '        default: 15'
require_absent_regex "$benchmark_workflow" '^[[:space:]]*-[[:space:]]*standard[[:space:]]*$'

if find .github -maxdepth 2 -type f \( -name 'pr184-*' -o -name 'pr183-*' -o -name '*hardening-error*' \) -print -quit | grep -q .; then
  fail 'temporary PR repair/finaliser artefacts remain in the source tree'
fi

for journey in \
  coldStartupWithoutProfiles coldStartupWithBaselineProfile warmStartupWithBaselineProfile \
  hotStartupWithBaselineProfile savedSessionColdStartupWithBaselineProfile \
  attachThenBootRestoreCold attachThenRestoreBurstCold pauseDuringRestore nextDuringRestore \
  seekDuringRestore generatedPlaylistsDoNotBlockFiveThousandSongResume \
  generatedPlaylistsDoNotBlockTwentyThousandSongResume \
  primitiveQueueControlsJourney findAndPlayJourney usbFolderPlaybackJourney \
  secondUsbFolderPlaybackJourney pagedLibraryJourney earlyMediaBrowserJourney \
  coldStartupWithUnavailableSecondUsb coldStartupWithInterruptedPendingGeneration \
  completeLibraryMilestonesRemainNonBlocking; do
  require_contains "$macrobenchmark" "$journey"
done
for token in \
  exerciseSavedSessionResume exerciseBootRestore exerciseRestoreBurst exercisePauseDuringRestore \
  exerciseNextDuringRestore exerciseSeekDuringRestore exerciseEarlyMediaBrowser \
  'exerciseUsbFolder(sourceIndex: Int = 0)' waitForAudioPlayback 'Required UI object not found' \
  TRACE_NEXT_COMMAND_TO_NEXT_AUDIO TRACE_MEDIA_BROWSER_FIRST_PAGE \
  TRACE_BOOT_RESTORE_TO_FIRST_AUDIO TRACE_RESTORE_BURST_TO_FIRST_AUDIO \
  'Next after Quick Find' 'first Album track'; do
  require_contains "$journeys" "$token"
done
require_absent "$journeys" 'clickIfPresent'

for required_class in \
  'Lorg/oxycblt/auxio/Auxio;' \
  'Lorg/oxycblt/auxio/AuxioService;' \
  'Lorg/oxycblt/auxio/music/StartupReadinessController;' \
  'Lorg/oxycblt/auxio/music/service/MusicBrowser;' \
  'Lorg/oxycblt/auxio/search/SearchViewModel;' \
  'Lorg/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser;'; do
  require_contains "$baseline_profile" "$required_class"
  require_contains "$startup_profile" "$required_class"
done

for forbidden in DBCache 'Musikr;' MusicGraph LibraryFactory EvaluateStep ExtractStep TagParser MetadataExtractor Artwork '/benchmark/' '/startupbenchmark/'; do
  require_absent "$startup_profile" "$forbidden"
done

if find app/src/main musikr/src/main -type f -path '*startupbenchmark*' -print -quit | grep -q .; then
  fail 'benchmark-only classes leaked into production source sets'
fi
python3 scripts/summarize-startup-benchmarks.py --self-test || fail 'benchmark result summarizer self-test failed'

for artifact in "$@"; do
  [[ -f $artifact ]] || fail "artefact does not exist: $artifact"
  case $artifact in
    *.apk)
      unzip -Z1 "$artifact" | grep -Eq '^assets/dexopt/baseline\.(prof|profm)$' ||
        fail "compiled Baseline Profile is missing from $artifact"
      printf 'compiled profile present: %s\n' "$artifact"
      ;;
    *.aab)
      unzip -Z1 "$artifact" | grep -Eq '(^|/)BUNDLE-METADATA/com\.android\.tools/r8\.json$' ||
        fail "R8 startup metadata is missing from $artifact"
      unzip -p "$artifact" '*/BUNDLE-METADATA/com.android.tools/r8.json' 2>/dev/null | grep -Eq '"startup"[[:space:]]*:[[:space:]]*true' ||
        fail "R8 metadata does not confirm startup DEX layout in $artifact"
      ;;
    *baseline-prof.txt|*startup-prof.txt)
      require_file "$artifact"
      require_absent "$artifact" '/benchmark/'
      require_absent "$artifact" '/startupbenchmark/'
      ;;
    *) fail "unsupported startup-performance artefact: $artifact" ;;
  esac
done

printf 'startup-performance contracts: PASS\n'
