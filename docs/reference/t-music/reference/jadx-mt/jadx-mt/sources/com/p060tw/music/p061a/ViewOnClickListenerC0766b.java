package com.p060tw.music.p061a;

import android.view.View;

/* JADX INFO: renamed from: com.tw.music.a.b */
/* JADX INFO: compiled from: MusicAdapter.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0766b implements View.OnClickListener {
    final /* synthetic */ C0767c this$0;
    final /* synthetic */ int val$position;

    ViewOnClickListenerC0766b(C0767c c0767c, int i) {
        this.this$0 = c0767c;
        this.val$position = i;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.this$0.mOnItemClickListener != null) {
            this.this$0.mOnItemClickListener.mo1367U(this.val$position);
        }
    }
}
