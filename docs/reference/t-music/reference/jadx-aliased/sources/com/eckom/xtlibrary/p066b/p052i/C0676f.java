package com.eckom.xtlibrary.p066b.p052i;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import java.util.concurrent.CountDownLatch;

/* compiled from: RunUtil.java */
/* renamed from: com.eckom.xtlibrary.b.i.f */
/* loaded from: classes3.dex */
public class C0676f {
    private static Handler sHandler;

    /* compiled from: RunUtil.java */
    /* renamed from: com.eckom.xtlibrary.b.i.f$a */
    private static class a extends Handler {
        public a() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                Pair pair = (Pair) message.obj;
                ((Runnable) pair.first).run();
                Object obj = pair.second;
                if (obj != null) {
                    ((CountDownLatch) obj).countDown();
                }
            }
        }
    }

    /* renamed from: a */
    public static void m932a(Runnable runnable, boolean z) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        CountDownLatch countDownLatch = z ? new CountDownLatch(1) : null;
        getHandler().obtainMessage(1, new Pair(runnable, countDownLatch)).sendToTarget();
        if (z) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Handler getHandler() {
        Handler handler;
        synchronized (C0676f.class) {
            if (sHandler == null) {
                sHandler = new a();
            }
            handler = sHandler;
        }
        return handler;
    }

    public static void runOnUiThread(Runnable runnable) {
        m932a(runnable, false);
    }
}
