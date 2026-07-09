/*
 * Copyright (c) 2026 Auxio Project
 * HomeListEmptyState.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
    val cachedStartupPending = empty && startupState is StartupReadinessState.CheckingCachedLibrary
    val showEmptyPanel = empty || cachedStartupPending || actionVisibleWhenNotEmpty
    val canShowSourceAction =
        !cachedStartupPending && (indexingState == null || indexingState is IndexingState.Completed)

    homeRecycler.isInvisible = empty
    homeNoMusic.isInvisible = !showEmptyPanel
    homeNoMusicProgress.isVisible = cachedStartupPending
    homeNoMusicPlaceholder.isVisible = showEmptyPanel && !cachedStartupPending
    homeNoMusicMsg.setText(
        when {
            cachedStartupPending -> R.string.lng_loading_cached_music_library
            startupState is StartupReadinessState.NeedsMusicSource -> R.string.lng_music_source_needed
            startupState is StartupReadinessState.CachedLibraryUnavailable ->
                R.string.lng_cached_library_unavailable
            else -> emptyMessage
        }
    )
    homeNoMusicAction.isVisible =
        if (actionVisibleWhenNotEmpty) true else empty && canShowSourceAction
    homeNoMusicAction.isEnabled = homeNoMusicAction.isVisible
}
