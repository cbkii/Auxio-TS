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
    suspend fun evaluate(extractedMusic: Channel<Extracted>): MutableLibrary

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
    override suspend fun evaluate(extractedMusic: Channel<Extracted>): MutableLibrary {
        val start = System.currentTimeMillis()
        val builder = MusicGraph.builder()
        for (extracted in extractedMusic) {
            when (extracted) {
                is RawSong -> builder.add(tagInterpreter.interpret(extracted))
                is RawPlaylist -> builder.add(playlistInterpreter.interpret(extracted.file))
                is NotAudio -> {}
                is InvalidSong -> {}
            }
        }
        val buildStart = System.currentTimeMillis()
        val graph = builder.build()
        Log.d("EvaluateStep", "Graph build took ${System.currentTimeMillis() - buildStart}ms")

        // Render graph to Graphviz in debug mode if explicitly enabled via system property
        if (BuildConfig.DEBUG && System.getProperty("org.oxycblt.musikr.render_graph") == "true") {
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

        val factoryStart = System.currentTimeMillis()
        val library = libraryFactory.create(graph, storedPlaylists, playlistInterpreter)
        Log.d("EvaluateStep", "Library factory took ${System.currentTimeMillis() - factoryStart}ms")
        Log.d("EvaluateStep", "Total evaluation took ${System.currentTimeMillis() - start}ms")
        return library
    }
}
