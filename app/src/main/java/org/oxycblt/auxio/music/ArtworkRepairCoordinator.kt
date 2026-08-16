/*
 * Copyright (c) 2026 Auxio Project
 * ArtworkRepairCoordinator.kt is part of Auxio.
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

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.image.CoverMode
import org.oxycblt.auxio.image.ImageSettings
import org.oxycblt.musikr.library.MetadataProfile
import timber.log.Timber as L

/**
 * One-shot compatibility repair for libraries produced before complete artwork enrichment existed.
 *
 * The durable incremental enrichment revision decides whether source work is actually needed. This
 * coordinator supplies the compatibility trigger once a usable full library graph exists, then
 * records the successful migration so later process recreations do not submit another enrichment
 * request. It deliberately waits while album art is OFF so an OFF-mode pass cannot mark artwork
 * repair as complete without creating artwork.
 *
 * Repository construction is deliberately lazy and resolved on an IO scope. Injecting this
 * coordinator into [org.oxycblt.auxio.Auxio] therefore does not eagerly construct the
 * database-backed music graph on the application main-thread startup path.
 */
@Singleton
class ArtworkRepairCoordinator
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val musicRepositoryProvider: Provider<MusicRepository>,
    private val musicSettingsProvider: Provider<MusicSettings>,
    private val imageSettingsProvider: Provider<ImageSettings>,
) :
    MusicRepository.StartupReadinessListener,
    MusicRepository.IndexingListener,
    ImageSettings.Listener {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val preferences by
        lazy(LazyThreadSafetyMode.NONE) { PreferenceManager.getDefaultSharedPreferences(context) }

    private var startScheduled = false
    private var started = false
    private var requested = false
    private var indexingListenerAttached = false
    private var repairRequest: IndexRequest? = null
    private var preexistingSessionId: Long? = null
    private var repairSessionId: Long? = null
    private var musicRepository: MusicRepository? = null
    private var musicSettings: MusicSettings? = null
    private var imageSettings: ImageSettings? = null

    @Synchronized
    fun start() {
        if (startScheduled) return
        startScheduled = true
        if (preferences.getBoolean(KEY_ARTWORK_REPAIR_V2_COMPLETE, false)) {
            L.d("Artwork enrichment compatibility repair already completed; skipping trigger")
            return
        }
        scope.launch {
            // Provider resolution is the potentially expensive part: MusicRepository owns the
            // Room-backed cache graph. Resolve it away from Application.onCreate's main thread.
            val repository = musicRepositoryProvider.get()
            val settings = musicSettingsProvider.get()
            val images = imageSettingsProvider.get()
            withContext(Dispatchers.Main.immediate) { attach(repository, settings, images) }
        }
    }

    @Synchronized
    private fun attach(
        repository: MusicRepository,
        settings: MusicSettings,
        images: ImageSettings,
    ) {
        if (started) return
        musicRepository = repository
        musicSettings = settings
        imageSettings = images
        started = true
        repository.addStartupReadinessListener(this)
        images.registerListener(this)
        maybeRequestRepair()
    }

    override fun onStartupReadinessStateChanged() {
        maybeRequestRepair()
    }

    override fun onImageSettingsChanged() {
        maybeRequestRepair()
    }

    override fun onIndexingStateChanged() {
        val repository = synchronized(this) { musicRepository } ?: return
        when (val state = repository.indexingState) {
            is IndexingState.Indexing -> {
                synchronized(this) {
                    val expected = repairRequest ?: return@synchronized
                    if (
                        repairSessionId == null &&
                            state.sessionId != preexistingSessionId &&
                            state.request == expected &&
                            ArtworkRepairCompletionPolicy.isRepairRequest(state.request)
                    ) {
                        repairSessionId = state.sessionId
                        L.d(
                            "Artwork compatibility repair session started [session=${state.sessionId}]"
                        )
                    }
                }
            }
            is IndexingState.Completed -> {
                val observedSession = synchronized(this) { repairSessionId } ?: return
                // Repository indexing is serialised: completion is dispatched synchronously before
                // IndexingHolder can start the next pending session. Once the submitted repair's
                // session has been observed, this is therefore its terminal state.
                if (ArtworkRepairCompletionPolicy.isSuccessfulCompletion(state.outcome)) {
                    preferences.edit { putBoolean(KEY_ARTWORK_REPAIR_V2_COMPLETE, true) }
                    L.i(
                        "Artwork enrichment compatibility repair completed and was checkpointed " +
                            "[session=$observedSession]"
                    )
                } else {
                    L.w(
                        "Artwork enrichment compatibility repair did not complete " +
                            "[session=$observedSession outcome=${state.outcome}]"
                    )
                }
                detachIndexingListener(repository)
            }
            null -> Unit
        }
    }

    @Synchronized
    private fun maybeRequestRepair() {
        if (!started || requested) return
        if (preferences.getBoolean(KEY_ARTWORK_REPAIR_V2_COMPLETE, false)) {
            detachReadinessListeners()
            return
        }
        val repository = musicRepository ?: return
        val settings = musicSettings ?: return
        val images = imageSettings ?: return
        if (
            !ArtworkRepairPolicy.shouldRequest(
                coverMode = images.coverMode,
                readiness = repository.startupReadinessState,
            )
        ) {
            return
        }
        val request =
            IndexRequest(
                reason = IndexReason.METADATA_ENRICHMENT,
                withCache = true,
                metadataProfile = MetadataProfile.FULL,
                configurationGeneration = settings.sourceConfigurationGeneration,
            )
        requested = true
        repairRequest = request
        repairSessionId = null
        preexistingSessionId = (repository.indexingState as? IndexingState.Indexing)?.sessionId
        if (!indexingListenerAttached) {
            indexingListenerAttached = true
            repository.addIndexingListener(this)
        }
        L.i("Scheduling one-shot artwork enrichment compatibility repair")
        repository.requestIndex(request)
        detachReadinessListeners()
    }

    @Synchronized
    private fun detachReadinessListeners() {
        val repository = musicRepository ?: return
        val images = imageSettings ?: return
        repository.removeStartupReadinessListener(this)
        images.unregisterListener(this)
    }

    @Synchronized
    private fun detachIndexingListener(repository: MusicRepository) {
        if (!indexingListenerAttached) return
        indexingListenerAttached = false
        repository.removeIndexingListener(this)
    }

    private companion object {
        const val KEY_ARTWORK_REPAIR_V2_COMPLETE = "auxio_artwork_repair_v2_complete"
    }
}

internal object ArtworkRepairPolicy {
    fun shouldRequest(coverMode: CoverMode, readiness: StartupReadinessState): Boolean =
        coverMode != CoverMode.OFF && readiness.rank >= StartupReadinessState.FullLibraryReady.rank
}

internal object ArtworkRepairCompletionPolicy {
    fun isRepairRequest(request: IndexRequest?): Boolean =
        request?.reason == IndexReason.METADATA_ENRICHMENT &&
            request.withCache &&
            request.metadataProfile == MetadataProfile.FULL &&
            request.sourceKeys == null

    fun isSuccessfulCompletion(outcome: IndexingTerminalOutcome): Boolean =
        outcome == IndexingTerminalOutcome.SUCCESS
}
