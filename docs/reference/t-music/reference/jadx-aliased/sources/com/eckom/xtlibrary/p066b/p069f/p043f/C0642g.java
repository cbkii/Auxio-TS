package com.eckom.xtlibrary.p066b.p069f.p043f;

import com.eckom.xtlibrary.p066b.p069f.p039b.C0579f;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MusicUtils.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.g */
/* loaded from: classes3.dex */
class C0642g extends Thread {

    /* renamed from: Hn */
    final /* synthetic */ ArrayList f685Hn;

    C0642g(ArrayList arrayList) {
        this.f685Hn = arrayList;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.run();
        try {
            ArrayList arrayList = new ArrayList();
            Iterator it = this.f685Hn.iterator();
            while (it.hasNext()) {
                arrayList.add(((C0579f) it.next()).mPath);
            }
            if (new File("/data/tw/.like").exists()) {
                C0643h.delete("/data/tw/.like");
            }
            C0643h.m758b("/data/tw/.like", arrayList);
            arrayList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
