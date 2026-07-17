/*
 * Copyright (c) 2026 Auxio Project
 * FastStartFolderDialogFragment.kt is part of Auxio.
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

package org.oxycblt.auxio.home

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.ts18.FastStartDirectFolderBrowser
import org.oxycblt.auxio.playback.PlaybackViewModel
import org.oxycblt.auxio.playback.state.DeferredPlayback
import timber.log.Timber as L

/** Bounded TS18 USB folder navigation available before full library hydration. */
@AndroidEntryPoint
class FastStartFolderDialogFragment : DialogFragment() {
    @Inject lateinit var browser: FastStartDirectFolderBrowser
    private val playbackModel: PlaybackViewModel by activityViewModels()
    private val entries = mutableListOf<FastStartDirectFolderBrowser.Entry>()
    private lateinit var adapter: ArrayAdapter<String>
    private var loaded = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireArguments().getString(ARG_PATH)?.substringAfterLast('/'))
            .setAdapter(adapter) { _, which -> handleEntry(which) }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    override fun onStart() {
        super.onStart()
        if (loaded) return
        loaded = true
        val path = requireArguments().getString(ARG_PATH) ?: return
        lifecycleScope.launch {
            try {
                val page = browser.browse(path, limit = PAGE_LIMIT)
                entries.clear()
                entries.addAll(page.entries.filter { it.directory || it.playable })
                adapter.clear()
                adapter.addAll(
                    if (entries.isEmpty()) {
                        listOf(getString(R.string.def_song_count))
                    } else {
                        entries.map { entry ->
                            if (entry.directory) "📁 ${entry.name}" else "▶ ${entry.name}"
                        }
                    }
                )
                adapter.notifyDataSetChanged()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                L.w(e, "Unable to browse Fast Start USB path $path")
                adapter.clear()
                adapter.add(getString(R.string.def_song_count))
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun handleEntry(index: Int) {
        val entry = entries.getOrNull(index) ?: return
        if (entry.directory) {
            dismissAllowingStateLoss()
            FastStartFolderDialogFragment.show(parentFragmentManager, entry.path)
            return
        }
        if (!entry.playable) return
        playbackModel.playDeferred(DeferredPlayback.Open(Uri.fromFile(File(entry.path))))
        playbackModel.openPlayback()
        dismissAllowingStateLoss()
    }

    companion object {
        private const val ARG_PATH = "path"
        private const val TAG = "fast_start_folder"
        private const val PAGE_LIMIT = 60

        fun show(manager: androidx.fragment.app.FragmentManager, path: String) {
            if (manager.isStateSaved) return
            FastStartFolderDialogFragment()
                .apply { arguments = Bundle().apply { putString(ARG_PATH, path) } }
                .show(manager, TAG)
        }
    }
}
