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
        "pm list packages -f -U | grep -E 'com\\.tw\\.music|com\\.tw\\.media|com\\.dofun\\.variety'"
    ),
    ResolveMusicComponents(
        "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN com.tw.media"
    ),
    ResolveTopwayAlias(
        "cmd package resolve-activity --user 0 --brief -c android.intent.category.LAUNCHER -a android.intent.action.MAIN -n com.tw.media/com.tw.music.MusicActivity"
    ),
    OverlayRuntime(
        "appops get com.tw.media SYSTEM_ALERT_WINDOW 2>&1; dumpsys activity services com.tw.media 2>&1 | head -n 160; dumpsys window windows 2>&1 | grep -E 'com.tw.media|CarFloatingControls' | head -n 80"
    ),
    EqualizerComponents(
        "dumpsys package com.tw.eq 2>&1 | grep -E 'EQChoiceActivity|DSPActivity|EQActivity|enabledComponents|disabledComponents' | head -n 160"
    ),
    VisualizerEffects(
        "dumpsys media.audio_flinger 2>&1 | grep -i -E 'visualizer|session|com.tw.media' | head -n 200"
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
    AuxioTwMediaWithStockCoexisting,
    AuxioInstalledButDebugPackage,
    AuxioMissingStockAlias,
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
                    } catch (_: PackageManager.NameNotFoundException) {}
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
                - Floating-only routing applies to MAIN/MUSIC_PLAYER; ACTION_VIEW still opens the player
                """
                    .trimIndent()

            val hasStock = installedPackages.contains("com.tw.music")
            val hasMedia = installedPackages.contains("com.tw.media")
            val hasDebugMedia = installedPackages.contains("com.tw.media.debug")

            val detectedPath =
                when {
                    hasDebugMedia -> Ts18DofunDetectedPath.AuxioInstalledButDebugPackage
                    hasStock && hasMedia -> Ts18DofunDetectedPath.AuxioTwMediaWithStockCoexisting
                    hasStock -> Ts18DofunDetectedPath.StockTwMusicSelected
                    hasMedia -> Ts18DofunDetectedPath.AuxioTwMediaSelected
                    else -> Ts18DofunDetectedPath.Unknown
                }

            val recommendedStep =
                when (detectedPath) {
                    Ts18DofunDetectedPath.StockTwMusicSelected ->
                        "Install topwayTwMediaRelease or the systemless topwayTwMusic module; do not mutate stock solely from this check."
                    Ts18DofunDetectedPath.AuxioTwMediaSelected ->
                        "Exact com.tw.media identity is present. Verify the fixed alias, overlay runtime, widget and media-session probes."
                    Ts18DofunDetectedPath.AuxioTwMediaWithStockCoexisting ->
                        "Stock com.tw.music and Auxio com.tw.media can safely coexist. Package presence alone does not prove DoFun preference; do not disable stock unless a bounded reversible component-selection test requires it."
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
