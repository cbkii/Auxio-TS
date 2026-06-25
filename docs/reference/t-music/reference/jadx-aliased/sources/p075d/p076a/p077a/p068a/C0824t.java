package p075d.p076a.p077a.p068a;

/* renamed from: d.a.a.a.t */
/* loaded from: classes4.dex */
public class C0824t extends AbstractC0804A {

    /* renamed from: co */
    public static int[] f1324co;

    /* renamed from: do */
    public static int f1325do;

    /* renamed from: eo */
    public static String f1326eo;
    public static int[] states;

    public C0824t() {
        f1324co = new int[32];
        int[] iArr = f1324co;
        iArr[0] = 2;
        iArr[1] = 570425344;
        iArr[2] = 0;
        iArr[3] = 4096;
        iArr[4] = 458752;
        iArr[5] = 3;
        iArr[6] = 0;
        iArr[7] = 0;
        iArr[8] = 1030;
        iArr[9] = 1280;
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
        states = new int[6];
        int[] iArr2 = states;
        iArr2[0] = 304;
        iArr2[1] = 286331153;
        iArr2[2] = 572662306;
        iArr2[3] = 1091653905;
        iArr2[4] = 303173905;
        iArr2[5] = 287445265;
        f1326eo = "ISO-2022-JP";
        f1325do = 8;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    public String charset() {
        return f1326eo;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: he */
    public int[] mo1556he() {
        return f1324co;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: ie */
    public boolean mo1557ie() {
        return false;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: je */
    public int mo1558je() {
        return f1325do;
    }

    @Override // p075d.p076a.p077a.p068a.AbstractC0804A
    /* renamed from: ke */
    public int[] mo1559ke() {
        return states;
    }
}
