package com.eckom.xtlibrary.p020b.p030b;

import android.os.Bundle;
import com.eckom.xtlibrary.p020b.p031c.InterfaceC0565d;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p054k.p056b.C0706a;
import p011c.p015b.p016a.p017a.p018a.InterfaceC0518f;

/* compiled from: VideoCallBackImp.java */
/* renamed from: com.eckom.xtlibrary.b.b.e */
/* loaded from: classes3.dex */
public class BinderC0561e extends InterfaceC0518f.a {

    /* renamed from: ed */
    private C0706a f462ed;

    public BinderC0561e(AbstractC0658a abstractC0658a) {
        if (abstractC0658a instanceof C0706a) {
            this.f462ed = (C0706a) abstractC0658a;
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0518f
    /* renamed from: J */
    public void mo163J() {
        C0706a c0706a = this.f462ed;
        if (c0706a != null) {
            InterfaceC0565d interfaceC0565d = c0706a.f856Hk;
            if (interfaceC0565d != null) {
                interfaceC0565d.m411J();
            } else {
                c0706a.m1064ic();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0518f
    /* renamed from: P */
    public void mo164P() {
        C0706a c0706a = this.f462ed;
        if (c0706a != null) {
            InterfaceC0565d interfaceC0565d = c0706a.f856Hk;
            if (interfaceC0565d != null) {
                interfaceC0565d.m412P();
            } else {
                c0706a.m1053P();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0518f
    /* renamed from: a */
    public void mo165a(Bundle bundle) {
        InterfaceC0565d interfaceC0565d;
        C0706a c0706a = this.f462ed;
        if (c0706a == null || (interfaceC0565d = c0706a.f856Hk) == null) {
            return;
        }
        interfaceC0565d.m413a(bundle);
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0518f
    /* renamed from: ha */
    public void mo166ha() {
        C0706a c0706a = this.f462ed;
        if (c0706a != null) {
            InterfaceC0565d interfaceC0565d = c0706a.f856Hk;
            if (interfaceC0565d != null) {
                interfaceC0565d.m414ha();
            } else {
                c0706a.m1065jc();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0518f
    /* renamed from: ma */
    public void mo167ma() {
        C0706a c0706a = this.f462ed;
        if (c0706a != null) {
            InterfaceC0565d interfaceC0565d = c0706a.f856Hk;
            if (interfaceC0565d != null) {
                interfaceC0565d.m415ma();
            } else {
                c0706a.m1067ma();
            }
        }
    }
}
