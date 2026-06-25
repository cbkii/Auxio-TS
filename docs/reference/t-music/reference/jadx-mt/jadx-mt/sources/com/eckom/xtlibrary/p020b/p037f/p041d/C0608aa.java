package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.media.MediaPlayer;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0654s;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import java.util.Iterator;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.aa */
/* JADX INFO: compiled from: MusicModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0608aa implements MediaPlayer.OnErrorListener {
    final /* synthetic */ C0610ba this$0;

    C0608aa(C0610ba c0610ba) {
        this.this$0 = c0610ba;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Iterator it = C0610ba.f615hi.iterator();
        while (it.hasNext()) {
            ((InterfaceC0656a) it.next()).mo727c(false);
        }
        this.this$0.f622Qh = true;
        if (this.this$0.m639Xb() != null) {
            String strM639Xb = this.this$0.m639Xb();
            C0654s unused = C0610ba.f616jd;
            if (!C0636a.m743a(null, strM639Xb, C0654s.f705Ed) && i == -1010) {
                C0654s unused2 = C0610ba.f616jd;
                C0654s.f705Ed.add(new C0579f(this.this$0.getFileName(), this.this$0.m639Xb()));
            }
        }
        this.this$0.mHandler.removeMessages(65296);
        this.this$0.mHandler.sendEmptyMessageDelayed(65296, 1000L);
        return true;
    }
}
