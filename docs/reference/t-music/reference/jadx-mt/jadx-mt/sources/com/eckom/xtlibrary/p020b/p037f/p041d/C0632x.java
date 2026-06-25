package com.eckom.xtlibrary.p020b.p037f.p041d;

import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.x */
/* JADX INFO: compiled from: MusicIjkID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0632x implements FileFilter {
    final /* synthetic */ C0633y this$1;

    C0632x(C0633y c0633y) {
        this.this$1 = c0633y;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        String upperCase = file.getName().toUpperCase(Locale.ENGLISH);
        return file.isFile() && !upperCase.startsWith(".") && C0643h.isAudio(upperCase);
    }
}
