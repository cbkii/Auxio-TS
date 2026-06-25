package com.eckom.xtlibrary.p020b.p037f.p041d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.eckom.xtlibrary.p020b.p037f.p039b.C0580g;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0636a;
import com.eckom.xtlibrary.p020b.p037f.p043f.C0654s;
import com.eckom.xtlibrary.p020b.p037f.p044g.InterfaceC0656a;
import java.util.Iterator;
import tv.danmaku.ijk.media.player.p069tw.TWMediaPlayer;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.O */
/* JADX INFO: compiled from: MusicIjkModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0596O implements Handler.Callback {
    final /* synthetic */ C0601U this$0;

    C0596O(C0601U c0601u) {
        this.this$0 = c0601u;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x013a A[Catch: Exception -> 0x0610, TryCatch #0 {Exception -> 0x0610, blocks: (B:3:0x0009, B:4:0x001b, B:6:0x0020, B:7:0x0049, B:8:0x0056, B:10:0x005c, B:12:0x006a, B:13:0x009a, B:14:0x00aa, B:15:0x00ba, B:17:0x00dc, B:19:0x00e4, B:20:0x00e9, B:22:0x0125, B:24:0x012d, B:26:0x0133, B:28:0x013a, B:30:0x0141, B:31:0x0162, B:32:0x017f, B:33:0x0196, B:35:0x019c, B:37:0x01aa, B:38:0x01b7, B:40:0x01bf, B:41:0x01c6, B:43:0x01ce, B:44:0x01d5, B:46:0x01dd, B:53:0x0202, B:54:0x021c, B:56:0x0222, B:57:0x0233, B:61:0x026b, B:65:0x0283, B:66:0x02a1, B:67:0x02b5, B:73:0x02c2, B:75:0x02c8, B:76:0x02d3, B:93:0x035b, B:95:0x0362, B:97:0x036d, B:99:0x0371, B:100:0x037f, B:102:0x03aa, B:104:0x03b4, B:106:0x03bb, B:108:0x03c3, B:109:0x03df, B:110:0x03e5, B:111:0x03ed, B:113:0x03f3, B:115:0x0401, B:116:0x040a, B:77:0x02e2, B:79:0x02e6, B:81:0x030b, B:83:0x030f, B:84:0x0317, B:80:0x02fa, B:85:0x031f, B:87:0x0323, B:89:0x0348, B:91:0x034c, B:92:0x0354, B:88:0x0337, B:117:0x0412, B:118:0x0422, B:119:0x0424, B:121:0x0429, B:122:0x0431, B:123:0x0438, B:124:0x043f, B:125:0x0446, B:126:0x044e, B:127:0x0455, B:129:0x045d, B:130:0x0464, B:132:0x046c, B:133:0x0473, B:134:0x047a, B:135:0x0481, B:136:0x0497, B:138:0x049f, B:139:0x04a6, B:147:0x04f5, B:150:0x04ff, B:158:0x0522, B:160:0x052a, B:162:0x0532, B:163:0x0538, B:164:0x053e, B:165:0x054a, B:167:0x0558, B:169:0x0560, B:170:0x0567, B:172:0x057a, B:174:0x0581, B:175:0x0588, B:177:0x0590, B:179:0x0598, B:180:0x059f, B:181:0x05a7, B:183:0x05ad, B:187:0x05bd, B:188:0x05c1, B:190:0x05d7, B:192:0x05db, B:193:0x05e0, B:195:0x05e4, B:197:0x05e8, B:198:0x05ef, B:202:0x05f8, B:203:0x0600, B:205:0x0606, B:146:0x04db, B:141:0x04ad, B:143:0x04b5), top: B:211:0x0009, inners: #1 }] */
    @Override // android.os.Handler.Callback
    @SuppressLint({"SdCardPath"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleMessage(Message message) throws Throwable {
        try {
            boolean z = true;
            switch (message.what) {
                case 274:
                    if ((message.arg1 & 65536) != 65536) {
                        z = false;
                    }
                    Iterator it = C0601U.f592hi.iterator();
                    while (it.hasNext()) {
                        ((InterfaceC0656a) it.next()).mo735q(z);
                    }
                    return false;
                case 514:
                    this.this$0.mHandler.removeMessages(65289);
                    this.this$0.mHandler.sendEmptyMessage(65289);
                    if (message.arg1 == 3 && message.arg2 == 1) {
                        this.this$0.f599Rh = true;
                    }
                    if (message.arg1 != 3 || message.arg2 != 0) {
                        return false;
                    }
                    this.this$0.f599Rh = false;
                    return false;
                case 515:
                    Iterator it2 = C0601U.f592hi.iterator();
                    while (it2.hasNext()) {
                        ((InterfaceC0656a) it2.next()).mo729f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                    }
                    return false;
                case 769:
                    C0601U.f593jd.mSource = message.arg1 & 255;
                    if (C0601U.f593jd.mSource == 9) {
                        C0654s unused = C0601U.f593jd;
                        if (C0654s.f710Jd) {
                            this.this$0.m584Tb();
                            return false;
                        }
                    }
                    if (C0601U.f593jd.mSource == 3 || !this.this$0.isPlaying()) {
                        return false;
                    }
                    this.this$0.m585Ua();
                    return false;
                case 770:
                    if (C0601U.f593jd.mSource != 3) {
                        return false;
                    }
                    int i = message.arg1;
                    Log.d("MusicModel", "BT_CALL STATE:" + i);
                    if (i == 0) {
                        this.this$0.f596Cg = false;
                        if (!this.this$0.f600Sh || this.this$0.f599Rh) {
                            return false;
                        }
                        this.this$0.mo529Va();
                        return false;
                    }
                    if (i != 1 && i != 2 && i != 3 && i != 4) {
                        return false;
                    }
                    if (!this.this$0.f596Cg) {
                        if (this.this$0.isPlaying()) {
                            this.this$0.f600Sh = true;
                        } else {
                            this.this$0.f600Sh = false;
                        }
                    }
                    this.this$0.f596Cg = true;
                    this.this$0.m585Ua();
                    return false;
                case 1296:
                    try {
                        byte[] bArr = (byte[]) message.obj;
                        if (message.arg1 != 255) {
                            return false;
                        }
                        this.this$0.f597Eh = bArr[0] & 255;
                        Log.d("MusicModel", "music XTL: " + this.this$0.f597Eh);
                        return false;
                    } catch (Exception e) {
                        Log.e("MusicModel", "handleMessage: 0x0510:" + e.getMessage());
                        return false;
                    }
                case 40451:
                    switch (message.arg1) {
                        case 1:
                            if (this.this$0.isPlaying()) {
                                this.this$0.m585Ua();
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
                            if (this.this$0.isPlaying()) {
                                return false;
                            }
                            this.this$0.mo529Va();
                            return false;
                        case 6:
                            if (!this.this$0.isPlaying()) {
                                return false;
                            }
                            this.this$0.m585Ua();
                            return false;
                        case 7:
                            this.this$0.m547L(true);
                            return false;
                        case 8:
                            this.this$0.m547L(false);
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
                    this.this$0.m586Ub();
                    return false;
                case 40479:
                    String str = null;
                    int i2 = message.arg1;
                    if (i2 == 1) {
                        str = C0601U.f591Gd ? "/mnt/" + message.obj : "/storage/" + message.obj;
                        if (message.arg2 == 0) {
                            C0601U.f593jd.m786ra(str);
                        } else {
                            C0601U.f593jd.m784pa(str);
                        }
                    } else if (i2 == 2) {
                        str = C0601U.f591Gd ? "/mnt/usbhost/" + message.obj : "/storage/" + message.obj;
                        if (message.arg2 == 0) {
                            C0601U.f593jd.m787sa(str);
                        } else {
                            C0601U.f593jd.m785qa(str);
                        }
                    } else if (i2 == 3) {
                        str = "/mnt/sdcard/iNand";
                        if (message.arg2 == 0) {
                            C0601U.f593jd.f725td.m453wc();
                        } else {
                            C0601U.f593jd.m780b(C0601U.f593jd.f725td, "/mnt/sdcard/iNand");
                        }
                    }
                    C0654s unused2 = C0601U.f593jd;
                    if (C0654s.f703Cd != null) {
                        C0654s unused3 = C0601U.f593jd;
                        if (C0654s.f703Cd.startsWith(str)) {
                            if (message.arg2 == 0) {
                                C0654s unused4 = C0601U.f593jd;
                                C0654s.f704Dd.m453wc();
                                this.this$0.m584Tb();
                            } else {
                                C0654s c0654s = C0601U.f593jd;
                                Context context = this.this$0.mContext;
                                C0654s unused5 = C0601U.f593jd;
                                C0580g c0580g = C0654s.f704Dd;
                                C0654s unused6 = C0601U.f593jd;
                                c0654s.m778a(context, c0580g, C0654s.f703Cd);
                                C0654s c0654s2 = C0601U.f593jd;
                                C0654s unused7 = C0601U.f593jd;
                                c0654s2.m783ea(C0654s.f701Ad);
                                if (!this.this$0.isPlaying() && C0601U.f593jd.getService() == 3) {
                                    C0654s unused8 = C0601U.f593jd;
                                    if (C0654s.f702Bd != null && !this.this$0.isPlaying()) {
                                        TWMediaPlayer tWMediaPlayer = this.this$0.mMediaPlayer;
                                        C0654s unused9 = C0601U.f593jd;
                                        tWMediaPlayer.setMPPath(C0654s.f702Bd);
                                        this.this$0.seekTo(C0601U.f593jd.f719md);
                                        this.this$0.mo529Va();
                                    }
                                    this.this$0.m547L(false);
                                }
                            }
                        }
                    }
                    for (InterfaceC0656a interfaceC0656a : C0601U.f592hi) {
                        if (C0601U.f593jd.f726ud != null) {
                            interfaceC0656a.mo721a(C0601U.f593jd.f726ud);
                        }
                        interfaceC0656a.mo722a(this.this$0.mMediaPlayer);
                    }
                    return false;
                case 65281:
                    if (this.this$0.isPlaying()) {
                        int duration = this.this$0.mMediaPlayer.getDuration();
                        int currentPosition = this.this$0.mMediaPlayer.getCurrentPosition();
                        C0601U.f593jd.f719md = currentPosition;
                        C0601U.f593jd.mDuration = duration;
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
                        C0601U.f593jd.f719md = currentPosition;
                        Iterator it3 = C0601U.f592hi.iterator();
                        while (it3.hasNext()) {
                            ((InterfaceC0656a) it3.next()).mo728d(C0601U.f593jd.f719md, duration);
                        }
                        int i9 = (currentPosition * 100) / duration;
                        C0654s c0654s3 = C0601U.f593jd;
                        C0654s unused10 = C0601U.f593jd;
                        int i10 = C0654s.f701Ad + 1;
                        C0654s unused11 = C0601U.f593jd;
                        c0654s3.m779b(1, i10, C0654s.f704Dd.f545kk, (i7 << 8) | (i8 << 16) | i6, i9);
                        int i11 = i9 & 127;
                        C0601U.f593jd.write(40704, 3, (this.this$0.isPlaying() ? 128 : 0) | i11);
                        C0601U.f593jd.write(771, 3, i11 | (this.this$0.isPlaying() ? 128 : 0));
                        Intent intent = new Intent("com.tw.launcher.music_progress_duration");
                        intent.putExtra("msg_music_progress", currentPosition);
                        intent.putExtra("msg_music_duration", duration);
                        this.this$0.mContext.sendBroadcast(intent);
                    }
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                    return false;
                case 65282:
                    if (C0601U.f593jd.mSource != 3) {
                        return false;
                    }
                    this.this$0.m549Re();
                    return false;
                case 65283:
                    if (C0601U.f593jd.mSource != 3) {
                        return false;
                    }
                    this.this$0.m551Se();
                    return false;
                case 65285:
                    C0601U.f593jd.m780b(C0601U.f593jd.f725td, "/mnt/sdcard");
                    for (InterfaceC0656a interfaceC0656a2 : C0601U.f592hi) {
                        if (C0601U.f593jd.f726ud != null) {
                            interfaceC0656a2.mo721a(C0601U.f593jd.f726ud);
                            interfaceC0656a2.mo719L();
                        }
                    }
                    return false;
                case 65287:
                    Log.d("MusicModel", "RESUME:activityResume:activityResume:" + this.this$0.f608wg);
                    if (!this.this$0.f608wg) {
                        return false;
                    }
                    if (this.this$0.f610xi) {
                        this.this$0.m587Wb();
                    }
                    Log.d("MusicModel", "RESUME:isPlaying:" + this.this$0.isPlaying() + " mTW.mSource:" + C0601U.f593jd.mSource + " isInitiativePause:" + this.this$0.f601Th);
                    if (!this.this$0.isPlaying() && C0601U.f593jd.mSource == 3) {
                        if (this.this$0.f601Th) {
                            C0654s unused12 = C0601U.f593jd;
                            if (C0654s.f710Jd) {
                                C0654s unused13 = C0601U.f593jd;
                                if (C0654s.f702Bd != null) {
                                    TWMediaPlayer tWMediaPlayer2 = this.this$0.mMediaPlayer;
                                    C0654s unused14 = C0601U.f593jd;
                                    tWMediaPlayer2.setMPPath(C0654s.f702Bd);
                                    this.this$0.seekTo(C0601U.f593jd.f719md);
                                    Log.w("MusicModel", "playMusic()--5:");
                                    this.this$0.mo529Va();
                                }
                            }
                        }
                    }
                    this.this$0.mHandler.sendEmptyMessage(40454);
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                    return false;
                case 65288:
                    Context context2 = this.this$0.mContext;
                    C0654s unused15 = C0601U.f593jd;
                    C0636a.m744b(context2, C0654s.f712Tc);
                    return false;
                case 65289:
                    this.this$0.mHandler.removeMessages(65289);
                    this.this$0.m550Sa();
                    return false;
                case 65296:
                    this.this$0.mo539pb();
                    for (InterfaceC0656a interfaceC0656a3 : C0601U.f592hi) {
                        if (!this.this$0.isPlaying()) {
                            C0654s unused16 = C0601U.f593jd;
                            String str2 = C0654s.f702Bd;
                            C0654s unused17 = C0601U.f593jd;
                            String str3 = C0654s.f703Cd;
                            C0654s unused18 = C0601U.f593jd;
                            interfaceC0656a3.mo725b("", "", "", null, str2, str3, C0601U.f593jd.f726ud.f543ik + C0654s.f701Ad);
                            interfaceC0656a3.mo727c(this.this$0.isPlaying());
                            interfaceC0656a3.mo728d(0, 0);
                        }
                    }
                    return false;
                case 65297:
                    this.this$0.mMediaPlayer.stopPlayback();
                    this.this$0.mHandler.removeMessages(65281);
                    C0601U.f593jd.f720nd = "";
                    C0601U.f593jd.f721od = "";
                    C0601U.f593jd.f722pd = "";
                    this.this$0.m554Xe();
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
