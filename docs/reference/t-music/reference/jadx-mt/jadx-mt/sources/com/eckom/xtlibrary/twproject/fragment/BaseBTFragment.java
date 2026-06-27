package com.eckom.xtlibrary.twproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0533a;
import com.eckom.xtlibrary.p020b.p021a.p026e.C0549a;
import com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a;
import com.eckom.xtlibrary.twproject.p059bt.bean.TWContact;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes3.dex */
public abstract class BaseBTFragment extends XTFragment<C0549a> implements InterfaceC0551a {
    protected Context mContext;

    /* JADX INFO: renamed from: la */
    protected C0533a f894la = C0533a.getInstance();

    /* JADX INFO: renamed from: ma */
    public String f895ma = "";

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: C */
    public void mo325C(int i) {
        Log.d("BaseBTFragment", "onDeviceHFP: " + i);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: H */
    public void mo326H(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: I */
    public void mo327I() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: J */
    public void mo328J(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: M */
    public void mo329M(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: N */
    public void mo330N() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: P */
    public void mo331P(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: Q */
    public void mo332Q(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: T */
    public void mo333T() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: W */
    public void mo334W() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: X */
    public void mo335X() {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: a */
    public void mo336a(int i, String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: a */
    public void mo337a(int i, String str, String str2) {
        Log.d("BaseBTFragment", "onPairInfo: " + i + " pairName: pairMac:" + str2);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: a */
    public void mo338a(int i, boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: a */
    public void mo339a(ArrayList<TWContact> arrayList) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: aa */
    public void mo340aa(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: b */
    public void mo341b(int i, String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: b */
    public void mo342b(int i, boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: b */
    public void mo343b(ArrayList<TWContact> arrayList) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: ca */
    public void mo344ca(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: da */
    public void mo345da(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: e */
    public void mo346e(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: e */
    public void mo347e(int i, String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: f */
    public void mo348f(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: f */
    public void mo349f(int i, String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: f */
    public void mo350f(String str, String str2, String str3) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: f */
    public void mo351f(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: ga */
    public void mo352ga(String str) {
        Log.d("BaseBTFragment", "onBtNameInfo: " + str);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: i */
    public void mo353i(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: j */
    public void mo354j(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: ka */
    public void mo355ka(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: m */
    public void mo356m(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: n */
    public void mo357n(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: o */
    public void mo358o(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: o */
    public void mo359o(boolean z) {
    }

    @Override // com.eckom.xtlibrary.twproject.fragment.XTFragment, android.app.Fragment
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
    }

    @Override // com.eckom.xtlibrary.twproject.fragment.XTFragment, android.app.Fragment
    public void onDestroy() {
        P p = this.mPresenter;
        if (p != 0) {
            ((C0549a) p).m314Sa(this.f895ma);
        }
        super.onDestroy();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        m1136sa();
    }

    @Override // com.eckom.xtlibrary.twproject.fragment.XTFragment, android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.f895ma = m1137ta();
        m1138a(view);
        ((C0549a) this.mPresenter).m315Ta(this.f895ma);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: p */
    public void mo360p(int i) {
        Log.d("BaseBTFragment", "onDeviceHFPInfo: " + i);
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: p */
    public void mo361p(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: q */
    public void mo362q(String str, String str2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: q */
    public void mo363q(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: s */
    public void mo364s(String str, String str2) {
    }

    /* JADX INFO: renamed from: sa */
    public abstract void m1136sa();

    /* JADX INFO: renamed from: ta */
    public abstract String m1137ta();

    @Override // com.eckom.xtlibrary.p020b.p021a.p028g.InterfaceC0551a
    /* JADX INFO: renamed from: w */
    public void mo365w(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.fragment.XTFragment
    /* JADX INFO: renamed from: ra */
    public C0549a mo1135ra() {
        return new C0549a(this.mContext);
    }
}
