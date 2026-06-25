package com.eckom.xtlibrary.p020b.p037f.p043f;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.f.m */
/* JADX INFO: compiled from: TWMusic.java */
/* JADX INFO: loaded from: classes3.dex */
class C0648m implements FileFilter {
    final /* synthetic */ C0654s this$0;

    C0648m(C0654s c0654s) {
        this.this$0 = c0654s;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        String upperCase = file.getName().toUpperCase(Locale.ENGLISH);
        if (C0654s.f708Hd) {
            return file.isFile() && !upperCase.startsWith(".") && TWMediaPlayer.isAudio(upperCase);
        }
        if (file.isFile() && !upperCase.startsWith(".")) {
            for (int i = 0; i < this.this$0.f731zd.size(); i++) {
                if (upperCase.endsWith(this.this$0.f731zd.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
