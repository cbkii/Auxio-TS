package com.eckom.xtlibrary.p020b.p037f.p042e;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.SystemProperties;
import com.eckom.xtlibrary.p020b.p031c.InterfaceC0563b;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a;
import com.eckom.xtlibrary.p020b.p037f.p041d.C0593L;
import com.eckom.xtlibrary.p020b.p037f.p041d.C0601U;
import com.eckom.xtlibrary.p020b.p037f.p041d.C0610ba;
import com.eckom.xtlibrary.p020b.p037f.p041d.C0628t;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.e.a */
/* JADX INFO: compiled from: MusicPresenter.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0635a extends AbstractC0658a<InterfaceC0657b, AbstractC0607a> implements InterfaceC0656a {

    /* JADX INFO: renamed from: Hk */
    public InterfaceC0563b f671Hk;

    public C0635a(Context context) {
        super(context, false, false);
    }

    /* JADX INFO: renamed from: Ab */
    public void m708Ab() {
        ((AbstractC0607a) this.mModel).mo505Ab();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: B */
    public void mo709B(int i) {
        if (get() != null) {
            get().mo795B(i);
        }
    }

    /* JADX INFO: renamed from: Bb */
    public void m710Bb() {
        ((AbstractC0607a) this.mModel).mo506Bb();
    }

    /* JADX INFO: renamed from: Cb */
    public void m711Cb() {
        ((AbstractC0607a) this.mModel).mo507Cb();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: D */
    public void mo712D(int i) {
        if (get() != null) {
            get().mo796D(i);
        }
    }

    /* JADX INFO: renamed from: Db */
    public void m713Db() {
        ((AbstractC0607a) this.mModel).mo508Db();
    }

    /* JADX INFO: renamed from: Ea */
    public void m714Ea(String str) {
        ((AbstractC0607a) this.mModel).mo509Ea(str);
    }

    /* JADX INFO: renamed from: Eb */
    public void m715Eb() {
        ((AbstractC0607a) this.mModel).mo510Eb();
    }

    /* JADX INFO: renamed from: Gb */
    public void m716Gb() {
        ((AbstractC0607a) this.mModel).mo513Gb();
    }

    /* JADX INFO: renamed from: Hb */
    public void m717Hb() {
        ((AbstractC0607a) this.mModel).mo514Hb();
    }

    /* JADX INFO: renamed from: Ib */
    public void m718Ib() {
        ((AbstractC0607a) this.mModel).mo515Ib();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: L */
    public void mo719L() {
        if (get() != null) {
            get().mo797L();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: a */
    public void mo721a(C0580g c0580g) {
        if (get() != null) {
            get().mo799a(c0580g);
        }
    }

    /* JADX INFO: renamed from: ba */
    public void m726ba() {
        ((AbstractC0607a) this.mModel).mo511Fb();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: c */
    public void mo727c(boolean z) {
        if (get() != null) {
            get().mo800a(Boolean.valueOf(z));
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: d */
    public void mo728d(int i, int i2) {
        if (get() != null) {
            get().mo803d(i, i2);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: f */
    public void mo729f(boolean z) {
        if (get() != null) {
            get().mo804f(z);
        }
    }

    /* JADX INFO: renamed from: fa */
    public void m730fa() {
        ((AbstractC0607a) this.mModel).mo529Va();
    }

    @Override // com.eckom.xtlibrary.p020b.p045g.AbstractC0658a
    public AbstractC0607a getModel() {
        return null;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: h */
    public void mo731h(boolean z) {
        if (get() != null) {
            get().mo805h(z);
        }
    }

    /* JADX INFO: renamed from: la */
    public void m732la(int i) {
        ((AbstractC0607a) this.mModel).mo537la(i);
    }

    @Override // com.eckom.xtlibrary.p020b.p045g.AbstractC0658a
    public void onDestroy() {
        m739yc();
    }

    public void onPause() {
        ((AbstractC0607a) this.mModel).onPause();
    }

    public void onResume() {
        ((AbstractC0607a) this.mModel).onResume();
    }

    /* JADX INFO: renamed from: pa */
    public void m733pa(int i) {
        ((AbstractC0607a) this.mModel).mo536ka(i);
    }

    /* JADX INFO: renamed from: pb */
    public void m734pb() {
        ((AbstractC0607a) this.mModel).mo539pb();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: q */
    public void mo735q(boolean z) {
        if (get() != null) {
            get().mo806q(z);
        }
    }

    /* JADX INFO: renamed from: rb */
    public void m736rb() {
        ((AbstractC0607a) this.mModel).mo542rb();
    }

    public void seekTo(int i) {
        ((AbstractC0607a) this.mModel).seekTo(i);
    }

    /* JADX INFO: renamed from: w */
    public void m737w(boolean z) {
        M m = this.mModel;
        if (m != 0) {
            ((AbstractC0607a) m).mo544w(z);
        }
    }

    /* JADX INFO: renamed from: xc */
    public void m738xc() {
        ((AbstractC0607a) this.mModel).mo532a(this, this.mContext);
    }

    /* JADX INFO: renamed from: yc */
    public void m739yc() {
        ((AbstractC0607a) this.mModel).mo531a(this);
    }

    @Override // com.eckom.xtlibrary.p020b.p045g.AbstractC0658a
    /* JADX INFO: renamed from: b */
    public AbstractC0607a mo723b(boolean z, boolean z2) {
        boolean z3 = SystemProperties.getInt("persist.media.type", 0) == 1;
        boolean z4 = SystemProperties.getInt("persist.music.id3", 0) == 1;
        return ((z || z4) && (z2 || z3)) ? C0593L.getInstant() : (z2 || z3) ? C0601U.getInstant() : (z || z4) ? C0628t.getInstant() : C0610ba.getInstant();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: a */
    public void mo720a(MediaPlayer mediaPlayer) {
        if (get() != null) {
            get().mo798a(mediaPlayer);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: a */
    public void mo722a(TWMediaPlayer tWMediaPlayer) {
        if (get() != null) {
            get().mo802a(tWMediaPlayer);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a
    /* JADX INFO: renamed from: b */
    public void mo725b(String str, String str2, String str3, Bitmap bitmap, String str4, String str5, int i) {
        if (get() != null) {
            get().mo801a(str, str2, str3, bitmap, str4, str5, i);
        }
    }

    /* JADX INFO: renamed from: b */
    public void m724b(C0579f c0579f, boolean z) {
        ((AbstractC0607a) this.mModel).mo534b(c0579f, z);
    }
}
