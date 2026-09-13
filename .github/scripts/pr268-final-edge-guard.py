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
