#!/usr/bin/env bash
# Structural/artefact gates for startup/profile work across the three distribution variants.
set -euo pipefail
repo_root=$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd -P); cd -- "$repo_root"
fail(){ printf 'startup-performance contract: %s\n' "$*" >&2; exit 1; }
req(){ [[ -s $1 ]] || fail "missing required file: $1"; }
has(){ grep -Fq -- "$2" "$1" || fail "$1 lacks: $2"; }
regex(){ grep -Eq -- "$2" "$1" || fail "$1 does not match: $2"; }
no(){ if grep -Fq -- "$2" "$1"; then fail "$1 contains forbidden token: $2"; fi; }

baseline=app/src/main/baseline-prof.txt
startup=app/src/main/generated/baselineProfiles/startup-prof.txt
generator=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BaselineProfileGenerator.kt
macro=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/StartupMacrobenchmark.kt
journeys=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/CriticalJourneys.kt
fixture=startup-benchmark/src/main/java/org/oxycblt/auxio/startupbenchmark/BenchmarkFixture.kt
receiver=app/src/benchmark/java/org/oxycblt/auxio/benchmark/BenchmarkFixtureReceiver.kt
browser=app/src/main/java/org/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser.kt
perf=app/src/main/java/org/oxycblt/auxio/util/PerfTimer.kt
repo=app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt
settings=app/src/main/java/org/oxycblt/auxio/music/MusicSettings.kt
startup_validation=.github/workflows/startup-performance.yml
benchmark_workflow=.github/workflows/startup-benchmarks.yml
android=.github/workflows/android.yml
quality=.github/workflows/lint.yml
release=.github/workflows/manual-release.yml

for path in "$baseline" "$startup" startup-benchmark/build.gradle "$generator" "$macro" "$journeys" "$fixture" "$receiver" "$browser" "$perf" "$repo" "$settings" "$startup_validation" "$benchmark_workflow" "$android" "$quality" "$release" scripts/check-product-contracts.sh scripts/release-asset-matrix.py scripts/summarize-startup-benchmarks.py docs/architecture/STARTUP_PROFILES_BENCHMARKS.md; do req "$path"; done
[[ ! -e app/src/main/startup-prof.txt ]] || fail 'obsolete Startup Profile path returned'

has settings.gradle "include ':startup-benchmark'"
regex build.gradle 'id "androidx\.baselineprofile" version "1\.5\.[0-9]+([.-][^"[:space:]]+)?" apply false'
has app/build.gradle 'id "androidx.baselineprofile"'
has app/build.gradle 'baselineProfile project(":startup-benchmark")'
has app/build.gradle 'implementation "androidx.profileinstaller:profileinstaller:1.4.1"'
has startup-benchmark/build.gradle 'id "com.android.test"'
has startup-benchmark/build.gradle 'id "androidx.baselineprofile"'
has startup-benchmark/build.gradle 'flavorDimensions += "distribution"'
for token in 'standard {' 'topwayTwMedia {' 'topwayTwMusic {' '"org.oxycblt.auxio"' '"com.tw.media"' '"com.tw.music"' 'pixel2Api29' 'apiLevel = 29' 'pixel6Api35'; do has startup-benchmark/build.gradle "$token"; done

# Automation must address explicit flavour-qualified tasks and retain topwayTwMedia as the TS18
# default benchmark lane without pretending emulator results are physical TS18 evidence.
for token in ':startup-benchmark:assembleStandardBenchmark' ':startup-benchmark:assembleTopwayTwMediaBenchmark' ':startup-benchmark:assembleTopwayTwMusicBenchmark'; do has "$startup_validation" "$token"; done
has "$benchmark_workflow" 'distribution:'
has "$benchmark_workflow" 'default: topwayTwMedia'
for token in 'standard' 'topwayTwMedia' 'topwayTwMusic' 'Requires device validation'; do has "$benchmark_workflow" "$token"; done
has "$android" ':app:connectedStandardDebugAndroidTest'
has "$android" ':app:connectedTopwayTwMediaDebugAndroidTest'
has "$android" ':app:connectedTopwayTwMusicDebugAndroidTest'
for token in ':app:testStandardDebugUnitTest' ':app:testTopwayTwMediaDebugUnitTest' ':app:testTopwayTwMusicDebugUnitTest' ':app:lintStandardDebug' ':app:lintTopwayTwMediaDebug' ':app:lintTopwayTwMusicDebug'; do has "$quality" "$token"; done
for token in 'include_standard_apk' 'include_topway_twmedia_apk' 'include_topway_twmusic_magisk' 'topway-twmusic-magisk.zip'; do has "$release" "$token"; done

has "$generator" '@RunWith(AndroidJUnit4::class)'
has "$generator" 'includeInStartupProfile = true'
has "$generator" 'filterPredicate = ::isProductionRule'
for call in 'exerciseSavedSessionResume()' 'exercisePlaybackControls()' 'exerciseUsbFolder(sourceIndex = 0)' 'exerciseUsbFolder(sourceIndex = 1)' 'exercisePagedLibrary()' 'exerciseEarlyMediaBrowser()'; do has "$generator" "$call"; done
for count in 500 5_000 20_000; do has "$fixture" "$count"; done
has "$fixture" 'const val SCHEMA_VERSION = 3'
has "$fixture" '"direct:usb0"'; has "$fixture" '"direct:usb1"'
for token in SOURCE_MODE_USB1_ABSENT SOURCE_MODE_PENDING LibraryGenreData LibraryPlaylistData writeSilenceWave ACTION_REPORT 'preparePlayableFixtures(context, sourceMode)' 'root.deleteRecursively()' 'seedPlaybackQueue(context, songCount, playableFiles)' 'QueueSessionEntity(' 'QueueItemRefEntity(' 'queueDao.insertQueueItemRefs(items)' 'private const val QUEUE_INSERT_BATCH_SIZE = 500'; do has "$receiver" "$token"; done
has "$browser" 'benchmarkRoot(context, 0)'; has "$browser" 'playbackPath'; has "$perf" 'BuildConfig.BUILD_TYPE == "benchmark"'
has "$repo" 'deferCachedLoad = true'; has "$repo" 'startCompatibilityHydration(worker)'; has "$repo" 'requestCompatibilityRecoveryIfNeeded('
no "$repo" 'persist.tw.storage.switch'; has "$settings" 'PerfTimer.configure(performanceCaptureEnabled)'

for journey in coldStartupWithoutProfiles coldStartupWithBaselineProfile warmStartupWithBaselineProfile hotStartupWithBaselineProfile savedSessionColdStartupWithBaselineProfile attachThenBootRestoreCold attachThenRestoreBurstCold pauseDuringRestore nextDuringRestore seekDuringRestore generatedPlaylistsDoNotBlockFiveThousandSongResume generatedPlaylistsDoNotBlockTwentyThousandSongResume primitiveQueueControlsJourney findAndPlayJourney usbFolderPlaybackJourney secondUsbFolderPlaybackJourney pagedLibraryJourney earlyMediaBrowserJourney coldStartupWithUnavailableSecondUsb coldStartupWithInterruptedPendingGeneration completeLibraryMilestonesRemainNonBlocking; do has "$macro" "$journey"; done
for token in exerciseSavedSessionResume exerciseBootRestore exerciseRestoreBurst exercisePauseDuringRestore exerciseNextDuringRestore exerciseSeekDuringRestore exerciseEarlyMediaBrowser 'exerciseUsbFolder(sourceIndex: Int = 0)' waitForAudioPlayback 'Required UI object not found' TRACE_NEXT_COMMAND_TO_NEXT_AUDIO TRACE_MEDIA_BROWSER_FIRST_PAGE TRACE_BOOT_RESTORE_TO_FIRST_AUDIO TRACE_RESTORE_BURST_TO_FIRST_AUDIO 'Next after Quick Find' 'first Album track'; do has "$journeys" "$token"; done
no "$journeys" 'clickIfPresent'

for cls in 'Lorg/oxycblt/auxio/Auxio;' 'Lorg/oxycblt/auxio/AuxioService;' 'Lorg/oxycblt/auxio/music/StartupReadinessController;' 'Lorg/oxycblt/auxio/music/service/MusicBrowser;' 'Lorg/oxycblt/auxio/search/SearchViewModel;' 'Lorg/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser;'; do has "$baseline" "$cls"; has "$startup" "$cls"; done
for forbidden in DBCache 'Musikr;' MusicGraph LibraryFactory EvaluateStep ExtractStep TagParser MetadataExtractor Artwork '/benchmark/' '/startupbenchmark/'; do no "$startup" "$forbidden"; done
if find app/src/main musikr/src/main -type f -path '*startupbenchmark*' -print -quit | grep -q .; then fail 'benchmark-only classes leaked into production source sets'; fi
python3 scripts/summarize-startup-benchmarks.py --self-test || fail 'benchmark summarizer self-test failed'

if find .github -maxdepth 2 -type f \( -name 'pr184-*' -o -name 'pr183-*' -o -name '*hardening-error*' \) -print -quit | grep -q .; then fail 'temporary repair artefacts remain'; fi

for artifact in "$@"; do
  [[ -f $artifact ]] || fail "artefact does not exist: $artifact"
  case "$artifact" in
    *.apk) unzip -Z1 "$artifact" | grep -Eq '^assets/dexopt/baseline\.(prof|profm)$' || fail "compiled Baseline Profile missing: $artifact" ;;
    *.aab) unzip -Z1 "$artifact" | grep -Eq '(^|/)BUNDLE-METADATA/com\.android\.tools/r8\.json$' || fail "R8 startup metadata missing: $artifact" ;;
    *baseline-prof.txt|*startup-prof.txt) req "$artifact"; no "$artifact" '/benchmark/'; no "$artifact" '/startupbenchmark/' ;;
    *) fail "unsupported startup-performance artefact: $artifact" ;;
  esac
done
printf 'startup-performance three-variant contracts: PASS\n'
