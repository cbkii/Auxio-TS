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

/* compiled from: VideoModel.java */
/* renamed from: com.eckom.xtlibrary.twproject.video.model.r */
/* loaded from: classes3.dex */
class C0740r implements Handler.Callback {
    final /* synthetic */ C0748z this$0;

    C0740r(C0748z c0748z) {
        this.this$0 = c0748z;
    }

    /* JADX WARN: Removed duplicated region for block: B:205:0x05b9 A[Catch: Exception -> 0x0621, TryCatch #0 {Exception -> 0x0621, blocks: (B:3:0x0007, B:4:0x000e, B:6:0x0013, B:7:0x0016, B:11:0x001b, B:13:0x0027, B:15:0x003f, B:16:0x0048, B:17:0x005d, B:19:0x006c, B:20:0x0054, B:21:0x0077, B:23:0x008e, B:24:0x0093, B:25:0x00a9, B:27:0x00b3, B:28:0x00c9, B:30:0x00d1, B:31:0x00d8, B:33:0x00e0, B:34:0x00e7, B:36:0x00ef, B:43:0x0109, B:46:0x015e, B:49:0x0174, B:52:0x0178, B:55:0x0194, B:57:0x01bd, B:60:0x01ca, B:62:0x01cf, B:63:0x01d6, B:65:0x01da, B:66:0x01e1, B:69:0x01f8, B:71:0x0268, B:73:0x026e, B:75:0x0276, B:77:0x027e, B:79:0x0286, B:80:0x028d, B:82:0x0297, B:84:0x029d, B:86:0x02a5, B:88:0x02ad, B:89:0x02b4, B:91:0x02c0, B:93:0x02c8, B:94:0x02d9, B:96:0x02e5, B:97:0x02f0, B:99:0x02fa, B:101:0x0316, B:103:0x031e, B:104:0x032a, B:106:0x0332, B:107:0x033e, B:114:0x034f, B:116:0x0355, B:117:0x0360, B:118:0x03ea, B:121:0x03f3, B:123:0x03fe, B:125:0x0402, B:126:0x041b, B:128:0x0421, B:129:0x042f, B:131:0x044f, B:133:0x0457, B:134:0x0466, B:135:0x036f, B:137:0x0376, B:138:0x0399, B:140:0x039d, B:141:0x03a5, B:142:0x0388, B:143:0x03ad, B:145:0x03b4, B:146:0x03d7, B:148:0x03db, B:149:0x03e3, B:150:0x03c6, B:151:0x0482, B:154:0x048c, B:156:0x0491, B:157:0x049c, B:159:0x04a1, B:160:0x04a8, B:161:0x04af, B:162:0x04b6, B:163:0x04bd, B:164:0x04c4, B:165:0x04cb, B:167:0x04df, B:168:0x04e4, B:170:0x04ec, B:171:0x04f1, B:173:0x04f7, B:174:0x04fe, B:176:0x0506, B:177:0x050d, B:178:0x0514, B:179:0x051b, B:180:0x0522, B:182:0x052a, B:183:0x0531, B:184:0x0538, B:185:0x054b, B:187:0x054f, B:189:0x0557, B:190:0x055c, B:191:0x0566, B:193:0x0570, B:194:0x0587, B:197:0x0592, B:199:0x05a0, B:203:0x05ac, B:205:0x05b9, B:209:0x05c3, B:212:0x05c8, B:215:0x05d1, B:217:0x05d9, B:219:0x05fa, B:223:0x0606, B:225:0x0613, B:229:0x061d), top: B:2:0x0007 }] */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0613 A[Catch: Exception -> 0x0621, TryCatch #0 {Exception -> 0x0621, blocks: (B:3:0x0007, B:4:0x000e, B:6:0x0013, B:7:0x0016, B:11:0x001b, B:13:0x0027, B:15:0x003f, B:16:0x0048, B:17:0x005d, B:19:0x006c, B:20:0x0054, B:21:0x0077, B:23:0x008e, B:24:0x0093, B:25:0x00a9, B:27:0x00b3, B:28:0x00c9, B:30:0x00d1, B:31:0x00d8, B:33:0x00e0, B:34:0x00e7, B:36:0x00ef, B:43:0x0109, B:46:0x015e, B:49:0x0174, B:52:0x0178, B:55:0x0194, B:57:0x01bd, B:60:0x01ca, B:62:0x01cf, B:63:0x01d6, B:65:0x01da, B:66:0x01e1, B:69:0x01f8, B:71:0x0268, B:73:0x026e, B:75:0x0276, B:77:0x027e, B:79:0x0286, B:80:0x028d, B:82:0x0297, B:84:0x029d, B:86:0x02a5, B:88:0x02ad, B:89:0x02b4, B:91:0x02c0, B:93:0x02c8, B:94:0x02d9, B:96:0x02e5, B:97:0x02f0, B:99:0x02fa, B:101:0x0316, B:103:0x031e, B:104:0x032a, B:106:0x0332, B:107:0x033e, B:114:0x034f, B:116:0x0355, B:117:0x0360, B:118:0x03ea, B:121:0x03f3, B:123:0x03fe, B:125:0x0402, B:126:0x041b, B:128:0x0421, B:129:0x042f, B:131:0x044f, B:133:0x0457, B:134:0x0466, B:135:0x036f, B:137:0x0376, B:138:0x0399, B:140:0x039d, B:141:0x03a5, B:142:0x0388, B:143:0x03ad, B:145:0x03b4, B:146:0x03d7, B:148:0x03db, B:149:0x03e3, B:150:0x03c6, B:151:0x0482, B:154:0x048c, B:156:0x0491, B:157:0x049c, B:159:0x04a1, B:160:0x04a8, B:161:0x04af, B:162:0x04b6, B:163:0x04bd, B:164:0x04c4, B:165:0x04cb, B:167:0x04df, B:168:0x04e4, B:170:0x04ec, B:171:0x04f1, B:173:0x04f7, B:174:0x04fe, B:176:0x0506, B:177:0x050d, B:178:0x0514, B:179:0x051b, B:180:0x0522, B:182:0x052a, B:183:0x0531, B:184:0x0538, B:185:0x054b, B:187:0x054f, B:189:0x0557, B:190:0x055c, B:191:0x0566, B:193:0x0570, B:194:0x0587, B:197:0x0592, B:199:0x05a0, B:203:0x05ac, B:205:0x05b9, B:209:0x05c3, B:212:0x05c8, B:215:0x05d1, B:217:0x05d9, B:219:0x05fa, B:223:0x0606, B:225:0x0613, B:229:0x061d), top: B:2:0x0007 }] */
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
        View view3;
        int i8;
        InterfaceC0708b interfaceC0708b13;
        Handler handler3;
        Handler handler4;
        InterfaceC0708b interfaceC0708b14;
        C0760l c0760l29;
        C0760l c0760l30;
        C0760l c0760l31;
        C0760l c0760l32;
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
            Log.i("VideoModel", "" + e.toString());
        }
        switch (i) {
            case 267:
                byte[] bArr = (byte[]) message.obj;
                c0760l = C0748z.f947jd;
                c0760l.f994Md = bArr[1];
                interfaceC0708b = C0748z.f944dj;
                interfaceC0708b.mo1055Y(bArr[1]);
                interfaceC0708b2 = C0748z.f944dj;
                c0760l2 = C0748z.f947jd;
                if (!c0760l2.f993Ld) {
                    c0760l5 = C0748z.f947jd;
                    if (c0760l5.f994Md != 0) {
                        z2 = false;
                        interfaceC0708b2.mo1066l(z2);
                        C0748z c0748z = this.this$0;
                        c0760l3 = C0748z.f947jd;
                        if (!c0760l3.f993Ld) {
                            c0760l4 = C0748z.f947jd;
                            if (c0760l4.f994Md != 0) {
                                z = false;
                            }
                        }
                        c0748z.m1214N(z);
                        return false;
                    }
                }
                z2 = true;
                interfaceC0708b2.mo1066l(z2);
                C0748z c0748z2 = this.this$0;
                c0760l3 = C0748z.f947jd;
                if (!c0760l3.f993Ld) {
                }
                c0748z2.m1214N(z);
                return false;
            case 274:
                if ((message.arg1 & 65536) != 65536) {
                    z3 = false;
                }
                interfaceC0708b3 = C0748z.f944dj;
                interfaceC0708b3.mo1069q(z3);
                return false;
            case 517:
                c0760l6 = C0748z.f947jd;
                c0760l6.f993Ld = message.arg1 == 1;
                interfaceC0708b4 = C0748z.f944dj;
                c0760l7 = C0748z.f947jd;
                if (!c0760l7.f993Ld) {
                    c0760l10 = C0748z.f947jd;
                    if (c0760l10.f994Md != 0) {
                        z4 = false;
                        interfaceC0708b4.mo1066l(z4);
                        C0748z c0748z3 = this.this$0;
                        c0760l8 = C0748z.f947jd;
                        if (!c0760l8.f993Ld) {
                            c0760l9 = C0748z.f947jd;
                            if (c0760l9.f994Md != 0) {
                                z5 = false;
                            }
                        }
                        c0748z3.m1214N(z5);
                        return false;
                    }
                }
                z4 = true;
                interfaceC0708b4.mo1066l(z4);
                C0748z c0748z32 = this.this$0;
                c0760l8 = C0748z.f947jd;
                if (!c0760l8.f993Ld) {
                }
                c0748z32.m1214N(z5);
                return false;
            case 769:
                if (message.arg1 != 9) {
                    if (this.this$0.isPlaying()) {
                        this.this$0.mo1154P();
                    }
                    this.this$0.m1261F(false);
                    this.this$0.m1260E(false);
                }
                c0760l11 = C0748z.f947jd;
                if (c0760l11.mSource != message.arg1) {
                    c0760l12 = C0748z.f947jd;
                    c0760l12.mSource = message.arg1;
                    interfaceC0708b5 = C0748z.f944dj;
                    c0760l13 = C0748z.f947jd;
                    interfaceC0708b5.setSource(c0760l13.mSource);
                }
                return false;
            case 40454:
                handler = this.this$0.mHandler;
                handler.removeMessages(40454);
                this.this$0.m1265oc();
                return false;
            case 40457:
                interfaceC0708b6 = C0748z.f944dj;
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
                        this.this$0.m1216Sa();
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
                        z6 = this.this$0.f951Oi;
                        if (z6) {
                            this.this$0.m1260E(false);
                        }
                        if (this.this$0.f961Wi != null) {
                            this.this$0.m1261F(false);
                        }
                        return false;
                    case 7:
                        this.this$0.m1213L(true);
                        return false;
                    case 8:
                        this.this$0.m1213L(false);
                        return false;
                    case 9:
                        this.this$0.m1263mc();
                        return false;
                    case 10:
                        this.this$0.m1264nc();
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
                interfaceC0708b7 = C0748z.f944dj;
                if (message.arg1 == 0) {
                    z7 = false;
                }
                interfaceC0708b7.mo1062g(z7);
                return false;
            case 40479:
                String str = null;
                int i9 = message.arg1;
                if (i9 == 1) {
                    unused12 = C0748z.f947jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        c0760l15 = C0748z.f947jd;
                        c0760l15.m1306ra(str);
                    } else {
                        c0760l14 = C0748z.f947jd;
                        c0760l14.m1304pa(str);
                    }
                } else if (i9 == 2) {
                    unused11 = C0748z.f947jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        c0760l22 = C0748z.f947jd;
                        c0760l22.m1307sa(str);
                    } else {
                        c0760l21 = C0748z.f947jd;
                        c0760l21.m1305qa(str);
                    }
                } else if (i9 == 3) {
                    str = "/mnt/sdcard/iNand";
                    if (message.arg2 == 0) {
                        c0760l25 = C0748z.f947jd;
                        c0760l25.f1001td.m1050wc();
                    } else {
                        c0760l23 = C0748z.f947jd;
                        c0760l24 = C0748z.f947jd;
                        c0760l23.m1301a(c0760l24.f1001td, "/mnt/sdcard/iNand");
                    }
                }
                unused7 = C0748z.f947jd;
                if (C0760l.f982Cd != null && str != null) {
                    unused8 = C0748z.f947jd;
                    if (C0760l.f982Cd.startsWith(str)) {
                        if (message.arg2 == 0) {
                            c0760l20 = C0748z.f947jd;
                            c0760l20.f992Dd.m1050wc();
                            this.this$0.stop();
                            this.this$0.m1261F(false);
                            this.this$0.m1260E(false);
                        } else {
                            if (C0686b.m1006_c()) {
                                this.this$0.m1228bf();
                                interfaceC0708b10 = C0748z.f944dj;
                                interfaceC0708b10.onMediaView(C0748z.mMediaPlayer);
                            }
                            c0760l18 = C0748z.f947jd;
                            context = this.this$0.mContext;
                            c0760l19 = C0748z.f947jd;
                            C0705b c0705b = c0760l19.f992Dd;
                            unused9 = C0748z.f947jd;
                            c0760l18.m1300a(context, c0705b, C0760l.f982Cd);
                            service = this.this$0.getService();
                            if (service == 9 && !this.this$0.isPlaying()) {
                                C0748z c0748z4 = this.this$0;
                                unused10 = C0748z.f947jd;
                                c0748z4.m1230c(C0760l.f991md, false);
                                this.this$0.mo1158ma();
                            }
                        }
                    }
                }
                interfaceC0708b8 = C0748z.f944dj;
                c0760l16 = C0748z.f947jd;
                interfaceC0708b8.mo1056a(c0760l16.f1002ud);
                interfaceC0708b9 = C0748z.f944dj;
                c0760l17 = C0748z.f947jd;
                interfaceC0708b9.mo1057b(c0760l17.f992Dd);
                return false;
            case 40732:
                i2 = this.this$0.f972wi;
                if (i2 != message.arg1) {
                    this.this$0.f972wi = message.arg1;
                    interfaceC0708b11 = C0748z.f944dj;
                    i3 = this.this$0.f972wi;
                    interfaceC0708b11.mo1072v(i3);
                    i4 = this.this$0.f972wi;
                    if (i4 != 0) {
                        z9 = this.this$0.f951Oi;
                        if (z9) {
                            this.this$0.f952Pi = true;
                            this.this$0.m1260E(false);
                        }
                    }
                    z8 = this.this$0.f952Pi;
                    if (z8) {
                        this.this$0.f952Pi = false;
                        this.this$0.m1260E(true);
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
                c0760l26 = C0748z.f947jd;
                if (c0760l26.mSource == 9) {
                    C0748z c0748z5 = this.this$0;
                    unused6 = C0748z.f947jd;
                    c0748z5.seekTo(C0760l.f991md);
                    this.this$0.mo1158ma();
                }
                return false;
            case 65297:
                C0748z c0748z6 = this.this$0;
                context2 = this.this$0.mContext;
                c0748z6.f950Ni = Settings.System.getInt(context2.getContentResolver(), "SYSTEM_FLOATVIDEO", 0) == 1;
                this.this$0.m1262lc();
                StringBuilder sb = new StringBuilder();
                sb.append("0xff11:floatVideo:");
                z10 = this.this$0.f950Ni;
                sb.append(z10);
                sb.append(" playing:");
                sb.append(this.this$0.f958Ui);
                sb.append(" inOnclickHome:");
                z11 = this.this$0.f949Mi;
                sb.append(z11);
                sb.append(" mReverse:");
                i5 = this.this$0.f972wi;
                sb.append(i5);
                sb.append(" showPipView:");
                z12 = this.this$0.f955Ri;
                sb.append(z12);
                sb.append(" MultiWindowMode:");
                c0760l27 = C0748z.f947jd;
                sb.append(c0760l27.m1299Ta());
                Log.d("VideoModel", sb.toString());
                z13 = this.this$0.f950Ni;
                if (z13 && this.this$0.f958Ui) {
                    z15 = this.this$0.f949Mi;
                    if (z15) {
                        i7 = this.this$0.f972wi;
                        if (i7 == 0) {
                            z16 = this.this$0.f955Ri;
                            if (!z16) {
                                this.this$0.m1260E(true);
                                return false;
                            }
                        }
                    }
                }
                c0760l28 = C0748z.f947jd;
                if (!c0760l28.m1299Ta() && this.this$0.f958Ui) {
                    i6 = this.this$0.f972wi;
                    if (i6 == 0) {
                        z14 = this.this$0.f955Ri;
                        if (!z14) {
                            this.this$0.m1261F(true);
                            return false;
                        }
                    }
                }
                this.this$0.m1261F(false);
                this.this$0.m1260E(false);
                return false;
            default:
                switch (i) {
                    case InputDeviceCompat.SOURCE_DPAD /* 513 */:
                        if (message.arg2 == 16) {
                            this.this$0.f949Mi = true;
                        }
                        return false;
                    case 514:
                        this.this$0.m1216Sa();
                        return false;
                    case 515:
                        interfaceC0708b12 = C0748z.f944dj;
                        if ((message.arg1 & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                            z17 = false;
                        }
                        interfaceC0708b12.mo1060f(z17);
                        return false;
                    default:
                        switch (i) {
                            case 65281:
                                if (this.this$0.isPlaying()) {
                                    int duration = C0748z.mMediaPlayer.getDuration();
                                    int currentPosition = C0748z.mMediaPlayer.getCurrentPosition();
                                    unused2 = C0748z.f947jd;
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
                                    unused3 = C0748z.f947jd;
                                    C0760l.f991md = currentPosition;
                                    interfaceC0708b14 = C0748z.f944dj;
                                    unused4 = C0748z.f947jd;
                                    interfaceC0708b14.mo1059d(C0760l.f991md, duration);
                                    int i12 = (currentPosition * 100) / duration;
                                    c0760l29 = C0748z.f947jd;
                                    unused5 = C0748z.f947jd;
                                    int i13 = C0760l.f980Ad + 1;
                                    c0760l30 = C0748z.f947jd;
                                    c0760l29.m1302b(1, i13, c0760l30.f992Dd.f851kk, (((i11 / 60) % 24) << 16) | ((i11 % 60) << 8) | (i10 % 60), i12);
                                    c0760l31 = C0748z.f947jd;
                                    int i14 = 128;
                                    int i15 = i12 & 127;
                                    c0760l31.write(40704, 9, (this.this$0.isPlaying() ? 128 : 0) | i15);
                                    c0760l32 = C0748z.f947jd;
                                    if (!this.this$0.isPlaying()) {
                                        i14 = 0;
                                    }
                                    c0760l32.write(771, 9, i15 | i14);
                                }
                                view3 = this.this$0.mRoot;
                                Drawable drawable = ((ImageView) view3.findViewById(R$id.img_suspension_pp)).getDrawable();
                                if (!this.this$0.isPlaying()) {
                                    i8 = 0;
                                }
                                drawable.setLevel(i8);
                                interfaceC0708b13 = C0748z.f944dj;
                                interfaceC0708b13.mo1058c(this.this$0.isPlaying());
                                handler3 = this.this$0.mHandler;
                                handler3.removeMessages(65281);
                                handler4 = this.this$0.mHandler;
                                handler4.sendEmptyMessageDelayed(65281, 1000L);
                                return false;
                            case 65282:
                                c0760l33 = C0748z.f947jd;
                                if (c0760l33.mSource == 9) {
                                    this.this$0.m1215Re();
                                }
                                return false;
                            case 65283:
                                c0760l34 = C0748z.f947jd;
                                if (c0760l34.mSource == 9) {
                                    this.this$0.m1217Se();
                                }
                                return false;
                            case 65284:
                                service2 = this.this$0.getService();
                                if ((service2 & 143) == 9) {
                                    C0748z c0748z7 = this.this$0;
                                    unused = C0748z.f947jd;
                                    c0748z7.seekTo(C0760l.f991md);
                                    this.this$0.mo1158ma();
                                    C0748z.mMediaPlayer.setVisibility(0);
                                }
                                return false;
                            case 65285:
                                c0760l35 = C0748z.f947jd;
                                c0760l36 = C0748z.f947jd;
                                c0760l35.m1301a(c0760l36.f1001td, "/mnt/sdcard");
                                c0760l37 = C0748z.f947jd;
                                if (c0760l37.f1002ud != null) {
                                    this.this$0.m1220Ze();
                                }
                                interfaceC0708b15 = C0748z.f944dj;
                                c0760l38 = C0748z.f947jd;
                                interfaceC0708b15.mo1056a(c0760l38.f1002ud);
                                interfaceC0708b16 = C0748z.f944dj;
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
