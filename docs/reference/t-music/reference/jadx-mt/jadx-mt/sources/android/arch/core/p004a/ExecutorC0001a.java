package android.arch.core.p004a;

import java.util.concurrent.Executor;

/* JADX INFO: renamed from: android.arch.core.a.a */
/* JADX INFO: compiled from: ArchTaskExecutor.java */
/* JADX INFO: loaded from: classes.dex */
class ExecutorC0001a implements Executor {
    ExecutorC0001a() {
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        C0003c.getInstance().mo12b(runnable);
    }
}
