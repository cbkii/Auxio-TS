#!/usr/bin/env python3
from pathlib import Path
import re


def replace(path: str, old: str, new: str, count: int = 1) -> None:
    p = Path(path)
    text = p.read_text()
    if old not in text:
        raise SystemExit(f"missing patch anchor in {path}: {old[:220]!r}")
    p.write_text(text.replace(old, new, count))


def sub(path: str, pattern: str, replacement: str) -> None:
    p = Path(path)
    text = p.read_text()
    updated, count = re.subn(pattern, replacement, text, flags=re.S)
    if count != 1:
        raise SystemExit(f"expected one match in {path}, got {count}: {pattern[:180]!r}")
    p.write_text(updated)


# A delayed ViewPager callback must not index a tab list that was reconfigured in the meantime.
replace(
    'app/src/main/java/org/oxycblt/auxio/home/HomeViewModel.kt',
    '        val next = currentTabTypes[pagerPos]\n',
    '        val next = currentTabTypes.getOrNull(pagerPos) ?: return\n',
)

# Repository orchestration stays vendor-neutral. Existing TS18 diagnostics belong behind the
# dedicated compatibility adapters rather than probing vendor properties here.
replace(
    'app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt',
    '''            if (BuildConfig.TOPWAY_COMPAT_FLAVOR) {
                val twStorageSwitch = readTwStorageSwitch()
                if (!twStorageSwitch.isNullOrEmpty()) {
                    L.d("TS18 diagnostic: persist.tw.storage.switch=$twStorageSwitch")
                }
            }

''',
    '',
)

# Observation updates availability and root metadata only. The fingerprint is a committed scan
# fact and must not advance before the generation transaction succeeds.
replace(
    'musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalDatabase.kt',
    '''            rootUri = snapshot.rootUri,
            rootPath = snapshot.rootPath,
            fingerprint = snapshot.fingerprint,
            fingerprintStrength = snapshot.fingerprintStrength.name,
            available = snapshot.available,''',
    '''            rootUri = snapshot.rootUri,
            rootPath = snapshot.rootPath,
            available = snapshot.available,''',
)

# Cache misses are not valid songs. Successful extraction/staging is the only path that publishes a
# previously unknown file into ScanSeenData.
replace(
    'musikr/src/main/java/org/oxycblt/musikr/cache/db/DBCache.kt',
    '''        if (dbSong == null) {
            incrementalStore.markSeen(file)
            return CacheResult.Miss(file)
        }''',
    '''        if (dbSong == null) {
            return CacheResult.Miss(file)
        }''',
)

# Make profile-derived defaults coherent even for callers that construct Config directly.
policy = 'musikr/src/main/java/org/oxycblt/musikr/library/MetadataWorkPolicy.kt'
replace(
    policy,
    '''data class MetadataWorkPolicy(
''',
    '''fun MetadataProfile.defaultDimensionPolicy(): LibraryDimensionPolicy =
    when (this) {
        MetadataProfile.LEAN ->
            LibraryDimensionPolicy(
                genres = false,
                playlists = true,
                detailedCollaborators = false,
                albumArtists = false,
                releaseTypes = false,
                advancedDates = false,
                replayGain = false,
                musicBrainz = false,
            )
        MetadataProfile.FULL ->
            LibraryDimensionPolicy(
                genres = true,
                playlists = true,
                detailedCollaborators = true,
                albumArtists = true,
                releaseTypes = true,
                advancedDates = true,
                replayGain = true,
                musicBrainz = true,
            )
    }

fun MetadataProfile.defaultArtworkPolicy(): ArtworkPolicy =
    when (this) {
        MetadataProfile.LEAN -> ArtworkPolicy.VISIBLE_ITEMS
        MetadataProfile.FULL -> ArtworkPolicy.FULL_INDEXING
    }

data class MetadataWorkPolicy(
''',
)
config = 'musikr/src/main/java/org/oxycblt/musikr/Config.kt'
replace(
    config,
    'import org.oxycblt.musikr.library.MetadataProfile\n',
    '''import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.library.defaultArtworkPolicy
import org.oxycblt.musikr.library.defaultDimensionPolicy
''',
)
sub(
    config,
    r'''    val metadataProfile: MetadataProfile = MetadataProfile\.FULL,
    val dimensionPolicy: LibraryDimensionPolicy =
        LibraryDimensionPolicy\(.*?\),
    val artworkPolicy: ArtworkPolicy = ArtworkPolicy\.FULL_INDEXING,''',
    '''    val metadataProfile: MetadataProfile = MetadataProfile.FULL,
    val dimensionPolicy: LibraryDimensionPolicy = metadataProfile.defaultDimensionPolicy(),
    val artworkPolicy: ArtworkPolicy = metadataProfile.defaultArtworkPolicy(),''',
)
driving = 'app/src/main/java/org/oxycblt/auxio/music/DrivingStartupPolicy.kt'
replace(
    driving,
    'import org.oxycblt.musikr.library.MetadataProfile\n',
    '''import org.oxycblt.musikr.library.MetadataProfile
import org.oxycblt.musikr.library.defaultArtworkPolicy
import org.oxycblt.musikr.library.defaultDimensionPolicy
''',
)
sub(
    driving,
    r'''    fun artworkPolicy\(profile: MetadataProfile\): ArtworkPolicy =.*?

    fun dimensions\(profile: MetadataProfile\): LibraryDimensionPolicy =.*?

    fun shouldDeferFullEnrichment''',
    '''    fun artworkPolicy(profile: MetadataProfile): ArtworkPolicy =
        profile.defaultArtworkPolicy()

    fun dimensions(profile: MetadataProfile): LibraryDimensionPolicy =
        profile.defaultDimensionPolicy()

    fun shouldDeferFullEnrichment''',
)

# DirectFS source keys are volume-scoped, so every configured root sharing that key contributes to
# the snapshot. Empty readable folders remain available; one unreadable root makes the source
# unavailable and prevents an empty generation from committing.
direct = 'musikr/src/main/java/org/oxycblt/musikr/fs/direct/DirectFS.kt'
sub(
    direct,
    r'''    override suspend fun sourceSnapshots\(\): List<SourceSnapshot> =
        kotlinx\.coroutines\.withContext\(Dispatchers\.IO\) \{.*?
        \}

    override fun selectSources''',
    '''    override suspend fun sourceSnapshots(): List<SourceSnapshot> =
        kotlinx.coroutines.withContext(Dispatchers.IO) {
            roots.groupBy(SourceIdentity::forLocation).map { (sourceKey, locations) ->
                val evaluated =
                    locations.map { location ->
                        val root = location.uri.path?.let(::JavaFile)
                        val allowed = root != null && isAllowedRoot(root)
                        val readable = allowed && listFilesSafe(requireNotNull(root)) != null
                        RootSnapshot(location, root, readable)
                    }
                val available = evaluated.isNotEmpty() && evaluated.all { it.readable }
                SourceSnapshot(
                    sourceKey = sourceKey,
                    sourceType = SOURCE_TYPE,
                    // A source key may cover more than one configured folder. The first path is
                    // display metadata only; the combined fingerprint below covers every root.
                    rootUri = locations.firstOrNull()?.uri?.toString(),
                    rootPath = evaluated.firstOrNull()?.root?.absolutePath,
                    available = available,
                    fingerprint =
                        if (available) {
                            combineRootFingerprints(
                                evaluated.map { requireNotNull(it.root) to it.location }
                            )
                        } else {
                            null
                        },
                    fingerprintStrength =
                        if (available) SourceFingerprintStrength.ADVISORY
                        else SourceFingerprintStrength.NONE,
                )
            }
        }

    override fun selectSources''',
)
replace(
    direct,
    '''    private data class DirectEntry(
''',
    '''    private data class RootSnapshot(
        val location: Location.Opened,
        val root: JavaFile?,
        val readable: Boolean,
    )

    private data class DirectEntry(
''',
)
replace(
    direct,
    '''        update(root.absolutePath)
        update("|${root.lastModified()}|${root.length()}|")
''',
    '''        update(root.absolutePath)
        update("|${root.lastModified()}|${root.length()}|")
''',
)
replace(
    direct,
    '''            .filterNot { it.name.startsWith(".") || it.isSymlink }
''',
    '''            .filterNot { it.isSymlink }
''',
)
replace(
    direct,
    '''            for (entry in entries) {
                if (entry.name.startsWith(".") || entry.isSymlink) continue
''',
    '''            for (entry in entries) {
                if (entry.isSymlink) continue
''',
)
replace(
    direct,
    '''    private fun boundedFingerprint(root: JavaFile): String {
''',
    '''    private fun combineRootFingerprints(
        roots: List<Pair<JavaFile, Location.Opened>>
    ): String {
        val digest = MessageDigest.getInstance("SHA-256")
        roots.sortedBy { it.first.absolutePath }.forEach { (root, location) ->
            digest.update(location.uri.toString().toByteArray(Charsets.UTF_8))
            digest.update(0)
            digest.update(boundedFingerprint(root).toByteArray(Charsets.UTF_8))
            digest.update(0)
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun boundedFingerprint(root: JavaFile): String {
''',
)

# Cleanup must complete even when the indexing coroutine has already been cancelled. The original
# failure remains authoritative and any abort failure is attached as suppressed evidence.
musikr = 'musikr/src/main/java/org/oxycblt/musikr/Musikr.kt'
replace(
    musikr,
    'import kotlinx.coroutines.Dispatchers\n',
    'import kotlinx.coroutines.Dispatchers\nimport kotlinx.coroutines.NonCancellable\n',
)
replace(
    musikr,
    '''        } catch (e: CancellationException) {
            if (plan != null) incremental?.abortScan(e)
            throw e
        } catch (e: Throwable) {
            if (plan != null) incremental?.abortScan(e)
            throw e
        }
''',
    '''        } catch (e: CancellationException) {
            abortIncremental(plan, incremental, e)
            throw e
        } catch (e: Throwable) {
            abortIncremental(plan, incremental, e)
            throw e
        }
''',
)
replace(
    musikr,
    '''    }
}

private class LibraryResultImpl''',
    '''    }

    private suspend fun abortIncremental(
        plan: org.oxycblt.musikr.cache.IncrementalScanPlan?,
        incremental: IncrementalCache?,
        original: Throwable,
    ) {
        if (plan == null || incremental == null) return
        try {
            withContext(NonCancellable) { incremental.abortScan(original) }
        } catch (abortFailure: Throwable) {
            original.addSuppressed(abortFailure)
        }
    }
}

private class LibraryResultImpl''',
)

# Regression tests for committed-fingerprint semantics and profile-derived defaults.
test = 'musikr/src/test/java/org/oxycblt/musikr/cache/db/IncrementalScanStoreTest.kt'
replace(
    test,
    '''    @Test
    fun `changed file publishes a new generation atomically`() = runBlocking {''',
    '''    @Test
    fun `planning never advances committed fingerprint before commit`() = runBlocking {
        val original = snapshot("v1")
        val first = store.planScan(listOf(original), false, MetadataProfile.LEAN, 1L)
        store.beginScan(first)
        store.stage(cachedFile("alpha.mp3", modifiedMs = 1L))
        store.commitScan()

        val candidate = original.copy(fingerprint = "v2")
        assertTrue(store.planScan(listOf(candidate), false, MetadataProfile.LEAN, 1L).hasWork)
        assertEquals("v1", db.incrementalDao().sourceLedger(original.sourceKey)?.fingerprint)
        assertTrue(store.planScan(listOf(candidate), false, MetadataProfile.LEAN, 1L).hasWork)
    }

    @Test
    fun `changed file publishes a new generation atomically`() = runBlocking {''',
)
metadata_test = 'musikr/src/test/java/org/oxycblt/musikr/library/MetadataWorkPolicyTest.kt'
replace(
    metadata_test,
    'import org.junit.Assert.assertTrue\n',
    'import org.junit.Assert.assertEquals\nimport org.junit.Assert.assertTrue\n',
)
replace(
    metadata_test,
    '''    @Test
    fun `full profile enables rich enrichment dimensions`() {''',
    '''    @Test
    fun `lean profile defaults cannot request full dimensions or artwork`() {
        val dimensions = MetadataProfile.LEAN.defaultDimensionPolicy()
        assertFalse(dimensions.genres)
        assertFalse(dimensions.detailedCollaborators)
        assertFalse(dimensions.albumArtists)
        assertFalse(dimensions.replayGain)
        assertFalse(dimensions.musicBrainz)
        assertEquals(ArtworkPolicy.VISIBLE_ITEMS, MetadataProfile.LEAN.defaultArtworkPolicy())
    }

    @Test
    fun `full profile enables rich enrichment dimensions`() {''',
)

# Temporary execution files must not enter the review diff.
Path('scripts/pr2-review-hardening.py').unlink()
Path('.github/workflows/pr2-review-hardening.yml').unlink()
