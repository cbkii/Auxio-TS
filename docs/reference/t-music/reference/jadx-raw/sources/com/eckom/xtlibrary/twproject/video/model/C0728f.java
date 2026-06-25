package com.eckom.xtlibrary.twproject.video.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.eckom.xtlibrary.R$id;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.p020b.p054k.p055a.C0705b;
import com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* compiled from: VideoIjkModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.f */
/* loaded from: classes3.dex */
class C0728f implements Handler.Callback {
    final /* synthetic */ C0735m this$0;

    C0728f(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    /* JADX WARN: Removed duplicated region for block: B:202:0x05b4 A[Catch: Exception -> 0x061c, TryCatch #0 {Exception -> 0x061c, blocks: (B:3:0x0007, B:4:0x000e, B:6:0x0013, B:7:0x0016, B:11:0x001b, B:13:0x0027, B:15:0x003f, B:16:0x0048, B:17:0x005d, B:19:0x006c, B:20:0x0054, B:21:0x0077, B:23:0x008e, B:24:0x0093, B:25:0x00a9, B:27:0x00b3, B:28:0x00cb, B:30:0x00d3, B:31:0x00da, B:33:0x00e2, B:34:0x00e9, B:41:0x0107, B:44:0x015c, B:47:0x0172, B:50:0x0192, B:54:0x01bb, B:57:0x01c8, B:59:0x01cd, B:60:0x01d4, B:62:0x01d8, B:63:0x01df, B:66:0x01f6, B:68:0x0266, B:70:0x026c, B:72:0x0274, B:74:0x027c, B:76:0x0284, B:77:0x028b, B:79:0x0295, B:81:0x029b, B:83:0x02a3, B:85:0x02ab, B:86:0x02b2, B:88:0x02be, B:90:0x02c6, B:91:0x02d7, B:93:0x02e3, B:94:0x02ee, B:96:0x02f8, B:98:0x0314, B:100:0x031c, B:101:0x0328, B:103:0x0330, B:104:0x033c, B:111:0x034d, B:113:0x0353, B:114:0x035e, B:115:0x03e8, B:118:0x03f1, B:120:0x03fc, B:122:0x0400, B:123:0x0414, B:125:0x041a, B:126:0x042a, B:128:0x044a, B:130:0x0452, B:131:0x0461, B:132:0x036d, B:134:0x0374, B:135:0x0397, B:137:0x039b, B:138:0x03a3, B:139:0x0386, B:140:0x03ab, B:142:0x03b2, B:143:0x03d5, B:145:0x03d9, B:146:0x03e1, B:147:0x03c4, B:148:0x047d, B:151:0x0487, B:153:0x048c, B:154:0x0497, B:156:0x049c, B:157:0x04a3, B:158:0x04aa, B:159:0x04b1, B:160:0x04b8, B:161:0x04bf, B:162:0x04c6, B:164:0x04da, B:165:0x04df, B:167:0x04e7, B:168:0x04ec, B:170:0x04f2, B:171:0x04f9, B:173:0x0501, B:174:0x0508, B:175:0x050f, B:176:0x0516, B:177:0x051d, B:179:0x0525, B:180:0x052c, B:181:0x0533, B:182:0x0546, B:184:0x054a, B:186:0x0552, B:187:0x0557, B:188:0x0561, B:190:0x056b, B:191:0x0582, B:194:0x058d, B:196:0x059b, B:200:0x05a7, B:202:0x05b4, B:206:0x05be, B:209:0x05c3, B:212:0x05cc, B:214:0x05d4, B:216:0x05f5, B:220:0x0601, B:222:0x060e, B:226:0x0618), top: B:2:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:222:0x060e A[Catch: Exception -> 0x061c, TryCatch #0 {Exception -> 0x061c, blocks: (B:3:0x0007, B:4:0x000e, B:6:0x0013, B:7:0x0016, B:11:0x001b, B:13:0x0027, B:15:0x003f, B:16:0x0048, B:17:0x005d, B:19:0x006c, B:20:0x0054, B:21:0x0077, B:23:0x008e, B:24:0x0093, B:25:0x00a9, B:27:0x00b3, B:28:0x00cb, B:30:0x00d3, B:31:0x00da, B:33:0x00e2, B:34:0x00e9, B:41:0x0107, B:44:0x015c, B:47:0x0172, B:50:0x0192, B:54:0x01bb, B:57:0x01c8, B:59:0x01cd, B:60:0x01d4, B:62:0x01d8, B:63:0x01df, B:66:0x01f6, B:68:0x0266, B:70:0x026c, B:72:0x0274, B:74:0x027c, B:76:0x0284, B:77:0x028b, B:79:0x0295, B:81:0x029b, B:83:0x02a3, B:85:0x02ab, B:86:0x02b2, B:88:0x02be, B:90:0x02c6, B:91:0x02d7, B:93:0x02e3, B:94:0x02ee, B:96:0x02f8, B:98:0x0314, B:100:0x031c, B:101:0x0328, B:103:0x0330, B:104:0x033c, B:111:0x034d, B:113:0x0353, B:114:0x035e, B:115:0x03e8, B:118:0x03f1, B:120:0x03fc, B:122:0x0400, B:123:0x0414, B:125:0x041a, B:126:0x042a, B:128:0x044a, B:130:0x0452, B:131:0x0461, B:132:0x036d, B:134:0x0374, B:135:0x0397, B:137:0x039b, B:138:0x03a3, B:139:0x0386, B:140:0x03ab, B:142:0x03b2, B:143:0x03d5, B:145:0x03d9, B:146:0x03e1, B:147:0x03c4, B:148:0x047d, B:151:0x0487, B:153:0x048c, B:154:0x0497, B:156:0x049c, B:157:0x04a3, B:158:0x04aa, B:159:0x04b1, B:160:0x04b8, B:161:0x04bf, B:162:0x04c6, B:164:0x04da, B:165:0x04df, B:167:0x04e7, B:168:0x04ec, B:170:0x04f2, B:171:0x04f9, B:173:0x0501, B:174:0x0508, B:175:0x050f, B:176:0x0516, B:177:0x051d, B:179:0x0525, B:180:0x052c, B:181:0x0533, B:182:0x0546, B:184:0x054a, B:186:0x0552, B:187:0x0557, B:188:0x0561, B:190:0x056b, B:191:0x0582, B:194:0x058d, B:196:0x059b, B:200:0x05a7, B:202:0x05b4, B:206:0x05be, B:209:0x05c3, B:212:0x05cc, B:214:0x05d4, B:216:0x05f5, B:220:0x0601, B:222:0x060e, B:226:0x0618), top: B:2:0x0007 }] */
    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleMessage(Message message) {
        int i;
        boolean z;
        C0760l c0760l;
        InterfaceC0708b interfaceC0708b;
        InterfaceC0708b interfaceC0708b2;
        C0760l c0760l2;
        boolean z2;
        C0760l c0760l3;
        C0760l c0760l4;
        C0760l c0760l5;
        boolean z3;
        InterfaceC0708b interfaceC0708b3;
        C0760l c0760l6;
        InterfaceC0708b interfaceC0708b4;
        C0760l c0760l7;
        boolean z4;
        C0760l c0760l8;
        boolean z5;
        C0760l c0760l9;
        C0760l c0760l10;
        C0760l c0760l11;
        C0760l c0760l12;
        InterfaceC0708b interfaceC0708b5;
        C0760l c0760l13;
        Handler handler;
        InterfaceC0708b interfaceC0708b6;
        Handler handler2;
        boolean z6;
        InterfaceC0708b interfaceC0708b7;
        boolean z7;
        C0760l c0760l14;
        C0760l c0760l15;
        InterfaceC0708b interfaceC0708b8;
        C0760l c0760l16;
        InterfaceC0708b interfaceC0708b9;
        C0760l c0760l17;
        C0760l c0760l18;
        Context context;
        C0760l c0760l19;
        int service;
        InterfaceC0708b interfaceC0708b10;
        C0760l c0760l20;
        C0760l c0760l21;
        C0760l c0760l22;
        C0760l c0760l23;
        C0760l c0760l24;
        C0760l c0760l25;
        int i2;
        InterfaceC0708b interfaceC0708b11;
        int i3;
        int i4;
        boolean z8;
        boolean z9;
        View view;
        View view2;
        C0760l c0760l26;
        Context context2;
        boolean z10;
        boolean z11;
        int i5;
        boolean z12;
        C0760l c0760l27;
        boolean z13;
        C0760l c0760l28;
        int i6;
        boolean z14;
        boolean z15;
        int i7;
        boolean z16;
        InterfaceC0708b interfaceC0708b12;
        boolean z17;
        InterfaceC0708b interfaceC0708b13;
        C0760l c0760l29;
        C0760l c0760l30;
        C0760l c0760l31;
        C0760l c0760l32;
        View view3;
        int i8;
        InterfaceC0708b interfaceC0708b14;
        Handler handler3;
        Handler handler4;
        C0760l c0760l33;
        C0760l c0760l34;
        int service2;
        C0760l c0760l35;
        C0760l c0760l36;
        C0760l c0760l37;
        InterfaceC0708b interfaceC0708b15;
        C0760l c0760l38;
        InterfaceC0708b interfaceC0708b16;
        View view4;
        View view5;
        Handler handler5;
        Handler handler6;
        View view6;
        Handler handler7;
        Handler handler8;
        Handler handler9;
        C0760l unused;
        C0760l unused2;
        C0760l unused3;
        C0760l unused4;
        C0760l unused5;
        C0760l unused6;
        C0760l unused7;
        C0760l unused8;
        C0760l unused9;
        C0760l unused10;
        C0760l unused11;
        C0760l unused12;
        try {
            i = message.what;
            z = true;
            i8 = 1;
            z17 = true;
            z7 = true;
            z5 = true;
            z5 = true;
            z3 = true;
            z = true;
        } catch (Exception e) {
            Log.i("VideoIjkModel", "" + e.toString());
        }
        switch (i) {
            case 267:
                byte[] bArr = (byte[]) message.obj;
                c0760l = C0735m.f915jd;
                c0760l.f994Md = bArr[1];
                interfaceC0708b = C0735m.f912dj;
                interfaceC0708b.mo1055Y(bArr[1]);
                interfaceC0708b2 = C0735m.f912dj;
                c0760l2 = C0735m.f915jd;
                if (!c0760l2.f993Ld) {
                    c0760l5 = C0735m.f915jd;
                    if (c0760l5.f994Md != 0) {
                        z2 = false;
                        interfaceC0708b2.mo1066l(z2);
                        C0735m c0735m = this.this$0;
                        c0760l3 = C0735m.f915jd;
                        if (!c0760l3.f993Ld) {
                            c0760l4 = C0735m.f915jd;
                            if (c0760l4.f994Md != 0) {
                                z = false;
                            }
                        }
                        c0735m.m1161N(z);
                        return false;
                    }
                }
                z2 = true;
                interfaceC0708b2.mo1066l(z2);
                C0735m c0735m2 = this.this$0;
                c0760l3 = C0735m.f915jd;
                if (!c0760l3.f993Ld) {
                }
                c0735m2.m1161N(z);
                return false;
            case 274:
                if ((message.arg1 & 65536) != 65536) {
                    z3 = false;
                }
                interfaceC0708b3 = C0735m.f912dj;
                interfaceC0708b3.mo1069q(z3);
                return false;
            case 517:
                c0760l6 = C0735m.f915jd;
                c0760l6.f993Ld = message.arg1 == 1;
                interfaceC0708b4 = C0735m.f912dj;
                c0760l7 = C0735m.f915jd;
                if (!c0760l7.f993Ld) {
                    c0760l10 = C0735m.f915jd;
                    if (c0760l10.f994Md != 0) {
                        z4 = false;
                        interfaceC0708b4.mo1066l(z4);
                        C0735m c0735m3 = this.this$0;
                        c0760l8 = C0735m.f915jd;
                        if (!c0760l8.f993Ld) {
                            c0760l9 = C0735m.f915jd;
                            if (c0760l9.f994Md != 0) {
                                z5 = false;
                            }
                        }
                        c0735m3.m1161N(z5);
                        return false;
                    }
                }
                z4 = true;
                interfaceC0708b4.mo1066l(z4);
                C0735m c0735m32 = this.this$0;
                c0760l8 = C0735m.f915jd;
                if (!c0760l8.f993Ld) {
                }
                c0735m32.m1161N(z5);
                return false;
            case 769:
                if (message.arg1 != 9) {
                    if (this.this$0.isPlaying()) {
                        this.this$0.mo1154P();
                    }
                    this.this$0.m1208F(false);
                    this.this$0.m1207E(false);
                }
                c0760l11 = C0735m.f915jd;
                if (c0760l11.mSource != message.arg1) {
                    c0760l12 = C0735m.f915jd;
                    c0760l12.mSource = message.arg1;
                    interfaceC0708b5 = C0735m.f912dj;
                    c0760l13 = C0735m.f915jd;
                    interfaceC0708b5.setSource(c0760l13.mSource);
                }
                return false;
            case 40454:
                handler = this.this$0.mHandler;
                handler.removeMessages(40454);
                this.this$0.m1212oc();
                return false;
            case 40457:
                interfaceC0708b6 = C0735m.f912dj;
                interfaceC0708b6.mo1070u(message.arg1);
                switch (message.arg1) {
                    case 1:
                        if (this.this$0.isPlaying()) {
                            this.this$0.mo1154P();
                        } else {
                            this.this$0.mo1158ma();
                        }
                        return false;
                    case 2:
                        this.this$0.m1163Sa();
                        return false;
                    case 3:
                        this.this$0.mo1156ic();
                        return false;
                    case 4:
                        this.this$0.mo1157jc();
                        return false;
                    case 5:
                        if (!this.this$0.isPlaying()) {
                            this.this$0.mo1158ma();
                        }
                        return false;
                    case 6:
                        handler2 = this.this$0.mHandler;
                        handler2.removeMessages(65284);
                        if (this.this$0.isPlaying()) {
                            this.this$0.mo1154P();
                        }
                        z6 = this.this$0.f919Oi;
                        if (z6) {
                            this.this$0.m1207E(false);
                        }
                        if (this.this$0.f929Wi != null) {
                            this.this$0.m1208F(false);
                        }
                        return false;
                    case 7:
                        this.this$0.m1160L(true);
                        return false;
                    case 8:
                        this.this$0.m1160L(false);
                        return false;
                    case 9:
                        this.this$0.m1210mc();
                        return false;
                    case 10:
                        this.this$0.m1211nc();
                        return false;
                    case 11:
                        this.this$0.mute(true);
                        return false;
                    case 12:
                        this.this$0.mute(false);
                        return false;
                    default:
                        return false;
                }
            case 40476:
                interfaceC0708b7 = C0735m.f912dj;
                if (message.arg1 == 0) {
                    z7 = false;
                }
                interfaceC0708b7.mo1062g(z7);
                return false;
            case 40479:
                String str = null;
                int i9 = message.arg1;
                if (i9 == 1) {
                    unused12 = C0735m.f915jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        c0760l15 = C0735m.f915jd;
                        c0760l15.m1306ra(str);
                    } else {
                        c0760l14 = C0735m.f915jd;
                        c0760l14.m1304pa(str);
                    }
                } else if (i9 == 2) {
                    unused11 = C0735m.f915jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        c0760l22 = C0735m.f915jd;
                        c0760l22.m1307sa(str);
                    } else {
                        c0760l21 = C0735m.f915jd;
                        c0760l21.m1305qa(str);
                    }
                } else if (i9 == 3) {
                    str = "/mnt/sdcard/iNand";
                    if (message.arg2 == 0) {
                        c0760l25 = C0735m.f915jd;
                        c0760l25.f1001td.m1050wc();
                    } else {
                        c0760l23 = C0735m.f915jd;
                        c0760l24 = C0735m.f915jd;
                        c0760l23.m1301a(c0760l24.f1001td, "/mnt/sdcard/iNand");
                    }
                }
                unused7 = C0735m.f915jd;
                if (C0760l.f982Cd != null && str != null) {
                    unused8 = C0735m.f915jd;
                    if (C0760l.f982Cd.startsWith(str)) {
                        if (message.arg2 == 0) {
                            c0760l20 = C0735m.f915jd;
                            c0760l20.f992Dd.m1050wc();
                            this.this$0.stop();
                            this.this$0.m1208F(false);
                        } else {
                            if (C0686b.m1006_c()) {
                                this.this$0.m1175bf();
                                interfaceC0708b10 = C0735m.f912dj;
                                interfaceC0708b10.onMediaView(this.this$0.mMediaPlayer);
                            }
                            c0760l18 = C0735m.f915jd;
                            context = this.this$0.mContext;
                            c0760l19 = C0735m.f915jd;
                            C0705b c0705b = c0760l19.f992Dd;
                            unused9 = C0735m.f915jd;
                            c0760l18.m1300a(context, c0705b, C0760l.f982Cd);
                            service = this.this$0.getService();
                            if (service == 9 && !this.this$0.isPlaying()) {
                                C0735m c0735m4 = this.this$0;
                                unused10 = C0735m.f915jd;
                                c0735m4.m1177c(C0760l.f991md, false);
                                this.this$0.mo1158ma();
                            }
                        }
                    }
                }
                interfaceC0708b8 = C0735m.f912dj;
                c0760l16 = C0735m.f915jd;
                interfaceC0708b8.mo1056a(c0760l16.f1002ud);
                interfaceC0708b9 = C0735m.f912dj;
                c0760l17 = C0735m.f915jd;
                interfaceC0708b9.mo1057b(c0760l17.f992Dd);
                return false;
            case 40732:
                i2 = this.this$0.f940wi;
                if (i2 != message.arg1) {
                    this.this$0.f940wi = message.arg1;
                    interfaceC0708b11 = C0735m.f912dj;
                    i3 = this.this$0.f940wi;
                    interfaceC0708b11.mo1072v(i3);
                    i4 = this.this$0.f940wi;
                    if (i4 != 0) {
                        z9 = this.this$0.f919Oi;
                        if (z9) {
                            this.this$0.f920Pi = true;
                            this.this$0.m1207E(false);
                        }
                    }
                    z8 = this.this$0.f920Pi;
                    if (z8) {
                        this.this$0.f920Pi = false;
                        this.this$0.m1207E(true);
                    }
                }
                return false;
            case 65288:
                view = this.this$0.layout_suspension;
                if (view.getVisibility() == 0) {
                    view2 = this.this$0.layout_suspension;
                    view2.setVisibility(8);
                }
                return false;
            case 65294:
                c0760l26 = C0735m.f915jd;
                if (c0760l26.mSource == 9) {
                    C0735m c0735m5 = this.this$0;
                    unused6 = C0735m.f915jd;
                    c0735m5.seekTo(C0760l.f991md);
                    this.this$0.mo1158ma();
                }
                return false;
            case 65297:
                C0735m c0735m6 = this.this$0;
                context2 = this.this$0.mContext;
                c0735m6.f918Ni = Settings.System.getInt(context2.getContentResolver(), "SYSTEM_FLOATVIDEO", 0) == 1;
                this.this$0.m1209lc();
                StringBuilder sb = new StringBuilder();
                sb.append("0xff11:floatVideo:");
                z10 = this.this$0.f918Ni;
                sb.append(z10);
                sb.append(" playing:");
                sb.append(this.this$0.f926Ui);
                sb.append(" inOnclickHome:");
                z11 = this.this$0.f917Mi;
                sb.append(z11);
                sb.append(" mReverse:");
                i5 = this.this$0.f940wi;
                sb.append(i5);
                sb.append(" showPipView:");
                z12 = this.this$0.f923Ri;
                sb.append(z12);
                sb.append(" MultiWindowMode:");
                c0760l27 = C0735m.f915jd;
                sb.append(c0760l27.m1299Ta());
                Log.d("VideoIjkModel", sb.toString());
                z13 = this.this$0.f918Ni;
                if (z13 && this.this$0.f926Ui) {
                    z15 = this.this$0.f917Mi;
                    if (z15) {
                        i7 = this.this$0.f940wi;
                        if (i7 == 0) {
                            z16 = this.this$0.f923Ri;
                            if (!z16) {
                                this.this$0.m1207E(true);
                                return false;
                            }
                        }
                    }
                }
                c0760l28 = C0735m.f915jd;
                if (!c0760l28.m1299Ta() && this.this$0.f926Ui) {
                    i6 = this.this$0.f940wi;
                    if (i6 == 0) {
                        z14 = this.this$0.f923Ri;
                        if (!z14) {
                            this.this$0.m1208F(true);
                            return false;
                        }
                    }
                }
                this.this$0.m1208F(false);
                this.this$0.m1207E(false);
                return false;
            default:
                switch (i) {
                    case InputDeviceCompat.SOURCE_DPAD /* 513 */:
                        if (message.arg2 == 16) {
                            this.this$0.f917Mi = true;
                        }
                        return false;
                    case 514:
                        this.this$0.m1163Sa();
                        return false;
                    case 515:
                        interfaceC0708b12 = C0735m.f912dj;
                        if ((message.arg1 & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                            z17 = false;
                        }
                        interfaceC0708b12.mo1060f(z17);
                        return false;
                    default:
                        switch (i) {
                            case 65281:
                                int duration = this.this$0.mMediaPlayer.getDuration();
                                int currentPosition = this.this$0.mMediaPlayer.getCurrentPosition();
                                unused2 = C0735m.f915jd;
                                C0760l.f991md = currentPosition;
                                if (duration < 0) {
                                    duration = 0;
                                }
                                if (currentPosition < 0) {
                                    currentPosition = 0;
                                }
                                if (currentPosition > duration) {
                                    return true;
                                }
                                int i10 = currentPosition / 1000;
                                int i11 = i10 / 60;
                                unused3 = C0735m.f915jd;
                                C0760l.f991md = currentPosition;
                                interfaceC0708b13 = C0735m.f912dj;
                                unused4 = C0735m.f915jd;
                                interfaceC0708b13.mo1059d(C0760l.f991md, duration);
                                int i12 = (currentPosition * 100) / duration;
                                c0760l29 = C0735m.f915jd;
                                unused5 = C0735m.f915jd;
                                int i13 = C0760l.f980Ad + 1;
                                c0760l30 = C0735m.f915jd;
                                c0760l29.m1302b(1, i13, c0760l30.f992Dd.f851kk, (((i11 / 60) % 24) << 16) | ((i11 % 60) << 8) | (i10 % 60), i12);
                                c0760l31 = C0735m.f915jd;
                                int i14 = 128;
                                int i15 = i12 & 127;
                                c0760l31.write(40704, 9, (this.this$0.isPlaying() ? 128 : 0) | i15);
                                c0760l32 = C0735m.f915jd;
                                if (!this.this$0.isPlaying()) {
                                    i14 = 0;
                                }
                                c0760l32.write(771, 9, i15 | i14);
                                view3 = this.this$0.mRoot;
                                Drawable drawable = ((ImageView) view3.findViewById(R$id.img_suspension_pp)).getDrawable();
                                if (!this.this$0.isPlaying()) {
                                    i8 = 0;
                                }
                                drawable.setLevel(i8);
                                interfaceC0708b14 = C0735m.f912dj;
                                interfaceC0708b14.mo1058c(this.this$0.isPlaying());
                                handler3 = this.this$0.mHandler;
                                handler3.removeMessages(65281);
                                handler4 = this.this$0.mHandler;
                                handler4.sendEmptyMessageDelayed(65281, 1000L);
                                return false;
                            case 65282:
                                c0760l33 = C0735m.f915jd;
                                if (c0760l33.mSource == 9) {
                                    this.this$0.m1162Re();
                                }
                                return false;
                            case 65283:
                                c0760l34 = C0735m.f915jd;
                                if (c0760l34.mSource == 9) {
                                    this.this$0.m1164Se();
                                }
                                return false;
                            case 65284:
                                service2 = this.this$0.getService();
                                if ((service2 & 143) == 9) {
                                    C0735m c0735m7 = this.this$0;
                                    unused = C0735m.f915jd;
                                    c0735m7.seekTo(C0760l.f991md);
                                    this.this$0.mo1158ma();
                                    this.this$0.mMediaPlayer.setVisibility(0);
                                }
                                return false;
                            case 65285:
                                c0760l35 = C0735m.f915jd;
                                c0760l36 = C0735m.f915jd;
                                c0760l35.m1301a(c0760l36.f1001td, "/mnt/sdcard");
                                c0760l37 = C0735m.f915jd;
                                if (c0760l37.f1002ud != null) {
                                    this.this$0.m1167Ze();
                                }
                                interfaceC0708b15 = C0735m.f912dj;
                                c0760l38 = C0735m.f915jd;
                                interfaceC0708b15.mo1056a(c0760l38.f1002ud);
                                interfaceC0708b16 = C0735m.f912dj;
                                interfaceC0708b16.mo1052L();
                                return false;
                            case 65286:
                                view4 = this.this$0.layout_suspension;
                                if (view4.getVisibility() == 8) {
                                    view6 = this.this$0.layout_suspension;
                                    view6.setVisibility(0);
                                    handler7 = this.this$0.mHandler;
                                    if (handler7.hasMessages(65288)) {
                                        handler9 = this.this$0.mHandler;
                                        handler9.removeMessages(65288);
                                    }
                                    handler8 = this.this$0.mHandler;
                                    handler8.sendEmptyMessageDelayed(65288, 4000L);
                                } else {
                                    view5 = this.this$0.layout_suspension;
                                    view5.setVisibility(8);
                                }
                                handler5 = this.this$0.mHandler;
                                if (handler5.hasMessages(65286)) {
                                    handler6 = this.this$0.mHandler;
                                    handler6.removeMessages(65286);
                                }
                                return false;
                            default:
                                return false;
                        }
                }
        }
    }
}
