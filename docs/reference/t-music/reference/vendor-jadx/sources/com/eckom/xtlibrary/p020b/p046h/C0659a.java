package com.eckom.xtlibrary.p020b.p046h;

import android.graphics.drawable.Drawable;
import com.eckom.xtlibrary.p020b.p046h.p047a.C0660a;

/* compiled from: RadioDataHolder.java */
/* renamed from: com.eckom.xtlibrary.b.h.a */
/* loaded from: classes3.dex */
public class C0659a {
    private static C0659a instance;

    /* renamed from: Gi */
    public C0660a[] f734Gi;

    /* renamed from: Sk */
    public int f744Sk;

    /* renamed from: Tk */
    public int f745Tk;

    /* renamed from: Uk */
    public int f746Uk;

    /* renamed from: Vk */
    public int f747Vk;

    /* renamed from: Xk */
    public int f749Xk;

    /* renamed from: Yk */
    public boolean f750Yk;

    /* renamed from: Zk */
    public boolean f751Zk;

    /* renamed from: _k */
    public boolean f752_k;

    /* renamed from: cl */
    public boolean f753cl;

    /* renamed from: dl */
    public boolean f754dl;

    /* renamed from: el */
    public boolean f755el;

    /* renamed from: fl */
    public boolean f756fl;

    /* renamed from: gl */
    public String f757gl;

    /* renamed from: hl */
    public String f758hl;

    /* renamed from: il */
    public boolean f759il;

    /* renamed from: jl */
    public int f760jl;

    /* renamed from: kl */
    public int f761kl;

    /* renamed from: ll */
    public int f762ll;
    public int mRegion;
    public int mSource;

    /* renamed from: ml */
    public Drawable f763ml;

    /* renamed from: nl */
    public int f764nl;

    /* renamed from: ol */
    public boolean f765ol;

    /* renamed from: pl */
    public String f766pl;

    /* renamed from: ql */
    public int f767ql;

    /* renamed from: Jk */
    public int f735Jk = 10800;

    /* renamed from: Kk */
    public int f736Kk = 8750;

    /* renamed from: Lk */
    public int f737Lk = 10800;

    /* renamed from: Mk */
    public int f738Mk = 8750;

    /* renamed from: Nk */
    public int f739Nk = 1629;

    /* renamed from: Ok */
    public int f740Ok = 522;

    /* renamed from: Pk */
    public int f741Pk = 5;

    /* renamed from: Qk */
    public int f742Qk = 5;

    /* renamed from: Rk */
    public int f743Rk = 9;

    /* renamed from: Wk */
    public int f748Wk = 8750;

    /* renamed from: rl */
    public String[] f768rl = {"  None  ", "  News  ", "Affairs ", "  Info  ", " Sport  ", "Educate ", " Drama  ", "Culture ", "Science ", " Varied ", " Pop M  ", " Rock M ", " Easy M ", "Light M ", "Classics", "Other M ", "Weather ", "Finance ", "Children", " Social ", "Religion", "Phone In", " Travel ", "Leisure ", "  Jazz  ", "Country ", "Nation M", " Oldies ", " Folk M ", "Document", "  Test  ", " Alarm  "};

    private C0659a() {
        m808_e();
    }

    /* renamed from: _e */
    private void m808_e() {
        this.f734Gi = new C0660a[18];
        for (int i = 0; i < 18; i++) {
            this.f734Gi[i] = new C0660a();
        }
    }

    public static C0659a getInstance() {
        if (instance == null) {
            synchronized (C0659a.class) {
                if (instance == null) {
                    instance = new C0659a();
                }
            }
        }
        return instance;
    }
}
