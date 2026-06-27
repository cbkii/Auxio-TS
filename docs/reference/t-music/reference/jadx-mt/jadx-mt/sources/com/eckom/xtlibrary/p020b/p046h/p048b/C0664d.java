package com.eckom.xtlibrary.p020b.p046h.p048b;

import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p046h.p047a.C0660a;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.twproject.radio.utils.C0721b;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.h.b.d */
/* JADX INFO: compiled from: RadioModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0664d implements C0721b.a {
    final /* synthetic */ C0665e this$0;

    C0664d(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    @Override // com.eckom.xtlibrary.twproject.radio.utils.C0721b.a
    /* JADX INFO: renamed from: O */
    public void mo809O() {
        Log.d("RadioModel", "onUpdateFinished: ");
        for (int i = 0; i < this.this$0.f779Fi.f734Gi.length; i++) {
            C0660a c0660a = this.this$0.f779Fi.f734Gi[i];
            c0660a.f774xl = C0686b.m1007a(c0660a.f773wl, c0660a.f770tl, this.this$0.f781Hi, this.this$0.location);
        }
        this.this$0.mHandler.removeMessages(65282);
        Message messageObtain = Message.obtain();
        messageObtain.arg1 = this.this$0.f779Fi.f762ll;
        messageObtain.what = 65282;
        this.this$0.mHandler.sendMessage(messageObtain);
    }

    @Override // com.eckom.xtlibrary.twproject.radio.utils.C0721b.a
    /* JADX INFO: renamed from: Q */
    public void mo810Q() {
        Log.d("RadioModel", "onUpdateFinished: ");
    }
}
