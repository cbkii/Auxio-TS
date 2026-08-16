/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanAuthorityPolicy.kt is part of Auxio.
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

package org.oxycblt.auxio.music.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import androidx.core.content.ContextCompat
import java.io.File
import java.util.UUID
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.StartupLibraryPolicy
import org.oxycblt.auxio.music.locations.LocationMode

/** Origin of a playback-service startup request. */
enum class StartupScanOrigin {
    USER_VISIBLE,
    BACKGROUND,
}

/** Maps trusted lifecycle origin and current source authority to automatic scan authority. */
object StartupScanAuthorityPolicy {
    private var trustedUserVisibleNonce: String? = null

    @Synchronized
    fun issueTrustedUserVisibleStart(): String =
        UUID.randomUUID().toString().also { trustedUserVisibleNonce = it }

    @Synchronized
    fun consumeTrustedUserVisibleStart(candidate: String?): Boolean {
        val trusted = candidate != null && candidate == trustedUserVisibleNonce
        if (trusted) trustedUserVisibleNonce = null
        return trusted
    }

    /**
     * The maintained TS18/Topway product never turns an Activity/service lifecycle event into
     * source-enumeration authority. Initial source configuration is handled independently by the
     * durable configuration checkpoint, while later refresh/rescan/retry actions must remain
     * explicit. This prevents a slow cached-library hydration from silently becoming a new scan.
     *
     * The generic Android fallback retains the historical recovery posture for policy tests and
     * non-product reuse of this component.
     */
    fun originAllowsAutomaticScan(topwayProduct: Boolean, origin: StartupScanOrigin): Boolean =
        when {
            !topwayProduct -> true
            origin == StartupScanOrigin.USER_VISIBLE -> false
            else -> false
        }

    fun allowAutomaticScan(
        topwayProduct: Boolean,
        origin: StartupScanOrigin,
        sourceAuthority: Boolean,
    ): Boolean = sourceAuthority && originAllowsAutomaticScan(topwayProduct, origin)

    /** Current source authority check. Never invokes root or recursively enumerates contents. */
    fun hasCurrentSourceAuthority(context: Context, settings: MusicSettings): Boolean {
        if (
            !StartupLibraryPolicy.isMusicSourceConfigured(
                settings.locationMode,
                settings.configuredSourceCount,
            )
        ) {
            return false
        }

        if (settings.locationMode == LocationMode.MEDIA_STORE) return hasStoragePermission(context)

        // Persisted entries that can no longer be opened are ignored here. One surviving authorised
        // source is enough to recover the library; the real scan will retain prior generations for
        // any unavailable siblings and report their individual failure.
        val sources = settings.safQuery.source
        if (sources.isEmpty()) return false

        return when (settings.locationMode) {
            LocationMode.MEDIA_STORE -> hasStoragePermission(context)
            LocationMode.SAF -> sources.any { hasUriReadAuthority(context, it.uri) }
            LocationMode.DIRECT_FS ->
                // File-process readability is the authority. Internal shared storage naturally
                // remains unreadable without Android storage permission, while a validated
                // app-readable USB or prepared alias must not be blocked by an unrelated blanket
                // permission gate.
                sources.any { location ->
                    val file = location.uri.path?.let(::File) ?: return@any false
                    file.exists() && file.isDirectory && file.canRead()
                }
        }
    }

    private fun hasStoragePermission(context: Context): Boolean {
        val permission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_AUDIO
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        return ContextCompat.checkSelfPermission(context, permission) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun hasUriReadAuthority(context: Context, uri: android.net.Uri): Boolean {
        if (uri.scheme == "file") {
            val file = uri.path?.let(::File) ?: return false
            return file.exists() && file.isDirectory && file.canRead()
        }
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (
            context.checkUriPermission(uri, Process.myPid(), Process.myUid(), flags) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return context.contentResolver.persistedUriPermissions.any { permission ->
            permission.isReadPermission && permission.uri == uri
        }
    }
}
