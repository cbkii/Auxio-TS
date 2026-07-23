/*
 * Copyright (c) 2025 Auxio Project
 * CoverProvider.kt is part of Auxio.
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

package org.oxycblt.auxio.image

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.image.covers.SettingCovers
import org.oxycblt.musikr.covers.CoverResult
import timber.log.Timber as L

class CoverProvider : ContentProvider() {
    private lateinit var writerExecutor: ThreadPoolExecutor
    private lateinit var transferTimeoutExecutor: ScheduledThreadPoolExecutor

    override fun onCreate(): Boolean {
        writerExecutor =
            ThreadPoolExecutor(
                    COVER_WRITER_THREADS,
                    COVER_WRITER_THREADS,
                    COVER_WRITER_KEEP_ALIVE_SECONDS,
                    TimeUnit.SECONDS,
                    ArrayBlockingQueue(COVER_WRITER_QUEUE_SIZE),
                    NamedThreadFactory("AuxioCoverProvider"),
                    ThreadPoolExecutor.AbortPolicy(),
                )
                .apply { allowCoreThreadTimeOut(true) }
        transferTimeoutExecutor =
            ScheduledThreadPoolExecutor(1, NamedThreadFactory("AuxioCoverTimeout")).apply {
                removeOnCancelPolicy = true
                executeExistingDelayedTasksAfterShutdownPolicy = false
                continueExistingPeriodicTasksAfterShutdownPolicy = false
            }
        return true
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        if (mode != "r" || uriMatcher.match(uri) != MATCH_COVER) return null
        val id = uri.lastPathSegment?.takeIf { it.length <= MAX_COVER_ID_LENGTH } ?: return null
        val pipe =
            try {
                ParcelFileDescriptor.createPipe()
            } catch (e: Exception) {
                L.w(e, "Unable to create cover-provider pipe")
                return null
            }

            val timedOut = AtomicBoolean(false)
            val timeoutFuture =
                try {
                    transferTimeoutExecutor.schedule(
                        {
                            timedOut.set(true)
                            pipe[1].closeQuietly()
                        },
                        COVER_TRANSFER_TIMEOUT_MS,
                        TimeUnit.MILLISECONDS,
                    )
                } catch (e: RejectedExecutionException) {
                    pipe[0].closeQuietly()
                    pipe[1].closeQuietly()
                    L.w(e, "Cover-provider timeout executor rejected request")
                    return null
                }

            return try {
                writerExecutor.execute { writeCoverToPipe(id, pipe[1], timedOut, timeoutFuture) }
                pipe[0]
            } catch (e: RejectedExecutionException) {
                timeoutFuture.cancel(false)
                pipe[0].closeQuietly()
                pipe[1].closeQuietly()
                L.w("Cover-provider writer queue is full; rejecting request")
                null
            }
        }

        private fun writeCoverToPipe(
            id: String,
            writeSide: ParcelFileDescriptor,
            timedOut: AtomicBoolean,
            timeoutFuture: ScheduledFuture<*>,
        ) {
            try {
                ParcelFileDescriptor.AutoCloseOutputStream(writeSide).use { output ->
                    val coverDescriptor = runBlocking {
                        withTimeoutOrNull(COVER_LOAD_TIMEOUT_MS) {
                            withContext(Dispatchers.IO) {
                                when (
                                    val result =
                                        SettingCovers.immutable(requireNotNull(context)).obtain(id)
                                ) {
                                    is CoverResult.Hit -> result.cover.fd()
                                    else -> null
                                }
                            }
                        }
                    }
                    if (coverDescriptor == null) {
                        L.d("Cover-provider request missed or timed out: $id")
                        return
                    }
                    val declaredSize = coverDescriptor.statSize
                    if (declaredSize > MAX_COVER_BYTES) {
                        coverDescriptor.closeQuietly()
                        L.w("Cover-provider payload exceeded $MAX_COVER_BYTES bytes: $id")
                        return
                    }
                    ParcelFileDescriptor.AutoCloseInputStream(coverDescriptor).use { input ->
                        if (!copyBounded(input, output, MAX_COVER_BYTES)) {
                            L.w("Cover-provider payload exceeded $MAX_COVER_BYTES bytes: $id")
                        }
                    }
                }
            } catch (e: IOException) {
                if (timedOut.get()) {
                    L.w("Cover-provider transfer timed out: $id")
                } else {
                    L.w(e, "Cover-provider transfer failed: $id")
                }
            } catch (e: RuntimeException) {
                if (timedOut.get()) {
                    L.w("Cover-provider transfer timed out: $id")
                } else {
                    L.w(e, "Cover-provider request failed: $id")
                }
            } finally {
                timeoutFuture.cancel(false)
            }
        }

    override fun shutdown() {
        if (::writerExecutor.isInitialized) writerExecutor.shutdownNow()
        if (::transferTimeoutExecutor.isInitialized) transferTimeoutExecutor.shutdownNow()
        super.shutdown()
    }

    override fun getType(uri: Uri): String {
        check(uriMatcher.match(uri) == MATCH_COVER) { "Unknown URI: $uri" }
        return "image/*"
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor = throw UnsupportedOperationException()

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int = 0

    private class NamedThreadFactory(private val prefix: String) : ThreadFactory {
        private val nextId = AtomicInteger(1)

        override fun newThread(runnable: Runnable): Thread =
            Thread(runnable, "$prefix-${nextId.getAndIncrement()}").apply { isDaemon = true }
    }

    companion object {
        private const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.image.CoverProvider"
        private const val IMAGES_PATH = "covers"
        private const val MATCH_COVER = 1
        private const val MAX_COVER_ID_LENGTH = 512
        private const val COVER_WRITER_THREADS = 2
        private const val COVER_WRITER_QUEUE_SIZE = 32
        private const val COVER_WRITER_KEEP_ALIVE_SECONDS = 30L
        private const val COVER_LOAD_TIMEOUT_MS = 5_000L
        private const val COVER_TRANSFER_TIMEOUT_MS = 15_000L
        internal const val MAX_COVER_BYTES = 32L * 1024L * 1024L
        private const val COPY_BUFFER_BYTES = 16 * 1024

        private val uriMatcher: UriMatcher by
            lazy(LazyThreadSafetyMode.PUBLICATION) {
                UriMatcher(UriMatcher.NO_MATCH).apply {
                    addURI(AUTHORITY, "$IMAGES_PATH/*", MATCH_COVER)
                }
            }

        val CONTENT_URI: Uri by
            lazy(LazyThreadSafetyMode.PUBLICATION) {
                Uri.Builder()
                    .scheme(ContentResolver.SCHEME_CONTENT)
                    .authority(AUTHORITY)
                    .appendPath(IMAGES_PATH)
                    .build()
            }

        internal fun copyBounded(
            input: InputStream,
            output: OutputStream,
            maxBytes: Long,
        ): Boolean {
            require(maxBytes > 0)
            val buffer = ByteArray(COPY_BUFFER_BYTES)
            var copied = 0L
            while (true) {
                val read = input.read(buffer)
                if (read < 0) return true
                if (copied + read > maxBytes) return false
                output.write(buffer, 0, read)
                copied += read
            }
        }

        private fun ParcelFileDescriptor.closeQuietly() {
            try {
                close()
            } catch (_: Exception) {}
        }
    }
}
