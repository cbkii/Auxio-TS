package com.eckom.xtlibrary.twproject.activity;

import android.os.Bundle;
import com.eckom.xtlibrary.p020b.p032d.p034b.C0568a;
import com.eckom.xtlibrary.p020b.p032d.p035c.InterfaceC0569a;
import com.eckom.xtlibrary.p020b.p052i.C0683m;

/* JADX INFO: loaded from: classes3.dex */
public abstract class BaseLauncherActivity extends XTActivity<C0568a> implements InterfaceC0569a {

    /* JADX INFO: renamed from: Va */
    private String f860Va = "";

    /* JADX INFO: renamed from: Da */
    public abstract String m1108Da();

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ha */
    public String mo1101Ha() {
        return null;
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ia */
    public String mo1102Ia() {
        return "com.tw.launcher.theme";
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ja */
    public String mo1103Ja() {
        return "/data/tw/theme/default/Launcher/LauncherTheme.apk";
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ka */
    public C0683m mo1104Ka() {
        return null;
    }

    @Override // com.eckom.xtlibrary.p020b.p032d.p035c.InterfaceC0569a
    /* JADX INFO: renamed from: b */
    public void mo424b(Bundle bundle) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: c */
    public void mo1105c(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: d */
    public void mo1106d(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        P p = this.mPresenter;
        if (p != 0) {
            ((C0568a) p).onCreate();
        }
        this.f860Va = m1108Da();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        P p = this.mPresenter;
        if (p != 0) {
            ((C0568a) p).m420Ra(this.f860Va);
            ((C0568a) this.mPresenter).onDestroy();
        }
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        P p = this.mPresenter;
        if (p != 0) {
            ((C0568a) p).m422Ua("BaseLauncherActivity");
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        P p = this.mPresenter;
        if (p != 0) {
            ((C0568a) p).m421Ta("BaseLauncherActivity");
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: za */
    public C0568a mo1107za() {
        return new C0568a(this);
    }
}
