package com.p060tw.music.lrc;

/* compiled from: LrcView.java */
/* renamed from: com.tw.music.lrc.f */
/* loaded from: classes3.dex */
class RunnableC0788f implements Runnable {
    final /* synthetic */ LrcView this$0;
    final /* synthetic */ int val$position;

    RunnableC0788f(LrcView lrcView, int i) {
        this.this$0 = lrcView;
        this.val$position = i;
    }

    @Override // java.lang.Runnable
    public void run() {
        int m1470Ja;
        int i;
        boolean z;
        if (this.this$0.m1500Wa()) {
            m1470Ja = this.this$0.m1470Ja(this.val$position);
            i = this.this$0.mCurrentLine;
            if (m1470Ja != i) {
                this.this$0.mCurrentLine = m1470Ja;
                z = this.this$0.f1164jf;
                if (z) {
                    this.this$0.invalidate();
                } else {
                    this.this$0.m1472La(m1470Ja);
                }
            }
        }
    }
}
