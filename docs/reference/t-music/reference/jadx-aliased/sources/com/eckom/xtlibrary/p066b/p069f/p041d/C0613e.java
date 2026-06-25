package com.eckom.xtlibrary.p066b.p069f.p041d;

import java.io.File;
import java.io.FileFilter;

/* compiled from: MusicID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.e */
/* loaded from: classes3.dex */
class C0613e implements FileFilter {
    final /* synthetic */ C0628t this$0;

    C0613e(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.canRead() && file.isDirectory() && file.getName().startsWith("extsd");
    }
}
