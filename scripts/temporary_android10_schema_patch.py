#!/usr/bin/env python3
"""Apply exact Android 10 code-health patches before Room schema generation."""

from pathlib import Path


def replace_exact(path: str, old: str, new: str) -> None:
    target = Path(path)
    text = target.read_text(encoding="utf-8")
    count = text.count(old)
    if count != 1:
        raise RuntimeError(f"Expected one match in {path}, found {count}")
    target.write_text(text.replace(old, new), encoding="utf-8")


def main() -> None:
    cache_database = "musikr/src/main/java/org/oxycblt/musikr/cache/db/CacheDatabase.kt"
    replace_exact(
        cache_database,
        "    exportSchema = false,",
        "    exportSchema = true,",
    )
    replace_exact(
        cache_database,
        '''    @Transaction
    suspend fun deleteExcludingUris(uris: Set<String>) {
        val delete = selectAllUris().toSet() - uris
        for (chunk in delete.chunked(999)) {
            deleteExcludingUriChunk(chunk)
        }
    }

    @Query("SELECT uri FROM CachedFileData") suspend fun selectAllUris(): List<String>
''',
        '''    @Transaction
    suspend fun deleteExcludingUris(uris: Set<String>) {
        var afterUri: String? = null
        while (true) {
            val page = selectUrisAfter(afterUri, LEGACY_RECONCILIATION_PAGE_SIZE)
            if (page.isEmpty()) return
            page.filterNot(uris::contains).chunked(SQLITE_BIND_LIMIT).forEach {
                deleteExcludingUriChunk(it)
            }
            afterUri = page.last()
        }
    }

    @Query(
        "SELECT uri FROM CachedFileData " +
            "WHERE (:afterUri IS NULL OR uri > :afterUri) " +
            "ORDER BY uri LIMIT :limit"
    )
    suspend fun selectUrisAfter(afterUri: String?, limit: Int): List<String>
''',
    )
    replace_exact(
        cache_database,
        '''    @Query("DELETE FROM CachedFileData WHERE uri IN (:uris)")
    suspend fun deleteExcludingUriChunk(uris: List<String>)
}
''',
        '''    @Query("DELETE FROM CachedFileData WHERE uri IN (:uris)")
    suspend fun deleteExcludingUriChunk(uris: List<String>)

    companion object {
        const val LEGACY_RECONCILIATION_PAGE_SIZE = 512
        const val SQLITE_BIND_LIMIT = 999
    }
}
''',
    )
    replace_exact(
        "app/src/main/java/org/oxycblt/auxio/Auxio.kt",
        """            try {
                settings.migrate()
            } catch (e: Exception) {
""",
        """            try {
                PerfTimer.trace("Settings.migrate:${settings.javaClass.simpleName}") {
                    settings.migrate()
                }
            } catch (e: Exception) {
""",
    )
    replace_exact(
        "app/src/main/java/org/oxycblt/auxio/playback/ui/visualizer/VisualizerCoordinator.kt",
        "    private val runtimeMetrics = VisualizerRuntimeMetrics(diagnosticJournal)",
        """    private val runtimeMetrics =
        VisualizerRuntimeMetrics(
            diagnosticJournal ?: VisualizerDiagnosticsResolver.resolve(context)
        )""",
    )


if __name__ == "__main__":
    main()
