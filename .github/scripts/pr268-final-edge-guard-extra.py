from pathlib import Path


def replace_once(path: Path, old: str, new: str, label: str) -> None:
    text = path.read_text()
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{label}: expected one match, found {count}")
    path.write_text(text.replace(old, new, 1))


holder = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt"
)
policy = Path(
    "app/src/main/java/org/oxycblt/auxio/playback/service/FastResumeCanonicalHandoffPolicy.kt"
)
policy_test = Path(
    "app/src/test/java/org/oxycblt/auxio/playback/service/FastResumeCanonicalHandoffPolicyTest.kt"
)
music_repository = Path("app/src/main/java/org/oxycblt/auxio/music/MusicRepository.kt")
authority_test = Path(
    "app/src/test/java/org/oxycblt/auxio/music/DeviceLibraryAuthorityPolicyTest.kt"
)

# A cold Next/Previous is an intent against the future canonical queue. It must not retarget the
# single raw snapshot, because that snapshot still identifies the persisted current item.
replace_once(
    policy,
    """    fun descriptorForRawIntent(
        descriptor: QueueDescriptor?,
        skipDelta: Int,
        seekPositionMs: Long?,
    ): QueueDescriptor? {
        descriptor ?: return null
        val target =
            (descriptor.currentLogicalPosition.toLong() + skipDelta)
                .coerceIn(0L, (descriptor.totalCount - 1).coerceAtLeast(0).toLong())
                .toInt()
        val position = seekPositionMs ?: if (skipDelta != 0) 0L else descriptor.positionMs
        return descriptor.copy(
            currentLogicalPosition = target,
            positionMs = position.coerceAtLeast(0L),
        )
    }
""",
    """    fun descriptorForRawIntent(
        descriptor: QueueDescriptor?,
        skipDelta: Int,
        seekPositionMs: Long?,
    ): QueueDescriptor? {
        descriptor ?: return null
        // Keep raw identity pinned to the persisted current item. A cold skip is replayed only
        // after that item has been reconciled into the complete canonical queue.
        val position =
            if (skipDelta == 0) seekPositionMs ?: descriptor.positionMs else descriptor.positionMs
        return descriptor.copy(positionMs = position.coerceAtLeast(0L))
    }

    fun targetLogicalPosition(currentLogicalPosition: Int, totalCount: Int, skipDelta: Int): Int {
        if (totalCount <= 0) return 0
        return (currentLogicalPosition.toLong() + skipDelta)
            .coerceIn(0L, (totalCount - 1).toLong())
            .toInt()
    }
""",
    "keep raw descriptor identity stable across cold navigation",
)

replace_once(
    policy_test,
    """    fun rawDescriptorFoldsColdSkipAndSeekWithoutChangingQueueIdentity() {
        val descriptor =
            QueueDescriptor(
                sessionId = 42L,
                totalCount = 8,
                currentLogicalPosition = 3,
                positionMs = 9_000L,
                repeatMode = RepeatMode.ALL,
                shuffleScope = ShuffleScope.ALL,
                revision = 7L,
                updatedAtMs = 123L,
            )

        val skipped =
            requireNotNull(
                FastResumeCanonicalHandoffPolicy.descriptorForRawIntent(
                    descriptor = descriptor,
                    skipDelta = 2,
                    seekPositionMs = null,
                )
            )
        assertEquals(42L, skipped.sessionId)
        assertEquals(7L, skipped.revision)
        assertEquals(5, skipped.currentLogicalPosition)
        assertEquals(0L, skipped.positionMs)
        assertEquals(ShuffleScope.ALL, skipped.shuffleScope)

        val sought =
            requireNotNull(
                FastResumeCanonicalHandoffPolicy.descriptorForRawIntent(
                    descriptor = descriptor,
                    skipDelta = 0,
                    seekPositionMs = 4_321L,
                )
            )
        assertEquals(3, sought.currentLogicalPosition)
        assertEquals(4_321L, sought.positionMs)
    }
""",
    """    fun rawDescriptorKeepsPhysicalIdentityUntilCanonicalNavigationCanRun() {
        val descriptor =
            QueueDescriptor(
                sessionId = 42L,
                totalCount = 8,
                currentLogicalPosition = 3,
                positionMs = 9_000L,
                repeatMode = RepeatMode.ALL,
                shuffleScope = ShuffleScope.ALL,
                revision = 7L,
                updatedAtMs = 123L,
            )

        val skipped =
            requireNotNull(
                FastResumeCanonicalHandoffPolicy.descriptorForRawIntent(
                    descriptor = descriptor,
                    skipDelta = 2,
                    seekPositionMs = null,
                )
            )
        assertEquals(42L, skipped.sessionId)
        assertEquals(7L, skipped.revision)
        assertEquals(3, skipped.currentLogicalPosition)
        assertEquals(9_000L, skipped.positionMs)
        assertEquals(RepeatMode.ALL, skipped.repeatMode)
        assertEquals(ShuffleScope.ALL, skipped.shuffleScope)
        assertEquals(5, FastResumeCanonicalHandoffPolicy.targetLogicalPosition(3, 8, 2))
        assertEquals(0, FastResumeCanonicalHandoffPolicy.targetLogicalPosition(3, 8, -99))
        assertEquals(7, FastResumeCanonicalHandoffPolicy.targetLogicalPosition(3, 8, 99))

        val sought =
            requireNotNull(
                FastResumeCanonicalHandoffPolicy.descriptorForRawIntent(
                    descriptor = descriptor,
                    skipDelta = 0,
                    seekPositionMs = 4_321L,
                )
            )
        assertEquals(3, sought.currentLogicalPosition)
        assertEquals(4_321L, sought.positionMs)
        assertEquals(RepeatMode.ALL, sought.repeatMode)
    }
""",
    "raw cold-navigation policy regression",
)

# Keep pending cold navigation separate from the raw physical item and preserve persisted repeat.
replace_once(
    holder,
    """    private var rawFastResumeQueueDescriptor: QueueDescriptor? = null
    private var rawFastResumeAllowsAllSongsFallback = false
    private var rawFastResumeReconciliationJob: Job? = null
""",
    """    private var rawFastResumeQueueDescriptor: QueueDescriptor? = null
    private var rawFastResumeAllowsAllSongsFallback = false
    private var rawFastResumePendingNavigation: RawPendingNavigation? = null
    private var rawFastResumeReconciliationJob: Job? = null
""",
    "raw pending navigation state",
)

replace_once(
    holder,
    """    private data class CanonicalCurrentSourceLease(val song: Song)

    private data class PersistedQueueHydration(
""",
    """    private data class CanonicalCurrentSourceLease(val song: Song)

    private data class RawPendingNavigation(val skipDelta: Int, val seekPositionMs: Long?)

    private data class PersistedQueueHydration(
""",
    "raw pending navigation model",
)

replace_once(
    holder,
    """                    startRawFastResume(
                        item = validation.item,
                        play = shouldPlayImmediately(finalIntent.play),
                        descriptor = rawDescriptor,
                        allowAllSongsFallback = rawDescriptor == null,
                    )
                    rawDescriptor
                        ?.positionMs
                        ?.takeIf {
                            finalIntent.seekPositionMs != null || finalIntent.skipDelta != 0
                        }
                        ?.let(player::seekTo)
""",
    """                    startRawFastResume(
                        item = validation.item,
                        play = shouldPlayImmediately(finalIntent.play),
                        descriptor = rawDescriptor,
                        allowAllSongsFallback = rawDescriptor == null,
                        pendingNavigation =
                            RawPendingNavigation(
                                skipDelta = finalIntent.skipDelta,
                                seekPositionMs = finalIntent.seekPositionMs,
                            ),
                    )
""",
    "carry cold navigation separately from raw descriptor",
)

replace_once(
    holder,
    """    private fun startRawFastResume(
        item: RawFastResumeItem,
        play: Boolean,
        descriptor: QueueDescriptor?,
        allowAllSongsFallback: Boolean,
    ) {
""",
    """    private fun startRawFastResume(
        item: RawFastResumeItem,
        play: Boolean,
        descriptor: QueueDescriptor?,
        allowAllSongsFallback: Boolean,
        pendingNavigation: RawPendingNavigation? = null,
    ) {
""",
    "raw start pending-navigation parameter",
)

replace_once(
    holder,
    """        rawFastResumeItem = item
        rawFastResumeQueueDescriptor = descriptor
        rawFastResumeAllowsAllSongsFallback = allowAllSongsFallback
        parent = null
        player.shuffleModeEnabled = false
        player.setMediaItems(listOf(item.buildMediaItem()))
        player.seekTo(0, descriptor?.positionMs ?: item.positionMs)
""",
    """        rawFastResumeItem = item
        rawFastResumeQueueDescriptor = descriptor
        rawFastResumeAllowsAllSongsFallback = allowAllSongsFallback
        rawFastResumePendingNavigation = pendingNavigation
        parent = null
        player.shuffleModeEnabled = false
        descriptor?.repeatMode?.let { persistedRepeat ->
            player.repeatMode =
                when (persistedRepeat) {
                    RepeatMode.NONE -> Player.REPEAT_MODE_OFF
                    RepeatMode.ALL -> Player.REPEAT_MODE_ALL
                    RepeatMode.TRACK -> Player.REPEAT_MODE_ONE
                }
        }
        player.setMediaItems(listOf(item.buildMediaItem()))
        val rawPositionMs =
            descriptor?.positionMs
                ?: pendingNavigation
                    ?.takeIf { it.skipDelta == 0 }
                    ?.seekPositionMs
                ?: item.positionMs
        player.seekTo(0, rawPositionMs.coerceAtLeast(0L))
""",
    "preserve repeat and seek without retargeting raw item",
)

replace_once(
    holder,
    """        rawFastResumeItem = null
        rawFastResumeQueueDescriptor = null
        rawFastResumeAllowsAllSongsFallback = false
        if (cancelReconciliation) rawFastResumeReconciliationJob?.cancel()
""",
    """        rawFastResumeItem = null
        rawFastResumeQueueDescriptor = null
        rawFastResumeAllowsAllSongsFallback = false
        rawFastResumePendingNavigation = null
        if (cancelReconciliation) rawFastResumeReconciliationJob?.cancel()
""",
    "clear raw pending navigation",
)

replace_once(
    holder,
    """        val raw = rawFastResumeItem ?: return
        val descriptor = rawFastResumeQueueDescriptor
        val allowAllSongsFallback = rawFastResumeAllowsAllSongsFallback
""",
    """        val raw = rawFastResumeItem ?: return
        val descriptor = rawFastResumeQueueDescriptor
        val allowAllSongsFallback = rawFastResumeAllowsAllSongsFallback
        val pendingNavigation = rawFastResumePendingNavigation
""",
    "capture raw pending navigation for reconciliation",
)

replace_once(
    holder,
    """                    val positionBefore = player.currentPosition.coerceAtLeast(0L)
                    val audioSessionBefore = player.audioSessionId
                    if (
""",
    """                    val pendingTarget =
                        pendingNavigation?.takeIf { it.skipDelta != 0 }?.let { navigation ->
                            val currentLogicalPosition =
                                descriptor?.currentLogicalPosition ?: hydration.currentHeapIndex
                            val targetLogicalPosition =
                                FastResumeCanonicalHandoffPolicy.targetLogicalPosition(
                                    currentLogicalPosition = currentLogicalPosition,
                                    totalCount = hydration.songs.size,
                                    skipDelta = navigation.skipDelta,
                                )
                            val targetHeapIndex =
                                if (hydration.shuffledMapping.isEmpty()) {
                                    targetLogicalPosition
                                } else {
                                    hydration.shuffledMapping.getOrNull(targetLogicalPosition)
                                }
                            if (
                                targetHeapIndex == null ||
                                    targetHeapIndex !in hydration.songs.indices
                            ) {
                                L.w(
                                    "Unable to map pending cold navigation into canonical queue " +
                                        "[logical=$targetLogicalPosition count=${hydration.songs.size}]"
                                )
                                rawFastResumeReconciliationJob = null
                                armFastResumeCanonicalRetry()
                                return@withContext
                            }
                            targetHeapIndex to
                                (navigation.seekPositionMs ?: 0L).coerceAtLeast(0L)
                        }

                    val positionBefore = player.currentPosition.coerceAtLeast(0L)
                    val audioSessionBefore = player.audioSessionId
                    if (
""",
    "validate pending cold navigation before canonical mutation",
)

replace_once(
    holder,
    """                    currentSaveJob?.cancel()
                    currentSaveJob = null
                    clearRawFastResumeState(cancelReconciliation = false)
""",
    """                    pendingTarget?.let { (targetIndex, targetPositionMs) ->
                        player.seekTo(targetIndex, targetPositionMs)
                    }
                    currentSaveJob?.cancel()
                    currentSaveJob = null
                    clearRawFastResumeState(cancelReconciliation = false)
""",
    "replay pending cold navigation only after canonical install",
)

replace_once(
    holder,
    """                    L.i(
                        "Reconciled raw Fast Resume to canonical authority without current-source " +
                            "reset [persisted=${descriptor != null} count=${hydration.songs.size} " +
""",
    """                    L.i(
                        "Reconciled raw Fast Resume to canonical authority " +
                            "[persisted=${descriptor != null} pendingSkip=${pendingNavigation?.skipDelta ?: 0} " +
                            "count=${hydration.songs.size} " +
""",
    "accurate raw reconciliation log",
)

# Source authority is a separate state transition from library-content mutation. Publish it even
# when a successful scan leaves the library object unchanged, and never trust a published outcome
# that has already diverged from the repository's current source outcome.
replace_once(
    music_repository,
    """    data class Changes(
        val deviceLibrary: Boolean,
        val userLibrary: Boolean,
        val deviceGeneration: Long,
        val userGeneration: Long,
        val deviceSourceScanOutcome: SourceScanOutcome? = null,
    )
""",
    """    data class Changes(
        val deviceLibrary: Boolean,
        val userLibrary: Boolean,
        val deviceGeneration: Long,
        val userGeneration: Long,
        val deviceSourceScanOutcome: SourceScanOutcome? = null,
        val deviceSourceAuthority: Boolean = false,
    )
""",
    "source authority change flag",
)

replace_once(
    music_repository,
    """                userGeneration = userLibraryGeneration.get(),
                deviceSourceScanOutcome = deviceAuthority.sourceScanOutcome,
""",
    """                userGeneration = userLibraryGeneration.get(),
                deviceSourceScanOutcome = deviceAuthority.sourceScanOutcome,
                deviceSourceAuthority = true,
""",
    "late listener receives authority snapshot",
)

replace_once(
    music_repository,
    """    private fun currentDeviceLibraryAuthority(): DeviceLibraryAuthority =
        synchronized(this) {
            DeviceLibraryAuthorityPolicy.coherentSnapshot(
                currentGeneration = deviceLibraryGeneration.get(),
                published = publishedDeviceLibraryAuthority,
            )
        }
""",
    """    private fun currentDeviceLibraryAuthority(): DeviceLibraryAuthority =
        synchronized(this) {
            DeviceLibraryAuthorityPolicy.coherentSnapshot(
                currentGeneration = deviceLibraryGeneration.get(),
                published = publishedDeviceLibraryAuthority,
                currentSourceOutcome = lastSourceScanOutcome,
            )
        }
""",
    "late listener rejects stale source authority",
)

replace_once(
    music_repository,
    """internal object DeviceLibraryAuthorityPolicy {
    fun coherentSnapshot(
        currentGeneration: Long,
        published: DeviceLibraryAuthority?,
    ): DeviceLibraryAuthority =
        published?.takeIf { it.generation == currentGeneration }
            ?: DeviceLibraryAuthority(currentGeneration, null)
}
""",
    """internal object DeviceLibraryAuthorityPolicy {
    fun coherentSnapshot(
        currentGeneration: Long,
        published: DeviceLibraryAuthority?,
        currentSourceOutcome: SourceScanOutcome? = published?.sourceScanOutcome,
    ): DeviceLibraryAuthority =
        published?.takeIf {
            it.generation == currentGeneration && it.sourceScanOutcome == currentSourceOutcome
        } ?: DeviceLibraryAuthority(currentGeneration, null)
}
""",
    "coherent authority must match current source outcome",
)

replace_once(
    music_repository,
    """                deviceSourceScanOutcome = if (device) deviceAuthority.sourceScanOutcome else null,
            )
""",
    """                deviceSourceScanOutcome = if (device) deviceAuthority.sourceScanOutcome else null,
                deviceSourceAuthority = device,
            )
""",
    "library changes publish source authority coherently",
)

replace_once(
    music_repository,
    """        L.d("Dispatching library change [changes=$changes]")
        for (listener in updateListeners) {
            listener.onMusicChanges(changes)
        }
    }
}
""",
    """        L.d("Dispatching library change [changes=$changes]")
        for (listener in updateListeners) {
            listener.onMusicChanges(changes)
        }
    }

    private fun dispatchDeviceSourceAuthorityChange() {
        val deviceAuthority =
            synchronized(this) {
                DeviceLibraryAuthority(
                        generation = deviceLibraryGeneration.incrementAndGet(),
                        sourceScanOutcome = lastSourceScanOutcome,
                    )
                    .also { publishedDeviceLibraryAuthority = it }
            }
        val changes =
            MusicRepository.Changes(
                deviceLibrary = false,
                userLibrary = false,
                deviceGeneration = deviceAuthority.generation,
                userGeneration = userLibraryGeneration.get(),
                deviceSourceScanOutcome = deviceAuthority.sourceScanOutcome,
                deviceSourceAuthority = true,
            )
        L.d("Dispatching device source authority change [changes=$changes]")
        for (listener in updateListeners) {
            listener.onMusicChanges(changes)
        }
    }
}
""",
    "standalone source authority dispatcher",
)

replace_once(
    music_repository,
    """    override fun markSourcesTemporarilyUnavailable(sourceKeys: Set<String>) {
        if (sourceKeys.isEmpty()) return
        lastSourceScanOutcome = SourceScanOutcome.TemporarilyUnavailable(sourceKeys)
        musicSettings.markSourcesUnresolved(sourceKeys, "TemporarilyUnavailable")
        emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
    }
""",
    """    override fun markSourcesTemporarilyUnavailable(sourceKeys: Set<String>) {
        if (sourceKeys.isEmpty()) return
        lastSourceScanOutcome = SourceScanOutcome.TemporarilyUnavailable(sourceKeys)
        musicSettings.markSourcesUnresolved(sourceKeys, "TemporarilyUnavailable")
        dispatchDeviceSourceAuthorityChange()
        emitStartupLibraryStatus(StartupLibraryStatus.SourceUnavailable)
    }
""",
    "publish removable-source authority revocation",
)

replace_once(
    music_repository,
    """                    if (libraryChanged) {
                        withContext(Dispatchers.Main) {
                            dispatchLibraryChange(device = true, user = true)
                        }
                    }
""",
    """                    withContext(Dispatchers.Main) {
                        if (libraryChanged) {
                            dispatchLibraryChange(device = true, user = true)
                        } else if (IndexRequestPolicy.recordsSourceOutcome(request)) {
                            dispatchDeviceSourceAuthorityChange()
                        }
                    }
""",
    "publish successful source authority without library-content mutation",
)

replace_once(
    holder,
    """    private fun authoritativeFastResumeLibrary(): Library? {
        if (!FastResumeCanonicalHandoffPolicy.isAuthoritative(latestDeviceSourceOutcome)) {
            return null
        }
        return musicRepository.library?.takeIf { !it.empty() }
    }
""",
    """    private fun authoritativeFastResumeLibrary(): Library? {
        if (
            latestDeviceSourceOutcome != musicRepository.lastSourceScanOutcome ||
                !FastResumeCanonicalHandoffPolicy.isAuthoritative(latestDeviceSourceOutcome)
        ) {
            return null
        }
        return musicRepository.library?.takeIf { !it.empty() }
    }
""",
    "reject stale published source success at commit boundary",
)

replace_once(
    holder,
    """    override fun onMusicChanges(changes: MusicRepository.Changes) {
        if (!changes.deviceLibrary) return
""",
    """    override fun onMusicChanges(changes: MusicRepository.Changes) {
        if (!changes.deviceLibrary && !changes.deviceSourceAuthority) return
""",
    "holder consumes authority-only changes",
)

replace_once(
    holder,
    """        rawFastResumeItem?.let {
            if (authoritativeLibrary == null) {
                L.i(
                    "Device library generation is not authoritative for raw Fast Resume handoff " +
                        "[generation=${changes.deviceGeneration} " +
                        "outcome=${changes.deviceSourceScanOutcome?.javaClass?.simpleName}]"
                )
                armFastResumeCanonicalRetry()
                return
            }
            L.d("Authoritative library obtained while raw Fast Resume is active; reconciling")
            reconcileRawFastResume(
                library = authoritativeLibrary,
                libraryGeneration = changes.deviceGeneration,
                sourceOutcome = changes.deviceSourceScanOutcome,
            )
            return
        }

        if (library == null) return
""",
    """        rawFastResumeItem?.let {
            if (authoritativeLibrary == null) {
                L.i(
                    "Device library generation is not authoritative for raw Fast Resume handoff " +
                        "[generation=${changes.deviceGeneration} " +
                        "outcome=${changes.deviceSourceScanOutcome?.javaClass?.simpleName}]"
                )
                armFastResumeCanonicalRetry()
                return
            }
            L.d("Authoritative library obtained while raw Fast Resume is active; reconciling")
            reconcileRawFastResume(
                library = authoritativeLibrary,
                libraryGeneration = changes.deviceGeneration,
                sourceOutcome = changes.deviceSourceScanOutcome,
            )
            return
        }

        if (!changes.deviceLibrary) return
        if (library == null) return
""",
    "authority-only change must not trigger ordinary library action",
)

replace_once(
    authority_test,
    """    @Test
    fun currentPartialOutcomeRemainsNonAuthoritativeForLateListener() {
""",
    """    @Test
    fun stalePublishedSuccessIsRejectedWhenCurrentSourceOutcomeChanged() {
        val success = SourceScanOutcome.Success(setOf("internal", "usb"))
        val unavailable = SourceScanOutcome.TemporarilyUnavailable(setOf("usb"))
        val snapshot =
            DeviceLibraryAuthorityPolicy.coherentSnapshot(
                currentGeneration = 9L,
                published = DeviceLibraryAuthority(9L, success),
                currentSourceOutcome = unavailable,
            )

        assertEquals(9L, snapshot.generation)
        assertNull(snapshot.sourceScanOutcome)
    }

    @Test
    fun currentPartialOutcomeRemainsNonAuthoritativeForLateListener() {
""",
    "source authority stale-success regression",
)
