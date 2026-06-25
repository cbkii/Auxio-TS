package com.eckom.xtlibrary.twproject.video.model;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.view.View;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.y */
/* JADX INFO: compiled from: VideoModel.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0747y implements View.OnClickListener {
    final /* synthetic */ C0748z this$0;

    ViewOnClickListenerC0747y(C0748z c0748z) {
        this.this$0 = c0748z;
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
