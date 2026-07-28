/*
 * Copyright (c) 2024 Auxio Project
 * EvaluateStep.kt is part of Auxio.
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

package org.oxycblt.musikr.pipeline

import android.content.Context
import android.os.SystemClock
import android.util.Log
import kotlinx.coroutines.channels.Channel
import org.oxycblt.musikr.BuildConfig
import org.oxycblt.musikr.Config
import org.oxycblt.musikr.Interpretation
import org.oxycblt.musikr.MutableLibrary
import org.oxycblt.musikr.graph.MusicGraph
import org.oxycblt.musikr.model.LibraryFactory
import org.oxycblt.musikr.playlist.db.StoredPlaylists
import org.oxycblt.musikr.playlist.interpret.PlaylistInterpreter
import org.oxycblt.musikr.tag.interpret.TagInterpreter

internal interface EvaluateStep {
    suspend fun evaluate(
        extractedMusic: Channel<Extracted>,
        onItemStarted: suspend (Extracted) -> Unit = {},
        onItemCompleted: suspend (Extracted) -> Unit = {},
    ): MutableLibrary

    companion object {
        fun new(context: Context, config: Config, interpretation: Interpretation): EvaluateStep =
            EvaluateStepImpl(
                context,
                TagInterpreter.new(interpretation),
                PlaylistInterpreter.new(interpretation),
                config.storage.storedPlaylists,
                LibraryFactory.new(),
            )
    }
}

private class EvaluateStepImpl(
    private val context: Context,
    private val tagInterpreter: TagInterpreter,
    private val playlistInterpreter: PlaylistInterpreter,
    private val storedPlaylists: StoredPlaylists,
    private val libraryFactory: LibraryFactory,
) : EvaluateStep {
    override suspend fun evaluate(
        extractedMusic: Channel<Extracted>,
        onItemStarted: suspend (Extracted) -> Unit,
        onItemCompleted: suspend (Extracted) -> Unit,
    ): MutableLibrary {
        val builder = MusicGraph.builder()
        for (extracted in extractedMusic) {
            onItemStarted(extracted)
            val startedAtElapsedMs = SystemClock.elapsedRealtime()
            when (extracted) {
                is RawSong -> builder.add(tagInterpreter.interpret(extracted))
                is RawPlaylist -> builder.add(playlistInterpreter.interpret(extracted.file))
                is NotAudio -> {}
                is InvalidSong -> {}
            }
            onItemCompleted(extracted)
            val elapsedMs = SystemClock.elapsedRealtime() - startedAtElapsedMs
            if (elapsedMs >= SLOW_ITEM_WARNING_MS) {
                Log.w(
                    "EvaluateStep",
                    "Slow library item evaluation [elapsedMs=$elapsedMs item=${extracted.label()}]",
                )
            }
        }
        val graph = builder.build()

        // Render graph to Graphviz only when explicitly opted in via Android system property.
        // This avoids catastrophic startup cost on debug/head-unit builds where the graph
        // can be very large. Enable with: adb shell setprop debug.auxio.graphviz true
        val isGraphvizEnabled =
            try {
                val clazz = Class.forName("android.os.SystemProperties")
                val get = clazz.getMethod("get", String::class.java)
                get.invoke(null, "debug.auxio.graphviz") == "true"
            } catch (e: Exception) {
                false
            }
        if (BuildConfig.DEBUG && isGraphvizEnabled) {
            try {
                val fileName = "music_graph_debug.dot"
                graph.renderToGraphviz(context, fileName)
                val filePath = context.filesDir.resolve(fileName).absolutePath
                Log.d("EvaluateStep", "Music graph rendered to: $filePath")
                Log.d("EvaluateStep", "To pull the file, run: adb pull $filePath")
            } catch (e: Exception) {
                Log.e("EvaluateStep", "Failed to render music graph", e)
            }
        }

        return libraryFactory.create(graph, storedPlaylists, playlistInterpreter)
    }

    private fun Extracted.label(): String =
        when (this) {
            is RawSong -> file.path.resolve(context)
            is RawPlaylist -> file.name
            is InvalidSong -> "invalid song"
            is NotAudio -> "non-audio file"
        }

    private companion object {
        const val SLOW_ITEM_WARNING_MS = 5_000L
    }
}
