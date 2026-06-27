package com.p060tw.music.lrc;

/* JADX INFO: renamed from: com.tw.music.lrc.c */
/* JADX INFO: compiled from: LrcView.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0785c implements Runnable {
    final /* synthetic */ LrcView this$0;
    final /* synthetic */ String val$label;

    RunnableC0785c(LrcView lrcView, String str) {
        this.this$0 = lrcView;
        this.val$label = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.this$0.f1160ff = this.val$label;
        this.this$0.invalidate();
    }
}
