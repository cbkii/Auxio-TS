from pathlib import Path
import re


def replace_once(path: str, old: str, new: str) -> None:
    target = Path(path)
    text = target.read_text()
    if old not in text:
        raise SystemExit(f"missing expected block in {path}: {old[:120]!r}")
    target.write_text(text.replace(old, new, 1))


# Room entity, migration and stage/commit code must use one flattened durable schema.
db = Path("musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalDatabase.kt")
text = db.read_text()
pending = '''internal data class PendingCachedFileData(
    val scanId: String,
    val sourceKey: String,
    val uri: String,
    val modifiedMs: Long,
    val addedMs: Long,
    val mimeType: String?,
    val durationMs: Long?,
    val bitrateKbps: Int?,
    val sampleRateHz: Int?,
    val musicBrainzId: String?,
    val name: String?,
    val sortName: String?,
    val track: Int?,
    val disc: Int?,
    val subtitle: String?,
    val date: org.oxycblt.musikr.tag.Date?,
    val albumMusicBrainzId: String?,
    val albumName: String?,
    val albumSortName: String?,
    val releaseTypes: List<String>?,
    val artistMusicBrainzIds: List<String>?,
    val artistNames: List<String>?,
    val artistSortNames: List<String>?,
    val albumArtistMusicBrainzIds: List<String>?,
    val albumArtistNames: List<String>?,
    val albumArtistSortNames: List<String>?,
    val genreNames: List<String>?,
    val replayGainTrackAdjustment: Float?,
    val replayGainAlbumAdjustment: Float?,
    val coverId: String?,
)'''
text, count = re.subn(
    r"internal data class PendingCachedFileData\(.*?\n\)",
    pending,
    text,
    count=1,
    flags=re.S,
)
if count != 1:
    raise SystemExit(f"PendingCachedFileData replacements={count}")
db.write_text(text)

store = Path("musikr/src/main/java/org/oxycblt/musikr/cache/db/IncrementalScanStore.kt")
text = store.read_text()
marker = "    private fun CommittedCachedRow.toCachedFile(): CachedFile {"
conversion = '''    private fun PendingCachedFileData.toCachedFileData() =
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

'''
if marker not in text:
    raise SystemExit("CommittedCachedRow conversion marker not found")
if "private fun PendingCachedFileData.toCachedFileData()" not in text:
    text = text.replace(marker, conversion + marker, 1)

# A metadata-only pass must preserve an existing durable cover reference.
old = '''        cachedFile: CachedFile?,
        profile: String,
    ) {'''
new = '''        cachedFile: CachedFile?,
        profile: String,
        artworkRef: String? = cachedFile?.audio?.coverId,
    ) {'''
if old in text:
    text = text.replace(old, new, 1)
text = text.replace("                artworkRef = cached?.coverId,", "                artworkRef = artworkRef,", 1)
old = '''        val audio = cachedFile.audio
        val tags = audio?.tags
        dao.upsertPending('''
new = '''        val audio = cachedFile.audio
        val tags = audio?.tags
        val durableCoverId =
            audio?.coverId ?: readDao.selectSongByUri(cachedFile.file.uri)?.coverId
        dao.upsertPending('''
if old in text:
    text = text.replace(old, new, 1)
text = text.replace("                coverId = audio?.coverId,", "                coverId = durableCoverId,", 1)
old = '''            cachedFile = cachedFile,
            profile = plan.metadataProfile.name,
        )'''
new = '''            cachedFile = cachedFile,
            profile = plan.metadataProfile.name,
            artworkRef = durableCoverId,
        )'''
if old in text:
    text = text.replace(old, new, 1)
store.write_text(text)

# Indexing stores metadata and references; image extraction stays demand-driven.
replace_once(
    "musikr/src/main/java/org/oxycblt/musikr/library/MetadataWorkPolicy.kt",
    "        MetadataProfile.FULL -> ArtworkPolicy.FULL_INDEXING",
    "        MetadataProfile.FULL -> ArtworkPolicy.VISIBLE_ITEMS",
)

# Attach observers early but debounce notifications before scan planning.
holder = Path("app/src/main/java/org/oxycblt/auxio/music/service/IndexingHolder.kt")
text = holder.read_text()
if "import kotlinx.coroutines.delay" not in text:
    text = text.replace("import kotlinx.coroutines.Job\n", "import kotlinx.coroutines.Job\nimport kotlinx.coroutines.delay\n", 1)
if "private val observationBurstGate" not in text:
    text = text.replace(
        "    private var trackingJob: Job? = null\n",
        "    private var trackingJob: Job? = null\n    private var observationRequestJob: Job? = null\n    private val observationBurstGate = ObservationBurstGate()\n",
        1,
    )
text = text.replace(
    '''        playbackManager.addListener(this)
        // Delay storage tracking until the cached library is emitted (or first index completes).
        // On TS18 firmware, SAF/MediaStore tracking setup can trigger slow provider queries that
        // compete with the cached startup path. Tracking will begin once onMusicChanges fires.
''',
    '''        playbackManager.addListener(this)
        // Observer attachment is cheap: it registers notifications only. Provider enumeration and
        // extraction remain planner-controlled and notification bursts are conflated below.
        if (musicSettings.shouldBeObserving) startTracking()
''',
    1,
)
text = text.replace(
    '''        stopTracking()
        currentIndexJob?.cancel()
''',
    '''        stopTracking()
        observationRequestJob?.cancel()
        observationRequestJob = null
        currentIndexJob?.cancel()
''',
    1,
)
text = text.replace(
    '''    override fun onMusicChanges(changes: MusicRepository.Changes) {
        if (musicRepository.library == null) return
        L.d("Music changed [device=${changes.deviceLibrary}, user=${changes.userLibrary}]")
        if (musicSettings.shouldBeObserving && trackingJob == null) {
            startTracking()
        }
''',
    '''    override fun onMusicChanges(changes: MusicRepository.Changes) {
        L.d("Music changed [device=${changes.deviceLibrary}, user=${changes.userLibrary}]")
        if (musicSettings.shouldBeObserving && trackingJob == null) startTracking()
''',
    1,
)
text = text.replace(
    '''                    if (musicRepository.library == null) {
                        L.i("Ignoring storage change before cached/startup library is available")
                    } else {
                        L.i("Storage change observed; refreshing library with cache")
                        requestIndex(true)
                    }
''',
    '''                    val token = observationBurstGate.nextToken()
                    observationRequestJob?.cancel()
                    observationRequestJob =
                        indexScope.launch {
                            delay(OBSERVATION_DEBOUNCE_MS)
                            if (observationBurstGate.isLatest(token)) {
                                L.i("Storage notification burst settled; planning cached refresh")
                                requestIndex(true)
                            }
                        }
''',
    1,
)
text = text.replace(
    '''    private fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
    }
''',
    '''    private fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
        observationRequestJob?.cancel()
        observationRequestJob = null
    }
''',
    1,
)
text = text.replace(
    '''    companion object {
        const val WAKELOCK_TIMEOUT_MS = 60 * 1000L
    }
''',
    '''    companion object {
        const val WAKELOCK_TIMEOUT_MS = 60 * 1000L
        internal const val OBSERVATION_DEBOUNCE_MS = 750L
    }
''',
    1,
)
holder.write_text(text)

Path("app/src/main/java/org/oxycblt/auxio/music/service/ObservationBurstGate.kt").write_text('''/*
 * Copyright (c) 2026 Auxio Project
 * ObservationBurstGate.kt is part of Auxio.
 */

package org.oxycblt.auxio.music.service

/** Latest-wins token used to conflate rapid source observer notifications. */
internal class ObservationBurstGate {
    private var generation = 0L

    @Synchronized fun nextToken(): Long = ++generation

    @Synchronized fun isLatest(token: Long): Boolean = token == generation
}
''')
Path("app/src/test/java/org/oxycblt/auxio/music/service/ObservationBurstGateTest.kt").write_text('''/*
 * Copyright (c) 2026 Auxio Project
 * ObservationBurstGateTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.music.service

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ObservationBurstGateTest {
    @Test
    fun `only latest observer event may request a scan`() {
        val gate = ObservationBurstGate()
        val first = gate.nextToken()
        val second = gate.nextToken()
        val third = gate.nextToken()

        assertFalse(gate.isLatest(first))
        assertFalse(gate.isLatest(second))
        assertTrue(gate.isLatest(third))
    }
}
''')

# Extend source lifecycle coverage with restart, reinsertion, ordering and scale cases.
test = Path("musikr/src/test/java/org/oxycblt/musikr/cache/db/IncrementalScanStoreTest.kt")
text = test.read_text()
marker = '    private fun snapshot(fingerprint: String, root: String = "/storage/usbdisk0"): SourceSnapshot {'
if marker not in text:
    raise SystemExit("IncrementalScanStoreTest insertion marker missing")
additions = '''    @Test
    fun `new store safely restarts stale pending generation after process death`() = runBlocking {
        val source = snapshot("v1")
        val initial = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(initial)
        store.stage(cachedFile("alpha.mp3", 1L))
        store.commitScan()

        val interrupted = store.planScan(listOf(source.copy(fingerprint = "v2")), false, MetadataProfile.FULL, 1L)
        store.beginScan(interrupted)
        store.stage(cachedFile("alpha.mp3", 2L))

        val restarted = IncrementalScanStore(db, db.readDao(), db.writeDao(), db.incrementalDao())
        val restartPlan = restarted.planScan(listOf(source.copy(fingerprint = "v2")), false, MetadataProfile.FULL, 1L)
        assertTrue(restartPlan.hasWork)
        restarted.beginScan(restartPlan)
        restarted.abortScan(CancellationException("simulated process restart"))

        assertEquals(1L, db.readDao().selectSongByUri(Uri.parse("file:///storage/usbdisk0/alpha.mp3"))?.modifiedMs)
    }

    @Test
    fun `unchanged source reinsertion reuses committed generation`() = runBlocking {
        val source = snapshot("same")
        val initial = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(initial)
        store.stage(cachedFile("alpha.mp3", 1L))
        store.commitScan()

        store.planScan(listOf(source.copy(available = false, fingerprint = null)), false, MetadataProfile.LEAN, 1L)
        val reinserted = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)

        assertFalse(reinserted.hasWork)
        assertEquals(setOf(source.sourceKey), reinserted.reuseSourceKeys)
    }

    @Test
    fun `changed source reinsertion schedules only changed volume`() = runBlocking {
        val usb0 = snapshot("usb0-v1", "/storage/usbdisk0")
        val usb1 = snapshot("usb1-v1", "/storage/usbdisk1")
        val initial = store.planScan(listOf(usb0, usb1), false, MetadataProfile.LEAN, 1L)
        store.beginScan(initial)
        store.stage(cachedFile("alpha.mp3", 1L, "/storage/usbdisk0"))
        store.stage(cachedFile("beta.mp3", 1L, "/storage/usbdisk1"))
        store.commitScan()

        store.planScan(listOf(usb1, usb0.copy(available = false, fingerprint = null)), false, MetadataProfile.LEAN, 1L)
        val reinserted = store.planScan(listOf(usb1, usb0.copy(fingerprint = "usb0-v2")), false, MetadataProfile.LEAN, 1L)

        assertEquals(setOf(usb0.sourceKey), reinserted.scanSourceKeys)
        assertEquals(setOf(usb1.sourceKey), reinserted.reuseSourceKeys)
    }

    @Test
    fun `source ordering cannot swap two USB identities`() = runBlocking {
        val usb0 = snapshot("usb0", "/storage/usbdisk0")
        val usb1 = snapshot("usb1", "/storage/usbdisk1")
        val initial = store.planScan(listOf(usb0, usb1), false, MetadataProfile.LEAN, 1L)
        store.beginScan(initial)
        store.stage(cachedFile("alpha.mp3", 1L, "/storage/usbdisk0"))
        store.stage(cachedFile("beta.mp3", 1L, "/storage/usbdisk1"))
        store.commitScan()

        val reordered = store.planScan(listOf(usb1, usb0), false, MetadataProfile.LEAN, 1L)
        assertFalse(reordered.hasWork)
        assertEquals(setOf(usb0.sourceKey, usb1.sourceKey), reordered.reuseSourceKeys)
    }

    @Test
    fun `large committed library keeps startup query bounded`() = runBlocking {
        val source = snapshot("large")
        val plan = store.planScan(listOf(source), false, MetadataProfile.LEAN, 1L)
        store.beginScan(plan)
        repeat(5_000) { index -> store.stage(cachedFile("track-$index.mp3", index.toLong())) }
        store.commitScan()

        assertEquals(5_000, db.incrementalLibraryDao().songCount())
        assertEquals(20, DBCache.from(db, store).firstSongs(20, 0).size)
    }

'''
if "new store safely restarts stale pending generation" not in text:
    text = text.replace(marker, additions + marker, 1)
test.write_text(text)

replace_once(
    "musikr/src/test/java/org/oxycblt/musikr/library/MetadataWorkPolicyTest.kt",
    '''        assertTrue(full.extractArtwork)
    }
}''',
    '''        assertTrue(full.extractArtwork)
        assertEquals(ArtworkPolicy.VISIBLE_ITEMS, MetadataProfile.FULL.defaultArtworkPolicy())
    }
}''',
)

# Protect the immediate lane against accidental complete-graph calls.
Path("app/src/test/java/org/oxycblt/auxio/music/ImmediateLaneArchitectureTest.kt").write_text('''/*
 * Copyright (c) 2026 Auxio Project
 * ImmediateLaneArchitectureTest.kt is part of Auxio.
 */

package org.oxycblt.auxio.music

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertFalse

class ImmediateLaneArchitectureTest {
    private val root =
        Path.of(System.getProperty("user.dir")).let { cwd ->
            if (Files.exists(cwd.resolve("settings.gradle"))) cwd else cwd.parent
        }

    @Test
    fun `fast interaction consumers never materialise the complete graph`() {
        val paths =
            listOf(
                "app/src/main/java/org/oxycblt/auxio/home/HomeViewModel.kt",
                "app/src/main/java/org/oxycblt/auxio/search/SearchViewModel.kt",
                "app/src/main/java/org/oxycblt/auxio/music/service/MusicBrowser.kt",
                "app/src/main/java/org/oxycblt/auxio/headunit/ts18/FastStartDirectFolderBrowser.kt",
            )
        val forbidden =
            listOf(
                "DBCache.snapshot",
                "Musikr.loadCached",
                "MusicGraph",
                "LibraryFactory",
                "selectAllSongs",
            )
        for (path in paths) {
            val source = root.resolve(path).readText()
            forbidden.forEach { symbol -> assertFalse(source.contains(symbol), "$path uses $symbol") }
        }
    }
}
''')

doc = Path("docs/architecture/INCREMENTAL_LIBRARY_PIPELINE.md")
text = doc.read_text()
text = text.replace(
    "**Observed:** attaching a source observer only increments an invalidation version. It does not itself enumerate files or construct a library.",
    "**Observed:** source observers attach before rich-library hydration. Attachment does not enumerate files or construct a library; notification bursts are debounced and persist only source invalidation before scan planning.",
)
text = text.replace(
    "Artwork creation is disabled in Lean. Existing cover IDs remain durable references and are resolved only when a visible/current surface requests them. Ordinary incremental scans do not globally clear the cover store.",
    "Artwork creation is disabled during Lean and Full indexing. Existing cover IDs remain durable references and visible/current, widget and explicit detail surfaces resolve artwork on demand. Ordinary incremental scans neither eagerly extract complete-library artwork nor globally clear the cover store.",
)
doc.write_text(text)

# The final branch must not retain repair machinery.
Path(".github/workflows/pr183-finalize.yml").unlink(missing_ok=True)
Path(".github/pr183-finalize.py").unlink(missing_ok=True)
