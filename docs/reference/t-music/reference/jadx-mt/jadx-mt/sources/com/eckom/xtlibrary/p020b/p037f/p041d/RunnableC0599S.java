package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p053j.C0687c;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.S */
/* JADX INFO: compiled from: MusicIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0599S implements Runnable {
    final /* synthetic */ C0601U this$0;

    RunnableC0599S(C0601U c0601u) {
        this.this$0 = c0601u;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        new C0687c().m1015jb("/mnt/sdcard");
        this.this$0.mHandler.removeMessages(65285);
        this.this$0.mHandler.sendEmptyMessage(65285);
    }
}
