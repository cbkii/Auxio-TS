package com.eckom.xtlibrary.p066b.p030b;

import android.os.Bundle;
import com.eckom.xtlibrary.p066b.p031c.InterfaceC0563b;
import com.eckom.xtlibrary.p066b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p066b.p069f.p042e.C0635a;
import p060c.p063b.p064a.p065a.p018a.InterfaceC0514b;

/* compiled from: MusicCallBackImp.java */
/* renamed from: com.eckom.xtlibrary.b.b.c */
/* loaded from: classes3.dex */
public class BinderC0559c extends InterfaceC0514b.a {

    /* renamed from: ed */
    private C0635a f459ed;

    public BinderC0559c(AbstractC0658a abstractC0658a) {
        if (abstractC0658a instanceof C0635a) {
            this.f459ed = (C0635a) abstractC0658a;
        }
    }

    @Override // p060c.p063b.p064a.p065a.p018a.InterfaceC0514b
    /* renamed from: a */
    public void mo127a(Bundle bundle) {
        InterfaceC0563b interfaceC0563b;
        C0635a c0635a = this.f459ed;
        if (c0635a == null || (interfaceC0563b = c0635a.f671Hk) == null) {
            return;
        }
        interfaceC0563b.m397a(bundle);
    }

    @Override // p060c.p063b.p064a.p065a.p018a.InterfaceC0514b
    /* renamed from: ba */
    public void mo128ba() {
        C0635a c0635a = this.f459ed;
        if (c0635a != null) {
            InterfaceC0563b interfaceC0563b = c0635a.f671Hk;
            if (interfaceC0563b != null) {
                interfaceC0563b.m398ba();
            } else {
                c0635a.m726ba();
            }
        }
    }

    @Override // p060c.p063b.p064a.p065a.p018a.InterfaceC0514b
    /* renamed from: ea */
    public void mo129ea() {
        C0635a c0635a = this.f459ed;
        if (c0635a != null) {
            InterfaceC0563b interfaceC0563b = c0635a.f671Hk;
            if (interfaceC0563b != null) {
                interfaceC0563b.m399ea();
            } else {
                c0635a.m736rb();
            }
        }
    }

    @Override // p060c.p063b.p064a.p065a.p018a.InterfaceC0514b
    /* renamed from: fa */
    public void mo130fa() {
        C0635a c0635a = this.f459ed;
        if (c0635a != null) {
            InterfaceC0563b interfaceC0563b = c0635a.f671Hk;
            if (interfaceC0563b != null) {
                interfaceC0563b.m400fa();
            } else {
                c0635a.m730fa();
            }
        }
    }

    @Override // p060c.p063b.p064a.p065a.p018a.InterfaceC0514b
    /* renamed from: na */
    public void mo131na() {
        C0635a c0635a = this.f459ed;
        if (c0635a != null) {
            InterfaceC0563b interfaceC0563b = c0635a.f671Hk;
            if (interfaceC0563b != null) {
                interfaceC0563b.m401na();
            } else {
                c0635a.m734pb();
            }
        }
    }

    @Override // p060c.p063b.p064a.p065a.p018a.InterfaceC0514b
    /* renamed from: z */
    public void mo132z(int i) {
        C0635a c0635a = this.f459ed;
        if (c0635a != null) {
            InterfaceC0563b interfaceC0563b = c0635a.f671Hk;
            if (interfaceC0563b != null) {
                interfaceC0563b.m402z(i);
            } else {
                c0635a.m733pa(i);
            }
        }
    }
}
