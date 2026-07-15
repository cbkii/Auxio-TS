/*
 * Copyright (c) 2026 Auxio Project
 * StartupReadinessController.kt is part of Auxio.
 */
package org.oxycblt.auxio.music

import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import javax.inject.Singleton
import org.oxycblt.auxio.util.PerfTimer

/**
 * Process-wide startup capability coordinator.
 *
 * Capability milestones are monotonic and are published only by their owning runtime component:
 * playback service/session by the playback service, primitive queue by the queue restore owner,
 * fast browse/search by the normalized projection owner, and full library by the legacy rich graph
 * publisher. Recoverable library/source conditions are tracked orthogonally and never compete with
 * capability ordering.
 */
@Singleton
class StartupReadinessController @Inject constructor() {
    data class State(
        val capability: StartupReadinessState = StartupReadinessState.ProcessVisible,
        val libraryStatus: StartupLibraryStatus = StartupLibraryStatus.Unknown,
    )

    fun interface Listener {
        fun onStartupReadinessStateChanged()
    }

    private val listeners = CopyOnWriteArrayList<Listener>()

    @Volatile private var currentState = State()

    val state: State
        get() = currentState

    val capability: StartupReadinessState
        get() = currentState.capability

    fun addListener(listener: Listener) {
        listeners.add(listener)
        listener.onStartupReadinessStateChanged()
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    fun publishCapability(capability: StartupReadinessState) {
        update { state ->
            val next = StartupReadinessTransitions.advance(state.capability, capability)
            if (next == state.capability) state else state.copy(capability = next)
        }
    }

    fun publishLibraryStatus(status: StartupLibraryStatus) {
        update { state -> if (state.libraryStatus == status) state else state.copy(libraryStatus = status) }
    }

    private fun update(transform: (State) -> State) {
        val snapshot = synchronized(this) {
            val next = transform(currentState)
            if (next == currentState) return
            currentState = next
            PerfTimer.point("startup.${next.capability.javaClass.simpleName}.${next.libraryStatus.javaClass.simpleName}")
            listeners.toList()
        }
        snapshot.forEach { it.onStartupReadinessStateChanged() }
    }
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
