package android.arch.lifecycle;

import android.arch.core.p005b.C0006a;
import android.arch.core.p005b.C0008c;
import android.arch.lifecycle.Lifecycle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* compiled from: LifecycleRegistry.java */
/* renamed from: android.arch.lifecycle.g */
/* loaded from: classes.dex */
public class C0018g extends Lifecycle {
    private final WeakReference<InterfaceC0016e> mLifecycleOwner;

    /* renamed from: Da */
    private C0006a<InterfaceC0015d, a> f52Da = new C0006a<>();

    /* renamed from: Ea */
    private int f53Ea = 0;

    /* renamed from: Fa */
    private boolean f54Fa = false;

    /* renamed from: Ga */
    private boolean f55Ga = false;

    /* renamed from: Ha */
    private ArrayList<Lifecycle.State> f56Ha = new ArrayList<>();
    private Lifecycle.State mState = Lifecycle.State.INITIALIZED;

    /* compiled from: LifecycleRegistry.java */
    /* renamed from: android.arch.lifecycle.g$a */
    static class a {

        /* renamed from: Ja */
        GenericLifecycleObserver f57Ja;
        Lifecycle.State mState;

        a(InterfaceC0015d interfaceC0015d, Lifecycle.State state) {
            this.f57Ja = C0020i.m63c(interfaceC0015d);
            this.mState = state;
        }

        /* renamed from: b */
        void m61b(InterfaceC0016e interfaceC0016e, Lifecycle.Event event) {
            Lifecycle.State m48a = C0018g.m48a(event);
            this.mState = C0018g.m49a(this.mState, m48a);
            this.f57Ja.mo21a(interfaceC0016e, event);
            this.mState = m48a;
        }
    }

    public C0018g(@NonNull InterfaceC0016e interfaceC0016e) {
        this.mLifecycleOwner = new WeakReference<>(interfaceC0016e);
    }

    /* renamed from: c */
    private void m52c(Lifecycle.State state) {
        if (this.mState == state) {
            return;
        }
        this.mState = state;
        if (this.f54Fa || this.f53Ea != 0) {
            this.f55Ga = true;
            return;
        }
        this.f54Fa = true;
        sync();
        this.f54Fa = false;
    }

    /* renamed from: d */
    private void m53d(Lifecycle.State state) {
        this.f56Ha.add(state);
    }

    /* renamed from: e */
    private static Lifecycle.Event m54e(Lifecycle.State state) {
        int i = C0017f.f51Ia[state.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return Lifecycle.Event.ON_START;
            }
            if (i == 3) {
                return Lifecycle.Event.ON_RESUME;
            }
            if (i == 4) {
                throw new IllegalArgumentException();
            }
            if (i != 5) {
                throw new IllegalArgumentException("Unexpected state value " + state);
            }
        }
        return Lifecycle.Event.ON_CREATE;
    }

    /* renamed from: h */
    private void m55h(InterfaceC0016e interfaceC0016e) {
        Iterator<Map.Entry<InterfaceC0015d, a>> descendingIterator = this.f52Da.descendingIterator();
        while (descendingIterator.hasNext() && !this.f55Ga) {
            Map.Entry<InterfaceC0015d, a> next = descendingIterator.next();
            a value = next.getValue();
            while (value.mState.compareTo(this.mState) > 0 && !this.f55Ga && this.f52Da.contains(next.getKey())) {
                Lifecycle.Event m50b = m50b(value.mState);
                m53d(m48a(m50b));
                value.m61b(interfaceC0016e, m50b);
                m58me();
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* renamed from: i */
    private void m56i(InterfaceC0016e interfaceC0016e) {
        C0008c<InterfaceC0015d, a>.d m16va = this.f52Da.m16va();
        while (m16va.hasNext() && !this.f55Ga) {
            Map.Entry next = m16va.next();
            a aVar = (a) next.getValue();
            while (aVar.mState.compareTo(this.mState) < 0 && !this.f55Ga && this.f52Da.contains(next.getKey())) {
                m53d(aVar.mState);
                aVar.m61b(interfaceC0016e, m54e(aVar.mState));
                m58me();
            }
        }
    }

    /* renamed from: le */
    private boolean m57le() {
        if (this.f52Da.size() == 0) {
            return true;
        }
        Lifecycle.State state = this.f52Da.eldest().getValue().mState;
        Lifecycle.State state2 = this.f52Da.m17wa().getValue().mState;
        return state == state2 && this.mState == state2;
    }

    /* renamed from: me */
    private void m58me() {
        this.f56Ha.remove(r1.size() - 1);
    }

    private void sync() {
        InterfaceC0016e interfaceC0016e = this.mLifecycleOwner.get();
        if (interfaceC0016e == null) {
            Log.w("LifecycleRegistry", "LifecycleOwner is garbage collected, you shouldn't try dispatch new events from it.");
            return;
        }
        while (!m57le()) {
            this.f55Ga = false;
            if (this.mState.compareTo(this.f52Da.eldest().getValue().mState) < 0) {
                m55h(interfaceC0016e);
            }
            Map.Entry<InterfaceC0015d, a> m17wa = this.f52Da.m17wa();
            if (!this.f55Ga && m17wa != null && this.mState.compareTo(m17wa.getValue().mState) > 0) {
                m56i(interfaceC0016e);
            }
        }
        this.f55Ga = false;
    }

    @MainThread
    /* renamed from: a */
    public void m59a(@NonNull Lifecycle.State state) {
        m52c(state);
    }

    /* renamed from: b */
    public void m60b(@NonNull Lifecycle.Event event) {
        m52c(m48a(event));
    }

    @Override // android.arch.lifecycle.Lifecycle
    @NonNull
    public Lifecycle.State getCurrentState() {
        return this.mState;
    }

    @Override // android.arch.lifecycle.Lifecycle
    /* renamed from: a */
    public void mo28a(@NonNull InterfaceC0015d interfaceC0015d) {
        InterfaceC0016e interfaceC0016e;
        Lifecycle.State state = this.mState;
        Lifecycle.State state2 = Lifecycle.State.DESTROYED;
        if (state != state2) {
            state2 = Lifecycle.State.INITIALIZED;
        }
        a aVar = new a(interfaceC0015d, state2);
        if (this.f52Da.putIfAbsent(interfaceC0015d, aVar) == null && (interfaceC0016e = this.mLifecycleOwner.get()) != null) {
            boolean z = this.f53Ea != 0 || this.f54Fa;
            Lifecycle.State m51c = m51c(interfaceC0015d);
            this.f53Ea++;
            while (aVar.mState.compareTo(m51c) < 0 && this.f52Da.contains(interfaceC0015d)) {
                m53d(aVar.mState);
                aVar.m61b(interfaceC0016e, m54e(aVar.mState));
                m58me();
                m51c = m51c(interfaceC0015d);
            }
            if (!z) {
                sync();
            }
            this.f53Ea--;
        }
    }

    @Override // android.arch.lifecycle.Lifecycle
    /* renamed from: b */
    public void mo29b(@NonNull InterfaceC0015d interfaceC0015d) {
        this.f52Da.remove(interfaceC0015d);
    }

    /* renamed from: b */
    private static Lifecycle.Event m50b(Lifecycle.State state) {
        int i = C0017f.f51Ia[state.ordinal()];
        if (i == 1) {
            throw new IllegalArgumentException();
        }
        if (i == 2) {
            return Lifecycle.Event.ON_DESTROY;
        }
        if (i == 3) {
            return Lifecycle.Event.ON_STOP;
        }
        if (i == 4) {
            return Lifecycle.Event.ON_PAUSE;
        }
        if (i != 5) {
            throw new IllegalArgumentException("Unexpected state value " + state);
        }
        throw new IllegalArgumentException();
    }

    /* renamed from: c */
    private Lifecycle.State m51c(InterfaceC0015d interfaceC0015d) {
        Map.Entry<InterfaceC0015d, a> m14b = this.f52Da.m14b(interfaceC0015d);
        Lifecycle.State state = null;
        Lifecycle.State state2 = m14b != null ? m14b.getValue().mState : null;
        if (!this.f56Ha.isEmpty()) {
            state = this.f56Ha.get(r0.size() - 1);
        }
        return m49a(m49a(this.mState, state2), state);
    }

    /* renamed from: a */
    static Lifecycle.State m48a(Lifecycle.Event event) {
        switch (C0017f.f50Ca[event.ordinal()]) {
            case 1:
            case 2:
                return Lifecycle.State.CREATED;
            case 3:
            case 4:
                return Lifecycle.State.STARTED;
            case 5:
                return Lifecycle.State.RESUMED;
            case 6:
                return Lifecycle.State.DESTROYED;
            default:
                throw new IllegalArgumentException("Unexpected event value " + event);
        }
    }

    /* renamed from: a */
    static Lifecycle.State m49a(@NonNull Lifecycle.State state, @Nullable Lifecycle.State state2) {
        return (state2 == null || state2.compareTo(state) >= 0) ? state : state2;
    }
}
