package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.os.Message;
import android.text.TextUtils;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0577d;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p040c.InterfaceC0581a;
import com.eckom.xtlibrary.p020b.p053j.C0703s;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.z */
/* JADX INFO: compiled from: MusicIjkID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0634z implements InterfaceC0581a {
    final /* synthetic */ C0593L this$0;

    /* JADX INFO: renamed from: uk */
    private String f670uk;

    C0634z(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p040c.InterfaceC0581a
    /* JADX INFO: renamed from: a */
    public void mo454a(String str, String str2, CopyOnWriteArrayList<C0579f> copyOnWriteArrayList, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList2, CopyOnWriteArrayList<C0577d> copyOnWriteArrayList3) {
        this.f670uk = str2;
        if (str.startsWith("/mnt/sdcard")) {
            this.this$0.f583mi.f678wk.addAll(copyOnWriteArrayList);
            C0703s.m1044a(this.this$0.f583mi.f679xk, copyOnWriteArrayList2);
            C0703s.m1044a(this.this$0.f583mi.f680yk, copyOnWriteArrayList3);
            this.this$0.mHandler.removeMessages(65284);
            Message messageObtain = Message.obtain();
            messageObtain.what = 65284;
            messageObtain.arg1 = 3;
            messageObtain.obj = str2;
            this.this$0.mHandler.sendMessageDelayed(messageObtain, 500L);
            return;
        }
        if (str.startsWith("/storage/usb")) {
            CopyOnWriteArrayList<C0579f> copyOnWriteArrayList4 = this.this$0.f583mi.f681zk.get(str2);
            if (copyOnWriteArrayList4 == null) {
                copyOnWriteArrayList4 = new CopyOnWriteArrayList<>();
                this.this$0.f583mi.f681zk.put(str2, copyOnWriteArrayList4);
            }
            copyOnWriteArrayList4.addAll(copyOnWriteArrayList);
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList5 = this.this$0.f583mi.f674Bj.get(str2);
            if (copyOnWriteArrayList5 == null) {
                copyOnWriteArrayList5 = new CopyOnWriteArrayList<>();
                this.this$0.f583mi.f674Bj.put(str2, copyOnWriteArrayList5);
            }
            C0703s.m1044a(copyOnWriteArrayList5, copyOnWriteArrayList2);
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList6 = this.this$0.f583mi.f675Cj.get(str2);
            if (copyOnWriteArrayList6 == null) {
                copyOnWriteArrayList6 = new CopyOnWriteArrayList<>();
                this.this$0.f583mi.f675Cj.put(str2, copyOnWriteArrayList6);
            }
            C0703s.m1044a(copyOnWriteArrayList6, copyOnWriteArrayList3);
            if (TextUtils.equals(this.f670uk, str2)) {
                this.this$0.mHandler.removeMessages(65288);
            }
            Message messageObtain2 = Message.obtain();
            messageObtain2.what = 65288;
            messageObtain2.arg1 = 2;
            messageObtain2.obj = str2;
            this.this$0.mHandler.sendMessageDelayed(messageObtain2, 500L);
            return;
        }
        if (str.startsWith("/storage/extsd")) {
            CopyOnWriteArrayList<C0579f> copyOnWriteArrayList7 = this.this$0.f583mi.f673Ak.get(str2);
            if (copyOnWriteArrayList7 == null) {
                copyOnWriteArrayList7 = new CopyOnWriteArrayList<>();
                this.this$0.f583mi.f673Ak.put(str2, copyOnWriteArrayList7);
            }
            copyOnWriteArrayList7.addAll(copyOnWriteArrayList);
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList8 = this.this$0.f583mi.f676Nj.get(str2);
            if (copyOnWriteArrayList8 == null) {
                copyOnWriteArrayList8 = new CopyOnWriteArrayList<>();
                this.this$0.f583mi.f676Nj.put(str2, copyOnWriteArrayList8);
            }
            C0703s.m1044a(copyOnWriteArrayList8, copyOnWriteArrayList2);
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList9 = this.this$0.f583mi.f677Oj.get(str2);
            if (copyOnWriteArrayList9 == null) {
                copyOnWriteArrayList9 = new CopyOnWriteArrayList<>();
                this.this$0.f583mi.f677Oj.put(str2, copyOnWriteArrayList9);
            }
            C0703s.m1044a(copyOnWriteArrayList9, copyOnWriteArrayList3);
            if (TextUtils.equals(this.f670uk, str2)) {
                this.this$0.mHandler.removeMessages(65286);
            }
            Message messageObtain3 = Message.obtain();
            messageObtain3.what = 65286;
            messageObtain3.arg1 = 1;
            messageObtain3.obj = str2;
            this.this$0.mHandler.sendMessageDelayed(messageObtain3, 500L);
        }
    }
}
