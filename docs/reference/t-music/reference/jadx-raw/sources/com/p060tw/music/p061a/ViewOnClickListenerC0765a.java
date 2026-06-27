package com.p060tw.music.p061a;

import android.view.View;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.p060tw.music.p061a.C0767c;

/* compiled from: MusicAdapter.java */
/* renamed from: com.tw.music.a.a */
/* loaded from: classes3.dex */
class ViewOnClickListenerC0765a implements View.OnClickListener {

    /* renamed from: rm */
    final /* synthetic */ C0767c.c f1083rm;
    final /* synthetic */ C0767c this$0;

    /* renamed from: tk */
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
        C0580g c0580g;
        C0580g c0580g2;
        C0580g c0580g3;
        C0767c.a aVar;
        C0767c.a aVar2;
        C0580g c0580g4;
        C0580g c0580g5;
        c0580g = this.this$0.mRecord;
        int i = c0580g.mIndex == 4 ? this.val$position : this.val$position - 1;
        String str = this.val$name;
        String str2 = this.f1084tk;
        c0580g2 = this.this$0.mRecord;
        C0579f c0579f = new C0579f(str, str2, c0580g2.f544jk[i].f539ek);
        if (c0579f.f539ek) {
            c0580g3 = this.this$0.mRecord;
            c0580g3.f544jk[i].f539ek = false;
            this.f1083rm.f1095ym.setImageLevel(0);
        } else {
            c0580g5 = this.this$0.mRecord;
            c0580g5.f544jk[i].f539ek = true;
            this.f1083rm.f1095ym.setImageLevel(1);
        }
        aVar = this.this$0.f1086xf;
        if (aVar != null) {
            aVar2 = this.this$0.f1086xf;
            c0580g4 = this.this$0.mRecord;
            aVar2.mo1366a(c0579f, c0580g4.f544jk[i].f539ek);
        }
    }
}
