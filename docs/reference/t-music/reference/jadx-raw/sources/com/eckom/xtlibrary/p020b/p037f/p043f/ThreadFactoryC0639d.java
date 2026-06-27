package com.eckom.xtlibrary.p020b.p037f.p043f;

import java.util.concurrent.ThreadFactory;

/* compiled from: MusicUtils.java */
/* renamed from: com.eckom.xtlibrary.b.f.f.d */
/* loaded from: classes3.dex */
class ThreadFactoryC0639d implements ThreadFactory {
    ThreadFactoryC0639d() {
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, "threadPool" + runnable.hashCode());
    }
}
