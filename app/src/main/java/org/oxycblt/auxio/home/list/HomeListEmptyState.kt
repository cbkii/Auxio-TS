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
import org.oxycblt.auxio.R
import org.oxycblt.auxio.databinding.FragmentHomeListBinding
import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.StartupReadinessState

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
}
