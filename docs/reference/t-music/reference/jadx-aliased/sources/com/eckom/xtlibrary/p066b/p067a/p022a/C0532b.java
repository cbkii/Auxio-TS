package com.eckom.xtlibrary.p066b.p067a.p022a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import com.eckom.xtlibrary.p066b.p067a.p023b.C0533a;

/* compiled from: BroadcastManager.java */
/* renamed from: com.eckom.xtlibrary.b.a.a.b */
/* loaded from: classes3.dex */
public class C0532b {

    /* renamed from: jg */
    private b f357jg;

    /* renamed from: kg */
    private a f358kg;
    private Context mContext;

    /* renamed from: lg */
    private boolean f360lg = false;
    private BroadcastReceiver mReceiver = new C0531a(this);

    /* renamed from: la */
    private C0533a f359la = C0533a.getInstance();

    /* renamed from: mg */
    private int f361mg = SystemProperties.getInt("persist.tw.bt.module", 2);

    /* compiled from: BroadcastManager.java */
    /* renamed from: com.eckom.xtlibrary.b.a.a.b$a */
    public interface a {
        /* renamed from: t */
        void m196t(String str, String str2);
    }

    /* compiled from: BroadcastManager.java */
    /* renamed from: com.eckom.xtlibrary.b.a.a.b$b */
    public interface b {
        /* renamed from: t */
        void mo197t(int i);

        /* renamed from: w */
        void mo198w(boolean z);
    }

    public C0532b(Context context, b bVar) {
        this.mContext = context.getApplicationContext();
        this.f357jg = bVar;
        m187f(context);
    }

    /* renamed from: f */
    private void m187f(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("net.easyconn.bt.checkstatus");
        intentFilter.addAction("net.easyconn.a2dp.acquire");
        intentFilter.addAction("net.easyconn.a2dp.release");
        intentFilter.addAction("com.unisound.intent.action.GET_CONTACTS");
        intentFilter.addAction("com.tw.launcher.btmsg");
        intentFilter.addAction("com.unisound.intent.action.GET_DEVICE_INFO");
        intentFilter.addAction("com.unisound.intent.action.GET_PAIR_INFO");
        intentFilter.addAction("com.unisound.intent.action.GET_CALL_INFO");
        intentFilter.addAction("com.unisound.intent.action.GET_ID3_INFO");
        intentFilter.addAction("com.unisound.intent.action.GET_BATTARY_INFO");
        intentFilter.addAction("com.unisound.intent.action.GET_BT_INFO");
        intentFilter.addAction("com.zjinnova.zlink");
        intentFilter.addAction("action.hicar.onconnect");
        intentFilter.addAction("action.hicar.ondisconnect");
        context.getApplicationContext().registerReceiver(this.mReceiver, intentFilter);
    }

    /* renamed from: fb */
    public void m190fb() {
        try {
            Intent intent = new Intent("com.aispeech.action.CONTACTS_SYNC_SUCCESS");
            intent.putExtra("ContactsZipSyncSuccess", "");
            this.mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("BroadcastManager", "notifyZipContactsSyncSuccess: " + e.getMessage());
        }
    }

    /* renamed from: h */
    public void m191h(int i, String str, String str2) {
        try {
            Intent intent = new Intent("com.unisound.intent.action.SEND_CALL_INFO2");
            intent.putExtra("state2", i);
            intent.putExtra("callName2", str);
            intent.putExtra("callNumber2", str2);
            this.mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("BroadcastManager", "notifyCall2Info: " + e.getMessage());
        }
    }

    /* renamed from: i */
    public void m192i(int i, int i2) {
        try {
            Intent intent = new Intent("com.tw.bt_phone_info");
            intent.putExtra("singal", i);
            intent.putExtra("power", i2);
            this.mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("BroadcastManager", "notifyPhoneInfo: " + e.getMessage());
        }
    }

    /* renamed from: x */
    public void m194x(String str, String str2) {
        try {
            Intent intent = new Intent("com.unisound.intent.action.SEND_DEVICE_INFO");
            intent.putExtra("deviceName", str);
            intent.putExtra("devicePin", str2);
            this.mContext.sendBroadcast(intent);
            Log.d("BroadcastManager", "notifyDeviceInfo:deviceName:" + str + " devicePin:" + str2);
        } catch (Exception e) {
            Log.e("BroadcastManager", "notifyDeviceInfo: " + e.getMessage());
        }
    }

    /* renamed from: y */
    public void m195y(String str, String str2) {
        try {
            Intent intent = new Intent("com.unisound.intent.action.SEND_PAIR_INFO");
            intent.putExtra("pairName", str);
            intent.putExtra("pairMac", str2);
            this.mContext.sendBroadcast(intent);
            Log.d("BroadcastManager", "notifyPairInfo:pairName:" + str + " pairMac:" + str2);
        } catch (Exception e) {
            Log.e("BroadcastManager", "notifyPairInfo: " + e.getMessage());
        }
    }

    /* renamed from: a */
    public void m188a(String str, String str2, int i, int i2) {
        if (this.f360lg) {
            try {
                Intent intent = new Intent("com.unisound.intent.action.SEND_ID3_INFO");
                intent.putExtra("musicTitle", str);
                intent.putExtra("musicArtist", str2);
                intent.putExtra("musicProgress", i);
                intent.putExtra("musicDuration", i2);
                this.mContext.sendBroadcast(intent);
            } catch (Exception e) {
                Log.e("BroadcastManager", "notifyId3Info: " + e.getMessage());
            }
        }
    }

    /* renamed from: i */
    public void m193i(int i, String str, String str2) {
        try {
            Intent intent = new Intent("com.unisound.intent.action.SEND_CALL_INFO");
            intent.putExtra("state", i);
            intent.putExtra("callName", str);
            intent.putExtra("callNumber", str2);
            Log.d("BroadcastManager", "notifyCallInfo:state:" + i + " callName:" + str + " callNumber:" + str2);
            this.mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("BroadcastManager", "notifyCallInfo: " + e.getMessage());
        }
    }

    /* renamed from: a */
    public void m189a(boolean z, String str) {
        Intent intent = new Intent();
        if (z) {
            intent.setAction("net.easyconn.bt.connected");
            intent.putExtra("name", new String[]{str});
        } else {
            intent.setAction("net.easyconn.bt.closed");
        }
        this.mContext.sendBroadcast(intent);
    }
}
