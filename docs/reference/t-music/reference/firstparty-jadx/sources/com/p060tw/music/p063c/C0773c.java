package com.p060tw.music.p063c;

import android.content.Context;
import com.eckom.xtlibrary.p020b.p052i.C0672b;
import com.eckom.xtlibrary.p020b.p052i.C0683m;

/* compiled from: MusicThemeInfo.java */
/* renamed from: com.tw.music.c.c */
/* loaded from: classes3.dex */
public class C0773c extends C0683m {

    /* renamed from: Bc */
    public C0772b f1143Bc;

    /* renamed from: fm */
    public C0771a f1144fm;

    @Override // com.eckom.xtlibrary.p020b.p052i.C0683m
    /* renamed from: Pc */
    public void mo989Pc() {
        super.mo989Pc();
        Context m986Mc = m987Nc().m986Mc();
        this.f1143Bc = C0772b.m1405sb(C0672b.m924b(m986Mc, "views_player_config.json"));
        this.f1144fm = C0771a.m1382rb(C0672b.m924b(m986Mc, "views_list_config.json"));
    }
}
