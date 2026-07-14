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
- **Scaffold only:** `LibraryReadDao` paged projections and search queries exist but are not yet
  consumed by ViewModels/adapters; generation columns exist but incremental generation-safe
  scanning is not yet implemented.
- **Not yet implemented:** Paging 3 end-to-end UI conversion, MediaBrowser bounded async browsing,
  database-backed search UI, Lean/Full metadata profile work differentiation.

| Legacy producer | Legacy consumer | Database-first replacement | Projection | Paging | Removal point |
| --- | --- | --- | --- | --- | --- |
| `DBCache.read()` building a URI map with `selectAllSongs()` | Musikr scan cache lookup | `CacheReadDao.selectSongByUri(uri)` | `CachedFileData` single row | No, point lookup | Complete now |
| `DBCache.snapshot()` | One-time legacy import only | `selectSongsPage(limit, offset)` batches, then normalized `LibrarySongData` | Lean song columns first | Yes, bounded batches | Remove after import migrates all users |
| `Musikr.loadCached()` | Startup library publication | `LibraryReadDao.songsPage/albumsPage/artistsPage` | `SongListRow`, `AlbumListRow`, `ArtistListRow` | Yes | Startup repository conversion |
| `MusicGraph.build()` | Category relationships | `SongArtistCrossRefData`, `SongGenreCrossRefData`, `AlbumArtistCrossRefData` | ID-based refs | Yes | Relationship enrichment pass |
| `LibraryFactory.create()` / `MutableLibrary` | UI, search, MediaBrowser | DB projections and bounded details | row/detail DTOs | Yes | Per-consumer migration |
| Full-library search | Search UI | indexed `LibraryReadDao.searchSongs()` | `SongListRow` | bounded limit | Search migration |

## Generation model

`LibraryVolumeData` stores availability plus committed and pending generations. `LibrarySongData`,
`LibraryAlbumData`, `LibraryArtistData`, `LibraryGenreData` and playlist rows carry generation and
metadata revision columns. Normal browsing must query available committed rows; failed or cancelled
pending generations do not replace the last committed generation. The legacy backfill writes rows
with `scanGeneration = 0` and `metadataRevision = 0`; the first real incremental scan will re-home
provisional `legacy:<uri>` identities onto durable source identity.

## TS18 claim labels

- **Evidence confidence:** Inferred from existing Auxio-TS architecture and repo schemas.
- **Porting decision:** Requires TS18 runtime validation for exact-device performance and launcher
  behaviour; schema/query changes are directly reusable Android-standard implementation work.
