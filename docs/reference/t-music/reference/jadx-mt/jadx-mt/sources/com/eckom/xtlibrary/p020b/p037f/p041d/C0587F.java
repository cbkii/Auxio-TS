package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0577d;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0579f;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0643h;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import com.eckom.xtlibrary.p020b.p053j.C0703s;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.F */
/* JADX INFO: compiled from: MusicIjkID3Model.java */
/* JADX INFO: loaded from: classes3.dex */
class C0587F implements Handler.Callback {
    final /* synthetic */ C0593L this$0;

    C0587F(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    public boolean handleMessage(Message message) throws Throwable {
        int i;
        boolean z;
        try {
            i = message.what;
            z = true;
        } catch (Exception e) {
            Log.i("MusicIjkID3Model", "Exception:" + e);
        }
        if (i != 274) {
            if (i == 1296) {
                try {
                    byte[] bArr = (byte[]) message.obj;
                    if (message.arg1 == 255) {
                        this.this$0.f556Eh = bArr[0] & 255;
                        Log.d("MusicIjkID3Model", "music XTL: " + this.this$0.f556Eh);
                    }
                } catch (Exception e2) {
                    Log.e("MusicIjkID3Model", "handleMessage: 0x0510:" + e2.getMessage());
                }
            } else if (i == 40451) {
                switch (message.arg1) {
                    case 1:
                        if (this.this$0.isPlaying()) {
                            this.this$0.m527Ua();
                        } else {
                            this.this$0.mo529Va();
                        }
                        break;
                    case 2:
                        this.this$0.mHandler.removeMessages(65289);
                        this.this$0.mHandler.sendEmptyMessageDelayed(65289, 500L);
                        break;
                    case 3:
                        this.this$0.mo539pb();
                        break;
                    case 4:
                        this.this$0.mo542rb();
                        break;
                    case 5:
                        if (!this.this$0.isPlaying()) {
                            this.this$0.mo529Va();
                        }
                        break;
                    case 6:
                        if (this.this$0.isPlaying()) {
                            this.this$0.m527Ua();
                        }
                        break;
                    case 7:
                        this.this$0.m460L(true);
                        break;
                    case 8:
                        this.this$0.m460L(false);
                        break;
                    case 9:
                        this.this$0.mo514Hb();
                        break;
                    case 10:
                        this.this$0.mo515Ib();
                        break;
                    case 11:
                        this.this$0.mute(true);
                        break;
                    case 12:
                        this.this$0.mute(false);
                        break;
                }
            } else if (i == 40454) {
                this.this$0.m528Ub();
            } else if (i == 40479) {
                int i2 = message.arg1;
                if (i2 == 1) {
                    String str = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m541ra(str);
                    } else {
                        this.this$0.m538pa(str);
                    }
                } else if (i2 == 2) {
                    String str2 = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m543sa(str2);
                    } else {
                        this.this$0.m540qa(str2);
                    }
                } else if (i2 == 3) {
                    if (message.arg2 == 0) {
                        this.this$0.f574Yc.f503Qj.m453wc();
                    } else {
                        C0643h.m753a(this.this$0.f574Yc.f503Qj, "/mnt/sdcard/iNand", this.this$0.isForward);
                    }
                }
                if (message.arg2 != 0 && this.this$0.f574Yc.f515ck.startsWith("/data/tw/.like")) {
                    C0643h.m757b(this.this$0.f574Yc.f491Fd, "/data/tw/.like", this.this$0.isForward, new C0585D(this));
                }
            } else if (i == 514) {
                this.this$0.mHandler.removeMessages(65289);
                this.this$0.mHandler.sendEmptyMessage(65289);
                if (message.arg1 == 3 && message.arg2 == 1) {
                    this.this$0.f567Rh = true;
                }
                if (message.arg1 == 3 && message.arg2 == 0) {
                    this.this$0.f567Rh = false;
                }
            } else if (i == 515) {
                Iterator it = C0593L.f552hi.iterator();
                while (it.hasNext()) {
                    ((InterfaceC0656a) it.next()).mo729f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                }
            } else if (i == 769) {
                this.this$0.f574Yc.mSource = message.arg1 & 255;
                if (this.this$0.f574Yc.mSource != 3 && this.this$0.isPlaying()) {
                    this.this$0.m527Ua();
                }
            } else if (i != 770) {
                switch (i) {
                    case 65281:
                        if (this.this$0.isPlaying()) {
                            int duration = this.this$0.mMediaPlayer.getDuration();
                            int currentPosition = this.this$0.mMediaPlayer.getCurrentPosition();
                            this.this$0.f574Yc.f521md = currentPosition;
                            this.this$0.f574Yc.mDuration = duration;
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
                            this.this$0.f574Yc.f521md = currentPosition;
                            Iterator it2 = C0593L.f552hi.iterator();
                            while (it2.hasNext()) {
                                ((InterfaceC0656a) it2.next()).mo728d(this.this$0.f574Yc.f521md, duration);
                            }
                            int i9 = (currentPosition * 100) / duration;
                            C0593L.f553jd.m791b(1, this.this$0.f574Yc.f482Ad + 1, this.this$0.f574Yc.f486Dd.f545kk, (i7 << 8) | (i8 << 16) | i6, i9);
                            int i10 = i9 & 127;
                            C0593L.f553jd.write(40704, 3, (this.this$0.isPlaying() ? 128 : 0) | i10);
                            C0593L.f553jd.write(771, 3, (this.this$0.isPlaying() ? 128 : 0) | i10);
                            Intent intent = new Intent("com.tw.launcher.music_progress_duration");
                            intent.putExtra("msg_music_progress", currentPosition);
                            intent.putExtra("msg_music_duration", duration);
                            this.this$0.mContext.sendBroadcast(intent);
                        }
                        this.this$0.mHandler.removeMessages(65281);
                        this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                        break;
                    case 65282:
                        if (this.this$0.f574Yc.mSource == 3) {
                            this.this$0.m463Re();
                        }
                        break;
                    case 65283:
                        if (this.this$0.f574Yc.mSource == 3) {
                            this.this$0.m464Se();
                        }
                        break;
                    case 65284:
                        break;
                    case 65285:
                        this.this$0.m512Ga("/mnt/sdcard");
                        break;
                    case 65286:
                        int i11 = message.arg1;
                        String str3 = (String) message.obj;
                        CopyOnWriteArrayList<C0579f> copyOnWriteArrayList = this.this$0.f583mi.f673Ak.get(str3);
                        C0580g c0580gM1041a = C0703s.m1041a(this.this$0.f574Yc.f494Hj, str3, 1, 1, 1);
                        c0580gM1041a.setLength(copyOnWriteArrayList.size());
                        c0580gM1041a.mKey = str3;
                        Iterator<C0579f> it3 = copyOnWriteArrayList.iterator();
                        while (it3.hasNext()) {
                            c0580gM1041a.m449a(it3.next());
                        }
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList2 = this.this$0.f583mi.f676Nj.get(str3);
                        C0580g c0580gM1042a = C0703s.m1042a(this.this$0.f574Yc.f498Lj, str3, 1, 3);
                        ArrayList<C0580g> arrayList = this.this$0.f574Yc.f500Nj.get(str3);
                        if (arrayList == null) {
                            arrayList = new ArrayList<>();
                            this.this$0.f574Yc.f500Nj.put(str3, arrayList);
                        }
                        C0703s.m1043a(arrayList, c0580gM1042a, copyOnWriteArrayList2);
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList3 = this.this$0.f583mi.f677Oj.get(str3);
                        C0580g c0580gM1042a2 = C0703s.m1042a(this.this$0.f574Yc.f497Kj, str3, 1, 2);
                        ArrayList<C0580g> arrayList2 = this.this$0.f574Yc.f501Oj.get(str3);
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList<>();
                            this.this$0.f574Yc.f501Oj.put(str3, arrayList2);
                        }
                        C0703s.m1043a(arrayList2, c0580gM1042a2, copyOnWriteArrayList3);
                        this.this$0.m462Qa(i11);
                        break;
                    case 65287:
                        if (this.this$0.f589wg) {
                            if (!this.this$0.isPlaying() && this.this$0.f574Yc.mSource == 3 && !this.this$0.f569Th && this.this$0.f574Yc.f514_j != null && new File(this.this$0.f574Yc.f514_j).canRead()) {
                                this.this$0.mMediaPlayer.setMPPath(this.this$0.f574Yc.f514_j);
                                this.this$0.seekTo(this.this$0.f574Yc.f521md);
                                this.this$0.mo529Va();
                            }
                            this.this$0.mHandler.removeMessages(40454);
                            this.this$0.mHandler.sendEmptyMessageDelayed(40454, 1000L);
                            this.this$0.mHandler.removeMessages(65281);
                            this.this$0.mHandler.sendEmptyMessage(65281);
                        }
                        break;
                    case 65288:
                        int i12 = message.arg1;
                        String str4 = (String) message.obj;
                        CopyOnWriteArrayList<C0579f> copyOnWriteArrayList4 = this.this$0.f583mi.f681zk.get(str4);
                        C0580g c0580gM1041a2 = C0703s.m1041a(this.this$0.f574Yc.f531vj, str4, 2, 1, 1);
                        c0580gM1041a2.setLength(copyOnWriteArrayList4.size());
                        c0580gM1041a2.mKey = str4;
                        Iterator<C0579f> it4 = copyOnWriteArrayList4.iterator();
                        while (it4.hasNext()) {
                            c0580gM1041a2.m449a(it4.next());
                        }
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList5 = this.this$0.f583mi.f674Bj.get(str4);
                        C0580g c0580gM1042a3 = C0703s.m1042a(this.this$0.f574Yc.f538zj, str4, 2, 3);
                        ArrayList<C0580g> arrayList3 = this.this$0.f574Yc.f484Bj.get(str4);
                        if (arrayList3 == null) {
                            arrayList3 = new ArrayList<>();
                            this.this$0.f574Yc.f484Bj.put(str4, arrayList3);
                        }
                        C0703s.m1043a(arrayList3, c0580gM1042a3, copyOnWriteArrayList5);
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList6 = this.this$0.f583mi.f675Cj.get(str4);
                        C0580g c0580gM1042a4 = C0703s.m1042a(this.this$0.f574Yc.f537yj, str4, 2, 2);
                        ArrayList<C0580g> arrayList4 = this.this$0.f574Yc.f485Cj.get(str4);
                        if (arrayList4 == null) {
                            arrayList4 = new ArrayList<>();
                            this.this$0.f574Yc.f485Cj.put(str4, arrayList4);
                        }
                        C0703s.m1043a(arrayList4, c0580gM1042a4, copyOnWriteArrayList6);
                        this.this$0.m462Qa(i12);
                        break;
                    case 65289:
                        this.this$0.mHandler.removeMessages(65289);
                        C0643h.m752a(this.this$0.f574Yc, C0593L.f553jd);
                        C0643h.m759c(this.this$0.f574Yc.f506Tc);
                        break;
                    case 65290:
                        new C0586E(this).start();
                        break;
                    default:
                        switch (i) {
                            case 65296:
                                this.this$0.mo539pb();
                                for (InterfaceC0656a interfaceC0656a : C0593L.f552hi) {
                                    if (!this.this$0.isPlaying()) {
                                        interfaceC0656a.mo725b("", "", "", null, this.this$0.f574Yc.f514_j, this.this$0.f574Yc.f515ck, this.this$0.f574Yc.f482Ad);
                                        interfaceC0656a.mo727c(this.this$0.isPlaying());
                                        interfaceC0656a.mo728d(0, 0);
                                    }
                                }
                                break;
                            case 65297:
                                this.this$0.mMediaPlayer.stopPlayback();
                                this.this$0.mHandler.removeMessages(65281);
                                this.this$0.f574Yc.f522nd = "";
                                this.this$0.f574Yc.f523od = "";
                                this.this$0.f574Yc.f524pd = "";
                                this.this$0.f574Yc.f521md = 0;
                                this.this$0.f574Yc.mDuration = 0;
                                this.this$0.m469Xe();
                                break;
                        }
                        break;
                }
            } else if (this.this$0.f574Yc.mSource == 3) {
                int i13 = message.arg1;
                Log.d("MusicIjkID3Model", "BT_CALL STATE:" + i13);
                if (i13 == 0) {
                    this.this$0.f555Cg = false;
                    if (this.this$0.f568Sh && !this.this$0.f567Rh) {
                        this.this$0.mo529Va();
                    }
                } else if (i13 == 1 || i13 == 2 || i13 == 3 || i13 == 4) {
                    if (!this.this$0.f555Cg) {
                        this.this$0.f568Sh = this.this$0.isPlaying();
                    }
                    this.this$0.f555Cg = true;
                    this.this$0.m527Ua();
                }
            }
            return false;
        }
        if ((message.arg1 & 65536) != 65536) {
            z = false;
        }
        Iterator it5 = C0593L.f552hi.iterator();
        while (it5.hasNext()) {
            ((InterfaceC0656a) it5.next()).mo735q(z);
        }
        int i14 = message.arg1;
        String str5 = (String) message.obj;
        this.this$0.f574Yc.f502Pj.setLength(this.this$0.f583mi.f678wk.size());
        this.this$0.f574Yc.f502Pj.mKey = str5;
        Iterator<C0579f> it6 = this.this$0.f583mi.f678wk.iterator();
        while (it6.hasNext()) {
            this.this$0.f574Yc.f502Pj.m449a(it6.next());
        }
        C0703s.m1043a(this.this$0.f574Yc.f508Uj, this.this$0.f574Yc.f504Rj, this.this$0.f583mi.f679xk);
        C0703s.m1043a(this.this$0.f574Yc.f509Vj, this.this$0.f574Yc.f505Sj, this.this$0.f583mi.f680yk);
        this.this$0.m462Qa(i14);
        return false;
    }
}
