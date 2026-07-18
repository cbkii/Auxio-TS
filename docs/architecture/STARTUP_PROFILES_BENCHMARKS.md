# Startup profiles, benchmarks and integrated performance gates

Status: implemented PR 3 architecture for the Auxio-TS 0–60 second startup programme. Repository validation is automated; managed-emulator measurements and exact-device TS18 validation remain separate evidence classes.

Base: final review-ready PR 2 branch `cx/incremental-library-pipeline`.

Tracking issue: #181.

## Goal

Measure, optimise and prevent regressions in the startup and first-minute experience without weakening the corrected PR 1/PR 2 architecture. Profiles accelerate the bounded immediate lane; they must not disguise complete-library construction, eager enrichment, unbounded queries or main-thread I/O.

No percentage or latency improvement is claimed without captured with-profile and without-profile results for the same commit, variant, fixture, device, iterations and compilation mode.

## Evidence labels

- **Observed:** compiled, tested or measured in the explicitly named repository, emulator or host environment.
- **Inferred:** supported by repository evidence but not directly measured on the exact unit.
- **Requires device validation:** must be captured on `s9863a1h10_Natv`, Android 10/API 29, `TS18.2.2_20241210.165912_WINDOW-THEME1`.
- **Unsupported:** no acceptable evidence; do not present as a result.

## Benchmark and profile architecture

### Dedicated module and production separation

`startup-benchmark` is a dedicated `com.android.test` module using AndroidX Macrobenchmark 1.4.1 and the AndroidX Baseline Profile plugin. It mirrors the maintained `standard`, `topwayTwMusic` and `topwayTwMedia` identities without adding benchmark dependencies or exported benchmark authority to normal debug/release variants.

The app keeps a separate, unminified `benchmark` target for profile collection. Release remains minified and consumes the checked-in human-readable Baseline and Startup Profile rules. The app also applies the Baseline Profile plugin with automatic build-time generation disabled, so profile regeneration is an explicit, reproducible operation rather than a hidden release side effect.

The module provides:

- `BaselineProfileGenerator` with separate startup and interaction collections;
- `StartupMacrobenchmark` for cold, warm, hot and first-minute journeys;
- `CriticalJourneys` as a fail-closed UI, playback and MediaBrowser contract;
- `BenchmarkFixtures` for deterministic 500, 5,000 and 20,000-song logical fixtures;
- `BenchmarkFixtureController` for seeding outside measured iterations and retrieving bounded app-process timing evidence.

### Device split

Profile generation and runtime compatibility use different managed devices intentionally:

- **Pixel 6 API 35 AOSP:** Baseline/Startup Profile generation. `BaselineProfileRule` requires API 33+ unless the device is rooted.
- **Pixel 2 API 29 AOSP:** Macrobenchmark runtime compatibility matching the TS18 Android API level.

Neither managed device proves the exact TS18 hardware, DoFun launcher, USB mount or ACC lifecycle behaviour.

### Deterministic fixture contract

The benchmark-only `BenchmarkFixtureReceiver` is present only in the benchmark manifest. It transactionally seeds committed source ledgers, lightweight library rows and the primitive playback queue before measurement.

Fixture contract:

- schema version: `2`;
- seed: `18022026`;
- supported song counts: 500, 5,000 and 20,000;
- independent source identities: `direct:usb0` and `direct:usb1`;
- logical roots: `/storage/usbdisk0` and `/storage/usbdisk1`;
- representative Albums, Artists, Genres, Folders and Playlists;
- normal, second-USB-unavailable and interrupted-pending-generation source modes;
- deterministic titles, stable IDs, paths, dates and source assignment;
- a deterministic primitive queue session anchored at logical item 10;
- queue rows written transactionally in bounded 500-row batches rather than materialising one unbounded insert;
- real app-private WAV references for managed-emulator playback while preserving logical TS18 display paths and metadata.

For managed-emulator USB interaction only, the benchmark build maps the same logical roots to app-private fixture directories and creates real bounded WAV files. Production builds continue using only the exact app-facing `/storage/usbdiskN` paths. UI rows retain logical TS18 paths while benchmark playback receives the private physical fixture path. The mapping cannot ship in maintained debug or release variants.

The music and primitive-queue fixtures are seeded once outside measured iterations. They do not run a source scan or reconstruct the queue for every iteration.

## Baseline and Startup Profiles

### Startup collection

`BaselineProfileGenerator.startupPaths` sets `includeInStartupProfile = true` and covers only:

1. launcher start to Fast Start;
2. process-death relaunch through the same immediate startup lane.

### Interaction collection

`BaselineProfileGenerator.immediateInteractionPaths` sets `includeInStartupProfile = false` and covers:

1. Quick Find and direct playback of a real fixture;
2. Play/Pause, Next and Previous through the existing media authority;
3. bounded USB root browse and direct playback;
4. first paged Songs and Albums interactions;
5. early MediaBrowser root and first child page.

The generator filters captured rules to `org.oxycblt.auxio` and `org.oxycblt.musikr` production code and excludes benchmark/test packages. Startup-only and broader Baseline rules are therefore not conflated.

`app/src/main/startup-prof.txt` deliberately excludes `DBCache.snapshot()`, complete Musikr graph construction, `LibraryFactory`, `EvaluateStep`, metadata extraction, TagLib parsing and artwork enrichment. `scripts/check-startup-performance-contracts.sh` enforces this boundary and verifies generated profile outputs contain no benchmark packages.

## Macrobenchmark journeys

The maintained class includes:

- cold startup with `CompilationMode.None`;
- cold startup with required Baseline Profile;
- warm startup with required Baseline Profile;
- hot startup with required Baseline Profile;
- saved-session cold relaunch from the pre-seeded primitive queue to first audio;
- primitive Play, Pause, Next and Previous, including command-to-next-audio timing;
- Quick Find first result and result-to-first-audio timing;
- direct USB 0 and USB 1 folder open to real first audio;
- first paged Songs and Albums results;
- early MediaBrowser root/first page;
- cold start with the second USB unavailable;
- cold start with an interrupted pending generation while the prior committed generation remains readable;
- full-library-ready as a separate non-blocking milestone rather than a first-interaction prerequisite.

Every required UI and playback step fails the test when absent. Journeys do not silently return when search, USB, Songs, Albums or result rows are missing. Playback journeys require the target media session to enter the expected `PLAYING` or `PAUSED` state and media identity to change for skip operations.

The manual workflow selects:

- target flavour;
- 500, 5,000 or 20,000 songs across two sources;
- 3–30 iterations;
- macrobenchmark or profile-generation suite.

AndroidX JSON is retained as machine evidence. `scripts/summarize-startup-benchmarks.py` creates a human-readable table containing median, P90, P95, variance and sample count where supplied by the result schema. Context records commit, variant, suite, fixture schema/seed/size, source count, device/API, compilation modes, configured iterations, warm-up policy, retries and exclusions.

## Timing and trace contract

`PerfTimer` retains at most 256 monotonic in-memory events. It is active by default in debug and benchmark builds; maintained release builds require explicit user opt-in. Benchmark capture therefore records the measured app process without enabling production diagnostics.

`StartupPerformanceReport` exports a bounded local report containing authority, application/variant, boot ID where readable, fixture/source context, process/thread identity and monotonic events. The benchmark receiver returns the report through an explicit ordered broadcast encoded as Base64; tests verify non-empty application-start events. The user-facing settings export remains explicit and local-only.

Named Macrobenchmark trace sections measure Quick Find to first result, result selection to first audio, saved-session launch to first audio, Next command to changed audio, both USB folder playback paths, first Songs/Albums pages and the first MediaBrowser page. Application events separately retain service, queue, Fast Start, full-library and enrichment milestones.

Full-library-ready and enrichment-complete remain separate non-blocking milestones. They are never used as proxies for first frame, queue readiness, first rows or first audio.

## Structural and artefact gates

Repository checks fail when:

- profile files, benchmark module or required journeys disappear;
- the profile target is minified;
- profile generation falls back to unsupported API 29;
- startup and non-startup collections are conflated;
- captured rules can include benchmark packages;
- required UI journeys can silently skip;
- deterministic scales, two sources, source-failure modes, primitive queue seeding or real playback fixtures disappear;
- benchmark instrumentation is active in normal release policy;
- startup rules include complete graph, extraction or artwork classes;
- benchmark-only packages leak into production source sets;
- release APKs lack `assets/dexopt/baseline.prof`/`.profm`;
- the standard release AAB lacks R8 metadata marking at least one startup DEX;
- normal CI, TS18 APK-reference or DoFun/Topway contracts fail.

Shared runners use structural/semantic gates, not flaky hosted-runner wall-clock thresholds.

## Workflows

### `startup-performance.yml`

Runs automatically for the PR 3 branch and relevant stacked changes. It includes:

- workflow, shell and Python syntax;
- static startup contracts and summarizer self-test;
- formatting;
- app and Musikr unit tests;
- Android lint;
- head-unit safety;
- benchmark/profile instrumentation compilation;
- all maintained debug variants;
- all maintained release variants;
- standard release AAB generation;
- compiled Baseline Profile verification in all three release APKs;
- R8 startup DEX metadata verification in the release AAB;
- TS18 APK-reference contracts;
- DoFun/Topway source compatibility checks.

Third-party actions are pinned to immutable commits and repository permission is read-only.

### `startup-benchmarks.yml`

Manual, bounded managed-emulator workflow. API 35 is selected for profile generation and API 29 for Macrobenchmark. Generated profile files are validated, benchmark JSON is summarised, and machine/human evidence is uploaded with 14-day retention. No recurring schedule is enabled.

## Optional-component and complete-stack audit

The complete PR 1 → PR 2 → PR 3 path preserves these boundaries:

- one player, one MediaSession, one foreground playback service and one notification;
- MediaCodec remains the normal renderer and FFmpeg the compatibility fallback;
- ReplayGain extraction follows metadata profile/settings policy and runtime processing remains a no-op while disabled;
- artwork stays visible/current/widget/detail driven and bounded;
- visualiser, diagnostic, shortcut and launcher integrations do not duplicate playback authority;
- profile rules do not eagerly initialise optional enrichment;
- benchmark instrumentation does not alter normal debug/release startup decisions;
- the benchmark fixture receiver, primitive queue seeding and app-private USB mapping are absent from production variants.

Profiles cannot turn rich enrichment or complete graph construction into a hidden prerequisite of first interaction.

## Acceptance and evidence policy

Review readiness requires:

- benchmark/profile configuration compiles;
- deterministic fixtures cover all three scales and two sources;
- static and executable architecture checks pass;
- maintained debug and release variants build;
- unit tests, lint, formatting and workflow syntax pass;
- compiled release artefacts contain Baseline and Startup Profile evidence;
- TS18 APK-reference and DoFun/Topway checks pass;
- no temporary workflow, generated APK, report or benchmark result remains in source;
- every actionable review thread is resolved;
- PR description and final comment distinguish tests actually run from manual/device work still required.

A managed-emulator run at each supported fixture scale is required before marking the corresponding execution checklist complete. A matched with-profile/without-profile run is required before making a numerical profile-improvement claim. Exact TS18 measurements are required before making any exact-unit latency claim.

## Exact-device TS18 validation

**Requires device validation.** The bounded prerequisite/action/evidence/pass-fail/rollback matrix is in [`../validation/EXACT_TS18_STARTUP_VALIDATION.md`](../validation/EXACT_TS18_STARTUP_VALIDATION.md). It covers:

- cold boot, warm launch, process death and launcher restart;
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

- Remove `startup-benchmark`, the profile plugin/dependency and profile text files to revert profile infrastructure without changing playback/library contracts.
- Remove the two startup workflows independently of application code.
- Remove the benchmark build type/source set to remove fixture seeding and app-private USB mapping; maintained production variants are unaffected.
- Revert report export independently; `PerfTimer` remains bounded.
- Any production optimisation discovered by measurements requires a focused commit, executable regression test and documented rollback.
