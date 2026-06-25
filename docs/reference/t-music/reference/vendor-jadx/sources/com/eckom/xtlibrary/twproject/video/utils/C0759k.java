package com.eckom.xtlibrary.twproject.video.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayerView;

/* compiled from: TWVideo.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.utils.k */
/* loaded from: classes3.dex */
class C0759k implements FileFilter {
    final /* synthetic */ C0760l this$0;

    C0759k(C0760l c0760l) {
        this.this$0 = c0760l;
    }

    @Override // java.io.FileFilter
    public boolean accept(File file) {
        String upperCase = file.getName().toUpperCase(Locale.ENGLISH);
        if (C0760l.f983Hd) {
            return file.isFile() && !upperCase.startsWith(".") && TWMediaPlayerView.isVideo(upperCase);
        }
        if (file.isFile() && !upperCase.startsWith(".")) {
            for (int i = 0; i < this.this$0.f996Od.size(); i++) {
                if (upperCase.endsWith(this.this$0.f996Od.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }
}
