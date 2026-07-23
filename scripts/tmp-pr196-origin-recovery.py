#!/usr/bin/env python3
"""Temporary exact-once patch helper for PR #196 startup-origin recovery."""

from pathlib import Path


def patch(path_value: str, replacements: list[tuple[str, str]]) -> None:
    path = Path(path_value)
    text = path.read_text()
    for old, new in replacements:
        count = text.count(old)
        if count < 1:
            raise SystemExit(
                f"{path}: expected at least one match, found {count}: {old[:100]!r}"
            )
        text = text.replace(old, new, 1)
    path.write_text(text)


patch(
    "app/src/main/java/org/oxycblt/auxio/music/StartupLibraryPolicy.kt",
    [
        (
            """    val allowAutomaticScan: Boolean
        get() = this == USER_VISIBLE
}
""",
            """    val allowAutomaticScan: Boolean
        get() = this == USER_VISIBLE

    internal val priority: Int
        get() = if (allowAutomaticScan) 1 else 0

    companion object {
        fun merge(current: StartupScanOrigin?, next: StartupScanOrigin): StartupScanOrigin =
            if (current == null || next.priority > current.priority) next else current
    }
}
""",
        )
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    [
        (
            """    private var currentIndexJob: Job? = null
    private var pendingIndexRequest: IndexRequest? = null
    private var startupJob: Job? = null
""",
            """    private var currentIndexJob: Job? = null
    private var pendingIndexRequest: IndexRequest? = null
    private var startupJob: Job? = null
    private var activeStartupOrigin: StartupScanOrigin? = null
    private var pendingStartupOrigin: StartupScanOrigin? = null
""",
        ),
        (
            """        startupJob?.cancel()
        startupJob = null
        stopTracking()
""",
            """        startupJob?.cancel()
        startupJob = null
        activeStartupOrigin = null
        pendingStartupOrigin = null
        stopTracking()
""",
        ),
        (
            """    @Synchronized
    fun start(origin: StartupScanOrigin = StartupScanOrigin.BACKGROUND) {
        PerfTimer.trace("IndexingHolder.start(origin=$origin)") {
            if (startupJob?.isActive == true) {
                L.d("Startup library load already running; ignoring duplicate start")
                return
            }
            startupJob =
                indexScope.launch {
                    // Root probing is intentionally on-demand. Normal startup must restore
                    // playback/session surfaces without waiting for su.
                    musicRepository.startup(this@IndexingHolder, origin)
                }
        }
    }
""",
            """    @Synchronized
    fun start(origin: StartupScanOrigin = StartupScanOrigin.BACKGROUND) {
        PerfTimer.trace("IndexingHolder.start(origin=$origin)") {
            if (startupJob?.isActive == true) {
                val activePriority = activeStartupOrigin?.priority ?: 0
                if (origin.priority > activePriority) {
                    pendingStartupOrigin = StartupScanOrigin.merge(pendingStartupOrigin, origin)
                    L.d(
                        "Queued higher-priority startup origin while load is active " +
                            "[active=$activeStartupOrigin pending=$pendingStartupOrigin]"
                    )
                } else {
                    L.d(
                        "Startup library load already running; ignoring duplicate " +
                            "[active=$activeStartupOrigin requested=$origin]"
                    )
                }
                return
            }
            activeStartupOrigin = origin
            startupJob =
                indexScope.launch {
                    try {
                        // Root probing is intentionally on-demand. Normal startup must restore
                        // playback/session surfaces without waiting for su.
                        musicRepository.startup(this@IndexingHolder, origin)
                    } finally {
                        val nextOrigin =
                            synchronized(this@IndexingHolder) {
                                startupJob = null
                                activeStartupOrigin = null
                                pendingStartupOrigin.also { pendingStartupOrigin = null }
                            }
                        if (nextOrigin != null) {
                            start(nextOrigin)
                        }
                    }
                }
        }
    }
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    [
        (
            """        startCompatibilityHydration(worker)
        startCompatibilityBackfill()
""",
            """        startCompatibilityHydration(worker, origin)
        startCompatibilityBackfill()
""",
        ),
        (
            """    private fun startCompatibilityHydration(worker: IndexingWorker) {
""",
            """    private fun startCompatibilityHydration(
        worker: IndexingWorker,
        origin: StartupScanOrigin,
    ) {
""",
        ),
        (
            """                        requestCompatibilityRecoveryIfNeeded(
                            worker,
                            priorState,
                            decision,
                            sourceConfigured,
                        )
""",
            """                        requestCompatibilityRecoveryIfNeeded(
                            worker,
                            priorState,
                            decision,
                            sourceConfigured,
                            origin,
                        )
""",
        ),
        (
            """                        requestCompatibilityRecoveryIfNeeded(
                            worker,
                            priorState,
                            decision,
                            sourceConfigured,
                        )
""",
            """                        requestCompatibilityRecoveryIfNeeded(
                            worker,
                            priorState,
                            decision,
                            sourceConfigured,
                            origin,
                        )
""",
        ),
        (
            """    private fun requestCompatibilityRecoveryIfNeeded(
        worker: IndexingWorker,
        priorState: LibraryState,
        decision: StartupLibraryPolicy.Decision,
        sourceConfigured: Boolean,
    ) {
""",
            """    private fun requestCompatibilityRecoveryIfNeeded(
        worker: IndexingWorker,
        priorState: LibraryState,
        decision: StartupLibraryPolicy.Decision,
        sourceConfigured: Boolean,
        origin: StartupScanOrigin,
    ) {
""",
        ),
        (
            """                sourceConfigured &&
                !BuildConfig.TOPWAY_COMPAT_FLAVOR
""",
            """                sourceConfigured &&
                (!BuildConfig.TOPWAY_COMPAT_FLAVOR || origin.allowAutomaticScan)
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/home/list/SongListFragment.kt",
    [
        (
            """            if (granted) {
                musicModel.refresh()
            } else if (isAdded) {
""",
            """            if (granted) {
                continueAfterStoragePermission()
            } else if (isAdded) {
""",
        ),
        (
            """        if (hasStoragePermission()) {
            musicModel.refresh()
            return
        }
""",
            """        if (hasStoragePermission()) {
            continueAfterStoragePermission()
            return
        }
""",
        ),
        (
            """    private fun showOpenAppSettingsDialog() {
""",
            """    private fun continueAfterStoragePermission() {
        val sourceConfigured =
            StartupLibraryPolicy.isMusicSourceConfigured(
                musicSettings.locationMode,
                musicSettings.configuredSourceCount,
            )
        if (sourceConfigured) {
            musicModel.refresh()
        } else {
            homeModel.startChooseMusicLocations()
        }
    }

    private fun showOpenAppSettingsDialog() {
""",
        ),
    ],
)
