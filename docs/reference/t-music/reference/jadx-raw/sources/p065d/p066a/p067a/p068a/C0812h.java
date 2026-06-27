package p065d.p066a.p067a.p068a;

/* renamed from: d.a.a.a.h */
/* loaded from: classes4.dex */
public class C0812h extends AbstractC0826v implements InterfaceC0822r {
    public InterfaceC0821q mObserver;

    public C0812h(int i) {
        super(i);
        this.mObserver = null;
    }

    /* renamed from: a */
    public void m1564a(InterfaceC0821q interfaceC0821q) {
        this.mObserver = interfaceC0821q;
    }

    /* renamed from: b */
    public boolean m1565b(byte[] bArr, int i, boolean z) {
        if (bArr == null || z) {
            return false;
        }
        m1578i(bArr, i);
        return this.mDone;
    }

    @Override // p065d.p066a.p067a.p068a.AbstractC0826v
    /* renamed from: tb */
    public void mo1566tb(String str) {
        InterfaceC0821q interfaceC0821q = this.mObserver;
        if (interfaceC0821q != null) {
            interfaceC0821q.Notify(str);
        }
    }
}
