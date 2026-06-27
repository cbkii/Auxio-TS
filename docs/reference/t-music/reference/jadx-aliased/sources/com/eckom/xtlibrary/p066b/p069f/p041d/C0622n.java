package com.eckom.xtlibrary.p066b.p069f.p041d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0578e;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0579f;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0580g;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0643h;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0655t;
import com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0656a;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: MusicID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.n */
/* loaded from: classes3.dex */
class C0622n implements Handler.Callback {
    final /* synthetic */ C0628t this$0;

    C0622n(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    public boolean handleMessage(Message message) {
        boolean z;
        int i;
        ArrayList arrayList;
        Handler handler;
        Handler handler2;
        ArrayList arrayList2;
        boolean isPlaying;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean isPlaying2;
        boolean isPlaying3;
        Handler handler3;
        Handler handler4;
        boolean isPlaying4;
        boolean isPlaying5;
        Context context;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean isPlaying6;
        Handler handler5;
        Handler handler6;
        ArrayList arrayList3;
        C0655t c0655t;
        C0655t c0655t2;
        boolean isPlaying7;
        C0655t c0655t3;
        boolean isPlaying8;
        Context context2;
        boolean z9;
        boolean isPlaying9;
        Handler handler7;
        Handler handler8;
        Handler handler9;
        Handler handler10;
        int m686zb;
        Handler handler11;
        C0655t c0655t4;
        ArrayList arrayList4;
        boolean isPlaying10;
        boolean isPlaying11;
        Handler handler12;
        try {
            z = true;
        } catch (Exception e) {
            Log.i("MusicID3Model", "Exception:" + e);
        }
        switch (message.what) {
            case 274:
                if ((message.arg1 & 65536) != 65536) {
                    z = false;
                }
                arrayList = C0628t.f637hi;
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    ((InterfaceC0656a) it.next()).mo735q(z);
                }
                return false;
            case 514:
                handler = this.this$0.mHandler;
                handler.removeMessages(65289);
                handler2 = this.this$0.mHandler;
                handler2.sendEmptyMessage(65289);
                if (message.arg1 == 3 && message.arg2 == 1) {
                    this.this$0.f652Rh = true;
                }
                if (message.arg1 == 3 && message.arg2 == 0) {
                    this.this$0.f652Rh = false;
                }
                return false;
            case 515:
                arrayList2 = C0628t.f637hi;
                Iterator it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    ((InterfaceC0656a) it2.next()).mo729f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                }
                return false;
            case 769:
                this.this$0.f659Yc.mSource = message.arg1 & 255;
                if (this.this$0.f659Yc.mSource != 3) {
                    isPlaying = this.this$0.isPlaying();
                    if (isPlaying) {
                        this.this$0.m700Ua();
                    }
                }
                return false;
            case 770:
                if (this.this$0.f659Yc.mSource == 3) {
                    int i2 = message.arg1;
                    Log.d("MusicID3Model", "BT_CALL STATE:" + i2);
                    if (i2 == 0) {
                        this.this$0.f640Cg = false;
                        z2 = this.this$0.f653Sh;
                        if (z2) {
                            z3 = this.this$0.f652Rh;
                            if (!z3) {
                                this.this$0.mo529Va();
                            }
                        }
                    } else if (i2 == 1 || i2 == 2 || i2 == 3 || i2 == 4) {
                        z4 = this.this$0.f640Cg;
                        if (!z4) {
                            C0628t c0628t = this.this$0;
                            isPlaying2 = this.this$0.isPlaying();
                            c0628t.f653Sh = isPlaying2;
                        }
                        this.this$0.f640Cg = true;
                        this.this$0.m700Ua();
                    }
                }
                return false;
            case 1296:
                try {
                    byte[] bArr = (byte[]) message.obj;
                    if (message.arg1 == 255) {
                        this.this$0.f641Eh = bArr[0] & 255;
                        StringBuilder sb = new StringBuilder();
                        sb.append("music XTL: ");
                        i = this.this$0.f641Eh;
                        sb.append(i);
                        Log.d("MusicID3Model", sb.toString());
                    }
                } catch (Exception e2) {
                    Log.e("MusicID3Model", "handleMessage: 0x0510:" + e2.getMessage());
                }
                return false;
            case 40451:
                switch (message.arg1) {
                    case 1:
                        isPlaying3 = this.this$0.isPlaying();
                        if (isPlaying3) {
                            this.this$0.m700Ua();
                        } else {
                            this.this$0.mo529Va();
                        }
                        return false;
                    case 2:
                        handler3 = this.this$0.mHandler;
                        handler3.removeMessages(65289);
                        handler4 = this.this$0.mHandler;
                        handler4.sendEmptyMessageDelayed(65289, 500L);
                        return false;
                    case 3:
                        this.this$0.mo539pb();
                        return false;
                    case 4:
                        this.this$0.mo542rb();
                        return false;
                    case 5:
                        isPlaying4 = this.this$0.isPlaying();
                        if (!isPlaying4) {
                            this.this$0.mo529Va();
                        }
                        return false;
                    case 6:
                        isPlaying5 = this.this$0.isPlaying();
                        if (isPlaying5) {
                            this.this$0.m700Ua();
                        }
                        return false;
                    case 7:
                        this.this$0.m644L(true);
                        return false;
                    case 8:
                        this.this$0.m644L(false);
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
                this.this$0.m701Ub();
                return false;
            case 40479:
                String str = null;
                int i3 = message.arg1;
                if (i3 == 1) {
                    str = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m705ra(str);
                    } else {
                        this.this$0.m703pa(str);
                        this.this$0.m687Fa(str);
                    }
                } else if (i3 == 2) {
                    str = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m706sa(str);
                    } else {
                        this.this$0.m704qa(str);
                        this.this$0.m687Fa(str);
                    }
                } else if (i3 == 3) {
                    str = "/mnt/sdcard/iNand";
                    if (message.arg2 == 0) {
                        this.this$0.f659Yc.f503Qj.m453wc();
                    } else {
                        C0580g c0580g = this.this$0.f659Yc.f503Qj;
                        z8 = this.this$0.isForward;
                        C0643h.m753a(c0580g, "/mnt/sdcard/iNand", z8);
                    }
                }
                if (this.this$0.f659Yc.f515ck.startsWith("/data/tw/.like")) {
                    C0580g c0580g2 = this.this$0.f659Yc.f491Fd;
                    z7 = this.this$0.isForward;
                    C0643h.m757b(c0580g2, "/data/tw/.like", z7, new C0619k(this));
                } else if (str != null && this.this$0.f659Yc.f515ck.startsWith("/data/tw/") && this.this$0.f659Yc.f515ck.contains(String.valueOf(message.obj))) {
                    if (message.arg2 == 0) {
                        this.this$0.f659Yc.f486Dd.m453wc();
                        this.this$0.f659Yc.f528ud = this.this$0.f659Yc.f486Dd;
                        this.this$0.m699Tb();
                    } else {
                        C0580g c0580g3 = this.this$0.f659Yc.f486Dd;
                        String str2 = this.this$0.f659Yc.f515ck;
                        z6 = this.this$0.isForward;
                        C0643h.m757b(c0580g3, str2, z6, new C0620l(this));
                    }
                } else if (str != null && this.this$0.f659Yc.f515ck.startsWith(str)) {
                    if (message.arg2 == 0) {
                        this.this$0.f659Yc.f486Dd.m453wc();
                        this.this$0.f659Yc.f528ud = this.this$0.f659Yc.f486Dd;
                        this.this$0.m699Tb();
                    } else {
                        context = this.this$0.mContext;
                        C0580g c0580g4 = this.this$0.f659Yc.f486Dd;
                        String str3 = this.this$0.f659Yc.f515ck;
                        ArrayList<C0579f> arrayList5 = this.this$0.f659Yc.f506Tc;
                        z5 = this.this$0.isForward;
                        C0643h.m750a(context, c0580g4, str3, arrayList5, z5, new C0621m(this));
                    }
                }
                this.this$0.m672f(this.this$0.f659Yc.f528ud);
                return false;
            case 65281:
                isPlaying6 = this.this$0.isPlaying();
                if (isPlaying6) {
                    int duration = this.this$0.m691Mb().getDuration();
                    int currentPosition = this.this$0.m691Mb().getCurrentPosition();
                    this.this$0.f659Yc.f521md = currentPosition;
                    this.this$0.f659Yc.mDuration = duration;
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
                    this.this$0.f659Yc.f521md = currentPosition;
                    arrayList3 = C0628t.f637hi;
                    Iterator it3 = arrayList3.iterator();
                    while (it3.hasNext()) {
                        ((InterfaceC0656a) it3.next()).mo728d(this.this$0.f659Yc.f521md, duration);
                    }
                    int i10 = (currentPosition * 100) / duration;
                    c0655t = C0628t.f638jd;
                    c0655t.m791b(1, this.this$0.f659Yc.f482Ad + 1, this.this$0.f659Yc.f486Dd.f545kk, (i8 << 8) | (i9 << 16) | i7, i10);
                    c0655t2 = C0628t.f638jd;
                    isPlaying7 = this.this$0.isPlaying();
                    int i11 = i10 & 127;
                    c0655t2.write(40704, 3, (isPlaying7 ? 128 : 0) | i11);
                    c0655t3 = C0628t.f638jd;
                    isPlaying8 = this.this$0.isPlaying();
                    c0655t3.write(771, 3, (isPlaying8 ? 128 : 0) | i11);
                    Intent intent = new Intent("com.tw.launcher.music_progress_duration");
                    intent.putExtra("msg_music_progress", currentPosition);
                    intent.putExtra("msg_music_duration", duration);
                    context2 = this.this$0.mContext;
                    context2.sendBroadcast(intent);
                }
                handler5 = this.this$0.mHandler;
                handler5.removeMessages(65281);
                handler6 = this.this$0.mHandler;
                handler6.sendEmptyMessageDelayed(65281, 1000L);
                return false;
            case 65282:
                if (this.this$0.f659Yc.mSource == 3) {
                    this.this$0.m646Re();
                }
                return false;
            case 65283:
                if (this.this$0.f659Yc.mSource == 3) {
                    this.this$0.m647Se();
                }
                return false;
            case 65285:
                return false;
            case 65287:
                z9 = this.this$0.f667wg;
                if (z9) {
                    isPlaying9 = this.this$0.isPlaying();
                    if (!isPlaying9 && this.this$0.f659Yc.mSource == 3 && !this.this$0.f654Th && this.this$0.f659Yc.f514_j != null && new File(this.this$0.f659Yc.f514_j).canRead()) {
                        m686zb = this.this$0.m686zb(this.this$0.f659Yc.f514_j);
                        if (m686zb == 0) {
                            this.this$0.seekTo(this.this$0.f659Yc.f521md);
                            this.this$0.mo529Va();
                        }
                    }
                    handler7 = this.this$0.mHandler;
                    handler7.removeMessages(40454);
                    handler8 = this.this$0.mHandler;
                    handler8.sendEmptyMessageDelayed(40454, 1000L);
                    handler9 = this.this$0.mHandler;
                    handler9.removeMessages(65281);
                    handler10 = this.this$0.mHandler;
                    handler10.sendEmptyMessage(65281);
                }
                return false;
            case 65289:
                handler11 = this.this$0.mHandler;
                handler11.removeMessages(65289);
                C0578e c0578e = this.this$0.f659Yc;
                c0655t4 = C0628t.f638jd;
                C0643h.m752a(c0578e, c0655t4);
                C0643h.m759c(this.this$0.f659Yc.f506Tc);
                return false;
            case 65296:
                this.this$0.mo539pb();
                arrayList4 = C0628t.f637hi;
                Iterator it4 = arrayList4.iterator();
                while (it4.hasNext()) {
                    InterfaceC0656a interfaceC0656a = (InterfaceC0656a) it4.next();
                    isPlaying10 = this.this$0.isPlaying();
                    if (!isPlaying10) {
                        interfaceC0656a.mo725b("", "", "", null, this.this$0.f659Yc.f514_j, this.this$0.f659Yc.f515ck, this.this$0.f659Yc.f482Ad);
                        isPlaying11 = this.this$0.isPlaying();
                        interfaceC0656a.mo727c(isPlaying11);
                        interfaceC0656a.mo728d(0, 0);
                    }
                }
                return false;
            case 65297:
                this.this$0.m691Mb().stop();
                handler12 = this.this$0.mHandler;
                handler12.removeMessages(65281);
                this.this$0.m691Mb().reset();
                this.this$0.f659Yc.f522nd = "";
                this.this$0.f659Yc.f523od = "";
                this.this$0.f659Yc.f524pd = "";
                this.this$0.f659Yc.f521md = 0;
                this.this$0.f659Yc.mDuration = 0;
                this.this$0.m652Xe();
                return false;
            default:
                return false;
        }
    }
}
