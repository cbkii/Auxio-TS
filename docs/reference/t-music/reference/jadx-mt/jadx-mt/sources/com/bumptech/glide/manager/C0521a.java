package com.bumptech.glide.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;
import p011c.p012a.p013a.p014a.C0512a;

/* JADX INFO: renamed from: com.bumptech.glide.manager.a */
/* JADX INFO: compiled from: ActivityFragmentLifecycle.java */
/* JADX INFO: loaded from: classes3.dex */
class C0521a implements InterfaceC0522b {

    /* JADX INFO: renamed from: Ef */
    private final Set<InterfaceC0523c> f341Ef = Collections.newSetFromMap(new WeakHashMap());

    /* JADX INFO: renamed from: Ff */
    private boolean f342Ff;

    /* JADX INFO: renamed from: Gf */
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
