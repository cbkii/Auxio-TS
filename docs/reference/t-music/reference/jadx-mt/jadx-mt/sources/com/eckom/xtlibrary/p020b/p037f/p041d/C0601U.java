package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.SystemProperties;
import android.support.v4.media.subtitle.Cea708CCParser;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0647l;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0654s;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.U */
/* JADX INFO: compiled from: MusicIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0601U<P extends AbstractC0658a> extends AbstractC0607a {

    /* JADX INFO: renamed from: jd */
    private static C0654s f593jd;
    private String fileName;
    private Context mContext;
    public TWMediaPlayer mMediaPlayer;

    /* JADX INFO: renamed from: r */
    C0580g f605r;

    /* JADX INFO: renamed from: wg */
    private boolean f608wg;

    /* JADX INFO: renamed from: hi */
    private static ArrayList<InterfaceC0656a> f592hi = new ArrayList<>();

    /* JADX INFO: renamed from: Gd */
    public static boolean f591Gd = false;

    /* JADX INFO: renamed from: ji */
    private static C0601U f594ji = null;

    /* JADX INFO: renamed from: wi */
    private int f609wi = 0;

    /* JADX INFO: renamed from: xi */
    private boolean f610xi = false;

    /* JADX INFO: renamed from: yi */
    private int f611yi = 7;

    /* JADX INFO: renamed from: Qh */
    private boolean f598Qh = false;
    private long[] mHints = new long[this.f611yi];

    /* JADX INFO: renamed from: zi */
    private boolean f612zi = false;

    /* JADX INFO: renamed from: Rh */
    private boolean f599Rh = false;

    /* JADX INFO: renamed from: Sh */
    private boolean f600Sh = false;

    /* JADX INFO: renamed from: Cg */
    private boolean f596Cg = false;

    /* JADX INFO: renamed from: Eh */
    private int f597Eh = 0;

    /* JADX INFO: renamed from: qi */
    private float f604qi = 1.0f;

    /* JADX INFO: renamed from: ri */
    private IMediaPlayer.OnCompletionListener f606ri = new C0594M(this);

    /* JADX INFO: renamed from: ti */
    private IMediaPlayer.OnErrorListener f607ti = new C0595N(this);

    /* JADX INFO: renamed from: Th */
    boolean f601Th = false;
    private Handler mHandler = new Handler(new C0596O(this));

    /* JADX INFO: renamed from: Uh */
    private int f602Uh = -1;

    /* JADX INFO: renamed from: Ai */
    private boolean f595Ai = false;

    /* JADX INFO: renamed from: Vh */
    private String f603Vh = "";

    private C0601U() {
    }

    /* JADX INFO: renamed from: Ab */
    private void m545Ab(String str) {
        C0654s c0654s = f593jd;
        c0654s.f720nd = "";
        c0654s.f721od = "";
        c0654s.f722pd = "";
        if (c0654s.f714Fb != null) {
            c0654s.f714Fb = null;
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            if (mediaMetadataRetriever.extractMetadata(12) != null) {
                f593jd.f720nd = mediaMetadataRetriever.extractMetadata(2);
                f593jd.f721od = mediaMetadataRetriever.extractMetadata(1);
                f593jd.f722pd = mediaMetadataRetriever.extractMetadata(7);
                try {
                    if (TextUtils.isEmpty(f593jd.f722pd)) {
                        f593jd.f722pd = getFileName();
                    }
                    if (TextUtils.isEmpty(f593jd.f720nd)) {
                        f593jd.f720nd = " ";
                    }
                    if (TextUtils.isEmpty(f593jd.f721od)) {
                        f593jd.f721od = " ";
                    }
                    f593jd.f722pd.getBytes(StandardCharsets.UTF_16LE);
                    f593jd.f720nd.getBytes(StandardCharsets.UTF_16LE);
                    f593jd.f721od.getBytes(StandardCharsets.UTF_16LE);
                    m561b(0, f593jd.f720nd);
                    this.mHandler.postDelayed(new RunnableC0597P(this), 100L);
                    this.mHandler.postDelayed(new RunnableC0598Q(this), 200L);
                } catch (Exception e) {
                    Log.e("MusicModel", Log.getStackTraceString(e));
                }
                m559a(f593jd.f722pd, f593jd.f720nd, f593jd.f721od, str);
                byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
                if (embeddedPicture != null) {
                    f593jd.f714Fb = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
                }
            } else {
                f593jd.f722pd = getFileName();
                f593jd.f720nd = this.mContext.getString(R.string.unknownName);
                f593jd.f721od = this.mContext.getString(R.string.unknownName);
            }
            mediaMetadataRetriever.release();
        } catch (Exception unused) {
            f593jd.f722pd = getFileName();
            f593jd.f720nd = this.mContext.getString(R.string.unknownName);
            f593jd.f721od = this.mContext.getString(R.string.unknownName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: L */
    public void m547L(boolean z) {
        if (z) {
            this.mMediaPlayer.setVolume(0.5f, 0.5f);
            this.f604qi = 0.5f;
        } else {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.f604qi = 1.0f;
        }
    }

    /* JADX INFO: renamed from: Pa */
    private boolean m548Pa(int i) {
        if (this.f602Uh == i) {
            return true;
        }
        this.f602Uh = i;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Re */
    public void m549Re() {
        C0654s c0654s = f593jd;
        int[] iArr = c0654s.f717kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0654s.f716ic != 2) {
            c0654s.f718ld++;
        }
        m565c(0, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Sa */
    public void m550Sa() {
        f593jd.m777Sa();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Se */
    public void m551Se() {
        C0654s c0654s = f593jd;
        int[] iArr = c0654s.f717kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0654s.f716ic != 2) {
            c0654s.f718ld--;
        }
        m565c(0, true);
    }

    /* JADX INFO: renamed from: Ue */
    private String m552Ue() {
        return f593jd.f720nd;
    }

    /* JADX INFO: renamed from: Ve */
    private boolean m553Ve() {
        return this.mHandler.hasMessages(65282) || this.mHandler.hasMessages(65283);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Xe */
    public void m554Xe() {
        int duration = getDuration();
        int currentPosition = getCurrentPosition();
        if (duration < 0) {
            duration = 0;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        int i = (duration <= 0 || currentPosition > duration) ? 0 : (currentPosition * 100) / duration;
        String strM582Nb = m582Nb();
        if (TextUtils.isEmpty(strM582Nb)) {
            strM582Nb = "";
        }
        int i2 = i & 127;
        f593jd.write(40704, 3, (isPlaying() ? 128 : 0) | i2, strM582Nb);
        f593jd.write(771, 3, i2 | (isPlaying() ? 128 : 0), strM582Nb);
        this.mHandler.sendEmptyMessageDelayed(40454, 500L);
    }

    /* JADX INFO: renamed from: fd */
    private String m572fd() {
        return f593jd.f721od;
    }

    private int getCurrentPosition() {
        return this.mMediaPlayer.getCurrentPosition();
    }

    private int getDuration() {
        return this.mMediaPlayer.getDuration();
    }

    public static C0601U getInstant() {
        if (f594ji == null) {
            f594ji = new C0601U();
        }
        return f594ji;
    }

    /* JADX INFO: renamed from: hd */
    private int m575hd() {
        C0654s c0654s = f593jd;
        if (c0654s.f715hc == 0 && c0654s.f716ic == 1) {
            return 0;
        }
        C0654s c0654s2 = f593jd;
        if (c0654s2.f715hc == 0 && c0654s2.f716ic == 2) {
            return 1;
        }
        C0654s c0654s3 = f593jd;
        return (c0654s3.f715hc == 1 && c0654s3.f716ic == 1) ? 2 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        return this.mMediaPlayer.isPlaying();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mute(boolean z) {
        if (z) {
            this.mMediaPlayer.setVolume(0.0f, 0.0f);
            this.f604qi = 0.0f;
        } else {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
            this.f604qi = 1.0f;
        }
    }

    private void onCreate() throws Throwable {
        this.f601Th = false;
        if (f593jd == null) {
            f593jd = C0654s.m774a(f591Gd, true, this.mContext);
            f593jd.addHandler("MusicModel", this.mHandler);
        }
        f593jd.m790z(this.f610xi);
        f593jd.m788ta(C0654s.f703Cd);
        Context context = this.mContext;
        ArrayList<C0579f> arrayList = C0654s.f712Tc;
        C0636a.m740a(context, arrayList);
        C0654s.f712Tc = arrayList;
        C0654s.f706Fd = C0654s.m773Ra();
        this.mMediaPlayer = new TWMediaPlayer(this.mContext);
        this.mMediaPlayer.setOnCompletionListener(this.f606ri);
        this.mMediaPlayer.setOnErrorListener(this.f607ti);
        String str = C0654s.f702Bd;
        if (str != null) {
            this.mMediaPlayer.setMPPath(str);
            seekTo(f593jd.f719md);
        }
    }

    private boolean play(int i) {
        int i2 = C0654s.f701Ad;
        if (i2 <= -1) {
            return false;
        }
        C0580g c0580g = C0654s.f704Dd;
        if (i2 >= c0580g.f545kk) {
            return false;
        }
        C0654s.f702Bd = c0580g.f544jk[C0654s.f701Ad].mPath;
        String str = C0654s.f702Bd;
        if (str == null) {
            return false;
        }
        this.mMediaPlayer.setMPPath(str);
        seekTo(i);
        mo529Va();
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessageDelayed(65289, 500L);
        return true;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Bb */
    public void mo506Bb() {
        f593jd.f726ud = C0654s.f704Dd;
        for (InterfaceC0656a interfaceC0656a : f592hi) {
            C0580g c0580g = f593jd.f726ud;
            if (c0580g != null) {
                interfaceC0656a.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Cb */
    public void mo507Cb() {
        if (m548Pa(1)) {
            if (f593jd.f727vd.size() > 0) {
                C0654s c0654s = f593jd;
                int i = c0654s.f729xd + 1;
                c0654s.f729xd = i;
                if (i >= c0654s.f727vd.size()) {
                    f593jd.f729xd = 0;
                }
                C0654s c0654s2 = f593jd;
                c0654s2.f726ud = c0654s2.f727vd.get(c0654s2.f729xd);
            } else {
                C0654s c0654s3 = f593jd;
                c0654s3.f726ud = c0654s3.f723qd;
            }
        } else if (f593jd.f727vd.size() > 0) {
            C0654s c0654s4 = f593jd;
            if (c0654s4.f729xd >= c0654s4.f727vd.size()) {
                f593jd.f729xd = 0;
            }
            C0654s c0654s5 = f593jd;
            c0654s5.f726ud = c0654s5.f727vd.get(c0654s5.f729xd);
        } else {
            C0654s c0654s6 = f593jd;
            c0654s6.f726ud = c0654s6.f723qd;
        }
        for (InterfaceC0656a interfaceC0656a : f592hi) {
            C0580g c0580g = f593jd.f726ud;
            if (c0580g != null) {
                interfaceC0656a.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Db */
    public void mo508Db() {
        if (m548Pa(2)) {
            if (f593jd.f728wd.size() > 0) {
                C0654s c0654s = f593jd;
                int i = c0654s.f730yd + 1;
                c0654s.f730yd = i;
                if (i >= c0654s.f728wd.size()) {
                    f593jd.f730yd = 0;
                }
                C0654s c0654s2 = f593jd;
                c0654s2.f726ud = c0654s2.f728wd.get(c0654s2.f730yd);
            } else {
                C0654s c0654s3 = f593jd;
                c0654s3.f726ud = c0654s3.f724rd;
            }
        } else if (f593jd.f728wd.size() > 0) {
            C0654s c0654s4 = f593jd;
            if (c0654s4.f730yd >= c0654s4.f728wd.size()) {
                f593jd.f730yd = 0;
            }
            C0654s c0654s5 = f593jd;
            c0654s5.f726ud = c0654s5.f728wd.get(c0654s5.f730yd);
        } else {
            C0654s c0654s6 = f593jd;
            c0654s6.f726ud = c0654s6.f724rd;
        }
        for (InterfaceC0656a interfaceC0656a : f592hi) {
            C0580g c0580g = f593jd.f726ud;
            if (c0580g != null) {
                interfaceC0656a.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Ea */
    public void mo509Ea(String str) {
        this.mHandler.postDelayed(new RunnableC0600T(this, str), 1500L);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Eb */
    public void mo510Eb() {
        C0654s c0654s = f593jd;
        c0654s.f726ud = c0654s.f725td;
        for (InterfaceC0656a interfaceC0656a : f592hi) {
            C0580g c0580g = f593jd.f726ud;
            if (c0580g != null) {
                interfaceC0656a.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Fb */
    public void mo511Fb() {
        this.f601Th = true;
        m585Ua();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Gb */
    public void mo513Gb() {
        if (TextUtils.isEmpty(C0654s.f702Bd)) {
            return;
        }
        if (C0636a.m743a(this.mContext, C0654s.f702Bd, C0654s.f712Tc)) {
            String str = C0654s.f702Bd;
            ArrayList<C0579f> arrayList = C0654s.f712Tc;
            C0636a.m742a(str, arrayList);
            C0654s.f712Tc = arrayList;
            Iterator<InterfaceC0656a> it = f592hi.iterator();
            while (it.hasNext()) {
                it.next().mo731h(false);
            }
        } else {
            C0579f c0579f = new C0579f(m582Nb(), m588Xb());
            ArrayList<C0579f> arrayList2 = C0654s.f712Tc;
            C0636a.m741a(c0579f, arrayList2);
            C0654s.f712Tc = arrayList2;
            Iterator<InterfaceC0656a> it2 = f592hi.iterator();
            while (it2.hasNext()) {
                it2.next().mo731h(true);
            }
        }
        C0580g c0580g = f593jd.f726ud;
        if (c0580g.f543ik == 1 && c0580g.mIndex == 2) {
            this.f605r = new C0580g(this.fileName, 2, 1, c0580g);
            f593jd.m778a(this.mContext, this.f605r, this.f603Vh);
            f593jd.f726ud.m451e(this.f605r);
            f593jd.f726ud = this.f605r;
            for (InterfaceC0656a interfaceC0656a : f592hi) {
                C0580g c0580g2 = f593jd.f726ud;
                if (c0580g2 != null) {
                    interfaceC0656a.mo721a(c0580g2);
                }
            }
        }
        C0654s.f706Fd = C0654s.m773Ra();
        if (f593jd.f726ud.mIndex == 4) {
            mo505Ab();
        }
        if (C0647l.m771a(this.mContext, "MUSIC_DATA", C0647l.f700Fk) == 4) {
            C0654s.f704Dd.m450c(C0654s.f706Fd);
        }
        this.mHandler.removeMessages(65288);
        this.mHandler.sendEmptyMessageDelayed(65288, 500L);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Hb */
    public void mo514Hb() {
        int currentPosition;
        if (!isPlaying() || (currentPosition = getCurrentPosition() + 15000) <= 0 || currentPosition >= getDuration()) {
            return;
        }
        seekTo(currentPosition);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Ib */
    public void mo515Ib() {
        int currentPosition;
        if (!isPlaying() || getCurrentPosition() - 10000 <= 0 || currentPosition >= getDuration()) {
            return;
        }
        seekTo(currentPosition);
    }

    /* JADX INFO: renamed from: Kb */
    public Bitmap m581Kb() {
        return f593jd.f714Fb;
    }

    /* JADX INFO: renamed from: Nb */
    public String m582Nb() {
        return f593jd.f722pd;
    }

    /* JADX INFO: renamed from: Pb */
    public void m583Pb() {
        this.f604qi = 1.0f;
        this.f601Th = false;
        f593jd.m789w(false);
        this.mMediaPlayer.release(true);
        this.mMediaPlayer = null;
        this.mHandler.removeCallbacksAndMessages(null);
        f593jd.removeHandler("MusicModel");
        f593jd.close();
        f593jd = null;
    }

    /* JADX INFO: renamed from: Tb */
    public void m584Tb() {
        this.mHandler.removeMessages(65281);
        f593jd.m789w(false);
        this.mHandler.removeMessages(65297);
        this.mHandler.sendEmptyMessageDelayed(65297, 500L);
    }

    /* JADX INFO: renamed from: Ua */
    public void m585Ua() {
        if (isPlaying()) {
            f593jd.f719md = this.mMediaPlayer.getCurrentPosition();
            this.mMediaPlayer.pause();
            this.mHandler.removeMessages(65281);
            m554Xe();
        }
    }

    /* JADX INFO: renamed from: Ub */
    public void m586Ub() {
        for (InterfaceC0656a interfaceC0656a : f592hi) {
            C0580g c0580g = f593jd.f726ud;
            if (c0580g != null) {
                interfaceC0656a.mo721a(c0580g);
            }
            C0654s c0654s = f593jd;
            interfaceC0656a.mo728d(c0654s.f719md, c0654s.mDuration);
            interfaceC0656a.mo731h(C0636a.m743a(this.mContext, C0654s.f702Bd, C0654s.f712Tc));
            interfaceC0656a.mo725b(m552Ue(), m572fd(), m582Nb(), m581Kb(), C0654s.f702Bd, C0654s.f703Cd, f593jd.f726ud.f543ik + C0654s.f701Ad);
            interfaceC0656a.mo727c(isPlaying());
            interfaceC0656a.mo712D(m575hd());
            interfaceC0656a.mo722a(this.mMediaPlayer);
            TWMediaPlayer tWMediaPlayer = this.mMediaPlayer;
            if (tWMediaPlayer != null) {
                interfaceC0656a.mo709B(tWMediaPlayer.getAudioSessionId());
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Va */
    public void mo529Va() {
        C0654s c0654s = f593jd;
        if (c0654s != null) {
            c0654s.m789w(true);
        }
        if (isPlaying()) {
            return;
        }
        this.mMediaPlayer.start();
        this.f601Th = false;
        this.mHandler.removeMessages(65281);
        this.mHandler.sendEmptyMessage(65281);
        m545Ab(C0654s.f702Bd);
        m554Xe();
        Log.d("MusicModel", "playMusic:playerVolume:" + this.f604qi);
        TWMediaPlayer tWMediaPlayer = this.mMediaPlayer;
        float f = this.f604qi;
        tWMediaPlayer.setVolume(f, f);
    }

    /* JADX INFO: renamed from: Wb */
    public void m587Wb() {
        new Thread(new RunnableC0599S(this)).start();
    }

    /* JADX INFO: renamed from: Xb */
    public String m588Xb() {
        return C0654s.f702Bd;
    }

    public String getFileName() {
        int i = C0654s.f701Ad;
        C0580g c0580g = C0654s.f704Dd;
        return i < c0580g.f545kk ? c0580g.f544jk[C0654s.f701Ad].mName : "";
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: ka */
    public void mo536ka(int i) {
        if (i == 0) {
            C0654s c0654s = f593jd;
            c0654s.f715hc = 0;
            c0654s.f716ic = 1;
            c0654s.m783ea(C0654s.f701Ad);
        } else if (i == 1) {
            C0654s c0654s2 = f593jd;
            c0654s2.f715hc = 0;
            c0654s2.f716ic = 2;
            c0654s2.m783ea(C0654s.f701Ad);
        } else if (i == 2) {
            C0654s c0654s3 = f593jd;
            c0654s3.f715hc = 1;
            c0654s3.f716ic = 1;
            c0654s3.m783ea(C0654s.f701Ad);
        }
        Iterator<InterfaceC0656a> it = f592hi.iterator();
        while (it.hasNext()) {
            it.next().mo712D(m575hd());
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: la */
    public void mo537la(int i) {
        C0654s c0654s = f593jd;
        C0580g c0580g = c0654s.f726ud;
        if (c0580g.mIndex == 4) {
            C0654s.f702Bd = C0654s.f712Tc.get(i).mPath;
            String str = C0654s.f702Bd;
            C0654s.f703Cd = str.substring(0, str.lastIndexOf("/"));
            C0654s.f704Dd.m450c(C0654s.f706Fd);
            f593jd.m783ea(i);
            m565c(0, false);
            this.f595Ai = true;
            C0647l.m772a(this.mContext, "MUSIC_DATA", C0647l.f700Fk, 4);
        } else if (c0580g.f543ik == 0 || i != 0) {
            if (f593jd.f726ud.f543ik != 0) {
                i--;
            }
            C0580g c0580g2 = f593jd.f726ud;
            if (c0580g2.f543ik != 0 || c0580g2.mIndex == 0) {
                this.f595Ai = false;
                C0654s.f701Ad = i;
                C0654s.f702Bd = f593jd.f726ud.f544jk[i].mPath;
                String str2 = C0654s.f702Bd;
                String strSubstring = str2.substring(0, str2.lastIndexOf("/"));
                if (strSubstring != null && f593jd.f726ud.f543ik == 1) {
                    C0654s.f704Dd.m450c(f593jd.f726ud);
                }
                f593jd.m783ea(i);
                C0654s.f703Cd = strSubstring;
                m565c(0, false);
                C0647l.m772a(this.mContext, "MUSIC_DATA", C0647l.f700Fk, f593jd.f726ud.mIndex);
            } else {
                C0579f[] c0579fArr = c0580g2.f544jk;
                this.fileName = c0579fArr[i].mName;
                this.f603Vh = c0579fArr[i].mPath;
                C0580g c0580gM452oa = c0580g2.m452oa(i);
                if (c0580gM452oa == null) {
                    C0580g c0580g3 = f593jd.f726ud;
                    c0580gM452oa = new C0580g(c0580g3.f544jk[i].mName, c0580g3.mIndex, c0580g3.f543ik + 1, c0580g3);
                    C0654s c0654s2 = f593jd;
                    c0654s2.m778a(this.mContext, c0580gM452oa, c0654s2.f726ud.f544jk[i].mPath);
                }
                f593jd.f726ud.m451e(c0580gM452oa);
                f593jd.f726ud = c0580gM452oa;
            }
        } else {
            c0654s.f726ud = c0580g.f548nk;
        }
        for (InterfaceC0656a interfaceC0656a : f592hi) {
            C0580g c0580g4 = f593jd.f726ud;
            if (c0580g4 != null) {
                interfaceC0656a.mo721a(c0580g4);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onPause() {
        this.f608wg = false;
        f593jd.m781ca(Cea708CCParser.Const.CODE_C1_CW3);
        if (TextUtils.isEmpty(C0654s.f702Bd) || f593jd.f719md <= 0) {
            return;
        }
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessage(65289);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onResume() {
        this.f608wg = true;
        C0654s c0654s = f593jd;
        if (c0654s != null) {
            c0654s.m789w(true);
            f593jd.m781ca(3);
        }
        f593jd.write(1296, 255);
        f593jd.write(515, 255);
        this.mHandler.removeMessages(65287);
        this.mHandler.sendEmptyMessageDelayed(65287, 666L);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: pb */
    public void mo539pb() {
        if (this.f598Qh) {
            this.f598Qh = false;
        } else {
            if (m553Ve()) {
                return;
            }
            this.mHandler.sendEmptyMessage(65282);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: rb */
    public void mo542rb() {
        if (m553Ve()) {
            return;
        }
        this.mHandler.sendEmptyMessage(65283);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void seekTo(int i) {
        this.mMediaPlayer.seekTo(i);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: w */
    public void mo544w(boolean z) {
        f593jd.m789w(z);
    }

    /* JADX INFO: renamed from: c */
    private void m565c(int i, boolean z) {
        int length;
        synchronized (f593jd) {
            int[] iArr = f593jd.f717kd;
            if (iArr != null && (length = iArr.length) > 0) {
                int i2 = f593jd.f718ld;
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
                        C0654s.f701Ad = iArr[i4];
                        if (play(i3)) {
                            f593jd.f718ld = i4;
                            i3 = 0;
                            break;
                        } else {
                            i4--;
                            i3 = 0;
                        }
                    }
                    if (f593jd.f716ic != 0 && i4 == -1) {
                        int i5 = length - 1;
                        while (true) {
                            if (i5 <= i2) {
                                break;
                            }
                            C0654s.f701Ad = iArr[i5];
                            if (play(i3)) {
                                f593jd.f718ld = i5;
                                break;
                            } else {
                                i5--;
                                i3 = 0;
                            }
                        }
                        if (i5 == i2) {
                            m584Tb();
                        }
                    }
                    if (f593jd.f718ld == -1) {
                        f593jd.f718ld = 0;
                        C0654s.f701Ad = iArr[f593jd.f718ld];
                        m584Tb();
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
                        C0654s.f701Ad = iArr[i7];
                        if (play(i6)) {
                            f593jd.f718ld = i7;
                            i6 = 0;
                            break;
                        } else {
                            i7++;
                            i6 = 0;
                        }
                    }
                    if (f593jd.f716ic != 0 && i7 == length) {
                        int i8 = 0;
                        while (true) {
                            if (i8 >= i2) {
                                break;
                            }
                            C0654s.f701Ad = iArr[i8];
                            if (play(i6)) {
                                f593jd.f718ld = i8;
                                break;
                            } else {
                                i8++;
                                i6 = 0;
                            }
                        }
                        if (i8 == i2) {
                            m584Tb();
                        }
                    }
                    if (f593jd.f718ld == length) {
                        f593jd.f718ld = length - 1;
                        C0654s.f701Ad = iArr[f593jd.f718ld];
                        m584Tb();
                    }
                }
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: b */
    public void mo534b(C0579f c0579f, boolean z) {
        if (!TextUtils.isEmpty(c0579f.mPath)) {
            if (!z) {
                String str = c0579f.mPath;
                ArrayList<C0579f> arrayList = C0654s.f712Tc;
                C0636a.m742a(str, arrayList);
                C0654s.f712Tc = arrayList;
            } else {
                ArrayList<C0579f> arrayList2 = C0654s.f712Tc;
                C0636a.m741a(c0579f, arrayList2);
                C0654s.f712Tc = arrayList2;
            }
        }
        Iterator<InterfaceC0656a> it = f592hi.iterator();
        while (it.hasNext()) {
            it.next().mo731h(C0636a.m743a(this.mContext, C0654s.f702Bd, C0654s.f712Tc));
        }
        C0654s.f706Fd = C0654s.m773Ra();
        if (this.f595Ai) {
            C0654s.f704Dd.m450c(C0654s.f706Fd);
        }
        if (f593jd.f726ud.mIndex == 4) {
            mo505Ab();
        }
        this.mHandler.removeMessages(65288);
        this.mHandler.sendEmptyMessageDelayed(65288, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Bb */
    public void m546Bb(String str) {
        if (new File(str).exists()) {
            if (str.contains("/storage/emulated/0/")) {
                str.replace("/storage/emulated/0", "mnt/sdcard");
            }
            C0654s.f702Bd = str;
            C0654s.f703Cd = str.substring(0, str.lastIndexOf("/"));
            C0580g c0580g = new C0580g("Playlist", 0, 0);
            f593jd.m778a(this.mContext, c0580g, C0654s.f703Cd);
            f593jd.f726ud.m451e(c0580g);
            C0654s c0654s = f593jd;
            c0654s.f726ud = c0580g;
            C0580g c0580g2 = c0654s.f726ud;
            c0580g2.mIndex = 0;
            C0654s.f704Dd = c0580g2;
            String strSubstring = str.substring(C0654s.f702Bd.lastIndexOf("/") + 1, C0654s.f702Bd.lastIndexOf("."));
            int i = 0;
            int i2 = 0;
            while (true) {
                C0580g c0580g3 = f593jd.f726ud;
                if (i >= c0580g3.f545kk) {
                    break;
                }
                if (strSubstring.equals(c0580g3.f544jk[i].mName)) {
                    i2 = i;
                }
                i++;
            }
            C0654s.f701Ad = i2;
            f593jd.m783ea(i2);
            m565c(0, false);
            for (InterfaceC0656a interfaceC0656a : f592hi) {
                C0580g c0580g4 = f593jd.f726ud;
                if (c0580g4 != null) {
                    interfaceC0656a.mo721a(c0580g4);
                }
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: a */
    public void mo532a(InterfaceC0656a interfaceC0656a, Context context) throws Throwable {
        if (f592hi.size() == 0) {
            this.mContext = context;
            f591Gd = Build.VERSION.SDK_INT <= 19;
            this.f610xi = SystemProperties.getBoolean("persist.sys.media.sdcardscan", false);
            onCreate();
        }
        if (f592hi.contains(interfaceC0656a)) {
            return;
        }
        f592hi.add(interfaceC0656a);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: a */
    public void mo531a(InterfaceC0656a interfaceC0656a) {
        if (f592hi.contains(interfaceC0656a)) {
            f592hi.remove(interfaceC0656a);
        }
        C0654s c0654s = f593jd;
        if (c0654s != null && c0654s.mSource == 3) {
            m550Sa();
        }
        if (f592hi.size() == 0) {
            m583Pb();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: b */
    public void m561b(int i, String str) {
        byte[] bytes = null;
        if (str == null) {
            f593jd.write(1296, i << 4, 0, (Object) null);
            return;
        }
        int i2 = this.f597Eh;
        if ((i2 & 1) == 1) {
            try {
                bytes = str.getBytes("UTF-16");
            } catch (Exception unused) {
            }
            f593jd.write(1296, (i << 4) | 0, bytes != null ? bytes.length : 0, bytes);
            return;
        }
        if ((i2 & 2) == 2) {
            try {
                bytes = str.getBytes("Unicode");
            } catch (Exception unused2) {
            }
            f593jd.write(1296, (i << 4) | 1, bytes != null ? bytes.length : 0, bytes);
            return;
        }
        int i3 = 3;
        if ((i2 & 4) == 4) {
            try {
                bytes = str.getBytes("GBK");
            } catch (Exception unused3) {
            }
            if (bytes == null) {
                try {
                    bytes = str.getBytes("GB2312");
                } catch (Exception unused4) {
                }
            } else {
                i3 = 2;
            }
            if (bytes == null && (this.f597Eh & 128) == 128) {
                try {
                    bytes = str.getBytes("UTF-16");
                } catch (Exception unused5) {
                }
                i3 = 0;
            }
            f593jd.write(1296, (i << 4) | i3, bytes != null ? bytes.length : 0, bytes);
            return;
        }
        if ((i2 & 8) == 8) {
            try {
                bytes = str.getBytes("GB2312");
            } catch (Exception unused6) {
            }
            if (bytes == null) {
                try {
                    bytes = str.getBytes("GBK");
                } catch (Exception unused7) {
                }
                i3 = 2;
            }
            if (bytes == null && (this.f597Eh & 128) == 128) {
                try {
                    bytes = str.getBytes("UTF-16");
                } catch (Exception unused8) {
                }
                i3 = 0;
            }
            f593jd.write(1296, (i << 4) | i3, bytes != null ? bytes.length : 0, bytes);
        }
    }

    /* JADX INFO: renamed from: a */
    private void m559a(String str, String str2, String str3, String str4) {
        Intent intent = new Intent("com.tw.music.info");
        intent.putExtra("musicTitle", str);
        intent.putExtra("musicaArtist", str2);
        intent.putExtra("musicAlbum", str3);
        intent.putExtra("musicPath", str4);
        this.mContext.sendBroadcast(intent);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* JADX INFO: renamed from: Ab */
    public void mo505Ab() {
        m548Pa(4);
        f593jd.f726ud = C0654s.f706Fd;
        for (InterfaceC0656a interfaceC0656a : f592hi) {
            C0580g c0580g = f593jd.f726ud;
            if (c0580g != null) {
                interfaceC0656a.mo721a(c0580g);
            }
        }
    }
}
