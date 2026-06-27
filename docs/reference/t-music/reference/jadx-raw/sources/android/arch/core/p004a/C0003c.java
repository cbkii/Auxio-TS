package android.arch.core.p004a;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.concurrent.Executor;

/* compiled from: ArchTaskExecutor.java */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: android.arch.core.a.c */
/* loaded from: classes.dex */
public class C0003c extends AbstractC0005e {

    /* renamed from: oa */
    @NonNull
    private static final Executor f24oa = new ExecutorC0001a();

    /* renamed from: pa */
    @NonNull
    private static final Executor f25pa = new ExecutorC0002b();
    private static volatile C0003c sInstance;

    /* renamed from: na */
    @NonNull
    private AbstractC0005e f26na = new C0004d();

    @NonNull
    private AbstractC0005e mDelegate = this.f26na;

    private C0003c() {
    }

    @NonNull
    public static C0003c getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (C0003c.class) {
            if (sInstance == null) {
                sInstance = new C0003c();
            }
        }
        return sInstance;
    }

    @Override // android.arch.core.p004a.AbstractC0005e
    /* renamed from: a */
    public void mo11a(Runnable runnable) {
        this.mDelegate.mo11a(runnable);
    }

    @Override // android.arch.core.p004a.AbstractC0005e
    /* renamed from: b */
    public void mo12b(Runnable runnable) {
        this.mDelegate.mo12b(runnable);
    }

    @Override // android.arch.core.p004a.AbstractC0005e
    /* renamed from: ua */
    public boolean mo13ua() {
        return this.mDelegate.mo13ua();
    }
}
