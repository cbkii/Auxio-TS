package com.eckom.xtlibrary.p066b.p067a.p025d;

import android.content.Context;
import android.media.MediaPlayer;
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
import com.eckom.xtlibrary.p066b.C0556b;
import com.eckom.xtlibrary.p066b.p053j.C0685a;
import com.eckom.xtlibrary.p066b.p053j.C0686b;
import com.eckom.xtlibrary.p066b.p053j.C0699o;
import com.eckom.xtlibrary.p066b.p067a.p022a.C0532b;
import com.eckom.xtlibrary.p066b.p067a.p023b.C0533a;
import com.eckom.xtlibrary.p066b.p067a.p023b.C0535c;
import com.eckom.xtlibrary.p066b.p067a.p024c.C0537b;
import com.eckom.xtlibrary.p066b.p067a.p029h.C0555d;
import com.eckom.xtlibrary.twproject.p072bt.bean.C0718b;
import com.eckom.xtlibrary.twproject.p072bt.bean.C0719c;
import com.eckom.xtlibrary.twproject.p072bt.bean.TWContact;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/* compiled from: BTModel.java */
/* renamed from: com.eckom.xtlibrary.b.a.d.c */
/* loaded from: classes3.dex */
class C0541c implements Handler.Callback {
    final /* synthetic */ C0544f this$0;

    C0541c(C0544f c0544f) {
        this.this$0 = c0544f;
    }

    /* JADX WARN: Removed duplicated region for block: B:294:0x093d A[Catch: Exception -> 0x1924, TryCatch #7 {Exception -> 0x1924, blocks: (B:3:0x0007, B:5:0x000d, B:6:0x0032, B:27:0x006c, B:29:0x0076, B:31:0x007a, B:36:0x0085, B:38:0x008f, B:40:0x0099, B:41:0x009d, B:44:0x00ac, B:46:0x00ba, B:48:0x00e4, B:50:0x00f5, B:53:0x00f8, B:56:0x00fc, B:59:0x010b, B:61:0x0119, B:63:0x0143, B:65:0x0154, B:68:0x0157, B:71:0x015b, B:74:0x016a, B:76:0x0178, B:78:0x01a2, B:80:0x01b3, B:83:0x01b6, B:85:0x01b9, B:86:0x01ca, B:88:0x01d0, B:93:0x01e0, B:94:0x0200, B:96:0x0206, B:99:0x021e, B:101:0x0228, B:103:0x022f, B:105:0x0236, B:107:0x023e, B:109:0x0242, B:114:0x0272, B:116:0x024f, B:117:0x025b, B:118:0x0267, B:119:0x0285, B:122:0x0292, B:127:0x029a, B:128:0x02da, B:130:0x02e0, B:132:0x02fc, B:134:0x0303, B:135:0x0311, B:137:0x0317, B:140:0x0327, B:142:0x034f, B:144:0x0356, B:146:0x035d, B:148:0x0367, B:149:0x037e, B:150:0x038c, B:152:0x0392, B:155:0x03aa, B:157:0x03b4, B:161:0x03bf, B:162:0x0411, B:164:0x0417, B:166:0x0437, B:168:0x043f, B:171:0x046a, B:172:0x0478, B:174:0x047e, B:177:0x0498, B:179:0x049e, B:182:0x04a6, B:183:0x04b2, B:190:0x0635, B:192:0x04dd, B:194:0x04e7, B:195:0x0531, B:196:0x053f, B:198:0x0545, B:200:0x050c, B:201:0x0555, B:203:0x055f, B:204:0x05a1, B:205:0x05af, B:207:0x05b5, B:209:0x0580, B:210:0x05c5, B:212:0x05cf, B:213:0x0611, B:214:0x061f, B:216:0x0625, B:218:0x05f0, B:219:0x04b6, B:222:0x04c0, B:225:0x04ca, B:230:0x0663, B:232:0x066b, B:234:0x069b, B:236:0x06a3, B:237:0x06b1, B:239:0x06b7, B:242:0x06f8, B:244:0x071a, B:246:0x0724, B:248:0x072e, B:249:0x074b, B:252:0x076a, B:253:0x077b, B:255:0x0781, B:259:0x073b, B:261:0x073f, B:263:0x0793, B:264:0x07a1, B:266:0x07a7, B:270:0x07bd, B:275:0x07c1, B:289:0x07f3, B:291:0x0929, B:292:0x0937, B:294:0x093d, B:298:0x0953, B:303:0x07f8, B:304:0x0802, B:305:0x080c, B:307:0x081f, B:309:0x0823, B:313:0x0856, B:315:0x086c, B:316:0x082e, B:320:0x0839, B:322:0x083e, B:324:0x084a, B:327:0x0873, B:328:0x087d, B:330:0x0887, B:340:0x08a0, B:342:0x08a5, B:343:0x08b5, B:346:0x08c6, B:348:0x08cb, B:349:0x08d7, B:350:0x08e3, B:354:0x08f1, B:356:0x08f6, B:357:0x08fd, B:358:0x0903, B:362:0x0911, B:363:0x0918, B:364:0x0921, B:365:0x0957, B:367:0x0963, B:368:0x0989, B:371:0x0993, B:372:0x09a1, B:374:0x09a7, B:378:0x096d, B:380:0x0974, B:382:0x097a, B:384:0x0980, B:385:0x0a51, B:387:0x0a5b, B:388:0x0aa4, B:390:0x0aaa, B:394:0x0abe, B:396:0x0ade, B:398:0x0afe, B:399:0x0b0c, B:401:0x0b12, B:403:0x0b24, B:405:0x0b2c, B:408:0x0b39, B:409:0x0b47, B:411:0x0b4d, B:415:0x0b69, B:420:0x0b6d, B:422:0x0b7b, B:424:0x0c21, B:433:0x0c81, B:445:0x0c07, B:446:0x0c99, B:448:0x0ca1, B:449:0x0caf, B:451:0x0cb5, B:453:0x0cc5, B:456:0x0ccd, B:457:0x0cdb, B:459:0x0ce1, B:597:0x0cf5, B:598:0x0d1a, B:599:0x0d28, B:601:0x0d2e, B:620:0x0d3e, B:621:0x0d63, B:622:0x0d71, B:624:0x0d77, B:462:0x0d87, B:463:0x0d95, B:465:0x0d9b, B:468:0x0dad, B:470:0x0db5, B:471:0x0de4, B:473:0x0dea, B:475:0x0dfa, B:478:0x0e02, B:480:0x0e0d, B:482:0x0e10, B:483:0x0e2f, B:485:0x0e35, B:488:0x0e45, B:489:0x0e64, B:491:0x0e6a, B:494:0x0e7a, B:495:0x0e9a, B:497:0x0ea0, B:500:0x0eb0, B:503:0x0ebd, B:505:0x0ec7, B:506:0x0ef0, B:509:0x0f06, B:510:0x0f1f, B:512:0x0f25, B:516:0x0f38, B:523:0x0f3c, B:524:0x0f4a, B:526:0x0f50, B:529:0x0f62, B:532:0x0f78, B:533:0x0fa3, B:543:0x1069, B:545:0x1071, B:546:0x10a1, B:548:0x10a9, B:549:0x10b4, B:551:0x10bc, B:554:0x107b, B:555:0x1089, B:557:0x108f, B:559:0x0fba, B:561:0x0fc5, B:562:0x0fd7, B:563:0x0fe2, B:564:0x1003, B:565:0x1027, B:567:0x1032, B:568:0x103a, B:570:0x1042, B:572:0x104e, B:573:0x1057, B:574:0x0f89, B:576:0x0f8d, B:577:0x10c9, B:578:0x10cb, B:580:0x10d3, B:582:0x10dd, B:584:0x10ef, B:585:0x1176, B:586:0x1184, B:588:0x118a, B:590:0x119a, B:592:0x10fb, B:617:0x06de, B:647:0x0a37, B:607:0x0d04, B:630:0x0d4d, B:654:0x120e, B:657:0x1268, B:658:0x12ab, B:660:0x12b1, B:662:0x12d1, B:663:0x12df, B:665:0x12e5, B:667:0x130d, B:669:0x1315, B:677:0x11e6, B:678:0x1340, B:679:0x134e, B:681:0x1354, B:684:0x1364, B:685:0x1372, B:687:0x1378, B:690:0x138a, B:692:0x138e, B:694:0x1396, B:696:0x13a2, B:697:0x13d9, B:698:0x13e7, B:700:0x13ed, B:703:0x13ac, B:705:0x13c4, B:707:0x13d0, B:708:0x13ff, B:714:0x140c, B:716:0x1414, B:717:0x1422, B:719:0x1428, B:721:0x1438, B:722:0x1461, B:724:0x1478, B:725:0x1483, B:727:0x1491, B:728:0x149c, B:729:0x14aa, B:731:0x14b0, B:733:0x14c0, B:735:0x14e1, B:737:0x14e9, B:738:0x14f7, B:740:0x14fd, B:742:0x150d, B:744:0x1542, B:746:0x1548, B:747:0x154c, B:749:0x1563, B:750:0x156e, B:752:0x157c, B:753:0x1587, B:754:0x1595, B:756:0x159b, B:758:0x15ab, B:760:0x15cd, B:762:0x15e2, B:763:0x15ed, B:764:0x15fb, B:766:0x1601, B:768:0x1613, B:770:0x161e, B:772:0x162e, B:774:0x1642, B:779:0x1649, B:781:0x1655, B:783:0x1669, B:784:0x1676, B:785:0x16f5, B:786:0x1703, B:788:0x1709, B:790:0x171b, B:792:0x1723, B:795:0x1736, B:797:0x173a, B:799:0x1740, B:801:0x1758, B:802:0x1763, B:804:0x1777, B:806:0x1781, B:807:0x1792, B:809:0x17ac, B:811:0x17bc, B:812:0x17c1, B:814:0x17c9, B:815:0x17d2, B:816:0x18a4, B:817:0x18b2, B:819:0x18b8, B:821:0x18ca, B:823:0x18d0, B:825:0x18de, B:827:0x18ee, B:829:0x18fc, B:835:0x17e3, B:837:0x17e8, B:839:0x17fb, B:841:0x1805, B:843:0x1817, B:845:0x1821, B:846:0x1832, B:848:0x184c, B:850:0x185c, B:851:0x1861, B:853:0x1869, B:854:0x1872, B:855:0x1881, B:857:0x1894, B:858:0x001b, B:860:0x0021, B:862:0x002b, B:427:0x0c32, B:429:0x0c78, B:609:0x06c9, B:611:0x06d1, B:435:0x0b8b, B:437:0x0b9b, B:438:0x0bac, B:440:0x0bd1, B:441:0x0bd7, B:632:0x09b7, B:634:0x09d8, B:636:0x09de, B:637:0x09f4, B:639:0x09fa, B:642:0x0a00, B:650:0x11af, B:652:0x11b8, B:674:0x11c9), top: B:2:0x0007, inners: #0, #1, #2, #3, #4, #5, #6 }] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x086c A[Catch: Exception -> 0x1924, TryCatch #7 {Exception -> 0x1924, blocks: (B:3:0x0007, B:5:0x000d, B:6:0x0032, B:27:0x006c, B:29:0x0076, B:31:0x007a, B:36:0x0085, B:38:0x008f, B:40:0x0099, B:41:0x009d, B:44:0x00ac, B:46:0x00ba, B:48:0x00e4, B:50:0x00f5, B:53:0x00f8, B:56:0x00fc, B:59:0x010b, B:61:0x0119, B:63:0x0143, B:65:0x0154, B:68:0x0157, B:71:0x015b, B:74:0x016a, B:76:0x0178, B:78:0x01a2, B:80:0x01b3, B:83:0x01b6, B:85:0x01b9, B:86:0x01ca, B:88:0x01d0, B:93:0x01e0, B:94:0x0200, B:96:0x0206, B:99:0x021e, B:101:0x0228, B:103:0x022f, B:105:0x0236, B:107:0x023e, B:109:0x0242, B:114:0x0272, B:116:0x024f, B:117:0x025b, B:118:0x0267, B:119:0x0285, B:122:0x0292, B:127:0x029a, B:128:0x02da, B:130:0x02e0, B:132:0x02fc, B:134:0x0303, B:135:0x0311, B:137:0x0317, B:140:0x0327, B:142:0x034f, B:144:0x0356, B:146:0x035d, B:148:0x0367, B:149:0x037e, B:150:0x038c, B:152:0x0392, B:155:0x03aa, B:157:0x03b4, B:161:0x03bf, B:162:0x0411, B:164:0x0417, B:166:0x0437, B:168:0x043f, B:171:0x046a, B:172:0x0478, B:174:0x047e, B:177:0x0498, B:179:0x049e, B:182:0x04a6, B:183:0x04b2, B:190:0x0635, B:192:0x04dd, B:194:0x04e7, B:195:0x0531, B:196:0x053f, B:198:0x0545, B:200:0x050c, B:201:0x0555, B:203:0x055f, B:204:0x05a1, B:205:0x05af, B:207:0x05b5, B:209:0x0580, B:210:0x05c5, B:212:0x05cf, B:213:0x0611, B:214:0x061f, B:216:0x0625, B:218:0x05f0, B:219:0x04b6, B:222:0x04c0, B:225:0x04ca, B:230:0x0663, B:232:0x066b, B:234:0x069b, B:236:0x06a3, B:237:0x06b1, B:239:0x06b7, B:242:0x06f8, B:244:0x071a, B:246:0x0724, B:248:0x072e, B:249:0x074b, B:252:0x076a, B:253:0x077b, B:255:0x0781, B:259:0x073b, B:261:0x073f, B:263:0x0793, B:264:0x07a1, B:266:0x07a7, B:270:0x07bd, B:275:0x07c1, B:289:0x07f3, B:291:0x0929, B:292:0x0937, B:294:0x093d, B:298:0x0953, B:303:0x07f8, B:304:0x0802, B:305:0x080c, B:307:0x081f, B:309:0x0823, B:313:0x0856, B:315:0x086c, B:316:0x082e, B:320:0x0839, B:322:0x083e, B:324:0x084a, B:327:0x0873, B:328:0x087d, B:330:0x0887, B:340:0x08a0, B:342:0x08a5, B:343:0x08b5, B:346:0x08c6, B:348:0x08cb, B:349:0x08d7, B:350:0x08e3, B:354:0x08f1, B:356:0x08f6, B:357:0x08fd, B:358:0x0903, B:362:0x0911, B:363:0x0918, B:364:0x0921, B:365:0x0957, B:367:0x0963, B:368:0x0989, B:371:0x0993, B:372:0x09a1, B:374:0x09a7, B:378:0x096d, B:380:0x0974, B:382:0x097a, B:384:0x0980, B:385:0x0a51, B:387:0x0a5b, B:388:0x0aa4, B:390:0x0aaa, B:394:0x0abe, B:396:0x0ade, B:398:0x0afe, B:399:0x0b0c, B:401:0x0b12, B:403:0x0b24, B:405:0x0b2c, B:408:0x0b39, B:409:0x0b47, B:411:0x0b4d, B:415:0x0b69, B:420:0x0b6d, B:422:0x0b7b, B:424:0x0c21, B:433:0x0c81, B:445:0x0c07, B:446:0x0c99, B:448:0x0ca1, B:449:0x0caf, B:451:0x0cb5, B:453:0x0cc5, B:456:0x0ccd, B:457:0x0cdb, B:459:0x0ce1, B:597:0x0cf5, B:598:0x0d1a, B:599:0x0d28, B:601:0x0d2e, B:620:0x0d3e, B:621:0x0d63, B:622:0x0d71, B:624:0x0d77, B:462:0x0d87, B:463:0x0d95, B:465:0x0d9b, B:468:0x0dad, B:470:0x0db5, B:471:0x0de4, B:473:0x0dea, B:475:0x0dfa, B:478:0x0e02, B:480:0x0e0d, B:482:0x0e10, B:483:0x0e2f, B:485:0x0e35, B:488:0x0e45, B:489:0x0e64, B:491:0x0e6a, B:494:0x0e7a, B:495:0x0e9a, B:497:0x0ea0, B:500:0x0eb0, B:503:0x0ebd, B:505:0x0ec7, B:506:0x0ef0, B:509:0x0f06, B:510:0x0f1f, B:512:0x0f25, B:516:0x0f38, B:523:0x0f3c, B:524:0x0f4a, B:526:0x0f50, B:529:0x0f62, B:532:0x0f78, B:533:0x0fa3, B:543:0x1069, B:545:0x1071, B:546:0x10a1, B:548:0x10a9, B:549:0x10b4, B:551:0x10bc, B:554:0x107b, B:555:0x1089, B:557:0x108f, B:559:0x0fba, B:561:0x0fc5, B:562:0x0fd7, B:563:0x0fe2, B:564:0x1003, B:565:0x1027, B:567:0x1032, B:568:0x103a, B:570:0x1042, B:572:0x104e, B:573:0x1057, B:574:0x0f89, B:576:0x0f8d, B:577:0x10c9, B:578:0x10cb, B:580:0x10d3, B:582:0x10dd, B:584:0x10ef, B:585:0x1176, B:586:0x1184, B:588:0x118a, B:590:0x119a, B:592:0x10fb, B:617:0x06de, B:647:0x0a37, B:607:0x0d04, B:630:0x0d4d, B:654:0x120e, B:657:0x1268, B:658:0x12ab, B:660:0x12b1, B:662:0x12d1, B:663:0x12df, B:665:0x12e5, B:667:0x130d, B:669:0x1315, B:677:0x11e6, B:678:0x1340, B:679:0x134e, B:681:0x1354, B:684:0x1364, B:685:0x1372, B:687:0x1378, B:690:0x138a, B:692:0x138e, B:694:0x1396, B:696:0x13a2, B:697:0x13d9, B:698:0x13e7, B:700:0x13ed, B:703:0x13ac, B:705:0x13c4, B:707:0x13d0, B:708:0x13ff, B:714:0x140c, B:716:0x1414, B:717:0x1422, B:719:0x1428, B:721:0x1438, B:722:0x1461, B:724:0x1478, B:725:0x1483, B:727:0x1491, B:728:0x149c, B:729:0x14aa, B:731:0x14b0, B:733:0x14c0, B:735:0x14e1, B:737:0x14e9, B:738:0x14f7, B:740:0x14fd, B:742:0x150d, B:744:0x1542, B:746:0x1548, B:747:0x154c, B:749:0x1563, B:750:0x156e, B:752:0x157c, B:753:0x1587, B:754:0x1595, B:756:0x159b, B:758:0x15ab, B:760:0x15cd, B:762:0x15e2, B:763:0x15ed, B:764:0x15fb, B:766:0x1601, B:768:0x1613, B:770:0x161e, B:772:0x162e, B:774:0x1642, B:779:0x1649, B:781:0x1655, B:783:0x1669, B:784:0x1676, B:785:0x16f5, B:786:0x1703, B:788:0x1709, B:790:0x171b, B:792:0x1723, B:795:0x1736, B:797:0x173a, B:799:0x1740, B:801:0x1758, B:802:0x1763, B:804:0x1777, B:806:0x1781, B:807:0x1792, B:809:0x17ac, B:811:0x17bc, B:812:0x17c1, B:814:0x17c9, B:815:0x17d2, B:816:0x18a4, B:817:0x18b2, B:819:0x18b8, B:821:0x18ca, B:823:0x18d0, B:825:0x18de, B:827:0x18ee, B:829:0x18fc, B:835:0x17e3, B:837:0x17e8, B:839:0x17fb, B:841:0x1805, B:843:0x1817, B:845:0x1821, B:846:0x1832, B:848:0x184c, B:850:0x185c, B:851:0x1861, B:853:0x1869, B:854:0x1872, B:855:0x1881, B:857:0x1894, B:858:0x001b, B:860:0x0021, B:862:0x002b, B:427:0x0c32, B:429:0x0c78, B:609:0x06c9, B:611:0x06d1, B:435:0x0b8b, B:437:0x0b9b, B:438:0x0bac, B:440:0x0bd1, B:441:0x0bd7, B:632:0x09b7, B:634:0x09d8, B:636:0x09de, B:637:0x09f4, B:639:0x09fa, B:642:0x0a00, B:650:0x11af, B:652:0x11b8, B:674:0x11c9), top: B:2:0x0007, inners: #0, #1, #2, #3, #4, #5, #6 }] */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleMessage(Message message) {
        String str;
        String str2;
        int i;
        C0532b c0532b;
        C0532b c0532b2;
        C0532b c0532b3;
        C0532b c0532b4;
        C0535c c0535c;
        C0532b c0532b5;
        C0532b c0532b6;
        C0535c c0535c2;
        C0535c c0535c3;
        C0535c c0535c4;
        MediaPlayer mediaPlayer;
        MediaPlayer mediaPlayer2;
        MediaPlayer mediaPlayer3;
        MediaPlayer mediaPlayer4;
        MediaPlayer mediaPlayer5;
        MediaPlayer mediaPlayer6;
        C0535c c0535c5;
        C0532b c0532b7;
        C0532b c0532b8;
        int i2;
        C0535c c0535c6;
        C0535c c0535c7;
        C0535c c0535c8;
        MediaPlayer mediaPlayer7;
        C0535c c0535c9;
        MediaPlayer mediaPlayer8;
        MediaPlayer mediaPlayer9;
        C0532b c0532b9;
        C0532b c0532b10;
        C0555d c0555d;
        C0535c c0535c10;
        C0535c c0535c11;
        C0535c c0535c12;
        C0535c c0535c13;
        boolean z;
        C0532b c0532b11;
        C0532b c0532b12;
        C0535c c0535c14;
        Iterator it;
        C0535c c0535c15;
        C0535c c0535c16;
        C0535c c0535c17;
        C0535c c0535c18;
        boolean z2;
        C0535c c0535c19;
        C0535c c0535c20;
        C0535c c0535c21;
        int i3;
        int i4;
        char c2;
        String m216Na;
        C0532b c0532b13;
        C0532b c0532b14;
        C0535c c0535c22;
        C0535c c0535c23;
        C0535c c0535c24;
        int i5;
        C0535c c0535c25;
        int i6;
        C0535c c0535c26;
        try {
            if (message.obj instanceof TWUtil.TWObject) {
                TWUtil.TWObject tWObject = (TWUtil.TWObject) message.obj;
                String str3 = (String) tWObject.obj3;
                str2 = (String) tWObject.obj4;
                str = str3;
            } else {
                str = message.obj instanceof byte[] ? new String((byte[]) message.obj) : (String) message.obj;
                str2 = null;
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
                c0535c = this.this$0.f427wh;
                c0535c.write(3, 2);
                if (this.this$0.f425la.f409yg != null) {
                    if (this.this$0.f425la.f374Mg) {
                        C0537b.m207a(this.this$0.mContext, this.this$0.f425la.f409yg);
                    }
                    if (!TextUtils.equals(this.this$0.f425la.f409yg, C0699o.m1032c(this.this$0.mContext, "BTModel", "ConnectDeviceMac"))) {
                        if (!this.this$0.f425la.f398ng.contains("KED18-0395")) {
                            this.this$0.m214Le();
                        }
                        c0532b5 = this.this$0.f428xh;
                        if (c0532b5 != null) {
                            c0532b6 = this.this$0.f428xh;
                            c0532b6.m190fb();
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
                                c0532b = this.this$0.f428xh;
                                if (c0532b != null) {
                                    c0532b2 = this.this$0.f428xh;
                                    c0532b2.m190fb();
                                }
                            }
                            this.this$0.m262tb();
                            this.this$0.m265wb();
                            this.this$0.m264vb();
                        }
                    }
                }
                if (!this.this$0.f425la.f388_g.contains(new C0718b(str2, str))) {
                    this.this$0.f425la.f388_g.add(new C0718b(str2, str));
                }
            }
            Iterator it2 = this.this$0.f429yh.entrySet().iterator();
            while (it2.hasNext()) {
                ((InterfaceC0545g) ((Map.Entry) it2.next()).getValue()).mo282a(message.arg1, str2, str);
            }
            if (TextUtils.isEmpty(str) || !TextUtils.equals(this.this$0.f425la.f409yg, str)) {
                return true;
            }
            this.this$0.f425la.f408xg = str2;
            c0532b3 = this.this$0.f428xh;
            if (c0532b3 == null || TextUtils.isEmpty(this.this$0.f425la.f408xg)) {
                return true;
            }
            c0532b4 = this.this$0.f428xh;
            c0532b4.m195y(this.this$0.f425la.f408xg, this.this$0.f425la.f409yg);
            this.this$0.m253a(true, this.this$0.f425la.f408xg);
            return true;
        }
        if (i != 7) {
            if (i == 23) {
                this.this$0.f425la.f404tg = message.arg1;
                if (1 == this.this$0.f425la.f404tg) {
                    C0699o.m1027a(this.this$0.mContext, "BTModel", "ConnectDeviceMac", "");
                }
                Iterator it3 = this.this$0.f429yh.entrySet().iterator();
                while (it3.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it3.next()).getValue()).mo273M(message.arg1);
                }
                if (this.this$0.f425la.f404tg == 1 || !this.this$0.f425la.f398ng.contains("KED18-0395") || !C0699o.m1031b(this.this$0.mContext, "BTModel", this.this$0.f425la.f409yg)) {
                    return true;
                }
                this.this$0.m213Ke();
                return true;
            }
            if (i == 24) {
                int i7 = (message.arg1 >> 24) & 255;
                if (i7 != 0) {
                    if (i7 != 1) {
                        return true;
                    }
                    if ((message.arg1 & ViewCompat.MEASURED_SIZE_MASK) == 0) {
                        Iterator it4 = this.this$0.f429yh.entrySet().iterator();
                        while (it4.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it4.next()).getValue()).mo274N();
                        }
                        this.this$0.f425la.f391eh.clear();
                        Message obtainMessage = this.this$0.mHandler.obtainMessage();
                        obtainMessage.what = 65286;
                        obtainMessage.arg1 = 0;
                        obtainMessage.arg2 = 1;
                        this.this$0.mHandler.removeMessages(65286);
                        this.this$0.mHandler.sendMessage(obtainMessage);
                    }
                    TWContact tWContact = new TWContact(str2, str, PinyinConv.cn2py(str2));
                    if (!this.this$0.f425la.f391eh.contains(tWContact)) {
                        this.this$0.f425la.f391eh.add(tWContact);
                    }
                    if (!this.this$0.f425la.f392fh.contains(tWContact)) {
                        this.this$0.f425la.f392fh.add(tWContact);
                    }
                    Iterator it5 = this.this$0.f429yh.entrySet().iterator();
                    while (it5.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it5.next()).getValue()).mo302o(str2, str);
                    }
                    Message obtainMessage2 = this.this$0.mHandler.obtainMessage();
                    obtainMessage2.what = 65286;
                    obtainMessage2.arg1 = 1;
                    obtainMessage2.arg2 = 1;
                    this.this$0.mHandler.removeMessages(65286);
                    this.this$0.mHandler.sendMessageDelayed(obtainMessage2, 2000L);
                    return true;
                }
                if ((message.arg1 & ViewCompat.MEASURED_SIZE_MASK) == 0) {
                    Iterator it6 = this.this$0.f429yh.entrySet().iterator();
                    while (it6.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it6.next()).getValue()).mo280X();
                    }
                    this.this$0.f425la.f390dh.clear();
                    this.this$0.f425la.f392fh.clear();
                    Message obtainMessage3 = this.this$0.mHandler.obtainMessage();
                    obtainMessage3.what = 65286;
                    obtainMessage3.arg1 = 0;
                    obtainMessage3.arg2 = 0;
                    this.this$0.mHandler.removeMessages(65286);
                    this.this$0.mHandler.sendMessage(obtainMessage3);
                }
                if (str2 != null) {
                    str2 = str2.trim();
                }
                if (str != null) {
                    str = str.trim();
                }
                TWContact tWContact2 = new TWContact(str2, str, PinyinConv.cn2py(str2));
                if (!this.this$0.f425la.f390dh.contains(tWContact2)) {
                    this.this$0.f425la.f390dh.add(tWContact2);
                }
                if (!this.this$0.f425la.f392fh.contains(tWContact2)) {
                    this.this$0.f425la.f392fh.add(tWContact2);
                }
                Iterator it7 = this.this$0.f429yh.entrySet().iterator();
                while (it7.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it7.next()).getValue()).mo305q(str2, str);
                }
                Message obtainMessage4 = this.this$0.mHandler.obtainMessage();
                obtainMessage4.what = 65286;
                obtainMessage4.arg1 = 1;
                obtainMessage4.arg2 = 0;
                this.this$0.mHandler.removeMessages(65286);
                this.this$0.mHandler.sendMessageDelayed(obtainMessage4, 3000L);
                return true;
            }
            if (i == 47) {
                if (message.arg1 == 0) {
                    mediaPlayer4 = this.this$0.mMediaPlayer;
                    if (mediaPlayer4 != null) {
                        mediaPlayer5 = this.this$0.mMediaPlayer;
                        if (mediaPlayer5.isPlaying()) {
                            mediaPlayer6 = this.this$0.mMediaPlayer;
                            mediaPlayer6.pause();
                        }
                    }
                } else {
                    c0535c4 = this.this$0.f427wh;
                    c0535c4.write(770, 2);
                    this.this$0.m221Qe();
                    mediaPlayer = this.this$0.mMediaPlayer;
                    if (mediaPlayer != null) {
                        mediaPlayer2 = this.this$0.mMediaPlayer;
                        if (!mediaPlayer2.isPlaying()) {
                            mediaPlayer3 = this.this$0.mMediaPlayer;
                            mediaPlayer3.start();
                        }
                    }
                }
                Iterator it8 = this.this$0.f429yh.entrySet().iterator();
                while (it8.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it8.next()).getValue()).mo272J(message.arg1);
                }
                return true;
            }
            if (i == 48) {
                Iterator it9 = this.this$0.f429yh.entrySet().iterator();
                while (it9.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it9.next()).getValue()).mo270H(message.arg1);
                }
                return true;
            }
            if (i == 54) {
                Iterator it10 = this.this$0.f429yh.entrySet().iterator();
                while (it10.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it10.next()).getValue()).mo288da(str);
                }
                return true;
            }
            if (i == 55) {
                try {
                    int lastIndexOf = str2.lastIndexOf("-#");
                    if (lastIndexOf == -1) {
                        this.this$0.f425la.f371Jg = str2;
                        this.this$0.f425la.f370Ig = "";
                    } else {
                        this.this$0.f425la.f371Jg = str2.substring(0, lastIndexOf);
                        this.this$0.f425la.f370Ig = str2.substring(lastIndexOf + 2);
                    }
                } catch (Exception e2) {
                    Log.d("BTModel", "RETURN_ID3:" + e2.getMessage());
                    this.this$0.f425la.f371Jg = str2;
                    this.this$0.f425la.f370Ig = "";
                }
                this.this$0.f425la.f369Hg = str;
                Log.d("BTModel", "RETURN_ID3:musicTitle:" + this.this$0.f425la.f369Hg + " musicAlbum:" + this.this$0.f425la.f370Ig + " musicArtist:" + this.this$0.f425la.f371Jg);
                c0535c5 = this.this$0.f427wh;
                c0535c5.write(40704, 8, this.this$0.f425la.f368Gg ? 1 : 0, this.this$0.f425la.f369Hg);
                this.this$0.m227b(0, this.this$0.f425la.f369Hg);
                this.this$0.mHandler.postDelayed(new RunnableC0539a(this), 100L);
                this.this$0.mHandler.postDelayed(new RunnableC0540b(this), 200L);
                Iterator it11 = this.this$0.f429yh.entrySet().iterator();
                while (it11.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it11.next()).getValue()).mo307r(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg);
                }
                Iterator it12 = this.this$0.f429yh.entrySet().iterator();
                while (it12.hasNext()) {
                    ((InterfaceC0545g) ((Map.Entry) it12.next()).getValue()).mo291e(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg, this.this$0.f425la.f370Ig);
                }
                c0532b7 = this.this$0.f428xh;
                if (c0532b7 == null) {
                    return true;
                }
                c0532b8 = this.this$0.f428xh;
                c0532b8.m188a(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg, this.this$0.f425la.f373Lg, this.this$0.f425la.f372Kg);
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
                        c0535c8 = this.this$0.f427wh;
                        c0535c8.write(3, 1);
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
                        c0535c6 = this.this$0.f427wh;
                        c0535c6.write(3, 2);
                        this.this$0.mHandler.sendEmptyMessage(MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                    }
                    Iterator it13 = this.this$0.f429yh.entrySet().iterator();
                    while (it13.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it13.next()).getValue()).mo303p(i2);
                    }
                    c0535c7 = this.this$0.f427wh;
                    c0535c7.write(1283, 16, this.this$0.f425la.f403sg);
                    return true;
                case 11:
                    this.this$0.f425la.f364Cg = message.arg1;
                    if (this.this$0.f425la.f364Cg != 0 && str != null) {
                        this.this$0.f425la.f400pg = str2;
                        this.this$0.f425la.f399og = str;
                    } else if (message.arg1 == 0) {
                        this.this$0.f425la.f400pg = null;
                        this.this$0.f425la.f399og = null;
                        this.this$0.m263ub();
                    }
                    int i8 = this.this$0.f425la.f364Cg;
                    if (i8 == 0) {
                        if (this.this$0.f425la.f365Dg != -1) {
                            this.this$0.f425la.f365Dg = -1;
                        }
                        mediaPlayer7 = this.this$0.mMediaPlayer;
                        if (mediaPlayer7 != null) {
                            mediaPlayer8 = this.this$0.mMediaPlayer;
                            if (mediaPlayer8.isPlaying()) {
                                mediaPlayer9 = this.this$0.mMediaPlayer;
                                mediaPlayer9.pause();
                            }
                        }
                        this.this$0.f425la.f367Fg = 0;
                        c0535c9 = this.this$0.f427wh;
                        c0535c9.write(1283, 0);
                    } else if (i8 == 1) {
                        this.this$0.f425la.f399og = str;
                        this.this$0.f425la.f400pg = str2;
                        this.this$0.f425la.f367Fg = 0;
                        c0535c10 = this.this$0.f427wh;
                        c0535c10.write(1283, 1);
                    } else if (i8 == 2) {
                        c0535c11 = this.this$0.f427wh;
                        c0535c11.write(1283, 3);
                        this.this$0.mHandler.removeMessages(65284);
                        this.this$0.mHandler.sendEmptyMessageDelayed(65284, 500L);
                    } else if (i8 == 3) {
                        c0535c12 = this.this$0.f427wh;
                        c0535c12.write(1283, 2);
                    } else if (i8 == 4 && this.this$0.f425la.f365Dg == -1) {
                        this.this$0.f425la.f365Dg = this.this$0.f425la.f366Eg;
                    }
                    if ("++++".equals(str)) {
                        c0555d = this.this$0.f430zh;
                        c0555d.show();
                    } else {
                        Iterator it14 = this.this$0.f429yh.entrySet().iterator();
                        while (it14.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it14.next()).getValue()).mo293f(message.arg1, str2, str);
                        }
                    }
                    c0532b9 = this.this$0.f428xh;
                    if (c0532b9 != null) {
                        c0532b10 = this.this$0.f428xh;
                        c0532b10.m193i(message.arg1, str2, str);
                    }
                    if (C0556b.getInstant().f455cd == null) {
                        return true;
                    }
                    C0556b.getInstant().f455cd.mo153d(message.arg1, str2, str);
                    return true;
                case 13:
                    Iterator it15 = this.this$0.f429yh.entrySet().iterator();
                    while (it15.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it15.next()).getValue()).mo277Q(message.arg1);
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
                    c0535c13 = this.this$0.f427wh;
                    c0535c13.write(40704, 8, this.this$0.f425la.f368Gg ? 1 : 0, this.this$0.f425la.f369Hg);
                    Iterator it16 = this.this$0.f429yh.entrySet().iterator();
                    while (it16.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it16.next()).getValue()).mo298j(message.arg1 != 0);
                    }
                    return true;
                case 27:
                    if ((message.arg1 & ViewCompat.MEASURED_SIZE_MASK) == 0) {
                        this.this$0.f425la.f394hh.clear();
                        this.this$0.f425la.f395ih.clear();
                        this.this$0.f425la.f396jh.clear();
                        Iterator it17 = this.this$0.f429yh.entrySet().iterator();
                        while (it17.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it17.next()).getValue()).mo271I();
                        }
                    }
                    if (TextUtils.isEmpty(str)) {
                        return false;
                    }
                    int i9 = (message.arg1 >> 24) & 255;
                    if ((i9 & 1) != 1) {
                        this.this$0.f425la.f395ih.add(0, new C0719c(str2, str, 1));
                        Iterator it18 = this.this$0.f429yh.entrySet().iterator();
                        while (it18.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it18.next()).getValue()).mo301m(str2, str);
                        }
                        return true;
                    }
                    if ((i9 & 2) == 2) {
                        this.this$0.f425la.f394hh.add(0, new C0719c(str2, str, 0));
                        Iterator it19 = this.this$0.f429yh.entrySet().iterator();
                        while (it19.hasNext()) {
                            ((InterfaceC0545g) ((Map.Entry) it19.next()).getValue()).mo308s(str2, str);
                        }
                        return true;
                    }
                    this.this$0.f425la.f396jh.add(0, new C0719c(str2, str, 2));
                    Iterator it20 = this.this$0.f429yh.entrySet().iterator();
                    while (it20.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it20.next()).getValue()).mo304p(str2, str);
                    }
                    return true;
                case 29:
                    Iterator it21 = this.this$0.f429yh.entrySet().iterator();
                    while (it21.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it21.next()).getValue()).mo290e(message.arg1, str2, str);
                    }
                    return true;
                case 31:
                    try {
                        this.this$0.f425la.f410zg = (String) message.obj;
                    } catch (ClassCastException e3) {
                        Log.e("BTModel", "" + e3.getMessage());
                    }
                    Iterator it22 = this.this$0.f429yh.entrySet().iterator();
                    while (it22.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it22.next()).getValue()).mo295ga(str);
                    }
                    return true;
                case 33:
                    try {
                        this.this$0.f425la.f362Ag = (String) message.obj;
                    } catch (ClassCastException e4) {
                        Log.e("BTModel", "" + e4.getMessage());
                    }
                    Iterator it23 = this.this$0.f429yh.entrySet().iterator();
                    while (it23.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it23.next()).getValue()).mo300ka(str);
                    }
                    return true;
                case 35:
                    Iterator it24 = this.this$0.f429yh.entrySet().iterator();
                    while (it24.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it24.next()).getValue()).mo292f(message.arg1, message.arg2);
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
                                String[] split = str.split("_")[2].split("\\(");
                                this.this$0.f425la.f363Bg = split[0];
                                if (C0685a.m995g("yyyy/MM/dd:HH:mm:ss", split[0], "2021/06/02:00:00:00")) {
                                    this.this$0.f420Ah = true;
                                }
                                Log.d("BTModel", "RETURN_VERSION CQ: mVersionName:" + this.this$0.f425la.mVersionName + " mVersionDate:" + this.this$0.f425la.f363Bg);
                            } catch (Exception e5) {
                                Log.e("BTModel", "RETURN_VERSION: " + e5.getMessage());
                            }
                        } else if (!this.this$0.f425la.mVersionName.startsWith("FD")) {
                            try {
                                String str4 = str.split("_")[2];
                                this.this$0.f425la.f363Bg = str4;
                                Log.d("BTModel", "RETURN_VERSION GK: mVersionName:" + this.this$0.f425la.mVersionName + " mVersionDate:" + this.this$0.f425la.f363Bg);
                                if (C0685a.m995g("yyyy/MM/dd:HH:mm:ss", str4, "2021/05/26:00:00:00")) {
                                    this.this$0.f420Ah = true;
                                }
                            } catch (Exception e6) {
                                Log.e("BTModel", "RETURN_VERSION: " + e6.getMessage());
                            }
                        }
                    }
                    z = this.this$0.f422Ch;
                    if (!z) {
                        return true;
                    }
                    Iterator it25 = this.this$0.f429yh.entrySet().iterator();
                    while (it25.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it25.next()).getValue()).mo299ja(str);
                    }
                    this.this$0.f422Ch = false;
                    return true;
                case 50:
                    Iterator it26 = this.this$0.f429yh.entrySet().iterator();
                    while (it26.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it26.next()).getValue()).mo283a((message.arg1 + 1) * 10, message.arg1 >= 9);
                    }
                    return true;
                case 59:
                    Iterator it27 = this.this$0.f429yh.entrySet().iterator();
                    while (it27.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it27.next()).getValue()).mo285b(message.arg1, str2, str);
                    }
                    c0532b11 = this.this$0.f428xh;
                    if (c0532b11 == null) {
                        return true;
                    }
                    c0532b12 = this.this$0.f428xh;
                    c0532b12.m191h(message.arg1, str2, str);
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
                    int i10 = (message.arg1 << 4) | message.arg2;
                    c0535c14 = this.this$0.f427wh;
                    c0535c14.write(1283, 16, i10);
                    this.this$0.f425la.f382Ug = message.arg2;
                    Context context = this.this$0.mContext;
                    C0533a unused = this.this$0.f425la;
                    C0533a unused2 = this.this$0.f425la;
                    C0699o.m1026a(context, "TABLE_BT", "BATTERY_LEVEL", this.this$0.f425la.f382Ug);
                    Iterator it28 = this.this$0.f429yh.entrySet().iterator();
                    while (it28.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it28.next()).getValue()).mo296i(message.arg1, message.arg2);
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
                        String m997Rc = C0686b.m997Rc();
                        this.this$0.f425la.f381Tg = Integer.getInteger(m997Rc).intValue();
                        Log.d("mxy", "010b mic:" + m997Rc);
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
                    boolean z3 = (message.arg1 & 65536) == 65536;
                    Iterator it29 = this.this$0.f429yh.entrySet().iterator();
                    while (it29.hasNext()) {
                        ((InterfaceC0545g) ((Map.Entry) it29.next()).getValue()).mo306q(z3);
                    }
                    return true;
                case InputDeviceCompat.SOURCE_DPAD /* 513 */:
                    int i11 = message.arg2;
                    Log.d("BTModel", "handleMessage: 0x0201:keyCode:" + i11);
                    if (i11 != 9) {
                        if (i11 != 10) {
                            if (i11 == 24 || i11 == 25) {
                                if (this.this$0.f425la.f403sg == 2) {
                                    int i12 = this.this$0.f425la.f364Cg;
                                    if (i12 != 1) {
                                        if (i12 == 2 || i12 == 3 || i12 == 4) {
                                            if (i11 != 24 || message.arg1 == 1) {
                                                c0535c17 = this.this$0.f427wh;
                                                c0535c17.write(10, 0);
                                            } else {
                                                c0535c18 = this.this$0.f427wh;
                                                c0535c18.write(33281, 1, 47);
                                            }
                                        }
                                    } else if (i11 == 24 && message.arg1 == 1) {
                                        c0535c16 = this.this$0.f427wh;
                                        c0535c16.write(10, 1);
                                    } else {
                                        c0535c15 = this.this$0.f427wh;
                                        c0535c15.write(10, 2);
                                    }
                                } else {
                                    if (this.this$0.f425la.f384Wg && i11 == 24) {
                                        if (message.arg1 == 1) {
                                            this.this$0.m218Oa(5);
                                        } else {
                                            this.this$0.m218Oa(6);
                                        }
                                    }
                                    if (this.this$0.f425la.f384Wg && i11 == 25) {
                                        this.this$0.m218Oa(6);
                                    }
                                }
                            } else if (i11 == 33 || i11 == 63) {
                                boolean z4 = this.this$0.f425la.f407wg;
                            } else if (i11 != 90) {
                                switch (i11) {
                                    case 60:
                                        boolean z5 = this.this$0.f425la.f407wg;
                                        break;
                                    case 61:
                                        boolean z6 = this.this$0.f425la.f407wg;
                                        break;
                                }
                            } else {
                                if (Settings.System.getInt(this.this$0.mContext.getContentResolver(), "SWC_VOICE_MODE", 0) != 0 ? message.arg1 != 1 || (this.this$0.f425la.f384Wg && this.this$0.f425la.f385Xg) : message.arg1 != 2 || (this.this$0.f425la.f384Wg && this.this$0.f425la.f385Xg)) {
                                    z2 = false;
                                    Log.d("BTModel", "0x0201:0x5a:needWakeUp phone voice:" + z2);
                                    if (z2) {
                                        this.this$0.m267yb();
                                    }
                                }
                                z2 = true;
                                Log.d("BTModel", "0x0201:0x5a:needWakeUp phone voice:" + z2);
                                if (z2) {
                                }
                            }
                            it = this.this$0.f429yh.entrySet().iterator();
                            while (it.hasNext()) {
                                ((InterfaceC0545g) ((Map.Entry) it.next()).getValue()).mo286b(message.arg2, message.arg1 != 1);
                            }
                            return true;
                        }
                        boolean z7 = this.this$0.f425la.f407wg;
                        it = this.this$0.f429yh.entrySet().iterator();
                        while (it.hasNext()) {
                        }
                        return true;
                    }
                    boolean z8 = this.this$0.f425la.f407wg;
                    it = this.this$0.f429yh.entrySet().iterator();
                    while (it.hasNext()) {
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
                            c0535c21 = this.this$0.f427wh;
                            c0535c21.write(20, 0);
                        }
                    } else if (message.arg1 == 8) {
                        c0535c19 = this.this$0.f427wh;
                        c0535c19.write(20, 0);
                    }
                    this.this$0.f425la.mSource = message.arg1;
                    c0535c20 = this.this$0.f427wh;
                    c0535c20.write(46, this.this$0.f425la.mSource == 8 ? 1 : 0);
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
                            i3 = this.this$0.f421Bh;
                            if (i3 >= 2) {
                                this.this$0.f421Bh = 0;
                                return true;
                            }
                            this.this$0.mHandler.removeMessages(65287);
                            Message obtainMessage5 = this.this$0.mHandler.obtainMessage();
                            obtainMessage5.what = 65287;
                            obtainMessage5.arg1 = 0;
                            i4 = this.this$0.f421Bh;
                            obtainMessage5.arg2 = i4;
                            this.this$0.mHandler.sendMessage(obtainMessage5);
                            C0544f.m234e(this.this$0);
                            return true;
                        case 69:
                            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                                String substring = str2.substring(0, 1);
                                String substring2 = str2.substring(1);
                                switch (substring.hashCode()) {
                                    case 52:
                                        if (substring.equals("4")) {
                                            c2 = 0;
                                            break;
                                        }
                                        c2 = 65535;
                                        break;
                                    case 53:
                                        if (substring.equals("5")) {
                                            c2 = 1;
                                            break;
                                        }
                                        c2 = 65535;
                                        break;
                                    case 54:
                                        if (substring.equals("6")) {
                                            c2 = 2;
                                            break;
                                        }
                                        c2 = 65535;
                                        break;
                                    default:
                                        c2 = 65535;
                                        break;
                                }
                                if (c2 == 0) {
                                    if (this.this$0.f425la.f387Zg) {
                                        this.this$0.f425la.f395ih.add(new C0719c(substring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 1));
                                    } else {
                                        this.this$0.f425la.f395ih.add(0, new C0719c(substring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 1));
                                    }
                                    Iterator it33 = this.this$0.f429yh.entrySet().iterator();
                                    while (it33.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it33.next()).getValue()).mo301m(substring2, str);
                                    }
                                } else if (c2 == 1) {
                                    if (this.this$0.f425la.f387Zg) {
                                        this.this$0.f425la.f394hh.add(new C0719c(substring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 0));
                                    } else {
                                        this.this$0.f425la.f394hh.add(0, new C0719c(substring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 0));
                                    }
                                    Iterator it34 = this.this$0.f429yh.entrySet().iterator();
                                    while (it34.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it34.next()).getValue()).mo308s(substring2, str);
                                    }
                                } else if (c2 == 2) {
                                    if (this.this$0.f425la.f387Zg) {
                                        this.this$0.f425la.f396jh.add(new C0719c(substring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 2));
                                    } else {
                                        this.this$0.f425la.f396jh.add(0, new C0719c(substring2, str, String.valueOf(message.arg1), String.valueOf(message.arg2), 2));
                                    }
                                    Iterator it35 = this.this$0.f429yh.entrySet().iterator();
                                    while (it35.hasNext()) {
                                        ((InterfaceC0545g) ((Map.Entry) it35.next()).getValue()).mo304p(substring2, str);
                                    }
                                }
                                Log.d("BTModel", "RETURN_CALL_LOG_PHONE_DATA: " + message.arg1 + " " + message.arg2 + " " + str2 + " " + str);
                                return true;
                            }
                            return false;
                        case 70:
                            Iterator it36 = this.this$0.f429yh.entrySet().iterator();
                            while (it36.hasNext()) {
                                InterfaceC0545g interfaceC0545g = (InterfaceC0545g) ((Map.Entry) it36.next()).getValue();
                                int i13 = message.arg1;
                                m216Na = this.this$0.m216Na(message.arg1);
                                interfaceC0545g.mo281a(i13, m216Na);
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
                            c0532b13 = this.this$0.f428xh;
                            if (c0532b13 == null) {
                                return true;
                            }
                            c0532b14 = this.this$0.f428xh;
                            c0532b14.m188a(this.this$0.f425la.f369Hg, this.this$0.f425la.f371Jg, this.this$0.f425la.f373Lg, this.this$0.f425la.f372Kg);
                            return true;
                        default:
                            switch (i) {
                                case 65283:
                                    if (this.this$0.f425la.mSource != 8) {
                                        return true;
                                    }
                                    c0535c22 = this.this$0.f427wh;
                                    c0535c22.m206w(false);
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
                                    Message obtainMessage6 = this.this$0.mHandler.obtainMessage();
                                    obtainMessage6.what = 65286;
                                    obtainMessage6.arg1 = 1;
                                    obtainMessage6.arg2 = 0;
                                    this.this$0.mHandler.removeMessages(65286);
                                    this.this$0.mHandler.sendMessageDelayed(obtainMessage6, 3000L);
                                    if (this.this$0.f425la.f374Mg) {
                                        this.this$0.m215Me();
                                        return true;
                                    }
                                    this.this$0.m217Ne();
                                    return true;
                                case 65286:
                                    int i14 = message.arg1;
                                    if (i14 == 0) {
                                        Iterator it39 = this.this$0.f429yh.entrySet().iterator();
                                        while (it39.hasNext()) {
                                            ((InterfaceC0545g) ((Map.Entry) it39.next()).getValue()).mo279W();
                                        }
                                        return true;
                                    }
                                    if (i14 != 1) {
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
                                    c0535c23 = this.this$0.f427wh;
                                    if (c0535c23 == null) {
                                        return true;
                                    }
                                    if (message.arg1 != 0) {
                                        c0535c24 = this.this$0.f427wh;
                                        c0535c24.write(26, 255);
                                        return true;
                                    }
                                    i5 = this.this$0.f421Bh;
                                    if (i5 == 0) {
                                        this.this$0.f425la.f395ih.clear();
                                    } else if (i5 == 1) {
                                        this.this$0.f425la.f394hh.clear();
                                    } else if (i5 == 2) {
                                        this.this$0.f425la.f396jh.clear();
                                    }
                                    c0535c25 = this.this$0.f427wh;
                                    i6 = this.this$0.f421Bh;
                                    c0535c25.write(67, i6);
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
                                    for (int i15 = 0; i15 < this.this$0.f425la.f392fh.size(); i15++) {
                                        for (int i16 = 0; i16 < this.this$0.f425la.f393gh.size(); i16++) {
                                            if (TextUtils.equals(this.this$0.f425la.f393gh.get(i16).m1131eb(), this.this$0.f425la.f392fh.get(i15).m1131eb())) {
                                                this.this$0.f425la.f392fh.get(i15).m1130A(true);
                                            }
                                        }
                                    }
                                    for (int i17 = 0; i17 < this.this$0.f425la.f390dh.size(); i17++) {
                                        for (int i18 = 0; i18 < this.this$0.f425la.f393gh.size(); i18++) {
                                            if (TextUtils.equals(this.this$0.f425la.f393gh.get(i18).m1131eb(), this.this$0.f425la.f390dh.get(i17).m1131eb())) {
                                                this.this$0.f425la.f390dh.get(i17).m1130A(true);
                                            }
                                        }
                                    }
                                    for (int i19 = 0; i19 < this.this$0.f425la.f391eh.size(); i19++) {
                                        for (int i20 = 0; i20 < this.this$0.f425la.f393gh.size(); i20++) {
                                            if (TextUtils.equals(this.this$0.f425la.f393gh.get(i20).m1131eb(), this.this$0.f425la.f391eh.get(i19).m1131eb())) {
                                                this.this$0.f425la.f391eh.get(i19).m1130A(true);
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
                                    c0535c26 = this.this$0.f427wh;
                                    c0535c26.m206w(true);
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
                c0535c3 = this.this$0.f427wh;
                c0535c3.write(3, 1);
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
                c0535c2 = this.this$0.f427wh;
                c0535c2.write(3, 2);
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
