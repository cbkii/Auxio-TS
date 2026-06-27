package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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
import com.eckom.xtlibrary.p020b.p053j.C0687c;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MusicModel.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.ba */
/* loaded from: classes3.dex */
public class C0610ba<P extends AbstractC0658a> extends AbstractC0607a {

    /* renamed from: jd */
    private static C0654s f616jd;

    /* renamed from: Bi */
    private C0610ba<P>.a f619Bi;
    private String fileName;
    private Context mContext;
    public MediaPlayer mMediaPlayer;

    /* renamed from: r */
    C0580g f629r;

    /* renamed from: wg */
    private boolean f630wg;

    /* renamed from: hi */
    private static ArrayList<InterfaceC0656a> f615hi = new ArrayList<>();

    /* renamed from: Gd */
    public static boolean f614Gd = false;

    /* renamed from: ji */
    private static C0610ba f617ji = null;

    /* renamed from: yi */
    private int f633yi = 7;

    /* renamed from: Qh */
    private boolean f622Qh = false;
    private long[] mHints = new long[this.f633yi];

    /* renamed from: xi */
    private boolean f632xi = false;

    /* renamed from: Rh */
    private boolean f623Rh = false;

    /* renamed from: Sh */
    private boolean f624Sh = false;

    /* renamed from: Cg */
    private boolean f620Cg = false;

    /* renamed from: Eh */
    private int f621Eh = 0;

    /* renamed from: wi */
    private int f631wi = 0;

    /* renamed from: qi */
    private float f628qi = 1.0f;

    /* renamed from: Th */
    boolean f625Th = false;
    private Handler mHandler = new Handler(new C0602V(this));

    /* renamed from: Uh */
    private int f626Uh = -1;

    /* renamed from: Ai */
    private boolean f618Ai = false;

    /* renamed from: Vh */
    private String f627Vh = "";

    /* compiled from: MusicModel.java */
    /* renamed from: com.eckom.xtlibrary.b.f.d.ba$a */
    private class a extends AsyncTask<String, Void, Void> {
        private a() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public Void doInBackground(String... strArr) {
            String str = strArr[0];
            Log.d("MusicModel", "MediaScanTask doInBackground:path: " + str);
            new C0687c().m1015jb(str);
            return null;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("MusicModel", "MediaScanTask onPreExecute: ");
        }

        /* synthetic */ a(C0610ba c0610ba, C0602V c0602v) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void r2) {
            super.onPostExecute((a) r2);
            Log.d("MusicModel", "MediaScanTask onPostExecute: ");
            C0610ba.this.mHandler.removeMessages(65285);
            C0610ba.this.mHandler.sendEmptyMessage(65285);
        }
    }

    private C0610ba() {
    }

    /* renamed from: Ab */
    private void m589Ab(String str) {
        C0654s c0654s = f616jd;
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
                f616jd.f720nd = mediaMetadataRetriever.extractMetadata(2);
                f616jd.f721od = mediaMetadataRetriever.extractMetadata(1);
                f616jd.f722pd = mediaMetadataRetriever.extractMetadata(7);
                try {
                    if (TextUtils.isEmpty(f616jd.f722pd)) {
                        f616jd.f722pd = getFileName();
                    }
                    if (TextUtils.isEmpty(f616jd.f720nd)) {
                        f616jd.f720nd = " ";
                    }
                    if (TextUtils.isEmpty(f616jd.f721od)) {
                        f616jd.f721od = " ";
                    }
                    f616jd.f722pd.getBytes(StandardCharsets.UTF_16LE);
                    f616jd.f720nd.getBytes(StandardCharsets.UTF_16LE);
                    f616jd.f721od.getBytes(StandardCharsets.UTF_16LE);
                    m608b(0, f616jd.f720nd);
                    this.mHandler.postDelayed(new RunnableC0603W(this), 100L);
                    this.mHandler.postDelayed(new RunnableC0604X(this), 200L);
                } catch (Exception e) {
                    Log.e("MusicModel", Log.getStackTraceString(e));
                }
                m606a(f616jd.f722pd, f616jd.f720nd, f616jd.f721od, str);
                byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
                if (embeddedPicture != null) {
                    f616jd.f714Fb = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
                }
            } else {
                f616jd.f722pd = getFileName();
                f616jd.f720nd = this.mContext.getString(R.string.unknownName);
                f616jd.f721od = this.mContext.getString(R.string.unknownName);
            }
            mediaMetadataRetriever.release();
        } catch (Exception unused) {
            f616jd.f722pd = getFileName();
            f616jd.f720nd = this.mContext.getString(R.string.unknownName);
            f616jd.f721od = this.mContext.getString(R.string.unknownName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: L */
    public void m591L(boolean z) {
        if (z) {
            m632Mb().setVolume(0.5f, 0.5f);
            this.f628qi = 0.5f;
        } else {
            m632Mb().setVolume(1.0f, 1.0f);
            this.f628qi = 1.0f;
        }
    }

    /* renamed from: Pa */
    private boolean m592Pa(int i) {
        if (this.f626Uh == i) {
            return true;
        }
        this.f626Uh = i;
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Re */
    public void m593Re() {
        C0654s c0654s = f616jd;
        int[] iArr = c0654s.f717kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0654s.f716ic != 2) {
            c0654s.f718ld++;
        }
        m602a(0, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Sa */
    public void m594Sa() {
        f616jd.m777Sa();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Se */
    public void m595Se() {
        C0654s c0654s = f616jd;
        int[] iArr = c0654s.f717kd;
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (c0654s.f716ic != 2) {
            c0654s.f718ld--;
        }
        m602a(0, true, false);
    }

    /* renamed from: Ue */
    private String m596Ue() {
        return f616jd.f720nd;
    }

    /* renamed from: Ve */
    private boolean m597Ve() {
        return this.mHandler.hasMessages(65282) || this.mHandler.hasMessages(65283);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Xe */
    public void m598Xe() {
        int duration = getDuration();
        int currentPosition = getCurrentPosition();
        if (duration < 0) {
            duration = 0;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        int i = (duration <= 0 || currentPosition > duration) ? 0 : (currentPosition * 100) / duration;
        String m633Nb = m633Nb();
        if (TextUtils.isEmpty(m633Nb)) {
            m633Nb = "";
        }
        int i2 = i & 127;
        f616jd.write(40704, 3, (isPlaying() ? 128 : 0) | i2, m633Nb);
        f616jd.write(771, 3, i2 | (isPlaying() ? 128 : 0), m633Nb);
        this.mHandler.sendEmptyMessage(40454);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ze */
    public void m599Ze() {
        if (TextUtils.isEmpty(this.f627Vh)) {
            return;
        }
        f616jd.m778a(this.mContext, this.f629r, this.f627Vh);
        f616jd.f726ud = this.f629r;
    }

    /* renamed from: fd */
    private String m620fd() {
        return f616jd.f721od;
    }

    private int getCurrentPosition() {
        return m632Mb().getCurrentPosition();
    }

    private int getDuration() {
        return m632Mb().getDuration();
    }

    public static C0610ba getInstant() {
        if (f617ji == null) {
            f617ji = new C0610ba();
        }
        return f617ji;
    }

    /* renamed from: hd */
    private int m623hd() {
        C0654s c0654s = f616jd;
        if (c0654s.f715hc == 0 && c0654s.f716ic == 1) {
            return 0;
        }
        C0654s c0654s2 = f616jd;
        if (c0654s2.f715hc == 0 && c0654s2.f716ic == 2) {
            return 1;
        }
        C0654s c0654s3 = f616jd;
        return (c0654s3.f715hc == 1 && c0654s3.f716ic == 1) ? 2 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaying() {
        return m632Mb().isPlaying();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mute(boolean z) {
        if (z) {
            m632Mb().setVolume(0.0f, 0.0f);
            this.f628qi = 0.0f;
        } else {
            m632Mb().setVolume(1.0f, 1.0f);
            this.f628qi = 1.0f;
        }
    }

    private void onCreate() {
        this.f625Th = false;
        if (f616jd == null) {
            f616jd = C0654s.m774a(f614Gd, false, this.mContext);
            f616jd.addHandler("MusicModel", this.mHandler);
        }
        f616jd.m790z(this.f632xi);
        f616jd.m788ta(C0654s.f703Cd);
        Context context = this.mContext;
        ArrayList<C0579f> arrayList = C0654s.f712Tc;
        C0636a.m740a(context, arrayList);
        C0654s.f712Tc = arrayList;
        C0654s.f706Fd = C0654s.m773Ra();
        if (C0647l.m771a(this.mContext, "MUSIC_DATA", C0647l.f700Fk) == 4) {
            C0654s.f704Dd.m450c(C0654s.f706Fd);
            f616jd.m783ea(C0654s.f701Ad);
        }
        f616jd.f726ud = C0654s.f704Dd;
        if (m630zb(C0654s.f702Bd) == 0) {
            seekTo(f616jd.f719md);
        }
    }

    private void reset() {
        m632Mb().release();
        this.mMediaPlayer = null;
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnCompletionListener(new C0606Z(this));
        this.mMediaPlayer.setOnErrorListener(new C0608aa(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: zb */
    public int m630zb(String str) {
        m632Mb().stop();
        this.mHandler.removeMessages(65281);
        reset();
        try {
            m632Mb().setDataSource(str);
            m632Mb().prepare();
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
        f616jd.f726ud = C0654s.f704Dd;
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0580g c0580g = f616jd.f726ud;
            if (c0580g != null) {
                next.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Cb */
    public void mo507Cb() {
        if (m592Pa(1)) {
            if (f616jd.f727vd.size() > 0) {
                C0654s c0654s = f616jd;
                int i = c0654s.f729xd + 1;
                c0654s.f729xd = i;
                if (i >= c0654s.f727vd.size()) {
                    f616jd.f729xd = 0;
                }
                C0654s c0654s2 = f616jd;
                c0654s2.f726ud = c0654s2.f727vd.get(c0654s2.f729xd);
            } else {
                C0654s c0654s3 = f616jd;
                c0654s3.f726ud = c0654s3.f723qd;
            }
        } else if (f616jd.f727vd.size() > 0) {
            C0654s c0654s4 = f616jd;
            if (c0654s4.f729xd >= c0654s4.f727vd.size()) {
                f616jd.f729xd = 0;
            }
            C0654s c0654s5 = f616jd;
            c0654s5.f726ud = c0654s5.f727vd.get(c0654s5.f729xd);
        } else {
            C0654s c0654s6 = f616jd;
            c0654s6.f726ud = c0654s6.f723qd;
        }
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0580g c0580g = f616jd.f726ud;
            if (c0580g != null) {
                next.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Db */
    public void mo508Db() {
        if (m592Pa(2)) {
            if (f616jd.f728wd.size() > 0) {
                C0654s c0654s = f616jd;
                int i = c0654s.f730yd + 1;
                c0654s.f730yd = i;
                if (i >= c0654s.f728wd.size()) {
                    f616jd.f730yd = 0;
                }
                C0654s c0654s2 = f616jd;
                c0654s2.f726ud = c0654s2.f728wd.get(c0654s2.f730yd);
            } else {
                C0654s c0654s3 = f616jd;
                c0654s3.f726ud = c0654s3.f724rd;
            }
        } else if (f616jd.f728wd.size() > 0) {
            C0654s c0654s4 = f616jd;
            if (c0654s4.f730yd >= c0654s4.f728wd.size()) {
                f616jd.f730yd = 0;
            }
            C0654s c0654s5 = f616jd;
            c0654s5.f726ud = c0654s5.f728wd.get(c0654s5.f730yd);
        } else {
            C0654s c0654s6 = f616jd;
            c0654s6.f726ud = c0654s6.f724rd;
        }
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0580g c0580g = f616jd.f726ud;
            if (c0580g != null) {
                next.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Ea */
    public void mo509Ea(String str) {
        this.mHandler.postDelayed(new RunnableC0605Y(this, str), 1500L);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Eb */
    public void mo510Eb() {
        C0654s c0654s = f616jd;
        c0654s.f726ud = c0654s.f725td;
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0580g c0580g = f616jd.f726ud;
            if (c0580g != null) {
                next.mo721a(c0580g);
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Fb */
    public void mo511Fb() {
        this.f625Th = true;
        m636Ua();
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Gb */
    public void mo513Gb() {
        if (TextUtils.isEmpty(C0654s.f702Bd)) {
            return;
        }
        if (C0636a.m743a(this.mContext, C0654s.f702Bd, C0654s.f712Tc)) {
            String str = C0654s.f702Bd;
            ArrayList<C0579f> arrayList = C0654s.f712Tc;
            C0636a.m742a(str, arrayList);
            C0654s.f712Tc = arrayList;
            Iterator<InterfaceC0656a> it = f615hi.iterator();
            while (it.hasNext()) {
                it.next().mo731h(false);
            }
        } else {
            C0579f c0579f = new C0579f(m633Nb(), m639Xb());
            ArrayList<C0579f> arrayList2 = C0654s.f712Tc;
            C0636a.m741a(c0579f, arrayList2);
            C0654s.f712Tc = arrayList2;
            Iterator<InterfaceC0656a> it2 = f615hi.iterator();
            while (it2.hasNext()) {
                it2.next().mo731h(true);
            }
        }
        C0580g c0580g = f616jd.f726ud;
        if (c0580g.f543ik == 1 && c0580g.mIndex == 2) {
            this.f629r = new C0580g(this.fileName, 2, 1, c0580g);
            f616jd.m778a(this.mContext, this.f629r, this.f627Vh);
            f616jd.f726ud.m451e(this.f629r);
            f616jd.f726ud = this.f629r;
            Iterator<InterfaceC0656a> it3 = f615hi.iterator();
            while (it3.hasNext()) {
                InterfaceC0656a next = it3.next();
                C0580g c0580g2 = f616jd.f726ud;
                if (c0580g2 != null) {
                    next.mo721a(c0580g2);
                }
            }
        }
        C0654s.f706Fd = C0654s.m773Ra();
        if (f616jd.f726ud.mIndex == 4) {
            mo505Ab();
        }
        if (C0647l.m771a(this.mContext, "MUSIC_DATA", C0647l.f700Fk) == 4) {
            C0654s.f704Dd.m450c(C0654s.f706Fd);
        }
        this.mHandler.removeMessages(65288);
        this.mHandler.sendEmptyMessageDelayed(65288, 500L);
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

    /* renamed from: Kb */
    public Bitmap m631Kb() {
        return f616jd.f714Fb;
    }

    /* renamed from: Mb */
    public MediaPlayer m632Mb() {
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = new MediaPlayer();
        }
        return this.mMediaPlayer;
    }

    /* renamed from: Nb */
    public String m633Nb() {
        return f616jd.f722pd;
    }

    /* renamed from: Pb */
    public void m634Pb() {
        this.f628qi = 1.0f;
        this.f625Th = false;
        this.f619Bi.cancel(true);
        f616jd.m789w(false);
        m632Mb().release();
        this.mMediaPlayer = null;
        this.mHandler.removeCallbacksAndMessages(null);
        f616jd.removeHandler("MusicModel");
        f616jd.close();
        f616jd = null;
    }

    /* renamed from: Tb */
    public void m635Tb() {
        this.mHandler.removeMessages(65281);
        this.mHandler.removeMessages(65297);
        this.mHandler.sendEmptyMessageDelayed(65297, 500L);
    }

    /* renamed from: Ua */
    public void m636Ua() {
        if (isPlaying()) {
            f616jd.f719md = m632Mb().getCurrentPosition();
            m632Mb().pause();
            this.mHandler.removeMessages(65281);
            m598Xe();
        }
    }

    /* renamed from: Ub */
    public void m637Ub() {
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0580g c0580g = f616jd.f726ud;
            if (c0580g != null) {
                next.mo721a(c0580g);
            }
            C0654s c0654s = f616jd;
            next.mo728d(c0654s.f719md, c0654s.mDuration);
            next.mo731h(C0636a.m743a(this.mContext, C0654s.f702Bd, C0654s.f712Tc));
            next.mo725b(m596Ue(), m620fd(), m633Nb(), m631Kb(), C0654s.f702Bd, C0654s.f703Cd, f616jd.f726ud.f543ik + C0654s.f701Ad);
            next.mo727c(isPlaying());
            next.mo712D(m623hd());
            next.mo720a(m632Mb());
            if (m632Mb() != null) {
                next.mo709B(m632Mb().getAudioSessionId());
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Va */
    public void mo529Va() {
        C0654s c0654s = f616jd;
        if (c0654s != null) {
            c0654s.m789w(true);
        }
        if (isPlaying()) {
            return;
        }
        m632Mb().start();
        this.f625Th = false;
        this.mHandler.removeMessages(65281);
        this.mHandler.sendEmptyMessage(65281);
        m589Ab(C0654s.f702Bd);
        m598Xe();
        Log.d("MusicModel", "playMusic:playerVolume:" + this.f628qi);
        MediaPlayer m632Mb = m632Mb();
        float f = this.f628qi;
        m632Mb.setVolume(f, f);
    }

    /* renamed from: Wb */
    public void m638Wb() {
        C0610ba<P>.a aVar = this.f619Bi;
        C0602V c0602v = null;
        if (aVar != null) {
            aVar.cancel(true);
            this.f619Bi = null;
        }
        this.f619Bi = new a(this, c0602v);
        this.f619Bi.execute("/mnt/sdcard");
    }

    /* renamed from: Xb */
    public String m639Xb() {
        return C0654s.f702Bd;
    }

    public String getFileName() {
        int i = C0654s.f701Ad;
        C0580g c0580g = C0654s.f704Dd;
        return i < c0580g.f545kk ? c0580g.f544jk[C0654s.f701Ad].mName : "";
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: ka */
    public void mo536ka(int i) {
        if (i == 0) {
            C0654s c0654s = f616jd;
            c0654s.f715hc = 0;
            c0654s.f716ic = 1;
            c0654s.m783ea(C0654s.f701Ad);
        } else if (i == 1) {
            C0654s c0654s2 = f616jd;
            c0654s2.f715hc = 0;
            c0654s2.f716ic = 2;
            c0654s2.m783ea(C0654s.f701Ad);
        } else if (i == 2) {
            C0654s c0654s3 = f616jd;
            c0654s3.f715hc = 1;
            c0654s3.f716ic = 1;
            c0654s3.m783ea(C0654s.f701Ad);
        }
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            it.next().mo712D(m623hd());
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: la */
    public void mo537la(int i) {
        try {
            if (f616jd.f726ud.mIndex == 4) {
                C0654s.f702Bd = C0654s.f712Tc.get(i).mPath;
                C0654s.f703Cd = C0654s.f702Bd.substring(0, C0654s.f702Bd.lastIndexOf("/"));
                C0654s.f704Dd.m450c(C0654s.f706Fd);
                f616jd.m783ea(i);
                m602a(0, false, true);
                this.f618Ai = true;
                C0647l.m772a(this.mContext, "MUSIC_DATA", C0647l.f700Fk, 4);
            } else if (f616jd.f726ud.f543ik == 0 || i != 0) {
                if (f616jd.f726ud.f543ik != 0) {
                    i--;
                }
                if (f616jd.f726ud.f543ik != 0 || f616jd.f726ud.mIndex == 0) {
                    this.f618Ai = false;
                    C0654s.f701Ad = i;
                    C0654s.f702Bd = f616jd.f726ud.f544jk[i].mPath;
                    String substring = C0654s.f702Bd.substring(0, C0654s.f702Bd.lastIndexOf("/"));
                    if (substring != null && f616jd.f726ud.f543ik == 1) {
                        C0654s.f704Dd.m450c(f616jd.f726ud);
                    }
                    f616jd.m783ea(i);
                    C0654s.f703Cd = substring;
                    m602a(0, false, true);
                    C0647l.m772a(this.mContext, "MUSIC_DATA", C0647l.f700Fk, f616jd.f726ud.mIndex);
                } else {
                    this.fileName = f616jd.f726ud.f544jk[i].mName;
                    this.f627Vh = f616jd.f726ud.f544jk[i].mPath;
                    this.f629r = new C0580g(f616jd.f726ud.f544jk[i].mName, f616jd.f726ud.mIndex, f616jd.f726ud.f543ik + 1, f616jd.f726ud);
                    f616jd.m778a(this.mContext, this.f629r, this.f627Vh);
                    f616jd.f726ud.m451e(this.f629r);
                    f616jd.f726ud = this.f629r;
                }
            } else {
                f616jd.f726ud = f616jd.f726ud.f548nk;
            }
            Iterator<InterfaceC0656a> it = f615hi.iterator();
            while (it.hasNext()) {
                InterfaceC0656a next = it.next();
                if (f616jd.f726ud != null) {
                    next.mo721a(f616jd.f726ud);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onPause() {
        this.f630wg = false;
        f616jd.m781ca(Cea708CCParser.Const.CODE_C1_CW3);
        if (TextUtils.isEmpty(C0654s.f702Bd) || f616jd.f719md <= 0) {
            return;
        }
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessage(65289);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void onResume() {
        this.f630wg = true;
        C0654s c0654s = f616jd;
        if (c0654s != null) {
            c0654s.m789w(true);
            f616jd.m781ca(3);
        }
        f616jd.write(274, 255);
        f616jd.write(1296, 255);
        f616jd.write(515, 255);
        this.mHandler.removeMessages(65287);
        this.mHandler.sendEmptyMessageDelayed(65287, 666L);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: pb */
    public void mo539pb() {
        if (this.f622Qh) {
            this.f622Qh = false;
        }
        if (m597Ve()) {
            return;
        }
        this.mHandler.sendEmptyMessage(65282);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: rb */
    public void mo542rb() {
        if (this.f622Qh) {
            this.f622Qh = false;
        } else {
            if (m597Ve()) {
                return;
            }
            this.mHandler.sendEmptyMessage(65283);
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    public void seekTo(int i) {
        int duration = m632Mb().getDuration();
        if (i <= 0 || duration <= 0 || i >= duration) {
            return;
        }
        m632Mb().seekTo(i);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: w */
    public void mo544w(boolean z) {
        f616jd.m789w(z);
    }

    /* renamed from: d */
    private boolean m613d(int i, boolean z) {
        int i2 = C0654s.f701Ad;
        if (i2 <= -1) {
            return false;
        }
        C0580g c0580g = C0654s.f704Dd;
        if (i2 >= c0580g.f545kk) {
            return false;
        }
        C0654s.f702Bd = c0580g.f544jk[C0654s.f701Ad].mPath;
        ArrayList<C0579f> arrayList = C0654s.f705Ed;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<C0579f> it = C0654s.f705Ed.iterator();
            while (it.hasNext()) {
                if (it.next().mPath.equals(C0654s.f702Bd)) {
                    if (z) {
                        mo539pb();
                    } else {
                        mo542rb();
                    }
                    return true;
                }
            }
        }
        if (m630zb(C0654s.f702Bd) != 0) {
            return false;
        }
        seekTo(i);
        mo529Va();
        this.mHandler.removeMessages(65289);
        this.mHandler.sendEmptyMessageDelayed(65289, 500L);
        return true;
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: b */
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
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            it.next().mo731h(C0636a.m743a(this.mContext, C0654s.f702Bd, C0654s.f712Tc));
        }
        C0654s.f706Fd = C0654s.m773Ra();
        if (this.f618Ai) {
            C0654s.f704Dd.m450c(C0654s.f706Fd);
        }
        if (f616jd.f726ud.mIndex == 4) {
            mo505Ab();
        }
        this.mHandler.removeMessages(65288);
        this.mHandler.sendEmptyMessageDelayed(65288, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Bb */
    public void m590Bb(String str) {
        if (new File(str).exists()) {
            if (str.contains("/storage/emulated/0/")) {
                str.replace("/storage/emulated/0", "mnt/sdcard");
            }
            C0654s.f702Bd = str;
            C0654s.f703Cd = str.substring(0, str.lastIndexOf("/"));
            C0580g c0580g = new C0580g("Playlist", 0, 0);
            f616jd.m778a(this.mContext, c0580g, C0654s.f703Cd);
            f616jd.f726ud.m451e(c0580g);
            C0654s c0654s = f616jd;
            c0654s.f726ud = c0580g;
            C0580g c0580g2 = c0654s.f726ud;
            c0580g2.mIndex = 0;
            C0654s.f704Dd = c0580g2;
            String substring = str.substring(C0654s.f702Bd.lastIndexOf("/") + 1, C0654s.f702Bd.lastIndexOf("."));
            int i = 0;
            int i2 = 0;
            while (true) {
                C0580g c0580g3 = f616jd.f726ud;
                if (i >= c0580g3.f545kk) {
                    break;
                }
                if (substring.equals(c0580g3.f544jk[i].mName)) {
                    i2 = i;
                }
                i++;
            }
            C0654s.f701Ad = i2;
            f616jd.m783ea(i2);
            m602a(0, false, true);
            Iterator<InterfaceC0656a> it = f615hi.iterator();
            while (it.hasNext()) {
                InterfaceC0656a next = it.next();
                C0580g c0580g4 = f616jd.f726ud;
                if (c0580g4 != null) {
                    next.mo721a(c0580g4);
                }
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: a */
    public void mo532a(InterfaceC0656a interfaceC0656a, Context context) {
        if (f615hi.size() == 0) {
            this.mContext = context;
            f614Gd = Build.VERSION.SDK_INT <= 19;
            this.f632xi = SystemProperties.getBoolean("persist.sys.media.sdcardscan", false);
            onCreate();
        }
        if (f615hi.contains(interfaceC0656a)) {
            return;
        }
        f615hi.add(interfaceC0656a);
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: a */
    public void mo531a(InterfaceC0656a interfaceC0656a) {
        if (f615hi.contains(interfaceC0656a)) {
            f615hi.remove(interfaceC0656a);
        }
        C0654s c0654s = f616jd;
        if (c0654s != null && c0654s.mSource == 3) {
            m594Sa();
        }
        if (f615hi.size() == 0) {
            m634Pb();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: b */
    public void m608b(int i, String str) {
        byte[] bArr = null;
        if (str == null) {
            f616jd.write(1296, i << 4, 0, (Object) null);
            return;
        }
        int i2 = this.f621Eh;
        if ((i2 & 1) == 1) {
            try {
                bArr = str.getBytes("UTF-16");
            } catch (Exception unused) {
            }
            f616jd.write(1296, (i << 4) | 0, bArr != null ? bArr.length : 0, bArr);
            return;
        }
        if ((i2 & 2) == 2) {
            try {
                bArr = str.getBytes("Unicode");
            } catch (Exception unused2) {
            }
            f616jd.write(1296, (i << 4) | 1, bArr != null ? bArr.length : 0, bArr);
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
            if (bArr == null && (this.f621Eh & 128) == 128) {
                try {
                    bArr = str.getBytes("UTF-16");
                } catch (Exception unused5) {
                }
                i3 = 0;
            }
            f616jd.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
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
                } catch (Exception unused7) {
                }
                i3 = 2;
            }
            if (bArr == null && (this.f621Eh & 128) == 128) {
                try {
                    bArr = str.getBytes("UTF-16");
                } catch (Exception unused8) {
                }
                i3 = 0;
            }
            f616jd.write(1296, (i << 4) | i3, bArr != null ? bArr.length : 0, bArr);
        }
    }

    /* renamed from: a */
    private void m602a(int i, boolean z, boolean z2) {
        int length;
        synchronized (f616jd) {
            int[] iArr = f616jd.f717kd;
            if (iArr != null && (length = iArr.length) > 0) {
                int i2 = f616jd.f718ld;
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
                        if (m613d(i3, z2)) {
                            f616jd.f718ld = i4;
                            i3 = 0;
                            break;
                        } else {
                            i4--;
                            i3 = 0;
                        }
                    }
                    if (f616jd.f716ic != 0 && i4 == -1) {
                        int i5 = length - 1;
                        while (true) {
                            if (i5 <= i2) {
                                break;
                            }
                            C0654s.f701Ad = iArr[i5];
                            if (m613d(i3, z2)) {
                                f616jd.f718ld = i5;
                                break;
                            } else {
                                i5--;
                                i3 = 0;
                            }
                        }
                        if (i5 == i2) {
                            m635Tb();
                        }
                    }
                    if (f616jd.f718ld == -1) {
                        f616jd.f718ld = 0;
                        C0654s.f701Ad = iArr[f616jd.f718ld];
                        m635Tb();
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
                        if (m613d(i6, z2)) {
                            f616jd.f718ld = i7;
                            i6 = 0;
                            break;
                        } else {
                            i7++;
                            i6 = 0;
                        }
                    }
                    if (f616jd.f716ic != 0 && i7 == length) {
                        int i8 = 0;
                        while (true) {
                            if (i8 >= i2) {
                                break;
                            }
                            C0654s.f701Ad = iArr[i8];
                            if (m613d(i6, z2)) {
                                f616jd.f718ld = i8;
                                break;
                            } else {
                                i8++;
                                i6 = 0;
                            }
                        }
                        if (i8 == i2) {
                            m635Tb();
                        }
                    }
                    if (f616jd.f718ld == length) {
                        f616jd.f718ld = length - 1;
                        C0654s.f701Ad = iArr[f616jd.f718ld];
                        m635Tb();
                    }
                }
            }
        }
    }

    @Override // com.eckom.xtlibrary.p020b.p037f.p041d.AbstractC0607a
    /* renamed from: Ab */
    public void mo505Ab() {
        m592Pa(4);
        f616jd.f726ud = C0654s.f706Fd;
        Iterator<InterfaceC0656a> it = f615hi.iterator();
        while (it.hasNext()) {
            InterfaceC0656a next = it.next();
            C0580g c0580g = f616jd.f726ud;
            if (c0580g != null) {
                next.mo721a(c0580g);
            }
        }
    }

    /* renamed from: a */
    private void m606a(String str, String str2, String str3, String str4) {
        Intent intent = new Intent("com.tw.music.info");
        intent.putExtra("musicTitle", str);
        intent.putExtra("musicaArtist", str2);
        intent.putExtra("musicAlbum", str3);
        intent.putExtra("musicPath", str4);
        this.mContext.sendBroadcast(intent);
    }
}
