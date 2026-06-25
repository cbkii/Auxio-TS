package com.p060tw.music.p061a;

import android.view.View;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.p060tw.music.p061a.C0767c;

/* JADX INFO: renamed from: com.tw.music.a.a */
/* JADX INFO: compiled from: MusicAdapter.java */
/* JADX INFO: loaded from: classes3.dex */
class ViewOnClickListenerC0765a implements View.OnClickListener {

    /* JADX INFO: renamed from: rm */
    final /* synthetic */ C0767c.c f1083rm;
    final /* synthetic */ C0767c this$0;

    /* JADX INFO: renamed from: tk */
    final /* synthetic */ String f1084tk;
    final /* synthetic */ String val$name;
    final /* synthetic */ int val$position;

    ViewOnClickListenerC0765a(C0767c c0767c, int i, String str, String str2, C0767c.c cVar) {
        this.this$0 = c0767c;
        this.val$position = i;
        this.val$name = str;
        this.f1084tk = str2;
        this.f1083rm = cVar;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int i = this.this$0.mRecord.mIndex == 4 ? this.val$position : this.val$position - 1;
        C0579f c0579f = new C0579f(this.val$name, this.f1084tk, this.this$0.mRecord.f544jk[i].f539ek);
        if (c0579f.f539ek) {
            this.this$0.mRecord.f544jk[i].f539ek = false;
            this.f1083rm.f1095ym.setImageLevel(0);
        } else {
            this.this$0.mRecord.f544jk[i].f539ek = true;
            this.f1083rm.f1095ym.setImageLevel(1);
        }
        if (this.this$0.f1086xf != null) {
            this.this$0.f1086xf.mo1366a(c0579f, this.this$0.mRecord.f544jk[i].f539ek);
        }
    }
}
