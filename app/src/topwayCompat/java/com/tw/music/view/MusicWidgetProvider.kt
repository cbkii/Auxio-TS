package com.tw.music.view

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import com.tw.music.MusicService
import org.oxycblt.auxio.R
import org.oxycblt.auxio.headunit.diag.DiagnosticJournal

class MusicWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        DiagnosticJournal.log(DiagnosticJournal.CAT_WIDGET, "Topway widget cold controls rendered")

        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_music)

            val serviceIntent = Intent(context, MusicService::class.java).apply {
                putExtra("cmd", "update")
                putExtra("appWidgetIds", appWidgetIds)
            }
            context.startService(serviceIntent)
            DiagnosticJournal.log(DiagnosticJournal.CAT_WIDGET, "Topway widget service update requested")

            try {
                val stickyIntent = Intent("com.tw.music.action.cmd").apply {
                    putExtra("cmd", "update")
                    putExtra("appWidgetIds", appWidgetIds)
                    addFlags(0x40000000)
                }
                context.sendStickyBroadcast(stickyIntent)
                DiagnosticJournal.log(DiagnosticJournal.CAT_WIDGET, "Topway widget sticky cmd update sent")
            } catch (e: SecurityException) {
                DiagnosticJournal.log(DiagnosticJournal.CAT_WIDGET, "Topway widget sticky cmd update denied")
            } catch (e: RuntimeException) {
                DiagnosticJournal.log(DiagnosticJournal.CAT_WIDGET, "Topway widget sticky cmd update failed")
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
