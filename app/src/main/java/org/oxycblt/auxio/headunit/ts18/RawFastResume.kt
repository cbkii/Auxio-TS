/*
 * Copyright (c) 2026 Auxio Project
 * RawFastResume.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.ts18

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import java.io.File
import org.oxycblt.auxio.playback.persist.FastResumeSnapshot
import org.oxycblt.auxio.playback.state.RawPlaybackMetadata

/** Normal-app-safe TS18 raw fast-resume validation and MediaItem construction. */
@SuppressLint("SdCardPath")
object RawFastResumeValidator {
    private val allowedDirectRoots =
        listOf("/storage/usbdisk0/", "/storage/usbdisk1/", "/storage/emulated/0/", "/sdcard/")

    private val audioExtensions =
        setOf("mp3", "flac", "m4a", "aac", "ogg", "opus", "wav", "ape", "wma")

    sealed interface Result {
        data class Valid(val item: RawFastResumeItem) : Result

        data class Invalid(val reason: Reason, val detail: String) : Result
    }

    enum class Reason {
        BLANK_SOURCE,
        MALFORMED_URI,
        UNSUPPORTED_SCHEME,
        UNSAFE_PATH,
        MISSING_FILE,
        UNREADABLE_FILE,
        NON_AUDIO_LIKE,
        SECURITY_FAILURE,
        PROVIDER_FAILURE,
    }

    fun validate(context: Context, snapshot: FastResumeSnapshot): Result {
        val uriText = snapshot.uri.trim()
        val pathText = snapshot.path?.trim()?.takeIf { it.isNotEmpty() }
        if (uriText.isEmpty() && pathText == null) {
            return invalid(Reason.BLANK_SOURCE, "snapshot has no uri or direct path")
        }

        val parsedUri =
            try {
                if (uriText.isNotEmpty()) Uri.parse(uriText) else Uri.fromFile(File(pathText!!))
            } catch (e: RuntimeException) {
                return invalid(Reason.MALFORMED_URI, e.message.orEmpty())
            }

        val scheme = parsedUri.scheme?.lowercase()
        var resolvedPath: String? = pathText
        val usableUri =
            when (scheme) {
                "content" -> {
                    val contentCheck = validateContentUri(context, parsedUri)
                    if (contentCheck != null) {
                        val fallbackCheck = validateDirectPath(pathText)
                        if (pathText != null && fallbackCheck == null) {
                            resolvedPath = pathText
                            Uri.fromFile(File(pathText))
                        } else {
                            return contentCheck
                        }
                    } else {
                        parsedUri
                    }
                }
                "file" -> {
                    val path = parsedUri.path ?: pathText
                    val fileCheck = validateDirectPath(path)
                    if (fileCheck != null) return fileCheck
                    resolvedPath = path
                    Uri.fromFile(File(path!!))
                }
                null,
                "" -> {
                    val path = pathText ?: uriText
                    val fileCheck = validateDirectPath(path)
                    if (fileCheck != null) return fileCheck
                    resolvedPath = path
                    Uri.fromFile(File(path))
                }
                else -> return invalid(Reason.UNSUPPORTED_SCHEME, scheme)
            }

        val title =
            snapshot.title?.takeIf { it.isNotBlank() } ?: resolvedPath?.substringAfterLast('/')
        val durationMs = snapshot.durationMs.coerceAtLeast(0L)
        val positionMs =
            snapshot.positionMs.coerceAtLeast(0L).let { position ->
                if (durationMs > 0L) position.coerceAtMost(durationMs) else position
            }

        return Result.Valid(
            RawFastResumeItem(
                uri = usableUri,
                uriString = usableUri.toString(),
                path = resolvedPath,
                title = title,
                artist = snapshot.artist?.takeIf { it.isNotBlank() },
                album = snapshot.album?.takeIf { it.isNotBlank() },
                durationMs = durationMs,
                positionMs = positionMs,
                savedAtMs = snapshot.savedAtMs,
            )
        )
    }

    private enum class Likeness {
        AUDIO_LIKE,
        NOT_AUDIO,
        UNKNOWN,
    }

    private fun checkAudioLikeness(context: Context, uri: Uri): Likeness {
        val contentResolver = context.applicationContext.contentResolver
        val mimeType = contentResolver.getType(uri)?.lowercase()

        if (mimeType != null) {
            if (
                mimeType.startsWith("audio/") ||
                    mimeType == "application/ogg" ||
                    mimeType == "application/x-ogg"
            ) {
                return Likeness.AUDIO_LIKE
            }
            if (
                mimeType.startsWith("image/") ||
                    mimeType.startsWith("video/") ||
                    mimeType.startsWith("text/") ||
                    mimeType == "application/pdf" ||
                    mimeType == "application/zip" ||
                    mimeType == "application/vnd.android.package-archive" ||
                    mimeType == "application/json" ||
                    mimeType == "application/xml" ||
                    mimeType == "application/x-tar"
            ) {
                return Likeness.NOT_AUDIO
            }
        }

        var nameLikeness = Likeness.UNKNOWN
        try {
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex =
                        cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        val displayName = cursor.getString(nameIndex)
                        if (displayName != null) {
                            if (hasAudioExtension(displayName)) {
                                nameLikeness = Likeness.AUDIO_LIKE
                            } else if (
                                displayName.contains(".") && !hasAudioExtension(displayName)
                            ) {
                                // If it has an extension but not an audio one, we can reject it
                                nameLikeness = Likeness.NOT_AUDIO
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Ignore query failures, fallback to unknown
        }

        return nameLikeness
    }

    private fun validateContentUri(context: Context, uri: Uri): Result.Invalid? {
        return try {
            val likeness = checkAudioLikeness(context, uri)
            if (likeness == Likeness.NOT_AUDIO) {
                return invalid(
                    Reason.NON_AUDIO_LIKE,
                    "content uri failed cheap audio-likeness check",
                )
            }

            context.applicationContext.contentResolver.openFileDescriptor(uri, "r")?.use {
                descriptor ->
                if (!descriptor.fileDescriptor.valid()) {
                    return invalid(Reason.PROVIDER_FAILURE, "provider returned invalid descriptor")
                }
            } ?: return invalid(Reason.PROVIDER_FAILURE, "provider returned null descriptor")
            null
        } catch (e: SecurityException) {
            invalid(Reason.SECURITY_FAILURE, e.message.orEmpty())
        } catch (e: Exception) {
            invalid(Reason.PROVIDER_FAILURE, e.message.orEmpty())
        }
    }

    private fun validateDirectPath(path: String?): Result.Invalid? {
        if (path.isNullOrBlank()) return invalid(Reason.BLANK_SOURCE, "direct path is blank")
        val normalized = path.trim()
        if (!isAllowedDirectPath(normalized)) {
            return invalid(Reason.UNSAFE_PATH, normalized)
        }
        if (!hasAudioExtension(normalized)) {
            return invalid(Reason.NON_AUDIO_LIKE, normalized)
        }
        return try {
            val file = File(normalized)
            when {
                !file.exists() -> invalid(Reason.MISSING_FILE, normalized)
                !file.isFile || !file.canRead() -> invalid(Reason.UNREADABLE_FILE, normalized)
                else -> null
            }
        } catch (e: SecurityException) {
            invalid(Reason.SECURITY_FAILURE, e.message.orEmpty())
        } catch (e: Exception) {
            invalid(Reason.PROVIDER_FAILURE, e.message.orEmpty())
        }
    }

    fun isAllowedDirectPath(path: String): Boolean {
        if (path.contains("/../") || path.endsWith("/..") || path == "..") return false
        return allowedDirectRoots.any { path == it.removeSuffix("/") || path.startsWith(it) }
    }

    fun hasAudioExtension(path: String): Boolean {
        val ext = path.substringAfterLast('.', missingDelimiterValue = "").lowercase()
        return ext in audioExtensions
    }

    private fun invalid(reason: Reason, detail: String) = Result.Invalid(reason, detail)
}

data class RawFastResumeItem(
    val uri: Uri,
    val uriString: String,
    val path: String?,
    val title: String?,
    val artist: String?,
    val album: String?,
    val durationMs: Long,
    val positionMs: Long,
    val savedAtMs: Long,
) {
    @OptIn(UnstableApi::class)
    fun buildMediaItem(): MediaItem {
        val metadata =
            MediaMetadata.Builder()
                .setTitle(
                    title ?: path?.substringAfterLast('/') ?: uri.lastPathSegment ?: "USB audio"
                )
                .setArtist(artist)
                .setAlbumTitle(album)
                .build()
        return MediaItem.Builder().setUri(uri).setMediaMetadata(metadata).setTag(this).build()
    }

    fun toSnapshot(positionMs: Long, playing: Boolean): FastResumeSnapshot {
        return FastResumeSnapshot(
            uri = uriString,
            path = path,
            title = title,
            artist = artist,
            album = album,
            durationMs = durationMs,
            positionMs =
                positionMs.coerceAtLeast(0L).let { p ->
                    if (durationMs > 0L) p.coerceAtMost(durationMs) else p
                },
            playing = playing,
            savedAtMs = System.currentTimeMillis(),
        )
    }

    fun toRawPlaybackMetadata(positionMs: Long, playing: Boolean): RawPlaybackMetadata {
        val clampedPosition =
            positionMs.coerceAtLeast(0L).let { p ->
                if (durationMs > 0L) p.coerceAtMost(durationMs) else p
            }
        return RawPlaybackMetadata(
            title = title,
            artist = artist,
            album = album,
            uriString = uriString,
            path = path,
            durationMs = durationMs.coerceAtLeast(0L),
            positionMs = clampedPosition,
            isPlaying = playing,
            savedAtMs = savedAtMs,
        )
    }
}

@OptIn(UnstableApi::class)
fun MediaItem.rawFastResumeItemOrNull(): RawFastResumeItem? =
    this.localConfiguration?.tag as? RawFastResumeItem

fun MediaItem.isRawFastResumeMediaItem(): Boolean = rawFastResumeItemOrNull() != null
