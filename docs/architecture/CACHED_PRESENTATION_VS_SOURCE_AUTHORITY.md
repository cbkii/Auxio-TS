# Cached presentation versus source authority

This document describes the fourth and final stage of the Auxio-TS source-reliability programme. PR
#228 made the shared Musikr pipeline fail fast and terminate deterministically, PR #230 established
canonical source identity and deterministic DirectFS traversal, and PR #231 added generation/attempt
ownership, guarded publication and stage-aware watchdog protection. This stage reintroduces
incremental performance **on top of** those guarantees rather than around them.

Read alongside:

- [Fast interaction startup](FAST_INTERACTION_STARTUP.md) — readiness staging and the immediate lane.
- [Incremental library pipeline](INCREMENTAL_LIBRARY_PIPELINE.md) — ledger, generation protocol and
  metadata profiles.
- [Canonical music sources and DirectFS traversal](CANONICAL_SOURCES_AND_DIRECTFS.md) — source
  identity and traversal.
- [Source-scan attempt leases and watchdog](SOURCE_SCAN_ATTEMPT_LEASES.md) — attempt ownership and
  terminal outcomes.
- [Startup profiles and benchmarks](STARTUP_PROFILES_BENCHMARKS.md) — benchmark module and fixtures.

## Evidence labels

- **Observed** — verified in this repository by reading the referenced code, or exercised by host
  tests.
- **Inferred** — follows from the implemented contracts but is not directly measured.
- **Requires TS18 validation** — must be captured on the exact unit before any behaviour or latency
  claim is accepted.

## Preconditions

All five stated preconditions were verified present on the branch base before implementation:

| Precondition | Where | Confidence |
| --- | --- | --- |
| Shared Musikr fail-fast pipeline termination | `musikr/.../pipeline/*`, `Musikr.run` | Observed |
| Canonical source deduplication | `musikr/.../fs/CanonicalSourcePolicy.kt`, `app/.../locations/MusicSourceCanonicalizer.kt` | Observed |
| Deterministic DirectFS traversal | `musikr/.../fs/direct/DirectFS.kt` | Observed |
| Source checkpoint attempt leasing | `app/.../music/SourceConfigurationCheckpoint.kt`, `IndexRequestPolicy.checkpointAuthority` | Observed |
| Exactly one terminal outcome per authoritative scan | `MusicRepositoryImpl.indexWithProfile`, `IndexingTerminalOutcome`, `IndexingSessionGate` | Observed |

## Upstream versus Auxio-TS

**Observed** by comparing the module trees of `OxygenCobalt/Auxio` and this fork:

| Behaviour | Upstream Auxio | Auxio-TS |
| --- | --- | --- |
| Filesystem backends | `musikr/fs/{mediastore,saf}` only | adds `musikr/fs/direct` (DirectFS) |
| Source identity | none; `Location` equality only | `CanonicalSourcePolicy`, `SourceIdentity`, `SourceSnapshot`, `StoragePathAliasPolicy` |
| Cache | `musikr/cache/Cache.kt` plus `db` | adds `IncrementalScan`, `SourceFingerprintReusePolicy`, `StartupProjections` |
| Library construction | one complete rich graph per load | adds `library/MetadataWorkPolicy` and Lean/Full profiles |
| App-side source policy | `MusicRepository`, `MusicSettings`, `locations` | adds checkpoints, attempt leases, index-request policy, startup readiness/optional-work gates, configured-source specs, generated-playlist coordination |
| Startup | library available only after the rich graph is built | staged readiness with a bounded cached presentation lane |

**Inferred:** upstream therefore has a single lane. Everything the user can see is produced by the
same reconstruction that also decides what the filesystem contains, so a launch is as slow as a
complete rebuild and there is nothing to show while it runs. Auxio-TS v6.0.7
(`1a4e2be27bd330e6c5ccf496005a368a8ddb1351`), PR #206 (`65da5e5`), PR #208 (`8eeb8f4`) and PR #209
(`9e86219`) progressively added incremental reuse and fast-start projections to remove that cost.
The correctness programme (#228/#230/#231) then re-established that reuse must never *be* the
authority. This stage keeps the speed and keeps that separation explicit.

## The two lanes

### Cached presentation lane — never source authority

Responsible for immediate browse/search projection, restoration of the last committed library,
playback queue restoration and last-known-good metadata. It reads committed Room projections
(`StartupProjections`) and the durable playback primitives.

It may **never**:

- declare a new filesystem source generation committed;
- advance the source ledger, configuration checkpoint or configuration revision;
- turn Activity/service/process lifecycle into source-enumeration authority;
- delete covers or cache resources;
- block first interaction on provider queries, root checks or generated playlists.

`StartupReadinessController` publishes capability from cached state. `StartupOptionalWorkGate` may
hold compatibility-graph hydration and normalized backfill behind Queue Ready and a terminal restore
outcome, but a slow hydration is not permission to start a recovery scan. Generated-playlist wrapper
publication also waits for optional-work readiness, while actual playlist compilation is lazy and
occurs only when a playlist surface requests the derived collection.

### Authoritative source lane — the only source of truth

Owns one immutable canonical source configuration, enumeration, extraction, evaluation, commit or
rollback, and exactly one terminal result. Durable source-configuration ownership is required for
initial configuration, user retry and automatic mounted-source replacement. Explicit user refresh or
rescan and explicitly enabled source-observation modes may also request indexing, but ordinary
Activity resume, playback-service recreation, process recreation and cached hydration may not.

On the maintained Topway product, a successful committed library is the default authority between
positive source-change signals. Initial/pending configuration is recovered from its durable
checkpoint; normal visible startup no longer contains a timed compatibility-recovery fallback.

Optional enrichment (`METADATA_ENRICHMENT`) and generated playlists deliberately hold no source
checkpoint lease and are outside source-membership authority.

## Source configuration identity

`SourceConfigurationIdentity` is the single definition of the configuration revision, shared by
`ConfiguredSourcePolicy.snapshot()` (browse/restore decisions) and
`MusicRepositoryImpl.sourceConfigurationRevision()` (the value written to the per-source ledger).
Before this change the two disagreed, and the ledger copy was order-sensitive and included
interpretation settings.

The revision decides whether every committed source generation is reusable, so:

1. semantically equal configurations must produce the same identity — canonical keys, sorting and
   deduplication remove alias, ordering and duplicate noise before hashing;
2. settings that only reinterpret already-extracted rows must be excluded — they may still request
   a library refresh, but they must never invalidate filesystem authority.

### Included material

| Field | Why it is authoritative |
| --- | --- |
| location mode | selects the backend and therefore the meaning of every configured root |
| canonical source roots, with origin and traversal scope (SAF/DirectFS) | the exact set of traversed roots and what each traversal may reach |
| canonical exclusions (SAF/DirectFS) | pruned subtrees change which files a successful generation may legitimately contain |
| hidden-file policy (SAF/DirectFS) | changes the eligible file set of every root |
| MediaStore filter mode, filtered roots, non-music filtering | the provider-side equivalent of roots and exclusions |
| TS18 system source filter (SAF/MediaStore only) | narrows SAF path keywords and relaxes the provider `IS_MUSIC` heuristic, so it changes which rows a generation may contain |
| root access policy (DirectFS only) | decides whether root-prepared candidate roots may become readable DirectFS roots at all |

### Deliberately excluded material

| Field | Why it is not authoritative |
| --- | --- |
| separators, intelligent sorting | tag interpretation only; cached rows hold raw tags, so invalidating generations would force a needless full re-enumeration and re-extraction |
| SAF multithread, scan priority, observation mode | resource/scheduling policy that cannot change the authoritative content of a source |
| generated playlists, performance capture, dynamic shortcuts | non-authoritative presentation or diagnostics toggles |
| source ordering and alias spelling | presentation of the same canonical set |
| availability and permission state | transient runtime conditions carried by the per-source ledger and fingerprints; a temporarily unavailable USB volume must not invalidate every other source |

The revision is a deterministic 64-bit FNV-style hash of a stable textual encoding rather than
`String.hashCode()`, which is only 32-bit and is not a durable cross-platform contract.

**Upgrade note (Observed):** because the formula changed, the persisted ledger revision will not
match after upgrading, so the first scan after this change re-enumerates every source exactly once.
That is the intended, safe direction of failure.

## Fingerprint confidence rules

`SourceFingerprintReusePolicy` classifies what a fingerprint actually proves, independent of the
strength an adapter claims:

| Confidence | Meaning | Maintained Topway reuse |
| --- | --- | --- |
| `STRONG` | provider-issued token covering the complete configured source | valid until the token changes |
| `ADVISORY` | bounded, cheap observation such as a shallow directory sample | reusable while unchanged; wall-clock age alone is not scan authority |
| `UNAVAILABLE` | no trustworthy change token | cannot suppress an index that has otherwise been authorised |

A missing or blank token is downgraded to `UNAVAILABLE` regardless of the reported strength, so an
adapter that returns `ADVISORY` with nothing in it cannot suppress an authorised validation pass.

**Observed:** no filesystem adapter currently produces `AUTHORITATIVE`. DirectFS and SAF produce
`ADVISORY` when they can observe a root sample or root-document metadata; MediaStore produces
`ADVISORY` only when the platform volume-version token exists. An advisory shallow directory
fingerprint is therefore never treated as proof of the whole tree. Instead, the maintained Topway
product relies on explicit invalidation, an observed token change, a configuration change or an
explicit user action before allowing a new source generation. Automatic observation modes continue
to receive provider/mount invalidation signals; they do not gain a second hidden wall-clock timer.

The generic Musikr policy still supports bounded advisory expiry for callers that explicitly enable
it. `IncrementalIndexPlanner` disables that expiry for the maintained Topway product. When an old
advisory timestamp is the only reason a LEAN plan would scan, the committed source is reused. For a
FULL request, age-only expiry is downgraded to `METADATA_PROFILE_UPGRADE`, keeping any genuine
artwork/metadata repair in the optional enrichment lane rather than source-membership authority.

Reuse is reached only after correctness gates, which are reported as a `SourceScanReason`:

1. `FORCED` — explicit cache bypass/rescan;
2. `NEVER_COMMITTED` — no committed generation to reuse;
3. `PREVIOUS_SCAN_INCOMPLETE` — the last authoritative attempt did not finish;
4. `CONFIGURATION_CHANGED` — the ledger revision differs from the current identity;
5. `INVALIDATED` — an enabled observer or mount path positively invalidated the source;
6. `FINGERPRINT_CHANGED` — token or claimed strength differs from the committed value;
7. `FINGERPRINT_UNAVAILABLE` — no token can justify reuse for an otherwise authorised index;
8. `ADVISORY_FINGERPRINT_EXPIRED` — available only to callers that explicitly enable periodic
   advisory validation; suppressed as source authority by the maintained Topway planner;
9. `METADATA_PROFILE_UPGRADE` — optional richer metadata work after source correctness is otherwise
   established.

## First-scan and incremental contracts

### A new or materially changed configuration

- one source-authoritative scan under an attempt lease;
- cache bypass still uses the source-aware incremental planner in forced mode when the cache and
  filesystem expose the required capabilities;
- stale source rows are never proof that the new configuration succeeded — the changed revision
  forces `CONFIGURATION_CHANGED` for every affected source;
- the canonical effective source set is used, not the raw persisted list;
- publication happens only while the generation and attempt are still current;
- on failure the previous committed library and ledger remain readable and an actionable
  `SourceScanOutcome` is reported;
- a Lean profile may publish a playback-first base library when it is still a valid authoritative
  base.

### Per source, on an incremental scan

| Source state | Behaviour |
| --- | --- |
| unchanged strong fingerprint | committed rows reused and streamed into the published library |
| unchanged advisory fingerprint on maintained Topway product | committed rows reused; age alone does not enumerate |
| changed or explicitly invalidated | enumerated and updated in place |
| temporarily unavailable | prior committed generation remains readable; the source is reported unresolved and full success is impossible |
| removed by the user | omission is a candidate only; prior rows stay visible until the replacement configuration commits successfully |
| failed/cancelled replacement | candidate removals are not applied; the prior configuration remains visible |
| re-add of a previously removed source | retained rows stay hidden while planning and after failure; a successful generation restores committed visibility |
| repeated unchanged removal | already-hidden ledgers are not rediscovered as removal work, so the next identical plan is no-work |
| truncated (DirectFS depth/count bounds) | `Truncated` outcome; full success is never claimed |
| partial multi-source | healthy siblings commit; failed or unobserved sources retain their generation and unresolved keys |

DirectFS removable-media handling is policy-scoped. Unmount/eject/removal marks configured sources
temporarily unavailable while preserving the committed library. In `MANUAL` observation mode, a
later `ACTION_MEDIA_MOUNTED` is availability information only and does not invalidate or rescan the
source. `WHEN_IDLE` and `CONTINUOUS` retain the explicitly enabled bounded mounted-source refresh.

Planner-detected unavailable sources are converted into request-scoped temporary failures before
repository classification. Retained rows therefore produce `Partial`, while an all-unavailable scan
with no readable rows remains `TemporarilyUnavailable`; neither can acknowledge full success.

## Metadata, enrichment and generated playlists

The boundaries are: base authoritative metadata (a usable library), rich enrichment, artwork
hydration, and generated playlists. Each later stage may fail without invalidating the earlier one.

**Observed guarantees:**

- enrichment holds no checkpoint lease (`IndexRequestPolicy.requiresAttemptClaim` excludes it) and
  never records a source outcome (`recordsSourceOutcome` excludes it), so an enrichment failure
  cannot regress a committed source generation or reopen the source checkpoint;
- age-only advisory expiry in a maintained-product FULL request is downgraded to
  `METADATA_PROFILE_UPGRADE`, so it cannot masquerade as a new source generation;
- enrichment does not allocate or advance a source generation, change fingerprints/configuration
  authority, add or remove source membership, or run destructive cover cleanup;
- incomplete enrichment is surfaced as a partial optional result and does not emit
  `EnrichmentComplete`; the committed base library and source checkpoint remain valid;
- the artwork-revision compatibility coordinator checkpoints its successful revision-2 repair in
  app preferences, so process recreation does not repeatedly submit the same repair after success;
- a non-authoritative result whose `configurationGeneration` is older than the current
  configuration generation is discarded before publication and reported as `SUPERSEDED`;
- generated playlists are opt-in and default off, are derived after the base library is available,
  are cancellable and coalesced, cannot mutate source authority, and changing the preference cannot
  trigger a source rescan;
- generated-playlist compilation itself is lazy and memoized inside the wrapper view, so restoring
  the base library with generated playlists enabled does not immediately sort/group the full song
  collection. Compilation occurs on first playlist access and is then reused by that view.

## Cleanup safety

`LibraryResultImpl.cleanup()` retains only the covers referenced by the library that was just
published, so it is destructive whenever that library is not a complete authoritative view.
`CoverCleanupPolicy` gates the invocation. Cleanup runs only when **all** of the following hold:

1. a new library was actually published;
2. the publication used the complete metadata profile;
3. the run was not enrichment-only;
4. no configured source is still unresolved;
5. no source was unobserved during this scan;
6. the terminal outcome is `Success` or `AuthoritativeEmpty`.

Any Lean, enrichment-only, failed, partial, truncated, permission or unavailability outcome therefore
cannot remove resources still required by the last-known-good library.

**Observed:** cleanup is no longer restricted to non-incremental scans. Reused sources stream their
complete committed rows into the published library (`ExploreStep` +
`IncrementalCache.reusedCachedFiles`), so an incremental publication that satisfies the conditions
above is a complete authoritative view and can safely reclaim expired covers.

## Tests

Focused host tests include:

- `SourceFingerprintReusePolicyTest` — strong/advisory confidence, automatic expiry, manual no-expiry,
  blank tokens, clock rollback, strengthened/changed tokens and correctness-gate precedence;
- `IncrementalScanManualAuthorityTest` — age-only LEAN work becomes reuse, FULL work becomes optional
  enrichment, and explicitly enabled automatic expiry remains available;
- `StartupScanAuthorityPolicyTest` — maintained Topway visible/background lifecycle starts do not
  acquire scan authority;
- `RemovableStorageEventPolicyTest` — manual remount is availability-only while opt-in automatic
  observation modes retain mounted-source refresh authority;
- `GeneratedPlaylistLibraryViewTest` — base-library access does not compile generated playlists,
  first playlist access compiles once, and disabling before access remains allocation-free;
- `ArtworkRepairPolicyTest` and `ArtworkEnrichmentRevisionTest` — only FULL enrichment can satisfy
  the compatibility repair and the durable Musikr revision becomes a one-time repair;
- `SourceConfigurationIdentityTest` — one shared identity, order/alias independence, mode scoping
  and exclusion of interpretation/resource settings;
- `IncrementalIndexPlannerTest` — forced first scans, source-aware fallback rules, scoped retries and
  removal-only empty configurations;
- `IncrementalScanStoreTest` — generation commit/rollback, enrichment membership isolation,
  deferred removals, removal-only commits, repeated-removal no-work, failed re-add visibility,
  temporary unmount preservation and multi-source isolation;
- `CoverCleanupPolicyTest` — cleanup only after a complete authoritative publication.

Representative scenario coverage:

| # | Scenario | Where |
| --- | --- | --- |
| 1 | immediate cached startup with no provider access | `StartupLibraryPolicyTest`, `DeferredStartupHydrationTest`, `ImmediateLaneArchitectureTest` |
| 2 | repeated visible/background lifecycle starts cannot scan on maintained product | `StartupScanAuthorityPolicyTest`, `IndexingHolder` static contract |
| 3 | unchanged advisory source remains reusable without wall-clock scan authority | `SourceFingerprintReusePolicyTest`, `IncrementalScanManualAuthorityTest` |
| 4 | first configuration scan using pending/committed generations | `IncrementalIndexPlannerTest`, `IncrementalScanStoreTest`, `RepositoryIndexRequestQueueTest` |
| 5 | failed new configuration preserves the prior library | `IncrementalScanStoreTest`, `SourceScanCommitPolicyTest` |
| 6 | source-local incremental update | `IncrementalScanStoreTest` |
| 7 | manual DirectFS remount does not request a scan | `RemovableStorageEventPolicyTest` |
| 8 | temporarily unavailable source | `IncrementalScanStoreTest`, `SourceAvailabilityOutcomePolicyTest` |
| 9 | partial multi-source success | `IncrementalScanStoreTest`, `SourceScanOutcomeTest`, `SourceAvailabilityOutcomePolicyTest` |
| 10 | enrichment failure after a base commit | `IncrementalScanStoreTest`, `IncrementalResultFailurePolicyTest` |
| 11 | generated playlists do not trigger source authority and compile lazily | `MusicSettingsIndexingTriggerTest`, `GeneratedPlaylistCoordinatorTest`, `GeneratedPlaylistLibraryViewTest` |
| 12 | stale enrichment after a newer generation | `RepositoryIndexRequestQueueTest.staleEnrichmentIsDiscardedAfterANewerSourceGeneration` |
| 13 | cover cleanup after successful publication | `CoverCleanupPolicyTest` |
| 14 | cleanup skipped after failure or enrichment | `CoverCleanupPolicyTest` |
| 15 | process recreation during cached presentation | `StartupLibraryPolicyTest` |
| 16 | process recreation during an authoritative scan | `IncrementalScanStoreTest`, `MusicSourceConfigurationTest`, `InterruptionOutcomeRecordingPolicyTest` |
| 17 | repeated warm launch | `StartupLibraryPolicyTest`, `DeferredStartupHydrationTest` |
| 18 | large synthetic libraries | 5,000-row committed fixture in `IncrementalScanStoreTest`; 5,000 and 20,000 in `startup-benchmark` fixtures |
| 19 | API 29 storage behaviour | Robolectric `@Config(sdk = [29])` across music/startup suites and maintained API 29 CI |
| 20 | DirectFS, SAF and MediaStore backends | `DirectFsRootPolicyTest`, `DirectFsTraversalTest`, `MediaStoreFilterPolicyTest`, `MusicSourceConfigurationTest` |

Assertions are on state, authority and bounded work; ordinary CI does not assert device-independent
latency thresholds.

## Benchmarks

Comparative metrics are collected through the existing `startup-benchmark` module and its
deterministic 500 / 5,000 / 20,000-song fixtures. Report **median and variability** across
iterations, never a single run, and always with the commit, variant, fixture, device, iteration
count and compilation mode attached.

| Metric | Fixtures |
| --- | --- |
| cold launch to browse-ready | 500 / 5,000 / 20,000 |
| warm launch to browse-ready | 500 / 5,000 / 20,000 |
| playback restore readiness | 5,000 |
| source preflight | 5,000, one and two sources |
| unchanged-source path | 5,000 / 20,000 |
| one changed file | 5,000 / 20,000 |
| 100 changed files | 5,000 / 20,000 |
| full authoritative scan | 5,000 / 20,000 |
| lean publication | 20,000 |
| rich enrichment | 20,000 |
| generated playlists disabled and enabled | 20,000 |

**Requires TS18 validation:** managed-emulator results do not prove exact-device timing.

## Physical TS18 validation — outstanding

Not performed in this change. [Evidence confidence: Requires TS18 validation]
[Porting decision: Requires exact-device runtime validation]. The important boundaries are:

1. one explicit/manual source scan reaches a terminal committed state;
2. three consecutive warm launches produce no source traversal;
3. process kill and relaunch produce no source traversal;
4. playback-service recreation produces no source traversal;
5. Android reboot/cold boot restores the committed library without source traversal;
6. ACC sleep/wake restores the committed library without source traversal;
7. DirectFS USB removal preserves the last committed library;
8. DirectFS USB remount in `MANUAL` mode does not rescan;
9. `WHEN_IDLE`/`CONTINUOUS` remount behaviour remains bounded when explicitly enabled;
10. generated playlists enabled do not create an indexing session on launch;
11. first playlist access may perform derived compilation but not source traversal;
12. completed artwork-revision repair does not repeat after process recreation;
13. no stale `RUNNING` attempt remains;
14. no indefinite `DISCOVERING` occurs;
15. production `com.tw.media` launcher/DoFun behaviour remains intact.

## Safety boundaries preserved

Repository/static compatibility claim: [Evidence confidence: Observed in current code and CI]
[Porting decision: Preserve for the maintained product; exact TS18 behaviour requires runtime validation].

One playback service, one queue owner, one MediaSession and one notification authority are
unchanged. Root is never the playback authority and protected storage is never enumerated as root.
The one `com.tw.media` product preserves its stock-compatible component contract, API 29 support is
preserved, and no APKs, logs, credentials, Room schema changes or temporary workflows are present
in the final diff.
