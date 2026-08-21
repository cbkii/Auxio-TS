#!/usr/bin/env python3
"""One-shot branch patcher for PR #258. Deleted by the bootstrap workflow after use."""

from pathlib import Path


def replace_once(path: str, old: str, new: str, label: str) -> None:
    file_path = Path(path)
    text = file_path.read_text()
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"{path}: {label}: expected one anchor, found {count}")
    file_path.write_text(text.replace(old, new, 1))


EXO = "app/src/main/java/org/oxycblt/auxio/playback/service/ExoPlaybackStateHolder.kt"

replace_once(
    EXO,
    """    private var rawFastResumeItem: RawFastResumeItem? = null
    private var activePrimitiveWindow: QueueWindow? = null
    private var primitiveNavigationJob: Job? = null
""",
    """    private var rawFastResumeItem: RawFastResumeItem? = null
    private var activePrimitiveWindow: QueueWindow? = null
    private val primitivePromotionGate = PrimitiveQueuePromotionGate()
    private var primitivePromotionPreparationJob: Job? = null
    private var primitivePromotionPreparationKey: PrimitiveQueuePromotionGate.Key? = null
    private var preparedPrimitivePromotion: PreparedPrimitivePromotion? = null
    private val pendingPrimitivePromotionActions = mutableListOf<() -> Unit>()
    private var primitiveNavigationJob: Job? = null
""",
    "promotion fields",
)

replace_once(
    EXO,
    """    private var pendingPrimitiveTarget: Int? = null
    private var pendingLibraryRestoreAfterRawFailure: DeferredPlayback.RestoreState? = null
    private var markedFirstPlaying = false
""",
    """    private var pendingPrimitiveTarget: Int? = null
    private var pendingLibraryRestoreAfterRawFailure: DeferredPlayback.RestoreState? = null
    private var markedFirstPlaying = false

    private data class PreparedPrimitivePromotion(
        val key: PrimitiveQueuePromotionGate.Key,
        val layout: PrimitiveQueuePromotionPolicy.Layout,
        val songsByCanonicalPosition: List<Song>,
    )
""",
    "prepared promotion value",
)

replace_once(
    EXO,
    """    private fun clearPrimitiveQueueState() {
        primitiveNavigationJob?.cancel()
        primitiveNavigationJob = null
        primitivePrefetchJob?.cancel()
        primitivePrefetchJob = null
        pendingPrimitiveTarget = null
        activePrimitiveWindow = null
    }

    private fun launchPrimitiveMutation(
""",
    """    private fun clearPrimitiveQueueState(clearPromotion: Boolean = true) {
        primitiveNavigationJob?.cancel()
        primitiveNavigationJob = null
        primitivePrefetchJob?.cancel()
        primitivePrefetchJob = null
        pendingPrimitiveTarget = null
        activePrimitiveWindow = null
        if (clearPromotion) clearPrimitivePromotionState("primitive-queue-cleared")
    }

    private fun clearPrimitivePromotionState(reason: String) {
        if (
            preparedPrimitivePromotion != null ||
                primitivePromotionPreparationJob?.isActive == true ||
                pendingPrimitivePromotionActions.isNotEmpty()
        ) {
            L.d("Clearing Fast Resume canonical promotion state [reason=$reason]")
        }
        primitivePromotionPreparationJob?.cancel()
        primitivePromotionPreparationJob = null
        primitivePromotionPreparationKey = null
        preparedPrimitivePromotion = null
        pendingPrimitivePromotionActions.clear()
        primitivePromotionGate.clear()
    }

    private fun QueueWindow.promotionKey() =
        PrimitiveQueuePromotionGate.Key(descriptor.sessionId, descriptor.revision)

    private fun preparePrimitivePromotion(library: Library, force: Boolean = false) {
        val active = activePrimitiveWindow ?: return
        val descriptor = active.descriptor
        val key = active.promotionKey()
        if (!force && preparedPrimitivePromotion?.key == key) return
        if (
            !force &&
                primitivePromotionPreparationJob?.isActive == true &&
                primitivePromotionPreparationKey == key
        ) {
            return
        }

        primitivePromotionPreparationJob?.cancel()
        primitivePromotionPreparationKey = key
        preparedPrimitivePromotion = null
        primitivePromotionPreparationJob =
            restoreScope.launch {
                val allItems = persistenceRepository.readAllQueueItems(descriptor)
                val layout = PrimitiveQueuePromotionPolicy.layout(descriptor, allItems)
                val songsByCanonicalPosition: List<Song?> =
                    layout
                        ?.itemsByCanonicalPosition
                        ?.map { item ->
                            item.stableSongUid?.let(library::findSong)
                                ?: item.uri?.let { uri ->
                                    library.songs.firstOrNull { it.uri.toString() == uri }
                                }
                                ?: item.pathFallback?.let { path ->
                                    library.songs.firstOrNull { it.path.toString() == path }
                                }
                        } ?: emptyList()
                val complete =
                    layout != null &&
                        songsByCanonicalPosition.size == layout.itemsByCanonicalPosition.size &&
                        songsByCanonicalPosition.all { it != null }

                withContext(Dispatchers.Main) {
                    if (primitivePromotionPreparationKey != key) return@withContext
                    primitivePromotionPreparationJob = null
                    primitivePromotionPreparationKey = null
                    val current = activePrimitiveWindow ?: return@withContext
                    val currentKey = current.promotionKey()
                    if (currentKey != key) {
                        L.d(
                            "Discarding stale Fast Resume canonical preparation " +
                                "[prepared=$key current=$currentKey]"
                        )
                        if (pendingPrimitivePromotionActions.isNotEmpty()) {
                            primitivePromotionGate.requestBoundary(
                                currentKey,
                                libraryReady = true,
                            )
                        }
                        preparePrimitivePromotion(library, force = true)
                        return@withContext
                    }

                    if (!complete || layout == null) {
                        val unresolved = songsByCanonicalPosition.count { it == null }
                        preparedPrimitivePromotion = null
                        primitivePromotionGate.onFailed(key)
                        L.w(
                            "Unable to hydrate complete Fast Resume queue; keeping primitive " +
                                "authority [session=${key.sessionId} revision=${key.revision} " +
                                "items=${allItems.size}/${descriptor.totalCount} unresolved=$unresolved]"
                        )
                        primitivePromotionGate.clearBoundary(key)
                        drainPendingPrimitivePromotionActions()
                        return@withContext
                    }

                    preparedPrimitivePromotion =
                        PreparedPrimitivePromotion(
                            key,
                            layout,
                            songsByCanonicalPosition.filterNotNull(),
                        )
                    val boundaryRequested = primitivePromotionGate.onPrepared(key)
                    L.i(
                        "Fast Resume canonical queue prepared " +
                            "[session=${key.sessionId} revision=${key.revision} " +
                            "count=${layout.itemsByCanonicalPosition.size} " +
                            "boundaryRequested=$boundaryRequested]"
                    )
                    if (boundaryRequested) {
                        if (!promotePreparedPrimitiveQueue("prepared-after-boundary")) {
                            primitivePromotionGate.onFailed(key)
                        }
                        drainPendingPrimitivePromotionActions()
                    }
                }
            }
    }

    private fun deferPrimitiveQueueInteractionUntilPromotion(
        reason: String,
        replay: () -> Unit,
    ): Boolean {
        val active = activePrimitiveWindow ?: return false
        val library = musicRepository.library?.takeIf { !it.empty() } ?: return false
        val key = active.promotionKey()
        return when (primitivePromotionGate.requestBoundary(key, libraryReady = true)) {
            PrimitiveQueuePromotionGate.Decision.BYPASS -> false
            PrimitiveQueuePromotionGate.Decision.PROMOTE -> {
                if (!promotePreparedPrimitiveQueue(reason)) {
                    primitivePromotionGate.onFailed(key)
                }
                false
            }
            PrimitiveQueuePromotionGate.Decision.PREPARE -> {
                L.i(
                    "Deferring Fast Resume queue interaction until canonical queue is ready " +
                        "[reason=$reason session=${key.sessionId} revision=${key.revision}]"
                )
                pendingPrimitivePromotionActions.add(replay)
                preparePrimitivePromotion(library)
                true
            }
        }
    }

    private fun requestPrimitivePromotionBoundary(reason: String) {
        val active = activePrimitiveWindow ?: return
        val library = musicRepository.library?.takeIf { !it.empty() } ?: return
        val key = active.promotionKey()
        when (primitivePromotionGate.requestBoundary(key, libraryReady = true)) {
            PrimitiveQueuePromotionGate.Decision.BYPASS -> Unit
            PrimitiveQueuePromotionGate.Decision.PROMOTE -> {
                if (!promotePreparedPrimitiveQueue(reason)) {
                    primitivePromotionGate.onFailed(key)
                }
            }
            PrimitiveQueuePromotionGate.Decision.PREPARE -> preparePrimitivePromotion(library)
        }
    }

    private fun promotePreparedPrimitiveQueue(reason: String): Boolean {
        val active = activePrimitiveWindow ?: return false
        val key = active.promotionKey()
        val prepared = preparedPrimitivePromotion ?: return false
        if (prepared.key != key || !primitivePromotionGate.isPrepared(key)) return false

        val logicalPosition = active.descriptor.currentLogicalPosition
        val heapIndex =
            prepared.layout.heapIndexForLogicalPosition(logicalPosition) ?: return false
        val currentSong = prepared.songsByCanonicalPosition.getOrNull(heapIndex) ?: return false
        val currentPositionMs =
            player.currentPosition.coerceAtLeast(0L).let { position ->
                if (currentSong.durationMs > 0L) position.coerceAtMost(currentSong.durationMs)
                else position
            }
        val keepPlaying = player.playWhenReady
        val audioSessionBefore = player.audioSessionId
        val shuffledMapping = prepared.layout.shuffledMapping

        primitivePromotionPreparationJob?.cancel()
        primitivePromotionPreparationJob = null
        primitivePromotionPreparationKey = null
        clearPrimitiveQueueState(clearPromotion = false)
        preparedPrimitivePromotion = null
        primitivePromotionGate.clear()
        rawFastResumeItem = null
        pendingLibraryRestoreAfterRawFailure = null
        parent = null

        player.setMediaItems(prepared.songsByCanonicalPosition.map { it.buildMediaItem() })
        if (shuffledMapping.isNotEmpty()) {
            player.shuffleModeEnabled = true
            player.setShuffleOrder(BetterShuffleOrder(shuffledMapping.toIntArray()))
        } else {
            player.shuffleModeEnabled = false
        }
        player.seekTo(heapIndex, currentPositionMs)
        player.prepare()
        player.playWhenReady = keepPlaying
        sessionOngoing = true
        playbackManager.notifyRestoreOutcome(RestoreOutcome.RESTORED_EXISTING_SESSION)
        playbackManager.ack(this, StateAck.NewPlayback)
        playbackManager.ack(this, StateAck.ProgressionChanged)
        Ts18FirstAudioLatency.mark("primitive_queue_promoted")
        deferSave()
        L.i(
            "Promoted Fast Resume queue to canonical library authority " +
                "[reason=$reason count=${prepared.songsByCanonicalPosition.size} " +
                "logical=$logicalPosition heap=$heapIndex positionMs=$currentPositionMs " +
                "audioSessionBefore=$audioSessionBefore audioSessionAfter=${player.audioSessionId}]"
        )
        return true
    }

    private fun drainPendingPrimitivePromotionActions() {
        if (pendingPrimitivePromotionActions.isEmpty()) return
        val actions = pendingPrimitivePromotionActions.toList()
        pendingPrimitivePromotionActions.clear()
        actions.forEach { action -> action() }
    }

    private fun launchPrimitiveMutation(
""",
    "promotion implementation",
)

for old, new, label in [
    (
        """    override fun shuffled(shuffled: Boolean) {
        cancelActiveRestore("queue-reordered")
        activePrimitiveWindow?.let { window ->
""",
        """    override fun shuffled(shuffled: Boolean) {
        cancelActiveRestore("queue-reordered")
        if (deferPrimitiveQueueInteractionUntilPromotion("shuffle") { shuffled(shuffled) }) return
        activePrimitiveWindow?.let { window ->
""",
        "shuffle boundary",
    ),
    (
        """        cancelActiveRestore("next")
        activePrimitiveWindow?.let { window ->
""",
        """        cancelActiveRestore("next")
        if (deferPrimitiveQueueInteractionUntilPromotion("next") { next() }) return
        activePrimitiveWindow?.let { window ->
""",
        "next boundary",
    ),
    (
        """        cancelActiveRestore("previous")
        activePrimitiveWindow?.let { window ->
""",
        """        cancelActiveRestore("previous")
        if (deferPrimitiveQueueInteractionUntilPromotion("previous") { prev() }) return
        activePrimitiveWindow?.let { window ->
""",
        "previous boundary",
    ),
    (
        """    override fun goto(index: Int) {
        cancelActiveRestore("queue-index")
        activePrimitiveWindow?.let { window ->
""",
        """    override fun goto(index: Int) {
        cancelActiveRestore("queue-index")
        if (deferPrimitiveQueueInteractionUntilPromotion("goto") { goto(index) }) return
        activePrimitiveWindow?.let { window ->
""",
        "goto boundary",
    ),
    (
        """    override fun playNext(songs: List<Song>, ack: StateAck.PlayNext) {
        cancelActiveRestore("play-next")
        activePrimitiveWindow?.let { window ->
""",
        """    override fun playNext(songs: List<Song>, ack: StateAck.PlayNext) {
        cancelActiveRestore("play-next")
        if (
            deferPrimitiveQueueInteractionUntilPromotion("play-next") { playNext(songs, ack) }
        ) {
            return
        }
        activePrimitiveWindow?.let { window ->
""",
        "play-next boundary",
    ),
    (
        """    override fun addToQueue(songs: List<Song>, ack: StateAck.AddToQueue) {
        cancelActiveRestore("add-to-queue")
        activePrimitiveWindow?.let { window ->
""",
        """    override fun addToQueue(songs: List<Song>, ack: StateAck.AddToQueue) {
        cancelActiveRestore("add-to-queue")
        if (
            deferPrimitiveQueueInteractionUntilPromotion("add-to-queue") {
                addToQueue(songs, ack)
            }
        ) {
            return
        }
        activePrimitiveWindow?.let { window ->
""",
        "add-to-queue boundary",
    ),
    (
        """    override fun move(from: Int, to: Int, ack: StateAck.Move) {
        cancelActiveRestore("move-queue-item")
        activePrimitiveWindow?.let {
""",
        """    override fun move(from: Int, to: Int, ack: StateAck.Move) {
        cancelActiveRestore("move-queue-item")
        if (deferPrimitiveQueueInteractionUntilPromotion("move") { move(from, to, ack) }) return
        activePrimitiveWindow?.let {
""",
        "move boundary",
    ),
    (
        """    override fun remove(at: Int, ack: StateAck.Remove) {
        cancelActiveRestore("remove-queue-item")
        activePrimitiveWindow?.let { window ->
""",
        """    override fun remove(at: Int, ack: StateAck.Remove) {
        cancelActiveRestore("remove-queue-item")
        if (deferPrimitiveQueueInteractionUntilPromotion("remove") { remove(at, ack) }) return
        activePrimitiveWindow?.let { window ->
""",
        "remove boundary",
    ),
]:
    replace_once(EXO, old, new, label)

replace_once(
    EXO,
    """        if (activePrimitiveWindow != null) {
            synchronizePrimitivePositionFromPlayer()
            playbackManager.ack(this, StateAck.QueueWindowChanged)
            deferSave()
            maybePrefetchPrimitiveWindow()
        } else if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
""",
    """        if (activePrimitiveWindow != null) {
            synchronizePrimitivePositionFromPlayer()
            playbackManager.ack(this, StateAck.QueueWindowChanged)
            deferSave()
            maybePrefetchPrimitiveWindow()
            if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                // Avoid re-entrant playlist replacement from inside ExoPlayer's transition callback.
                // The posted boundary still runs at the start of the newly selected track.
                mainHandler.post { requestPrimitivePromotionBoundary("auto-transition") }
            }
        } else if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
""",
    "natural transition boundary",
)

replace_once(
    EXO,
    """            activePrimitiveWindow?.let {
                L.d("Library obtained while primitive queue is active; enriching loaded range")
                reconcilePrimitiveWindow(library)
                return
            }
""",
    """            activePrimitiveWindow?.let { window ->
                val key = window.promotionKey()
                primitivePromotionGate.onLibraryChanged(key)
                L.d(
                    "Library obtained while primitive queue is active; preparing canonical " +
                        "promotion and enriching loaded range"
                )
                preparePrimitivePromotion(library, force = true)
                reconcilePrimitiveWindow(library)
                return
            }
""",
    "library-ready preparation",
)

HOME = "app/src/main/java/org/oxycblt/auxio/home/HomeFragment.kt"
replace_once(
    HOME,
    """            is IndexingState.Completed -> {
                val actionable = state.outcome != IndexingTerminalOutcome.SUCCESS
                binding.homeIndexingContainer.isInvisible = !actionable
""",
    """            is IndexingState.Completed -> {
                val actionable = HomeIndexingPresentationPolicy.shouldShowStatusCard(state)
                binding.homeIndexingContainer.isInvisible = !actionable
""",
    "completed index presentation",
)
replace_once(
    HOME,
    """            is IndexingState.Indexing -> {
                binding.homeIndexingContainer.isInvisible = false
""",
    """            is IndexingState.Indexing -> {
                // Routine launch/background indexing is silent. True first bootstrap progress is
                // already owned by HomeListEmptyState; this diagnostic card is reserved for
                // actionable terminal outcomes so it cannot flash on every normal launch.
                binding.homeIndexingContainer.isInvisible =
                    !HomeIndexingPresentationPolicy.shouldShowStatusCard(state)
""",
    "live index presentation",
)

VISUALIZER = (
    "app/src/main/java/org/oxycblt/auxio/playback/ui/visualizer/VisualizerCoordinator.kt"
)
replace_once(
    VISUALIZER,
    """    private var watchdogJob: Job? = null
    private var pauseReleaseJob: Job? = null
    private var monitorJob: Job? = null
""",
    """    private var watchdogJob: Job? = null
    private var pauseReleaseJob: Job? = null
    private var startRetryJob: Job? = null
    private var startRetryAttempt = 0
    private var monitorJob: Job? = null
""",
    "visualizer retry fields",
)
replace_once(
    VISUALIZER,
    """        activeScope = owner.lifecycleScope
        recoveryTracker.reset()
""",
    """        activeScope = owner.lifecycleScope
        recoveryTracker.reset()
        cancelStartRetry(resetAttempt = true)
""",
    "visualizer start reset",
)
replace_once(
    VISUALIZER,
    """    override fun onVisualizerModeChanged() {
        recoveryTracker.reset()
""",
    """    override fun onVisualizerModeChanged() {
        recoveryTracker.reset()
        cancelStartRetry(resetAttempt = true)
""",
    "visualizer mode reset",
)
replace_once(
    VISUALIZER,
    """        if (granted) {
            recoveryTracker.reset()
            clearPersistedPermissionDenial()
""",
    """        if (granted) {
            recoveryTracker.reset()
            cancelStartRetry(resetAttempt = true)
            clearPersistedPermissionDenial()
""",
    "permission retry reset",
)
replace_once(
    VISUALIZER,
    """        val sessionId = audioSessionIdFlow.value?.takeIf { it > 0 }
        if (!isPlayingFlow.value) {
""",
    """        val sessionId = audioSessionIdFlow.value?.takeIf { it > 0 }
        if (!isPlayingFlow.value) {
            cancelStartRetry(resetAttempt = true)
""",
    "pause retry reset",
)
replace_once(
    VISUALIZER,
    """        if (forceRestart || (visualizer != null && currentSessionId != sessionId)) {
            recoveryTracker.reset()
            releaseVisualizer(resetRecovery = false)
        }
""",
    """        if (forceRestart || (visualizer != null && currentSessionId != sessionId)) {
            recoveryTracker.reset()
            cancelStartRetry(resetAttempt = true)
            releaseVisualizer(resetRecovery = false)
        }
""",
    "session retry reset",
)
replace_once(
    VISUALIZER,
    """        startVisualizer(sessionId)
    }

    private fun pauseVisualizer() {
""",
    """        if (startRetryJob?.isActive == true) return
        startVisualizer(sessionId)
    }

    private fun pauseVisualizer() {
""",
    "avoid immediate retry bypass",
)
replace_once(
    VISUALIZER,
    """                _state.value =
                    VisualizerState.Unavailable("Listener registration failed: $listenerStatus")
                return
""",
    """                _state.value =
                    VisualizerState.Unavailable("Listener registration failed: $listenerStatus")
                scheduleStartRetry(sessionId, "listener-status-$listenerStatus")
                return
""",
    "listener retry",
)
replace_once(
    VISUALIZER,
    """            candidateToRelease = null
            L.i(
                "Visualizer started session=$sessionId captureSize=$targetSize " +
""",
    """            candidateToRelease = null
            cancelStartRetry(resetAttempt = true)
            L.i(
                "Visualizer started session=$sessionId captureSize=$targetSize " +
""",
    "successful retry reset",
)
replace_once(
    VISUALIZER,
    """            visualizer = null
            currentSessionId = null
            _state.value = VisualizerState.Unavailable(message)
        } finally {
""",
    """            visualizer = null
            currentSessionId = null
            _state.value = VisualizerState.Unavailable(message)
            if (error !is UnsupportedOperationException) {
                scheduleStartRetry(sessionId, message)
            }
        } finally {
""",
    "construction retry",
)
replace_once(
    VISUALIZER,
    """                    } else {
                        releaseVisualizer(resetRecovery = false)
                        _state.value = VisualizerState.Unavailable("No usable visualizer frames")
                    }
""",
    """                    } else {
                        releaseVisualizer(resetRecovery = false)
                        _state.value = VisualizerState.Unavailable("No usable visualizer frames")
                        scheduleStartRetry(sessionId, "frame-watchdog")
                    }
""",
    "watchdog delayed retry",
)
replace_once(
    VISUALIZER,
    """    private fun releaseVisualizer(resetRecovery: Boolean) {
        watchdogJob?.cancel()
""",
    """    private fun scheduleStartRetry(sessionId: Int, reason: String) {
        val scope = activeScope ?: return
        val delayMs = VisualizerRecoveryPolicy.startRetryDelayMs(startRetryAttempt)
        if (delayMs == null) {
            L.w(
                "Visualizer startup retry budget exhausted " +
                    "[session=$sessionId reason=$reason attempts=$startRetryAttempt]"
            )
            return
        }
        startRetryJob?.cancel()
        val attempt = startRetryAttempt + 1
        startRetryAttempt = attempt
        _state.value = VisualizerState.Starting
        L.i(
            "Scheduling visualizer startup retry " +
                "[session=$sessionId reason=$reason attempt=$attempt delayMs=$delayMs]"
        )
        startRetryJob =
            scope.launch {
                delay(delayMs)
                startRetryJob = null
                if (
                    !active ||
                        !isPlayingFlow.value ||
                        audioSessionIdFlow.value != sessionId ||
                        uiSettings.visualizerMode == UISettings.VisualizerMode.OFF ||
                        !hasPermission()
                ) {
                    return@launch
                }
                startVisualizer(sessionId)
            }
    }

    private fun cancelStartRetry(resetAttempt: Boolean) {
        startRetryJob?.cancel()
        startRetryJob = null
        if (resetAttempt) startRetryAttempt = 0
    }

    private fun releaseVisualizer(resetRecovery: Boolean) {
        watchdogJob?.cancel()
""",
    "visualizer retry scheduler",
)
replace_once(
    VISUALIZER,
    """        if (activeVisualizer != null) releaseCandidate(activeVisualizer)
        if (resetRecovery) recoveryTracker.reset()
""",
    """        if (activeVisualizer != null) releaseCandidate(activeVisualizer)
        if (resetRecovery) {
            recoveryTracker.reset()
            cancelStartRetry(resetAttempt = true)
        }
""",
    "visualizer release retry reset",
)

print("Fast Resume finalisation patch applied successfully")
