# Incremental library and enrichment pipeline

This document describes the second stage of the Auxio-TS startup-performance programme. It builds
on the Fast Interaction path and keeps playback, bounded browsing and Quick Find independent from
complete rich-library work.

## Status labels

- **Observed** — implemented and exercised by host tests or repository CI.
- **Inferred** — follows from the implemented contracts but still needs runtime evidence.
- **Requires device validation** — must be tested on the exact TS18 unit before a latency or
  vendor-behaviour claim is accepted.

## Goals

The pipeline must:

- skip provider traversal only when a configured source is safe to reuse;
- extract only new, changed or explicitly enriched files;
- keep pending scan data invisible until a successful source commit;
- retain each source's last-known-good generation after cancellation, process death, transient
  unavailability or provider failure;
- defer user-removal visibility until the replacement configuration commits successfully;
- allow healthy sources to commit when another source fails;
- use a real Lean pass for immediate playback and browsing metadata;
- defer Full relationship, ReplayGain, MusicBrainz, rich-date and artwork work until playback is
  idle;
- keep optional enrichment outside source membership authority;
- invalidate inactive categories cheaply and load them only when requested;
- preserve the one-player, one-session and one-playback-service architecture.

## Durable source ledger

Every configured MediaStore volume, SAF tree or DirectFS root has an independent
`SourceLedgerData` row. The row records:

- stable source key and source type;
- root URI/path;
- **committed visibility** (`available`), not merely the latest physical mount observation;
- advisory or authoritative fingerprint;
- invalidation and committed-invalidation versions;
- last committed and pending generations;
- configuration revision;
- committed metadata profile and enrichment revision;
- last successful scan and incomplete-scan state.

**Observed:** source observers attach before rich-library hydration. Attachment does not enumerate
files or construct a library; notification bursts are debounced and persist only source invalidation
before scan planning.

Source setup also has a configuration-level `SourceConfigurationCheckpoint`. Applying a new setup
atomically writes `PENDING`; claiming it records `RUNNING` without clearing durability. Only a scan
carrying the matching generation can acknowledge `COMMITTED` or `PARTIALLY_COMMITTED`. Cancellation,
process death, permission loss, and all-source failure retain retryable state; an older scan cannot
acknowledge a newer configuration. Partial commits retain unresolved configured keys and never cause
a retry loop for healthy siblings.

`ConfiguredSourceSpec` is parsed from raw persisted URIs before provider/file opening. A
`ConfiguredSourceAwareFS` combines real snapshots with synthetic permission-required or temporarily
unavailable snapshots. Synthetic rows are recovery metadata only: the app UID remains the sole scan
and playback authority, and root remains candidate discovery/preparation only.

MediaStore uses the platform volume-version token on Android 10. SAF uses root-document metadata.
DirectFS uses a bounded root sample and periodic refresh. DirectFS/SAF fingerprints are advisory:
they avoid needless warm work but are not treated as proof that an entire FAT/document tree cannot
have changed.

## Reuse policy

`SourceFingerprintReusePolicy` evaluates source correctness before metadata enrichment. The order is:

1. forced/cache-bypassing request;
2. never-committed source;
3. incomplete prior authoritative scan;
4. configuration revision change;
5. explicit invalidation;
6. fingerprint value or claimed-strength change;
7. unavailable or expired advisory evidence, including wall-clock rollback/future timestamps;
8. metadata-profile upgrade.

Only the final reason may create an enrichment-only plan. A profile request cannot disguise a
changed, invalidated or otherwise uncertain source.

## Generation protocol

For each source selected for authoritative work:

1. Allocate a pending generation.
2. Clear stale pending rows left by a killed process.
3. Discover files with bounded channels and worker count.
4. Record every seen file, including unchanged cache hits.
5. Extract only cache misses, modified files or rows requiring the requested metadata profile.
6. Stage changed cache rows separately from committed readers.
7. Publish all seen rows and changed metadata in one Room transaction.
8. Mark files missing from the successful generation unavailable and reconcile them in SQL.
9. Advance the source ledger only after the transaction succeeds.
10. Delete the older indexed generation after the new generation is committed.

Cancellation or failure clears pending rows and leaves the previous committed generation readable.
Cache-bypassing first/materially changed scans use the same source-aware generation protocol in
forced mode whenever `IncrementalCache` and `SourceAwareFS` are available. The legacy write-only path
is retained only when those capabilities genuinely do not exist.

### Multi-source isolation

Source adapters collect failures by stable source key. A failed source has its pending rows discarded
and keeps its prior committed generation. Successful sibling sources still commit in the same
transaction.

The rich compatibility graph is rebuilt from committed, currently visible rows when a source-local
failure, unobserved source, enrichment-only run or committed removal makes the in-flight graph
non-authoritative. Fast Start and Quick Find continue to use committed Room projections throughout.

## Transactional source removal and re-add

Physical mount state, configured membership, committed visibility, app access and playback viability
are separate.

- **Temporary absence at planning:** do not scan or delete the source. Keep its committed generation
  readable, report it unresolved, and prevent a full-success terminal outcome.
- **User removes a configured source:** omission is only a candidate removal. Its rows remain visible
  until the replacement configuration completes successfully.
- **Replacement fails or is cancelled:** candidate removals are not applied; the previous committed
  configuration remains visible.
- **Successful replacement:** candidate removals are atomically marked unavailable after the
  remaining configuration commits. Retained database rows remain available for rollback/re-add but
  are excluded from committed projections.
- **Removal-only configuration:** an empty source set still produces a deterministic commit when
  visible prior sources must be removed.
- **Repeated unchanged removal:** ledgers already committed as unavailable are not rediscovered as
  removal work, so the next identical plan is a true no-work plan.
- **Failed re-add of a previously removed source:** retained rows stay hidden during planning and
  after failure. Only a successful replacement source generation restores committed visibility.
- **Two USB roots:** `/storage/usbdisk0` and `/storage/usbdisk1` retain separate keys and generations;
  source-list ordering cannot make one invalidate the other.

On Android 10, the application integration layer listens for mounted, unmounted, eject, and removed
file-scheme media broadcasts only while DirectFS is configured. Mount bursts coalesce, use bounded
500 ms / 1.5 s / 3 s readability attempts, invalidate only matched source keys, and submit a
`STORAGE_MOUNTED` request after startup-critical work. Removal cancels work targeting the matched
source and records temporary unavailability without deleting the committed library. There is no
polling.

## Publication and terminal outcomes

Every scan request carries a reason, cache policy, optional metadata profile, configuration
generation, and optional source-key scope. Initial setup and user retry outrank observer/mount work;
Full enrichment remains lowest and cannot strengthen a cache-bypassing initial request. A newer
configuration supersedes older source work.

`IncrementalResultFailurePolicy` folds planner-detected `unavailableSourceKeys` into request-scoped
failure evidence without overwriting a more specific source failure. Repository classification then
uses `SourceScanOutcome`:

- `Success` and authoritative all-source `AuthoritativeEmpty` may acknowledge complete authority;
- `Partial` and `Truncated` preserve failed/unobserved source generations and unresolved keys;
- a source with retained readable rows but unavailable preflight is `Partial`, not `Success`;
- all configured sources unavailable with no readable rows is `TemporarilyUnavailable`;
- permission, cancellation and temporary-unavailability outcomes preserve the readable library and
  retryable checkpoint;
- DirectFS depth and directory-count bounds emit `Truncated`, never authoritative deletion.

A non-authoritative result whose configuration generation is older than the current generation is
discarded before publication and reported as `SUPERSEDED`.

**Requires device validation:** real mount-name swaps, FAT timestamp behaviour and ACC sleep/wake
transitions on `s9863a1h10_Natv`.

## Lean and Full metadata

`MetadataWorkPolicy` changes actual extractor work.

### Lean

Lean uses the Android platform retriever and reads only:

- playback URI/path and stable source identity;
- title;
- first artist;
- album fallback;
- duration and MIME/bitrate where cheaply available;
- track/disc values;
- deterministic sort fields.

Lean does not invoke TagLib rich extraction, embedded-art extraction, multi-artist expansion,
genres, ReplayGain, MusicBrainz, release types or advanced date processing.

### Full enrichment

Full uses the existing TagLib path and enables configured rich dimensions. It runs as an incremental
profile upgrade and is deferred while audio is active. An enrichment-only plan is permitted only
when every selected source reason is `METADATA_PROFILE_UPGRADE`.

Enrichment-only work:

- allocates no pending source generation;
- leaves committed generation, fingerprint, configuration revision, invalidation and `incomplete`
  state unchanged;
- stages updates only for URIs already belonging to the committed source generation;
- cannot add or remove source membership;
- cannot apply user removals or destructive cover cleanup;
- reloads the complete committed graph before publication;
- advances the committed Full profile only when seen membership equals committed membership;
- reports incomplete enrichment as a partial optional terminal result without invalidating the base
  source checkpoint/library.

A source already committed as Full is not downgraded by a later no-change Lean pass.

## Artwork and optional work

Artwork creation is disabled during Lean and Full indexing. Existing cover IDs remain durable
references and visible/current, widget and explicit detail surfaces resolve artwork on demand.
Ordinary incremental scans neither eagerly extract complete-library artwork nor globally clear the
cover store.

`CoverCleanupPolicy` permits destructive cleanup only after a newly published, complete Full,
non-enrichment authoritative view with no unresolved or unobserved source and a terminal `Success`
or `AuthoritativeEmpty`. Lean, enrichment-only, partial, truncated, permission and unavailable runs
cannot define the retained cover set.

ReplayGain metadata extraction is a Full-profile dimension. The existing playback processor remains
the single standards-compatible audio processor and stays dormant when ReplayGain mode is disabled.
Android's platform MediaCodec renderer remains first for normal formats. FFmpeg remains a fallback
playback compatibility renderer rather than an indexing/enrichment component; this PR does not add
a second renderer pipeline or player.

Generated playlists remain opt-in/default-off, derived only after the base library is available,
cancellable and coalesced. They cannot mutate source authority or trigger a source rescan.

## Subscriber-driven categories

`CategorySubscriptionGate` keeps only the visible Home category active. Source and library
invalidations for inactive categories conflate into one pending refresh. Opening the category
consumes that refresh; repeated changes do not eagerly sort every category.

The bounded Room projections introduced by PR 1 remain the authority for startup Home, Quick Find
and pre-hydration MediaBrowser results. Rich relationship screens continue through the compatibility
graph only after Full Library readiness; that bridge remains outside the immediate interaction lane
and is guarded from Fast Start.

## Compatibility bridge

The following remains intentionally compatibility-only:

- `DBCache.snapshot()` for complete rich graph reconstruction;
- `Musikr.loadCached()` for relationship-heavy screens and recovery after source-local failure,
  unavailability, enrichment or committed removal;
- existing rich Album/Artist/Genre/Playlist detail models.

The compatibility snapshot reads only committed, currently visible source generations plus legacy
rows not yet claimed by the generation ledger. It does not expose pending or user-removed rows and
does not gate playback, Fast Start, Quick Find, direct folders or bounded MediaBrowser results.

## Resource policy

The Topway variants default to the Driving Startup policy.

TS18 policy claim: [Evidence confidence: Requires TS18 validation]
[Porting decision: Requires TS18 runtime validation].

- Lean metadata first;
- one or low indexing concurrency while playback is active;
- Fast Start, Quick Find and folder access remain prioritised;
- Full enrichment waits for idle;
- artwork remains demand-driven;
- no kernel, governor, LMK, zRAM or vendor-service changes.

Users retain the existing scan-priority controls. An explicit Full request is never silently
downgraded.

## Verification matrix

Maintained repository verification runs:

- `bash ./scripts/ci-gradle.sh --continue spotlessKotlinCheck`;
- `bash ./scripts/ci-gradle.sh :app:testTopwayTwMediaDebugUnitTest :musikr:testDebugUnitTest`;
- `bash ./scripts/ci-gradle.sh :app:lintTopwayTwMediaDebug`;
- maintained `topwayTwMedia` and `topwayTwMusic` debug/release builds;
- Android 10/API 29 compatibility checks;
- TS18 APK-reference and DoFun/Topway compatibility contracts;
- workflow/script syntax and head-unit safety checks.

Host tests cover:

- unchanged, changed, new and deleted rows;
- forced first scans using pending/committed generations and rollback on failure;
- cancellation and stale-pending restart handling;
- temporary unmount without deleting or hiding an active committed source;
- transactional removal, removal-only configuration and repeated-removal no-work;
- failed re-add remaining hidden and successful re-add restoring visibility;
- independent `/storage/usbdisk0` and `/storage/usbdisk1` identity despite source-list reordering;
- source-local failure with successful sibling commit;
- unavailable-source outcomes with retained rows, healthy siblings and all-unavailable empty results;
- enrichment success/failure/cancellation without generation or membership authority;
- incomplete enrichment producing a partial optional result;
- observer-event conflation and repeated scan-request coalescing;
- Lean/Full work gates and cleanup safety;
- subscriber invalidation conflation;
- database migration and committed-generation projections;
- a 5,000-row committed fixture with a bounded first-page query.

**Requires device validation:** cold boot, process death, launcher restart, Bluetooth/media-button
launch, hardware-key Next/Previous, source removal while playing, real two-volume mount-order
changes, ACC sleep/wake and active indexing during playback on the exact TS18 build.
