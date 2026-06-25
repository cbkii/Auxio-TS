package com.eckom.xtlibrary.twproject.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import com.eckom.xtlibrary.p020b.p046h.p047a.C0660a;
import com.eckom.xtlibrary.p020b.p046h.p049c.C0667a;
import com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a;
import com.eckom.xtlibrary.p020b.p052i.C0683m;

/* JADX INFO: loaded from: classes3.dex */
public abstract class BaseRadioActivity extends XTActivity<C0667a> implements InterfaceC0670a {

    /* JADX INFO: renamed from: Va */
    private String f861Va = "";

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: A */
    public void mo895A(int i) {
    }

    /* JADX INFO: renamed from: Da */
    public abstract String m1109Da();

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: E */
    public void mo896E(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ha */
    public String mo1101Ha() {
        return null;
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: I */
    public void mo897I(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ia */
    public String mo1102Ia() {
        return "com.tw.radio.theme";
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ja */
    public String mo1103Ja() {
        return "/data/tw/theme/default/Sub/RadioTheme.apk";
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: K */
    public void mo898K(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ka */
    public C0683m mo1104Ka() {
        return null;
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: S */
    public void mo899S(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: a */
    public void mo900a(int i, int i2, int i3, int i4, int i5) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: a */
    public void mo901a(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: a */
    public void mo902a(C0660a[] c0660aArr) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: b */
    public void mo903b(Drawable drawable) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: ba */
    public void mo904ba(String str) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: c */
    public void mo1105c(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: d */
    public void mo1106d(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: e */
    public void mo905e(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: f */
    public void mo906f(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: ha */
    public void mo907ha(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: i */
    public void mo908i(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: k */
    public void mo909k(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: m */
    public void mo910m(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: n */
    public void mo911n(int i) {
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        ((C0667a) this.mPresenter).m894w(false);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f861Va = m1109Da();
        ((C0667a) this.mPresenter).m884Va(this.f861Va);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        P p = this.mPresenter;
        if (p != 0) {
            ((C0667a) p).m882Ra(this.f861Va);
        }
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        ((C0667a) this.mPresenter).m880B(false);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        if (Build.VERSION.SDK_INT != 30) {
            ((C0667a) this.mPresenter).m880B(true);
        }
        super.onResume();
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 && !((C0667a) this.mPresenter).m885Zb()) {
            ((C0667a) this.mPresenter).m894w(true);
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (Build.VERSION.SDK_INT == 30 && z) {
            ((C0667a) this.mPresenter).m883Ta(this.f861Va);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: p */
    public void mo912p(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: q */
    public void mo913q(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: r */
    public void mo914r(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: r */
    public void mo915r(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: s */
    public void mo916s(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: s */
    public void mo917s(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: t */
    public void mo918t(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: u */
    public void mo919u(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: x */
    public void mo920x(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* JADX INFO: renamed from: y */
    public void mo921y(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: za */
    public C0667a mo1107za() {
        return new C0667a(this);
    }
}
