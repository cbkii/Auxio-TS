package com.eckom.xtlibrary.twproject.video.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/* compiled from: VideoModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.u */
/* loaded from: classes3.dex */
class C0743u extends BroadcastReceiver {
    final /* synthetic */ C0748z this$0;

    C0743u(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        boolean z;
        String action = intent.getAction();
        if (!action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
            if (TextUtils.equals(action, "android.intent.action.LOCALE_CHANGED")) {
                z = this.this$0.f951Oi;
                if (z) {
                    this.this$0.m1260E(false);
                    return;
                }
                return;
            }
            return;
        }
        String stringExtra = intent.getStringExtra("reason");
        if ("homekey".equals(stringExtra)) {
            this.this$0.f949Mi = true;
        }
        if ("recentapps".equals(stringExtra)) {
            this.this$0.f949Mi = false;
            this.this$0.m1260E(false);
        }
    }
}
