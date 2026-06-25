package p065d.p066a.p067a.p068a;

/* renamed from: d.a.a.a.p */
/* loaded from: classes4.dex */
public class C0820p extends AbstractC0804A {

    /* renamed from: co */
    public static int[] f1318co;

    /* renamed from: do */
    public static int f1319do;

    /* renamed from: eo */
    public static String f1320eo;
    public static int[] states;

    public C0820p() {
        f1318co = new int[32];
        int[] iArr = f1318co;
        iArr[0] = 1;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 4096;
        iArr[4] = 0;
        iArr[5] = 0;
        iArr[6] = 0;
        iArr[7] = 0;
        iArr[8] = 0;
        iArr[9] = 0;
        iArr[10] = 0;
        iArr[11] = 0;
        iArr[12] = 0;
        iArr[13] = 0;
        iArr[14] = 0;
        iArr[15] = 38813696;
        iArr[16] = 286331153;
        iArr[17] = 286331153;
        iArr[18] = 286331153;
        iArr[19] = 286331153;
        iArr[20] = 286331153;
        iArr[21] = 286331153;
        iArr[22] = 286331153;
        iArr[23] = 286331153;
        iArr[24] = 286331153;
        iArr[25] = 286331153;
        iArr[26] = 286331153;
        iArr[27] = 286331153;
        iArr[28] = 286331153;
        iArr[29] = 286331153;
        iArr[30] = 286331153;
        iArr[31] = 286331153;
        states = new int[6];
        int[] iArr2 = states;
        iArr2[0] = 285213456;
        iArr2[1] = 572657937;
        iArr2[2] = 335548706;
        iArr2[3] = 341120533;
        iArr2[4] = 336872468;
        iArr2[5] = 36;
        f1320eo = "HZ-GB-2312";
        f1319do = 6;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    public String charset() {
        return f1320eo;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* renamed from: he */
    public int[] mo1556he() {
        return f1318co;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* renamed from: ie */
    public boolean mo1557ie() {
        return false;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* renamed from: je */
    public int mo1558je() {
        return f1319do;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* renamed from: ke */
    public int[] mo1559ke() {
        return states;
    }
}
