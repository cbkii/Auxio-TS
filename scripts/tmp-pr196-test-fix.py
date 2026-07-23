#!/usr/bin/env python3
"""Apply the remaining PR #196 interface and root-consent fixes exactly once."""

from pathlib import Path


def patch(path_value: str, replacements: list[tuple[str, str]]) -> None:
    path = Path(path_value)
    text = path.read_text(encoding="utf-8")
    for old, new in replacements:
        if text.count(old) != 1:
            raise SystemExit(f"{path}: expected exactly one match: {old!r}")
        text = text.replace(old, new, 1)
    path.write_text(text, encoding="utf-8", newline="\n")


patch(
    "app/src/test/java/org/oxycblt/auxio/playback/persist/PersistenceCancellationTest.kt",
    [
        (
            "import org.oxycblt.auxio.music.StartupReadinessState\n",
            "import org.oxycblt.auxio.music.StartupReadinessState\n"
            "import org.oxycblt.auxio.music.StartupScanOrigin\n",
        ),
        (
            "        override suspend fun startup(worker: MusicRepository.IndexingWorker) = Unit\n",
            """        override suspend fun startup(
            worker: MusicRepository.IndexingWorker,
            origin: StartupScanOrigin,
        ) = Unit
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/headunit/root/RootStateHolder.kt",
    [
        (
            "import org.oxycblt.auxio.BuildConfig\n",
            "import org.oxycblt.auxio.BuildConfig\nimport org.oxycblt.auxio.R\n",
        ),
        (
            "import org.oxycblt.auxio.diagnostics.DiagnosticJournal\n",
            "import org.oxycblt.auxio.diagnostics.DiagnosticJournal\n"
            "import org.oxycblt.auxio.music.RootAccessPolicy\n",
        ),
        (
            """        prefs.edit { putBoolean(KEY_USE_ROOT_FS, enabled) }
        state = if (enabled) State.Unknown else State.DisabledByUser
""",
            """        prefs.edit {
            putBoolean(KEY_USE_ROOT_FS, enabled)
            putString(
                context.getString(R.string.set_key_root_access_policy),
                if (enabled) RootAccessPolicy.ON_DEMAND.name else RootAccessPolicy.OFF.name,
            )
        }
        state = if (enabled) State.Unknown else State.DisabledByUser
""",
        ),
    ],
)
