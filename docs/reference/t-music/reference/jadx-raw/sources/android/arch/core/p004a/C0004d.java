package android.arch.core.p004a;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* compiled from: DefaultTaskExecutor.java */
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* renamed from: android.arch.core.a.d */
/* loaded from: classes.dex */
public class C0004d extends AbstractC0005e {
    private final Object mLock = new Object();

    /* renamed from: qa */
    private ExecutorService f27qa = Executors.newFixedThreadPool(2);

    /* renamed from: ra */
    @Nullable
    private volatile Handler f28ra;

    @Override // android.arch.core.p004a.AbstractC0005e
    /* renamed from: a */
    public void mo11a(Runnable runnable) {
        this.f27qa.execute(runnable);
    }

    @Override // android.arch.core.p004a.AbstractC0005e
    /* renamed from: b */
    public void mo12b(Runnable runnable) {
        if (this.f28ra == null) {
            synchronized (this.mLock) {
                if (this.f28ra == null) {
                    this.f28ra = new Handler(Looper.getMainLooper());
                }
            }
        }
        this.f28ra.post(runnable);
    }

    @Override // android.arch.core.p004a.AbstractC0005e
    /* renamed from: ua */
    public boolean mo13ua() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
