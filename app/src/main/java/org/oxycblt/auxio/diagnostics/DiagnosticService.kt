/*
 * Copyright (c) 2026 Auxio Project
 * DiagnosticService.kt is part of Auxio.
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

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.R
import timber.log.Timber as L

@AndroidEntryPoint
class DiagnosticService : Service() {

    @Inject lateinit var repository: DiagnosticsRepository
    @Inject lateinit var journal: DiagnosticJournal
    @Inject lateinit var markerController: DiagnosticMarkerController
    @Inject lateinit var diagnosticsSettings: DiagnosticsSettings

    private val timeoutHandler = Handler(Looper.getMainLooper())
    private var receiverRegistered = false
    private var activeSessionId: String? = null
    private var activeOrigin: String = ORIGIN_MANUAL
    private var activeDurationMs: Long = MAX_CAPTURE_DURATION_MS
    private val timeoutRunnable = Runnable {
        val sessionId = activeSessionId
        journal.log(
            DiagnosticJournal.CAT_SYSTEM,
            "Capture timed out",
            "Session: $sessionId, durationMs=$activeDurationMs",
        )
        stopCapture(sessionId, "timeout")
        stopSelfCleanly()
    }

    private val packageReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) = handleEventIntent(intent)
        }

    private val storageReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) = handleEventIntent(intent)
        }

    private fun handleEventIntent(intent: Intent?) {
        val action = intent?.action ?: return
        journal.log(DiagnosticJournal.CAT_SYSTEM, "System Intent", action)
        if (
            action == Intent.ACTION_MEDIA_MOUNTED ||
                action == Intent.ACTION_MEDIA_UNMOUNTED ||
                action == Intent.ACTION_MEDIA_EJECT
        ) {
            journal.log(
                DiagnosticJournal.CAT_STORAGE,
                "Media Event",
                "Action: $action, Data: ${intent.data}",
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        L.d("DiagnosticService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action == ACTION_STOP) {
            stopCapture(intent.getStringExtra(EXTRA_SESSION_ID), "user stop")
            stopSelfCleanly()
            return START_NOT_STICKY
        }
        if (action != ACTION_START_CAPTURE) {
            stopSelfCleanly()
            return START_NOT_STICKY
        }

        val sessionId = intent.getStringExtra(EXTRA_SESSION_ID) ?: return START_NOT_STICKY
        val requestedDurationMs =
            clampDurationMs(intent.getLongExtra(EXTRA_DURATION_MS, MAX_CAPTURE_DURATION_MS))
        val origin = intent.getStringExtra(EXTRA_ORIGIN) ?: ORIGIN_MANUAL

        if (!promoteForeground()) {
            markerController.restoreCurrentMetadata()
            stopSelfCleanly()
            return START_NOT_STICKY
        }

        if (!repository.startCapture(sessionId, origin, requestedDurationMs)) {
            markerController.restoreCurrentMetadata()
            stopSelfCleanly()
            return START_NOT_STICKY
        }
        activeSessionId = sessionId
        activeOrigin = origin
        activeDurationMs = requestedDurationMs
        if (origin == ORIGIN_BOOT || origin == ORIGIN_APP_START_FALLBACK) {
            diagnosticsSettings.clearArmedCapture()
        }
        journal.log(
            DiagnosticJournal.CAT_SYSTEM,
            "Capture Service Started",
            "Session: $sessionId, origin=$origin, durationMs=$requestedDurationMs",
        )

        registerEventReceiversOnce()
        timeoutHandler.removeCallbacks(timeoutRunnable)
        timeoutHandler.postDelayed(timeoutRunnable, requestedDurationMs)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        timeoutHandler.removeCallbacks(timeoutRunnable)
        unregisterEventReceivers()
        stopCapture(activeSessionId, "service destroyed")
        markerController.restoreCurrentMetadata()
        L.d("DiagnosticService destroyed")
        super.onDestroy()
    }

    private fun stopCapture(sessionId: String?, reason: String) {
        if (sessionId == null) return
        if (repository.isCaptureActive.value) {
            journal.log(DiagnosticJournal.CAT_SYSTEM, "Capture Service Stopping", reason)
            repository.stopCapture(sessionId)
            journal.log(DiagnosticJournal.CAT_SYSTEM, "Capture Service Stopped", reason)
        }
        if (activeSessionId == sessionId) activeSessionId = null
    }

    private fun registerEventReceiversOnce() {
        if (receiverRegistered) return
        val packageFilter =
            IntentFilter().apply {
                addAction(Intent.ACTION_PACKAGE_ADDED)
                addAction(Intent.ACTION_PACKAGE_REMOVED)
                addDataScheme("package")
            }
        val storageFilter =
            IntentFilter().apply {
                addAction(Intent.ACTION_MEDIA_MOUNTED)
                addAction(Intent.ACTION_MEDIA_UNMOUNTED)
                addAction(Intent.ACTION_MEDIA_EJECT)
                addDataScheme("file")
            }
        ContextCompat.registerReceiver(
            this,
            packageReceiver,
            packageFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        ContextCompat.registerReceiver(
            this,
            storageReceiver,
            storageFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        receiverRegistered = true
    }

    private fun unregisterEventReceivers() {
        if (!receiverRegistered) return
        try {
            unregisterReceiver(packageReceiver)
        } catch (e: Exception) {
            L.w(e, "Failed to unregister diagnostic package receiver")
        }
        try {
            unregisterReceiver(storageReceiver)
        } catch (e: Exception) {
            L.w(e, "Failed to unregister diagnostic storage receiver")
        }
        receiverRegistered = false
    }

    private fun promoteForeground(): Boolean {
        val nm = NotificationManagerCompat.from(this)
        val channel =
            NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName("TS18 Diagnostics")
                .setShowBadge(false)
                .build()
        nm.createNotificationChannel(channel)

        val stopIntent = Intent(this, DiagnosticService::class.java).setAction(ACTION_STOP)
        val stopPendingIntent =
            android.app.PendingIntent.getService(
                this,
                0,
                stopIntent,
                android.app.PendingIntent.FLAG_IMMUTABLE,
            )

        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_auxio_24)
                .setContentTitle("Auxio-TS Diagnostics Active")
                .setContentText("Recording integration events...")
                .setOngoing(true)
                .addAction(0, "Stop", stopPendingIntent)
                .build()

        return try {
            ServiceCompat.startForeground(
                this,
                NOTIFICATION_ID,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                } else {
                    0
                },
            )
            true
        } catch (e: Exception) {
            L.e(e, "Failed to promote DiagnosticService to foreground")
            false
        }
    }

    private fun stopSelfCleanly() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    companion object {
        private const val CHANNEL_ID = "auxio_diagnostics_channel"
        private const val NOTIFICATION_ID = 101
        private const val MAX_CAPTURE_DURATION_MS = 15 * 60 * 1000L

        const val ACTION_START_CAPTURE = BuildConfig.APPLICATION_ID + ".diagnostics.START_CAPTURE"
        const val ACTION_STOP = BuildConfig.APPLICATION_ID + ".diagnostics.STOP"
        const val EXTRA_SESSION_ID = "extra_session_id"

        const val EXTRA_DURATION_MS = "extra_duration_ms"
        const val EXTRA_ORIGIN = "extra_origin"
        const val ORIGIN_MANUAL = "manual"
        const val ORIGIN_GUIDED = "guided"
        const val ORIGIN_TIMED = "timed"
        const val ORIGIN_BOOT = "boot"
        const val ORIGIN_APP_START_FALLBACK = "app_start_fallback"

        fun clampDurationMs(durationMs: Long): Long =
            durationMs.coerceIn(1_000L, MAX_CAPTURE_DURATION_MS)

        fun start(
            context: Context,
            sessionId: String,
            durationMs: Long = MAX_CAPTURE_DURATION_MS,
            origin: String = ORIGIN_MANUAL,
        ) {
            val intent =
                Intent(context, DiagnosticService::class.java).apply {
                    action = ACTION_START_CAPTURE
                    putExtra(EXTRA_SESSION_ID, sessionId)
                    putExtra(EXTRA_DURATION_MS, clampDurationMs(durationMs))
                    putExtra(EXTRA_ORIGIN, origin)
                }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context, sessionId: String? = null) {
            val intent =
                Intent(context, DiagnosticService::class.java).apply {
                    action = ACTION_STOP
                    sessionId?.let { putExtra(EXTRA_SESSION_ID, it) }
                }
            context.startService(intent)
        }
    }
}
