package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import java.io.File;
import java.util.Iterator;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.n */
/* JADX INFO: compiled from: MusicID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0622n implements Handler.Callback {
    final /* synthetic */ C0628t this$0;

    C0622n(C0628t c0628t) {
        this.this$0 = c0628t;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    public boolean handleMessage(Message message) throws Throwable {
        boolean z;
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
                Iterator it = C0628t.f637hi.iterator();
                while (it.hasNext()) {
                    ((InterfaceC0656a) it.next()).mo735q(z);
                }
                return false;
            case 514:
                this.this$0.mHandler.removeMessages(65289);
                this.this$0.mHandler.sendEmptyMessage(65289);
                if (message.arg1 == 3 && message.arg2 == 1) {
                    this.this$0.f652Rh = true;
                }
                if (message.arg1 == 3 && message.arg2 == 0) {
                    this.this$0.f652Rh = false;
                }
                return false;
            case 515:
                Iterator it2 = C0628t.f637hi.iterator();
                while (it2.hasNext()) {
                    ((InterfaceC0656a) it2.next()).mo729f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                }
                return false;
            case 769:
                this.this$0.f659Yc.mSource = message.arg1 & 255;
                if (this.this$0.f659Yc.mSource != 3 && this.this$0.isPlaying()) {
                    this.this$0.m700Ua();
                }
                return false;
            case 770:
                if (this.this$0.f659Yc.mSource == 3) {
                    int i = message.arg1;
                    Log.d("MusicID3Model", "BT_CALL STATE:" + i);
                    if (i == 0) {
                        this.this$0.f640Cg = false;
                        if (this.this$0.f653Sh && !this.this$0.f652Rh) {
                            this.this$0.mo529Va();
                        }
                    } else if (i == 1 || i == 2 || i == 3 || i == 4) {
                        if (!this.this$0.f640Cg) {
                            this.this$0.f653Sh = this.this$0.isPlaying();
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
                        Log.d("MusicID3Model", "music XTL: " + this.this$0.f641Eh);
                    }
                    break;
                } catch (Exception e2) {
                    Log.e("MusicID3Model", "handleMessage: 0x0510:" + e2.getMessage());
                }
                return false;
            case 40451:
                switch (message.arg1) {
                    case 1:
                        if (this.this$0.isPlaying()) {
                            this.this$0.m700Ua();
                        } else {
                            this.this$0.mo529Va();
                        }
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
                        if (!this.this$0.isPlaying()) {
                            this.this$0.mo529Va();
                        }
                        return false;
                    case 6:
                        if (this.this$0.isPlaying()) {
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
                int i2 = message.arg1;
                if (i2 == 1) {
                    str = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m705ra(str);
                    } else {
                        this.this$0.m703pa(str);
                        this.this$0.m687Fa(str);
                    }
                } else if (i2 == 2) {
                    str = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m706sa(str);
                    } else {
                        this.this$0.m704qa(str);
                        this.this$0.m687Fa(str);
                    }
                } else if (i2 == 3) {
                    str = "/mnt/sdcard/iNand";
                    if (message.arg2 == 0) {
                        this.this$0.f659Yc.f503Qj.m453wc();
                    } else {
                        C0643h.m753a(this.this$0.f659Yc.f503Qj, "/mnt/sdcard/iNand", this.this$0.isForward);
                    }
                }
                if (this.this$0.f659Yc.f515ck.startsWith("/data/tw/.like")) {
                    C0643h.m757b(this.this$0.f659Yc.f491Fd, "/data/tw/.like", this.this$0.isForward, new C0619k(this));
                } else if (str != null && this.this$0.f659Yc.f515ck.startsWith("/data/tw/") && this.this$0.f659Yc.f515ck.contains(String.valueOf(message.obj))) {
                    if (message.arg2 == 0) {
                        this.this$0.f659Yc.f486Dd.m453wc();
                        this.this$0.f659Yc.f528ud = this.this$0.f659Yc.f486Dd;
                        this.this$0.m699Tb();
                    } else {
                        C0643h.m757b(this.this$0.f659Yc.f486Dd, this.this$0.f659Yc.f515ck, this.this$0.isForward, new C0620l(this));
                    }
                } else if (str != null && this.this$0.f659Yc.f515ck.startsWith(str)) {
                    if (message.arg2 == 0) {
                        this.this$0.f659Yc.f486Dd.m453wc();
                        this.this$0.f659Yc.f528ud = this.this$0.f659Yc.f486Dd;
                        this.this$0.m699Tb();
                    } else {
                        C0643h.m750a(this.this$0.mContext, this.this$0.f659Yc.f486Dd, this.this$0.f659Yc.f515ck, this.this$0.f659Yc.f506Tc, this.this$0.isForward, new C0621m(this));
                    }
                }
                this.this$0.m672f(this.this$0.f659Yc.f528ud);
                return false;
            case 65281:
                if (this.this$0.isPlaying()) {
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
                    int i3 = currentPosition / 1000;
                    int i4 = i3 / 60;
                    int i5 = i4 / 60;
                    int i6 = i3 % 60;
                    int i7 = i4 % 60;
                    int i8 = i5 % 24;
                    this.this$0.f659Yc.f521md = currentPosition;
                    Iterator it3 = C0628t.f637hi.iterator();
                    while (it3.hasNext()) {
                        ((InterfaceC0656a) it3.next()).mo728d(this.this$0.f659Yc.f521md, duration);
                    }
                    int i9 = (currentPosition * 100) / duration;
                    C0628t.f638jd.m791b(1, this.this$0.f659Yc.f482Ad + 1, this.this$0.f659Yc.f486Dd.f545kk, (i7 << 8) | (i8 << 16) | i6, i9);
                    int i10 = i9 & 127;
                    C0628t.f638jd.write(40704, 3, (this.this$0.isPlaying() ? 128 : 0) | i10);
                    C0628t.f638jd.write(771, 3, (this.this$0.isPlaying() ? 128 : 0) | i10);
                    Intent intent = new Intent("com.tw.launcher.music_progress_duration");
                    intent.putExtra("msg_music_progress", currentPosition);
                    intent.putExtra("msg_music_duration", duration);
                    this.this$0.mContext.sendBroadcast(intent);
                }
                this.this$0.mHandler.removeMessages(65281);
                this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
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
                if (this.this$0.f667wg) {
                    if (!this.this$0.isPlaying() && this.this$0.f659Yc.mSource == 3 && !this.this$0.f654Th && this.this$0.f659Yc.f514_j != null && new File(this.this$0.f659Yc.f514_j).canRead() && this.this$0.m686zb(this.this$0.f659Yc.f514_j) == 0) {
                        this.this$0.seekTo(this.this$0.f659Yc.f521md);
                        this.this$0.mo529Va();
                    }
                    this.this$0.mHandler.removeMessages(40454);
                    this.this$0.mHandler.sendEmptyMessageDelayed(40454, 1000L);
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.mHandler.sendEmptyMessage(65281);
                }
                return false;
            case 65289:
                this.this$0.mHandler.removeMessages(65289);
                C0643h.m752a(this.this$0.f659Yc, C0628t.f638jd);
                C0643h.m759c(this.this$0.f659Yc.f506Tc);
                return false;
            case 65296:
                this.this$0.mo539pb();
                for (InterfaceC0656a interfaceC0656a : C0628t.f637hi) {
                    if (!this.this$0.isPlaying()) {
                        interfaceC0656a.mo725b("", "", "", null, this.this$0.f659Yc.f514_j, this.this$0.f659Yc.f515ck, this.this$0.f659Yc.f482Ad);
                        interfaceC0656a.mo727c(this.this$0.isPlaying());
                        interfaceC0656a.mo728d(0, 0);
                    }
                }
                return false;
            case 65297:
                this.this$0.m691Mb().stop();
                this.this$0.mHandler.removeMessages(65281);
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
