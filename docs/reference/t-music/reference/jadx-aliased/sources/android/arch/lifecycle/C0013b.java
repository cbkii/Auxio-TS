package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;

/* compiled from: FullLifecycleObserverAdapter.java */
/* renamed from: android.arch.lifecycle.b */
/* loaded from: classes.dex */
/* synthetic */ class C0013b {

    /* renamed from: Ca */
    static final /* synthetic */ int[] f49Ca = new int[Lifecycle.Event.values().length];

    static {
        try {
            f49Ca[Lifecycle.Event.ON_CREATE.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            f49Ca[Lifecycle.Event.ON_START.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            f49Ca[Lifecycle.Event.ON_RESUME.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            f49Ca[Lifecycle.Event.ON_PAUSE.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            f49Ca[Lifecycle.Event.ON_STOP.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            f49Ca[Lifecycle.Event.ON_DESTROY.ordinal()] = 6;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            f49Ca[Lifecycle.Event.ON_ANY.ordinal()] = 7;
        } catch (NoSuchFieldError unused7) {
        }
    }
}
