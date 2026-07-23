/*
 * Copyright (c) 2026 Auxio Project
 * HomeListEmptyState.kt is part of Auxio.
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

package org.oxycblt.auxio.home.list

import androidx.annotation.StringRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.material.button.MaterialButton
import org.oxycblt.auxio.R
import org.oxycblt.auxio.databinding.FragmentHomeListBinding
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.StartupReadinessState

/**
 * Legacy compact empty-state renderer used by non-song tabs.
 *
 * The song tab uses [updateLibraryRecoveryState] so that first-launch and missing-library recovery
 * remains actionable while startup capabilities or indexing progress are still advancing.
 */
fun FragmentHomeListBinding.updateLibraryEmptyState(
    empty: Boolean,
    indexingState: IndexingState?,
    startupState: StartupReadinessState,
    @StringRes emptyMessage: Int,
    actionVisibleWhenNotEmpty: Boolean = false,
) {
    val cachedStartupPending =
        empty && startupState.rank < StartupReadinessState.FastBrowseReady.rank
    val indexingPending = empty && indexingState is IndexingState.Indexing
    val showProgress = cachedStartupPending || indexingPending
    val showEmptyPanel = empty || showProgress || actionVisibleWhenNotEmpty
    val canShowSourceAction =
        !showProgress && (indexingState == null || indexingState is IndexingState.Completed)

    homeRecycler.isInvisible = empty
    homeNoMusic.isInvisible = !showEmptyPanel
    homeNoMusicProgress.isVisible = showProgress
    homeNoMusicPlaceholder.isVisible = showEmptyPanel && !showProgress
    homeNoMusicMsg.setText(
        when {
            cachedStartupPending -> R.string.lng_loading_cached_music_library
            indexingPending -> R.string.lng_loading_music_library
            else -> emptyMessage
        }
    )
    homeNoMusicAction.isVisible =
        if (actionVisibleWhenNotEmpty) true else empty && canShowSourceAction
    homeNoMusicAction.isEnabled = homeNoMusicAction.isVisible
    homeNoMusicSecondaryAction.isVisible = false
    homeNoMusicTertiaryAction.isVisible = false
}

/** Render the state-driven first-launch and library-recovery surface used by the Songs tab. */
fun FragmentHomeListBinding.updateLibraryRecoveryState(state: LibraryRecoveryPolicy.State) {
    homeRecycler.isInvisible = state.visible
    homeNoMusic.isInvisible = !state.visible
    homeNoMusicProgress.isVisible = state.visible && state.showProgress
    homeNoMusicPlaceholder.isVisible = state.visible && !state.showProgress
    if (state.visible) {
        homeNoMusicMsg.setText(state.kind.messageRes)
    }
    homeNoMusicAction.bindRecoveryAction(state.primary)
    homeNoMusicSecondaryAction.bindRecoveryAction(state.secondary)
    homeNoMusicTertiaryAction.bindRecoveryAction(state.tertiary)
}

private fun MaterialButton.bindRecoveryAction(item: LibraryRecoveryPolicy.ActionItem?) {
    isVisible = item != null
    isEnabled = item?.enabled == true
    item?.let { setText(it.action.labelRes) }
}

private val LibraryRecoveryPolicy.Kind.messageRes: Int
    @StringRes
    get() =
        when (this) {
            LibraryRecoveryPolicy.Kind.HIDDEN -> R.string.lng_empty_songs
            LibraryRecoveryPolicy.Kind.WAITING -> R.string.recovery_loading_cache
            LibraryRecoveryPolicy.Kind.PERMISSION_REQUIRED -> R.string.recovery_permission_required
            LibraryRecoveryPolicy.Kind.SOURCE_REQUIRED -> R.string.recovery_source_required
            LibraryRecoveryPolicy.Kind.SOURCE_UNAVAILABLE -> R.string.recovery_source_unavailable
            LibraryRecoveryPolicy.Kind.CACHE_UNAVAILABLE -> R.string.recovery_cache_unavailable
            LibraryRecoveryPolicy.Kind.INDEXING -> R.string.recovery_indexing
            LibraryRecoveryPolicy.Kind.EMPTY -> R.string.recovery_empty
            LibraryRecoveryPolicy.Kind.FAILED -> R.string.recovery_failed
        }

private val LibraryRecoveryPolicy.Action.labelRes: Int
    @StringRes
    get() =
        when (this) {
            LibraryRecoveryPolicy.Action.GRANT_PERMISSION ->
                R.string.recovery_action_grant_permission
            LibraryRecoveryPolicy.Action.CHOOSE_SOURCE -> R.string.recovery_action_choose_source
            LibraryRecoveryPolicy.Action.REFRESH -> R.string.recovery_action_refresh
            LibraryRecoveryPolicy.Action.RESCAN -> R.string.recovery_action_rescan
            LibraryRecoveryPolicy.Action.ENABLE_ROOT -> R.string.recovery_action_enable_root
        }
