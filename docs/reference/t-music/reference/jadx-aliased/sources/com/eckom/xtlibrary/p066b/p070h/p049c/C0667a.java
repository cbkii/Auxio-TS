package com.eckom.xtlibrary.p066b.p070h.p049c;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.eckom.xtlibrary.p066b.p031c.InterfaceC0564c;
import com.eckom.xtlibrary.p066b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p066b.p070h.p047a.C0660a;
import com.eckom.xtlibrary.p066b.p070h.p048b.C0665e;
import com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f;
import com.eckom.xtlibrary.p066b.p070h.p051e.InterfaceC0670a;

/* compiled from: RadioPresenter.java */
/* renamed from: com.eckom.xtlibrary.b.h.c.a */
/* loaded from: classes3.dex */
public class C0667a extends AbstractC0658a<InterfaceC0670a, C0665e> implements InterfaceC0666f {

    /* renamed from: Hk */
    public InterfaceC0564c f789Hk;
    private Context mContext;

    public C0667a(Context context) {
        super(context);
        this.mContext = context;
    }

    private void onCreate() {
        ((C0665e) this.mModel).m836a(this.mContext);
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: A */
    public void mo853A(int i) {
        if (get() != null) {
            get().mo895A(i);
        }
    }

    /* renamed from: B */
    public void m880B(boolean z) {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m829B(z);
        }
    }

    /* renamed from: C */
    public void m881C(boolean z) {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m830C(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: E */
    public void mo854E(int i) {
        if (get() != null) {
            get().mo896E(i);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: I */
    public void mo855I(int i) {
        if (get() != null) {
            get().mo897I(i);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: K */
    public void mo856K(int i) {
        if (get() != null) {
            get().mo898K(i);
        }
    }

    /* renamed from: Ra */
    public void m882Ra(String str) {
        ((C0665e) this.mModel).m832Ha(str);
        ((C0665e) this.mModel).m852zb();
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: S */
    public void mo857S(int i) {
        if (get() != null) {
            get().mo899S(i);
        }
    }

    /* renamed from: Ta */
    public void m883Ta(String str) {
        ((C0665e) this.mModel).onResume();
        C0665e.getInstance().m838a(str, this);
    }

    /* renamed from: Va */
    public void m884Va(String str) {
        onCreate();
        C0665e.getInstance().m838a(str, this);
    }

    /* renamed from: Zb */
    public boolean m885Zb() {
        M m = this.mModel;
        if (m != 0) {
            return ((C0665e) m).m834Zb();
        }
        return false;
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: a */
    public void mo861a(C0660a[] c0660aArr) {
        if (get() != null) {
            get().mo902a(c0660aArr);
        }
    }

    /* renamed from: ac */
    public void m886ac() {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m839ac();
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: ba */
    public void mo862ba(String str) {
        if (get() != null) {
            get().mo904ba(str);
        }
    }

    /* renamed from: cc */
    public void m887cc() {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m844cc();
        }
    }

    /* renamed from: dc */
    public void m888dc() {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m845dc();
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: e */
    public void mo863e(boolean z) {
        if (get() != null) {
            get().mo905e(z);
        }
    }

    /* renamed from: ec */
    public void m889ec() {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m846ec();
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: f */
    public void mo864f(boolean z) {
        if (get() != null) {
            get().mo906f(z);
        }
    }

    /* renamed from: fc */
    public void m890fc() {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m847fc();
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: ha */
    public void mo865ha(String str) {
        if (get() != null) {
            get().mo907ha(str);
        }
    }

    /* renamed from: hc */
    public void m891hc() {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m848hc();
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: i */
    public void mo866i(boolean z) {
        if (get() != null) {
            get().mo908i(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: k */
    public void mo867k(boolean z) {
        if (get() != null) {
            get().mo909k(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: m */
    public void mo868m(boolean z) {
        if (get() != null) {
            get().mo910m(z);
        }
    }

    /* renamed from: ma */
    public void m892ma(int i) {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m849ma(i);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: n */
    public void mo869n(int i) {
        if (get() != null) {
            get().mo911n(i);
        }
    }

    /* renamed from: na */
    public void m893na(int i) {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m850na(i);
        }
    }

    public void next() {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).next();
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: p */
    public void mo870p(boolean z) {
        if (get() != null) {
            get().mo912p(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: q */
    public void mo871q(boolean z) {
        if (get() != null) {
            get().mo913q(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: r */
    public void mo872r(int i) {
        if (get() != null) {
            get().mo914r(i);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: s */
    public void mo875s(boolean z) {
        if (get() != null) {
            get().mo917s(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: t */
    public void mo876t(boolean z) {
        if (get() != null) {
            get().mo918t(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: u */
    public void mo877u(boolean z) {
        if (get() != null) {
            get().mo919u(z);
        }
    }

    /* renamed from: w */
    public void m894w(boolean z) {
        M m = this.mModel;
        if (m != 0) {
            ((C0665e) m).m851w(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: x */
    public void mo878x(int i) {
        if (get() != null) {
            get().mo920x(i);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: y */
    public void mo879y(int i) {
        if (get() != null) {
            get().mo921y(i);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p045g.AbstractC0658a
    /* renamed from: getModel, reason: avoid collision after fix types in other method */
    public C0665e getModel2() {
        return C0665e.getInstance();
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: a */
    public void mo858a(int i, int i2, int i3, int i4, int i5) {
        if (get() != null) {
            get().mo900a(i, i2, i3, i4, i5);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: r */
    public void mo873r(boolean z) {
        if (get() != null) {
            get().mo915r(z);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: s */
    public void mo874s(int i) {
        if (get() != null) {
            get().mo916s(i);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: a */
    public void mo860a(Drawable drawable) {
        if (get() != null) {
            get().mo903b(drawable);
        }
    }

    @Override // com.eckom.xtlibrary.p066b.p070h.p048b.InterfaceC0666f
    /* renamed from: a */
    public void mo859a(int i, int i2, int i3, int i4, int i5, int i6) {
        if (get() != null) {
            get().mo901a(i, i2, i3, i4, i5, i6);
        }
    }
}
