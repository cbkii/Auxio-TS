package com.eckom.xtlibrary.twproject.activity;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p042e.C0635a;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b;
import com.eckom.xtlibrary.twproject.service.XTService;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayer;

/* loaded from: classes3.dex */
public abstract class BaseMusicService extends XTService<C0635a> implements InterfaceC0657b {
    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: B */
    public void mo795B(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: D */
    public void mo796D(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: L */
    public void mo797L() {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo798a(MediaPlayer mediaPlayer) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo799a(C0580g c0580g) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo800a(Boolean bool) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo801a(String str, String str2, String str3, Bitmap bitmap, String str4, String str5, int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo802a(TWMediaPlayer tWMediaPlayer) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: d */
    public void mo803d(int i, int i2) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: f */
    public void mo804f(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: h */
    public void mo805h(boolean z) {
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onCreate() {
        super.onCreate();
        ((C0635a) this.mPresenter).m738xc();
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onDestroy() {
        ((C0635a) this.mPresenter).m739yc();
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: q */
    public void mo806q(boolean z) {
    }
}
