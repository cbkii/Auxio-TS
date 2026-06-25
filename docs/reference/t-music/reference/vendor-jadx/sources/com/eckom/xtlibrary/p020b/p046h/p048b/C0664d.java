package com.eckom.xtlibrary.p020b.p046h.p048b;

import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p046h.C0659a;
import com.eckom.xtlibrary.p020b.p046h.p047a.C0660a;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.twproject.radio.utils.C0721b;

/* compiled from: RadioModel.java */
/* renamed from: com.eckom.xtlibrary.b.h.b.d */
/* loaded from: classes3.dex */
class C0664d implements C0721b.a {
    final /* synthetic */ C0665e this$0;

    C0664d(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    /* JADX WARN: Incorrect condition in loop: B:3:0x0011 */
    @Override // com.eckom.xtlibrary.twproject.radio.utils.C0721b.a
    /* renamed from: O */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void mo809O() {
        C0659a c0659a;
        C0659a c0659a2;
        C0659a c0659a3;
        C0721b c0721b;
        Log.d("RadioModel", "onUpdateFinished: ");
        for (int i = 0; i < c0659a.f734Gi.length; i++) {
            c0659a3 = this.this$0.f779Fi;
            C0660a c0660a = c0659a3.f734Gi[i];
            int i2 = c0660a.f773wl;
            int i3 = c0660a.f770tl;
            c0721b = this.this$0.f781Hi;
            c0660a.f774xl = C0686b.m1007a(i2, i3, c0721b, this.this$0.location);
        }
        this.this$0.mHandler.removeMessages(65282);
        Message obtain = Message.obtain();
        c0659a2 = this.this$0.f779Fi;
        obtain.arg1 = c0659a2.f762ll;
        obtain.what = 65282;
        this.this$0.mHandler.sendMessage(obtain);
    }

    @Override // com.eckom.xtlibrary.twproject.radio.utils.C0721b.a
    /* renamed from: Q */
    public void mo810Q() {
        Log.d("RadioModel", "onUpdateFinished: ");
    }
}
