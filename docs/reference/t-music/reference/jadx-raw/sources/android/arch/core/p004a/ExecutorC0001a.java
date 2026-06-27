package android.arch.core.p004a;

import java.util.concurrent.Executor;

/* compiled from: ArchTaskExecutor.java */
/* renamed from: android.arch.core.a.a */
/* loaded from: classes.dex */
class ExecutorC0001a implements Executor {
    ExecutorC0001a() {
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        C0003c.getInstance().mo12b(runnable);
    }
}
