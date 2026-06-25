package com.p073tw.music.lrc;

import java.util.Locale;

/* compiled from: LrcUtils.java */
/* renamed from: com.tw.music.lrc.b */
/* loaded from: classes3.dex */
public class C0784b {
    /* renamed from: g */
    public static String m1508g(long j) {
        return String.format(Locale.getDefault(), "%02d", Integer.valueOf((int) (j / 60000))) + ":" + String.format(Locale.getDefault(), "%02d", Integer.valueOf((int) ((j / 1000) % 60)));
    }
}
