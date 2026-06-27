package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.media.MediaPlayer;
import android.os.Handler;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MusicID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.j */
/* loaded from: classes3.dex */
class C0618j implements MediaPlayer.OnErrorListener {
    final /* synthetic */ C0628t this$0;

    C0618j(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        ArrayList arrayList;
        Handler handler;
        Handler handler2;
        arrayList = C0628t.f637hi;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((InterfaceC0656a) it.next()).mo727c(false);
        }
        this.this$0.f651Qh = true;
        if (this.this$0.m690Lb() != null && !C0636a.m743a(null, this.this$0.m690Lb(), this.this$0.f659Yc.f488Ed) && i == -1010) {
            C0628t c0628t = this.this$0;
            c0628t.f659Yc.f488Ed.add(new C0579f(c0628t.getFileName(), this.this$0.m690Lb()));
        }
        handler = this.this$0.mHandler;
        handler.removeMessages(65296);
        handler2 = this.this$0.mHandler;
        handler2.sendEmptyMessageDelayed(65296, 1000L);
        return true;
    }
}
