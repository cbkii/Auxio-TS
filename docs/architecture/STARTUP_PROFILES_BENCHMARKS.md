# Startup profiles, benchmarks and integrated performance gates

Status: implementation plan for PR 3 of the Auxio-TS 0–60 second startup programme.

Base: completed PR 2 branch `cx/incremental-library-pipeline`.

Tracking issue: #181.

## Goal

Measure, optimise and prevent regressions in the complete startup and first-minute experience without weakening the corrected PR 1/PR 2 architecture. Profiles may accelerate the bounded immediate lane; they must not disguise complete-library construction, eager enrichment, unbounded queries or main-thread I/O.

## Evidence labels

- **Observed:** measured or structurally verified in the stated repository, emulator or host environment.
- **Inferred:** supported by repository evidence but not directly timed on the exact head unit.
- **Requires TS18 validation:** must be captured on `s9863a1h10_Natv`, Android 10/API 29, `TS18.2.2_20241210.165912_WINDOW-THEME1`.
- **Unsupported:** no acceptable evidence; do not present as a result.

## Workstream 1 — benchmark module and deterministic fixtures

- Add a maintained Android Macrobenchmark module compatible with the repository's Gradle/AGP/Kotlin baseline and API 29 runtime target.
- Keep benchmark-only dependencies out of production variants.
- Provide deterministic, versioned database fixtures around 500, 5,000 and 20,000 songs.
- Include at least two independent removable-source identities and representative Albums, Artists, Genres, Folders and Playlists.
- Load fixtures without requiring a complete source scan on each benchmark iteration.
- Validate fixture schema/revision compatibility before use and fail clearly rather than silently rebuilding unrelated state.
- Keep fixture generation bounded and reproducible; record seed, schema version and source layout.

## Workstream 2 — Baseline Profiles

Generate maintained Baseline Profiles from real critical journeys:

1. cold launch with saved-session primitive queue restore;
2. Fast Start first render and first actionable rows;
3. Play, Pause, Next and Previous through the single service/player/session;
4. Quick Find query, first result and direct playback;
5. `/storage/usbdisk0` and `/storage/usbdisk1` folder browse and direct playback;
6. first paged Songs and Albums interactions;
7. early MediaBrowser root and first page;
8. process-death relaunch with persisted queue and committed source generations.

Requirements:

- Generate profiles reproducibly from an explicit managed-device/emulator configuration.
- Check the generated profile into the intended production module/location only.
- Keep unstable identifiers and benchmark-only packages out of the profile.
- Verify release APK/AAB artefacts contain expected profile data.
- Record the profile-generation command and environment.

## Workstream 3 — Startup Profiles

- Add Startup Profile rules for the actual fast-path application, service, playback, primitive queue, Room projection, Quick Find, direct-folder and early MediaBrowser classes.
- Exclude full graph reconstruction, TagLib-rich extraction, artwork enrichment and inactive category generation from startup-critical rules.
- Prove that profile compilation does not move optional enrichment into the immediate lane.
- Measure with profile compilation enabled and disabled before claiming an improvement.

## Workstream 4 — Macrobenchmark journeys

Provide repeatable cold and warm measurements for:

- process start to first frame;
- process start to `PLAYBACK_SERVICE_READY`;
- process start to `QUEUE_READY`;
- process start to Fast Start first rows;
- Quick Find input to first result;
- Quick Find result selection to first audio;
- saved-session launch to first audio;
- Next command to next audio;
- first MediaBrowser root response;
- first paged Songs and Albums result;
- direct USB folder open and direct playback;
- `FULL_LIBRARY_READY` and enrichment completion as separate non-blocking milestones.

Each result must record:

- commit and variant;
- device/emulator identity and API level;
- compilation mode and profile state;
- fixture size and source count;
- iteration/warmup count;
- median, relevant percentiles and variance;
- failures, retries and excluded samples with reasons.

## Workstream 5 — timing and trace contract

Audit and retain bounded timing points for:

- process start;
- application initialisation complete;
- playback service ready;
- queue ready;
- media prepared;
- first frame;
- Fast Start first rows;
- Quick Find first result;
- first audio;
- skip command;
- next audio;
- full library ready;
- enrichment complete.

Rules:

- Timing must be monotonic and process/boot scoped.
- Instrumentation must remain local-only, bounded and disabled or negligible outside explicit diagnostics/benchmarks.
- No unbounded log accumulation.
- Record authority and run identity so process restarts are not merged accidentally.

## Workstream 6 — structural regression gates

Add executable checks that fail on architectural regression, including:

- immediate-lane source files referencing `DBCache.snapshot()`, `Musikr.loadCached()`, `MusicGraph`, `LibraryFactory`, complete `Library` sorting/filtering or `selectAllSongs()`;
- queue readiness depending on full-library readiness;
- main-thread Room, filesystem, metadata or artwork work in guarded paths;
- unbounded Room limits, folder counts, channel capacity or queue windows;
- Quick Find without debounce, cancellation, stale-result suppression or result caps;
- source observation that enumerates or starts a scan merely because a listener attaches;
- pending rows exposed to committed projections;
- full enrichment running at high pressure while audio is active;
- missing Baseline/Startup Profile artefacts from release outputs.

Use structural and semantic gates for shared CI. Do not impose flaky absolute wall-clock thresholds on hosted runners.

## Workstream 7 — workflow support

- Add scoped CI compilation for the benchmark module and profile-generation configuration.
- Verify profile artefact presence during release validation.
- Provide manual `workflow_dispatch` benchmark execution with fixture/profile/compilation-mode inputs.
- A scheduled trend run may be added only when cost and retention are bounded.
- Upload readable machine and human summaries containing the evidence fields above.
- Pin third-party actions to immutable SHAs and expose write credentials only to the smallest publishing step.
- Never weaken existing Android Build, Android Quality, TS18 contract or DoFun/Topway checks to make benchmark CI pass.

## Workstream 8 — optional component and integrated audit

Re-audit the complete three-PR path:

- MediaCodec remains the normal first renderer and FFmpeg the compatibility fallback.
- ReplayGain metadata work follows profile/settings policy; the runtime processor remains a no-op while disabled.
- artwork is loaded only for visible/current/widget/detail demand;
- visualiser, diagnostic, shortcut and launcher integrations do not duplicate player/session/service/notification infrastructure;
- profile rules do not eagerly initialise optional components;
- benchmark instrumentation does not alter normal startup decisions.

Fix only demonstrated in-scope defects. Record unrelated findings separately rather than expanding PR 3 without evidence.

## Repository acceptance gates

- Baseline and Startup Profiles cover the listed real journeys.
- Benchmark journeys execute against 500, 5,000 and 20,000-song fixtures.
- Profile generation is reproducible and expected profile data is present in release artefacts.
- Results disclose compilation mode, fixture size, median/percentiles and variance.
- Any improvement claim includes measured with/without-profile evidence.
- Structural gates prevent reintroduction of full-library startup work.
- Maintained debug and release variants compile.
- Unit tests, lint, formatting, workflow syntax, head-unit safety, TS18 APK-reference and DoFun/Topway checks pass.
- No temporary workflows, diagnostic logs or benchmark outputs remain in the source diff.

## Exact-device TS18 validation plan

**Requires TS18 validation.** Repository and emulator results do not prove these outcomes.

Validate on the exact unit and build identity:

- cold boot and warm application launch;
- process death and launcher restart;
- Bluetooth and hardware media-key starts;
- saved queue restore and first audio;
- Fast Start and Quick Find before rich hydration;
- `/storage/usbdisk0` and `/storage/usbdisk1` insertion, removal and reinsertion;
- mount-order changes and removal during playback/indexing;
- indexing/enrichment while audio is active;
- real ACC sleep/wake;
- DoFun/TWTHEME launch, metadata and key routing;
- memory, CPU, I/O and thermal observations around each bounded journey.

Capture boot ID, process identity, exact APK/commit, timing events, warnings and any source/mount changes. Stop the test and preserve evidence if playback authority, storage identity or vendor routing becomes ambiguous.

## Rollback

- Profile artefacts and benchmark modules must be removable without changing production playback/library contracts.
- Workflow additions must be independently revertible.
- Any production optimisation discovered by benchmarks requires its own focused commit, executable regression test and documented rollback.
