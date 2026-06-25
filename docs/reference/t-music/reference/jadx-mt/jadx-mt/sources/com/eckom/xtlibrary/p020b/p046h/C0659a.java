package com.eckom.xtlibrary.p020b.p046h;

import android.graphics.drawable.Drawable;
import com.eckom.xtlibrary.p020b.p046h.p047a.C0660a;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.h.a */
/* JADX INFO: compiled from: RadioDataHolder.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0659a {
    private static C0659a instance;

    /* JADX INFO: renamed from: Gi */
    public C0660a[] f734Gi;

    /* JADX INFO: renamed from: Sk */
    public int f744Sk;

    /* JADX INFO: renamed from: Tk */
    public int f745Tk;

    /* JADX INFO: renamed from: Uk */
    public int f746Uk;

    /* JADX INFO: renamed from: Vk */
    public int f747Vk;

    /* JADX INFO: renamed from: Xk */
    public int f749Xk;

    /* JADX INFO: renamed from: Yk */
    public boolean f750Yk;

    /* JADX INFO: renamed from: Zk */
    public boolean f751Zk;

    /* JADX INFO: renamed from: _k */
    public boolean f752_k;

    /* JADX INFO: renamed from: cl */
    public boolean f753cl;

    /* JADX INFO: renamed from: dl */
    public boolean f754dl;

    /* JADX INFO: renamed from: el */
    public boolean f755el;

    /* JADX INFO: renamed from: fl */
    public boolean f756fl;

    /* JADX INFO: renamed from: gl */
    public String f757gl;

    /* JADX INFO: renamed from: hl */
    public String f758hl;

    /* JADX INFO: renamed from: il */
    public boolean f759il;

    /* JADX INFO: renamed from: jl */
    public int f760jl;

    /* JADX INFO: renamed from: kl */
    public int f761kl;

    /* JADX INFO: renamed from: ll */
    public int f762ll;
    public int mRegion;
    public int mSource;

    /* JADX INFO: renamed from: ml */
    public Drawable f763ml;

    /* JADX INFO: renamed from: nl */
    public int f764nl;

    /* JADX INFO: renamed from: ol */
    public boolean f765ol;

    /* JADX INFO: renamed from: pl */
    public String f766pl;

    /* JADX INFO: renamed from: ql */
    public int f767ql;

    /* JADX INFO: renamed from: Jk */
    public int f735Jk = 10800;

    /* JADX INFO: renamed from: Kk */
    public int f736Kk = 8750;

    /* JADX INFO: renamed from: Lk */
    public int f737Lk = 10800;

    /* JADX INFO: renamed from: Mk */
    public int f738Mk = 8750;

    /* JADX INFO: renamed from: Nk */
    public int f739Nk = 1629;

    /* JADX INFO: renamed from: Ok */
    public int f740Ok = 522;

    /* JADX INFO: renamed from: Pk */
    public int f741Pk = 5;

    /* JADX INFO: renamed from: Qk */
    public int f742Qk = 5;

    /* JADX INFO: renamed from: Rk */
    public int f743Rk = 9;

    /* JADX INFO: renamed from: Wk */
    public int f748Wk = 8750;

    /* JADX INFO: renamed from: rl */
    public String[] f768rl = {"  None  ", "  News  ", "Affairs ", "  Info  ", " Sport  ", "Educate ", " Drama  ", "Culture ", "Science ", " Varied ", " Pop M  ", " Rock M ", " Easy M ", "Light M ", "Classics", "Other M ", "Weather ", "Finance ", "Children", " Social ", "Religion", "Phone In", " Travel ", "Leisure ", "  Jazz  ", "Country ", "Nation M", " Oldies ", " Folk M ", "Document", "  Test  ", " Alarm  "};

    private C0659a() {
        m808_e();
    }

    /* JADX INFO: renamed from: _e */
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
