package android.arch.lifecycle;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/* loaded from: classes.dex */
public abstract class Lifecycle {

    public enum Event {
        ON_CREATE,
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        ON_STOP,
        ON_DESTROY,
        ON_ANY
    }

    public enum State {
        DESTROYED,
        INITIALIZED,
        CREATED,
        STARTED,
        RESUMED;

        public boolean isAtLeast(@NonNull State state) {
            return compareTo(state) >= 0;
        }
    }

    @MainThread
    /* renamed from: a */
    public abstract void mo28a(@NonNull InterfaceC0015d interfaceC0015d);

    @MainThread
    /* renamed from: b */
    public abstract void mo29b(@NonNull InterfaceC0015d interfaceC0015d);

    @NonNull
    @MainThread
    public abstract State getCurrentState();
}
