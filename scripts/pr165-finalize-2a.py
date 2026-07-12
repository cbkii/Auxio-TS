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
frag='app/src/main/java/org/oxycblt/auxio/MainFragment.kt'
rep(frag,'import android.view.LayoutInflater\nimport android.view.ViewTreeObserver','import android.view.LayoutInflater\nimport android.view.View\nimport android.view.ViewTreeObserver')
rep(frag,
'''import org.oxycblt.auxio.music.MusicType
import org.oxycblt.auxio.music.MusicViewModel
import org.oxycblt.auxio.playback.OpenPanel
import org.oxycblt.auxio.playback.PanelRouteRequest''',
'''import org.oxycblt.auxio.music.MusicType
import org.oxycblt.auxio.music.MusicViewModel
import org.oxycblt.auxio.music.StartupReadinessState
import org.oxycblt.auxio.playback.OpenPanel
import org.oxycblt.auxio.playback.PanelRouteRequest
import org.oxycblt.auxio.playback.PendingPanelRouteDecision
import org.oxycblt.auxio.playback.PendingPanelRouteInput
import org.oxycblt.auxio.playback.StartupLibraryRouteState
import org.oxycblt.auxio.playback.StartupPlaybackPolicy''')
rep(frag,
'''    private var lastStretchRatio = -1f
    @Inject lateinit var uiSettings: UISettings''',
'''    private var lastStretchRatio = -1f
    private val panelRouteSheetCallback =
        object : BackportBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BackportBottomSheetBehavior.STATE_DRAGGING) {
                    playbackModel.cancelStartupPanelRoute("user-sheet-drag")
                    return
                }
                if (newState != BackportBottomSheetBehavior.STATE_SETTLING) {
                    applyPendingPanelRoute()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        }
    @Inject lateinit var uiSettings: UISettings''')
rep(frag,
'''        queueSheetBehavior?.uiSettings = uiSettings

        elevationNormal =''',
'''        queueSheetBehavior?.uiSettings = uiSettings
        playbackSheetBehavior.addBottomSheetCallback(panelRouteSheetCallback)
        queueSheetBehavior?.addBottomSheetCallback(panelRouteSheetCallback)

        elevationNormal =''')
rep(frag,
'''            SheetBackPressedCallback(
                playbackSheetBehavior = playbackSheetBehavior,
                queueSheetBehavior = queueSheetBehavior,
            )''',
'''            SheetBackPressedCallback(
                playbackSheetBehavior = playbackSheetBehavior,
                queueSheetBehavior = queueSheetBehavior,
                onManualPanelNavigation = {
                    playbackModel.cancelPanelRoute("user-panel-back")
                },
            )''')
rep(frag,
'''        collectImmediately(playbackModel.song, ::updateSong)
        collectImmediately(musicModel.startupReadinessState) { applyPendingPanelRoute() }
        collectImmediately(playbackModel.openPanel.flow, ::handlePanel)
        collectImmediately(playbackModel.panelRoute, ::handlePanelRoute)''',
'''        collectImmediately(playbackModel.song, ::updateSong)
        collectImmediately(musicModel.startupReadinessState) { applyPendingPanelRoute() }
        collectImmediately(playbackModel.rawPlaybackMetadata) { applyPendingPanelRoute() }
        collectImmediately(playbackModel.restoreProgress) { applyPendingPanelRoute() }
        collectImmediately(playbackModel.panelRoute, ::handlePanelRoute)''')
rep(frag,
'''    override fun onDestroyBinding(binding: FragmentMainBinding) {
        super.onDestroyBinding(binding)''',
'''    override fun onDestroyBinding(binding: FragmentMainBinding) {
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?
        playbackSheetBehavior.removeBottomSheetCallback(panelRouteSheetCallback)
        queueSheetBehavior?.removeBottomSheetCallback(panelRouteSheetCallback)
        super.onDestroyBinding(binding)''')
rep(frag,
'''        findNavController().navigateSafe(directions)
        homeModel.showOuter.consume()''',
'''        playbackModel.cancelPanelRoute("explicit-outer-navigation")
        findNavController().navigateSafe(directions)
        homeModel.showOuter.consume()''')
sub(frag,
r'''    private fun handlePanelRoute\(route: PanelRouteRequest\?\) = applyPendingPanelRoute\(route\).*?\n    private fun tryShowSheets\(\) \{''',
'''    private fun handlePanelRoute(route: PanelRouteRequest?) = applyPendingPanelRoute(route)

    private fun applyPendingPanelRoute(route: PanelRouteRequest? = playbackModel.panelRoute.value) {
        route ?: return
        when (
            val decision =
                StartupPlaybackPolicy.pendingRouteDecision(
                    PendingPanelRouteInput(
                        route = route,
                        libraryState = startupLibraryRouteState(),
                        hasNormalSong = playbackModel.song.value != null,
                        rawFastResumeActive = playbackModel.rawPlaybackMetadata.value != null,
                        restoreProgress = playbackModel.restoreProgress.value,
                    )
                )
        ) {
            is PendingPanelRouteDecision.Wait ->
                playbackModel.acknowledgePanelRoute(
                    route.id,
                    rendered = false,
                    decision.reason,
                )
            is PendingPanelRouteDecision.Cancel ->
                playbackModel.cancelPanelRoute(route.id, decision.reason)
            is PendingPanelRouteDecision.Apply -> {
                val rendered =
                    when (route.destination) {
                        OpenPanel.MAIN -> tryClosePlaybackPanel()
                        OpenPanel.PLAYBACK -> tryOpenPlaybackPanel()
                        OpenPanel.PLAYBACK_QUEUE -> tryOpenPlaybackQueuePanel()
                        OpenPanel.QUEUE -> tryOpenQueuePanel()
                    }
                playbackModel.acknowledgePanelRoute(
                    route.id,
                    rendered,
                    if (rendered) "rendered-${decision.reason}" else "sheet-transition-pending",
                )
            }
        }
    }

    private fun startupLibraryRouteState() =
        when (musicModel.startupReadinessState.value) {
            StartupReadinessState.CheckingCachedLibrary -> StartupLibraryRouteState.CHECKING
            StartupReadinessState.Ready -> StartupLibraryRouteState.READY_OR_UNKNOWN
            StartupReadinessState.NeedsMusicSource -> StartupLibraryRouteState.NEEDS_SOURCE
            StartupReadinessState.CachedLibraryUnavailable -> StartupLibraryRouteState.RECOVERY
            StartupReadinessState.EmptyLibrary -> StartupLibraryRouteState.EMPTY
        }

    private fun tryOpenPlaybackPanel(): Boolean {
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as? QueueBottomSheetBehavior

        if (playbackSheetBehavior.state != BackportBottomSheetBehavior.STATE_EXPANDED) {
            if (playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED) {
                L.d("Expanding playback sheet")
                playbackSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
            }
            return false
        }

        if (queueSheetBehavior == null) return true
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED) return true
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED) {
            L.d("Collapsing queue sheet for playback destination")
            queueSheetBehavior.state = BackportBottomSheetBehavior.STATE_COLLAPSED
        }
        return false
    }

    private fun tryOpenPlaybackQueuePanel(): Boolean {
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as? QueueBottomSheetBehavior

        if (playbackSheetBehavior.state != BackportBottomSheetBehavior.STATE_EXPANDED) {
            if (playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED) {
                L.d("Expanding playback sheet before queue destination")
                playbackSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
            }
            return false
        }

        if (queueSheetBehavior == null) {
            // Wide dual-pane layout: queue is static beside the expanded playback panel.
            return true
        }
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED) return true
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED) {
            L.d("Expanding queue sheet after playback reached expanded state")
            queueSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
        }
        return false
    }

    private fun tryClosePlaybackPanel(): Boolean {
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as? QueueBottomSheetBehavior

        val playbackClosed =
            playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED ||
                playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_HIDDEN
        val queueClosed =
            queueSheetBehavior == null ||
                queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED
        if (playbackClosed && queueClosed) return true

        if (queueSheetBehavior?.state == BackportBottomSheetBehavior.STATE_EXPANDED) {
            queueSheetBehavior.state = BackportBottomSheetBehavior.STATE_COLLAPSED
        }
        if (playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED) {
            playbackSheetBehavior.state = BackportBottomSheetBehavior.STATE_COLLAPSED
        }
        return false
    }

    private fun tryOpenQueuePanel(): Boolean {
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as? QueueBottomSheetBehavior
                ?: return playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED

        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED) return true
        if (
            playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED &&
                queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED
        ) {
            L.d("Expanding queue sheet")
            queueSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
        }
        return false
    }

    private fun tryShowSheets() {''')
rep(frag,
'''    private class SheetBackPressedCallback(
        private val playbackSheetBehavior: PlaybackBottomSheetBehavior<*>,
        private val queueSheetBehavior: QueueBottomSheetBehavior<*>?,
    ) : OnBackPressedCallback(false) {''',
'''    private class SheetBackPressedCallback(
        private val playbackSheetBehavior: PlaybackBottomSheetBehavior<*>,
        private val queueSheetBehavior: QueueBottomSheetBehavior<*>?,
        private val onManualPanelNavigation: () -> Unit,
    ) : OnBackPressedCallback(false) {''')
rep(frag,
'''        override fun handleOnBackPressed() {
            if (queueSheetShown()) {''',
'''        override fun handleOnBackPressed() {
            if (queueSheetShown() || playbackSheetShown()) {
                onManualPanelNavigation()
            }
            if (queueSheetShown()) {''')
