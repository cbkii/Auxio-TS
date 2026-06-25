package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemProperties;
import android.support.v4.media.subtitle.Cea708CCParser;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0655t;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import com.eckom.xtlibrary.p020b.p053j.C0699o;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MusicID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.t */
/* loaded from: classes3.dex */
public class C0628t extends AbstractC0607a {

    /* renamed from: jd */
    private static C0655t f638jd;

    /* renamed from: Th */
    boolean f654Th;

    /* renamed from: Uh */
    private int f655Uh;

    /* renamed from: Vh */
    private String f656Vh;

    /* renamed from: Wh */
    public boolean f657Wh;

    /* renamed from: Xh */
    public boolean f658Xh;

    /* renamed from: Yc */
    C0578e f659Yc;

    /* renamed from: Yh */
    public boolean f660Yh;

    /* renamed from: Zh */
    public boolean f661Zh;

    /* renamed from: _h */
    public boolean f662_h;

    /* renamed from: di */
    public boolean f663di;

    /* renamed from: ei */
    public boolean f664ei;

    /* renamed from: fi */
    public boolean f665fi;
    private String fileName;

    /* renamed from: gi */
    public boolean f666gi;
    private final boolean isForward;
    private Context mContext;
    private final Handler mHandler;
    public MediaPlayer mMediaPlayer;

    /* renamed from: wg */
    private boolean f667wg;

    /* renamed from: hi */
    private static final ArrayList<InterfaceC0656a> f637hi = new ArrayList<>();

    /* renamed from: ji */
    private static C0628t f639ji = null;

    /* renamed from: Eh */
    private int f641Eh = 0;

    /* renamed from: Hh */
    private final int f642Hh = 0;

    /* renamed from: Ih */
    private final int f643Ih = 1;

    /* renamed from: Jh */
    private final int f644Jh = 2;

    /* renamed from: Kh */
    private final int f645Kh = 3;

    /* renamed from: Lh */
    private final int f646Lh = 1;

    /* renamed from: Mh */
    private final int f647Mh = 2;

    /* renamed from: Nh */
    private final int f648Nh = 4;

    /* renamed from: Oh */
    private final int f649Oh = 8;

    /* renamed from: Ph */
    private final int f650Ph = 128;

    /* renamed from: Qh */
    private boolean f651Qh = false;

    /* renamed from: Rh */
    private boolean f652Rh = false;

    /* renamed from: Sh */
    private boolean f653Sh = false;

    /* renamed from: Cg */
    private boolean f640Cg = false;

    private C0628t() {
        this.isForward = SystemProperties.getInt("persist.media.forward", 1) == 1;
        this.f654Th = false;
        this.mHandler = new Handler(new C0622n(this));
        this.f655Uh = -1;
        this.f656Vh = "";
        this.f657Wh = false;
        this.f658Xh = false;
        this.f660Yh = false;
        this.f661Zh = false;
        this.f662_h = false;
        this.f663di = false;
        this.f664ei = false;
        this.f665fi = false;
        this.f666gi = false;
    }

    /* renamed from: Ab */
    private void m642Ab(String str) {
        C0578e c0578e = this.f659Yc;
        c0578e.f522nd = "";
        c0578e.f523od = "";
        c0578e.f524pd = "";
        if (c0578e.f490Fb != null) {
            c0578e.f490Fb = null;
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            if (mediaMetadataRetriever.extractMetadata(12) != null) {
                this.f659Yc.f522nd = mediaMetadataRetriever.extractMetadata(2);
                this.f659Yc.f523od = mediaMetadataRetriever.extractMetadata(1);
                this.f659Yc.f524pd = mediaMetadataRetriever.extractMetadata(7);
                try {
                    if (TextUtils.isEmpty(this.f659Yc.f524pd) || this.f659Yc.f524pd.equals("")) {
                        this.f659Yc.f524pd = new File(str).getName();
                    }
                    if (TextUtils.isEmpty(this.f659Yc.f522nd) || this.f659Yc.f522nd.equals("")) {
                        this.f659Yc.f522nd = " ";
                    }
                    if (TextUtils.isEmpty(this.f659Yc.f523od) || this.f659Yc.f523od.equals("")) {
                        this.f659Yc.f523od = " ";
                    }
                    m661b(0, this.f659Yc.f522nd);
                    this.mHandler.postDelayed(new RunnableC0623o(this), 100L);
                    this.mHandler.postDelayed(new RunnableC0624p(this), 200L);
                } catch (Exception e) {
                    Log.e("MusicID3Model", Log.getStackTraceString(e));
                }
                m660a(this.f659Yc.f524pd, this.f659Yc.f522nd, this.f659Yc.f523od, str);
                byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
                if (embeddedPicture != null) {
                    this.f659Yc.f490Fb = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
                }
            } else {
                this.f659Yc.f524pd = new File(str).getName();
                this.f659Yc.f522nd = this.mContext.getString(R.string.unknownName);
                this.f659Yc.f523od = this.mContext.getString(R.string.unknownName);
            }
            mediaMetadataRetriever.release();
        } catch (Exception unused) {
            this.f659Yc.f524pd = new File(str).getName();
            this.f659Yc.f522nd = this.mContext.getString(R.string.unknownName);
            this.f659Yc.f523od = this.mContext.getString(R.string.unknownName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: L */
    public void m644L(boolean z) {
        if (z) {
            m691Mb().setVolume(0.5f, 0.5f);
        } else {
            m691Mb().setVolume(1.0f, 1.0f);
        }
    }

    /* renamed from: Pa */
    private boolean m645Pa(int i) {
        if (this.f655Uh == i) {
            return true;
        }
        this.f655Uh = i;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Re */
    public void m646Re() {
        C0578e c0578e = this.f659Yc;
        int[] iArr = c0578e.f519kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0578e.f518ic != 2) {
            c0578e.f520ld++;
        }
        m666c(0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Se */
    public void m647Se() {
        C0578e c0578e = this.f659Yc;
        int[] iArr = c0578e.f519kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0578e.f518ic != 2) {
            c0578e.f520ld--;
        }
        m666c(0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Te */
    public void m648Te() {
        m702ea(this.f659Yc.f482Ad);
        m707ta(this.f659Yc.f515ck);
        String str = this.f659Yc.f514_j;
        if (str != null && new File(str).canRead() && m686zb(this.f659Yc.f514_j) == 0) {
            seekTo(this.f659Yc.f521md);
            mo529Va();
        }
        C0578e c0578e = this.f659Yc;
        c0578e.f528ud = c0578e.f486Dd;
        if (c0578e.f528ud.f545kk == 0) {
            if (c0578e.f530vd.size() > 0) {
                C0578e c0578e2 = this.f659Yc;
                c0578e2.f528ud = c0578e2.f530vd.get(0);
            } else {
                C0578e c0578e3 = this.f659Yc;
                c0578e3.f528ud = c0578e3.m446uc();
            }
            C0578e c0578e4 = this.f659Yc;
            if (c0578e4.f528ud.f545kk == 0) {
                if (c0578e4.f532wd.size() > 0) {
                    C0578e c0578e5 = this.f659Yc;
                    c0578e5.f528ud = c0578e5.f532wd.get(0);
                } else {
                    C0578e c0578e6 = this.f659Yc;
                    c0578e6.f528ud = c0578e6.f525rj;
                }
                C0578e c0578e7 = this.f659Yc;
                if (c0578e7.f528ud.f545kk == 0) {
                    c0578e7.f528ud = c0578e7.f503Qj;
                    if (c0578e7.f528ud.f545kk == 0) {
                        c0578e7.f528ud = c0578e7.f486Dd;
                    }
                }
            }
        }
        this.mHandler.removeMessages(40454);
        this.mHandler.sendEmptyMessageDelayed(40454, 2000L);
    }

    /* renamed from: Ue */
    private String m649Ue() {
        return this.f659Yc.f522nd;
    }

    /* renamed from: Ve */
    private boolean m650Ve() {
        return this.mHandler.hasMessages(65282) || this.mHandler.hasMessages(65283);
    }

    /* renamed from: We */
    private void m651We() {
        C0529b.m178a(this.f659Yc.f515ck);
        C0643h.m757b(this.f659Yc.f491Fd, "/data/tw/.like", this.isForward, new C0616h(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Xe */
    public void m652Xe() {
        int duration = getDuration();
        int currentPosition = getCurrentPosition();
        if (duration < 0) {
            duration = 0;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        int i = (duration <= 0 || currentPosition > duration) ? 0 : (currentPosition * 100) / duration;
        String m692Nb = m692Nb();
        if (TextUtils.isEmpty(m692Nb)) {
            m692Nb = "";
        }
        int i2 = i & 127;
        f638jd.write(40704, 3, (isPlaying() ? 128 : 0) | i2, m692Nb);
        f638jd.write(771, 3, i2 | (isPlaying() ? 128 : 0), m692Nb);
        this.mHandler.sendEmptyMessageDelayed(40454, 100L);
    }

    /* renamed from: fd */
    private String m675fd() {
        return this.f659Yc.f523od;
    }

    private int getCurrentPosition() {
        return m691Mb().getCurrentPosition();
    }

    private int getDuration() {
        return m691Mb().getDuration();
    }

    public static C0628t getInstant() {
        if (f639ji == null) {
            f639ji = new C0628t();
        }
        return f639ji;
    }

    /* renamed from: hd */
    private int m678hd() {
        C0578e c0578e = this.f659Yc;
        if (c0578e.f517hc == 0 && c0578e.f518ic == 1) {
            return 0;
        }
        C0578e c0578e2 = this.f659Yc;
        if (c0578e2.f517hc == 0 && c0578e2.f518ic == 2) {
            return 1;
        }
        C0578e c0578e3 = this.f659Yc;
        return (c0578e3.f517hc == 1 && c0578e3.f518ic == 1) ? 2 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        return m691Mb().isPlaying();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mute(boolean z) {
        if (z) {
            m691Mb().setVolume(0.0f, 0.0f);
        } else {
            m691Mb().setVolume(1.0f, 1.0f);
        }
    }

    private void onCreate() {
        this.f654Th = false;
        if (f638jd == null) {
            f638jd = C0655t.open();
        }
        C0655t c0655t = f638jd;
        if (c0655t != null) {
            c0655t.addHandler("MusicID3Model", this.mHandler);
        }
        this.mMediaPlayer = m691Mb();
        this.f659Yc = new C0578e();
        C0643h.m751a(this.f659Yc);
        m651We();
        m693Ob();
    }

    private boolean play(int i) {
        C0578e c0578e = this.f659Yc;
        int i2 = c0578e.f482Ad;
        if (i2 <= -1) {
            return false;
        }
        C0580g c0580g = c0578e.f486Dd;
        if (i2 >= c0580g.f545kk) {
            return false;
        }
        c0578e.f514_j = c0580g.f544jk[i2].mPath;
        String str = c0578e.f514_j;
        if (str == null || !new File(str).canRead() || m686zb(this.f659Yc.f514_j) != 0) {
            return false;
        }
        seekTo(i);
        mo529Va();
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessageDelayed(65289, 500L);
        return true;
    }

    private void reset() {
        m691Mb().release();
        this.mMediaPlayer = null;
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnCompletionListener(new C0617i(this));
        this.mMediaPlayer.setOnErrorListener(new C0618j(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: zb */
    public int m686zb(String str) {
        m691Mb().stop();
        this.mHandler.removeMessages(65281);
        reset();
        try {
            m691Mb().setDataSource(str);
            m691Mb().prepare();
            return 0;
        } catch (IOException unused) {
            return -3;
        } catch (IllegalArgumentException unused2) {
            return -1;
        } catch (IllegalStateException unused3) {
            return -2;
        } catch (Exception unused4) {
            return -4;
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Bb */
    public void mo506Bb() {
        C0578e c0578e = this.f659Yc;
        c0578e.f528ud = c0578e.f486Dd;
        m672f(c0578e.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Cb */
    public void mo507Cb() {
        if (m645Pa(1)) {
            if (this.f659Yc.f530vd.size() > 0) {
                C0578e c0578e = this.f659Yc;
                int i = c0578e.f534xd + 1;
                c0578e.f534xd = i;
                if (i >= c0578e.f530vd.size()) {
                    this.f659Yc.f534xd = 0;
                }
                C0578e c0578e2 = this.f659Yc;
                c0578e2.f487Dj = c0578e2.f530vd.get(c0578e2.f534xd);
            } else {
                C0578e c0578e3 = this.f659Yc;
                c0578e3.f487Dj = c0578e3.m446uc();
            }
            if (this.f659Yc.f495Ij.size() > 0) {
                C0578e c0578e4 = this.f659Yc;
                if (c0578e4.f534xd < c0578e4.f495Ij.size()) {
                    C0578e c0578e5 = this.f659Yc;
                    c0578e5.f492Fj = c0578e5.f495Ij.get(c0578e5.f534xd);
                }
            }
            if (this.f659Yc.f496Jj.size() > 0) {
                C0578e c0578e6 = this.f659Yc;
                if (c0578e6.f534xd < c0578e6.f496Jj.size()) {
                    C0578e c0578e7 = this.f659Yc;
                    c0578e7.f493Gj = c0578e7.f496Jj.get(c0578e7.f534xd);
                }
            }
            if (this.f659Yc.f494Hj.size() > 0) {
                C0578e c0578e8 = this.f659Yc;
                if (c0578e8.f534xd < c0578e8.f494Hj.size()) {
                    C0578e c0578e9 = this.f659Yc;
                    c0578e9.f489Ej = c0578e9.f494Hj.get(c0578e9.f534xd);
                }
            }
        } else {
            if (this.f659Yc.f530vd.size() > 0) {
                C0578e c0578e10 = this.f659Yc;
                if (c0578e10.f534xd >= c0578e10.f530vd.size()) {
                    this.f659Yc.f534xd = 0;
                }
                C0578e c0578e11 = this.f659Yc;
                c0578e11.f487Dj = c0578e11.f530vd.get(c0578e11.f534xd);
            } else {
                C0578e c0578e12 = this.f659Yc;
                c0578e12.f487Dj = c0578e12.m446uc();
            }
            if (this.f659Yc.f495Ij.size() > 0) {
                C0578e c0578e13 = this.f659Yc;
                c0578e13.f492Fj = c0578e13.f495Ij.get(c0578e13.f536yd);
            }
            if (this.f659Yc.f496Jj.size() > 0) {
                C0578e c0578e14 = this.f659Yc;
                c0578e14.f493Gj = c0578e14.f496Jj.get(c0578e14.f536yd);
            }
            if (this.f659Yc.f494Hj.size() > 0) {
                C0578e c0578e15 = this.f659Yc;
                c0578e15.f489Ej = c0578e15.f494Hj.get(c0578e15.f536yd);
            }
        }
        C0578e c0578e16 = this.f659Yc;
        c0578e16.f512Yj = c0578e16.f492Fj;
        c0578e16.f511Xj = c0578e16.f489Ej;
        c0578e16.f513Zj = c0578e16.f493Gj;
        c0578e16.f510Wj = c0578e16.f487Dj;
        c0578e16.f528ud = c0578e16.f510Wj;
        m672f(c0578e16.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Db */
    public void mo508Db() {
        if (m645Pa(2)) {
            if (this.f659Yc.f532wd.size() > 0) {
                C0578e c0578e = this.f659Yc;
                int i = c0578e.f536yd + 1;
                c0578e.f536yd = i;
                if (i >= c0578e.f532wd.size()) {
                    this.f659Yc.f536yd = 0;
                }
                C0578e c0578e2 = this.f659Yc;
                c0578e2.f525rj = c0578e2.f532wd.get(c0578e2.f536yd);
            } else {
                C0578e c0578e3 = this.f659Yc;
                c0578e3.f525rj = c0578e3.m447vc();
            }
            if (this.f659Yc.f535xj.size() > 0) {
                C0578e c0578e4 = this.f659Yc;
                if (c0578e4.f536yd < c0578e4.f535xj.size()) {
                    C0578e c0578e5 = this.f659Yc;
                    c0578e5.f527tj = c0578e5.f535xj.get(c0578e5.f536yd);
                }
            }
            if (this.f659Yc.f533wj.size() > 0) {
                C0578e c0578e6 = this.f659Yc;
                if (c0578e6.f536yd < c0578e6.f533wj.size()) {
                    C0578e c0578e7 = this.f659Yc;
                    c0578e7.f529uj = c0578e7.f533wj.get(c0578e7.f536yd);
                }
            }
            if (this.f659Yc.f531vj.size() > 0) {
                C0578e c0578e8 = this.f659Yc;
                if (c0578e8.f536yd < c0578e8.f531vj.size()) {
                    C0578e c0578e9 = this.f659Yc;
                    c0578e9.f526sj = c0578e9.f531vj.get(c0578e9.f536yd);
                }
            }
        } else {
            if (this.f659Yc.f532wd.size() > 0) {
                C0578e c0578e10 = this.f659Yc;
                if (c0578e10.f536yd >= c0578e10.f532wd.size()) {
                    this.f659Yc.f536yd = 0;
                }
                C0578e c0578e11 = this.f659Yc;
                c0578e11.f525rj = c0578e11.f532wd.get(c0578e11.f536yd);
            } else {
                C0578e c0578e12 = this.f659Yc;
                c0578e12.f525rj = c0578e12.m447vc();
            }
            if (this.f659Yc.f535xj.size() > 0) {
                C0578e c0578e13 = this.f659Yc;
                c0578e13.f527tj = c0578e13.f535xj.get(c0578e13.f536yd);
            }
            if (this.f659Yc.f533wj.size() > 0) {
                C0578e c0578e14 = this.f659Yc;
                c0578e14.f529uj = c0578e14.f533wj.get(c0578e14.f536yd);
            }
            if (this.f659Yc.f531vj.size() > 0) {
                C0578e c0578e15 = this.f659Yc;
                c0578e15.f526sj = c0578e15.f531vj.get(c0578e15.f536yd);
            }
        }
        C0578e c0578e16 = this.f659Yc;
        c0578e16.f512Yj = c0578e16.f527tj;
        c0578e16.f511Xj = c0578e16.f526sj;
        c0578e16.f513Zj = c0578e16.f529uj;
        c0578e16.f510Wj = c0578e16.f525rj;
        c0578e16.f528ud = c0578e16.f510Wj;
        m672f(c0578e16.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Ea */
    public void mo509Ea(String str) {
        this.mHandler.postDelayed(new RunnableC0627s(this, str), 1500L);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Eb */
    public void mo510Eb() {
        C0578e c0578e = this.f659Yc;
        c0578e.f512Yj = c0578e.f504Rj;
        c0578e.f511Xj = c0578e.f502Pj;
        c0578e.f513Zj = c0578e.f505Sj;
        C0580g c0580g = c0578e.f503Qj;
        c0578e.f510Wj = c0580g;
        c0578e.f528ud = c0580g;
        m672f(c0578e.f528ud);
    }

    /* renamed from: Fa */
    public void m687Fa(String str) {
        C0529b.m181e("scanMediaID3 开始整理：" + str + "   ,可读" + new File(str).canRead());
        C0643h.m756a(str, this.f659Yc, this.isForward, new C0615g(this, str));
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Fb */
    public void mo511Fb() {
        this.f654Th = true;
        m700Ua();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Gb */
    public void mo513Gb() {
        if (TextUtils.isEmpty(this.f659Yc.f514_j)) {
            return;
        }
        Context context = this.mContext;
        C0578e c0578e = this.f659Yc;
        if (C0636a.m743a(context, c0578e.f514_j, c0578e.f506Tc)) {
            C0578e c0578e2 = this.f659Yc;
            C0636a.m742a(c0578e2.f514_j, c0578e2.f506Tc);
            Iterator<InterfaceC0656a> it = f637hi.iterator();
            while (it.hasNext()) {
                it.next().mo731h(false);
            }
        } else {
            C0636a.m741a(new C0579f(m692Nb(), m690Lb()), this.f659Yc.f506Tc);
            Iterator<InterfaceC0656a> it2 = f637hi.iterator();
            while (it2.hasNext()) {
                it2.next().mo731h(true);
            }
        }
        this.f659Yc.f491Fd = m696Ra();
        if (this.f659Yc.f515ck.startsWith("/data/tw/.like")) {
            C0578e c0578e3 = this.f659Yc;
            c0578e3.f486Dd.m450c(c0578e3.f491Fd);
            C0578e c0578e4 = this.f659Yc;
            if (c0578e4.f528ud.mIndex == 4) {
                c0578e4.f528ud = c0578e4.f491Fd;
            }
            m672f(this.f659Yc.f528ud);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Hb */
    public void mo514Hb() {
        int currentPosition;
        if (!isPlaying() || (currentPosition = getCurrentPosition() + 15000) <= 0 || currentPosition >= getDuration()) {
            return;
        }
        seekTo(currentPosition);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Ib */
    public void mo515Ib() {
        int currentPosition;
        if (!isPlaying() || getCurrentPosition() - 10000 <= 0 || currentPosition >= getDuration()) {
            return;
        }
        seekTo(currentPosition);
    }

    @SuppressLint({"SdCardPath"})
    /* renamed from: Jb */
    public int m688Jb() {
        if (this.f659Yc.f514_j.contains("usb")) {
            return 0;
        }
        if (this.f659Yc.f514_j.contains("extsd")) {
            return 1;
        }
        return this.f659Yc.f514_j.contains("/mnt/sdcard/./iNand") ? 2 : 0;
    }

    /* renamed from: Kb */
    public Bitmap m689Kb() {
        return this.f659Yc.f490Fb;
    }

    /* renamed from: Lb */
    public String m690Lb() {
        return this.f659Yc.f514_j;
    }

    /* renamed from: Mb */
    public MediaPlayer m691Mb() {
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = new MediaPlayer();
        }
        return this.mMediaPlayer;
    }

    /* renamed from: Nb */
    public String m692Nb() {
        return this.f659Yc.f524pd;
    }

    @SuppressLint({"SdCardPath"})
    /* renamed from: Ob */
    public void m693Ob() {
        File[] listFiles = new File("/storage").listFiles(new C0613e(this));
        if (listFiles != null) {
            for (File file : listFiles) {
                m703pa(file.getAbsolutePath());
                m687Fa(file.getAbsolutePath());
            }
        }
        File[] listFiles2 = new File("/storage").listFiles(new C0614f(this));
        if (listFiles2 != null) {
            for (File file2 : listFiles2) {
                m704qa(file2.getAbsolutePath());
                m687Fa(file2.getAbsolutePath());
            }
        }
        m687Fa("/mnt/sdcard/iNand");
        C0643h.m753a(this.f659Yc.f503Qj, "/mnt/sdcard/iNand", this.isForward);
    }

    /* renamed from: Pb */
    public void m694Pb() {
        this.f654Th = false;
        f638jd.m794w(false);
        m691Mb().release();
        this.mMediaPlayer = null;
        this.mHandler.removeCallbacksAndMessages(null);
        f638jd.removeHandler("MusicID3Model");
        f638jd.close();
        f638jd = null;
    }

    /* renamed from: Qb */
    public void m695Qb() {
        C0578e c0578e = this.f659Yc;
        C0580g c0580g = c0578e.f512Yj;
        C0579f[] c0579fArr = c0580g.f544jk;
        if (c0579fArr == null || c0579fArr.length <= 0) {
            return;
        }
        c0578e.f528ud = c0580g;
        m672f(c0578e.f528ud);
    }

    /* renamed from: Ra */
    public C0580g m696Ra() {
        C0579f[] c0579fArr = new C0579f[this.f659Yc.f506Tc.size()];
        for (int i = 0; i < this.f659Yc.f506Tc.size(); i++) {
            c0579fArr[i] = new C0579f(this.f659Yc.f506Tc.get(i).mName, this.f659Yc.f506Tc.get(i).mPath, true);
        }
        C0578e c0578e = this.f659Yc;
        c0578e.f491Fd.setLength(c0578e.f506Tc.size());
        C0578e c0578e2 = this.f659Yc;
        C0580g c0580g = c0578e2.f491Fd;
        c0580g.f544jk = c0579fArr;
        c0580g.f545kk = c0578e2.f506Tc.size();
        return this.f659Yc.f491Fd;
    }

    /* renamed from: Rb */
    public void m697Rb() {
        C0578e c0578e = this.f659Yc;
        C0580g c0580g = c0578e.f511Xj;
        C0579f[] c0579fArr = c0580g.f544jk;
        if (c0579fArr == null || c0579fArr.length <= 0) {
            return;
        }
        c0578e.f528ud = c0580g;
        m672f(c0578e.f528ud);
    }

    /* renamed from: Sb */
    public void m698Sb() {
        C0578e c0578e = this.f659Yc;
        C0580g c0580g = c0578e.f513Zj;
        C0579f[] c0579fArr = c0580g.f544jk;
        if (c0579fArr == null || c0579fArr.length <= 0) {
            return;
        }
        c0578e.f528ud = c0580g;
        m672f(c0578e.f528ud);
    }

    /* renamed from: Tb */
    public void m699Tb() {
        this.mHandler.removeMessages(65281);
        this.mHandler.removeMessages(65297);
        this.mHandler.sendEmptyMessageDelayed(65297, 500L);
    }

    /* renamed from: Ua */
    public void m700Ua() {
        if (isPlaying()) {
            this.f659Yc.f521md = m691Mb().getCurrentPosition();
            m691Mb().pause();
            this.mHandler.removeMessages(65281);
            m652Xe();
        }
    }

    /* renamed from: Ub */
    public void m701Ub() {
        Iterator<InterfaceC0656a> it = f637hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0578e c0578e = this.f659Yc;
            next.mo728d(c0578e.f521md, c0578e.mDuration);
            Context context = this.mContext;
            C0578e c0578e2 = this.f659Yc;
            next.mo731h(C0636a.m743a(context, c0578e2.f514_j, c0578e2.f506Tc));
            String m649Ue = m649Ue();
            String m675fd = m675fd();
            String m692Nb = m692Nb();
            Bitmap m689Kb = m689Kb();
            C0578e c0578e3 = this.f659Yc;
            next.mo725b(m649Ue, m675fd, m692Nb, m689Kb, c0578e3.f514_j, c0578e3.f515ck, c0578e3.f482Ad);
            next.mo727c(isPlaying());
            next.mo712D(m678hd());
            next.mo709B(this.mMediaPlayer.getAudioSessionId());
        }
        m672f(this.f659Yc.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Va */
    public void mo529Va() {
        C0655t c0655t = f638jd;
        if (c0655t != null) {
            c0655t.m794w(true);
        }
        if (isPlaying()) {
            return;
        }
        m691Mb().start();
        this.f654Th = false;
        this.mHandler.removeMessages(65281);
        this.mHandler.sendEmptyMessage(65281);
        m642Ab(this.f659Yc.f514_j);
        m652Xe();
    }

    /* renamed from: ea */
    public void m702ea(int i) {
        int i2;
        C0578e c0578e = this.f659Yc;
        c0578e.f519kd = null;
        c0578e.f520ld = 0;
        C0580g c0580g = c0578e.f486Dd;
        if (c0580g == null || (i2 = c0580g.f545kk) <= 0) {
            return;
        }
        c0578e.f519kd = new int[i2];
        if (i >= i2) {
            i = 0;
        }
        this.f659Yc.f519kd[0] = i;
        if (i2 > 1) {
            for (int i3 = i + 1; i3 < i2; i3++) {
                int i4 = i3 - i;
                if (this.f659Yc.f517hc != 0) {
                    i4 = m683m(i, i2);
                }
                this.f659Yc.f519kd[i4] = i3;
            }
            for (int i5 = 0; i5 < i; i5++) {
                int i6 = (i5 + i2) - i;
                if (this.f659Yc.f517hc != 0) {
                    i6 = m683m(i, i2);
                }
                this.f659Yc.f519kd[i6] = i5;
            }
        }
    }

    public String getFileName() {
        C0578e c0578e = this.f659Yc;
        int i = c0578e.f482Ad;
        C0580g c0580g = c0578e.f486Dd;
        return i < c0580g.f545kk ? c0580g.f544jk[i].mName : "";
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: ka */
    public void mo536ka(int i) {
        if (i == 0) {
            C0578e c0578e = this.f659Yc;
            c0578e.f517hc = 0;
            c0578e.f518ic = 1;
            m702ea(c0578e.f482Ad);
        } else if (i == 1) {
            C0578e c0578e2 = this.f659Yc;
            c0578e2.f517hc = 0;
            c0578e2.f518ic = 2;
            m702ea(c0578e2.f482Ad);
        } else if (i == 2) {
            C0578e c0578e3 = this.f659Yc;
            c0578e3.f517hc = 1;
            c0578e3.f518ic = 1;
            m702ea(c0578e3.f482Ad);
        }
        Iterator<InterfaceC0656a> it = f637hi.iterator();
        while (it.hasNext()) {
            it.next().mo712D(m678hd());
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: la */
    public void mo537la(int i) {
        int i2;
        C0578e c0578e = this.f659Yc;
        C0580g c0580g = c0578e.f528ud;
        if (c0580g.f543ik == 1 && i == 0 && c0580g.f549qk != 1 && c0580g.mIndex != 4) {
            c0578e.f528ud = c0580g.f548nk;
            m672f(c0578e.f528ud);
            return;
        }
        if (this.f659Yc.f528ud.f543ik == 1) {
            i--;
        }
        C0580g c0580g2 = this.f659Yc.f528ud;
        if (c0580g2.f543ik == 0 && (i2 = c0580g2.mIndex) != 0 && c0580g2.f549qk != 1 && i2 != 4) {
            C0579f[] c0579fArr = c0580g2.f544jk;
            this.fileName = c0579fArr[i].mName;
            this.f656Vh = c0579fArr[i].mPath;
            C0580g m452oa = c0580g2.m452oa(i);
            if (m452oa != null) {
                this.f659Yc.f528ud.m451e(m452oa);
                C0578e c0578e2 = this.f659Yc;
                c0578e2.f528ud = m452oa;
                m672f(c0578e2.f528ud);
                return;
            }
            String str = this.fileName;
            C0580g c0580g3 = this.f659Yc.f528ud;
            C0580g c0580g4 = new C0580g(str, c0580g3.mIndex, c0580g3.f549qk, c0580g3.f543ik + 1, c0580g3);
            C0578e c0578e3 = this.f659Yc;
            int i3 = c0578e3.f528ud.f549qk;
            if (i3 == 2 || i3 == 3) {
                C0643h.m757b(c0580g4, this.f656Vh, this.isForward, new C0625q(this));
                return;
            } else {
                C0643h.m750a(this.mContext, c0580g4, this.f656Vh, c0578e3.f506Tc, this.isForward, new C0626r(this));
                return;
            }
        }
        C0578e c0578e4 = this.f659Yc;
        c0578e4.f482Ad = i;
        C0580g c0580g5 = c0578e4.f528ud;
        int i4 = c0580g5.f549qk;
        if (i4 == 1) {
            c0578e4.f515ck = this.f659Yc.f511Xj.mName + "/.all";
            C0578e c0578e5 = this.f659Yc;
            c0578e5.f486Dd.m450c(c0578e5.f511Xj);
            C0578e c0578e6 = this.f659Yc;
            c0578e6.f514_j = c0578e6.f486Dd.f544jk[i].mPath;
        } else if (c0580g5.mIndex == 4) {
            c0578e4.f515ck = "/data/tw/.like";
            c0578e4.f486Dd.m450c(c0578e4.f491Fd);
            C0578e c0578e7 = this.f659Yc;
            c0578e7.f514_j = c0578e7.f486Dd.f544jk[i].mPath;
        } else if (i4 == 0) {
            c0578e4.f486Dd.m450c(c0580g5);
            C0578e c0578e8 = this.f659Yc;
            c0578e8.f514_j = c0578e8.f528ud.f544jk[i].mPath;
            String str2 = c0578e8.f514_j;
            c0578e8.f515ck = str2.substring(0, str2.lastIndexOf("/"));
        } else if (i4 == 3) {
            c0578e4.f486Dd.m450c(c0580g5);
            C0578e c0578e9 = this.f659Yc;
            c0578e9.f514_j = c0578e9.f528ud.f544jk[i].mPath;
        } else if (i4 == 2) {
            c0578e4.f486Dd.m450c(c0580g5);
            C0578e c0578e10 = this.f659Yc;
            c0578e10.f514_j = c0578e10.f528ud.f544jk[i].mPath;
        }
        m702ea(i);
        m666c(0, false);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onPause() {
        this.f667wg = false;
        f638jd.m792ca(Cea708CCParser.Const.CODE_C1_CW3);
        if (TextUtils.isEmpty(this.f659Yc.f514_j) || this.f659Yc.f521md <= 0) {
            return;
        }
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessage(65289);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onResume() {
        this.f667wg = true;
        C0699o.m1026a(this.mContext, "MUSIC_DATA", C0699o.f844pm, m688Jb());
        C0655t c0655t = f638jd;
        if (c0655t != null) {
            c0655t.m794w(true);
            f638jd.m792ca(3);
        }
        C0655t c0655t2 = f638jd;
        if (c0655t2 != null) {
            c0655t2.write(1296, 255);
            f638jd.write(515, 255);
        }
        this.mHandler.removeMessages(65287);
        this.mHandler.sendEmptyMessageDelayed(65287, 150L);
    }

    /* renamed from: pa */
    public void m703pa(String str) {
        Iterator<C0580g> it = this.f659Yc.f530vd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0580g c0580g = new C0580g(str, 1, 0, 0);
        C0643h.m753a(c0580g, str, this.isForward);
        if (c0580g.mLength > 0) {
            this.f659Yc.f530vd.add(c0580g);
        }
        C0578e c0578e = this.f659Yc;
        c0578e.f487Dj = c0578e.f530vd.get(0);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: pb */
    public void mo539pb() {
        if (this.f651Qh) {
            this.f651Qh = false;
        } else {
            if (m650Ve()) {
                return;
            }
            this.mHandler.sendEmptyMessage(65282);
        }
    }

    /* renamed from: qa */
    public void m704qa(String str) {
        Iterator<C0580g> it = this.f659Yc.f532wd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0580g c0580g = new C0580g(str, 2, 0, 0);
        C0643h.m753a(c0580g, str, this.isForward);
        if (c0580g.mLength > 0) {
            this.f659Yc.f532wd.add(c0580g);
        }
        C0578e c0578e = this.f659Yc;
        c0578e.f525rj = c0578e.f532wd.get(0);
    }

    /* renamed from: ra */
    public void m705ra(String str) {
        Iterator<C0580g> it = this.f659Yc.f530vd.iterator();
        while (it.hasNext()) {
            C0580g next = it.next();
            if (str.equals(next.mName)) {
                C0580g c0580g = this.f659Yc.f528ud;
                if (c0580g.f543ik == 1 && c0580g.f549qk != 1) {
                    c0580g = c0580g.f548nk;
                }
                String str2 = c0580g.mName;
                next.m453wc();
                it.remove();
                C0578e c0578e = this.f659Yc;
                if (c0578e.f534xd >= c0578e.f530vd.size()) {
                    C0578e c0578e2 = this.f659Yc;
                    c0578e2.f534xd = c0578e2.f530vd.size() - 1;
                    C0578e c0578e3 = this.f659Yc;
                    if (c0578e3.f534xd < 0) {
                        c0578e3.f534xd = 0;
                    }
                }
                if (str.equals(str2)) {
                    if (this.f659Yc.f530vd.size() > 0) {
                        C0578e c0578e4 = this.f659Yc;
                        c0578e4.f487Dj = c0578e4.f530vd.get(c0578e4.f534xd);
                    } else {
                        C0578e c0578e5 = this.f659Yc;
                        c0578e5.f487Dj = c0578e5.m446uc();
                    }
                }
            }
        }
        Iterator<C0580g> it2 = this.f659Yc.f495Ij.iterator();
        while (it2.hasNext()) {
            C0580g next2 = it2.next();
            String str3 = next2.mName;
            if (str.contains(str3.substring(str3.lastIndexOf("/")))) {
                next2.m453wc();
                it2.remove();
                if (this.f659Yc.f495Ij.size() > 1) {
                    C0578e c0578e6 = this.f659Yc;
                    c0578e6.f492Fj = c0578e6.f495Ij.get(c0578e6.f534xd);
                } else {
                    C0578e c0578e7 = this.f659Yc;
                    c0578e7.f492Fj = c0578e7.f495Ij.get(0);
                }
                C0529b.m178a(this.f659Yc.f492Fj.mName);
            }
        }
        Iterator<C0580g> it3 = this.f659Yc.f496Jj.iterator();
        while (it3.hasNext()) {
            C0580g next3 = it3.next();
            String str4 = next3.mName;
            if (str.contains(str4.substring(str4.lastIndexOf("/")))) {
                next3.m453wc();
                it3.remove();
                if (this.f659Yc.f496Jj.size() > 1) {
                    C0578e c0578e8 = this.f659Yc;
                    c0578e8.f493Gj = c0578e8.f496Jj.get(c0578e8.f534xd);
                } else {
                    C0578e c0578e9 = this.f659Yc;
                    c0578e9.f493Gj = c0578e9.f496Jj.get(0);
                }
                C0529b.m178a(this.f659Yc.f493Gj.mName);
            }
        }
        Iterator<C0580g> it4 = this.f659Yc.f494Hj.iterator();
        while (it4.hasNext()) {
            C0580g next4 = it4.next();
            String str5 = next4.mName;
            if (str.contains(str5.substring(str5.lastIndexOf("/")))) {
                next4.m453wc();
                it4.remove();
                if (this.f659Yc.f494Hj.size() > 1) {
                    C0578e c0578e10 = this.f659Yc;
                    c0578e10.f489Ej = c0578e10.f494Hj.get(c0578e10.f534xd);
                } else {
                    C0578e c0578e11 = this.f659Yc;
                    c0578e11.f489Ej = c0578e11.f494Hj.get(0);
                }
                C0529b.m178a(this.f659Yc.f489Ej.mName);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: rb */
    public void mo542rb() {
        if (this.f651Qh) {
            this.f651Qh = false;
        } else {
            if (m650Ve()) {
                return;
            }
            this.mHandler.sendEmptyMessage(65283);
        }
    }

    /* renamed from: sa */
    public void m706sa(String str) {
        Iterator<C0580g> it = this.f659Yc.f532wd.iterator();
        while (it.hasNext()) {
            C0580g next = it.next();
            if (str.equals(next.mName)) {
                C0580g c0580g = this.f659Yc.f528ud;
                if (c0580g.f543ik == 1 && c0580g.f549qk != 1) {
                    c0580g = c0580g.f548nk;
                }
                String str2 = c0580g.mName;
                next.m453wc();
                it.remove();
                C0578e c0578e = this.f659Yc;
                if (c0578e.f536yd >= c0578e.f532wd.size()) {
                    C0578e c0578e2 = this.f659Yc;
                    c0578e2.f536yd = c0578e2.f532wd.size() - 1;
                    C0578e c0578e3 = this.f659Yc;
                    if (c0578e3.f536yd < 0) {
                        c0578e3.f536yd = 0;
                    }
                }
                if (str.equals(str2)) {
                    if (this.f659Yc.f532wd.size() > 0) {
                        C0578e c0578e4 = this.f659Yc;
                        c0578e4.f525rj = c0578e4.f532wd.get(c0578e4.f536yd);
                    } else {
                        C0578e c0578e5 = this.f659Yc;
                        c0578e5.f525rj = c0578e5.m447vc();
                    }
                }
            }
        }
        Iterator<C0580g> it2 = this.f659Yc.f535xj.iterator();
        while (it2.hasNext()) {
            C0580g next2 = it2.next();
            String str3 = next2.mName;
            if (str.contains(str3.substring(str3.lastIndexOf("/")))) {
                next2.m453wc();
                it2.remove();
                if (this.f659Yc.f535xj.size() > 1) {
                    C0578e c0578e6 = this.f659Yc;
                    c0578e6.f527tj = c0578e6.f535xj.get(c0578e6.f536yd);
                } else if (this.f659Yc.f535xj.size() > 0) {
                    C0578e c0578e7 = this.f659Yc;
                    c0578e7.f527tj = c0578e7.f535xj.get(0);
                }
            }
        }
        Iterator<C0580g> it3 = this.f659Yc.f533wj.iterator();
        while (it3.hasNext()) {
            C0580g next3 = it3.next();
            String str4 = next3.mName;
            if (str.contains(str4.substring(str4.lastIndexOf("/")))) {
                next3.m453wc();
                it3.remove();
                if (this.f659Yc.f533wj.size() > 1) {
                    C0578e c0578e8 = this.f659Yc;
                    c0578e8.f529uj = c0578e8.f533wj.get(c0578e8.f536yd);
                } else if (this.f659Yc.f533wj.size() > 0) {
                    C0578e c0578e9 = this.f659Yc;
                    c0578e9.f529uj = c0578e9.f533wj.get(0);
                }
            }
        }
        Iterator<C0580g> it4 = this.f659Yc.f531vj.iterator();
        while (it4.hasNext()) {
            C0580g next4 = it4.next();
            String str5 = next4.mName;
            if (str.contains(str5.substring(str5.lastIndexOf("/")))) {
                next4.m453wc();
                it4.remove();
                if (this.f659Yc.f531vj.size() > 1) {
                    C0578e c0578e10 = this.f659Yc;
                    c0578e10.f526sj = c0578e10.f531vj.get(c0578e10.f536yd);
                } else if (this.f659Yc.f531vj.size() > 0) {
                    C0578e c0578e11 = this.f659Yc;
                    c0578e11.f526sj = c0578e11.f531vj.get(0);
                }
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void seekTo(int i) {
        int duration = this.mMediaPlayer.getDuration();
        if (i <= 0 || duration <= 0 || i >= duration) {
            return;
        }
        m691Mb().seekTo(i);
    }

    @SuppressLint({"SdCardPath"})
    /* renamed from: ta */
    public void m707ta(String str) {
        if (str == null) {
            return;
        }
        if (str.contains("/mnt/sdcard/iNand")) {
            C0578e c0578e = this.f659Yc;
            c0578e.f528ud = c0578e.f503Qj;
            return;
        }
        if (str.contains("/storage/usb") || str.contains("/mnt/usbhost")) {
            if (this.f659Yc.f532wd.size() <= 0) {
                C0578e c0578e2 = this.f659Yc;
                c0578e2.f528ud = c0578e2.m447vc();
                return;
            }
            C0578e c0578e3 = this.f659Yc;
            if (c0578e3.f536yd >= c0578e3.f532wd.size()) {
                this.f659Yc.f536yd = 0;
            }
            C0578e c0578e4 = this.f659Yc;
            c0578e4.f528ud = c0578e4.f532wd.get(c0578e4.f536yd);
            return;
        }
        if (!str.contains("/storage/extsd") && !str.contains("/mnt/extsd")) {
            C0578e c0578e5 = this.f659Yc;
            c0578e5.f528ud = c0578e5.f486Dd;
        } else {
            if (this.f659Yc.f530vd.size() <= 0) {
                C0578e c0578e6 = this.f659Yc;
                c0578e6.f528ud = c0578e6.m446uc();
                return;
            }
            C0578e c0578e7 = this.f659Yc;
            if (c0578e7.f534xd >= c0578e7.f530vd.size()) {
                this.f659Yc.f534xd = 0;
            }
            C0578e c0578e8 = this.f659Yc;
            c0578e8.f528ud = c0578e8.f530vd.get(c0578e8.f534xd);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: w */
    public void mo544w(boolean z) {
        f638jd.m794w(z);
    }

    /* renamed from: m */
    private int m683m(int i, int i2) {
        int random;
        do {
            random = (int) (Math.random() * i2);
            if (random != 0) {
                break;
            }
        } while (i == 0);
        int i3 = random;
        while (i3 < i2 && this.f659Yc.f519kd[i3] != 0) {
            i3++;
        }
        if (i3 == i2) {
            i3 = 1;
            while (i3 < random && this.f659Yc.f519kd[i3] != 0) {
                i3++;
            }
        }
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Bb */
    public void m643Bb(String str) {
        if (new File(str).exists()) {
            if (str.contains("/storage/emulated/0/")) {
                str.replace("/storage/emulated/0", "mnt/sdcard");
            }
            C0578e c0578e = this.f659Yc;
            c0578e.f514_j = str;
            c0578e.f515ck = str.substring(0, str.lastIndexOf("/"));
            C0580g c0580g = new C0580g("Playlist", 0, 0, 0);
            Context context = this.mContext;
            C0578e c0578e2 = this.f659Yc;
            C0643h.m750a(context, c0580g, c0578e2.f515ck, c0578e2.f506Tc, this.isForward, new C0612d(this, str));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: c */
    public void m666c(int i, boolean z) {
        int length;
        synchronized (f638jd) {
            int[] iArr = this.f659Yc.f519kd;
            if (iArr != null && (length = iArr.length) > 0) {
                int i2 = this.f659Yc.f520ld;
                if (z) {
                    if (i2 < -1) {
                        i2 = -1;
                    }
                    int i3 = i;
                    int i4 = i2;
                    while (true) {
                        if (i4 <= -1) {
                            break;
                        }
                        this.f659Yc.f482Ad = iArr[i4];
                        if (play(i3)) {
                            this.f659Yc.f520ld = i4;
                            i3 = 0;
                            break;
                        } else {
                            i4--;
                            i3 = 0;
                        }
                    }
                    if (this.f659Yc.f518ic != 0 && i4 == -1) {
                        int i5 = length - 1;
                        while (true) {
                            if (i5 <= i2) {
                                break;
                            }
                            this.f659Yc.f482Ad = iArr[i5];
                            if (play(i3)) {
                                this.f659Yc.f520ld = i5;
                                break;
                            } else {
                                i5--;
                                i3 = 0;
                            }
                        }
                        if (i5 == i2) {
                            m699Tb();
                        }
                    }
                    if (this.f659Yc.f520ld == -1) {
                        this.f659Yc.f520ld = 0;
                        this.f659Yc.f482Ad = iArr[this.f659Yc.f520ld];
                        m699Tb();
                    }
                } else {
                    if (i2 > length) {
                        i2 = length;
                    }
                    int i6 = i;
                    int i7 = i2;
                    while (true) {
                        if (i7 >= length) {
                            break;
                        }
                        this.f659Yc.f482Ad = iArr[i7];
                        if (play(i6)) {
                            this.f659Yc.f520ld = i7;
                            i6 = 0;
                            break;
                        } else {
                            i7++;
                            i6 = 0;
                        }
                    }
                    if (this.f659Yc.f518ic != 0 && i7 == length) {
                        int i8 = 0;
                        while (true) {
                            if (i8 >= i2) {
                                break;
                            }
                            this.f659Yc.f482Ad = iArr[i8];
                            if (play(i6)) {
                                this.f659Yc.f520ld = i8;
                                break;
                            } else {
                                i8++;
                                i6 = 0;
                            }
                        }
                        if (i8 == i2) {
                            m699Tb();
                        }
                    }
                    if (this.f659Yc.f520ld == length) {
                        this.f659Yc.f520ld = length - 1;
                        this.f659Yc.f482Ad = iArr[this.f659Yc.f520ld];
                        m699Tb();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: f */
    public void m672f(C0580g c0580g) {
        Iterator<InterfaceC0656a> it = f637hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            if (c0580g != null) {
                C0579f[] c0579fArr = c0580g.f544jk;
                if (c0579fArr != null && c0579fArr.length > 0) {
                    for (C0579f c0579f : c0579fArr) {
                        String str = c0579f.mPath;
                        if (str != null) {
                            c0579f.f539ek = C0636a.m743a(this.mContext, str, this.f659Yc.f506Tc);
                        }
                    }
                }
                next.mo721a(c0580g);
                C0529b.m181e("onCurrentCList:" + c0580g.f544jk);
            } else {
                C0529b.m181e("record == null");
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: b */
    public void mo534b(C0579f c0579f, boolean z) {
        if (!TextUtils.isEmpty(c0579f.mPath)) {
            if (z && !C0636a.m743a(this.mContext, c0579f.mPath, this.f659Yc.f506Tc)) {
                C0636a.m741a(c0579f, this.f659Yc.f506Tc);
            } else if (!z && C0636a.m743a(this.mContext, c0579f.mPath, this.f659Yc.f506Tc)) {
                C0636a.m742a(c0579f.mPath, this.f659Yc.f506Tc);
            }
        }
        Iterator<InterfaceC0656a> it = f637hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            Context context = this.mContext;
            C0578e c0578e = this.f659Yc;
            next.mo731h(C0636a.m743a(context, c0578e.f514_j, c0578e.f506Tc));
        }
        this.f659Yc.f491Fd = m696Ra();
        int i = this.f659Yc.f528ud.mIndex;
        if (i == 4 || i == 0) {
            if (this.f659Yc.f515ck.startsWith("/data/tw/.like")) {
                C0578e c0578e2 = this.f659Yc;
                c0578e2.f486Dd.m450c(c0578e2.f491Fd);
            }
            C0578e c0578e3 = this.f659Yc;
            if (c0578e3.f528ud.mIndex == 4) {
                c0578e3.f528ud = c0578e3.f491Fd;
                mo505Ab();
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: a */
    public void mo532a(InterfaceC0656a interfaceC0656a, Context context) {
        if (f637hi.size() == 0) {
            this.mContext = context;
            onCreate();
        }
        if (f637hi.contains(interfaceC0656a)) {
            return;
        }
        f637hi.add(interfaceC0656a);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: a */
    public void mo531a(InterfaceC0656a interfaceC0656a) {
        f637hi.remove(interfaceC0656a);
        C0655t c0655t = f638jd;
        if (c0655t != null) {
            C0578e c0578e = this.f659Yc;
            if (c0578e.mSource == 3) {
                C0643h.m752a(c0578e, c0655t);
            }
        }
        if (f637hi.size() == 0) {
            m694Pb();
        }
    }

    /* renamed from: a */
    private void m660a(String str, String str2, String str3, String str4) {
        Intent intent = new Intent("com.tw.music.info");
        intent.putExtra("musicTitle", str);
        intent.putExtra("musicaArtist", str2);
        intent.putExtra("musicAlbum", str3);
        intent.putExtra("musicPath", str4);
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: b */
    public void m661b(int i, String str) {
        byte[] bArr = null;
        if (str == null) {
            f638jd.write(1296, i << 4, 0, (Object) null);
            return;
        }
        int i2 = this.f641Eh;
        if ((i2 & 1) == 1) {
            try {
                bArr = str.getBytes(StandardCharsets.UTF_16);
            } catch (Exception unused) {
            }
            f638jd.write(1296, (i << 4) | 0, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        if ((i2 & 2) == 2) {
            try {
                bArr = str.getBytes("Unicode");
            } catch (Exception unused2) {
            }
            f638jd.write(1296, (i << 4) | 1, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        int i3 = 3;
        if ((i2 & 4) == 4) {
            try {
                bArr = str.getBytes("GBK");
            } catch (Exception unused3) {
            }
            if (bArr == null) {
                try {
                    bArr = str.getBytes("GB2312");
                } catch (Exception unused4) {
                }
            } else {
                i3 = 2;
            }
            if (bArr == null && (this.f641Eh & 128) == 128) {
                try {
                    bArr = str.getBytes(StandardCharsets.UTF_16);
                } catch (Exception unused5) {
                }
                i3 = 0;
            }
            f638jd.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        if ((i2 & 8) == 8) {
            try {
                bArr = str.getBytes("GB2312");
            } catch (Exception unused6) {
            }
            if (bArr == null) {
                try {
                    bArr = str.getBytes("GBK");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i3 = 2;
            }
            if (bArr == null && (this.f641Eh & 128) == 128) {
                try {
                    bArr = str.getBytes(StandardCharsets.UTF_16);
                } catch (Exception unused7) {
                }
                i3 = 0;
            }
            f638jd.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Ab */
    public void mo505Ab() {
        m645Pa(4);
        C0578e c0578e = this.f659Yc;
        c0578e.f528ud = c0578e.f491Fd;
        m672f(c0578e.f528ud);
    }
}
