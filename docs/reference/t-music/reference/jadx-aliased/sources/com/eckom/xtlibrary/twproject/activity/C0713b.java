package com.eckom.xtlibrary.twproject.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.eckom.xtlibrary.p066b.p052i.C0677g;
import com.eckom.xtlibrary.p066b.p052i.C0678h;
import com.eckom.xtlibrary.p066b.p052i.C0681k;
import com.eckom.xtlibrary.p066b.p052i.C0683m;
import com.eckom.xtlibrary.p066b.p052i.C0684n;
import java.io.File;

/* compiled from: XTActivity.java */
/* renamed from: com.eckom.xtlibrary.twproject.activity.b */
/* loaded from: classes3.dex */
class C0713b extends BroadcastReceiver {
    final /* synthetic */ XTActivity this$0;

    C0713b(XTActivity xTActivity) {
        this.this$0 = xTActivity;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("notify_theme_change".equals(intent.getAction())) {
            C0677g.f792Tl = "/data/tw/theme/theme_config.json";
            int intExtra = intent.getIntExtra("theme_type", 2);
            Log.d("XTActivity", "onReceive: themeType:" + intExtra);
            C0684n.m992a(this.this$0.f864db, C0677g.f792Tl);
            C0683m mo1104Ka = this.this$0.mo1104Ka();
            this.this$0.mo1106d(mo1104Ka);
            if (intExtra == 0) {
                if (mo1104Ka.f821em == 1) {
                    XTActivity xTActivity = this.this$0;
                    xTActivity.f864db.m948_a(xTActivity.mo1101Ha());
                    C0684n.m992a(this.this$0.f864db, C0677g.f792Tl);
                    Log.d("XTActivity", "NOTIFY_THEME_CHANGE:THEME_TYPE_ALL 1: " + this.this$0.f864db.m936Dc());
                    if (new File(this.this$0.f864db.m936Dc()).exists()) {
                        C0678h.m961a(this.this$0.f864db.m936Dc(), mo1104Ka);
                        C0681k.get().m978e(mo1104Ka);
                        this.this$0.f866fb = true;
                    }
                } else {
                    XTActivity xTActivity2 = this.this$0;
                    xTActivity2.f864db.m954fb(xTActivity2.mo1101Ha());
                    C0684n.m992a(this.this$0.f864db, C0677g.f792Tl);
                    Log.d("XTActivity", "NOTIFY_THEME_CHANGE:THEME_TYPE_ALL 2: " + this.this$0.f864db.m942Ic());
                    if (new File(this.this$0.f864db.m942Ic()).exists()) {
                        C0678h.m961a(this.this$0.f864db.m942Ic(), mo1104Ka);
                        C0681k.get().m978e(mo1104Ka);
                        this.this$0.f866fb = true;
                    }
                }
            } else if (intExtra == 1) {
                XTActivity xTActivity3 = this.this$0;
                xTActivity3.f864db.m948_a(xTActivity3.mo1101Ha());
                C0684n.m992a(this.this$0.f864db, C0677g.f792Tl);
                Log.d("XTActivity", "NOTIFY_THEME_CHANGE:THEME_TYPE_LAUNCHER: " + this.this$0.f864db.m936Dc());
                if (new File(this.this$0.f864db.m936Dc()).exists()) {
                    C0678h.m961a(this.this$0.f864db.m936Dc(), mo1104Ka);
                    C0681k.get().m978e(mo1104Ka);
                    this.this$0.f866fb = true;
                }
            } else if (intExtra == 2) {
                XTActivity xTActivity4 = this.this$0;
                xTActivity4.f864db.m954fb(xTActivity4.mo1101Ha());
                C0684n.m992a(this.this$0.f864db, C0677g.f792Tl);
                Log.d("XTActivity", "NOTIFY_THEME_CHANGE:THEME_TYPE_SUB: " + this.this$0.f864db.m942Ic());
                if (new File(this.this$0.f864db.m942Ic()).exists()) {
                    C0678h.m961a(this.this$0.f864db.m942Ic(), mo1104Ka);
                    C0681k.get().m978e(mo1104Ka);
                    this.this$0.f866fb = true;
                }
            }
            this.this$0.mo1105c(mo1104Ka);
        }
    }
}
