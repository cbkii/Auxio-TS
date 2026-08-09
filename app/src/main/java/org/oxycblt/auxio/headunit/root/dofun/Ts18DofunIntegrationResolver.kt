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
import android.database.Cursor
import android.net.Uri
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
    val selectionEvidenceSource: String,
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
            val installedPackages = mutableListOf<String>()
            val pm = context.packageManager
            listOf("com.tw.media", "com.tw.media.debug", "com.tw.music", "com.dofun.variety")
                .forEach {
                    try {
                        pm.getPackageInfo(it, 0)
                        installedPackages.add(it)
                    } catch (_: PackageManager.NameNotFoundException) {}
                }
            val topology = DofunIntegrationClassifier.topology(installedPackages)

            // Query the launcher-owned exported selection surface under Auxio's real app UID first.
            // A later root read can improve observability but must never substitute for app authority.
            val appSelectionEvidence =
                if (topology.dofunPresent) readDofunSelectionFromAppUid() else null
            val appSelectedTarget =
                DofunIntegrationClassifier.selectedMusicTarget(appSelectionEvidence)

            val rootState = rootStateHolder.probeSync()
            val probeResults = mutableMapOf<Ts18RootProbe, String>()
            if (rootState == RootStateHolder.State.Available) {
                Ts18RootProbe.entries.forEach { probe ->
                    val result = rootStateHolder.runTs18ProbeSync(probe) ?: "null"
                    probeResults[probe] = result.take(MAX_PROBE_RESULT_CHARS)
                }
            } else {
                probeResults[Ts18RootProbe.Id] = "Root checks skipped"
            }

            val rootSelectionEvidence = probeResults[Ts18RootProbe.DofunDataHintsReadOnly]
            val selection =
                chooseSelectionEvidence(
                    appSelectionEvidence,
                    appSelectedTarget,
                    rootSelectionEvidence,
                )

            val classification =
                """
                Playback Resume Classification:
                - Activity launch resume requires: autoplayOnLaunch && first cold resume
                - Boot restore requires: autostartOnBoot && autoplayOnLaunch
                - Launcher 'pp' command: can restore playback with play=true
                - cmd=update, seek, prev, next: should not start playback from nothing
                - Floating-only routing applies to MAIN/MUSIC_PLAYER; ACTION_VIEW still opens the player
                - Installed package topology is not evidence that DoFun selected that package
                - Only app-UID provider evidence may establish the selected DoFun target
                - Root provider output is retained as observation and cannot replace failed app authority
                - DoFun selected target remains UNKNOWN unless the app-authority surface proves it
                """
                    .trimIndent()

            DofunIntegrationReport(
                rootState = rootState,
                installedPackages = installedPackages,
                packageTopology = topology,
                selectedMusicTarget = selection.target,
                selectionEvidence = selection.evidence,
                selectionEvidenceSource = selection.source,
                probeResults = probeResults,
                bootClassification = classification,
                recommendedStep =
                    DofunIntegrationClassifier.recommendation(topology, selection.target),
            )
        }

    private fun readDofunSelectionFromAppUid(): String =
        try {
            val cursor =
                context.contentResolver.query(
                    DOFUN_SELECTION_URI,
                    null,
                    null,
                    null,
                    null,
                ) ?: return "Provider returned null cursor"
            cursor.use { serializeSelectionCursor(it) }
        } catch (e: SecurityException) {
            "SecurityException: ${e.message.orEmpty().take(MAX_PROVIDER_ERROR_CHARS)}"
        } catch (e: IllegalArgumentException) {
            "IllegalArgumentException: ${e.message.orEmpty().take(MAX_PROVIDER_ERROR_CHARS)}"
        } catch (e: RuntimeException) {
            "${e.javaClass.simpleName}: ${e.message.orEmpty().take(MAX_PROVIDER_ERROR_CHARS)}"
        }

    private fun serializeSelectionCursor(cursor: Cursor): String {
        if (!cursor.moveToFirst()) return "No result found."
        val columnNames = cursor.columnNames.take(MAX_SELECTION_COLUMNS)
        val rows = mutableListOf<String>()
        do {
            val row =
                columnNames.mapIndexed { index, column ->
                    "$column=${readCursorValue(cursor, index)}"
                }.joinToString(prefix = "Row: ", separator = ", ")
            rows.add(row.take(MAX_SELECTION_ROW_CHARS))
        } while (rows.size < MAX_SELECTION_ROWS && cursor.moveToNext())
        return rows.joinToString("\n").take(MAX_PROBE_RESULT_CHARS)
    }

    private fun readCursorValue(cursor: Cursor, index: Int): String =
        try {
            when (cursor.getType(index)) {
                Cursor.FIELD_TYPE_NULL -> "null"
                Cursor.FIELD_TYPE_BLOB -> "<blob>"
                else -> cursor.getString(index)?.take(MAX_SELECTION_VALUE_CHARS) ?: "null"
            }
        } catch (_: RuntimeException) {
            "<unreadable>"
        }

    private fun chooseSelectionEvidence(
        appEvidence: String?,
        appTarget: DofunSelectedMusicTarget,
        rootEvidence: String?,
    ): SelectionEvidence =
        when {
            appTarget != DofunSelectedMusicTarget.UNKNOWN ->
                SelectionEvidence(appTarget, appEvidence, "APP_UID_EXPORTED_PROVIDER")
            appEvidence != null ->
                SelectionEvidence(DofunSelectedMusicTarget.UNKNOWN, appEvidence, "APP_UID_EXPORTED_PROVIDER")
            rootEvidence != null ->
                SelectionEvidence(
                    DofunSelectedMusicTarget.UNKNOWN,
                    rootEvidence,
                    "ROOT_OBSERVATION_ONLY",
                )
            else -> SelectionEvidence(DofunSelectedMusicTarget.UNKNOWN, null, "NONE")
        }

    private data class SelectionEvidence(
        val target: DofunSelectedMusicTarget,
        val evidence: String?,
        val source: String,
    )

    private companion object {
        val DOFUN_SELECTION_URI: Uri =
            Uri.parse("content://com.dofun.variety.ExportedProvider/hotseat_app_music")
        const val MAX_PROBE_RESULT_CHARS = 5_000
        const val MAX_PROVIDER_ERROR_CHARS = 240
        const val MAX_SELECTION_COLUMNS = 16
        const val MAX_SELECTION_ROWS = 8
        const val MAX_SELECTION_ROW_CHARS = 1_024
        const val MAX_SELECTION_VALUE_CHARS = 512
    }
}
