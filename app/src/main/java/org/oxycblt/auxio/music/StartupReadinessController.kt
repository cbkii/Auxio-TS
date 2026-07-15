/*
 * Copyright (c) 2026 Auxio Project
 * StartupReadinessController.kt is part of Auxio.
 */
package org.oxycblt.auxio.music

import java.util.EnumSet
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.util.PerfTimer

/**
 * Process-wide startup capability coordinator.
 *
 * Capability milestones are recorded independently and the public contiguous stage advances only
 * when every prerequisite has actually been achieved. Recoverable library/source conditions are
 * tracked orthogonally and never compete with capability ordering.
 */
@Singleton
class StartupReadinessController @Inject constructor() {
    data class State(
        val achieved: Set<StartupCapability> = setOf(StartupCapability.PROCESS_VISIBLE),
        val contiguous: StartupReadinessState = StartupReadinessState.ProcessVisible,
        val libraryStatus: StartupLibraryStatus = StartupLibraryStatus.Unknown,
    )

    fun interface Listener {
        fun onStartupReadinessStateChanged()
    }

    private val listeners = CopyOnWriteArrayList<Listener>()
    private val achieved = EnumSet.of(StartupCapability.PROCESS_VISIBLE)

    @Volatile private var currentState = State()

    val state: State
        get() = currentState

    val capability: StartupReadinessState
        get() = currentState.contiguous

    fun addListener(listener: Listener) {
        listeners.add(listener)
        listener.onStartupReadinessStateChanged()
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun publishCapability(capability: StartupReadinessState) {
        val milestone = capability.toCapability()
        val snapshot = synchronized(this) {
            if (!achieved.add(milestone)) return
            currentState =
                currentState.copy(
                    achieved = achieved.toSet(),
                    contiguous = deriveContiguous(),
                )
            PerfTimer.point("startup.capability.${milestone.name}")
            listeners.toList()
        }
        snapshot.forEach { it.onStartupReadinessStateChanged() }
    }

    fun publishLibraryStatus(status: StartupLibraryStatus) {
        val snapshot = synchronized(this) {
            if (currentState.libraryStatus == status) return
            currentState = currentState.copy(libraryStatus = status)
            PerfTimer.point("startup.libraryStatus.${status.javaClass.simpleName}")
            listeners.toList()
        }
        snapshot.forEach { it.onStartupReadinessStateChanged() }
    }

    private fun deriveContiguous(): StartupReadinessState {
        var latest = StartupReadinessState.ProcessVisible
        for (capability in StartupCapability.entries) {
            if (capability !in achieved) break
            latest = capability.toReadinessState()
        }
        return latest
    }
}

enum class StartupCapability {
    PROCESS_VISIBLE,
    PLAYBACK_SERVICE_READY,
    QUEUE_READY,
    FAST_BROWSE_READY,
    SEARCH_READY,
    FULL_LIBRARY_READY,
    ENRICHMENT_COMPLETE,
}

private fun StartupReadinessState.toCapability(): StartupCapability =
    when (this) {
        StartupReadinessState.ProcessVisible -> StartupCapability.PROCESS_VISIBLE
        StartupReadinessState.PlaybackServiceReady -> StartupCapability.PLAYBACK_SERVICE_READY
        StartupReadinessState.QueueReady -> StartupCapability.QUEUE_READY
        StartupReadinessState.FastBrowseReady -> StartupCapability.FAST_BROWSE_READY
        StartupReadinessState.SearchReady -> StartupCapability.SEARCH_READY
        StartupReadinessState.FullLibraryReady -> StartupCapability.FULL_LIBRARY_READY
        StartupReadinessState.EnrichmentComplete -> StartupCapability.ENRICHMENT_COMPLETE
    }

private fun StartupCapability.toReadinessState(): StartupReadinessState =
    when (this) {
        StartupCapability.PROCESS_VISIBLE -> StartupReadinessState.ProcessVisible
        StartupCapability.PLAYBACK_SERVICE_READY -> StartupReadinessState.PlaybackServiceReady
        StartupCapability.QUEUE_READY -> StartupReadinessState.QueueReady
        StartupCapability.FAST_BROWSE_READY -> StartupReadinessState.FastBrowseReady
        StartupCapability.SEARCH_READY -> StartupReadinessState.SearchReady
        StartupCapability.FULL_LIBRARY_READY -> StartupReadinessState.FullLibraryReady
        StartupCapability.ENRICHMENT_COMPLETE -> StartupReadinessState.EnrichmentComplete
    }

/** Recoverable library/source status independent of monotonic startup capabilities. */
sealed interface StartupLibraryStatus {
    data object Unknown : StartupLibraryStatus
    data object Usable : StartupLibraryStatus
    data object Empty : StartupLibraryStatus
    data object NeedsMusicSource : StartupLibraryStatus
    data object CacheUnavailable : StartupLibraryStatus
    data object SourceUnavailable : StartupLibraryStatus
}
