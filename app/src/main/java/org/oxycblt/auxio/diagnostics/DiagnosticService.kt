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

    private val timeoutHandler = Handler(Looper.getMainLooper())
    private var receiverRegistered = false
    private val timeoutRunnable = Runnable {
        journal.log(DiagnosticJournal.CAT_SYSTEM, "Capture timed out", "15 minute limit reached")
        stopCapture()
        stopSelfCleanly()
    }

    private val eventReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
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
        }

    override fun onCreate() {
        super.onCreate()
        L.d("DiagnosticService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null || intent.action == ACTION_STOP) {
            stopCapture()
            stopSelfCleanly()
            return START_NOT_STICKY
        }

        val sessionId = intent.getStringExtra(EXTRA_SESSION_ID) ?: "default"

        if (!promoteForeground()) {
            stopSelfCleanly()
            return START_NOT_STICKY
        }

        if (intent.action == ACTION_START_CAPTURE) {
            if (!repository.startCapture(sessionId)) {
                stopSelfCleanly()
                return START_NOT_STICKY
            }
            journal.log(
                DiagnosticJournal.CAT_SYSTEM,
                "Capture Service Started",
                "Session: $sessionId",
            )

            registerEventReceiverOnce()
            timeoutHandler.removeCallbacks(timeoutRunnable)
            timeoutHandler.postDelayed(timeoutRunnable, MAX_CAPTURE_DURATION_MS)
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        timeoutHandler.removeCallbacks(timeoutRunnable)
        if (receiverRegistered) {
            repeat(2) {
                try {
                    unregisterReceiver(eventReceiver)
                } catch (e: Exception) {
                    L.w(e, "Failed to unregister diagnostic event receiver")
                }
            }
            receiverRegistered = false
        }
        stopCapture()
        L.d("DiagnosticService destroyed")
        super.onDestroy()
    }

    private fun stopCapture() {
        if (repository.isCaptureActive.value) {
            repository.stopCapture()
            journal.log(DiagnosticJournal.CAT_SYSTEM, "Capture Service Stopped")
        }
    }

    private fun registerEventReceiverOnce() {
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
            eventReceiver,
            packageFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        ContextCompat.registerReceiver(
            this,
            eventReceiver,
            storageFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        receiverRegistered = true
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

        fun start(context: Context, sessionId: String) {
            val intent =
                Intent(context, DiagnosticService::class.java).apply {
                    action = ACTION_START_CAPTURE
                    putExtra(EXTRA_SESSION_ID, sessionId)
                }
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            val intent =
                Intent(context, DiagnosticService::class.java).apply { action = ACTION_STOP }
            context.startService(intent)
        }
    }
}
