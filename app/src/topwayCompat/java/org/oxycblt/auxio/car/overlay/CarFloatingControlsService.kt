/*
 * Copyright (c) 2024 Auxio Project
 * CarFloatingControlsService.kt is part of Auxio.
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

package org.oxycblt.auxio.car.overlay

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.Keep
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import org.oxycblt.auxio.BuildConfig
import org.oxycblt.auxio.MainActivity
import org.oxycblt.auxio.R
import org.oxycblt.auxio.diagnostics.DiagnosticJournal
import org.oxycblt.auxio.headunit.overlay.CarOverlayContract
import org.oxycblt.auxio.playback.service.PlaybackActions
import timber.log.Timber as L

/**
 * Foreground service managing the car floating controls overlay window. Handles overlay lifecycle,
 * drag persistence, and visibility toggling based on Auxio foreground state.
 *
 * Playback commands are dispatched via Auxio's own broadcast actions ([PlaybackActions]), which
 * route deterministically to [org.oxycblt.auxio.playback.service.SystemPlaybackReceiver] and then
 * to [org.oxycblt.auxio.playback.state.PlaybackStateManager]. This is the same path used by
 * notifications and widgets -- not generic media key dispatch.
 */
@AndroidEntryPoint
@Keep
class CarFloatingControlsService : Service(), CarFloatingControlsView.Callbacks {

    @Inject lateinit var journal: DiagnosticJournal

    private lateinit var prefs: CarOverlayPrefs
    private var overlayView: CarFloatingControlsView? = null
    private var windowManager: WindowManager? = null
    private var isOverlayAttached = false
    private var isAuxioForeground = false
    private var isForegroundPromoted = false
    private var screenOnReceiverRegistered = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private var attachRetryCount = 0
    private val attachRetryRunnable = Runnable {
        if (prefs.enabled && Settings.canDrawOverlays(this) && !isOverlayAttached) {
            showOverlayIfAllowed()
        }
    }
    private val screenOnReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_SCREEN_ON) {
                    L.d("Dynamic SCREEN_ON received for running overlay service")
                    restoreIfEnabled(this@CarFloatingControlsService, "dynamic:SCREEN_ON")
                }
            }
        }

    override fun onCreate() {
        super.onCreate()
        OverlayLifecycleJournal.init(this)
        prefs = CarOverlayPrefs.from(this)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        isServiceCreated = true
        OverlayLifecycleJournal.log(
            "service_create",
            prefs.enabled,
            Settings.canDrawOverlays(this),
            CarOverlayVisibilityHooks.isSuppressedByAuxioForeground,
            isServiceCreated,
            "Created",
        )
        L.d("CarFloatingControlsService created")
        journal.log(DiagnosticJournal.CAT_OVERLAY, "Service created")
        registerScreenOnReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) {
            // Null intent: system restarted the service after process death.
            // Re-establish the overlay if still enabled and permitted; otherwise stop cleanly.
            if (!prefs.enabled || !Settings.canDrawOverlays(this)) {
                stopSelfCleanly()
                return START_NOT_STICKY
            }
            journal.log(
                DiagnosticJournal.CAT_OVERLAY,
                "Sticky restart restore",
                "reason=null_intent",
            )
            // A null-intent sticky restart is still subject to Android's foreground-service
            // promotion deadline. Promote before applying optional window suppression.
            if (!promoteForeground()) {
                stopSelfCleanly()
                return START_NOT_STICKY
            }
            isForegroundPromoted = true
            if (!shouldSuppressForForegroundPreference()) startOverlayRuntime()
            return START_STICKY
        }

        if (intent.action == ACTION_STOP) {
            stopOverlayRuntime()
            stopSelfCleanly()
            return START_NOT_STICKY
        }

        if (!isForegroundPromoted) {
            if (!prefs.enabled || !Settings.canDrawOverlays(this)) {
                stopSelfCleanly()
                return START_NOT_STICKY
            }
            if (!promoteForeground()) {
                stopSelfCleanly()
                return START_NOT_STICKY
            }
            isForegroundPromoted = true
        }

        when (intent.action) {
            ACTION_START -> {
                journal.log(
                    DiagnosticJournal.CAT_OVERLAY,
                    "Overlay restore requested",
                    intent.getStringExtra(EXTRA_START_REASON) ?: "unspecified",
                )
                startOverlayRuntime()
            }
            ACTION_SHOW -> showOverlayIfAllowed()
            ACTION_HIDE -> hideOverlay()
            ACTION_TOGGLE -> {
                if (isOverlayAttached) hideOverlay() else showOverlayIfAllowed()
            }
            ACTION_RESET_POSITION -> {
                // Only reposition a live overlay. Position prefs are already updated by caller.
                if (isOverlayAttached) {
                    val bounds = fullDisplayBounds()
                    val resolved = resolveInitialPosition(overlaySize(overlayView, bounds), bounds)
                    updateOverlayPosition(resolved.x, resolved.y)
                } else {
                    L.d("Ignoring reset-position command with no live overlay")
                    stopSelfCleanly()
                }
            }
            ACTION_AUXIO_FOREGROUND_CHANGED -> {
                isAuxioForeground = intent.getBooleanExtra(EXTRA_AUXIO_FOREGROUND, false)
                CarOverlayVisibilityHooks.isSuppressedByAuxioForeground =
                    isAuxioForeground && prefs.hideWhileAuxioForeground
                if (shouldSuppressForForegroundPreference()) {
                    hideOverlay()
                } else if (!isAuxioForeground && prefs.enabled) {
                    startOverlayRuntime()
                }
            }
            else -> {
                L.w("Unknown action: ${intent.action}, stopping idle service")
                stopSelfCleanly()
            }
        }
        return restartMode()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        mainHandler.removeCallbacks(attachRetryRunnable)
        unregisterScreenOnReceiver()
        removeOverlay()
        isServiceCreated = false
        isOverlayRuntimeAttached = false
        OverlayLifecycleJournal.log(
            "service_destroy",
            prefs.enabled,
            Settings.canDrawOverlays(this),
            CarOverlayVisibilityHooks.isSuppressedByAuxioForeground,
            isServiceCreated,
            "Destroyed",
        )
        L.d("CarFloatingControlsService destroyed")
        super.onDestroy()
    }

    // --- Overlay lifecycle ---

    private fun startOverlayRuntime() {
        if (!isForegroundPromoted) {
            if (!promoteForeground()) {
                // Foreground promotion failed — stop cleanly.
                stopSelfCleanly()
                return
            }
            isForegroundPromoted = true
        }
        if (!isOverlayAttached) {
            mainHandler.removeCallbacks(attachRetryRunnable)
            attachRetryCount = 0
            showOverlayIfAllowed()
        }
    }

    private fun stopOverlayRuntime() {
        removeOverlay()
    }

    private fun registerScreenOnReceiver() {
        if (screenOnReceiverRegistered) return
        try {
            registerReceiver(screenOnReceiver, IntentFilter(Intent.ACTION_SCREEN_ON))
            screenOnReceiverRegistered = true
        } catch (e: RuntimeException) {
            L.w(e, "Unable to register dynamic SCREEN_ON overlay receiver")
        }
    }

    private fun unregisterScreenOnReceiver() {
        if (!screenOnReceiverRegistered) return
        try {
            unregisterReceiver(screenOnReceiver)
        } catch (e: RuntimeException) {
            L.w(e, "Unable to unregister dynamic SCREEN_ON overlay receiver")
        } finally {
            screenOnReceiverRegistered = false
        }
    }

    private fun shouldSuppressForForegroundPreference(): Boolean =
        prefs.hideWhileAuxioForeground &&
            (isAuxioForeground || CarOverlayVisibilityHooks.isSuppressedByAuxioForeground)

    private fun restartMode(): Int =
        if (prefs.enabled && Settings.canDrawOverlays(this)) START_STICKY else START_NOT_STICKY

    private fun showOverlayIfAllowed() {
        if (!prefs.enabled) {
            L.d("Cannot show overlay: disabled, stopping")
            removeOverlay()
            stopSelfCleanly()
            return
        }
        if (!Settings.canDrawOverlays(this)) {
            L.w("Cannot show overlay: permission revoked, stopping")
            removeOverlay()
            stopSelfCleanly()
            return
        }
        if (isOverlayAttached) return
        if (shouldSuppressForForegroundPreference()) return

        journal.log(DiagnosticJournal.CAT_OVERLAY, "Attaching overlay")
        val view = CarFloatingControlsView(this, this)
        view.applyOpacity(prefs.opacityPercent)

        val params = createLayoutParams()
        val bounds = fullDisplayBounds()
        val resolved = resolveInitialPosition(overlaySize(view, bounds), bounds)
        params.x = resolved.x
        params.y = resolved.y

        if (resolved.shouldPersist) {
            prefs.positionX = resolved.x
            prefs.positionY = resolved.y
        }

        try {
            windowManager?.addView(view, params)
        } catch (e: Exception) {
            L.e(e, "Failed to add overlay view")
            journal.log(
                DiagnosticJournal.CAT_OVERLAY,
                "Overlay attach failed",
                "attempt=$attachRetryCount error=${e.javaClass.simpleName}",
            )
            if (attachRetryCount < MAX_ATTACH_RETRIES) {
                attachRetryCount++
                mainHandler.removeCallbacks(attachRetryRunnable)
                mainHandler.postDelayed(attachRetryRunnable, ATTACH_RETRY_DELAY_MS)
            } else {
                stopSelfCleanly()
            }
            return
        }
        mainHandler.removeCallbacks(attachRetryRunnable)
        attachRetryCount = 0
        overlayView = view
        isOverlayAttached = true
        isOverlayRuntimeAttached = true
        L.d("Overlay attached at (${resolved.x}, ${resolved.y})")
    }

    private fun hideOverlay() {
        if (!isOverlayAttached) return
        removeOverlay()
    }

    private fun removeOverlay() {
        overlayView?.let { view ->
            try {
                windowManager?.removeView(view)
            } catch (e: IllegalArgumentException) {
                L.w("View already removed from window")
            } catch (e: Exception) {
                L.w(e, "Unexpected error removing overlay view")
            }
        }
        overlayView = null
        isOverlayAttached = false
        isOverlayRuntimeAttached = false
    }

    private fun updateOverlayPosition(x: Int, y: Int) {
        val view = overlayView ?: return
        val params = view.layoutParams as? WindowManager.LayoutParams ?: return
        params.x = x
        params.y = y
        try {
            windowManager?.updateViewLayout(view, params)
        } catch (e: IllegalArgumentException) {
            L.w("Cannot update layout: view not attached")
        } catch (e: Exception) {
            L.w(e, "Unexpected error updating overlay position")
        }
    }

    private fun createLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT,
            )
            .apply {
                gravity = Gravity.TOP or Gravity.START
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }
    }

    // --- Position clamping ---

    /**
     * Resolves the persisted or default overlay position against full-screen display bounds. New
     * installs, explicit reset-position requests, and the exact legacy below-status-bar default are
     * placed at top-centre with y=0. Deliberately dragged custom positions are preserved and only
     * clamped enough to avoid permanently losing the overlay off-screen.
     */
    private fun resolveInitialPosition(
        size: OverlaySize = OverlaySize(),
        bounds: Rect = fullDisplayBounds(),
    ): InitialPosition {
        if (prefs.hasOldDefaultPosition) {
            L.d("Migrating from legacy default position (437, 55)")
            prefs.resetPosition()
            val (dx, dy) = defaultTopCenterPosition(bounds, size)
            return InitialPosition(dx, dy, shouldPersist = false)
        }

        if (!prefs.hasSavedPosition) {
            val (dx, dy) = defaultTopCenterPosition(bounds, size)
            return InitialPosition(dx, dy, shouldPersist = false)
        }

        val sx = prefs.positionX
        val sy = prefs.positionY
        val (cx, cy) = clampPosition(sx, sy, bounds, size)

        return InitialPosition(cx, cy, shouldPersist = (cx != sx || cy != sy))
    }

    @Suppress("DEPRECATION")
    private fun fullDisplayBounds(): Rect {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val metrics = windowManager?.maximumWindowMetrics ?: windowManager?.currentWindowMetrics
            return metrics?.bounds ?: Rect(0, 0, DEFAULT_SCREEN_WIDTH, DEFAULT_SCREEN_HEIGHT)
        }

        val display = windowManager?.defaultDisplay
        val size = Point()
        display?.getRealSize(size)
        val width = if (size.x > 0) size.x else DEFAULT_SCREEN_WIDTH
        val height = if (size.y > 0) size.y else DEFAULT_SCREEN_HEIGHT
        return Rect(0, 0, width, height)
    }

    private fun defaultTopCenterPosition(bounds: Rect, size: OverlaySize): Pair<Int, Int> {
        val screenW = bounds.width().takeIf { it > 0 } ?: DEFAULT_SCREEN_WIDTH
        val x = ((screenW - size.width) / 2).coerceAtLeast(0)
        return clampPosition(x, DEFAULT_TOP_EDGE_Y, bounds, size)
    }

    /**
     * Clamp against the full physical display, not the app usable area below system bars. Public
     * overlay windows still remain below critical system windows in z-order on stock Android, but
     * y=0 plus full-screen/no-limits layout flags requests the maximum public-API top-edge extent.
     */
    private fun clampPosition(
        x: Int,
        y: Int,
        bounds: Rect = fullDisplayBounds(),
        size: OverlaySize = OverlaySize(),
    ): Pair<Int, Int> {
        val screenW = bounds.width().takeIf { it > 0 } ?: DEFAULT_SCREEN_WIDTH
        val screenH = bounds.height().takeIf { it > 0 } ?: DEFAULT_SCREEN_HEIGHT
        val minX = 0
        val minY = DEFAULT_TOP_EDGE_Y
        val maxX = (screenW - size.width).coerceAtLeast(minX)
        val maxY = (screenH - size.height).coerceAtLeast(minY)

        return x.coerceIn(minX, maxX) to y.coerceIn(minY, maxY)
    }

    private fun overlaySize(view: View?, bounds: Rect): OverlaySize {
        val width = view?.width?.takeIf { it > 0 }
        val height = view?.height?.takeIf { it > 0 }
        if (width != null && height != null) {
            return OverlaySize(width, height)
        }

        view?.measure(
            View.MeasureSpec.makeMeasureSpec(
                bounds.width().coerceAtLeast(0),
                View.MeasureSpec.AT_MOST,
            ),
            View.MeasureSpec.makeMeasureSpec(
                bounds.height().coerceAtLeast(0),
                View.MeasureSpec.AT_MOST,
            ),
        )
        return OverlaySize(
            width = view?.measuredWidth?.takeIf { it > 0 } ?: OVERLAY_ESTIMATED_WIDTH_PX,
            height = view?.measuredHeight?.takeIf { it > 0 } ?: OVERLAY_ESTIMATED_HEIGHT_PX,
        )
    }

    private data class OverlaySize(
        val width: Int = OVERLAY_ESTIMATED_WIDTH_PX,
        val height: Int = OVERLAY_ESTIMATED_HEIGHT_PX,
    )

    private data class InitialPosition(val x: Int, val y: Int, val shouldPersist: Boolean)

    // --- Foreground notification ---

    /**
     * Promotes the service to foreground. Returns true on success, false if foreground promotion
     * failed (e.g., on Android 10 where specialUse is not supported but manifest declares it).
     */
    private fun promoteForeground(): Boolean {
        val nm = NotificationManagerCompat.from(this)
        val channel =
            NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName(getString(R.string.car_overlay_notification_channel))
                .setShowBadge(false)
                .setLightsEnabled(false)
                .setVibrationEnabled(false)
                .build()
        nm.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_auxio_24)
                .setContentTitle(getString(R.string.car_overlay_notification_title))
                .setContentText(getString(R.string.car_overlay_notification_text))
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

        return try {
            val serviceType = foregroundServiceType()
            ServiceCompat.startForeground(this, NOTIFICATION_ID, notification, serviceType)
            true
        } catch (e: IllegalStateException) {
            L.e(e, "Failed to promote to foreground (IllegalState)")
            false
        } catch (e: SecurityException) {
            L.e(e, "Failed to promote to foreground (Security)")
            false
        } catch (e: Exception) {
            L.e(e, "Failed to promote to foreground (unexpected)")
            false
        }
    }

    private fun stopSelfCleanly() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    // --- CarFloatingControlsView.Callbacks ---

    override fun onDrag(deltaX: Int, deltaY: Int) {
        val view = overlayView ?: return
        val params = view.layoutParams as? WindowManager.LayoutParams ?: return
        params.x += deltaX
        params.y += deltaY
        try {
            windowManager?.updateViewLayout(view, params)
        } catch (e: IllegalArgumentException) {
            L.w("Cannot update layout during drag")
        }
    }

    override fun onDragFinished(x: Int, y: Int) {
        val view = overlayView ?: return
        val params = view.layoutParams as? WindowManager.LayoutParams ?: return
        val bounds = fullDisplayBounds()
        val (cx, cy) =
            clampPosition(params.x, params.y, bounds = bounds, size = overlaySize(view, bounds))
        params.x = cx
        params.y = cy
        try {
            windowManager?.updateViewLayout(view, params)
        } catch (_: IllegalArgumentException) {
            // View may have been detached between clamp and update — safe to ignore.
        }
        prefs.positionX = cx
        prefs.positionY = cy
    }

    override fun onPrevious() {
        sendPlaybackBroadcast(PlaybackActions.ACTION_SKIP_PREV)
    }

    override fun onPlayPause() {
        sendPlaybackBroadcast(PlaybackActions.ACTION_PLAY_PAUSE)
    }

    override fun onNext() {
        sendPlaybackBroadcast(PlaybackActions.ACTION_SKIP_NEXT)
    }

    override fun onOpenAuxio() {
        // The Topway build deliberately has a routing launcher component. Never ask PackageManager
        // to choose between launcher entries; open the real app activity explicitly.
        startActivity(
            Intent(this, MainActivity::class.java)
                .setAction(org.oxycblt.auxio.headunit.HeadUnitEntryPoints.ACTION_OPEN_NOW_PLAYING)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        )
    }

    override fun onStopRequested() {
        CarOverlaySettings.setEnabled(this, false)
        L.d("Stop requested via triple-tap")
        stopOverlayRuntime()
        stopSelfCleanly()
    }

    /**
     * Dispatches a playback command via Auxio's own broadcast actions. These route to
     * [org.oxycblt.auxio.playback.service.SystemPlaybackReceiver] which is registered with an
     * exported intent filter and dispatches directly to PlaybackStateManager.
     */
    private fun sendPlaybackBroadcast(action: String) {
        sendBroadcast(Intent(action).apply { setPackage(packageName) })
    }

    companion object {
        private const val CHANNEL_ID = "auxio_car_overlay_channel"
        private const val NOTIFICATION_ID = 42

        // TS18 fallback display dimensions used only when Android cannot report real metrics.
        private const val DEFAULT_SCREEN_WIDTH = 1280
        private const val DEFAULT_SCREEN_HEIGHT = 720
        private const val DEFAULT_TOP_EDGE_Y = 0
        private const val OVERLAY_ESTIMATED_WIDTH_PX = 350
        private const val OVERLAY_ESTIMATED_HEIGHT_PX = 80
        private const val MAX_ATTACH_RETRIES = 2
        private const val ATTACH_RETRY_DELAY_MS = 750L

        @Volatile private var isServiceCreated = false
        @Volatile private var isOverlayRuntimeAttached = false

        val ACTION_START: String = CarOverlayContract.ACTION_START
        const val ACTION_STOP = BuildConfig.APPLICATION_ID + ".car.overlay.STOP"
        const val ACTION_SHOW = BuildConfig.APPLICATION_ID + ".car.overlay.SHOW"
        const val ACTION_HIDE = BuildConfig.APPLICATION_ID + ".car.overlay.HIDE"
        const val ACTION_TOGGLE = BuildConfig.APPLICATION_ID + ".car.overlay.TOGGLE"
        const val ACTION_RESET_POSITION = BuildConfig.APPLICATION_ID + ".car.overlay.RESET_POSITION"
        const val ACTION_AUXIO_FOREGROUND_CHANGED =
            BuildConfig.APPLICATION_ID + ".car.overlay.AUXIO_FG_CHANGED"
        const val EXTRA_AUXIO_FOREGROUND = "extra_auxio_foreground"
        const val EXTRA_START_REASON = CarOverlayContract.EXTRA_START_REASON

        /**
         * Returns the appropriate foreground service type for the current API level.
         * `FOREGROUND_SERVICE_TYPE_SPECIAL_USE` is only valid on API 34+ (Upside Down Cake). On
         * older APIs, use 0 (none) which is the legacy behaviour and is accepted by ServiceCompat.
         */
        fun foregroundServiceType(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }
        }

        fun restoreIfEnabled(context: Context, reason: String) {
            val prefs =
                try {
                    CarOverlayPrefs.from(context)
                } catch (e: RuntimeException) {
                    L.w(e, "Cannot restore overlay: preferences unavailable")
                    return
                }
            if (!prefs.enabled) {
                L.d("Skipping overlay restore; disabled [$reason]")
                return
            }
            if (!Settings.canDrawOverlays(context)) {
                L.w("Skipping overlay restore; permission missing [$reason]")
                return
            }
            if (clearsForegroundSuppression(reason)) {
                CarOverlayVisibilityHooks.isSuppressedByAuxioForeground = false
            }
            // Always start the foreground service when the feature is enabled. Optional
            // hide-over-Auxio affects only the overlay window; it must not prevent the persistent
            // service from existing and later reattaching the controls.
            start(context, reason)
        }

        private fun clearsForegroundSuppression(reason: String): Boolean =
            reason.contains("BOOT_COMPLETED") ||
                reason.contains("QUICKBOOT_POWERON") ||
                reason.contains("USER_UNLOCKED") ||
                reason.contains("MY_PACKAGE_REPLACED")

        fun start(context: Context) {
            start(context, "explicit")
        }

        fun start(context: Context, reason: String) {
            if (!Settings.canDrawOverlays(context)) return
            val intent = Intent(context, CarFloatingControlsService::class.java)
            intent.action = ACTION_START
            intent.putExtra(EXTRA_START_REASON, reason)
            try {
                ContextCompat.startForegroundService(context, intent)
            } catch (e: IllegalStateException) {
                L.w(e, "Cannot start overlay service: IllegalStateException")
            } catch (e: SecurityException) {
                L.w(e, "Cannot start overlay service: SecurityException")
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, CarFloatingControlsService::class.java))
        }

        /**
         * Signals foreground/background changes. Foreground transitions only need to hide an
         * existing overlay, so they never cold-start the service. Background transitions use the
         * foreground-service path and [onStartCommand] promotes the service or stops it promptly if
         * the signal leaves no live overlay.
         */
        fun setAuxioForeground(context: Context, isForeground: Boolean) {
            val prefs = CarOverlayPrefs.from(context)
            if (!prefs.enabled) return
            if (!Settings.canDrawOverlays(context)) return
            if (isForeground && !isServiceCreated) return
            val intent = Intent(context, CarFloatingControlsService::class.java)
            intent.action = ACTION_AUXIO_FOREGROUND_CHANGED
            intent.putExtra(EXTRA_AUXIO_FOREGROUND, isForeground)
            try {
                ContextCompat.startForegroundService(context, intent)
            } catch (e: IllegalStateException) {
                L.d("Cannot signal foreground change: service not running/background")
            } catch (e: SecurityException) {
                L.d("Cannot signal foreground change: security policy")
            }
        }

        /**
         * Sends a position-reset command only to a known live overlay. Position prefs are already
         * updated by the caller; if no overlay is attached, the next overlay show will use prefs.
         * If a reset signal is delivered after the overlay detaches, [onStartCommand] stops the
         * foreground service promptly.
         */
        fun resetPositionIfRunning(context: Context) {
            val prefs = CarOverlayPrefs.from(context)
            val overlayLive =
                CarOverlaySettingsPolicy.overlayLive(
                    serviceCreated = isServiceCreated,
                    overlayAttached = isOverlayRuntimeAttached,
                )
            if (
                !CarOverlaySettingsPolicy.shouldSignalResetToService(
                    enabled = prefs.enabled,
                    hasOverlayPermission = Settings.canDrawOverlays(context),
                    overlayLive = overlayLive,
                )
            ) {
                return
            }
            val intent = Intent(context, CarFloatingControlsService::class.java)
            intent.action = ACTION_RESET_POSITION
            try {
                ContextCompat.startForegroundService(context, intent)
            } catch (e: IllegalStateException) {
                L.d("Cannot reset position: service not running/background")
            } catch (e: SecurityException) {
                L.d("Cannot reset position: security policy")
            }
        }
    }
}
