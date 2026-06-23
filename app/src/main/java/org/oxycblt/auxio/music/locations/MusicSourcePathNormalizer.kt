/*
 * Copyright (c) 2026 Auxio Project
 * MusicSourcePathNormalizer.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music.locations

import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import timber.log.Timber as L

/** Mode-aware persisted Music Source normalisation used before runtime backend creation. */
internal object MusicSourcePathNormalizer {
    fun normalizePersistedLocation(value: String, fileOnly: Boolean): String? {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return null
        val uri = trimmed.toUri()
        val repaired = repairDuplicatedStoragePath(uri)
        val candidate = repaired ?: uri
        if (!fileOnly) {
            val pathUri =
                if (candidate.scheme.isNullOrEmpty() && candidate.path?.startsWith("/") == true) {
                    Uri.fromFile(File(requireNotNull(candidate.path)))
                } else {
                    candidate
                }
            return pathUri.toString()
        }

        val fileUri =
            when (candidate.scheme) {
                "file" -> candidate
                null, "" ->
                    candidate.path?.takeIf { it.startsWith("/") }?.let { Uri.fromFile(File(it)) }
                "content" -> externalStorageTreeToFileUri(candidate)
                else -> null
            }
        val path = fileUri?.path ?: return null
        if (!isSafeDirectPath(path)) {
            L.w("Skipping unsafe DirectFS source: $candidate")
            return null
        }
        if (fileUri != candidate) {
            L.i("Normalised DirectFS source $candidate -> $fileUri")
        }
        return fileUri.toString()
    }

    fun repairDuplicatedStoragePath(uri: Uri): Uri? {
        val path = uri.path ?: return null
        val repaired = repairDuplicatedStoragePath(path) ?: return null
        val repairedUri =
            if (uri.scheme == "file") Uri.fromFile(File(repaired)) else repaired.toUri()
        L.i("Repaired duplicated music source path $uri -> $repairedUri")
        return repairedUri
    }

    private fun repairDuplicatedStoragePath(path: String): String? {
        val prefixes =
            listOf(
                "/storage/emulated/0",
                "/storage/usbdisk0",
                "/storage/usbdisk1",
                "/mnt/media_rw/usbdisk0",
                "/mnt/media_rw/usbdisk1",
                "/sdcard",
            )
        for (prefix in prefixes) {
            val duplicated = prefix + prefix
            if (path == duplicated || path.startsWith(duplicated + "/")) {
                return prefix + path.removePrefix(duplicated)
            }
        }
        return null
    }

    private fun externalStorageTreeToFileUri(uri: Uri): Uri? {
        if (uri.authority != "com.android.externalstorage.documents") return null
        val encodedTree =
            uri.pathSegments.zipWithNext().firstOrNull { it.first == "tree" }?.second
                ?: return null
        val treeId = Uri.decode(encodedTree)
        val parts = treeId.split(':', limit = 2)
        val volume = parts.firstOrNull() ?: return null
        val relative = parts.getOrNull(1).orEmpty().trim('/')
        val root = if (volume == "primary") "/storage/emulated/0" else "/storage/$volume"
        return Uri.fromFile(File(if (relative.isEmpty()) root else "$root/$relative"))
    }

    private fun isSafeDirectPath(path: String): Boolean {
        val clean = path.trimEnd('/')
        if (clean.isBlank()) return false
        val protected =
            listOf(
                "/",
                "/system",
                "/vendor",
                "/data",
                "/proc",
                "/sys",
                "/dev",
                "/acct",
                "/config",
            )
        if (protected.any { clean == it }) return false
        if (
            clean.startsWith("/data/") ||
                clean.startsWith("/system/") ||
                clean.startsWith("/vendor/")
        ) {
            return false
        }
        return clean.startsWith("/storage/") ||
            clean.startsWith("/mnt/media_rw/") ||
            clean.startsWith("/sdcard/") ||
            clean == "/sdcard"
    }
}
