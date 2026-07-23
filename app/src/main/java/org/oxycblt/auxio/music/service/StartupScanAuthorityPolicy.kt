/*
 * Copyright (c) 2026 Auxio Project
 * StartupScanAuthorityPolicy.kt is part of Auxio.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

package org.oxycblt.auxio.music.service

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.os.SystemClock
import androidx.core.content.ContextCompat
import java.io.File
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.StartupLibraryPolicy
import org.oxycblt.auxio.music.locations.LocationMode

/** Origin of a service startup request. */
enum class StartupScanOrigin {
    USER_VISIBLE,
    BACKGROUND,
    EARLY_PRESTART;
}

/**
 * Compatibility-boundary policy that maps trusted lifecycle origin and current source authority to
 * automatic scan authority.
 *
 * The shared music startup core receives only the resulting boolean. Topway boot/ACC restrictions,
 * Android permissions and path readability stay outside generic source/cache policy.
 */
object StartupScanAuthorityPolicy {
    private var trustedUserVisibleUntilElapsedMs = 0L

    /** Issue a short-lived, process-local token immediately before MainActivity starts the service. */
    @Synchronized
    fun issueTrustedUserVisibleStart() {
        trustedUserVisibleUntilElapsedMs =
            SystemClock.elapsedRealtime() + TRUSTED_USER_VISIBLE_WINDOW_MS
    }

    /** Consume the one-shot token; exported service callers cannot mint it through Intent extras. */
    @Synchronized
    fun consumeTrustedUserVisibleStart(): Boolean {
        val trusted =
            trustedUserVisibleUntilElapsedMs > 0L &&
                SystemClock.elapsedRealtime() <= trustedUserVisibleUntilElapsedMs
        trustedUserVisibleUntilElapsedMs = 0L
        return trusted
    }

    fun allowAutomaticScan(
        topwayCompatFlavor: Boolean,
        origin: StartupScanOrigin,
        sourceAuthority: Boolean,
    ): Boolean = sourceAuthority && (!topwayCompatFlavor || origin == StartupScanOrigin.USER_VISIBLE)

    /** Lightweight current authority check; never invokes root and never enumerates a source tree. */
    fun hasCurrentSourceAuthority(context: Context, settings: MusicSettings): Boolean {
        if (
            !StartupLibraryPolicy.isMusicSourceConfigured(
                settings.locationMode,
                settings.configuredSourceCount,
            )
        ) {
            return false
        }
        return when (settings.locationMode) {
            LocationMode.MEDIA_STORE -> hasStoragePermission(context)
            LocationMode.SAF -> settings.safQuery.source.all { hasUriReadAuthority(context, it.uri) }
            LocationMode.DIRECT_FS ->
                settings.safQuery.source.all { location ->
                    val path = location.uri.path ?: return@all false
                    val file = File(path)
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
            return file.exists() && file.canRead()
        }
        val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (
            context.checkUriPermission(uri, Process.myPid(), Process.myUid(), flags) ==
                PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return context.contentResolver.persistedUriPermissions.any { permission ->
            permission.isReadPermission && uri.toString().startsWith(permission.uri.toString())
        }
    }

    private const val TRUSTED_USER_VISIBLE_WINDOW_MS = 5_000L
}
