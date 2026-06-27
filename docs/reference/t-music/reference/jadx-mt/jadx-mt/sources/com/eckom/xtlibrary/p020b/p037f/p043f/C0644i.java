package com.eckom.xtlibrary.p020b.p037f.p043f;

import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import java.io.File;
import java.io.FileFilter;
import java.util.Locale;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.i */
/* JADX INFO: compiled from: MusicUtils.java */
/* JADX INFO: loaded from: classes3.dex */
class C0644i implements FileFilter {
    final /* synthetic */ C0643h.b this$0;

    C0644i(C0643h.b bVar) {
        this.this$0 = bVar;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        String upperCase = file.getName().toUpperCase(Locale.ENGLISH);
        return file.isFile() && !upperCase.startsWith(".") && C0643h.isAudio(upperCase);
    }
}
