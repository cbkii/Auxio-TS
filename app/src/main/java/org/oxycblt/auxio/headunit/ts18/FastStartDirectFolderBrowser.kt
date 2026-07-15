/*
 * Copyright (c) 2026 Auxio Project
 * FastStartDirectFolderBrowser.kt is part of Auxio.
 */
package org.oxycblt.auxio.headunit.ts18

import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Bounded one-level DirectFS browser for the startup/Fast Start surface. */
class FastStartDirectFolderBrowser @Inject constructor() {
    data class Entry(
        val path: String,
        val name: String,
        val directory: Boolean,
        val playable: Boolean,
    )

    suspend fun usbRoots(limit: Int = DEFAULT_LIMIT): List<Entry> =
        browse(ROOT, limit).filter { it.path == USB0 || it.path == USB1 }

    suspend fun browse(path: String, limit: Int = DEFAULT_LIMIT): List<Entry> =
        withContext(Dispatchers.IO) {
            val clean = normalize(path) ?: return@withContext emptyList()
            val dir = File(clean)
            if (!dir.isDirectory) return@withContext emptyList()
            dir.listFiles()
                ?.asSequence()
                ?.filter { !it.name.startsWith('.') }
                ?.map { file -> file.toEntry() }
                ?.sortedWith(
                    compareBy<Entry>({ !it.directory }, { it.name.lowercase() }, { it.path })
                )
                ?.take(limit.coerceIn(1, MAX_LIMIT))
                ?.toList() ?: emptyList()
        }

    private fun File.toEntry(): Entry {
        val appPath = absolutePath.replace(MEDIA_RW_USB_PREFIX, STORAGE_USB_PREFIX)
        return Entry(appPath, name, isDirectory, isFile && isAudioLike(name))
    }

    private fun normalize(path: String): String? {
        val clean = path.trim().replace('\\', '/').replace(Regex("/+$"), "").ifEmpty { ROOT }
        return when {
            clean == ROOT -> ROOT
            clean == USB0 || clean.startsWith("$USB0/") -> clean
            clean == USB1 || clean.startsWith("$USB1/") -> clean
            else -> null
        }
    }

    private fun isAudioLike(name: String): Boolean =
        AUDIO_EXTENSIONS.any { name.endsWith(it, ignoreCase = true) }

    companion object {
        const val ROOT = "/storage"
        const val USB0 = "/storage/usbdisk0"
        const val USB1 = "/storage/usbdisk1"
        private const val STORAGE_USB_PREFIX = "/storage/usbdisk"
        private const val MEDIA_RW_USB_PREFIX = "/mnt/media_rw/usbdisk"
        private const val DEFAULT_LIMIT = 30
        private const val MAX_LIMIT = 100
        private val AUDIO_EXTENSIONS =
            listOf(".mp3", ".flac", ".m4a", ".ogg", ".opus", ".wav", ".aac")
    }
}
