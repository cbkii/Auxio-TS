#!/usr/bin/env python3
from pathlib import Path


def replace(path: str, old: str, new: str, count: int = 1) -> None:
    p = Path(path)
    text = p.read_text()
    if old not in text:
        raise SystemExit(f"missing patch anchor in {path}: {old[:180]!r}")
    p.write_text(text.replace(old, new, count))


# Match the actual LibraryDimensionPolicy and ArtworkPolicy contracts already present on dev.
replace(
    'musikr/src/main/java/org/oxycblt/musikr/Config.kt',
    '''        LibraryDimensionPolicy(
            songIdentity = true,
            basicTags = true,
            albums = true,
            artists = true,
            genres = true,
            musicBrainz = true,
            replayGain = true,
            releaseTypes = true,
        ),''',
    '''        LibraryDimensionPolicy(
            genres = true,
            playlists = true,
            detailedCollaborators = true,
            albumArtists = true,
            releaseTypes = true,
            advancedDates = true,
            replayGain = true,
            musicBrainz = true,
        ),''',
)

replace(
    'app/src/main/java/org/oxycblt/auxio/music/DrivingStartupPolicy.kt',
    'ArtworkPolicy.VISIBLE_ONLY',
    'ArtworkPolicy.VISIBLE_ITEMS',
)
replace(
    'app/src/main/java/org/oxycblt/auxio/music/DrivingStartupPolicy.kt',
    '''            LibraryDimensionPolicy(
                songIdentity = true,
                basicTags = true,
                albums = true,
                artists = true,
                genres = false,
                musicBrainz = false,
                replayGain = false,
                releaseTypes = false,
            )''',
    '''            LibraryDimensionPolicy(
                genres = false,
                playlists = true,
                detailedCollaborators = false,
                albumArtists = false,
                releaseTypes = false,
                advancedDates = false,
                replayGain = false,
                musicBrainz = false,
            )''',
)
replace(
    'app/src/main/java/org/oxycblt/auxio/music/DrivingStartupPolicy.kt',
    '''            LibraryDimensionPolicy(
                songIdentity = true,
                basicTags = true,
                albums = true,
                artists = true,
                genres = true,
                musicBrainz = true,
                replayGain = true,
                releaseTypes = true,
            )''',
    '''            LibraryDimensionPolicy(
                genres = true,
                playlists = true,
                detailedCollaborators = true,
                albumArtists = true,
                releaseTypes = true,
                advancedDates = true,
                replayGain = true,
                musicBrainz = true,
            )''',
)
replace(
    'app/src/test/java/org/oxycblt/auxio/music/DrivingStartupPolicyTest.kt',
    'assertTrue(dimensions.songIdentity)\n        assertTrue(dimensions.basicTags)\n',
    'assertTrue(dimensions.playlists)\n        assertFalse(dimensions.detailedCollaborators)\n',
)
replace(
    'app/src/test/java/org/oxycblt/auxio/music/DrivingStartupPolicyTest.kt',
    'ArtworkPolicy.VISIBLE_ONLY',
    'ArtworkPolicy.VISIBLE_ITEMS',
)

# Make parser gates line up with the established policy: basic title/album/first artist remain lean,
# while collaborator expansion, album artists, advanced dates and rich dimensions are optional.
tag = 'musikr/src/main/java/org/oxycblt/musikr/tag/parse/TagParser.kt'
replace(
    tag,
    'LibraryDimensionPolicy(true, true, true, true, true, true, true, true)',
    '''LibraryDimensionPolicy(
                    genres = true,
                    playlists = true,
                    detailedCollaborators = true,
                    albumArtists = true,
                    releaseTypes = true,
                    advancedDates = true,
                    replayGain = true,
                    musicBrainz = true,
                )''',
)
replace(
    tag,
    'if (work.expandMultipleArtists) rawArtistNames else rawArtistNames.take(1)',
    '''if (work.expandMultipleArtists && dimensions.detailedCollaborators) {
                rawArtistNames
            } else {
                rawArtistNames.take(1)
            }''',
)
replace(
    tag,
    'if (work.expandMultipleArtists) rawArtistSortNames else rawArtistSortNames.take(1)',
    '''if (work.expandMultipleArtists && dimensions.detailedCollaborators) {
                rawArtistSortNames
            } else {
                rawArtistSortNames.take(1)
            }''',
)
replace(
    tag,
    'if (work.expandMultipleArtists) rawAlbumArtistNames else rawAlbumArtistNames.take(1)',
    '''if (dimensions.albumArtists) {
                if (work.expandMultipleArtists) rawAlbumArtistNames else rawAlbumArtistNames.take(1)
            } else {
                emptyList()
            }''',
)
replace(tag, 'metadata.date().takeIf { work.readDetailedDates }', 'metadata.date().takeIf { work.readDetailedDates && dimensions.advancedDates }')
replace(tag, 'metadata.albumName().takeIf { dimensions.albums }', 'metadata.albumName()')
replace(tag, 'metadata.albumSortName().takeIf { dimensions.albums }', 'metadata.albumSortName()')
replace(tag, 'artistNames.takeIf { dimensions.artists }.orEmpty()', 'artistNames')
replace(tag, 'artistSortNames.takeIf { dimensions.artists }.orEmpty()', 'artistSortNames')
replace(
    tag,
    '''            albumArtistNames =
                if (dimensions.artists) {
                    albumArtistNames.ifEmpty {
                        listOf("Various Artists").takeIf { compilation }.orEmpty()
                    }
                } else {
                    emptyList()
                },''',
    '''            albumArtistNames =
                if (dimensions.albumArtists) {
                    albumArtistNames.ifEmpty {
                        listOf("Various Artists").takeIf { compilation }.orEmpty()
                    }
                } else {
                    emptyList()
                },''',
)
replace(
    tag,
    '''            albumArtistSortNames =
                if (dimensions.artists) {
                    val values = metadata.albumArtistSortNames().orEmpty()
                    if (work.expandMultipleArtists) values else values.take(1)
                } else {
                    emptyList()
                },''',
    '''            albumArtistSortNames =
                if (dimensions.albumArtists) {
                    val values = metadata.albumArtistSortNames().orEmpty()
                    if (work.expandMultipleArtists) values else values.take(1)
                } else {
                    emptyList()
                },''',
)

# Correct the Context extension receiver in the SAF snapshot probe.
replace(
    'musikr/src/main/java/org/oxycblt/musikr/fs/saf/SAF.kt',
    '''                contentResolverSafe.useQuery(
                    documentUri,''',
    '''                context.contentResolverSafe.useQuery(
                    documentUri,''',
)

# Keep the active plan available when commit fails so abort can remove pending data and mark the
# generation failed. Also clear stale rows left by a killed process before a source restarts.
db = 'musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalDatabase.kt'
replace(
    db,
    '''    @Query("DELETE FROM ScanSeenData WHERE scanId = :scanId") suspend fun deleteSeen(scanId: String)

    @Query("DELETE FROM PendingCachedFileData WHERE scanId = :scanId")
    suspend fun deletePending(scanId: String)''',
    '''    @Query("DELETE FROM ScanSeenData WHERE scanId = :scanId") suspend fun deleteSeen(scanId: String)

    @Query("DELETE FROM ScanSeenData WHERE sourceKey = :sourceKey")
    suspend fun deleteSeenForSource(sourceKey: String)

    @Query("DELETE FROM PendingCachedFileData WHERE scanId = :scanId")
    suspend fun deletePending(scanId: String)

    @Query("DELETE FROM PendingCachedFileData WHERE sourceKey = :sourceKey")
    suspend fun deletePendingForSource(sourceKey: String)''',
)
store = 'musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt'
replace(
    store,
    '''            for (snapshot in plan.scanSources) {
                val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                val generation = (ledger.lastCommittedGeneration ?: 0L) + 1L''',
    '''            for (snapshot in plan.scanSources) {
                val ledger = requireNotNull(dao.sourceLedger(snapshot.sourceKey))
                dao.deletePendingForSource(snapshot.sourceKey)
                dao.deleteSeenForSource(snapshot.sourceKey)
                val generation = (ledger.lastCommittedGeneration ?: 0L) + 1L''',
)
replace(
    store,
    '''        try {
            db.withTransaction {''',
    '''        var committedSuccessfully = false
        try {
            db.withTransaction {''',
    1,
)
# The first occurrence above is commitScan's try (not plan/begin). Assert the exact tail and alter it.
replace(
    store,
    '''                dao.deletePending(plan.scanId)
                dao.deleteSeen(plan.scanId)
            }
        } finally {
            currentPlan = null
        }
        return IncrementalScanCommit(''',
    '''                dao.deletePending(plan.scanId)
                dao.deleteSeen(plan.scanId)
            }
            committedSuccessfully = true
        } finally {
            if (committedSuccessfully) currentPlan = null
        }
        return IncrementalScanCommit(''',
)

# Avoid downgrading a source from Full to Lean when a Lean pass found no changed rows.
replace(
    store,
    '''                    var offset = 0
                    while (true) {
                        val page =
                            dao.pendingPage(plan.scanId, snapshot.sourceKey, PAGE_SIZE, offset)''',
    '''                    val sourceChangedRows = dao.pendingCount(plan.scanId, snapshot.sourceKey)
                    var offset = 0
                    while (true) {
                        val page =
                            dao.pendingPage(plan.scanId, snapshot.sourceKey, PAGE_SIZE, offset)''',
)
replace(
    store,
    '''                            committedProfile = plan.metadataProfile.name,
                            enrichmentRevision =
                                if (plan.metadataProfile == MetadataProfile.FULL) {''',
    '''                            committedProfile =
                                if (
                                    sourceChangedRows == 0 &&
                                        ledger.committedProfile == MetadataProfile.FULL.name
                                ) {
                                    MetadataProfile.FULL.name
                                } else {
                                    plan.metadataProfile.name
                                },
                            enrichmentRevision =
                                if (plan.metadataProfile == MetadataProfile.FULL) {''',
)

# One-shot files must never enter the review diff.
Path('scripts/pr2-hardening-pass.py').unlink()
Path('.github/workflows/pr2-hardening-pass.yml').unlink()
