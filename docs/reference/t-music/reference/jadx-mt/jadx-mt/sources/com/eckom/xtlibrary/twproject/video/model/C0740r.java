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

/* JADX INFO: renamed from: com.eckom.xtlibrary.twproject.video.model.r */
/* JADX INFO: compiled from: VideoModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0740r implements Handler.Callback {
    final /* synthetic */ C0748z this$0;

    C0740r(C0748z c0748z) {
        this.this$0 = c0748z;
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
            Log.i("VideoModel", "" + e.toString());
        }
        switch (i) {
            case 267:
                byte[] bArr = (byte[]) message.obj;
                C0748z.f947jd.f994Md = bArr[1];
                C0748z.f944dj.mo1055Y(bArr[1]);
                C0748z.f944dj.mo1066l(C0748z.f947jd.f993Ld || C0748z.f947jd.f994Md == 0);
                C0748z c0748z = this.this$0;
                if (!C0748z.f947jd.f993Ld && C0748z.f947jd.f994Md != 0) {
                    z = false;
                }
                c0748z.m1214N(z);
                return false;
            case 274:
                if ((message.arg1 & 65536) != 65536) {
                    z2 = false;
                }
                C0748z.f944dj.mo1069q(z2);
                return false;
            case 517:
                C0748z.f947jd.f993Ld = message.arg1 == 1;
                C0748z.f944dj.mo1066l(C0748z.f947jd.f993Ld || C0748z.f947jd.f994Md == 0);
                C0748z c0748z2 = this.this$0;
                if (!C0748z.f947jd.f993Ld && C0748z.f947jd.f994Md != 0) {
                    z3 = false;
                }
                c0748z2.m1214N(z3);
                return false;
            case 769:
                if (message.arg1 != 9) {
                    if (this.this$0.isPlaying()) {
                        this.this$0.mo1154P();
                    }
                    this.this$0.m1261F(false);
                    this.this$0.m1260E(false);
                }
                if (C0748z.f947jd.mSource != message.arg1) {
                    C0748z.f947jd.mSource = message.arg1;
                    C0748z.f944dj.setSource(C0748z.f947jd.mSource);
                }
                return false;
            case 40454:
                this.this$0.mHandler.removeMessages(40454);
                this.this$0.m1265oc();
                return false;
            case 40457:
                C0748z.f944dj.mo1070u(message.arg1);
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
                        this.this$0.mHandler.removeMessages(65284);
                        if (this.this$0.isPlaying()) {
                            this.this$0.mo1154P();
                        }
                        if (this.this$0.f951Oi) {
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
                InterfaceC0708b interfaceC0708b = C0748z.f944dj;
                if (message.arg1 == 0) {
                    z4 = false;
                }
                interfaceC0708b.mo1062g(z4);
                return false;
            case 40479:
                String str = null;
                int i3 = message.arg1;
                if (i3 == 1) {
                    C0760l unused = C0748z.f947jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        C0748z.f947jd.m1306ra(str);
                    } else {
                        C0748z.f947jd.m1304pa(str);
                    }
                } else if (i3 == 2) {
                    C0760l unused2 = C0748z.f947jd;
                    if (C0760l.f984Qd) {
                        str = "/mnt/" + message.obj;
                    } else {
                        str = "/storage/" + message.obj;
                    }
                    if (message.arg2 == 0) {
                        C0748z.f947jd.m1307sa(str);
                    } else {
                        C0748z.f947jd.m1305qa(str);
                    }
                } else if (i3 == 3) {
                    str = "/mnt/sdcard/iNand";
                    if (message.arg2 == 0) {
                        C0748z.f947jd.f1001td.m1050wc();
                    } else {
                        C0748z.f947jd.m1301a(C0748z.f947jd.f1001td, "/mnt/sdcard/iNand");
                    }
                }
                C0760l unused3 = C0748z.f947jd;
                if (C0760l.f982Cd != null && str != null) {
                    C0760l unused4 = C0748z.f947jd;
                    if (C0760l.f982Cd.startsWith(str)) {
                        if (message.arg2 == 0) {
                            C0748z.f947jd.f992Dd.m1050wc();
                            this.this$0.stop();
                            this.this$0.m1261F(false);
                            this.this$0.m1260E(false);
                        } else {
                            if (C0686b.m1006_c()) {
                                this.this$0.m1228bf();
                                C0748z.f944dj.onMediaView(C0748z.mMediaPlayer);
                            }
                            C0760l c0760l = C0748z.f947jd;
                            Context context = this.this$0.mContext;
                            C0705b c0705b = C0748z.f947jd.f992Dd;
                            C0760l unused5 = C0748z.f947jd;
                            c0760l.m1300a(context, c0705b, C0760l.f982Cd);
                            if (this.this$0.getService() == 9 && !this.this$0.isPlaying()) {
                                C0748z c0748z3 = this.this$0;
                                C0760l unused6 = C0748z.f947jd;
                                c0748z3.m1230c(C0760l.f991md, false);
                                this.this$0.mo1158ma();
                            }
                        }
                    }
                }
                C0748z.f944dj.mo1056a(C0748z.f947jd.f1002ud);
                C0748z.f944dj.mo1057b(C0748z.f947jd.f992Dd);
                return false;
            case 40732:
                if (this.this$0.f972wi != message.arg1) {
                    this.this$0.f972wi = message.arg1;
                    C0748z.f944dj.mo1072v(this.this$0.f972wi);
                    if (this.this$0.f972wi != 0 && this.this$0.f951Oi) {
                        this.this$0.f952Pi = true;
                        this.this$0.m1260E(false);
                    } else if (this.this$0.f952Pi) {
                        this.this$0.f952Pi = false;
                        this.this$0.m1260E(true);
                    }
                }
                return false;
            case 65288:
                if (this.this$0.layout_suspension.getVisibility() == 0) {
                    this.this$0.layout_suspension.setVisibility(8);
                }
                return false;
            case 65294:
                if (C0748z.f947jd.mSource == 9) {
                    C0748z c0748z4 = this.this$0;
                    C0760l unused7 = C0748z.f947jd;
                    c0748z4.seekTo(C0760l.f991md);
                    this.this$0.mo1158ma();
                }
                return false;
            case 65297:
                this.this$0.f950Ni = Settings.System.getInt(this.this$0.mContext.getContentResolver(), "SYSTEM_FLOATVIDEO", 0) == 1;
                this.this$0.m1262lc();
                Log.d("VideoModel", "0xff11:floatVideo:" + this.this$0.f950Ni + " playing:" + this.this$0.f958Ui + " inOnclickHome:" + this.this$0.f949Mi + " mReverse:" + this.this$0.f972wi + " showPipView:" + this.this$0.f955Ri + " MultiWindowMode:" + C0748z.f947jd.m1299Ta());
                if (this.this$0.f950Ni && this.this$0.f958Ui && this.this$0.f949Mi && this.this$0.f972wi == 0 && !this.this$0.f955Ri) {
                    this.this$0.m1260E(true);
                } else if (C0748z.f947jd.m1299Ta() || !this.this$0.f958Ui || this.this$0.f972wi != 0 || this.this$0.f955Ri) {
                    this.this$0.m1261F(false);
                    this.this$0.m1260E(false);
                } else {
                    this.this$0.m1261F(true);
                }
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
                        InterfaceC0708b interfaceC0708b2 = C0748z.f944dj;
                        if ((message.arg1 & Integer.MIN_VALUE) != Integer.MIN_VALUE) {
                            z5 = false;
                        }
                        interfaceC0708b2.mo1060f(z5);
                        return false;
                    default:
                        switch (i) {
                            case 65281:
                                if (this.this$0.isPlaying()) {
                                    int duration = C0748z.mMediaPlayer.getDuration();
                                    int currentPosition = C0748z.mMediaPlayer.getCurrentPosition();
                                    C0760l unused8 = C0748z.f947jd;
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
                                    C0760l unused9 = C0748z.f947jd;
                                    C0760l.f991md = currentPosition;
                                    InterfaceC0708b interfaceC0708b3 = C0748z.f944dj;
                                    C0760l unused10 = C0748z.f947jd;
                                    interfaceC0708b3.mo1059d(C0760l.f991md, duration);
                                    int i6 = (currentPosition * 100) / duration;
                                    C0760l c0760l2 = C0748z.f947jd;
                                    C0760l unused11 = C0748z.f947jd;
                                    c0760l2.m1302b(1, C0760l.f980Ad + 1, C0748z.f947jd.f992Dd.f851kk, (((i5 / 60) % 24) << 16) | ((i5 % 60) << 8) | (i4 % 60), i6);
                                    int i7 = 128;
                                    int i8 = i6 & 127;
                                    C0748z.f947jd.write(40704, 9, (this.this$0.isPlaying() ? 128 : 0) | i8);
                                    C0760l c0760l3 = C0748z.f947jd;
                                    if (!this.this$0.isPlaying()) {
                                        i7 = 0;
                                    }
                                    c0760l3.write(771, 9, i8 | i7);
                                }
                                Drawable drawable = ((ImageView) this.this$0.mRoot.findViewById(R$id.img_suspension_pp)).getDrawable();
                                if (!this.this$0.isPlaying()) {
                                    i2 = 0;
                                }
                                drawable.setLevel(i2);
                                C0748z.f944dj.mo1058c(this.this$0.isPlaying());
                                this.this$0.mHandler.removeMessages(65281);
                                this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                                return false;
                            case 65282:
                                if (C0748z.f947jd.mSource == 9) {
                                    this.this$0.m1215Re();
                                }
                                return false;
                            case 65283:
                                if (C0748z.f947jd.mSource == 9) {
                                    this.this$0.m1217Se();
                                }
                                return false;
                            case 65284:
                                if ((this.this$0.getService() & 143) == 9) {
                                    C0748z c0748z5 = this.this$0;
                                    C0760l unused12 = C0748z.f947jd;
                                    c0748z5.seekTo(C0760l.f991md);
                                    this.this$0.mo1158ma();
                                    C0748z.mMediaPlayer.setVisibility(0);
                                }
                                return false;
                            case 65285:
                                C0748z.f947jd.m1301a(C0748z.f947jd.f1001td, "/mnt/sdcard");
                                if (C0748z.f947jd.f1002ud != null) {
                                    this.this$0.m1220Ze();
                                }
                                C0748z.f944dj.mo1056a(C0748z.f947jd.f1002ud);
                                C0748z.f944dj.mo1052L();
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
