/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourceConfigurationTest.kt is part of Auxio.
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
import android.net.Uri
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import java.io.File
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.oxycblt.auxio.music.locations.LocationMode
import org.oxycblt.musikr.fs.Location
import org.oxycblt.musikr.fs.saf.SAF
import org.oxycblt.musikr.library.MetadataProfile
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class MusicSourceConfigurationTest {
    private lateinit var context: Context
    private lateinit var settings: MusicSettingsImpl

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit()
        settings = MusicSettingsImpl(context)
    }

    @Test
    fun `source configuration is durable and pending generation is claimed once`() {
        settings.lastScanFailed = true
        val before = settings.sourceConfigurationGeneration

        assertTrue(applyDirectSource())
        assertEquals(LocationMode.DIRECT_FS, settings.locationMode)
        assertEquals(1, settings.configuredSourceCount)
        assertEquals(before + 1L, settings.sourceConfigurationGeneration)
        assertFalse(settings.lastScanFailed)

        val claimed = claim(ownerA, "attempt-a", 100L)
        assertEquals(SourceConfigurationCheckpoint.State.RUNNING, claimed.state)
        assertEquals("attempt-a", claimed.attemptId)
        assertNull(
            settings.claimPendingConfiguration(
                claimed.generation,
                ownerB,
                "attempt-b",
                101L,
                SourceScanClaimReason.CONFIGURATION_CHANGE,
            )
        )
    }

    @Test
    fun `two concurrent claim attempts produce one owner`() {
        settings.forceLocationUpdate()
        val generation = settings.sourceConfigurationGeneration
        val otherSettings = MusicSettingsImpl(context)
        val ready = CountDownLatch(2)
        val start = CountDownLatch(1)
        val executor = Executors.newFixedThreadPool(2)
        try {
            val first =
                executor.submit<SourceConfigurationCheckpoint?> {
                    ready.countDown()
                    start.await()
                    settings.claimPendingConfiguration(
                        generation,
                        ownerA,
                        "attempt-a",
                        100L,
                        SourceScanClaimReason.CONFIGURATION_CHANGE,
                    )
                }
            val second =
                executor.submit<SourceConfigurationCheckpoint?> {
                    ready.countDown()
                    start.await()
                    otherSettings.claimPendingConfiguration(
                        generation,
                        ownerB,
                        "attempt-b",
                        100L,
                        SourceScanClaimReason.CONFIGURATION_CHANGE,
                    )
                }
            ready.await()
            start.countDown()

            assertEquals(1, listOf(first.get(), second.get()).count { it != null })
            assertEquals(
                SourceConfigurationCheckpoint.State.RUNNING,
                settings.sourceConfigurationCheckpoint?.state,
            )
        } finally {
            executor.shutdownNow()
        }
    }

    @Test
    fun `process recreation terminalises stale running attempt before retry`() {
        settings.forceLocationUpdate()
        val first = claim(ownerA, "attempt-a", 100L)

        val recovered =
            requireNotNull(
                MusicSettingsImpl(context)
                    .recoverInterruptedSourceConfiguration(processRecreatedOwner, 200L)
            )

        assertEquals(first.generation, recovered.generation)
        assertEquals(SourceConfigurationCheckpoint.State.INTERRUPTED, recovered.state)
        assertEquals(SourceScanAttemptOutcome.PROCESS_INTERRUPTED, recovered.terminalOutcome)
        val retry =
            requireNotNull(
                settings.claimPendingConfiguration(
                    first.generation,
                    processRecreatedOwner,
                    "attempt-b",
                    201L,
                    SourceScanClaimReason.STARTUP_RECOVERY,
                )
            )
        assertNotEquals(first.attemptId, retry.attemptId)
    }

    @Test
    fun `same-process service recreation uses explicit lifecycle handoff`() {
        settings.forceLocationUpdate()
        val claimed = claim(ownerA, "attempt-a", 100L)

        assertTrue(
            settings.handoffSourceConfigurationAttempt(
                claimed.generation,
                "attempt-a",
                ownerA,
                ownerSameProcess,
                150L,
            )
        )
        assertFalse(
            settings.ownsSourceConfigurationAttempt(claimed.generation, "attempt-a", ownerA)
        )
        assertTrue(
            settings.ownsSourceConfigurationAttempt(
                claimed.generation,
                "attempt-a",
                ownerSameProcess,
            )
        )
        assertFalse(
            settings.handoffSourceConfigurationAttempt(
                claimed.generation,
                "attempt-a",
                ownerSameProcess,
                processRecreatedOwner,
                160L,
            )
        )
    }

    @Test
    fun `old terminal callback cannot clobber a newer running generation`() {
        settings.forceLocationUpdate()
        val old = claim(ownerA, "attempt-a", 100L)
        settings.forceLocationUpdate()
        val newer = claim(ownerB, "attempt-b", 200L)

        assertFalse(complete(old, ownerA, successCompletion(UUID.randomUUID()), 201L))
        val current = requireNotNull(settings.sourceConfigurationCheckpoint)
        assertEquals(newer.generation, current.generation)
        assertEquals("attempt-b", current.attemptId)
        assertEquals(SourceConfigurationCheckpoint.State.RUNNING, current.state)
    }

    @Test
    fun `stale generation success acknowledgement is rejected`() {
        settings.forceLocationUpdate()
        val old = claim(ownerA, "attempt-a", 100L)
        settings.forceLocationUpdate()

        assertFalse(complete(old, ownerA, successCompletion(UUID.randomUUID()), 200L))
        assertEquals(
            SourceConfigurationCheckpoint.State.PENDING,
            settings.sourceConfigurationCheckpoint?.state,
        )
    }

    @Test
    fun `stale attempt success acknowledgement is rejected`() {
        settings.forceLocationUpdate()
        val first = claim(ownerA, "attempt-a", 100L)
        assertTrue(
            complete(
                first,
                ownerA,
                completion(SourceScanAttemptOutcome.CANCELLED, lastScanFailed = false),
                150L,
            )
        )
        val second =
            requireNotNull(
                settings.claimPendingConfiguration(
                    first.generation,
                    ownerB,
                    "attempt-b",
                    200L,
                    SourceScanClaimReason.USER_RETRY,
                )
            )

        assertFalse(complete(first, ownerA, successCompletion(UUID.randomUUID()), 210L))
        assertEquals("attempt-b", settings.sourceConfigurationCheckpoint?.attemptId)
        assertEquals(second.generation, settings.sourceConfigurationCheckpoint?.generation)
    }

    @Test
    fun `process death during discovery is recorded with progress evidence`() {
        assertProcessDeathAt("DISCOVERING", explored = 0, loaded = 0, evaluated = 0)
    }

    @Test
    fun `process death during extraction is recorded with progress evidence`() {
        assertProcessDeathAt("EXTRACTING", explored = 12, loaded = 4, evaluated = 0)
    }

    @Test
    fun `process death during final commit rejects the dead owner publication`() {
        val dead = prepareAttemptAt("FINALISING", explored = 12, loaded = 12, evaluated = 12)
        val recovered =
            requireNotNull(
                settings.recoverInterruptedSourceConfiguration(processRecreatedOwner, 300L)
            )

        assertFalse(complete(dead, ownerA, successCompletion(UUID.randomUUID()), 301L))
        assertEquals(SourceScanAttemptOutcome.PROCESS_INTERRUPTED, recovered.terminalOutcome)
        assertEquals("FINALISING", recovered.attempt?.progress?.phase)
    }

    @Test
    fun `cancellation is terminal retryable and not a fatal scan failure`() {
        val claimed = pendingAndClaim()

        assertTrue(
            complete(
                claimed,
                ownerA,
                completion(SourceScanAttemptOutcome.CANCELLED, lastScanFailed = false),
                200L,
            )
        )
        assertTerminal(SourceConfigurationCheckpoint.State.CANCELLED, false)
        assertTrue(
            requireNotNull(settings.sourceConfigurationCheckpoint)
                .canClaim(SourceScanClaimReason.USER_RETRY)
        )
    }

    @Test
    fun `timeout is one retryable terminal result and preserves unresolved sources`() {
        val claimed = pendingAndClaim()

        assertTrue(
            complete(
                claimed,
                ownerA,
                completion(
                    SourceScanAttemptOutcome.TIMED_OUT,
                    unresolved = setOf("direct:usb"),
                    lastScanFailed = true,
                ),
                200L,
            )
        )
        assertTerminal(SourceConfigurationCheckpoint.State.TIMED_OUT, true)
        assertEquals(
            setOf("direct:usb"),
            settings.sourceConfigurationCheckpoint?.unresolvedSourceKeys,
        )
    }

    @Test
    fun `fatal failure is terminal retryable and retains the committed library`() {
        val priorRevision = UUID.randomUUID()
        settings.revision = priorRevision
        settings.libraryState = LibraryState.USABLE
        val claimed = pendingAndClaim()

        assertTrue(
            complete(
                claimed,
                ownerA,
                completion(
                    SourceScanAttemptOutcome.FAILED_RETRYABLE,
                    lastScanFailed = true,
                    failureClass = "java.io.IOException",
                ),
                200L,
            )
        )
        assertTerminal(SourceConfigurationCheckpoint.State.FAILED_RETRYABLE, true)
        assertEquals(priorRevision, settings.revision)
        assertEquals(LibraryState.USABLE, settings.libraryState)
    }

    @Test
    fun `temporarily unavailable source retains prior library and source key`() {
        val priorRevision = UUID.randomUUID()
        settings.revision = priorRevision
        settings.libraryState = LibraryState.USABLE
        val claimed = pendingAndClaim()

        assertTrue(
            complete(
                claimed,
                ownerA,
                completion(
                    SourceScanAttemptOutcome.TEMPORARILY_UNAVAILABLE,
                    unresolved = setOf("direct:usb"),
                    lastScanFailed = true,
                ),
                200L,
            )
        )
        assertTerminal(SourceConfigurationCheckpoint.State.FAILED_RETRYABLE, true)
        assertEquals(priorRevision, settings.revision)
        assertEquals(LibraryState.USABLE, settings.libraryState)
    }

    @Test
    fun `partial source success publishes a readable library and unresolved keys`() {
        val claimed = pendingAndClaim()
        val revision = UUID.randomUUID()

        assertTrue(
            complete(
                claimed,
                ownerA,
                completion(
                    SourceScanAttemptOutcome.PARTIAL_SUCCESS,
                    unresolved = setOf("direct:usb"),
                    revision = revision,
                    libraryState = LibraryState.USABLE,
                    lastScanFailed = true,
                ),
                200L,
            )
        )
        assertTerminal(SourceConfigurationCheckpoint.State.PARTIALLY_COMMITTED, true)
        assertEquals(revision, settings.revision)
        assertEquals(LibraryState.USABLE, settings.libraryState)
    }

    @Test
    fun `authoritative empty result commits empty library state`() {
        val claimed = pendingAndClaim()
        val revision = UUID.randomUUID()

        assertTrue(
            complete(
                claimed,
                ownerA,
                completion(
                    SourceScanAttemptOutcome.AUTHORITATIVE_EMPTY,
                    revision = revision,
                    libraryState = LibraryState.EMPTY,
                    lastScanFailed = false,
                ),
                200L,
            )
        )
        assertTerminal(SourceConfigurationCheckpoint.State.COMMITTED, false)
        assertEquals(revision, settings.revision)
        assertEquals(LibraryState.EMPTY, settings.libraryState)
    }

    @Test
    fun `successful non-empty result commits usable library state`() {
        val claimed = pendingAndClaim()
        val revision = UUID.randomUUID()

        assertTrue(complete(claimed, ownerA, successCompletion(revision), 200L))
        assertTerminal(SourceConfigurationCheckpoint.State.COMMITTED, false)
        assertEquals(revision, settings.revision)
        assertEquals(LibraryState.USABLE, settings.libraryState)
    }

    @Test
    fun `metadata enrichment after source commit cannot reopen the checkpoint`() {
        val claimed = pendingAndClaim()
        assertTrue(complete(claimed, ownerA, successCompletion(UUID.randomUUID()), 200L))
        val enrichment =
            IndexRequest(
                reason = IndexReason.METADATA_ENRICHMENT,
                withCache = true,
                metadataProfile = MetadataProfile.FULL,
                configurationGeneration = claimed.generation,
            )

        assertNull(IndexRequestPolicy.checkpointAuthority(enrichment))
        assertEquals(
            SourceConfigurationCheckpoint.State.COMMITTED,
            settings.sourceConfigurationCheckpoint?.state,
        )
    }

    @Test
    fun `metadata enrichment while source scan runs cannot claim its attempt`() {
        val claimed = pendingAndClaim()
        val enrichment =
            IndexRequest(
                reason = IndexReason.METADATA_ENRICHMENT,
                withCache = true,
                metadataProfile = MetadataProfile.FULL,
                configurationGeneration = claimed.generation,
            )

        assertNull(IndexRequestPolicy.checkpointAuthority(enrichment))
        assertTrue(
            settings.ownsSourceConfigurationAttempt(
                claimed.generation,
                requireNotNull(claimed.attemptId),
                ownerA,
            )
        )
    }

    @Test
    fun `repeated process restart never reuses the poisoned attempt`() {
        settings.forceLocationUpdate()
        val first = claim(ownerA, "attempt-a", 100L)
        settings.recoverInterruptedSourceConfiguration(processRecreatedOwner, 200L)
        val second =
            requireNotNull(
                settings.claimPendingConfiguration(
                    first.generation,
                    processRecreatedOwner,
                    "attempt-b",
                    201L,
                    SourceScanClaimReason.STARTUP_RECOVERY,
                )
            )
        val thirdOwner = SourceScanAttemptOwner("process-c", "service-c")
        settings.recoverInterruptedSourceConfiguration(thirdOwner, 300L)
        val third =
            requireNotNull(
                settings.claimPendingConfiguration(
                    first.generation,
                    thirdOwner,
                    "attempt-c",
                    301L,
                    SourceScanClaimReason.STARTUP_RECOVERY,
                )
            )

        assertNotEquals(first.attemptId, second.attemptId)
        assertNotEquals(second.attemptId, third.attemptId)
        assertEquals("attempt-c", settings.sourceConfigurationCheckpoint?.attemptId)
    }

    @Test
    fun `cancellation and failure race assigns exactly one terminal result`() {
        val claimed = pendingAndClaim()
        val ready = CountDownLatch(2)
        val start = CountDownLatch(1)
        val executor = Executors.newFixedThreadPool(2)
        try {
            val completions =
                listOf(
                    completion(SourceScanAttemptOutcome.CANCELLED, lastScanFailed = false),
                    completion(SourceScanAttemptOutcome.FAILED_RETRYABLE, lastScanFailed = true),
                )
            val futures =
                completions.mapIndexed { index, completion ->
                    executor.submit<Boolean> {
                        ready.countDown()
                        start.await()
                        complete(claimed, ownerA, completion, 200L + index)
                    }
                }
            ready.await()
            start.countDown()

            assertEquals(1, futures.count { it.get() })
            assertNotNull(settings.sourceConfigurationCheckpoint?.terminalOutcome)
            assertFalse(complete(claimed, ownerA, successCompletion(UUID.randomUUID()), 300L))
        } finally {
            executor.shutdownNow()
        }
    }

    @Test
    fun `legacy running checkpoint without attempt ID migrates and recovers`() {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putLong("auxio_source_configuration_generation", 6L)
            .putString("auxio_source_checkpoint_state", "RUNNING")
            .putLong("auxio_source_checkpoint_last_attempt", 123L)
            .commit()

        val migrated = requireNotNull(settings.sourceConfigurationCheckpoint)
        assertEquals("legacy-generation-6", migrated.attemptId)
        assertEquals(SourceConfigurationCheckpoint.State.RUNNING, migrated.state)
        val recovered = requireNotNull(settings.recoverInterruptedSourceConfiguration(ownerA, 200L))
        assertEquals(SourceScanAttemptOutcome.PROCESS_INTERRUPTED, recovered.terminalOutcome)
    }

    @Test
    fun `legacy pending checkpoint without attempt ID can be claimed`() {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putLong("auxio_source_configuration_generation", 4L)
            .putBoolean("auxio_pending_initial_music_scan", true)
            .commit()

        val claimed = claim(ownerA, "attempt-a", 100L)
        assertEquals(4L, claimed.generation)
        assertEquals("attempt-a", claimed.attemptId)
    }

    private fun applyDirectSource(): Boolean {
        val source =
            requireNotNull(
                Location.Unopened.from(context, Uri.fromFile(File("/storage/emulated/0/Music")))
                    .open(context)
            )
        return settings.applySourceConfiguration(
            LocationMode.DIRECT_FS,
            SAF.Query(
                source = listOf(source),
                exclude = emptyList(),
                withHidden = false,
                multithread = true,
            ),
            settings.mediaStoreQuery,
        )
    }

    private fun pendingAndClaim(): SourceConfigurationCheckpoint {
        settings.forceLocationUpdate()
        return claim(ownerA, "attempt-a", 100L)
    }

    private fun claim(
        owner: SourceScanAttemptOwner,
        attemptId: String,
        nowMs: Long,
    ): SourceConfigurationCheckpoint =
        requireNotNull(
            settings.claimPendingConfiguration(
                settings.sourceConfigurationGeneration,
                owner,
                attemptId,
                nowMs,
                SourceScanClaimReason.CONFIGURATION_CHANGE,
            )
        )

    private fun prepareAttemptAt(
        phase: String,
        explored: Int,
        loaded: Int,
        evaluated: Int,
    ): SourceConfigurationCheckpoint {
        val claimed = pendingAndClaim()
        assertTrue(
            settings.heartbeatSourceConfigurationAttempt(
                claimed.generation,
                "attempt-a",
                ownerA,
                150L,
                SourceScanAttemptProgress(phase, explored, loaded, evaluated),
            )
        )
        return claimed
    }

    private fun assertProcessDeathAt(phase: String, explored: Int, loaded: Int, evaluated: Int) {
        prepareAttemptAt(phase, explored, loaded, evaluated)
        val recovered =
            requireNotNull(
                settings.recoverInterruptedSourceConfiguration(processRecreatedOwner, 200L)
            )
        assertEquals(SourceConfigurationCheckpoint.State.INTERRUPTED, recovered.state)
        assertEquals(SourceScanAttemptOutcome.PROCESS_INTERRUPTED, recovered.terminalOutcome)
        assertEquals(phase, recovered.attempt?.progress?.phase)
        assertEquals(explored, recovered.attempt?.progress?.explored)
    }

    private fun complete(
        checkpoint: SourceConfigurationCheckpoint,
        owner: SourceScanAttemptOwner,
        completion: SourceScanAttemptCompletion,
        nowMs: Long,
    ): Boolean =
        settings.completeSourceConfigurationAttempt(
            checkpoint.generation,
            requireNotNull(checkpoint.attemptId),
            owner,
            nowMs,
            completion,
        )

    private fun completion(
        outcome: SourceScanAttemptOutcome,
        unresolved: Set<String> = emptySet(),
        revision: UUID? = null,
        libraryState: LibraryState? = null,
        lastScanFailed: Boolean,
        failureClass: String? = null,
    ) =
        SourceScanAttemptCompletion(
            outcome = outcome,
            unresolvedSourceKeys = unresolved,
            reason = outcome.name,
            failureClass = failureClass,
            publishedRevision = revision,
            publishedLibraryState = libraryState,
            lastScanFailed = lastScanFailed,
        )

    private fun successCompletion(revision: UUID) =
        completion(
            SourceScanAttemptOutcome.SUCCESS,
            revision = revision,
            libraryState = LibraryState.USABLE,
            lastScanFailed = false,
        )

    private fun assertTerminal(
        state: SourceConfigurationCheckpoint.State,
        lastScanFailed: Boolean,
    ) {
        val checkpoint = requireNotNull(settings.sourceConfigurationCheckpoint)
        assertEquals(state, checkpoint.state)
        assertNotNull(checkpoint.terminalAtMs)
        assertNotNull(checkpoint.terminalOutcome)
        assertEquals(lastScanFailed, settings.lastScanFailed)
    }

    private companion object {
        val ownerA = SourceScanAttemptOwner("process-a", "service-a")
        val ownerB = SourceScanAttemptOwner("process-b", "service-b")
        val ownerSameProcess = SourceScanAttemptOwner("process-a", "service-b")
        val processRecreatedOwner = SourceScanAttemptOwner("process-new", "service-new")
    }
}
