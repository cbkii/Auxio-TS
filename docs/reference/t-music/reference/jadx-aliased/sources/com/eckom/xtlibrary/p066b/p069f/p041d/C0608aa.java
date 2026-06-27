package com.eckom.xtlibrary.p066b.p069f.p041d;

import android.media.MediaPlayer;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0579f;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0636a;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0654s;
import com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0656a;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MusicModel.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.aa */
/* loaded from: classes3.dex */
class C0608aa implements MediaPlayer.OnErrorListener {
    final /* synthetic */ C0610ba this$0;

    C0608aa(C0610ba c0610ba) {
        this.this$0 = c0610ba;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        ArrayList arrayList;
        C0654s unused;
        C0654s unused2;
        arrayList = C0610ba.f615hi;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((InterfaceC0656a) it.next()).mo727c(false);
        }
        this.this$0.f622Qh = true;
        if (this.this$0.m639Xb() != null) {
            String m639Xb = this.this$0.m639Xb();
            unused = C0610ba.f616jd;
            if (!C0636a.m743a(null, m639Xb, C0654s.f705Ed) && i == -1010) {
                unused2 = C0610ba.f616jd;
                C0654s.f705Ed.add(new C0579f(this.this$0.getFileName(), this.this$0.m639Xb()));
            }
        }
        this.this$0.mHandler.removeMessages(65296);
        this.this$0.mHandler.sendEmptyMessageDelayed(65296, 1000L);
        return true;
    }
}
