#!/usr/bin/env python3
"""Apply the remaining PR #196 review fixes exactly once."""

from pathlib import Path


def patch(path_value: str, replacements: list[tuple[str, str]]) -> None:
    path = Path(path_value)
    text = path.read_text(encoding="utf-8")
    for old, new in replacements:
        count = text.count(old)
        if count < 1:
            raise SystemExit(f"{path}: expected at least one match, found {count}: {old[:120]!r}")
        text = text.replace(old, new, 1)
    path.write_text(text, encoding="utf-8", newline="\n")


patch(
    "app/src/main/java/org/oxycblt/auxio/BootReceiver.kt",
    [
        (
            """                    .setAction(AuxioService.ACTION_EARLY_PRESTART)
                    .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_ACTIVITY)
""",
            """                    .setAction(AuxioService.ACTION_EARLY_PRESTART)
                    .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_BOOT)
""",
        )
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/headunit/prestart/EarlyPrestartSettings.kt",
    [
        (
            "import android.content.Context\n",
            "import android.content.Context\nimport android.text.format.DateUtils\n",
        ),
        (
            """        return if (lastRunEpochMs > 0L) {
            appContext.getString(
                R.string.set_early_prestart_status_with_time,
                state,
                lastRunEpochMs,
            )
        } else {
""",
            """        return if (lastRunEpochMs > 0L) {
            val formatted =
                DateUtils.getRelativeTimeSpanString(
                        lastRunEpochMs,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                    )
                    .toString()
            appContext.getString(
                R.string.set_early_prestart_status_with_time,
                state,
                formatted,
            )
        } else {
""",
        ),
    ],
)

patch(
    "app/src/main/res/values/strings_source_recovery.xml",
    [
        (
            '<string name="set_early_prestart_status_with_time">%1$s (epoch ms %2$d)</string>',
            '<string name="set_early_prestart_status_with_time">%1$s (%2$s)</string>',
        )
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/StartupLibraryPolicy.kt",
    [
        (
            """        lastScanFailed: () -> Boolean,
        isTopwayCompat: Boolean,
        loadCachedLibrary: suspend () -> T,
""",
            """        lastScanFailed: () -> Boolean,
        loadCachedLibrary: suspend () -> T,
""",
        ),
        (
            """        sourceConfigured: Boolean = true,
        allowAutomaticScan: Boolean = !isTopwayCompat,
""",
            """        sourceConfigured: Boolean = true,
        automaticScanAllowed: Boolean = true,
""",
        ),
        (
            """        // Source availability and start origin are separate authorities. Never scan with no
        // configured source. Standard Android launches retain their historic automatic behaviour;
        // Topway launches scan automatically only when the caller identifies a user-visible start.
        if (!sourceConfigured || (isTopwayCompat && !allowAutomaticScan)) {
""",
            """        // Source availability and caller-provided scan authority are separate. The shared
        // startup core never infers build flavour, boot state or vehicle lifecycle.
        if (!sourceConfigured || !automaticScanAllowed) {
""",
        ),
        (
            """/** Origin of a startup request, used to keep TS18 scanning user- and boot-aware. */
enum class StartupScanOrigin {
    USER_VISIBLE,
    BACKGROUND,
    EARLY_PRESTART;

    val allowAutomaticScan: Boolean
        get() = this == USER_VISIBLE

    internal val priority: Int
        get() = if (allowAutomaticScan) 1 else 0

    companion object {
        fun merge(current: StartupScanOrigin?, next: StartupScanOrigin): StartupScanOrigin =
            if (current == null || next.priority > current.priority) next else current
    }
}

""",
            "",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt",
    [
        (
            """    suspend fun startup(
        worker: IndexingWorker,
        origin: StartupScanOrigin = StartupScanOrigin.BACKGROUND,
    )
""",
            """    suspend fun startup(
        worker: IndexingWorker,
        automaticScanAllowed: Boolean = true,
    )
""",
        ),
        (
            """    override suspend fun startup(worker: IndexingWorker, origin: StartupScanOrigin) {
        PerfTimer.traceSuspend("MusicRepository.startup(origin=$origin)") {
""",
            """    override suspend fun startup(worker: IndexingWorker, automaticScanAllowed: Boolean) {
        PerfTimer.traceSuspend(
            "MusicRepository.startup(automaticScanAllowed=$automaticScanAllowed)"
        ) {
""",
        ),
        (
            """                    lastScanFailed = { musicSettings.lastScanFailed },
                    isTopwayCompat = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    loadCachedLibrary = { 0 },
""",
            """                    lastScanFailed = { musicSettings.lastScanFailed },
                    loadCachedLibrary = { 0 },
""",
        ),
        (
            """                    allowAutomaticScan = origin.allowAutomaticScan,
""",
            """                    automaticScanAllowed = automaticScanAllowed,
""",
        ),
        (
            '                    "[origin=$origin state=${decision.libraryState}, scan=${decision.requestScan}, reason=${decision.reason}]"\n',
            '                    "[automaticScanAllowed=$automaticScanAllowed state=${decision.libraryState}, scan=${decision.requestScan}, reason=${decision.reason}]"\n',
        ),
        (
            """        startCompatibilityHydration(worker, origin)
""",
            """        startCompatibilityHydration(worker, automaticScanAllowed)
""",
        ),
        (
            """    private fun startCompatibilityHydration(worker: IndexingWorker, origin: StartupScanOrigin) {
""",
            """    private fun startCompatibilityHydration(
        worker: IndexingWorker,
        automaticScanAllowed: Boolean,
    ) {
""",
        ),
        (
            """                            sourceConfigured,
                            origin,
""",
            """                            sourceConfigured,
                            automaticScanAllowed,
""",
        ),
        (
            """                            sourceConfigured,
                            origin,
""",
            """                            sourceConfigured,
                            automaticScanAllowed,
""",
        ),
        (
            """        sourceConfigured: Boolean,
        origin: StartupScanOrigin,
""",
            """        sourceConfigured: Boolean,
        automaticScanAllowed: Boolean,
""",
        ),
        (
            """                sourceConfigured &&
                (!BuildConfig.TOPWAY_COMPAT_FLAVOR || origin.allowAutomaticScan)
""",
            """                sourceConfigured &&
                automaticScanAllowed
""",
        ),
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt",
    [
        ("import org.oxycblt.auxio.music.StartupScanOrigin\n", ""),
        (
            """    private var startupJob: Job? = null
    private var activeStartupOrigin: StartupScanOrigin? = null
""",
            """    private var startupJob: Job? = null
    private var attached = false
    private var activeStartupOrigin: StartupScanOrigin? = null
""",
        ),
        (
            """    fun attach() {
        musicSettings.registerListener(this)
        musicRepository.addUpdateListener(this)
        musicRepository.addIndexingListener(this)
        musicRepository.registerWorker(this)
        playbackManager.addListener(this)
        // Observer attachment is cheap: it registers notifications only. Provider enumeration and
        // extraction remain planner-controlled and notification bursts are conflated below.
        if (musicSettings.shouldBeObserving) startTracking()
    }

    fun release() {
        startupJob?.cancel()
        startupJob = null
        activeStartupOrigin = null
        pendingStartupOrigin = null
        stopTracking()
        observationRequestJob?.cancel()
        observationRequestJob = null
        currentIndexJob?.cancel()
        currentIndexJob = null
        pendingIndexRequest = null
        indexJob.cancel()
        wakeLock.releaseSafe()
        musicRepository.unregisterWorker(this)
        playbackManager.removeListener(this)
        musicRepository.removeIndexingListener(this)
        musicRepository.removeUpdateListener(this)
        musicSettings.unregisterListener(this)
    }

    @Synchronized
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
            """    fun attach() {
        synchronized(this) {
            if (attached) return
            attached = true
        }
        musicSettings.registerListener(this)
        musicRepository.addUpdateListener(this)
        musicRepository.addIndexingListener(this)
        musicRepository.registerWorker(this)
        playbackManager.addListener(this)
        // Observer attachment is cheap: it registers notifications only. Provider enumeration and
        // extraction remain planner-controlled and notification bursts are conflated below.
        if (musicSettings.shouldBeObserving) startTracking()
    }

    fun release() {
        val startupToCancel =
            synchronized(this) {
                if (!attached) return
                attached = false
                startupJob.also {
                    startupJob = null
                    activeStartupOrigin = null
                    pendingStartupOrigin = null
                }
            }
        startupToCancel?.cancel()
        stopTracking()
        observationRequestJob?.cancel()
        observationRequestJob = null
        currentIndexJob?.cancel()
        currentIndexJob = null
        pendingIndexRequest = null
        indexJob.cancel()
        wakeLock.releaseSafe()
        musicRepository.unregisterWorker(this)
        playbackManager.removeListener(this)
        musicRepository.removeIndexingListener(this)
        musicRepository.removeUpdateListener(this)
        musicSettings.unregisterListener(this)
    }

    @Synchronized
    fun start(origin: StartupScanOrigin = StartupScanOrigin.BACKGROUND) {
        PerfTimer.trace("IndexingHolder.start(origin=$origin)") {
            if (!attached) {
                L.d("Ignoring startup request after IndexingHolder release [origin=$origin]")
                return
            }
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
            val automaticScanAllowed =
                StartupScanAuthorityPolicy.allowAutomaticScan(
                    topwayCompatFlavor = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    origin = origin,
                )
            activeStartupOrigin = origin
            startupJob =
                indexScope.launch {
                    try {
                        // Root probing is intentionally on-demand. Normal startup must restore
                        // playback/session surfaces without waiting for su.
                        musicRepository.startup(this@IndexingHolder, automaticScanAllowed)
                    } finally {
                        val nextOrigin =
                            synchronized(this@IndexingHolder) {
                                if (!attached) {
                                    startupJob = null
                                    activeStartupOrigin = null
                                    pendingStartupOrigin = null
                                    null
                                } else {
                                    startupJob = null
                                    activeStartupOrigin = null
                                    pendingStartupOrigin.also { pendingStartupOrigin = null }
                                }
                            }
                        // start() rechecks attached under the same monitor, closing the release race
                        // between capturing a queued origin and handing it off.
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
    "app/src/main/java/org/oxycblt/auxio/AuxioService.kt",
    [
        (
            "import org.oxycblt.auxio.music.StartupScanOrigin\n",
            "import org.oxycblt.auxio.music.service.StartupScanOrigin\n",
        )
    ],
)

patch(
    "app/src/main/java/org/oxycblt/auxio/music/service/MusicServiceFragment.kt",
    [("import org.oxycblt.auxio.music.StartupScanOrigin\n", "")],
)

patch(
    "app/src/test/java/org/oxycblt/auxio/playback/persist/PersistenceCancellationTest.kt",
    [
        ("import org.oxycblt.auxio.music.StartupScanOrigin\n", ""),
        (
            """        override suspend fun startup(
            worker: MusicRepository.IndexingWorker,
            origin: StartupScanOrigin,
        ) = Unit
""",
            """        override suspend fun startup(
            worker: MusicRepository.IndexingWorker,
            automaticScanAllowed: Boolean,
        ) = Unit
""",
        ),
    ],
)

startup_test = Path("app/src/test/java/org/oxycblt/auxio/music/StartupScanOriginTest.kt")
startup_test.write_text(
    '''/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanOriginTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oxycblt.auxio.music.service.StartupScanAuthorityPolicy
import org.oxycblt.auxio.music.service.StartupScanOrigin

class StartupScanOriginTest {
    @Test
    fun userVisibleOriginOverridesBackgroundOrigin() {
        assertEquals(
            StartupScanOrigin.USER_VISIBLE,
            StartupScanOrigin.merge(StartupScanOrigin.BACKGROUND, StartupScanOrigin.USER_VISIBLE),
        )
        assertEquals(
            StartupScanOrigin.USER_VISIBLE,
            StartupScanOrigin.merge(
                StartupScanOrigin.USER_VISIBLE,
                StartupScanOrigin.EARLY_PRESTART,
            ),
        )
    }

    @Test
    fun topwayUserVisibleFirstStartMayRequestOneScan() = runBlocking {
        var requests = 0
        val decision =
            StartupLibraryStartup.run(
                hasInMemoryLibrary = false,
                revisionKnown = false,
                priorState = LibraryState.NEVER,
                deferCachedLoad = true,
                lastScanFailed = { false },
                loadCachedLibrary = { Unit },
                cachedSongCount = { 0 },
                emitCachedLibrary = {},
                emitCachedLoadFailure = {},
                setLibraryState = {},
                requestIndex = { requests++ },
                sourceConfigured = true,
                automaticScanAllowed =
                    StartupScanAuthorityPolicy.allowAutomaticScan(
                        topwayCompatFlavor = true,
                        origin = StartupScanOrigin.USER_VISIBLE,
                    ),
            )

        assertTrue(decision.requestScan)
        assertEquals(1, requests)
    }

    @Test
    fun topwayBackgroundAndEarlyPrestartDoNotRequestScans() = runBlocking {
        for (origin in listOf(StartupScanOrigin.BACKGROUND, StartupScanOrigin.EARLY_PRESTART)) {
            var requests = 0
            val decision =
                StartupLibraryStartup.run(
                    hasInMemoryLibrary = false,
                    revisionKnown = false,
                    priorState = LibraryState.NEVER,
                    deferCachedLoad = true,
                    lastScanFailed = { false },
                    loadCachedLibrary = { Unit },
                    cachedSongCount = { 0 },
                    emitCachedLibrary = {},
                    emitCachedLoadFailure = {},
                    setLibraryState = {},
                    requestIndex = { requests++ },
                    sourceConfigured = true,
                    automaticScanAllowed =
                        StartupScanAuthorityPolicy.allowAutomaticScan(
                            topwayCompatFlavor = true,
                            origin = origin,
                        ),
                )

            assertFalse(decision.requestScan)
            assertEquals(0, requests)
        }
    }

    @Test
    fun standardBackgroundRetainsAutomaticFirstStartScan() {
        assertTrue(
            StartupScanAuthorityPolicy.allowAutomaticScan(
                topwayCompatFlavor = false,
                origin = StartupScanOrigin.BACKGROUND,
            )
        )
    }

    @Test
    fun noConfiguredSourceNeverRequestsScan() = runBlocking {
        var requests = 0
        val decision =
            StartupLibraryStartup.run(
                hasInMemoryLibrary = false,
                revisionKnown = false,
                priorState = LibraryState.NEVER,
                deferCachedLoad = true,
                lastScanFailed = { false },
                loadCachedLibrary = { Unit },
                cachedSongCount = { 0 },
                emitCachedLibrary = {},
                emitCachedLoadFailure = {},
                setLibraryState = {},
                requestIndex = { requests++ },
                sourceConfigured = false,
                automaticScanAllowed = true,
            )

        assertFalse(decision.requestScan)
        assertEquals(0, requests)
    }
}
''',
    encoding="utf-8",
    newline="\n",
)
