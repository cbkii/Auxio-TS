package p065d.p066a.p067a.p068a;

/* JADX INFO: renamed from: d.a.a.a.u */
/* JADX INFO: loaded from: classes4.dex */
public class C0825u extends AbstractC0804A {

    /* JADX INFO: renamed from: co */
    public static int[] f1327co;

    /* JADX INFO: renamed from: do */
    public static int f1328do;

    /* JADX INFO: renamed from: eo */
    public static String f1329eo;
    public static int[] states;

    public C0825u() {
        f1327co = new int[32];
        int[] iArr = f1327co;
        iArr[0] = 2;
        iArr[1] = 0;
        iArr[2] = 0;
        iArr[3] = 4096;
        iArr[4] = 196608;
        iArr[5] = 64;
        iArr[6] = 0;
        iArr[7] = 0;
        iArr[8] = 20480;
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
        states = new int[5];
        int[] iArr2 = states;
        iArr2[0] = 285212976;
        iArr2[1] = 572657937;
        iArr2[2] = 289476898;
        iArr2[3] = 286593297;
        iArr2[4] = 8465;
        f1329eo = "ISO-2022-KR";
        f1328do = 6;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    public String charset() {
        return f1329eo;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* JADX INFO: renamed from: he */
    public int[] mo1556he() {
        return f1327co;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* JADX INFO: renamed from: ie */
    public boolean mo1557ie() {
        return false;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* JADX INFO: renamed from: je */
    public int mo1558je() {
        return f1328do;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0804A
    /* JADX INFO: renamed from: ke */
    public int[] mo1559ke() {
        return states;
    }
}
