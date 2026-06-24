/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticsRepository.kt is part of Auxio.
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

package org.oxycblt.auxio.diagnostics

import android.annotation.TargetApi
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.headunit.root.RootStateHolder
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.music.MusicSettings
import org.oxycblt.auxio.music.resolve
import org.oxycblt.auxio.playback.state.PlaybackStateManager

/** Repository for orchestrating diagnostic checks and managing capture sessions. */
@Singleton
class DiagnosticsRepository
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val journal: DiagnosticJournal,
    private val playbackManager: PlaybackStateManager,
    private val musicSettings: MusicSettings,
    private val rootGate: RootStateHolder,
) {
    private val _isCaptureActive = MutableStateFlow(false)
    val isCaptureActive: StateFlow<Boolean> = _isCaptureActive.asStateFlow()

    suspend fun runAutomatedChecks(): List<DiagnosticEntry> =
        withContext(Dispatchers.IO) {
            val results = mutableListOf<DiagnosticEntry>()

            // 1. Device and Build Identity
            results.add(checkBuildIdentity())
            results.add(checkDisplayMetrics())

            // 2. Targeted Package Checks
            results.add(checkPackage("com.dofun.variety", "DoFun Variety"))
            results.add(checkPackage("com.tw.music", "Stock TW Music"))
            results.add(checkPackage("com.tw.media", "Stock TW Media"))

            // 3. Component Checks
            results.addAll(checkComponents())

            // 4. Launcher State
            results.add(checkLauncherState())

            // 5. Notifications
            results.addAll(checkNotificationState())

            // 6. Widget State
            results.addAll(checkWidgetState())

            // 7. Playback and Session State
            results.addAll(checkPlaybackState())

            // 7. Storage
            results.addAll(checkStorageState())

            results
        }

    private fun checkBuildIdentity(): DiagnosticEntry {
        val info =
            "Pkg: ${context.packageName}, Ver: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}), Flavor: ${BuildConfig.FLAVOR}, SDK: ${Build.VERSION.SDK_INT}, Release: ${Build.VERSION.RELEASE}, Fingerprint: ${Build.FINGERPRINT}"
        return DiagnosticEntry(
            "Device and Build Identity",
            info,
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            primaryMethod = "android.os.Build",
        )
    }

    private fun checkDisplayMetrics(): DiagnosticEntry {
        val dm = context.resources.displayMetrics
        val info =
            "${dm.widthPixels}x${dm.heightPixels}, density=${dm.density}, dpi=${dm.densityDpi}"
        return DiagnosticEntry(
            "Display Metrics",
            info,
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            primaryMethod = "Resources.displayMetrics",
        )
    }

    private fun checkPackage(packageName: String, label: String): DiagnosticEntry {
        val pm = context.packageManager
        return try {
            val info = pm.getApplicationInfo(packageName, 0)
            val pkg = pm.getPackageInfo(packageName, 0)
            val system = (info.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
            val priv = info.sourceDir.contains("/priv-app/")
            DiagnosticEntry(
                "$label ($packageName)",
                "Installed: true, Enabled: ${info.enabled}, System: $system, Privileged: $priv, Version: ${pkg.versionName}",
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod = "PackageManager.getApplicationInfo",
            )
        } catch (e: PackageManager.NameNotFoundException) {
            DiagnosticEntry(
                "$label ($packageName)",
                "Not installed",
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod = "PackageManager.getApplicationInfo",
            )
        } catch (e: Exception) {
            DiagnosticEntry(
                "$label ($packageName)",
                "Query failed: ${e.message}",
                EvidenceClassification.QUERY_FAILED,
            )
        }
    }

    private fun checkComponents(): List<DiagnosticEntry> {
        val components =
            listOf(
                Triple("com.tw.music.MusicActivity", "Topway Activity", true),
                Triple("com.tw.music.MusicService", "Topway Service", false),
                Triple("com.tw.music.view.MusicWidgetProvider", "Topway Widget Provider", false),
                Triple(
                    "org.oxycblt.auxio.headunit.topway.TopwayMusicBridgeReceiver",
                    "Topway Bridge Receiver",
                    false,
                ),
            )

        return components.map { (cls, label, isActivity) ->
            val intent = Intent().setClassName(context.packageName, cls)
            val pm = context.packageManager
            val resolvable =
                if (isActivity) {
                    pm.resolveActivity(intent, 0) != null
                } else {
                    pm.resolveService(intent, 0) != null ||
                        pm.queryBroadcastReceivers(intent, 0).isNotEmpty()
                }

            DiagnosticEntry(
                "$label ($cls)",
                "Resolvable: $resolvable",
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod =
                    if (isActivity) "resolveActivity" else "resolveService/queryReceivers",
            )
        }
    }

    private fun checkLauncherState(): DiagnosticEntry {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val resolve =
            context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val pkg = resolve?.activityInfo?.packageName ?: "unresolved"

        return DiagnosticEntry(
            "Default Launcher (HOME)",
            pkg,
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            primaryMethod = "resolveActivity(ACTION_MAIN/CATEGORY_HOME)",
        )
    }

    private fun checkNotificationState(): List<DiagnosticEntry> {
        val nm = NotificationManagerCompat.from(context)
        val entries = mutableListOf<DiagnosticEntry>()

        entries.add(
            DiagnosticEntry(
                "Notifications Enabled",
                nm.areNotificationsEnabled().toString(),
                EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
                primaryMethod = "NotificationManagerCompat.areNotificationsEnabled",
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sysNm =
                context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as android.app.NotificationManager
            val channels =
                sysNm.notificationChannels.filter { it.id.startsWith(context.packageName) }
            entries.add(
                DiagnosticEntry(
                    "App Notification Channels",
                    channels.joinToString { "${it.id}(${it.importance})" },
                    EvidenceClassification.OBSERVED_BY_AUXIO,
                    primaryMethod = "notificationChannels",
                )
            )
        }

        val artworkEvents =
            journal.events.value.filter {
                it.category == DiagnosticJournal.CAT_NOTIFICATION && it.event.startsWith("Artwork")
            }
        if (artworkEvents.isNotEmpty()) {
            val last = artworkEvents.last()
            entries.add(
                DiagnosticEntry(
                    "Last Notification Artwork",
                    "${last.event}: ${last.detail}",
                    EvidenceClassification.OBSERVED_BY_AUXIO,
                )
            )
        }

        return entries
    }

    private fun checkWidgetState(): List<DiagnosticEntry> {
        val entries = mutableListOf<DiagnosticEntry>()
        val awm =
            try {
                AppWidgetManager.getInstance(context)
            } catch (e: Exception) {
                null
            }

        if (awm != null) {
            val components =
                org.oxycblt.auxio.headunit.topway.TopwayWidgetProviderPolicy.providerComponents(
                    context
                )
            components.forEach { comp ->
                val ids =
                    try {
                        awm.getAppWidgetIds(comp)
                    } catch (e: Exception) {
                        intArrayOf()
                    }
                entries.add(
                    DiagnosticEntry(
                        "Widget IDs (${comp.shortClassName})",
                        ids.joinToString(),
                        EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
                    )
                )
            }
        } else {
            entries.add(
                DiagnosticEntry(
                    "AppWidgetManager",
                    "Unavailable",
                    EvidenceClassification.API_UNAVAILABLE,
                )
            )
        }

        val updateEvents =
            journal.events.value.filter { it.category == DiagnosticJournal.CAT_WIDGET }
        entries.add(
            DiagnosticEntry(
                "Recent Widget Updates",
                if (updateEvents.isEmpty()) "None observed" else "${updateEvents.size} events",
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )

        val zeroIdFallback = BuildConfig.TOPWAY_COMPAT_FLAVOR
        entries.add(
            DiagnosticEntry(
                "Zero-ID Widget Fallback",
                "Active: $zeroIdFallback",
                EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
                detail = "TS18/DoFun often uses fixed cards without standard AppWidget IDs.",
            )
        )

        return entries
    }

    private fun checkPlaybackState(): List<DiagnosticEntry> {
        val entries = mutableListOf<DiagnosticEntry>()
        entries.add(
            DiagnosticEntry(
                "Playback State",
                if (playbackManager.progression.isPlaying) "Playing" else "Stopped/Paused",
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )
        entries.add(
            DiagnosticEntry(
                "Current Song",
                playbackManager.currentSong?.name?.resolve(context) ?: "None",
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )
        entries.add(
            DiagnosticEntry(
                "Overlay Permission",
                Settings.canDrawOverlays(context).toString(),
                EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            )
        )
        return entries
    }

    private fun checkStorageState(): List<DiagnosticEntry> {
        val entries = mutableListOf<DiagnosticEntry>()
        val query = musicSettings.safQuery

        entries.add(
            DiagnosticEntry(
                "Diagnostic Authority",
                "normal app context; no ADB, Shizuku, or root shell authority used",
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )
        entries.add(
            DiagnosticEntry(
                "Storage Source Mode",
                musicSettings.locationMode.name,
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )
        entries.add(
            DiagnosticEntry(
                "Configured Source Locations",
                query.source.joinToString { it.uri.toString() }.ifBlank { "None" },
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )
        entries.add(
            DiagnosticEntry(
                "Configured Excluded Locations",
                query.exclude.joinToString { it.uri.toString() }.ifBlank { "None" },
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )
        entries.add(persistedSafUriPermissionsEntry())
        entries.add(
            DiagnosticEntry(
                "Last Scan Failed",
                musicSettings.lastScanFailed.toString(),
                EvidenceClassification.OBSERVED_BY_AUXIO,
            )
        )
        entries.add(
            DiagnosticEntry(
                "Root Gate State",
                rootGate.state.name,
                EvidenceClassification.OBSERVED_BY_AUXIO,
                detail =
                    "Topway compat root gate state only; diagnostics did not execute root commands.",
            )
        )

        val storageChildren = listDirectoryChildren(File("/storage"), removableOnly = false)
        val mediaRwChildren = listDirectoryChildren(File("/mnt/media_rw"), removableOnly = true)
        val discoveredRoots = TopwaySourcePolicy.discoverCandidateRoots()

        val storageUsbChildren =
            storageChildren.filter { File(it).name.startsWith("usbdisk", ignoreCase = true) }

        entries.add(storageListEntry("/storage Roots", storageChildren, removableOnly = false))
        entries.add(
            storageListEntry("/storage USB Roots", storageUsbChildren, removableOnly = true)
        )
        entries.add(
            storageListEntry("/mnt/media_rw USB Roots", mediaRwChildren, removableOnly = true)
        )
        entries.add(
            DiagnosticEntry(
                "Discovered Source Roots",
                discoveredRoots.joinToString().ifBlank { "None visible/readable" },
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod = "TopwaySourcePolicy.discoverCandidateRoots",
            )
        )
        (storageChildren + storageUsbChildren + mediaRwChildren + discoveredRoots)
            .distinct()
            .forEach { path -> entries.add(candidatePathEntry(path)) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val volumes = mediaStoreVolumes(entries)
            volumes?.forEach { volume -> entries.add(mediaStoreAudioRowCountEntry(volume)) }
        } else {
            entries.add(
                DiagnosticEntry(
                    "MediaStore External Volumes",
                    "API ${Build.VERSION.SDK_INT} does not expose getExternalVolumeNames",
                    EvidenceClassification.API_UNAVAILABLE,
                )
            )
        }

        return entries
    }

    private fun persistedSafUriPermissionsEntry(): DiagnosticEntry =
        try {
            DiagnosticEntry(
                "Persisted SAF URI Permissions",
                context.contentResolver.persistedUriPermissions
                    .joinToString {
                        "${it.uri} read=${it.isReadPermission} write=${it.isWritePermission}"
                    }
                    .ifBlank { "None" },
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod = "ContentResolver.persistedUriPermissions",
            )
        } catch (e: SecurityException) {
            DiagnosticEntry(
                "Persisted SAF URI Permissions",
                "Permission denied: ${e.message ?: "no message"}",
                EvidenceClassification.PERMISSION_DENIED,
                primaryMethod = "ContentResolver.persistedUriPermissions",
            )
        } catch (e: RuntimeException) {
            DiagnosticEntry(
                "Persisted SAF URI Permissions",
                "Query failed: ${e.javaClass.simpleName}: ${e.message ?: "no message"}",
                EvidenceClassification.QUERY_FAILED,
                primaryMethod = "ContentResolver.persistedUriPermissions",
            )
        }

    private fun listDirectoryChildren(root: File, removableOnly: Boolean): List<String> =
        try {
            root
                .listFiles()
                ?.asSequence()
                ?.filter { it.isDirectory }
                ?.filter { !removableOnly || it.name.startsWith("usbdisk", ignoreCase = true) }
                ?.filter { it.name != "self" && it.name != "emulated" }
                ?.sortedBy { it.absolutePath }
                ?.map { it.absolutePath }
                ?.toList()
                .orEmpty()
        } catch (e: SecurityException) {
            emptyList()
        } catch (e: RuntimeException) {
            emptyList()
        }

    private fun storageListEntry(
        name: String,
        paths: List<String>,
        removableOnly: Boolean,
    ): DiagnosticEntry =
        DiagnosticEntry(
            name,
            paths.joinToString().ifBlank { "None visible" },
            EvidenceClassification.OBSERVED_BY_AUXIO,
            detail =
                if (removableOnly) "Only usbdiskN children are reported from /mnt/media_rw."
                else "emulated/self pseudo-directories are excluded from removable candidates.",
        )

    private fun candidatePathEntry(path: String): DiagnosticEntry {
        val file = File(path)
        return DiagnosticEntry(
            "Candidate Path State: $path",
            "exists=${file.exists()}, isDirectory=${file.isDirectory}, canRead=${file.canRead()}",
            EvidenceClassification.OBSERVED_BY_AUXIO,
            primaryMethod = "java.io.File",
        )
    }

    // @TargetApi documents SDK-gated API-29 MediaStore helpers; callers guard with SDK_INT >= Q.
    @TargetApi(Build.VERSION_CODES.Q)
    private fun mediaStoreVolumes(entries: MutableList<DiagnosticEntry>): Set<String>? {
        val volumes =
            try {
                MediaStore.getExternalVolumeNames(context)
            } catch (e: Exception) {
                entries.add(
                    DiagnosticEntry(
                        "MediaStore External Volumes",
                        "Query failed: ${e.javaClass.simpleName}: ${e.message ?: "no message"}",
                        EvidenceClassification.QUERY_FAILED,
                        primaryMethod = "MediaStore.getExternalVolumeNames",
                    )
                )
                return null
            }
        entries.add(
            DiagnosticEntry(
                "MediaStore External Volumes",
                volumes.joinToString().ifBlank { "None" },
                EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
                primaryMethod = "MediaStore.getExternalVolumeNames",
            )
        )
        return volumes
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private fun mediaStoreAudioRowCountEntry(volume: String): DiagnosticEntry {
        val uri: Uri = MediaStore.Audio.Media.getContentUri(volume)
        return try {
            context.contentResolver
                .query(uri, arrayOf(MediaStore.Audio.Media._ID), null, null, null)
                ?.use { cursor ->
                    DiagnosticEntry(
                        "MediaStore Audio Rows: $volume",
                        cursor.count.toString(),
                        EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
                        primaryMethod = "ContentResolver.query(${uri})",
                    )
                }
                ?: DiagnosticEntry(
                    "MediaStore Audio Rows: $volume",
                    "Query returned null cursor",
                    EvidenceClassification.QUERY_FAILED,
                    primaryMethod = "ContentResolver.query(${uri})",
                )
        } catch (e: SecurityException) {
            DiagnosticEntry(
                "MediaStore Audio Rows: $volume",
                "Permission denied: ${e.message ?: "no message"}",
                EvidenceClassification.PERMISSION_DENIED,
                primaryMethod = "ContentResolver.query(${uri})",
            )
        } catch (e: RuntimeException) {
            DiagnosticEntry(
                "MediaStore Audio Rows: $volume",
                "Query failed: ${e.javaClass.simpleName}: ${e.message ?: "no message"}",
                EvidenceClassification.QUERY_FAILED,
                primaryMethod = "ContentResolver.query(${uri})",
            )
        }
    }

    fun startCapture(
        sessionId: String,
        origin: String = DiagnosticService.ORIGIN_MANUAL,
        durationMs: Long = 15 * 60 * 1000L,
    ): Boolean {
        val started = journal.startSession(sessionId)
        _isCaptureActive.value = journal.hasActiveSession
        if (started) {
            journal.log(
                DiagnosticJournal.CAT_SESSION,
                "Capture owner established",
                "origin=$origin, durationMs=${DiagnosticService.clampDurationMs(durationMs)}",
            )
        }
        return started
    }

    fun stopCapture(sessionId: String? = null): Boolean {
        val ended = journal.endSession(sessionId)
        _isCaptureActive.value = journal.hasActiveSession
        return ended
    }
}
