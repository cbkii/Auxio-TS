package com.p060tw.music.lrc;

import java.util.Locale;

/* JADX INFO: renamed from: com.tw.music.lrc.b */
/* JADX INFO: compiled from: LrcUtils.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0784b {
    /* JADX INFO: renamed from: g */
    public static String m1508g(long j) {
        return String.format(Locale.getDefault(), "%02d", Integer.valueOf((int) (j / 60000))) + ":" + String.format(Locale.getDefault(), "%02d", Integer.valueOf((int) ((j / 1000) % 60)));
    }
}
