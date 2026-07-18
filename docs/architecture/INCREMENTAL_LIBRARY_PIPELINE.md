# Incremental library and enrichment pipeline

This document describes the second stage of the Auxio-TS startup-performance programme. It builds on the Fast Interaction path and keeps playback, bounded browsing and Quick Find independent from complete rich-library work.

## Status labels

- **Observed** — implemented and exercised by host tests or repository CI.
- **Inferred** — follows from the implemented contracts but still needs runtime evidence.
- **Requires device validation** — must be tested on the exact TS18 unit before a latency or vendor-behaviour claim is accepted.

## Goals

The pipeline must:

- skip provider traversal when a configured source is unchanged;
- extract only new or changed files;
- keep pending scan data invisible until a successful source commit;
- retain each source's last-known-good generation after cancellation, process death, removal or provider failure;
- allow healthy sources to commit when another source fails;
- use a real Lean pass for immediate playback and browsing metadata;
- defer Full relationship, ReplayGain, MusicBrainz, rich-date and artwork work until playback is idle;
- invalidate inactive categories cheaply and load them only when requested;
- preserve the one-player, one-session and one-playback-service architecture from PR 1.

## Durable source ledger

Every configured MediaStore volume, SAF tree or DirectFS root has an independent `SourceLedgerData` row. The row records:

- stable source key and source type;
- root URI/path;
- availability;
- advisory or authoritative fingerprint;
- invalidation and committed-invalidation versions;
- last committed and pending generations;
- configuration revision;
- committed metadata profile and enrichment revision;
- last successful scan and incomplete-scan state.

**Observed:** source observers attach before rich-library hydration. Attachment does not enumerate files or construct a library; notification bursts are debounced and persist only source invalidation before scan planning.

MediaStore uses the platform volume-version token on Android 10. SAF uses root-document metadata. DirectFS uses a bounded root sample and periodic refresh. Direct/SAF fingerprints are advisory: they avoid needless warm work but are not treated as proof that an entire FAT/document tree cannot have changed.

## Generation protocol

For each source selected for work:

1. Allocate a pending generation.
2. Clear stale pending rows left by a killed process.
3. Discover files with bounded channels and worker count.
4. Record every seen file, including unchanged cache hits.
5. Extract only cache misses, modified files or rows requiring a metadata-profile upgrade.
6. Stage changed cache rows separately from committed readers.
7. Publish all seen rows and changed metadata in one Room transaction.
8. Mark files missing from the successful generation unavailable and reconcile them in SQL.
9. Advance the source ledger only after the transaction succeeds.
10. Delete the older indexed generation after the new generation is committed.

Cancellation or failure clears pending rows and leaves the previous committed generation readable.

### Multi-source isolation

Source adapters collect failures by stable source key. A failed source has its pending rows discarded and keeps its prior committed generation. Successful sibling sources still commit in the same transaction.

The rich compatibility graph is rebuilt from committed, available rows when a source-local failure occurred, so partially explored data is not published. Fast Start and Quick Find continue to use committed Room projections throughout.

## Removable-source semantics

Mount state, app access, committed library availability and playback viability are separate.

- **Absent at startup:** mark the source unavailable; do not scan it and do not delete its committed rows.
- **Removed while browsing:** bounded readers return no available rows after the ledger update.
- **Removed while indexing:** discard that source's pending generation; healthy sources may still commit.
- **Removed while playing:** the current player error path remains authoritative; indexing never replaces or restarts the queue.
- **Reinserted unchanged:** restore access to the last committed generation without metadata extraction; advisory refresh still applies on its normal bounded interval.
- **Reinserted changed:** extract only new or changed rows before committing the replacement generation.
- **Two USB roots:** `/storage/usbdisk0` and `/storage/usbdisk1` have separate keys and generations; source-list ordering cannot make one invalidate the other.

**Requires device validation:** real mount-name swaps, FAT timestamp behaviour and ACC sleep/wake transitions on `s9863a1h10_Natv`.

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

Lean does not invoke TagLib rich extraction, embedded-art extraction, multi-artist expansion, genres, ReplayGain, MusicBrainz, release types or advanced date processing.

### Full

Full uses the existing TagLib path and enables configured rich dimensions. It runs as an incremental profile upgrade, updates only affected rows and is deferred while audio is active. A source already committed as Full is not downgraded by a later no-change Lean pass.

## Artwork and optional work

Artwork creation is disabled during Lean and Full indexing. Existing cover IDs remain durable references and visible/current, widget and explicit detail surfaces resolve artwork on demand. Ordinary incremental scans neither eagerly extract complete-library artwork nor globally clear the cover store.

ReplayGain metadata extraction is a Full-profile dimension. The existing playback processor remains the single standards-compatible audio processor and stays dormant when ReplayGain mode is disabled. Android's platform MediaCodec renderer is ordered first for normal formats. FFmpeg remains a fallback playback compatibility renderer rather than an indexing/enrichment component; this PR does not add a second renderer pipeline or player.

## Subscriber-driven categories

`CategorySubscriptionGate` keeps only the visible Home category active. Source and library invalidations for inactive categories conflate into one pending refresh. Opening the category consumes that refresh; repeated changes do not eagerly sort every category.

The bounded Room projections introduced by PR 1 remain the authority for startup Home, Quick Find and pre-hydration MediaBrowser results. Rich relationship screens continue through the compatibility graph only after Full Library readiness; that bridge is kept outside the immediate interaction lane and is explicitly guarded from Fast Start.

## Compatibility bridge

The following remains intentionally compatibility-only:

- `DBCache.snapshot()` for complete rich graph reconstruction;
- `Musikr.loadCached()` for relationship-heavy screens and recovery after a source-local scan failure;
- existing rich Album/Artist/Genre/Playlist detail models.

The compatibility snapshot reads only committed, currently available source generations plus legacy rows not yet claimed by the generation ledger. It does not expose pending or unavailable rows and does not gate playback, Fast Start, Quick Find, direct folders or bounded MediaBrowser results.

## Resource policy

The Topway variants default to the Driving Startup policy.

TS18 policy claim: [Evidence confidence: Requires TS18 validation] [Porting decision: Requires TS18 runtime validation].

- Lean metadata first;
- one or low indexing concurrency while playback is active;
- Fast Start, Quick Find and folder access remain prioritised;
- Full enrichment waits for idle;
- artwork remains demand-driven;
- no kernel, governor, LMK, zRAM or vendor-service changes.

Users retain the existing scan-priority controls. An explicit Full request is never silently downgraded.

## Verification matrix

Maintained repository verification runs:

- `bash ./scripts/ci-gradle.sh spotlessCheck`;
- `bash ./scripts/ci-gradle.sh :app:testStandardDebugUnitTest :musikr:testDebugUnitTest`;
- Android lint;
- maintained debug and release APK builds;
- TS18 APK-reference contracts;
- DoFun/Topway compatibility checks;
- workflow/script syntax and head-unit safety checks.

Host tests cover:

- unchanged, changed, new and deleted rows;
- cancellation and stale-pending restart handling;
- temporary unmount without deletion;
- unchanged and changed reinsertion;
- independent `/storage/usbdisk0` and `/storage/usbdisk1` identity despite source-list reordering;
- source-local failure with successful sibling commit;
- observer-event conflation and repeated scan-request coalescing;
- Lean/Full work gates and lazy artwork policy;
- subscriber invalidation conflation;
- database migration and committed-generation projections;
- a 5,000-row committed fixture with a bounded first-page query.

**Requires device validation:** cold boot, process death, launcher restart, Bluetooth/media-button launch, hardware-key Next/Previous, source removal while playing, real two-volume mount-order changes, ACC sleep/wake and active indexing during playback on the exact TS18 build.
