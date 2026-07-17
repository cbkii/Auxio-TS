# Fast interaction startup — PR 1

Auxio-TS now treats launch readiness as staged capabilities plus orthogonal recoverable library/source status. The first PR keeps one playback service/session/notification, but makes early MediaBrowser and quick database search consume bounded Room projections before the complete Musikr graph is available.

Startup stages:

1. `ProcessVisible`
2. `PlaybackServiceReady`
3. `QueueReady`
4. `FastBrowseReady`
5. `SearchReady`
6. `FullLibraryReady`
7. `EnrichmentComplete`

Critical path rule: Fast Start, bounded MediaBrowser song children and pre-hydration search must use `StartupProjectionCache` methods, not `DBCache.snapshot()` or `Musikr.loadCached()`. `DBCache.snapshot()` remains compatibility-only for legacy rich-library screens until PR 2 migrates category subscribers.

Readiness ownership:

- process/UI shell owns `ProcessVisible`;
- `PlaybackServiceFragment` owns `PlaybackServiceReady` after the canonical service/session/receiver stack is attached;
- `ExoPlaybackStateHolder` owns `QueueReady` after primitive queue restore resolves to a playable window, raw fallback, or explicit empty/failure state;
- `MusicRepository` owns `FastBrowseReady` and `SearchReady` only after normalized projection backfill/readiness work has run;
- `MusicRepository` owns `FullLibraryReady` only after publishing the legacy rich Musikr graph.

Recoverable statuses such as no source, empty library, cache unavailable, and source unavailable live in `StartupLibraryStatus` and do not regress capability milestones. This is required for USB removal/reinsertion and empty-to-populated recovery.

Direct USB browsing is bounded to the visible level under `/storage/usbdisk0` and `/storage/usbdisk1`, returns app-facing `/storage/...` paths, and is separate from source indexing or MediaStore visibility.

TS18 exact-device claim: [Evidence confidence: Requires TS18 validation] [Porting decision: Requires TS18 runtime validation].


## User-facing fast path

Before `Musikr.loadCached()` completes, Home can show bounded recent/first-song chips and mounted
`/storage/usbdisk0` or `/storage/usbdisk1` roots. Search debounces and queries normalized Room song
projections, and both search rows and the in-app USB folder dialog can start validated raw playback
through the existing single playback service/session. Rich `Song` objects, relationships and artwork
enrich later without replacing current audio.

## PR 2 compatibility bridge

Complete Home category materialisation, subscriber-driven category paging, source-scoped committed
generations, changed-file-only extraction and Lean/Full enrichment remain intentionally deferred to
the stacked incremental-library PR. `DBCache.snapshot()` and `Musikr.loadCached()` remain explicit,
background compatibility paths for screens not yet migrated; neither gates queue restore, bounded
Home rows, Quick Find, USB browsing or MediaBrowser startup projections.
