package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class SingleGeneratedAdapterObserver implements GenericLifecycleObserver {

    /* renamed from: Oa */
    private final InterfaceC0014c f45Oa;

    SingleGeneratedAdapterObserver(InterfaceC0014c interfaceC0014c) {
        this.f45Oa = interfaceC0014c;
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    /* renamed from: a */
    public void mo21a(InterfaceC0016e interfaceC0016e, Lifecycle.Event event) {
        this.f45Oa.m47a(interfaceC0016e, event, false, null);
        this.f45Oa.m47a(interfaceC0016e, event, true, null);
    }
}
