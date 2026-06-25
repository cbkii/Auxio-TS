package com.eckom.xtlibrary.p020b.p037f.p038a;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.util.concurrent.ThreadPoolExecutor;

/* JADX INFO: renamed from: com.eckom.xtlibrary.b.f.a.b */
/* JADX INFO: compiled from: ThreadPoolManager.java */
/* JADX INFO: loaded from: classes3.dex */
class HandlerC0572b extends Handler {
    final /* synthetic */ C0573c this$0;

    HandlerC0572b(C0573c c0573c) {
        this.this$0 = c0573c;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        String str;
        ThreadPoolExecutor threadPoolExecutorM434Ja;
        super.handleMessage(message);
        if (message.what == 65281 && (threadPoolExecutorM434Ja = this.this$0.m434Ja((str = (String) message.obj))) != null) {
            Log.d("ThreadPoolManager", "handleMessage: POOL_GET_TASK_COUNT " + str + "," + threadPoolExecutorM434Ja.getQueue().size());
            if (threadPoolExecutorM434Ja.getQueue().size() == 0) {
                this.this$0.m428Db("/mnt/sdcard");
                this.this$0.m429Eb("/storage/usb");
                this.this$0.m429Eb("/storage/extsd");
            } else {
                Message messageObtain = Message.obtain();
                messageObtain.what = 65281;
                messageObtain.obj = str;
                this.this$0.mHandler.sendMessageDelayed(messageObtain, 1000L);
            }
        }
    }
}
