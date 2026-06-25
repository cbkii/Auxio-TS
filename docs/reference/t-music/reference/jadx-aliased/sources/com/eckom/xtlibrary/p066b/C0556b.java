package com.eckom.xtlibrary.p066b;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import com.eckom.xtlibrary.p066b.p030b.BinderC0557a;
import com.eckom.xtlibrary.p066b.p030b.BinderC0558b;
import com.eckom.xtlibrary.p066b.p030b.BinderC0559c;
import com.eckom.xtlibrary.p066b.p030b.BinderC0560d;
import com.eckom.xtlibrary.p066b.p030b.BinderC0561e;
import com.eckom.xtlibrary.p066b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p066b.p067a.p026e.C0549a;
import com.eckom.xtlibrary.p066b.p068d.p034b.C0568a;
import com.eckom.xtlibrary.p066b.p069f.p042e.C0635a;
import com.eckom.xtlibrary.p066b.p070h.p049c.C0667a;
import com.eckom.xtlibrary.p066b.p071k.p056b.C0706a;
import p060c.p063b.p064a.p065a.p018a.InterfaceC0516d;

/* compiled from: XTManage.java */
/* renamed from: com.eckom.xtlibrary.b.b */
/* loaded from: classes3.dex */
public class C0556b {

    /* renamed from: Pf */
    private AbstractC0658a f446Pf;

    /* renamed from: Qf */
    private BinderC0560d f447Qf;

    /* renamed from: Rf */
    private BinderC0557a f448Rf;

    /* renamed from: Sf */
    private BinderC0558b f449Sf;

    /* renamed from: Tf */
    public BinderC0559c f450Tf;

    /* renamed from: Uf */
    public BinderC0561e f451Uf;

    /* renamed from: Vf */
    private final String f452Vf;

    /* renamed from: Wf */
    private final String f453Wf;

    /* renamed from: Xf */
    private final String f454Xf;

    /* renamed from: cd */
    public InterfaceC0516d f455cd;
    private ServiceConnection mConnection;
    public Context mContext;

    /* compiled from: XTManage.java */
    /* renamed from: com.eckom.xtlibrary.b.b$a */
    private static class a {
        private static final C0556b sInstance = new C0556b(null);
    }

    /* synthetic */ C0556b(ServiceConnectionC0530a serviceConnectionC0530a) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Ie */
    public void m381Ie() {
        Log.d("XTManage", "=(basePresenter instanceof RadioPresenter)=" + (this.f446Pf instanceof C0667a));
        AbstractC0658a abstractC0658a = this.f446Pf;
        if (abstractC0658a instanceof C0667a) {
            if (this.f447Qf == null) {
                this.f447Qf = new BinderC0560d(abstractC0658a, this.f455cd);
            }
            Log.d("XTManage", "registerCallBack iRadioCallBack");
            Log.d("XTManage", "=registerRadioCallBack=");
            this.f455cd.mo150b(this.f447Qf);
            return;
        }
        if (abstractC0658a instanceof C0549a) {
            if (this.f448Rf == null) {
                this.f448Rf = new BinderC0557a(abstractC0658a, this.f455cd);
            }
            Log.d("XTManage", "registerCallBack iBTCallBack");
            this.f455cd.mo148b(this.f448Rf);
            return;
        }
        if (abstractC0658a instanceof C0635a) {
            if (this.f450Tf == null) {
                this.f450Tf = new BinderC0559c(abstractC0658a);
            }
            Log.d("XTManage", "registerCallBack iMusicCallBack");
            this.f455cd.mo144a(this.f450Tf);
            return;
        }
        if (abstractC0658a instanceof C0706a) {
            if (this.f451Uf == null) {
                this.f451Uf = new BinderC0561e(abstractC0658a);
            }
            Log.d("XTManage", "registerCallBack iVideoCallBack");
            this.f455cd.mo152b(this.f451Uf);
            return;
        }
        if (abstractC0658a instanceof C0568a) {
            if (this.f449Sf == null) {
                this.f449Sf = new BinderC0558b(abstractC0658a);
            }
            this.f455cd.mo151b(this.f449Sf);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: Je */
    public void m382Je() {
        BinderC0558b binderC0558b;
        AbstractC0658a abstractC0658a = this.f446Pf;
        if (abstractC0658a instanceof C0667a) {
            BinderC0560d binderC0560d = this.f447Qf;
            if (binderC0560d != null) {
                this.f455cd.mo145a(binderC0560d);
                return;
            }
            return;
        }
        if (abstractC0658a instanceof C0549a) {
            BinderC0557a binderC0557a = this.f448Rf;
            if (binderC0557a != null) {
                this.f455cd.mo143a(binderC0557a);
                return;
            }
            return;
        }
        if (abstractC0658a instanceof C0635a) {
            BinderC0559c binderC0559c = this.f450Tf;
            if (binderC0559c != null) {
                this.f455cd.mo149b(binderC0559c);
                return;
            }
            return;
        }
        if (abstractC0658a instanceof C0706a) {
            BinderC0561e binderC0561e = this.f451Uf;
            if (binderC0561e != null) {
                this.f455cd.mo147a(binderC0561e);
                return;
            }
            return;
        }
        if (!(abstractC0658a instanceof C0568a) || (binderC0558b = this.f449Sf) == null) {
            return;
        }
        this.f455cd.mo146a(binderC0558b);
    }

    public static C0556b getInstant() {
        return a.sInstance;
    }

    /* renamed from: db */
    public void m386db() {
        try {
            if (this.mConnection != null) {
                this.mContext.unbindService(this.mConnection);
            }
            this.f446Pf.onDestroy();
        } catch (Exception unused) {
        }
    }

    public void init(Context context) {
        this.mContext = context;
        Log.d("XTManage", "XTManage init: ");
        Intent intent = new Intent();
        intent.setClassName("com.tw.service.xt", "com.tw.service.xt.CommandService");
        intent.setAction("com.tw.service.xt.CommandService.Bind");
        intent.getClass();
        intent.getAction();
        this.mContext.getApplicationContext().bindService(intent, this.mConnection, 1);
        Log.d("XTManage", "XTManage init: bindService");
    }

    private C0556b() {
        this.f452Vf = "com.tw.service.xt";
        this.f453Wf = "com.tw.service.xt.CommandService";
        this.f454Xf = "com.tw.service.xt.CommandService.Bind";
        this.mConnection = new ServiceConnectionC0530a(this);
    }

    /* renamed from: a */
    public void m385a(AbstractC0658a abstractC0658a) {
        this.f446Pf = abstractC0658a;
    }
}
