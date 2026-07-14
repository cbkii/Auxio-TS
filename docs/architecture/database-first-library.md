# Database-first library architecture migration map

This PR establishes the Room schema and bounded query surface that replaces routine cached-library
hydration. The legacy `CachedFileData` table remains only as the import/source cache until all UI
callers move to the normalized projections.

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
pending generations do not replace the last committed generation.

## TS18 claim labels

- **Evidence confidence:** Inferred from existing Auxio-TS architecture and repo schemas.
- **Porting decision:** Requires TS18 runtime validation for exact-device performance and launcher
  behaviour; schema/query changes are directly reusable Android-standard implementation work.
