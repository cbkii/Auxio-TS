package com.p073tw.music;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import com.eckom.xtlibrary.p066b.p069f.p042e.C0635a;
import com.eckom.xtlibrary.twproject.activity.BaseMusicService;
import com.p073tw.music.p062b.C0769a;
import com.p073tw.music.view.MusicWidgetProvider;

/* loaded from: classes3.dex */
public class MusicService extends BaseMusicService {
    private final IBinder mBinder = new BinderC0763a();

    /* renamed from: Pa */
    public C0769a f1072Pa = new C0769a();

    /* renamed from: Qa */
    private MusicWidgetProvider f1073Qa = null;

    /* renamed from: Ra */
    private BroadcastReceiver f1074Ra = new C0781k(this);

    /* renamed from: com.tw.music.MusicService$a */
    public class BinderC0763a extends Binder {
        public BinderC0763a() {
        }

        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0657b
    /* renamed from: B */
    public void mo795B(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo801a(String str, String str2, String str3, Bitmap bitmap, String str4, String str5, int i) {
        super.mo801a(str, str2, str3, bitmap, str4, str5, i);
        this.f1073Qa.m1530a(this);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0657b
    /* renamed from: d */
    public void mo803d(int i, int i2) {
        super.mo803d(i, i2);
        this.f1073Qa.m1530a(this);
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f1072Pa = new C0769a();
        this.f1073Qa = MusicWidgetProvider.getInstance();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tw.music.action.cmd");
        intentFilter.addAction("com.tw.music.action.prev");
        intentFilter.addAction("com.tw.music.action.next");
        intentFilter.addAction("com.tw.music.action.pp");
        registerReceiver(this.f1074Ra, intentFilter);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onDestroy() {
        try {
            unregisterReceiver(this.f1074Ra);
            this.f1072Pa = null;
        } catch (Exception unused) {
        }
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent == null) {
            return 1;
        }
        String action = intent.getAction();
        String stringExtra = intent.getStringExtra("cmd");
        if ("prev".equals(stringExtra) || "com.tw.music.action.prev".equals(action)) {
            ((C0635a) this.mPresenter).m736rb();
            return 1;
        }
        if ("next".equals(stringExtra) || "com.tw.music.action.next".equals(action)) {
            ((C0635a) this.mPresenter).m734pb();
            return 1;
        }
        if (!"pp".equals(stringExtra) && !"com.tw.music.action.pp".equals(action)) {
            return 1;
        }
        if (this.f1072Pa.isPlaying()) {
            ((C0635a) this.mPresenter).m726ba();
            return 1;
        }
        ((C0635a) this.mPresenter).m730fa();
        return 1;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.eckom.xtlibrary.twproject.service.XTService
    /* renamed from: za */
    public C0635a mo1151za() {
        return new C0635a(this);
    }

    @Override // com.eckom.xtlibrary.twproject.activity.BaseMusicService, com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0657b
    /* renamed from: a */
    public void mo800a(Boolean bool) {
        super.mo800a(bool);
        C0769a c0769a = this.f1072Pa;
        if (c0769a != null) {
            c0769a.m1368H(bool.booleanValue());
        }
    }
}
