package com.eckom.xtlibrary.twproject.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManagerGlobal;
import com.eckom.xtlibrary.p066b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p066b.p052i.C0677g;
import com.eckom.xtlibrary.p066b.p052i.C0678h;
import com.eckom.xtlibrary.p066b.p052i.C0681k;
import com.eckom.xtlibrary.p066b.p052i.C0683m;
import com.eckom.xtlibrary.p066b.p052i.C0684n;
import com.eckom.xtlibrary.p066b.p052i.InterfaceC0673c;
import com.eckom.xtlibrary.p066b.p053j.C0686b;
import com.eckom.xtlibrary.p066b.p053j.C0698n;
import com.eckom.xtlibrary.p066b.p053j.C0700p;
import com.eckom.xtlibrary.p066b.p058l.InterfaceC0710a;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public abstract class XTActivity<P extends AbstractC0658a> extends BaseActivity implements InterfaceC0673c {
    static final Palette.Filter DEFAULT_FILTER = new C0714c();

    /* renamed from: db */
    protected C0677g f864db;

    /* renamed from: eb */
    protected Context f865eb;

    /* renamed from: gb */
    private String f867gb;

    /* renamed from: hb */
    private String f868hb;

    /* renamed from: ib */
    public String f869ib;

    /* renamed from: jb */
    private String f870jb;

    /* renamed from: kb */
    private String f871kb;

    /* renamed from: lb */
    private XTActivity<P>.C0711a f872lb;
    public P mPresenter;

    /* renamed from: mb */
    private boolean f873mb;

    /* renamed from: ob */
    public boolean f875ob;

    /* renamed from: pb */
    public String f876pb;

    /* renamed from: qb */
    private String f877qb;

    /* renamed from: rb */
    private String f878rb;

    /* renamed from: sb */
    private AlertDialog f879sb;

    /* renamed from: Ya */
    private ArrayList<String> f862Ya = new ArrayList<>();

    /* renamed from: Za */
    public boolean f863Za = false;

    /* renamed from: fb */
    private boolean f866fb = false;

    /* renamed from: nb */
    private boolean f874nb = true;

    /* renamed from: ub */
    boolean f880ub = false;
    private Handler mHandler = new Handler(new C0712a(this));

    /* renamed from: vb */
    public BroadcastReceiver f881vb = new C0713b(this);

    /* renamed from: wb */
    private Palette.PaletteAsyncListener f882wb = new C0715d(this);

    /* renamed from: com.eckom.xtlibrary.twproject.activity.XTActivity$a */
    class C0711a extends Thread {
        C0711a() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            XTActivity.this.f877qb = C0686b.m996Qc();
            while (true) {
                XTActivity xTActivity = XTActivity.this;
                boolean m1126na = xTActivity.m1126na(xTActivity.f870jb);
                xTActivity.f874nb = m1126na;
                if (m1126na || isInterrupted()) {
                    break;
                }
                XTActivity.this.mHandler.postDelayed(new RunnableC0716e(this), 3000L);
                try {
                    if (!isInterrupted()) {
                        Thread.sleep(3000L);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            if (!XTActivity.this.f874nb || XTActivity.this.f879sb == null) {
                return;
            }
            XTActivity.this.f879sb.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: I */
    public void m1110I(boolean z) {
        View decorView;
        Log.d("XTActivity", "setDarkStatusBar:dark:" + z);
        Window window = getWindow();
        if (window == null || (decorView = window.getDecorView()) == null) {
            return;
        }
        int systemUiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(z ? systemUiVisibility & (-8193) : systemUiVisibility | 8192);
        Log.i("XTActivity", "updateStatusBarLightDark:end");
    }

    @TargetApi(24)
    /* renamed from: oe */
    private void m1120oe() {
        try {
            if (Build.VERSION.SDK_INT >= 30) {
                this.f863Za = isInMultiWindowMode();
            } else if (Build.VERSION.SDK_INT >= 24) {
                this.f863Za = WindowManagerGlobal.getWindowManagerService().getDockedStackSide() > 0;
            }
        } catch (Exception e) {
            Log.e("XTActivity", "getMultiWindowMode:" + e.getMessage());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.eckom.xtlibrary.twproject.activity.BaseActivity
    /* renamed from: Aa */
    protected void mo1096Aa() {
        if (this.mPresenter == null) {
            this.mPresenter = (P) mo1107za();
            if (this instanceof InterfaceC0710a) {
                this.mPresenter.m807a((InterfaceC0710a) this);
            }
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseActivity
    /* renamed from: Ea */
    protected void mo1097Ea() {
        super.mo1097Ea();
    }

    /* renamed from: Ga */
    public void m1121Ga() {
        String str = (C0686b.m1006_c() || C0686b.m1000Uc()) ? "/system_tw" : "/system";
        C0677g.f792Tl = str + "/etc/theme/theme_config.json";
        C0677g c0677g = this.f864db;
        if (c0677g != null) {
            c0677g.m944Wa(str + "/etc/theme/default/Launcher/");
            this.f864db.m945Xa(str + "/etc/theme/default/Sub/");
            this.f864db.m946Ya(str + "/etc/theme/day/Launcher/");
            this.f864db.m947Za(str + "/etc/theme/day/Sub/");
            this.f864db.m950bb(str + "/etc/theme/night/Launcher/");
            this.f864db.m953eb(str + "/etc/theme/night/Sub/");
        }
    }

    /* renamed from: Ha */
    public abstract String mo1101Ha();

    /* renamed from: Ia */
    public abstract String mo1102Ia();

    /* renamed from: Ja */
    public abstract String mo1103Ja();

    /* renamed from: Ka */
    public abstract C0683m mo1104Ka();

    /* renamed from: La */
    public void m1122La() {
        if (SystemProperties.getBoolean("persist.tw.theme", false)) {
            C0681k.get().m975a(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("notify_theme_change");
            Log.d("XTActivity", "initThemeBroadCast");
            getApplicationContext().registerReceiver(this.f881vb, intentFilter);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p052i.InterfaceC0673c
    /* renamed from: M */
    public int mo925M() {
        return 0;
    }

    /* renamed from: Ma */
    public void m1123Ma() {
        if (SystemProperties.getBoolean("persist.tw.theme", false) && !TextUtils.isEmpty(mo1101Ha())) {
            this.f868hb = mo1103Ja();
            this.f867gb = mo1102Ia();
            Log.d("XTActivity", "initThemePlugin:themeApkPath:" + this.f868hb + " themeApkPackage:" + this.f867gb);
            C0683m mo1104Ka = mo1104Ka();
            if (mo1104Ka == null || this.f866fb) {
                return;
            }
            this.f864db = new C0677g();
            File file = new File(C0677g.f792Tl);
            if (!file.exists() || !file.canRead() || file.length() == 0) {
                m1121Ga();
            }
            if (mo1104Ka.f821em == 1) {
                this.f864db.m948_a(mo1101Ha());
                C0684n.m992a(this.f864db, C0677g.f792Tl);
                Log.d("XTActivity", "initThemePlugin1: " + this.f864db.m936Dc());
                if (new File(this.f864db.m936Dc()).exists()) {
                    C0678h.m961a(this.f864db.m936Dc(), mo1104Ka);
                    C0681k.get().m978e(mo1104Ka);
                    this.f866fb = true;
                    return;
                }
                return;
            }
            this.f864db.m954fb(mo1101Ha());
            C0684n.m992a(this.f864db, C0677g.f792Tl);
            Log.d("XTActivity", "initThemePlugin2: " + this.f864db.m942Ic());
            if (new File(this.f864db.m942Ic()).exists()) {
                C0678h.m961a(this.f864db.m942Ic(), mo1104Ka);
                C0681k.get().m978e(mo1104Ka);
                this.f866fb = true;
            }
        }
    }

    /* renamed from: Na */
    public void m1124Na() {
        View decorView;
        Log.i("XTActivity", "updateStatusBarLightDark:start");
        Window window = getWindow();
        if (window == null || (decorView = window.getDecorView()) == null) {
            return;
        }
        int i = C0700p.m1033b(this)[0];
        int m1034c = C0700p.m1034c(this);
        Log.d("XTActivity", "updateStatusBarLightDark:screenWidth:" + i + " statusBarHeight:" + m1034c);
        Palette.from(m1111a(decorView, new Rect(0, 0, i, m1034c), 1.0f)).clearFilters().generate(this.f882wb);
    }

    @Override // com.eckom.xtlibrary.p066b.p052i.InterfaceC0673c
    /* renamed from: V */
    public int mo926V() {
        return 0;
    }

    @Override // com.eckom.xtlibrary.p066b.p052i.InterfaceC0673c
    /* renamed from: a */
    public void mo927a(C0683m c0683m) {
    }

    @Override // com.eckom.xtlibrary.p066b.p052i.InterfaceC0673c
    /* renamed from: b */
    public boolean mo929b(C0683m c0683m) {
        return false;
    }

    /* renamed from: c */
    public abstract void mo1105c(C0683m c0683m);

    /* renamed from: d */
    public abstract void mo1106d(C0683m c0683m);

    @Override // com.eckom.xtlibrary.p066b.p052i.InterfaceC0673c
    /* renamed from: ia */
    public int mo930ia() {
        return 0;
    }

    /* renamed from: ma */
    public void m1125ma(String str) {
        this.f870jb = str;
        String str2 = Build.MODEL;
        this.f873mb = true;
        if (TextUtils.equals(getResources().getConfiguration().locale.getLanguage(), "zh")) {
            this.f871kb = "UI 未激活，请激活！！！";
            this.f878rb = "软件激活";
        } else {
            this.f871kb = "UI is not activated, please activate it !!!";
            this.f878rb = "Software activation";
        }
        this.f879sb = new AlertDialog.Builder(this).setTitle(this.f878rb).setMessage(this.f871kb).create();
        this.f879sb.getWindow().setGravity(80);
        if (this.f872lb != null || C0686b.m999Tc().contains("MOHAWK") || !SystemProperties.getBoolean("ro.tw.uiCheck", true) || TextUtils.equals(str, str2)) {
            return;
        }
        this.f872lb = new C0711a();
        this.f872lb.start();
    }

    /* renamed from: na */
    public boolean m1126na(String str) {
        BufferedReader bufferedReader;
        File file;
        BufferedReader bufferedReader2 = null;
        try {
            try {
                file = new File("/twdataconfig").exists() ? new File("/twdataconfig/UIAuthorization") : new File("/sdcard/.Tcfg/UIAuthorization");
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
            bufferedReader = bufferedReader2;
        }
        try {
            this.f862Ya.clear();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                this.f862Ya.add(readLine);
            }
            Log.e("HYH", "UIAuthorizationCheck->getAuthorizationStation:" + file.canRead() + "," + this.f862Ya.size());
            Iterator<String> it = this.f862Ya.iterator();
            while (it.hasNext()) {
                if (C0698n.m1023h(C0698n.decrypt(it.next()), str, this.f877qb)) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return true;
                }
            }
            try {
                bufferedReader.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            return false;
        } catch (Exception e4) {
            e = e4;
            bufferedReader2 = bufferedReader;
            Log.e("HYH", "UIAuthorizationCheck->getAuthorizationStation:" + e.toString());
            e.printStackTrace();
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
            throw th;
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        m1120oe();
        this.f869ib = SystemProperties.get("persist.sys.tw.current.theme.type", "day");
        this.f875ob = SystemProperties.getBoolean("persist.sys.tw.theme_change_wallpaperOrPackage", false);
        this.f876pb = SystemProperties.get("persist.sys.tw.theme_change_colorOrPath");
        super.onCreate(bundle);
        m1122La();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        C0681k.get().m977b(this);
        try {
            if (this.f881vb != null) {
                getApplicationContext().unregisterReceiver(this.f881vb);
            }
        } catch (Exception e) {
            Log.e("XTActivity", "onDestroy:" + e.getMessage());
        }
        P p = this.mPresenter;
        if (p != null) {
            p.delete();
            this.mPresenter = null;
        }
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        XTActivity<P>.C0711a c0711a = this.f872lb;
        if (c0711a != null) {
            c0711a.interrupt();
            this.f872lb = null;
        }
        AlertDialog alertDialog = this.f879sb;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.f879sb = null;
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        Log.d("HYH", "onResume: isCheckingUI=" + this.f873mb + "," + this.f874nb);
        if (!this.f873mb || this.f874nb) {
            return;
        }
        m1125ma(this.f870jb);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onStart() {
        super.onStart();
        m1123Ma();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        this.f880ub = z;
        if (C0686b.m1000Uc()) {
            this.f880ub = false;
        }
        if (z) {
            this.mHandler.removeMessages(65281);
            this.mHandler.sendEmptyMessageDelayed(65281, 100L);
        }
    }

    /* renamed from: za */
    public abstract P mo1107za();

    @Override // com.eckom.xtlibrary.p066b.p052i.InterfaceC0673c
    /* renamed from: a */
    public void mo928a(C0683m c0683m, boolean z) {
        this.f865eb = c0683m.m987Nc().m986Mc();
    }

    /* renamed from: a */
    private Bitmap m1111a(View view, Rect rect, float f) {
        int round = Math.round(rect.width() * f);
        int round2 = Math.round(rect.height() * f);
        if (view.getWidth() > 0 && view.getHeight() > 0 && round > 0 && round2 > 0) {
            Bitmap createBitmap = Bitmap.createBitmap(round, round2, Bitmap.Config.ARGB_4444);
            Canvas canvas = new Canvas(createBitmap);
            Matrix matrix = new Matrix();
            matrix.preScale(f, f);
            matrix.postTranslate((-rect.left) * f, (-rect.top) * f);
            canvas.setMatrix(matrix);
            view.draw(canvas);
            return createBitmap;
        }
        throw new IllegalArgumentException("No screen available (width or height = 0)");
    }
}
