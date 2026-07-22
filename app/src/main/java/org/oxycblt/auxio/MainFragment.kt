/*
 * Copyright (c) 2021 Auxio Project
 * MainFragment.kt is part of Auxio.
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

package org.oxycblt.auxio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowInsets
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.R as MR
import com.google.android.material.bottomsheet.BackportBottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.transition.MaterialFadeThrough
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Method
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min
import org.oxycblt.auxio.databinding.FragmentMainBinding
import org.oxycblt.auxio.detail.DetailViewModel
import org.oxycblt.auxio.detail.Show
import org.oxycblt.auxio.home.HomeViewModel
import org.oxycblt.auxio.home.Outer
import org.oxycblt.auxio.list.ListViewModel
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.MusicType
import org.oxycblt.auxio.music.MusicViewModel
import org.oxycblt.auxio.playback.OpenPanel
import org.oxycblt.auxio.playback.PlaybackBottomSheetBehavior
import org.oxycblt.auxio.playback.PlaybackViewModel
import org.oxycblt.auxio.playback.queue.QueueBottomSheetBehavior
import org.oxycblt.auxio.ui.DialogAwareNavigationListener
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.ui.ViewBindingFragment
import org.oxycblt.auxio.util.collect
import org.oxycblt.auxio.util.collectImmediately
import org.oxycblt.auxio.util.context
import org.oxycblt.auxio.util.coordinatorLayoutBehavior
import org.oxycblt.auxio.util.getAttrColorCompat
import org.oxycblt.auxio.util.getDimen
import org.oxycblt.auxio.util.lazyReflectedMethod
import org.oxycblt.auxio.util.navigateSafe
import org.oxycblt.auxio.util.unlikelyToBeNull
import org.oxycblt.musikr.Music
import org.oxycblt.musikr.Song
import timber.log.Timber as L

/**
 * A wrapper around the home fragment that shows the playback fragment and high-level navigation.
 *
 * @author Alexander Capehart (OxygenCobalt)
 */
@AndroidEntryPoint
class MainFragment :
    ViewBindingFragment<FragmentMainBinding>(),
    ViewTreeObserver.OnPreDrawListener,
    SpeedDialView.OnActionSelectedListener {
    private val musicModel: MusicViewModel by activityViewModels()
    private val detailModel: DetailViewModel by activityViewModels()
    private val homeModel: HomeViewModel by activityViewModels()
    private val listModel: ListViewModel by activityViewModels()
    private val playbackModel: PlaybackViewModel by activityViewModels()
    private val startupPanelCoordinator: org.oxycblt.auxio.headunit.StartupPanelCoordinator by
        activityViewModels()
    private var sheetBackCallback: SheetBackPressedCallback? = null
    private var detailBackCallback: DetailBackPressedCallback? = null
    private var selectionBackCallback: SelectionBackPressedCallback? = null
    private var speedDialBackCallback: SpeedDialBackPressedCallback? = null
    private var navigationListener: DialogAwareNavigationListener? = null
    private var lastInsets: WindowInsets? = null
    private var elevationNormal = 0f
    private var normalCornerSize = 0f
    private var maxScaleXDistance = 0f
    private var sheetRising: Boolean? = null
    private var lastStretchRatio = -1f
    private val panelRouteSheetCallback =
        object : BackportBottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BackportBottomSheetBehavior.STATE_DRAGGING) {
                    startupPanelCoordinator.cancelRouting("user-sheet-drag")
                    return
                }
                if (newState != BackportBottomSheetBehavior.STATE_SETTLING) {
                    handleStartupRoute(startupPanelCoordinator.routeEvaluation.value)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        }
    @Inject lateinit var uiSettings: UISettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateBinding(inflater: LayoutInflater) = FragmentMainBinding.inflate(inflater)

    override fun onBindingCreated(binding: FragmentMainBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        playbackSheetBehavior.uiSettings = uiSettings
        playbackSheetBehavior.makeBackgroundDrawable(requireContext())
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?
        queueSheetBehavior?.uiSettings = uiSettings
        playbackSheetBehavior.addBottomSheetCallback(panelRouteSheetCallback)
        queueSheetBehavior?.addBottomSheetCallback(panelRouteSheetCallback)

        elevationNormal = binding.context.getDimen(MR.dimen.m3_sys_elevation_level1)

        // Currently all back press callbacks are handled in MainFragment, as it's not guaranteed
        // that instantiating these callbacks in their respective fragments would result in the
        // correct order.
        sheetBackCallback =
            SheetBackPressedCallback(
                playbackSheetBehavior = playbackSheetBehavior,
                queueSheetBehavior = queueSheetBehavior,
                onManualPanelNavigation = {
                    startupPanelCoordinator.cancelRouting("user-panel-back")
                },
            )
        val detailBackCallback =
            DetailBackPressedCallback(detailModel).also { detailBackCallback = it }
        val selectionBackCallback =
            SelectionBackPressedCallback(listModel).also { selectionBackCallback = it }
        speedDialBackCallback = SpeedDialBackPressedCallback()

        navigationListener = DialogAwareNavigationListener(::onExploreNavigate)

        // --- UI SETUP ---
        val context = requireActivity()

        binding.root.setOnApplyWindowInsetsListener { _, insets ->
            lastInsets = insets
            insets
        }

        // Send meaningful accessibility events for bottom sheets
        ViewCompat.setAccessibilityPaneTitle(
            binding.playbackSheet,
            context.getString(R.string.lbl_playback),
        )
        ViewCompat.setAccessibilityPaneTitle(
            binding.queueSheet,
            context.getString(R.string.lbl_queue),
        )

        if (queueSheetBehavior != null) {
            // In portrait mode, set up click listeners on the stacked sheets.
            L.d("Configuring stacked bottom sheets")
            unlikelyToBeNull(binding.queueHandleWrapper).setOnClickListener {
                playbackModel.openQueue()
            }
        } else {
            // Dual-pane mode, manually style the static queue sheet.
            L.d("Configuring dual-pane bottom sheet")
            binding.queueSheet.apply {
                // Emulate the elevated bottom sheet style.
                background =
                    MaterialShapeDrawable.createWithElevationOverlay(context).apply {
                        shapeAppearanceModel =
                            ShapeAppearanceModel.builder(
                                    context,
                                    MR.style.ShapeAppearance_Material3_Corner_ExtraLarge,
                                    MR.style.ShapeAppearanceOverlay_Material3_Corner_Top,
                                )
                                .build()
                        fillColor = context.getAttrColorCompat(MR.attr.colorSurfaceContainerHigh)
                    }
            }
            applyHeadUnitDriverSideLayout(binding)
        }

        normalCornerSize = playbackSheetBehavior.sheetBackgroundDrawable.topLeftCornerResolvedSize
        // The sheet background drawables were just recreated; force the next onPreDraw pass to
        // re-apply corner sizes regardless of the previous ratio.
        lastStretchRatio = -1f
        maxScaleXDistance =
            context.getDimen(MR.dimen.m3_back_progress_bottom_container_max_scale_x_distance)

        binding.playbackSheet.elevation = 0f

        binding.mainScrim.setOnClickListener { binding.homeNewPlaylistFab.close() }
        binding.sheetScrim.setOnClickListener { binding.homeNewPlaylistFab.close() }
        binding.homeShuffleFab.setOnClickListener { playbackModel.shuffleAll() }
        binding.homeNewPlaylistFab.apply {
            inflate(R.menu.new_playlist_actions)
            setOnActionSelectedListener(this@MainFragment)
            setChangeListener(::updateSpeedDial)
        }

        forceHideAllFabs()
        updateSpeedDial(false)
        updateFabVisibility(
            binding,
            homeModel.songList.value,
            homeModel.isFastScrolling.value,
            homeModel.currentTabType.value,
        )

        // --- VIEWMODEL SETUP ---
        // This has to be done here instead of the playback panel to make sure that it's prioritized
        // by StateFlow over any detail fragment.
        // FIXME: This is a consequence of sharing events across several consumers. There has to be
        //  a better way of doing this.
        collect(detailModel.toShow.flow, ::handleShow)
        collectImmediately(detailModel.editedPlaylist, detailBackCallback::invalidateEnabled)
        collectImmediately(homeModel.showOuter.flow, ::handleShowOuter)
        collectImmediately(homeModel.currentTabType, ::updateCurrentTab)
        collectImmediately(homeModel.songList, homeModel.isFastScrolling, ::updateFab)
        collectImmediately(musicModel.indexingState, ::updateIndexerState)
        collectImmediately(listModel.selected, selectionBackCallback::invalidateEnabled)
        collectImmediately(playbackModel.bannerState, ::updateBannerVisibility)
        collectImmediately(playbackModel.openPanel.flow, ::handlePanel)
        collectImmediately(startupPanelCoordinator.routeEvaluation, ::handleStartupRoute)

        collectImmediately(musicModel.startupReadinessState) { updateStartupPanelState() }
        collectImmediately(musicModel.startupLibraryStatus) { updateStartupPanelState() }
        collectImmediately(playbackModel.restoreOutcome) { updateStartupPanelState() }
        collectImmediately(playbackModel.rawPlaybackMetadata) { updateStartupPanelState() }
    }

    private fun updateStartupPanelState() {
        val bannerState = playbackModel.bannerState.value
        val hasPlayableItem =
            bannerState is org.oxycblt.auxio.playback.BannerState.Rich ||
                bannerState is org.oxycblt.auxio.playback.BannerState.Raw
        startupPanelCoordinator.updateState(
            hasPlayableItem = hasPlayableItem,
            outcome = playbackModel.restoreOutcome.value,
            readiness = musicModel.startupReadinessState.value,
            libraryStatus = musicModel.startupLibraryStatus.value,
        )
    }

    override fun onStart() {
        super.onStart()
        val binding = requireBinding()
        // Once we add the destination change callback, we will receive another initialization call,
        // so handle that by resetting the flag.
        requireNotNull(navigationListener) { "NavigationListener was not available" }
            .attach(binding.exploreNavHost.findNavController())
        // Listener could still reasonably fire even if we clear the binding, attach/detach
        // our pre-draw listener our listener in onStart/onStop respectively.
        binding.playbackSheet.viewTreeObserver.addOnPreDrawListener(this@MainFragment)
    }

    override fun onResume() {
        super.onResume()
        // Override the back pressed listener so we can map back navigation to collapsing
        // navigation, navigation out of detail views, etc. We have to do this here in
        // onResume or otherwise the FragmentManager will have precedence.
        requireActivity().onBackPressedDispatcher.apply {
            addCallback(viewLifecycleOwner, requireNotNull(speedDialBackCallback))
            addCallback(viewLifecycleOwner, requireNotNull(selectionBackCallback))
            addCallback(viewLifecycleOwner, requireNotNull(detailBackCallback))
            addCallback(viewLifecycleOwner, requireNotNull(sheetBackCallback))
        }
    }

    override fun onStop() {
        super.onStop()
        val binding = requireBinding()
        requireNotNull(navigationListener) { "NavigationListener was not available" }
            .release(binding.exploreNavHost.findNavController())
        binding.playbackSheet.viewTreeObserver.removeOnPreDrawListener(this)
    }

    private fun applyHeadUnitDriverSideLayout(binding: FragmentMainBinding) {
        val dualPaneRoot = binding.playbackPanelFragment.parent
        if (
            dualPaneRoot !is ConstraintLayout ||
                uiSettings.driverSide != UISettings.DriverSide.RIGHT
        ) {
            return
        }
        ConstraintSet().apply {
            clone(dualPaneRoot)
            clear(R.id.playback_panel_fragment, ConstraintSet.START)
            clear(R.id.playback_panel_fragment, ConstraintSet.END)
            connect(
                R.id.playback_panel_fragment,
                ConstraintSet.START,
                R.id.queue_sheet,
                ConstraintSet.END,
            )
            connect(
                R.id.playback_panel_fragment,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
            )
            clear(R.id.queue_sheet, ConstraintSet.START)
            clear(R.id.queue_sheet, ConstraintSet.END)
            connect(
                R.id.queue_sheet,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,
            )
            connect(
                R.id.queue_sheet,
                ConstraintSet.END,
                R.id.playback_panel_fragment,
                ConstraintSet.START,
            )
            applyTo(dualPaneRoot)
        }
    }

    override fun onDestroyBinding(binding: FragmentMainBinding) {
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?
        playbackSheetBehavior.removeBottomSheetCallback(panelRouteSheetCallback)
        queueSheetBehavior?.removeBottomSheetCallback(panelRouteSheetCallback)
        super.onDestroyBinding(binding)
        speedDialBackCallback = null
        sheetBackCallback = null
        detailBackCallback = null
        selectionBackCallback = null
        navigationListener = null
        binding.homeNewPlaylistFab.setChangeListener(null)
        binding.homeNewPlaylistFab.setOnActionSelectedListener(null)
    }

    override fun onPreDraw(): Boolean {
        // This is where I shove literally all the UI logic that won't behave any callback
        // or "normal" method I've tried. Surely running this on every frame will actually cause
        // it to work properly!

        // We overload CoordinatorLayout far too much to rely on any of it's typical
        // listener functionality. Just update all transitions before every draw. Should
        // probably be cheap enough.
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?

        val playbackRatio = max(playbackSheetBehavior.calculateSlideOffset(), 0f)
        // Stupid hack to prevent you from sliding the sheet up without closing the speed
        // dial. Filtering out ACTION_MOVE events will cause back gestures to close the
        // speed dial, which is super finicky behavior.
        val rising = playbackRatio > 0f
        if (rising != sheetRising) {
            sheetRising = rising
            updateFabVisibility(
                binding,
                homeModel.songList.value,
                homeModel.isFastScrolling.value,
                homeModel.currentTabType.value,
            )
        }

        val playbackOutRatio = 1 - min(playbackRatio * 2, 1f)
        val playbackInRatio = max(playbackRatio - 0.5f, 0f) * 2

        val playbackMaxXScaleDelta = maxScaleXDistance / binding.playbackSheet.width
        val playbackEdgeRatio = max(playbackRatio - 0.9f, 0f) / 0.1f
        val playbackBackRatio =
            max(1 - ((1 - binding.playbackSheet.scaleX) / playbackMaxXScaleDelta), 0f)
        val playbackLastStretchRatio = min(playbackEdgeRatio * playbackBackRatio, 1f)
        binding.mainSheetScrim.alpha = playbackLastStretchRatio

        // Mutating the sheet background shape invalidates the drawable; only do it when the
        // ratio actually changed so idle frames don't redraw the sheet background every vsync
        // on weak head-unit GPUs. Exact equality is intentional: idle frames recompute the
        // ratio from identical inputs, yielding a bit-identical value, while any real sheet
        // movement must always be applied.
        val stretchChanged = playbackLastStretchRatio != lastStretchRatio
        if (stretchChanged) {
            lastStretchRatio = playbackLastStretchRatio
            playbackSheetBehavior.sheetBackgroundDrawable.setCornerSize(
                normalCornerSize * (1 - playbackLastStretchRatio)
            )
        }
        binding.exploreNavHost.isInvisible = playbackLastStretchRatio == 1f
        binding.playbackSheet.translationZ = (1 - playbackLastStretchRatio) * elevationNormal

        if (queueSheetBehavior != null) {
            val queueRatio = max(queueSheetBehavior.calculateSlideOffset(), 0f)
            val queueInRatio = max(queueRatio - 0.5f, 0f) * 2

            val queueMaxXScaleDelta = maxScaleXDistance / binding.queueSheet.width
            val queueBackRatio =
                max(1 - ((1 - binding.queueSheet.scaleX) / queueMaxXScaleDelta), 0f)

            val queueEdgeRatio = max(queueRatio - 0.9f, 0f) / 0.1f

            val queueBarEdgeRatio = max(queueEdgeRatio - 0.5f, 0f) * 2
            val queueBarBackRatio = max(queueBackRatio - 0.5f, 0f) * 2
            val queueBarRatio = min(queueBarEdgeRatio * queueBarBackRatio, 1f)

            val queuePanelEdgeRatio = min(queueEdgeRatio * 2, 1f)
            val queuePanelBackRatio = min(queueBackRatio * 2, 1f)
            val queuePanelRatio = 1 - min(queuePanelEdgeRatio * queuePanelBackRatio, 1f)

            binding.playbackBarFragment.alpha = max(playbackOutRatio, queueBarRatio)
            binding.playbackPanelFragment.alpha = min(playbackInRatio, queuePanelRatio)
            binding.queueFragment.alpha = queueInRatio

            if (playbackModel.song.value != null) {
                // Playback sheet intercepts queue sheet touch events, prevent that from
                // occurring by disabling dragging whenever the queue sheet is expanded.
                playbackSheetBehavior.isDraggable =
                    queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED
            }
        } else {
            // No queue sheet, fade normally based on the playback sheet
            binding.playbackBarFragment.alpha = playbackOutRatio
            binding.playbackPanelFragment.alpha = playbackInRatio
            if (stretchChanged) {
                (binding.queueSheet.background as? MaterialShapeDrawable)?.shapeAppearanceModel =
                    ShapeAppearanceModel.builder()
                        .setTopLeftCornerSize(normalCornerSize)
                        .setTopRightCornerSize(normalCornerSize * (1 - playbackLastStretchRatio))
                        .build()
            }
        }
        // Fade out the playback bar as the panel expands.
        binding.playbackBarFragment.apply {
            // Prevent interactions when the playback bar fully fades out.
            isInvisible = alpha == 0f
        }

        // Prevent interactions when the playback panel fully fades out.
        binding.playbackPanelFragment.isInvisible = binding.playbackPanelFragment.alpha == 0f

        binding.queueSheet.apply {
            // Queue sheet (not queue content) should fade out with the playback panel.
            alpha = playbackInRatio
            // Prevent interactions when the queue sheet fully fades out.
            binding.queueSheet.isInvisible = alpha == 0f
        }

        // Prevent interactions when the queue content fully fades out.
        binding.queueFragment.isInvisible = binding.queueFragment.alpha == 0f

        // Since the navigation listener is also reliant on the bottom sheets, we must also update
        // it every frame.
        requireNotNull(sheetBackCallback) { "SheetBackPressedCallback was not available" }
            .invalidateEnabled()

        // Stop the FrameLayout containing the fabs from eating touch events elsewhere
        binding.mainFabContainer.isVisible =
            binding.homeNewPlaylistFab.mainFab.isVisible || binding.homeShuffleFab.isVisible

        return true
    }

    override fun onActionSelected(actionItem: SpeedDialActionItem): Boolean {
        when (actionItem.id) {
            R.id.action_new_playlist -> {
                L.d("Creating playlist")
                musicModel.createPlaylist()
            }
            R.id.action_import_playlist -> {
                L.d("Importing playlist")
                musicModel.importPlaylist()
            }
            else -> {}
        }
        // Returning false to close the speed dial results in no animation, manually close instead.
        // Adapted from Material Files: https://github.com/zhanghai/MaterialFiles
        requireBinding().homeNewPlaylistFab.close()
        return true
    }

    private fun onExploreNavigate() {
        listModel.dropSelection()
        updateFabVisibility(
            requireBinding(),
            homeModel.songList.value,
            homeModel.isFastScrolling.value,
            homeModel.currentTabType.value,
        )
    }

    private fun updateCurrentTab(tabType: MusicType) {
        val binding = requireBinding()
        updateFabVisibility(
            binding,
            homeModel.songList.value,
            homeModel.isFastScrolling.value,
            tabType,
        )
    }

    private fun updateIndexerState(state: IndexingState?) {
        if (state is IndexingState.Completed && state.error == null) {
            L.d("Received ok response")
            val binding = requireBinding()
            updateFabVisibility(
                binding,
                homeModel.songList.value,
                homeModel.isFastScrolling.value,
                homeModel.currentTabType.value,
            )
        }
    }

    private fun updateFab(songs: List<Song>, isFastScrolling: Boolean) {
        val binding = requireBinding()
        updateFabVisibility(binding, songs, isFastScrolling, homeModel.currentTabType.value)
    }

    private fun updateFabVisibility(
        binding: FragmentMainBinding,
        songs: List<Song>,
        isFastScrolling: Boolean,
        tabType: MusicType,
    ) {
        // If there are no songs, it's likely that the library has not been loaded, so
        // displaying the shuffle FAB makes no sense. We also don't want the fast scroll
        // popup to overlap with the FAB, so we hide the FAB when fast scrolling too.
        if (shouldHideAllFabs(binding, songs, isFastScrolling)) {
            L.d("Hiding fab: [empty: ${songs.isEmpty()} scrolling: $isFastScrolling]")
            forceHideAllFabs()
        } else {
            if (tabType != MusicType.PLAYLISTS) {
                if (binding.homeShuffleFab.isOrWillBeShown) {
                    return
                }

                if (binding.homeNewPlaylistFab.mainFab.isOrWillBeShown) {
                    L.d("Animating transition")
                    binding.homeNewPlaylistFab.hide(
                        object : FloatingActionButton.OnVisibilityChangedListener() {
                            override fun onHidden(fab: FloatingActionButton) {
                                super.onHidden(fab)
                                if (
                                    shouldHideAllFabs(
                                        binding,
                                        homeModel.songList.value,
                                        homeModel.isFastScrolling.value,
                                    )
                                ) {
                                    return
                                }
                                binding.homeShuffleFab.show()
                            }
                        }
                    )
                } else {
                    L.d("Showing immediately")
                    binding.homeShuffleFab.show()
                }
            } else {
                L.d("Showing playlist button")
                if (binding.homeNewPlaylistFab.mainFab.isOrWillBeShown) {
                    return
                }

                if (binding.homeShuffleFab.isOrWillBeShown) {
                    L.d("Animating transition")
                    binding.homeShuffleFab.hide(
                        object : FloatingActionButton.OnVisibilityChangedListener() {
                            override fun onHidden(fab: FloatingActionButton) {
                                super.onHidden(fab)
                                if (
                                    shouldHideAllFabs(
                                        binding,
                                        homeModel.songList.value,
                                        homeModel.isFastScrolling.value,
                                    )
                                ) {
                                    return
                                }
                                binding.homeNewPlaylistFab.show()
                            }
                        }
                    )
                } else {
                    L.d("Showing immediately")
                    binding.homeNewPlaylistFab.show()
                }
            }
        }
    }

    private fun shouldHideAllFabs(
        binding: FragmentMainBinding,
        songs: List<Song>,
        isFastScrolling: Boolean,
    ) =
        binding.exploreNavHost.findNavController().currentDestination?.id != R.id.home_fragment ||
            sheetRising == true ||
            songs.isEmpty() ||
            isFastScrolling

    private fun forceHideAllFabs() {
        val binding = requireBinding()
        if (binding.homeShuffleFab.isOrWillBeShown) {
            FAB_HIDE_FROM_USER_FIELD.invoke(binding.homeShuffleFab, null, false)
        }
        if (binding.homeNewPlaylistFab.isOpen) {
            binding.homeNewPlaylistFab.close()
        }
        if (binding.homeNewPlaylistFab.mainFab.isOrWillBeShown) {
            FAB_HIDE_FROM_USER_FIELD.invoke(binding.homeNewPlaylistFab.mainFab, null, false)
        }
    }

    private fun updateSpeedDial(open: Boolean) {
        requireNotNull(speedDialBackCallback) { "SpeedDialBackPressedCallback was not available" }
            .invalidateEnabled(open)
        val binding = requireBinding()
        binding.mainScrim.isInvisible = !open
        binding.sheetScrim.isInvisible = !open
    }

    private fun handleShow(show: Show?) {
        when (show) {
            is Show.SongAlbumDetails,
            is Show.ArtistDetails,
            is Show.AlbumDetails -> playbackModel.openMain()
            is Show.SongDetails,
            is Show.SongArtistDecision,
            is Show.AlbumArtistDecision,
            is Show.GenreDetails,
            is Show.PlaylistDetails,
            null -> {}
        }
    }

    private fun handleShowOuter(outer: Outer?) {
        val directions =
            when (outer) {
                is Outer.Settings -> MainFragmentDirections.preferences()
                is Outer.About -> MainFragmentDirections.about()
                null -> return
            }
        startupPanelCoordinator.cancelRouting("explicit-outer-navigation")
        findNavController().navigateSafe(directions)
        homeModel.showOuter.consume()
    }

    private fun updateBannerVisibility(state: org.oxycblt.auxio.playback.BannerState) {
        val isExpandable =
            state is org.oxycblt.auxio.playback.BannerState.Rich ||
                state is org.oxycblt.auxio.playback.BannerState.Raw
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior
                as org.oxycblt.auxio.playback.PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior
                as org.oxycblt.auxio.playback.queue.QueueBottomSheetBehavior?

        if (
            playbackSheetBehavior.targetState ==
                com.google.android.material.bottomsheet.BackportBottomSheetBehavior.STATE_HIDDEN
        ) {
            timber.log.Timber.d("Unhiding playback sheet for persistent banner")
            playbackSheetBehavior.state =
                com.google.android.material.bottomsheet.BackportBottomSheetBehavior.STATE_COLLAPSED
        }

        playbackSheetBehavior.isDraggable = isExpandable
        queueSheetBehavior?.isDraggable = isExpandable

        if (!isExpandable) {
            if (
                playbackSheetBehavior.state ==
                    com.google.android.material.bottomsheet.BackportBottomSheetBehavior
                        .STATE_EXPANDED
            ) {
                playbackSheetBehavior.state =
                    com.google.android.material.bottomsheet.BackportBottomSheetBehavior
                        .STATE_COLLAPSED
            }
            if (
                queueSheetBehavior?.state ==
                    com.google.android.material.bottomsheet.BackportBottomSheetBehavior
                        .STATE_EXPANDED
            ) {
                queueSheetBehavior.state =
                    com.google.android.material.bottomsheet.BackportBottomSheetBehavior
                        .STATE_COLLAPSED
            }
        }

        updateStartupPanelState()
    }

    private fun handleStartupRoute(
        evaluation: org.oxycblt.auxio.headunit.StartupPanelCoordinator.RouteEvaluation
    ) {
        when (evaluation) {
            org.oxycblt.auxio.headunit.StartupPanelCoordinator.RouteEvaluation.Idle,
            is org.oxycblt.auxio.headunit.StartupPanelCoordinator.RouteEvaluation.Wait -> Unit
            is org.oxycblt.auxio.headunit.StartupPanelCoordinator.RouteEvaluation.Cancel ->
                startupPanelCoordinator.cancelRoute(evaluation.request.token, evaluation.reason)
            is org.oxycblt.auxio.headunit.StartupPanelCoordinator.RouteEvaluation.Render -> {
                val request = evaluation.request
                val rendered =
                    when (request.destination) {
                        OpenPanel.MAIN -> tryClosePlaybackPanel(cancelPendingRoute = false)
                        OpenPanel.PLAYBACK -> tryOpenPlaybackPanel(cancelPendingRoute = false)
                        OpenPanel.PLAYBACK_QUEUE,
                        OpenPanel.QUEUE -> tryOpenPlaybackQueuePanel(cancelPendingRoute = false)
                    }
                if (rendered) {
                    L.d(
                        "Consumed rendered startup route: " +
                            "${request.description} -> ${request.destination}"
                    )
                    startupPanelCoordinator.consumeRoute(request.token)
                }
            }
        }
    }

    private fun handlePanel(panel: OpenPanel?) {
        if (panel == null) return
        L.d("Trying to update panel to $panel")
        when (panel) {
            OpenPanel.MAIN -> tryClosePlaybackPanel()
            OpenPanel.PLAYBACK -> tryOpenPlaybackPanel()
            OpenPanel.PLAYBACK_QUEUE -> {
                tryOpenPlaybackQueuePanel()
                val root = requireBinding().root
                root.post {
                    if (isAdded && view != null) {
                        tryOpenPlaybackQueuePanel()
                    }
                }
            }
            OpenPanel.QUEUE -> tryOpenQueuePanel()
        }
        playbackModel.openPanel.consume()
    }

    private fun tryOpenPlaybackPanel(cancelPendingRoute: Boolean = true): Boolean {
        if (cancelPendingRoute) {
            startupPanelCoordinator.cancelRouting("manual-playback-open")
        }
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?

        if (playbackSheetBehavior.state != BackportBottomSheetBehavior.STATE_EXPANDED) {
            if (playbackSheetBehavior.targetState == BackportBottomSheetBehavior.STATE_COLLAPSED) {
                L.d("Expanding playback sheet")
                playbackSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
            }
            return false
        }
        if (queueSheetBehavior == null) return true
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED) {
            return true
        }
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED) {
            queueSheetBehavior.state = BackportBottomSheetBehavior.STATE_COLLAPSED
        }
        return false
    }

    private fun tryOpenPlaybackQueuePanel(cancelPendingRoute: Boolean = true): Boolean {
        if (cancelPendingRoute) {
            startupPanelCoordinator.cancelRouting("manual-playback-queue-open")
        }
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?

        if (playbackSheetBehavior.state != BackportBottomSheetBehavior.STATE_EXPANDED) {
            if (playbackSheetBehavior.targetState == BackportBottomSheetBehavior.STATE_COLLAPSED) {
                L.d("Expanding playback sheet before queue destination")
                playbackSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
            }
            return false
        }
        if (queueSheetBehavior == null) return true
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED) {
            return true
        }
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED) {
            queueSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
        }
        return false
    }

    private fun tryClosePlaybackPanel(cancelPendingRoute: Boolean = true): Boolean {
        if (cancelPendingRoute) {
            startupPanelCoordinator.cancelRouting("manual-playback-close")
        }
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?
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

    private fun tryOpenQueuePanel(cancelPendingRoute: Boolean = true): Boolean {
        if (cancelPendingRoute) {
            startupPanelCoordinator.cancelRouting("manual-queue-open")
        }
        val binding = requireBinding()
        val playbackSheetBehavior =
            binding.playbackSheet.coordinatorLayoutBehavior as PlaybackBottomSheetBehavior
        val queueSheetBehavior =
            binding.queueSheet.coordinatorLayoutBehavior as QueueBottomSheetBehavior?
                ?: return playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED
        if (queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED) {
            return true
        }
        if (
            playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED &&
                queueSheetBehavior.state == BackportBottomSheetBehavior.STATE_COLLAPSED
        ) {
            queueSheetBehavior.state = BackportBottomSheetBehavior.STATE_EXPANDED
        }
        return false
    }

    private class SheetBackPressedCallback(
        private val playbackSheetBehavior: PlaybackBottomSheetBehavior<*>,
        private val queueSheetBehavior: QueueBottomSheetBehavior<*>?,
        private val onManualPanelNavigation: () -> Unit,
    ) : OnBackPressedCallback(false) {
        override fun handleOnBackStarted(backEvent: BackEventCompat) {
            onManualPanelNavigation()
            if (queueSheetShown()) {
                unlikelyToBeNull(queueSheetBehavior).startBackProgress(backEvent)
            }

            if (playbackSheetShown()) {
                playbackSheetBehavior.startBackProgress(backEvent)
                return
            }
        }

        override fun handleOnBackProgressed(backEvent: BackEventCompat) {
            if (queueSheetShown()) {
                unlikelyToBeNull(queueSheetBehavior).updateBackProgress(backEvent)
                return
            }

            if (playbackSheetShown()) {
                playbackSheetBehavior.updateBackProgress(backEvent)
                return
            }
        }

        override fun handleOnBackPressed() {
            onManualPanelNavigation()
            if (queueSheetShown()) {
                unlikelyToBeNull(queueSheetBehavior).handleBackInvoked()
                return
            }

            if (playbackSheetShown()) {
                playbackSheetBehavior.handleBackInvoked()
                return
            }
        }

        override fun handleOnBackCancelled() {
            if (queueSheetShown()) {
                unlikelyToBeNull(queueSheetBehavior).cancelBackProgress()
                return
            }

            if (playbackSheetShown()) {
                playbackSheetBehavior.cancelBackProgress()
                return
            }
        }

        fun invalidateEnabled() {
            isEnabled = queueSheetShown() || playbackSheetShown()
        }

        private fun playbackSheetShown() =
            playbackSheetBehavior.targetState != BackportBottomSheetBehavior.STATE_COLLAPSED &&
                playbackSheetBehavior.targetState != BackportBottomSheetBehavior.STATE_HIDDEN

        private fun queueSheetShown() =
            queueSheetBehavior != null &&
                playbackSheetBehavior.state == BackportBottomSheetBehavior.STATE_EXPANDED &&
                queueSheetBehavior.targetState != BackportBottomSheetBehavior.STATE_COLLAPSED
    }

    private class DetailBackPressedCallback(private val detailModel: DetailViewModel) :
        OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (detailModel.dropPlaylistEdit()) {
                L.d("Dropped playlist edits")
            }
        }

        fun invalidateEnabled(playlistEdit: List<Song>?) {
            isEnabled = playlistEdit != null
        }
    }

    private inner class SelectionBackPressedCallback(private val listModel: ListViewModel) :
        OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (listModel.dropSelection()) {
                L.d("Dropped selection")
            }
        }

        fun invalidateEnabled(selection: List<Music>) {
            isEnabled = selection.isNotEmpty()
        }
    }

    private inner class SpeedDialBackPressedCallback : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            val binding = requireBinding()
            if (binding.homeNewPlaylistFab.isOpen) {
                binding.homeNewPlaylistFab.close()
            }
        }

        fun invalidateEnabled(open: Boolean) {
            isEnabled = open
        }
    }

    private companion object {
        val FAB_HIDE_FROM_USER_FIELD: Method by
            lazyReflectedMethod(
                FloatingActionButton::class,
                "hide",
                FloatingActionButton.OnVisibilityChangedListener::class,
                Boolean::class,
            )
    }
}
