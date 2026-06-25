package com.eckom.xtlibrary.p066b.p069f.p041d;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.eckom.xtlibrary.p066b.p053j.C0703s;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0577d;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0579f;
import com.eckom.xtlibrary.p066b.p069f.p040c.InterfaceC0581a;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0638c;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.z */
/* loaded from: classes3.dex */
class C0634z implements InterfaceC0581a {
    final /* synthetic */ C0593L this$0;

    /* renamed from: uk */
    private String f670uk;

    C0634z(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // com.eckom.xtlibrary.p066b.p069f.p040c.InterfaceC0581a
    /* renamed from: a */
    public void mo454a(String str, String str2, CopyOnWriteArrayList<C0579f> copyOnWriteArrayList, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList2, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList3) {
        C0638c c0638c;
        C0638c c0638c2;
        C0638c c0638c3;
        Handler handler;
        Handler handler2;
        C0638c c0638c4;
        C0638c c0638c5;
        C0638c c0638c6;
        C0638c c0638c7;
        C0638c c0638c8;
        C0638c c0638c9;
        Handler handler3;
        Handler handler4;
        C0638c c0638c10;
        C0638c c0638c11;
        C0638c c0638c12;
        C0638c c0638c13;
        C0638c c0638c14;
        C0638c c0638c15;
        Handler handler5;
        Handler handler6;
        this.f670uk = str2;
        if (str.startsWith("/mnt/sdcard")) {
            c0638c13 = this.this$0.f583mi;
            c0638c13.f678wk.addAll(copyOnWriteArrayList);
            c0638c14 = this.this$0.f583mi;
            C0703s.m1044a(c0638c14.f679xk, copyOnWriteArrayList2);
            c0638c15 = this.this$0.f583mi;
            C0703s.m1044a(c0638c15.f680yk, copyOnWriteArrayList3);
            handler5 = this.this$0.mHandler;
            handler5.removeMessages(65284);
            Message obtain = Message.obtain();
            obtain.what = 65284;
            obtain.arg1 = 3;
            obtain.obj = str2;
            handler6 = this.this$0.mHandler;
            handler6.sendMessageDelayed(obtain, 500L);
            return;
        }
        if (str.startsWith("/storage/usb")) {
            c0638c7 = this.this$0.f583mi;
            CopyOnWriteArrayList<C0579f> copyOnWriteArrayList4 = c0638c7.f681zk.get(str2);
            if (copyOnWriteArrayList4 == null) {
                copyOnWriteArrayList4 = new CopyOnWriteArrayList<>();
                c0638c12 = this.this$0.f583mi;
                c0638c12.f681zk.put(str2, copyOnWriteArrayList4);
            }
            copyOnWriteArrayList4.addAll(copyOnWriteArrayList);
            c0638c8 = this.this$0.f583mi;
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList5 = c0638c8.f674Bj.get(str2);
            if (copyOnWriteArrayList5 == null) {
                copyOnWriteArrayList5 = new CopyOnWriteArrayList<>();
                c0638c11 = this.this$0.f583mi;
                c0638c11.f674Bj.put(str2, copyOnWriteArrayList5);
            }
            C0703s.m1044a(copyOnWriteArrayList5, copyOnWriteArrayList2);
            c0638c9 = this.this$0.f583mi;
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList6 = c0638c9.f675Cj.get(str2);
            if (copyOnWriteArrayList6 == null) {
                copyOnWriteArrayList6 = new CopyOnWriteArrayList<>();
                c0638c10 = this.this$0.f583mi;
                c0638c10.f675Cj.put(str2, copyOnWriteArrayList6);
            }
            C0703s.m1044a(copyOnWriteArrayList6, copyOnWriteArrayList3);
            if (TextUtils.equals(this.f670uk, str2)) {
                handler4 = this.this$0.mHandler;
                handler4.removeMessages(65288);
            }
            Message obtain2 = Message.obtain();
            obtain2.what = 65288;
            obtain2.arg1 = 2;
            obtain2.obj = str2;
            handler3 = this.this$0.mHandler;
            handler3.sendMessageDelayed(obtain2, 500L);
            return;
        }
        if (str.startsWith("/storage/extsd")) {
            c0638c = this.this$0.f583mi;
            CopyOnWriteArrayList<C0579f> copyOnWriteArrayList7 = c0638c.f673Ak.get(str2);
            if (copyOnWriteArrayList7 == null) {
                copyOnWriteArrayList7 = new CopyOnWriteArrayList<>();
                c0638c6 = this.this$0.f583mi;
                c0638c6.f673Ak.put(str2, copyOnWriteArrayList7);
            }
            copyOnWriteArrayList7.addAll(copyOnWriteArrayList);
            c0638c2 = this.this$0.f583mi;
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList8 = c0638c2.f676Nj.get(str2);
            if (copyOnWriteArrayList8 == null) {
                copyOnWriteArrayList8 = new CopyOnWriteArrayList<>();
                c0638c5 = this.this$0.f583mi;
                c0638c5.f676Nj.put(str2, copyOnWriteArrayList8);
            }
            C0703s.m1044a(copyOnWriteArrayList8, copyOnWriteArrayList2);
            c0638c3 = this.this$0.f583mi;
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList9 = c0638c3.f677Oj.get(str2);
            if (copyOnWriteArrayList9 == null) {
                copyOnWriteArrayList9 = new CopyOnWriteArrayList<>();
                c0638c4 = this.this$0.f583mi;
                c0638c4.f677Oj.put(str2, copyOnWriteArrayList9);
            }
            C0703s.m1044a(copyOnWriteArrayList9, copyOnWriteArrayList3);
            if (TextUtils.equals(this.f670uk, str2)) {
                handler2 = this.this$0.mHandler;
                handler2.removeMessages(65286);
            }
            Message obtain3 = Message.obtain();
            obtain3.what = 65286;
            obtain3.arg1 = 1;
            obtain3.obj = str2;
            handler = this.this$0.mHandler;
            handler.sendMessageDelayed(obtain3, 500L);
        }
    }
}
