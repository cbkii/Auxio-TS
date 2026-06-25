package com.eckom.xtlibrary.p020b.p037f.p043f;

import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/* compiled from: MusicUtils.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.j */
/* loaded from: classes3.dex */
class C0645j implements FileFilter {

    /* renamed from: Ck */
    final /* synthetic */ ArrayList f697Ck;
    final /* synthetic */ C0643h.c this$0;

    C0645j(C0643h.c cVar, ArrayList arrayList) {
        this.this$0 = cVar;
        this.f697Ck = arrayList;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        this.f697Ck.add(new C0579f(file.getName(), file.getAbsolutePath()));
        return true;
    }
}
