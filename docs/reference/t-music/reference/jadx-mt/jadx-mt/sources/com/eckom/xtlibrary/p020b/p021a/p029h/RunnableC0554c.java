package com.eckom.xtlibrary.p020b.p021a.p029h;

import android.graphics.drawable.AnimationDrawable;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.h.c */
/* JADX INFO: compiled from: VoiceCallView.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0554c implements Runnable {
    final /* synthetic */ C0555d this$0;

    RunnableC0554c(C0555d c0555d) {
        this.this$0 = c0555d;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.this$0.ll_anim_all.setVisibility(8);
        ((AnimationDrawable) this.this$0.ll_anim_list.getBackground()).stop();
        this.this$0.hide();
    }
}
