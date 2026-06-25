package com.eckom.xtlibrary.p020b.p054k.p056b;

import android.os.SystemProperties;
import android.view.View;
import com.eckom.xtlibrary.p020b.p031c.InterfaceC0565d;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p054k.p055a.C0705b;
import com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b;
import com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0709c;
import com.eckom.xtlibrary.twproject.video.model.BaseVideoMode;
import com.eckom.xtlibrary.twproject.video.model.C0735m;
import com.eckom.xtlibrary.twproject.video.model.C0748z;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.k.b.a */
/* JADX INFO: compiled from: VideoPresenter.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0706a extends AbstractC0658a<InterfaceC0709c, BaseVideoMode> implements InterfaceC0708b {

    /* JADX INFO: renamed from: Ik */
    public static boolean f855Ik;

    /* JADX INFO: renamed from: Hk */
    public InterfaceC0565d f856Hk;

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: D */
    public void mo1051D(int i) {
        get().mo1075D(i);
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: L */
    public void mo1052L() {
        if (get() != null) {
            get().mo1076L();
        }
    }

    /* JADX INFO: renamed from: P */
    public void m1053P() {
        ((BaseVideoMode) this.mModel).mo1154P();
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: Y */
    public void mo1055Y(int i) {
        if (get() != null) {
            get().mo1078Y(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: a */
    public void mo1056a(C0705b c0705b) {
        if (get() != null) {
            get().mo1079a(c0705b);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: b */
    public void mo1057b(C0705b c0705b) {
        if (get() != null) {
            get().mo1081b(c0705b);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: c */
    public void mo1058c(boolean z) {
        if (get() != null) {
            get().mo1080a(Boolean.valueOf(z));
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: d */
    public void mo1059d(int i, int i2) {
        get().mo1082d(i, i2);
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: f */
    public void mo1060f(boolean z) {
        if (get() != null) {
            get().mo1084f(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: fa */
    public void mo1061fa(String str) {
        if (get() != null) {
            get().mo1085fa(str);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: g */
    public void mo1062g(boolean z) {
        if (get() != null) {
            get().mo1083d(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: h */
    public void mo1063h(boolean z) {
        if (get() != null) {
            get().mo1086h(z);
        }
    }

    /* JADX INFO: renamed from: ic */
    public void m1064ic() {
        ((BaseVideoMode) this.mModel).mo1156ic();
    }

    /* JADX INFO: renamed from: jc */
    public void m1065jc() {
        ((BaseVideoMode) this.mModel).mo1157jc();
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: l */
    public void mo1066l(boolean z) {
        if (get() != null) {
            get().mo1088l(z);
        }
    }

    /* JADX INFO: renamed from: ma */
    public void m1067ma() {
        ((BaseVideoMode) this.mModel).mo1158ma();
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: n */
    public void mo1068n(boolean z) {
        if (get() != null) {
            get().mo1089n(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p045g.AbstractC0658a
    public void onDestroy() {
        ((BaseVideoMode) this.mModel).mo1155Pb();
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    public void onMediaView(View view) {
        if (get() != null) {
            get().onMediaView(view);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: q */
    public void mo1069q(boolean z) {
        if (get() != null) {
            get().mo1091q(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    public void setSource(int i) {
        if (get() != null) {
            get().mo1090q(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: u */
    public void mo1070u(int i) {
        if (get() != null) {
            get().mo1087l(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: v */
    public void mo1072v(int i) {
        if (get() != null) {
            get().mo1093v(i);
        }
    }

    /* JADX INFO: renamed from: w */
    public void m1074w(boolean z) {
        M m = this.mModel;
        if (m != 0) {
            ((BaseVideoMode) m).mo1159w(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p045g.AbstractC0658a
    public BaseVideoMode getModel() {
        f855Ik = SystemProperties.getInt("persist.media.type", 0) == 1;
        return f855Ik ? C0735m.getInstant() : C0748z.getInstant();
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: Y */
    public void mo1054Y() {
        if (get() != null) {
            get().mo1077Y();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: u */
    public void mo1071u(String str, String str2) {
        if (get() != null) {
            get().mo1092u(str, str2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b
    /* JADX INFO: renamed from: v */
    public void mo1073v(boolean z) {
        if (get() != null) {
            get().mo1094v(z);
        }
    }
}
