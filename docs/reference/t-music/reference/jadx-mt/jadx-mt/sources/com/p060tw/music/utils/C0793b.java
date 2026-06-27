package com.p060tw.music.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* JADX INFO: renamed from: com.tw.music.utils.b */
/* JADX INFO: compiled from: SPUtil.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0793b {

    /* JADX INFO: renamed from: Cn */
    private SharedPreferences.Editor f1177Cn;

    /* JADX INFO: renamed from: sh */
    private SharedPreferences f1178sh;

    /* JADX INFO: renamed from: com.tw.music.utils.b$a */
    /* JADX INFO: compiled from: SPUtil.java */
    private static class a {

        /* JADX INFO: renamed from: Bn */
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
