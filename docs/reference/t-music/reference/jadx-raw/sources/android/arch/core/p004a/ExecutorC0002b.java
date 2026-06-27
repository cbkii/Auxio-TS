package android.arch.core.p004a;

import java.util.concurrent.Executor;

/* compiled from: ArchTaskExecutor.java */
/* renamed from: android.arch.core.a.b */
/* loaded from: classes.dex */
class ExecutorC0002b implements Executor {
    ExecutorC0002b() {
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        C0003c.getInstance().mo11a(runnable);
    }
}
