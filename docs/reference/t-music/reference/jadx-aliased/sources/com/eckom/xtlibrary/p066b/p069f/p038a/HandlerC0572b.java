package com.eckom.xtlibrary.p066b.p069f.p038a;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.ThreadPoolExecutor;

/* compiled from: ThreadPoolManager.java */
/* renamed from: com.eckom.xtlibrary.b.f.a.b */
/* loaded from: classes3.dex */
class HandlerC0572b extends Handler {
    final /* synthetic */ C0573c this$0;

    HandlerC0572b(C0573c c0573c) {
        this.this$0 = c0573c;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        String str;
        ThreadPoolExecutor m434Ja;
        Handler handler;
        super.handleMessage(message);
        if (message.what == 65281 && (m434Ja = this.this$0.m434Ja((str = (String) message.obj))) != null) {
            Log.d("ThreadPoolManager", "handleMessage: POOL_GET_TASK_COUNT " + str + "," + m434Ja.getQueue().size());
            if (m434Ja.getQueue().size() == 0) {
                this.this$0.m428Db("/mnt/sdcard");
                this.this$0.m429Eb("/storage/usb");
                this.this$0.m429Eb("/storage/extsd");
            } else {
                Message obtain = Message.obtain();
                obtain.what = 65281;
                obtain.obj = str;
                handler = this.this$0.mHandler;
                handler.sendMessageDelayed(obtain, 1000L);
            }
        }
    }
}
