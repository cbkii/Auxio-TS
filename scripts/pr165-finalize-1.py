from pathlib import Path
import re
root=Path.cwd()

def p(rel): return root/rel

def read(rel): return p(rel).read_text()
def write(rel,s): p(rel).write_text(s)
def rep(rel, old, new, n=1):
    s=read(rel); c=s.count(old)
    if c!=n: raise SystemExit(f'{rel}: expected {n} matches got {c}: {old[:100]!r}')
    write(rel,s.replace(old,new,n))
def sub(rel, pat, repl):
    s=read(rel); s2,c=re.subn(pat,repl,s,count=1,flags=re.S)
    if c!=1: raise SystemExit(f'{rel}: regex count {c}: {pat[:100]}')
    write(rel,s2)

activity='app/src/main/java/org/oxycblt/auxio/MainActivity.kt'
rep(activity,
'''import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.PlaybackViewModel''',
'''import org.oxycblt.auxio.playback.PanelRouteOrigin
import org.oxycblt.auxio.playback.PanelRoutePriority
import org.oxycblt.auxio.playback.PlaybackSettings
import org.oxycblt.auxio.playback.PlaybackViewModel''')
rep(activity,
'''                playbackModel.playDeferred(action)
                maybeRouteToPlaybackOnColdHeadUnitLaunch()''',
'''                val restoreRequestId = playbackModel.requestStartupRestore(action)
                maybeRouteToPlaybackOnColdHeadUnitLaunch(restoreRequestId)''')
sub(activity,
r'''    private fun maybeRouteToPlaybackOnColdHeadUnitLaunch\(\) \{.*?\n    \}\n\n    override fun onNewIntent''',
'''    private fun maybeRouteToPlaybackOnColdHeadUnitLaunch(restoreRequestId: Long) {
        if (!isFirstResume) return
        val library = musicRepository.library
        val libraryRouteState =
            when {
                library?.empty() == false -> StartupLibraryRouteState.READY_OR_UNKNOWN
                library?.empty() == true -> StartupLibraryRouteState.EMPTY
                musicSettings.libraryState == LibraryState.NEVER ->
                    StartupLibraryRouteState.NEEDS_SOURCE
                musicSettings.libraryState == LibraryState.EMPTY ->
                    StartupLibraryRouteState.EMPTY
                musicSettings.libraryState == LibraryState.RECOVERY ->
                    StartupLibraryRouteState.RECOVERY
                else -> StartupLibraryRouteState.CHECKING
            }
        val decision =
            StartupPlaybackPolicy.startupRoute(
                StartupPanelInput(
                    coldLaunch = true,
                    restoredTask = false,
                    // Product decision: preserve standard behavior; Topway-compatible variants
                    // enable launch-to-panel by default without coupling it to autoplay.
                    launchToPanel = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    topwayCompatFlavor = BuildConfig.TOPWAY_COMPAT_FLAVOR,
                    headUnitLandscapeMode = pendingHeadUnitLaunchRoute,
                    libraryState = libraryRouteState,
                    hasNormalSong = playbackModel.song.value != null,
                    rawFastResumeActive = playbackModel.rawPlaybackMetadata.value != null,
                )
            )
        when (decision) {
            is StartupPanelDecision.KeepCurrent ->
                L.i("Startup panel route skipped reason=${decision.reason}")
            is StartupPanelDecision.RequestRoute -> {
                pendingHeadUnitLaunchRoute = false
                L.i(
                    "Startup panel route selected destination=${decision.destination} " +
                        "reason=${decision.reason} restore=$restoreRequestId"
                )
                playbackModel.requestPanelRoute(
                    decision.destination,
                    decision.origin,
                    decision.priority,
                    decision.waitForSong,
                    decision.reason,
                    restoreRequestId.takeIf { decision.restoreBound },
                )
            }
        }
    }

    override fun onNewIntent''')
rep(activity,
'''        if (action != null) {
            L.d("Translated intent to $action")
            playbackModel.playDeferred(action)
            return true
        }''',
'''        if (action != null) {
            L.d("Translated intent to $action")
            playbackModel.cancelPanelRoute("explicit-playback-intent")
            playbackModel.playDeferred(action)
            return true
        }''')
rep(activity,
'''        when (destination) {
            HeadUnitEntryPoints.EntryDestination.NOW_PLAYING -> playbackModel.openPlayback()
            HeadUnitEntryPoints.EntryDestination.QUEUE -> playbackModel.openPlaybackQueue()
            null -> {''',
'''        when (destination) {
            HeadUnitEntryPoints.EntryDestination.NOW_PLAYING ->
                playbackModel.requestPanelRoute(
                    org.oxycblt.auxio.playback.OpenPanel.PLAYBACK,
                    PanelRouteOrigin.EXPLICIT_INTENT,
                    PanelRoutePriority.EXPLICIT,
                    waitForSong = true,
                    reason = "explicit-now-playing-intent",
                )
            HeadUnitEntryPoints.EntryDestination.QUEUE ->
                playbackModel.requestPanelRoute(
                    org.oxycblt.auxio.playback.OpenPanel.PLAYBACK_QUEUE,
                    PanelRouteOrigin.EXPLICIT_INTENT,
                    PanelRoutePriority.EXPLICIT,
                    waitForSong = true,
                    reason = "explicit-queue-intent",
                )
            null -> {''')
rep(activity,
'''            else -> {
                intent.putExtra(HeadUnitEntryPoints.EXTRA_ENTRY_DESTINATION, destination.name)''',
'''            else -> {
                playbackModel.cancelPanelRoute("explicit-navigation-intent")
                intent.putExtra(HeadUnitEntryPoints.EXTRA_ENTRY_DESTINATION, destination.name)''')

vm='app/src/main/java/org/oxycblt/auxio/playback/PlaybackViewModel.kt'
rep(vm,
'''import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.ShuffleMode''',
'''import org.oxycblt.auxio.playback.state.RepeatMode
import org.oxycblt.auxio.playback.state.RestoreOutcome
import org.oxycblt.auxio.playback.state.RestoreProgress
import org.oxycblt.auxio.playback.state.ShuffleMode''')
sub(vm,
r'''    private val _openPanel = MutableEvent<OpenPanel>\(\).*?    private var nextPanelRouteId = 1L\n''',
'''    private val _panelRoute = MutableStateFlow<PanelRouteRequest?>(null)
    /** Durable panel route retained until rendered, cancelled, or superseded. */
    val panelRoute: StateFlow<PanelRouteRequest?> = _panelRoute
    private var nextPanelRouteId = 1L
    private var nextRestoreRequestId = 1L
    private var activeRestoreRequestId: Long? = null
    private var lastPendingRouteLog: Pair<Long, String>? = null
''')
rep(vm,
'''    private val _rawPlaybackMetadata = MutableStateFlow(playbackManager.rawPlaybackMetadata)
    val rawPlaybackMetadata: StateFlow<RawPlaybackMetadata?> = _rawPlaybackMetadata''',
'''    private val _rawPlaybackMetadata = MutableStateFlow(playbackManager.rawPlaybackMetadata)
    /** Raw pre-library TS18 playback metadata; never a normal Musikr [Song]. */
    val rawPlaybackMetadata: StateFlow<RawPlaybackMetadata?> = _rawPlaybackMetadata

    private val _restoreProgress = MutableStateFlow(playbackManager.restoreProgress)
    /** Outcome of the Activity-scoped startup restore request. */
    val restoreProgress: StateFlow<RestoreProgress?> = _restoreProgress''')
# Mark restore when normal song is observed from playback callbacks.
rep(vm,
'''        _song.value = playbackManager.currentSong

        _pagerCommand.put(PagerCommand(update = null, scroll = index))''',
'''        _song.value = playbackManager.currentSong
        markRestoreRenderableIfNeeded()

        _pagerCommand.put(PagerCommand(update = null, scroll = index))''')
rep(vm,
'''            _song.value = playbackManager.currentSong
        }

        _pagerCommand.put(''',
'''            _song.value = playbackManager.currentSong
            markRestoreRenderableIfNeeded()
        }

        _pagerCommand.put(''')
rep(vm,
'''        _song.value = playbackManager.currentSong
        _parent.value = parent''',
'''        _song.value = playbackManager.currentSong
        markRestoreRenderableIfNeeded()
        _parent.value = parent''')
rep(vm,
'''    override fun onRawPlaybackMetadataChanged(metadata: RawPlaybackMetadata?) {
        _rawPlaybackMetadata.value = metadata
    }

    override fun onBarActionChanged()''',
'''    override fun onRawPlaybackMetadataChanged(metadata: RawPlaybackMetadata?) {
        _rawPlaybackMetadata.value = metadata
        if (metadata != null) {
            activeRestoreRequestId?.let { requestId ->
                playbackManager.notifyRestoreOutcome(
                    requestId,
                    RestoreOutcome.RAW_FAST_RESUME_ACTIVE,
                )
            }
        }
    }

    override fun onRestoreProgressChanged(progress: RestoreProgress) {
        _restoreProgress.value = progress
        if (
            progress.requestId == activeRestoreRequestId &&
                progress.outcome in TERMINAL_RESTORE_OUTCOMES
        ) {
            activeRestoreRequestId = null
        }
    }

    private fun markRestoreRenderableIfNeeded() {
        if (playbackManager.currentSong == null) return
        activeRestoreRequestId?.let { requestId ->
            playbackManager.notifyRestoreOutcome(
                requestId,
                RestoreOutcome.RESTORED_EXISTING_SESSION,
            )
        }
    }

    override fun onBarActionChanged()''')
rep(vm,
'''    private fun playImpl(
        command: PlaybackCommand?,
        shuffleScope: ShuffleScope? = null,
        play: Boolean = true,
    ) {
        val playbackCommand = requireNotNull(command) { "Invalid playback parameters" }''',
'''    private fun playImpl(
        command: PlaybackCommand?,
        shuffleScope: ShuffleScope? = null,
        play: Boolean = true,
    ) {
        val playbackCommand = requireNotNull(command) { "Invalid playback parameters" }
        cancelStartupPanelRoute("user-playback")''')
rep(vm,
'''    fun playDeferred(action: DeferredPlayback) {
        L.d("Starting action $action")
        playbackManager.playDeferred(action)
    }''',
'''    fun playDeferred(action: DeferredPlayback) {
        L.d("Starting action $action")
        playbackManager.playDeferred(action)
    }

    /**
     * Start and identify the one restore request associated with a new Activity launch.
     *
     * The returned ID binds the generic panel request to this restore. A service attach restore
     * without this ID is deduplicated while the tracked request remains active.
     */
    fun requestStartupRestore(action: DeferredPlayback.RestoreState): Long {
        val requestId = nextRestoreRequestId++
        activeRestoreRequestId = requestId
        L.i("Starting tracked startup restore request=$requestId")
        playbackManager.playDeferred(action.copy(requestId = requestId))
        return requestId
    }''')
# Add cancellation to queue-edit methods; regex all 12 methods.
s=read(vm)
s,c=re.subn(r'(    fun (?:playNext|addToQueue)\([^\n]*\) \{\n)(        L\.d)',r'\1        cancelStartupPanelRoute("user-queue-playback")\n\2',s)
if c != 12: print('queue methods patched',c)
write(vm,s)
sub(vm,
r'''    private fun openImpl\(panel: OpenPanel\) \{.*?\n    private companion object \{''',
'''    private fun openImpl(panel: OpenPanel) {
        requestPanelRoute(
            panel,
            PanelRouteOrigin.USER_ACTION,
            PanelRoutePriority.EXPLICIT,
            waitForSong = panel != OpenPanel.MAIN,
            reason = "user-panel-action",
        )
    }

    /** Record a durable panel route, preserving any higher-priority request. */
    fun requestPanelRoute(
        panel: OpenPanel,
        origin: PanelRouteOrigin,
        priority: PanelRoutePriority,
        waitForSong: Boolean,
        reason: String,
        restoreRequestId: Long? = null,
    ): Long? {
        val existing = _panelRoute.value
        if (existing != null && existing.priority.value > priority.value) {
            L.i(
                "Keeping panel route ${existing.id}/${existing.destination}; rejected $panel " +
                    "origin=$origin reason=$reason"
            )
            return null
        }
        val request =
            PanelRouteRequest(
                nextPanelRouteId++,
                panel,
                origin,
                priority,
                waitForSong,
                reason,
                restoreRequestId,
            )
        L.i("Panel route requested $request")
        lastPendingRouteLog = null
        _panelRoute.value = request
        return request.id
    }

    /** Keep or consume [id] after a bounded render attempt. */
    fun acknowledgePanelRoute(id: Long, rendered: Boolean, reason: String) {
        val existing = _panelRoute.value ?: return
        if (existing.id != id) return
        if (rendered) {
            L.i("Panel route acknowledged id=$id reason=$reason")
            lastPendingRouteLog = null
            _panelRoute.value = null
        } else if (lastPendingRouteLog != (id to reason)) {
            L.i("Panel route pending id=$id reason=$reason")
            lastPendingRouteLog = id to reason
        }
    }

    /** Cancel a specific request without clearing a newer superseding route. */
    fun cancelPanelRoute(id: Long, reason: String) {
        val existing = _panelRoute.value ?: return
        if (existing.id != id) return
        L.i("Panel route cancelled id=$id reason=$reason")
        lastPendingRouteLog = null
        _panelRoute.value = null
    }

    fun cancelPanelRoute(reason: String) {
        _panelRoute.value?.let { cancelPanelRoute(it.id, reason) }
    }

    fun cancelStartupPanelRoute(reason: String) {
        val existing = _panelRoute.value ?: return
        if (existing.origin == PanelRouteOrigin.STARTUP_RESTORE) {
            cancelPanelRoute(existing.id, reason)
        }
    }

    private companion object {
        val TERMINAL_RESTORE_OUTCOMES =
            setOf(
                RestoreOutcome.RESTORED_EXISTING_SESSION,
                RestoreOutcome.FALLBACK_QUEUE_CREATED,
                RestoreOutcome.NO_SAVED_SESSION,
                RestoreOutcome.FAILED,
                RestoreOutcome.CANCELLED,
            )''')

# Manager: dedupe untracked service attach restore instead of cancelling tracked request.
manager='app/src/main/java/org/oxycblt/auxio/playback/state/PlaybackStateManager.kt'
rep(manager,
'''        } else if (action is DeferredPlayback.RestoreState) {
            val active = currentRestoreProgress
            if (active != null && active.outcome !in TERMINAL_RESTORE_OUTCOMES) {
                L.i("Untracked restore superseded tracked request ${active.requestId}")
                updateRestoreProgress(RestoreProgress(active.requestId, RestoreOutcome.CANCELLED))
            }
        }

        val stateHolder = stateHolder''',
'''        } else if (action is DeferredPlayback.RestoreState) {
            val active = currentRestoreProgress
            if (active != null && active.outcome !in TERMINAL_RESTORE_OUTCOMES) {
                L.i(
                    "Ignoring duplicate untracked restore while tracked request " +
                        "${active.requestId}/${active.outcome} is active"
                )
                return
            }
        }

        val stateHolder = stateHolder''')


# A warm foreground return must not start another persistence restore or generic route.
activity_text=read(activity)
old='''            if (!startIntentAction(intent)) {
                // No intent action to do, restore the previously saved state.
                // Only autoplay on the first resume (cold launch) so that returning to the app
                // from the background does not force playback to resume after the user paused it.
                val action =
                    StartupPlaybackPolicy.restoreActionForLaunch(
                        playbackSettings.autoplayOnLaunch && isFirstResume
                    )
                val restoreRequestId = playbackModel.requestStartupRestore(action)
                maybeRouteToPlaybackOnColdHeadUnitLaunch(restoreRequestId)
            }
'''
new='''            if (!startIntentAction(intent)) {
                if (isFirstResume) {
                    // A genuine cold launch owns one tracked restore request. Warm foreground
                    // returns preserve the current playback and panel state instead of reading
                    // persistence or creating another startup route.
                    val action =
                        StartupPlaybackPolicy.restoreActionForLaunch(
                            playbackSettings.autoplayOnLaunch
                        )
                    val restoreRequestId = playbackModel.requestStartupRestore(action)
                    maybeRouteToPlaybackOnColdHeadUnitLaunch(restoreRequestId)
                } else {
                    L.d("Warm return; preserving current playback and panel state")
                }
            }
'''
if activity_text.count(old) != 1:
    raise SystemExit(f'warm-return block count {activity_text.count(old)}')
write(activity, activity_text.replace(old,new,1))
print('part 1 complete')
