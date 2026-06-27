package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.os.Handler;
import com.eckom.xtlibrary.p020b.p053j.C0687c;

/* compiled from: MusicIjkModel.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.S */
/* loaded from: classes3.dex */
class RunnableC0599S implements Runnable {
    final /* synthetic */ C0601U this$0;

    RunnableC0599S(C0601U c0601u) {
        this.this$0 = c0601u;
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
