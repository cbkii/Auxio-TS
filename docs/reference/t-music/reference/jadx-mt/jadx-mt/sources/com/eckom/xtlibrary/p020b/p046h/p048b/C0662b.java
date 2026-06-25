package com.eckom.xtlibrary.p020b.p046h.p048b;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.Log;
import com.eckom.xtlibrary.p019a.C0529b;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.p020b.p053j.C0699o;
import java.io.File;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.h.b.b */
/* JADX INFO: compiled from: RadioModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0662b implements Handler.Callback {
    final /* synthetic */ C0665e this$0;

    C0662b(C0665e c0665e) {
        this.this$0 = c0665e;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) throws Throwable {
        try {
            int i = message.what;
            if (i == 265) {
                byte[] bArr = (byte[]) message.obj;
                Log.d("RadioModel", "handleMessage: 0x0109=" + ((int) bArr[0]) + "," + this.this$0.location);
                if (this.this$0.location != bArr[0]) {
                    this.this$0.location = bArr[0];
                    int iM1029b = C0699o.m1029b(this.this$0.mContext, "Radio", "radio_location", -1);
                    long jLongValue = C0699o.m1025a(this.this$0.mContext, "Radio", "radio_freq_logo_data_size", -1L).longValue();
                    File file = new File("/sdcard/iNand/radio//" + (this.this$0.location != 3 ? "radio_data.txt" : "radio_data_dny.txt"));
                    Log.d("RadioModel", "handleMessage: 0x0109=," + file.exists() + ",logoSize=" + jLongValue + "," + file.length() + "," + iM1029b + "," + this.this$0.location);
                    if ((file.exists() && file.length() != jLongValue) || iM1029b != this.this$0.location) {
                        this.this$0.mHandler.removeMessages(65281);
                        this.this$0.mHandler.sendEmptyMessageDelayed(65281, 2000L);
                    }
                }
            } else if (i == 266) {
                Log.d("RadioModel", "handleMessage: " + message.arg1);
                if (message.arg1 == 0) {
                    if (message.obj instanceof String) {
                        this.this$0.f779Fi.f766pl = (String) message.obj;
                    }
                    if (this.this$0.f779Fi.f766pl != null && this.this$0.f779Fi.f766pl.length() > 0) {
                        try {
                            this.this$0.f779Fi.f767ql = Integer.parseInt(this.this$0.f779Fi.f766pl.split("-")[3].substring(2, 3), 16);
                        } catch (Exception unused) {
                        }
                    }
                }
            } else if (i == 274) {
                boolean z = (message.arg1 & 65536) == 65536;
                Iterator it = this.this$0.f788yh.entrySet().iterator();
                while (it.hasNext()) {
                    ((InterfaceC0666f) ((Map.Entry) it.next()).getValue()).mo871q(z);
                }
            } else if (i == 513) {
                this.this$0.m842c(message);
            } else if (i == 515) {
                this.this$0.f779Fi.f765ol = (message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE;
                Iterator it2 = this.this$0.f788yh.entrySet().iterator();
                while (it2.hasNext()) {
                    ((InterfaceC0666f) ((Map.Entry) it2.next()).getValue()).mo864f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                }
            } else if (i == 769) {
                this.this$0.m840b(message);
            } else if (i == 1025) {
                this.this$0.m837a(message);
            } else if (i != 1026) {
                switch (i) {
                    case 1028:
                        this.this$0.m821d(message);
                        break;
                    case 1029:
                        if (message.obj instanceof String) {
                            this.this$0.f779Fi.f758hl = (String) message.obj;
                        }
                        Iterator it3 = this.this$0.f788yh.entrySet().iterator();
                        while (it3.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it3.next()).getValue()).mo865ha((String) message.obj);
                        }
                        break;
                    case 1030:
                        this.this$0.m824e(message);
                        break;
                    default:
                        switch (i) {
                            case 40448:
                                this.this$0.mActivity = message.arg1;
                                break;
                            case 40449:
                                int i2 = message.arg1;
                                if (i2 == 3) {
                                    this.this$0.next();
                                } else if (i2 == 4) {
                                    this.this$0.m839ac();
                                } else if (i2 == 9) {
                                    this.this$0.m845dc();
                                } else if (i2 == 10) {
                                    this.this$0.m846ec();
                                }
                                break;
                            default:
                                switch (i) {
                                    case MotionEventCompat.ACTION_POINTER_INDEX_MASK /* 65280 */:
                                        if (this.this$0.f786wg) {
                                            this.this$0.m833Yb();
                                        }
                                        break;
                                    case 65281:
                                        Log.d("RadioModel", "handleMessage: MSG_DB_HELPER_UPDATE");
                                        this.this$0.f781Hi.m1144ba(this.this$0.location);
                                        this.this$0.f781Hi.m1142a(this.this$0.f781Hi.getWritableDatabase());
                                        break;
                                    case 65282:
                                        int i3 = message.arg1;
                                        Iterator it4 = this.this$0.f788yh.entrySet().iterator();
                                        while (it4.hasNext()) {
                                            ((InterfaceC0666f) ((Map.Entry) it4.next()).getValue()).mo872r(i3);
                                        }
                                        break;
                                    case 65283:
                                        Drawable drawableM1007a = C0686b.m1007a(this.this$0.f785pi, this.this$0.f779Fi.f748Wk, this.this$0.f781Hi, this.this$0.location);
                                        this.this$0.f779Fi.f763ml = drawableM1007a;
                                        if (message.obj instanceof String) {
                                            this.this$0.f779Fi.f757gl = (String) message.obj;
                                        }
                                        for (Map.Entry entry : this.this$0.f788yh.entrySet()) {
                                            ((InterfaceC0666f) entry.getValue()).mo854E((message.arg2 >> 8) & 255);
                                            ((InterfaceC0666f) entry.getValue()).mo853A(message.arg2 & 255);
                                            if (message.obj != null) {
                                                ((InterfaceC0666f) entry.getValue()).mo862ba((String) message.obj);
                                            }
                                            ((InterfaceC0666f) entry.getValue()).mo860a(drawableM1007a);
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
            } else {
                if ((this.this$0.f784if & 128) == 128) {
                    int i4 = message.arg1;
                    if (i4 == 0) {
                        Iterator it5 = this.this$0.f788yh.entrySet().iterator();
                        while (it5.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it5.next()).getValue()).mo874s(0);
                        }
                    } else if (i4 == 6) {
                        Iterator it6 = this.this$0.f788yh.entrySet().iterator();
                        while (it6.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it6.next()).getValue()).mo874s(1);
                        }
                    } else if (i4 == 12) {
                        Iterator it7 = this.this$0.f788yh.entrySet().iterator();
                        while (it7.hasNext()) {
                            ((InterfaceC0666f) ((Map.Entry) it7.next()).getValue()).mo874s(2);
                        }
                    }
                }
                int i5 = message.arg1 & 255;
                int i6 = message.arg2;
                int i7 = (message.arg1 >> 8) & 255;
                int i8 = (message.arg1 >> 16) & SupportMenu.USER_MASK;
                String strM1013ib = C0686b.m1013ib(Integer.toHexString(i8));
                Drawable drawableM1007a2 = C0686b.m1007a(i8, i6, this.this$0.f781Hi, this.this$0.location);
                C0529b.m178a("0402:" + i5 + "," + message.arg2 + "  ||    " + i8 + "  ||   " + strM1013ib + "|| icon == " + drawableM1007a2);
                if (i5 < this.this$0.f779Fi.f734Gi.length) {
                    this.this$0.f779Fi.f734Gi[i5].f769sl = i7;
                    this.this$0.f779Fi.f734Gi[i5].f770tl = i6;
                    this.this$0.f779Fi.f734Gi[i5].f774xl = drawableM1007a2;
                    this.this$0.f779Fi.f734Gi[i5].mPi = strM1013ib;
                    this.this$0.f779Fi.f734Gi[i5].f773wl = i8;
                    if (!(message.obj instanceof String) || TextUtils.isEmpty((String) message.obj)) {
                        this.this$0.f779Fi.f734Gi[i5].f771ul = "";
                    } else {
                        this.this$0.f779Fi.f734Gi[i5].f771ul = (String) message.obj;
                    }
                    Message messageObtain = Message.obtain();
                    messageObtain.arg1 = i5;
                    messageObtain.what = 65282;
                    this.this$0.mHandler.sendMessage(messageObtain);
                }
            }
        } catch (Exception e) {
            Log.e("RadioModel", "handleMessage: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }
}
