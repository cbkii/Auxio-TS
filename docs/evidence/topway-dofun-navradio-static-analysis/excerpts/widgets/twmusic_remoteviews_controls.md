# Evidence excerpt: twmusic_remoteviews_controls.md

  Source APK/variant: `com.tw.music_TW_THEME.20240715`
  Source path: `com.tw.music_TW_THEME.20240715/jadx/sources/com/p060tw/music/view/MusicWidgetProvider.java`
  Source lines: `52-132`
  Status: observation from static decompile/extract.
  Why it matters: Stock RemoteViews layout population and exact PendingIntent actions for album, prev, play/pause, next.

  ```java
     52:         RemoteViews remoteViews = new RemoteViews(musicService.getPackageName(), R.layout.music_widget);
 53:         String strM1369Nb = musicService.f1072Pa.m1369Nb();
 54:         String strM1375jd = musicService.f1072Pa.m1375jd();
 55:         if (strM1369Nb == null && (strM1369Nb = musicService.f1072Pa.m1369Nb()) == null) {
 56:             musicService.getString(R.string.unknown);
 57:         }
 58:         remoteViews.setTextViewText(R.id.title, strM1369Nb);
 59:         if (strM1375jd == null) {
 60:             musicService.getString(R.string.unknown);
 61:         }
 62:         remoteViews.setTextViewText(R.id.artist, strM1375jd);
 63:         int currentPosition = musicService.f1072Pa.getCurrentPosition();
 64:         int duration = musicService.f1072Pa.getDuration();
 65:         if (currentPosition < 0) {
 66:             currentPosition = 0;
 67:         }
 68:         if (duration <= 0) {
 69:             duration = 0;
 70:         }
 71:         int i = currentPosition / 1000;
 72:         int i2 = i / 60;
 73:         int i3 = i2 / 60;
 74:         int i4 = i % 60;
 75:         int i5 = i2 % 60;
 76:         int i6 = i3 % 24;
 77:         if (i6 == 0) {
 78:             remoteViews.setTextViewText(R.id.tv_current_time, String.format(Locale.US, "%d:%02d", Integer.valueOf(i5), Integer.valueOf(i4)));
 79:         } else {
 80:             remoteViews.setTextViewText(R.id.tv_current_time, String.format(Locale.US, "%d:%02d:%02d", Integer.valueOf(i6), Integer.valueOf(i5), Integer.valueOf(i4)));
 81:         }
 82:         int i7 = duration / 1000;
 83:         int i8 = i7 / 60;
 84:         int i9 = i8 / 60;
 85:         int i10 = i7 % 60;
 86:         int i11 = i8 % 60;
 87:         int i12 = i9 % 24;
 88:         if (i12 == 0) {
 89:             remoteViews.setTextViewText(R.id.tv_duration, String.format(Locale.US, "%d:%02d", Integer.valueOf(i11), Integer.valueOf(i10)));
 90:         } else {
 91:             remoteViews.setTextViewText(R.id.tv_duration, String.format(Locale.US, "%d:%02d:%02d", Integer.valueOf(i12), Integer.valueOf(i11), Integer.valueOf(i10)));
 92:         }
 93:         remoteViews.setProgressBar(R.id.seek_bar_progress, i7, i, false);
 94:         Bitmap bitmapM1371ed = musicService.f1072Pa.m1371ed();
 95:         if (bitmapM1371ed != null && bitmapM1371ed.getByteCount() <= 3680000) {
 96:             remoteViews.setImageViewBitmap(R.id.albumart, bitmapM1371ed);
 97:         } else {
 98:             remoteViews.setImageViewResource(R.id.albumart, R.drawable.album);
 99:         }
100:         m1526a(musicService, remoteViews);
101:         m1528a(musicService, iArr, remoteViews);
102:     }
103: 
104:     /* JADX INFO: renamed from: a */
105:     private void m1526a(Context context, RemoteViews remoteViews) {
106:         ComponentName componentName = new ComponentName(context, (Class<?>) MusicService.class);
107:         remoteViews.setOnClickPendingIntent(R.id.albumart, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MusicActivity.class), 0));
108:         Intent intent = new Intent("com.tw.music.action.prev");
109:         intent.setComponent(componentName);
110:         remoteViews.setOnClickPendingIntent(R.id.control_prev, PendingIntent.getService(context, 0, intent, 0));
111:         Intent intent2 = new Intent("com.tw.music.action.pp");
112:         intent2.setComponent(componentName);
113:         remoteViews.setOnClickPendingIntent(R.id.control_play, PendingIntent.getService(context, 0, intent2, 0));
114:         Intent intent3 = new Intent("com.tw.music.action.next");
115:         intent3.setComponent(componentName);
116:         remoteViews.setOnClickPendingIntent(R.id.control_next, PendingIntent.getService(context, 0, intent3, 0));
117:     }
118: 
119:     /* JADX INFO: renamed from: a */
120:     private void m1528a(Context context, int[] iArr, RemoteViews remoteViews) {
121:         try {
122:             AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
123:             if (iArr != null) {
124:                 appWidgetManager.updateAppWidget(iArr, remoteViews);
125:             } else {
126:                 appWidgetManager.updateAppWidget(new ComponentName(context, getClass()), remoteViews);
127:             }
128:         } catch (Exception unused) {
129:         }
130:     }
131: 
132:     /* JADX INFO: renamed from: a */
  ```
