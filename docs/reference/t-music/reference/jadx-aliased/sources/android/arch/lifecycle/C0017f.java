package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;

/* compiled from: LifecycleRegistry.java */
/* renamed from: android.arch.lifecycle.f */
/* loaded from: classes.dex */
/* synthetic */ class C0017f {

    /* renamed from: Ca */
    static final /* synthetic */ int[] f50Ca;

    /* renamed from: Ia */
    static final /* synthetic */ int[] f51Ia = new int[Lifecycle.State.values().length];

    static {
        try {
            f51Ia[Lifecycle.State.INITIALIZED.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            f51Ia[Lifecycle.State.CREATED.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            f51Ia[Lifecycle.State.STARTED.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            f51Ia[Lifecycle.State.RESUMED.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            f51Ia[Lifecycle.State.DESTROYED.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
        f50Ca = new int[Lifecycle.Event.values().length];
        try {
            f50Ca[Lifecycle.Event.ON_CREATE.ordinal()] = 1;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            f50Ca[Lifecycle.Event.ON_STOP.ordinal()] = 2;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            f50Ca[Lifecycle.Event.ON_START.ordinal()] = 3;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            f50Ca[Lifecycle.Event.ON_PAUSE.ordinal()] = 4;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            f50Ca[Lifecycle.Event.ON_RESUME.ordinal()] = 5;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            f50Ca[Lifecycle.Event.ON_DESTROY.ordinal()] = 6;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            f50Ca[Lifecycle.Event.ON_ANY.ordinal()] = 7;
        } catch (NoSuchFieldError unused12) {
        }
    }
}
