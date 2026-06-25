package com.eckom.xtlibrary.twproject.video.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.InputDeviceCompat;
import android.util.Log;
import android.widget.ImageView;
import com.eckom.xtlibrary.R$id;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.p020b.p054k.p055a.C0705b;
import com.eckom.xtlibrary.p020b.p054k.p057c.InterfaceC0708b;
import com.eckom.xtlibrary.twproject.video.utils.C0760l;

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.f */
/* JADX INFO: compiled from: VideoIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0728f implements Handler.Callback {
    final /* synthetic */ C0735m this$0;

    C0728f(C0735m c0735m) {
        this.this$0 = c0735m;
    }

    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    public boolean handleMessage(Message message) throws Throwable {
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        int i2;
        try {
            i = message.what;
            z = true;
            i2 = 1;
            z5 = true;
            z4 = true;
            z3 = true;
            z3 = true;
            z2 = true;
            z = true;
        } catch (Exception e) {
            Log.i("VideoIjkModel", "" + e.toString());
        }
        switch (i) {
            case 267:
                byte[] bArr = (byte[]) message.obj;
                C0735m.f915jd.f994Md = bArr[1];
                C0735m.f912dj.mo1055Y(bArr[1]);
                C0735m.f912dj.mo1066l(C0735m.f915jd.f993Ld || C0735m.f915jd.f994Md == 0);
                C0735m c0735m = this.this$0;
                if (!C0735m.f915jd.f993Ld && C0735m.f915jd.f994Md != 0) {
                    z = false;
                }
                c0735m.m1161N(z);
                return false;
            case 274:
                if ((message.arg1 & 65536) != 65536) {
                    z2 = false;
                }
                C0735m.f912dj.mo1069q(z2);
                return false;
            case 517:
                C0735m.f915jd.f993Ld = message.arg1 == 1;
                C0735m.f912dj.mo1066l(C0735m.f915jd.f993Ld || C0735m.f915jd.f994Md == 0);
                C0735m c0735m2 = this.this$0;
                if (!C0735m.f915jd.f993Ld && C0735m.f915jd.f994Md != 0) {
                    z3 = false;
                }
                c0735m2.m1161N(z3);
                return false;
            case 769:
                if (message.arg1 != 9) {
                    if (this.this$0.isPlaying()) {
                        this.this$0.mo1154P();
                    }
                    this.this$0.m1208F(false);
                    this.this$0.m1207E(false);
                }
                if (C0735m.f915jd.mSource != message.arg1) {
                    C0735m.f915jd.mSource = message.arg1;
                    C0735m.f912dj.setSource(C0735m.f915jd.mSource);
                }
                return false;
            case 40454:
                this.this$0.mHandler.removeMessages(40454);
                this.this$0.m1212oc();
                return false;
            case 40457:
                C0735m.f912dj.mo1070u(message.arg1);
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
                        this.this$0.mHandler.removeMessages(65284);
                        if (this.this$0.isPlaying()) {
                            this.this$0.mo1154P();
                        }
                        if (this.this$0.f919Oi) {
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
                InterfaceC0708b interfaceC0708b = C0735m.f912dj;
                if (message.arg1 == 0) {
                    z4 = false;
                }
                interfaceC0708b.mo1062g(z4);
                return false;
            case 40479:
                String str = null;
                int i3 = message.arg1;
                if (i3 == 1) {
                    C0760l unused = C0735m.f915jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        C0735m.f915jd.m1306ra(str);
                    } else {
                        C0735m.f915jd.m1304pa(str);
                    }
                } else if (i3 == 2) {
                    C0760l unused2 = C0735m.f915jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        C0735m.f915jd.m1307sa(str);
                    } else {
                        C0735m.f915jd.m1305qa(str);
                    }
                } else if (i3 == 3) {
                    str = "/mnt/sdcard/iNand";
                    if (message.arg2 == 0) {
                        C0735m.f915jd.f1001td.m1050wc();
                    } else {
                        C0735m.f915jd.m1301a(C0735m.f915jd.f1001td, "/mnt/sdcard/iNand");
                    }
                }
                C0760l unused3 = C0735m.f915jd;
                if (C0760l.f982Cd != null && str != null) {
                    C0760l unused4 = C0735m.f915jd;
                    if (C0760l.f982Cd.startsWith(str)) {
                        if (message.arg2 == 0) {
                            C0735m.f915jd.f992Dd.m1050wc();
                            this.this$0.stop();
                            this.this$0.m1208F(false);
                        } else {
                            if (C0686b.m1006_c()) {
                                this.this$0.m1175bf();
                                C0735m.f912dj.onMediaView(this.this$0.mMediaPlayer);
                            }
                            C0760l c0760l = C0735m.f915jd;
                            Context context = this.this$0.mContext;
                            C0705b c0705b = C0735m.f915jd.f992Dd;
                            C0760l unused5 = C0735m.f915jd;
                            c0760l.m1300a(context, c0705b, C0760l.f982Cd);
                            if (this.this$0.getService() == 9 && !this.this$0.isPlaying()) {
                                C0735m c0735m3 = this.this$0;
                                C0760l unused6 = C0735m.f915jd;
                                c0735m3.m1177c(C0760l.f991md, false);
                                this.this$0.mo1158ma();
                            }
                        }
                    }
                }
                C0735m.f912dj.mo1056a(C0735m.f915jd.f1002ud);
                C0735m.f912dj.mo1057b(C0735m.f915jd.f992Dd);
                return false;
            case 40732:
                if (this.this$0.f940wi != message.arg1) {
                    this.this$0.f940wi = message.arg1;
                    C0735m.f912dj.mo1072v(this.this$0.f940wi);
                    if (this.this$0.f940wi != 0 && this.this$0.f919Oi) {
                        this.this$0.f920Pi = true;
                        this.this$0.m1207E(false);
                    } else if (this.this$0.f920Pi) {
                        this.this$0.f920Pi = false;
                        this.this$0.m1207E(true);
                    }
                }
                return false;
            case 65288:
                if (this.this$0.layout_suspension.getVisibility() == 0) {
                    this.this$0.layout_suspension.setVisibility(8);
                }
                return false;
            case 65294:
                if (C0735m.f915jd.mSource == 9) {
                    C0735m c0735m4 = this.this$0;
                    C0760l unused7 = C0735m.f915jd;
                    c0735m4.seekTo(C0760l.f991md);
                    this.this$0.mo1158ma();
                }
                return false;
            case 65297:
                this.this$0.f918Ni = Settings.System.getInt(this.this$0.mContext.getContentResolver(), "SYSTEM_FLOATVIDEO", 0) == 1;
                this.this$0.m1209lc();
                Log.d("VideoIjkModel", "0xff11:floatVideo:" + this.this$0.f918Ni + " playing:" + this.this$0.f926Ui + " inOnclickHome:" + this.this$0.f917Mi + " mReverse:" + this.this$0.f940wi + " showPipView:" + this.this$0.f923Ri + " MultiWindowMode:" + C0735m.f915jd.m1299Ta());
                if (this.this$0.f918Ni && this.this$0.f926Ui && this.this$0.f917Mi && this.this$0.f940wi == 0 && !this.this$0.f923Ri) {
                    this.this$0.m1207E(true);
                } else if (C0735m.f915jd.m1299Ta() || !this.this$0.f926Ui || this.this$0.f940wi != 0 || this.this$0.f923Ri) {
                    this.this$0.m1208F(false);
                    this.this$0.m1207E(false);
                } else {
                    this.this$0.m1208F(true);
                }
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
                        InterfaceC0708b interfaceC0708b2 = C0735m.f912dj;
                        if ((message.arg1 & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                            z5 = false;
                        }
                        interfaceC0708b2.mo1060f(z5);
                        return false;
                    default:
                        switch (i) {
                            case 65281:
                                int duration = this.this$0.mMediaPlayer.getDuration();
                                int currentPosition = this.this$0.mMediaPlayer.getCurrentPosition();
                                C0760l unused8 = C0735m.f915jd;
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
                                int i4 = currentPosition / 1000;
                                int i5 = i4 / 60;
                                C0760l unused9 = C0735m.f915jd;
                                C0760l.f991md = currentPosition;
                                InterfaceC0708b interfaceC0708b3 = C0735m.f912dj;
                                C0760l unused10 = C0735m.f915jd;
                                interfaceC0708b3.mo1059d(C0760l.f991md, duration);
                                int i6 = (currentPosition * 100) / duration;
                                C0760l c0760l2 = C0735m.f915jd;
                                C0760l unused11 = C0735m.f915jd;
                                c0760l2.m1302b(1, C0760l.f980Ad + 1, C0735m.f915jd.f992Dd.f851kk, (((i5 / 60) % 24) << 16) | ((i5 % 60) << 8) | (i4 % 60), i6);
                                int i7 = 128;
                                int i8 = i6 & 127;
                                C0735m.f915jd.write(40704, 9, (this.this$0.isPlaying() ? 128 : 0) | i8);
                                C0760l c0760l3 = C0735m.f915jd;
                                if (!this.this$0.isPlaying()) {
                                    i7 = 0;
                                }
                                c0760l3.write(771, 9, i8 | i7);
                                Drawable drawable = ((ImageView) this.this$0.mRoot.findViewById(R$id.img_suspension_pp)).getDrawable();
                                if (!this.this$0.isPlaying()) {
                                    i2 = 0;
                                }
                                drawable.setLevel(i2);
                                C0735m.f912dj.mo1058c(this.this$0.isPlaying());
                                this.this$0.mHandler.removeMessages(65281);
                                this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                                return false;
                            case 65282:
                                if (C0735m.f915jd.mSource == 9) {
                                    this.this$0.m1162Re();
                                }
                                return false;
                            case 65283:
                                if (C0735m.f915jd.mSource == 9) {
                                    this.this$0.m1164Se();
                                }
                                return false;
                            case 65284:
                                if ((this.this$0.getService() & 143) == 9) {
                                    C0735m c0735m5 = this.this$0;
                                    C0760l unused12 = C0735m.f915jd;
                                    c0735m5.seekTo(C0760l.f991md);
                                    this.this$0.mo1158ma();
                                    this.this$0.mMediaPlayer.setVisibility(0);
                                }
                                return false;
                            case 65285:
                                C0735m.f915jd.m1301a(C0735m.f915jd.f1001td, "/mnt/sdcard");
                                if (C0735m.f915jd.f1002ud != null) {
                                    this.this$0.m1167Ze();
                                }
                                C0735m.f912dj.mo1056a(C0735m.f915jd.f1002ud);
                                C0735m.f912dj.mo1052L();
                                return false;
                            case 65286:
                                if (this.this$0.layout_suspension.getVisibility() == 8) {
                                    this.this$0.layout_suspension.setVisibility(0);
                                    if (this.this$0.mHandler.hasMessages(65288)) {
                                        this.this$0.mHandler.removeMessages(65288);
                                    }
                                    this.this$0.mHandler.sendEmptyMessageDelayed(65288, 4000L);
                                } else {
                                    this.this$0.layout_suspension.setVisibility(8);
                                }
                                if (this.this$0.mHandler.hasMessages(65286)) {
                                    this.this$0.mHandler.removeMessages(65286);
                                }
                                return false;
                            default:
                                return false;
                        }
                }
        }
    }
}
