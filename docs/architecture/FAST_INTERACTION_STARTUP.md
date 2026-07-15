# Fast interaction startup — PR 1

Auxio-TS now treats launch readiness as staged instead of binary. The first PR keeps one playback service/session/notification, but makes early UI, MediaBrowser and quick database search consume bounded Room projections before the complete Musikr graph is available.

Startup stages:

1. `ProcessVisible`
2. `PlaybackServiceReady`
3. `QueueReady`
4. `FastBrowseReady`
5. `SearchReady`
6. `FullLibraryReady`
7. `EnrichmentComplete`

Critical path rule: Fast Start, bounded MediaBrowser song children and pre-hydration search must use `StartupProjectionCache` methods, not `DBCache.snapshot()` or `Musikr.loadCached()`. `DBCache.snapshot()` remains compatibility-only for legacy rich-library screens until PR 2 migrates category subscribers.

Direct USB browsing is bounded to the visible level under `/storage/usbdisk0` and `/storage/usbdisk1`, returns app-facing `/storage/...` paths, and is separate from source indexing or MediaStore visibility.

TS18 exact-device claim: [Evidence confidence: Requires TS18 validation] [Porting decision: Requires TS18 runtime validation].
