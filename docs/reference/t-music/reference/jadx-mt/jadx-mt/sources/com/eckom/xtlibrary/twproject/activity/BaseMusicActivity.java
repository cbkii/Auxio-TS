package com.eckom.xtlibrary.twproject.activity;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p042e.C0635a;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b;
import com.eckom.xtlibrary.p020b.p052i.C0683m;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayer;

/* JADX INFO: loaded from: classes3.dex */
public abstract class BaseMusicActivity extends XTActivity<C0635a> implements InterfaceC0657b {
    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: B */
    public void mo795B(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: D */
    public void mo796D(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ha */
    public String mo1101Ha() {
        return null;
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ia */
    public String mo1102Ia() {
        return "com.tw.music.theme";
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ja */
    public String mo1103Ja() {
        return "/data/tw/theme/default/Sub/MusicTheme.apk";
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: Ka */
    public C0683m mo1104Ka() {
        return null;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: L */
    public void mo797L() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: a */
    public void mo798a(MediaPlayer mediaPlayer) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: a */
    public void mo799a(C0580g c0580g) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: a */
    public void mo800a(Boolean bool) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: a */
    public void mo801a(String str, String str2, String str3, Bitmap bitmap, String str4, String str5, int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: a */
    public void mo802a(TWMediaPlayer tWMediaPlayer) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: c */
    public void mo1105c(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: d */
    public void mo803d(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* JADX INFO: renamed from: d */
    public void mo1106d(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: f */
    public void mo804f(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: h */
    public void mo805h(boolean z) {
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        ((C0635a) this.mPresenter).m737w(false);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ((C0635a) this.mPresenter).m738xc();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        ((C0635a) this.mPresenter).m739yc();
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        ((C0635a) this.mPresenter).onPause();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        if (Build.VERSION.SDK_INT != 30) {
            ((C0635a) this.mPresenter).onResume();
        }
        super.onResume();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (Build.VERSION.SDK_INT == 30 && z) {
            ((C0635a) this.mPresenter).onResume();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* JADX INFO: renamed from: q */
    public void mo806q(boolean z) {
    }
}
