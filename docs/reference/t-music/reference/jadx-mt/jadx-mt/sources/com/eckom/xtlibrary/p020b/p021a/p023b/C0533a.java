package com.eckom.xtlibrary.p020b.p021a.p023b;

import android.text.TextUtils;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.twproject.p059bt.bean.C0718b;
import com.eckom.xtlibrary.twproject.p059bt.bean.C0719c;
import com.eckom.xtlibrary.twproject.p059bt.bean.TWContact;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.b.a */
/* JADX INFO: compiled from: CommonData.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0533a {
    private static C0533a instance;

    /* JADX INFO: renamed from: Bg */
    public String f363Bg;

    /* JADX INFO: renamed from: Cg */
    public int f364Cg;

    /* JADX INFO: renamed from: Hg */
    public String f369Hg;

    /* JADX INFO: renamed from: Ig */
    public String f370Ig;

    /* JADX INFO: renamed from: Jg */
    public String f371Jg;

    /* JADX INFO: renamed from: Qg */
    public boolean f378Qg;

    /* JADX INFO: renamed from: Rg */
    public boolean f379Rg;

    /* JADX INFO: renamed from: Sg */
    public boolean f380Sg;

    /* JADX INFO: renamed from: Zg */
    public boolean f387Zg;
    public String mVersionName;

    /* JADX INFO: renamed from: ng */
    public String f398ng;

    /* JADX INFO: renamed from: og */
    public String f399og;

    /* JADX INFO: renamed from: pg */
    public String f400pg;

    /* JADX INFO: renamed from: tg */
    public int f404tg;

    /* JADX INFO: renamed from: qg */
    public boolean f401qg = false;

    /* JADX INFO: renamed from: rg */
    public boolean f402rg = false;

    /* JADX INFO: renamed from: sg */
    public int f403sg = -1;

    /* JADX INFO: renamed from: ug */
    public int f405ug = 0;

    /* JADX INFO: renamed from: vg */
    public boolean f406vg = false;

    /* JADX INFO: renamed from: wg */
    public boolean f407wg = false;

    /* JADX INFO: renamed from: xg */
    public String f408xg = "";

    /* JADX INFO: renamed from: yg */
    public String f409yg = "";

    /* JADX INFO: renamed from: zg */
    public String f410zg = "";

    /* JADX INFO: renamed from: Ag */
    public String f362Ag = "";

    /* JADX INFO: renamed from: Dg */
    public int f365Dg = -1;

    /* JADX INFO: renamed from: Eg */
    public int f366Eg = -1;

    /* JADX INFO: renamed from: Fg */
    public int f367Fg = 0;

    /* JADX INFO: renamed from: Gg */
    public boolean f368Gg = false;

    /* JADX INFO: renamed from: Kg */
    public int f372Kg = 0;

    /* JADX INFO: renamed from: Lg */
    public int f373Lg = 0;
    public int mSource = -1;

    /* JADX INFO: renamed from: Mg */
    public boolean f374Mg = false;

    /* JADX INFO: renamed from: Ng */
    public boolean f375Ng = false;

    /* JADX INFO: renamed from: Og */
    public boolean f376Og = false;

    /* JADX INFO: renamed from: Pg */
    public boolean f377Pg = false;

    /* JADX INFO: renamed from: Tg */
    public int f381Tg = 1;

    /* JADX INFO: renamed from: Ug */
    public int f382Ug = 0;

    /* JADX INFO: renamed from: Vg */
    public int f383Vg = 0;

    /* JADX INFO: renamed from: Wg */
    public boolean f384Wg = false;

    /* JADX INFO: renamed from: Xg */
    public boolean f385Xg = false;

    /* JADX INFO: renamed from: Yg */
    public String f386Yg = "";

    /* JADX INFO: renamed from: _g */
    public ArrayList<C0718b> f388_g = new ArrayList<>();

    /* JADX INFO: renamed from: ch */
    public ArrayList<C0718b> f389ch = new ArrayList<>();

    /* JADX INFO: renamed from: dh */
    public ArrayList<TWContact> f390dh = new ArrayList<>();

    /* JADX INFO: renamed from: eh */
    public ArrayList<TWContact> f391eh = new ArrayList<>();

    /* JADX INFO: renamed from: fh */
    public ArrayList<TWContact> f392fh = new ArrayList<>();

    /* JADX INFO: renamed from: gh */
    public ArrayList<TWContact> f393gh = new ArrayList<>();

    /* JADX INFO: renamed from: hh */
    public ArrayList<C0719c> f394hh = new ArrayList<>();

    /* JADX INFO: renamed from: ih */
    public ArrayList<C0719c> f395ih = new ArrayList<>();

    /* JADX INFO: renamed from: jh */
    public ArrayList<C0719c> f396jh = new ArrayList<>();

    /* JADX INFO: renamed from: kh */
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

    /* JADX INFO: renamed from: gb */
    public boolean m199gb() {
        return TextUtils.equals(this.f386Yg, "airplay_wired");
    }

    /* JADX INFO: renamed from: hb */
    public boolean m200hb() {
        return TextUtils.equals(this.f386Yg, "android_mirror_wired") || TextUtils.equals(this.f386Yg, "android_mirror_wireless");
    }
}
