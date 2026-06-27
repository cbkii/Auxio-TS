package com.eckom.xtlibrary.twproject.activity;

import com.eckom.xtlibrary.twproject.activity.XTActivity;

/* compiled from: XTActivity.java */
/* renamed from: com.eckom.xtlibrary.twproject.activity.e */
/* loaded from: classes3.dex */
class RunnableC0716e implements Runnable {
    final /* synthetic */ XTActivity.C0711a this$1;

    RunnableC0716e(XTActivity.C0711a c0711a) {
        this.this$1 = c0711a;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (XTActivity.this.f879sb == null || XTActivity.this.f879sb.isShowing()) {
            return;
        }
        XTActivity.this.f879sb.show();
    }
}
