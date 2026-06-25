package com.eckom.xtlibrary.twproject.video.model;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.view.View;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.a */
/* JADX INFO: compiled from: VideoIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0723a implements View.OnClickListener {
    final /* synthetic */ C0735m this$0;

    ViewOnClickListenerC0723a(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClassName("com.tw.video", "com.tw.video.VideoActivity");
        intent.setFlags(268435456);
        try {
            (Build.VERSION.SDK_INT >= 31 ? PendingIntent.getActivity(this.this$0.mContext, 0, intent, 67108864) : PendingIntent.getActivity(this.this$0.mContext, 0, intent, 0)).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
