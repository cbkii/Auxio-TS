# Startup profiles, benchmarks and integrated performance gates

Status: implemented PR 3 architecture for the Auxio-TS 0–60 second startup programme. Repository validation is automated; managed-emulator timing runs and exact-device TS18 validation remain explicitly separate.

Base: final review-ready PR 2 branch `cx/incremental-library-pipeline`.

Tracking issue: #181.

## Goal

Measure, optimise and prevent regressions in the complete startup and first-minute experience without weakening the corrected PR 1/PR 2 architecture. Profiles accelerate the bounded immediate lane; they must not disguise complete-library construction, eager enrichment, unbounded queries or main-thread I/O.

No wall-clock improvement is claimed by this document. A percentage or latency claim requires captured with-profile and without-profile results for the same commit, variant, fixture, device and compilation mode.

## Evidence labels

- **Observed:** compiled, tested or structurally verified in the stated repository, managed-emulator or host environment.
- **Inferred:** supported by repository evidence but not directly timed on the exact head unit.
- **Requires device validation:** must be captured on `s9863a1h10_Natv`, Android 10/API 29, `TS18.2.2_20241210.165912_WINDOW-THEME1`.
- **Unsupported:** no acceptable evidence; do not present as a result.

## Implemented benchmark and profile architecture

### Benchmark module

`startup-benchmark` is a dedicated `com.android.test` module using Android Macrobenchmark and the AndroidX Baseline Profile plugin. It targets the app's benchmark variants and mirrors the maintained `standard`, `topwayTwMusic` and `topwayTwMedia` flavour identities without adding benchmark dependencies to production runtime code.

The module provides:

- `BaselineProfileGenerator` for the immediate-interaction journeys;
- `StartupMacrobenchmark` for cold/warm startup and interaction journeys;
- `CriticalJourneys` as the shared UI/media-control contract;
- `BenchmarkFixtures` for deterministic 500, 5,000 and 20,000-row logical fixtures;
- `BenchmarkFixtureController` for seeding the committed database outside measured iterations.

The target app exposes `BenchmarkFixtureReceiver` only in the benchmark build type. It transactionally inserts committed source-ledger and lightweight song rows for two independent source identities without scanning a filesystem. The receiver is absent from maintained debug and release variants.

Fixture contract:

- schema version: `1`;
- seed: `18022026`;
- source identities: `direct:usb0` and `direct:usb1`;
- representative paths: `/storage/usbdisk0/Music/...` and `/storage/usbdisk1/Music/...`;
- supported row counts: 500, 5,000 and 20,000;
- deterministic title, artist, album, genre, folder, duration, date and stable-ID generation.

The fixture is seeded once before a benchmark test and retained across measured iterations. It does not invoke a complete source scan during each iteration.

### Baseline Profile

The generator covers these critical journeys:

1. cold launch and Fast Start;
2. primitive queue/service restoration;
3. Play/Pause, Next and Previous through the existing media authority;
4. Quick Find query and direct selection;
5. bounded USB folder browse and direct playback;
6. first paged library interactions.

The checked-in Baseline Profile seed contains immediate-lane application, service, queue, Room projection, search, MediaBrowser and direct-folder classes. The managed workflow can regenerate it from the API 29 journey instead of relying only on manually maintained rules.

### Startup Profile

`app/src/main/startup-prof.txt` contains the classes needed for:

- application and Activity startup;
- playback service and media session readiness;
- primitive queue/current item restoration;
- startup capability coordination;
- Fast Start Room projections;
- Quick Find setup;
- bounded MediaBrowser and direct-folder entry points.

The Startup Profile deliberately excludes `DBCache.snapshot()`, complete Musikr graph construction, `LibraryFactory`, `EvaluateStep`, metadata extraction, TagLib parsing and artwork enrichment. `scripts/check-startup-performance-contracts.sh` enforces this boundary.

## Benchmark journeys and metrics

The maintained benchmark class provides:

- cold startup with no profile compilation;
- cold startup with the Baseline Profile required;
- warm startup with the Baseline Profile required;
- Find and Play;
- USB folder playback;
- paged library browsing.

Each managed run selects:

- target flavour;
- 500, 5,000 or 20,000 committed rows;
- 3–30 measurement iterations;
- macrobenchmark or profile-generation suite.

The workflow records commit, flavour, fixture size, iteration count, managed device/API and evidence classification alongside AndroidX benchmark JSON/reports. AndroidX reports contain compilation mode, sample timings and distribution data. The workflow does not convert emulator timing into an exact-device claim.

## Timing and trace contract

The app retains bounded monotonic events for:

- `Application.onCreate` start and end;
- startup capability transitions including playback service, queue, Fast Browse, Search, full library and enrichment readiness;
- Fast Start first rows;
- Quick Find first result;
- playback/media milestones already emitted by the playback service and player path.

`PerfTimer` retains at most 256 events. Capture is debug-enabled or explicitly user-enabled; it does not write continuously to disk. `StartupPerformanceReport` exports a bounded local text report containing:

- explicit authority;
- application ID, version and variant;
- optional commit and fixture size;
- boot ID where readable;
- source-state context;
- process/thread identity;
- monotonic event times and durations.

The Content settings screen exposes an explicit **Export startup report** action. Sharing is user-started; the application does not upload the report automatically.

## Structural regression gates

Repository checks fail when:

- required Baseline/Startup Profile files or benchmark classes are missing;
- startup rules include complete graph, extraction or artwork classes;
- benchmark-only packages leak into production source sets;
- required Fast Start/service/search/MediaBrowser classes are absent from profiles;
- deterministic fixture sizes or the two source identities disappear;
- release APKs do not contain compiled Baseline Profile data;
- guarded immediate-lane source files reference complete graph APIs;
- normal CI, TS18 APK-reference or DoFun/Topway source contracts fail.

Existing executable tests also preserve:

- MediaCodec before the FFmpeg compatibility fallback;
- one ReplayGain processor path;
- bounded fast consumers with no `DBCache.snapshot()`, `Musikr.loadCached()`, `MusicGraph`, `LibraryFactory` or `selectAllSongs()` references;
- queue/capability ordering and generation safety inherited from PR 1 and PR 2.

Shared runners use structural/semantic gates rather than flaky absolute latency limits.

## Workflows

### `startup-performance.yml`

Runs automatically for the PR 3 branch and relevant stacked PR changes. It includes:

- static startup contracts;
- formatting;
- app and Musikr unit tests;
- Android lint;
- benchmark/profile module compilation;
- all maintained debug variants;
- all maintained release variants;
- compiled profile verification in release APKs;
- TS18 APK-reference contracts;
- DoFun/Topway source compatibility checks.

Third-party actions are pinned to immutable commits and the workflow has read-only repository permission.

### `startup-benchmarks.yml`

Manual, bounded managed-emulator workflow with flavour, fixture, iteration and suite inputs. It uses a Pixel 2 API 29 AOSP managed device, uploads benchmark/profile reports and clearly labels the result as observed only in that emulator environment.

No recurring benchmark schedule is enabled. A trend schedule should be added only after its emulator cost, retention and interpretation are accepted.

## Optional-component and integrated audit

The complete three-PR path preserves these boundaries:

- MediaCodec remains the normal first renderer and FFmpeg the compatibility fallback.
- ReplayGain metadata work follows profile/settings policy; the runtime processor remains a no-op while disabled.
- Artwork is loaded only for visible, current-playback, widget or explicit detail demand.
- Visualiser, diagnostic, shortcut and launcher integrations do not duplicate player, session, service or notification infrastructure.
- Profile rules do not eagerly initialise optional components.
- Benchmark instrumentation does not alter normal debug/release startup decisions.
- The benchmark-only fixture receiver is excluded from maintained production variants.

Profiles do not include rich enrichment or complete-graph construction. They cannot make those paths a hidden prerequisite of first interaction.

## Repository acceptance gates

Required before review:

- benchmark/profile configuration compiles;
- deterministic fixtures cover 500, 5,000 and 20,000 committed rows and two sources;
- static and executable architecture checks pass;
- maintained debug and release variants build;
- unit tests, lint, formatting and workflow syntax pass;
- compiled release APKs contain expected profile data;
- TS18 APK-reference and DoFun/Topway checks pass;
- all temporary repair/formatter files are removed;
- every actionable review thread is resolved;
- the PR description and final comment report only checks actually run.

Managed-emulator journey execution is manually dispatchable and may be run after merge/release preparation. Exact-device timings are never a repository merge gate for this PR, but the implementation and collection plan must be complete.

## Exact-device TS18 validation

**Requires device validation.** Repository and managed-emulator results do not prove exact-unit latency, DoFun routing, USB behaviour or ACC lifecycle behaviour.

The complete prerequisite, action, evidence, pass/fail and rollback matrix is in [`../validation/EXACT_TS18_STARTUP_VALIDATION.md`](../validation/EXACT_TS18_STARTUP_VALIDATION.md). It covers:

- cold boot and warm launch;
- process death and launcher restart;
- Bluetooth and hardware/media-key starts;
- saved queue restore and first audio;
- Fast Start and Quick Find before rich hydration;
- direct folder playback after cache loss;
- two USB volumes, mount-order changes, removal and reinsertion;
- removal while browsing, indexing and playing;
- active-playback enrichment;
- real ACC sleep/wake;
- DoFun/TWTHEME launch, metadata and key routing;
- bounded CPU, memory, I/O and thermal evidence.

STOP and preserve evidence when build identity, playback authority, source identity, package/signing lane or rollback is ambiguous.

## Rollback

- Remove the `startup-benchmark` module, profile plugin/dependency and profile text files to revert profile infrastructure without changing playback/library contracts.
- Remove the two startup workflows independently of application code.
- Remove the benchmark build type/source set to remove fixture seeding; maintained production variants are unaffected.
- Revert the report-export action independently; `PerfTimer` remains bounded.
- Any production optimisation discovered by later measurements requires a focused commit, executable regression test and documented rollback.
