package android.arch.lifecycle;

import android.arch.core.p004a.C0003c;
import android.arch.core.p005b.C0008c;
import android.arch.lifecycle.Lifecycle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class LiveData<T> {
    private static final Object NOT_SET = new Object();
    static final int START_VERSION = -1;
    private volatile Object mData;
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private volatile Object mPendingData;
    private final Runnable mPostValueRunnable;
    private int mVersion;
    private final Object mDataLock = new Object();
    private C0008c<InterfaceC0024m<? super T>, LiveData<T>.AbstractC0010b> mObservers = new C0008c<>();
    private int mActiveCount = 0;

    class LifecycleBoundObserver extends LiveData<T>.AbstractC0010b implements GenericLifecycleObserver {

        @NonNull
        final InterfaceC0016e mOwner;

        LifecycleBoundObserver(@NonNull InterfaceC0016e interfaceC0016e, InterfaceC0024m<? super T> interfaceC0024m) {
            super(interfaceC0024m);
            this.mOwner = interfaceC0016e;
        }

        @Override // android.arch.lifecycle.GenericLifecycleObserver
        /* JADX INFO: renamed from: a */
        public void mo21a(InterfaceC0016e interfaceC0016e, Lifecycle.Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                LiveData.this.removeObserver(this.mObserver);
            } else {
                m33y(mo32ya());
            }
        }

        @Override // android.arch.lifecycle.LiveData.AbstractC0010b
        /* JADX INFO: renamed from: g */
        boolean mo30g(InterfaceC0016e interfaceC0016e) {
            return this.mOwner == interfaceC0016e;
        }

        @Override // android.arch.lifecycle.LiveData.AbstractC0010b
        /* JADX INFO: renamed from: xa */
        void mo31xa() {
            this.mOwner.getLifecycle().mo29b(this);
        }

        @Override // android.arch.lifecycle.LiveData.AbstractC0010b
        /* JADX INFO: renamed from: ya */
        boolean mo32ya() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        }
    }

    /* JADX INFO: renamed from: android.arch.lifecycle.LiveData$a */
    private class C0009a extends LiveData<T>.AbstractC0010b {
        C0009a(InterfaceC0024m<? super T> interfaceC0024m) {
            super(interfaceC0024m);
        }

        @Override // android.arch.lifecycle.LiveData.AbstractC0010b
        /* JADX INFO: renamed from: ya */
        boolean mo32ya() {
            return true;
        }
    }

    /* JADX INFO: renamed from: android.arch.lifecycle.LiveData$b */
    private abstract class AbstractC0010b {

        /* JADX INFO: renamed from: Ma */
        int f35Ma = -1;
        boolean mActive;
        final InterfaceC0024m<? super T> mObserver;

        AbstractC0010b(InterfaceC0024m<? super T> interfaceC0024m) {
            this.mObserver = interfaceC0024m;
        }

        /* JADX INFO: renamed from: g */
        boolean mo30g(InterfaceC0016e interfaceC0016e) {
            return false;
        }

        /* JADX INFO: renamed from: xa */
        void mo31xa() {
        }

        /* JADX INFO: renamed from: y */
        void m33y(boolean z) {
            if (z == this.mActive) {
                return;
            }
            this.mActive = z;
            boolean z2 = LiveData.this.mActiveCount == 0;
            LiveData.this.mActiveCount += this.mActive ? 1 : -1;
            if (z2 && this.mActive) {
                LiveData.this.onActive();
            }
            if (LiveData.this.mActiveCount == 0 && !this.mActive) {
                LiveData.this.onInactive();
            }
            if (this.mActive) {
                LiveData.this.dispatchingValue(this);
            }
        }

        /* JADX INFO: renamed from: ya */
        abstract boolean mo32ya();
    }

    public LiveData() {
        Object obj = NOT_SET;
        this.mData = obj;
        this.mPendingData = obj;
        this.mVersion = -1;
        this.mPostValueRunnable = new RunnableC0021j(this);
    }

    private static void assertMainThread(String str) {
        if (C0003c.getInstance().mo13ua()) {
            return;
        }
        throw new IllegalStateException("Cannot invoke " + str + " on a background thread");
    }

    private void considerNotify(LiveData<T>.AbstractC0010b abstractC0010b) {
        if (abstractC0010b.mActive) {
            if (!abstractC0010b.mo32ya()) {
                abstractC0010b.m33y(false);
                return;
            }
            int i = abstractC0010b.f35Ma;
            int i2 = this.mVersion;
            if (i >= i2) {
                return;
            }
            abstractC0010b.f35Ma = i2;
            abstractC0010b.mObserver.onChanged((Object) this.mData);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchingValue(@Nullable LiveData<T>.AbstractC0010b abstractC0010b) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        do {
            this.mDispatchInvalidated = false;
            if (abstractC0010b != null) {
                considerNotify(abstractC0010b);
                abstractC0010b = null;
            } else {
                C0008c<InterfaceC0024m<? super T>, LiveData<T>.AbstractC0010b>.d dVarM16va = this.mObservers.m16va();
                while (dVarM16va.hasNext()) {
                    considerNotify((AbstractC0010b) dVarM16va.next().getValue());
                    if (this.mDispatchInvalidated) {
                        break;
                    }
                }
            }
        } while (this.mDispatchInvalidated);
        this.mDispatchingValue = false;
    }

    @Nullable
    public T getValue() {
        T t = (T) this.mData;
        if (t != NOT_SET) {
            return t;
        }
        return null;
    }

    int getVersion() {
        return this.mVersion;
    }

    public boolean hasActiveObservers() {
        return this.mActiveCount > 0;
    }

    public boolean hasObservers() {
        return this.mObservers.size() > 0;
    }

    @MainThread
    public void observe(@NonNull InterfaceC0016e interfaceC0016e, @NonNull InterfaceC0024m<? super T> interfaceC0024m) {
        assertMainThread("observe");
        if (interfaceC0016e.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(interfaceC0016e, interfaceC0024m);
        LiveData<T>.AbstractC0010b abstractC0010bPutIfAbsent = this.mObservers.putIfAbsent(interfaceC0024m, lifecycleBoundObserver);
        if (abstractC0010bPutIfAbsent != null && !abstractC0010bPutIfAbsent.mo30g(interfaceC0016e)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (abstractC0010bPutIfAbsent != null) {
            return;
        }
        interfaceC0016e.getLifecycle().mo28a(lifecycleBoundObserver);
    }

    @MainThread
    public void observeForever(@NonNull InterfaceC0024m<? super T> interfaceC0024m) {
        assertMainThread("observeForever");
        C0009a c0009a = new C0009a(interfaceC0024m);
        LiveData<T>.AbstractC0010b abstractC0010bPutIfAbsent = this.mObservers.putIfAbsent(interfaceC0024m, c0009a);
        if (abstractC0010bPutIfAbsent != null && (abstractC0010bPutIfAbsent instanceof LifecycleBoundObserver)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (abstractC0010bPutIfAbsent != null) {
            return;
        }
        c0009a.m33y(true);
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    protected void postValue(T t) {
        boolean z;
        synchronized (this.mDataLock) {
            z = this.mPendingData == NOT_SET;
            this.mPendingData = t;
        }
        if (z) {
            C0003c.getInstance().mo12b(this.mPostValueRunnable);
        }
    }

    @MainThread
    public void removeObserver(@NonNull InterfaceC0024m<? super T> interfaceC0024m) {
        assertMainThread("removeObserver");
        LiveData<T>.AbstractC0010b abstractC0010bRemove = this.mObservers.remove(interfaceC0024m);
        if (abstractC0010bRemove == null) {
            return;
        }
        abstractC0010bRemove.mo31xa();
        abstractC0010bRemove.m33y(false);
    }

    @MainThread
    public void removeObservers(@NonNull InterfaceC0016e interfaceC0016e) {
        assertMainThread("removeObservers");
        for (Map.Entry<InterfaceC0024m<? super T>, LiveData<T>.AbstractC0010b> entry : this.mObservers) {
            if (entry.getValue().mo30g(interfaceC0016e)) {
                removeObserver(entry.getKey());
            }
        }
    }

    @MainThread
    protected void setValue(T t) {
        assertMainThread("setValue");
        this.mVersion++;
        this.mData = t;
        dispatchingValue(null);
    }
}
