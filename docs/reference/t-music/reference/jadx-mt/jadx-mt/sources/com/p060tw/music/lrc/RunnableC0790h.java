package com.p060tw.music.lrc;

/* JADX INFO: renamed from: com.tw.music.lrc.h */
/* JADX INFO: compiled from: LrcView.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0790h implements Runnable {
    final /* synthetic */ LrcView this$0;

    RunnableC0790h(LrcView lrcView) {
        this.this$0 = lrcView;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.this$0.m1500Wa() && this.this$0.f1164jf) {
            this.this$0.f1164jf = false;
            LrcView lrcView = this.this$0;
            lrcView.m1472La(lrcView.mCurrentLine);
        }
    }
}
