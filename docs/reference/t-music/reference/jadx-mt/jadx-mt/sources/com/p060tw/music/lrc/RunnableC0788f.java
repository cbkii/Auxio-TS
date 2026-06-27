package com.p060tw.music.lrc;

/* JADX INFO: renamed from: com.tw.music.lrc.f */
/* JADX INFO: compiled from: LrcView.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0788f implements Runnable {
    final /* synthetic */ LrcView this$0;
    final /* synthetic */ int val$position;

    RunnableC0788f(LrcView lrcView, int i) {
        this.this$0 = lrcView;
        this.val$position = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        int iM1470Ja;
        if (this.this$0.m1500Wa() && (iM1470Ja = this.this$0.m1470Ja(this.val$position)) != this.this$0.mCurrentLine) {
            this.this$0.mCurrentLine = iM1470Ja;
            if (this.this$0.f1164jf) {
                this.this$0.invalidate();
            } else {
                this.this$0.m1472La(iM1470Ja);
            }
        }
    }
}
