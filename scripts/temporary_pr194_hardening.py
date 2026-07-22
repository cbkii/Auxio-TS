#!/usr/bin/env python3
"""Apply exact, temporary pre-merge hardening edits for PR 194."""

from pathlib import Path


REPO_ROOT = Path(__file__).resolve().parents[1]


def replace_exact(path: str, old: str, new: str, expected: int = 1) -> None:
    target = REPO_ROOT / path
    text = target.read_text(encoding="utf-8")
    count = text.count(old)
    if count != expected:
        raise RuntimeError(
            f"{path}: expected {expected} occurrence(s), found {count}: {old[:80]!r}"
        )
    target.write_text(text.replace(old, new), encoding="utf-8", newline="\n")
    print(f"patched {path}: {count} replacement(s)")


def patch_topway_source_policy() -> None:
    path = "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwaySourcePolicy.kt"
    replace_exact(path, "import android.os.SystemClock\n", "")
    replace_exact(
        path,
        "    private const val MAX_SCAN_ELAPSED_MS = 1200L\n",
        "    private const val MAX_SCAN_ELAPSED_MS = 1200L\n"
        "    private const val NANOS_PER_MILLISECOND = 1_000_000L\n\n"
        "    private fun monotonicNowMs(): Long = System.nanoTime() / NANOS_PER_MILLISECOND\n",
    )
    replace_exact(
        path,
        "SystemClock.elapsedRealtime()",
        "monotonicNowMs()",
        expected=4,
    )
    replace_exact(
        path,
        '        if (canonical != null && canonical != clean && canonical != "/") {\n',
        "        if (canonical != null && canonical != clean) {\n",
    )


def patch_root_state_holder() -> None:
    path = "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt"
    replace_exact(
        path,
        "import org.oxycblt.auxio.BuildConfig\n",
        "import org.oxycblt.auxio.BuildConfig\n"
        "import org.oxycblt.auxio.diagnostics.DiagnosticJournal\n",
    )
    replace_exact(
        path,
        "    private val processRunner: RootProcessRunner,\n) : RootGate {\n",
        "    private val processRunner: RootProcessRunner,\n"
        "    private val journal: DiagnosticJournal,\n"
        ") : RootGate {\n",
    )
    replace_exact(
        path,
        "            is RootProcessResult.Success ->\n"
        "                result.stdout\n"
        "                    .lineSequence()\n"
        "                    .filter(String::isNotBlank)\n"
        "                    .take(MAX_ROOT_LIST_LINES)\n"
        "                    .toList()\n"
        "            RootProcessResult.TimedOut -> {\n"
        "                state = State.TimedOut\n"
        "                null\n"
        "            }\n"
        "            is RootProcessResult.NonZeroExit,\n"
        "            RootProcessResult.OutputLimitExceeded,\n"
        "            is RootProcessResult.ExecutionFailure -> null\n",
        "            is RootProcessResult.Success -> {\n"
        "                val lines =\n"
        "                    result.stdout\n"
        "                        .lineSequence()\n"
        "                        .filter(String::isNotBlank)\n"
        "                        .take(MAX_ROOT_LIST_LINES + 1)\n"
        "                        .toList()\n"
        "                if (lines.size > MAX_ROOT_LIST_LINES) {\n"
        "                    journal.log(\n"
        "                        DiagnosticJournal.CAT_STORAGE,\n"
        "                        \"Root listing line limit exceeded\",\n"
        "                        \"maxLines=$MAX_ROOT_LIST_LINES\",\n"
        "                    )\n"
        "                }\n"
        "                lines.take(MAX_ROOT_LIST_LINES)\n"
        "            }\n"
        "            RootProcessResult.TimedOut -> {\n"
        "                state = State.TimedOut\n"
        "                null\n"
        "            }\n"
        "            RootProcessResult.OutputLimitExceeded -> {\n"
        "                journal.log(\n"
        "                    DiagnosticJournal.CAT_STORAGE,\n"
        "                    \"Root listing output limit exceeded\",\n"
        "                    \"maxBytes=$ROOT_LIST_OUTPUT_BYTES\",\n"
        "                )\n"
        "                null\n"
        "            }\n"
        "            is RootProcessResult.NonZeroExit,\n"
        "            is RootProcessResult.ExecutionFailure -> null\n",
    )


def patch_topway_bridge_logging() -> None:
    replace_exact(
        "app/src/main/java/org/oxycblt/auxio/headunit/topway/TopwayMusicBridgeReceiver.kt",
        "            L.w(\"Dropping excessive Topway bridge action: $action\")\n"
        "            journal.log(DiagnosticJournal.CAT_TOPWAY_CMD, \"Rate limited\", action)\n"
        "            return\n",
        "            L.w(\"Dropping excessive Topway bridge action: $action\")\n"
        "            return\n",
    )


def patch_root_process_tests() -> None:
    path = "app/src/test/java/org/oxycblt/auxio/headunit/root/RootProcessRunnerTest.kt"
    replace_exact(
        path,
        "    @Test\n"
        "    fun `rejects output beyond the configured capture limit`() {\n"
        "        val result =\n"
        "            runner.runProcessForTest(\n"
        "                arrayOf(\"sh\", \"-c\", \"yes x | head -c 4096\"),\n"
        "                timeoutMs = 2_000,\n"
        "                maxOutputBytes = 128,\n"
        "            )\n\n"
        "        assertEquals(RootProcessResult.OutputLimitExceeded, result)\n"
        "    }\n"
        "}\n",
        "    @Test\n"
        "    fun `rejects output beyond the configured capture limit`() {\n"
        "        val result =\n"
        "            runner.runProcessForTest(\n"
        "                arrayOf(\"sh\", \"-c\", \"yes x | head -c 4096\"),\n"
        "                timeoutMs = 2_000,\n"
        "                maxOutputBytes = 128,\n"
        "            )\n\n"
        "        assertEquals(RootProcessResult.OutputLimitExceeded, result)\n"
        "    }\n\n"
        "    @Test\n"
        "    fun `rejects invalid process limits`() {\n"
        "        assertTrue(\n"
        "            runner.runProcessForTest(\n"
        "                arrayOf(\"sh\", \"-c\", \"true\"),\n"
        "                timeoutMs = 0,\n"
        "            ) is RootProcessResult.ExecutionFailure\n"
        "        )\n"
        "        assertTrue(\n"
        "            runner.runProcessForTest(\n"
        "                arrayOf(\"sh\", \"-c\", \"true\"),\n"
        "                timeoutMs = 1_000,\n"
        "                maxOutputBytes = 0,\n"
        "            ) is RootProcessResult.ExecutionFailure\n"
        "        )\n"
        "    }\n\n"
        "    @Test\n"
        "    fun `reports execution failure for a missing binary`() {\n"
        "        val result =\n"
        "            runner.runProcessForTest(\n"
        "                arrayOf(\"definitely-not-a-real-binary-pr194\"),\n"
        "                timeoutMs = 1_000,\n"
        "            )\n\n"
        "        assertTrue(result is RootProcessResult.ExecutionFailure)\n"
        "    }\n"
        "}\n",
    )


def patch_migration_tests() -> None:
    path = "musikr/src/androidTest/java/org/oxycblt/musikr/cache/db/CacheMigrationInstrumentedTest.kt"
    replace_exact(
        path,
        "        migrated\n"
        "            .query(\n"
        "                \"SELECT COUNT(*) FROM sqlite_master \" +\n"
        "                    \"WHERE type = 'table' AND name = 'SourceLedgerData'\"\n"
        "            )\n"
        "            .use { cursor ->\n"
        "                assertTrue(cursor.moveToFirst())\n"
        "                assertEquals(1, cursor.getInt(0))\n"
        "            }\n",
        "        assertTablesExist(\n"
        "            migrated,\n"
        "            listOf(\n"
        "                \"SourceLedgerData\",\n"
        "                \"SourceScanGenerationData\",\n"
        "                \"ScanSeenData\",\n"
        "                \"PendingCachedFileData\",\n"
        "                \"IndexedSongData\",\n"
        "                \"IndexedUriStateData\",\n"
        "            ),\n"
        "        )\n",
    )
    replace_exact(
        path,
        "    private companion object {\n",
        "    private fun assertTablesExist(\n"
        "        database: androidx.sqlite.db.SupportSQLiteDatabase,\n"
        "        tableNames: List<String>,\n"
        "    ) {\n"
        "        tableNames.forEach { tableName ->\n"
        "            database\n"
        "                .query(\n"
        "                    \"SELECT COUNT(*) FROM sqlite_master \" +\n"
        "                        \"WHERE type = 'table' AND name = ?\",\n"
        "                    arrayOf(tableName),\n"
        "                )\n"
        "                .use { cursor ->\n"
        "                    assertTrue(cursor.moveToFirst())\n"
        "                    assertEquals(\"Missing table $tableName\", 1, cursor.getInt(0))\n"
        "                }\n"
        "        }\n"
        "    }\n\n"
        "    private companion object {\n",
    )


def patch_cache_reconciliation() -> None:
    path = "musikr/src/main/java/org/oxycblt/musikr/cache/db/CacheDatabase.kt"
    replace_exact(path, "import androidx.room.Transaction\n", "")
    replace_exact(
        path,
        "    @Transaction\n"
        "    suspend fun deleteExcludingUris(uris: Set<String>) {\n",
        "    // Legacy compatibility cleanup is restart-safe. Let each bounded query/delete call\n"
        "    // commit independently so a large library does not hold one write transaction throughout.\n"
        "    suspend fun deleteExcludingUris(uris: Set<String>) {\n",
    )


def patch_visualizer_metrics() -> None:
    path = "app/src/main/java/org/oxycblt/auxio/playback/ui/visualizer/VisualizerRuntimeMetrics.kt"
    replace_exact(
        path,
        "    fun recordFrame(bytes: Int, elapsedCopyNanos: Long, nowMs: Long) {\n"
        "        if (bytes <= 0) return\n",
        "    fun recordFrame(bytes: Int, elapsedCopyNanos: Long, nowMs: Long) {\n"
        "        if (journal?.hasActiveSession != true || bytes <= 0) return\n",
    )
    replace_exact(
        path,
        "    fun recordSuppressedWaveform() {\n"
        "        suppressedWaveforms.incrementAndGet()\n"
        "    }\n\n"
        "    fun recordWatchdogRetry() {\n"
        "        watchdogRetries.incrementAndGet()\n"
        "    }\n",
        "    fun recordSuppressedWaveform() {\n"
        "        if (journal?.hasActiveSession == true) suppressedWaveforms.incrementAndGet()\n"
        "    }\n\n"
        "    fun recordWatchdogRetry() {\n"
        "        if (journal?.hasActiveSession == true) watchdogRetries.incrementAndGet()\n"
        "    }\n",
    )
    replace_exact(
        path,
        "        watchdogRetries.set(0L)\n"
        "    }\n",
        "        watchdogRetries.set(0L)\n"
        "        lastReportMs.set(0L)\n"
        "    }\n",
    )


def patch_media_button_limiter() -> None:
    replace_exact(
        "app/src/main/java/org/oxycblt/auxio/playback/service/MediaButtonReceiver.kt",
        '                key = "media-button:$keyCode",\n',
        '                key = "media-button",\n',
    )


def patch_format_diagnostics() -> None:
    path = ".github/workflows/lint.yml"
    replace_exact(
        path,
        "        run: bash ./scripts/ci-gradle.sh spotlessCheck\n",
        "        run: bash ./scripts/check-format-with-diff.sh\n",
    )
    replace_exact(
        path,
        "          path: build/reports/problems/**\n",
        "          path: |\n"
        "            build/reports/startup-performance/**\n"
        "            build/reports/problems/**\n",
        expected=1,
    )


def main() -> int:
    patch_topway_source_policy()
    patch_root_state_holder()
    patch_topway_bridge_logging()
    patch_root_process_tests()
    patch_migration_tests()
    patch_cache_reconciliation()
    patch_visualizer_metrics()
    patch_media_button_limiter()
    patch_format_diagnostics()
    print("all exact PR 194 hardening patches applied")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
