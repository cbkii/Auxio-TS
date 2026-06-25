package com.eckom.xtlibrary.twproject.activity;

import android.os.Handler;
import android.os.Message;

/* compiled from: XTActivity.java */
/* renamed from: com.eckom.xtlibrary.twproject.activity.a */
/* loaded from: classes3.dex */
class C0712a implements Handler.Callback {
    final /* synthetic */ XTActivity this$0;

    C0712a(XTActivity xTActivity) {
        this.this$0 = xTActivity;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        if (message.what != 65281) {
            return true;
        }
        XTActivity xTActivity = this.this$0;
        if (!xTActivity.f880ub) {
            return true;
        }
        xTActivity.m1124Na();
        return true;
    }
}
