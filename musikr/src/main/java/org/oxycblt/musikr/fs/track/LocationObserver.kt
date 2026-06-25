/*
 * Copyright (c) 2025 Auxio Project
 * LocationObserver.kt is part of Auxio.
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

package org.oxycblt.musikr.fs.track

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log

internal class LocationObserver(
    private val context: Context,
    private val uri: Uri,
    private val onUpdate: () -> Unit,
) : ContentObserver(Handler(Looper.getMainLooper())), Runnable {
    private val handler = Handler(Looper.getMainLooper())
    private var registered = false

    init {
        registered = tryRegisterContentObserver(context, uri, this)
    }

    fun release() {
        handler.removeCallbacks(this)
        if (registered) {
            try {
                context.applicationContext.contentResolver.unregisterContentObserver(this)
            } catch (e: RuntimeException) {
                Log.w(
                    TAG,
                    "Ignoring content observer unregister failure for ${uri.redactedForLog()}",
                    e,
                )
            }
            registered = false
        }
    }

    override fun onChange(selfChange: Boolean) {
        // Batch rapid-fire updates into a single callback after delay
        handler.removeCallbacks(this)
        handler.postDelayed(this, REINDEX_DELAY_MS)
    }

    override fun run() {
        onUpdate()
    }

    internal companion object {
        private const val TAG = "LocationObserver"
        const val REINDEX_DELAY_MS = 500L

        fun isObservableContentUri(context: Context, uri: Uri): Boolean =
            isObservableContentUri(uri) { authority ->
                context.applicationContext.packageManager.resolveContentProvider(authority, 0) !=
                    null
            }

        internal fun isObservableContentUri(
            uri: Uri,
            authorityResolver: (String) -> Boolean,
        ): Boolean {
            if (uri.scheme != ContentResolver.SCHEME_CONTENT) return false
            val authority = uri.authority
            if (authority.isNullOrBlank()) return false
            return authorityResolver(authority)
        }

        private fun tryRegisterContentObserver(
            context: Context,
            uri: Uri,
            observer: ContentObserver,
        ): Boolean {
            if (!isObservableContentUri(context, uri)) {
                Log.w(
                    TAG,
                    "Skipping content observer for unsupported or unresolved location: ${uri.redactedForLog()}",
                )
                return false
            }
            return try {
                context.applicationContext.contentResolver.registerContentObserver(
                    uri,
                    true,
                    observer,
                )
                true
            } catch (e: SecurityException) {
                Log.w(
                    TAG,
                    "Skipping content observer after provider security failure: ${uri.redactedForLog()}",
                    e,
                )
                false
            } catch (e: IllegalArgumentException) {
                Log.w(
                    TAG,
                    "Skipping content observer after provider argument failure: ${uri.redactedForLog()}",
                    e,
                )
                false
            } catch (e: RuntimeException) {
                Log.w(
                    TAG,
                    "Skipping content observer after provider runtime failure: ${uri.redactedForLog()}",
                    e,
                )
                false
            }
        }

        private fun Uri.redactedForLog(): String {
            val authorityState = if (authority.isNullOrBlank()) "blank" else "present"
            return "scheme=${scheme ?: "none"}, authority=$authorityState"
        }
    }
}
