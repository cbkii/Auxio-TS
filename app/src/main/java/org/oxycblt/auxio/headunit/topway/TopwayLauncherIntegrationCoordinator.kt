/*
 * Copyright (c) 2026 Auxio Project
 * TopwayLauncherIntegrationCoordinator.kt is part of Auxio.
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

package org.oxycblt.auxio.headunit.topway

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.SystemClock
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.compat.HeadUnitMetadataSnapshot
import org.oxycblt.auxio.playback.state.Progression
import timber.log.Timber as L

class TopwayLauncherIntegrationCoordinator
@Inject
constructor(
    @ApplicationContext private val context: Context,
    private val journal: DiagnosticJournal,
) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private var lastMetadata: HeadUnitMetadataSnapshot? = null
    private var lastProgress: TopwayProgressSnapshot? = null
    private var lastProgressAtMs = 0L
    private val isDoFunInstalled: Boolean by lazy {
        try {
            context.packageManager.getLaunchIntentForPackage(DOFUN_PACKAGE) != null
        } catch (e: RuntimeException) {
            L.w(e, "Unable to query DoFun launcher package state")
            false
        }
    }

    var mode: Ts18LauncherIntegrationMode
        get() =
            Ts18LauncherIntegrationMode.fromPreference(
                prefs.getString(Ts18LauncherIntegrationMode.PREF_KEY, null)
            )
        set(value) = prefs.edit { putString(Ts18LauncherIntegrationMode.PREF_KEY, value.name) }

    var seekUnitPolicy: TopwaySeekUnitPolicy
        get() =
            TopwaySeekUnitPolicy.entries.firstOrNull {
                it.name == prefs.getString(PREF_SEEK_UNIT, null)
            } ?: TopwaySeekUnitPolicy.Auto
        set(value) = prefs.edit { putString(PREF_SEEK_UNIT, value.name) }

    fun publishMetadata(
        snapshot: HeadUnitMetadataSnapshot?,
        reason: String,
        force: Boolean = false,
    ) {
        if (!canBroadcast("TX metadata:$reason")) return
        if (!force && snapshot == lastMetadata) return
        sendTopwayBroadcast(
            TopwayMusicIntentFactory.metadataIntent(snapshot),
            "metadata:$reason",
            snapshot?.safeSummary(),
        )
        lastMetadata = snapshot
    }

    fun publishProgress(
        progressMs: Long,
        durationMs: Long,
        reason: String,
        force: Boolean = false,
        nowMs: Long = SystemClock.elapsedRealtime(),
    ) {
        if (!canBroadcast("TX progress:$reason")) return
        val snapshot =
            TopwayProgressStatePolicy.active(progressMs, durationMs)
                ?: TopwayProgressStatePolicy.CLEAR
        if (
            !force &&
                !TopwayProgressStatePolicy.shouldPublish(
                    snapshot,
                    lastProgress,
                    nowMs,
                    lastProgressAtMs,
                    MIN_PROGRESS_INTERVAL_MS,
                )
        ) {
            return
        }
        sendTopwayBroadcast(
            TopwayMusicIntentFactory.progressIntent(snapshot.progressMs, snapshot.durationMs),
            "progress:$reason",
            "${snapshot.progressMs}/${snapshot.durationMs}",
        )
        lastProgress = snapshot
        lastProgressAtMs = nowMs
    }

    fun clear(reason: String) {
        publishMetadata(null, "clear:$reason", force = true)
        publishProgress(0L, 0L, "clear:$reason", force = true)
    }

    fun onProgressionChanged(
        progression: Progression,
        durationMs: Long,
        reason: String,
        force: Boolean = false,
    ) {
        publishProgress(progression.calculateElapsedPositionMs(), durationMs, reason, force)
    }

    fun handle(intent: Intent?, callbacks: TopwayStartCallbacks): Boolean {
        if (intent == null || !TopwayMusicContract.isIncomingAction(intent.action)) return false
        val extras =
            TopwayBridgeExtrasPolicy.sanitizeIncomingExtras(
                TopwayBridgeExtrasPolicy.safelyExtractIncomingExtras(
                    intent,
                    javaClass.classLoader,
                    source = "TopwayLauncherIntegrationCoordinator",
                )
            )
        logJournalAndTimber(
            DiagnosticJournal.CAT_TOPWAY_CMD,
            "RX command",
            "action=${intent.action} cmd=${extras.cmd} seek=${extras.widgetProgress}",
        )
        if (!mode.handlesTopwayCommands) {
            logSuppressed("RX command:${intent.action}")
            callbacks.ignore()
            return true
        }
        if (intent.action == TopwayMusicContract.ACTION_LAUNCHER_WIDGET_SEEK) {
            val decision =
                TopwaySeekPolicyConverter.convert(
                    extras.widgetProgress,
                    callbacks.currentDurationMs,
                    seekUnitPolicy,
                )
            logJournalAndTimber(
                DiagnosticJournal.CAT_TOPWAY_CMD,
                "RX widget seek",
                decision.detail,
                decision.unit?.name ?: "ignored",
            )
            decision.positionMs?.let { callbacks.seekTo(it) } ?: callbacks.ignore()
            return true
        }
        when (TopwayMusicCommandMapper.map(intent.action, extras.cmd)) {
            TopwayMappedCommand.PREV ->
                if (callbacks.hasCurrentSong) callbacks.previous() else callbacks.ignore()
            TopwayMappedCommand.NEXT ->
                if (callbacks.hasCurrentSong) callbacks.next() else callbacks.ignore()
            TopwayMappedCommand.PLAY_PAUSE -> callbacks.playPause()
            TopwayMappedCommand.UPDATE -> {
                logJournalAndTimber(DiagnosticJournal.CAT_TOPWAY_CMD, "RX forced update")
                callbacks.widgetUpdate()
            }
            TopwayMappedCommand.UNKNOWN -> {
                logJournalAndTimber(DiagnosticJournal.CAT_TOPWAY_CMD, "RX unknown", intent.action)
                callbacks.ignore()
            }
        }
        return true
    }

    private fun canBroadcast(reason: String): Boolean {
        if (mode.sendsTopwayBroadcasts) return true
        logSuppressed(reason)
        return false
    }

    private fun logSuppressed(reason: String) {
        logJournalAndTimber(
            DiagnosticJournal.CAT_TOPWAY_BROADCAST,
            "Ignored due to mode",
            reason,
            mode.name,
        )
    }

    private fun sendTopwayBroadcast(intent: Intent, reason: String, detail: String?) {
        if (mode.diagnosticsOnly) {
            logSuppressed(reason)
            return
        }
        try {
            context.sendBroadcast(Intent(intent))
            logJournalAndTimber(
                DiagnosticJournal.CAT_TOPWAY_BROADCAST,
                "TX ${intent.txKind()}",
                detail,
                reason,
            )
        } catch (e: RuntimeException) {
            L.w(e, "Unable to send Topway broadcast")
            logJournalAndTimber(
                DiagnosticJournal.CAT_TOPWAY_BROADCAST,
                "TX failed",
                intent.action,
                e.javaClass.simpleName,
            )
        }
        if (isDoFunInstalled) {
            try {
                context.sendBroadcast(Intent(intent).setPackage(DOFUN_PACKAGE))
            } catch (e: SecurityException) {
                L.w(e, "Unable to send DoFun-targeted broadcast due to security policy")
            } catch (e: RuntimeException) {
                L.w(e, "Unable to send DoFun-targeted broadcast")
            }
        }
    }

    private fun logJournalAndTimber(
        category: String,
        event: String,
        detail: String? = null,
        result: String? = null,
    ) {
        journal.log(category, event, detail, result)
        L.i("Topway launcher bridge: $category/$event detail=$detail result=$result")
    }

    private fun Intent.txKind(): String =
        when (action) {
            TopwayMusicContract.ACTION_MUSIC_INFO -> "metadata"
            TopwayMusicContract.ACTION_PROGRESS_DURATION -> "progress"
            else -> action.orEmpty()
        }

    private fun HeadUnitMetadataSnapshot.safeSummary(): String =
        "titleLen=${displayTitle.length} artistLen=${artist.length} duration=$durationMs path=${mediaUri.isNotBlank()}"

    companion object {
        const val PREF_SEEK_UNIT = "auxio_ts18_launcher_seek_unit_policy"
        private const val DOFUN_PACKAGE = "com.dofun.variety"
        private const val MIN_PROGRESS_INTERVAL_MS = 1000L
    }
}
