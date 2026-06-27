package com.eckom.xtlibrary.p066b.p069f.p041d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p066b.p053j.C0703s;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0577d;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0578e;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0579f;
import com.eckom.xtlibrary.p066b.p069f.p039b.C0580g;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0638c;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0643h;
import com.eckom.xtlibrary.p066b.p069f.p043f.C0655t;
import com.eckom.xtlibrary.p066b.p069f.p044g.InterfaceC0656a;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* compiled from: MusicIjkID3Model.java */
/* renamed from: com.eckom.xtlibrary.b.f.d.F */
/* loaded from: classes3.dex */
class C0587F implements Handler.Callback {
    final /* synthetic */ C0593L this$0;

    C0587F(C0593L c0593l) {
        this.this$0 = c0593l;
    }

    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    public boolean handleMessage(Message message) {
        int i;
        boolean z;
        ArrayList arrayList;
        C0638c c0638c;
        C0638c c0638c2;
        C0638c c0638c3;
        C0638c c0638c4;
        int i2;
        boolean isPlaying;
        Handler handler;
        Handler handler2;
        boolean isPlaying2;
        boolean isPlaying3;
        boolean z2;
        boolean z3;
        Handler handler3;
        Handler handler4;
        ArrayList arrayList2;
        boolean isPlaying4;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean isPlaying5;
        boolean isPlaying6;
        Handler handler5;
        Handler handler6;
        ArrayList arrayList3;
        C0655t c0655t;
        C0655t c0655t2;
        boolean isPlaying7;
        C0655t c0655t3;
        boolean isPlaying8;
        Context context;
        C0638c c0638c5;
        C0638c c0638c6;
        C0638c c0638c7;
        boolean z7;
        boolean isPlaying9;
        Handler handler7;
        Handler handler8;
        Handler handler9;
        Handler handler10;
        C0638c c0638c8;
        C0638c c0638c9;
        C0638c c0638c10;
        Handler handler11;
        C0655t c0655t4;
        ArrayList arrayList4;
        boolean isPlaying10;
        boolean isPlaying11;
        Handler handler12;
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
                        StringBuilder sb = new StringBuilder();
                        sb.append("music XTL: ");
                        i2 = this.this$0.f556Eh;
                        sb.append(i2);
                        Log.d("MusicIjkID3Model", sb.toString());
                    }
                } catch (Exception e2) {
                    Log.e("MusicIjkID3Model", "handleMessage: 0x0510:" + e2.getMessage());
                }
            } else if (i == 40451) {
                switch (message.arg1) {
                    case 1:
                        isPlaying = this.this$0.isPlaying();
                        if (!isPlaying) {
                            this.this$0.mo529Va();
                            break;
                        } else {
                            this.this$0.m527Ua();
                            break;
                        }
                    case 2:
                        handler = this.this$0.mHandler;
                        handler.removeMessages(65289);
                        handler2 = this.this$0.mHandler;
                        handler2.sendEmptyMessageDelayed(65289, 500L);
                        break;
                    case 3:
                        this.this$0.mo539pb();
                        break;
                    case 4:
                        this.this$0.mo542rb();
                        break;
                    case 5:
                        isPlaying2 = this.this$0.isPlaying();
                        if (!isPlaying2) {
                            this.this$0.mo529Va();
                            break;
                        }
                        break;
                    case 6:
                        isPlaying3 = this.this$0.isPlaying();
                        if (isPlaying3) {
                            this.this$0.m527Ua();
                            break;
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
                int i3 = message.arg1;
                if (i3 == 1) {
                    String str = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m541ra(str);
                    } else {
                        this.this$0.m538pa(str);
                    }
                } else if (i3 == 2) {
                    String str2 = "/storage/" + message.obj;
                    if (message.arg2 == 0) {
                        this.this$0.m543sa(str2);
                    } else {
                        this.this$0.m540qa(str2);
                    }
                } else if (i3 == 3) {
                    if (message.arg2 == 0) {
                        this.this$0.f574Yc.f503Qj.m453wc();
                    } else {
                        C0580g c0580g = this.this$0.f574Yc.f503Qj;
                        z3 = this.this$0.isForward;
                        C0643h.m753a(c0580g, "/mnt/sdcard/iNand", z3);
                    }
                }
                if (message.arg2 != 0 && this.this$0.f574Yc.f515ck.startsWith("/data/tw/.like")) {
                    C0580g c0580g2 = this.this$0.f574Yc.f491Fd;
                    z2 = this.this$0.isForward;
                    C0643h.m757b(c0580g2, "/data/tw/.like", z2, new C0585D(this));
                }
            } else if (i == 514) {
                handler3 = this.this$0.mHandler;
                handler3.removeMessages(65289);
                handler4 = this.this$0.mHandler;
                handler4.sendEmptyMessage(65289);
                if (message.arg1 == 3 && message.arg2 == 1) {
                    this.this$0.f567Rh = true;
                }
                if (message.arg1 == 3 && message.arg2 == 0) {
                    this.this$0.f567Rh = false;
                }
            } else if (i == 515) {
                arrayList2 = C0593L.f552hi;
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    ((InterfaceC0656a) it.next()).mo729f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                }
            } else if (i == 769) {
                this.this$0.f574Yc.mSource = message.arg1 & 255;
                if (this.this$0.f574Yc.mSource != 3) {
                    isPlaying4 = this.this$0.isPlaying();
                    if (isPlaying4) {
                        this.this$0.m527Ua();
                    }
                }
            } else if (i != 770) {
                switch (i) {
                    case 65281:
                        isPlaying6 = this.this$0.isPlaying();
                        if (isPlaying6) {
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
                            int i4 = currentPosition / 1000;
                            int i5 = i4 / 60;
                            int i6 = i5 / 60;
                            int i7 = i4 % 60;
                            int i8 = i5 % 60;
                            int i9 = i6 % 24;
                            this.this$0.f574Yc.f521md = currentPosition;
                            arrayList3 = C0593L.f552hi;
                            Iterator it2 = arrayList3.iterator();
                            while (it2.hasNext()) {
                                ((InterfaceC0656a) it2.next()).mo728d(this.this$0.f574Yc.f521md, duration);
                            }
                            int i10 = (currentPosition * 100) / duration;
                            c0655t = C0593L.f553jd;
                            c0655t.m791b(1, this.this$0.f574Yc.f482Ad + 1, this.this$0.f574Yc.f486Dd.f545kk, (i8 << 8) | (i9 << 16) | i7, i10);
                            c0655t2 = C0593L.f553jd;
                            isPlaying7 = this.this$0.isPlaying();
                            int i11 = i10 & 127;
                            c0655t2.write(40704, 3, (isPlaying7 ? 128 : 0) | i11);
                            c0655t3 = C0593L.f553jd;
                            isPlaying8 = this.this$0.isPlaying();
                            c0655t3.write(771, 3, (isPlaying8 ? 128 : 0) | i11);
                            Intent intent = new Intent("com.tw.launcher.music_progress_duration");
                            intent.putExtra("msg_music_progress", currentPosition);
                            intent.putExtra("msg_music_duration", duration);
                            context = this.this$0.mContext;
                            context.sendBroadcast(intent);
                        }
                        handler5 = this.this$0.mHandler;
                        handler5.removeMessages(65281);
                        handler6 = this.this$0.mHandler;
                        handler6.sendEmptyMessageDelayed(65281, 1000L);
                        break;
                    case 65282:
                        if (this.this$0.f574Yc.mSource == 3) {
                            this.this$0.m463Re();
                            break;
                        }
                        break;
                    case 65283:
                        if (this.this$0.f574Yc.mSource == 3) {
                            this.this$0.m464Se();
                            break;
                        }
                        break;
                    case 65284:
                        break;
                    case 65285:
                        this.this$0.m512Ga("/mnt/sdcard");
                        break;
                    case 65286:
                        int i12 = message.arg1;
                        String str3 = (String) message.obj;
                        c0638c5 = this.this$0.f583mi;
                        CopyOnWriteArrayList<C0579f> copyOnWriteArrayList = c0638c5.f673Ak.get(str3);
                        C0580g m1041a = C0703s.m1041a(this.this$0.f574Yc.f494Hj, str3, 1, 1, 1);
                        m1041a.setLength(copyOnWriteArrayList.size());
                        m1041a.mKey = str3;
                        Iterator<C0579f> it3 = copyOnWriteArrayList.iterator();
                        while (it3.hasNext()) {
                            m1041a.m449a(it3.next());
                        }
                        c0638c6 = this.this$0.f583mi;
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList2 = c0638c6.f676Nj.get(str3);
                        C0580g m1042a = C0703s.m1042a(this.this$0.f574Yc.f498Lj, str3, 1, 3);
                        ArrayList<C0580g> arrayList5 = this.this$0.f574Yc.f500Nj.get(str3);
                        if (arrayList5 == null) {
                            arrayList5 = new ArrayList<>();
                            this.this$0.f574Yc.f500Nj.put(str3, arrayList5);
                        }
                        C0703s.m1043a(arrayList5, m1042a, copyOnWriteArrayList2);
                        c0638c7 = this.this$0.f583mi;
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList3 = c0638c7.f677Oj.get(str3);
                        C0580g m1042a2 = C0703s.m1042a(this.this$0.f574Yc.f497Kj, str3, 1, 2);
                        ArrayList<C0580g> arrayList6 = this.this$0.f574Yc.f501Oj.get(str3);
                        if (arrayList6 == null) {
                            arrayList6 = new ArrayList<>();
                            this.this$0.f574Yc.f501Oj.put(str3, arrayList6);
                        }
                        C0703s.m1043a(arrayList6, m1042a2, copyOnWriteArrayList3);
                        this.this$0.m462Qa(i12);
                        break;
                    case 65287:
                        z7 = this.this$0.f589wg;
                        if (z7) {
                            isPlaying9 = this.this$0.isPlaying();
                            if (!isPlaying9 && this.this$0.f574Yc.mSource == 3 && !this.this$0.f569Th && this.this$0.f574Yc.f514_j != null && new File(this.this$0.f574Yc.f514_j).canRead()) {
                                this.this$0.mMediaPlayer.setMPPath(this.this$0.f574Yc.f514_j);
                                this.this$0.seekTo(this.this$0.f574Yc.f521md);
                                this.this$0.mo529Va();
                            }
                            handler7 = this.this$0.mHandler;
                            handler7.removeMessages(40454);
                            handler8 = this.this$0.mHandler;
                            handler8.sendEmptyMessageDelayed(40454, 1000L);
                            handler9 = this.this$0.mHandler;
                            handler9.removeMessages(65281);
                            handler10 = this.this$0.mHandler;
                            handler10.sendEmptyMessage(65281);
                            break;
                        }
                        break;
                    case 65288:
                        int i13 = message.arg1;
                        String str4 = (String) message.obj;
                        c0638c8 = this.this$0.f583mi;
                        CopyOnWriteArrayList<C0579f> copyOnWriteArrayList4 = c0638c8.f681zk.get(str4);
                        C0580g m1041a2 = C0703s.m1041a(this.this$0.f574Yc.f531vj, str4, 2, 1, 1);
                        m1041a2.setLength(copyOnWriteArrayList4.size());
                        m1041a2.mKey = str4;
                        Iterator<C0579f> it4 = copyOnWriteArrayList4.iterator();
                        while (it4.hasNext()) {
                            m1041a2.m449a(it4.next());
                        }
                        c0638c9 = this.this$0.f583mi;
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList5 = c0638c9.f674Bj.get(str4);
                        C0580g m1042a3 = C0703s.m1042a(this.this$0.f574Yc.f538zj, str4, 2, 3);
                        ArrayList<C0580g> arrayList7 = this.this$0.f574Yc.f484Bj.get(str4);
                        if (arrayList7 == null) {
                            arrayList7 = new ArrayList<>();
                            this.this$0.f574Yc.f484Bj.put(str4, arrayList7);
                        }
                        C0703s.m1043a(arrayList7, m1042a3, copyOnWriteArrayList5);
                        c0638c10 = this.this$0.f583mi;
                        CopyOnWriteArrayList<C0577d> copyOnWriteArrayList6 = c0638c10.f675Cj.get(str4);
                        C0580g m1042a4 = C0703s.m1042a(this.this$0.f574Yc.f537yj, str4, 2, 2);
                        ArrayList<C0580g> arrayList8 = this.this$0.f574Yc.f485Cj.get(str4);
                        if (arrayList8 == null) {
                            arrayList8 = new ArrayList<>();
                            this.this$0.f574Yc.f485Cj.put(str4, arrayList8);
                        }
                        C0703s.m1043a(arrayList8, m1042a4, copyOnWriteArrayList6);
                        this.this$0.m462Qa(i13);
                        break;
                    case 65289:
                        handler11 = this.this$0.mHandler;
                        handler11.removeMessages(65289);
                        C0578e c0578e = this.this$0.f574Yc;
                        c0655t4 = C0593L.f553jd;
                        C0643h.m752a(c0578e, c0655t4);
                        C0643h.m759c(this.this$0.f574Yc.f506Tc);
                        break;
                    case 65290:
                        new C0586E(this).start();
                        break;
                    default:
                        switch (i) {
                            case 65296:
                                this.this$0.mo539pb();
                                arrayList4 = C0593L.f552hi;
                                Iterator it5 = arrayList4.iterator();
                                while (it5.hasNext()) {
                                    InterfaceC0656a interfaceC0656a = (InterfaceC0656a) it5.next();
                                    isPlaying10 = this.this$0.isPlaying();
                                    if (!isPlaying10) {
                                        interfaceC0656a.mo725b("", "", "", null, this.this$0.f574Yc.f514_j, this.this$0.f574Yc.f515ck, this.this$0.f574Yc.f482Ad);
                                        isPlaying11 = this.this$0.isPlaying();
                                        interfaceC0656a.mo727c(isPlaying11);
                                        interfaceC0656a.mo728d(0, 0);
                                    }
                                }
                                break;
                            case 65297:
                                this.this$0.mMediaPlayer.stopPlayback();
                                handler12 = this.this$0.mHandler;
                                handler12.removeMessages(65281);
                                this.this$0.f574Yc.f522nd = "";
                                this.this$0.f574Yc.f523od = "";
                                this.this$0.f574Yc.f524pd = "";
                                this.this$0.f574Yc.f521md = 0;
                                this.this$0.f574Yc.mDuration = 0;
                                this.this$0.m469Xe();
                                break;
                        }
                }
            } else if (this.this$0.f574Yc.mSource == 3) {
                int i14 = message.arg1;
                Log.d("MusicIjkID3Model", "BT_CALL STATE:" + i14);
                if (i14 == 0) {
                    this.this$0.f555Cg = false;
                    z4 = this.this$0.f568Sh;
                    if (z4) {
                        z5 = this.this$0.f567Rh;
                        if (!z5) {
                            this.this$0.mo529Va();
                        }
                    }
                } else if (i14 == 1 || i14 == 2 || i14 == 3 || i14 == 4) {
                    z6 = this.this$0.f555Cg;
                    if (!z6) {
                        C0593L c0593l = this.this$0;
                        isPlaying5 = this.this$0.isPlaying();
                        c0593l.f568Sh = isPlaying5;
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
        arrayList = C0593L.f552hi;
        Iterator it6 = arrayList.iterator();
        while (it6.hasNext()) {
            ((InterfaceC0656a) it6.next()).mo735q(z);
        }
        int i15 = message.arg1;
        String str5 = (String) message.obj;
        C0580g c0580g3 = this.this$0.f574Yc.f502Pj;
        c0638c = this.this$0.f583mi;
        c0580g3.setLength(c0638c.f678wk.size());
        this.this$0.f574Yc.f502Pj.mKey = str5;
        c0638c2 = this.this$0.f583mi;
        Iterator<C0579f> it7 = c0638c2.f678wk.iterator();
        while (it7.hasNext()) {
            this.this$0.f574Yc.f502Pj.m449a(it7.next());
        }
        ArrayList<C0580g> arrayList9 = this.this$0.f574Yc.f508Uj;
        C0580g c0580g4 = this.this$0.f574Yc.f504Rj;
        c0638c3 = this.this$0.f583mi;
        C0703s.m1043a(arrayList9, c0580g4, c0638c3.f679xk);
        ArrayList<C0580g> arrayList10 = this.this$0.f574Yc.f509Vj;
        C0580g c0580g5 = this.this$0.f574Yc.f505Sj;
        c0638c4 = this.this$0.f583mi;
        C0703s.m1043a(arrayList10, c0580g5, c0638c4.f680yk);
        this.this$0.m462Qa(i15);
        return false;
    }
}
