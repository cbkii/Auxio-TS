package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.support.annotation.RestrictTo;

/* JADX INFO: loaded from: classes.dex */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class CompositeGeneratedAdaptersObserver implements GenericLifecycleObserver {

    /* JADX INFO: renamed from: Ba */
    private final InterfaceC0014c[] f34Ba;

    CompositeGeneratedAdaptersObserver(InterfaceC0014c[] interfaceC0014cArr) {
        this.f34Ba = interfaceC0014cArr;
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    /* JADX INFO: renamed from: a */
    public void mo21a(InterfaceC0016e interfaceC0016e, Lifecycle.Event event) {
        C0022k c0022k = new C0022k();
        for (InterfaceC0014c interfaceC0014c : this.f34Ba) {
            interfaceC0014c.m47a(interfaceC0016e, event, false, c0022k);
        }
        for (InterfaceC0014c interfaceC0014c2 : this.f34Ba) {
            interfaceC0014c2.m47a(interfaceC0016e, event, true, c0022k);
        }
    }
}
