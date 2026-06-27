package p075d.p076a.p077a.p068a;

/* renamed from: d.a.a.a.k */
/* loaded from: classes4.dex */
public class C0815k {

    /* renamed from: In */
    public int f1304In = 0;
    public int mThreshold = 200;
    public int mState = 0;

    /* renamed from: Jn */
    public int[] f1305Jn = new int[94];

    /* renamed from: Kn */
    public int[] f1306Kn = new int[94];

    /* renamed from: Ln */
    public float[] f1307Ln = new float[94];

    /* renamed from: Mn */
    public float[] f1308Mn = new float[94];

    public C0815k() {
        Reset();
    }

    public void Reset() {
        this.f1304In = 0;
        this.mState = 0;
        for (int i = 0; i < 94; i++) {
            int[] iArr = this.f1305Jn;
            this.f1306Kn[i] = 0;
            iArr[i] = 0;
        }
    }

    /* renamed from: Zd */
    public void m1567Zd() {
        for (int i = 0; i < 94; i++) {
            float[] fArr = this.f1307Ln;
            float f = this.f1305Jn[i];
            float f2 = this.f1304In;
            fArr[i] = f / f2;
            this.f1308Mn[i] = this.f1306Kn[i] / f2;
        }
    }

    /* renamed from: _d */
    public boolean m1568_d() {
        return this.f1304In > this.mThreshold;
    }

    /* renamed from: a */
    public float m1569a(float[] fArr, float f, float[] fArr2, float f2) {
        return (m1570a(fArr2, this.f1308Mn) * f2) + (m1570a(fArr, this.f1307Ln) * f);
    }

    /* renamed from: a */
    public float m1570a(float[] fArr, float[] fArr2) {
        float f = 0.0f;
        for (int i = 0; i < 94; i++) {
            float f2 = fArr[i] - fArr2[i];
            f += f2 * f2;
        }
        return ((float) Math.sqrt(f)) / 94.0f;
    }

    /* renamed from: ae */
    public boolean m1571ae() {
        return this.f1304In > 1;
    }

    /* renamed from: h */
    public boolean m1572h(byte[] bArr, int i) {
        if (this.mState == 1) {
            return false;
        }
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            int i4 = this.mState;
            if (1 == i4) {
                break;
            }
            if (i4 != 0) {
                if (i4 != 1) {
                    if (i4 == 2 && (bArr[i3] & 128) != 0 && 255 != (bArr[i3] & 255) && 161 <= (bArr[i3] & 255)) {
                        this.f1304In++;
                        int[] iArr = this.f1306Kn;
                        int i5 = (bArr[i3] & 255) - 161;
                        iArr[i5] = iArr[i5] + 1;
                        this.mState = 0;
                    }
                    this.mState = 1;
                }
            } else if ((bArr[i3] & 128) != 0) {
                if (255 != (bArr[i3] & 255) && 161 <= (bArr[i3] & 255)) {
                    this.f1304In++;
                    int[] iArr2 = this.f1305Jn;
                    int i6 = (255 & bArr[i3]) - 161;
                    iArr2[i6] = iArr2[i6] + 1;
                    this.mState = 2;
                }
                this.mState = 1;
            }
            i2++;
            i3++;
        }
        return 1 != this.mState;
    }
}
