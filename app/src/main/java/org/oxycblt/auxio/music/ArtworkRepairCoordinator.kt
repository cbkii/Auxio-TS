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
 * The durable incremental enrichment revision decides whether any source work is actually needed.
 * This coordinator only supplies the missing trigger once a usable full library graph exists. It
 * deliberately waits while album art is OFF so an OFF-mode pass cannot mark artwork repair as
 * complete without creating artwork.
 *
 * Repository construction is deliberately lazy and resolved on an IO scope. Injecting this
 * coordinator into [org.oxycblt.auxio.Auxio] therefore does not eagerly construct the
 * database-backed music graph on the application main-thread startup path.
 */
@Singleton
class ArtworkRepairCoordinator
@Inject
constructor(
    private val musicRepositoryProvider: Provider<MusicRepository>,
    private val musicSettingsProvider: Provider<MusicSettings>,
    private val imageSettingsProvider: Provider<ImageSettings>,
) : MusicRepository.StartupReadinessListener, ImageSettings.Listener {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var startScheduled = false
    private var started = false
    private var requested = false
    private var musicRepository: MusicRepository? = null
    private var musicSettings: MusicSettings? = null
    private var imageSettings: ImageSettings? = null

    @Synchronized
    fun start() {
        if (startScheduled) return
        startScheduled = true
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

    @Synchronized
    private fun maybeRequestRepair() {
        if (!started || requested) return
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
        requested = true
        val request =
            IndexRequest(
                reason = IndexReason.METADATA_ENRICHMENT,
                withCache = true,
                metadataProfile = MetadataProfile.FULL,
                configurationGeneration = settings.sourceConfigurationGeneration,
            )
        L.i("Scheduling one-shot artwork enrichment compatibility repair")
        repository.requestIndex(request)
        repository.removeStartupReadinessListener(this)
        images.unregisterListener(this)
    }
}

internal object ArtworkRepairPolicy {
    fun shouldRequest(coverMode: CoverMode, readiness: StartupReadinessState): Boolean =
        coverMode != CoverMode.OFF && readiness.rank >= StartupReadinessState.FullLibraryReady.rank
}
