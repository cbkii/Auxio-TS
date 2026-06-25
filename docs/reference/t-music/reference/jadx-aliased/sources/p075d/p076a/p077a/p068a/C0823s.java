package p075d.p076a.p077a.p068a;

/* renamed from: d.a.a.a.s */
/* loaded from: classes4.dex */
public class C0823s extends AbstractC0804A {

    /* renamed from: co */
    public static int[] f1321co;

    /* renamed from: do */
    public static int f1322do;

    /* renamed from: eo */
    public static String f1323eo;
    public static int[] states;

    public C0823s() {
        f1321co = new int[32];
        int[] iArr = f1321co;
        iArr[0] = 2;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 4096;
        iArr[4] = 0;
        iArr[5] = 48;
        iArr[6] = 0;
        iArr[7] = 0;
        iArr[8] = 16384;
        iArr[9] = 0;
        iArr[10] = 0;
        iArr[11] = 0;
        iArr[12] = 0;
        iArr[13] = 0;
        iArr[14] = 0;
        iArr[15] = 0;
        iArr[16] = 572662306;
        iArr[17] = 572662306;
        iArr[18] = 572662306;
        iArr[19] = 572662306;
        iArr[20] = 572662306;
        iArr[21] = 572662306;
        iArr[22] = 572662306;
        iArr[23] = 572662306;
        iArr[24] = 572662306;
        iArr[25] = 572662306;
        iArr[26] = 572662306;
        iArr[27] = 572662306;
        iArr[28] = 572662306;
        iArr[29] = 572662306;
        iArr[30] = 572662306;
        iArr[31] = 572662306;
        states = new int[8];
        int[] iArr2 = states;
        iArr2[0] = 304;
        iArr2[1] = 286331152;
        iArr2[2] = 572662289;
        iArr2[3] = 336663074;
        iArr2[4] = 286335249;
        iArr2[5] = 286331237;
        iArr2[6] = 286335249;
        iArr2[7] = 18944273;
        f1323eo = "ISO-2022-CN";
        f1322do = 9;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    public String charset() {
        return f1323eo;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: he */
    public int[] mo1556he() {
        return f1321co;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: ie */
    public boolean mo1557ie() {
        return false;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: je */
    public int mo1558je() {
        return f1322do;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: ke */
    public int[] mo1559ke() {
        return states;
    }
}
