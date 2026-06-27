package com.eckom.xtlibrary.p020b.p046h.p048b;

import android.content.Context;
import com.eckom.xtlibrary.p020b.p053j.C0702r;
import java.util.Locale;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.h.b.c */
/* JADX INFO: compiled from: RadioModel.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0663c implements Runnable {
    final /* synthetic */ C0665e this$0;

    RunnableC0663c(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    @Override // java.lang.Runnable
    public void run() {
        String str = "FM - " + String.format(Locale.US, "%.1f", Float.valueOf(this.this$0.f779Fi.f748Wk / 100.0f)) + "MHz";
        String str2 = "AM - " + String.format(Locale.US, "%d", Integer.valueOf(this.this$0.f779Fi.f748Wk)) + "KHz";
        Context applicationContext = this.this$0.mContext.getApplicationContext();
        if (this.this$0.f779Fi.f749Xk == 2) {
            str = str2;
        }
        C0702r.m1037a(applicationContext, str, 3500.0d).show();
    }
}
