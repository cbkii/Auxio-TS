package com.eckom.xtlibrary.p020b.p030b;

import android.os.Bundle;
import com.eckom.xtlibrary.p020b.p031c.InterfaceC0564c;
import com.eckom.xtlibrary.p020b.p045g.AbstractC0658a;
import com.eckom.xtlibrary.p020b.p046h.p049c.C0667a;
import p011c.p015b.p016a.p017a.p018a.InterfaceC0515c;
import p011c.p015b.p016a.p017a.p018a.InterfaceC0516d;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.b.d */
/* JADX INFO: compiled from: RadioCallBackImp.java */
/* JADX INFO: loaded from: classes3.dex */
public class BinderC0560d extends InterfaceC0515c.a {

    /* JADX INFO: renamed from: gd */
    private final InterfaceC0516d f460gd;

    /* JADX INFO: renamed from: hd */
    private C0667a f461hd;

    public BinderC0560d(AbstractC0658a abstractC0658a, InterfaceC0516d interfaceC0516d) {
        if (abstractC0658a instanceof C0667a) {
            this.f461hd = (C0667a) abstractC0658a;
        }
        this.f460gd = interfaceC0516d;
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: K */
    public void mo133K() {
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m403K();
            } else {
                c0667a.m891hc();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: R */
    public void mo134R() {
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m404R();
            } else {
                c0667a.m888dc();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: U */
    public void mo135U() {
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m405U();
            } else {
                c0667a.m889ec();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: a */
    public void mo136a(Bundle bundle) {
        String string = bundle.getString("action");
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m406a(bundle);
                return;
            }
            byte b2 = -1;
            int iHashCode = string.hashCode();
            if (iHashCode != 1907733728) {
                if (iHashCode == 2090787472 && string.equals("nextChannel")) {
                    b2 = 0;
                }
            } else if (string.equals("preChannel")) {
                b2 = 1;
            }
            if (b2 == 0) {
                this.f461hd.next();
            } else {
                if (b2 != 1) {
                    return;
                }
                this.f461hd.m886ac();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: aa */
    public void mo137aa(String str) {
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m407aa(str);
                return;
            }
            try {
                c0667a.m893na(Integer.parseInt(str));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: ka */
    public void mo138ka() {
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m408ka();
            } else {
                c0667a.m887cc();
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: o */
    public void mo139o(int i) {
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m409o(i);
            } else {
                c0667a.m892ma(i);
            }
        }
    }

    @Override // p011c.p015b.p016a.p017a.p018a.InterfaceC0515c
    /* JADX INFO: renamed from: oa */
    public void mo140oa() {
        C0667a c0667a = this.f461hd;
        if (c0667a != null) {
            InterfaceC0564c interfaceC0564c = c0667a.f789Hk;
            if (interfaceC0564c != null) {
                interfaceC0564c.m410oa();
            } else {
                c0667a.m890fc();
            }
        }
    }
}
