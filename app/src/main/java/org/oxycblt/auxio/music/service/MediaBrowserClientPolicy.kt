/*
 * Copyright (c) 2026 Auxio Project
 * MediaBrowserClientPolicy.kt is part of Auxio.
 */

package org.oxycblt.auxio.music.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import timber.log.Timber as L

/** Trust policy for the exported stock-compatible MediaBrowser wrapper. */
class MediaBrowserClientPolicy(private val context: Context) {
    fun isTrusted(clientPackageName: String, clientUid: Int): Boolean {
        if (
            clientUid < 0 ||
                clientPackageName.isBlank() ||
                clientPackageName.length > MAX_PACKAGE_NAME_LENGTH ||
                clientPackageName.any(Char::isISOControl)
        ) {
            return false
        }

        val packagesForUid =
            try {
                context.packageManager.getPackagesForUid(clientUid)?.toSet().orEmpty()
            } catch (e: RuntimeException) {
                L.w(e, "Unable to resolve MediaBrowser caller packages")
                return false
            }
        val packageMatchesUid = clientPackageName in packagesForUid
        val hasMediaControlPermission =
            try {
                context.packageManager.checkPermission(
                    Manifest.permission.MEDIA_CONTENT_CONTROL,
                    clientPackageName,
                ) == PackageManager.PERMISSION_GRANTED
            } catch (e: RuntimeException) {
                L.w(e, "Unable to resolve MediaBrowser caller permission")
                false
            }

        return classify(
            packageMatchesUid = packageMatchesUid,
            sameAppUid = clientUid == Process.myUid(),
            systemUid = clientUid == Process.SYSTEM_UID,
            hasMediaControlPermission = hasMediaControlPermission,
            knownOemPackage = clientPackageName in TRUSTED_OEM_PACKAGES,
        )
    }

    companion object {
        private const val MAX_PACKAGE_NAME_LENGTH = 255
        private val TRUSTED_OEM_PACKAGES = setOf("com.dofun.variety", "com.tw.music")

        internal fun classify(
            packageMatchesUid: Boolean,
            sameAppUid: Boolean,
            systemUid: Boolean,
            hasMediaControlPermission: Boolean,
            knownOemPackage: Boolean,
        ): Boolean =
            packageMatchesUid &&
                (sameAppUid || systemUid || hasMediaControlPermission || knownOemPackage)
    }
}
