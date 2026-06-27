package com.p060tw.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: SPUtil.java */
/* renamed from: com.tw.music.utils.b */
/* loaded from: classes3.dex */
public class C0793b {

    /* renamed from: Cn */
    private SharedPreferences.Editor f1177Cn;

    /* renamed from: sh */
    private SharedPreferences f1178sh;

    /* compiled from: SPUtil.java */
    /* renamed from: com.tw.music.utils.b$a */
    private static class a {

        /* renamed from: Bn */
        private static final C0793b f1179Bn = new C0793b();
    }

    public static C0793b getInstance() {
        return a.f1179Bn;
    }

    public void init(Context context) {
        this.f1178sh = context.getSharedPreferences("tw_config", 0);
        this.f1177Cn = this.f1178sh.edit();
    }
}
