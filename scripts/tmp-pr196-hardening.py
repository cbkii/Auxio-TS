#!/usr/bin/env python3
"""Temporary exact-once patch helper for PR #196 hardening."""

from pathlib import Path


def patch(path_value: str, replacements: list[tuple[str, str]]) -> None:
    path = Path(path_value)
    text = path.read_text()
    for old, new in replacements:
        count = text.count(old)
        if count != 1:
            raise SystemExit(
                f"{path}: expected one match, found {count}: {old[:100]!r}"
            )
        text = text.replace(old, new, 1)
    path.write_text(text)


patch(
    "app/src/main/java/org/oxycblt/auxio/music/StartupLibraryPolicy.kt",
    [
        (
            "/** Cache mode constants for user-driven scan actions. */\nobject MusicScanRequestMode {",
            """/** Origin of a startup request, used to keep TS18 scanning user- and boot-aware. */
enum class StartupScanOrigin {
    USER_VISIBLE,
    BACKGROUND,
    EARLY_PRESTART;

    val allowAutomaticScan: Boolean
        get() = this == USER_VISIBLE
}

/** Cache mode constants for user-driven scan actions. */
object MusicScanRequestMode {""",
        )
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    [
        (
            "    suspend fun startup(worker: IndexingWorker)\n",
            """    suspend fun startup(
        worker: IndexingWorker,
        origin: StartupScanOrigin = StartupScanOrigin.BACKGROUND,
    )
""",
        ),
        (
            "    override suspend fun startup(worker: IndexingWorker) {\n        PerfTimer.traceSuspend(\"MusicRepository.startup\") {",
            """    override suspend fun startup(worker: IndexingWorker, origin: StartupScanOrigin) {
        PerfTimer.traceSuspend("MusicRepository.startup(origin=$origin)") {""",
        ),
        (
            """                    sourceConfigured =
                        StartupLibraryPolicy.isMusicSourceConfigured(
                            musicSettings.locationMode,
                            musicSettings.configuredSourceCount,
                        ),
                )
""",
            """                    sourceConfigured =
                        StartupLibraryPolicy.isMusicSourceConfigured(
                            musicSettings.locationMode,
                            musicSettings.configuredSourceCount,
                        ),
                    allowAutomaticScan = origin.allowAutomaticScan,
                )
""",
        ),
        (
            '                    "[state=${decision.libraryState}, scan=${decision.requestScan}, reason=${decision.reason}]"\n',
            '                    "[origin=$origin state=${decision.libraryState}, scan=${decision.requestScan}, reason=${decision.reason}]"\n',
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    [
        (
            "import org.oxycblt.auxio.music.RootAccessPolicy\n",
            "import org.oxycblt.auxio.music.RootAccessPolicy\nimport org.oxycblt.auxio.music.StartupScanOrigin\n",
        ),
        (
            """    @Synchronized
    fun start() {
        PerfTimer.trace("IndexingHolder.start") {
""",
            """    @Synchronized
    fun start(origin: StartupScanOrigin = StartupScanOrigin.BACKGROUND) {
        PerfTimer.trace("IndexingHolder.start(origin=$origin)") {
""",
        ),
        (
            """                    musicRepository.startup(this@IndexingHolder)
""",
            """                    musicRepository.startup(this@IndexingHolder, origin)
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/service/MusicServiceFragment.kt",
    [
        (
            "import org.oxycblt.auxio.ForegroundServiceNotification\n",
            "import org.oxycblt.auxio.ForegroundServiceNotification\nimport org.oxycblt.auxio.music.StartupScanOrigin\n",
        ),
        (
            """    fun start() {
        L.d("Starting music service fragment without forcing a scan")
        indexer.start()
    }
""",
            """    fun start(origin: StartupScanOrigin) {
        L.d("Starting music service fragment [origin=$origin]")
        indexer.start(origin)
    }
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    [
        (
            "import org.oxycblt.auxio.music.StartupReadinessState\n",
            "import org.oxycblt.auxio.music.StartupReadinessState\nimport org.oxycblt.auxio.music.StartupScanOrigin\n",
        ),
        (
            """    private fun onHandleForeground(intent: Intent?) {
        // TS18 fast-resume priority: handle playback/launcher commands before any heavy
        // music indexing path. This keeps raw snapshot restore independent from library readiness.
        playbackFragment.start(intent)
        musicFragment.start()
    }
""",
            """    private fun onHandleForeground(intent: Intent?) {
        // TS18 fast-resume priority: handle playback/launcher commands before any heavy
        // music indexing path. This keeps raw snapshot restore independent from library readiness.
        val startId = intent?.getIntExtra(INTENT_KEY_START_ID, -1)
        val origin =
            when {
                intent?.action == ACTION_EARLY_PRESTART -> StartupScanOrigin.EARLY_PRESTART
                startId == IntegerTable.START_ID_ACTIVITY -> StartupScanOrigin.USER_VISIBLE
                else -> StartupScanOrigin.BACKGROUND
            }
        playbackFragment.start(intent)
        musicFragment.start(origin)
    }
""",
        ),
        (
            """                // Do not remove a real playback/indexing foreground owner or stop a service that
                // acquired useful foreground work while prestart was running.
                if (playbackFragment.notification == null && !musicFragment.hasForegroundWork()) {
                    ServiceCompat.stopForeground(
                        this@AuxioService,
                        ServiceCompat.STOP_FOREGROUND_REMOVE,
                    )
                    isForeground = false
                    stopSelfResult(startId)
                }
""",
            """                // Restore any real playback/indexing foreground owner that became active while
                // prestart was running. Otherwise remove only the temporary prestart notification.
                val playbackForeground = playbackFragment.notification != null
                val musicForeground = musicFragment.hasForegroundWork()
                if (playbackForeground || musicForeground) {
                    updateForeground(
                        if (playbackForeground) {
                            ForegroundListener.Change.MEDIA_SESSION
                        } else {
                            ForegroundListener.Change.INDEXER
                        }
                    )
                } else {
                    ServiceCompat.stopForeground(
                        this@AuxioService,
                        ServiceCompat.STOP_FOREGROUND_REMOVE,
                    )
                    isForeground = false
                    stopSelfResult(startId)
                }
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/home/HomeFragment.kt",
    [
        (
            "    private var storagePermissionLauncher: ActivityResultLauncher<String>? = null\n",
            "",
        ),
        (
            """        // Have to set up the permission launcher before the view is shown
        storagePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                musicModel.refresh()
            }

""",
            "",
        ),
        ("        storagePermissionLauncher = null\n", ""),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/locations/LocationsDialog.kt",
    [
        (
            """            // Hide the permission card when permissions are granted
            locationsPermsCard.isVisible = !hasStoragePermission
""",
            """            // SAF/File Picker owns its URI grant and does not need broad storage permission.
            locationsPermsCard.isVisible =
                locationMode != LocationMode.SAF && !hasStoragePermission
""",
        )
    ],
)
