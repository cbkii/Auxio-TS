package com.eckom.xtlibrary.p020b.p021a.p025d;

import android.os.Handler;
import android.os.Message;
import android.tw.john.TWUtil;
import android.util.Log;

/* compiled from: BuildInBTModel.java */
/* renamed from: com.eckom.xtlibrary.b.a.d.i */
/* loaded from: classes3.dex */
class C0547i implements Handler.Callback {
    final /* synthetic */ C0548j this$0;

    C0547i(C0548j c0548j) {
        this.this$0 = c0548j;
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        try {
            if (message.obj instanceof TWUtil.TWObject) {
                TWUtil.TWObject tWObject = (TWUtil.TWObject) message.obj;
            } else if (message.obj instanceof byte[]) {
                new String((byte[]) message.obj);
            }
            return true;
        } catch (Exception e) {
            Log.e("BuildInBTModel", "handleMessage: msg.what:" + message.what + " Error:" + e.getMessage());
            return true;
        }
    }
}
