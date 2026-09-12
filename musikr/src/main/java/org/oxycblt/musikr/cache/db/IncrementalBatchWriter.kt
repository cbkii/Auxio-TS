/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalBatchWriter.kt is part of Auxio.
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

package org.oxycblt.musikr.cache.db

import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.fs.SourceIdentity

/** Bulk persistence equivalent of [IncrementalScanStore.stage] for bounded cache-write batches. */
internal class IncrementalBatchWriter(db: CacheDatabase) {
    private val readDao = db.readDao()
    private val writeDao = db.writeDao()
    private val dao = db.incrementalDao()

    /**
     * Stage every in-scope row with one Room list insert per pending/seen table.
     *
     * Rows outside the active plan are returned for legacy compatibility persistence. During an
     * enrichment-only scan, newly observed rows are consumed without leaking them into the legacy
     * cache, matching [IncrementalScanStore.stage].
     */
    suspend fun stageAll(
        plan: IncrementalScanPlan,
        cachedFiles: List<CachedFile>,
    ): List<CachedFile> {
        if (cachedFiles.isEmpty()) return emptyList()
        val pendingRows = ArrayList<PendingCachedFileData>(cachedFiles.size)
        val seenRows = ArrayList<ScanSeenData>(cachedFiles.size)
        val legacyFallback = ArrayList<CachedFile>()

        for (cachedFile in cachedFiles) {
            val file = cachedFile.file
            val sourceKey = SourceIdentity.forFile(file)
            if (sourceKey !in plan.scanSourceKeys) {
                legacyFallback += cachedFile
                continue
            }
            if (
                plan.enrichmentOnly &&
                    dao.uriState(sourceKey, file.uri.toString())?.available != true
            ) {
                continue
            }

            val audio = cachedFile.audio
            val tags = audio?.tags
            val durableCoverId = audio?.coverId ?: readDao.selectSongByUri(file.uri)?.coverId
            pendingRows +=
                PendingCachedFileData(
                    scanId = plan.scanId,
                    sourceKey = sourceKey,
                    uri = file.uri.toString(),
                    modifiedMs = file.modifiedMs,
                    addedMs = cachedFile.addedMs,
                    mimeType = audio?.properties?.mimeType,
                    durationMs = audio?.properties?.durationMs,
                    bitrateKbps = audio?.properties?.bitrateKbps,
                    sampleRateHz = audio?.properties?.sampleRateHz,
                    musicBrainzId = tags?.musicBrainzId,
                    name = tags?.name,
                    sortName = tags?.sortName,
                    track = tags?.track,
                    disc = tags?.disc,
                    subtitle = tags?.subtitle,
                    date = tags?.date,
                    albumMusicBrainzId = tags?.albumMusicBrainzId,
                    albumName = tags?.albumName,
                    albumSortName = tags?.albumSortName,
                    releaseTypes = tags?.releaseTypes,
                    artistMusicBrainzIds = tags?.artistMusicBrainzIds,
                    artistNames = tags?.artistNames,
                    artistSortNames = tags?.artistSortNames,
                    albumArtistMusicBrainzIds = tags?.albumArtistMusicBrainzIds,
                    albumArtistNames = tags?.albumArtistNames,
                    albumArtistSortNames = tags?.albumArtistSortNames,
                    genreNames = tags?.genreNames,
                    replayGainTrackAdjustment = tags?.replayGainTrackAdjustment,
                    replayGainAlbumAdjustment = tags?.replayGainAlbumAdjustment,
                    coverId = durableCoverId,
                )

            val fileName = file.path.name ?: file.uri.lastPathSegment ?: file.uri.toString()
            seenRows +=
                ScanSeenData(
                    scanId = plan.scanId,
                    sourceKey = sourceKey,
                    uri = file.uri.toString(),
                    displayPath = file.uri.path ?: file.path.components.unixString,
                    fileName = fileName,
                    sizeBytes = file.size,
                    modifiedTimeMs = file.modifiedMs,
                    dateAddedMs = cachedFile.addedMs,
                    mimeType = audio?.properties?.mimeType ?: file.mimeType,
                    title = tags?.name ?: fileName,
                    titleSort = tags?.sortName ?: tags?.name ?: fileName,
                    primaryArtistName = tags?.artistNames?.firstOrNull(),
                    primaryArtistSort =
                        tags?.artistSortNames?.firstOrNull() ?: tags?.artistNames?.firstOrNull(),
                    albumName = tags?.albumName,
                    albumSort = tags?.albumSortName ?: tags?.albumName,
                    trackNumber = tags?.track,
                    discNumber = tags?.disc,
                    durationMs = audio?.properties?.durationMs,
                    artworkRef = durableCoverId,
                    metadataProfile = plan.metadataProfile.name,
                )
        }

        if (pendingRows.isNotEmpty()) dao.upsertPendingRows(pendingRows)
        if (seenRows.isNotEmpty()) dao.upsertSeenRows(seenRows)
        return legacyFallback
    }

    suspend fun writeLegacyAll(cachedFiles: List<CachedFile>) {
        if (cachedFiles.isNotEmpty()) {
            writeDao.updateSongs(cachedFiles.map { it.toCachedFileData() })
        }
    }

    private fun CachedFile.toCachedFileData() =
        CachedFileData(
            uri = file.uri,
            modifiedMs = file.modifiedMs,
            addedMs = addedMs,
            mimeType = audio?.properties?.mimeType,
            durationMs = audio?.properties?.durationMs,
            bitrateKbps = audio?.properties?.bitrateKbps,
            sampleRateHz = audio?.properties?.sampleRateHz,
            musicBrainzId = audio?.tags?.musicBrainzId,
            name = audio?.tags?.name,
            sortName = audio?.tags?.sortName,
            track = audio?.tags?.track,
            disc = audio?.tags?.disc,
            subtitle = audio?.tags?.subtitle,
            date = audio?.tags?.date,
            albumMusicBrainzId = audio?.tags?.albumMusicBrainzId,
            albumName = audio?.tags?.albumName,
            albumSortName = audio?.tags?.albumSortName,
            releaseTypes = audio?.tags?.releaseTypes,
            artistMusicBrainzIds = audio?.tags?.artistMusicBrainzIds,
            artistNames = audio?.tags?.artistNames,
            artistSortNames = audio?.tags?.artistSortNames,
            albumArtistMusicBrainzIds = audio?.tags?.albumArtistMusicBrainzIds,
            albumArtistNames = audio?.tags?.albumArtistNames,
            albumArtistSortNames = audio?.tags?.albumArtistSortNames,
            genreNames = audio?.tags?.genreNames,
            replayGainTrackAdjustment = audio?.tags?.replayGainTrackAdjustment,
            replayGainAlbumAdjustment = audio?.tags?.replayGainAlbumAdjustment,
            coverId = audio?.coverId,
        )
}
