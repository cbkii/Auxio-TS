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

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.d.V */
/* JADX INFO: compiled from: MusicModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0602V implements Handler.Callback {
    final /* synthetic */ C0610ba this$0;

    C0602V(C0610ba c0610ba) {
        this.this$0 = c0610ba;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0145 A[Catch: Exception -> 0x0622, TryCatch #1 {Exception -> 0x0622, blocks: (B:3:0x0009, B:4:0x001b, B:6:0x0020, B:7:0x0054, B:8:0x0061, B:10:0x0067, B:12:0x0075, B:13:0x00a5, B:14:0x00b5, B:15:0x00c5, B:17:0x00e7, B:19:0x00ef, B:20:0x00f4, B:22:0x0130, B:24:0x0138, B:26:0x013e, B:28:0x0145, B:30:0x0152, B:31:0x0167, B:32:0x0184, B:34:0x019b, B:35:0x01a0, B:36:0x01a8, B:38:0x01ae, B:40:0x01bc, B:41:0x01c9, B:43:0x01d1, B:44:0x01d8, B:46:0x01e0, B:47:0x01e7, B:49:0x01ef, B:56:0x0218, B:57:0x0232, B:59:0x0238, B:60:0x0249, B:64:0x0281, B:68:0x0299, B:69:0x02b7, B:70:0x02cb, B:76:0x02d8, B:78:0x02de, B:79:0x02e9, B:96:0x0371, B:98:0x0378, B:100:0x0383, B:102:0x0387, B:103:0x0395, B:105:0x03c0, B:107:0x03ca, B:109:0x03d7, B:111:0x03df, B:112:0x03ef, B:113:0x03f5, B:114:0x03fd, B:116:0x0403, B:118:0x0411, B:119:0x041a, B:80:0x02f8, B:82:0x02fc, B:84:0x0321, B:86:0x0325, B:87:0x032d, B:83:0x0310, B:88:0x0335, B:90:0x0339, B:92:0x035e, B:94:0x0362, B:95:0x036a, B:91:0x034d, B:120:0x0424, B:121:0x0434, B:122:0x0436, B:124:0x043b, B:125:0x0443, B:126:0x044a, B:127:0x0451, B:128:0x0458, B:129:0x0460, B:130:0x0467, B:132:0x046f, B:133:0x0476, B:135:0x047e, B:136:0x0485, B:137:0x048c, B:138:0x0493, B:139:0x04a9, B:141:0x04b1, B:142:0x04b8, B:150:0x0507, B:153:0x0511, B:161:0x0534, B:163:0x053c, B:165:0x0544, B:166:0x054a, B:167:0x0550, B:168:0x055c, B:170:0x056a, B:172:0x0572, B:173:0x0579, B:175:0x058c, B:177:0x0593, B:178:0x059a, B:180:0x05a2, B:182:0x05aa, B:183:0x05b1, B:184:0x05b9, B:186:0x05bf, B:190:0x05cf, B:191:0x05d3, B:193:0x05e9, B:195:0x05ed, B:196:0x05f2, B:198:0x05f6, B:200:0x05fa, B:201:0x0601, B:205:0x060a, B:206:0x0612, B:208:0x0618, B:149:0x04ed, B:144:0x04bf, B:146:0x04c7), top: B:216:0x0009, inners: #0 }] */
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
                    Iterator it = C0610ba.f615hi.iterator();
                    while (it.hasNext()) {
                        ((InterfaceC0656a) it.next()).mo735q(z);
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
                    Iterator it2 = C0610ba.f615hi.iterator();
                    while (it2.hasNext()) {
                        ((InterfaceC0656a) it2.next()).mo729f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                    }
                    return false;
                case 769:
                    C0610ba.f616jd.mSource = message.arg1 & 255;
                    if (C0610ba.f616jd.mSource == 9) {
                        C0654s unused = C0610ba.f616jd;
                        if (C0654s.f710Jd) {
                            this.this$0.m635Tb();
                            return false;
                        }
                    }
                    if (C0610ba.f616jd.mSource == 3 || !this.this$0.isPlaying()) {
                        return false;
                    }
                    this.this$0.m636Ua();
                    return false;
                case 770:
                    if (C0610ba.f616jd.mSource != 3) {
                        return false;
                    }
                    int i = message.arg1;
                    Log.d("MusicModel", "BT_CALL STATE:" + i);
                    if (i == 0) {
                        this.this$0.f620Cg = false;
                        if (!this.this$0.f624Sh || this.this$0.f623Rh) {
                            return false;
                        }
                        this.this$0.mo529Va();
                        return false;
                    }
                    if (i != 1 && i != 2 && i != 3 && i != 4) {
                        return false;
                    }
                    if (!this.this$0.f620Cg) {
                        if (this.this$0.isPlaying()) {
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
                        Log.d("MusicModel", "music XTL: " + this.this$0.f621Eh);
                        return false;
                    } catch (Exception e) {
                        Log.e("MusicModel", "handleMessage: 0x0510:" + e.getMessage());
                        return false;
                    }
                case 40451:
                    switch (message.arg1) {
                        case 1:
                            if (this.this$0.isPlaying()) {
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
                            if (this.this$0.isPlaying()) {
                                return false;
                            }
                            this.this$0.mo529Va();
                            return false;
                        case 6:
                            if (!this.this$0.isPlaying()) {
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
                    int i2 = message.arg1;
                    if (i2 == 1) {
                        str = C0610ba.f614Gd ? "/mnt/" + message.obj : "/storage/" + message.obj;
                        if (message.arg2 == 0) {
                            C0610ba.f616jd.m786ra(str);
                        } else {
                            C0610ba.f616jd.m784pa(str);
                        }
                    } else if (i2 == 2) {
                        str = C0610ba.f614Gd ? "/mnt/usbhost/" + message.obj : "/storage/" + message.obj;
                        if (message.arg2 == 0) {
                            C0610ba.f616jd.m787sa(str);
                        } else {
                            C0610ba.f616jd.m785qa(str);
                        }
                    } else if (i2 == 3) {
                        str = "/mnt/sdcard/iNand";
                        if (message.arg2 == 0) {
                            C0610ba.f616jd.f725td.m453wc();
                        } else {
                            C0610ba.f616jd.m780b(C0610ba.f616jd.f725td, "/mnt/sdcard/iNand");
                        }
                    }
                    C0654s unused2 = C0610ba.f616jd;
                    if (C0654s.f703Cd != null) {
                        C0654s unused3 = C0610ba.f616jd;
                        if (C0654s.f703Cd.startsWith(str)) {
                            if (message.arg2 == 0) {
                                C0654s unused4 = C0610ba.f616jd;
                                C0654s.f704Dd.m453wc();
                                this.this$0.m635Tb();
                            } else {
                                C0654s c0654s = C0610ba.f616jd;
                                Context context = this.this$0.mContext;
                                C0654s unused5 = C0610ba.f616jd;
                                C0580g c0580g = C0654s.f704Dd;
                                C0654s unused6 = C0610ba.f616jd;
                                c0654s.m778a(context, c0580g, C0654s.f703Cd);
                                C0654s c0654s2 = C0610ba.f616jd;
                                C0654s unused7 = C0610ba.f616jd;
                                c0654s2.m783ea(C0654s.f701Ad);
                                if (!this.this$0.isPlaying() && C0610ba.f616jd.getService() == 3) {
                                    C0610ba c0610ba = this.this$0;
                                    C0654s unused8 = C0610ba.f616jd;
                                    if (c0610ba.m630zb(C0654s.f702Bd) == 0 && !this.this$0.isPlaying()) {
                                        this.this$0.seekTo(C0610ba.f616jd.f719md);
                                        this.this$0.mo529Va();
                                    }
                                    this.this$0.m591L(false);
                                }
                            }
                        }
                    }
                    for (InterfaceC0656a interfaceC0656a : C0610ba.f615hi) {
                        if (C0610ba.f616jd.f726ud != null) {
                            interfaceC0656a.mo721a(C0610ba.f616jd.f726ud);
                        }
                        interfaceC0656a.mo720a(this.this$0.m632Mb());
                    }
                    return false;
                case 65281:
                    if (this.this$0.isPlaying()) {
                        int duration = this.this$0.m632Mb().getDuration();
                        int currentPosition = this.this$0.m632Mb().getCurrentPosition();
                        C0610ba.f616jd.f719md = currentPosition;
                        C0610ba.f616jd.mDuration = duration;
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
                        C0610ba.f616jd.f719md = currentPosition;
                        Iterator it3 = C0610ba.f615hi.iterator();
                        while (it3.hasNext()) {
                            ((InterfaceC0656a) it3.next()).mo728d(C0610ba.f616jd.f719md, duration);
                        }
                        int i9 = (currentPosition * 100) / duration;
                        C0654s c0654s3 = C0610ba.f616jd;
                        C0654s unused9 = C0610ba.f616jd;
                        int i10 = C0654s.f701Ad + 1;
                        C0654s unused10 = C0610ba.f616jd;
                        c0654s3.m779b(1, i10, C0654s.f704Dd.f545kk, (i7 << 8) | (i8 << 16) | i6, i9);
                        int i11 = i9 & 127;
                        C0610ba.f616jd.write(40704, 3, (this.this$0.isPlaying() ? 128 : 0) | i11);
                        C0610ba.f616jd.write(771, 3, i11 | (this.this$0.isPlaying() ? 128 : 0));
                        Intent intent = new Intent("com.tw.launcher.music_progress_duration");
                        intent.putExtra("msg_music_progress", currentPosition);
                        intent.putExtra("msg_music_duration", duration);
                        this.this$0.mContext.sendBroadcast(intent);
                    }
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.mHandler.sendEmptyMessageDelayed(65281, 1000L);
                    return false;
                case 65282:
                    if (C0610ba.f616jd.mSource != 3) {
                        return false;
                    }
                    this.this$0.m593Re();
                    return false;
                case 65283:
                    if (C0610ba.f616jd.mSource != 3) {
                        return false;
                    }
                    this.this$0.m595Se();
                    return false;
                case 65285:
                    C0610ba.f616jd.m780b(C0610ba.f616jd.f725td, "/mnt/sdcard");
                    if (C0610ba.f616jd.f726ud != null) {
                        this.this$0.m599Ze();
                    }
                    for (InterfaceC0656a interfaceC0656a2 : C0610ba.f615hi) {
                        if (C0610ba.f616jd.f726ud != null) {
                            interfaceC0656a2.mo721a(C0610ba.f616jd.f726ud);
                            interfaceC0656a2.mo719L();
                        }
                    }
                    return false;
                case 65287:
                    Log.d("MusicModel", "RESUME:activityResume:activityResume:" + this.this$0.f630wg);
                    if (!this.this$0.f630wg) {
                        return false;
                    }
                    if (this.this$0.f632xi) {
                        this.this$0.m638Wb();
                    }
                    Log.d("MusicModel", "RESUME:isPlaying:" + this.this$0.isPlaying() + " mTW.mSource:" + C0610ba.f616jd.mSource + " isInitiativePause:" + this.this$0.f625Th);
                    if (!this.this$0.isPlaying() && C0610ba.f616jd.mSource == 3) {
                        if (this.this$0.f625Th) {
                            C0654s unused11 = C0610ba.f616jd;
                            if (C0654s.f710Jd) {
                                C0610ba c0610ba2 = this.this$0;
                                C0654s unused12 = C0610ba.f616jd;
                                if (c0610ba2.m630zb(C0654s.f702Bd) == 0) {
                                    this.this$0.seekTo(C0610ba.f616jd.f719md);
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
                    C0654s unused13 = C0610ba.f616jd;
                    C0636a.m744b(context2, C0654s.f712Tc);
                    return false;
                case 65289:
                    this.this$0.mHandler.removeMessages(65289);
                    this.this$0.m594Sa();
                    return false;
                case 65296:
                    this.this$0.mo539pb();
                    for (InterfaceC0656a interfaceC0656a3 : C0610ba.f615hi) {
                        if (!this.this$0.isPlaying()) {
                            C0654s unused14 = C0610ba.f616jd;
                            String str2 = C0654s.f702Bd;
                            C0654s unused15 = C0610ba.f616jd;
                            String str3 = C0654s.f703Cd;
                            C0654s unused16 = C0610ba.f616jd;
                            interfaceC0656a3.mo725b("", "", "", null, str2, str3, C0610ba.f616jd.f726ud.f543ik + C0654s.f701Ad);
                            interfaceC0656a3.mo727c(this.this$0.isPlaying());
                            interfaceC0656a3.mo728d(0, 0);
                        }
                    }
                    return false;
                case 65297:
                    this.this$0.m632Mb().stop();
                    this.this$0.mHandler.removeMessages(65281);
                    this.this$0.m632Mb().reset();
                    C0610ba.f616jd.f720nd = "";
                    C0610ba.f616jd.f721od = "";
                    C0610ba.f616jd.f722pd = "";
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
