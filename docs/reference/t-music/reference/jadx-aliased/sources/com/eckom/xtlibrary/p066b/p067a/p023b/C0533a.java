package com.eckom.xtlibrary.p066b.p067a.p023b;

import android.text.TextUtils;
import com.eckom.xtlibrary.p066b.p053j.C0686b;
import com.eckom.xtlibrary.twproject.p072bt.bean.C0718b;
import com.eckom.xtlibrary.twproject.p072bt.bean.C0719c;
import com.eckom.xtlibrary.twproject.p072bt.bean.TWContact;
import java.util.ArrayList;
import java.util.List;

/* compiled from: CommonData.java */
/* renamed from: com.eckom.xtlibrary.b.a.b.a */
/* loaded from: classes3.dex */
public class C0533a {
    private static C0533a instance;

    /* renamed from: Bg */
    public String f363Bg;

    /* renamed from: Cg */
    public int f364Cg;

    /* renamed from: Hg */
    public String f369Hg;

    /* renamed from: Ig */
    public String f370Ig;

    /* renamed from: Jg */
    public String f371Jg;

    /* renamed from: Qg */
    public boolean f378Qg;

    /* renamed from: Rg */
    public boolean f379Rg;

    /* renamed from: Sg */
    public boolean f380Sg;

    /* renamed from: Zg */
    public boolean f387Zg;
    public String mVersionName;

    /* renamed from: ng */
    public String f398ng;

    /* renamed from: og */
    public String f399og;

    /* renamed from: pg */
    public String f400pg;

    /* renamed from: tg */
    public int f404tg;

    /* renamed from: qg */
    public boolean f401qg = false;

    /* renamed from: rg */
    public boolean f402rg = false;

    /* renamed from: sg */
    public int f403sg = -1;

    /* renamed from: ug */
    public int f405ug = 0;

    /* renamed from: vg */
    public boolean f406vg = false;

    /* renamed from: wg */
    public boolean f407wg = false;

    /* renamed from: xg */
    public String f408xg = "";

    /* renamed from: yg */
    public String f409yg = "";

    /* renamed from: zg */
    public String f410zg = "";

    /* renamed from: Ag */
    public String f362Ag = "";

    /* renamed from: Dg */
    public int f365Dg = -1;

    /* renamed from: Eg */
    public int f366Eg = -1;

    /* renamed from: Fg */
    public int f367Fg = 0;

    /* renamed from: Gg */
    public boolean f368Gg = false;

    /* renamed from: Kg */
    public int f372Kg = 0;

    /* renamed from: Lg */
    public int f373Lg = 0;
    public int mSource = -1;

    /* renamed from: Mg */
    public boolean f374Mg = false;

    /* renamed from: Ng */
    public boolean f375Ng = false;

    /* renamed from: Og */
    public boolean f376Og = false;

    /* renamed from: Pg */
    public boolean f377Pg = false;

    /* renamed from: Tg */
    public int f381Tg = 1;

    /* renamed from: Ug */
    public int f382Ug = 0;

    /* renamed from: Vg */
    public int f383Vg = 0;

    /* renamed from: Wg */
    public boolean f384Wg = false;

    /* renamed from: Xg */
    public boolean f385Xg = false;

    /* renamed from: Yg */
    public String f386Yg = "";

    /* renamed from: _g */
    public ArrayList<C0718b> f388_g = new ArrayList<>();

    /* renamed from: ch */
    public ArrayList<C0718b> f389ch = new ArrayList<>();

    /* renamed from: dh */
    public ArrayList<TWContact> f390dh = new ArrayList<>();

    /* renamed from: eh */
    public ArrayList<TWContact> f391eh = new ArrayList<>();

    /* renamed from: fh */
    public ArrayList<TWContact> f392fh = new ArrayList<>();

    /* renamed from: gh */
    public ArrayList<TWContact> f393gh = new ArrayList<>();

    /* renamed from: hh */
    public ArrayList<C0719c> f394hh = new ArrayList<>();

    /* renamed from: ih */
    public ArrayList<C0719c> f395ih = new ArrayList<>();

    /* renamed from: jh */
    public ArrayList<C0719c> f396jh = new ArrayList<>();

    /* renamed from: kh */
    public List<String> f397kh = new ArrayList();

    private C0533a() {
        this.f378Qg = C0686b.m1011dd() || C0686b.m1005Zc() || C0686b.m1008ad() || C0686b.m1006_c() || C0686b.m1004Yc();
        this.f398ng = C0686b.m999Tc();
    }

    public static C0533a getInstance() {
        if (instance == null) {
            synchronized (C0533a.class) {
                if (instance == null) {
                    instance = new C0533a();
                }
            }
        }
        return instance;
    }

    /* renamed from: gb */
    public boolean m199gb() {
        return TextUtils.equals(this.f386Yg, "airplay_wired");
    }

    /* renamed from: hb */
    public boolean m200hb() {
        return TextUtils.equals(this.f386Yg, "android_mirror_wired") || TextUtils.equals(this.f386Yg, "android_mirror_wireless");
    }
}
