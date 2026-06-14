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

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.headunit.topway.TopwaySourcePolicy
import org.oxycblt.auxio.playback.state.PlaybackStateManager
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for orchestrating diagnostic checks and managing capture sessions.
 */
@Singleton
class DiagnosticsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val journal: DiagnosticJournal,
    private val playbackManager: PlaybackStateManager
) {
    private val _isCaptureActive = MutableStateFlow(false)
    val isCaptureActive: StateFlow<Boolean> = _isCaptureActive.asStateFlow()

    suspend fun runAutomatedChecks(): List<DiagnosticEntry> = withContext(Dispatchers.IO) {
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
        val info = "Pkg: ${context.packageName}, Ver: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}), Flavor: ${BuildConfig.FLAVOR}, SDK: ${Build.VERSION.SDK_INT}, Release: ${Build.VERSION.RELEASE}, Fingerprint: ${Build.FINGERPRINT}"
        return DiagnosticEntry(
            "Device and Build Identity",
            info,
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            primaryMethod = "android.os.Build"
        )
    }

    private fun checkDisplayMetrics(): DiagnosticEntry {
        val dm = context.resources.displayMetrics
        val info = "${dm.widthPixels}x${dm.heightPixels}, density=${dm.density}, dpi=${dm.densityDpi}"
        return DiagnosticEntry(
            "Display Metrics",
            info,
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            primaryMethod = "Resources.displayMetrics"
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
                primaryMethod = "PackageManager.getApplicationInfo"
            )
        } catch (e: PackageManager.NameNotFoundException) {
            DiagnosticEntry(
                "$label ($packageName)",
                "Not installed",
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod = "PackageManager.getApplicationInfo"
            )
        } catch (e: Exception) {
            DiagnosticEntry(
                "$label ($packageName)",
                "Query failed: ${e.message}",
                EvidenceClassification.QUERY_FAILED
            )
        }
    }

    private fun checkComponents(): List<DiagnosticEntry> {
        val components = listOf(
            Triple("com.tw.music.MusicActivity", "Topway Activity", true),
            Triple("com.tw.music.MusicService", "Topway Service", false),
            Triple("com.tw.music.view.MusicWidgetProvider", "Topway Widget Provider", false),
            Triple("org.oxycblt.auxio.headunit.topway.TopwayMusicBridgeReceiver", "Topway Bridge Receiver", false)
        )

        return components.map { (cls, label, isActivity) ->
            val intent = Intent().setClassName(context.packageName, cls)
            val pm = context.packageManager
            val resolvable = if (isActivity) {
                pm.resolveActivity(intent, 0) != null
            } else {
                pm.resolveService(intent, 0) != null || pm.queryBroadcastReceivers(intent, 0).isNotEmpty()
            }

            DiagnosticEntry(
                "$label ($cls)",
                "Resolvable: $resolvable",
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod = if (isActivity) "resolveActivity" else "resolveService/queryReceivers"
            )
        }
    }

    private fun checkLauncherState(): DiagnosticEntry {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val resolve = context.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val pkg = resolve?.activityInfo?.packageName ?: "unresolved"

        return DiagnosticEntry(
            "Default Launcher (HOME)",
            pkg,
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            primaryMethod = "resolveActivity(ACTION_MAIN/CATEGORY_HOME)"
        )
    }

    private fun checkNotificationState(): List<DiagnosticEntry> {
        val nm = NotificationManagerCompat.from(context)
        val entries = mutableListOf<DiagnosticEntry>()

        entries.add(DiagnosticEntry(
            "Notifications Enabled",
            nm.areNotificationsEnabled().toString(),
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            primaryMethod = "NotificationManagerCompat.areNotificationsEnabled"
        ))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sysNm = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            val channels = sysNm.notificationChannels.filter { it.id.startsWith(context.packageName) }
            entries.add(DiagnosticEntry(
                "App Notification Channels",
                channels.joinToString { "${it.id}(${it.importance})" },
                EvidenceClassification.OBSERVED_BY_AUXIO,
                primaryMethod = "notificationChannels"
            ))
        }

        val artworkEvents = journal.events.value.filter { it.category == DiagnosticJournal.CAT_NOTIFICATION && it.event.startsWith("Artwork") }
        if (artworkEvents.isNotEmpty()) {
            val last = artworkEvents.last()
            entries.add(DiagnosticEntry(
                "Last Notification Artwork",
                "${last.event}: ${last.detail}",
                EvidenceClassification.OBSERVED_BY_AUXIO
            ))
        }

        return entries
    }

    private fun checkWidgetState(): List<DiagnosticEntry> {
        val entries = mutableListOf<DiagnosticEntry>()
        val awm = try { AppWidgetManager.getInstance(context) } catch (e: Exception) { null }

        if (awm != null) {
            val components = org.oxycblt.auxio.headunit.topway.TopwayWidgetProviderPolicy.providerComponents(context)
            components.forEach { comp ->
                val ids = try { awm.getAppWidgetIds(comp) } catch (e: Exception) { intArrayOf() }
                entries.add(DiagnosticEntry(
                    "Widget IDs (${comp.shortClassName})",
                    ids.joinToString(),
                    EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE
                ))
            }
        } else {
            entries.add(DiagnosticEntry("AppWidgetManager", "Unavailable", EvidenceClassification.API_UNAVAILABLE))
        }

        val updateEvents = journal.events.value.filter { it.category == DiagnosticJournal.CAT_WIDGET }
        entries.add(DiagnosticEntry(
            "Recent Widget Updates",
            if (updateEvents.isEmpty()) "None observed" else "${updateEvents.size} events",
            EvidenceClassification.OBSERVED_BY_AUXIO
        ))

        val zeroIdFallback = BuildConfig.TOPWAY_COMPAT_FLAVOR
        entries.add(DiagnosticEntry(
            "Zero-ID Widget Fallback",
            "Active: $zeroIdFallback",
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
            detail = "TS18/DoFun often uses fixed cards without standard AppWidget IDs."
        ))

        return entries
    }

    private fun checkPlaybackState(): List<DiagnosticEntry> {
        val entries = mutableListOf<DiagnosticEntry>()
        entries.add(DiagnosticEntry(
            "Playback State",
            if (playbackManager.progression.isPlaying) "Playing" else "Stopped/Paused",
            EvidenceClassification.OBSERVED_BY_AUXIO
        ))
        entries.add(DiagnosticEntry(
            "Current Song",
            playbackManager.currentSong?.name?.resolve(context) ?: "None",
            EvidenceClassification.OBSERVED_BY_AUXIO
        ))
        entries.add(DiagnosticEntry(
            "Overlay Permission",
            Settings.canDrawOverlays(context).toString(),
            EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE
        ))
        return entries
    }

    private fun checkStorageState(): List<DiagnosticEntry> {
        val entries = mutableListOf<DiagnosticEntry>()
        val roots = TopwaySourcePolicy.discoverCandidateRoots()
        entries.add(DiagnosticEntry(
            "Discovered Source Roots",
            roots.joinToString(),
            EvidenceClassification.OBSERVED_BY_AUXIO,
            primaryMethod = "TopwaySourcePolicy.discoverCandidateRoots"
        ))

        val mediaRw = File("/mnt/media_rw")
        entries.add(DiagnosticEntry(
            "/mnt/media_rw Accessible",
            mediaRw.canRead().toString(),
            EvidenceClassification.OBSERVED_BY_AUXIO,
            fallbackMethod = "File.canRead()"
        ))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val volumes = try { MediaStore.getExternalVolumeNames(context) } catch (e: Exception) { emptySet() }
            entries.add(DiagnosticEntry(
                "MediaStore External Volumes",
                volumes.joinToString(),
                EvidenceClassification.INFERRED_FROM_PUBLIC_ANDROID_STATE,
                primaryMethod = "MediaStore.getExternalVolumeNames"
            ))
        }

        return entries
    }

    fun startCapture(sessionId: String): Boolean {
        if (journal.startSession(sessionId)) {
            _isCaptureActive.value = true
            return true
        }
        return false
    }

    fun stopCapture() {
        journal.endSession()
        _isCaptureActive.value = false
    }
}
