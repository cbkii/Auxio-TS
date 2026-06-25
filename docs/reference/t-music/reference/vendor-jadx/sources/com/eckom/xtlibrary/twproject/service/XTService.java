package com.eckom.xtlibrary.twproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.eckom.xtlibrary.p020b.C0556b;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p058l.InterfaceC0710a;

/* loaded from: classes3.dex */
public abstract class XTService<P extends AbstractC0658a> extends Service {
    private Handler mHandler = new HandlerC0722a(this);
    public P mPresenter;

    /* JADX WARN: Multi-variable type inference failed */
    /* renamed from: Aa */
    protected void m1153Aa() {
        if (this.mPresenter == null) {
            this.mPresenter = (P) mo1151za();
            if (this instanceof InterfaceC0710a) {
                this.mPresenter.m807a((InterfaceC0710a) this);
            }
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        m1153Aa();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        C0556b.getInstant().m386db();
        this.mPresenter.delete();
        this.mPresenter = null;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        this.mHandler.removeMessages(65281);
        this.mHandler.sendEmptyMessageDelayed(65281, 2500L);
        return super.onStartCommand(intent, i, i2);
    }

    /* renamed from: za */
    public abstract P mo1151za();
}
