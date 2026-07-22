/*
 * Copyright (c) 2026 Auxio Project
 * VisualizerDiagnosticsResolver.kt is part of Auxio.
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

package org.oxycblt.auxio.playback.ui.visualizer

import android.content.Context
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import org.oxycblt.auxio.diagnostics.DiagnosticJournal

internal object VisualizerDiagnosticsResolver {
    fun resolve(context: Context): DiagnosticJournal? =
        runCatching {
                EntryPointAccessors.fromApplication(
                        context.applicationContext,
                        VisualizerDiagnosticsEntryPoint::class.java,
                    )
                    .diagnosticJournal()
            }
            .getOrNull()
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface VisualizerDiagnosticsEntryPoint {
    fun diagnosticJournal(): DiagnosticJournal
}
