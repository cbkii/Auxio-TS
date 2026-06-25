package com.eckom.xtlibrary.p066b.p067a.p029h;

import android.graphics.drawable.AnimationDrawable;
import android.widget.LinearLayout;

/* compiled from: VoiceCallView.java */
/* renamed from: com.eckom.xtlibrary.b.a.h.c */
/* loaded from: classes3.dex */
class RunnableC0554c implements Runnable {
    final /* synthetic */ C0555d this$0;

    RunnableC0554c(C0555d c0555d) {
        this.this$0 = c0555d;
    }

    @Override // java.lang.Runnable
    public void run() {
        LinearLayout linearLayout;
        LinearLayout linearLayout2;
        linearLayout = this.this$0.ll_anim_all;
        linearLayout.setVisibility(8);
        linearLayout2 = this.this$0.ll_anim_list;
        ((AnimationDrawable) linearLayout2.getBackground()).stop();
        this.this$0.hide();
    }
}
