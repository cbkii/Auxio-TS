package com.p060tw.music.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import com.p060tw.music.MusicActivity;
import com.p060tw.music.MusicService;
import com.p060tw.music.R;
import java.util.Locale;

/* loaded from: classes3.dex */
public class MusicWidgetProvider extends AppWidgetProvider {
    private static MusicWidgetProvider sInstance;

    /* renamed from: d */
    private boolean m1529d(Context context) {
        return AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, (Class<?>) MusicWidgetProvider.class)).length > 0;
    }

    public static synchronized MusicWidgetProvider getInstance() {
        MusicWidgetProvider musicWidgetProvider;
        synchronized (MusicWidgetProvider.class) {
            if (sInstance == null) {
                sInstance = new MusicWidgetProvider();
            }
            musicWidgetProvider = sInstance;
        }
        return musicWidgetProvider;
    }

    /* renamed from: a */
    public void m1530a(MusicService musicService) {
        if (m1529d(musicService)) {
            m1531a(musicService, (int[]) null);
        }
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        m1527a(context, iArr);
        context.startService(new Intent(context, (Class<?>) MusicService.class));
        Intent intent = new Intent("com.tw.music.action.cmd");
        intent.putExtra("cmd", "update");
        intent.putExtra("appWidgetIds", iArr);
        intent.addFlags(1073741824);
        context.sendStickyBroadcast(intent);
    }

    /* renamed from: a */
    public void m1531a(MusicService musicService, int[] iArr) {
        RemoteViews remoteViews = new RemoteViews(musicService.getPackageName(), R.layout.music_widget);
        String m1369Nb = musicService.f1072Pa.m1369Nb();
        String m1375jd = musicService.f1072Pa.m1375jd();
        if (m1369Nb == null && (m1369Nb = musicService.f1072Pa.m1369Nb()) == null) {
            musicService.getString(R.string.unknown);
        }
        remoteViews.setTextViewText(R.id.title, m1369Nb);
        if (m1375jd == null) {
            musicService.getString(R.string.unknown);
        }
        remoteViews.setTextViewText(R.id.artist, m1375jd);
        int currentPosition = musicService.f1072Pa.getCurrentPosition();
        int duration = musicService.f1072Pa.getDuration();
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        if (duration <= 0) {
            duration = 0;
        }
        int i = currentPosition / 1000;
        int i2 = i / 60;
        int i3 = i2 / 60;
        int i4 = i % 60;
        int i5 = i2 % 60;
        int i6 = i3 % 24;
        if (i6 == 0) {
            remoteViews.setTextViewText(R.id.tv_current_time, String.format(Locale.US, "%d:%02d", Integer.valueOf(i5), Integer.valueOf(i4)));
        } else {
            remoteViews.setTextViewText(R.id.tv_current_time, String.format(Locale.US, "%d:%02d:%02d", Integer.valueOf(i6), Integer.valueOf(i5), Integer.valueOf(i4)));
        }
        int i7 = duration / 1000;
        int i8 = i7 / 60;
        int i9 = i8 / 60;
        int i10 = i7 % 60;
        int i11 = i8 % 60;
        int i12 = i9 % 24;
        if (i12 == 0) {
            remoteViews.setTextViewText(R.id.tv_duration, String.format(Locale.US, "%d:%02d", Integer.valueOf(i11), Integer.valueOf(i10)));
        } else {
            remoteViews.setTextViewText(R.id.tv_duration, String.format(Locale.US, "%d:%02d:%02d", Integer.valueOf(i12), Integer.valueOf(i11), Integer.valueOf(i10)));
        }
        remoteViews.setProgressBar(R.id.seek_bar_progress, i7, i, false);
        Bitmap m1371ed = musicService.f1072Pa.m1371ed();
        if (m1371ed != null && m1371ed.getByteCount() <= 3680000) {
            remoteViews.setImageViewBitmap(R.id.albumart, m1371ed);
        } else {
            remoteViews.setImageViewResource(R.id.albumart, R.drawable.album);
        }
        m1526a(musicService, remoteViews);
        m1528a(musicService, iArr, remoteViews);
    }

    /* renamed from: a */
    private void m1526a(Context context, RemoteViews remoteViews) {
        ComponentName componentName = new ComponentName(context, (Class<?>) MusicService.class);
        remoteViews.setOnClickPendingIntent(R.id.albumart, PendingIntent.getActivity(context, 0, new Intent(context, (Class<?>) MusicActivity.class), 0));
        Intent intent = new Intent("com.tw.music.action.prev");
        intent.setComponent(componentName);
        remoteViews.setOnClickPendingIntent(R.id.control_prev, PendingIntent.getService(context, 0, intent, 0));
        Intent intent2 = new Intent("com.tw.music.action.pp");
        intent2.setComponent(componentName);
        remoteViews.setOnClickPendingIntent(R.id.control_play, PendingIntent.getService(context, 0, intent2, 0));
        Intent intent3 = new Intent("com.tw.music.action.next");
        intent3.setComponent(componentName);
        remoteViews.setOnClickPendingIntent(R.id.control_next, PendingIntent.getService(context, 0, intent3, 0));
    }

    /* renamed from: a */
    private void m1528a(Context context, int[] iArr, RemoteViews remoteViews) {
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            if (iArr != null) {
                appWidgetManager.updateAppWidget(iArr, remoteViews);
            } else {
                appWidgetManager.updateAppWidget(new ComponentName(context, getClass()), remoteViews);
            }
        } catch (Exception unused) {
        }
    }

    /* renamed from: a */
    private void m1527a(Context context, int[] iArr) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.music_widget);
        m1526a(context, remoteViews);
        m1528a(context, iArr, remoteViews);
    }
}
