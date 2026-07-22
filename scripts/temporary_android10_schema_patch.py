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
    replace_exact(
        "musikr/src/main/java/org/oxycblt/musikr/cache/db/CacheDatabase.kt",
        "    exportSchema = false,",
        "    exportSchema = true,",
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
