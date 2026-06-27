package com.eckom.xtlibrary.p020b.p021a.p025d;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.tw.john.PinyinConv;
import android.tw.john.TWUtil;
import android.util.Log;
import com.eckom.xtlibrary.p020b.C0556b;
import com.eckom.xtlibrary.p020b.p021a.p023b.C0533a;
import com.eckom.xtlibrary.p020b.p021a.p024c.C0537b;
import com.eckom.xtlibrary.p020b.p053j.C0685a;
import com.eckom.xtlibrary.p020b.p053j.C0686b;
import com.eckom.xtlibrary.p020b.p053j.C0699o;
import com.eckom.xtlibrary.twproject.p059bt.bean.C0718b;
import com.eckom.xtlibrary.twproject.p059bt.bean.C0719c;
import com.eckom.xtlibrary.twproject.p059bt.bean.TWContact;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.a.d.c */
/* JADX INFO: compiled from: BTModel.java */
/* JADX INFO: loaded from: classes3.dex */
class C0541c implements Handler.Callback {
    final /* synthetic */ C0544f this$0;

    C0541c(C0544f c0544f) {
        this.this$0 = c0544f;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:156:0x04d4  */
    /* JADX WARN: Removed duplicated region for block: B:306:0x0918 A[Catch: Exception -> 0x1924, TryCatch #7 {Exception -> 0x1924, blocks: (B:3:0x0007, B:5:0x000d, B:11:0x0032, B:33:0x006c, B:35:0x0076, B:37:0x007a, B:38:0x0085, B:40:0x008f, B:42:0x0099, B:43:0x009d, B:46:0x00ac, B:48:0x00ba, B:50:0x00e4, B:51:0x00f5, B:52:0x00f8, B:54:0x00fc, B:57:0x010b, B:59:0x0119, B:61:0x0143, B:62:0x0154, B:63:0x0157, B:65:0x015b, B:68:0x016a, B:70:0x0178, B:72:0x01a2, B:73:0x01b3, B:74:0x01b6, B:75:0x01b9, B:76:0x01ca, B:78:0x01d0, B:79:0x01e0, B:80:0x0200, B:82:0x0206, B:83:0x021e, B:85:0x0228, B:86:0x022f, B:87:0x0236, B:89:0x023e, B:91:0x0242, B:99:0x0272, B:96:0x024f, B:97:0x025b, B:98:0x0267, B:100:0x0285, B:101:0x0292, B:105:0x029a, B:106:0x02da, B:108:0x02e0, B:109:0x02fc, B:110:0x0303, B:111:0x0311, B:113:0x0317, B:114:0x0327, B:116:0x034f, B:117:0x0356, B:118:0x035d, B:120:0x0367, B:121:0x037e, B:122:0x038c, B:124:0x0392, B:125:0x03aa, B:127:0x03b4, B:128:0x03bf, B:129:0x0411, B:131:0x0417, B:132:0x0437, B:134:0x043f, B:135:0x046a, B:136:0x0478, B:138:0x047e, B:139:0x0498, B:141:0x049e, B:144:0x04a6, B:145:0x04b2, B:185:0x0635, B:161:0x04dd, B:163:0x04e7, B:165:0x0531, B:166:0x053f, B:168:0x0545, B:164:0x050c, B:169:0x0555, B:171:0x055f, B:173:0x05a1, B:174:0x05af, B:176:0x05b5, B:172:0x0580, B:177:0x05c5, B:179:0x05cf, B:181:0x0611, B:182:0x061f, B:184:0x0625, B:180:0x05f0, B:147:0x04b6, B:150:0x04c0, B:153:0x04ca, B:188:0x0663, B:190:0x066b, B:191:0x069b, B:192:0x06a3, B:193:0x06b1, B:195:0x06b7, B:204:0x06f8, B:206:0x071a, B:208:0x0724, B:210:0x072e, B:214:0x074b, B:218:0x076a, B:219:0x077b, B:221:0x0781, B:211:0x073b, B:213:0x073f, B:222:0x0793, B:223:0x07a1, B:225:0x07a7, B:229:0x07bd, B:230:0x07c1, B:244:0x07f3, B:308:0x0929, B:309:0x0937, B:311:0x093d, B:315:0x0953, B:246:0x07f8, B:247:0x0802, B:248:0x080c, B:250:0x081f, B:252:0x0823, B:267:0x0856, B:269:0x086c, B:255:0x082e, B:258:0x0839, B:260:0x083e, B:263:0x084a, B:270:0x0873, B:271:0x087d, B:273:0x0887, B:283:0x08a0, B:285:0x08a5, B:286:0x08b5, B:289:0x08c6, B:291:0x08cb, B:292:0x08d7, B:293:0x08e3, B:297:0x08f1, B:299:0x08f6, B:300:0x08fd, B:301:0x0903, B:305:0x0911, B:306:0x0918, B:307:0x0921, B:316:0x0957, B:318:0x0963, B:326:0x0989, B:330:0x0993, B:331:0x09a1, B:333:0x09a7, B:319:0x096d, B:321:0x0974, B:323:0x097a, B:325:0x0980, B:349:0x0a51, B:351:0x0a5b, B:352:0x0aa4, B:354:0x0aaa, B:355:0x0abe, B:356:0x0ade, B:357:0x0afe, B:358:0x0b0c, B:360:0x0b12, B:361:0x0b24, B:363:0x0b2c, B:364:0x0b39, B:365:0x0b47, B:367:0x0b4d, B:371:0x0b69, B:372:0x0b6d, B:374:0x0b7b, B:387:0x0c21, B:396:0x0c81, B:386:0x0c07, B:397:0x0c99, B:399:0x0ca1, B:400:0x0caf, B:402:0x0cb5, B:403:0x0cc5, B:404:0x0ccd, B:405:0x0cdb, B:407:0x0ce1, B:409:0x0cf5, B:414:0x0d1a, B:415:0x0d28, B:417:0x0d2e, B:419:0x0d3e, B:424:0x0d63, B:425:0x0d71, B:427:0x0d77, B:428:0x0d87, B:429:0x0d95, B:431:0x0d9b, B:432:0x0dad, B:434:0x0db5, B:435:0x0de4, B:437:0x0dea, B:438:0x0dfa, B:442:0x0e02, B:444:0x0e0d, B:446:0x0e10, B:447:0x0e2f, B:449:0x0e35, B:450:0x0e45, B:451:0x0e64, B:453:0x0e6a, B:454:0x0e7a, B:455:0x0e9a, B:457:0x0ea0, B:458:0x0eb0, B:462:0x0ebd, B:464:0x0ec7, B:465:0x0ef0, B:469:0x0f06, B:470:0x0f1f, B:472:0x0f25, B:476:0x0f38, B:477:0x0f3c, B:478:0x0f4a, B:480:0x0f50, B:481:0x0f62, B:484:0x0f78, B:488:0x0fa3, B:513:0x1069, B:515:0x1071, B:520:0x10a1, B:522:0x10a9, B:523:0x10b4, B:525:0x10bc, B:516:0x107b, B:517:0x1089, B:519:0x108f, B:498:0x0fba, B:500:0x0fc5, B:501:0x0fd7, B:502:0x0fe2, B:503:0x1003, B:504:0x1027, B:506:0x1032, B:507:0x103a, B:509:0x1042, B:511:0x104e, B:512:0x1057, B:485:0x0f89, B:487:0x0f8d, B:526:0x10c9, B:527:0x10cb, B:531:0x10d3, B:533:0x10dd, B:535:0x10ef, B:537:0x1176, B:538:0x1184, B:540:0x118a, B:541:0x119a, B:536:0x10fb, B:203:0x06de, B:348:0x0a37, B:413:0x0d04, B:423:0x0d4d, B:551:0x120e, B:555:0x1268, B:556:0x12ab, B:558:0x12b1, B:559:0x12d1, B:560:0x12df, B:562:0x12e5, B:563:0x130d, B:565:0x1315, B:550:0x11e6, B:566:0x1340, B:567:0x134e, B:569:0x1354, B:570:0x1364, B:571:0x1372, B:573:0x1378, B:574:0x138a, B:576:0x138e, B:578:0x1396, B:580:0x13a2, B:586:0x13d9, B:587:0x13e7, B:589:0x13ed, B:581:0x13ac, B:583:0x13c4, B:585:0x13d0, B:590:0x13ff, B:595:0x140c, B:597:0x1414, B:598:0x1422, B:600:0x1428, B:601:0x1438, B:602:0x1461, B:604:0x1478, B:605:0x1483, B:607:0x1491, B:608:0x149c, B:609:0x14aa, B:611:0x14b0, B:612:0x14c0, B:613:0x14e1, B:615:0x14e9, B:616:0x14f7, B:618:0x14fd, B:619:0x150d, B:621:0x1542, B:623:0x1548, B:624:0x154c, B:626:0x1563, B:627:0x156e, B:629:0x157c, B:630:0x1587, B:631:0x1595, B:633:0x159b, B:634:0x15ab, B:635:0x15cd, B:637:0x15e2, B:638:0x15ed, B:639:0x15fb, B:641:0x1601, B:642:0x1613, B:644:0x161e, B:646:0x162e, B:648:0x1642, B:649:0x1649, B:651:0x1655, B:653:0x1669, B:654:0x1676, B:655:0x16f5, B:656:0x1703, B:658:0x1709, B:659:0x171b, B:661:0x1723, B:662:0x1736, B:664:0x173a, B:666:0x1740, B:668:0x1758, B:669:0x1763, B:671:0x1777, B:673:0x1781, B:674:0x1792, B:676:0x17ac, B:678:0x17bc, B:679:0x17c1, B:681:0x17c9, B:682:0x17d2, B:706:0x18a4, B:707:0x18b2, B:709:0x18b8, B:710:0x18ca, B:712:0x18d0, B:714:0x18de, B:716:0x18ee, B:718:0x18fc, B:683:0x17e3, B:685:0x17e8, B:687:0x17fb, B:689:0x1805, B:691:0x1817, B:693:0x1821, B:694:0x1832, B:696:0x184c, B:698:0x185c, B:699:0x1861, B:701:0x1869, B:702:0x1872, B:703:0x1881, B:705:0x1894, B:6:0x001b, B:8:0x0021, B:9:0x002b, B:390:0x0c32, B:392:0x0c78, B:197:0x06c9, B:199:0x06d1, B:376:0x0b8b, B:378:0x0b9b, B:379:0x0bac, B:381:0x0bd1, B:382:0x0bd7, B:335:0x09b7, B:337:0x09d8, B:339:0x09de, B:340:0x09f4, B:342:0x09fa, B:344:0x0a00, B:543:0x11af, B:545:0x11b8, B:546:0x11c9), top: B:739:0x0007, inners: #0, #1, #2, #3, #4, #5, #6 }] */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleMessage(Message message) throws Throwable {
        String str;
        String strTrim;
        int i;
        int i2;
        boolean z;
        byte b2;
        try {
            if (message.obj instanceof TWUtil.TWObject) {
                TWUtil.TWObject tWObject = (TWUtil.TWObject) message.obj;
                String str2 = (String) tWObject.obj3;
                strTrim = (String) tWObject.obj4;
                str = str2;
            } else {
                str = message.obj instanceof byte[] ? new String((byte[]) message.obj) : (String) message.obj;
                strTrim = null;
            }
            i = message.what;
        } catch (Exception e) {
            Log.e("BTModel", "handleMessage: msg.what:" + message.what + " Error:" + e.getMessage());
            return true;
        }
        if (i == 4) {
            if (message.arg1 == 0) {
                if (!TextUtils.isEmpty(str)) {
                    this.this$0.f425la.f409yg = str;
                    if (this.this$0.f425la.f398ng.contains("KED18-0395")) {
                        this.this$0.f425la.f392fh.clear();
                    }
                }
                this.this$0.f427wh.write(3, 2);
                if (this.this$0.f425la.f409yg != null) {
                    if (this.this$0.f425la.f374Mg) {
                        C0537b.m207a(this.this$0.mContext, this.this$0.f425la.f409yg);
                    }
                    if (!TextUtils.equals(this.this$0.f425la.f409yg, C0699o.m1032c(this.this$0.mContext, "BTModel", "ConnectDeviceMac"))) {
                        if (!this.this$0.f425la.f398ng.contains("KED18-0395")) {
                            this.this$0.m214Le();
                        }
                        if (this.this$0.f428xh != null) {
                            this.this$0.f428xh.m190fb();
                        }
                    }
                    this.this$0.m262tb();
                    this.this$0.m265wb();
                    this.this$0.m264vb();
                }
            } else {
                if (message.arg1 == 1) {
                    C0533a.getInstance().f388_g.clear();
                    if (this.this$0.f425la.f403sg == 2 && this.this$0.f425la.f409yg == null) {
                        this.this$0.f425la.f409yg = str;
                        if (this.this$0.f425la.f409yg != null) {
                            if (this.this$0.f425la.f374Mg) {
                                C0537b.m207a(this.this$0.mContext, this.this$0.f425la.f409yg);
                            }
                            if (!TextUtils.equals(this.this$0.f425la.f409yg, C0699o.m1032c(this.this$0.mContext, "BTModel", "ConnectDeviceMac"))) {
                                if (!this.this$0.f425la.f398ng.contains("KED18-0395")) {
                                    this.this$0.m214Le();
                                }
                                if (this.this$0.f428xh != null) {
                                    this.this$0.f428xh.m190fb();
                                }
                            }
                            this.this$0.m262tb();
                            this.this$0.m265wb();
                            this.this$0.m264vb();
                        }
                    }
                }
                if (!this.this$0.f425la.f388_g.contains(new C0718b(strTrim, str))) {
                    this.this$0.f425la.f388_g.add(new C0718b(strTrim, str));
                }
            }
            Iterator it = this.this$0.f429yh.entrySet().iterator();
            while (it.hasNext()) {
                ((InterfaceC0545g) ((Map.Entry) it.next()).getValue()).mo282a(message.arg1, strTrim, str);
            }
            if (TextUtils.isEmpty(str) || !TextUtils.equals(this.this$0.f425la.f409yg, str)) {
                return true;
            }
            this.this$0.f425la.f408xg = strTrim;
            if (this.this$0.f428xh == null || TextUtils.isEmpty(this.this$0.f425la.f408xg)) {
                return true;
            }
            this.this$0.f428xh.m195y(this.this$0.f425la.f408xg, this.this$0.f425la.f409yg);
            this.this$0.m253a(true, this.this$0.f425la.f408xg);
            return true;
        }
        if (i != 7) {
            if (i == 23) {
                this.this$0.f425la.f404tg = message.arg1;
                if (1 == this.this$0.f425la.f404tg) {
                    C0699o.m1027a(this.this$0.mContext, "BTModel", "ConnectDeviceMac", "");
                }
                Iterator it2 = this.this$0.f429yh.entrySet().iterator();
                while (it2.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it2.next()).getValue()).mo273M(message.arg1);
                }
                if (this.this$0.f425la.f404tg == 1 || !this.this$0.f425la.f398ng.contains("KED18-0395") || !C0699o.m1031b(this.this$0.mContext, "BTModel", this.this$0.f425la.f409yg)) {
                    return true;
                }
                this.this$0.m213Ke();
                return true;
            }
            if (i == 24) {
                int i3 = (message.arg1 >> 24) & 255;
                if (i3 != 0) {
                    if (i3 != 1) {
                        return true;
                    }
                    if ((message.arg1 & ViewCompat.MEASURED_SIZE_MASK) == 0) {
                        Iterator it3 = this.this$0.f429yh.entrySet().iterator();
                        while (it3.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it3.next()).getValue()).mo274N();
                        }
                        this.this$0.f425la.f391eh.clear();
                        Message messageObtainMessage = this.this$0.mHandler.obtainMessage();
                        messageObtainMessage.what = 65286;
                        messageObtainMessage.arg1 = 0;
                        messageObtainMessage.arg2 = 1;
                        this.this$0.mHandler.removeMessages(65286);
                        this.this$0.mHandler.sendMessage(messageObtainMessage);
                    }
                    TWContact tWContact = new TWContact(strTrim, str, PinyinConv.cn2py(strTrim));
                    if (!this.this$0.f425la.f391eh.contains(tWContact)) {
                        this.this$0.f425la.f391eh.add(tWContact);
                    }
                    if (!this.this$0.f425la.f392fh.contains(tWContact)) {
                        this.this$0.f425la.f392fh.add(tWContact);
                    }
                    Iterator it4 = this.this$0.f429yh.entrySet().iterator();
                    while (it4.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it4.next()).getValue()).mo302o(strTrim, str);
                    }
                    Message messageObtainMessage2 = this.this$0.mHandler.obtainMessage();
                    messageObtainMessage2.what = 65286;
                    messageObtainMessage2.arg1 = 1;
                    messageObtainMessage2.arg2 = 1;
                    this.this$0.mHandler.removeMessages(65286);
                    this.this$0.mHandler.sendMessageDelayed(messageObtainMessage2, 2000L);
                    return true;
                }
                if ((message.arg1 & ViewCompat.MEASURED_SIZE_MASK) == 0) {
                    Iterator it5 = this.this$0.f429yh.entrySet().iterator();
                    while (it5.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it5.next()).getValue()).mo280X();
                    }
                    this.this$0.f425la.f390dh.clear();
                    this.this$0.f425la.f392fh.clear();
                    Message messageObtainMessage3 = this.this$0.mHandler.obtainMessage();
                    messageObtainMessage3.what = 65286;
                    messageObtainMessage3.arg1 = 0;
                    messageObtainMessage3.arg2 = 0;
                    this.this$0.mHandler.removeMessages(65286);
                    this.this$0.mHandler.sendMessage(messageObtainMessage3);
                }
                if (strTrim != null) {
                    strTrim = strTrim.trim();
                }
                if (str != null) {
                    str = str.trim();
                }
                TWContact tWContact2 = new TWContact(strTrim, str, PinyinConv.cn2py(strTrim));
                if (!this.this$0.f425la.f390dh.contains(tWContact2)) {
                    this.this$0.f425la.f390dh.add(tWContact2);
                }
                if (!this.this$0.f425la.f392fh.contains(tWContact2)) {
                    this.this$0.f425la.f392fh.add(tWContact2);
                }
                Iterator it6 = this.this$0.f429yh.entrySet().iterator();
                while (it6.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it6.next()).getValue()).mo305q(strTrim, str);
                }
                Message messageObtainMessage4 = this.this$0.mHandler.obtainMessage();
                messageObtainMessage4.what = 65286;
                messageObtainMessage4.arg1 = 1;
                messageObtainMessage4.arg2 = 0;
                this.this$0.mHandler.removeMessages(65286);
                this.this$0.mHandler.sendMessageDelayed(messageObtainMessage4, 3000L);
                return true;
            }
            if (i == 47) {
                if (message.arg1 != 0) {
                    this.this$0.f427wh.write(770, 2);
                    this.this$0.m221Qe();
                    if (this.this$0.mMediaPlayer != null && !this.this$0.mMediaPlayer.isPlaying()) {
                        this.this$0.mMediaPlayer.start();
                    }
                } else if (this.this$0.mMediaPlayer != null && this.this$0.mMediaPlayer.isPlaying()) {
                    this.this$0.mMediaPlayer.pause();
                }
                Iterator it7 = this.this$0.f429yh.entrySet().iterator();
                while (it7.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it7.next()).getValue()).mo272J(message.arg1);
                }
                return true;
            }
            if (i == 48) {
                Iterator it8 = this.this$0.f429yh.entrySet().iterator();
                while (it8.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it8.next()).getValue()).mo270H(message.arg1);
                }
                return true;
            }
            if (i == 54) {
                Iterator it9 = this.this$0.f429yh.entrySet().iterator();
                while (it9.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it9.next()).getValue()).mo288da(str);
                }
                return true;
            }
            if (i == 55) {
                try {
                    int iLastIndexOf = strTrim.lastIndexOf("-#");
                    if (iLastIndexOf == -1) {
                        this.this$0.f425la.f371Jg = strTrim;
                        this.this$0.f425la.f370Ig = "";
                    } else {
                        this.this$0.f425la.f371Jg = strTrim.substring(0, iLastIndexOf);
                        this.this$0.f425la.f370Ig = strTrim.substring(iLastIndexOf + 2);
                    }
                } catch (Exception e2) {
                    Log.d("BTModel", "RETURN_ID3:" + e2.getMessage());
                    this.this$0.f425la.f371Jg = strTrim;
                    this.this$0.f425la.f370Ig = "";
                }
                this.this$0.f425la.f369Hg = str;
                Log.d("BTModel", "RETURN_ID3:musicTitle:" + this.this$0.f425la.f369Hg + " musicAlbum:" + this.this$0.f425la.f370Ig + " musicArtist:" + this.this$0.f425la.f371Jg);
                this.this$0.f427wh.write(40704, 8, this.this$0.f425la.f368Gg ? 1 : 0, this.this$0.f425la.f369Hg);
                this.this$0.m227b(0, this.this$0.f425la.f369Hg);
                this.this$0.mHandler.postDelayed(new RunnableC0539a(this), 100L);
                this.this$0.mHandler.postDelayed(new RunnableC0540b(this), 200L);
                Iterator it10 = this.this$0.f429yh.entrySet().iterator();
                while (it10.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it10.next()).getValue()).mo307r(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg);
                }
                Iterator it11 = this.this$0.f429yh.entrySet().iterator();
                while (it11.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it11.next()).getValue()).mo291e(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg, this.this$0.f425la.f370Ig);
                }
                if (this.this$0.f428xh == null) {
                    return true;
                }
                this.this$0.f428xh.m188a(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg, this.this$0.f425la.f373Lg, this.this$0.f425la.f372Kg);
                return true;
            }
            switch (i) {
                case 7:
                    break;
                case 9:
                    switch (message.arg1) {
                        case 0:
                        case 1:
                        default:
                            i2 = 0;
                            break;
                        case 2:
                            i2 = 1;
                            break;
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            i2 = 2;
                            break;
                    }
                    if (this.this$0.f425la.f403sg == i2) {
                        return true;
                    }
                    this.this$0.f425la.f403sg = i2;
                    if (this.this$0.f425la.f403sg == 2) {
                        this.this$0.f427wh.write(3, 1);
                    } else {
                        this.this$0.f425la.f364Cg = 0;
                        this.this$0.f425la.f408xg = null;
                        this.this$0.f425la.f409yg = null;
                        this.this$0.f425la.f390dh.clear();
                        this.this$0.f425la.f391eh.clear();
                        this.this$0.f425la.f392fh.clear();
                        this.this$0.f425la.f393gh.clear();
                        this.this$0.f425la.f394hh.clear();
                        this.this$0.f425la.f395ih.clear();
                        this.this$0.f425la.f396jh.clear();
                        this.this$0.f427wh.write(3, 2);
                        this.this$0.mHandler.sendEmptyMessage(MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                    }
                    Iterator it12 = this.this$0.f429yh.entrySet().iterator();
                    while (it12.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it12.next()).getValue()).mo303p(i2);
                    }
                    this.this$0.f427wh.write(1283, 16, this.this$0.f425la.f403sg);
                    return true;
                case 11:
                    this.this$0.f425la.f364Cg = message.arg1;
                    if (this.this$0.f425la.f364Cg != 0 && str != null) {
                        this.this$0.f425la.f400pg = strTrim;
                        this.this$0.f425la.f399og = str;
                    } else if (message.arg1 == 0) {
                        this.this$0.f425la.f400pg = null;
                        this.this$0.f425la.f399og = null;
                        this.this$0.m263ub();
                    }
                    int i4 = this.this$0.f425la.f364Cg;
                    if (i4 == 0) {
                        if (this.this$0.f425la.f365Dg != -1) {
                            this.this$0.f425la.f365Dg = -1;
                        }
                        if (this.this$0.mMediaPlayer != null && this.this$0.mMediaPlayer.isPlaying()) {
                            this.this$0.mMediaPlayer.pause();
                        }
                        this.this$0.f425la.f367Fg = 0;
                        this.this$0.f427wh.write(1283, 0);
                    } else if (i4 == 1) {
                        this.this$0.f425la.f399og = str;
                        this.this$0.f425la.f400pg = strTrim;
                        this.this$0.f425la.f367Fg = 0;
                        this.this$0.f427wh.write(1283, 1);
                    } else if (i4 == 2) {
                        this.this$0.f427wh.write(1283, 3);
                        this.this$0.mHandler.removeMessages(65284);
                        this.this$0.mHandler.sendEmptyMessageDelayed(65284, 500L);
                    } else if (i4 == 3) {
                        this.this$0.f427wh.write(1283, 2);
                    } else if (i4 == 4 && this.this$0.f425la.f365Dg == -1) {
                        this.this$0.f425la.f365Dg = this.this$0.f425la.f366Eg;
                    }
                    if ("++++".equals(str)) {
                        this.this$0.f430zh.show();
                    } else {
                        Iterator it13 = this.this$0.f429yh.entrySet().iterator();
                        while (it13.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it13.next()).getValue()).mo293f(message.arg1, strTrim, str);
                        }
                    }
                    if (this.this$0.f428xh != null) {
                        this.this$0.f428xh.m193i(message.arg1, strTrim, str);
                    }
                    if (C0556b.getInstant().f455cd == null) {
                        return true;
                    }
                    C0556b.getInstant().f455cd.mo153d(message.arg1, strTrim, str);
                    return true;
                case 13:
                    Iterator it14 = this.this$0.f429yh.entrySet().iterator();
                    while (it14.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it14.next()).getValue()).mo277Q(message.arg1);
                    }
                    return true;
                case 21:
                    this.this$0.f425la.f368Gg = message.arg1 != 0;
                    if (C0556b.getInstant().f455cd != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("dateType", "send");
                        bundle.putString("action", "com.tw.bt.av.play");
                        bundle.putBoolean("playState", this.this$0.f425la.f368Gg);
                        C0556b.getInstant().f455cd.mo142a(bundle);
                    }
                    this.this$0.f427wh.write(40704, 8, this.this$0.f425la.f368Gg ? 1 : 0, this.this$0.f425la.f369Hg);
                    Iterator it15 = this.this$0.f429yh.entrySet().iterator();
                    while (it15.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it15.next()).getValue()).mo298j(message.arg1 != 0);
                    }
                    return true;
                case 27:
                    if ((message.arg1 & ViewCompat.MEASURED_SIZE_MASK) == 0) {
                        this.this$0.f425la.f394hh.clear();
                        this.this$0.f425la.f395ih.clear();
                        this.this$0.f425la.f396jh.clear();
                        Iterator it16 = this.this$0.f429yh.entrySet().iterator();
                        while (it16.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it16.next()).getValue()).mo271I();
                        }
                    }
                    if (TextUtils.isEmpty(str)) {
                        return false;
                    }
                    int i5 = (message.arg1 >> 24) & 255;
                    if ((i5 & 1) != 1) {
                        this.this$0.f425la.f395ih.add(0, new C0719c(strTrim, str, 1));
                        Iterator it17 = this.this$0.f429yh.entrySet().iterator();
                        while (it17.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it17.next()).getValue()).mo301m(strTrim, str);
                        }
                        return true;
                    }
                    if ((i5 & 2) == 2) {
                        this.this$0.f425la.f394hh.add(0, new C0719c(strTrim, str, 0));
                        Iterator it18 = this.this$0.f429yh.entrySet().iterator();
                        while (it18.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it18.next()).getValue()).mo308s(strTrim, str);
                        }
                        return true;
                    }
                    this.this$0.f425la.f396jh.add(0, new C0719c(strTrim, str, 2));
                    Iterator it19 = this.this$0.f429yh.entrySet().iterator();
                    while (it19.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it19.next()).getValue()).mo304p(strTrim, str);
                    }
                    return true;
                case 29:
                    Iterator it20 = this.this$0.f429yh.entrySet().iterator();
                    while (it20.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it20.next()).getValue()).mo290e(message.arg1, strTrim, str);
                    }
                    return true;
                case 31:
                    try {
                        this.this$0.f425la.f410zg = (String) message.obj;
                        break;
                    } catch (ClassCastException e3) {
                        Log.e("BTModel", "" + e3.getMessage());
                    }
                    Iterator it21 = this.this$0.f429yh.entrySet().iterator();
                    while (it21.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it21.next()).getValue()).mo295ga(str);
                    }
                    return true;
                case 33:
                    try {
                        this.this$0.f425la.f362Ag = (String) message.obj;
                        break;
                    } catch (ClassCastException e4) {
                        Log.e("BTModel", "" + e4.getMessage());
                    }
                    Iterator it22 = this.this$0.f429yh.entrySet().iterator();
                    while (it22.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it22.next()).getValue()).mo300ka(str);
                    }
                    return true;
                case 35:
                    Iterator it23 = this.this$0.f429yh.entrySet().iterator();
                    while (it23.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it23.next()).getValue()).mo292f(message.arg1, message.arg2);
                    }
                    return true;
                case 45:
                    this.this$0.f425la.mVersionName = str;
                    if (!TextUtils.isEmpty(str)) {
                        if (this.this$0.f425la.mVersionName.startsWith("CQ")) {
                            try {
                                if (this.this$0.f425la.mVersionName.startsWith("CQ131")) {
                                    this.this$0.f425la.f379Rg = true;
                                    this.this$0.f425la.f387Zg = true;
                                }
                                String[] strArrSplit = str.split("_")[2].split("\\(");
                                this.this$0.f425la.f363Bg = strArrSplit[0];
                                if (C0685a.m995g("yyyy/MM/dd:HH:mm:ss", strArrSplit[0], "2021/06/02:00:00:00")) {
                                    this.this$0.f420Ah = true;
                                }
                                Log.d("BTModel", "RETURN_VERSION CQ: mVersionName:" + this.this$0.f425la.mVersionName + " mVersionDate:" + this.this$0.f425la.f363Bg);
                            } catch (Exception e5) {
                                Log.e("BTModel", "RETURN_VERSION: " + e5.getMessage());
                            }
                        } else if (!this.this$0.f425la.mVersionName.startsWith("FD")) {
                            try {
                                String str3 = str.split("_")[2];
                                this.this$0.f425la.f363Bg = str3;
                                Log.d("BTModel", "RETURN_VERSION GK: mVersionName:" + this.this$0.f425la.mVersionName + " mVersionDate:" + this.this$0.f425la.f363Bg);
                                if (C0685a.m995g("yyyy/MM/dd:HH:mm:ss", str3, "2021/05/26:00:00:00")) {
                                    this.this$0.f420Ah = true;
                                }
                            } catch (Exception e6) {
                                Log.e("BTModel", "RETURN_VERSION: " + e6.getMessage());
                            }
                        }
                        break;
                    }
                    if (!this.this$0.f422Ch) {
                        return true;
                    }
                    Iterator it24 = this.this$0.f429yh.entrySet().iterator();
                    while (it24.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it24.next()).getValue()).mo299ja(str);
                    }
                    this.this$0.f422Ch = false;
                    return true;
                case 50:
                    Iterator it25 = this.this$0.f429yh.entrySet().iterator();
                    while (it25.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it25.next()).getValue()).mo283a((message.arg1 + 1) * 10, message.arg1 >= 9);
                    }
                    return true;
                case 59:
                    Iterator it26 = this.this$0.f429yh.entrySet().iterator();
                    while (it26.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it26.next()).getValue()).mo285b(message.arg1, strTrim, str);
                    }
                    if (this.this$0.f428xh == null) {
                        return true;
                    }
                    this.this$0.f428xh.m191h(message.arg1, strTrim, str);
                    return true;
                case 61:
                    Log.d("BTModel", "RETURN_MIC_GAIN: " + message.arg1 + " " + message.arg2);
                    return true;
                case 63:
                    Log.d("BTModel", "RETURN_SPK_GAIN: " + message.arg1 + " " + message.arg2);
                    return true;
                case 65:
                    if (this.this$0.f425la.f403sg != 2) {
                        return true;
                    }
                    this.this$0.f427wh.write(1283, 16, (message.arg1 << 4) | message.arg2);
                    this.this$0.f425la.f382Ug = message.arg2;
                    Context context = this.this$0.mContext;
                    C0533a unused = this.this$0.f425la;
                    C0533a unused2 = this.this$0.f425la;
                    C0699o.m1026a(context, "TABLE_BT", "BATTERY_LEVEL", this.this$0.f425la.f382Ug);
                    Iterator it27 = this.this$0.f429yh.entrySet().iterator();
                    while (it27.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it27.next()).getValue()).mo296i(message.arg1, message.arg2);
                    }
                    return true;
                case 267:
                    try {
                        byte[] bArr = (byte[]) message.obj;
                        Log.d("BTModel", "0x010b: " + Arrays.toString(bArr));
                        if (bArr.length > 20 && bArr[20] > 0) {
                            this.this$0.f425la.f380Sg = true;
                            this.this$0.f425la.f381Tg = bArr[20] - 1;
                        }
                        if (!C0686b.m1006_c() && !C0686b.m1005Zc()) {
                            return true;
                        }
                        this.this$0.f425la.f380Sg = true;
                        String strM997Rc = C0686b.m997Rc();
                        this.this$0.f425la.f381Tg = Integer.getInteger(strM997Rc).intValue();
                        Log.d("mxy", "010b mic:" + strM997Rc);
                        return true;
                    } catch (Exception e7) {
                        Log.e("BTModel", "0x010b Error : " + e7.getMessage());
                        return true;
                    }
                case 274:
                    if (C0686b.m999Tc().contains("GT")) {
                        this.this$0.f425la.f379Rg = true;
                    } else if ((message.arg1 & 6) == 6 && (C0686b.m1005Zc() || C0686b.m1008ad())) {
                        this.this$0.f425la.f379Rg = true;
                    }
                    boolean z2 = (message.arg1 & 65536) == 65536;
                    Iterator it28 = this.this$0.f429yh.entrySet().iterator();
                    while (it28.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it28.next()).getValue()).mo306q(z2);
                    }
                    return true;
                case InputDeviceCompat.SOURCE_DPAD /* 513 */:
                    int i6 = message.arg2;
                    Log.d("BTModel", "handleMessage: 0x0201:keyCode:" + i6);
                    if (i6 == 9 || i6 == 10) {
                        boolean z3 = this.this$0.f425la.f407wg;
                    } else if (i6 == 24 || i6 == 25) {
                        if (this.this$0.f425la.f403sg == 2) {
                            int i7 = this.this$0.f425la.f364Cg;
                            if (i7 != 1) {
                                if (i7 == 2 || i7 == 3 || i7 == 4) {
                                    if (i6 != 24 || message.arg1 == 1) {
                                        this.this$0.f427wh.write(10, 0);
                                    } else {
                                        this.this$0.f427wh.write(33281, 1, 47);
                                    }
                                }
                            } else if (i6 == 24 && message.arg1 == 1) {
                                this.this$0.f427wh.write(10, 1);
                            } else {
                                this.this$0.f427wh.write(10, 2);
                            }
                        } else {
                            if (this.this$0.f425la.f384Wg && i6 == 24) {
                                if (message.arg1 == 1) {
                                    this.this$0.m218Oa(5);
                                } else {
                                    this.this$0.m218Oa(6);
                                }
                            }
                            if (this.this$0.f425la.f384Wg && i6 == 25) {
                                this.this$0.m218Oa(6);
                            }
                        }
                    } else if (i6 == 33 || i6 == 63) {
                        boolean z4 = this.this$0.f425la.f407wg;
                    } else if (i6 != 90) {
                        switch (i6) {
                            case 58:
                                boolean z5 = this.this$0.f425la.f407wg;
                                break;
                            case 60:
                                boolean z6 = this.this$0.f425la.f407wg;
                                break;
                            case 61:
                                boolean z7 = this.this$0.f425la.f407wg;
                                break;
                        }
                    } else {
                        if (Settings.System.getInt(this.this$0.mContext.getContentResolver(), "SWC_VOICE_MODE", 0) == 0) {
                            z = message.arg1 == 2 && !(this.this$0.f425la.f384Wg && this.this$0.f425la.f385Xg);
                        } else if (message.arg1 != 1 || (this.this$0.f425la.f384Wg && this.this$0.f425la.f385Xg)) {
                        }
                        Log.d("BTModel", "0x0201:0x5a:needWakeUp phone voice:" + z);
                        if (z) {
                            this.this$0.m267yb();
                        }
                    }
                    Iterator it29 = this.this$0.f429yh.entrySet().iterator();
                    while (it29.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it29.next()).getValue()).mo286b(message.arg2, message.arg1 != 1);
                    }
                    return true;
                case 515:
                    Iterator it30 = this.this$0.f429yh.entrySet().iterator();
                    while (it30.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it30.next()).getValue()).mo294f((message.arg1 & Integer.MIN_VALUE) == Integer.MIN_VALUE);
                    }
                    return true;
                case 769:
                    Log.d("BTModel", "handleMessage: 0x0301:" + message.arg1);
                    if (this.this$0.f425la.mSource == message.arg1) {
                        return true;
                    }
                    if (this.this$0.f425la.mSource == 8) {
                        if (this.this$0.f425la.f368Gg) {
                            this.this$0.f427wh.write(20, 0);
                        }
                    } else if (message.arg1 == 8) {
                        this.this$0.f427wh.write(20, 0);
                    }
                    this.this$0.f425la.mSource = message.arg1;
                    this.this$0.f427wh.write(46, this.this$0.f425la.mSource == 8 ? 1 : 0);
                    Iterator it31 = this.this$0.f429yh.entrySet().iterator();
                    while (it31.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it31.next()).getValue()).mo297j(message.arg1);
                    }
                    return true;
                case 1296:
                    try {
                        byte[] bArr2 = (byte[]) message.obj;
                        if (message.arg1 != 255) {
                            return true;
                        }
                        this.this$0.f424Eh = bArr2[0] & 255;
                        return true;
                    } catch (Exception e8) {
                        Log.e("BTModel", "handleMessage: 0x0510:" + e8.getMessage());
                        return true;
                    }
                case 40456:
                    Iterator it32 = this.this$0.f429yh.entrySet().iterator();
                    while (it32.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it32.next()).getValue()).mo275N(message.arg1);
                    }
                    return true;
                default:
                    switch (i) {
                        case 68:
                            if (this.this$0.f421Bh >= 2) {
                                this.this$0.f421Bh = 0;
                                return true;
                            }
                            this.this$0.mHandler.removeMessages(65287);
                            Message messageObtainMessage5 = this.this$0.mHandler.obtainMessage();
                            messageObtainMessage5.what = 65287;
                            messageObtainMessage5.arg1 = 0;
                            messageObtainMessage5.arg2 = this.this$0.f421Bh;
                            this.this$0.mHandler.sendMessage(messageObtainMessage5);
                            C0544f.m234e(this.this$0);
                            return true;
                        case 69:
                            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(strTrim)) {
                                String strSubstring = strTrim.substring(0, 1);
                                String strSubstring2 = strTrim.substring(1);
                                switch (strSubstring.hashCode()) {
                                    case 52:
                                        b2 = strSubstring.equals("4") ? (byte) 0 : (byte) -1;
                                        break;
                                    case 53:
                                        if (strSubstring.equals("5")) {
                                            b2 = 1;
                                            break;
                                        }
                                        break;
                                    case 54:
                                        if (strSubstring.equals("6")) {
                                            b2 = 2;
                                            break;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                if (b2 == 0) {
                                    if (this.this$0.f425la.f387Zg) {
                                        this.this$0.f425la.f395ih.add(new C0719c(strSubstring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 1));
                                    } else {
                                        this.this$0.f425la.f395ih.add(0, new C0719c(strSubstring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 1));
                                    }
                                    Iterator it33 = this.this$0.f429yh.entrySet().iterator();
                                    while (it33.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it33.next()).getValue()).mo301m(strSubstring2, str);
                                    }
                                } else if (b2 == 1) {
                                    if (this.this$0.f425la.f387Zg) {
                                        this.this$0.f425la.f394hh.add(new C0719c(strSubstring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 0));
                                    } else {
                                        this.this$0.f425la.f394hh.add(0, new C0719c(strSubstring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 0));
                                    }
                                    Iterator it34 = this.this$0.f429yh.entrySet().iterator();
                                    while (it34.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it34.next()).getValue()).mo308s(strSubstring2, str);
                                    }
                                } else if (b2 == 2) {
                                    if (this.this$0.f425la.f387Zg) {
                                        this.this$0.f425la.f396jh.add(new C0719c(strSubstring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 2));
                                    } else {
                                        this.this$0.f425la.f396jh.add(0, new C0719c(strSubstring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 2));
                                    }
                                    Iterator it35 = this.this$0.f429yh.entrySet().iterator();
                                    while (it35.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it35.next()).getValue()).mo304p(strSubstring2, str);
                                    }
                                }
                                Log.d("BTModel", "RETURN_CALL_LOG_PHONE_DATA: " + message.arg1 + " " + message.arg2 + " " + strTrim + " " + str);
                                return true;
                            }
                            return false;
                        case 70:
                            Iterator it36 = this.this$0.f429yh.entrySet().iterator();
                            while (it36.hasNext()) {
                                ((InterfaceC0545g) ((Map.Entry) it36.next()).getValue()).mo281a(message.arg1, this.this$0.m216Na(message.arg1));
                            }
                            return true;
                        case 71:
                            this.this$0.f425la.f372Kg = message.arg2;
                            this.this$0.f425la.f373Lg = message.arg1 & Integer.MAX_VALUE;
                            Log.d("BTModel", "RETURN_MS:totalTime：" + this.this$0.f425la.f372Kg + " currentTime:" + this.this$0.f425la.f373Lg);
                            Iterator it37 = this.this$0.f429yh.entrySet().iterator();
                            while (it37.hasNext()) {
                                ((InterfaceC0545g) ((Map.Entry) it37.next()).getValue()).mo289e(this.this$0.f425la.f373Lg, this.this$0.f425la.f372Kg);
                            }
                            if (this.this$0.f428xh == null) {
                                return true;
                            }
                            this.this$0.f428xh.m188a(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg, this.this$0.f425la.f373Lg, this.this$0.f425la.f372Kg);
                            return true;
                        default:
                            switch (i) {
                                case 65283:
                                    if (this.this$0.f425la.mSource != 8) {
                                        return true;
                                    }
                                    this.this$0.f427wh.m206w(false);
                                    return true;
                                case 65284:
                                    if (this.this$0.f425la.f364Cg == 2) {
                                        this.this$0.f425la.f367Fg++;
                                        this.this$0.mHandler.sendEmptyMessageDelayed(65284, 1000L);
                                    }
                                    Iterator it38 = this.this$0.f429yh.entrySet().iterator();
                                    while (it38.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it38.next()).getValue()).mo276P(this.this$0.f425la.f367Fg);
                                    }
                                    return true;
                                case 65285:
                                    Message messageObtainMessage6 = this.this$0.mHandler.obtainMessage();
                                    messageObtainMessage6.what = 65286;
                                    messageObtainMessage6.arg1 = 1;
                                    messageObtainMessage6.arg2 = 0;
                                    this.this$0.mHandler.removeMessages(65286);
                                    this.this$0.mHandler.sendMessageDelayed(messageObtainMessage6, 3000L);
                                    if (this.this$0.f425la.f374Mg) {
                                        this.this$0.m215Me();
                                        return true;
                                    }
                                    this.this$0.m217Ne();
                                    return true;
                                case 65286:
                                    int i8 = message.arg1;
                                    if (i8 == 0) {
                                        Iterator it39 = this.this$0.f429yh.entrySet().iterator();
                                        while (it39.hasNext()) {
                                            ((InterfaceC0545g) ((Map.Entry) it39.next()).getValue()).mo279W();
                                        }
                                        return true;
                                    }
                                    if (i8 != 1) {
                                        return true;
                                    }
                                    this.this$0.f425la.f376Og = true;
                                    Log.d("BTModel", "MSG_CONTACT_RETURN:" + this.this$0.f425la.f392fh.size());
                                    this.this$0.mHandler.sendEmptyMessage(65290);
                                    Iterator it40 = this.this$0.f429yh.entrySet().iterator();
                                    while (it40.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it40.next()).getValue()).mo309w(this.this$0.f425la.f392fh.size());
                                    }
                                    this.this$0.m263ub();
                                    return true;
                                case 65287:
                                    if (this.this$0.f427wh == null) {
                                        return true;
                                    }
                                    if (message.arg1 != 0) {
                                        this.this$0.f427wh.write(26, 255);
                                        return true;
                                    }
                                    int i9 = this.this$0.f421Bh;
                                    if (i9 == 0) {
                                        this.this$0.f425la.f395ih.clear();
                                    } else if (i9 == 1) {
                                        this.this$0.f425la.f394hh.clear();
                                    } else if (i9 == 2) {
                                        this.this$0.f425la.f396jh.clear();
                                    }
                                    this.this$0.f427wh.write(67, this.this$0.f421Bh);
                                    return true;
                                case 65288:
                                    if (this.this$0.f425la.f374Mg) {
                                        this.this$0.m219Oe();
                                        return true;
                                    }
                                    this.this$0.m220Pe();
                                    return true;
                                case 65289:
                                    this.this$0.f425la.f377Pg = true;
                                    this.this$0.mHandler.sendEmptyMessage(65290);
                                    Iterator it41 = this.this$0.f429yh.entrySet().iterator();
                                    while (it41.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it41.next()).getValue()).mo284a(this.this$0.f425la.f393gh);
                                    }
                                    return true;
                                case 65290:
                                    if (!this.this$0.f425la.f376Og || !this.this$0.f425la.f377Pg) {
                                        return true;
                                    }
                                    System.nanoTime();
                                    for (int i10 = 0; i10 < this.this$0.f425la.f392fh.size(); i10++) {
                                        for (int i11 = 0; i11 < this.this$0.f425la.f393gh.size(); i11++) {
                                            if (TextUtils.equals(this.this$0.f425la.f393gh.get(i11).m1131eb(), this.this$0.f425la.f392fh.get(i10).m1131eb())) {
                                                this.this$0.f425la.f392fh.get(i10).m1130A(true);
                                            }
                                        }
                                    }
                                    for (int i12 = 0; i12 < this.this$0.f425la.f390dh.size(); i12++) {
                                        for (int i13 = 0; i13 < this.this$0.f425la.f393gh.size(); i13++) {
                                            if (TextUtils.equals(this.this$0.f425la.f393gh.get(i13).m1131eb(), this.this$0.f425la.f390dh.get(i12).m1131eb())) {
                                                this.this$0.f425la.f390dh.get(i12).m1130A(true);
                                            }
                                        }
                                    }
                                    for (int i14 = 0; i14 < this.this$0.f425la.f391eh.size(); i14++) {
                                        for (int i15 = 0; i15 < this.this$0.f425la.f393gh.size(); i15++) {
                                            if (TextUtils.equals(this.this$0.f425la.f393gh.get(i15).m1131eb(), this.this$0.f425la.f391eh.get(i14).m1131eb())) {
                                                this.this$0.f425la.f391eh.get(i14).m1130A(true);
                                            }
                                        }
                                    }
                                    System.nanoTime();
                                    Iterator it42 = this.this$0.f429yh.entrySet().iterator();
                                    while (it42.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it42.next()).getValue()).mo278T();
                                    }
                                    return true;
                                case 65291:
                                    if (this.this$0.f425la.mSource == 8 || message.arg1 != 1) {
                                        return true;
                                    }
                                    this.this$0.f427wh.m206w(true);
                                    return true;
                                default:
                                    return true;
                            }
                    }
            }
            Log.e("BTModel", "handleMessage: msg.what:" + message.what + " Error:" + e.getMessage());
            return true;
        }
        if (this.this$0.f425la.f403sg != message.arg1) {
            this.this$0.f425la.f403sg = message.arg1;
            if (this.this$0.f425la.f403sg == 2) {
                this.this$0.f427wh.write(3, 1);
            } else {
                this.this$0.f425la.f364Cg = 0;
                this.this$0.f425la.f408xg = null;
                this.this$0.f425la.f409yg = null;
                this.this$0.f425la.f390dh.clear();
                this.this$0.f425la.f391eh.clear();
                this.this$0.f425la.f392fh.clear();
                this.this$0.f425la.f393gh.clear();
                this.this$0.f425la.f394hh.clear();
                this.this$0.f425la.f395ih.clear();
                this.this$0.f425la.f396jh.clear();
                this.this$0.f427wh.write(3, 2);
                this.this$0.m253a(false, this.this$0.f425la.f408xg);
            }
        }
        Iterator it43 = this.this$0.f429yh.entrySet().iterator();
        while (it43.hasNext()) {
            ((InterfaceC0545g) ((Map.Entry) it43.next()).getValue()).mo269C(message.arg1);
        }
        if (C0556b.getInstant().f455cd == null) {
            return true;
        }
        C0556b.getInstant().f455cd.mo154m(this.this$0.f425la.f403sg);
        return true;
    }
}
