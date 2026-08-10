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
import javax.inject.Singleton
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
 */
@Singleton
class ArtworkRepairCoordinator
@Inject
constructor(
    private val musicRepository: MusicRepository,
    private val musicSettings: MusicSettings,
    private val imageSettings: ImageSettings,
) : MusicRepository.StartupReadinessListener, ImageSettings.Listener {
    private var started = false
    private var requested = false

    @Synchronized
    fun start() {
        if (started) return
        started = true
        musicRepository.addStartupReadinessListener(this)
        imageSettings.registerListener(this)
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
        if (
            !ArtworkRepairPolicy.shouldRequest(
                coverMode = imageSettings.coverMode,
                readiness = musicRepository.startupReadinessState,
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
                configurationGeneration = musicSettings.sourceConfigurationGeneration,
            )
        L.i("Scheduling one-shot artwork enrichment compatibility repair")
        musicRepository.requestIndex(request)
        musicRepository.removeStartupReadinessListener(this)
        imageSettings.unregisterListener(this)
    }
}

internal object ArtworkRepairPolicy {
    fun shouldRequest(coverMode: CoverMode, readiness: StartupReadinessState): Boolean =
        coverMode != CoverMode.OFF && readiness.rank >= StartupReadinessState.FullLibraryReady.rank
}
