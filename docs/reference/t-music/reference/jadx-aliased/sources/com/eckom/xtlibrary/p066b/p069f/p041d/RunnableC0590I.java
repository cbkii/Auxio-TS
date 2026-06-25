package com.eckom.xtlibrary.p066b.p069f.p041d;

import android.os.Handler;
import com.eckom.xtlibrary.p066b.p053j.C0687c;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.I */
/* loaded from: classes3.dex */
class RunnableC0590I implements Runnable {
    final /* synthetic */ C0593L this$0;

    RunnableC0590I(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // java.lang.Runnable
    public void run() {
        Handler handler;
        Handler handler2;
        new C0687c().m1015jb("/mnt/sdcard");
        handler = this.this$0.mHandler;
        handler.removeMessages(65285);
        handler2 = this.this$0.mHandler;
        handler2.sendEmptyMessage(65285);
    }
}
