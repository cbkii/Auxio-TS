/*
 * Copyright (c) 2026 Auxio Project
 * MusicWidgetProvider.kt is part of Auxio.
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

package com.tw.music.view

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import com.tw.music.MusicService
import org.oxycblt.auxio.AuxioService
import org.oxycblt.auxio.IntegerTable
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.topway.TopwayBridgeExtrasPolicy
import org.oxycblt.auxio.headunit.topway.TopwayMusicContract
import org.oxycblt.auxio.headunit.topway.TopwayWidgetProviderPolicy
import org.oxycblt.auxio.playback.service.PendingIntentRequestCodePolicy
import org.oxycblt.auxio.ui.UISettings
import org.oxycblt.auxio.widgets.WidgetComponent
import org.oxycblt.auxio.widgets.WidgetTimeline
import timber.log.Timber as L

@SuppressLint("RemoteViewLayout")
class MusicWidgetProvider : AppWidgetProvider() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) {
            L.d("Ignoring null Topway widget/provider intent")
            return
        }

        val action = intent.action
        if (action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            // AppWidgetProvider.super.onReceive() dispatches this to onUpdate().
            super.onReceive(context, intent)
            return
        }

        super.onReceive(context, intent)

        if (TopwayMusicContract.isIncomingAction(action)) {
            forwardTopwayIntent(context, intent)
        } else {
            L.d("Ignoring unsupported Topway widget/provider action: $action")
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        L.d("onUpdate called for Topway widget with ${appWidgetIds.size} IDs")
        // Stock com.tw.music immediately gives the DoFun/AppWidget host a control-capable
        // RemoteViews layout, then asks MusicService for a full update with cmd=update and
        // appWidgetIds. Mirror that surface instead of relying only on Auxio's normal widget path,
        // which can be absent during cold launcher/widget binding.
        renderColdWidgetControls(context, appWidgetManager, appWidgetIds)
        startTopwayWidgetUpdateService(context, appWidgetIds)
    }

    private fun renderColdWidgetControls(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        val rv = RemoteViews(context.packageName, R.layout.app_widget_topway)
        val timeline = WidgetTimeline.NO_SESSION

        rv.setTextViewText(R.id.title, "")
        rv.setTextViewText(R.id.artist, "")
        rv.setTextViewText(R.id.tv_current_time, timeline.currentText)
        rv.setTextViewText(R.id.tv_duration, timeline.durationText)
        rv.setProgressBar(
            R.id.seek_bar_progress,
            timeline.maxSeconds,
            timeline.progressSeconds,
            false,
        )
        rv.setImageViewResource(R.id.albumart, R.drawable.ic_remote_default_cover_24)
        rv.setImageViewResource(R.id.control_play, R.drawable.ic_play_24)
        bindTopwayControls(context, rv)

        try {
            if (appWidgetIds.isNotEmpty()) {
                appWidgetManager.updateAppWidget(appWidgetIds, rv)
            } else {
                appWidgetManager.updateAppWidget(
                    ComponentName(context, MusicWidgetProvider::class.java),
                    rv,
                )
            }
        } catch (e: Exception) {
            L.w(e, "Unable to render cold Topway widget controls")
        }
    }

    @android.annotation.SuppressLint("MissingPermission")
    private fun startTopwayWidgetUpdateService(context: Context, appWidgetIds: IntArray) {
        @Suppress("DEPRECATION")
        try {
            val stickyIntent =
                Intent(TopwayMusicContract.ACTION_CMD).apply {
                    putExtra(TopwayMusicContract.EXTRA_CMD, TopwayMusicContract.CMD_UPDATE)
                    if (appWidgetIds.isNotEmpty()) putExtra(EXTRA_APP_WIDGET_IDS, appWidgetIds)
                    @android.annotation.SuppressLint("WrongConstant") addFlags(0x40000000.toInt())
                }
            context.sendStickyBroadcast(stickyIntent)
            L.d("Topway widget sticky cmd update sent")
        } catch (e: SecurityException) {
            L.w(e, "Topway widget sticky cmd update denied")
        } catch (e: RuntimeException) {
            L.w(e, "Topway widget sticky cmd update failed")
        }

        val serviceIntent =
            Intent(context, MusicService::class.java)
                .setAction(TopwayMusicContract.ACTION_CMD)
                .putExtra(TopwayMusicContract.EXTRA_CMD, TopwayMusicContract.CMD_UPDATE)
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_TOPWAY)

        if (appWidgetIds.isNotEmpty()) {
            serviceIntent.putExtra(EXTRA_APP_WIDGET_IDS, appWidgetIds)
        }

        try {
            ContextCompat.startForegroundService(context, serviceIntent)
        } catch (e: IllegalStateException) {
            L.w(e, "Unable to request Topway widget update due to service state")
        } catch (e: SecurityException) {
            L.w(e, "Unable to request Topway widget update due to security policy")
        }
    }

    /**
     * Update the currently shown layout based on the given [WidgetComponent.PlaybackState].
     *
     * @param context [Context] required to update the widget layout.
     * @param state [WidgetComponent.PlaybackState] to show, or null if no playback is going on.
     */
    @Suppress("UNUSED_PARAMETER")
    fun update(context: Context, uiSettings: UISettings, state: WidgetComponent.PlaybackState?) {
        val awm =
            try {
                AppWidgetManager.getInstance(context)
            } catch (e: Exception) {
                L.w(e, "Unable to get AppWidgetManager instance")
                null
            } ?: return

        val component = ComponentName(context, MusicWidgetProvider::class.java)

        if (!TopwayWidgetProviderPolicy.shouldHandleTopwayUpdate(context)) {
            L.d("Skipping Topway widget update: no active instances and not in Topway flavor")
            return
        }

        val views =
            if (state == null) {
                RemoteViews(context.packageName, R.layout.widget_default)
            } else {
                val rv = RemoteViews(context.packageName, R.layout.app_widget_topway)

                rv.setTextViewText(R.id.title, state.title)
                rv.setTextViewText(R.id.artist, state.artist)

                // Stock com.tw.music refuses large RemoteViews album bitmaps using a 3,680,000-byte
                // cap before falling back to the default album resource. Preserve that
                // launcher-safe
                // behaviour for DoFun/Topway widget hosts.
                if (
                    state.cover != null && state.cover.byteCount <= STOCK_WIDGET_ARTWORK_MAX_BYTES
                ) {
                    rv.setImageViewBitmap(R.id.albumart, state.cover)
                } else {
                    rv.setImageViewResource(R.id.albumart, R.drawable.ic_remote_default_cover_24)
                }

                val timeline = WidgetTimeline.state(state.positionMs, state.durationMs)
                rv.setTextViewText(R.id.tv_current_time, timeline.currentText)
                rv.setTextViewText(R.id.tv_duration, timeline.durationText)
                rv.setProgressBar(
                    R.id.seek_bar_progress,
                    timeline.maxSeconds,
                    timeline.progressSeconds,
                    false,
                )

                rv.setImageViewResource(
                    R.id.control_play,
                    if (state.isPlaying) R.drawable.ic_pause_24 else R.drawable.ic_play_24,
                )

                bindTopwayControls(context, rv)
                rv
            }

        try {
            awm.updateAppWidget(component, views)
            L.d("Successfully updated Topway RemoteViews layout")
        } catch (e: Exception) {
            L.w(e, "Unable to update Topway widget")
        }
    }

    private fun bindTopwayControls(context: Context, remoteViews: RemoteViews) {
        remoteViews.setOnClickPendingIntent(R.id.albumart, newActivityPendingIntent(context))
        remoteViews.setOnClickPendingIntent(
            R.id.control_prev,
            newServicePendingIntent(context, TopwayMusicContract.ACTION_PREV),
        )
        remoteViews.setOnClickPendingIntent(
            R.id.control_play,
            newServicePendingIntent(context, TopwayMusicContract.ACTION_PLAY_PAUSE),
        )
        remoteViews.setOnClickPendingIntent(
            R.id.control_next,
            newServicePendingIntent(context, TopwayMusicContract.ACTION_NEXT),
        )
    }

    private fun newActivityPendingIntent(context: Context): PendingIntent {
        val intent =
            Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(ComponentName(context.packageName, STOCK_MUSIC_ACTIVITY_CLASS))

        return PendingIntent.getActivity(
            context,
            PendingIntentRequestCodePolicy.forAction(STOCK_MUSIC_ACTIVITY_CLASS),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private fun newServicePendingIntent(context: Context, action: String): PendingIntent {
        val intent =
            Intent(context, MusicService::class.java)
                .setAction(action)
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_TOPWAY)

        return PendingIntent.getService(
            context,
            PendingIntentRequestCodePolicy.forAction(action),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private fun forwardTopwayIntent(context: Context, intent: Intent?) {
        val incomingAction = intent?.action
        val action =
            incomingAction?.takeIf { TopwayMusicContract.isIncomingAction(it) }
                ?: TopwayMusicContract.ACTION_CMD

        val extras =
            TopwayBridgeExtrasPolicy.sanitizeIncomingExtras(
                TopwayBridgeExtrasPolicy.safelyExtractIncomingExtras(
                    intent,
                    javaClass.classLoader,
                    source = "MusicWidgetProvider",
                )
            )

        val serviceIntent =
            Intent(context, MusicService::class.java)
                .setAction(action)
                .putExtra(AuxioService.INTENT_KEY_START_ID, IntegerTable.START_ID_TOPWAY)

        when {
            extras.cmd != null -> serviceIntent.putExtra(TopwayMusicContract.EXTRA_CMD, extras.cmd)
            action == TopwayMusicContract.ACTION_CMD ->
                serviceIntent.putExtra(
                    TopwayMusicContract.EXTRA_CMD,
                    TopwayMusicContract.CMD_UPDATE,
                )
        }

        extras.widgetProgress?.let {
            serviceIntent.putExtra(TopwayMusicContract.EXTRA_WIDGET_PROGRESS, it)
        }

        try {
            ContextCompat.startForegroundService(context, serviceIntent)
        } catch (e: IllegalStateException) {
            L.w(e, "Unable to forward Topway widget/provider intent due to service state")
        } catch (e: SecurityException) {
            L.w(e, "Unable to forward Topway widget/provider intent due to security policy")
        }
    }

    private companion object {
        const val EXTRA_APP_WIDGET_IDS = "appWidgetIds"
        const val STOCK_MUSIC_ACTIVITY_CLASS = "com.tw.music.MusicActivity"
        const val STOCK_WIDGET_ARTWORK_MAX_BYTES = 3_680_000
    }
}
