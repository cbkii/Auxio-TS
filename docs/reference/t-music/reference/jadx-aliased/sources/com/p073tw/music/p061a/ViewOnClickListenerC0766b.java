package com.p073tw.music.p061a;

import android.view.View;
import com.p073tw.music.p061a.C0767c;

/* compiled from: MusicAdapter.java */
/* renamed from: com.tw.music.a.b */
/* loaded from: classes3.dex */
class ViewOnClickListenerC0766b implements View.OnClickListener {
    final /* synthetic */ C0767c this$0;
    final /* synthetic */ int val$position;

    ViewOnClickListenerC0766b(C0767c c0767c, int i) {
        this.this$0 = c0767c;
        this.val$position = i;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        C0767c.b bVar;
        C0767c.b bVar2;
        bVar = this.this$0.mOnItemClickListener;
        if (bVar != null) {
            bVar2 = this.this$0.mOnItemClickListener;
            bVar2.mo1367U(this.val$position);
        }
    }
}
