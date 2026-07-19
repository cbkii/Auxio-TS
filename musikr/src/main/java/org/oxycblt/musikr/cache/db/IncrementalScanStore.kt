/*
 * Copyright (c) 2026 Auxio Project
 * IncrementalScanStore.kt is part of Auxio.
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

import android.net.Uri
import androidx.room.withTransaction
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.oxycblt.musikr.cache.Audio
import org.oxycblt.musikr.cache.CachedFile
import org.oxycblt.musikr.cache.IncrementalCache
import org.oxycblt.musikr.cache.IncrementalScanCommit
import org.oxycblt.musikr.cache.IncrementalScanPlan
import org.oxycblt.musikr.cache.incrementalRank
import org.oxycblt.musikr.fs.AddedMs
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.File
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.SourceSnapshot
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.metadata.Properties
import org.oxycblt.musikr.tag.parse.ParsedTags

/** Room implementation of the source-scoped pending/commit protocol. */
internal class IncrementalScanStore(
    private val db: CacheDatabase,
    private val readDao: CacheReadDao,
    private val writeDao: CacheWriteDao,
    private val dao: IncrementalScanDao,
) : IncrementalCache {
    @Volatile private var currentPlan: IncrementalScanPlan? = null
    private val sourceFailures = ConcurrentHashMap<String, String>()

    override fun activePlan(): IncrementalScanPlan? = currentPlan

    override suspend fun planScan(
        snapshots: List<SourceSnapshot>,
        force: Boolean,
        metadataProfile: MetadataProfile,
        configurationRevision: Long,
    ): IncrementalScanPlan {
        check(currentPlan == null) { "Cannot plan a second scan while one is active" }
        val scanSources = mutableListOf<SourceSnapshot>()
        val reuse = linkedSetOf<String>()
        val unavailable = linkedSetOf<String>()
        val now = System.currentTimeMillis()
        val distinctSnapshots = snapshots.distinctBy { it.sourceKey }
        val currentSourceKeys = distinctSnapshots.mapTo(linkedSetOf()) { it.sourceKey }

        db.withTransaction {
            // A source omitted from the complete configured snapshot set is no longer active.
            // Retain its last-known-good rows for rollback/re-add, but hide them immediately.
            for (ledger in dao.sourceLedgers()) {
                if (ledger.sourceKey !in currentSourceKeys && ledger.available) {
                    dao.upsertSourceLedger(ledger.copy(available = false, lastSeenMs = now))
                    unavailable += ledger.sourceKey
                }
            }
            for (snapshot in distinctSnapshots) {
                val previous = dao.sourceLedger(snapshot.sourceKey)
                val observed =
                    (previous
                            ?: SourceLedgerData(
                                sourceKey = snapshot.sourceKey,
                                sourceType = snapshot.sourceType,
                                rootUri = snapshot.rootUri,
                                rootPath = snapshot.rootPath,
                                fingerprint = null,
                                fingerprintStrength = SourceFingerprintStrength.NONE.name,
                                available = snapshot.available,
                                lastSeenMs = snapshot.observedAtMs,
                                lastCommittedGeneration = null,
                                pendingGeneration = null,
                                lastSuccessfulScanMs = null,
                                configurationRevision = configurationRevision,
                                invalidationVersion = 0L,
                                committedInvalidationVersion = 0L,
                                committedProfile = null,
                                enrichmentRevision = 0L,
                                incomplete = false,
                            ))
                        .observed(snapshot)
                dao.upsertSourceLedger(observed)

                if (!snapshot.available) {
                    unavailable += snapshot.sourceKey
                    continue
                }

                val previousProfile =
                    previous?.committedProfile?.let {
                        runCatching { MetadataProfile.valueOf(it) }.getOrNull()
                    }
                val profileUpgrade =
                    previousProfile == null ||
                        metadataProfile.incrementalRank > previousProfile.incrementalRank
                val fingerprintChanged =
                    previous == null ||
                        previous.fingerprint != snapshot.fingerprint ||
                        previous.fingerprintStrength != snapshot.fingerprintStrength.name
                val invalidated =
                    previous != null &&
                        previous.invalidationVersion > previous.committedInvalidationVersion
                val advisoryExpired =
                    snapshot.fingerprintStrength == SourceFingerprintStrength.ADVISORY &&
                        (previous?.lastSuccessfulScanMs == null ||
                            now - previous.lastSuccessfulScanMs >= ADVISORY_REFRESH_MS)
                val mustScan =
                    force ||
                        previous == null ||
                        previous.lastCommittedGeneration == null ||
                        previous.incomplete ||
                        previous.configurationRevision != configurationRevision ||
                        profileUpgrade ||
                        invalidated ||
                        fingerprintChanged ||
                        advisoryExpired ||
                        snapshot.fingerprintStrength == SourceFingerprintStrength.NONE

                if (mustScan) scanSources += snapshot else reuse += snapshot.sourceKey
            }
        }

        return IncrementalScanPlan(
            scanId = UUID.randomUUID().toString(),
            scanSources = scanSources,
            reuseSourceKeys = reuse,
            unavailableSourceKeys = unavailable,
            metadataProfile = metadataProfile,
            configurationRevision = configurationRevision,
            force = force,
        )
    }

    override suspend fun beginScan(plan: IncrementalScanPlan) {
        check(currentPlan == null) { "An incremental scan is already active" }
        sourceFailures.clear()
        val now = System.currentTimeMillis()
        db.withTransaction {
            for (snapshot in plan.scanSources) {
                val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                dao.deletePendingForSource(snapshot.sourceKey)
                dao.deleteSeenForSource(snapshot.sourceKey)
                val generation = (ledger.lastCommittedGeneration ?: 0L) + 1L
                dao.upsertSourceLedger(
                    ledger.copy(
                        pendingGeneration = generation,
                        configurationRevision = plan.configurationRevision,
                        incomplete = true,
                    )
                )
                dao.upsertGeneration(
                    SourceScanGenerationData(
                        scanId = plan.scanId,
                        sourceKey = snapshot.sourceKey,
                        generation = generation,
                        state = STATE_PENDING,
                        startedAtMs = now,
                        completedAtMs = null,
                        error = null,
                    )
                )
            }
        }
        currentPlan = plan
    }

    override suspend fun markSeen(file: File, cachedFile: CachedFile?) {
        val plan = currentPlan ?: return
        val sourceKey = SourceIdentity.forFile(file)
        if (sourceKey !in plan.scanSourceKeys) return
        val profile =
            dao.uriState(sourceKey, file.uri.toString())?.metadataProfile
                ?: plan.metadataProfile.name
        upsertSeen(plan, sourceKey, file, cachedFile, profile)
    }

    private suspend fun upsertSeen(
        plan: IncrementalScanPlan,
        sourceKey: String,
        file: File,
        cachedFile: CachedFile?,
        profile: String,
        artworkRef: String? = cachedFile?.audio?.coverId,
    ) {
        val cached = cachedFile?.audio
        val tags = cached?.tags
        val fileName = file.path.name ?: file.uri.lastPathSegment ?: file.uri.toString()
        dao.upsertSeen(
            ScanSeenData(
                scanId = plan.scanId,
                sourceKey = sourceKey,
                uri = file.uri.toString(),
                displayPath = file.uri.path ?: file.path.components.unixString,
                fileName = fileName,
                sizeBytes = file.size,
                modifiedTimeMs = file.modifiedMs,
                dateAddedMs = cachedFile?.addedMs ?: 0L,
                mimeType = cached?.properties?.mimeType ?: file.mimeType,
                title = tags?.name ?: fileName,
                titleSort = tags?.sortName ?: tags?.name ?: fileName,
                primaryArtistName = tags?.artistNames?.firstOrNull(),
                primaryArtistSort =
                    tags?.artistSortNames?.firstOrNull() ?: tags?.artistNames?.firstOrNull(),
                albumName = tags?.albumName,
                albumSort = tags?.albumSortName ?: tags?.albumName,
                trackNumber = tags?.track,
                discNumber = tags?.disc,
                durationMs = cached?.properties?.durationMs,
                artworkRef = artworkRef,
                metadataProfile = profile,
            )
        )
    }

    internal suspend fun cachedProfileAccepts(file: File): Boolean {
        val plan = currentPlan ?: return true
        if (plan.force) return false
        val sourceKey = SourceIdentity.forFile(file)
        if (sourceKey !in plan.scanSourceKeys) return true
        val state =
            dao.uriState(sourceKey, file.uri.toString())
                ?: return plan.metadataProfile == MetadataProfile.LEAN
        val cachedProfile =
            runCatching { MetadataProfile.valueOf(state.metadataProfile) }.getOrNull()
        return cachedProfile != null &&
            cachedProfile.incrementalRank >= plan.metadataProfile.incrementalRank
    }

    override suspend fun stage(cachedFile: CachedFile): Boolean {
        val plan = currentPlan ?: return false
        val sourceKey = SourceIdentity.forFile(cachedFile.file)
        if (sourceKey !in plan.scanSourceKeys) return false
        val audio = cachedFile.audio
        val tags = audio?.tags
        val durableCoverId = audio?.coverId ?: readDao.selectSongByUri(cachedFile.file.uri)?.coverId
        dao.upsertPending(
            PendingCachedFileData(
                scanId = plan.scanId,
                sourceKey = sourceKey,
                uri = cachedFile.file.uri.toString(),
                modifiedMs = cachedFile.file.modifiedMs,
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
        )
        upsertSeen(
            plan = plan,
            sourceKey = sourceKey,
            file = cachedFile.file,
            cachedFile = cachedFile,
            profile = plan.metadataProfile.name,
            artworkRef = durableCoverId,
        )
        return true
    }

    override fun reusedCachedFiles(sourceKeys: Set<String>): Flow<CachedFile> = flow {
        if (sourceKeys.isEmpty()) return@flow
        var offset = 0
        while (true) {
            val page = dao.committedCachedPage(sourceKeys, PAGE_SIZE, offset)
            if (page.isEmpty()) break
            for (row in page) emit(row.toCachedFile())
            if (page.size < PAGE_SIZE) break
            offset += page.size
        }
    }

    internal fun compatibilityCachedFiles(): Flow<CachedFile> = flow {
        var offset = 0
        while (true) {
            val page = dao.compatibilityCachedPage(PAGE_SIZE, offset)
            if (page.isEmpty()) break
            for (row in page) emit(row.toCachedFile())
            if (page.size < PAGE_SIZE) break
            offset += page.size
        }
    }

    override suspend fun markSourceFailed(sourceKey: String, detail: String) {
        val plan = currentPlan ?: return
        if (sourceKey in plan.scanSourceKeys) {
            sourceFailures[sourceKey] = detail.take(MAX_ERROR_LENGTH)
        }
    }

    override suspend fun commitScan(): IncrementalScanCommit {
        val plan = requireNotNull(currentPlan) { "No incremental scan is active" }
        var changedRows = 0
        var removedRows = 0
        val committed = linkedSetOf<String>()
        var committedSuccessfully = false
        try {
            db.withTransaction {
                for (snapshot in plan.scanSources) {
                    val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                    val generation = requireNotNull(ledger.pendingGeneration)
                    val sourceFailure = sourceFailures[snapshot.sourceKey]
                    if (sourceFailure != null) {
                        dao.deletePendingForSource(snapshot.sourceKey)
                        dao.deleteSeenForSource(snapshot.sourceKey)
                        dao.upsertSourceLedger(
                            ledger.copy(pendingGeneration = null, incomplete = true)
                        )
                        dao.completeGeneration(
                            plan.scanId,
                            snapshot.sourceKey,
                            STATE_FAILED,
                            System.currentTimeMillis(),
                            sourceFailure,
                        )
                        continue
                    }
                    val sourceChangedRows = dao.pendingCount(plan.scanId, snapshot.sourceKey)
                    var offset = 0
                    while (true) {
                        val page =
                            dao.pendingPage(plan.scanId, snapshot.sourceKey, PAGE_SIZE, offset)
                        if (page.isEmpty()) break
                        writeDao.updateSongs(page.map { it.toCachedFileData() })
                        changedRows += page.size
                        if (page.size < PAGE_SIZE) break
                        offset += page.size
                    }

                    dao.publishSeenSongs(
                        plan.scanId,
                        snapshot.sourceKey,
                        generation,
                        if (plan.metadataProfile == MetadataProfile.FULL) FULL_ENRICHMENT_REVISION
                        else ledger.enrichmentRevision,
                    )
                    dao.publishSeenUriStates(plan.scanId, snapshot.sourceKey, generation)
                    ledger.lastCommittedGeneration?.let { oldGeneration ->
                        dao.tombstoneMissingUris(
                            plan.scanId,
                            snapshot.sourceKey,
                            oldGeneration,
                            generation,
                        )
                        removedRows +=
                            dao.deleteMissingCachedRows(
                                plan.scanId,
                                snapshot.sourceKey,
                                oldGeneration,
                            )
                    }
                    val completedAt = System.currentTimeMillis()
                    dao.upsertSourceLedger(
                        ledger.copy(
                            sourceType = snapshot.sourceType,
                            rootUri = snapshot.rootUri,
                            rootPath = snapshot.rootPath,
                            fingerprint = snapshot.fingerprint,
                            fingerprintStrength = snapshot.fingerprintStrength.name,
                            available = true,
                            lastSeenMs = snapshot.observedAtMs,
                            lastCommittedGeneration = generation,
                            pendingGeneration = null,
                            lastSuccessfulScanMs = completedAt,
                            configurationRevision = plan.configurationRevision,
                            committedInvalidationVersion = ledger.invalidationVersion,
                            committedProfile =
                                if (
                                    sourceChangedRows == 0 &&
                                        ledger.committedProfile == MetadataProfile.FULL.name
                                ) {
                                    MetadataProfile.FULL.name
                                } else {
                                    plan.metadataProfile.name
                                },
                            enrichmentRevision =
                                if (plan.metadataProfile == MetadataProfile.FULL) {
                                    FULL_ENRICHMENT_REVISION
                                } else {
                                    ledger.enrichmentRevision
                                },
                            incomplete = false,
                        )
                    )
                    dao.completeGeneration(
                        plan.scanId,
                        snapshot.sourceKey,
                        STATE_COMMITTED,
                        completedAt,
                        null,
                    )
                    dao.deleteOlderIndexedRows(snapshot.sourceKey, generation)
                    committed += snapshot.sourceKey
                }
                dao.deletePending(plan.scanId)
                dao.deleteSeen(plan.scanId)
            }
            committedSuccessfully = true
        } finally {
            if (committedSuccessfully) {
                currentPlan = null
            }
        }
        val failed = sourceFailures.toMap()
        sourceFailures.clear()
        return IncrementalScanCommit(
            scanId = plan.scanId,
            committedSources = committed,
            reusedSources = plan.reuseSourceKeys,
            unavailableSources = plan.unavailableSourceKeys,
            failedSources = failed,
            changedRows = changedRows,
            removedRows = removedRows,
            metadataProfile = plan.metadataProfile,
        )
    }

    override suspend fun abortScan(cause: Throwable?) {
        val plan = currentPlan ?: return
        try {
            db.withTransaction {
                for (snapshot in plan.scanSources) {
                    val ledger = dao.sourceLedger(snapshot.sourceKey) ?: continue
                    dao.upsertSourceLedger(ledger.copy(pendingGeneration = null, incomplete = true))
                    dao.completeGeneration(
                        plan.scanId,
                        snapshot.sourceKey,
                        if (cause is CancellationException) STATE_CANCELLED else STATE_FAILED,
                        System.currentTimeMillis(),
                        cause?.message?.take(MAX_ERROR_LENGTH),
                    )
                }
                dao.deletePending(plan.scanId)
                dao.deleteSeen(plan.scanId)
            }
        } finally {
            currentPlan = null
            sourceFailures.clear()
        }
    }

    override suspend fun invalidateSource(sourceKey: String?) {
        if (sourceKey == null) dao.invalidateAllSources() else dao.invalidateSource(sourceKey)
    }

    private fun PendingCachedFileData.toCachedFileData() =
        CachedFileData(
            uri = Uri.parse(uri),
            modifiedMs = modifiedMs,
            addedMs = addedMs,
            mimeType = mimeType,
            durationMs = durationMs,
            bitrateKbps = bitrateKbps,
            sampleRateHz = sampleRateHz,
            musicBrainzId = musicBrainzId,
            name = name,
            sortName = sortName,
            track = track,
            disc = disc,
            subtitle = subtitle,
            date = date,
            albumMusicBrainzId = albumMusicBrainzId,
            albumName = albumName,
            albumSortName = albumSortName,
            releaseTypes = releaseTypes,
            artistMusicBrainzIds = artistMusicBrainzIds,
            artistNames = artistNames,
            artistSortNames = artistSortNames,
            albumArtistMusicBrainzIds = albumArtistMusicBrainzIds,
            albumArtistNames = albumArtistNames,
            albumArtistSortNames = albumArtistSortNames,
            genreNames = genreNames,
            replayGainTrackAdjustment = replayGainTrackAdjustment,
            replayGainAlbumAdjustment = replayGainAlbumAdjustment,
            coverId = coverId,
        )

    private fun CommittedCachedRow.toCachedFile(): CachedFile {
        val uri = cache.uri
        val path =
            Path(
                Volume.ThirdParty(
                    committedSourceUri?.let(Uri::parse) ?: Uri.parse("source://${sourceKey}")
                ),
                committedDisplayPath
                    ?.substringAfter(committedRootPath.orEmpty())
                    ?.let(Components::parseUnix) ?: Components.root(),
            )
        val file =
            File(
                uri = uri,
                path = path,
                addedMs = FixedAddedMs(cache.addedMs),
                modifiedMs = cache.modifiedMs,
                mimeType = cache.mimeType ?: "application/octet-stream",
                size = committedSizeBytes,
                parent = null,
            )
        val audio =
            cache.durationMs?.let { duration ->
                Audio(
                    Properties(
                        cache.mimeType ?: "application/octet-stream",
                        duration,
                        cache.bitrateKbps ?: 0,
                        cache.sampleRateHz ?: 0,
                    ),
                    ParsedTags(
                        durationMs = duration,
                        replayGainTrackAdjustment = cache.replayGainTrackAdjustment,
                        replayGainAlbumAdjustment = cache.replayGainAlbumAdjustment,
                        musicBrainzId = cache.musicBrainzId,
                        name = cache.name,
                        sortName = cache.sortName,
                        track = cache.track,
                        disc = cache.disc,
                        subtitle = cache.subtitle,
                        date = cache.date,
                        albumMusicBrainzId = cache.albumMusicBrainzId,
                        albumName = cache.albumName,
                        albumSortName = cache.albumSortName,
                        releaseTypes = cache.releaseTypes.orEmpty(),
                        artistMusicBrainzIds = cache.artistMusicBrainzIds.orEmpty(),
                        artistNames = cache.artistNames.orEmpty(),
                        artistSortNames = cache.artistSortNames.orEmpty(),
                        albumArtistMusicBrainzIds = cache.albumArtistMusicBrainzIds.orEmpty(),
                        albumArtistNames = cache.albumArtistNames.orEmpty(),
                        albumArtistSortNames = cache.albumArtistSortNames.orEmpty(),
                        genreNames = cache.genreNames.orEmpty(),
                    ),
                    cache.coverId,
                )
            }
        return CachedFile(file, audio, cache.addedMs)
    }

    private class FixedAddedMs(private val value: Long) : AddedMs {
        override suspend fun resolve(): Long = value
    }

    companion object {
        private const val PAGE_SIZE = 256
        private const val ADVISORY_REFRESH_MS = 6 * 60 * 60 * 1000L
        private const val FULL_ENRICHMENT_REVISION = 1L
        private const val MAX_ERROR_LENGTH = 512
        private const val STATE_PENDING = "PENDING"
        private const val STATE_COMMITTED = "COMMITTED"
        private const val STATE_CANCELLED = "CANCELLED"
        private const val STATE_FAILED = "FAILED"
    }
}
