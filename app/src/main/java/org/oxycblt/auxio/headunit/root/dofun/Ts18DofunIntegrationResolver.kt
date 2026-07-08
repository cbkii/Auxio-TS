/*
 * Copyright (c) 2026 Auxio Project
 * Ts18DofunIntegrationResolver.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.root.dofun

import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.oxycblt.auxio.headunit.root.RootStateHolder

enum class Ts18RootProbe(val command: String) {
    Id("id"),
    PackageSummary(
        "pm list packages -f -u -U -i --user 0 | grep -E 'com\\\\.tw\\\\.music|com\\\\.tw\\\\.media|com\\\\.dofun\\\\.variety'"
    ),
    ResolveMusicComponents(
        "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN com.tw.media"
    ),
    PackageDumpMedia("dumpsys package com.tw.media"),
    PackageDumpMusic("dumpsys package com.tw.music"),
    AppWidgetSummary("dumpsys appwidget"),
    MediaSessionSummary("dumpsys media_session"),
    ActivityBroadcastSummary("dumpsys activity broadcasts"),
    DofunDataHintsReadOnly(
        "content query --uri content://com.dofun.variety.ExportedProvider/hotseat_app_music"
    ),
}

enum class Ts18RootMutation(val command: String) {
    DisableStockMusicForUser0("pm disable-user --user 0 com.tw.music"),
    EnableStockMusicForUser0("pm enable --user 0 com.tw.music"),
}

enum class Ts18DofunDetectedPath {
    StockTwMusicSelected,
    AuxioTwMediaSelected,
    AuxioInstalledButDebugPackage,
    AuxioMissingStockAlias,
    StockTwMusicEnabledMayBePreferred,
    WidgetProviderBound,
    AndroidMediaSessionOnly,
    RootChecksSkipped,
    Unknown,
}

data class DofunIntegrationReport(
    val rootState: RootStateHolder.State,
    val installedPackages: List<String>,
    val probeResults: Map<Ts18RootProbe, String>,
    val bootClassification: String,
    val detectedPath: Ts18DofunDetectedPath,
    val recommendedStep: String,
)

class Ts18DofunIntegrationResolver(
    private val context: Context,
    private val rootStateHolder: RootStateHolder,
) {
    suspend fun runIntegrationCheck(): DofunIntegrationReport =
        withContext(Dispatchers.IO) {
            val rootState = rootStateHolder.probeSync()
            val installedPackages = mutableListOf<String>()

            val pm = context.packageManager
            listOf("com.tw.media", "com.tw.media.debug", "com.tw.music", "com.dofun.variety")
                .forEach {
                    try {
                        pm.getPackageInfo(it, 0)
                        installedPackages.add(it)
                    } catch (e: PackageManager.NameNotFoundException) {}
                }

            val probeResults = mutableMapOf<Ts18RootProbe, String>()

            if (rootState == RootStateHolder.State.Available) {
                Ts18RootProbe.entries.forEach { probe ->
                    val result = rootStateHolder.runTs18ProbeSync(probe) ?: "null"
                    probeResults[probe] = result.take(5000)
                }
            } else {
                probeResults[Ts18RootProbe.Id] = "Root checks skipped"
            }

            val classification =
                """
                Playback Resume Classification:
                - Activity launch resume requires: autoplayOnLaunch && first cold resume
                - Boot restore requires: autostartOnBoot && autoplayOnLaunch
                - Launcher 'pp' command: can restore playback with play=true
                - cmd=update, seek, prev, next: should not start playback from nothing
                """
                    .trimIndent()

            val hasStock = installedPackages.contains("com.tw.music")
            val hasMedia = installedPackages.contains("com.tw.media")
            val hasDebugMedia = installedPackages.contains("com.tw.media.debug")

            val detectedPath =
                when {
                    hasDebugMedia -> Ts18DofunDetectedPath.AuxioInstalledButDebugPackage
                    hasStock && hasMedia -> Ts18DofunDetectedPath.StockTwMusicEnabledMayBePreferred
                    hasStock -> Ts18DofunDetectedPath.StockTwMusicSelected
                    hasMedia -> Ts18DofunDetectedPath.AuxioTwMediaSelected
                    else -> Ts18DofunDetectedPath.Unknown
                }

            val recommendedStep =
                when (detectedPath) {
                    Ts18DofunDetectedPath.StockTwMusicSelected ->
                        "If DoFun controls stock instead of Auxio, consider using the advanced root test to disable stock com.tw.music."
                    Ts18DofunDetectedPath.AuxioTwMediaSelected ->
                        "Integration appears correct based on package state. Verify widget updates locally."
                    Ts18DofunDetectedPath.StockTwMusicEnabledMayBePreferred ->
                        "Stock com.tw.music is enabled and may still be preferred. Run the reversible stock-selection test to test DoFun fallback."
                    Ts18DofunDetectedPath.AuxioInstalledButDebugPackage ->
                        "Uninstall com.tw.media.debug and install topwayTwMediaRelease. DoFun requires exact match."
                    Ts18DofunDetectedPath.AndroidMediaSessionOnly ->
                        "Open Auxio once and rerun widget update because aliases exist but no widget binding is visible."
                    Ts18DofunDetectedPath.RootChecksSkipped ->
                        "Enable the existing root/directFS toggle if root-assisted checks are wanted."
                    else -> "Install the topwayTwMediaRelease variant to integrate with DoFun."
                }

            DofunIntegrationReport(
                rootState,
                installedPackages,
                probeResults,
                classification,
                detectedPath,
                recommendedStep,
            )
        }

    suspend fun testStockSelectionDisabledUser0(): Boolean =
        withContext(Dispatchers.IO) {
            if (rootStateHolder.stateSnapshot() != RootStateHolder.State.Available)
                return@withContext false
            rootStateHolder.runTs18MutationSync(Ts18RootMutation.DisableStockMusicForUser0) != null
        }

    suspend fun restoreStockSelectionDisabledUser0(): Boolean =
        withContext(Dispatchers.IO) {
            if (rootStateHolder.stateSnapshot() != RootStateHolder.State.Available)
                return@withContext false
            rootStateHolder.runTs18MutationSync(Ts18RootMutation.EnableStockMusicForUser0) != null
        }
}
