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
    NotificationSummary(
        "dumpsys notification --noredact 2>&1 | grep -i -E 'com.tw.media|channel.PLAYBACK|NotifyService|notification listener' | head -n 320"
    ),
    DofunServiceSummary(
        "dumpsys activity services com.dofun.variety 2>&1 | grep -i -E 'NotifyService|Media|music|listener' | head -n 240"
    ),
    ActivityBroadcastSummary("dumpsys activity broadcasts"),
    DofunDataHintsReadOnly(
        "content query --uri content://com.dofun.variety.ExportedProvider/hotseat_app_music"
    ),
}

data class DofunIntegrationReport(
    val rootState: RootStateHolder.State,
    val installedPackages: List<String>,
    val packageTopology: DofunPackageTopology,
    val selectedMusicTarget: DofunSelectedMusicTarget,
    val selectionEvidence: String?,
    val probeResults: Map<Ts18RootProbe, String>,
    val bootClassification: String,
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
                    probeResults[probe] = result.take(MAX_PROBE_RESULT_CHARS)
                }
            } else {
                probeResults[Ts18RootProbe.Id] = "Root checks skipped"
            }

            val topology = DofunIntegrationClassifier.topology(installedPackages)
            val selectionEvidence = probeResults[Ts18RootProbe.DofunDataHintsReadOnly]
            val selectedMusicTarget =
                DofunIntegrationClassifier.selectedMusicTarget(selectionEvidence)

            val classification =
                """
                Playback Resume Classification:
                - Activity launch resume requires: autoplayOnLaunch && first cold resume
                - Boot restore requires: autostartOnBoot && autoplayOnLaunch
                - Launcher 'pp' command: can restore playback with play=true
                - cmd=update, seek, prev, next: should not start playback from nothing
                - Floating-only routing applies to MAIN/MUSIC_PLAYER; ACTION_VIEW still opens the player
                - Installed package topology is not evidence that DoFun selected that package
                - DoFun selected target remains UNKNOWN unless a launcher-owned selection surface proves it
                """
                    .trimIndent()

            DofunIntegrationReport(
                rootState = rootState,
                installedPackages = installedPackages,
                packageTopology = topology,
                selectedMusicTarget = selectedMusicTarget,
                selectionEvidence = selectionEvidence,
                probeResults = probeResults,
                bootClassification = classification,
                recommendedStep =
                    DofunIntegrationClassifier.recommendation(topology, selectedMusicTarget),
            )
        }

    private companion object {
        const val MAX_PROBE_RESULT_CHARS = 5_000
    }
}
