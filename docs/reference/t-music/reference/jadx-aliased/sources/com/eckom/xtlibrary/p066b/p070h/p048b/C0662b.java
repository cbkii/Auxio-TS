package com.eckom.xtlibrary.p066b.p070h.p048b;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p066b.p053j.C0686b;
import com.eckom.xtlibrary.p066b.p053j.C0699o;
import com.eckom.xtlibrary.p066b.p070h.C0659a;
import com.eckom.xtlibrary.twproject.radio.utils.C0721b;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

/* compiled from: RadioModel.java */
/* renamed from: com.eckom.xtlibrary.b.h.b.b */
/* loaded from: classes3.dex */
class C0662b implements Handler.Callback {
    final /* synthetic */ C0665e this$0;

    C0662b(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        Context context;
        Context context2;
        C0659a c0659a;
        C0659a c0659a2;
        C0659a c0659a3;
        C0659a c0659a4;
        C0659a c0659a5;
        Map map;
        C0659a c0659a6;
        Map map2;
        int i;
        C0721b c0721b;
        C0659a c0659a7;
        C0659a c0659a8;
        C0659a c0659a9;
        C0659a c0659a10;
        C0659a c0659a11;
        C0659a c0659a12;
        C0659a c0659a13;
        C0659a c0659a14;
        C0659a c0659a15;
        Map map3;
        Map map4;
        Map map5;
        Map map6;
        C0659a c0659a16;
        boolean z;
        C0721b c0721b2;
        C0721b c0721b3;
        C0721b c0721b4;
        Map map7;
        int i2;
        C0659a c0659a17;
        C0721b c0721b5;
        C0659a c0659a18;
        Map map8;
        C0659a c0659a19;
        try {
            int i3 = message.what;
            if (i3 == 265) {
                byte[] bArr = (byte[]) message.obj;
                Log.d("RadioModel", "handleMessage: 0x0109=" + ((int) bArr[0]) + "," + this.this$0.location);
                if (this.this$0.location != bArr[0]) {
                    this.this$0.location = bArr[0];
                    context = this.this$0.mContext;
                    int m1029b = C0699o.m1029b(context, "Radio", "radio_location", -1);
                    context2 = this.this$0.mContext;
                    long longValue = C0699o.m1025a(context2, "Radio", "radio_freq_logo_data_size", -1L).longValue();
                    File file = new File("/sdcard/iNand/radio//" + (this.this$0.location != 3 ? "radio_data.txt" : "radio_data_dny.txt"));
                    Log.d("RadioModel", "handleMessage: 0x0109=," + file.exists() + ",logoSize=" + longValue + "," + file.length() + "," + m1029b + "," + this.this$0.location);
                    if ((file.exists() && file.length() != longValue) || m1029b != this.this$0.location) {
                        this.this$0.mHandler.removeMessages(65281);
                        this.this$0.mHandler.sendEmptyMessageDelayed(65281, 2000L);
                    }
                }
            } else if (i3 == 266) {
                Log.d("RadioModel", "handleMessage: " + message.arg1);
                if (message.arg1 == 0) {
                    if (message.obj instanceof String) {
                        c0659a5 = this.this$0.f779Fi;
                        c0659a5.f766pl = (String) message.obj;
                    }
                    c0659a = this.this$0.f779Fi;
                    if (c0659a.f766pl != null) {
                        c0659a2 = this.this$0.f779Fi;
                        if (c0659a2.f766pl.length() > 0) {
                            try {
                                c0659a3 = this.this$0.f779Fi;
                                String str = c0659a3.f766pl.split("-")[3];
                                c0659a4 = this.this$0.f779Fi;
                                c0659a4.f767ql = Integer.parseInt(str.substring(2, 3), 16);
                            } catch (Exception unused) {
                            }
                        }
                    }
                }
            } else if (i3 == 274) {
                boolean z2 = (message.arg1 & 65536) == 65536;
                map = this.this$0.f788yh;
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    ((InterfaceC0666f) ((Map.Entry) it.next()).getValue()).mo871q(z2);
                }
            } else if (i3 == 513) {
                this.this$0.m842c(message);
            } else if (i3 == 515) {
                c0659a6 = this.this$0.f779Fi;
                c0659a6.f765ol = (message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE;
                map2 = this.this$0.f788yh;
                Iterator it2 = map2.entrySet().iterator();
                while (it2.hasNext()) {
                    ((InterfaceC0666f) ((Map.Entry) it2.next()).getValue()).mo864f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                }
            } else if (i3 == 769) {
                this.this$0.m840b(message);
            } else if (i3 == 1025) {
                this.this$0.m837a(message);
            } else if (i3 != 1026) {
                switch (i3) {
                    case 1028:
                        this.this$0.m821d(message);
                        break;
                    case 1029:
                        if (message.obj instanceof String) {
                            c0659a16 = this.this$0.f779Fi;
                            c0659a16.f758hl = (String) message.obj;
                        }
                        map6 = this.this$0.f788yh;
                        Iterator it3 = map6.entrySet().iterator();
                        while (it3.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it3.next()).getValue()).mo865ha((String) message.obj);
                        }
                        break;
                    case 1030:
                        this.this$0.m824e(message);
                        break;
                    default:
                        switch (i3) {
                            case 40448:
                                this.this$0.mActivity = message.arg1;
                                break;
                            case 40449:
                                int i4 = message.arg1;
                                if (i4 != 3) {
                                    if (i4 != 4) {
                                        if (i4 != 9) {
                                            if (i4 != 10) {
                                                break;
                                            } else {
                                                this.this$0.m846ec();
                                                break;
                                            }
                                        } else {
                                            this.this$0.m845dc();
                                            break;
                                        }
                                    } else {
                                        this.this$0.m839ac();
                                        break;
                                    }
                                } else {
                                    this.this$0.next();
                                    break;
                                }
                            default:
                                switch (i3) {
                                    case MotionEventCompat.ACTION_POINTER_INDEX_MASK /* 65280 */:
                                        z = this.this$0.f786wg;
                                        if (z) {
                                            this.this$0.m833Yb();
                                            break;
                                        }
                                        break;
                                    case 65281:
                                        Log.d("RadioModel", "handleMessage: MSG_DB_HELPER_UPDATE");
                                        c0721b2 = this.this$0.f781Hi;
                                        c0721b2.m1144ba(this.this$0.location);
                                        c0721b3 = this.this$0.f781Hi;
                                        SQLiteDatabase writableDatabase = c0721b3.getWritableDatabase();
                                        c0721b4 = this.this$0.f781Hi;
                                        c0721b4.m1142a(writableDatabase);
                                        break;
                                    case 65282:
                                        int i5 = message.arg1;
                                        map7 = this.this$0.f788yh;
                                        Iterator it4 = map7.entrySet().iterator();
                                        while (it4.hasNext()) {
                                            ((InterfaceC0666f) ((Map.Entry) it4.next()).getValue()).mo872r(i5);
                                        }
                                        break;
                                    case 65283:
                                        i2 = this.this$0.f785pi;
                                        c0659a17 = this.this$0.f779Fi;
                                        int i6 = c0659a17.f748Wk;
                                        c0721b5 = this.this$0.f781Hi;
                                        Drawable m1007a = C0686b.m1007a(i2, i6, c0721b5, this.this$0.location);
                                        c0659a18 = this.this$0.f779Fi;
                                        c0659a18.f763ml = m1007a;
                                        if (message.obj instanceof String) {
                                            c0659a19 = this.this$0.f779Fi;
                                            c0659a19.f757gl = (String) message.obj;
                                        }
                                        map8 = this.this$0.f788yh;
                                        for (Map.Entry entry : map8.entrySet()) {
                                            ((InterfaceC0666f) entry.getValue()).mo854E((message.arg2 >> 8) & 255);
                                            ((InterfaceC0666f) entry.getValue()).mo853A(message.arg2 & 255);
                                            if (message.obj != null) {
                                                ((InterfaceC0666f) entry.getValue()).mo862ba((String) message.obj);
                                            }
                                            ((InterfaceC0666f) entry.getValue()).mo860a(m1007a);
                                        }
                                        break;
                                }
                        }
                }
            } else {
                i = this.this$0.f784if;
                if ((i & 128) == 128) {
                    int i7 = message.arg1;
                    if (i7 == 0) {
                        map3 = this.this$0.f788yh;
                        Iterator it5 = map3.entrySet().iterator();
                        while (it5.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it5.next()).getValue()).mo874s(0);
                        }
                    } else if (i7 == 6) {
                        map4 = this.this$0.f788yh;
                        Iterator it6 = map4.entrySet().iterator();
                        while (it6.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it6.next()).getValue()).mo874s(1);
                        }
                    } else if (i7 == 12) {
                        map5 = this.this$0.f788yh;
                        Iterator it7 = map5.entrySet().iterator();
                        while (it7.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it7.next()).getValue()).mo874s(2);
                        }
                    }
                }
                int i8 = message.arg1 & 255;
                int i9 = message.arg2;
                int i10 = (message.arg1 >> 8) & 255;
                int i11 = (message.arg1 >> 16) & SupportMenu.USER_MASK;
                String m1013ib = C0686b.m1013ib(Integer.toHexString(i11));
                c0721b = this.this$0.f781Hi;
                Drawable m1007a2 = C0686b.m1007a(i11, i9, c0721b, this.this$0.location);
                C0529b.m178a("0402:" + i8 + "," + message.arg2 + "  ||    " + i11 + "  ||   " + m1013ib + "|| icon == " + m1007a2);
                c0659a7 = this.this$0.f779Fi;
                if (i8 < c0659a7.f734Gi.length) {
                    c0659a8 = this.this$0.f779Fi;
                    c0659a8.f734Gi[i8].f769sl = i10;
                    c0659a9 = this.this$0.f779Fi;
                    c0659a9.f734Gi[i8].f770tl = i9;
                    c0659a10 = this.this$0.f779Fi;
                    c0659a10.f734Gi[i8].f774xl = m1007a2;
                    c0659a11 = this.this$0.f779Fi;
                    c0659a11.f734Gi[i8].mPi = m1013ib;
                    c0659a12 = this.this$0.f779Fi;
                    c0659a12.f734Gi[i8].f773wl = i11;
                    if (!(message.obj instanceof String)) {
                        c0659a13 = this.this$0.f779Fi;
                        c0659a13.f734Gi[i8].f771ul = "";
                    } else if (TextUtils.isEmpty((String) message.obj)) {
                        c0659a14 = this.this$0.f779Fi;
                        c0659a14.f734Gi[i8].f771ul = "";
                    } else {
                        c0659a15 = this.this$0.f779Fi;
                        c0659a15.f734Gi[i8].f771ul = (String) message.obj;
                    }
                    Message obtain = Message.obtain();
                    obtain.arg1 = i8;
                    obtain.what = 65282;
                    this.this$0.mHandler.sendMessage(obtain);
                }
            }
        } catch (Exception e) {
            Log.e("RadioModel", "handleMessage: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
}
