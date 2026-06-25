package com.eckom.xtlibrary.p020b.p021a.p026e;

import android.content.Context;
import android.os.SystemProperties;
import com.eckom.xtlibrary.p020b.p021a.p025d.AbstractC0546h;
import com.eckom.xtlibrary.p020b.p021a.p025d.C0544f;
import com.eckom.xtlibrary.p020b.p021a.p025d.C0548j;
import com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g;
import com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a;
import com.eckom.xtlibrary.p020b.p031c.InterfaceC0562a;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.twproject.p059bt.bean.TWContact;
import java.util.ArrayList;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.e.a */
/* JADX INFO: compiled from: BTPresenter.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0549a extends AbstractC0658a<InterfaceC0551a, AbstractC0546h> implements InterfaceC0545g {

    /* JADX INFO: renamed from: Hk */
    public InterfaceC0562a f439Hk;
    private Context mContext;

    public C0549a(Context context) {
        super(context);
        this.mContext = context;
        onCreate();
    }

    private void onCreate() {
        ((AbstractC0546h) this.mModel).mo251a(this.mContext);
    }

    /* JADX INFO: renamed from: Aa */
    public void m310Aa(String str) {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo247Aa(str);
        }
    }

    /* JADX INFO: renamed from: B */
    public void m311B(boolean z) {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo248B(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: C */
    public void mo269C(int i) {
        if (get() != null) {
            get().mo325C(i);
        }
    }

    /* JADX INFO: renamed from: Ca */
    public void m312Ca(String str) {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo250Ca(str);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: H */
    public void mo270H(int i) {
        if (get() != null) {
            get().mo326H(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: I */
    public void mo271I() {
        if (get() != null) {
            get().mo327I();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: J */
    public void mo272J(int i) {
        if (get() != null) {
            get().mo328J(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: M */
    public void mo273M(int i) {
        if (get() != null) {
            get().mo329M(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: N */
    public void mo274N() {
        if (get() != null) {
            get().mo330N();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: P */
    public void mo276P(int i) {
        if (get() != null) {
            get().mo331P(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: Q */
    public void mo277Q(int i) {
        if (get() != null) {
            get().mo332Q(i);
        }
    }

    /* JADX INFO: renamed from: Ra */
    public void m313Ra(String str) {
        ((AbstractC0546h) this.mModel).mo249Ba(str);
    }

    /* JADX INFO: renamed from: Sa */
    public void m314Sa(String str) {
        ((AbstractC0546h) this.mModel).mo249Ba(str);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: T */
    public void mo278T() {
        if (get() != null) {
            get().mo333T();
        }
    }

    /* JADX INFO: renamed from: Ta */
    public void m315Ta(String str) {
        ((AbstractC0546h) this.mModel).mo252a(str, this);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: W */
    public void mo279W() {
        if (get() != null) {
            get().mo334W();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: X */
    public void mo280X() {
        if (get() != null) {
            get().mo335X();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: a */
    public void mo283a(int i, boolean z) {
        if (get() != null) {
            get().mo338a(i, z);
        }
    }

    public void answer() {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).answer();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: b */
    public void mo286b(int i, boolean z) {
        if (get() != null) {
            get().mo342b(i, z);
        }
    }

    /* JADX INFO: renamed from: ba */
    public void m316ba() {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo254ba();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: da */
    public void mo288da(String str) {
        if (get() != null) {
            get().mo345da(str);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: e */
    public void mo290e(int i, String str, String str2) {
        if (get() != null) {
            get().mo347e(i, str, str2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: f */
    public void mo292f(int i, int i2) {
        if (get() != null) {
            get().mo348f(i, i2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: ga */
    public void mo295ga(String str) {
        if (get() != null) {
            get().mo352ga(str);
        }
    }

    public int getCallState() {
        M m = this.mModel;
        if (m != 0) {
            return ((AbstractC0546h) m).getCallState();
        }
        return 0;
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: i */
    public void mo296i(int i, int i2) {
        if (get() != null) {
            get().mo353i(i, i2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: j */
    public void mo298j(boolean z) {
        if (get() != null) {
            get().mo359o(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: ja */
    public void mo299ja(String str) {
        if (get() != null) {
            get().mo344ca(str);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: ka */
    public void mo300ka(String str) {
        if (get() != null) {
            get().mo355ka(str);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: m */
    public void mo301m(String str, String str2) {
        if (get() != null) {
            get().mo356m(str, str2);
        }
    }

    /* JADX INFO: renamed from: mb */
    public int m317mb() {
        M m = this.mModel;
        if (m != 0) {
            return ((AbstractC0546h) m).mo255mb();
        }
        return 0;
    }

    /* JADX INFO: renamed from: nb */
    public void m318nb() {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo256nb();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: o */
    public void mo302o(String str, String str2) {
        if (get() != null) {
            get().mo358o(str, str2);
        }
    }

    /* JADX INFO: renamed from: ob */
    public void m319ob() {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo257ob();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: p */
    public void mo304p(String str, String str2) {
        if (get() != null) {
            get().mo361p(str, str2);
        }
    }

    /* JADX INFO: renamed from: pb */
    public void m320pb() {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo258pb();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: q */
    public void mo305q(String str, String str2) {
        if (get() != null) {
            get().mo362q(str, str2);
        }
    }

    /* JADX INFO: renamed from: qb */
    public void m321qb() {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo259qb();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: r */
    public void mo307r(String str, String str2) {
        if (get() != null) {
            get().mo357n(str, str2);
        }
    }

    /* JADX INFO: renamed from: rb */
    public void m322rb() {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo260rb();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: s */
    public void mo308s(String str, String str2) {
        if (get() != null) {
            get().mo364s(str, str2);
        }
    }

    public void setDeviceName(String str) {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).setDeviceName(str);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: w */
    public void mo309w(int i) {
        if (get() != null) {
            get().mo365w(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p045g.AbstractC0658a
    public AbstractC0546h getModel() {
        return SystemProperties.getInt("persist.tw.bt.module", 2) == 0 ? C0548j.getInstance() : C0544f.getInstance();
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: N */
    public void mo275N(int i) {
        if (get() != null) {
            get().mo340aa(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: a */
    public void mo284a(ArrayList<TWContact> arrayList) {
        if (get() != null) {
            get().mo339a(arrayList);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: b */
    public void mo287b(ArrayList<TWContact> arrayList) {
        if (get() != null) {
            get().mo343b(arrayList);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: e */
    public void mo291e(String str, String str2, String str3) {
        if (get() != null) {
            get().mo350f(str, str2, str3);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: f */
    public void mo293f(int i, String str, String str2) {
        if (get() != null) {
            get().mo349f(i, str, str2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: j */
    public void mo297j(int i) {
        if (get() != null) {
            get().mo354j(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: p */
    public void mo303p(int i) {
        if (get() != null) {
            get().mo360p(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: q */
    public void mo306q(boolean z) {
        if (get() != null) {
            get().mo363q(z);
        }
    }

    /* JADX INFO: renamed from: w */
    public void m323w(boolean z) {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0546h) m).mo198w(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: a */
    public void mo282a(int i, String str, String str2) {
        if (get() != null) {
            get().mo337a(i, str, str2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: b */
    public void mo285b(int i, String str, String str2) {
        if (get() != null) {
            get().mo341b(i, str, str2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: e */
    public void mo289e(int i, int i2) {
        if (get() != null) {
            get().mo346e(i, i2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: f */
    public void mo294f(boolean z) {
        if (get() != null) {
            get().mo351f(z);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p025d.InterfaceC0545g
    /* JADX INFO: renamed from: a */
    public void mo281a(int i, String str) {
        if (get() != null) {
            get().mo336a(i, str);
        }
    }
}
