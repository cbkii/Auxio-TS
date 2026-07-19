# Database-first library architecture migration map

The database-first programme has two deliberately separate lanes:

1. **Immediate interaction** reads bounded committed rows for Fast Start, Quick Find, direct-folder playback and pre-hydration MediaBrowser responses.
2. **Rich compatibility** reconstructs the complete Musikr graph asynchronously after startup capabilities are available, for relationship-heavy screens and playlist mutation that have not yet been converted to row/detail models.

The normalised Room schema is upgraded non-destructively through `MIGRATION_70_71` and `MIGRATION_71_72`. The legacy `CachedFileData` table remains an import and compatibility source; no migration deletes it or falls back to destructive recreation.

## Current implementation

### Observed

- `CacheReadDao.selectSongByUri(uri)` performs point cache lookup without `selectAllSongs()` or a process-wide URI map.
- `LibraryBackfill` migrates legacy rows in bounded, transactional, restart-safe batches.
- Fast Start song, album, artist, folder and search projections are bounded and deterministically ordered.
- Quick Find escapes `LIKE` metacharacters, caps page sizes, cancels obsolete searches and suppresses stale results.
- `SourceLedgerData`, pending generations and committed generations isolate every MediaStore volume, SAF tree and DirectFS source.
- Pending rows are invisible to normal readers and are published only by a successful Room transaction.
- Cancellation, process restart, provider failure and removable-source loss retain the last committed generation.
- Lean and Full metadata profiles change real extractor work. Lean skips TagLib-rich metadata, artwork, ReplayGain, MusicBrainz, genres, relationship expansion and rich dates.
- Category invalidation is subscriber-driven: inactive Home categories accumulate one dirty marker rather than eagerly rebuilding every category.
- The complete graph cannot be reached from guarded immediate-lane source files.
- Persisted `USABLE` or `EMPTY` startup state is published without synchronous complete-graph construction; compatibility hydration runs on the IO supervisor and is discarded when a newer revision or committed generation wins.
- Persisted performance-capture policy is restored when `MusicSettingsImpl` is reconstructed, while release capture remains explicit and bounded.

### Exact-device boundary

- **Evidence confidence:** Requires TS18 validation.
- **Porting decision:** Directly reusable architecture and validation constraints; exact timing, launcher, removable-storage and ACC behaviour requires TS18 runtime validation.

Cold boot, process death, launcher restart, Bluetooth/media-button launch, real FAT timestamp behaviour, USB removal/reinsertion, two-volume mount-order changes and ACC sleep/wake remain exact-device tests on `s9863a1h10_Natv`.

## Routine-consumer migration audit

| Consumer | Startup authority | Complete graph allowed? | Current classification |
| --- | --- | --- | --- |
| Fast Start Home surface | bounded Room projections plus primitive saved-session state | No | Routine immediate-lane consumer; migrated |
| Quick Find | bounded Room search projections | No | Routine immediate-lane consumer; migrated |
| MediaBrowser root and early pages | bounded asynchronous Room/direct-folder results | No | Routine immediate-lane consumer; migrated |
| Direct USB folder navigation/playback | one-level filesystem projection and primitive playback refs | No | Routine immediate-lane consumer; migrated |
| Saved queue, resume and first-audio restore | bounded primitive queue window / raw resume snapshot | No | Routine immediate-lane consumer; migrated |
| Recently added startup rows | bounded committed Room projection | No | Routine immediate-lane consumer; migrated |
| Rich Album/Artist/Genre/Playlist details | compatibility Musikr graph after `FullLibraryReady` | Yes | Relationship-heavy compatibility consumer |
| Favourites and playlist mutation | compatibility `MutableLibrary` after readiness | Yes | Compatibility consumer until a durable row-level playlist API exists |

The app does not invent a second “recently played” authority. The persisted primitive queue/current item is the canonical resumable recent-playback context, while `recentlyAdded()` is a separate bounded library projection. This preserves one queue and one playback authority rather than adding a duplicate history store merely to populate startup UI.

This satisfies the roadmap requirement for routine startup and first-minute consumers: they no longer wait for or construct the complete graph. It does **not** claim that the rich Musikr domain model has been removed. Removing that model requires replacement relationship-detail and playlist-mutation contracts and is outside the immediate interaction lane.

## Compatibility-bridge boundaries

The allowed complete-graph entry points are deliberately narrow:

- `MusicRepositoryImpl.startCompatibilityHydration(worker)` invokes `Musikr.loadCached()` on an IO supervisor only after bounded startup projections are prepared.
- Startup first preserves persisted `USABLE`/`EMPTY` knowledge and does not synchronously invoke complete graph hydration.
- The hydration result is discarded when a newer device-library generation or revision supersedes it.
- An unexpected empty/failed prior-usable cache may request one bounded standard-variant recovery scan when a source is configured; Topway variants retain the playback-first no-implicit-startup-scan policy.
- A source-local scan failure may rebuild the rich graph from committed, available rows so partially explored data is never published.
- Rich relationship screens receive the graph only after library readiness; Fast Start, Quick Find, direct folders, primitive playback and early MediaBrowser responses do not depend on it.

`DBCache.snapshot()` and `Musikr.loadCached()` are therefore compatibility bridges, not startup authorities. Architecture tests and static gates fail if they are referenced from guarded immediate-lane consumers.

## Producer and consumer migration map

| Legacy producer | Legacy consumer | Database-first replacement | State |
| --- | --- | --- | --- |
| `DBCache.read()` with a full URI map | scan cache lookup | `CacheReadDao.selectSongByUri(uri)` | Complete |
| `DBCache.snapshot()` | startup and routine browsing | bounded committed projections | Removed from routine startup; compatibility-only |
| `Musikr.loadCached()` | startup publication | persisted startup state plus Fast Start, Quick Find and MediaBrowser projections | Removed from immediate lane; asynchronous compatibility-only |
| `MusicGraph.build()` | eager category relationships | normalised song/album/artist/genre tables and cross-references | Persisted; rich graph still used only after readiness |
| `LibraryFactory.create()` / `MutableLibrary` | all UI and playback decisions | bounded row/detail DTOs plus primitive playback refs | Routine startup migrated; rich detail/mutation bridge retained |
| full-library in-memory search | Search UI | cancellable bounded Quick Find | Complete for pre-hydration and first-minute use |

## Generation model

`SourceLedgerData` records source identity, availability, committed fingerprint, invalidation versions, configuration revision, committed metadata profile and pending/committed generation state. Candidate fingerprints do not become committed during observation or planning.

For a selected source, the pipeline allocates a pending generation, stages validated changed rows, records seen cache hits, reconciles missing rows in SQL and advances the ledger only after a successful transaction. Failed sources discard pending rows and preserve their previous committed generation while healthy sibling sources may still commit.

Normal projections read only committed, currently available rows. Legacy rows remain readable until claimed by a durable source identity.

## Optional playback components

ReplayGain metadata extraction belongs to the Full metadata profile, but the single playback processor remains attached so changing the playback setting at runtime does not require a second player or unsafe player rebuild. It is a unity-gain pass when ReplayGain is disabled or no adjustment exists.

The platform `MediaCodecAudioRenderer` is the preferred renderer for normal Android-supported formats. FFmpeg remains a fallback renderer for compatibility formats; it is not an indexing component and does not create a second playback pipeline.

Artwork remains an ID/reference in durable startup rows and is resolved only for visible/current/widget/detail demand. Ordinary incremental scans do not perform a global artwork-cache clear.

## Completed three-part programme

1. **Fast interaction startup — PR #182 / issue #179:** staged readiness, bounded Fast Start and Quick Find, direct-folder playback, asynchronous MediaBrowser and primitive service-first playback.
2. **Incremental library pipeline — PR #183 / issue #180:** source generations, changed-file extraction, failure isolation, Lean/Full enrichment, subscriber-driven categories and compatibility-bridge containment.
3. **Startup profiles and benchmarks — PR #184 / issue #181:** Baseline/Startup Profiles, deterministic multi-scale fixtures, macrobenchmarks, structural gates, release profile verification and an exact-device TS18 validation runbook.

## PR #184 final architecture audit

- **Evidence confidence:** Observed in repository source and maintained CI when the referenced checks pass; managed-emulator measurements are observed only on the named emulator.
- **Porting decision:** Directly reusable for this Auxio-TS codebase; exact TS18 runtime claims remain gated by the device runbook.

The final audit found and corrected three production gaps rather than attempting to hide them with profiles:

1. complete cached-graph hydration was still started asynchronously without first preserving the persisted startup state through the real policy path;
2. persisted release performance-capture preference was not restored after process recreation;
3. benchmark database/queue state did not also seed deterministic startup settings, allowing emulator preference residue to alter the measured decision path.

The audit also removed the direct `persist.tw.storage.switch` process probe from `MusicRepository`. Vendor-specific diagnostics remain outside core repository orchestration, consistent with the adapter/facade boundary and external-diagnostics policy.
