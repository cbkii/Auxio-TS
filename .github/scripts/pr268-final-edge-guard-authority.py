from pathlib import Path


def replace_once(path: Path, old: str, new: str, label: str) -> None:
    text = path.read_text()
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{label}: expected one match, found {count}")
    path.write_text(text.replace(old, new, 1))


repository = Path("app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt")

# Authority-only publication is idempotent: a preceding library publication may already have
# paired this exact source outcome with the current generation.
replace_once(
    repository,
    """    private fun dispatchDeviceSourceAuthorityChange() {
        val deviceAuthority =
            synchronized(this) {
                DeviceLibraryAuthority(
                        generation = deviceLibraryGeneration.incrementAndGet(),
                        sourceScanOutcome = lastSourceScanOutcome,
                    )
                    .also { publishedDeviceLibraryAuthority = it }
            }
        val changes =
""",
    """    private fun dispatchDeviceSourceAuthorityChange() {
        val deviceAuthority =
            synchronized(this) {
                val currentGeneration = deviceLibraryGeneration.get()
                publishedDeviceLibraryAuthority
                    ?.takeIf {
                        it.generation == currentGeneration &&
                            it.sourceScanOutcome == lastSourceScanOutcome
                    }
                    ?.let { return }
                DeviceLibraryAuthority(
                        generation = deviceLibraryGeneration.incrementAndGet(),
                        sourceScanOutcome = lastSourceScanOutcome,
                    )
                    .also { publishedDeviceLibraryAuthority = it }
            }
        val changes =
""",
    "make authority-only publication idempotent",
)

# USER_REFRESH/SOURCE_OBSERVER recovery scans deliberately hold no checkpoint lease. emitLibrary()
# publishes when content changes, but returns silently when the same library is re-proven. Publish
# the newly recorded source authority in both cases; the idempotence guard avoids a duplicate event
# when emitLibrary() already published it.
replace_once(
    repository,
    """                } else {
                    musicSettings.revision = newRevision
                    emitLibrary(publishedLibrary)
                    musicSettings.libraryState = publishedState
                    if (request.reason != IndexReason.METADATA_ENRICHMENT) {
                        musicSettings.lastScanFailed = partial
                    }
                }
""",
    """                } else {
                    musicSettings.revision = newRevision
                    emitLibrary(publishedLibrary)
                    musicSettings.libraryState = publishedState
                    if (request.reason != IndexReason.METADATA_ENRICHMENT) {
                        musicSettings.lastScanFailed = partial
                    }
                    if (IndexRequestPolicy.recordsSourceOutcome(request)) {
                        withContext(Dispatchers.Main) { dispatchDeviceSourceAuthorityChange() }
                    }
                }
""",
    "publish non-lease source authority even when library content is unchanged",
)
