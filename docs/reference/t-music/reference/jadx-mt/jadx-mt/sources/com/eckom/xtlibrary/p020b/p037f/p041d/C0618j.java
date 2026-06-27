package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.media.MediaPlayer;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import java.util.Iterator;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.j */
/* JADX INFO: compiled from: MusicID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0618j implements MediaPlayer.OnErrorListener {
    final /* synthetic */ C0628t this$0;

    C0618j(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        Iterator it = C0628t.f637hi.iterator();
        while (it.hasNext()) {
            ((InterfaceC0656a) it.next()).mo727c(false);
        }
        this.this$0.f651Qh = true;
        if (this.this$0.m690Lb() != null && !C0636a.m743a(null, this.this$0.m690Lb(), this.this$0.f659Yc.f488Ed) && i == -1010) {
            C0628t c0628t = this.this$0;
            c0628t.f659Yc.f488Ed.add(new C0579f(c0628t.getFileName(), this.this$0.m690Lb()));
        }
        this.this$0.mHandler.removeMessages(65296);
        this.this$0.mHandler.sendEmptyMessageDelayed(65296, 1000L);
        return true;
    }
}
