package com.eckom.xtlibrary.twproject.video.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/* compiled from: VideoIjkModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.i */
/* loaded from: classes3.dex */
class C0731i extends BroadcastReceiver {
    final /* synthetic */ C0735m this$0;

    C0731i(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        boolean z;
        String action = intent.getAction();
        if (!action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
            if (TextUtils.equals(action, "android.intent.action.LOCALE_CHANGED")) {
                z = this.this$0.f919Oi;
                if (z) {
                    this.this$0.m1207E(false);
                    return;
                }
                return;
            }
            return;
        }
        String stringExtra = intent.getStringExtra("reason");
        if ("homekey".equals(stringExtra)) {
            this.this$0.f917Mi = true;
        }
        if ("recentapps".equals(stringExtra)) {
            this.this$0.f917Mi = false;
            this.this$0.m1207E(false);
        }
    }
}
