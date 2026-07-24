/*
 * Copyright (c) 2026 Auxio Project
 * LibraryRecoveryPolicy.kt is part of Auxio.
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

import org.oxycblt.auxio.music.IndexingState
import org.oxycblt.auxio.music.StartupLibraryStatus
import org.oxycblt.auxio.music.StartupReadinessState

/** Pure first-launch and missing-library recovery policy. */
object LibraryRecoveryPolicy {
    data class Input(
        val empty: Boolean,
        val indexingState: IndexingState?,
        val startupState: StartupReadinessState,
        val libraryStatus: StartupLibraryStatus,
        val sourceConfigured: Boolean,
        val storagePermissionRequired: Boolean,
        val storagePermissionGranted: Boolean,
        val lastScanFailed: Boolean,
    )

    enum class Kind {
        HIDDEN,
        WAITING,
        PERMISSION_REQUIRED,
        SOURCE_REQUIRED,
        SOURCE_UNAVAILABLE,
        CACHE_UNAVAILABLE,
        INDEXING,
        EMPTY,
        FAILED,
    }

    enum class Action {
        GRANT_PERMISSION,
        CHOOSE_SOURCE,
        REFRESH,
        RESCAN,
    }

    data class ActionItem(val action: Action, val enabled: Boolean = true)

    data class State(
        val kind: Kind,
        val showProgress: Boolean,
        val primary: ActionItem? = null,
        val secondary: ActionItem? = null,
        val tertiary: ActionItem? = null,
    ) {
        val visible: Boolean
            get() = kind != Kind.HIDDEN
    }

    fun resolve(input: Input): State {
        if (!input.empty) return State(Kind.HIDDEN, showProgress = false)

        if (input.storagePermissionRequired && !input.storagePermissionGranted) {
            return State(
                kind = Kind.PERMISSION_REQUIRED,
                showProgress = false,
                primary = ActionItem(Action.GRANT_PERMISSION),
                secondary = ActionItem(Action.CHOOSE_SOURCE),
            )
        }
        if (input.indexingState is IndexingState.Indexing) {
            return State(
                kind = Kind.INDEXING,
                showProgress = true,
                primary = ActionItem(Action.CHOOSE_SOURCE),
            )
        }
        if (
            input.libraryStatus == StartupLibraryStatus.NeedsMusicSource || !input.sourceConfigured
        ) {
            return State(
                kind = Kind.SOURCE_REQUIRED,
                showProgress = false,
                primary = ActionItem(Action.CHOOSE_SOURCE),
            )
        }
        if (input.libraryStatus == StartupLibraryStatus.SourceUnavailable) {
            return State(
                kind = Kind.SOURCE_UNAVAILABLE,
                showProgress = false,
                primary = ActionItem(Action.REFRESH),
                secondary = ActionItem(Action.CHOOSE_SOURCE),
            )
        }
        if (
            input.lastScanFailed ||
                (input.indexingState is IndexingState.Completed &&
                    input.indexingState.error != null)
        ) {
            return State(
                kind = Kind.FAILED,
                showProgress = false,
                primary = ActionItem(Action.REFRESH),
                secondary = ActionItem(Action.RESCAN),
                tertiary = ActionItem(Action.CHOOSE_SOURCE),
            )
        }
        return when (input.libraryStatus) {
            StartupLibraryStatus.CacheUnavailable ->
                State(
                    kind = Kind.CACHE_UNAVAILABLE,
                    showProgress = false,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.RESCAN),
                    tertiary = ActionItem(Action.CHOOSE_SOURCE),
                )
            StartupLibraryStatus.Empty ->
                State(
                    kind = Kind.EMPTY,
                    showProgress = false,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.RESCAN),
                    tertiary = ActionItem(Action.CHOOSE_SOURCE),
                )
            StartupLibraryStatus.Unknown -> {
                val waiting = input.startupState.rank < StartupReadinessState.FastBrowseReady.rank
                State(
                    kind = if (waiting) Kind.WAITING else Kind.CACHE_UNAVAILABLE,
                    showProgress = waiting,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.CHOOSE_SOURCE),
                )
            }
            StartupLibraryStatus.Usable ->
                State(
                    kind = Kind.CACHE_UNAVAILABLE,
                    showProgress = false,
                    primary = ActionItem(Action.REFRESH),
                    secondary = ActionItem(Action.RESCAN),
                    tertiary = ActionItem(Action.CHOOSE_SOURCE),
                )
            StartupLibraryStatus.NeedsMusicSource,
            StartupLibraryStatus.SourceUnavailable -> error("Handled above")
        }
    }
}
