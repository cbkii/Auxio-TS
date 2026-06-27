package com.eckom.xtlibrary.p020b.p021a.p022a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p021a.p022a.C0532b;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0533a;

/* compiled from: BroadcastManager.java */
/* renamed from: com.eckom.xtlibrary.b.a.a.a */
/* loaded from: classes3.dex */
class C0531a extends BroadcastReceiver {
    final /* synthetic */ C0532b this$0;

    C0531a(C0532b c0532b) {
        this.this$0 = c0532b;
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x02e7, code lost:
    
        if (r0.m199gb() != false) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0331, code lost:
    
        if (r0.m199gb() != false) goto L109;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x02b4, code lost:
    
        if (r0.m199gb() != false) goto L91;
     */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    @Override // android.content.BroadcastReceiver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onReceive(Context context, Intent intent) {
        Context context2;
        C0533a c0533a;
        C0533a c0533a2;
        C0532b.a aVar;
        C0532b.a aVar2;
        C0533a c0533a3;
        C0533a c0533a4;
        C0532b.b bVar;
        C0532b.b bVar2;
        C0533a c0533a5;
        C0533a c0533a6;
        C0533a c0533a7;
        C0533a c0533a8;
        C0532b.b bVar3;
        C0532b.b bVar4;
        C0533a c0533a9;
        C0533a c0533a10;
        C0533a c0533a11;
        C0533a c0533a12;
        C0532b.b bVar5;
        C0532b.b bVar6;
        C0533a c0533a13;
        C0533a c0533a14;
        C0533a c0533a15;
        C0533a c0533a16;
        C0533a c0533a17;
        C0533a c0533a18;
        C0533a c0533a19;
        C0533a c0533a20;
        C0533a c0533a21;
        C0533a c0533a22;
        C0533a c0533a23;
        C0533a c0533a24;
        C0533a c0533a25;
        C0533a c0533a26;
        C0532b.b bVar7;
        C0532b.b bVar8;
        C0533a c0533a27;
        C0533a c0533a28;
        Context context3;
        C0533a c0533a29;
        C0532b.b bVar9;
        C0532b.b bVar10;
        C0532b.b bVar11;
        C0532b.b bVar12;
        C0533a c0533a30;
        C0533a c0533a31;
        C0533a c0533a32;
        C0533a c0533a33;
        C0533a c0533a34;
        C0533a c0533a35;
        C0533a c0533a36;
        if ("net.easyconn.bt.checkstatus".equals(intent.getAction())) {
            Intent intent2 = new Intent();
            c0533a30 = this.this$0.f359la;
            if (c0533a30.f403sg == 2) {
                intent2.setAction("net.easyconn.bt.connected");
                C0532b c0532b = this.this$0;
                c0533a36 = c0532b.f359la;
                c0532b.m189a(true, c0533a36.f410zg);
            } else {
                C0532b c0532b2 = this.this$0;
                c0533a31 = c0532b2.f359la;
                c0532b2.m189a(false, c0533a31.f410zg);
                intent2.setAction("net.easyconn.bt.opened");
                c0533a32 = this.this$0.f359la;
                if (c0533a32.f410zg != null) {
                    c0533a35 = this.this$0.f359la;
                    intent2.putExtra("name", c0533a35.f410zg);
                }
                c0533a33 = this.this$0.f359la;
                if (c0533a33.f362Ag != null) {
                    c0533a34 = this.this$0.f359la;
                    intent2.putExtra("pin", c0533a34.f362Ag);
                }
            }
            context.sendBroadcast(intent2);
            return;
        }
        if (intent.getAction().equals("net.easyconn.a2dp.acquire")) {
            bVar11 = this.this$0.f357jg;
            if (bVar11 != null) {
                bVar12 = this.this$0.f357jg;
                bVar12.mo198w(true);
                return;
            }
            return;
        }
        if (intent.getAction().equals("net.easyconn.a2dp.release")) {
            bVar9 = this.this$0.f357jg;
            if (bVar9 != null) {
                bVar10 = this.this$0.f357jg;
                bVar10.mo198w(false);
                return;
            }
            return;
        }
        if ("com.unisound.intent.action.GET_CONTACTS".equals(intent.getAction())) {
            c0533a27 = this.this$0.f359la;
            if (c0533a27.f403sg == 2) {
                c0533a28 = this.this$0.f359la;
                if (c0533a28.f409yg != null) {
                    context3 = this.this$0.mContext;
                    Intent intent3 = new Intent("com.unisound.intent.action.SYNC_CONTACTS");
                    c0533a29 = this.this$0.f359la;
                    context3.sendBroadcast(intent3.putExtra("mac", c0533a29.f409yg));
                    return;
                }
                return;
            }
            return;
        }
        char c2 = 65535;
        if ("com.tw.launcher.btmsg".equals(intent.getAction())) {
            int intExtra = intent.getIntExtra(NotificationCompat.CATEGORY_MESSAGE, -1);
            bVar7 = this.this$0.f357jg;
            if (bVar7 != null) {
                bVar8 = this.this$0.f357jg;
                bVar8.mo197t(intExtra);
                return;
            }
            return;
        }
        if ("com.unisound.intent.action.GET_DEVICE_INFO".equals(intent.getAction())) {
            C0532b c0532b3 = this.this$0;
            c0533a25 = c0532b3.f359la;
            String str = c0533a25.f410zg;
            c0533a26 = this.this$0.f359la;
            c0532b3.m194x(str, c0533a26.f362Ag);
            return;
        }
        if ("com.unisound.intent.action.GET_PAIR_INFO".equals(intent.getAction())) {
            C0532b c0532b4 = this.this$0;
            c0533a23 = c0532b4.f359la;
            String str2 = c0533a23.f408xg;
            c0533a24 = this.this$0.f359la;
            c0532b4.m195y(str2, c0533a24.f409yg);
            return;
        }
        if ("com.unisound.intent.action.GET_CALL_INFO".equals(intent.getAction())) {
            C0532b c0532b5 = this.this$0;
            c0533a20 = c0532b5.f359la;
            int i = c0533a20.f364Cg;
            c0533a21 = this.this$0.f359la;
            String str3 = c0533a21.f400pg;
            c0533a22 = this.this$0.f359la;
            c0532b5.m193i(i, str3, c0533a22.f399og);
            return;
        }
        if ("com.unisound.intent.action.GET_ID3_INFO".equals(intent.getAction())) {
            this.this$0.f360lg = true;
            C0532b c0532b6 = this.this$0;
            c0533a16 = c0532b6.f359la;
            String str4 = c0533a16.f369Hg;
            c0533a17 = this.this$0.f359la;
            String str5 = c0533a17.f371Jg;
            c0533a18 = this.this$0.f359la;
            int i2 = c0533a18.f373Lg;
            c0533a19 = this.this$0.f359la;
            c0532b6.m188a(str4, str5, i2, c0533a19.f372Kg);
            return;
        }
        if ("com.unisound.intent.action.GET_BATTARY_INFO".equals(intent.getAction())) {
            C0532b c0532b7 = this.this$0;
            c0533a14 = c0532b7.f359la;
            int i3 = c0533a14.f383Vg;
            c0533a15 = this.this$0.f359la;
            c0532b7.m192i(i3, c0533a15.f382Ug);
            return;
        }
        if ("com.unisound.intent.action.GET_BT_INFO".equals(intent.getAction())) {
            try {
                Intent intent4 = new Intent("com.unisound.intent.action.SEND_BT_INFO");
                context2 = this.this$0.mContext;
                context2.sendBroadcast(intent4);
                return;
            } catch (Exception e) {
                Log.e("BroadcastManager", "YZS_SEND_BT_INFO: " + e.getMessage());
                return;
            }
        }
        if (!"com.zjinnova.zlink".equals(intent.getAction())) {
            if ("action.hicar.onconnect".equals(intent.getAction())) {
                c0533a2 = this.this$0.f359la;
                c0533a2.f385Xg = true;
                return;
            } else {
                if ("action.hicar.ondisconnect".equals(intent.getAction())) {
                    c0533a = this.this$0.f359la;
                    c0533a.f385Xg = false;
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
                        c2 = 0;
                        break;
                    }
                    break;
                case -1843701849:
                    if (stringExtra.equals("MAIN_PAGE_SHOW")) {
                        c2 = 4;
                        break;
                    }
                    break;
                case -497207953:
                    if (stringExtra.equals("PHONE_CALL_ON")) {
                        c2 = 2;
                        break;
                    }
                    break;
                case 1015497884:
                    if (stringExtra.equals("DISCONNECT")) {
                        c2 = 1;
                        break;
                    }
                    break;
                case 1766422463:
                    if (stringExtra.equals("PHONE_CALL_OFF")) {
                        c2 = 3;
                        break;
                    }
                    break;
            }
            if (c2 == 0) {
                c0533a3 = this.this$0.f359la;
                c0533a3.f384Wg = true;
                if (!TextUtils.isEmpty(stringExtra2)) {
                    c0533a6 = this.this$0.f359la;
                    c0533a6.f386Yg = stringExtra2;
                }
                c0533a4 = this.this$0.f359la;
                if (!c0533a4.m200hb()) {
                    c0533a5 = this.this$0.f359la;
                }
                bVar = this.this$0.f357jg;
                if (bVar != null) {
                    bVar2 = this.this$0.f357jg;
                    bVar2.mo198w(true);
                }
            } else if (c2 == 1) {
                c0533a7 = this.this$0.f359la;
                c0533a7.f384Wg = false;
                c0533a8 = this.this$0.f359la;
                if (!c0533a8.m200hb()) {
                    c0533a10 = this.this$0.f359la;
                }
                bVar3 = this.this$0.f357jg;
                if (bVar3 != null) {
                    bVar4 = this.this$0.f357jg;
                    bVar4.mo198w(false);
                }
                c0533a9 = this.this$0.f359la;
                c0533a9.f386Yg = "";
            } else if (c2 != 2 && c2 != 3 && c2 == 4) {
                c0533a11 = this.this$0.f359la;
                if (c0533a11.f384Wg) {
                    c0533a12 = this.this$0.f359la;
                    if (!c0533a12.m200hb()) {
                        c0533a13 = this.this$0.f359la;
                    }
                    bVar5 = this.this$0.f357jg;
                    if (bVar5 != null) {
                        bVar6 = this.this$0.f357jg;
                        bVar6.mo198w(true);
                    }
                }
            }
        }
        aVar = this.this$0.f358kg;
        if (aVar != null) {
            aVar2 = this.this$0.f358kg;
            aVar2.m196t(stringExtra, stringExtra2);
        }
    }
}
