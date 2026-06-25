package com.eckom.xtlibrary.p020b.p021a.p029h;

import android.util.Log;
import android.view.View;
import com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.h.a */
/* JADX INFO: compiled from: VoiceCallView.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0552a implements View.OnClickListener {

    /* JADX INFO: renamed from: qh */
    final /* synthetic */ AbstractC0546h f440qh;
    final /* synthetic */ C0555d this$0;

    ViewOnClickListenerC0552a(C0555d c0555d, AbstractC0546h abstractC0546h) {
        this.this$0 = c0555d;
        this.f440qh = abstractC0546h;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Log.d("VoiceCallView", "Downey:onClick: --------------");
        this.f440qh.mo261sb();
    }
}
