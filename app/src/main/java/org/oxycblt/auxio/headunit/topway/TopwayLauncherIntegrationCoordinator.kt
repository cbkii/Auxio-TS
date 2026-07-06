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
        if (!canBroadcast("metadata:$reason")) return
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
        if (!canBroadcast("progress:$reason")) return
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
        journal.log(
            DiagnosticJournal.CAT_TOPWAY_CMD,
            "RX",
            "action=${intent.action} cmd=${extras.cmd} seek=${extras.widgetProgress}",
        )
        if (!mode.handlesTopwayCommands) {
            logSuppressed("command:${intent.action}")
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
            journal.log(
                DiagnosticJournal.CAT_TOPWAY_CMD,
                "Seek interpreted",
                decision.detail,
                decision.unit.name,
            )
            decision.positionMs?.let { callbacks.seekTo(it) } ?: callbacks.ignore()
            return true
        }
        when (TopwayMusicCommandMapper.map(intent.action, extras.cmd)) {
            TopwayMappedCommand.PREV -> callbacks.previous()
            TopwayMappedCommand.NEXT -> callbacks.next()
            TopwayMappedCommand.PLAY_PAUSE -> callbacks.playPause()
            TopwayMappedCommand.UPDATE -> callbacks.widgetUpdate()
            TopwayMappedCommand.UNKNOWN -> callbacks.ignore()
        }
        return true
    }

    private fun canBroadcast(reason: String): Boolean {
        if (mode.sendsTopwayBroadcasts) return true
        logSuppressed(reason)
        return false
    }

    private fun logSuppressed(reason: String) {
        journal.log(DiagnosticJournal.CAT_TOPWAY_BROADCAST, "Suppressed", reason, mode.name)
    }

    private fun sendTopwayBroadcast(intent: Intent, reason: String, detail: String?) {
        if (mode.diagnosticsOnly) {
            logSuppressed(reason)
            return
        }
        try {
            context.sendBroadcast(Intent(intent))
            journal.log(
                DiagnosticJournal.CAT_TOPWAY_BROADCAST,
                "TX ${intent.action}",
                detail,
                reason,
            )
        } catch (e: RuntimeException) {
            L.w(e, "Unable to send Topway broadcast")
            journal.log(
                DiagnosticJournal.CAT_TOPWAY_BROADCAST,
                "TX failed",
                intent.action,
                e.javaClass.simpleName,
            )
        }
        if (context.packageManager.getLaunchIntentForPackage(DOFUN_PACKAGE) != null) {
            try {
                context.sendBroadcast(Intent(intent).setPackage(DOFUN_PACKAGE))
            } catch (e: RuntimeException) {
                L.w(e, "Unable to send DoFun-targeted broadcast")
            }
        }
    }

    private fun HeadUnitMetadataSnapshot.safeSummary(): String =
        "title=${displayTitle.take(48)} artist=${artist.take(48)} duration=$durationMs path=${mediaUri.isNotBlank()}"

    companion object {
        const val PREF_SEEK_UNIT = "auxio_ts18_launcher_seek_unit_policy"
        private const val DOFUN_PACKAGE = "com.dofun.variety"
        private const val MIN_PROGRESS_INTERVAL_MS = 1000L
    }
}
