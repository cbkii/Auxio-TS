package com.eckom.xtlibrary.p020b.p030b;

import android.os.Bundle;
import android.util.Log;
import com.eckom.xtlibrary.p020b.C0556b;
import com.eckom.xtlibrary.p020b.p021a.p026e.C0549a;
import com.eckom.xtlibrary.p020b.p031c.InterfaceC0562a;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import p011c.p015b.p016a.p017a.p018a.InterfaceC0513a;
import p011c.p015b.p016a.p017a.p018a.InterfaceC0516d;

/* compiled from: BTCallBackImp.java */
/* renamed from: com.eckom.xtlibrary.b.b.a */
/* loaded from: classes3.dex */
public class BinderC0557a extends InterfaceC0513a.a {

    /* renamed from: cd */
    private InterfaceC0516d f456cd;

    /* renamed from: dd */
    private C0549a f457dd;

    public BinderC0557a(AbstractC0658a abstractC0658a, InterfaceC0516d interfaceC0516d) {
        this.f456cd = interfaceC0516d;
        if (abstractC0658a instanceof C0549a) {
            this.f457dd = (C0549a) abstractC0658a;
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: H */
    public void mo117H() {
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m387H();
            } else {
                c0549a.answer();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: Z */
    public void mo118Z() {
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m388Z();
            } else {
                c0549a.m321qb();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: a */
    public void mo119a(Bundle bundle) {
        InterfaceC0562a interfaceC0562a;
        Log.d("BTCallBackImp", "extendedInterface:" + bundle.getString("action"));
        C0549a c0549a = this.f457dd;
        if (c0549a == null || (interfaceC0562a = c0549a.f439Hk) == null) {
            return;
        }
        interfaceC0562a.m389a(bundle);
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: aa */
    public void mo120aa() {
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m390aa();
            } else {
                c0549a.m320pb();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: ca */
    public void mo121ca() {
        Log.d("BTCallBackImp", "btGetConnectedStatus**7777**btGetConnectedStatus#:" + this.f457dd.m317mb());
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m391ca();
            } else {
                C0556b.getInstant().f455cd.mo154m(this.f457dd.m317mb());
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: da */
    public void mo122da() {
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m392da();
            } else {
                c0549a.m322rb();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: ea */
    public void mo123ea(String str) {
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m393ea(str);
                return;
            }
            c0549a.m310Aa(str + "");
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: ga */
    public void mo124ga() {
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m394ga();
            } else {
                c0549a.m321qb();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: ja */
    public void mo125ja() {
        Log.d("BTCallBackImp", "btGetPhoneStatus**777**btGetPhoneStatus#:" + this.f457dd.getCallState());
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m395ja();
            } else {
                C0556b.getInstant().f455cd.mo141W(this.f457dd.getCallState());
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0513a
    /* renamed from: la */
    public void mo126la() {
        C0549a c0549a = this.f457dd;
        if (c0549a != null) {
            InterfaceC0562a interfaceC0562a = c0549a.f439Hk;
            if (interfaceC0562a != null) {
                interfaceC0562a.m396la();
            } else {
                c0549a.m318nb();
            }
        }
    }
}
