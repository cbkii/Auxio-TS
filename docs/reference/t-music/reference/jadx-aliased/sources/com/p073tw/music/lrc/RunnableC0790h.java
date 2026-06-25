package com.p073tw.music.lrc;

/* compiled from: LrcView.java */
/* renamed from: com.tw.music.lrc.h */
/* loaded from: classes3.dex */
class RunnableC0790h implements Runnable {
    final /* synthetic */ LrcView this$0;

    RunnableC0790h(LrcView lrcView) {
        this.this$0 = lrcView;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean z;
        int i;
        if (this.this$0.m1500Wa()) {
            z = this.this$0.f1164jf;
            if (z) {
                this.this$0.f1164jf = false;
                LrcView lrcView = this.this$0;
                i = lrcView.mCurrentLine;
                lrcView.m1472La(i);
            }
        }
    }
}
