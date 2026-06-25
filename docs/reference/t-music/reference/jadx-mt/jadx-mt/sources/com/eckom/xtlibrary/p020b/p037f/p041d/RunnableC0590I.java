package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p053j.C0687c;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.I */
/* JADX INFO: compiled from: MusicIjkID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0590I implements Runnable {
    final /* synthetic */ C0593L this$0;

    RunnableC0590I(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        new C0687c().m1015jb("/mnt/sdcard");
        this.this$0.mHandler.removeMessages(65285);
        this.this$0.mHandler.sendEmptyMessage(65285);
    }
}
