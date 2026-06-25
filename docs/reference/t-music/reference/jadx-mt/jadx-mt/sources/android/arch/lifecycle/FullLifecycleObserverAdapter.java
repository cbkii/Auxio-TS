package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;

/* JADX INFO: loaded from: classes.dex */
class FullLifecycleObserverAdapter implements GenericLifecycleObserver {
    private final FullLifecycleObserver mObserver;

    FullLifecycleObserverAdapter(FullLifecycleObserver fullLifecycleObserver) {
        this.mObserver = fullLifecycleObserver;
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    /* JADX INFO: renamed from: a */
    public void mo21a(InterfaceC0016e interfaceC0016e, Lifecycle.Event event) {
        switch (C0013b.f49Ca[event.ordinal()]) {
            case 1:
                this.mObserver.m23b(interfaceC0016e);
                return;
            case 2:
                this.mObserver.m25d(interfaceC0016e);
                return;
            case 3:
                this.mObserver.m27f(interfaceC0016e);
                return;
            case 4:
                this.mObserver.m26e(interfaceC0016e);
                return;
            case 5:
                this.mObserver.m24c(interfaceC0016e);
                return;
            case 6:
                this.mObserver.m22a(interfaceC0016e);
                return;
            case 7:
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
            default:
                return;
        }
    }
}
