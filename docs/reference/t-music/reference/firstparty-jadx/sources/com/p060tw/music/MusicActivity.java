package com.p060tw.music;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p042e.C0635a;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0637b;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0654s;
import com.eckom.xtlibrary.p020b.p052i.C0678h;
import com.eckom.xtlibrary.p020b.p052i.C0681k;
import com.eckom.xtlibrary.p020b.p052i.C0683m;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.twproject.activity.BaseMusicActivity;
import com.p060tw.music.lrc.LrcView;
import com.p060tw.music.p061a.C0767c;
import com.p060tw.music.p062b.C0769a;
import com.p060tw.music.p063c.C0771a;
import com.p060tw.music.p063c.C0772b;
import com.p060tw.music.p063c.C0773c;
import com.p060tw.music.utils.C0792a;
import com.p060tw.music.utils.C0793b;
import com.p060tw.music.utils.C0794c;
import com.p060tw.music.utils.ImagViewAndTextView;
import com.p060tw.music.view.BaseVisualizerView;
import com.p060tw.music.view.CircleImageView;
import com.p060tw.preference.TogglePreference;
import java.io.File;
import java.util.Locale;

/* loaded from: classes3.dex */
public class MusicActivity extends BaseMusicActivity implements View.OnLongClickListener {

    /* renamed from: Dc */
    public static int f1016Dc;

    /* renamed from: Ab */
    private View f1017Ab;

    /* renamed from: Bb */
    private CircleImageView f1019Bb;

    /* renamed from: Bc */
    C0772b f1020Bc;

    /* renamed from: Cb */
    private SeekBar f1021Cb;

    /* renamed from: Db */
    private TextView f1023Db;

    /* renamed from: Eb */
    private TextView f1024Eb;

    /* renamed from: Fb */
    private CircleImageView f1025Fb;

    /* renamed from: Gb */
    private ImageView f1026Gb;

    /* renamed from: Hb */
    private TextView f1027Hb;

    /* renamed from: Ib */
    private TextView f1028Ib;

    /* renamed from: Jb */
    private TextView f1029Jb;

    /* renamed from: Kb */
    private TextView f1030Kb;

    /* renamed from: Lb */
    private ImagViewAndTextView f1031Lb;

    /* renamed from: Mb */
    private TextView f1032Mb;

    /* renamed from: Nb */
    private ImagViewAndTextView f1033Nb;

    /* renamed from: Ob */
    private TextView f1034Ob;

    /* renamed from: Pb */
    private ImageView f1035Pb;

    /* renamed from: Qb */
    private ImageView f1036Qb;

    /* renamed from: Rb */
    private ImageView f1037Rb;

    /* renamed from: Sb */
    private ImageView f1038Sb;

    /* renamed from: Tb */
    private ImageView f1039Tb;

    /* renamed from: Ub */
    private ImageView f1040Ub;

    /* renamed from: Vb */
    private ImageView f1041Vb;

    /* renamed from: Wb */
    private ImageView f1043Wb;

    /* renamed from: Xb */
    private ImageView f1044Xb;

    /* renamed from: Yb */
    private ImageView f1045Yb;

    /* renamed from: Zb */
    private RelativeLayout f1046Zb;

    /* renamed from: cc */
    private ImageView f1048cc;
    private boolean fromUser;
    private View layout_player;
    private View layout_settings;
    private LinearLayout layout_settings_bg;
    private LinearLayout ll_fx;
    public LrcView lrc_view;
    private ListView mListView;
    private C0580g mRecord;
    private TogglePreference pref_lrc;

    /* renamed from: sc */
    boolean f1062sc;

    /* renamed from: uc */
    private C0773c f1063uc;

    /* renamed from: xb */
    private C0767c f1066xb;

    /* renamed from: yb */
    private C0793b f1068yb;

    /* renamed from: zb */
    private View f1070zb;
    private MusicService mService = null;

    /* renamed from: ec */
    private BaseVisualizerView f1049ec = null;

    /* renamed from: fc */
    private boolean f1050fc = true;

    /* renamed from: gc */
    int[] f1051gc = {R.id.btn_playlist, R.id.btn_sd, R.id.btn_usb, R.id.btn_inand, R.id.collect};

    /* renamed from: hc */
    int f1052hc = 0;

    /* renamed from: ic */
    int f1053ic = 0;

    /* renamed from: jc */
    private final int f1054jc = 0;

    /* renamed from: kc */
    private final int f1055kc = 1;

    /* renamed from: lc */
    private final int f1056lc = 2;

    /* renamed from: mc */
    private final int f1057mc = 0;

    /* renamed from: nc */
    private final int f1058nc = 1;

    /* renamed from: oc */
    private final int f1059oc = 2;

    /* renamed from: qc */
    private final String f1060qc = "music_view";

    /* renamed from: Wa */
    private int f1042Wa = 0;

    /* renamed from: rc */
    String[] f1061rc = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};

    /* renamed from: vc */
    private ServiceConnection f1064vc = new ServiceConnectionC0774d(this);

    /* renamed from: wc */
    private C0767c.b f1065wc = new C0775e(this);

    /* renamed from: xc */
    private C0767c.a f1067xc = new C0776f(this);

    /* renamed from: yc */
    private SeekBar.OnSeekBarChangeListener f1069yc = new C0777g(this);

    /* renamed from: c */
    int f1047c = 0;

    /* renamed from: zc */
    int[] f1071zc = {R.drawable.f1075bg, R.drawable.bg1, R.drawable.bg2, R.drawable.bg4, R.drawable.bg5, R.drawable.bg6};

    /* renamed from: Ac */
    private int f1018Ac = 0;

    /* renamed from: Cc */
    private TogglePreference.InterfaceC0798a f1022Cc = new C0779i(this);
    private BroadcastReceiver mReceiver = new C0780j(this);

    /* renamed from: Ba */
    private int m1329Ba(int i) {
        return (((i / 1000) / 60) / 60) % 24;
    }

    /* renamed from: Ca */
    private int m1330Ca(int i) {
        return ((i / 1000) / 60) % 60;
    }

    /* renamed from: Da */
    private int m1331Da(int i) {
        return (i / 1000) % 60;
    }

    /* renamed from: Ea */
    private void m1332Ea(int i) {
        if (i == 0) {
            this.f1070zb.setVisibility(0);
            this.f1017Ab.setVisibility(8);
        } else {
            if (i != 2) {
                return;
            }
            this.f1070zb.setVisibility(8);
            this.f1017Ab.setVisibility(0);
        }
    }

    /* renamed from: Fa */
    private void m1333Fa(int i) {
        if (i == 0) {
            ((C0635a) this.mPresenter).m736rb();
            return;
        }
        if (i != 1) {
            if (i != 2) {
                return;
            }
            ((C0635a) this.mPresenter).m734pb();
        } else if (this.mService.f1072Pa.isPlaying()) {
            ((C0635a) this.mPresenter).m726ba();
        } else {
            ((C0635a) this.mPresenter).m730fa();
        }
    }

    /* renamed from: Ga */
    private void m1334Ga(int i) {
        try {
            if (this.f1049ec != null) {
                this.f1049ec.setVisualizer(null);
                this.ll_fx.removeAllViews();
            }
            this.f1049ec = new BaseVisualizerView(this);
            this.f1049ec.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            if (this.f1020Bc != null) {
                this.f1049ec.m1520j(this.f1020Bc.m1432Rd(), this.f1020Bc.m1431Qd());
            }
            this.ll_fx.setOrientation(1);
            this.ll_fx.addView(this.f1049ec);
            Visualizer visualizer = new Visualizer(i);
            visualizer.setEnabled(false);
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            this.f1049ec.setVisualizer(visualizer);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MusicActivity", "setupVisualizerFxAndUi->Exception:" + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: J */
    public void m1335J(boolean z) {
        this.fromUser = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: K */
    public void m1336K(boolean z) {
        ((ImageView) findViewById(R.id.iv_fx)).getDrawable().setLevel(z ? 1 : 0);
        if (this.f863Za) {
            findViewById(R.id.ll_fx).setVisibility(8);
            findViewById(R.id.lrc_view).setVisibility(8);
        } else {
            findViewById(R.id.ll_fx).setVisibility(z ? 8 : 0);
            findViewById(R.id.lrc_view).setVisibility(z ? 0 : 8);
        }
    }

    /* renamed from: c */
    private void m1343c(C0773c c0773c) {
        C0771a c0771a = c0773c.f1144fm;
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.f1079tb);
        linearLayout.setBackground(c0771a.m1389nd());
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View childAt = linearLayout.getChildAt(i);
            if (childAt instanceof ImageView) {
                ImageView imageView = (ImageView) childAt;
                imageView.setImageDrawable(c0771a.m1390od().get(i).m1403md());
                imageView.setBackground(c0771a.m1390od().get(i).m1402ld());
            }
        }
        findViewById(R.id.bot).setBackground(c0771a.m1397ud());
        this.f1026Gb.setImageDrawable(c0771a.getAlbum());
        this.f1025Fb.setImageDrawable(c0771a.getAlbum());
        ((ImageView) findViewById(R.id.prev_list)).setImageDrawable(c0771a.m1394rd());
        ((ImageView) findViewById(R.id.pp_list)).setImageDrawable(c0771a.m1393qd());
        ((ImageView) findViewById(R.id.next_list)).setImageDrawable(c0771a.getNext());
        this.lrc_view.setNormalColor(c0771a.m1395sd());
        this.lrc_view.setCurrentColor(c0771a.m1396td());
        this.f1027Hb.setTextColor(c0771a.m1395sd());
        this.f1031Lb.setTxColor(c0771a.m1395sd());
        this.f1033Nb.setTxColor(c0771a.m1395sd());
        this.f1023Db.setTextColor(c0771a.m1395sd());
        this.f1024Eb.setTextColor(c0771a.m1395sd());
        ((TextView) findViewById(R.id.tv_music_title)).setTextColor(c0771a.m1395sd());
        ((TextView) findViewById(R.id.tv_music_artis)).setTextColor(c0771a.m1395sd());
    }

    /* renamed from: l */
    private void m1344l(int i, int i2) {
        int i3 = i != 1 ? i2 == 2 ? 1 : 0 : 2;
        ((C0635a) this.mPresenter).m733pa(i3);
        this.mService.f1072Pa.m1381va(i3);
    }

    /* renamed from: pe */
    private void m1345pe() {
        WindowManager windowManager = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        this.f1042Wa = displayMetrics.widthPixels;
    }

    /* renamed from: qe */
    private void m1346qe() {
        this.f1029Jb.setText(this.mService.f1072Pa.m1375jd());
        this.f1028Ib.setText(this.mService.f1072Pa.m1369Nb());
        this.f1027Hb.setText(this.mService.f1072Pa.m1369Nb());
        this.f1031Lb.setTx(this.mService.f1072Pa.m1375jd());
        this.f1033Nb.setTx(this.mService.f1072Pa.m1372fd());
        m1352wb(this.mService.f1072Pa.m1373gd());
        m1339a(this.f1025Fb);
        m1339a(this.f1019Bb);
        this.f1030Kb.setText(this.mService.f1072Pa.m1369Nb());
        this.f1032Mb.setText(this.mService.f1072Pa.m1375jd());
        this.f1034Ob.setText(this.mService.f1072Pa.m1372fd());
    }

    /* renamed from: re */
    private void m1347re() {
        this.f1068yb = C0793b.getInstance();
        this.f1068yb.init(this);
        this.f1021Cb.setOnSeekBarChangeListener(this.f1069yc);
        this.f1037Rb.setOnLongClickListener(this);
        this.f1038Sb.setOnLongClickListener(this);
    }

    /* renamed from: se */
    private void m1348se() {
        this.layout_settings = LayoutInflater.from(this).inflate(R.layout.layout_settings, (ViewGroup) null, false);
        this.layout_settings.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        this.layout_settings_bg = (LinearLayout) this.layout_settings.findViewById(R.id.layout_settings_bg);
        this.pref_lrc = (TogglePreference) this.layout_settings.findViewById(R.id.pref_lrc);
        this.pref_lrc.setOnToggleStateChange(this.f1022Cc);
        this.pref_lrc.setToggleState(C0794c.m1516b(getApplicationContext(), "MusicActivity", "lrcorVisible"));
        ((RelativeLayout) findViewById(R.id.ll_music_play)).addView(this.layout_settings);
        C0773c c0773c = this.f1063uc;
        if (c0773c != null) {
            m1342b(c0773c);
        }
    }

    /* renamed from: te */
    private boolean m1349te() {
        return this.fromUser;
    }

    /* renamed from: ue */
    private void m1350ue() {
        int i = this.f1047c;
        if (i == 0) {
            findViewById(R.id.drag_layer).setBackgroundResource(R.drawable.f1075bg);
            return;
        }
        if (i == 1) {
            findViewById(R.id.drag_layer).setBackgroundResource(R.drawable.bg1);
            return;
        }
        if (i == 2) {
            findViewById(R.id.drag_layer).setBackgroundResource(R.drawable.bg2);
            return;
        }
        if (i == 3) {
            findViewById(R.id.drag_layer).setBackgroundResource(R.drawable.bg4);
        } else if (i == 4) {
            findViewById(R.id.drag_layer).setBackgroundResource(R.drawable.bg5);
        } else {
            if (i != 5) {
                return;
            }
            findViewById(R.id.drag_layer).setBackgroundResource(R.drawable.bg6);
        }
    }

    /* renamed from: ve */
    private void m1351ve() {
        int i = C0654s.f701Ad;
        if (i >= 0) {
            this.mListView.setSelection(i);
        }
    }

    /* renamed from: wb */
    private void m1352wb(String str) {
        if (str != null) {
            String str2 = str.substring(0, str.lastIndexOf(".")) + ".lrc";
            if (new File(str2).exists()) {
                this.lrc_view.m1502va(C0637b.m745Oa(str2));
                this.lrc_view.setOnPlayClickListener(new C0778h(this));
            } else {
                this.lrc_view.reset();
                Log.i("MusicActivity", "LRC NOT EXISTS");
            }
        }
    }

    /* renamed from: we */
    private void m1353we() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tw.eq", "com.tw.eq.EQActivity"));
            startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(this, R.string.activity_not_found, 0).show();
        }
    }

    /* renamed from: xe */
    private void m1354xe() {
        getWindow().setFormat(-3);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
            return;
        }
        Window window2 = getWindow();
        WindowManager.LayoutParams attributes = window2.getAttributes();
        attributes.flags = 67108864 | attributes.flags;
        attributes.flags |= 134217728;
        window2.setAttributes(attributes);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity
    /* renamed from: Aa */
    public void mo1096Aa() {
        super.mo1096Aa();
        this.f1070zb = findViewById(R.id.layout_music_play);
        this.f1017Ab = findViewById(R.id.layout_split_screen);
        if (C0686b.m999Tc().contains("-IPS")) {
            this.f1070zb.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
        this.f1025Fb = (CircleImageView) findViewById(R.id.albumart);
        this.f1026Gb = (ImageView) findViewById(R.id.iv_main_album_list);
        this.f1048cc = (ImageView) findViewById(R.id.iv_collect);
        this.f1028Ib = (TextView) findViewById(R.id.tv_music_title);
        this.f1028Ib.setSelected(true);
        this.f1029Jb = (TextView) findViewById(R.id.tv_music_artis);
        this.f1029Jb.setSelected(true);
        this.f1027Hb = (TextView) findViewById(R.id.tv_main_song);
        this.f1027Hb.setSelected(true);
        this.f1031Lb = (ImagViewAndTextView) findViewById(R.id.tv_main_singer);
        this.f1031Lb.setSelected(true);
        this.f1033Nb = (ImagViewAndTextView) findViewById(R.id.tv_main_albumName);
        this.f1033Nb.setSelected(true);
        this.f1035Pb = (ImageView) findViewById(R.id.pp_list);
        this.f1036Qb = (ImageView) findViewById(R.id.iv_main_play);
        this.f1037Rb = (ImageView) findViewById(R.id.iv_main_prev);
        this.f1038Sb = (ImageView) findViewById(R.id.iv_main_next);
        this.f1039Tb = (ImageView) findViewById(R.id.iv_setting);
        this.f1023Db = (TextView) findViewById(R.id.tv_current_time);
        this.f1024Eb = (TextView) findViewById(R.id.tv_duration);
        this.f1021Cb = (SeekBar) findViewById(R.id.seek_bar_progress);
        this.f1044Xb = (ImageView) findViewById(R.id.shuffle);
        this.f1045Yb = (ImageView) findViewById(R.id.repeat);
        this.f1046Zb = (RelativeLayout) findViewById(R.id.frame_album);
        this.lrc_view = (LrcView) findViewById(R.id.lrc_view);
        this.ll_fx = (LinearLayout) findViewById(R.id.ll_fx);
        f1016Dc = this.ll_fx.getHeight();
        this.mListView = (ListView) findViewById(R.id.list);
        this.f1066xb = new C0767c(this);
        this.mListView.setAdapter((ListAdapter) this.f1066xb);
        this.f1066xb.m1365a(this.f1065wc);
        this.f1066xb.m1364a(this.f1067xc);
        this.f1040Ub = (ImageView) findViewById(R.id.iv_main_play_split_screen);
        this.f1041Vb = (ImageView) findViewById(R.id.iv_main_prev_split_screen);
        this.f1043Wb = (ImageView) findViewById(R.id.iv_main_next_split_screen);
        this.f1030Kb = (TextView) findViewById(R.id.tv_split_song);
        this.f1032Mb = (TextView) findViewById(R.id.tv_split_singer);
        this.f1034Ob = (TextView) findViewById(R.id.tv_split_albumName);
        this.f1019Bb = (CircleImageView) findViewById(R.id.iv_split_album);
        this.f1062sc = getResources().getBoolean(R.bool.show_zoom_mode_split);
        this.layout_player = findViewById(R.id.layout_player);
        if (C0686b.m999Tc().contains("-IPS")) {
            findViewById(R.id.drag_layer).setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: B */
    public void mo795B(int i) {
        Log.d("MusicActivity", "onAudioSessionId: audioSessionId:" + this.f1018Ac + " sessionId:" + i);
        if (this.f1018Ac != i) {
            this.f1018Ac = i;
            m1334Ga(this.f1018Ac);
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: D */
    public void mo796D(int i) {
        this.mService.f1072Pa.m1381va(i);
        if (i == 0) {
            this.f1053ic = 1;
        } else if (i == 1) {
            this.f1053ic = 2;
        } else if (i == 2) {
            this.f1053ic = 3;
        }
        this.f1044Xb.getDrawable().setLevel(this.f1052hc);
        this.f1045Yb.getDrawable().setLevel(this.f1053ic);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: Ha */
    public String mo1101Ha() {
        return "MusicTheme.apk";
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: Ka */
    public C0683m mo1104Ka() {
        return new C0773c();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: d */
    public void mo803d(int i, int i2) {
        this.lrc_view.m1501fa(i);
        this.mService.f1072Pa.m1380ua(i);
        this.mService.f1072Pa.setDuration(i2);
        if (!m1349te()) {
            this.f1021Cb.setProgress(i);
        }
        m1335J(false);
        this.f1021Cb.setMax(i2);
        int m1329Ba = m1329Ba(i2);
        int m1330Ca = m1330Ca(i2);
        int m1331Da = m1331Da(i2);
        if (m1329Ba == 0) {
            this.f1024Eb.setText(String.format(Locale.US, "%d:%02d", Integer.valueOf(m1330Ca), Integer.valueOf(m1331Da)));
        } else {
            this.f1024Eb.setText(String.format(Locale.US, "%d:%02d:%02d", Integer.valueOf(m1329Ba), Integer.valueOf(m1330Ca), Integer.valueOf(m1331Da)));
        }
        int m1329Ba2 = m1329Ba(i);
        int m1330Ca2 = m1330Ca(i);
        int m1331Da2 = m1331Da(i);
        if (m1329Ba2 == 0) {
            this.f1023Db.setText(String.format(Locale.US, "%d:%02d", Integer.valueOf(m1330Ca2), Integer.valueOf(m1331Da2)));
        } else {
            this.f1023Db.setText(String.format(Locale.US, "%d:%02d:%02d", Integer.valueOf(m1329Ba2), Integer.valueOf(m1330Ca2), Integer.valueOf(m1331Da2)));
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            if (keyEvent.getKeyCode() == 66 && this.mListView.getSelectedView() != null && this.mListView.isFocused()) {
                ((C0635a) this.mPresenter).m732la(this.mListView.getSelectedItemPosition());
                return true;
            }
        } else if (keyEvent.getAction() == 1) {
            keyEvent.getKeyCode();
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseActivity
    protected int getContentView() {
        return R.layout.music_activity;
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: h */
    public void mo805h(boolean z) {
        this.f1048cc.setImageLevel(z ? 1 : 0);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        if (findViewById(R.id.ll_music_play).getVisibility() != 0) {
            findViewById(R.id.ll_music_list).setVisibility(8);
            findViewById(R.id.ll_music_play).setVisibility(0);
            return;
        }
        View view = this.layout_settings;
        if (view != null && view.getVisibility() == 0) {
            this.layout_settings.setVisibility(8);
            this.layout_player.setVisibility(0);
        } else {
            super.onBackPressed();
            if (Build.VERSION.SDK_INT >= 30) {
                finish();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00fb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back /* 2131230753 */:
                finish();
                break;
            case R.id.bot /* 2131230756 */:
                findViewById(R.id.ll_music_list).setVisibility(8);
                findViewById(R.id.ll_music_play).setVisibility(0);
                break;
            case R.id.btn_bg /* 2131230758 */:
                int i = this.f1047c;
                if (i == 3) {
                    this.f1047c = 0;
                } else {
                    this.f1047c = i + 1;
                }
                if (!C0686b.m999Tc().contains("-IPS")) {
                    findViewById(R.id.drag_layer).setBackgroundResource(this.f1071zc[this.f1047c]);
                    m1350ue();
                }
                C0794c.m1514a(this, "id", "id", this.f1047c);
                break;
            case R.id.collect /* 2131230774 */:
                ((C0635a) this.mPresenter).m708Ab();
                break;
            case R.id.home /* 2131230799 */:
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.setFlags(268435456);
                intent.addCategory("android.intent.category.HOME");
                startActivity(intent);
                break;
            case R.id.next_list /* 2131230864 */:
                m1333Fa(2);
                break;
            case R.id.play_list /* 2131230871 */:
                findViewById(R.id.ll_music_list).setVisibility(0);
                findViewById(R.id.ll_music_play).setVisibility(8);
                this.f1066xb.m1362Xa();
                break;
            case R.id.pp_list /* 2131230873 */:
                m1333Fa(1);
                break;
            case R.id.prev_list /* 2131230875 */:
                m1333Fa(0);
                break;
            case R.id.repeat /* 2131230880 */:
                int m1374hd = this.mService.f1072Pa.m1374hd() + 1;
                if (m1374hd > 2) {
                    m1374hd = 0;
                }
                ((C0635a) this.mPresenter).m733pa(m1374hd);
                this.mService.f1072Pa.m1381va(m1374hd);
                break;
            case R.id.shuffle /* 2131230906 */:
                int i2 = this.f1052hc + 1;
                this.f1052hc = i2;
                if (i2 > 1) {
                    this.f1052hc = 0;
                }
                m1344l(this.f1052hc, this.f1053ic);
                break;
            default:
                switch (id) {
                    case R.id.btn_inand /* 2131230760 */:
                        ((C0635a) this.mPresenter).m715Eb();
                        break;
                    case R.id.btn_playlist /* 2131230761 */:
                        ((C0635a) this.mPresenter).m710Bb();
                        break;
                    case R.id.btn_sd /* 2131230762 */:
                        ((C0635a) this.mPresenter).m711Cb();
                        break;
                    case R.id.btn_usb /* 2131230763 */:
                        ((C0635a) this.mPresenter).m713Db();
                        break;
                    default:
                        switch (id) {
                            case R.id.iv_collect /* 2131230820 */:
                                ((C0635a) this.mPresenter).m716Gb();
                                break;
                            case R.id.iv_eq /* 2131230821 */:
                                m1353we();
                                break;
                            case R.id.iv_fx /* 2131230822 */:
                                if (findViewById(R.id.ll_fx).getVisibility() != 8) {
                                    ((ImageView) findViewById(R.id.iv_fx)).getDrawable().setLevel(1);
                                    findViewById(R.id.ll_fx).setVisibility(8);
                                    findViewById(R.id.lrc_view).setVisibility(0);
                                    break;
                                } else {
                                    ((ImageView) findViewById(R.id.iv_fx)).getDrawable().setLevel(0);
                                    findViewById(R.id.ll_fx).setVisibility(0);
                                    findViewById(R.id.lrc_view).setVisibility(8);
                                    break;
                                }
                            default:
                                switch (id) {
                                    case R.id.iv_setting /* 2131230832 */:
                                        if (this.layout_settings == null) {
                                            m1348se();
                                        }
                                        this.layout_player.setVisibility(8);
                                        this.layout_settings.setVisibility(0);
                                        break;
                                }
                        }
                }
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        m1354xe();
        startService(new Intent(this, (Class<?>) MusicService.class));
        m1347re();
        m1345pe();
        bindService(new Intent(this, (Class<?>) MusicService.class), this.f1064vc, 1);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.launcher.widget_music_progress");
        registerReceiver(this.mReceiver, intentFilter);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.twproject.activity.BaseActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        try {
            unbindService(this.f1064vc);
        } catch (Exception e) {
            Log.e("MusicActivity", e.toString());
        }
        BaseVisualizerView baseVisualizerView = this.f1049ec;
        if (baseVisualizerView != null) {
            baseVisualizerView.setVisualizer(null);
        }
        super.onDestroy();
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_main_next) {
            ((C0635a) this.mPresenter).m717Hb();
            return true;
        }
        if (id != R.id.iv_main_prev) {
            return true;
        }
        ((C0635a) this.mPresenter).m718Ib();
        return true;
    }

    @Override // android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        if (!this.f863Za || this.f1062sc) {
            m1332Ea(0);
        } else {
            m1332Ea(2);
        }
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            ((C0635a) this.mPresenter).m714Ea(data.toString());
            setIntent(null);
        }
        super.onNewIntent(intent);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        CircleImageView circleImageView = this.f1025Fb;
        if (circleImageView != null) {
            circleImageView.m1524Ua();
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.twproject.activity.XTActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        C0769a c0769a;
        CircleImageView circleImageView;
        m1336K(C0794c.m1516b(getApplicationContext(), "MusicActivity", "lrcorVisible"));
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                ((C0635a) this.mPresenter).m714Ea(data.toString());
            }
            setIntent(null);
        }
        MusicService musicService = this.mService;
        if (musicService != null && (c0769a = musicService.f1072Pa) != null && c0769a.isPlaying() && (circleImageView = this.f1025Fb) != null) {
            circleImageView.m1525Va();
        }
        super.onResume();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.twproject.activity.XTActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: q */
    public void mo806q(boolean z) {
        super.mo806q(z);
        if (z) {
            findViewById(R.id.drag_layer).setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            return;
        }
        if (C0686b.m999Tc().contains("-IPS")) {
            findViewById(R.id.drag_layer).setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            return;
        }
        m1350ue();
        C0772b c0772b = this.f1020Bc;
        if (c0772b == null || c0772b.m1460yd() == null) {
            return;
        }
        findViewById(R.id.drag_layer).setBackground(this.f1020Bc.m1460yd());
    }

    @RequiresApi(api = 21)
    /* renamed from: b */
    private void m1342b(C0773c c0773c) {
        TogglePreference togglePreference;
        this.f1020Bc = c0773c.f1143Bc;
        C0772b c0772b = this.f1020Bc;
        if (c0772b != null) {
            if (c0772b.m1460yd() != null && !C0686b.m999Tc().contains("-IPS")) {
                findViewById(R.id.drag_layer).setBackground(this.f1020Bc.m1460yd());
            }
            if (this.f1020Bc.m1421Hd() != null) {
                this.layout_player.setBackground(this.f1020Bc.m1421Hd());
            }
            if (this.f1020Bc.m1437Wd() != null && this.layout_settings != null) {
                this.layout_settings_bg.setBackground(this.f1020Bc.m1437Wd());
            }
            if (this.f1020Bc.m1439Yd() != null && (togglePreference = this.pref_lrc) != null) {
                togglePreference.setRightIcon(this.f1020Bc.m1439Yd());
            }
            this.f1020Bc.m1453vd();
            if (this.f1020Bc.m1453vd() != null) {
                this.f1025Fb.setImageDrawable(this.f1020Bc.m1453vd());
            }
            StringBuilder sb = new StringBuilder();
            sb.append("(mainPlugin.getAlbum_bg() != null)  ");
            sb.append(this.f1020Bc.m1453vd() != null);
            Log.i("MainPlugin", sb.toString());
            if (this.f1020Bc.m1438Xd() != null) {
                findViewById(R.id.tab_btn_layout).setBackground(this.f1020Bc.m1438Xd());
            }
            if (this.f1020Bc.m1457xd() != null) {
                this.f1048cc.setImageDrawable(this.f1020Bc.m1457xd());
            }
            if (this.f1020Bc.m1433Sd() != null) {
                this.f1045Yb.setImageDrawable(this.f1020Bc.m1433Sd());
            }
            this.f1020Bc.m1455wd();
            this.f1020Bc.m1455wd();
            this.f1020Bc.m1435Ud();
            if (this.f1020Bc.m1436Vd() != null) {
                this.f1021Cb.setThumb(this.f1020Bc.m1436Vd());
            }
            if (this.f1020Bc.m1434Td() != null) {
                Rect bounds = this.f1021Cb.getProgressDrawable().getBounds();
                this.f1021Cb.setProgressDrawableTiled(this.f1020Bc.m1434Td());
                this.f1021Cb.getProgressDrawable().setBounds(bounds);
            }
            if (this.f1020Bc.m1427Md() != null) {
                this.f1037Rb.setImageDrawable(this.f1020Bc.m1427Md());
                this.f1037Rb.setBackground(this.f1020Bc.m1428Nd());
            }
            if (this.f1020Bc.m1425Kd() != null) {
                this.f1036Qb.setImageDrawable(this.f1020Bc.m1425Kd());
                this.f1036Qb.setBackground(this.f1020Bc.m1426Ld());
            }
            if (this.f1020Bc.m1413Dd() != null) {
                this.f1038Sb.setImageDrawable(this.f1020Bc.m1413Dd());
                this.f1038Sb.setBackground(this.f1020Bc.m1415Ed());
                findViewById(R.id.iv_fx).setBackground(C0678h.m964d(C0681k.get().m973Kc(), "music_playlist_bg"));
            }
            if (this.f1020Bc.m1429Od() != null) {
                this.f1039Tb.setBackground(this.f1020Bc.m1430Pd());
                this.f1039Tb.setImageDrawable(this.f1020Bc.m1429Od());
            }
            if (this.f1020Bc.m1463zd() != null) {
                ((ImageView) findViewById(R.id.btn_bg)).setImageDrawable(this.f1020Bc.m1463zd());
            }
            if (this.f1020Bc.m1407Ad() != null) {
                ((ImageView) findViewById(R.id.btn_bg)).setBackground(this.f1020Bc.m1407Ad());
            }
            if (this.f1020Bc.m1409Bd() != null) {
                ((ImageView) findViewById(R.id.iv_eq)).setImageDrawable(this.f1020Bc.m1409Bd());
            }
            if (this.f1020Bc.m1411Cd() != null) {
                ((ImageView) findViewById(R.id.iv_eq)).setBackground(this.f1020Bc.m1411Cd());
            }
            if (this.f1020Bc.m1423Id() != null) {
                ((ImageView) findViewById(R.id.play_list)).setImageDrawable(this.f1020Bc.m1423Id());
            }
            if (this.f1020Bc.m1424Jd() != null) {
                ((ImageView) findViewById(R.id.play_list)).setBackground(this.f1020Bc.m1424Jd());
            }
            if (this.f1020Bc.m1417Fd() != null) {
                ((ImageView) findViewById(R.id.iv_fx)).setImageDrawable(this.f1020Bc.m1417Fd());
            }
            if (this.f1020Bc.m1419Gd() != null) {
                ((ImageView) findViewById(R.id.iv_fx)).setBackground(this.f1020Bc.m1419Gd());
            }
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity
    /* renamed from: za */
    public C0635a mo1107za() {
        return new C0635a(this);
    }

    /* renamed from: a */
    private void m1339a(ImageView imageView) {
        if (this.mService.f1072Pa.m1371ed() != null) {
            imageView.setImageBitmap(this.mService.f1072Pa.m1371ed());
        } else {
            imageView.setImageResource(R.drawable.album);
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo801a(String str, String str2, String str3, Bitmap bitmap, String str4, String str5, int i) {
        if (str != " ") {
            this.mService.f1072Pa.m1378mb(str);
        } else {
            this.mService.f1072Pa.m1378mb(getString(R.string.unknown));
        }
        if (str2 != " ") {
            this.mService.f1072Pa.m1376kb(str2);
        } else {
            this.mService.f1072Pa.m1376kb(getString(R.string.unknown));
        }
        if (str3 != " ") {
            this.mService.f1072Pa.m1379nb(str3);
        } else {
            this.mService.f1072Pa.m1379nb(getString(R.string.unknown));
        }
        this.mService.f1072Pa.m1377lb(str4);
        this.mService.f1072Pa.m1370a(bitmap);
        this.mService.f1072Pa.setIndex(i);
        C0792a.m1512d("info..: " + this.mService.f1072Pa.toString());
        m1346qe();
        m1351ve();
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo800a(Boolean bool) {
        if (bool.booleanValue()) {
            CircleImageView circleImageView = this.f1025Fb;
            if (circleImageView.state != 1) {
                circleImageView.m1525Va();
            }
            this.f1019Bb.m1525Va();
            this.f1036Qb.setImageLevel(1);
            this.f1035Pb.setImageLevel(1);
            this.f1040Ub.setImageLevel(1);
        } else {
            CircleImageView circleImageView2 = this.f1025Fb;
            if (circleImageView2.state == 1) {
                circleImageView2.m1524Ua();
                Log.d("MusicActivity", "Downey:onPlayingState: mAlbumArt.pauseMusic()");
            }
            CircleImageView circleImageView3 = this.f1019Bb;
            if (circleImageView3.state == 1) {
                circleImageView3.m1524Ua();
            }
            this.f1036Qb.setImageLevel(0);
            this.f1035Pb.setImageLevel(0);
            this.f1040Ub.setImageLevel(0);
        }
        C0792a.m1512d("onPlayingState info..: " + bool);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo799a(C0580g c0580g) {
        if (c0580g == null) {
            return;
        }
        this.mRecord = c0580g;
        this.f1066xb.m1363a(c0580g, this.mService.f1072Pa);
        m1351ve();
        for (int i = 0; i <= 4; i++) {
            if (i == c0580g.mIndex) {
                ((ImageView) findViewById(this.f1051gc[i])).getBackground().setLevel(1);
            } else {
                ((ImageView) findViewById(this.f1051gc[i])).getBackground().setLevel(0);
            }
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicActivity, com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo798a(MediaPlayer mediaPlayer) {
        super.mo798a(mediaPlayer);
    }

    @RequiresApi(api = 21)
    /* renamed from: a */
    public void m1355a(C0773c c0773c) {
        this.f865eb = c0773c.m987Nc().m986Mc();
        this.f1063uc = c0773c;
        m1342b(c0773c);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.XTActivity, com.eckom.xtlibrary.p020b.p052i.InterfaceC0673c
    @RequiresApi(api = 21)
    /* renamed from: a */
    public void mo928a(C0683m c0683m, boolean z) {
        Log.d("MusicActivity", "onThemeSwitchFinish: " + c0683m.m988Oc());
        C0773c c0773c = (C0773c) c0683m;
        m1355a(c0773c);
        m1343c(c0773c);
    }
}
