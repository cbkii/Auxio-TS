/*
 * Copyright (c) 2026 Auxio Project
 * DirectFsTraversalTest.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.direct

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import java.io.File as JavaFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.musikr.fs.CanonicalSourcePolicy
import org.oxycblt.musikr.fs.Components
import org.oxycblt.musikr.fs.File as MusicFile
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.Path
import org.oxycblt.musikr.fs.SourceFingerprintStrength
import org.oxycblt.musikr.fs.SourceIdentity
import org.oxycblt.musikr.fs.Volume
import org.oxycblt.musikr.fs.saf.SAF
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Executable completion proofs for the DirectFS coordinator.
 *
 * The defect these guard against is a traversal that starts and never finishes, so every test is
 * bounded by an explicit timeout and asserts one explicit per-source outcome rather than only "did
 * not throw".
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class DirectFsTraversalTest {
    private lateinit var tmp: JavaFile

    @Before
    fun setUp() {
        tmp = Files.createTempDirectory("directfs-traversal").toFile().canonicalFile
    }

    @After
    fun tearDown() {
        tmp.setReadable(true, false)
        tmp.walkBottomUp().forEach { it.setReadable(true, false) }
        tmp.deleteRecursively()
    }

    @Test(timeout = TIMEOUT_MS)
    fun `small explicit music folder completes with every file`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")
        track(music, "b.mp3")

        val run = traverse(listOf(explicit(music)))

        assertEquals(2, run.files.size)
        assertEquals(SourceCompletion.COMPLETED, run.completion(music))
        assertEquals(2, run.metrics.filesEmitted)
        assertEquals(1, run.metrics.directoriesVisited)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `empty folder completes empty`() = runBlocking {
        val music = dir("Music")

        val run = traverse(listOf(explicit(music)))

        assertTrue(run.files.isEmpty())
        assertEquals(SourceCompletion.COMPLETED_EMPTY, run.completion(music))
    }

    @Test(timeout = TIMEOUT_MS)
    fun `nested folders are fully enumerated`() = runBlocking {
        val music = dir("Music")
        track(music, "root.mp3")
        track(dir("Music/Album"), "one.mp3")
        track(dir("Music/Album/Disc 2"), "two.mp3")

        val run = traverse(listOf(explicit(music)))

        assertEquals(3, run.files.size)
        assertEquals(3, run.metrics.directoriesVisited)
        assertEquals(SourceCompletion.COMPLETED, run.completion(music))
    }

    @Test(timeout = TIMEOUT_MS)
    fun `exact duplicate roots are traversed once`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")

        val run = traverse(listOf(explicit(music), explicit(music)))

        assertEquals(1, run.files.size)
        assertEquals(1, run.metrics.directoriesVisited)
        assertEquals(1, run.metrics.duplicateDirectoriesSuppressed)
        assertEquals(2, run.results.size)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `trailing slash aliases collapse onto one traversal`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")

        val run =
            traverse(
                listOf(
                    explicit(music),
                    explicit(JavaFile(music.path + "/")),
                    explicit(JavaFile(music.path + "//")),
                )
            )

        assertEquals(1, run.files.size)
        assertEquals(2, run.metrics.duplicateDirectoriesSuppressed)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `whole volume and nested music source never scan one directory twice`() = runBlocking {
        val volume = dir("volume")
        val music = dir("volume/Music")
        track(music, "a.mp3")
        track(dir("volume/Other"), "b.mp3")

        val run = traverse(listOf(explicit(music), wholeVolume(volume)))

        assertEquals(setOf("a.mp3", "b.mp3"), run.files.map { it.path.name }.toSet())
        // The explicit Music root runs first; the whole-volume root then suppresses it.
        assertEquals(1, run.metrics.duplicateDirectoriesSuppressed)
        assertEquals(SourceCompletion.COMPLETED, run.completion(music))
        assertEquals(SourceCompletion.COMPLETED, run.completion(volume))
    }

    @Test(timeout = TIMEOUT_MS)
    fun `explicit sources keep generically named child folders`() = runBlocking {
        val music = dir("Music")
        track(dir("Music/Download"), "a.mp3")
        track(dir("Music/Movies"), "b.mp3")

        val run = traverse(listOf(explicit(music)))

        assertEquals(setOf("a.mp3", "b.mp3"), run.files.map { it.path.name }.toSet())
    }

    @Test(timeout = TIMEOUT_MS)
    fun `whole volume sources still exclude platform media trees`() = runBlocking {
        val volume = dir("volume")
        track(dir("volume/Download"), "a.mp3")
        track(dir("volume/Music"), "b.mp3")

        val run = traverse(listOf(wholeVolume(volume)))

        assertEquals(listOf("b.mp3"), run.files.map { it.path.name })
    }

    @Test(timeout = TIMEOUT_MS)
    fun `explicit whole volume source retains ordinary and hidden content when enabled`() =
        runBlocking {
            val volume = dir("volume")
            track(dir("volume/Download"), "download.mp3")
            track(dir("volume/.archive"), "hidden.mp3")

            val root =
                wholeVolume(volume)
                    .copy(origin = CanonicalSourcePolicy.Origin.EXPLICIT, withHidden = true)
            val run = traverse(listOf(root))

            assertEquals(
                setOf("download.mp3", "hidden.mp3"),
                run.files.map { it.path.name }.toSet(),
            )
        }

    @Test(timeout = TIMEOUT_MS)
    fun `hidden files and directories obey configured visibility`() = runBlocking {
        val music = dir("Music")
        track(music, ".root.mp3")
        track(dir("Music/.archive"), "hidden.mp3")
        track(music, "visible.mp3")

        val hiddenOff = traverse(listOf(explicit(music)))
        val hiddenOn = traverse(listOf(explicit(music).copy(withHidden = true)))

        assertEquals(listOf("visible.mp3"), hiddenOff.files.map { it.path.name })
        assertEquals(
            setOf(".root.mp3", "hidden.mp3", "visible.mp3"),
            hiddenOn.files.map { it.path.name }.toSet(),
        )
    }

    @Test(timeout = TIMEOUT_MS)
    fun `excluded subtree is not enumerated or emitted`() = runBlocking {
        val music = dir("Music")
        val excluded = dir("Music/Podcasts")
        track(excluded, "talk.mp3")
        track(dir("Music/Albums"), "song.mp3")
        track(music, "root.mp3")

        val run =
            traverse(
                listOf(explicit(music).copy(excludedCanonicalPaths = setOf(excluded.canonicalPath)))
            )

        assertEquals(setOf("root.mp3", "song.mp3"), run.files.map { it.path.name }.toSet())
        assertEquals(2, run.metrics.directoriesVisited)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `excluded root completes empty without enumeration`() = runBlocking {
        val music = dir("Music")
        track(music, "song.mp3")
        var enumerations = 0

        val run =
            traverse(
                listOf(explicit(music).copy(excludedCanonicalPaths = setOf(music.canonicalPath))),
                options()
                    .copy(
                        listDirectory = {
                            enumerations++
                            it.listFiles()
                        }
                    ),
            )

        assertTrue(run.files.isEmpty())
        assertEquals(0, enumerations)
        assertEquals(SourceCompletion.COMPLETED_EMPTY, run.completion(music))
    }

    @Test(timeout = TIMEOUT_MS)
    fun `exclusion remains effective beneath deliberate overlapping roots`() = runBlocking {
        val volume = dir("volume")
        val music = dir("volume/Music")
        val excluded = dir("volume/Music/Podcasts")
        track(excluded, "talk.mp3")
        track(music, "song.mp3")
        track(dir("volume/Other"), "other.mp3")
        val exclusions = setOf(excluded.canonicalPath)

        val run =
            traverse(
                listOf(
                    explicit(music).copy(excludedCanonicalPaths = exclusions),
                    wholeVolume(volume)
                        .copy(
                            origin = CanonicalSourcePolicy.Origin.EXPLICIT,
                            excludedCanonicalPaths = exclusions,
                        ),
                )
            )

        assertEquals(setOf("song.mp3", "other.mp3"), run.files.map { it.path.name }.toSet())
    }

    @Test(timeout = TIMEOUT_MS)
    fun `explicit exclusion and fallback noise policy remain independent`() = runBlocking {
        val volume = dir("volume")
        val excluded = dir("volume/Music/Ignore")
        track(excluded, "ignored.mp3")
        track(dir("volume/Music"), "song.mp3")
        track(dir("volume/Download"), "download.mp3")

        val run =
            traverse(
                listOf(
                    wholeVolume(volume).copy(excludedCanonicalPaths = setOf(excluded.canonicalPath))
                )
            )

        assertEquals(listOf("song.mp3"), run.files.map { it.path.name })
    }

    @Test(timeout = TIMEOUT_MS)
    fun `symbolic links escaping the root are not followed`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")
        val outside = dir("Outside")
        track(outside, "escaped.mp3")
        Files.createSymbolicLink(Paths.get(music.path, "escape"), Paths.get(outside.canonicalPath))

        val run = traverse(listOf(explicit(music)))

        assertEquals(listOf("a.mp3"), run.files.map { it.path.name })
        assertEquals(SourceCompletion.COMPLETED, run.completion(music))
    }

    @Test(timeout = TIMEOUT_MS)
    fun `alias directories resolving onto one canonical path are visited once`() = runBlocking {
        val music = dir("Music")
        val album = dir("Music/Album")
        track(album, "a.mp3")
        Files.createSymbolicLink(
            Paths.get(music.path, "Album Link"),
            Paths.get(album.canonicalPath),
        )

        val run = traverse(listOf(explicit(music)))

        assertEquals(listOf("a.mp3"), run.files.map { it.path.name })
        assertEquals(2, run.metrics.directoriesVisited)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `unreadable child directory does not fail the source`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")
        val locked = dir("Music/Locked")
        track(locked, "locked.mp3")
        locked.setReadable(false, false)

        try {
            val run = traverse(listOf(explicit(music)))

            assertEquals(SourceCompletion.COMPLETED, run.completion(music))
            assertTrue(run.files.map { it.path.name }.contains("a.mp3"))
        } finally {
            locked.setReadable(true, false)
        }
    }

    @Test(timeout = TIMEOUT_MS)
    fun `unavailable configured root reports temporarily unavailable`() = runBlocking {
        val missing = JavaFile(tmp, "Gone")

        val run = traverse(listOf(explicit(missing)))

        assertEquals(SourceCompletion.TEMPORARILY_UNAVAILABLE, run.results.single().completion)
        assertTrue(run.files.isEmpty())
    }

    @Test(timeout = TIMEOUT_MS)
    fun `removable root disappearing during traversal is reported not hung`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")
        val album = dir("Music/Album")
        track(album, "b.mp3")
        var listedDirectories = 0

        val run =
            traverse(
                listOf(explicit(music)),
                options()
                    .copy(
                        listDirectory = { directory ->
                            if (listedDirectories++ == 1) {
                                // The volume vanished after the root was enumerated.
                                music.deleteRecursively()
                            }
                            directory.listFiles()
                        }
                    ),
            )

        assertEquals(1, run.results.size)
        assertEquals(SourceCompletion.TEMPORARILY_UNAVAILABLE, run.results.single().completion)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `cancellation unwinds the traversal instead of parking`() = runBlocking {
        val music = dir("Music")
        repeat(32) { index -> track(music, "track-$index.mp3") }

        // One rendezvous receive proves the producer reached channel emission; with no further
        // receiver, the next send is owned by the cancellation path below.
        val output = Channel<MusicFile>(Channel.RENDEZVOUS)
        val traversal = DirectFsTraversal(listOf(explicit(music)), options())

        val failure =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    val run = async(Dispatchers.IO) { traversal.explore(output) }
                    output.receive()
                    run.cancel(CancellationException("cancelled by test"))
                    runCatching { run.await() }.exceptionOrNull()
                }
            }
        output.close()

        assertTrue("expected cancellation, got $failure", failure is CancellationException)
        assertEquals(0, traversal.metricsSnapshot().activeEnumerators)
        assertEquals(0, traversal.metricsSnapshot().queuedDirectories)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `cancellation during directory listing releases traversal state`() = runBlocking {
        val music = dir("Music")
        val entered = CompletableDeferred<Unit>()
        val traversal =
            DirectFsTraversal(
                listOf(explicit(music)),
                options()
                    .copy(
                        listDirectory = {
                            entered.complete(Unit)
                            awaitCancellation()
                        }
                    ),
            )
        val output = Channel<MusicFile>(Channel.UNLIMITED)

        val failure = coroutineScope {
            val run = async(Dispatchers.IO) { traversal.explore(output) }
            entered.await()
            run.cancel(CancellationException("cancel during list"))
            runCatching { run.await() }.exceptionOrNull()
        }
        output.close()

        assertTrue(failure is CancellationException)
        assertEquals(0, traversal.metricsSnapshot().activeEnumerators)
        assertEquals(0, traversal.metricsSnapshot().queuedDirectories)
        assertEquals(
            SourceCompletion.CANCELLED,
            traversal.metricsSnapshot().results.single().completion,
        )
    }

    @Test(timeout = TIMEOUT_MS)
    fun `cancellation during entry inspection releases traversal state`() = runBlocking {
        val music = dir("Music")
        track(music, "song.mp3")
        val entered = CompletableDeferred<Unit>()
        val traversal =
            DirectFsTraversal(
                listOf(explicit(music)),
                options()
                    .copy(
                        inspectEntry = { _, _, _ ->
                            entered.complete(Unit)
                            awaitCancellation()
                        }
                    ),
            )
        val output = Channel<MusicFile>(Channel.UNLIMITED)

        val failure = coroutineScope {
            val run = async(Dispatchers.IO) { traversal.explore(output) }
            entered.await()
            run.cancel(CancellationException("cancel during stat"))
            runCatching { run.await() }.exceptionOrNull()
        }
        output.close()

        assertTrue(failure is CancellationException)
        assertEquals(0, traversal.metricsSnapshot().activeEnumerators)
        assertEquals(0, traversal.metricsSnapshot().queuedDirectories)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `downstream back-pressure is respected instead of buffering the tree`() = runBlocking {
        val music = dir("Music")
        repeat(8) { index -> track(music, "track-$index.mp3") }

        val output = Channel<MusicFile>(1)
        val traversal = DirectFsTraversal(listOf(explicit(music)), options())
        val received = mutableListOf<MusicFile>()

        val metrics =
            withTimeout(TIMEOUT_MS) {
                coroutineScope {
                    val run =
                        async(Dispatchers.IO) {
                            try {
                                traversal.explore(output)
                            } finally {
                                output.close()
                            }
                        }
                    for (file in output) {
                        received += file
                        delay(1)
                    }
                    run.await()
                }
            }

        assertEquals(8, received.size)
        assertEquals(8, metrics.filesEmitted)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `directory safety limit truncates deterministically`() = runBlocking {
        val music = dir("Music")
        repeat(6) { index -> track(dir("Music/album-$index"), "track-$index.mp3") }

        val run =
            traverse(
                listOf(explicit(music)),
                options().copy(explicitBudget = TraversalBudget(maxDirectories = 3, maxFiles = 100)),
            )

        assertEquals(SourceCompletion.TRUNCATED, run.completion(music))
        assertTrue(run.metrics.directoriesVisited <= 3)
        assertNotNull(run.results.single().detail)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `file safety limit truncates deterministically`() = runBlocking {
        val music = dir("Music")
        repeat(6) { index -> track(music, "track-$index.mp3") }

        val run =
            traverse(
                listOf(explicit(music)),
                options().copy(explicitBudget = TraversalBudget(maxDirectories = 100, maxFiles = 2)),
            )

        assertEquals(SourceCompletion.TRUNCATED, run.completion(music))
        assertEquals(2, run.files.size)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `maximum depth truncates without stranding sibling trees`() = runBlocking {
        val music = dir("Music")
        track(dir("Music/Shallow"), "shallow.mp3")
        track(dir("Music/One/Two/Three"), "deep.mp3")

        val run = traverse(listOf(explicit(music)), options().copy(maxDepth = 2))

        assertEquals(SourceCompletion.TRUNCATED, run.completion(music))
        assertTrue(run.files.map { it.path.name }.contains("shallow.mp3"))
        assertFalse(run.files.map { it.path.name }.contains("deep.mp3"))
    }

    @Test(timeout = TIMEOUT_MS)
    fun `every configured source reports exactly one completion`() = runBlocking {
        val first = dir("First")
        track(first, "a.mp3")
        val second = dir("Second")
        val third = JavaFile(tmp, "Missing")

        val run = traverse(listOf(explicit(first), explicit(second), explicit(third)))

        assertEquals(3, run.results.size)
        assertEquals(SourceCompletion.COMPLETED, run.completion(first))
        assertEquals(SourceCompletion.COMPLETED_EMPTY, run.completion(second))
        assertEquals(SourceCompletion.TEMPORARILY_UNAVAILABLE, run.completion(third))
    }

    @Test(timeout = TIMEOUT_MS)
    fun `an exception in one directory enumeration does not strand later sources`() = runBlocking {
        val broken = dir("Broken")
        track(broken, "a.mp3")
        val healthy = dir("Healthy")
        track(healthy, "b.mp3")

        val causalFailure = IOException("enumeration exploded")
        val output = Channel<MusicFile>(Channel.UNLIMITED)
        val traversal =
            DirectFsTraversal(
                listOf(explicit(broken), explicit(healthy)),
                options()
                    .copy(
                        listDirectory = { directory ->
                            if (directory.canonicalPath == broken.canonicalPath) {
                                throw causalFailure
                            }
                            directory.listFiles()
                        }
                    ),
            )

        val failure = runCatching { traversal.explore(output) }.exceptionOrNull()
        output.close()
        val files = mutableListOf<MusicFile>()
        for (file in output) files += file
        val metrics = traversal.metricsSnapshot()

        assertTrue(failure === causalFailure)
        assertEquals(SourceCompletion.FAILED, metrics.completion(broken))
        assertEquals(SourceCompletion.COMPLETED, metrics.completion(healthy))
        assertEquals(listOf("b.mp3"), files.map { it.path.name })
        assertEquals(0, metrics.activeEnumerators)
        assertEquals(0, metrics.queuedDirectories)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `entry stat exception remains the causal scan failure`() = runBlocking {
        val music = dir("Music")
        track(music, "song.mp3")
        val causalFailure = IOException("entry stat exploded")
        val output = Channel<MusicFile>(Channel.UNLIMITED)
        val traversal =
            DirectFsTraversal(
                listOf(explicit(music)),
                options().copy(inspectEntry = { _, _, _ -> throw causalFailure }),
            )

        val failure = runCatching { traversal.explore(output) }.exceptionOrNull()
        output.close()
        val metrics = traversal.metricsSnapshot()

        assertTrue(failure === causalFailure)
        assertEquals(SourceCompletion.FAILED, metrics.results.single().completion)
        assertEquals(0, metrics.activeEnumerators)
        assertEquals(0, metrics.queuedDirectories)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `canonical path exception remains the causal scan failure`() = runBlocking {
        val music = dir("Music")
        track(music, "song.mp3")
        val causalFailure = IOException("canonical resolution exploded")
        val output = Channel<MusicFile>(Channel.UNLIMITED)
        val traversal =
            DirectFsTraversal(
                listOf(explicit(music)),
                options().copy(resolveCanonicalPath = { throw causalFailure }),
            )

        val failure = runCatching { traversal.explore(output) }.exceptionOrNull()
        output.close()
        val metrics = traversal.metricsSnapshot()

        assertTrue(failure === causalFailure)
        assertEquals(SourceCompletion.FAILED, metrics.results.single().completion)
        assertEquals(0, metrics.activeEnumerators)
        assertEquals(0, metrics.queuedDirectories)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `direct fs owning task fails with the traversal cause`() = runBlocking {
        val music = dir("Music")
        track(music, "song.mp3")
        val context = ApplicationProvider.getApplicationContext<Context>()
        val location =
            requireNotNull(Location.Unopened.from(context, Uri.fromFile(music)).open(context))
        val causalFailure = IOException("mandatory traversal failure")
        val directFs =
            DirectFS(
                SAF.Query(
                    source = listOf(location),
                    exclude = emptyList(),
                    withHidden = false,
                    multithread = false,
                ),
                options().copy(listDirectory = { throw causalFailure }),
            )
        val output = Channel<MusicFile>(Channel.UNLIMITED)

        val result = directFs.explore(output).await()
        val metrics = requireNotNull(directFs.lastTraversalMetrics())

        assertTrue(result.exceptionOrNull() === causalFailure)
        assertEquals(SourceCompletion.FAILED, metrics.results.single().completion)
        assertEquals(0, metrics.activeEnumerators)
        assertEquals(0, metrics.queuedDirectories)
        val channelFailure = output.receiveCatching().exceptionOrNull()
        assertTrue(channelFailure is IOException)
        assertEquals(causalFailure.message, channelFailure?.message)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `source snapshots retain configured identity when root preparation rejects it`() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val location =
                requireNotNull(
                    Location.Unopened.from(context, Uri.fromFile(JavaFile("/system"))).open(context)
                )
            val directFs = DirectFS(listOf(location))
            val sourceKey = SourceIdentity.forLocation(location)

            val snapshot = directFs.sourceSnapshots().single()

            assertEquals(sourceKey, snapshot.sourceKey)
            assertEquals(location.uri.toString(), snapshot.rootUri)
            assertEquals(location.uri.path, snapshot.rootPath)
            assertFalse(snapshot.available)
            assertEquals(null, snapshot.fingerprint)
            assertEquals(SourceFingerprintStrength.NONE, snapshot.fingerprintStrength)
            assertTrue(
                directFs
                    .drainSourceFailures()
                    .getValue(sourceKey)
                    .startsWith("TEMPORARILY_UNAVAILABLE|")
            )
        }

    @Test
    fun `fingerprint changes only with effective source policy`() {
        val music = dir("Music")
        track(music, "song.mp3")
        val directFs = DirectFS(emptyList<Location.Opened>())
        val root =
            explicit(music)
                .copy(
                    canonicalPath = "/storage/emulated/0/Music",
                    canonicalKey = "path:/storage/emulated/0/Music",
                )
        val baseline = directFs.combineRootFingerprints(listOf(root))

        assertEquals(
            baseline,
            directFs.combineRootFingerprints(
                listOf(
                    root.copy(
                        normalizedUri = "file:///sdcard/Music",
                        displayPath = "/sdcard/Music",
                    ),
                    root,
                )
            ),
        )
        assertNotEquals(
            baseline,
            directFs.combineRootFingerprints(
                listOf(root.copy(origin = CanonicalSourcePolicy.Origin.AUTOMATIC_SUGGESTION))
            ),
        )
        assertNotEquals(
            baseline,
            directFs.combineRootFingerprints(listOf(root.copy(withHidden = true))),
        )
        assertNotEquals(
            baseline,
            directFs.combineRootFingerprints(
                listOf(
                    root.copy(excludedCanonicalPaths = setOf("/storage/emulated/0/Music/Podcasts"))
                )
            ),
        )
        assertEquals(
            baseline,
            directFs.combineRootFingerprints(
                listOf(root.copy(excludedCanonicalPaths = setOf("/storage/emulated/0/Other")))
            ),
        )
    }

    @Test(timeout = TIMEOUT_MS)
    fun `traversal leaves no active enumerator or queued work behind`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")
        track(dir("Music/Album"), "b.mp3")

        val run = traverse(listOf(explicit(music)))

        assertEquals(0, run.metrics.activeEnumerators)
        assertEquals(0, run.metrics.queuedDirectories)
        assertTrue(run.metrics.peakQueuedDirectories >= 1)
        assertEquals(1, run.results.size)
        assertTrue(run.metrics.elapsedMs >= 0)
    }

    @Test(timeout = TIMEOUT_MS)
    fun `protected canonical children are never descended into`() = runBlocking {
        val music = dir("Music")
        track(music, "a.mp3")
        val secret = dir("Music/Secret")
        track(secret, "secret.mp3")

        val run =
            traverse(
                listOf(explicit(music)),
                options().copy(isAllowedCanonicalPath = { it != secret.canonicalPath }),
            )

        assertEquals(listOf("a.mp3"), run.files.map { it.path.name })
    }

    private fun options() = DirectFsOptions(isAllowedCanonicalPath = { true })

    private fun dir(relative: String): JavaFile =
        JavaFile(tmp, relative).also { assertTrue(it.path, it.exists() || it.mkdirs()) }

    private fun track(parent: JavaFile, name: String): JavaFile =
        JavaFile(parent, name).also {
            it.writeText("fake tag data")
            assertTrue(it.path, it.exists())
        }

    private fun rootPath() = Path(TestVolume, Components.root())

    private fun explicit(directory: JavaFile) =
        prepared(directory, CanonicalSourcePolicy.Scope.EXPLICIT)

    private fun wholeVolume(directory: JavaFile) =
        prepared(directory, CanonicalSourcePolicy.Scope.WHOLE_VOLUME)
            .copy(origin = CanonicalSourcePolicy.Origin.WHOLE_VOLUME_FALLBACK)

    private fun prepared(directory: JavaFile, scope: CanonicalSourcePolicy.Scope) =
        PreparedRoot(
            sourceKey = directory.name,
            directory = directory,
            canonicalPath = directory.canonicalPath,
            relativePath = rootPath(),
            scope = scope,
        )

    private suspend fun traverse(
        roots: List<PreparedRoot>,
        options: DirectFsOptions = options(),
    ): TraversalRun {
        val output = Channel<MusicFile>(Channel.UNLIMITED)
        val metrics = withTimeout(TIMEOUT_MS) { DirectFsTraversal(roots, options).explore(output) }
        output.close()
        val files = mutableListOf<MusicFile>()
        for (file in output) files += file
        return TraversalRun(files, metrics)
    }

    private class TraversalRun(val files: List<MusicFile>, val metrics: DirectFsTraversalMetrics) {
        val results: List<SourceTraversalResult>
            get() = metrics.results

        fun completion(directory: JavaFile): SourceCompletion? =
            results.firstOrNull { it.canonicalPath == directory.canonicalPath }?.completion
    }

    private fun DirectFsTraversalMetrics.completion(directory: JavaFile): SourceCompletion? =
        results.firstOrNull { it.canonicalPath == directory.canonicalPath }?.completion

    private object TestVolume : Volume.Internal {
        override val mediaStoreName: String? = null
        override val components = Components.root()

        override fun resolveName(context: Context) = "test"

        override fun isAccessible() = true
    }

    private companion object {
        const val TIMEOUT_MS = 30_000L
    }
}
