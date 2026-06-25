package com.eckom.xtlibrary.twproject.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.WindowManagerGlobal;
import com.eckom.xtlibrary.p066b.p052i.C0683m;
import com.eckom.xtlibrary.p066b.p067a.p023b.C0533a;
import com.eckom.xtlibrary.p066b.p067a.p026e.C0549a;
import com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a;
import com.eckom.xtlibrary.twproject.p072bt.bean.TWContact;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public abstract class BaseBTActivity extends XTActivity<C0549a> implements InterfaceC0551a {

    /* renamed from: Va */
    private String f859Va = "";

    @TargetApi(24)
    /* renamed from: oe */
    private void m1099oe() {
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                C0533a.getInstance().f375Ng = isInMultiWindowMode();
            } else if (Build.VERSION.SDK_INT >= 24) {
                C0533a.getInstance().f375Ng = WindowManagerGlobal.getWindowManagerService().getDockedStackSide() > 0;
            }
        } catch (Exception e) {
            Log.e("BaseBTActivity", "getMultiWindowMode:" + e.getMessage());
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: C */
    public void mo325C(int i) {
    }

    /* renamed from: Da */
    public abstract String m1100Da();

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: H */
    public void mo326H(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: Ha */
    public String mo1101Ha() {
        return null;
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: I */
    public void mo327I() {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: Ia */
    public String mo1102Ia() {
        return "com.tw.bt.theme";
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: J */
    public void mo328J(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: Ja */
    public String mo1103Ja() {
        return "/data/tw/theme/default/Sub/BluetoothTheme.apk";
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: Ka */
    public C0683m mo1104Ka() {
        return null;
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

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: c */
    public void mo1105c(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: ca */
    public void mo344ca(String str) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: d */
    public void mo1106d(C0683m c0683m) {
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

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        ((C0549a) this.mPresenter).m323w(false);
        if (SystemProperties.getInt("persist.tw.bt.module", 2) == 0) {
            ((C0549a) this.mPresenter).m316ba();
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        m1099oe();
        super.onCreate(bundle);
        this.f859Va = m1100Da();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        P p = this.mPresenter;
        if (p != 0) {
            ((C0549a) p).m313Ra(this.f859Va);
        }
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        ((C0549a) this.mPresenter).m311B(false);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        ((C0549a) this.mPresenter).m311B(true);
    }

    @Override // com.eckom.xtlibrary.p066b.p067a.p028g.InterfaceC0551a
    /* renamed from: p */
    public void mo360p(int i) {
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
    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: za */
    public C0549a mo1107za() {
        return new C0549a(this);
    }
}
