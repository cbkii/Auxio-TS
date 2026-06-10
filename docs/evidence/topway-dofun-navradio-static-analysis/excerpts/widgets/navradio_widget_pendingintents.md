# Evidence excerpt: navradio_widget_pendingintents.md

  Source APK/variant: `NavRadio+_4.00_apks`
  Source path: `NavRadio+_4.00_apks/jadx/sources/com/navimods/radio/widgets/RadioWidgetExtended.java`
  Source lines: `376-386`
  Status: observation from static decompile/extract.
  Why it matters: NavRadio+ widget buttons are simple explicit broadcast/action surfaces: previous station, next station, open activity.

  ```java
    376:                     remoteViews.setTextViewText(m92.rds_text, "NavRadio+");
377:                     remoteViews.setViewVisibility(m92.freqRangeUnits, 8);
378:                     remoteViews.setViewVisibility(m92.freqView, 8);
379:                     remoteViews.setViewVisibility(m92.PTYName, 8);
380:                 }
381:             }
382:             remoteViews.setOnClickPendingIntent(m92.widgetPrevStationBtn, PendingIntent.getBroadcast(context, 0, new Intent("com.navimods.radio.set.prev_station"), 201326592));
383:             remoteViews.setOnClickPendingIntent(m92.widgetNextStationBtn, PendingIntent.getBroadcast(context, 0, new Intent("com.navimods.radio.set.next_station"), 201326592));
384:             remoteViews.setOnClickPendingIntent(m92.radioWidgetExtended, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) RadioActivity.class), 201326592));
385:             appWidgetManager.updateAppWidget(i3, remoteViews);
386:             Binder.restoreCallingIdentity(jClearCallingIdentity);
  ```
