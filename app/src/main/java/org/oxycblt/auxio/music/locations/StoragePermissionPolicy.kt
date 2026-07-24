/*
 * Copyright (c) 2026 Auxio Project
 * StoragePermissionPolicy.kt is part of Auxio.
 */

package org.oxycblt.auxio.music.locations

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object StoragePermissionPolicy {
    private const val PREFS_NAME = "source_recovery"
    private const val KEY_REQUESTED = "storage_permission_requested"

    fun requiredPermission(): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
  Manifest.permission.READ_MEDIA_AUDIO
        } else {
  Manifest.permission.READ_EXTERNAL_STORAGE
        }

    fun isGranted(context: Context): Boolean =
        ContextCompat.checkSelfPermission(context, requiredPermission()) ==
  PackageManager.PERMISSION_GRANTED

    fun directPathRequiresPermission(path: String?): Boolean =
        path == "/sdcard" ||
  path?.startsWith("/sdcard/") == true ||
  path == "/storage" ||
  path?.startsWith("/storage/") == true

    fun wasRequested(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  .getBoolean(KEY_REQUESTED, false)

    fun markRequested(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  .edit()
  .putBoolean(KEY_REQUESTED, true)
  .apply()
    }
}
