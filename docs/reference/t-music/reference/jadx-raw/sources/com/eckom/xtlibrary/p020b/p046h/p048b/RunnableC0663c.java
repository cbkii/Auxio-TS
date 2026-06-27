package com.eckom.xtlibrary.p020b.p046h.p048b;

import android.content.Context;
import com.eckom.xtlibrary.p020b.p046h.C0659a;
import com.eckom.xtlibrary.p020b.p053j.C0702r;
import java.util.Locale;

/* compiled from: RadioModel.java */
/* renamed from: com.eckom.xtlibrary.b.h.b.c */
/* loaded from: classes3.dex */
class RunnableC0663c implements Runnable {
    final /* synthetic */ C0665e this$0;

    RunnableC0663c(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    @Override // java.lang.Runnable
    public void run() {
        C0659a c0659a;
        C0659a c0659a2;
        Context context;
        C0659a c0659a3;
        StringBuilder sb = new StringBuilder();
        sb.append("FM - ");
        Locale locale = Locale.US;
        c0659a = this.this$0.f779Fi;
        sb.append(String.format(locale, "%.1f", Float.valueOf(c0659a.f748Wk / 100.0f)));
        sb.append("MHz");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("AM - ");
        Locale locale2 = Locale.US;
        c0659a2 = this.this$0.f779Fi;
        sb3.append(String.format(locale2, "%d", Integer.valueOf(c0659a2.f748Wk)));
        sb3.append("KHz");
        String sb4 = sb3.toString();
        context = this.this$0.mContext;
        Context applicationContext = context.getApplicationContext();
        c0659a3 = this.this$0.f779Fi;
        if (c0659a3.f749Xk == 2) {
            sb2 = sb4;
        }
        C0702r.m1037a(applicationContext, sb2, 3500.0d).show();
    }
}
