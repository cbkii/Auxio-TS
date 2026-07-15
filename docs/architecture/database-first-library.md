# Database-first library architecture migration map

The consolidated implementation establishes the Room schema, the non-destructive `MIGRATION_70_71`
upgrade, a bounded restart-safe backfill from the legacy cache into the normalized tables, and a
bounded query surface for the remaining consumer migration. The legacy `CachedFileData` table
remains the active import/source cache until startup, UI, search and MediaBrowser callers move to the
normalized projections; it is never deleted by migration or backfill.

## Implementation status

- **Implemented:** normalized Room schema (`MIGRATION_70_71`, no destructive fallback),
  database-scoped `@TypeConverters`, reverse-direction relationship indexes,
  `LibraryBackfill` (bounded batches, transactional writes, idempotent restart-safe resume via
  unique URI identity), runtime wiring via `MutableCache.populateNormalizedLibrary()` invoked at
  the end of `MusicRepositoryImpl.startup()` on the indexing scope, executable Room migration and
  backfill tests (`CacheMigrationAndBackfillTest`).
- **Scaffold only:** `LibraryReadDao` paged projections are not yet consumed by ViewModels/adapters;
  generation columns exist but incremental generation-safe scanning is not yet implemented.
- **Implemented as a prerequisite for the fast-interaction startup PR:** database-backed song search
  via `LibraryReadDao.searchSongs` escapes user `LIKE` metacharacters (`%`, `_`, `\`) with an
  `ESCAPE '\'` clause, is strictly bounded and paged (`limit`/`offset`), and is deterministically
  ordered (`titleSort, id`). `LikeQuery` provides the escaping helper and `LibrarySearcher` adds
  cancellation, obsolete-query suppression, page-size caps, and overflow-safe offsets. Paged
  song/album/artist projections also use an `id` tie-breaker so offset paging cannot skip or repeat
  rows across equal sort keys. Covered by `LibrarySearchTest`.
- **Not yet wired to normal runtime consumers:** the app Search UI still uses the complete in-memory
  `SearchEngine` path. Leading-wildcard `LIKE` is database-backed and bounded but is not represented
  as index-backed; the fast-interaction PR should introduce the final indexed Quick Find design or
  a measured bounded fallback before switching the runtime UI.
- **Not yet implemented:** Paging 3 end-to-end UI conversion, bounded asynchronous MediaBrowser,
  startup decoupling from complete `Musikr.loadCached()` reconstruction, source-scoped committed
  scan generations, changed-file-only extraction, real Lean/Full metadata work differentiation,
  and the domain-model bridge that lets routine consumers stop constructing the complete in-memory
  `Library` graph.

## Three-part completion programme

The remaining architecture is deliberately split so the search/schema foundation does not become an
unreviewable catch-all change. The authoritative tracking issues are #179, #180 and #181:

1. **Fast interaction startup**
   - staged startup readiness;
   - Fast Start bounded projections;
   - indexed, cancellable Quick Find runtime wiring;
   - direct-folder playback;
   - bounded asynchronous MediaBrowser;
   - useful startup independent from complete library hydration.
2. **Incremental library pipeline**
   - source-version ledgers and observation coalescing;
   - pending/committed generations;
   - unchanged-file fingerprint skips;
   - database-side missing-file reconciliation;
   - subscriber-driven categories;
   - real Lean/Full enrichment and bounded artwork work.
3. **Startup profiles and benchmarks**
   - Baseline and Startup Profiles;
   - startup/interaction macrobenchmarks;
   - conservative performance regression gates;
   - exact-device TS18 validation plan.

| Legacy producer | Legacy consumer | Database-first replacement | Projection | Paging | Removal point |
| --- | --- | --- | --- | --- | --- |
| `DBCache.read()` building a URI map with `selectAllSongs()` | Musikr scan cache lookup | `CacheReadDao.selectSongByUri(uri)` | `CachedFileData` single row | No, point lookup | Complete now |
| `DBCache.snapshot()` | One-time legacy import only | `selectSongsPage(limit, offset)` batches, then normalized `LibrarySongData` | Lean song columns first | Yes, bounded batches | Remove after import migrates all users |
| `Musikr.loadCached()` | Startup library publication | `LibraryReadDao.songsPage/albumsPage/artistsPage` | `SongListRow`, `AlbumListRow`, `ArtistListRow` | Yes | Fast-interaction startup conversion |
| `MusicGraph.build()` | Category relationships | `SongArtistCrossRefData`, `SongGenreCrossRefData`, `AlbumArtistCrossRefData` | ID-based refs | Yes | Incremental relationship enrichment |
| `LibraryFactory.create()` / `MutableLibrary` | UI, search, MediaBrowser | DB projections and bounded details | Row/detail DTOs | Yes | Per-consumer migration |
| Full-library search | Search UI | indexed Quick Find or measured bounded DB fallback | lightweight result rows | Yes | Fast-interaction search migration |

## Generation model

`LibraryVolumeData` stores availability plus committed and pending generations. `LibrarySongData`,
`LibraryAlbumData`, `LibraryArtistData`, `LibraryGenreData` and playlist rows carry generation and
metadata revision columns. Normal browsing must query available committed rows; failed or cancelled
pending generations do not replace the last committed generation. The legacy backfill writes rows
with `scanGeneration = 0` and `metadataRevision = 0`; the first real incremental scan will re-home
provisional `legacy:<uri>` identities onto durable source identity.

## TS18 claim labels

- **Observed:** Room migration, backfill, bounded query and JVM/Room test behaviour proven by CI.
- **Inferred:** the three-part architecture should reduce startup contention based on the current
  code paths and Android-standard database behaviour.
- **Requires device validation:** cold boot, process death, DoFun browsing, USB removal/reinsertion,
  Bluetooth/media-key starts, and real ACC sleep/wake on `s9863a1h10_Natv`.
