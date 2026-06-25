package com.eckom.xtlibrary.p020b.p037f.p041d;

import java.io.File;
import java.io.FileFilter;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.v */
/* loaded from: classes3.dex */
class C0630v implements FileFilter {
    final /* synthetic */ C0593L this$0;

    C0630v(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.canRead() && file.isDirectory() && file.getName().startsWith("usb");
    }
}
