/*
 * Copyright (c) 2026 Auxio Project
 * StartupOptionalWorkGate.kt is part of Auxio.
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

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.oxycblt.auxio.util.PerfTimer

/**
 * One-shot startup gate for work that may allocate or scan the whole library.
 *
 * Fast browse/search remain outside this gate. Compatibility hydration, cache backfill, and
 * generated playlists wait for both queue readiness and a terminal restore state. The bounded
 * no-session release prevents a missing or invalid saved session from becoming a deadlock.
 */
@Singleton
class StartupOptionalWorkGate
@Inject
constructor(private val readinessController: StartupReadinessController) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val opened = CompletableDeferred<Unit>()
    private var restoreActive = true
    private var noSessionGeneration = 0L

    private val readinessListener =
        StartupReadinessController.Listener { synchronized(this) { maybeOpenLocked() } }

    init {
        readinessController.addListener(readinessListener)
    }

    suspend fun awaitOpen() = opened.await()

    @Synchronized
    fun onRestoreStarted() {
        if (opened.isCompleted) return
        noSessionGeneration += 1
        restoreActive = true
    }

    @Synchronized
    fun onRestoreFinished() {
        if (opened.isCompleted) return
        noSessionGeneration += 1
        restoreActive = false
        maybeOpenLocked()
    }

    fun onNoSavedSession() {
        val generation =
            synchronized(this) {
                if (opened.isCompleted) return
                ++noSessionGeneration
            }
        scope.launch {
            delay(NO_SAVED_SESSION_GRACE_MS)
            synchronized(this@StartupOptionalWorkGate) {
                if (generation != noSessionGeneration || opened.isCompleted) return@synchronized
                restoreActive = false
                maybeOpenLocked()
            }
        }
    }

    private fun maybeOpenLocked() {
        if (
            !opened.isCompleted &&
                !restoreActive &&
                StartupCapability.QUEUE_READY in readinessController.state.achieved
        ) {
            PerfTimer.point("startup.optional_work_gate_open")
            opened.complete(Unit)
        }
    }

    companion object {
        internal const val NO_SAVED_SESSION_GRACE_MS = 750L
    }
}
