package com.eckom.xtlibrary.twproject.video.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

/* compiled from: VideoIjkModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.a */
/* loaded from: classes3.dex */
class ViewOnClickListenerC0723a implements View.OnClickListener {
    final /* synthetic */ C0735m this$0;

    ViewOnClickListenerC0723a(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Context context;
        PendingIntent activity;
        Context context2;
        Intent intent = new Intent();
        intent.setClassName("com.tw.video", "com.tw.video.VideoActivity");
        intent.setFlags(268435456);
        try {
            if (Build.VERSION.SDK_INT >= 31) {
                context2 = this.this$0.mContext;
                activity = PendingIntent.getActivity(context2, 0, intent, 67108864);
            } else {
                context = this.this$0.mContext;
                activity = PendingIntent.getActivity(context, 0, intent, 0);
            }
            activity.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
