package com.eckom.xtlibrary.p020b.p046h.p048b;

import android.support.v4.view.InputDeviceCompat;
import com.eckom.xtlibrary.p020b.p046h.C0659a;
import com.eckom.xtlibrary.p020b.p046h.p050d.C0669b;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import java.util.Iterator;
import java.util.Map;

/* compiled from: RadioModel.java */
/* renamed from: com.eckom.xtlibrary.b.h.b.a */
/* loaded from: classes3.dex */
class RunnableC0661a implements Runnable {
    final /* synthetic */ C0665e this$0;

    RunnableC0661a(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    @Override // java.lang.Runnable
    public void run() {
        C0669b c0669b;
        C0669b c0669b2;
        C0669b c0669b3;
        C0669b c0669b4;
        C0669b c0669b5;
        C0669b c0669b6;
        C0669b c0669b7;
        C0669b c0669b8;
        Map map;
        C0659a c0659a;
        if (C0686b.m1009bd()) {
            this.this$0.m835_b();
        }
        c0669b = this.this$0.f787wh;
        c0669b.write(265, 255);
        c0669b2 = this.this$0.f787wh;
        c0669b2.write(266, 255);
        c0669b3 = this.this$0.f787wh;
        c0669b3.write(769, 255);
        c0669b4 = this.this$0.f787wh;
        c0669b4.write(274, 255);
        c0669b5 = this.this$0.f787wh;
        c0669b5.write(1030, 0);
        c0669b6 = this.this$0.f787wh;
        c0669b6.write(InputDeviceCompat.SOURCE_GAMEPAD, 255);
        c0669b7 = this.this$0.f787wh;
        c0669b7.write(1028, 255);
        c0669b8 = this.this$0.f787wh;
        c0669b8.write(515, 255);
        map = this.this$0.f788yh;
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            InterfaceC0666f interfaceC0666f = (InterfaceC0666f) ((Map.Entry) it.next()).getValue();
            c0659a = this.this$0.f779Fi;
            interfaceC0666f.mo861a(c0659a.f734Gi);
        }
        this.this$0.m811M(true);
    }
}
