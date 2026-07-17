/*
 * Copyright (c) 2026 Auxio Project
 * StartupLibraryPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.oxycblt.auxio.music

import org.oxycblt.auxio.music.locations.LocationMode

/** UI-visible monotonic startup/readiness capability for launch UX. */
sealed interface StartupReadinessState {
    val rank: Int

    /** Process is visible; stable shell can render without library hydration. */
    data object ProcessVisible : StartupReadinessState {
        override val rank = 0
    }

    /** The canonical playback service/session is constructed and can accept controller commands. */
    data object PlaybackServiceReady : StartupReadinessState {
        override val rank = 1
    }

    /**
     * Primitive queue restore has resolved to a playable window, raw fallback, or explicit empty
     * state.
     */
    data object QueueReady : StartupReadinessState {
        override val rank = 2
    }

    /** Bounded database/source projections are available for Fast Start browsing. */
    data object FastBrowseReady : StartupReadinessState {
        override val rank = 3
    }

    /** Quick database search may run without full library hydration. */
    data object SearchReady : StartupReadinessState {
        override val rank = 4
    }

    /** Complete legacy Musikr library graph is available for rich screens. */
    data object FullLibraryReady : StartupReadinessState {
        override val rank = 5
    }

    /** Post-load enrichment has finished. */
    data object EnrichmentComplete : StartupReadinessState {
        override val rank = 6
    }
}

object StartupReadinessTransitions {
    fun advance(
        current: StartupReadinessState,
        next: StartupReadinessState,
    ): StartupReadinessState = if (next.rank >= current.rank) next else current
}

/**
 * Pure startup state-machine for restoring the persisted music library before any slow scan.
 *
 * This keeps the launch path offline-first: a usable cache is emitted immediately, a known empty
 * cache stays an intentional empty state, and corrupt/missing cached data for a previously usable
 * library enters recovery instead of leaving the UI blank forever.
 */
object StartupLibraryPolicy {
    data class Decision(
        val libraryState: LibraryState,
        val requestScan: Boolean,
        val reason: String,
    )

    fun shouldAttemptCachedLoad(
        hasInMemoryLibrary: Boolean,
        revisionKnown: Boolean,
        priorState: LibraryState,
    ): Boolean =
        !hasInMemoryLibrary &&
            (revisionKnown || priorState == LibraryState.USABLE || priorState == LibraryState.EMPTY)

    fun onNoCachedRevision(priorState: LibraryState, lastScanFailed: Boolean): Decision =
        when (priorState) {
            LibraryState.NEVER ->
                Decision(
                    libraryState = LibraryState.NEVER,
                    requestScan = !lastScanFailed,
                    reason = "first-start-no-cache",
                )
            LibraryState.USABLE ->
                Decision(
                    libraryState = LibraryState.RECOVERY,
                    requestScan = !lastScanFailed,
                    reason = "usable-state-without-revision",
                )
            LibraryState.EMPTY ->
                Decision(
                    libraryState = LibraryState.EMPTY,
                    requestScan = false,
                    reason = "known-empty-without-cache",
                )
            LibraryState.RECOVERY ->
                Decision(
                    libraryState = LibraryState.RECOVERY,
                    requestScan = false,
                    reason = "already-in-recovery",
                )
        }

    fun onCachedLoadSucceeded(
        priorState: LibraryState,
        songCount: Int,
        lastScanFailed: Boolean,
    ): Decision =
        when {
            songCount > 0 ->
                Decision(
                    libraryState = LibraryState.USABLE,
                    requestScan = false,
                    reason = "cached-library-usable",
                )
            priorState == LibraryState.EMPTY ->
                Decision(
                    libraryState = LibraryState.EMPTY,
                    requestScan = false,
                    reason = "cached-library-known-empty",
                )
            priorState == LibraryState.NEVER ->
                Decision(
                    libraryState = LibraryState.NEVER,
                    requestScan = !lastScanFailed,
                    reason = "empty-cache-before-first-success",
                )
            priorState == LibraryState.USABLE ->
                Decision(
                    libraryState = LibraryState.RECOVERY,
                    requestScan = !lastScanFailed,
                    reason = "usable-cache-unexpectedly-empty",
                )
            else ->
                Decision(
                    libraryState = LibraryState.RECOVERY,
                    requestScan = false,
                    reason = "empty-cache-in-recovery",
                )
        }

    fun onCachedLoadFailed(priorState: LibraryState, lastScanFailed: Boolean): Decision =
        when (priorState) {
            LibraryState.NEVER ->
                Decision(
                    libraryState = LibraryState.NEVER,
                    requestScan = !lastScanFailed,
                    reason = "cache-failed-before-first-success",
                )
            LibraryState.USABLE ->
                Decision(
                    libraryState = LibraryState.RECOVERY,
                    requestScan = !lastScanFailed,
                    reason = "cache-failed-after-usable-library",
                )
            LibraryState.EMPTY ->
                Decision(
                    libraryState = LibraryState.RECOVERY,
                    requestScan = false,
                    reason = "cache-failed-after-empty-library",
                )
            LibraryState.RECOVERY ->
                Decision(
                    libraryState = LibraryState.RECOVERY,
                    requestScan = false,
                    reason = "cache-failed-in-recovery",
                )
        }

    fun isMusicSourceConfigured(locationMode: LocationMode, sourceCount: Int): Boolean =
        when (locationMode) {
            LocationMode.SAF,
            LocationMode.DIRECT_FS -> sourceCount > 0
            // MediaStore is the selected system music provider. An empty filter list means "all".
            LocationMode.MEDIA_STORE -> true
        }

    fun startupReadinessAfterDecision(
        decision: Decision,
        sourceConfigured: Boolean,
        cachedSongCount: Int?,
    ): StartupLibraryStatus =
        when {
            cachedSongCount != null && cachedSongCount > 0 -> StartupLibraryStatus.Usable
            decision.libraryState == LibraryState.USABLE -> StartupLibraryStatus.Usable
            decision.libraryState == LibraryState.EMPTY -> StartupLibraryStatus.Empty
            !sourceConfigured -> StartupLibraryStatus.NeedsMusicSource
            else -> StartupLibraryStatus.CacheUnavailable
        }

    fun onIndexFailed(priorState: LibraryState): LibraryState =
        if (priorState == LibraryState.NEVER) LibraryState.RECOVERY else priorState
}

/**
 * Production startup runner used by [MusicRepositoryImpl] and tests.
 *
 * Keeping the side-effect wiring here makes it possible to verify that policy decisions are not
 * bypassed by the repository launch path.
 */
object StartupLibraryStartup {
    suspend fun <T> run(
        hasInMemoryLibrary: Boolean,
        revisionKnown: Boolean,
        priorState: LibraryState,
        lastScanFailed: () -> Boolean,
        isTopwayCompat: Boolean,
        loadCachedLibrary: suspend () -> T,
        cachedSongCount: (T) -> Int,
        emitCachedLibrary: suspend (T) -> Unit,
        emitCachedLoadFailure: suspend (Exception) -> Unit,
        setLibraryState: (LibraryState) -> Unit,
        requestIndex: (withCache: Boolean) -> Unit,
        setStartupReadinessState: (StartupReadinessState) -> Unit = {},
        setStartupLibraryStatus: (StartupLibraryStatus) -> Unit = {},
        sourceConfigured: Boolean = true,
    ): StartupLibraryPolicy.Decision {
        setStartupReadinessState(StartupReadinessState.ProcessVisible)
        setStartupLibraryStatus(StartupLibraryStatus.Unknown)
        var cachedSongCountOrNull: Int? = null
        var decision =
            if (
                StartupLibraryPolicy.shouldAttemptCachedLoad(
                    hasInMemoryLibrary,
                    revisionKnown,
                    priorState,
                )
            ) {
                try {
                    val cached = loadCachedLibrary()
                    val songCount = cachedSongCount(cached)
                    cachedSongCountOrNull = songCount
                    StartupLibraryPolicy.onCachedLoadSucceeded(
                            priorState,
                            songCount,
                            lastScanFailed(),
                        )
                        .also { decision ->
                            if (songCount > 0 || decision.libraryState == LibraryState.EMPTY) {
                                emitCachedLibrary(cached)
                            }
                        }
                } catch (e: Exception) {
                    emitCachedLoadFailure(e)
                    StartupLibraryPolicy.onCachedLoadFailed(priorState, lastScanFailed())
                }
            } else if (!hasInMemoryLibrary) {
                StartupLibraryPolicy.onNoCachedRevision(priorState, lastScanFailed())
            } else {
                StartupLibraryPolicy.Decision(
                    libraryState = priorState,
                    requestScan = false,
                    reason = "library-already-in-memory",
                )
            }

        // On TS18/Topway builds, scanning/rescan/refresh must only be user-triggered
        if (isTopwayCompat) {
            decision = decision.copy(requestScan = false)
        }

        setLibraryState(decision.libraryState)
        setStartupLibraryStatus(
            StartupLibraryPolicy.startupReadinessAfterDecision(
                decision,
                sourceConfigured,
                cachedSongCountOrNull,
            )
        )
        if (
            decision.libraryState == LibraryState.USABLE &&
                cachedSongCountOrNull != null &&
                cachedSongCountOrNull > 0
        ) {
            setStartupReadinessState(StartupReadinessState.FullLibraryReady)
        }
        if (decision.requestScan) {
            requestIndex(MusicScanRequestMode.REFRESH_WITH_CACHE)
        }
        return decision
    }
}

/** Cache mode constants for user-driven scan actions. */
object MusicScanRequestMode {
    const val REFRESH_WITH_CACHE = true
    const val RESCAN_WITH_CACHE = false
}
