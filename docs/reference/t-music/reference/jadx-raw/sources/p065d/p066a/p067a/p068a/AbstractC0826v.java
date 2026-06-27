package p065d.p066a.p067a.p068a;

/* renamed from: d.a.a.a.v */
/* loaded from: classes4.dex */
public abstract class AbstractC0826v {

    /* renamed from: Un */
    public AbstractC0804A[] f1330Un;

    /* renamed from: Vn */
    public AbstractC0816l[] f1331Vn;

    /* renamed from: Yn */
    public int f1334Yn;

    /* renamed from: Zn */
    public boolean f1335Zn;

    /* renamed from: _n */
    public boolean f1336_n;
    public boolean mDone;
    public int mItems;

    /* renamed from: Wn */
    public C0815k f1332Wn = new C0815k();
    public byte[] mState = new byte[16];

    /* renamed from: Xn */
    public int[] f1333Xn = new int[16];

    public AbstractC0826v(int i) {
        m1573Aa(i);
        Reset();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0246  */
    /* renamed from: Aa */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void m1573Aa(int i) {
        AbstractC0816l[] abstractC0816lArr;
        AbstractC0804A[] abstractC0804AArr;
        int i2 = i;
        if (i2 < 0 || i2 >= 6) {
            i2 = 0;
        }
        this.f1330Un = null;
        this.f1331Vn = null;
        if (i2 != 4) {
            if (i2 == 5) {
                abstractC0804AArr = new AbstractC0804A[]{new C0830z(), new C0814j(), new C0825u(), new C0811g(), new C0828x(), new C0829y()};
            } else if (i2 == 3) {
                abstractC0804AArr = new AbstractC0804A[]{new C0830z(), new C0819o(), new C0818n(), new C0823s(), new C0820p(), new C0811g(), new C0828x(), new C0829y()};
            } else if (i2 == 1) {
                abstractC0804AArr = new AbstractC0804A[]{new C0830z(), new C0827w(), new C0813i(), new C0824t(), new C0811g(), new C0828x(), new C0829y()};
            } else {
                if (i2 != 2) {
                    if (i2 == 0) {
                        this.f1330Un = new AbstractC0804A[]{new C0830z(), new C0827w(), new C0813i(), new C0824t(), new C0814j(), new C0825u(), new C0810f(), new C0817m(), new C0819o(), new C0818n(), new C0823s(), new C0820p(), new C0811g(), new C0828x(), new C0829y()};
                        abstractC0816lArr = new AbstractC0816l[]{null, null, new C0806b(), null, new C0807c(), null, new C0805a(), new C0808d(), new C0809e(), null, null, null, null, null, null};
                    }
                    this.f1336_n = this.f1331Vn != null;
                    this.f1334Yn = this.f1330Un.length;
                }
                this.f1330Un = new AbstractC0804A[]{new C0830z(), new C0819o(), new C0818n(), new C0810f(), new C0823s(), new C0820p(), new C0817m(), new C0811g(), new C0828x(), new C0829y()};
                abstractC0816lArr = new AbstractC0816l[]{null, new C0809e(), null, new C0805a(), null, null, new C0808d(), null, null, null};
            }
            this.f1330Un = abstractC0804AArr;
            this.f1336_n = this.f1331Vn != null;
            this.f1334Yn = this.f1330Un.length;
        }
        this.f1330Un = new AbstractC0804A[]{new C0830z(), new C0810f(), new C0823s(), new C0817m(), new C0811g(), new C0828x(), new C0829y()};
        abstractC0816lArr = new AbstractC0816l[]{null, new C0805a(), null, new C0808d(), null, null, null};
        this.f1331Vn = abstractC0816lArr;
        this.f1336_n = this.f1331Vn != null;
        this.f1334Yn = this.f1330Un.length;
    }

    public void Reset() {
        this.f1335Zn = this.f1336_n;
        this.mDone = false;
        this.mItems = this.f1334Yn;
        for (int i = 0; i < this.mItems; i++) {
            this.mState[i] = 0;
            this.f1333Xn[i] = i;
        }
        this.f1332Wn.Reset();
    }

    /* renamed from: a */
    public void m1574a(byte[] bArr, int i, boolean z) {
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < this.mItems; i4++) {
            if (this.f1331Vn[this.f1333Xn[i4]] != null) {
                i2++;
            }
            if (!this.f1330Un[this.f1333Xn[i4]].mo1557ie() && !this.f1330Un[this.f1333Xn[i4]].charset().equals("GB18030")) {
                i3++;
            }
        }
        this.f1335Zn = i2 > 1;
        if (this.f1335Zn) {
            this.f1335Zn = this.f1332Wn.m1572h(bArr, i);
            if (((z && this.f1332Wn.m1571ae()) || this.f1332Wn.m1568_d()) && i2 == i3) {
                this.f1332Wn.m1567Zd();
                int i5 = -1;
                float f = 0.0f;
                int i6 = 0;
                for (int i7 = 0; i7 < this.mItems; i7++) {
                    AbstractC0816l[] abstractC0816lArr = this.f1331Vn;
                    int[] iArr = this.f1333Xn;
                    if (abstractC0816lArr[iArr[i7]] != null && !this.f1330Un[iArr[i7]].charset().equals("Big5")) {
                        float m1569a = this.f1332Wn.m1569a(this.f1331Vn[this.f1333Xn[i7]].mo1560be(), this.f1331Vn[this.f1333Xn[i7]].mo1561ce(), this.f1331Vn[this.f1333Xn[i7]].mo1562de(), this.f1331Vn[this.f1333Xn[i7]].mo1563ee());
                        int i8 = i6 + 1;
                        if (i6 == 0 || f > m1569a) {
                            i5 = i7;
                            f = m1569a;
                        }
                        i6 = i8;
                    }
                }
                if (i5 >= 0) {
                    mo1566tb(this.f1330Un[this.f1333Xn[i5]].charset());
                    this.mDone = true;
                }
            }
        }
    }

    /* renamed from: fe */
    public void m1575fe() {
        AbstractC0804A abstractC0804A;
        if (this.mDone) {
            return;
        }
        if (this.mItems == 2) {
            if (this.f1330Un[this.f1333Xn[0]].charset().equals("GB18030")) {
                abstractC0804A = this.f1330Un[this.f1333Xn[1]];
            } else if (this.f1330Un[this.f1333Xn[1]].charset().equals("GB18030")) {
                abstractC0804A = this.f1330Un[this.f1333Xn[0]];
            }
            mo1566tb(abstractC0804A.charset());
            this.mDone = true;
        }
        if (this.f1335Zn) {
            m1574a(null, 0, true);
        }
    }

    /* renamed from: ge */
    public String[] m1576ge() {
        int i = this.mItems;
        if (i <= 0) {
            return new String[]{"nomatch"};
        }
        String[] strArr = new String[i];
        for (int i2 = 0; i2 < this.mItems; i2++) {
            strArr[i2] = this.f1330Un[this.f1333Xn[i2]].charset();
        }
        return strArr;
    }

    /* renamed from: h */
    public void m1577h(byte[] bArr, int i) {
        m1574a(bArr, i, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0053, code lost:
    
        if (r4 > 1) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0060, code lost:
    
        r2 = 0;
        r3 = 0;
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0065, code lost:
    
        if (r2 >= r8.mItems) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0073, code lost:
    
        if (r8.f1330Un[r8.f1333Xn[r2]].mo1557ie() != false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0081, code lost:
    
        if (r8.f1330Un[r8.f1333Xn[r2]].mo1557ie() != false) goto L53;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0083, code lost:
    
        r3 = r3 + 1;
        r4 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0086, code lost:
    
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0089, code lost:
    
        if (1 != r3) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0094, code lost:
    
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x008b, code lost:
    
        r9 = r8.f1330Un[r8.f1333Xn[r4]];
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0055, code lost:
    
        if (1 != r4) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0057, code lost:
    
        r9 = r8.f1330Un[r8.f1333Xn[0]];
     */
    /* renamed from: i */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean m1578i(byte[] bArr, int i) {
        AbstractC0804A abstractC0804A;
        int i2 = 0;
        loop0: while (i2 < i) {
            byte b2 = bArr[i2];
            int i3 = 0;
            while (true) {
                int i4 = this.mItems;
                if (i3 >= i4) {
                    break;
                }
                byte m1555a = AbstractC0804A.m1555a(this.f1330Un[this.f1333Xn[i3]], b2, this.mState[i3]);
                if (m1555a == 2) {
                    abstractC0804A = this.f1330Un[this.f1333Xn[i3]];
                    break loop0;
                }
                if (m1555a == 1) {
                    this.mItems--;
                    int i5 = this.mItems;
                    if (i3 < i5) {
                        int[] iArr = this.f1333Xn;
                        iArr[i3] = iArr[i5];
                        byte[] bArr2 = this.mState;
                        bArr2[i3] = bArr2[i5];
                    }
                } else {
                    this.mState[i3] = m1555a;
                    i3++;
                }
            }
            mo1566tb(abstractC0804A.charset());
            this.mDone = true;
            return this.mDone;
        }
        if (this.f1335Zn) {
            m1577h(bArr, i);
        }
        return this.mDone;
    }

    /* renamed from: tb */
    public abstract void mo1566tb(String str);
}
