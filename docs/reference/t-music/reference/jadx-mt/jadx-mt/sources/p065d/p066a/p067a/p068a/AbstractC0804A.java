package p065d.p066a.p067a.p068a;

/* JADX INFO: renamed from: d.a.a.a.A */
/* JADX INFO: loaded from: classes4.dex */
public abstract class AbstractC0804A {
    /* JADX INFO: renamed from: a */
    public static byte m1555a(AbstractC0804A abstractC0804A, byte b2, byte b3) {
        int i = (b2 & 255) >> 3;
        int i2 = (b2 & 7) << 2;
        return (byte) ((abstractC0804A.mo1559ke()[(((abstractC0804A.mo1558je() * b3) + ((abstractC0804A.mo1556he()[i] >> i2) & 15)) & 255) >> 3] >> (((((abstractC0804A.mo1558je() * b3) + ((abstractC0804A.mo1556he()[i] >> i2) & 15)) & 255) & 7) << 2)) & 15 & 255);
    }

    public abstract String charset();

    /* JADX INFO: renamed from: he */
    public abstract int[] mo1556he();

    /* JADX INFO: renamed from: ie */
    public abstract boolean mo1557ie();

    /* JADX INFO: renamed from: je */
    public abstract int mo1558je();

    /* JADX INFO: renamed from: ke */
    public abstract int[] mo1559ke();
}
