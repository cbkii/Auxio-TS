package android.arch.lifecycle;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

/* compiled from: ViewModelProvider.java */
/* renamed from: android.arch.lifecycle.p */
/* loaded from: classes.dex */
public class C0027p {
    private final a mFactory;
    private final C0028q mViewModelStore;

    /* compiled from: ViewModelProvider.java */
    /* renamed from: android.arch.lifecycle.p$a */
    public interface a {
        @NonNull
        <T extends AbstractC0026o> T create(@NonNull Class<T> cls);
    }

    public C0027p(@NonNull C0028q c0028q, @NonNull a aVar) {
        this.mFactory = aVar;
        this.mViewModelStore = c0028q;
    }

    @NonNull
    @MainThread
    /* renamed from: a */
    public <T extends AbstractC0026o> T m69a(@NonNull String str, @NonNull Class<T> cls) {
        T t = (T) this.mViewModelStore.get(str);
        if (cls.isInstance(t)) {
            return t;
        }
        T t2 = (T) this.mFactory.create(cls);
        this.mViewModelStore.m71a(str, t2);
        return t2;
    }

    @NonNull
    @MainThread
    /* renamed from: c */
    public <T extends AbstractC0026o> T m70c(@NonNull Class<T> cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return (T) m69a("android.arch.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, cls);
    }
}
