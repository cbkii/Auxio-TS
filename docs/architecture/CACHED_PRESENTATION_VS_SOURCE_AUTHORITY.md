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
- decide that a source is unchanged;
- delete covers or cache resources;
- block first interaction on provider queries, root checks or generated playlists.

`StartupReadinessController` publishes capability from cached state; `StartupOptionalWorkGate` holds
compatibility-graph hydration, normalized backfill and generated-playlist compilation behind Queue
Ready and a terminal restore outcome.

### Authoritative source lane — the only source of truth

Owns one immutable canonical source configuration, one generation and attempt, enumeration,
extraction, evaluation, commit or rollback, and exactly one terminal outcome. It is entered only
through `MusicRepositoryImpl.indexWithProfile` with a request whose reason requires an attempt claim
(`INITIAL_CONFIGURATION`, `USER_RETRY`, `STORAGE_MOUNTED`).

Optional enrichment (`METADATA_ENRICHMENT`) and generated playlists deliberately hold no checkpoint
lease and are outside authority ownership.

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

| Confidence | Meaning | Reuse |
| --- | --- | --- |
| `STRONG` | provider-issued token covering the complete configured source | valid until the token changes |
| `ADVISORY` | bounded, cheap observation such as a shallow directory sample | bounded by `ADVISORY_REFRESH_MS` (6 h) since the last successful scan |
| `UNAVAILABLE` | no trustworthy change token | never; the source is enumerated |

A missing or blank token is downgraded to `UNAVAILABLE` regardless of the reported strength, so an
adapter that returns `ADVISORY` with nothing in it can never suppress a scan. A persisted advisory
timestamp from the future or across a wall-clock rollback also fails safe to a scan.

**Observed:** no filesystem adapter currently produces `AUTHORITATIVE`. DirectFS and SAF produce
`ADVISORY` when they can observe a root sample or root-document metadata; MediaStore produces
`ADVISORY` only when the platform volume-version token exists. An advisory shallow directory
fingerprint is therefore never treated as definitive proof that a large source is unchanged.

Reuse is only reached after the correctness gates, which are evaluated in this order and reported as
a `SourceScanReason`:

1. `FORCED` — cache bypass requested;
2. `NEVER_COMMITTED` — no committed generation to reuse;
3. `PREVIOUS_SCAN_INCOMPLETE` — the last authoritative attempt did not finish;
4. `CONFIGURATION_CHANGED` — the ledger revision differs from the current identity;
5. `INVALIDATED` — an observer or mount event invalidated the source;
6. `FINGERPRINT_CHANGED` — token or claimed strength differs from the committed value;
7. `FINGERPRINT_UNAVAILABLE` / `ADVISORY_FINGERPRINT_EXPIRED` — confidence is insufficient;
8. `METADATA_PROFILE_UPGRADE` — only after source correctness is otherwise established, request
   richer metadata without taking source membership authority.

The profile-upgrade check is deliberately last. A changed, invalidated or untrusted source remains
an authoritative source scan even when the request also asks for richer metadata.

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
| unchanged, strong fingerprint | committed rows reused and streamed into the published library |
| changed | enumerated and updated in place |
| temporarily unavailable | prior committed generation remains readable; the source is reported unresolved and full success is impossible |
| removed by the user | omission is a candidate only; prior rows stay visible until the replacement configuration commits successfully |
| failed/cancelled replacement | candidate removals are not applied; the prior configuration remains visible |
| re-add of a previously removed source | retained rows stay hidden while planning and after failure; a successful generation restores committed visibility |
| repeated unchanged removal | already-hidden ledgers are not rediscovered as removal work, so the next identical plan is no-work |
| truncated (DirectFS depth/count bounds) | `Truncated` outcome; full success is never claimed |
| partial multi-source | healthy siblings commit; failed or unobserved sources retain their generation and unresolved keys |

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
- an enrichment-only plan is created only when every selected source is being scanned solely for
  `METADATA_PROFILE_UPGRADE`; correctness reasons always take precedence;
- enrichment does not allocate or advance a source generation, change fingerprints/configuration
  authority, add or remove source membership, or run destructive cover cleanup;
- incomplete enrichment is surfaced as a partial optional result and does not emit
  `EnrichmentComplete`; the committed base library and source checkpoint remain valid;
- a non-authoritative result whose `configurationGeneration` is older than the current
  configuration generation is discarded before publication and reported as `SUPERSEDED`. The newer
  generation owns the reported source outcome, so the discarded request records none;
- generated playlists are opt-in and default off, are derived after the base library is available,
  are cancellable and coalesced, cannot mutate source authority, and changing the preference cannot
  trigger a source rescan (the preference is excluded from the configuration identity).

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

Focused host tests added or extended by this stage include:

- `SourceFingerprintReusePolicyTest` — strong/advisory confidence, expiry, blank tokens, clock
  rollback, strengthened/changed tokens and correctness-gate precedence;
- `SourceConfigurationIdentityTest` — one shared identity, order/alias independence, mode scoping
  and exclusion of interpretation/resource settings;
- `IncrementalIndexPlannerTest` — forced first scans, source-aware fallback rules, scoped retries and
  removal-only empty configurations;
- `IncrementalScanStoreTest` — generation commit/rollback, enrichment membership isolation,
  deferred removals, removal-only commits, repeated-removal no-work, failed re-add visibility,
  temporary unmount preservation and multi-source isolation;
- `IncrementalResultFailurePolicyTest` and `SourceAvailabilityOutcomePolicyTest` — planner-detected
  unavailability and incomplete enrichment cannot be reported as full success;
- `CoverCleanupPolicyTest` — cleanup only after a complete authoritative publication.

These complement the existing suites. Coverage of the required scenario matrix
(**Observed** unless noted):

| # | Scenario | Where |
| --- | --- | --- |
| 1 | immediate cached startup with no provider access | `StartupLibraryPolicyTest`, `DeferredStartupHydrationTest`, `ImmediateLaneArchitectureTest` |
| 2 | cached startup then unchanged strong-fingerprint reuse | `IncrementalScanStoreTest`, `SourceFingerprintReusePolicyTest` |
| 3 | advisory fingerprint requiring validation | `SourceFingerprintReusePolicyTest`, `IncrementalIndexPlannerTest` |
| 4 | first configuration scan using pending/committed generations | `IncrementalIndexPlannerTest`, `IncrementalScanStoreTest`, `RepositoryIndexRequestQueueTest` |
| 5 | failed new configuration preserves the prior library | `IncrementalScanStoreTest`, `SourceScanCommitPolicyTest` |
| 6 | source-local incremental update | `IncrementalScanStoreTest` |
| 7 | removed source and removal-only configuration | `IncrementalScanStoreTest` |
| 8 | temporarily unavailable source | `IncrementalScanStoreTest`, `SourceAvailabilityOutcomePolicyTest` |
| 9 | partial multi-source success | `IncrementalScanStoreTest`, `SourceScanOutcomeTest`, `SourceAvailabilityOutcomePolicyTest` |
| 10 | enrichment failure after a base commit | `IncrementalScanStoreTest`, `IncrementalResultFailurePolicyTest` |
| 11 | generated-playlist failure after a base commit | `GeneratedPlaylistCoordinatorTest` |
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

Not performed in this change. All eleven items remain outstanding
[Evidence confidence: Requires TS18 validation]
[Porting decision: Requires TS18 runtime validation]:

1. MediaStore with permission;
2. SAF persisted tree;
3. DirectFS internal Music folder;
4. DirectFS removable USB;
5. cold boot;
6. process kill;
7. ACC sleep/wake;
8. source unmount/remount;
9. cancel/retry;
10. three consecutive warm launches;
11. playback during deferred enrichment.

## Safety boundaries preserved

One playback service, one queue owner, one MediaSession and one notification authority are
unchanged. Root is never the playback authority and protected storage is never enumerated as root.
The `topwayTwMusic` and `topwayTwMedia` variants keep distinct package/component contracts, API 29
support is preserved, and no APKs, logs, credentials, Room schema changes or temporary workflows are
present in the final diff.
