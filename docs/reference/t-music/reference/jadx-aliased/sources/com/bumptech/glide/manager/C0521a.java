package com.bumptech.glide.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import p060c.p061a.p062a.p014a.C0512a;

/* compiled from: ActivityFragmentLifecycle.java */
/* renamed from: com.bumptech.glide.manager.a */
/* loaded from: classes3.dex */
class C0521a implements InterfaceC0522b {

    /* renamed from: Ef */
    private final Set<InterfaceC0523c> f341Ef = Collections.newSetFromMap(new WeakHashMap());

    /* renamed from: Ff */
    private boolean f342Ff;

    /* renamed from: Gf */
    private boolean f343Gf;

    C0521a() {
    }

    void onDestroy() {
        this.f343Gf = true;
        Iterator it = C0512a.m116a(this.f341Ef).iterator();
        while (it.hasNext()) {
            ((InterfaceC0523c) it.next()).onDestroy();
        }
    }

    void onStart() {
        this.f342Ff = true;
        Iterator it = C0512a.m116a(this.f341Ef).iterator();
        while (it.hasNext()) {
            ((InterfaceC0523c) it.next()).onStart();
        }
    }

    void onStop() {
        this.f342Ff = false;
        Iterator it = C0512a.m116a(this.f341Ef).iterator();
        while (it.hasNext()) {
            ((InterfaceC0523c) it.next()).onStop();
        }
    }
}
