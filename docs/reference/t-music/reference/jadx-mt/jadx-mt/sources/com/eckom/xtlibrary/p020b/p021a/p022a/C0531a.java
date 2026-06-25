package com.eckom.xtlibrary.p020b.p021a.p022a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.a.a */
/* JADX INFO: compiled from: BroadcastManager.java */
/* JADX INFO: loaded from: classes3.dex */
class C0531a extends BroadcastReceiver {
    final /* synthetic */ C0532b this$0;

    C0531a(C0532b c0532b) {
        this.this$0 = c0532b;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("net.easyconn.bt.checkstatus".equals(intent.getAction())) {
            Intent intent2 = new Intent();
            if (this.this$0.f359la.f403sg == 2) {
                intent2.setAction("net.easyconn.bt.connected");
                C0532b c0532b = this.this$0;
                c0532b.m189a(true, c0532b.f359la.f410zg);
            } else {
                C0532b c0532b2 = this.this$0;
                c0532b2.m189a(false, c0532b2.f359la.f410zg);
                intent2.setAction("net.easyconn.bt.opened");
                if (this.this$0.f359la.f410zg != null) {
                    intent2.putExtra("name", this.this$0.f359la.f410zg);
                }
                if (this.this$0.f359la.f362Ag != null) {
                    intent2.putExtra("pin", this.this$0.f359la.f362Ag);
                }
            }
            context.sendBroadcast(intent2);
            return;
        }
        if (intent.getAction().equals("net.easyconn.a2dp.acquire")) {
            if (this.this$0.f357jg != null) {
                this.this$0.f357jg.mo198w(true);
                return;
            }
            return;
        }
        if (intent.getAction().equals("net.easyconn.a2dp.release")) {
            if (this.this$0.f357jg != null) {
                this.this$0.f357jg.mo198w(false);
                return;
            }
            return;
        }
        if ("com.unisound.intent.action.GET_CONTACTS".equals(intent.getAction())) {
            if (this.this$0.f359la.f403sg != 2 || this.this$0.f359la.f409yg == null) {
                return;
            }
            this.this$0.mContext.sendBroadcast(new Intent("com.unisound.intent.action.SYNC_CONTACTS").putExtra("mac", this.this$0.f359la.f409yg));
            return;
        }
        byte b2 = -1;
        if ("com.tw.launcher.btmsg".equals(intent.getAction())) {
            int intExtra = intent.getIntExtra(NotificationCompat.CATEGORY_MESSAGE, -1);
            if (this.this$0.f357jg != null) {
                this.this$0.f357jg.mo197t(intExtra);
                return;
            }
            return;
        }
        if ("com.unisound.intent.action.GET_DEVICE_INFO".equals(intent.getAction())) {
            C0532b c0532b3 = this.this$0;
            c0532b3.m194x(c0532b3.f359la.f410zg, this.this$0.f359la.f362Ag);
            return;
        }
        if ("com.unisound.intent.action.GET_PAIR_INFO".equals(intent.getAction())) {
            C0532b c0532b4 = this.this$0;
            c0532b4.m195y(c0532b4.f359la.f408xg, this.this$0.f359la.f409yg);
            return;
        }
        if ("com.unisound.intent.action.GET_CALL_INFO".equals(intent.getAction())) {
            C0532b c0532b5 = this.this$0;
            c0532b5.m193i(c0532b5.f359la.f364Cg, this.this$0.f359la.f400pg, this.this$0.f359la.f399og);
            return;
        }
        if ("com.unisound.intent.action.GET_ID3_INFO".equals(intent.getAction())) {
            this.this$0.f360lg = true;
            C0532b c0532b6 = this.this$0;
            c0532b6.m188a(c0532b6.f359la.f369Hg, this.this$0.f359la.f371Jg, this.this$0.f359la.f373Lg, this.this$0.f359la.f372Kg);
            return;
        }
        if ("com.unisound.intent.action.GET_BATTARY_INFO".equals(intent.getAction())) {
            C0532b c0532b7 = this.this$0;
            c0532b7.m192i(c0532b7.f359la.f383Vg, this.this$0.f359la.f382Ug);
            return;
        }
        if ("com.unisound.intent.action.GET_BT_INFO".equals(intent.getAction())) {
            try {
                this.this$0.mContext.sendBroadcast(new Intent("com.unisound.intent.action.SEND_BT_INFO"));
                return;
            } catch (Exception e) {
                Log.e("BroadcastManager", "YZS_SEND_BT_INFO: " + e.getMessage());
                return;
            }
        }
        if (!"com.zjinnova.zlink".equals(intent.getAction())) {
            if ("action.hicar.onconnect".equals(intent.getAction())) {
                this.this$0.f359la.f385Xg = true;
                return;
            } else {
                if ("action.hicar.ondisconnect".equals(intent.getAction())) {
                    this.this$0.f359la.f385Xg = false;
                    return;
                }
                return;
            }
        }
        String stringExtra = intent.getStringExtra(NotificationCompat.CATEGORY_STATUS);
        String stringExtra2 = intent.getStringExtra("phoneMode");
        Log.d("BroadcastManager", "status:status:" + stringExtra + " phoneMode:" + stringExtra2);
        if (!TextUtils.isEmpty(stringExtra)) {
            switch (stringExtra.hashCode()) {
                case -2087582999:
                    if (stringExtra.equals("CONNECTED")) {
                        b2 = 0;
                    }
                    break;
                case -1843701849:
                    if (stringExtra.equals("MAIN_PAGE_SHOW")) {
                        b2 = 4;
                    }
                    break;
                case -497207953:
                    if (stringExtra.equals("PHONE_CALL_ON")) {
                        b2 = 2;
                    }
                    break;
                case 1015497884:
                    if (stringExtra.equals("DISCONNECT")) {
                        b2 = 1;
                    }
                    break;
                case 1766422463:
                    if (stringExtra.equals("PHONE_CALL_OFF")) {
                        b2 = 3;
                    }
                    break;
            }
            if (b2 == 0) {
                this.this$0.f359la.f384Wg = true;
                if (!TextUtils.isEmpty(stringExtra2)) {
                    this.this$0.f359la.f386Yg = stringExtra2;
                }
                if ((this.this$0.f359la.m200hb() || this.this$0.f359la.m199gb()) && this.this$0.f357jg != null) {
                    this.this$0.f357jg.mo198w(true);
                }
            } else if (b2 == 1) {
                this.this$0.f359la.f384Wg = false;
                if ((this.this$0.f359la.m200hb() || this.this$0.f359la.m199gb()) && this.this$0.f357jg != null) {
                    this.this$0.f357jg.mo198w(false);
                }
                this.this$0.f359la.f386Yg = "";
            } else if (b2 != 2 && b2 != 3 && b2 == 4 && this.this$0.f359la.f384Wg && ((this.this$0.f359la.m200hb() || this.this$0.f359la.m199gb()) && this.this$0.f357jg != null)) {
                this.this$0.f357jg.mo198w(true);
            }
        }
        if (this.this$0.f358kg != null) {
            this.this$0.f358kg.m196t(stringExtra, stringExtra2);
        }
    }
}
