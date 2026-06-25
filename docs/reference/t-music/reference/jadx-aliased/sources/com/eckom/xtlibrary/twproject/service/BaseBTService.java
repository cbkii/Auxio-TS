package com.eckom.xtlibrary.twproject.service;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p066b.p053j.C0699o;
import com.eckom.xtlibrary.p066b.p067a.p023b.C0533a;
import com.eckom.xtlibrary.p066b.p067a.p026e.C0549a;
import com.eckom.xtlibrary.p066b.p067a.p027f.C0550a;
import com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a;
import com.eckom.xtlibrary.twproject.p072bt.bean.TWContact;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public abstract class BaseBTService extends XTService<C0549a> implements InterfaceC0551a {

    /* renamed from: Sa */
    protected C0550a f904Sa;

    /* renamed from: la */
    protected C0533a f908la = C0533a.getInstance();

    /* renamed from: Ta */
    private String f905Ta = "";

    /* renamed from: Ua */
    private String f906Ua = "";

    /* renamed from: Va */
    private String f907Va = "";

    /* renamed from: ub */
    private void m1146ub(String str) {
        this.f905Ta = m1148Ba();
        if (TextUtils.isEmpty(this.f905Ta) || TextUtils.equals(this.f905Ta, str) || !C0699o.m1031b(this, "BaseBTService", "needSetDefaultName")) {
            return;
        }
        ((C0549a) this.mPresenter).setDeviceName(this.f905Ta);
        C0699o.m1028a((Context) this, "BaseBTService", "needSetDefaultName", false);
    }

    /* renamed from: vb */
    private void m1147vb(String str) {
        this.f906Ua = m1149Ca();
        if (TextUtils.isEmpty(this.f906Ua) || TextUtils.equals(this.f906Ua, str) || !C0699o.m1031b(this, "BaseBTService", "needSetDefaultPin")) {
            return;
        }
        ((C0549a) this.mPresenter).m312Ca(this.f906Ua);
        C0699o.m1028a((Context) this, "BaseBTService", "needSetDefaultPin", false);
    }

    /* renamed from: Ba */
    public abstract String m1148Ba();

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: C */
    public void mo325C(int i) {
    }

    /* renamed from: Ca */
    public abstract String m1149Ca();

    /* renamed from: Da */
    public abstract String m1150Da();

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: H */
    public void mo326H(int i) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: I */
    public void mo327I() {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: J */
    public void mo328J(int i) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: M */
    public void mo329M(int i) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: N */
    public void mo330N() {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: P */
    public void mo331P(int i) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: Q */
    public void mo332Q(int i) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: T */
    public void mo333T() {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: W */
    public void mo334W() {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: X */
    public void mo335X() {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: a */
    public void mo336a(int i, String str) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: a */
    public void mo337a(int i, String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: a */
    public void mo338a(int i, boolean z) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: a */
    public void mo339a(ArrayList<TWContact> arrayList) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: aa */
    public void mo340aa(int i) {
        if (i == 11) {
            C0550a c0550a = this.f904Sa;
            if (c0550a == null) {
                return;
            }
            c0550a.m324za("mute");
            throw null;
        }
        if (i == 12) {
            C0550a c0550a2 = this.f904Sa;
            if (c0550a2 == null) {
                return;
            }
            c0550a2.m324za("unmute");
            throw null;
        }
        switch (i) {
            case 1:
                ((C0549a) this.mPresenter).m319ob();
                return;
            case 2:
            default:
                return;
            case 3:
                ((C0549a) this.mPresenter).m320pb();
                return;
            case 4:
                ((C0549a) this.mPresenter).m322rb();
                return;
            case 5:
                C0533a c0533a = this.f908la;
                if (c0533a.mSource != 8 || c0533a.f368Gg) {
                    return;
                }
                ((C0549a) this.mPresenter).m319ob();
                return;
            case 6:
                C0533a c0533a2 = this.f908la;
                if (c0533a2.mSource == 8 && c0533a2.f368Gg) {
                    ((C0549a) this.mPresenter).m319ob();
                    return;
                }
                return;
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: b */
    public void mo341b(int i, String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: b */
    public void mo342b(int i, boolean z) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: b */
    public void mo343b(ArrayList<TWContact> arrayList) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: ca */
    public void mo344ca(String str) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: da */
    public void mo345da(String str) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: e */
    public void mo346e(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: e */
    public void mo347e(int i, String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: f */
    public void mo348f(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: f */
    public void mo349f(int i, String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: f */
    public void mo350f(String str, String str2, String str3) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: f */
    public void mo351f(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: ga */
    public void mo352ga(String str) {
        m1146ub(str);
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: i */
    public void mo353i(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: j */
    public void mo354j(int i) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: ka */
    public void mo355ka(String str) {
        m1147vb(str);
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: m */
    public void mo356m(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: n */
    public void mo357n(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: o */
    public void mo358o(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: o */
    public void mo359o(boolean z) {
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f907Va = m1150Da();
        ((C0549a) this.mPresenter).m315Ta(this.f907Va);
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: p */
    public void mo360p(int i) {
        Log.d("BaseBTService", "onDeviceHFPInfo: " + i);
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: p */
    public void mo361p(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: q */
    public void mo362q(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: q */
    public void mo363q(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: s */
    public void mo364s(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: w */
    public void mo365w(int i) {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.eckom.xtlibrary.twproject.service.XTService
    /* renamed from: za */
    public C0549a mo1151za() {
        return new C0549a(this);
    }
}
