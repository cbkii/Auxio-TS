package p075d.p076a.p077a.p068a;

/* renamed from: d.a.a.a.A */
/* loaded from: classes4.dex */
public abstract class AbstractC0804A {
    /* renamed from: a */
    public static byte m1555a(AbstractC0804A abstractC0804A, byte b2, byte b3) {
        int i = (b2 & 255) >> 3;
        int i2 = (b2 & 7) << 2;
        return (byte) ((abstractC0804A.mo1559ke()[(((abstractC0804A.mo1558je() * b3) + ((abstractC0804A.mo1556he()[i] >> i2) & 15)) & 255) >> 3] >> (((((abstractC0804A.mo1558je() * b3) + ((abstractC0804A.mo1556he()[i] >> i2) & 15)) & 255) & 7) << 2)) & 15 & 255);
    }

    public abstract String charset();

    /* renamed from: he */
    public abstract int[] mo1556he();

    /* renamed from: ie */
    public abstract boolean mo1557ie();

    /* renamed from: je */
    public abstract int mo1558je();

    /* renamed from: ke */
    public abstract int[] mo1559ke();
}
