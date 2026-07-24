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
import androidx.core.content.ContextCompat
import java.io.File
import java.util.UUID
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.StartupLibraryPolicy
import org.oxycblt.auxio.music.locations.LocationMode

/** Origin of a service startup request. */
enum class StartupScanOrigin {
    USER_VISIBLE,
    BACKGROUND;
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

    fun originAllowsAutomaticScan(
        topwayCompatFlavor: Boolean,
        origin: StartupScanOrigin,
    ): Boolean = !topwayCompatFlavor || origin == StartupScanOrigin.USER_VISIBLE

    fun allowAutomaticScan(
        topwayCompatFlavor: Boolean,
        origin: StartupScanOrigin,
        sourceAuthority: Boolean,
    ): Boolean = sourceAuthority && originAllowsAutomaticScan(topwayCompatFlavor, origin)

    /** Current source authority check. Never invokes root or enumerates source contents. */
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

        val sources = settings.safQuery.source
        if (sources.isEmpty() || sources.size != settings.configuredSourceCount) return false

        return when (settings.locationMode) {
  LocationMode.MEDIA_STORE -> hasStoragePermission(context)
  LocationMode.SAF -> sources.all { hasUriReadAuthority(context, it.uri) }
  LocationMode.DIRECT_FS ->
      sources.all { location ->
          val file = location.uri.path?.let(::File) ?: return@all false
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
