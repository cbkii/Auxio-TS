package com.eckom.xtlibrary.p066b.p052i;

import com.eckom.xtlibrary.p066b.p052i.C0681k;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* compiled from: ThemeManager.java */
/* renamed from: com.eckom.xtlibrary.b.i.j */
/* loaded from: classes3.dex */
class RunnableC0680j implements Runnable {
    final /* synthetic */ C0681k this$0;
    final /* synthetic */ C0683m val$info;

    RunnableC0680j(C0681k c0681k, C0683m c0683m) {
        this.this$0 = c0681k;
        this.val$info = c0683m;
    }

    @Override // java.lang.Runnable
    public void run() {
        List list;
        List list2;
        boolean m970a;
        List list3;
        list = this.this$0.f813Xl;
        C0671a.m923a("ThemeManager", "theme switch start. size = %s", Integer.valueOf(list.size()));
        list2 = this.this$0.f813Xl;
        Collections.sort(list2, new C0681k.c(null));
        Iterator it = list2.iterator();
        while (it.hasNext()) {
            ((InterfaceC0673c) it.next()).mo927a(this.val$info);
        }
        Collections.sort(list2, new C0681k.d(null));
        m970a = this.this$0.m970a(list2, this.val$info);
        Collections.sort(list2, new C0681k.b(null));
        this.this$0.m968a((List<InterfaceC0673c>) list2, this.val$info, !m970a);
        list3 = this.this$0.f813Xl;
        C0671a.m923a("ThemeManager", "theme switch finshed. size = %s", Integer.valueOf(list3.size()));
    }
}
