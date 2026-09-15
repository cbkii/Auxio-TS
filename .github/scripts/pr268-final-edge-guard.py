from pathlib import Path


def replace_once(path: Path, old: str, new: str, label: str) -> None:
    text = path.read_text()
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{label}: expected one match, found {count}")
    path.write_text(text.replace(old, new, 1))


policy = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/persist/PrimitiveQueueIntegrityPolicy.kt"
)
policy_test = Path(
    "app/src/test/java/org/oxycblt/auxio/playback/persist/PrimitiveQueueIntegrityPolicyTest.kt"
)
database = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/persist/PersistenceDatabase.kt"
)
repository = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/persist/PersistenceRepository.kt"
)
holder = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt"
)
fast_resume_policy = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/FastResumeCanonicalHandoffPolicy.kt"
)
fast_resume_policy_test = Path(
    "app/src/test/java/org/oxycblt/auxio/playback/service/FastResumeCanonicalHandoffPolicyTest.kt"
)
media_session = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/MediaSessionInterface.kt"
)
playback_state_manager = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/state/PlaybackStateManager.kt"
)
cold_control_test = Path(
    "app/src/test/java/org/oxycblt/auxio/playback/state/DeferredPlaybackColdControlPolicyTest.kt"
)
restore_arbiter_test = Path(
    "app/src/test/java/org/oxycblt/auxio/playback/service/RestoreIntentArbiterTest.kt"
)
playback_fragment = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/PlaybackServiceFragment.kt"
)
preferences_audio = Path("app/src/main/res/xml/preferences_audio.xml")
strings_pr268 = Path("app/src/main/res/values/strings_pr268.xml")

# A Fast Resume snapshot belongs only to the exact persisted current queue item that created it.
replace_once(
    policy,
    """    fun validTotalCount(declaredCount: Int, actualCount: Int, currentPosition: Int): Int? {
        if (declaredCount <= 0 || actualCount != declaredCount) return null
        if (currentPosition !in 0 until declaredCount) return null
        return declaredCount
    }
""",
    """    fun validTotalCount(declaredCount: Int, actualCount: Int, currentPosition: Int): Int? {
        if (declaredCount <= 0 || actualCount != declaredCount) return null
        if (currentPosition !in 0 until declaredCount) return null
        return declaredCount
    }

    fun canEnrichCurrentItem(descriptorCurrentPosition: Int, logicalPosition: Int): Boolean =
        logicalPosition == descriptorCurrentPosition
""",
    "current-item enrichment policy",
)

replace_once(
    policy_test,
    """    fun invalidCurrentPositionIsRejected() {
        assertNull(
            PrimitiveQueueIntegrityPolicy.validTotalCount(
                declaredCount = 3,
                actualCount = 3,
                currentPosition = 3,
            )
        )
    }
""",
    """    fun invalidCurrentPositionIsRejected() {
        assertNull(
            PrimitiveQueueIntegrityPolicy.validTotalCount(
                declaredCount = 3,
                actualCount = 3,
                currentPosition = 3,
            )
        )
    }

    @Test
    fun fastResumeSnapshotMayOnlyEnrichExactPersistedCurrentItem() {
        assertEquals(
            true,
            PrimitiveQueueIntegrityPolicy.canEnrichCurrentItem(
                descriptorCurrentPosition = 3,
                logicalPosition = 3,
            ),
        )
    }

    @Test
    fun coldSkipTargetCannotInheritPreviousCurrentSnapshot() {
        assertEquals(
            false,
            PrimitiveQueueIntegrityPolicy.canEnrichCurrentItem(
                descriptorCurrentPosition = 3,
                logicalPosition = 4,
            ),
        )
    }
""",
    "snapshot enrichment policy tests",
)

# Guard the Room write itself so a concurrent queue move/revision cannot race a prior check.
replace_once(
    database,
    """    @Query(
        "UPDATE QueueItemRefEntity SET uri = COALESCE(:uri, uri), " +
            "pathFallback = COALESCE(:pathFallback, pathFallback), " +
            "titleFallback = COALESCE(:titleFallback, titleFallback), " +
            "artistFallback = COALESCE(:artistFallback, artistFallback), " +
            "albumFallback = COALESCE(:albumFallback, albumFallback), " +
            "durationMs = CASE WHEN :durationMs > 0 THEN :durationMs ELSE durationMs END " +
            "WHERE sessionId = :sessionId AND logicalPosition = :logicalPosition"
    )
    suspend fun enrichQueueItem(
        sessionId: Long,
        logicalPosition: Int,
        uri: String?,
        pathFallback: String?,
        titleFallback: String?,
        artistFallback: String?,
        albumFallback: String?,
        durationMs: Long,
    ): Int
""",
    """    @Query(
        "UPDATE QueueItemRefEntity SET uri = COALESCE(:uri, uri), " +
            "pathFallback = COALESCE(:pathFallback, pathFallback), " +
            "titleFallback = COALESCE(:titleFallback, titleFallback), " +
            "artistFallback = COALESCE(:artistFallback, artistFallback), " +
            "albumFallback = COALESCE(:albumFallback, albumFallback), " +
            "durationMs = CASE WHEN :durationMs > 0 THEN :durationMs ELSE durationMs END " +
            "WHERE sessionId = :sessionId AND logicalPosition = :logicalPosition " +
            "AND logicalPosition = :expectedCurrentLogicalPosition " +
            "AND EXISTS (SELECT 1 FROM QueueSessionEntity " +
            "WHERE id = :sessionId AND revision = :expectedRevision " +
            "AND currentLogicalPosition = :expectedCurrentLogicalPosition)"
    )
    suspend fun enrichQueueItem(
        sessionId: Long,
        logicalPosition: Int,
        expectedRevision: Long,
        expectedCurrentLogicalPosition: Int,
        uri: String?,
        pathFallback: String?,
        titleFallback: String?,
        artistFallback: String?,
        albumFallback: String?,
        durationMs: Long,
    ): Int
""",
    "atomically guard queue enrichment",
)

replace_once(
    repository,
    """    /** Fill missing primitive playback metadata without replacing logical queue order. */
    suspend fun enrichQueueItem(
""",
    """    /** Fill missing metadata for the exact persisted current item only. */
    suspend fun enrichQueueItem(
""",
    "repository enrichment contract",
)

replace_once(
    repository,
    """    override suspend fun enrichQueueItem(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        snapshot: FastResumeSnapshot,
    ): Boolean =
        try {
            queueDao.enrichQueueItem(
                sessionId = descriptor.sessionId,
                logicalPosition = logicalPosition,
                uri = snapshot.uri.takeIf { it.isNotBlank() },
                pathFallback = snapshot.path,
                titleFallback = snapshot.title,
                artistFallback = snapshot.artist,
                albumFallback = snapshot.album,
                durationMs = snapshot.durationMs.coerceAtLeast(0L),
            ) == 1
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            L.w(e, "Unable to enrich primitive queue item")
            false
        }
""",
    """    override suspend fun enrichQueueItem(
        descriptor: QueueDescriptor,
        logicalPosition: Int,
        snapshot: FastResumeSnapshot,
    ): Boolean =
        try {
            if (
                !PrimitiveQueueIntegrityPolicy.canEnrichCurrentItem(
                    descriptorCurrentPosition = descriptor.currentLogicalPosition,
                    logicalPosition = logicalPosition,
                )
            ) {
                L.w(
                    "Refusing Fast Resume snapshot enrichment for non-current queue item " +
                        "[session=${descriptor.sessionId} revision=${descriptor.revision} " +
                        "descriptorCurrent=${descriptor.currentLogicalPosition} logical=$logicalPosition]"
                )
                false
            } else {
                queueDao.enrichQueueItem(
                    sessionId = descriptor.sessionId,
                    logicalPosition = logicalPosition,
                    expectedRevision = descriptor.revision,
                    expectedCurrentLogicalPosition = descriptor.currentLogicalPosition,
                    uri = snapshot.uri.takeIf { it.isNotBlank() },
                    pathFallback = snapshot.path,
                    titleFallback = snapshot.title,
                    artistFallback = snapshot.artist,
                    albumFallback = snapshot.album,
                    durationMs = snapshot.durationMs.coerceAtLeast(0L),
                ) == 1
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            L.w(e, "Unable to enrich primitive queue item")
            false
        }
""",
    "guard durable snapshot enrichment",
)

replace_once(
    holder,
    """                                persistenceRepository.enrichQueueItem(requested, target, snapshot)
""",
    """                                persistenceRepository.enrichQueueItem(descriptor, target, snapshot)
""",
    "preserve original descriptor authority during enrichment",
)

# Clear primitive bookkeeping before Media3 topology mutation so synchronous callbacks cannot
# persist a stale primitive window. Failure reconstruction restores the captured window in-place.
replace_once(
    holder,
    """        Ts18FirstAudioLatency.mark("canonical_commit_start")
        if (
            !installCanonicalQueueAroundCurrentSource(
""",
    """        Ts18FirstAudioLatency.mark("canonical_commit_start")
        activePrimitiveWindow = null
        if (
            !installCanonicalQueueAroundCurrentSource(
""",
    "clear primitive authority before canonical mutation",
)

# Generic canonical persistence is forbidden while any raw Fast Resume authority is active. A
# descriptor-backed raw state may update only its persisted position; direct-open raw authority
# preserves the prior canonical queue until canonicalisation produces a replacement.
replace_once(
    fast_resume_policy,
    """    fun rawCurrentMatchesExpected(expectedUid: Music.UID?, resolvedRawUid: Music.UID?): Boolean =
        expectedUid != null && resolvedRawUid == expectedUid

    /**
""",
    """    fun rawCurrentMatchesExpected(expectedUid: Music.UID?, resolvedRawUid: Music.UID?): Boolean =
        expectedUid != null && resolvedRawUid == expectedUid

    fun suppressCanonicalPersistence(rawAuthorityActive: Boolean): Boolean = rawAuthorityActive

    /**
""",
    "raw authority persistence policy",
)

replace_once(
    fast_resume_policy_test,
    """    fun persistedQueueAlwaysWinsOverAllSongsFallback() {
""",
    """    fun rawAuthorityAlwaysSuppressesGenericCanonicalPersistence() {
        assertTrue(FastResumeCanonicalHandoffPolicy.suppressCanonicalPersistence(true))
        assertFalse(FastResumeCanonicalHandoffPolicy.suppressCanonicalPersistence(false))
    }

    @Test
    fun persistedQueueAlwaysWinsOverAllSongsFallback() {
""",
    "raw persistence policy test",
)

replace_once(
    holder,
    """        val rawState =
            withContext(Dispatchers.Main) {
                rawFastResumeQueueDescriptor?.let {
                    Triple(it, player.currentPosition.coerceAtLeast(0L), repeatMode)
                }
            }
        if (rawState != null) {
            val (descriptor, positionMs, rawRepeatMode) = rawState
            persistenceRepository.updateQueuePosition(
                descriptor = descriptor,
                logicalPosition = descriptor.currentLogicalPosition,
                positionMs = positionMs,
                repeatMode = rawRepeatMode,
            )
            return
        }

        persistenceRepository.saveState(playbackManager.toSavedState())
""",
    """        val rawAuthorityActive = withContext(Dispatchers.Main) { rawFastResumeItem != null }
        if (FastResumeCanonicalHandoffPolicy.suppressCanonicalPersistence(rawAuthorityActive)) {
            val rawState =
                withContext(Dispatchers.Main) {
                    rawFastResumeQueueDescriptor?.let {
                        Triple(it, player.currentPosition.coerceAtLeast(0L), repeatMode)
                    }
                }
            if (rawState != null) {
                val (descriptor, positionMs, rawRepeatMode) = rawState
                persistenceRepository.updateQueuePosition(
                    descriptor = descriptor,
                    logicalPosition = descriptor.currentLogicalPosition,
                    positionMs = positionMs,
                    repeatMode = rawRepeatMode,
                )
            }
            return
        }

        persistenceRepository.saveState(playbackManager.toSavedState())
""",
    "suppress canonical persistence during raw authority",
)

# Pause is latest-wins input to an existing restore. It must not create a cold restore by itself.
replace_once(
    media_session,
    """        if (hasCurrentMedia()) {
            playbackManager.playing(false)
        }
""",
    """        playbackManager.playing(false)
""",
    "unconditionally forward pause to playback manager",
)

replace_once(
    playback_state_manager,
    """class PlaybackStateManagerImpl @Inject constructor() : PlaybackStateManager {
""",
    """internal object DeferredPlaybackColdControlPolicy {
    fun applyPause(pending: DeferredPlayback?): DeferredPlayback? =
        if (pending is DeferredPlayback.RestoreState) pending.copy(play = false) else pending
}

class PlaybackStateManagerImpl @Inject constructor() : PlaybackStateManager {
""",
    "cold pause coalescing policy",
)

replace_once(
    playback_state_manager,
    """    @Synchronized
    override fun playing(isPlaying: Boolean) {
        val stateHolder = stateHolder ?: return
        L.d("Updating playing state to $isPlaying")
        stateHolder.playing(isPlaying)
    }
""",
    """    @Synchronized
    override fun playing(isPlaying: Boolean) {
        val stateHolder = stateHolder
        if (stateHolder == null) {
            if (!isPlaying) {
                pendingDeferredPlayback =
                    DeferredPlaybackColdControlPolicy.applyPause(pendingDeferredPlayback)
            }
            return
        }
        L.d("Updating playing state to $isPlaying")
        stateHolder.playing(isPlaying)
    }
""",
    "coalesce pause into pending cold restore",
)

if cold_control_test.exists():
    raise SystemExit("DeferredPlaybackColdControlPolicyTest.kt unexpectedly already exists")
cold_control_test.write_text(
    """/*
 * Copyright (c) 2026 Auxio Project
 * DeferredPlaybackColdControlPolicyTest.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.playback.state

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class DeferredPlaybackColdControlPolicyTest {
    @Test
    fun pauseOverridesPendingRestoreWithoutCreatingFallback() {
        val pending = DeferredPlayback.RestoreState(play = true)
        val result = DeferredPlaybackColdControlPolicy.applyPause(pending)
        assertEquals(DeferredPlayback.RestoreState(play = false), result)
    }

    @Test
    fun pauseDoesNotCreateOrReplaceNonRestoreWork() {
        assertEquals(null, DeferredPlaybackColdControlPolicy.applyPause(null))
        val shuffle = DeferredPlayback.ShuffleAll()
        assertSame(shuffle, DeferredPlaybackColdControlPolicy.applyPause(shuffle))
    }
}
"""
)

# Active restore path: the arbiter also follows latest-wins Pause semantics.
replace_once(
    restore_arbiter_test,
    """        assertTrue(arbiter.snapshot().play)
""",
    """        assertTrue(arbiter.snapshot().play)
        assertFalse(arbiter.begin(DeferredPlayback.RestoreState(play = false)))
        assertFalse(arbiter.snapshot().play)
""",
    "latest pause wins restore arbiter test",
)

# Cold Topway seek starts a restore and must get the same bounded watchdog as other ingress paths.
replace_once(
    playback_fragment,
    """                    ) {
                        playbackManager.playDeferred(DeferredPlayback.RestoreState(play = false))
                    }
                    playbackManager.seekTo(positionMs)
                    publishTopwayProgress("launcher-seek", force = true)
""",
    """                    ) {
                        playbackManager.playDeferred(DeferredPlayback.RestoreState(play = false))
                        scheduleRestoreWatchdog()
                    }
                    playbackManager.seekTo(positionMs)
                    publishTopwayProgress("launcher-seek", force = true)
""",
    "supervise cold Topway seek restore",
)

# Keep user-visible residency copy on the normal Android resource surface.
replace_once(
    preferences_audio,
    """<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto" xmlns:tools="http://schemas.android.com/tools" app:title="@string/set_audio">
""",
    """<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto" app:title="@string/set_audio">
""",
    "remove unused tools namespace",
)

replace_once(
    preferences_audio,
    """        <SwitchPreferenceCompat app:defaultValue="true" app:key="auxio_keep_playback_ready" app:summary="Keep the canonical TS18 playback service and prepared current track resident while paused for faster media-control response." app:title="Keep playback ready" tools:ignore="HardcodedText" />
""",
    """        <SwitchPreferenceCompat app:defaultValue="true" app:key="auxio_keep_playback_ready" app:summary="@string/set_keep_playback_ready_desc" app:title="@string/set_keep_playback_ready" />
""",
    "resource playback residency preference text",
)

if strings_pr268.exists():
    raise SystemExit("strings_pr268.xml unexpectedly already exists")
strings_pr268.write_text(
    """<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="set_keep_playback_ready">Keep playback ready</string>
    <string name="set_keep_playback_ready_desc">Keep the canonical TS18 playback service and prepared current track resident while paused for faster media-control response.</string>
</resources>
"""
)
