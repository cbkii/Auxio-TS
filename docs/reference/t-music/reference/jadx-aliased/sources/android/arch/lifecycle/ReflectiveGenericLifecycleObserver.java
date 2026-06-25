package android.arch.lifecycle;

import android.arch.lifecycle.C0012a;
import android.arch.lifecycle.Lifecycle;

/* loaded from: classes.dex */
class ReflectiveGenericLifecycleObserver implements GenericLifecycleObserver {
    private final C0012a.a mInfo;
    private final Object mWrapped;

    ReflectiveGenericLifecycleObserver(Object obj) {
        this.mWrapped = obj;
        this.mInfo = C0012a.sInstance.m42a(this.mWrapped.getClass());
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    /* renamed from: a */
    public void mo21a(InterfaceC0016e interfaceC0016e, Lifecycle.Event event) {
        this.mInfo.m45a(interfaceC0016e, event, this.mWrapped);
    }
}
