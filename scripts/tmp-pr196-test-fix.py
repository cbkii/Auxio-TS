#!/usr/bin/env python3
"""Update the persistence test stub for StartupScanOrigin."""

from pathlib import Path

path = Path("app/src/test/java/org/oxycblt/auxio/playback/persist/PersistenceCancellationTest.kt")
text = path.read_text(encoding="utf-8")
replacements = [
    (
        "import org.oxycblt.auxio.music.StartupReadinessState\n",
        "import org.oxycblt.auxio.music.StartupReadinessState\nimport org.oxycblt.auxio.music.StartupScanOrigin\n",
    ),
    (
        "        override suspend fun startup(worker: MusicRepository.IndexingWorker) = Unit\n",
        """        override suspend fun startup(
            worker: MusicRepository.IndexingWorker,
            origin: StartupScanOrigin,
        ) = Unit
""",
    ),
]
for old, new in replacements:
    if text.count(old) != 1:
        raise SystemExit(f"Expected exactly one match: {old!r}")
    text = text.replace(old, new, 1)
path.write_text(text, encoding="utf-8", newline="\n")
