package com.eckom.xtlibrary.p020b.p046h.p048b;

import android.support.v4.view.InputDeviceCompat;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.h.b.a */
/* JADX INFO: compiled from: RadioModel.java */
/* JADX INFO: loaded from: classes3.dex */
class RunnableC0661a implements Runnable {
    final /* synthetic */ C0665e this$0;

    RunnableC0661a(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    @Override // java.lang.Runnable
    public void run() throws Throwable {
        if (C0686b.m1009bd()) {
            this.this$0.m835_b();
        }
        this.this$0.f787wh.write(265, 255);
        this.this$0.f787wh.write(266, 255);
        this.this$0.f787wh.write(769, 255);
        this.this$0.f787wh.write(274, 255);
        this.this$0.f787wh.write(1030, 0);
        this.this$0.f787wh.write(InputDeviceCompat.SOURCE_GAMEPAD, 255);
        this.this$0.f787wh.write(1028, 255);
        this.this$0.f787wh.write(515, 255);
        Iterator it = this.this$0.f788yh.entrySet().iterator();
        while (it.hasNext()) {
            ((InterfaceC0666f) ((Map.Entry) it.next()).getValue()).mo861a(this.this$0.f779Fi.f734Gi);
        }
        this.this$0.m811M(true);
    }
}
