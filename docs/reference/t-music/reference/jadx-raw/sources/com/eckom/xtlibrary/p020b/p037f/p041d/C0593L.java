package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.SystemProperties;
import android.support.v4.media.subtitle.Cea708CCParser;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p037f.p038a.C0573c;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0577d;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0578e;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p040c.InterfaceC0581a;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0638c;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0655t;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import com.eckom.xtlibrary.p020b.p053j.C0699o;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayer;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.L */
/* loaded from: classes3.dex */
public class C0593L extends AbstractC0607a {

    /* renamed from: jd */
    private static C0655t f553jd;

    /* renamed from: Th */
    boolean f569Th;

    /* renamed from: Uh */
    private int f570Uh;

    /* renamed from: Vh */
    private String f571Vh;

    /* renamed from: Wh */
    public boolean f572Wh;

    /* renamed from: Xh */
    public boolean f573Xh;

    /* renamed from: Yc */
    C0578e f574Yc;

    /* renamed from: Yh */
    public boolean f575Yh;

    /* renamed from: Zh */
    public boolean f576Zh;

    /* renamed from: _h */
    public boolean f577_h;

    /* renamed from: di */
    public boolean f578di;

    /* renamed from: ei */
    public boolean f579ei;

    /* renamed from: fi */
    public boolean f580fi;
    private String fileName;

    /* renamed from: gi */
    public boolean f581gi;
    private final boolean isForward;

    /* renamed from: li */
    private final boolean f582li;
    private Context mContext;
    private final Handler mHandler;
    public TWMediaPlayer mMediaPlayer;

    /* renamed from: mi */
    private C0638c f583mi;

    /* renamed from: ni */
    private C0573c f584ni;

    /* renamed from: qi */
    private float f585qi;

    /* renamed from: ri */
    private final IMediaPlayer.OnCompletionListener f586ri;

    /* renamed from: ti */
    private final IMediaPlayer.OnErrorListener f587ti;

    /* renamed from: ui */
    private InterfaceC0581a f588ui;

    /* renamed from: wg */
    private boolean f589wg;

    /* renamed from: hi */
    private static final ArrayList<InterfaceC0656a> f552hi = new ArrayList<>();

    /* renamed from: ji */
    private static C0593L f554ji = null;

    /* renamed from: Eh */
    private int f556Eh = 0;

    /* renamed from: Hh */
    private final int f557Hh = 0;

    /* renamed from: Ih */
    private final int f558Ih = 1;

    /* renamed from: Jh */
    private final int f559Jh = 2;

    /* renamed from: Kh */
    private final int f560Kh = 3;

    /* renamed from: Lh */
    private final int f561Lh = 1;

    /* renamed from: Mh */
    private final int f562Mh = 2;

    /* renamed from: Nh */
    private final int f563Nh = 4;

    /* renamed from: Oh */
    private final int f564Oh = 8;

    /* renamed from: Ph */
    private final int f565Ph = 128;

    /* renamed from: Qh */
    private boolean f566Qh = false;

    /* renamed from: Rh */
    private boolean f567Rh = false;

    /* renamed from: Sh */
    private boolean f568Sh = false;

    /* renamed from: Cg */
    private boolean f555Cg = false;

    private C0593L() {
        this.isForward = SystemProperties.getInt("persist.media.forward", 1) == 1;
        this.f582li = SystemProperties.getBoolean("persist.sys.media.sdcardscan", false);
        this.f585qi = 1.0f;
        this.f586ri = new C0583B(this);
        this.f587ti = new C0584C(this);
        this.f569Th = false;
        this.mHandler = new Handler(new C0587F(this));
        this.f570Uh = -1;
        this.f571Vh = "";
        this.f588ui = new C0634z(this);
        this.f572Wh = false;
        this.f573Xh = false;
        this.f575Yh = false;
        this.f576Zh = false;
        this.f577_h = false;
        this.f578di = false;
        this.f579ei = false;
        this.f580fi = false;
        this.f581gi = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ab */
    public void m457Ab(String str) {
        C0578e c0578e = this.f574Yc;
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
                this.f574Yc.f522nd = mediaMetadataRetriever.extractMetadata(2);
                this.f574Yc.f523od = mediaMetadataRetriever.extractMetadata(1);
                this.f574Yc.f524pd = mediaMetadataRetriever.extractMetadata(7);
                try {
                    if (TextUtils.isEmpty(this.f574Yc.f524pd) || this.f574Yc.f524pd.equals("")) {
                        this.f574Yc.f524pd = new File(str).getName();
                    }
                    if (TextUtils.isEmpty(this.f574Yc.f522nd) || this.f574Yc.f522nd.equals("")) {
                        this.f574Yc.f522nd = " ";
                    }
                    if (TextUtils.isEmpty(this.f574Yc.f523od) || this.f574Yc.f523od.equals("")) {
                        this.f574Yc.f523od = " ";
                    }
                    m480b(0, this.f574Yc.f522nd);
                    this.mHandler.postDelayed(new RunnableC0588G(this), 100L);
                    this.mHandler.postDelayed(new RunnableC0589H(this), 200L);
                } catch (Exception e) {
                    Log.e("MusicIjkID3Model", Log.getStackTraceString(e));
                }
                m477a(this.f574Yc.f524pd, this.f574Yc.f522nd, this.f574Yc.f523od, str);
                byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
                if (embeddedPicture != null) {
                    this.f574Yc.f490Fb = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
                }
            } else {
                this.f574Yc.f524pd = new File(str).getName();
                this.f574Yc.f522nd = this.mContext.getString(R.string.unknownName);
                this.f574Yc.f523od = this.mContext.getString(R.string.unknownName);
            }
            mediaMetadataRetriever.release();
        } catch (Exception unused) {
            this.f574Yc.f524pd = new File(str).getName();
            this.f574Yc.f522nd = this.mContext.getString(R.string.unknownName);
            this.f574Yc.f523od = this.mContext.getString(R.string.unknownName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: L */
    public void m460L(boolean z) {
        if (z) {
            this.mMediaPlayer.setVolume(0.5f, 0.5f);
            this.f585qi = 0.5f;
        } else {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.f585qi = 1.0f;
        }
    }

    /* renamed from: Pa */
    private boolean m461Pa(int i) {
        if (this.f570Uh == i) {
            return true;
        }
        this.f570Uh = i;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Qa */
    public void m462Qa(int i) {
        C0578e c0578e = this.f574Yc;
        C0580g c0580g = c0578e.f528ud;
        int i2 = c0580g.mIndex;
        if (i == i2) {
            String str = c0580g.mKey;
            if (i2 == 1) {
                if (c0578e.f498Lj.size() > 0) {
                    C0578e c0578e2 = this.f574Yc;
                    c0578e2.f492Fj = c0578e2.f498Lj.get(str);
                }
                if (this.f574Yc.f497Kj.size() > 0) {
                    C0578e c0578e3 = this.f574Yc;
                    c0578e3.f493Gj = c0578e3.f497Kj.get(str);
                }
                if (this.f574Yc.f494Hj.size() > 0) {
                    C0578e c0578e4 = this.f574Yc;
                    c0578e4.f489Ej = c0578e4.f494Hj.get(c0578e4.f534xd);
                }
                C0578e c0578e5 = this.f574Yc;
                c0578e5.f512Yj = c0578e5.f492Fj;
                c0578e5.f511Xj = c0578e5.f489Ej;
                c0578e5.f513Zj = c0578e5.f493Gj;
            } else if (i2 == 2) {
                if (c0578e.f538zj.size() > 0) {
                    C0578e c0578e6 = this.f574Yc;
                    c0578e6.f527tj = c0578e6.f538zj.get(str);
                }
                if (this.f574Yc.f537yj.size() > 0) {
                    C0578e c0578e7 = this.f574Yc;
                    c0578e7.f529uj = c0578e7.f537yj.get(str);
                }
                if (this.f574Yc.f531vj.size() > 0) {
                    C0578e c0578e8 = this.f574Yc;
                    c0578e8.f526sj = c0578e8.f531vj.get(c0578e8.f536yd);
                }
                C0578e c0578e9 = this.f574Yc;
                c0578e9.f512Yj = c0578e9.f527tj;
                c0578e9.f511Xj = c0578e9.f526sj;
                c0578e9.f513Zj = c0578e9.f529uj;
            } else if (i2 == 3) {
                c0578e.f512Yj = c0578e.f504Rj;
                c0578e.f511Xj = c0578e.f502Pj;
                c0578e.f513Zj = c0578e.f505Sj;
            }
            C0580g c0580g2 = this.f574Yc.f528ud;
            int i3 = c0580g2.f549qk;
            if (c0580g2.f543ik == 0) {
                if (i3 == 1) {
                    m524Rb();
                } else if (i3 == 2) {
                    m525Sb();
                } else {
                    if (i3 != 3) {
                        return;
                    }
                    m522Qb();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Re */
    public void m463Re() {
        C0578e c0578e = this.f574Yc;
        int[] iArr = c0578e.f519kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0578e.f518ic != 2) {
            c0578e.f520ld++;
        }
        m485c(0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Se */
    public void m464Se() {
        C0578e c0578e = this.f574Yc;
        int[] iArr = c0578e.f519kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0578e.f518ic != 2) {
            c0578e.f520ld--;
        }
        m485c(0, true);
    }

    /* renamed from: Ue */
    private String m465Ue() {
        return this.f574Yc.f522nd;
    }

    /* renamed from: Ve */
    private boolean m467Ve() {
        return this.mHandler.hasMessages(65282) || this.mHandler.hasMessages(65283);
    }

    /* renamed from: We */
    private void m468We() {
        Log.d("MusicIjkID3Model", "initializePlayListRecord: " + this.f574Yc.f515ck);
        m470Ye();
        Context context = this.mContext;
        C0578e c0578e = this.f574Yc;
        C0643h.m750a(context, c0578e.f486Dd, c0578e.f515ck, c0578e.f506Tc, this.isForward, new C0582A(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Xe */
    public void m469Xe() {
        int duration = getDuration();
        int currentPosition = getCurrentPosition();
        if (duration < 0) {
            duration = 0;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        int i = (duration <= 0 || currentPosition > duration) ? 0 : (currentPosition * 100) / duration;
        String m519Nb = m519Nb();
        if (TextUtils.isEmpty(m519Nb)) {
            m519Nb = "";
        }
        int i2 = i & 127;
        f553jd.write(40704, 3, (isPlaying() ? 128 : 0) | i2, m519Nb);
        f553jd.write(771, 3, i2 | (isPlaying() ? 128 : 0), m519Nb);
        this.mHandler.sendEmptyMessage(40454);
    }

    /* renamed from: Ye */
    private void m470Ye() {
        TWMediaPlayer tWMediaPlayer;
        m535ea(this.f574Yc.f482Ad);
        String str = this.f574Yc.f514_j;
        if (str == null || !new File(str).canRead() || (tWMediaPlayer = this.mMediaPlayer) == null) {
            return;
        }
        tWMediaPlayer.setMPPath(this.f574Yc.f514_j);
        seekTo(this.f574Yc.f521md);
        mo529Va();
        this.mHandler.removeMessages(40454);
        this.mHandler.sendEmptyMessageDelayed(40454, 2000L);
    }

    /* renamed from: fd */
    private String m493fd() {
        return this.f574Yc.f523od;
    }

    private int getCurrentPosition() {
        return this.mMediaPlayer.getCurrentPosition();
    }

    private int getDuration() {
        return this.mMediaPlayer.getDuration();
    }

    public static C0593L getInstant() {
        if (f554ji == null) {
            f554ji = new C0593L();
        }
        return f554ji;
    }

    /* renamed from: hd */
    private int m496hd() {
        C0578e c0578e = this.f574Yc;
        if (c0578e.f517hc == 0 && c0578e.f518ic == 1) {
            return 0;
        }
        C0578e c0578e2 = this.f574Yc;
        if (c0578e2.f517hc == 0 && c0578e2.f518ic == 2) {
            return 1;
        }
        C0578e c0578e3 = this.f574Yc;
        return (c0578e3.f517hc == 1 && c0578e3.f518ic == 1) ? 2 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        return this.mMediaPlayer.isPlaying();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mute(boolean z) {
        if (z) {
            this.mMediaPlayer.setVolume(0.0f, 0.0f);
            this.f585qi = 0.0f;
        } else {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.f585qi = 1.0f;
        }
    }

    private void onCreate() {
        this.f569Th = false;
        IjkMediaPlayer.loadLibrariesOnce(null);
        if (f553jd == null) {
            f553jd = C0655t.open();
        }
        C0655t c0655t = f553jd;
        if (c0655t != null) {
            c0655t.addHandler("MusicIjkID3Model", this.mHandler);
        }
        this.mMediaPlayer = new TWMediaPlayer(this.mContext);
        this.mMediaPlayer.setOnCompletionListener(this.f586ri);
        this.mMediaPlayer.setOnErrorListener(this.f587ti);
        this.f574Yc = new C0578e();
        this.f583mi = C0638c.getInstance();
        C0643h.m751a(this.f574Yc);
        m468We();
        m520Ob();
    }

    private boolean play(int i) {
        C0578e c0578e = this.f574Yc;
        int i2 = c0578e.f482Ad;
        if (i2 <= -1) {
            return false;
        }
        C0580g c0580g = c0578e.f486Dd;
        if (i2 >= c0580g.f545kk) {
            return false;
        }
        C0579f[] c0579fArr = c0580g.f544jk;
        c0578e.f514_j = c0579fArr[i2].mPath;
        c0578e.f524pd = c0579fArr[i2].mName;
        String str = c0578e.f514_j;
        if (str == null || !new File(str).canRead()) {
            return false;
        }
        this.mMediaPlayer.setMPPath(this.f574Yc.f514_j);
        seekTo(i);
        mo529Va();
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessageDelayed(65289, 500L);
        return true;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Bb */
    public void mo506Bb() {
        C0578e c0578e = this.f574Yc;
        C0580g c0580g = c0578e.f486Dd;
        if (c0580g.f544jk == null) {
            return;
        }
        c0578e.f528ud = c0580g;
        m491f(c0578e.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Cb */
    public void mo507Cb() {
        if (m461Pa(1)) {
            this.f574Yc.f534xd++;
        }
        if (this.f574Yc.f530vd.size() > 0) {
            C0578e c0578e = this.f574Yc;
            if (c0578e.f534xd >= c0578e.f530vd.size()) {
                this.f574Yc.f534xd = 0;
            }
            C0578e c0578e2 = this.f574Yc;
            c0578e2.f487Dj = c0578e2.f530vd.get(c0578e2.f534xd);
        } else {
            C0578e c0578e3 = this.f574Yc;
            c0578e3.f487Dj = c0578e3.m446uc();
        }
        String str = this.f574Yc.f487Dj.mKey;
        this.f584ni.m438Na("/storage/" + str);
        if (this.f574Yc.f498Lj.size() > 0) {
            C0578e c0578e4 = this.f574Yc;
            c0578e4.f492Fj = c0578e4.f498Lj.get(str);
        }
        if (this.f574Yc.f497Kj.size() > 0) {
            C0578e c0578e5 = this.f574Yc;
            c0578e5.f493Gj = c0578e5.f497Kj.get(str);
        }
        if (this.f574Yc.f494Hj.size() > 0) {
            Iterator<C0580g> it = this.f574Yc.f494Hj.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                C0580g next = it.next();
                if (TextUtils.equals(next.mKey, str)) {
                    this.f574Yc.f489Ej = next;
                    break;
                }
            }
        }
        C0578e c0578e6 = this.f574Yc;
        c0578e6.f512Yj = c0578e6.f492Fj;
        c0578e6.f511Xj = c0578e6.f489Ej;
        c0578e6.f513Zj = c0578e6.f493Gj;
        c0578e6.f510Wj = c0578e6.f487Dj;
        c0578e6.f528ud = c0578e6.f510Wj;
        m491f(c0578e6.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Db */
    public void mo508Db() {
        if (m461Pa(2)) {
            this.f574Yc.f536yd++;
        }
        if (this.f574Yc.f532wd.size() > 0) {
            C0578e c0578e = this.f574Yc;
            if (c0578e.f536yd >= c0578e.f532wd.size()) {
                this.f574Yc.f536yd = 0;
            }
            C0578e c0578e2 = this.f574Yc;
            c0578e2.f525rj = c0578e2.f532wd.get(c0578e2.f536yd);
        } else {
            C0578e c0578e3 = this.f574Yc;
            c0578e3.f525rj = c0578e3.m447vc();
        }
        String str = this.f574Yc.f525rj.mKey;
        this.f584ni.m438Na("/storage/" + str);
        if (this.f574Yc.f538zj.size() > 0) {
            C0578e c0578e4 = this.f574Yc;
            c0578e4.f527tj = c0578e4.f538zj.get(str);
        }
        if (this.f574Yc.f537yj.size() > 0) {
            C0578e c0578e5 = this.f574Yc;
            c0578e5.f529uj = c0578e5.f537yj.get(str);
        }
        if (this.f574Yc.f531vj.size() > 0) {
            Iterator<C0580g> it = this.f574Yc.f531vj.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                C0580g next = it.next();
                if (TextUtils.equals(next.mKey, str)) {
                    this.f574Yc.f526sj = next;
                    break;
                }
            }
        }
        C0578e c0578e6 = this.f574Yc;
        c0578e6.f512Yj = c0578e6.f527tj;
        c0578e6.f511Xj = c0578e6.f526sj;
        c0578e6.f513Zj = c0578e6.f529uj;
        c0578e6.f510Wj = c0578e6.f525rj;
        c0578e6.f528ud = c0578e6.f510Wj;
        m491f(c0578e6.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Ea */
    public void mo509Ea(String str) {
        this.mHandler.postDelayed(new RunnableC0591J(this, str), 1500L);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Eb */
    public void mo510Eb() {
        this.f584ni.m438Na("/mnt/sdcard");
        C0578e c0578e = this.f574Yc;
        c0578e.f512Yj = c0578e.f504Rj;
        c0578e.f511Xj = c0578e.f502Pj;
        c0578e.f513Zj = c0578e.f505Sj;
        C0580g c0580g = c0578e.f503Qj;
        c0578e.f510Wj = c0580g;
        c0578e.f528ud = c0580g;
        m491f(c0578e.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Fb */
    public void mo511Fb() {
        this.f569Th = true;
        m527Ua();
    }

    /* renamed from: Ga */
    public void m512Ga(String str) {
        C0643h.m753a(this.f574Yc.f503Qj, str, this.isForward);
        m533a(str, this.f574Yc.f503Qj);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Gb */
    public void mo513Gb() {
        if (TextUtils.isEmpty(this.f574Yc.f514_j)) {
            return;
        }
        Context context = this.mContext;
        C0578e c0578e = this.f574Yc;
        if (C0636a.m743a(context, c0578e.f514_j, c0578e.f506Tc)) {
            C0578e c0578e2 = this.f574Yc;
            C0636a.m742a(c0578e2.f514_j, c0578e2.f506Tc);
            Iterator<InterfaceC0656a> it = f552hi.iterator();
            while (it.hasNext()) {
                it.next().mo731h(false);
            }
        } else {
            C0636a.m741a(new C0579f(m519Nb(), m518Lb()), this.f574Yc.f506Tc);
            Iterator<InterfaceC0656a> it2 = f552hi.iterator();
            while (it2.hasNext()) {
                it2.next().mo731h(true);
            }
        }
        this.f574Yc.f491Fd = m523Ra();
        if (this.f574Yc.f515ck.startsWith("/data/tw/.like")) {
            C0578e c0578e3 = this.f574Yc;
            c0578e3.f486Dd.m450c(c0578e3.f491Fd);
            C0578e c0578e4 = this.f574Yc;
            if (c0578e4.f528ud.mIndex == 4) {
                c0578e4.f528ud = c0578e4.f491Fd;
            }
            m491f(this.f574Yc.f528ud);
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
    public int m516Jb() {
        if (this.f574Yc.f514_j.contains("usb")) {
            return 0;
        }
        if (this.f574Yc.f514_j.contains("extsd")) {
            return 1;
        }
        return this.f574Yc.f514_j.contains("/mnt/sdcard/./iNand") ? 2 : 0;
    }

    /* renamed from: Kb */
    public Bitmap m517Kb() {
        return this.f574Yc.f490Fb;
    }

    /* renamed from: Lb */
    public String m518Lb() {
        return this.f574Yc.f514_j;
    }

    /* renamed from: Nb */
    public String m519Nb() {
        return this.f574Yc.f524pd;
    }

    @SuppressLint({"SdCardPath"})
    /* renamed from: Ob */
    public void m520Ob() {
        this.f584ni = new C0573c();
        if (this.f582li) {
            m530Wb();
        } else {
            m512Ga("/mnt/sdcard/iNand");
        }
        File[] listFiles = new File("/storage").listFiles(new C0629u(this));
        if (listFiles != null) {
            for (File file : listFiles) {
                m538pa(file.getAbsolutePath());
            }
        }
        File[] listFiles2 = new File("/storage").listFiles(new C0630v(this));
        if (listFiles2 != null) {
            for (File file2 : listFiles2) {
                m540qa(file2.getAbsolutePath());
            }
        }
        m459Cb("/data/tw/.like");
    }

    /* renamed from: Pb */
    public void m521Pb() {
        this.f585qi = 1.0f;
        this.f569Th = false;
        f553jd.m794w(false);
        this.mMediaPlayer.release(true);
        this.mMediaPlayer = null;
        this.mHandler.removeCallbacksAndMessages(null);
        f553jd.removeHandler("MusicIjkID3Model");
        f553jd.close();
        f553jd = null;
        this.f584ni.m442qc();
    }

    /* renamed from: Qb */
    public void m522Qb() {
        C0578e c0578e = this.f574Yc;
        C0580g c0580g = c0578e.f512Yj;
        C0579f[] c0579fArr = c0580g.f544jk;
        if (c0579fArr == null || c0579fArr.length <= 0) {
            return;
        }
        c0578e.f528ud = c0580g;
        m491f(c0578e.f528ud);
    }

    /* renamed from: Ra */
    public C0580g m523Ra() {
        C0579f[] c0579fArr = new C0579f[this.f574Yc.f506Tc.size()];
        for (int i = 0; i < this.f574Yc.f506Tc.size(); i++) {
            c0579fArr[i] = new C0579f(this.f574Yc.f506Tc.get(i).mName, this.f574Yc.f506Tc.get(i).mPath, true);
        }
        C0578e c0578e = this.f574Yc;
        c0578e.f491Fd.setLength(c0578e.f506Tc.size());
        C0578e c0578e2 = this.f574Yc;
        C0580g c0580g = c0578e2.f491Fd;
        c0580g.f544jk = c0579fArr;
        c0580g.f545kk = c0578e2.f506Tc.size();
        return this.f574Yc.f491Fd;
    }

    /* renamed from: Rb */
    public void m524Rb() {
        C0578e c0578e = this.f574Yc;
        C0580g c0580g = c0578e.f511Xj;
        C0579f[] c0579fArr = c0580g.f544jk;
        if (c0579fArr == null || c0579fArr.length <= 0) {
            return;
        }
        c0578e.f528ud = c0580g;
        m491f(c0578e.f528ud);
    }

    /* renamed from: Sb */
    public void m525Sb() {
        C0578e c0578e = this.f574Yc;
        C0580g c0580g = c0578e.f513Zj;
        C0579f[] c0579fArr = c0580g.f544jk;
        if (c0579fArr == null || c0579fArr.length <= 0) {
            return;
        }
        c0578e.f528ud = c0580g;
        m491f(c0578e.f528ud);
    }

    /* renamed from: Tb */
    public void m526Tb() {
        this.mHandler.removeMessages(65281);
        this.mHandler.removeMessages(65297);
        this.mHandler.sendEmptyMessageDelayed(65297, 500L);
    }

    /* renamed from: Ua */
    public void m527Ua() {
        if (isPlaying()) {
            this.f574Yc.f521md = this.mMediaPlayer.getCurrentPosition();
            this.mMediaPlayer.pause();
            this.mHandler.removeMessages(65281);
            m469Xe();
        }
    }

    /* renamed from: Ub */
    public void m528Ub() {
        Iterator<InterfaceC0656a> it = f552hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0578e c0578e = this.f574Yc;
            next.mo728d(c0578e.f521md, c0578e.mDuration);
            Context context = this.mContext;
            C0578e c0578e2 = this.f574Yc;
            next.mo731h(C0636a.m743a(context, c0578e2.f514_j, c0578e2.f506Tc));
            String m465Ue = m465Ue();
            String m493fd = m493fd();
            String m519Nb = m519Nb();
            Bitmap m517Kb = m517Kb();
            C0578e c0578e3 = this.f574Yc;
            next.mo725b(m465Ue, m493fd, m519Nb, m517Kb, c0578e3.f514_j, c0578e3.f515ck, c0578e3.f482Ad);
            next.mo727c(isPlaying());
            next.mo712D(m496hd());
            next.mo709B(this.mMediaPlayer.getAudioSessionId());
        }
        m491f(this.f574Yc.f528ud);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Va */
    public void mo529Va() {
        C0655t c0655t = f553jd;
        if (c0655t != null) {
            c0655t.m794w(true);
        }
        if (isPlaying()) {
            return;
        }
        this.mMediaPlayer.start();
        this.f569Th = false;
        this.mHandler.removeMessages(65281);
        this.mHandler.sendEmptyMessage(65281);
        this.mHandler.removeMessages(65290);
        this.mHandler.sendEmptyMessageDelayed(65290, 500L);
        Iterator<InterfaceC0656a> it = f552hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            String m465Ue = m465Ue();
            String m493fd = m493fd();
            String m519Nb = m519Nb();
            Bitmap m517Kb = m517Kb();
            C0578e c0578e = this.f574Yc;
            next.mo725b(m465Ue, m493fd, m519Nb, m517Kb, c0578e.f514_j, c0578e.f515ck, c0578e.f482Ad);
        }
        m491f(this.f574Yc.f528ud);
        Log.d("MusicIjkID3Model", "playMusic:playerVolume:" + this.f585qi);
        TWMediaPlayer tWMediaPlayer = this.mMediaPlayer;
        float f = this.f585qi;
        tWMediaPlayer.setVolume(f, f);
    }

    /* renamed from: Wb */
    public void m530Wb() {
        new Thread(new RunnableC0590I(this)).start();
    }

    /* renamed from: ea */
    public void m535ea(int i) {
        int i2;
        C0578e c0578e = this.f574Yc;
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
        this.f574Yc.f519kd[0] = i;
        if (i2 > 1) {
            for (int i3 = i + 1; i3 < i2; i3++) {
                int i4 = i3 - i;
                if (this.f574Yc.f517hc != 0) {
                    i4 = m501m(i, i2);
                }
                this.f574Yc.f519kd[i4] = i3;
            }
            for (int i5 = 0; i5 < i; i5++) {
                int i6 = (i5 + i2) - i;
                if (this.f574Yc.f517hc != 0) {
                    i6 = m501m(i, i2);
                }
                this.f574Yc.f519kd[i6] = i5;
            }
        }
    }

    public String getFileName() {
        C0578e c0578e = this.f574Yc;
        int i = c0578e.f482Ad;
        C0580g c0580g = c0578e.f486Dd;
        return i < c0580g.f545kk ? c0580g.f544jk[i].mName : "";
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: ka */
    public void mo536ka(int i) {
        if (i == 0) {
            C0578e c0578e = this.f574Yc;
            c0578e.f517hc = 0;
            c0578e.f518ic = 1;
            m535ea(c0578e.f482Ad);
        } else if (i == 1) {
            C0578e c0578e2 = this.f574Yc;
            c0578e2.f517hc = 0;
            c0578e2.f518ic = 2;
            m535ea(c0578e2.f482Ad);
        } else if (i == 2) {
            C0578e c0578e3 = this.f574Yc;
            c0578e3.f517hc = 1;
            c0578e3.f518ic = 1;
            m535ea(c0578e3.f482Ad);
        }
        Iterator<InterfaceC0656a> it = f552hi.iterator();
        while (it.hasNext()) {
            it.next().mo712D(m496hd());
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: la */
    public void mo537la(int i) {
        int i2;
        int i3;
        C0578e c0578e = this.f574Yc;
        C0580g c0580g = c0578e.f528ud;
        if (c0580g.f543ik == 1 && i == 0 && c0580g.f549qk != 1 && c0580g.mIndex != 4) {
            c0578e.f528ud = c0580g.f548nk;
            m491f(c0578e.f528ud);
            return;
        }
        C0580g c0580g2 = this.f574Yc.f528ud;
        if (c0580g2.f549qk != 1 && c0580g2.f543ik == 1) {
            i--;
        }
        C0578e c0578e2 = this.f574Yc;
        C0580g c0580g3 = c0578e2.f528ud;
        if (c0580g3.f543ik != 0 || (i2 = c0580g3.mIndex) == 0 || (i3 = c0580g3.f549qk) == 1 || i2 == 4) {
            C0578e c0578e3 = this.f574Yc;
            c0578e3.f482Ad = i;
            c0578e3.f486Dd.m450c(c0578e3.f528ud);
            C0578e c0578e4 = this.f574Yc;
            C0580g c0580g4 = c0578e4.f528ud;
            c0578e4.f514_j = c0580g4.f544jk[i].mPath;
            if (c0580g4.mIndex == 4) {
                c0578e4.f515ck = "/data/tw/.like";
            } else if (!TextUtils.isEmpty(c0578e4.f514_j)) {
                C0578e c0578e5 = this.f574Yc;
                String str = c0578e5.f514_j;
                c0578e5.f515ck = str.substring(0, str.lastIndexOf("/"));
            }
            m535ea(i);
            m485c(0, false);
            return;
        }
        C0579f[] c0579fArr = c0580g3.f544jk;
        this.fileName = c0579fArr[i].mName;
        this.f571Vh = c0579fArr[i].mPath;
        C0580g c0580g5 = null;
        if (i2 == 3) {
            if (i3 == 0) {
                c0580g5 = c0578e2.f507Tj.get(i);
            } else if (i3 == 2) {
                c0580g5 = c0578e2.f509Vj.get(i);
            } else if (i3 == 3) {
                c0580g5 = c0578e2.f508Uj.get(i);
            }
        } else if (i2 == 2) {
            ArrayList<C0580g> arrayList = i3 != 0 ? i3 != 2 ? i3 != 3 ? null : c0578e2.f484Bj.get(c0580g3.mKey) : c0578e2.f485Cj.get(c0580g3.mKey) : c0578e2.f483Aj.get(c0580g3.mKey);
            if (arrayList != null && arrayList.size() > i) {
                c0580g5 = arrayList.get(i);
            }
        } else if (i2 == 1) {
            ArrayList<C0580g> arrayList2 = i3 != 0 ? i3 != 2 ? i3 != 3 ? null : c0578e2.f500Nj.get(c0580g3.mKey) : c0578e2.f501Oj.get(c0580g3.mKey) : c0578e2.f499Mj.get(c0580g3.mKey);
            if (arrayList2 != null && arrayList2.size() > i) {
                c0580g5 = arrayList2.get(i);
            }
        }
        if (c0580g5 != null && c0580g5.f544jk != null) {
            C0578e c0578e6 = this.f574Yc;
            c0580g5.f548nk = c0578e6.f528ud;
            c0578e6.f528ud = C0580g.m448d(c0580g5);
            m491f(this.f574Yc.f528ud);
            return;
        }
        Log.d("MusicIjkID3Model", "setListItemPosition: " + i + ",record == null");
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onPause() {
        this.f589wg = false;
        f553jd.m792ca(Cea708CCParser.Const.CODE_C1_CW3);
        if (TextUtils.isEmpty(this.f574Yc.f514_j) || this.f574Yc.f521md <= 0) {
            return;
        }
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessage(65289);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onResume() {
        this.f589wg = true;
        C0699o.m1026a(this.mContext, "MUSIC_DATA", C0699o.f844pm, m516Jb());
        C0655t c0655t = f553jd;
        if (c0655t != null) {
            c0655t.m794w(true);
            f553jd.m792ca(3);
        }
        C0655t c0655t2 = f553jd;
        if (c0655t2 != null) {
            c0655t2.write(1296, 255);
            f553jd.write(515, 255);
        }
        this.mHandler.removeMessages(65287);
        this.mHandler.sendEmptyMessageDelayed(65287, 150L);
    }

    /* renamed from: pa */
    public void m538pa(String str) {
        Iterator<C0580g> it = this.f574Yc.f530vd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0580g c0580g = new C0580g(str, 1, 0, 0);
        C0643h.m753a(c0580g, str, this.isForward);
        m533a(str, c0580g);
        if (c0580g.mLength > 0) {
            this.f574Yc.f530vd.add(c0580g);
        }
        C0578e c0578e = this.f574Yc;
        c0578e.f487Dj = c0578e.f530vd.get(0);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: pb */
    public void mo539pb() {
        if (this.f566Qh) {
            this.f566Qh = false;
        } else {
            if (m467Ve()) {
                return;
            }
            this.mHandler.sendEmptyMessage(65282);
        }
    }

    /* renamed from: qa */
    public void m540qa(String str) {
        Iterator<C0580g> it = this.f574Yc.f532wd.iterator();
        while (it.hasNext()) {
            if (str.equals(it.next().mName)) {
                return;
            }
        }
        C0580g c0580g = new C0580g(str, 2, 0, 0);
        if (!TextUtils.isEmpty(str) && str.length() > 9) {
            c0580g.mKey = str.substring(9);
        }
        C0643h.m753a(c0580g, str, this.isForward);
        m533a(str, c0580g);
        if (c0580g.mLength > 0) {
            this.f574Yc.f532wd.add(c0580g);
        }
        if (this.f574Yc.f532wd.size() > 0) {
            C0578e c0578e = this.f574Yc;
            c0578e.f525rj = c0578e.f532wd.get(0);
        }
    }

    /* renamed from: ra */
    public void m541ra(String str) {
        C0578e c0578e;
        int i;
        this.f584ni.m436La(str);
        Iterator<C0580g> it = this.f574Yc.f530vd.iterator();
        C0580g c0580g = null;
        while (it.hasNext()) {
            C0580g next = it.next();
            if (str.equals(next.mName)) {
                c0580g = this.f574Yc.f528ud;
                if (c0580g.f543ik == 1 && c0580g.f549qk != 1) {
                    c0580g = c0580g.f548nk;
                }
                next.m453wc();
                it.remove();
            }
        }
        if (!TextUtils.isEmpty("")) {
            this.f574Yc.f498Lj.remove("");
            this.f574Yc.f497Kj.remove("");
            Iterator<C0580g> it2 = this.f574Yc.f494Hj.iterator();
            while (it2.hasNext()) {
                C0580g next2 = it2.next();
                if (TextUtils.equals(next2.mKey, "")) {
                    next2.m453wc();
                    this.f574Yc.f494Hj.remove(next2);
                }
            }
        }
        C0578e c0578e2 = this.f574Yc;
        if (c0578e2.f534xd >= c0578e2.f530vd.size()) {
            C0578e c0578e3 = this.f574Yc;
            c0578e3.f534xd = c0578e3.f530vd.size() - 1;
            C0578e c0578e4 = this.f574Yc;
            if (c0578e4.f534xd < 0) {
                c0578e4.f534xd = 0;
            }
        }
        if (c0580g == null || !str.equals(c0580g.mName)) {
            return;
        }
        if (this.f574Yc.f530vd.size() > 0) {
            C0578e c0578e5 = this.f574Yc;
            c0578e5.f487Dj = c0578e5.f530vd.get(c0578e5.f534xd);
        } else {
            C0578e c0578e6 = this.f574Yc;
            c0578e6.f487Dj = c0578e6.m447vc();
        }
        if (TextUtils.isEmpty(this.f574Yc.f487Dj.mKey)) {
            return;
        }
        C0578e c0578e7 = this.f574Yc;
        c0578e7.f492Fj = c0578e7.f498Lj.get("");
        C0578e c0578e8 = this.f574Yc;
        c0578e8.f493Gj = c0578e8.f497Kj.get("");
        if (this.f574Yc.f494Hj.size() <= 0 || (i = (c0578e = this.f574Yc).f534xd) <= 0 || i >= c0578e.f494Hj.size()) {
            this.f574Yc.f489Ej = null;
        } else {
            C0578e c0578e9 = this.f574Yc;
            c0578e9.f489Ej = c0578e9.f494Hj.get(c0578e9.f534xd);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: rb */
    public void mo542rb() {
        if (this.f566Qh) {
            this.f566Qh = false;
        } else {
            if (m467Ve()) {
                return;
            }
            this.mHandler.sendEmptyMessage(65283);
        }
    }

    /* renamed from: sa */
    public void m543sa(String str) {
        String str2;
        C0580g c0580g;
        C0578e c0578e;
        int i;
        this.f584ni.m437Ma(str);
        Iterator<C0580g> it = this.f574Yc.f532wd.iterator();
        while (true) {
            if (!it.hasNext()) {
                str2 = "";
                c0580g = null;
                break;
            }
            C0580g next = it.next();
            if (str.equals(next.mName)) {
                str2 = next.mKey;
                c0580g = this.f574Yc.f528ud;
                if (c0580g.f543ik == 1 && c0580g.f549qk != 1) {
                    c0580g = c0580g.f548nk;
                }
                next.m453wc();
                it.remove();
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            this.f574Yc.f538zj.remove(str2);
            this.f574Yc.f537yj.remove(str2);
            Iterator<C0580g> it2 = this.f574Yc.f531vj.iterator();
            while (it2.hasNext()) {
                C0580g next2 = it2.next();
                if (TextUtils.equals(next2.mKey, str2)) {
                    next2.m453wc();
                    this.f574Yc.f531vj.remove(next2);
                }
            }
        }
        C0578e c0578e2 = this.f574Yc;
        if (c0578e2.f536yd >= c0578e2.f532wd.size()) {
            C0578e c0578e3 = this.f574Yc;
            c0578e3.f536yd = c0578e3.f532wd.size() - 1;
            C0578e c0578e4 = this.f574Yc;
            if (c0578e4.f536yd < 0) {
                c0578e4.f536yd = 0;
            }
        }
        if (c0580g != null && str.equals(c0580g.mName)) {
            if (this.f574Yc.f532wd.size() > 0) {
                C0578e c0578e5 = this.f574Yc;
                c0578e5.f525rj = c0578e5.f532wd.get(c0578e5.f536yd);
            } else {
                C0578e c0578e6 = this.f574Yc;
                c0578e6.f525rj = c0578e6.m447vc();
            }
            if (!TextUtils.isEmpty(this.f574Yc.f525rj.mKey)) {
                C0578e c0578e7 = this.f574Yc;
                c0578e7.f527tj = c0578e7.f538zj.get(str2);
                C0578e c0578e8 = this.f574Yc;
                c0578e8.f529uj = c0578e8.f537yj.get(str2);
                if (this.f574Yc.f531vj.size() <= 0 || (i = (c0578e = this.f574Yc).f536yd) <= 0 || i >= c0578e.f531vj.size()) {
                    this.f574Yc.f526sj = null;
                } else {
                    C0578e c0578e9 = this.f574Yc;
                    c0578e9.f526sj = c0578e9.f531vj.get(c0578e9.f536yd);
                }
            }
        }
        mo508Db();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void seekTo(int i) {
        if (i > 0) {
            this.mMediaPlayer.seekTo(i);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: w */
    public void mo544w(boolean z) {
        f553jd.m794w(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: f */
    public void m491f(C0580g c0580g) {
        String str;
        Iterator<InterfaceC0656a> it = f552hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            if (c0580g != null) {
                C0579f[] c0579fArr = c0580g.f544jk;
                if (c0579fArr != null && c0579fArr.length > 0) {
                    for (C0579f c0579f : c0579fArr) {
                        if (c0579f != null && (str = c0579f.mPath) != null) {
                            c0579f.f539ek = C0636a.m743a(this.mContext, str, this.f574Yc.f506Tc);
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

    /* renamed from: m */
    private int m501m(int i, int i2) {
        int random;
        do {
            random = (int) (Math.random() * i2);
            if (random != 0) {
                break;
            }
        } while (i == 0);
        int i3 = random;
        while (i3 < i2 && this.f574Yc.f519kd[i3] != 0) {
            i3++;
        }
        if (i3 == i2) {
            i3 = 1;
            while (i3 < random && this.f574Yc.f519kd[i3] != 0) {
                i3++;
            }
        }
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: c */
    public void m485c(int i, boolean z) {
        int length;
        synchronized (f553jd) {
            int[] iArr = this.f574Yc.f519kd;
            if (iArr != null && (length = iArr.length) > 0) {
                int i2 = this.f574Yc.f520ld;
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
                        this.f574Yc.f482Ad = iArr[i4];
                        if (play(i3)) {
                            this.f574Yc.f520ld = i4;
                            i3 = 0;
                            break;
                        } else {
                            i4--;
                            i3 = 0;
                        }
                    }
                    if (this.f574Yc.f518ic != 0 && i4 == -1) {
                        int i5 = length - 1;
                        while (true) {
                            if (i5 <= i2) {
                                break;
                            }
                            this.f574Yc.f482Ad = iArr[i5];
                            if (play(i3)) {
                                this.f574Yc.f520ld = i5;
                                break;
                            } else {
                                i5--;
                                i3 = 0;
                            }
                        }
                        if (i5 == i2) {
                            m526Tb();
                        }
                    }
                    if (this.f574Yc.f520ld == -1) {
                        this.f574Yc.f520ld = 0;
                        this.f574Yc.f482Ad = iArr[this.f574Yc.f520ld];
                        m526Tb();
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
                        this.f574Yc.f482Ad = iArr[i7];
                        if (play(i6)) {
                            this.f574Yc.f520ld = i7;
                            i6 = 0;
                            break;
                        } else {
                            i7++;
                            i6 = 0;
                        }
                    }
                    if (this.f574Yc.f518ic != 0 && i7 == length) {
                        int i8 = 0;
                        while (true) {
                            if (i8 >= i2) {
                                break;
                            }
                            this.f574Yc.f482Ad = iArr[i8];
                            if (play(i6)) {
                                this.f574Yc.f520ld = i8;
                                break;
                            } else {
                                i8++;
                                i6 = 0;
                            }
                        }
                        if (i8 == i2) {
                            m526Tb();
                        }
                    }
                    if (this.f574Yc.f520ld == length) {
                        this.f574Yc.f520ld = length - 1;
                        this.f574Yc.f482Ad = iArr[this.f574Yc.f520ld];
                        m526Tb();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Bb */
    public void m458Bb(String str) {
        if (new File(str).exists()) {
            if (str.contains("/storage/emulated/0/")) {
                str.replace("/storage/emulated/0", "mnt/sdcard");
            }
            C0578e c0578e = this.f574Yc;
            c0578e.f514_j = str;
            c0578e.f515ck = str.substring(0, str.lastIndexOf("/"));
            C0580g c0580g = new C0580g("Playlist", 0, 0, 0);
            Context context = this.mContext;
            C0578e c0578e2 = this.f574Yc;
            C0643h.m750a(context, c0580g, c0578e2.f515ck, c0578e2.f506Tc, this.isForward, new C0592K(this, str));
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: b */
    public void mo534b(C0579f c0579f, boolean z) {
        if (!TextUtils.isEmpty(c0579f.mPath)) {
            if (z && !C0636a.m743a(this.mContext, c0579f.mPath, this.f574Yc.f506Tc)) {
                C0636a.m741a(c0579f, this.f574Yc.f506Tc);
            } else if (!z && C0636a.m743a(this.mContext, c0579f.mPath, this.f574Yc.f506Tc)) {
                C0636a.m742a(c0579f.mPath, this.f574Yc.f506Tc);
            }
        }
        Iterator<InterfaceC0656a> it = f552hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            Context context = this.mContext;
            C0578e c0578e = this.f574Yc;
            next.mo731h(C0636a.m743a(context, c0578e.f514_j, c0578e.f506Tc));
        }
        this.f574Yc.f491Fd = m523Ra();
        int i = this.f574Yc.f528ud.mIndex;
        if (i == 4 || i == 0) {
            if (this.f574Yc.f515ck.startsWith("/data/tw/.like")) {
                C0578e c0578e2 = this.f574Yc;
                c0578e2.f486Dd.m450c(c0578e2.f491Fd);
            }
            C0578e c0578e3 = this.f574Yc;
            if (c0578e3.f528ud.mIndex == 4) {
                c0578e3.f528ud = c0578e3.f491Fd;
                mo505Ab();
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: a */
    public void mo532a(InterfaceC0656a interfaceC0656a, Context context) {
        if (f552hi.size() == 0) {
            this.mContext = context;
            onCreate();
        }
        if (f552hi.contains(interfaceC0656a)) {
            return;
        }
        f552hi.add(interfaceC0656a);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: a */
    public void mo531a(InterfaceC0656a interfaceC0656a) {
        f552hi.remove(interfaceC0656a);
        C0655t c0655t = f553jd;
        if (c0655t != null) {
            C0578e c0578e = this.f574Yc;
            if (c0578e.mSource == 3) {
                C0643h.m752a(c0578e, c0655t);
            }
        }
        if (f552hi.size() == 0) {
            m521Pb();
        }
    }

    /* renamed from: a */
    private void m477a(String str, String str2, String str3, String str4) {
        Intent intent = new Intent("com.tw.music.info");
        intent.putExtra("musicTitle", str);
        intent.putExtra("musicaArtist", str2);
        intent.putExtra("musicAlbum", str3);
        intent.putExtra("musicPath", str4);
        this.mContext.sendBroadcast(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: b */
    public void m480b(int i, String str) {
        byte[] bArr = null;
        if (str == null) {
            f553jd.write(1296, i << 4, 0, (Object) null);
            return;
        }
        int i2 = this.f556Eh;
        if ((i2 & 1) == 1) {
            try {
                bArr = str.getBytes(StandardCharsets.UTF_16);
            } catch (Exception unused) {
            }
            f553jd.write(1296, (i << 4) | 0, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        if ((i2 & 2) == 2) {
            try {
                bArr = str.getBytes("Unicode");
            } catch (Exception unused2) {
            }
            f553jd.write(1296, (i << 4) | 1, bArr != null ? bArr.length : 0, bArr);
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
            if (bArr == null && (this.f556Eh & 128) == 128) {
                try {
                    bArr = str.getBytes(StandardCharsets.UTF_16);
                } catch (Exception unused5) {
                }
                i3 = 0;
            }
            f553jd.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
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
            if (bArr == null && (this.f556Eh & 128) == 128) {
                try {
                    bArr = str.getBytes(StandardCharsets.UTF_16);
                } catch (Exception unused7) {
                }
                i3 = 0;
            }
            f553jd.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
        }
    }

    /* renamed from: Cb */
    private void m459Cb(String str) {
        C0643h.m757b(this.f574Yc.f491Fd, "/data/tw/.like", this.isForward, new C0631w(this));
    }

    /* renamed from: a */
    public void m533a(String str, C0580g c0580g) {
        C0579f[] c0579fArr;
        C0529b.m181e("scanMediaID3 开始整理：" + str + "   ,可读" + new File(str).canRead());
        Log.d("MusicIjkID3Model", "scanMediaID3:" + str + " currentTime=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(System.currentTimeMillis())));
        if (str.startsWith("/mnt/sdcard")) {
            this.f583mi.f678wk.clear();
            this.f583mi.f679xk.clear();
            this.f583mi.f680yk.clear();
        } else if (str.startsWith("/storage/usb")) {
            String substring = str.substring(9);
            CopyOnWriteArrayList<C0579f> copyOnWriteArrayList = this.f583mi.f681zk.get(substring);
            if (copyOnWriteArrayList != null) {
                copyOnWriteArrayList.clear();
            }
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList2 = this.f583mi.f674Bj.get(substring);
            if (copyOnWriteArrayList2 != null) {
                copyOnWriteArrayList2.clear();
            }
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList3 = this.f583mi.f675Cj.get(substring);
            if (copyOnWriteArrayList3 != null) {
                copyOnWriteArrayList3.clear();
            }
        } else if (str.startsWith("/storage/usb")) {
            String substring2 = str.substring(9);
            CopyOnWriteArrayList<C0579f> copyOnWriteArrayList4 = this.f583mi.f673Ak.get(substring2);
            if (copyOnWriteArrayList4 != null) {
                copyOnWriteArrayList4.clear();
            }
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList5 = this.f583mi.f676Nj.get(substring2);
            if (copyOnWriteArrayList5 != null) {
                copyOnWriteArrayList5.clear();
            }
            CopyOnWriteArrayList<C0577d> copyOnWriteArrayList6 = this.f583mi.f677Oj.get(substring2);
            if (copyOnWriteArrayList6 != null) {
                copyOnWriteArrayList6.clear();
            }
        }
        if (c0580g == null || (c0579fArr = c0580g.f544jk) == null) {
            return;
        }
        new C0633y(this, c0579fArr, str).start();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Ab */
    public void mo505Ab() {
        m461Pa(4);
        C0578e c0578e = this.f574Yc;
        c0578e.f528ud = c0578e.f491Fd;
        m491f(c0578e.f528ud);
    }
}
