package com.eckom.xtlibrary.twproject.service;

import android.graphics.drawable.Drawable;
import com.eckom.xtlibrary.p020b.p046h.p047a.C0660a;
import com.eckom.xtlibrary.p020b.p046h.p049c.C0667a;
import com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a;

/* loaded from: classes3.dex */
public abstract class BaseRadioService extends XTService<C0667a> implements InterfaceC0670a {

    /* renamed from: Va */
    private String f909Va = "";

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: A */
    public void mo895A(int i) {
    }

    /* renamed from: Da */
    public abstract String m1152Da();

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: E */
    public void mo896E(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: I */
    public void mo897I(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: K */
    public void mo898K(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: S */
    public void mo899S(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: a */
    public void mo900a(int i, int i2, int i3, int i4, int i5) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: a */
    public void mo901a(int i, int i2, int i3, int i4, int i5, int i6) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: a */
    public void mo902a(C0660a[] c0660aArr) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: b */
    public void mo903b(Drawable drawable) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: ba */
    public void mo904ba(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: e */
    public void mo905e(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: f */
    public void mo906f(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: ha */
    public void mo907ha(String str) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: i */
    public void mo908i(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: k */
    public void mo909k(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: m */
    public void mo910m(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: n */
    public void mo911n(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onCreate() {
        super.onCreate();
        this.f909Va = m1152Da();
        ((C0667a) this.mPresenter).m881C(false);
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: p */
    public void mo912p(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: q */
    public void mo913q(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: r */
    public void mo914r(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: r */
    public void mo915r(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: s */
    public void mo916s(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: s */
    public void mo917s(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: t */
    public void mo918t(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: u */
    public void mo919u(boolean z) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: x */
    public void mo920x(int i) {
    }

    @Override // com.eckom.xtlibrary.p020b.p046h.p051e.InterfaceC0670a
    /* renamed from: y */
    public void mo921y(int i) {
    }

    @Override // com.eckom.xtlibrary.twproject.service.XTService
    /* renamed from: za, reason: avoid collision after fix types in other method */
    public C0667a mo1151za() {
        return new C0667a(this);
    }
}
