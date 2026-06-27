package com.eckom.xtlibrary.twproject.video.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaPlayer;
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
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p054k.p055a.C0705b;
import com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b;
import com.eckom.xtlibrary.twproject.video.model.BaseVideoMode;
import com.eckom.xtlibrary.twproject.video.utils.C0750b;
import com.eckom.xtlibrary.twproject.video.utils.C0758j;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;
import com.eckom.xtlibrary.twproject.video.utils.MediaView;
import com.eckom.xtlibrary.twproject.video.utils.PresentationC0749a;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.z */
/* JADX INFO: compiled from: VideoModel.java */
/* JADX INFO: loaded from: classes3.dex */
public class C0748z<P extends AbstractC0658a> extends BaseVideoMode {

    /* JADX INFO: renamed from: dj */
    private static InterfaceC0708b f944dj;

    /* JADX INFO: renamed from: ej */
    private static C0748z f945ej;

    /* JADX INFO: renamed from: fj */
    public static C0758j f946fj;

    /* JADX INFO: renamed from: jd */
    private static C0760l f947jd;
    public static MediaView mMediaPlayer;

    /* JADX INFO: renamed from: Bi */
    private C0748z<P>.a f948Bi;

    /* JADX INFO: renamed from: Ri */
    private boolean f955Ri;

    /* JADX INFO: renamed from: Ti */
    private BaseVideoMode.VIDEO_MODEL_STATE f956Ti;

    /* JADX INFO: renamed from: Ui */
    boolean f958Ui;

    /* JADX INFO: renamed from: Wi */
    public PresentationC0749a f961Wi;
    private View layout_suspension;
    private Context mContext;
    public Display mDisplay;

    /* JADX INFO: renamed from: r */
    private C0705b f968r;
    private View warning_driving;
    private TextView warning_tx;

    /* JADX INFO: renamed from: Qh */
    private boolean f953Qh = false;

    /* JADX INFO: renamed from: xi */
    private boolean f973xi = false;

    /* JADX INFO: renamed from: Mi */
    private boolean f949Mi = false;

    /* JADX INFO: renamed from: Ni */
    private boolean f950Ni = false;

    /* JADX INFO: renamed from: Oi */
    private boolean f951Oi = false;

    /* JADX INFO: renamed from: Pi */
    private boolean f952Pi = false;
    private View mRoot = null;

    /* JADX INFO: renamed from: rh */
    private WindowManager f969rh = null;
    private WindowManager.LayoutParams mLayoutParams = null;
    private FrameLayout floatframelayout = null;

    /* JADX INFO: renamed from: Qi */
    private double f954Qi = -1.0d;
    private int mService = 0;

    /* JADX INFO: renamed from: wi */
    private int f972wi = 0;
    private Handler mHandler = new Handler(new C0740r(this));

    /* JADX INFO: renamed from: ri */
    private MediaPlayer.OnCompletionListener f970ri = new C0741s(this);

    /* JADX INFO: renamed from: Vi */
    private int f960Vi = 7;
    private long[] mHints = new long[this.f960Vi];

    /* JADX INFO: renamed from: ti */
    private MediaPlayer.OnErrorListener f971ti = new C0742t(this);

    /* JADX INFO: renamed from: Uh */
    private int f957Uh = -1;

    /* JADX INFO: renamed from: Vh */
    private String f959Vh = "";

    /* JADX INFO: renamed from: Xi */
    String f962Xi = "";

    /* JADX INFO: renamed from: Yi */
    private BroadcastReceiver f963Yi = new C0743u(this);
    View.OnClickListener img_suspension_finish = new ViewOnClickListenerC0744v(this);
    View.OnClickListener img_suspension_smaller = new ViewOnClickListenerC0745w(this);
    View.OnClickListener img_suspension_bigger = new ViewOnClickListenerC0746x(this);
    View.OnClickListener img_suspension_video = new ViewOnClickListenerC0747y(this);
    View.OnClickListener img_suspension_prve = new ViewOnClickListenerC0736n(this);
    View.OnClickListener img_suspension_next = new ViewOnClickListenerC0737o(this);
    View.OnClickListener img_suspension_pp = new ViewOnClickListenerC0738p(this);

    /* JADX INFO: renamed from: Zi */
    View.OnTouchListener f964Zi = new ViewOnTouchListenerC0739q(this);
    float startX = 0.0f;
    float startY = 0.0f;

    /* JADX INFO: renamed from: kf */
    int f967kf = 0;

    /* JADX INFO: renamed from: _i */
    boolean f965_i = true;

    /* JADX INFO: renamed from: cj */
    Boolean f966cj = false;

    /* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.z$a */
    /* JADX INFO: compiled from: VideoModel.java */
    private class a extends AsyncTask<String, Void, Void> {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: L */
    public void m1213L(boolean z) {
        if (z) {
            mMediaPlayer.setVolume(0.5f, 0.5f);
        } else {
            mMediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: N */
    public void m1214N(boolean z) {
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
            textView.setText(this.f962Xi);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Re */
    public void m1215Re() {
        if (f947jd.f992Dd.f851kk > 0) {
            if (C0760l.f988ic != 2) {
                C0760l.f990ld++;
            }
            m1230c(0, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Sa */
    public void m1216Sa() throws Throwable {
        C0760l.m1298Sa();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Se */
    public void m1217Se() {
        if (f947jd.f992Dd.f851kk > 0) {
            if (C0760l.f988ic != 2) {
                C0760l.f990ld--;
            }
            m1230c(0, true);
        }
    }

    /* JADX INFO: renamed from: Ve */
    private boolean m1218Ve() {
        return (this.mHandler.hasMessages(65282) || this.mHandler.hasMessages(65283)) ? false : true;
    }

    /* JADX INFO: renamed from: Xe */
    private void m1219Xe() {
        int duration = mMediaPlayer.getDuration();
        int currentPosition = mMediaPlayer.getCurrentPosition();
        if (duration < 0) {
            duration = 0;
        }
        if (currentPosition < 0) {
            currentPosition = 0;
        }
        int i = (duration <= 0 || currentPosition > duration) ? 0 : (currentPosition * 100) / duration;
        C0760l c0760l = f947jd;
        if (c0760l != null) {
            int i2 = i & 127;
            c0760l.write(40704, 9, (isPlaying() ? 128 : 0) | i2);
            f947jd.write(771, 9, i2 | (isPlaying() ? 128 : 0));
        }
        this.mHandler.sendEmptyMessage(40454);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: Ze */
    public void m1220Ze() {
        if (TextUtils.isEmpty(this.f959Vh)) {
            return;
        }
        f947jd.m1300a(this.mContext, this.f968r, this.f959Vh);
        f947jd.f1002ud = this.f968r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: bf */
    public void m1228bf() {
        try {
            ViewGroup viewGroup = (ViewGroup) mMediaPlayer.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mMediaPlayer);
            }
        } catch (Exception e) {
            Log.i("md", "[273]" + e.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: cf */
    public void m1232cf() {
        if (this.mHandler.hasMessages(65288)) {
            this.mHandler.removeMessages(65288);
            this.mHandler.sendEmptyMessageDelayed(65288, 4000L);
        }
    }

    /* JADX INFO: renamed from: df */
    private void m1235df() {
        try {
            this.mContext.getApplicationContext().unregisterReceiver(this.f963Yi);
        } catch (Exception e) {
            Log.e("VideoModel", "unregisterHomeKeyReceiver:" + e.getMessage());
        }
    }

    private String getFileName() {
        int i = C0760l.f980Ad;
        C0705b c0705b = f947jd.f992Dd;
        return i < c0705b.f851kk ? c0705b.f850jk[i].mName : "";
    }

    public static C0748z getInstant() {
        if (f945ej == null) {
            f945ej = new C0748z();
        }
        return f945ej;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getService() {
        return this.mService;
    }

    /* JADX INFO: renamed from: hd */
    private int m1243hd() {
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
            mMediaPlayer.setVolume(0.0f, 0.0f);
        } else {
            mMediaPlayer.setVolume(1.0f, 1.0f);
        }
    }

    private boolean play(int i) throws Throwable {
        try {
            if (C0760l.f980Ad <= -1 || C0760l.f980Ad >= f947jd.f992Dd.f851kk) {
                return false;
            }
            C0760l.f981Bd = f947jd.f992Dd.f850jk[C0760l.f980Ad].mPath;
            mMediaPlayer.setVideoPath(C0760l.f981Bd);
            seekTo(i);
            mo1158ma();
            m1216Sa();
            return true;
        } catch (Exception e) {
            Log.e("VideoModel", "play error:" + e.getMessage());
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stop() {
        mMediaPlayer.stopPlayback();
        this.mHandler.removeMessages(65281);
        m1219Xe();
    }

    /* JADX INFO: renamed from: E */
    public void m1260E(boolean z) {
        this.f949Mi = false;
        if (z == this.f951Oi) {
            if (z) {
                return;
            }
            this.floatframelayout.setVisibility(8);
            try {
                this.f969rh.removeView(this.mRoot);
            } catch (Exception unused) {
            }
            this.f951Oi = false;
            InterfaceC0708b interfaceC0708b = f944dj;
            if (interfaceC0708b != null) {
                interfaceC0708b.mo1068n(this.f951Oi);
                return;
            }
            return;
        }
        if (z) {
            this.floatframelayout.setVisibility(0);
            try {
                m1228bf();
                this.floatframelayout.addView(mMediaPlayer);
                this.f969rh.addView(this.mRoot, this.mLayoutParams);
            } catch (Exception e) {
                Log.d("VideoModel", Log.getStackTraceString(e));
            }
            mMediaPlayer.setVisibility(0);
            seekTo(C0760l.f991md);
            mo1158ma();
            this.mHandler.sendEmptyMessageDelayed(65288, 4000L);
            ((ImageView) this.mRoot.findViewById(R$id.img_suspension_pp)).getDrawable().setLevel(1);
            C0760l c0760l = f947jd;
            m1214N(c0760l.f993Ld || c0760l.f994Md == 0);
        } else {
            try {
                this.f969rh.removeView(this.mRoot);
            } catch (Exception unused2) {
            }
            this.floatframelayout.setVisibility(8);
        }
        this.f951Oi = z;
        InterfaceC0708b interfaceC0708b2 = f944dj;
        if (interfaceC0708b2 != null) {
            interfaceC0708b2.mo1068n(this.f951Oi);
        }
    }

    /* JADX INFO: renamed from: F */
    public void m1261F(boolean z) {
        try {
            Display[] displays = ((DisplayManager) this.mContext.getSystemService("display")).getDisplays();
            if (displays.length < 2) {
                return;
            }
            this.mDisplay = displays[1];
            if (z) {
                if (this.mDisplay != null && this.f961Wi == null) {
                    this.f961Wi = PresentationC0749a.m1293a(this.mContext, this.mDisplay);
                }
                if (this.f961Wi != null) {
                    m1228bf();
                    if (!this.f961Wi.m1294pa()) {
                        this.f961Wi.show();
                        try {
                            this.f961Wi.m1295qa().addView(mMediaPlayer);
                            this.f961Wi.m1296x(true);
                        } catch (Exception e) {
                            Log.d("VideoModel", Log.getStackTraceString(e));
                        }
                    }
                    this.mHandler.removeMessages(65294);
                    this.mHandler.sendEmptyMessageDelayed(65294, 1000L);
                }
            } else if (this.f961Wi != null) {
                if (this.f961Wi.m1294pa()) {
                    this.f961Wi.dismiss();
                    this.f961Wi.m1296x(false);
                }
                this.f961Wi = null;
            }
        } catch (Exception e2) {
            Log.w("VideoModel", "showMultiScreen:" + e2.getMessage());
        }
        InterfaceC0708b interfaceC0708b = f944dj;
        if (interfaceC0708b != null) {
            interfaceC0708b.mo1073v(z);
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* JADX INFO: renamed from: P */
    public void mo1154P() {
        if (isPlaying()) {
            C0760l.f991md = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
            m1219Xe();
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* JADX INFO: renamed from: Pb */
    public void mo1155Pb() throws Throwable {
        C0758j c0758j = f946fj;
        if (c0758j != null) {
            c0758j.onDestroy();
            throw null;
        }
        try {
            if (this.f948Bi != null) {
                this.f948Bi.cancel(true);
            }
            this.f956Ti = BaseVideoMode.VIDEO_MODEL_STATE.VIDEO_MODEL_DESTROY;
            this.f950Ni = false;
            m1261F(false);
            m1260E(false);
            this.mHandler.removeMessages(65294);
            this.mHandler.removeMessages(65297);
            m1216Sa();
            m1235df();
            mMediaPlayer.setVisibility(8);
            mMediaPlayer.stopPlayback();
            f947jd.removeHandler("VideoModel");
            f947jd.close();
            f947jd = null;
        } catch (Exception e) {
            Log.d("VideoModel", "onDestory:" + e.getMessage());
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* JADX INFO: renamed from: ic */
    public void mo1156ic() {
        if (!m1218Ve() || this.f953Qh) {
            return;
        }
        this.mHandler.removeMessages(65282);
        this.mHandler.sendEmptyMessageDelayed(65282, 500L);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* JADX INFO: renamed from: jc */
    public void mo1157jc() {
        if (!m1218Ve() || this.f953Qh) {
            return;
        }
        this.mHandler.removeMessages(65283);
        this.mHandler.sendEmptyMessageDelayed(65283, 500L);
    }

    /* JADX INFO: renamed from: lc */
    public void m1262lc() {
        if (TextUtils.equals(SystemProperties.get("persist.tw.forcepip", "0"), "0")) {
            this.f955Ri = false;
        } else if (SystemProperties.get("sys.df.desktop", "").contains("video")) {
            this.f955Ri = true;
        } else {
            this.f955Ri = false;
        }
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* JADX INFO: renamed from: ma */
    public void mo1158ma() {
        if (isPlaying()) {
            return;
        }
        C0760l c0760l = f947jd;
        if (c0760l != null) {
            c0760l.m1308w(true);
        }
        mMediaPlayer.start();
        this.mHandler.removeMessages(65281);
        this.mHandler.sendEmptyMessageDelayed(65281, 1000L);
        m1219Xe();
    }

    /* JADX INFO: renamed from: mc */
    public void m1263mc() {
        int currentPosition;
        if (!mMediaPlayer.isPlaying() || (currentPosition = mMediaPlayer.getCurrentPosition() + 15000) <= 0 || currentPosition >= mMediaPlayer.getDuration()) {
            return;
        }
        mMediaPlayer.seekTo(currentPosition);
    }

    /* JADX INFO: renamed from: nc */
    public void m1264nc() {
        int currentPosition;
        if (!mMediaPlayer.isPlaying() || mMediaPlayer.getCurrentPosition() - 10000 <= 0 || currentPosition >= mMediaPlayer.getDuration()) {
            return;
        }
        mMediaPlayer.seekTo(currentPosition);
    }

    /* JADX INFO: renamed from: oc */
    public void m1265oc() {
        f944dj.mo1071u(C0760l.f981Bd, C0760l.f982Cd);
        f944dj.mo1057b(f947jd.f992Dd);
        f944dj.mo1056a(f947jd.f1002ud);
        f944dj.mo1061fa(getFileName());
        f944dj.mo1058c(isPlaying());
        f944dj.mo1066l(f947jd.f993Ld);
        f944dj.mo1051D(m1243hd());
        f944dj.mo1063h(C0750b.m1297b(this.mContext, C0760l.f981Bd, f947jd.f997Pd));
    }

    public void seekTo(int i) {
        mMediaPlayer.seekTo(i);
    }

    @Override // com.eckom.xtlibrary.twproject.video.model.BaseVideoMode
    /* JADX INFO: renamed from: w */
    public void mo1159w(boolean z) {
        f947jd.m1308w(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: c */
    public void m1230c(int i, boolean z) {
        int length;
        synchronized (f947jd) {
            int[] iArr = f947jd.f998kd;
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
