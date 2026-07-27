/*
 * Copyright (c) 2026 Auxio Project
 * GeneratedPlaylistCoordinator.kt is part of Auxio.
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber as L

enum class GeneratedPlaylistStatus {
    OFF,
    WAITING_FOR_LIBRARY,
    GENERATING,
    UP_TO_DATE,
    FAILED,
}

/**
 * Owns optional generated-playlist work independently from source indexing.
 *
 * Requests for an already-published library fingerprint coalesce. A newer library cancels stale
 * work, and disabling the feature removes projections immediately without waiting for startup
 * readiness.
 */
@Singleton
class GeneratedPlaylistCoordinator
@Inject
constructor(private val optionalWorkGate: StartupOptionalWorkGate) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val projectionMutex = Mutex()
    private val mutableStatus = MutableStateFlow(GeneratedPlaylistStatus.OFF)
    private var job: Job? = null
    private var publishedFingerprint: String? = null
    private var activeFingerprint: String? = null
    private var requestGeneration = 0L

    val status: StateFlow<GeneratedPlaylistStatus> = mutableStatus

    @Synchronized
    fun request(
        enabled: Boolean,
        fingerprint: String,
        force: Boolean,
        project: suspend (enabled: Boolean) -> Boolean,
    ) {
        if (enabled && !force && publishedFingerprint == fingerprint) {
            mutableStatus.value = GeneratedPlaylistStatus.UP_TO_DATE
            return
        }
        if (enabled && !force && job?.isActive == true && activeFingerprint == fingerprint) return

        val generation = ++requestGeneration
        if (!enabled) {
            job?.cancel()
            job = null
            activeFingerprint = null
            publishedFingerprint = null
            mutableStatus.value = GeneratedPlaylistStatus.OFF
            job =
                scope.launch {
                    try {
                        projectionMutex.withLock { project(false) }
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        synchronized(this@GeneratedPlaylistCoordinator) {
                            if (generation == requestGeneration) {
                                mutableStatus.value = GeneratedPlaylistStatus.FAILED
                            }
                        }
                        L.w(e, "Unable to remove generated-playlist projections")
                    } finally {
                        synchronized(this@GeneratedPlaylistCoordinator) {
                            if (generation == requestGeneration) job = null
                        }
                    }
                }
            return
        }

        job?.cancel()
        activeFingerprint = fingerprint
        mutableStatus.value = GeneratedPlaylistStatus.WAITING_FOR_LIBRARY
        job =
            scope.launch {
                try {
                    optionalWorkGate.awaitOpen()
                    val current =
                        synchronized(this@GeneratedPlaylistCoordinator) {
                            if (generation != requestGeneration) {
                                false
                            } else {
                                mutableStatus.value = GeneratedPlaylistStatus.GENERATING
                                true
                            }
                        }
                    if (!current) return@launch
                    val projected = projectionMutex.withLock { project(true) }
                    if (projected) {
                        synchronized(this@GeneratedPlaylistCoordinator) {
                            if (generation == requestGeneration) {
                                publishedFingerprint = fingerprint
                            }
                        }
                    }
                    synchronized(this@GeneratedPlaylistCoordinator) {
                        if (generation == requestGeneration) {
                            mutableStatus.value = GeneratedPlaylistStatus.UP_TO_DATE
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    synchronized(this@GeneratedPlaylistCoordinator) {
                        if (generation == requestGeneration) {
                            mutableStatus.value = GeneratedPlaylistStatus.FAILED
                        }
                    }
                    L.w(e, "Generated playlists failed; base library remains available")
                } finally {
                    synchronized(this@GeneratedPlaylistCoordinator) {
                        if (generation == requestGeneration) {
                            job = null
                            activeFingerprint = null
                        }
                    }
                }
            }
    }
}
