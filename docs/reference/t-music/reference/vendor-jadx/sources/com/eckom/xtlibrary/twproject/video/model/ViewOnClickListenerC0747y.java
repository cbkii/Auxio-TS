package com.eckom.xtlibrary.twproject.video.model;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

/* compiled from: VideoModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.y */
/* loaded from: classes3.dex */
class ViewOnClickListenerC0747y implements View.OnClickListener {
    final /* synthetic */ C0748z this$0;

    ViewOnClickListenerC0747y(C0748z c0748z) {
        this.this$0 = c0748z;
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
