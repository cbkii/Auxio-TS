package com.eckom.xtlibrary.p020b.p037f.p041d;

import java.io.File;
import java.io.FileFilter;

/* compiled from: MusicID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.f */
/* loaded from: classes3.dex */
class C0614f implements FileFilter {
    final /* synthetic */ C0628t this$0;

    C0614f(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.canRead() && file.isDirectory() && file.getName().startsWith("usb");
    }
}
