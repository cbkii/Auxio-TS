package com.eckom.xtlibrary.twproject.service;

import android.os.Handler;
import android.os.Message;
import com.eckom.xtlibrary.p066b.C0556b;

/* compiled from: XTService.java */
/* renamed from: com.eckom.xtlibrary.twproject.service.a */
/* loaded from: classes3.dex */
class HandlerC0722a extends Handler {
    private int mCount = 0;
    final /* synthetic */ XTService this$0;

    HandlerC0722a(XTService xTService) {
        this.this$0 = xTService;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (message.what != 65281) {
            return;
        }
        C0556b.getInstant().init(this.this$0.getApplicationContext());
        C0556b.getInstant().m385a(this.this$0.mPresenter);
    }
}
