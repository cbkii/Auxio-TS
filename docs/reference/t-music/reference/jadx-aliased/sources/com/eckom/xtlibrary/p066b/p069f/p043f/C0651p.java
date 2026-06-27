package com.eckom.xtlibrary.p066b.p069f.p043f;

import java.io.File;
import java.io.FileFilter;

/* compiled from: TWMusic.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.p */
/* loaded from: classes3.dex */
class C0651p implements FileFilter {
    final /* synthetic */ C0654s this$0;

    C0651p(C0654s c0654s) {
        this.this$0 = c0654s;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.canRead() && file.isDirectory() && file.getName().startsWith("extsd");
    }
}
