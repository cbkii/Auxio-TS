package com.eckom.xtlibrary.p066b.p069f.p041d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0580g;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0636a;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0654s;
import com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0656a;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MusicModel.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.V */
/* loaded from: classes3.dex */
class C0602V implements Handler.Callback {
    final /* synthetic */ C0610ba this$0;

    C0602V(C0610ba c0610ba) {
        this.this$0 = c0610ba;
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0143, code lost:
    
        if (com.eckom.xtlibrary.p066b.p069f.p043f.C0654s.f710Jd != false) goto L28;
     */
    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleMessage(Message message) {
        int i;
        ArrayList arrayList;
        ArrayList arrayList2;
        C0654s c0654s;
        C0654s c0654s2;
        C0654s c0654s3;
        boolean isPlaying;
        C0654s c0654s4;
        boolean z;
        boolean z2;
        boolean z3;
        boolean isPlaying2;
        boolean isPlaying3;
        boolean isPlaying4;
        boolean isPlaying5;
        C0654s c0654s5;
        C0654s c0654s6;
        ArrayList arrayList3;
        C0654s c0654s7;
        C0654s c0654s8;
        C0654s c0654s9;
        Context context;
        C0654s c0654s10;
        boolean isPlaying6;
        C0654s c0654s11;
        int m630zb;
        boolean isPlaying7;
        C0654s c0654s12;
        C0654s c0654s13;
        C0654s c0654s14;
        C0654s c0654s15;
        C0654s c0654s16;
        C0654s c0654s17;
        boolean isPlaying8;
        C0654s c0654s18;
        C0654s c0654s19;
        C0654s c0654s20;
        ArrayList arrayList4;
        C0654s c0654s21;
        C0654s c0654s22;
        boolean isPlaying9;
        C0654s c0654s23;
        boolean isPlaying10;
        Context context2;
        C0654s c0654s24;
        C0654s c0654s25;
        C0654s c0654s26;
        C0654s c0654s27;
        C0654s c0654s28;
        C0654s c0654s29;
        ArrayList arrayList5;
        C0654s c0654s30;
        C0654s c0654s31;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean isPlaying11;
        C0654s c0654s32;
        boolean isPlaying12;
        C0654s c0654s33;
        int m630zb2;
        C0654s c0654s34;
        Context context3;
        ArrayList arrayList6;
        boolean isPlaying13;
        C0654s c0654s35;
        boolean isPlaying14;
        C0654s c0654s36;
        C0654s c0654s37;
        C0654s c0654s38;
        C0654s unused;
        C0654s unused2;
        C0654s unused3;
        C0654s unused4;
        C0654s unused5;
        C0654s unused6;
        C0654s unused7;
        C0654s unused8;
        C0654s unused9;
        C0654s unused10;
        C0654s unused11;
        C0654s unused12;
        C0654s unused13;
        C0654s unused14;
        C0654s unused15;
        C0654s unused16;
        try {
            boolean z7 = true;
            switch (message.what) {
                case 274:
                    if ((message.arg1 & 65536) != 65536) {
                        z7 = false;
                    }
                    arrayList = C0610ba.f615hi;
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        ((InterfaceC0656a) it.next()).mo735q(z7);
                    }
                    return false;
                case 514:
                    this.this$0.mHandler.removeMessages(65289);
                    this.this$0.mHandler.sendEmptyMessage(65289);
                    if (message.arg1 == 3 && message.arg2 == 1) {
                        this.this$0.f623Rh = true;
                    }
                    if (message.arg1 != 3 || message.arg2 != 0) {
                        return false;
                    }
                    this.this$0.f623Rh = false;
                    return false;
                case 515:
                    arrayList2 = C0610ba.f615hi;
                    Iterator it2 = arrayList2.iterator();
                    while (it2.hasNext()) {
                        ((InterfaceC0656a) it2.next()).mo729f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                    }
                    return false;
                case 769:
                    c0654s = C0610ba.f616jd;
                    c0654s.mSource = message.arg1 & 255;
                    c0654s2 = C0610ba.f616jd;
                    if (c0654s2.mSource == 9) {
                        unused16 = C0610ba.f616jd;
                        if (C0654s.f710Jd) {
                            this.this$0.m635Tb();
                            return false;
                        }
                    }
                    c0654s3 = C0610ba.f616jd;
                    if (c0654s3.mSource == 3) {
                        return false;
                    }
                    isPlaying = this.this$0.isPlaying();
                    if (!isPlaying) {
                        return false;
                    }
                    this.this$0.m636Ua();
                    return false;
                case 770:
                    c0654s4 = C0610ba.f616jd;
                    if (c0654s4.mSource != 3) {
                        return false;
                    }
                    int i2 = message.arg1;
                    Log.d("MusicModel", "BT_CALL STATE:" + i2);
                    if (i2 == 0) {
                        this.this$0.f620Cg = false;
                        z = this.this$0.f624Sh;
                        if (!z) {
                            return false;
                        }
                        z2 = this.this$0.f623Rh;
                        if (z2) {
                            return false;
                        }
                        this.this$0.mo529Va();
                        return false;
                    }
                    if (i2 != 1 && i2 != 2 && i2 != 3 && i2 != 4) {
                        return false;
                    }
                    z3 = this.this$0.f620Cg;
                    if (!z3) {
                        isPlaying2 = this.this$0.isPlaying();
                        if (isPlaying2) {
                            this.this$0.f624Sh = true;
                        } else {
                            this.this$0.f624Sh = false;
                        }
                    }
                    this.this$0.f620Cg = true;
                    this.this$0.m636Ua();
                    return false;
                case 1296:
                    try {
                        byte[] bArr = (byte[]) message.obj;
                        if (message.arg1 != 255) {
                            return false;
                        }
                        this.this$0.f621Eh = bArr[0] & 255;
                        StringBuilder sb = new StringBuilder();
                        sb.append("music XTL: ");
                        i = this.this$0.f621Eh;
                        sb.append(i);
                        Log.d("MusicModel", sb.toString());
                        return false;
                    } catch (Exception e) {
                        Log.e("MusicModel", "handleMessage: 0x0510:" + e.getMessage());
                        return false;
                    }
                case 40451:
                    switch (message.arg1) {
                        case 1:
                            isPlaying3 = this.this$0.isPlaying();
                            if (isPlaying3) {
                                this.this$0.m636Ua();
                                return false;
                            }
                            this.this$0.mo529Va();
                            return false;
                        case 2:
                            this.this$0.mHandler.removeMessages(65289);
                            this.this$0.mHandler.sendEmptyMessageDelayed(65289, 500L);
                            return false;
                        case 3:
                            this.this$0.mo539pb();
                            return false;
                        case 4:
                            this.this$0.mo542rb();
                            return false;
                        case 5:
                            isPlaying4 = this.this$0.isPlaying();
                            if (isPlaying4) {
                                return false;
                            }
                            this.this$0.mo529Va();
                            return false;
                        case 6:
                            isPlaying5 = this.this$0.isPlaying();
                            if (!isPlaying5) {
                                return false;
                            }
                            this.this$0.m636Ua();
                            return false;
                        case 7:
                            this.this$0.m591L(true);
                            return false;
                        case 8:
                            this.this$0.m591L(false);
                            return false;
                        case 9:
                            this.this$0.mo514Hb();
                            return false;
                        case 10:
                            this.this$0.mo515Ib();
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
                case 40454:
                    this.this$0.mHandler.removeMessages(40454);
                    this.this$0.m637Ub();
                    return false;
                case 40479:
                    String str = null;
                    int i3 = message.arg1;
                    if (i3 == 1) {
                        if (C0610ba.f614Gd) {
                            str = "/mnt/" + message.obj;
                        } else {
                            str = "/storage/" + message.obj;
                        }
                        if (message.arg2 == 0) {
                            c0654s6 = C0610ba.f616jd;
                            c0654s6.m786ra(str);
                        } else {
                            c0654s5 = C0610ba.f616jd;
                            c0654s5.m784pa(str);
                        }
                    } else if (i3 == 2) {
                        if (C0610ba.f614Gd) {
                            str = "/mnt/usbhost/" + message.obj;
                        } else {
                            str = "/storage/" + message.obj;
                        }
                        if (message.arg2 == 0) {
                            c0654s14 = C0610ba.f616jd;
                            c0654s14.m787sa(str);
                        } else {
                            c0654s13 = C0610ba.f616jd;
                            c0654s13.m785qa(str);
                        }
                    } else if (i3 == 3) {
                        str = "/mnt/sdcard/iNand";
                        if (message.arg2 == 0) {
                            c0654s17 = C0610ba.f616jd;
                            c0654s17.f725td.m453wc();
                        } else {
                            c0654s15 = C0610ba.f616jd;
                            c0654s16 = C0610ba.f616jd;
                            c0654s15.m780b(c0654s16.f725td, "/mnt/sdcard/iNand");
                        }
                    }
                    unused9 = C0610ba.f616jd;
                    if (C0654s.f703Cd != null) {
                        unused10 = C0610ba.f616jd;
                        if (C0654s.f703Cd.startsWith(str)) {
                            if (message.arg2 == 0) {
                                unused11 = C0610ba.f616jd;
                                C0654s.f704Dd.m453wc();
                                this.this$0.m635Tb();
                            } else {
                                c0654s9 = C0610ba.f616jd;
                                context = this.this$0.mContext;
                                unused12 = C0610ba.f616jd;
                                C0580g c0580g = C0654s.f704Dd;
                                unused13 = C0610ba.f616jd;
                                c0654s9.m778a(context, c0580g, C0654s.f703Cd);
                                c0654s10 = C0610ba.f616jd;
                                unused14 = C0610ba.f616jd;
                                c0654s10.m783ea(C0654s.f701Ad);
                                isPlaying6 = this.this$0.isPlaying();
                                if (!isPlaying6) {
                                    c0654s11 = C0610ba.f616jd;
                                    if (c0654s11.getService() == 3) {
                                        C0610ba c0610ba = this.this$0;
                                        unused15 = C0610ba.f616jd;
                                        m630zb = c0610ba.m630zb(C0654s.f702Bd);
                                        if (m630zb == 0) {
                                            isPlaying7 = this.this$0.isPlaying();
                                            if (!isPlaying7) {
                                                C0610ba c0610ba2 = this.this$0;
                                                c0654s12 = C0610ba.f616jd;
                                                c0610ba2.seekTo(c0654s12.f719md);
                                                this.this$0.mo529Va();
                                            }
                                        }
                                        this.this$0.m591L(false);
                                    }
                                }
                            }
                        }
                    }
                    arrayList3 = C0610ba.f615hi;
                    Iterator it3 = arrayList3.iterator();
                    while (it3.hasNext()) {
                        InterfaceC0656a interfaceC0656a = (InterfaceC0656a) it3.next();
                        c0654s7 = C0610ba.f616jd;
                        if (c0654s7.f726ud != null) {
                            c0654s8 = C0610ba.f616jd;
                            interfaceC0656a.mo721a(c0654s8.f726ud);
                        }
                        interfaceC0656a.mo720a(this.this$0.m632Mb());
                    }
                    return false;
                case 65281:
                    isPlaying8 = this.this$0.isPlaying();
                    if (isPlaying8) {
                        int duration = this.this$0.m632Mb().getDuration();
                        int currentPosition = this.this$0.m632Mb().getCurrentPosition();
                        c0654s18 = C0610ba.f616jd;
                        c0654s18.f719md = currentPosition;
                        c0654s19 = C0610ba.f616jd;
                        c0654s19.mDuration = duration;
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
                        int i6 = i5 / 60;
                        int i7 = i4 % 60;
                        int i8 = i5 % 60;
                        int i9 = i6 % 24;
                        c0654s20 = C0610ba.f616jd;
                        c0654s20.f719md = currentPosition;
                        arrayList4 = C0610ba.f615hi;
                        Iterator it4 = arrayList4.iterator();
                        while (it4.hasNext()) {
                            InterfaceC0656a interfaceC0656a2 = (InterfaceC0656a) it4.next();
                            c0654s24 = C0610ba.f616jd;
                            interfaceC0656a2.mo728d(c0654s24.f719md, duration);
                        }
                        int i10 = (currentPosition * 100) / duration;
                        c0654s21 = C0610ba.f616jd;
                        unused7 = C0610ba.f616jd;
                        int i11 = C0654s.f701Ad + 1;
                        unused8 = C0610ba.f616jd;
                        c0654s21.m779b(1, i11, C0654s.f704Dd.f545kk, (i8 << 8) | (i9 << 16) | i7, i10);
                        c0654s22 = C0610ba.f616jd;
                        isPlaying9 = this.this$0.isPlaying();
                        int i12 = i10 & 127;
                        c0654s22.write(40704, 3, (isPlaying9 ? 128 : 0) | i12);
                        c0654s23 = C0610ba.f616jd;
                        isPlaying10 = this.this$0.isPlaying();
                        c0654s23.write(771, 3, i12 | (isPlaying10 ? 128 : 0));
                        Intent intent = new Intent("com.tw.launcher.music_progress_duration");
                        intent.putExtra("msg_music_progress", currentPosition);
                        intent.putExtra("msg_music_duration", duration);
                        context2 = this.this$0.mContext;
                        context2.sendBroadcast(intent);
                    }
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                    return false;
                case 65282:
                    c0654s25 = C0610ba.f616jd;
                    if (c0654s25.mSource != 3) {
                        return false;
                    }
                    this.this$0.m593Re();
                    return false;
                case 65283:
                    c0654s26 = C0610ba.f616jd;
                    if (c0654s26.mSource != 3) {
                        return false;
                    }
                    this.this$0.m595Se();
                    return false;
                case 65285:
                    c0654s27 = C0610ba.f616jd;
                    c0654s28 = C0610ba.f616jd;
                    c0654s27.m780b(c0654s28.f725td, "/mnt/sdcard");
                    c0654s29 = C0610ba.f616jd;
                    if (c0654s29.f726ud != null) {
                        this.this$0.m599Ze();
                    }
                    arrayList5 = C0610ba.f615hi;
                    Iterator it5 = arrayList5.iterator();
                    while (it5.hasNext()) {
                        InterfaceC0656a interfaceC0656a3 = (InterfaceC0656a) it5.next();
                        c0654s30 = C0610ba.f616jd;
                        if (c0654s30.f726ud != null) {
                            c0654s31 = C0610ba.f616jd;
                            interfaceC0656a3.mo721a(c0654s31.f726ud);
                            interfaceC0656a3.mo719L();
                        }
                    }
                    return false;
                case 65287:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("RESUME:activityResume:activityResume:");
                    z4 = this.this$0.f630wg;
                    sb2.append(z4);
                    Log.d("MusicModel", sb2.toString());
                    z5 = this.this$0.f630wg;
                    if (!z5) {
                        return false;
                    }
                    z6 = this.this$0.f632xi;
                    if (z6) {
                        this.this$0.m638Wb();
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("RESUME:isPlaying:");
                    isPlaying11 = this.this$0.isPlaying();
                    sb3.append(isPlaying11);
                    sb3.append(" mTW.mSource:");
                    c0654s32 = C0610ba.f616jd;
                    sb3.append(c0654s32.mSource);
                    sb3.append(" isInitiativePause:");
                    sb3.append(this.this$0.f625Th);
                    Log.d("MusicModel", sb3.toString());
                    isPlaying12 = this.this$0.isPlaying();
                    if (!isPlaying12) {
                        c0654s33 = C0610ba.f616jd;
                        if (c0654s33.mSource == 3) {
                            if (this.this$0.f625Th) {
                                unused5 = C0610ba.f616jd;
                                break;
                            }
                            C0610ba c0610ba3 = this.this$0;
                            unused6 = C0610ba.f616jd;
                            m630zb2 = c0610ba3.m630zb(C0654s.f702Bd);
                            if (m630zb2 == 0) {
                                C0610ba c0610ba4 = this.this$0;
                                c0654s34 = C0610ba.f616jd;
                                c0610ba4.seekTo(c0654s34.f719md);
                                Log.w("MusicModel", "playMusic()--5:");
                                this.this$0.mo529Va();
                            }
                        }
                    }
                    this.this$0.mHandler.sendEmptyMessage(40454);
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                    return false;
                case 65288:
                    context3 = this.this$0.mContext;
                    unused4 = C0610ba.f616jd;
                    C0636a.m744b(context3, C0654s.f712Tc);
                    return false;
                case 65289:
                    this.this$0.mHandler.removeMessages(65289);
                    this.this$0.m594Sa();
                    return false;
                case 65296:
                    this.this$0.mo539pb();
                    arrayList6 = C0610ba.f615hi;
                    Iterator it6 = arrayList6.iterator();
                    while (it6.hasNext()) {
                        InterfaceC0656a interfaceC0656a4 = (InterfaceC0656a) it6.next();
                        isPlaying13 = this.this$0.isPlaying();
                        if (!isPlaying13) {
                            unused = C0610ba.f616jd;
                            String str2 = C0654s.f702Bd;
                            unused2 = C0610ba.f616jd;
                            String str3 = C0654s.f703Cd;
                            unused3 = C0610ba.f616jd;
                            int i13 = C0654s.f701Ad;
                            c0654s35 = C0610ba.f616jd;
                            interfaceC0656a4.mo725b("", "", "", null, str2, str3, c0654s35.f726ud.f543ik + i13);
                            isPlaying14 = this.this$0.isPlaying();
                            interfaceC0656a4.mo727c(isPlaying14);
                            interfaceC0656a4.mo728d(0, 0);
                        }
                    }
                    return false;
                case 65297:
                    this.this$0.m632Mb().stop();
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.m632Mb().reset();
                    c0654s36 = C0610ba.f616jd;
                    c0654s36.f720nd = "";
                    c0654s37 = C0610ba.f616jd;
                    c0654s37.f721od = "";
                    c0654s38 = C0610ba.f616jd;
                    c0654s38.f722pd = "";
                    this.this$0.m598Xe();
                    return false;
                default:
                    return false;
            }
        } catch (Exception e2) {
            Log.i("MusicModel", "" + e2.toString());
            return false;
        }
        Log.i("MusicModel", "" + e2.toString());
        return false;
    }
}
