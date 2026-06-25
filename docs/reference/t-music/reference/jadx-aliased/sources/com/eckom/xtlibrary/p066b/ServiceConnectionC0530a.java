package com.eckom.xtlibrary.p066b;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import p060c.p063b.p064a.p065a.p018a.InterfaceC0516d;

/* compiled from: XTManage.java */
/* renamed from: com.eckom.xtlibrary.b.a */
/* loaded from: classes3.dex */
class ServiceConnectionC0530a implements ServiceConnection {
    final /* synthetic */ C0556b this$0;

    ServiceConnectionC0530a(C0556b c0556b) {
        this.this$0 = c0556b;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        try {
            Log.d("XTManage", "=onServiceConnected=");
            this.this$0.f455cd = InterfaceC0516d.a.asInterface(iBinder);
            this.this$0.m381Ie();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        try {
            Log.d("XTManage", "=onServiceDisconnected=");
            this.this$0.m382Je();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
