package com.eckom.xtlibrary.p020b.p037f.p043f;

import java.io.File;
import java.io.FileFilter;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.q */
/* JADX INFO: compiled from: TWMusic.java */
/* JADX INFO: loaded from: classes3.dex */
class C0652q implements FileFilter {
    final /* synthetic */ C0654s this$0;

    C0652q(C0654s c0654s) {
        this.this$0 = c0654s;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        return file.canRead() && file.isDirectory() && file.getName().startsWith("usb");
    }
}
