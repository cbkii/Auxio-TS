package com.eckom.xtlibrary.twproject.video.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.eckom.xtlibrary.R$id;
import com.eckom.xtlibrary.p066b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p066b.p071k.p055a.C0705b;
import com.eckom.xtlibrary.p066b.p071k.p057c.InterfaceC0708b;
import com.eckom.xtlibrary.twproject.video.model.BaseVideoMode;
import com.eckom.xtlibrary.twproject.video.utils.C0750b;
import com.eckom.xtlibrary.twproject.video.utils.C0758j;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;
import com.eckom.xtlibrary.twproject.video.utils.PresentationC0749a;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayerView;

/* compiled from: VideoIjkModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.m */
/* loaded from: classes3.dex */
public class C0735m<P extends AbstractC0658a> extends BaseVideoMode {

    /* renamed from: dj */
    private static InterfaceC0708b f912dj;

    /* renamed from: ej */
    private static C0735m f913ej;

    /* renamed from: fj */
    public static C0758j f914fj;

    /* renamed from: jd */
    private static C0760l f915jd;

    /* renamed from: Bi */
    private C0735m<P>.a f916Bi;

    /* renamed from: Ri */
    private boolean f923Ri;

    /* renamed from: Ti */
    private BaseVideoMode.VIDEO_MODEL_STATE f924Ti;

    /* renamed from: Ui */
    boolean f926Ui;

    /* renamed from: Wi */
    public PresentationC0749a f929Wi;
    private View layout_suspension;
    private Context mContext;
    public Display mDisplay;

    /* renamed from: r */
    private C0705b f936r;
    private View warning_driving;
    private TextView warning_tx;

    /* renamed from: Qh */
    private boolean f921Qh = false;
    public TWMediaPlayerView mMediaPlayer = null;

    /* renamed from: xi */
    private boolean f941xi = false;

    /* renamed from: Mi */
    private boolean f917Mi = false;

    /* renamed from: Ni */
    private boolean f918Ni = false;

    /* renamed from: Oi */
    private boolean f919Oi = false;

    /* renamed from: Pi */
    private boolean f920Pi = false;
    private View mRoot = null;

    /* renamed from: rh */
    private WindowManager f937rh = null;
    private WindowManager.LayoutParams mLayoutParams = null;
    private FrameLayout floatframelayout = null;

    /* renamed from: Qi */
    private double f922Qi = -1.0d;
    private int mService = 0;

    /* renamed from: wi */
    private int f940wi = 0;
    private Handler mHandler = new Handler(new C0728f(this));

    /* renamed from: ri */
    private IMediaPlayer.OnCompletionListener f938ri = new C0729g(this);

    /* renamed from: Vi */
    private int f928Vi = 7;
    private long[] mHints = new long[this.f928Vi];

    /* renamed from: ti */
    private IMediaPlayer.OnErrorListener f939ti = new C0730h(this);

    /* renamed from: Uh */
    private int f925Uh = -1;

    /* renamed from: Vh */
    private String f927Vh = "";

    /* renamed from: Xi */
    String f930Xi = "";

    /* renamed from: Yi */
    private BroadcastReceiver f931Yi = new C0731i(this);
    View.OnClickListener img_suspension_finish = new ViewOnClickListenerC0732j(this);
    View.OnClickListener img_suspension_smaller = new ViewOnClickListenerC0733k(this);
    View.OnClickListener img_suspension_bigger = new ViewOnClickListenerC0734l(this);
    View.OnClickListener img_suspension_video = new ViewOnClickListenerC0723a(this);
    View.OnClickListener img_suspension_prve = new ViewOnClickListenerC0724b(this);
    View.OnClickListener img_suspension_next = new ViewOnClickListenerC0725c(this);
    View.OnClickListener img_suspension_pp = new ViewOnClickListenerC0726d(this);

    /* renamed from: Zi */
    View.OnTouchListener f932Zi = new ViewOnTouchListenerC0727e(this);
    float startX = 0.0f;
    float startY = 0.0f;

    /* renamed from: kf */
    int f935kf = 0;

    /* renamed from: _i */
    boolean f933_i = true;

    /* renamed from: cj */
    Boolean f934cj = false;

    /* compiled from: VideoIjkModel.java */
    /* renamed from: com.eckom.xtlibrary.twproject.video.model.m$a */
    private class a extends AsyncTask<String, Void, Void> {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: L */
    public void m1160L(boolean z) {
        if (z) {
            this.mMediaPlayer.setVolume(0.5f, 0.5f);
        } else {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: N */
    public void m1161N(boolean z) {
        View view = this.warning_driving;
        if (view != null) {
            if (z) {
                view.setVisibility(8);
            } else {
                view.setVisibility(0);
            }
        }
        TextView textView = this.warning_tx;
        if (textView != null) {
            textView.setText(this.f930Xi);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Re */
    public void m1162Re() {
        if (f915jd.f992Dd.f851kk > 0) {
            if (C0760l.f988ic != 2) {
                C0760l.f990ld++;
            }
            m1177c(0, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Sa */
    public void m1163Sa() {
        C0760l.m1298Sa();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Se */
    public void m1164Se() {
        if (f915jd.f992Dd.f851kk > 0) {
            if (C0760l.f988ic != 2) {
                C0760l.f990ld--;
            }
            m1177c(0, true);
        }
    }

    /* renamed from: Ve */
    private boolean m1165Ve() {
        return (this.mHandler.hasMessages(65282) || this.mHandler.hasMessages(65283)) ? false : true;
    }

    /* renamed from: Xe */
    private void m1166Xe() {
        int duration = this.mMediaPlayer.getDuration();
        int currentPosition = this.mMediaPlayer.getCurrentPosition();
        if (duration < 0) {
            duration = 0;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        int i = ((duration <= 0 || currentPosition > duration) ? 0 : (currentPosition * 100) / duration) & 127;
        f915jd.write(40704, 9, (isPlaying() ? 128 : 0) | i);
        f915jd.write(771, 9, i | (isPlaying() ? 128 : 0));
        this.mHandler.sendEmptyMessage(40454);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ze */
    public void m1167Ze() {
        if (TextUtils.isEmpty(this.f927Vh)) {
            return;
        }
        f915jd.m1300a(this.mContext, this.f936r, this.f927Vh);
        f915jd.f1002ud = this.f936r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: bf */
    public void m1175bf() {
        try {
            ViewGroup viewGroup = (ViewGroup) this.mMediaPlayer.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(this.mMediaPlayer);
            }
        } catch (Exception e) {
            Log.i("md", "[273]" + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: cf */
    public void m1179cf() {
        if (this.mHandler.hasMessages(65288)) {
            this.mHandler.removeMessages(65288);
            this.mHandler.sendEmptyMessageDelayed(65288, 4000L);
        }
    }

    /* renamed from: df */
    private void m1182df() {
        try {
            this.mContext.unregisterReceiver(this.f931Yi);
        } catch (Exception e) {
            Log.e("VideoIjkModel", "unregisterHomeKeyReceiver:" + e.getMessage());
        }
    }

    private String getFileName() {
        int i = C0760l.f980Ad;
        C0705b c0705b = f915jd.f992Dd;
        return i < c0705b.f851kk ? c0705b.f850jk[i].mName : "";
    }

    public static C0735m getInstant() {
        if (f913ej == null) {
            f913ej = new C0735m();
        }
        return f913ej;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getService() {
        return this.mService;
    }

    /* renamed from: hd */
    private int m1190hd() {
        if (C0760l.f987hc == 0 && C0760l.f988ic == 1) {
            return 0;
        }
        if (C0760l.f987hc == 0 && C0760l.f988ic == 2) {
            return 1;
        }
        return (C0760l.f987hc == 1 && C0760l.f988ic == 1) ? 2 : -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void mute(boolean z) {
        if (z) {
            this.mMediaPlayer.setVolume(0.0f, 0.0f);
        } else {
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    private boolean play(int i) {
        try {
            if (C0760l.f980Ad <= -1 || C0760l.f980Ad >= f915jd.f992Dd.f851kk) {
                return false;
            }
            C0760l.f981Bd = f915jd.f992Dd.f850jk[C0760l.f980Ad].mPath;
            this.mMediaPlayer.setMPPath(C0760l.f981Bd);
            seekTo(i);
            mo1158ma();
            m1163Sa();
            return true;
        } catch (Exception e) {
            Log.e("VideoIjkModel", "play error:" + e.getMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stop() {
        this.mMediaPlayer.stopPlayback();
        this.mHandler.removeMessages(65281);
        m1166Xe();
    }

    /* renamed from: E */
    public void m1207E(boolean z) {
        this.f917Mi = false;
        if (z == this.f919Oi) {
            if (z) {
                return;
            }
            this.floatframelayout.setVisibility(8);
            try {
                this.f937rh.removeView(this.mRoot);
            } catch (Exception unused) {
            }
            this.f919Oi = false;
            InterfaceC0708b interfaceC0708b = f912dj;
            if (interfaceC0708b != null) {
                interfaceC0708b.mo1068n(this.f919Oi);
                return;
            }
            return;
        }
        if (z) {
            this.floatframelayout.setVisibility(0);
            try {
                m1175bf();
                this.floatframelayout.addView(this.mMediaPlayer);
                this.f937rh.addView(this.mRoot, this.mLayoutParams);
            } catch (Exception e) {
                Log.d("VideoIjkModel", Log.getStackTraceString(e));
            }
            this.mMediaPlayer.setVisibility(0);
            seekTo(C0760l.f991md);
            mo1158ma();
            this.mHandler.sendEmptyMessageDelayed(65288, 4000L);
            ((ImageView) this.mRoot.findViewById(R$id.img_suspension_pp)).getDrawable().setLevel(1);
            C0760l c0760l = f915jd;
            m1161N(c0760l.f993Ld || c0760l.f994Md == 0);
        } else {
            try {
                this.f937rh.removeView(this.mRoot);
            } catch (Exception unused2) {
            }
            this.floatframelayout.setVisibility(8);
        }
        this.f919Oi = z;
        InterfaceC0708b interfaceC0708b2 = f912dj;
        if (interfaceC0708b2 != null) {
            interfaceC0708b2.mo1068n(this.f919Oi);
        }
    }

    /* renamed from: F */
    public void m1208F(boolean z) {
        Display[] displays;
        try {
            displays = ((DisplayManager) this.mContext.getSystemService("display")).getDisplays();
        } catch (Exception e) {
            Log.w("VideoIjkModel", "showMultiScreen:" + e.getMessage());
        }
        if (displays.length < 2) {
            return;
        }
        this.mDisplay = displays[1];
        if (z) {
            if (this.mDisplay != null && this.f929Wi == null) {
                this.f929Wi = PresentationC0749a.m1293a(this.mContext, this.mDisplay);
            }
            if (this.f929Wi != null) {
                m1175bf();
                if (!this.f929Wi.m1294pa()) {
                    this.f929Wi.show();
                    try {
                        this.f929Wi.m1295qa().addView(this.mMediaPlayer);
                        this.f929Wi.m1296x(true);
                    } catch (Exception e2) {
                        Log.d("VideoIjkModel", Log.getStackTraceString(e2));
                    }
                }
                this.mHandler.removeMessages(65294);
                this.mHandler.sendEmptyMessageDelayed(65294, 1000L);
            }
        } else if (this.f929Wi != null) {
            if (this.f929Wi.m1294pa()) {
                this.f929Wi.dismiss();
                this.f929Wi.m1296x(false);
            }
            this.f929Wi = null;
        }
        InterfaceC0708b interfaceC0708b = f912dj;
        if (interfaceC0708b != null) {
            interfaceC0708b.mo1073v(z);
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* renamed from: P */
    public void mo1154P() {
        if (isPlaying()) {
            C0760l.f991md = this.mMediaPlayer.getCurrentPosition();
            this.mMediaPlayer.pause();
            m1166Xe();
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* renamed from: Pb */
    public void mo1155Pb() {
        C0758j c0758j = f914fj;
        if (c0758j != null) {
            c0758j.onDestroy();
            throw null;
        }
        try {
            if (this.f916Bi != null) {
                this.f916Bi.cancel(true);
            }
            this.f924Ti = BaseVideoMode.VIDEO_MODEL_STATE.VIDEO_MODEL_DESTROY;
            this.f918Ni = false;
            m1208F(false);
            m1207E(false);
            this.mHandler.removeMessages(65294);
            this.mHandler.removeMessages(65297);
            m1163Sa();
            m1182df();
            this.mMediaPlayer.setVisibility(8);
            this.mMediaPlayer.stopPlayback();
            f915jd.removeHandler("VideoIjkModel");
            f915jd.close();
            f915jd = null;
        } catch (Exception e) {
            Log.d("VideoIjkModel", "onDestory:" + e.getMessage());
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* renamed from: ic */
    public void mo1156ic() {
        if (!m1165Ve() || this.f921Qh) {
            return;
        }
        this.mHandler.removeMessages(65282);
        this.mHandler.sendEmptyMessageDelayed(65282, 500L);
    }

    public boolean isPlaying() {
        return this.mMediaPlayer.isPlaying();
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* renamed from: jc */
    public void mo1157jc() {
        if (!m1165Ve() || this.f921Qh) {
            return;
        }
        this.mHandler.removeMessages(65283);
        this.mHandler.sendEmptyMessageDelayed(65283, 500L);
    }

    /* renamed from: lc */
    public void m1209lc() {
        if (TextUtils.equals(SystemProperties.get("persist.tw.forcepip", "0"), "0")) {
            this.f923Ri = false;
        } else if (SystemProperties.get("sys.df.desktop", "").contains("video")) {
            this.f923Ri = true;
        } else {
            this.f923Ri = false;
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* renamed from: ma */
    public void mo1158ma() {
        if (isPlaying()) {
            return;
        }
        f915jd.m1308w(true);
        this.mMediaPlayer.start();
        this.mHandler.removeMessages(65281);
        this.mHandler.sendEmptyMessageDelayed(65281, 1000L);
        m1166Xe();
    }

    /* renamed from: mc */
    public void m1210mc() {
        int currentPosition;
        if (!this.mMediaPlayer.isPlaying() || (currentPosition = this.mMediaPlayer.getCurrentPosition() + 15000) <= 0 || currentPosition >= this.mMediaPlayer.getDuration()) {
            return;
        }
        this.mMediaPlayer.seekTo(currentPosition);
    }

    /* renamed from: nc */
    public void m1211nc() {
        int currentPosition;
        if (!this.mMediaPlayer.isPlaying() || this.mMediaPlayer.getCurrentPosition() - 10000 <= 0 || currentPosition >= this.mMediaPlayer.getDuration()) {
            return;
        }
        this.mMediaPlayer.seekTo(currentPosition);
    }

    /* renamed from: oc */
    public void m1212oc() {
        f912dj.mo1071u(C0760l.f981Bd, C0760l.f982Cd);
        f912dj.mo1057b(f915jd.f992Dd);
        f912dj.mo1056a(f915jd.f1002ud);
        f912dj.mo1061fa(getFileName());
        f912dj.mo1058c(isPlaying());
        f912dj.mo1066l(f915jd.f993Ld);
        f912dj.mo1051D(m1190hd());
        f912dj.mo1063h(C0750b.m1297b(this.mContext, C0760l.f981Bd, f915jd.f997Pd));
    }

    public void seekTo(int i) {
        this.mMediaPlayer.seekTo(i);
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* renamed from: w */
    public void mo1159w(boolean z) {
        f915jd.m1308w(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: c */
    public void m1177c(int i, boolean z) {
        int length;
        synchronized (f915jd) {
            int[] iArr = f915jd.f998kd;
            if (iArr != null && (length = iArr.length) > 0) {
                int i2 = C0760l.f990ld;
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
                        C0760l.f980Ad = iArr[i4];
                        if (play(i3)) {
                            C0760l.f990ld = i4;
                            i3 = 0;
                            break;
                        } else {
                            i4--;
                            i3 = 0;
                        }
                    }
                    if (C0760l.f988ic != 0 && i4 == -1) {
                        int i5 = length - 1;
                        while (true) {
                            if (i5 <= i2) {
                                break;
                            }
                            C0760l.f980Ad = iArr[i5];
                            if (play(i3)) {
                                C0760l.f990ld = i5;
                                break;
                            } else {
                                i5--;
                                i3 = 0;
                            }
                        }
                        if (i5 == i2) {
                            stop();
                        }
                    }
                    if (C0760l.f990ld == -1) {
                        C0760l.f990ld = 0;
                        C0760l.f980Ad = iArr[C0760l.f990ld];
                        stop();
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
                        C0760l.f980Ad = iArr[i7];
                        if (play(i6)) {
                            C0760l.f990ld = i7;
                            i6 = 0;
                            break;
                        } else {
                            i7++;
                            i6 = 0;
                        }
                    }
                    if (C0760l.f988ic != 0 && i7 == length) {
                        int i8 = 0;
                        while (true) {
                            if (i8 >= i2) {
                                break;
                            }
                            C0760l.f980Ad = iArr[i8];
                            if (play(i6)) {
                                C0760l.f990ld = i8;
                                break;
                            } else {
                                i8++;
                                i6 = 0;
                            }
                        }
                        if (i8 == i2) {
                            stop();
                        }
                    }
                    if (C0760l.f990ld == length) {
                        C0760l.f990ld = length - 1;
                        C0760l.f980Ad = iArr[C0760l.f990ld];
                        stop();
                    }
                }
            }
        }
    }
}
